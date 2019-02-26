import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class GameBoard {
	
	// class constants:
	public static final PieceColor BLACK = PieceColor.BLACK;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final int SIZE = 15; // 15 * 15 board
	public static final int MARGIN = 100; // 100 pixels for margin
	public static final int WIDTH = 60; // 100 pixels wide between lines
	public static final int RADIUS = 20; // 20 pixels for piece radius
	public static final int PANEL_SIZE = 2 * MARGIN + (SIZE - 1) * WIDTH;

	// fields:
	private GamePiece[][] board;
	private boolean gameover;
	private PieceColor winner;
	private DrawingPanel panel;
	private Graphics g;
	
	// static states:
	private static int mouseSpotX;
	private static int mouseSpotY;
	public static boolean mouseClicked;
	public static boolean gameRestarted;

	// constructor:
	public GameBoard() {
		board = new GamePiece[15][15]; // [x][y] 0 - 14
		gameover = false;
		mouseClicked = false;
		panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE);
		g = panel.getGraphics();
		getImage();
		
		// mouse click function
		panel.onClick((x, y) -> {
			getClickSpot(x, y);
			mouseClicked = true;
		});
		
		// keyboard function (add new keyboard reactions here)
		panel.onKeyDown(ch -> {
			if (ch == 'r') {
				restart();
//				close();
//				System.exit(0);
//				try {
//					GobangMouseGameLauncher.restart();
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
			}
		});
	}

	// getters:
	public static int getMouseSpotX() {
		return mouseSpotX;
	}

	public static int getMouseSpotY() {
		return mouseSpotY;
	}

	public GamePiece getPiece(int x, int y) {
		return board[x][y];
	}

	public void setPiece(int x, int y, PieceColor color) {
		System.out.println("piece set at: (" + mouseSpotX + ", " + mouseSpotY + ")");
		board[x][y] = new GamePiece(color, x, y);
		board[x][y].getImage(g);
	}
	
	public void restart() {
		System.out.println("Game restarted");
		board = new GamePiece[15][15];
		gameover = false;
		winner = null;
		mouseClicked = false;
		mouseSpotX = 0;
		mouseSpotY = 0;
		panel.clear();
		getImage();
		gameRestarted = true;
	}
	
	// returns true if the game is over
	public boolean gameover() {
		return gameover;
	}

	// returns the color of the winner (returns null if the game is not over)
	public PieceColor winner() {
		return winner;
	}

	// asks every existing piece on the board to check if its side wins and returns
	// true if
	// either side wins. Updates the fields accordingly.
	public boolean checkWinner(PieceColor color) {
		for (GamePiece[] row : board) {
			for (GamePiece piece : row) {
				if (piece != null && piece.color == color && piece.wins(this)) {
					gameover = true;
					winner = color;
					return true;
				}
			}
		}
		return false;
	}
	
	// returns true if the spot specified by the given x and y value is available
	// returns false if the spot is out of bound or is already occupied
	public boolean validPosition(int x, int y) {
		return x >= 0 && x <= 14 && y >= 0 && y <= 14 && board[x][y] == null;
	}

	public void showWinner() {
		g.setColor(Color.RED);
		g.setFont(new Font("TimesRoman", Font.BOLD, 120));
		if (winner == BLACK) {
			g.drawString("BLACK wins!", MARGIN, MARGIN + 7 * WIDTH);
		} else {
			g.drawString("WHITE wins!", MARGIN, MARGIN + 7 * WIDTH);
		}
	}

	public void close() {
		panel.setVisible(false);
	}

	private void getImage() {
		panel.setBackground(Color.PINK);
		g.setColor(Color.BLACK);
		for (int i = 0; i < SIZE; i++) {
			g.drawLine(MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14, MARGIN + WIDTH * i);
		}
		for (int i = 0; i < SIZE; i++) {
			g.drawLine(MARGIN + WIDTH * i, MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14);
		}

		g.setFont(new Font("TimesRoman", Font.BOLD, 25));
		for (int i = 0; i <= 9; i++) {
			String number = "" + i;
			g.drawString(number, MARGIN + WIDTH * i - 7, MARGIN - 35);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			String number = "" + i;
			g.drawString(number, MARGIN + WIDTH * i - 15, MARGIN - 35);
		}
		for (int i = 0; i <= 9; i++) {
			String number = "" + i;
			g.drawString(number, MARGIN - 45, MARGIN + WIDTH * i + 5);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			String number = "" + i;
			g.drawString(number, MARGIN - 55, MARGIN + WIDTH * i + 5);
		}
	}
	
	private void getClickSpot(int x, int y) {
		mouseSpotX = (int) (((double) x - MARGIN + (double) WIDTH / 2) / WIDTH);
		mouseSpotY = (int) (((double) y - MARGIN + (double) WIDTH / 2) / WIDTH);
	}
}