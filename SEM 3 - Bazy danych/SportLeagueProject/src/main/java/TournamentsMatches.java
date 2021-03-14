import TablesClasses.Match;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class TournamentsMatches extends JFrame implements ActionListener {

    /** Tabela przyciskow akceptujacych uzytkownikow */
    private JButton[] seeMatchButton;
    /** Tabela przyciskow odrzucajacych uzytkownikow */
    private JButton[] addScoreButton;
    /** Lista loginow */
    private List requestsMatches;
    /** Id zalogowanego uzytkownika */
    private int tournamentId;
    /** Id uzytkownika */
    private int userId;
    /** Czy włączony jest tryb widza */
    private boolean isSpectatorMode;

    /** Initializator etykiet
     * @param text tekst w etykiecie
     * @return etykieta
     */
    private JLabel labelInitializor(String text) {
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        JLabel label = new JLabel(text);
        label.setFont(font);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    /** Konstruktor okna
     * @param tournamentId Id zalogowanego uzytkownika
     */
    TournamentsMatches(int tournamentId, int userId, boolean isSpectatorMode) {
        this.userId = userId;
        this.tournamentId = tournamentId;
        this.isSpectatorMode = isSpectatorMode;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JScrollPane scrollBar = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollBar, BorderLayout.CENTER);

        int columnsNumber = 7;

        if(isSpectatorMode) {
            columnsNumber = 6;
        }

        setTitle("Mecze w turnieju " + DatabaseApplication.queries(new String[]{"tournamentNameById", String.valueOf(tournamentId)}).get(0));

        requestsMatches = DatabaseApplication.queries(new String[]{"requestMatchesByTournamentId", String.valueOf(tournamentId)});
        panel.setLayout(new GridLayout(requestsMatches.size(), columnsNumber,10,10));

        seeMatchButton = new JButton[requestsMatches.size()];
        addScoreButton = new JButton[requestsMatches.size()];
        for(int i = 0; i < requestsMatches.size(); i++) {
            seeMatchButton[i] = new JButton("Zobacz szczegóły");
            seeMatchButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            seeMatchButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            seeMatchButton[i].addActionListener(this);
        }
        for(int i = 0; i < requestsMatches.size(); i++) {
            addScoreButton[i] = new JButton("Dodaj zdarzenie");
            addScoreButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            addScoreButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            addScoreButton[i].addActionListener(this);
            if(isSpectatorMode) {
                addScoreButton[i].setEnabled(false);
            }
        }

        JComponent[][] data = new JComponent[requestsMatches.size()+1][7];
        String[] columnNames = {"Drużyna pierwsza", "Punkty", "Drużyna druga", "Punkty", "Status", "Zobacz szczegóły", "Dodaj zdarzenie"};
        for(int i = 0; i < columnsNumber; i++) {
            data[0][i] = labelInitializor(columnNames[i]);
            data[0][i].setFont(new Font("Segoe UI", Font.BOLD, 23));
        }
        for(int i = 1; i < requestsMatches.size() + 1; i++) {
            data[i][0] = labelInitializor(DatabaseApplication.queries(new String []{"getTeamNameById", String.valueOf(((Match) requestsMatches.get(i-1)).getIdDruzynyPierwszej())}).get(0).toString());
            data[i][1] = labelInitializor(String.valueOf(((Match) requestsMatches.get(i-1)).getPunktyDruzynyPierwszej()));
            data[i][2] = labelInitializor(DatabaseApplication.queries(new String []{"getTeamNameById", String.valueOf(((Match) requestsMatches.get(i-1)).getIdDruzynyDrugiej())}).get(0).toString());
            data[i][3] = labelInitializor(String.valueOf(((Match) requestsMatches.get(i-1)).getPunktyDruzynyDrugiej()));
            data[i][5] = seeMatchButton[i-1];
            data[i][6] = addScoreButton[i-1];
            if(((Match) requestsMatches.get(i-1)).isStatus() == true) {
                data[i][4] = labelInitializor("Zakończony");
                data[i][6].setEnabled(false);
            }
            else {
                data[i][4] = labelInitializor("W trakcie...");
            }
        }

        JPanel headerPanel = new JPanel(new GridLayout(1, columnsNumber, 10, 10));
        for(int i = 0; i < columnsNumber; i++) {
            headerPanel.add(data[0][i]);
        }
        add(headerPanel, BorderLayout.NORTH);

        for (int i = 1; i < requestsMatches.size() + 1; i++) {
            for (int j = 0; j < columnNames.length; j++) {
                if(j == 6 && isSpectatorMode) {
                    continue;
                }
                panel.add(data[i][j]);
            }
        }
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 8;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        for(int i = 0; i < seeMatchButton.length; i++) {
            if(!isSpectatorMode) {
                if (event == addScoreButton[i]) {
                    new AddScore(((Match) requestsMatches.get(i)).getIdMeczu(), userId);
                    this.dispose();
                }
            }
            if(event == seeMatchButton[i]) {
                if(DatabaseApplication.queries(new String[]{"matchDetailsByMatchId", String.valueOf(((Match)requestsMatches.get(i)).getIdMeczu())}).size() > 0) {
                    new MatchDetails(((Match)requestsMatches.get(i)).getIdMeczu());
                }
                else {
                    JOptionPane.showMessageDialog(this, "Brak zdobytych punktów", "Informacja", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }
}
