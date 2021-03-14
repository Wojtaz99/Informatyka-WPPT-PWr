import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    void getSIZE() {
        GUI gui = new GUI(9,false);
        Board board = new Board(gui, 9);
        assertEquals(board.getSIZE(), 9);
    }
}