import TablesClasses.Match;
import TablesClasses.Player;
import TablesClasses.Scores;

import javax.swing.*;
import java.awt.*;
import java.util.List;

class MatchDetails extends JFrame {

    /** Lista loginow */
    private List scoresList;

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
     * @param matchId Id meczu
     */
    MatchDetails(int matchId) {
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JScrollPane scrollBar = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollBar, BorderLayout.CENTER);

        setTitle("Szczegóły meczu");
        List thisMatch = DatabaseApplication.queries(new String[]{"getMatchById", String.valueOf(matchId)});

        String[] columnNames = {String.valueOf(DatabaseApplication.queries(new String[]{"getTeamNameById", String.valueOf(((Match) thisMatch.get(0)).getIdDruzynyPierwszej())}).get(0)),
                String.valueOf(DatabaseApplication.queries(new String[]{"getTeamNameById", String.valueOf(((Match) thisMatch.get(0)).getIdDruzynyDrugiej())}).get(0)),
                "Imię zdobywającego", "Nazwisko zdobywającego", "Numer zawodnika", "Nazwa drużyny"};
        scoresList = DatabaseApplication.queries(new String[]{"matchDetailsByMatchId", String.valueOf(matchId)});
        panel.setLayout(new GridLayout(scoresList.size(), columnNames.length,10,10));

        JComponent[][] data = new JComponent[scoresList.size()+1][6];

        for(int i = 0; i < columnNames.length; i++) {
            data[0][i] = labelInitializor(columnNames[i]);
            data[0][i].setFont(new Font("Segoe UI", Font.BOLD, 23));
        }

        for(int i = 1; i < scoresList.size() + 1; i++) {
            data[i][0] = labelInitializor(String.valueOf(((Scores) scoresList.get(i-1)).getTeamOneScoreAfterPoint()));
            data[i][1] = labelInitializor(String.valueOf(((Scores) scoresList.get(i-1)).getTeamTwoScoreAfterPoint()));
            List playerList = DatabaseApplication.queries(new String []{"playerById", String.valueOf(((Scores) scoresList.get(i-1)).getPlayerId())});
            data[i][2] = labelInitializor(((Player) playerList.get(0)).getName());
            data[i][3] = labelInitializor(((Player) playerList.get(0)).getSurname());
            data[i][4] = labelInitializor(String.valueOf(((Player) playerList.get(0)).getPlayerNumber()));
            data[i][5] = labelInitializor(DatabaseApplication.queries(new String []{"getTeamNameById", String.valueOf(((Player) playerList.get(0)).getTeamId())}).get(0).toString());
        }

        JPanel headerPanel = new JPanel(new GridLayout(1, columnNames.length, 10, 10));
        for(int i = 0; i < columnNames.length; i++) {
            headerPanel.add(data[0][i]);
        }
        add(headerPanel, BorderLayout.NORTH);

        for (int i = 1; i < scoresList.size() + 1; i++) {
            for (int j = 0; j < columnNames.length; j++) {
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
}
