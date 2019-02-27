import java.awt.Color;
import java.awt.Graphics;

public class GamePiece {
	
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final PieceColor BLACK = PieceColor.BLACK;
	
	public static final int MARGIN = 100;	// 100 pixels for margin
	public static final int WIDTH = 60; 	// 60 pixels wide between lines
	public static final int RADIUS = 20;	// 20 pixels for piece radius
	
	public PieceColor color;	// the color of this piece
	public int x;			// the x - position
	public int y;			// the y - position
	
	// constructs a new piece with the given x, y - positions and type/color
	public GamePiece(PieceColor color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	public void getImage(Graphics g) {
		if (color == BLACK) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}
		
		int x = MARGIN + this.x * WIDTH;
		int y = MARGIN + this.y * WIDTH;
		g.fillOval(x - RADIUS, y - RADIUS, RADIUS * 2, RADIUS * 2);
	}
	
	public void removeImage(Graphics g) {
		int x = MARGIN + this.x * WIDTH - WIDTH / 2;
		int y = MARGIN + this.y * WIDTH - WIDTH / 2;
		g.setColor(GameBoard.BACKGROUND_COLOR);
		g.fillRect(x, y, WIDTH, WIDTH);
		g.setColor(Color.BLACK);
		g.drawLine(x + WIDTH / 2, y, x + WIDTH / 2, y + WIDTH);
		g.drawLine(x, y + WIDTH / 2, x + WIDTH, y + WIDTH / 2);
	}
	
	public boolean hasFive(GameBoard board) {
		boolean result = false;
		if (!result) {
			result = sameColor(board, x, y - 1) && sameColor(board, x, y - 2) && sameColor(board, x, y - 3) && sameColor(board, x, y - 4);
		}
		if (!result) {
			result = sameColor(board, x, y + 1) && sameColor(board, x, y + 2) && sameColor(board, x, y + 3) && sameColor(board, x, y + 4);
		}
		if (!result) {
			result = sameColor(board, x - 1, y) && sameColor(board, x - 2, y) && sameColor(board, x - 3, y) && sameColor(board, x - 4, y);
		}
		if (!result) {
			result = sameColor(board, x + 1, y) && sameColor(board, x + 2, y) && sameColor(board, x + 3, y) && sameColor(board, x + 4, y);
		}
		if (!result) {
			result = sameColor(board, x - 1, y - 1) && sameColor(board, x - 2, y - 2) && sameColor(board, x - 3, y - 3) && sameColor(board, x - 4, y - 4);
		}
		if (!result) {
			result = sameColor(board, x + 1, y + 1) && sameColor(board, x + 2, y + 2) && sameColor(board, x + 3, y + 3) && sameColor(board, x + 4, y + 4);
		}
		if (!result) {
			result = sameColor(board, x - 1, y + 1) && sameColor(board, x - 2, y + 2) && sameColor(board, x - 3, y + 3) && sameColor(board, x - 4, y + 4);
		}
		if (!result) {
			result = sameColor(board, x + 1, y - 1) && sameColor(board, x + 2, y - 2) && sameColor(board, x + 3, y - 3) && sameColor(board, x + 4, y - 4);
		}
		return result;
	}
	
	private boolean sameColor(GameBoard board, int x, int y) {
		try {
			return board.getPiece(x, y) != null && board.getPiece(x, y).color == color;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}
}
