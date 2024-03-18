package nd.jar.neuralpoker.prepare;

import nd.jar.neuralpoker.common.Common;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Utils {
    public static CardName getCardName(String filename, int cardNumber) {
        final var withoutTens = filename.replaceAll("10", "T");
        final var length = withoutTens.length();
        if (cardNumber * 2 + 2 + 4 > length ) {
            return EMPTY;
        } else {
            return toCardName(withoutTens.substring(cardNumber * 2, cardNumber * 2 + 2));
        }
    }
    public static CardName EMPTY = new CardName("-1", 'E');
    private static CardName toCardName(String str) {
        var numChar = str.charAt(0);
        String num = numChar == 'T'? "10":String.valueOf(numChar);
        final var suit = str.charAt(1);
        return new CardName(num, suit);
    }
    public record CardName(String num, char suit){ };
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
