import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameClientTest {

    @Test
    void connectClient() {
        GameClient gameClient = new GameClient();
        GUI gui = new GUI(9,false);
        assertEquals(1, gameClient.connectClient());
    }

    @Test
    void setSettings() {
        GameClient gameClient = new GameClient();
        GUI gui = new GUI(9,false);
        GameClient.gui = gui;
        gameClient.setSettings(9);
    }

}