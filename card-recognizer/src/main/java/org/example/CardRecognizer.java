package org.example;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

import static nd.jar.neuralpoker.common.Common.*;

public class CardRecognizer {
    private static final String[] numClasses = {"10", "2", "3", "4", "5", "6", "7", "8", "9", "A", "J", "K", "Q"};
    private static final char[] suitClasses = {'E', 'c', 'd', 'h', 's'}; // E - means empty

    public static void main(String[] args) throws IOException {
        final var folder = args[0];
        final var numPredictor = new DeepLearningModel("num", 221, 128, 13);
        final var suitPredictor = new DeepLearningModel("suit", 306, 128, 5);
        for (File file : Objects.requireNonNull(new File(folder).listFiles())) {
            final var image = ImageIO.read(file);
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < topLeftCornerX.length; i++) {
                final var s = suitClasses[suitPredictor.predict(getNthSizedBitSet(image, i, suitCoordinates, suitSize).set())];
                if (s == 'E') break; // Next cards are empty
                result.append(numClasses[numPredictor.predict(getNthSizedBitSet(image, i, numCoordinates, numSize).set())]).append(s);
            }
            System.out.println(file.getName() + " " + result);
        }
    }
}

class DeepLearningModel {
    private final double[][] w1;
    private final double[][] w2;
    private final double[] b1;
    private final double[] b2;
    DeepLearningModel(String folderWithModel, int input, int hidden, int output){
        w1 = ReadUtils.readWeights(folderWithModel+"/W1.txt", input, hidden);
        w2 = ReadUtils.readWeights(folderWithModel+"/W2.txt", hidden, output);
        b1 = ReadUtils.readWeights(folderWithModel+"/b1.txt", 1, hidden)[0];
        b2 = ReadUtils.readWeights(folderWithModel+"/b2.txt", 1, output)[0];
    }
    public int predict(double[] x) {
        final var a1 = relu(plus(multiply(x, w1), b1));
        final var a2 = sigmoid(plus(multiply(a1, w2), b2));
        int maxIndex = 0; double maxA = a2[maxIndex];
        for (int i = 1; i < a2.length; i++) {
            if (maxA < a2[i]) {
                maxIndex = i; maxA = a2[i];
            }
        }
        return maxIndex;
    }

    private static double[] plus(double[] arr1, double[] arr2) { // modifying 1st param to save space and code
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = arr1[i] + arr2[i];
        }
        return arr1;
    }

    private static double[] multiply(double[] vector, double[][] matrix) {
        double[] result = new double[matrix[0].length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[j] += vector[i] * matrix[i][j];
            }
        }
        return result;
    }

    private static double[] relu(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = Math.max(0, vector[i]); // modifying 1st param to save space and code
        }
        return vector;
    }

    private static double[] sigmoid(double[] vector) {
        for (int i = 0; i < vector.length; i++) {
            vector[i] = 1.0 / (1.0 + Math.exp(-vector[i])); // modifying 1st param to save space and code
        }
        return vector;
    }
}

class ReadUtils {
    public static double[][] readWeights(String filePath, int rows, int cols) {
        try (Scanner scanner = new Scanner(Objects.requireNonNull(ReadUtils.class.getClassLoader().getResourceAsStream(filePath)))) {
            double[][] weights = new double[rows][cols];
            int row = 0;
            while (scanner.hasNextLine() && row < rows) {
                String[] values = scanner.nextLine().trim().split(" ");
                for (int col = 0; col < cols && col < values.length; col++) {
                    weights[row][col] = Double.parseDouble(values[col]);
                }
                row++;
            }
            return weights;
        }
    }
}