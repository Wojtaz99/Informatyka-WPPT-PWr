import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/** Klasa plansza */
public class Board extends JPanel{

    /** Ilosc pol nq planszy w pionie/poziomie */
	private static int SIZE;
	/** Promien kamieni */
	static int STONERADIUS=15;
	/** Rozmiar planszy w pixelach */
	private final double DIMX = 600, DIMY = 600;
	private final int MARGIN;
	/** Tablica z kamieniami na planszy */
	private static Stone[][] boardTab;
	/** Ostatni tymczasowy kamień */
	private Stone lastMovedField;
	private GUI gui;

	/** Zwroc tablice ze stanem planszy
	 * @return stan planszy
	 */
	Stone[][] getBoardTab() {
        return boardTab;
    }

	/** Konstruktor planszy */
	Board(GUI gui, int size) {
		MARGIN = 4*STONERADIUS;
	    switch (size) {
            case 9:
                STONERADIUS = 25;
                break;
            case 13:
                STONERADIUS = 20;
                break;
            case 19:
                STONERADIUS = 15;
                break;
        }
        this.gui = gui;
		SIZE = size;
		setBackground(new Color(193,154,107));
		this.setPreferredSize(new Dimension((int)DIMX+2*MARGIN, (int)DIMY+2*MARGIN));
		boardTab = new Stone [SIZE][SIZE];
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				boardTab[i][j] = new Stone(i,j,(i*DIMX/(SIZE-1))+MARGIN,(j*DIMY/(SIZE-1))+MARGIN);
			}
		}
		repaint();
	}

	/** Zwroc rozmiar planszy
	 * @return rozmiar palnszy
	 */
	int getSIZE () {
	    return SIZE;
    }

	/* public void clickedOnStone(double x, double y) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].isInsideStone(x,y)) {
				    boardTab[i][j].setPlayer(Player.BLACK);
				}
			}
		}
		repaint();
	} */

	/** Podświetlanie najechanych pól
     * @param event ruch myszki
     * */
	void enteredStone(MouseEvent event) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].circle.contains(event.getPoint())) {
					if ((gui.checkIfIsYourTurn()) && (boardTab[i][j].visibility == Visibility.INVISIBLE) && (!gui.checkIfGameIsFinished())) {
						lastMovedField = boardTab[i][j];
						boardTab[i][j].visibility= Visibility.HALFVISIBLE;
					}
				}
			}
		}
		repaint();
	}

	/** Podswietlanie kamieni podczas ruszania mysza
	 *
     * @param event ruch myszki
     * */
	void draggedStone(MouseEvent event) {
		for (int i=0; i<SIZE; i++) {
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].circle.contains(event.getPoint())) {
				    if(lastMovedField != null) {
						if ((gui.checkIfIsYourTurn()) && (!gui.checkIfGameIsFinished()) && boardTab[i][j].visibility == Visibility.INVISIBLE) {
							lastMovedField.visibility = Visibility.INVISIBLE;
							lastMovedField = boardTab[i][j];
							lastMovedField.visibility = Visibility.HALFVISIBLE;
						}
                    }
				}

			}
		}
		repaint();
	}

    /** Utworzenie kamienia przy puszczeniu myszki */
	String releasedStone() {
	    if (lastMovedField != null) {
	    	lastMovedField.visibility = Visibility.INVISIBLE;
            String line = lastMovedField.row + " " + lastMovedField.column;
            lastMovedField = null;
            return line;
        }
	    return null;
	}

	/** Rysowanie */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		//wydrukowanie tła
		Image bgImage = new ImageIcon("src/main/resources/wood.jpg").getImage();
		g.drawImage(bgImage, 0, 0, null);

		for (int i=0; i<SIZE; i++) { // pionowe linie
				Point2D x = new Point2D.Double((DIMX / (SIZE-1)) * i + MARGIN, MARGIN);
				Point2D y = new Point2D.Double((DIMY / (SIZE-1)) * i + MARGIN, DIMY+MARGIN);
				Line2D line = new Line2D.Double(x,y);
				g2d.setPaint(Color.DARK_GRAY);
				g2d.setStroke(new BasicStroke(3));
				g2d.draw(line);
		}
		for (int i=0; i<SIZE; i++) { // poziome linie
			Point2D x = new Point2D.Double(MARGIN, (DIMY / (SIZE-1)) * i + MARGIN);
			Point2D y = new Point2D.Double(DIMX + MARGIN, (DIMX / (SIZE-1)) * i + MARGIN);
			Line2D line = new Line2D.Double(x,y);
			g2d.setPaint(Color.DARK_GRAY);
			g2d.setStroke(new BasicStroke(3));
			g2d.draw(line);
		}
		if (SIZE == 13 || SIZE == 9) {
			for (int i = 0; i < 3; i++) { //rysowanie punktów strategicznych
				for (int j = 0; j < 3; j++) {
					Ellipse2D circle = new Ellipse2D.Double(DIMX / 4 * (i + 1) - 5 + MARGIN, DIMY / 4 * (j + 1) - 5 + MARGIN, 10, 10);
					g2d.setPaint(Color.DARK_GRAY);
					g2d.draw(circle);
					g2d.fill(circle);
				}
			}
		}
		else if (SIZE == 19) {
			for (int i = 0; i < 5; i+=2) { //rysowanie punktów strategicznych
				for (int j = 0; j < 5; j+=2) {
					Ellipse2D circle = new Ellipse2D.Double(DIMX / 6 * (i + 1) - 5 + MARGIN, DIMY / 6 * (j + 1) - 5 + MARGIN, 10, 10);
					g2d.setPaint(Color.DARK_GRAY);
					g2d.draw(circle);
					g2d.fill(circle);
				}
			}
		}
		for (int i=0; i<SIZE; i++) { //rysowanie kamieni
			for (int j=0; j<SIZE; j++) {
				if (boardTab[i][j].visibility == Visibility.VISIBLE) {
					g2d.setPaint(boardTab[i][j].stoneColor());
					g2d.draw(boardTab[i][j].circle);
					g2d.fill(boardTab[i][j].circle);
				}

				//wyświetlanie najchanych pól
				if (boardTab[i][j].visibility == Visibility.HALFVISIBLE) {
					Ellipse2D circle = new Ellipse2D.Double(boardTab[i][j].x-STONERADIUS, boardTab[i][j].y-STONERADIUS, STONERADIUS*2, STONERADIUS*2);
					g2d.setPaint(boardTab[i][j].stoneColor());
					/*float alpha = i * 0.5f; //półprzeźroczyste
					AlphaComposite alcom = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha);
					g2d.setComposite(alcom); */
					g2d.draw(circle);
					g2d.fill(circle);
				}
			}
		}
	}
}
