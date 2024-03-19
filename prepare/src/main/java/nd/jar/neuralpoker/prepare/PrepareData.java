package nd.jar.neuralpoker.prepare;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

import static nd.jar.neuralpoker.common.Common.*;
import static nd.jar.neuralpoker.prepare.Utils.*;

public class PrepareData {
    public static void main(String[] args) throws IOException {
        var resourcesFolder = Paths.get(System.getProperty("user.dir"),"resources");

        var folder = Paths.get(resourcesFolder.toString(),"imgs_marked").toFile();
        if (!folder.exists()) {
            throw new RuntimeException("folder %s does not exists".formatted(folder.getPath()));
        }
        var outputDir = Paths.get(resourcesFolder.toString(), "learning");
        var outputFolder = outputDir.toFile();

        if (!outputFolder.exists()) {
            outputFolder.mkdir();
        }
        final var numsFilename = "data_nums.csv";
        final var suitFilename = "data_suits.csv";


        Path numsCsvPath = Paths.get(outputDir.toString(), numsFilename);
        Path suitCsvPath = Paths.get(outputDir.toString(), suitFilename);
        process(numsCsvPath, suitCsvPath, folder);
    }

    private static void process(Path numsCsvPath, Path suitCsvPath, File folder) throws IOException {
        var processed = 0;
        try (PrintWriter numsPw = new PrintWriter(Files.newBufferedWriter(numsCsvPath));
             PrintWriter suitsPw = new PrintWriter(Files.newBufferedWriter(suitCsvPath))){
            for (File file :  Objects.requireNonNull(folder.listFiles())) {
                processFile(file, suitsPw, numsPw);
                processed++;
                if (processed % 10 == 0){
                    System.out.printf("Processed %d files", processed);
                }
            }
        }
        System.out.printf("Done. Processed %d files", processed);
    }

    private static void processFile(File file, PrintWriter suitsPw, PrintWriter numsPw) throws IOException {
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
            numsPw.println(toCsv(numBitset) +"\t"+ name.num());
        }
    }
}
