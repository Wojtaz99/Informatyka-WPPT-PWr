import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Głowna klasa gracza */
public class GUI extends JFrame implements ActionListener, MouseListener, MouseMotionListener {
    /** Pasek menu */
    private JMenuBar menuBar;
    /** Opcje na pasku menu */
    private JMenu optionsMenu;
    /** Info na pasku menu */
    private JMenu infoMenu;
    /** Opcja wyjdz z gry */
    private JMenuItem exitItem;
    /** Opcja spasuj kolejke */
    private JMenuItem passItem;
    /** Informacja o zasadach */
    private JMenuItem rulesItem;
    /** Informacja o autorach */
    private JMenuItem authorsItem;
    /** Plansza */
    private static Board board;
    /** Wiadomosc do serwera */
    private String msg = null;
    /** Id gracza */
    private int playerID;
    /** Etykieta wyswietlajaca stan gry */
    private JLabel gameStatusLabel;
    /** Twoi jency */
    private int score[];
    /** Przycisk nastepnego ruchu w symulacji */
    private JButton nextButton;
    /** Przycisk poprzedniego ruchu w symulacji */
    private JButton previousButton;

    /** Konstruktor maina
     * @param boardSize rozmiar planszy
     * */
    GUI(int boardSize, boolean isSimulation) {
        gameInitialize(boardSize, isSimulation);
    }

