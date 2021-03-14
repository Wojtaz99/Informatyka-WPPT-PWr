import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTeam extends JFrame implements ActionListener {
    /** Panel glowny */
    private JPanel panel;
    /** Panel pomocniczy na logowanie */
    private JPanel panel1;
    /** Etykieta naglowka */
    private JLabel headerLabel;
    /** Etykieta nazwisko */
    private JLabel cityLabel;
    /** Etykieta imie */
    private JLabel teamNameLabel;
    /** Pole na nazwisko */
    private JTextField cityField;
    /** Pole na imie */
    private JTextField teamNameField;
    /** Przycisk rejestrujacy */
    private JButton addPlayerButton;
    /** Etykieta rok */
    private JLabel divisionLabel;
    /** Combobox z dywizją */
    private JComboBox divisionList;
    /** Etykieta plec */
    private JLabel foundationYearLabel;
    /** Pole na plec */
    private JTextField foundationYearField;
    /** Panel na label i guzik */
    private JPanel panel2;
    /** Id uzytkownika */
    private int userId;

    /** Konstruktor druzyny */
    AddTeam(int userId) {
        this.userId = userId;
        setTitle("Dodaj drużynę");
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        panel = new JPanel(new BorderLayout(10,10));

        headerLabel = new JLabel("Dodaj drużynę");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        teamNameLabel = new JLabel("Nazwa drużyny:");
        teamNameLabel.setFont(font);
        teamNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        teamNameField = new JTextField(10);
        teamNameField.setFont(font);

        cityLabel = new JLabel("Miasto:");
        cityLabel.setFont(font);
        cityLabel.setHorizontalAlignment(SwingConstants.CENTER);
        cityField = new JTextField(10);
        cityField.setFont(font);

        foundationYearLabel = new JLabel("Rok założenia:");
        foundationYearLabel.setFont(font);
        foundationYearLabel.setHorizontalAlignment(SwingConstants.CENTER);
        foundationYearField = new JTextField(10);
        foundationYearField.setFont(font);

        divisionLabel = new JLabel("Dyzwizja:");
        divisionLabel.setFont(font);
        divisionLabel.setHorizontalAlignment(SwingConstants.CENTER);
        String[] divisionStrings = { "Open", "Women", "Mixed"};
        divisionList = new JComboBox(divisionStrings);
        divisionList.setSelectedIndex(0);
        divisionList.setFont(font);
        divisionList.addActionListener(this);

        addPlayerButton = new JButton("Dodaj");
        addPlayerButton.setFont(font);
        addPlayerButton.addActionListener(this);

        panel2 = new JPanel(new FlowLayout());
        panel2.add(addPlayerButton);

        panel1 = new JPanel(new GridLayout(4,2, 10, 10));
        panel1.add(teamNameLabel);
        panel1.add(teamNameField);
        panel1.add(cityLabel);
        panel1.add(cityField);
        panel1.add(foundationYearLabel);
        panel1.add(foundationYearField);
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
        if(object == addPlayerButton) {
            if (teamNameField.getText().equals("") || cityField.getText().equals("") || foundationYearField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            else {
                try {
                    Integer.parseInt(foundationYearField.getText());
                }
                catch(NumberFormatException e) {
                    JOptionPane.showMessageDialog(this, "Podano nieprawidłowy rok!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                JOptionPane.showMessageDialog(this, "Poprawnie dodano drużynę", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                DatabaseApplication.queries(new String[]{"addTeam", teamNameField.getText(), cityField.getText(),
                        foundationYearField.getText(), divisionList.getSelectedItem().toString(), String.valueOf(userId)});
                this.dispose();
            }
        }
    }
}