import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AddMatch extends JFrame implements ActionListener {
    /** Panel glowny */
    private JPanel panel;
    /** Panel pomocniczy na logowanie */
    private JPanel panel1;
    /** Etykieta naglowka */
    private JLabel headerLabel;
    /** Pole na nazwe drugiej druzyny */
    private JComboBox teamName2List;
    /** Przycisk dodajacy mecz */
    private JButton addMatchButton;
    /** Pole na nazwy turniejow */
    private JComboBox tournamentNameList;
    /** Panel na label i guzik */
    private JPanel panel2;
    /** Id druzyny do ktorej dodajemy */
    private int userId;
    /** Pole na nazwe pierwszej druzyny */
    private JComboBox teamNameList;

    /** Konstruktor okna dodaj gracza */
    AddMatch(int userId, int tournamentId) {
        super("Dodaj mecz");
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        panel = new JPanel(new BorderLayout(10,10));

        this.userId = userId;
        List teamNameString = DatabaseApplication.queries(new String[] {"allTeamsByDivision", String.valueOf(tournamentId)});
        teamNameString.add("------");
        teamNameList = new JComboBox(teamNameString.toArray());
        teamNameList.setSelectedIndex(0);
        teamNameList.addActionListener(this);
        teamNameList.setFont(font);
        JLabel teamNameLabel = new JLabel("Nazwa pierwszej drużyny:");
        teamNameLabel.setFont(font);
        teamNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        teamName2List = new JComboBox(teamNameString.toArray());
        teamName2List.setSelectedIndex(0);
        teamName2List.addActionListener(this);
        teamName2List.setFont(font);
        JLabel teamName2Label = new JLabel("Nazwa drugiej drużyny:");
        teamName2Label.setFont(font);
        teamName2Label.setHorizontalAlignment(SwingConstants.CENTER);

        List tournamentString = DatabaseApplication.queries(new String[] {"allTournaments", String.valueOf(userId)});
        tournamentNameList = new JComboBox(tournamentString.toArray());
        tournamentNameList.setSelectedIndex(0);
        tournamentNameList.setFont(font);
        tournamentNameList.addActionListener(this);

        headerLabel = new JLabel("Dodaj mecz");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        addMatchButton = new JButton("Dodaj");
        addMatchButton.setFont(font);

        JLabel tournamentNameLabel = new JLabel("Nazwa turnieju:");
        tournamentNameLabel.setFont(font);
        tournamentNameLabel.setHorizontalAlignment(SwingConstants.CENTER);

        panel2 = new JPanel(new FlowLayout());
        panel2.add(addMatchButton);

        panel1 = new JPanel(new GridLayout(3,2, 10, 10));
        panel1.add(teamNameLabel);
        panel1.add(teamNameList);
        panel1.add(teamName2Label);
        panel1.add(teamName2List);
        panel1.add(tournamentNameLabel);
        panel1.add(tournamentNameList);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(panel1, BorderLayout.CENTER);
        panel.add(panel2, BorderLayout.SOUTH);
        add(panel);

        setVisible(true);
        setResizable(false);

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();

        addMatchButton.addActionListener(this);
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if(object == addMatchButton) {
            if(teamName2List.getSelectedItem().toString().equals("------") || teamNameList.getSelectedItem().toString().equals("------")) {
                JOptionPane.showMessageDialog(this, "Nie wybrano drużyn", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(this, "Poprawnie dodano mecz do turnieju", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                DatabaseApplication.queries(new String[]{"addMatch", teamNameList.getSelectedItem().toString(), teamName2List.getSelectedItem().toString(),
                        tournamentNameList.getSelectedItem().toString()});
                this.dispose();
            }
        }
    }
}
