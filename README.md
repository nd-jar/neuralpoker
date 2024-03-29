# Распознавание карт
## Домашнее задание для собеседования
```text
## Техническое задание
- Необходимо написать программу на Java, которая распознает, какие карты лежат на столе (только по центру картинки)

- Тестирование программы будет осуществляться на аналогичных картинках, которых нет в исходном множестве
- Допускаются ошибки в распознавании не более 3% от общего количества распознанных карт

- Нельзя использовать готовые библиотеки для распознавания текста. Необходимо написать свой алгоритм распознавания карт
- На распознавание одного файла не должно уходить более 1 секунды

- Исходный код решения задачи не должен быть длиннее 500 строк с нормальным форматированием

- Программу нужно предоставить в виде, готовом к запуску на Windows десктопе. Файл run.bat **параметром** принимает путь до папки с картинками. В консоль
  распечатывается результат в виде "имя файла - карты" для всех файлов папки

- Пример вывода: QcJd5h.png - QcJd5h
- Программу нужно предоставить с исходными файлами
- В исходных файлах должен быть ВЕСЬ код, который был использован для решения задачи

- Программу нужно предоставить в виде ссылки на zip-файл. Ссылки на репозитории, например на github, не принимаются
## Рекомендации

- У автора этой задачи решение заняло 100 строк кода. У лучшего на данный момент кандидата - 160 строк. Ничего страшного, если заше решение занимает 500
строк. Однако, если больше и это - не комментарии, то стоит задуматься

Для решения задачи рекомендуется использовать следующие функции, встроенные в уауа
- BufferedImage img = ImageIO.read(f); - зачитка картинки из файла

- ImageIO.write(img, "png", f); - запись картинки в файл

- img.getWidth(); img.getHeight();; - рамеры картинки

- BufferedImage img1 = img.getSubimage(x, y, w, h); - взятие области в картинке

- img.getRGB(x, y); - взятие цвета точки по координате

- Color c = new Color(img.getRGB(x,y)); c.getRed(); c.getGreen(); c.getBlue(); c.equals(c1); - работа с цветом точки
``` 
## Решение
Задача разбита на 3 части:
1. prepare - подготовка данных.
2. learn - код для обучения модели.
3. card-recognizer - распознавание карт.
### 1. Prepare
Приложение берёт картинки из папки [./resources/imgs_marked](resources%2Fimgs_marked) и превращает их в 2 файла:
- [./resources/learning/data_nums.csv](resources%2Flearning%2Fdata_nums.csv)
- [./resources/learning/data_suits.csv](resources%2Flearning%2Fdata_suits.csv)

Csv-файлы являются битовым изображением цифр (data_nums.csv) и мастей (data_suits.csv). 

Одна строчка - одна картинка

Пример (используя вспомогательный метод [BitSetUtils#toAscii](prepare%2Fsrc%2Fmain%2Fjava%2Fnd%2Fjar%2Fneuralpoker%2Fprepare%2FBitSetUtils.java))

```text
....................................................................
....................................................................
........@@..........................................................
.......@@@@.........@@@@..@@@@@.........@@@@@..............@@.......
......@@@@@@.......@@@@@@@@@@@@@......@@@@@@@@............@@@@......
....@@@@@@@@@.....@@@@@@@@@@@@@@@....@@@@@@@@@@..........@@@@@......
...@@@@@@@@@@@...@@@@@@@@@@@@@@@@....@@@.....@@@.........@@@@@......
..@@@@@@@@@@@@@..@@@@@@@@@@@@@@@@...@@@......@@@........@@@@@@......
..@@@@@@@@@@@@@@..@@@@@@@@@@@@@@@...@@@......@@@@......@@@.@@@......
.@@@@@@@@@@@@@@@..@@@@@@@@@@@@@@@...@@@....@.@@@@.....@@@..@@@......
.@@@@@@@@@@@@@@@...@@@@@@@@@@@@@....@@@...@@@@@@.....@@@@@@@@@@.....
.@@@@@@@@@@@@@@@...@@@@@@@@@@@@......@@@...@@@@@.....@@@@@@@@@@.....
..@@@@@@@.@@@@@.....@@@@@@@@@@.......@@@@@@@@@@@...........@@@......
...@@@@.@..@@@........@@@@@@@.........@@@@@@@@@@...........@@@......
........@..............@@@@@...........@@@@@@.@@...........@@@......
.......@@@..............@@@.........................................
.........................@..........................................
....................................................................
```
### 2. Learn

Для обучения созданы две нейронные сети c 128 нейронами на скрытом слое.
Каждая нейронная сеть принимает на вход csv с шага 1, обучается на ней и на выходе отдаёт 4 файла:
- [num/b1.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fnum%2Fb1.txt), [suit/b1.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fsuit%2Fb1.txt) - bias скрытого слоя
- [num/b2.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fnum%2Fb2.txt), [suit/b2.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fsuit%2Fb2.txt) - bias output слоя
- [num/W1.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fnum%2FW1.txt), [suit/W1.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fsuit%2FW1.txt) - веса скрытого слоя
- [num/W2.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fnum%2FW2.txt), [suit/W2.txt](card-recognizer%2Fsrc%2Fmain%2Fresources%2Fsuit%2FW2.txt) - веса output слоя

### 3. Recognize

1. **Считываем веса и смещения из шага 2:**
    - Веса (W1, W2) и смещения (b1, b2) загружаются из сохраненных файлов.

2. **Преобразуем картинки в битовые массивы:**
    - Для каждой карты изображения используется код, который преобразует изображение в битовый массив.

3. **Магия:**
   - Вычисляем $a1 = \text{relu}(x \cdot W1 + b1)$, где $\text{relu}(x) = \max(0, x)$.
   - Вычисляем $a2 = \text{sigmoid}(a1 \cdot W2 + b2)$, где $\text{sigmoid}(x) = \frac{1}{1 + e^{-x}}$.

    Где 
   - $x = - битовый массив c изображением цифры или масти 
   - $W1$, $W2$ - матрицы весов
   - $b1$, $b2$ - векторы смещений
   - $relu$, $sigmoid$ - функции активации: 
     - $\text{relu}(x) = \max(0, x)$.
     - $\text{sigmoid}(x) = \frac{1}{1 + e^{-x}}$.
 
