import org.hibernate.Query;
import org.hibernate.Session;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/** Serwer gry */
public class GameServer {
    static GameServer gameServer;
    /** Socket serwera */
    private ServerSocket serverSocket;
    /** Licznik podlaczonych graczy */
    private int numPlayers;
    /** Przyjmowanie info od gracza 1 */
    private BufferedReader dataInPlayer1;
    /** Wysylanie info do gracza 1 */
    private PrintWriter dataOutPlayer1;
    /** Przyjmowanie info od gracza 2 */
    private BufferedReader dataInPlayer2;
    /** Wysylanie info do gracza 2 */
    private PrintWriter dataOutPlayer2;
    /** Obiekt zajmujacy sie logika */
    GameHandler gameHandler;
    /** Czyja tura- warosci 1/2 */
    private int whoseTurn = 1;
    /** Tablica ze stanem planszy */
    private int[][] stoneLogicTable;
    /** Rozmiar planszy */
    private int boardSize;
    /** Wspolrzedna x i y ruchu */
    private int moveX, moveY;
    /** Czy gra jest skończona */
    private boolean gameIsFinished = true;
    /** Czy jakis gracz opuscil gre? */
    private boolean playerLeft = false;
    /** Liczba jencow gracza nr 1 i 2 */
    int[] captives = new int[]{0, 0};
    /** Licznik spasowan */
    private int passCounter = 0;
    /** Czy z botem? */
    boolean bot = false;
    private int canBotMove = 1;
    /** terytorium graczy */
    private int[] territory = new int[3];
    /** Licznik prawidlowych ruchow */
    private int allowedMoveCounter = 1;
    /** Id ostatniego gracza */
    private int lastPlayerId = 1;
    /** Czy rozgrywana jest symulacja? */
    static boolean simulation = false;

