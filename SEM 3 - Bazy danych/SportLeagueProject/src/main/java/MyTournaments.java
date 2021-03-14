import TablesClasses.Tournament;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

class MyTournaments extends JFrame implements ActionListener {

    /** Tabela przyciskow akceptujacych uzytkownikow */
    private JButton[] addMatchButton;
    /** Tabela przyciskow odrzucajacych uzytkownikow */
    private JButton[] seeMatchesButton;
    /** Lista loginow */
    private List requestsTournaments;
    /** Id zalogowanego uzytkownika */
    private int userId;
    /** Czy wlaczony jest tryb widza */
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
     * @param userId Id zalogowanego uzytkownika
     */
    MyTournaments(int userId, boolean isSpectatorMode) {
        this.userId = userId;
        this.isSpectatorMode = isSpectatorMode;

        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        JScrollPane scrollBar = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollBar, BorderLayout.CENTER);
        setTitle("Moje turnieje");

        requestsTournaments = DatabaseApplication.queries(new String[]{"requestTournaments", String.valueOf(userId)});

        panel.setLayout(new GridLayout(requestsTournaments.size(), 6,10,10));

        addMatchButton = new JButton[requestsTournaments.size()];
        seeMatchesButton = new JButton[requestsTournaments.size()];
        for(int i = 0; i < requestsTournaments.size(); i++) {
            addMatchButton[i] = new JButton("Dodaj mecz");
            addMatchButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            addMatchButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            addMatchButton[i].addActionListener(this);
        }
        for(int i = 0; i < requestsTournaments.size(); i++) {
            seeMatchesButton[i] = new JButton("Zobacz mecze");
            seeMatchesButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            seeMatchesButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            seeMatchesButton[i].addActionListener(this);
        }

        JComponent[][] data = new JComponent[requestsTournaments.size()+1][7];
        String[] columnNames = {"Nazwa turnieju", "Data", "Miejsce", "Dywizja", "Dodaj mecz", "Zobacz mecze"};
        for(int i = 0; i < 6; i++) {
            data[0][i] = labelInitializor(columnNames[i]);
            data[0][i].setFont(new Font("Segoe UI", Font.BOLD, 23));
        }
        for(int i = 1; i < requestsTournaments.size() + 1; i++) {
            data[i][0] = labelInitializor(String.valueOf(((Tournament) requestsTournaments.get(i-1)).getTournamentName()));
            data[i][1] = labelInitializor(String.valueOf(((Tournament) requestsTournaments.get(i - 1)).getTournamentDate()));
            data[i][2] = labelInitializor(String.valueOf(((Tournament) requestsTournaments.get(i-1)).getLocation()));
            data[i][3] = labelInitializor(String.valueOf(((Tournament) requestsTournaments.get(i-1)).getDivision()));
            data[i][4] = addMatchButton[i-1];
            data[i][5] = seeMatchesButton[i-1];
        }

        JPanel headerPanel = new JPanel(new GridLayout(1, 6, 0, 10));
        for(int i = 0; i < 6; i++) {
            headerPanel.add(data[0][i]);
        }
        add(headerPanel, BorderLayout.NORTH);

        for (int i = 1; i < requestsTournaments.size() + 1; i++) {
            for (int j = 0; j < 6; j++) {
                panel.add(data[i][j]);
            }
        }
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        pack();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        for(int i = 0; i < addMatchButton.length; i++) {
            if (event == addMatchButton[i]) {
                new AddMatch(userId, ((Tournament) requestsTournaments.get(i)).getTournamentId());
            }
            else if(event == seeMatchesButton[i]) {
                new TournamentsMatches(((Tournament) requestsTournaments.get(i)).getTournamentId(), userId, isSpectatorMode);
            }
        }
    }
}
