package components;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import utility.Images;
import utility.PieceColor;

public class GamePiece {
	
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final PieceColor BLACK = PieceColor.BLACK;
	
	public static final int MARGIN = 100;	// 100 pixels for margin
	public static final int WIDTH = 60; 	// 60 pixels wide between lines
	public static final int RADIUS = 20;	// 20 pixels for piece radius
	
	public PieceColor color;	// the color of this piece
	public int x;			// the x - position
	public int y;			// the y - position
	private int stepNum;
	
	public static final BufferedImage BLACK_IMAGE;
	public static final BufferedImage WHITE_IMAGE;
	
	static {
		BLACK_IMAGE = new BufferedImage(RADIUS * 2, RADIUS * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = BLACK_IMAGE.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2.setColor(new Color(0,0,0,0));
		g2.fill(new Rectangle2D.Double(0, 0, RADIUS * 2, RADIUS * 2));
		g2.setColor(Color.BLACK);
		g2.fillOval(0, 0, RADIUS * 2, RADIUS * 2);
		g2.dispose();
		
		WHITE_IMAGE = new BufferedImage(RADIUS * 2, RADIUS * 2, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g21 = WHITE_IMAGE.createGraphics();
		g21.setComposite(AlphaComposite.Src);
		g21.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g21.setColor(new Color(0,0,0,0));
		g21.fill(new Rectangle2D.Double(0, 0, RADIUS * 2, RADIUS * 2));
		g21.setColor(Color.WHITE);
		g21.fillOval(0, 0, RADIUS * 2, RADIUS * 2);
		g21.dispose();
	}
	
	
	// constructs a new piece with the given x and y positions and color
	public GamePiece(PieceColor color, int x, int y, int stepNum) {
		this.stepNum = stepNum;
		this.color = color;
		this.x = x;
		this.y = y;
	}
	
	public PieceColor getColor() {
		return color;
	}
	
	public void getImage(Graphics g) {
		int x = MARGIN + this.x * WIDTH - RADIUS;
		int y = MARGIN + this.y * WIDTH - RADIUS;
		if (color == BLACK) 
			g.drawImage(BLACK_IMAGE, x, y, null);
		else 
			g.drawImage(WHITE_IMAGE, x, y, null);
	}
	
	public void showStepNum(Graphics g) {
		Graphics2D g2d = (Graphics2D)g;
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (color == BLACK)
			g2d.setColor(Color.WHITE);
		else
			g2d.setColor(Color.BLACK);
		
		int x = MARGIN + this.x * WIDTH - 7;
		int y = MARGIN + this.y * WIDTH + 10;
		
		if (stepNum >= 10) {
			x -= 8;
		}
		g2d.setFont(new Font("Arial", Font.BOLD, 28));
		g2d.drawString("" + stepNum, x, y);
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
	
	boolean hasFive(GamePiece[][] board) {
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
	
	boolean hasFourAlive(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = isEmpty(board, x, y + 1) && sameColor(board, x, y - 1) && sameColor(board, x, y - 2) && sameColor(board, x, y - 3) && isEmpty(board, x, y - 4);
		if (!result) 
			result = isEmpty(board, x, y - 1) && sameColor(board, x, y + 1) && sameColor(board, x, y + 2) && sameColor(board, x, y + 3) && isEmpty(board, x, y + 4);
		if (!result) 
			result = isEmpty(board, x + 1, y) && sameColor(board, x - 1, y) && sameColor(board, x - 2, y) && sameColor(board, x - 3, y) && isEmpty(board, x - 4, y);
		if (!result) 
			result = isEmpty(board, x - 1, y) && sameColor(board, x + 1, y) && sameColor(board, x + 2, y) && sameColor(board, x + 3, y) && isEmpty(board, x + 4, y);
		if (!result) 
			result = isEmpty(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && sameColor(board, x - 2, y - 2) && sameColor(board, x - 3, y - 3) && isEmpty(board, x - 4, y - 4);
		if (!result) 
			result = isEmpty(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && sameColor(board, x + 2, y + 2) && sameColor(board, x + 3, y + 3) && isEmpty(board, x + 4, y + 4);
		if (!result) 
			result = isEmpty(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && sameColor(board, x - 2, y + 2) && sameColor(board, x - 3, y + 3) && isEmpty(board, x - 4, y + 4);
		if (!result) 
			result = isEmpty(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && sameColor(board, x + 2, y - 2) && sameColor(board, x + 3, y - 3) && isEmpty(board, x + 4, y - 4);
		return result;
	}
	
	boolean hasFourDead(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = !sameColor(board, x, y + 1) && sameColor(board, x, y - 1) && sameColor(board, x, y - 2) && sameColor(board, x, y - 3) && isEmpty(board, x, y - 4);
		if (!result) 
			result = !sameColor(board, x, y - 1) && sameColor(board, x, y + 1) && sameColor(board, x, y + 2) && sameColor(board, x, y + 3) && isEmpty(board, x, y + 4);
		if (!result) 
			result = !sameColor(board, x + 1, y) && sameColor(board, x - 1, y) && sameColor(board, x - 2, y) && sameColor(board, x - 3, y) && isEmpty(board, x - 4, y);
		if (!result)
			result = !sameColor(board, x - 1, y) && sameColor(board, x + 1, y) && sameColor(board, x + 2, y) && sameColor(board, x + 3, y) && isEmpty(board, x + 4, y);
		if (!result) 
			result = !sameColor(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && sameColor(board, x - 2, y - 2) && sameColor(board, x - 3, y - 3) && isEmpty(board, x - 4, y - 4);
		if (!result) 
			result = !sameColor(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && sameColor(board, x + 2, y + 2) && sameColor(board, x + 3, y + 3) && isEmpty(board, x + 4, y + 4);
		if (!result) 
			result = !sameColor(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && sameColor(board, x - 2, y + 2) && sameColor(board, x - 3, y + 3) && isEmpty(board, x - 4, y + 4);
		if (!result) 
			result = !sameColor(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && sameColor(board, x + 2, y - 2) && sameColor(board, x + 3, y - 3) && isEmpty(board, x + 4, y - 4);
		return result;
	}
	
	boolean hasThreeAlive(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = isEmpty(board, x, y + 1) && sameColor(board, x, y - 1) && sameColor(board, x, y - 2) && isEmpty(board, x, y - 3);
		if (!result) 
			result = isEmpty(board, x, y - 1) && sameColor(board, x, y + 1) && sameColor(board, x, y + 2) && isEmpty(board, x, y + 3);
		if (!result) 
			result = isEmpty(board, x + 1, y) && sameColor(board, x - 1, y) && sameColor(board, x - 2, y) && isEmpty(board, x - 3, y);
		if (!result) 
			result = isEmpty(board, x - 1, y) && sameColor(board, x + 1, y) && sameColor(board, x + 2, y) && isEmpty(board, x + 3, y);
		if (!result) 
			result = isEmpty(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && sameColor(board, x - 2, y - 2) && isEmpty(board, x - 3, y - 3);
		if (!result) 
			result = isEmpty(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && sameColor(board, x + 2, y + 2) && isEmpty(board, x + 3, y + 3);
		if (!result) 
			result = isEmpty(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && sameColor(board, x - 2, y + 2) && isEmpty(board, x - 3, y + 3);
		if (!result) 
			result = isEmpty(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && sameColor(board, x + 2, y - 2) && isEmpty(board, x + 3, y - 3);
		return result;
	}
	
	boolean hasThreeDead(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = !sameColor(board, x, y + 1) && sameColor(board, x, y - 1) && sameColor(board, x, y - 2) && isEmpty(board, x, y - 3);
		if (!result) 
			result = !sameColor(board, x, y - 1) && sameColor(board, x, y + 1) && sameColor(board, x, y + 2) && isEmpty(board, x, y + 3);
		if (!result) 
			result = !sameColor(board, x + 1, y) && sameColor(board, x - 1, y) && sameColor(board, x - 2, y) && isEmpty(board, x - 3, y);
		if (!result) 
			result = !sameColor(board, x - 1, y) && sameColor(board, x + 1, y) && sameColor(board, x + 2, y) && isEmpty(board, x + 3, y);
		if (!result) 
			result = !sameColor(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && sameColor(board, x - 2, y - 2) && isEmpty(board, x - 3, y - 3);
		if (!result) 
			result = !sameColor(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && sameColor(board, x + 2, y + 2) && isEmpty(board, x + 3, y + 3);
		if (!result) 
			result = !sameColor(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && sameColor(board, x - 2, y + 2) && isEmpty(board, x - 3, y + 3);
		if (!result) 
			result = !sameColor(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && sameColor(board, x + 2, y - 2) && isEmpty(board, x + 3, y - 3);
		return result;
	}
	
	boolean hasTwoAlive(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = isEmpty(board, x, y + 1) && sameColor(board, x, y - 1) && isEmpty(board, x, y - 2);
		if (!result) 
			result = isEmpty(board, x, y - 1) && sameColor(board, x, y + 1) && isEmpty(board, x, y + 2);
		if (!result) 
			result = isEmpty(board, x + 1, y) && sameColor(board, x - 1, y) && isEmpty(board, x - 2, y);
		if (!result) 
			result = isEmpty(board, x - 1, y) && sameColor(board, x + 1, y) && isEmpty(board, x + 2, y);
		if (!result) 
			result = isEmpty(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && isEmpty(board, x - 2, y - 2);
		if (!result) 
			result = isEmpty(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && isEmpty(board, x + 2, y + 2);
		if (!result) 
			result = isEmpty(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && isEmpty(board, x - 2, y + 2);
		if (!result) 
			result = isEmpty(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && isEmpty(board, x + 2, y - 2);
		return result;
	}
	
	boolean hasTwoDead(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = !sameColor(board, x, y + 1) && sameColor(board, x, y - 1) && isEmpty(board, x, y - 2);
		if (!result) 
			result = !sameColor(board, x, y - 1) && sameColor(board, x, y + 1) && isEmpty(board, x, y + 2);
		if (!result) 
			result = !sameColor(board, x + 1, y) && sameColor(board, x - 1, y) && isEmpty(board, x - 2, y);
		if (!result) 
			result = !sameColor(board, x - 1, y) && sameColor(board, x + 1, y) && isEmpty(board, x + 2, y);
		if (!result) 
			result = !sameColor(board, x + 1, y + 1) && sameColor(board, x - 1, y - 1) && isEmpty(board, x - 2, y - 2);
		if (!result) 
			result = !sameColor(board, x - 1, y - 1) && sameColor(board, x + 1, y + 1) && isEmpty(board, x + 2, y + 2);
		if (!result) 
			result = !sameColor(board, x + 1, y - 1) && sameColor(board, x - 1, y + 1) && isEmpty(board, x - 2, y + 2);
		if (!result) 
			result = !sameColor(board, x - 1, y + 1) && sameColor(board, x + 1, y - 1) && isEmpty(board, x + 2, y - 2);
		return result;
	}
	
	boolean hasOneAlive(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = isEmpty(board, x, y + 1) && isEmpty(board, x, y - 1);
		if (!result) 
			result = isEmpty(board, x, y - 1) && isEmpty(board, x, y + 1);
		if (!result) 
			result = isEmpty(board, x + 1, y) && isEmpty(board, x - 1, y);
		if (!result) 
			result = isEmpty(board, x - 1, y) && isEmpty(board, x + 1, y);
		if (!result) 
			result = isEmpty(board, x + 1, y + 1) && isEmpty(board, x - 1, y - 1);
		if (!result) 
			result = isEmpty(board, x - 1, y - 1) && isEmpty(board, x + 1, y + 1);
		if (!result) 
			result = isEmpty(board, x + 1, y - 1) && isEmpty(board, x - 1, y + 1);
		if (!result) 
			result = isEmpty(board, x - 1, y + 1) && isEmpty(board, x + 1, y - 1);
		return result;
	}
	
	boolean hasOneDead(GamePiece[][] board) {
		boolean result = false;
		if (!result) 
			result = !sameColor(board, x, y + 1) && isEmpty(board, x, y - 1);
		if (!result) 
			result = !sameColor(board, x, y - 1) && isEmpty(board, x, y + 1);
		if (!result) 
			result = !sameColor(board, x + 1, y) && isEmpty(board, x - 1, y);
		if (!result) 
			result = !sameColor(board, x - 1, y) && isEmpty(board, x + 1, y);
		if (!result) 
			result = !sameColor(board, x + 1, y + 1) && isEmpty(board, x - 1, y - 1);
		if (!result) 
			result = !sameColor(board, x - 1, y - 1) && isEmpty(board, x + 1, y + 1);
		if (!result) 
			result = !sameColor(board, x + 1, y - 1) && isEmpty(board, x - 1, y + 1);
		if (!result) 
			result = !sameColor(board, x - 1, y + 1) && isEmpty(board, x + 1, y - 1);
		return result;
	}
	
	private boolean sameColor(GamePiece[][] board, int x, int y) {
		try {
			return board[x][y] != null && board[x][y].color == color;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}
	
	private boolean isEmpty(GamePiece[][] board, int x, int y) {
		try {
			return board[x][y] == null;
		} catch (ArrayIndexOutOfBoundsException ex) {
			return false;
		}
	}
}
