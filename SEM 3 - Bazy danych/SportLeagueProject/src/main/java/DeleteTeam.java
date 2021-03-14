import TablesClasses.Team;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/** Okno usuń druzyne */
class DeleteTeam extends JFrame implements ActionListener {

    /** Combobox z drużynami */
    private JComboBox teamListCombobox;
    /** Przycisk wykonujący usuniecie */
    private JButton deleteButton;
    /** Id uzytkownika ktory otwiera to okno */
    private int userId;

    /** Kostrukor okna
     * @param userId Id uzytkownika ktory otwiera to okno
     */
    DeleteTeam(int userId) {
        this.userId = userId;

        setLayout(new FlowLayout(1, 30,15));
        setTitle("Usuwanie drużyny");
        Font font = new Font("Segoe UI", Font.PLAIN, 20);
        JLabel teamNameLabel = new JLabel("Twoje drużyny");
        teamNameLabel.setFont(font);
        List teamList;
        if(Integer.parseInt(String.valueOf(DatabaseApplication.queries(new String [] {"userPermissionLevel", String.valueOf(userId)}).get(0))) == 1) {
            teamList = DatabaseApplication.queries(new String [] {"allTeams"});
        }
        else {
            teamList = DatabaseApplication.queries(new String [] {"myAllTeams", String.valueOf(userId)});
        }
        List listOfNames = new ArrayList();
        for(Object o: teamList) {
            listOfNames.add(((Team) o).getTeamName());
        }
        teamListCombobox = new JComboBox(listOfNames.toArray());
        teamListCombobox.setSelectedIndex(0);
        teamListCombobox.setFont(font);
        teamListCombobox.addActionListener(this);

        deleteButton = new JButton("Usuń");
        deleteButton.setFont(font);
        deleteButton.addActionListener(this);

        add(teamNameLabel);
        add(teamListCombobox);
        add(deleteButton);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setResizable(false);
        setVisible(true);
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);
        pack();
    }

    public void actionPerformed(ActionEvent actionEvent) {
        Object event = actionEvent.getSource();
        if(event == deleteButton) {
            JPasswordField pf = new JPasswordField();
            JPanel panel = new JPanel(new GridLayout(4,1,10,10));
            JLabel label1 = new JLabel("Czy na pewno chcesz usunąć drużynę?");
            JLabel label2 = new JLabel("Usunięcie jej spowoduje usunięcie również wszystkich zawodników i historii");
            JLabel label3 = new JLabel("Aby potwierdzić wpisz hasło");
            panel.add(label1);
            panel.add(label2);
            panel.add(label3);
            panel.add(pf);
            int val = JOptionPane.showConfirmDialog(null, panel, "Potwierdź hasłem", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

            if(val == JOptionPane.OK_OPTION) {
                String password = new String(pf.getPassword());
                if(String.valueOf(DatabaseApplication.queries(new String[]{"checkPassword", password, String.valueOf(userId)}).get(0)).equals("1")) {
                    DatabaseApplication.queries(new String[]{"deleteTeam", String.valueOf(DatabaseApplication.queries(new String[]{"teamIdByName", String.valueOf(teamListCombobox.getSelectedItem())}).get(0))});
                    JOptionPane.showMessageDialog(this, "Drużyna została poprawnie usunięta", "Sukces", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    JOptionPane.showMessageDialog(null, "Błędne hasło", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
