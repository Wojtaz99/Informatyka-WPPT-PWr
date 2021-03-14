import org.junit.Before;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class StoneTest {

    @Test
    void setPlayer() {
        Stone stone = new Stone(1, 1, 20, 20);
        stone.setPlayer("BLACK");
        assertEquals(Visibility.VISIBLE, stone.visibility);
        assertEquals(Player.BLACK, stone.getPlayer());
    }

    @Test
    void stoneColor() {
        Stone stone = new Stone(1, 1, 20, 20);
        stone.setPlayer("BLACK");
        assertEquals(Color.BLACK, stone.stoneColor());
    }

    @Test
    void setVisibility() {
        Stone stone = new Stone(1, 1, 20, 20);
        stone.setPlayer("BLACK");
        stone.setVisibility(Visibility.INVISIBLE);
        assertEquals(stone.visibility, Visibility.INVISIBLE);
    }
}