import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

class Requests extends JFrame implements ActionListener {

    /** Tabela przyciskow akceptujacych uzytkownikow */
    private JButton[] confirmButton;
    /** Tabela przyciskow odrzucajacych uzytkownikow */
    private JButton[] declineButton;
    /** Lista loginow */
    private List requestsLogins;

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

    /** Konstruktor okna */
    Requests() {
        setTitle("Prośby o utworzenia konta");
        setLayout(new BorderLayout());
        requestsLogins = DatabaseApplication.queries(new String[]{"requestsLogins"});
        JPanel panel = new JPanel(new GridLayout(requestsLogins.size(),7,20,10));
        JScrollPane scrollBar = new JScrollPane(panel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollBar, BorderLayout.CENTER);

        List requestsFirstNames = DatabaseApplication.queries(new String[]{"requestsFirstNames"});
        List requestsNames = DatabaseApplication.queries(new String[]{"requestsNames"});
        List requestsPesels = DatabaseApplication.queries(new String[]{"requestsPesels"});
        List requestsPermissions1 = DatabaseApplication.queries(new String[]{"requestsPermissions"});

        ArrayList<String> requestsPermissions = new ArrayList<String>();
        for (Object o : requestsPermissions1) {
            if (String.valueOf(o).equals("2")) {
                requestsPermissions.add("Kapitan drużyny");
            } else if (String.valueOf(o).equals("3")) {
                requestsPermissions.add("Organizator turnieju");
            } else {
                requestsPermissions.add("Inny");
            }
        }
        confirmButton = new JButton[requestsLogins.size()];
        declineButton = new JButton[requestsLogins.size()];
        for(int i = 0; i < requestsLogins.size(); i++) {
            confirmButton[i] = new JButton("Zaakceptuj");
            confirmButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            confirmButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            confirmButton[i].addActionListener(this);
        }
        for(int i = 0; i < requestsLogins.size(); i++) {
            declineButton[i] = new JButton("Odrzuć");
            declineButton[i].setFont(new Font("Segoe UI", Font.PLAIN, 20));
            declineButton[i].setHorizontalAlignment(SwingConstants.CENTER);
            declineButton[i].addActionListener(this);
        }

        JComponent[][] data = new JComponent[requestsLogins.size()+1][7];
        String[] columnNames = {"Login", "Imię", "Nazwisko", "PESEL", "Poziom Uprawnień", "Zaakceptuj", "Odrzuć"};
        JPanel headerPanel = new JPanel(new GridLayout(1,7, 0, 10));
        for(int i = 0; i < 7; i++) {
            data[0][i] = labelInitializor(columnNames[i]);
            data[0][i].setFont(new Font("Segoe UI", Font.BOLD, 23));
            headerPanel.add(data[0][i]);
        }
        add(headerPanel, BorderLayout.NORTH);
        for(int i = 1; i < requestsLogins.size() + 1; i++) {
            data[i][0] = labelInitializor(String.valueOf(requestsLogins.get(i-1)));
            data[i][1] = labelInitializor(String.valueOf(requestsFirstNames.get(i-1)));
            data[i][2] = labelInitializor(String.valueOf(requestsNames.get(i-1)));
            data[i][3] = labelInitializor(String.valueOf(requestsPesels.get(i-1)));
            data[i][4] = labelInitializor(String.valueOf(requestsPermissions.get(i-1)));
            data[i][5] = confirmButton[i-1];
            data[i][6] = declineButton[i-1];
        }

        for (int i = 1; i < requestsLogins.size() + 1; i++) {
            for (int j = 0; j < 7; j++) {
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
        for(int i = 0; i < confirmButton.length; i++) {
            if(event == confirmButton[i]) {
                DatabaseApplication.queries(new String[]{"confirmUser", String.valueOf(requestsLogins.get(i))});
                declineButton[i].setEnabled(false);
                confirmButton[i].setEnabled(false);
            }
            else if(event == declineButton[i]) {
                DatabaseApplication.queries(new String[]{"declineUser", String.valueOf(requestsLogins.get(i))});
                declineButton[i].setEnabled(false);
                confirmButton[i].setEnabled(false);
            }
        }
    }
}
