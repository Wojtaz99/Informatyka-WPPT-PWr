import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/** Klasa okna rejestracji */
 class SignUp extends JFrame implements ActionListener {
    /** Panel glowny */
    private JPanel panel;
    /** Panel pomocniczy na logowanie */
    private JPanel panel1;
    /** Etykieta naglowka */
    private JLabel headerLabel;
    /** Etykieta hasła */
    private JLabel passwordLabel;
    /** Etykieta loginu */
    private JLabel loginLabel;
    /** Pole na haslo */
    private JPasswordField passwordField;
    /** Pole na login */
    private JTextField loginField;
    /** Przycisk rejestrujacy */
    private JButton signUpButton;
    /** Etykieta pesel */
    private JLabel peselLabel;
    /** Pole na pesel */
    private JTextField peselField;
    /** Etykieta nazwisko */
    private JLabel nameLabel;
    /** Pole na nazwisko */
    private JTextField nameField;
    /** Etykieta imie */
    private JLabel firstNameLabel;
    /** Pole na imie */
    private JTextField firstNameField;
    /** Panel na label i guzik */
    private JPanel panel2;
    /** Combobox z uprawnieniami */
    private JComboBox permissionList;

    /** Konstruktor okna */
     SignUp() {
        super("Rejestracja");
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        panel = new JPanel(new BorderLayout(10,10));

        headerLabel = new JLabel("Rejestracja");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 23));
        headerLabel.setHorizontalAlignment(SwingConstants.CENTER);

        loginLabel = new JLabel("Login:");
        loginLabel.setFont(font);
        loginLabel.setHorizontalAlignment(SwingConstants.CENTER);
        loginField = new JTextField(10);
        loginField.setFont(font);

        passwordLabel = new JLabel("Hasło:");
        passwordLabel.setFont(font);
        passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passwordField = new JPasswordField(10);
        passwordField.setFont(font);

        firstNameLabel = new JLabel("Imię:");
        firstNameLabel.setFont(font);
        firstNameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        firstNameField = new JTextField(10);
        firstNameField.setFont(font);

        nameLabel = new JLabel("Nazwisko:");
        nameLabel.setFont(font);
        nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
        nameField = new JTextField(10);
        nameField.setFont(font);

        peselLabel = new JLabel("PESEL:");
        peselLabel.setFont(font);
        peselLabel.setHorizontalAlignment(SwingConstants.CENTER);
        peselField = new JTextField(10);
        peselField.setFont(font);

        String[] permissionStrings = {"Kapitan drużyny", "Organizator turnieju"};
        permissionList = new JComboBox(permissionStrings);
        permissionList.setSelectedIndex(0);
        permissionList.setFont(font);
        permissionList.addActionListener(this);
        JLabel permissionLabel = new JLabel("Rodzaj konta:");
        permissionLabel.setFont(font);
        permissionLabel.setHorizontalAlignment(SwingConstants.CENTER);

        signUpButton = new JButton("Zarejestruj");
        signUpButton.setFont(font);

        panel2 = new JPanel(new FlowLayout());
        panel2.add(signUpButton);

        panel1 = new JPanel(new GridLayout(6,2, 10, 10));
        panel1.add(loginLabel);
        panel1.add(loginField);
        panel1.add(passwordLabel);
        panel1.add(passwordField);
        panel1.add(firstNameLabel);
        panel1.add(firstNameField);
        panel1.add(nameLabel);
        panel1.add(nameField);
        panel1.add(peselLabel);
        panel1.add(peselField);
        panel1.add(permissionLabel);
        panel1.add(permissionList);

        panel.add(headerLabel, BorderLayout.NORTH);
        panel.add(panel1, BorderLayout.CENTER);
        panel.add(panel2, BorderLayout.SOUTH);
        add(panel);


        setVisible(true);
        setResizable(false);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);

        pack();

        signUpButton.addActionListener(this);
    }

    /** ActionPerformed
     * @param actionEvent Wydarzenie
     */
    public void actionPerformed(ActionEvent actionEvent) {
        Object object = actionEvent.getSource();
        if(object == signUpButton) {
            if(loginField.getText().equals("") || String.valueOf(passwordField.getPassword()).equals("") || firstNameField.getText().equals("") || nameField.getText().equals("") || peselField.getText().equals("")) {
                JOptionPane.showMessageDialog(this, "Wszystkie pola muszą być wypełnione!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
            else {
                int permissionLvl = 4;
                if(permissionList.getSelectedItem().toString().equals("Kapitan drużyny")) {
                    permissionLvl = 2;
                }
                else if(permissionList.getSelectedItem().toString().equals("Organizator turnieju")) {
                    permissionLvl = 3;
                }
                if(DatabaseApplication.queries(new String[]{"addUser", loginField.getText(), String.valueOf(passwordField.getPassword()),
                        firstNameField.getText(), nameField.getText(), peselField.getText(), String.valueOf(permissionLvl)}).size() > 0) {
                    JOptionPane.showMessageDialog(this, "Użytkownik o takim loginie już istnieje", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(this, "Rejestracja w trakcie weryfikacji", "Success", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose();
                }
            }
        }
    }
}
