import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;

public class GameBoard {
	
	// utility constants:
	public static final PieceColor BLACK = PieceColor.BLACK;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final int SIZE = 15; // 15 * 15 board
	public static final int MARGIN = 100; // 100 pixels for margin
	public static final int WIDTH = 60; // 100 pixels wide between lines
	public static final int RADIUS = 20; // 20 pixels for piece radius
	public static final int PANEL_SIZE = 2 * MARGIN + (SIZE - 1) * WIDTH;
	public static final Color BACKGROUND_COLOR = new Color(222, 184, 135);	// color of goldwood
	public static final Color DISPLAY_COLOR = new Color(96,40,30);

	// fields:
	private GamePiece[][] board;
	private DrawingPanel panel;
	private Graphics g;
	private Stack<GamePiece> undoTrack;	// tracking game pieces
	private PieceColor current;		// may or may not be used
	private boolean gameover;
	private PieceColor winner;
	private int mouseSpotX;
	private int mouseSpotY;
	private int stepTrack;
	
	private PrintStream tracker;
	private boolean recordingOn;
	public boolean mouseClicked;
	
	public GameBoard() {
		this(BLACK, true);
	}
	
	public GameBoard(boolean recordingOn) {
		this(BLACK, recordingOn);
	}
	
	public GameBoard(PieceColor start, boolean recordingOn) {
		board = new GamePiece[15][15]; // [x][y] 0 - 14
		panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE + 70);
		g = panel.getGraphics();
		undoTrack = new Stack<>();
		this.current = start;
		gameover = false;
		winner = null;
		mouseClicked = false;
		stepTrack = 1;
		this.recordingOn = recordingOn;
		getImage();
		if (recordingOn) {
			try {
				getTracker();
			} catch (FileNotFoundException e) {};
		}
		
		// mouse click function
		panel.onClick((x, y) -> {
			getClickSpot(x, y);
			mouseClicked = true;
		});
		
		// keyboard function (add new keyboard reactions here)
		panel.onKeyDown(ch -> {
			if (ch == 'r') {
				try {
					removeLastPiece();
				} catch (EmptyStackException ex) {
					System.out.println("cannot undo: no pieces have been placed");
				}
			}
		});
	}
	
	private void getTracker() throws FileNotFoundException {
		File dir = new File("replays");
		dir.mkdirs();
		Date date = new Date() ;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss") ;
		File file = new File(dir, dateFormat.format(date) + ".txt");
		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		tracker = new PrintStream(file);
	}
	
	public PieceColor getCurrent() {
		return current;
	}
	
	public GamePiece getPiece(int x, int y) {
		return board[x][y];
	}

	public void setPiece(int x, int y, PieceColor color) {
		if (current == BLACK)	current = WHITE;	else	current = BLACK;
		System.out.println("piece set at: (" + x + ", " + y + ")");
		board[x][y] = new GamePiece(color, x, y);
		undoTrack.push(board[x][y]);	// for undo function
		board[x][y].getImage(g);
		trackOutput(color, x, y);
		gameover = board[x][y].hasFive(this);
		winner = gameover ? color : null;
	}
	
	private void trackOutput(PieceColor color, int x, int y) {
		if (recordingOn) {
			tracker.println(stepTrack + "  " + color + " " + x + " " + y);
			stepTrack++;
		}
	}
	
	// returns true if the game is over
	public boolean gameover() {
		return gameover;
	}

	// returns the color of the winner (returns null if the game is not over)
	public PieceColor getWinner() {
		return winner;
	}

	public void removeLastPiece() {
		GamePiece lastPiece = undoTrack.pop();
		lastPiece.removeImage(g);
		board[lastPiece.x][lastPiece.y] = null;
		if (lastPiece.color == BLACK)	current = BLACK;	else	current = WHITE;
		if (recordingOn) 	tracker.println("undo");
		System.out.println("last piece withdrawn");
	}
	
	// returns true if the spot specified by the given x and y value is available
	// returns false if the spot is out of bound or is already occupied
	public boolean validPosition(int x, int y) {
		return x >= 0 && x <= 14 && y >= 0 && y <= 14 && board[x][y] == null;
	}

	public void showWinner() {
		g.setColor(DISPLAY_COLOR);
		g.setFont(new Font("Arial", Font.BOLD, 120));
		g.drawString(winner + " wins !", MARGIN, MARGIN + 16 * WIDTH);
	}

	public void close() {
		panel.setVisible(false);
	}
	
	public int getMouseSpotX() {
		return mouseSpotX;
	}

	public int getMouseSpotY() {
		return mouseSpotY;
	}

	private void getImage() {
		panel.setBackground(BACKGROUND_COLOR);
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
