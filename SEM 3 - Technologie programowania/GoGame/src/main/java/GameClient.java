import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;

/** Klient gry */
public class GameClient implements Runnable {

    /** Id gracza */
    private int playerID;
    /** Socket klienta */
    private Socket socket;
    /** Czytanie danych z serwera */
    private BufferedReader dataIn;
    /** Wysylanie danych do serwera */
    private PrintWriter dataOut;
    /** Klient gracza */
    static GameClient gameClient;
    /** Czy jest twoja tura */
    boolean isYourTurn = false;
    /** Gui gracza */
    static GUI gui;
    /** Wiadomosc o ruchu */
    private String moveMsg;
    /** Czy gra jest skonczona */
    boolean gameIsFinished = true;
    /** Rozmiar planszy */
    private int boardSize;
    /** Czy z botem? */
    private boolean bot = false;
    /** Czy trwa symulacja */
    private boolean isSimulation = false;

    static String dates;
    /** Konstruktor klienta */
    GameClient() {
        Menu menu = new Menu();
    }

    /**Symulacja*/
     void startSimulation () {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as as client to watch simulation");
            dataOut.println("simulation");
            dates = dataIn.readLine(); // czyta tablice dat
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
        } catch (IOException e) {
            System.out.println("Server you are trying to connect is disconnected");
        }
    }

    /** Rozpoczyna symulacje */
     void sendGame(Object date) {
        System.out.println(date.toString());
        dataOut.println(date.toString());
        isSimulation = true;
        Thread thread = new Thread(gameClient);
        thread.start();
    }

    /** Podmetoda, w ktorej trwa symulacja */
    private void watchGame() {
        String line = null;
        try {
            line = dataIn.readLine(); // pierwszy stan planszy
        } catch (IOException e) {
            e.printStackTrace();
        }
        String[] data = line.split(" ");
        int boardSize = (int) Math.sqrt(data[0].length()-1);
        gui = new GUI(boardSize, true);
        gui.setBoard(line);
        while(true) {
            moveMsg = null;
            while (moveMsg == null) {
                moveMsg = gui.getMsg(); // Oczekiwanie, aż gracz zrobi jakiś ruch
            }
            System.out.println(moveMsg);
            dataOut.println(moveMsg);
            gui.nullMsg();
            try {
                line = dataIn.readLine();
            } catch (IOException e) {
                gui.setGameStatusLabel("Oops, disconnected!");
                break;
            }
            gui.setBoard(line);
        }
    }

    /** Gra */
    public void run() {
        if(isSimulation) {
            watchGame();
            return;
        }
        gui.setGameStatusLabel("Waiting for opponent to join");
        try {
            if(dataIn.readLine().equals("Let's begin")) {
                gameIsFinished = false;
            }
        } catch (IOException e) {
            serverDisconnected();
            return;
        }
        while (!gameIsFinished) {
            String line;
            gui.setGameStatusLabel(isYourTurn);
            if (isYourTurn) {
                moveMsg = null;
                while (moveMsg == null) {
                    if(isYourTurn)
                        moveMsg = gui.getMsg(); // Oczekiwanie, aż gracz zrobi jakiś ruch
                }
                System.out.println(moveMsg);
                dataOut.println(moveMsg);
                if(gameIsFinished) break;
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch(IOException e){
                    serverDisconnected();
                    break;
                }
            }
            else {
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch (IOException e) {
                    serverDisconnected();
                    break;
                }

            }
        }
        gui.setGameStatusLabel("Game has ended");
        gui.showSummary();
    }

    /** Wyslij odpowiedz do serwera
     * @param response Y / N
     */
    private void sendSummaryResponse(String response) {
        dataOut.println(response);
    }
    /** Odczytuje informajce z serwera
     * @param line informacja z serwera
     * */
    private void readServerMsg (String line) {
        if((Character.toString(line.charAt(0)).equals("#"))) {
            if(playerID == 1)
                doWeEnd();
            if(playerID == 2)
                waitForOpponent();
            if(!gameIsFinished) {
                try {
                    line = dataIn.readLine();
                } catch (IOException e) {
                    serverDisconnected();
                    return;
                }
            }
            else return;
        }
        if(line.equals("Opponent left")) {
            gui.opponentLeft();
            gameIsFinished = true;
            return;
        }
        if (line.charAt(0)- 48 == playerID ) {
            isYourTurn = true;
            gui.nullMsg();
        }
        else isYourTurn = false;
        gui.setBoard(line);
    }

    /** Czekaj na przeciwnika az podejmie decyzje czy chce wznowic gre */
    private void waitForOpponent() {
        gui.waitForOpponent();
        try {
            if(dataIn.readLine().equals("Do you want to resume?")) doWeEnd();
        } catch (IOException e) {
            serverDisconnected();
        }
    }

    /** Sprawdzamy czy gracz chce skonczyc gre */
    private void doWeEnd() {
        gameIsFinished = true;
        switch (gui.doYouWantToEnd()) {
            case 0:
                sendSummaryResponse("Y");
                break;
            case 1:
                sendSummaryResponse("N");
                break;
            case -1:
                doWeEnd();
                return;
        }
        try {
            if(dataIn.readLine().equals("Game is continuing")) gameIsFinished = false;
        } catch (IOException e) {
            serverDisconnected();
        }
    }

    int getBoardSize() {
        return boardSize;
    }

    /** Podlacza klienta do serwera */
    int connectClient() {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as player " + playerID);
            if(playerID == 1) {
                if (bot) dataOut.println("BOT");
                else dataOut.println("NIEBOT");
            }
            if(playerID == 2) {
                boardSize = Integer.parseInt(dataIn.readLine());
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown host: localhost");
            return 0;
        } catch (IOException e) {
            System.out.println("Server you are trying to connect is disconnected");
            return 0;
        }
        return playerID;
    }

    /** Ustawienia poczatkowe */
    void setSettings(int boardSize) {
        gui.setTitle("Gracz #" + playerID);
        gui.setPlayerID(playerID);
        if (playerID == 1) {
            isYourTurn = true;
            dataOut.println(boardSize);
        }
        else {
            isYourTurn = false;
        }
        Thread thread = new Thread(gameClient);
        thread.start();
    }

    void startGameWithBot() {
        bot = true;
    }

    private void serverDisconnected() {
        System.out.println("Oops! Server has been disconnected");
        gui.setGameStatusLabel("Oops! Server has been disconnected");
        gameIsFinished = true;
        gui.showSummary();
    }

    /** Main
     * @param args puste
     */
    public static void main(String[] args) {
        gameClient = new GameClient();
    }

}
