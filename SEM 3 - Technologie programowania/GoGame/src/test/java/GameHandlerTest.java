import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {
    private GameHandler gameHandler;

    @Before
    public void settings() {
        gameHandler = new GameHandler(13);
    }

    @Test
    public void isLibertyLeft() {
        StoneChain chain = new StoneChain(1);
        chain.liberties.add(new Pair(4,5));

        assertTrue(gameHandler.isLibertyLeft(chain));
    }
}

