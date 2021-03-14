import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

class Bot implements Runnable {

    /** Id gracza */
    private int playerID;
    /** Socket bota */
    private Socket socket;
    /** Czytanie danych z serwera */
    private BufferedReader dataIn;
    /** Wysylanie danych do serwera */
    private PrintWriter dataOut;
    /** Czy jest twoja tura */
    private boolean isYourTurn = false;
    /** Czy gra jest skonczona */
    private boolean gameIsFinished = true;
    /** Bot */
    private int boardSize;

    private boolean canBotMove = true;

    /** Konstruktor klienta */
    Bot() {
        Thread thread = new Thread(this);
        thread.start();
    }

    /** Gra */
    public void run() {
        connectBot();
        try {
            if(dataIn.readLine().equals("Let's begin")) {
                gameIsFinished = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!gameIsFinished) {
            String line = "";

            if (isYourTurn) {
                doMove();
                if(gameIsFinished) break;
                try {
                    line = dataIn.readLine();
                    canBotMove = true;
                    readServerMsg(line);
                } catch(IOException e){
                    e.printStackTrace();
                }

            }
            else {
                try {
                    line = dataIn.readLine();
                    readServerMsg(line);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
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
        String[] tab = line.split(" ");
        if((Character.toString(line.charAt(0)).equals("#"))) {
            if(playerID == 1)
                doWeEnd();
            if(playerID == 2)
                waitForOpponent();
            if(!gameIsFinished) {
                try {
                    line = dataIn.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else return;
        }
        if(line.equals("Opponent left")) {
            gameIsFinished = true;
            return;
        }
        if(tab.length == 4 && tab[3].equals("0")) {
            canBotMove = false;
        }
        if (line.charAt(0) - 48 == playerID ) {
            isYourTurn = true;
        }
        else isYourTurn = false;
    }

    /** Czekaj na przeciwnika az podejmie decyzje czy chce wznowic gre */
    private void waitForOpponent() {
        try {
            if(dataIn.readLine().equals("Do you want to resume?")) doWeEnd();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Sprawdzamy czy gracz chce skonczyc gre */
    private void doWeEnd() {
        gameIsFinished = true;
        sendSummaryResponse("N");
        try {
            if(dataIn.readLine().equals("Game is continuing")) gameIsFinished = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Podlacza klienta do serwera */
    private int connectBot() {
        System.out.println("-----Client----");
        try {
            socket = new Socket("localhost", 4444);
            dataIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOut = new PrintWriter(socket.getOutputStream(), true);
            playerID = Integer.parseInt(dataIn.readLine());
            System.out.println("Connected as player " + playerID);
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
        setSettings(boardSize);
        return playerID;
    }

    /** Ustawienia poczatkowe */
    private void setSettings(int boardSize) {
        if (playerID == 1) {
            isYourTurn = true;
            dataOut.println(boardSize);
        }
        else {
            isYourTurn = false;
        }

    }

    /** Ruch bota */
    private void doMove() {
        if(canBotMove) {
            String move = (int) Math.floor(Math.random() * boardSize) + " " + (int) Math.floor(Math.random() * boardSize);
            dataOut.println(move);
        }
        else dataOut.println("pass");
    }
}
