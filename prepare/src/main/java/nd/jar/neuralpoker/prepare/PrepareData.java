package nd.jar.neuralpoker.prepare;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import static nd.jar.neuralpoker.common.Common.*;
import static nd.jar.neuralpoker.prepare.Utils.*;

public class PrepareData {
    public static void main(String[] args) throws IOException {
        final var learningFolder = "D:\\Projects\\java\\blackjack\\src\\main\\resources\\imgs_marked";
        final var numsFilename = "data_nums.csv";
        final var suitFilename = "data_suits.csv";
        try (PrintWriter numsPw = new PrintWriter(Files.newBufferedWriter(Paths.get(numsFilename)));
             PrintWriter suitsPw = new PrintWriter(Files.newBufferedWriter(Paths.get(suitFilename)))){
            for (File file :  Objects.requireNonNull(new File(learningFolder).listFiles())) {
                final var image = ImageIO.read(file);
                for (int i = 0; i < topLeftCornerX.length; i++) {
                    var name = getCardName(file.getName(), i);
                    final var suitBitset = getNthSizedBitSet(image, i, suitCoordinates, suitSize);
                    System.out.println(toAscii(suitBitset));
                    suitsPw.println(toCsv(suitBitset) +"\t"+ name.suit());

                    if (name == EMPTY) {
                        continue;
                    }
                    final var numBitset = getNthSizedBitSet(image, i, numCoordinates, numSize);
                    System.out.println(toAscii(numBitset));
                    System.out.println(toAscii(suitBitset));
                    numsPw.println(toCsv(numBitset) +"\t"+ name.num());

                }
            }
        }
        System.out.println("Done");
    }

}
