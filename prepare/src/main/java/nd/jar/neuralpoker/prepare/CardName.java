package nd.jar.neuralpoker.prepare;

public record CardName(String num, char suit){
    public static CardName EMPTY = new CardName("-1", 'E');
    static CardName fromNthFilename(String filename, int cardNumber) {
        final var withoutTens = filename.replaceAll("10", "T");
        final var length = withoutTens.length();
        if (cardNumber * 2 + 2 + 4 > length ) {
            return EMPTY;
        } else {
            return toCardName(withoutTens.substring(cardNumber * 2, cardNumber * 2 + 2));
        }
    }
    private static CardName toCardName(String str) {
        var numChar = str.charAt(0);
        String num = numChar == 'T'? "10":String.valueOf(numChar);
        final var suit = str.charAt(1);
        return new CardName(num, suit);
    }

}
