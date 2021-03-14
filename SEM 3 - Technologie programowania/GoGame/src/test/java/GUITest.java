import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GUITest {

    GUI gui;

    @BeforeEach
    void setUp() {
        gui = new GUI(9,false);
    }

    @Test
    void getMsg() {
        assertEquals(null, gui.getMsg());
    }

    @Test
    void setMsg() {
        gui.setMsg("cos");
        assertEquals("cos", gui.getMsg());
    }

    @Test
    void nullMsg() {
        gui.nullMsg();
        assertEquals(null, gui.getMsg());
    }

    @Test
    void setPlayerID() {
        gui.setPlayerID(1);
        assertEquals(1, gui.getPlayerID());
    }

    @Test
    void getLabelText() {
        assertEquals("Test", gui.getLabelText());
    }

    @Test
    void setGameStatusLabel() {
        gui.setGameStatusLabel(true);
        assertEquals("It's your turn", gui.getLabelText());
    }

    @Test
    void testSetGameStatusLabel() {
        gui.setGameStatusLabel("OK");
        assertEquals("OK", gui.getLabelText());
    }
}