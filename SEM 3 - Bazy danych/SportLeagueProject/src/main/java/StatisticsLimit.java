import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class StatisticsLimit extends JFrame implements ActionListener {
    private JTextField limitFiled;
    private JLabel menuLabel;
    private JPanel menuPanel;
    private JPanel buttonPanel;
    private JButton menuButton;

    StatisticsLimit () {
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
        setTitle("Ultimate Frisbee League");

        // Ustawia okno blizej srodka
        Dimension windowSize = getSize();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Point centerPoint = ge.getCenterPoint();
        int dx = (centerPoint.x - windowSize.width) / 2;
        int dy = (centerPoint.y - windowSize.height) / 2;
        setLocation(dx, dy);

        Font font = new Font ("Segoe UI", Font.PLAIN, 20);



        menuLabel = new JLabel("Wpisz ilu TOP zawodników chcesz wyświetlić");
        menuButton = new JButton("Wyświetl");
        limitFiled = new JTextField(3);

        menuPanel = new JPanel(new GridLayout(3, 1));
        menuLabel.setHorizontalAlignment(SwingConstants.CENTER);
        menuLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        menuLabel.setFont(font);
        limitFiled.setFont(font);
        menuButton.setFont(font);
        menuButton.setHorizontalAlignment(SwingConstants.CENTER);
        menuPanel.add(menuLabel);
        JPanel limitFiledPane = new JPanel();
        limitFiledPane.add(limitFiled);
        menuPanel.add(limitFiledPane);
        buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(menuButton);
        menuPanel.add(buttonPanel);

        add(menuPanel);

        limitFiled.addActionListener(this);
        menuButton.addActionListener(this);

        pack();
    }
    public void actionPerformed(ActionEvent e) {
        Object source = e.getSource();
        if (source == menuButton) {
            try {
                String limitNumber = limitFiled.getText();
                int limitNumInt = Integer.parseInt(limitNumber);
                String[] text = new String[2];
                text[0] = "Statystyki";
                text[1] = limitNumber;
                new ButtonList(text);
                this.dispose();
            }
            catch (NumberFormatException exeption){
                JOptionPane.showMessageDialog(null, "Wprowadź liczbę", "Brak", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
