import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTournament extends JFrame implements ActionListener {
    /** Panel glowny */
    private JPanel panel;
    /** Panel pomocniczy na logowanie */
    private JPanel panel1;
    /** Etykieta naglowka */
    private JLabel headerLabel;
    /** Etykieta nazwisko */
    private JLabel locationLabel;
    /** Etykieta imie */
    private JLabel tournamentNameLabel;
    /** Pole na nazwisko */
    private JTextField locationField;
    /** Pole na imie */
    private JTextField tournamentNameField;
    /** Przycisk rejestrujacy */
    private JButton addTournamentButton;
    /** Etykieta rok */
    private JLabel divisionLabel;
    /** Combobox z dywizją */
    private JComboBox divisionList;
    /** Etykieta plec */
    private JLabel dateLabel;
    /** Pole na plec */
    private JTextField dateField;
    /** Panel na label i guzik */
    private JPanel panel2;
    /** Id uzytkownika */
    private int userId;

    /** Konstruktor druzyny */
    AddTournament(int userId) {
        this.userId = userId;
        setTitle("Dodaj drużynę");
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        panel = new JPanel(new BorderLayout(10,10));

        headerLabel = new JLabel("Dodaj turniej");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        tournamentNameLabel = new JLabel("Nazwa turnieju:");
        tournamentNameLabel.setFont(font);
        tournamentNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        tournamentNameField = new JTextField(10);
        tournamentNameField.setFont(font);

        locationLabel = new JLabel("Miejsce:");
        locationLabel.setFont(font);
        locationLabel.setHorizontalAlignment(SwingConstants.CENTER);
        locationField = new JTextField(10);
        locationField.setFont(font);

        dateLabel = new JLabel("Data (d/m/r):");
        dateLabel.setFont(font);
        dateLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dateField = new JTextField(10);
        dateField.setFont(font);

        divisionLabel = new JLabel("Dyzwizja:");
        divisionLabel.setFont(font);
        divisionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] divisionStrings = { "Open", "Women", "Mixed", "Open/Women"};
        divisionList = new JComboBox(divisionStrings);
        divisionList.setSelectedIndex(0);
        divisionList.setFont(font);
        divisionList.addActionListener(this);

        addTournamentButton = new JButton("Utwórz");
        addTournamentButton.setFont(font);
        addTournamentButton.addActionListener(this);

        panel2 = new JPanel(new FlowLayout());
        panel2.add(addTournamentButton);

        panel1 = new JPanel(new GridLayout(4,2, 10, 10));
        panel1.add(tournamentNameLabel);
        panel1.add(tournamentNameField);
        panel1.add(locationLabel);
        panel1.add(locationField);
        panel1.add(dateLabel);
        panel1.add(dateField);
        panel1.add(divisionLabel);
        panel1.add(divisionList);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(panel1, BorderLayout.CENTER);
        panel.add(panel2, BorderLayout.SOUTH);
        add(panel);

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);

        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack();
    }

    /** Metoda actionPerformed */
    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if(object == addTournamentButton) {
            if (tournamentNameField.getText().equals("") || locationField.getText().equals("") || dateField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            else {
                if(DatabaseApplication.queries(new String[]{"addTournament", tournamentNameField.getText(), locationField.getText(),
                        dateField.getText(), divisionList.getSelectedItem().toString(), String.valueOf(userId)}).size() > 0) {
                    JOptionPane.showMessageDialog(this, "Nieprawidłowy format daty!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Poprawnie dodano turniej", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                this.dispose();
            }
        }
    }
}