    /** Konstruktor serwera */
    private GameServer() {
        System.out.println("----Game Server----");
        numPlayers = 0;
        try {
            serverSocket = new ServerSocket(4444);
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /** Wyslij stan planszy do obu graczy */
    private void sendToPlayers () {
        System.out.println("czyja tura po ruchu " + whoseTurn);
        String msg = Integer.toString(whoseTurn);
        for (int i=0; i<boardSize; i++) {
            for (int j=0; j<boardSize; j++) {
                msg = msg + stoneLogicTable[i][j];
            }
        }
        gameHandler.territory(stoneLogicTable);
        int score1 = captives[0]+territory[1];
        int score2 = captives[1]+territory[2];
        msg += " " + score1 + " " + score2;
        msg += " " + canBotMove;
        canBotMove = 1;
        System.out.println(msg);
        if(lastPlayerId != whoseTurn) {
            allowedMoveCounter++;
            insertToTable(msg);
        }
        lastPlayerId = whoseTurn;

        dataOutPlayer1.println(msg);
        dataOutPlayer2.println(msg);
    }

    /** Dodanie do bazy danych
     * @param msg tresc ruchu lub informacja o tym ze rozpoczynamy gre
     */
    private void insertToTable(String msg) {
        if(msg.equals("newGame")) {
            DatabaseApplication.queries(new String[]{msg});
        }
        else {
            DatabaseApplication.queries(new String[]{"doMove", Integer.toString(allowedMoveCounter), msg});
        }
    }

    /** Sluchanie socketa gracza
     * @param dataInPlayer obiekt do nasluchiwania odpowiedniego gracza
     * */
    private void listenPlayer (BufferedReader dataInPlayer) {
        String line;
        try {
            line = dataInPlayer.readLine();
            if (line.equals("pass")) {
                passTurn();
            }
            else {
                makeMoveInformationLine(line);
            }
        } catch (IOException e) {
            System.out.println("Player left the game");
            gameIsFinished = true;
            opponentLeft();
        }
    }

    /** Metoda odpowiadajaca za ruch graczy
     * @param line koordynaty ruchu gracza
     * */
    private void makeMoveInformationLine(String line) {
        String[] coordinates = line.split(" ");
        moveX = Integer.parseInt(coordinates[0]);
        moveY = Integer.parseInt(coordinates[1]);
        System.out.println(moveX +" " + moveY);
        whoseTurn = gameHandler.move(moveX, moveY, whoseTurn, stoneLogicTable);
        if (whoseTurn == 3) {
            whoseTurn = 1;
            canBotMove = 0;
        }
        System.out.println(whoseTurn);
    }

    /** Metoda wykonujaca sie jak klient spasuje */
    private void passTurn() {
        if (whoseTurn == 1) whoseTurn = 2;
        else whoseTurn = 1;
        passCounter+=2;
    }

    /** Ustawia roziar planszy i inicjalizuje tablice ze stanem planszy */
    private void setBoardSize() throws IOException {
        String line;
        line = dataInPlayer1.readLine(); //informacje o rozmiarze planszy
        try {
            boardSize = Integer.parseInt(String.valueOf(line));
        }
        catch(NumberFormatException e) {
            System.out.println("Podano zly rozmiar planszy - ustawiam domyslny");
            boardSize = 13;
        }
        stoneLogicTable = new int[boardSize][boardSize];
    }

    /** Ustaw tablice ze stanem plaszy
     * @param table stan planszy po stronie serwera
     */
    void setTable(int[][] table) {
        stoneLogicTable = table;
    }

    void setTerritory(int player1, int player2) {
        territory[1] = player1;
        territory[2] = player2;
    }

    /** Metoda podlaczająca klientow */
    private void acceptConnections() {
        try {
            System.out.println("Waiting for connections...");
            connectClient();
            if(simulation) {
                return;
            }
            if(bot) {
                new Bot();
            }
            connectClient();
        }
        catch (IOException ex) {
            System.out.println("Blad przy polaczeniu");
        }
    }
    /** Ustaw sockety klientow */
    private void connectClient() throws IOException {
        Socket socket = serverSocket.accept();
        numPlayers++;
        if(numPlayers == 1) {
            dataInPlayer1 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer1 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer1.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");
            String msg = dataInPlayer1.readLine();
            if(msg.equals("simulation")) {
                simulation = true;
                return;
            }
            if(msg.equals("BOT")) bot = true;
            setBoardSize();

        }
        else {
            dataInPlayer2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            dataOutPlayer2 = new PrintWriter(socket.getOutputStream(), true);
            dataOutPlayer2.println(numPlayers);
            System.out.println("Player " + numPlayers + " has connected");
            dataOutPlayer2.println(boardSize);
        }
        if (numPlayers == 2) {
            System.out.println("Now we have two players");
            dataOutPlayer1.println("Let's begin");
            dataOutPlayer2.println("Let's begin");
        }
    }

    /** Metoda trwajaca cala gre, ustawia kiedy nasluchiwac, a kiedy wysylac*/
    private void play() {
        gameHandler = new GameHandler(boardSize);
        gameIsFinished = false;
        while (!gameIsFinished) {
            if(whoseTurn == 1)
                listenPlayer(dataInPlayer1);
            else if(whoseTurn == 2) {
                listenPlayer(dataInPlayer2);
            }
            if(passCounter > 0) passCounter--;
            if(passCounter == 2) gameIsFinished = true;
            if(gameIsFinished && playerLeft) break;
            if(gameIsFinished) finishGame();
            sendToPlayers();
        }
    }

    /** Wywolywane gdy gra zakonczy sie poprawnie */
    private void finishGame() {
        String input1;
        String input2;
        passCounter = 0;
        dataOutPlayer1.println("#");
        dataOutPlayer2.println("#");
        try {
            input1 = dataInPlayer1.readLine();
            if(input1.equals("Y")) {
                whoseTurn = 2;
                gameIsFinished = false;
            }
            dataOutPlayer2.println("Do you want to resume?");
            input2 = dataInPlayer2.readLine();
            if(input2.equals("Y")) {
                whoseTurn = 1;
                gameIsFinished = false;
            }
            if(input2.equals("Y") && input1.equals("Y")) {
                whoseTurn = (int) Math.floor(Math.random()+1);
                gameIsFinished = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(!gameIsFinished) {
            dataOutPlayer1.println("Game is continuing");
            dataOutPlayer2.println("Game is continuing");
        }
        else {
            dataOutPlayer1.println(":(");
            dataOutPlayer2.println(":(");
        }
    }

    /** Wywolywane gdy gracz opusci gre */
    private void opponentLeft() {
        if(whoseTurn == 1) {
            dataOutPlayer2.println("Opponent left");
            playerLeft = true;
        }
        else {
            dataOutPlayer1.println("Opponent left");
            playerLeft = true;
        }
    }

    /** Tu odbywa sie symulacja */
    private void simulation() {
        System.out.println("Server connected with client and ready for simulation");
        List list = DatabaseApplication.queries(new String[]{"date list"});
        String dates = "";
        Iterator it = list.iterator();
        while(it.hasNext()) {
            dates += it.next().toString() + ";";
        }
        dataOutPlayer1.println(dates);
        String data;
        try {
            data = dataInPlayer1.readLine(); // czyta jaka gre klient chce otworzyc
        } catch (IOException e) {
            System.out.println("Client left before he picked simulation");
            return;
        }
        List moveList = DatabaseApplication.queries(new String[]{"data", data}); // lista ruchow w grze
        int i = 0;
        while(true) {
            dataOutPlayer1.println(moveList.get(i));
            System.out.println("Wysyłam stan planszy...");
            try {
                String line = dataInPlayer1.readLine();
                if(line.equals("next")) {
                    if (i < moveList.size() - 1) {
                        i++;
                        System.out.println("Następny");
                    }
                }
                else if(line.equals("previous")) {
                    if(i > 0) {
                        i--;
                        System.out.println("Poprzedni");
                    }
                }
            } catch (IOException e) {
                System.out.println("Client left");
                break;
            }
        }
    }
    /** Main
     * @param args pusto
     * */
    public static void main(String[] args) {
        gameServer = new GameServer();
        gameServer.acceptConnections();
        if (simulation) {
            gameServer.simulation();
        }
        else {
            gameServer.insertToTable("newGame");
            gameServer.play();
        }
    }
}
