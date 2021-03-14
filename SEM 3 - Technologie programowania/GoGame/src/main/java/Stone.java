import java.awt.*;
import java.awt.geom.Ellipse2D;

/** Klasa kamien */
class Stone {
	
	/** Numer wiersza */
	final int row;
	/** Numer kolumny */
	final int column;
	/** Kolor kamienia */
	private Player player;
	/** Wspolrzedna x kamienia */
	final double x;
	/** Wspolrzedna y kamienia */
	final double y;
	/** Widoczność kamienia */
	Visibility visibility = Visibility.INVISIBLE;
	/** Kolo */
	Ellipse2D circle;

	/** Konstruktor kamienia */
	Stone(int row, int column, double x, double y) {
		//this.player = player;
		this.row = row;
		this.column = column;
		this.x = x;
		this.y = y;
		circle = new Ellipse2D.Double(x-Board.STONERADIUS, y-Board.STONERADIUS, Board.STONERADIUS*2, Board.STONERADIUS*2);
	}


	/** Przypisuje gracza kamieniowi */
	private void setPlayer(Player player) {
		this.player = player;
		visibility = Visibility.VISIBLE;
    }

    /** Ustaw kolor playera na podstawie stringa */
	void setPlayer(String player) {
		if(player.equals("WHITE"))
			setPlayer(Player.WHITE);
		else if(player.equals("BLACK"))
			setPlayer(Player.BLACK);
	}

    /** Ustawienie koloru kamieniowi */
	Color stoneColor() {
		if (player == Player.BLACK) return Color.BLACK;
		if (player == Player.WHITE) return Color.WHITE;
		if (visibility == Visibility.HALFVISIBLE) return Color.DARK_GRAY;
		else return null;
	}

    /** Sprawdza czy wspolrzedne sa wewnatrz jakiegos kamienia
     * @param x wspolrzedna x myszy
     * @param y wspolrzedna y myszy
     */
	boolean isInsideStone(double x, double y) {
		return ((x - this.x) * (x - this.x)) + ((y - this.y) * (y - this.y)) <= Board.STONERADIUS * Board.STONERADIUS;
	}

	Player getPlayer() {
		return player;
	}

	/** Ustaw widocznosc */
	void setVisibility(Visibility visibility) {
		this.visibility = visibility;
		if(visibility == Visibility.INVISIBLE) player = null;
	}
}