    /** Metoda actionPerformed */
    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if (event == exitItem) {
            System.exit(0);
        } else if (event == authorsItem) {
            JOptionPane.showMessageDialog(this, "Authors: Wiktoria Byra, Wojciech Pakulski", "Authors", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(event == passItem) {
            msg = "pass";
        }
        else if(event == rulesItem) {
            JOptionPane.showMessageDialog(this, "You can find rules on dr Macyna's website", "Rules", JOptionPane.INFORMATION_MESSAGE);
        }
        else if(event == nextButton) {
            msg = "next";
        }
        else if(event == previousButton) {
            msg = "previous";
        }
    }

    /** Tworzy okno gry */
    private void gameInitialize(int boardSize, boolean isSimulation) {
        // tworzenie menu
        menuBar = new JMenuBar();
        optionsMenu = new JMenu("Options");
        passItem = new JMenuItem("Pass");
        exitItem = new JMenuItem("Surrender/Exit");
        // dodawanie elementow do menu
        optionsMenu.add(passItem);
        optionsMenu.add(exitItem);
        menuBar.add(optionsMenu);
        setJMenuBar(menuBar);
        // menu info z opcjami zasady i autorzy
        infoMenu = new JMenu("Info");
        rulesItem = new JMenuItem("Rules");
        authorsItem = new JMenuItem("Authors");
        infoMenu.add(rulesItem);
        infoMenu.add(authorsItem);
        menuBar.add(infoMenu);
        board = new Board(this, boardSize);
        add(board, BorderLayout.CENTER);


        gameStatusLabel = new JLabel("You are in spectator mode");
        gameStatusLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        board.add(gameStatusLabel, BorderLayout.SOUTH);


        // action Listenery
        passItem.addActionListener(this);
        exitItem.addActionListener(this);
        rulesItem.addActionListener(this);
        authorsItem.addActionListener(this);

        //mouse listener
        board.addMouseListener(this);
        board.addMouseMotionListener(this);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setResizable(false);

        if(isSimulation) {
            nextButton = new JButton("Next move");
            previousButton = new JButton("Previous move");
            board.add(previousButton, BorderLayout.SOUTH);
            board.add(nextButton, BorderLayout.SOUTH);
            // CZEMU TO TAK DZIALA?
            nextButton.addActionListener(this);
            previousButton.addActionListener(this);
        }
        pack();
    }

    /** Pokaz podsumowanie jencow na koncu gry */
    void showSummary() {
        JOptionPane.showMessageDialog(this, "Your score: " + score[playerID-1] + "\nOpponent's score: " + score[playerID%2],
                "Results", JOptionPane.INFORMATION_MESSAGE);
        if(playerID == 1 && score[0] > score[1] || playerID == 2 && score[1] > score[0]) setGameStatusLabel("Congratulations, you win!");
        else setGameStatusLabel("You loose!");
    }

    /** Zapytanie czy gracz chce wznowic gre
     * @return tak lub nie
     * */
    int doYouWantToEnd() {
        return JOptionPane.showConfirmDialog(this,
                "Your score: " + score[playerID-1] + "\nOpponent's score: " + score[playerID%2]
                        + "\nDo you want to resume the game?", "Is this the end?",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
    }

    /** Ustawia wyglad planszy analizujac wiadomosc od serwera
     * @param line wiadomosc o stanie gry od serwera
     * */
    void setBoard(String line) {
        for (int i = 0; i < board.getSIZE(); i++) {
            for (int j = 0; j < board.getSIZE(); j++) {
                if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 0)
                    board.getBoardTab()[i][j].setVisibility(Visibility.INVISIBLE);
                else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 1) {
                    board.getBoardTab()[i][j].setVisibility(Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("BLACK");
                } else if (Character.getNumericValue(line.charAt((i * board.getSIZE()) + j + 1)) == 2) {
                    board.getBoardTab()[i][j].setVisibility(Visibility.VISIBLE);
                    board.getBoardTab()[i][j].setPlayer("WHITE");
                }
            }
        }
        String[] dane = line.split(" ");
        score = new int[2];
        score[0] = Integer.parseInt(dane[1]);
        score[1] = Integer.parseInt(dane[2]);
        repaint();
    }

    /** Okno mowiace ze przeciwnik podejmuje decyzje */
    void waitForOpponent() {
        JOptionPane.showMessageDialog(this, "Your score: " + score[playerID-1] + "\nOpponent's score: " + score[playerID%2] +
                "\nWaiting for opponent...", "Results - waiting...", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Okno mowiace ze przeciwnik podejmuje decyzje */
    void opponentLeft() {
        JOptionPane.showMessageDialog(this, "You win! Opponent left or surrendered", "You win!", JOptionPane.INFORMATION_MESSAGE);
    }

    /** Ustaw tabele statusu gry
     * @param isYourTurn czy jest twoja tura
     */
    void setGameStatusLabel(boolean isYourTurn) {
        if(isYourTurn) {
            gameStatusLabel.setText("It's your turn");
        }
        else gameStatusLabel.setText("It's your opponent's turn");
    }

    /** Ustaw Stringa statusu gry
     * @param text tekst do labela
     */
    void setGameStatusLabel(String text) {
        gameStatusLabel.setText(text);
    }

    /** Sprawdza czy jest twoja tura
     * @return czy jest twoja tura
     */
    boolean checkIfIsYourTurn() {
        return GameClient.gameClient.isYourTurn;
    }

    /** Sprawdza czy gra juz sie skonczyla
     * @return czy gra sie skonczyla
     * */
    boolean checkIfGameIsFinished() {
        return GameClient.gameClient.gameIsFinished;
    }

    /** Zwroc msg
     * @return wiadomosc do serwera
     */
    String getMsg() {
        return msg;
    }

    /** Ustaw msg na null */
    void nullMsg() {
        msg = null;
    }

    /** Ustaw id gracza */
    void setPlayerID(int playerID) {
        this.playerID = playerID;
    }

    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
        //board.clickedOnStone(mouseEvent.getX(), mouseEvent.getY());
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
       board.enteredStone(mouseEvent);
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        msg = board.releasedStone();
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        board.draggedStone(mouseEvent);
    }
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseExited(MouseEvent mouseEvent) {}
    /** Implementowana funkcja mouse listenera */
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {}

    /** Ustaw msg (do testow) */
    void setMsg(String msg) { this.msg = msg;}

    /** Zwroc id gracza (do testow)
     * @return id gracza
     * */
    int getPlayerID() { return playerID; }

    /** Zwroc co wypisuje etykieta statusu (do testow)
     * @return tekst na etykiecie statusu
     * */
    String getLabelText() {
        return gameStatusLabel.getText();
    }
}
