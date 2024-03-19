package nd.jar.neuralpoker.prepare;

import nd.jar.neuralpoker.common.Common;

import java.util.Arrays;
import java.util.stream.Collectors;

public class BitSetUtils{

    public static String toCsv(Common.SizedBitSet bitSet){
        return Arrays.stream(bitSet.set())
                .mapToObj(String::valueOf)
                .collect(Collectors.joining("\t"));
    }

    public static String toAscii(Common.SizedBitSet bitSet){
        var s = Arrays.stream(bitSet.set()).mapToObj(d -> d == 1.0? "@":".").collect(Collectors.joining(""));
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < bitSet.height(); row++) {
            sb.append(s, row * bitSet.width(), (row+1) * bitSet.width()).append("\r\n");
        }
        return sb.toString();
    }
}
