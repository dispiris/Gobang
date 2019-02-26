import java.awt.Color;
import java.awt.Graphics;

public class GamePiece {
	
	public static final Direction N = Direction.N;
	public static final Direction S = Direction.S;
	public static final Direction W = Direction.W;
	public static final Direction E = Direction.E;
	public static final Direction NW = Direction.NW;
	public static final Direction NE = Direction.NE;
	public static final Direction SW = Direction.SW;
	public static final Direction SE = Direction.SE;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final PieceColor BLACK = PieceColor.BLACK;
	
	public static final int MARGIN = 100;	// 100 pixels for margin
	public static final int WIDTH = 60; 	// 100 pixels wide between lines
	public static final int RADIUS = 20;	// 20 pixels for piece radius
	
	public PieceColor color;	// the color of this piece
	public int x;			// the x - position
	public int y;			// the y - position
	
	public static boolean whiteWins = false; 
	public static boolean blackWins = false;
	
	// constructs a new piece with the given x, y - positions and type/color
	public GamePiece(PieceColor color, int x, int y) {
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	// returns if the color of this piece wins the game and updates the static states
	public boolean wins(GameBoard board) {
		boolean result = false;
		
		try {
			result = (hasThree(board, N) && hasThree(board, S));
		} catch (ArrayIndexOutOfBoundsException ex) {}
		
		try {
			if (result == false) {	
				result = (hasThree(board, W) && hasThree(board, E));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {}
					
		try {
			if (result == false) {	
				result = (hasThree(board, NW) && hasThree(board, SE));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {}
		
		try {
			if (result == false) {	
				result = (hasThree(board, NE) && hasThree(board, SW));
			}
		} catch (ArrayIndexOutOfBoundsException ex) {}
		
		if (result == true && color == BLACK) {
			blackWins = true;
		} else if (result == true && color == WHITE) {
			whiteWins = true;
		}
		
		return result;
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
	
	// returns true if the board has three pieces of the same color in a row in the given direction
	private boolean hasThree(GameBoard board, Direction dir) {
		try {
			switch (dir) {
				case N: return (board.getPiece(x, y - 1).color == color && board.getPiece(x, y - 2).color == color);
				case W: return (board.getPiece(x - 1, y).color == color && board.getPiece(x - 2, y).color == color);
				case NW: return (board.getPiece(x - 1, y - 1).color == color && board.getPiece(x - 2, y - 2).color == color);
				case SW: return (board.getPiece(x - 1, y + 1).color == color && board.getPiece(x - 2, y + 2).color == color);
				case S: return (board.getPiece(x, y + 1).color == color && board.getPiece(x, y + 2).color == color);
				case E: return (board.getPiece(x + 1, y).color == color && board.getPiece(x + 2, y).color == color);
				case NE: return (board.getPiece(x + 1, y - 1).color == color && board.getPiece(x + 2, y - 2).color == color);
				case SE: return (board.getPiece(x + 1, y + 1).color == color && board.getPiece(x + 2, y + 2).color == color);
				default: throw new IllegalArgumentException();	
			}
		} catch (NullPointerException ex) {
			return false;
		}
	}
}
