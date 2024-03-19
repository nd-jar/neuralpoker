package nd.jar.neuralpoker.prepare;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CardNameTest {

    @Test
    public void fromNthFilename() {
        assertEquals(new CardName("1", '1'), CardName.fromNthFilename("11223344.jpg", 0));
        assertEquals(new CardName("2", '2'), CardName.fromNthFilename("11223344.jpg", 1));
        assertEquals(new CardName("3", '3'), CardName.fromNthFilename("11223344.jpg", 2));
        assertEquals(new CardName("4", '4'), CardName.fromNthFilename("11223344.jpg", 3));
        assertEquals(new CardName("-1", 'E'), CardName.fromNthFilename("11223344.jpg", 4));
        assertEquals(new CardName("-1", 'E'), CardName.fromNthFilename("11223344.jpg", 5));
        assertEquals(CardName.EMPTY, CardName.fromNthFilename("11223344.jpg", 6));
        assertEquals(new CardName("1", 'a'), CardName.fromNthFilename("1a10b.jpg", 0));
        assertEquals(new CardName("10", 'b'), CardName.fromNthFilename("1a10b.jpg", 1));
    }
}
