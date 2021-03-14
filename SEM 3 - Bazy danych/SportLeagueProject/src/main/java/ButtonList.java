import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

public class ButtonList extends JFrame implements ActionListener {
    String[] type;
    JPanel buttonPane;
    JPanel headerPane;
    JPanel[] linePane;
    JScrollPane scrollPane;
    JButton[] buttonTable;
    JLabel[][] teamLabelTable;
    List buttonPaneContentList;
    ArrayList<List> labelContentList = new ArrayList<List>();

    private JLabel newLabel(String labelText) {
        Font font = new Font ("Segoe UI", Font.BOLD, 16);
        JLabel label = new JLabel(labelText);
        label.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        label.setFont(font);
        label.setSize(150, 10);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
    }

    public ButtonList(String[] type) {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.type = type;

        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);

        setVisible(true);
        setTitle("Ultimate Frisbee League");
        Font font = new Font ("Segoe UI", Font.PLAIN, 16);

        buttonPane = new JPanel();
        headerPane = new JPanel();

        if (type[0] == "Drużyny") {
            headerPane.setLayout(new GridLayout(1, 8));
            headerPane.add(newLabel("Drużyna"));
            buttonPaneContentList = DatabaseApplication.queries(new String[] {"getTeams"});
            headerPane.add(newLabel("Miasto"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamCity"}));
            headerPane.add(newLabel("Rok założenia"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamFoundationYear"}));
            headerPane.add(newLabel("Dywizja"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamDivision"}));
            headerPane.add(newLabel("Wygrane"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamWins"}));
            headerPane.add(newLabel("Remisy"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamDraws"}));
            headerPane.add(newLabel("Porażki"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamLosts"}));
            headerPane.add(newLabel("Zdobyte punkty"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"teamPoints"}));
        }
        else if (type[0] == "Turnieje") {
            headerPane.setLayout(new GridLayout(1, 4));
            headerPane.add(newLabel("Turniej"));
            buttonPaneContentList = DatabaseApplication.queries(new String[] {"getTournaments"});
            headerPane.add(newLabel("Data"));
            labelContentList.add(DatabaseApplication.queries(new String[] {"tournamentDate"}));
            headerPane.add(newLabel("Miasto"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"tournamentLocation"}));
            headerPane.add(newLabel("Dywizja"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"tournamentDivision"}));
        }
        else if (type[0] == "Statystyki") {
            String limit = type[1];
            headerPane.setLayout(new GridLayout(1, 7));
            headerPane.add(newLabel("Imię"));
            labelContentList.add(DatabaseApplication.queries(new String[] {"playerName", limit, "1"}));
            headerPane.add(newLabel("Nazwisko"));
            labelContentList.add(DatabaseApplication.queries(new String[] {"playerSurname", limit, "1"}));
            headerPane.add(newLabel("Drużyna"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerTeam", limit, "1"}));
            headerPane.add(newLabel("Numer zawodnika"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerNumber", limit, "1"}));
            headerPane.add(newLabel("Płeć"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerSex", limit, "1"}));
            headerPane.add(newLabel("Rok urodzenia"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerBirth", limit, "1"}));
            headerPane.add(newLabel("Zdobyte punkty"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerScoredPoints", limit, "1"}));
        }
        else if (type[0] == "Zawodnicy") {
            String druzyna = DatabaseApplication.queries(new String[] {"getTeamId", type[1]}).get(0).toString();
            headerPane.setLayout(new GridLayout(1, 6));
            headerPane.add(newLabel("Imię"));
            labelContentList.add(DatabaseApplication.queries(new String[] {"playerName", druzyna, "2"}));
            headerPane.add(newLabel("Nazwisko"));
            labelContentList.add(DatabaseApplication.queries(new String[] {"playerSurname", druzyna, "2"}));
            headerPane.add(newLabel("Numer zawodnika"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerNumber", druzyna, "2"}));
            headerPane.add(newLabel("Płeć"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerSex", druzyna, "2"}));
            headerPane.add(newLabel("Rok urodzenia"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerBirth", druzyna, "2"}));
            headerPane.add(newLabel("Zdobyte punkty"));
            labelContentList.add(DatabaseApplication.queries(new String[]{"playerScoredPoints", druzyna, "2"}));
        }

        int listSize;
        if (type[0] != "Statystyki" && type[0] != "Zawodnicy") {
            listSize = buttonPaneContentList.size();
            buttonTable = new JButton[listSize];
        }
        else {
            listSize = (labelContentList.get(0)).size();
        }
        int wid, hei;
        wid = labelContentList.size();
        if (type[0] != "Statystyki" && type[0] != "Zawodnicy") wid++;
        hei = listSize+1;

        linePane = new JPanel[hei];
        buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.Y_AXIS));
        teamLabelTable = new JLabel[listSize][labelContentList.size()];

        for (int i=0; i<listSize; i++) {
            linePane[i] = new JPanel();
            linePane[i].setLayout(new GridLayout(1, wid));
            if (!type[0].equals("Statystyki") && type[0] != "Zawodnicy") {
                String text = buttonPaneContentList.get(i).toString();
                buttonTable[i] = new JButton(text);
                linePane[i].add(buttonTable[i]);
                buttonTable[i].addActionListener(this);
                buttonTable[i].setBorder(BorderFactory.createEmptyBorder(0, 2, 0, 2));
                buttonTable[i].setFont(font);
                buttonTable[i].setSize(150, 15);
            }
            for (int j=0; j<labelContentList.size(); j++) {
                String labelText = labelContentList.get(j).get(i).toString();
                teamLabelTable[i][j] = new JLabel(labelText);
                teamLabelTable[i][j].setFont(font);
                teamLabelTable[i][j].setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
                teamLabelTable[i][j].setSize(150, 15);
                teamLabelTable[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                teamLabelTable[i][j].setVerticalAlignment(SwingConstants.CENTER);
                linePane[i].add(teamLabelTable[i][j]);
            }
            buttonPane.add(linePane[i]);
        }

        scrollPane = new JScrollPane(buttonPane,
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
        JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setLayout(new BorderLayout());
        add(headerPane, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        //this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        pack();
        this.setResizable(false);
    }

    public void actionPerformed(ActionEvent e) {
        Object object = e.getSource();
        for (JButton button: buttonTable) {
            if (button == object)  {
                if (type[0] == "Drużyny") {
                    String[] text = new String[2];
                    text[0] = "Zawodnicy";
                    System.out.println(button.getText());
                    text[1] = button.getText();
                    new ButtonList(text);
                }
                if (type[0] == "Turnieje") {
                    int tournament = Integer.parseInt(DatabaseApplication.queries(new String[] {"getTournamentId", button.getText()}).get(0).toString());
                    new TournamentsMatches(tournament,0, true);
                }
            }
        }
        this.dispose();
    }
}
