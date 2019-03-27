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

import utilPackage.DrawingPanel;

public class GameBoard {
	
	public static final PieceColor BLACK = PieceColor.BLACK;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final int SIZE = 15; // 15 * 15 board
	public static final int MARGIN = 100; // 100 pixels for margin
	public static final int WIDTH = 60; // 100 pixels wide between lines
	public static final int RADIUS = 20; // 20 pixels for piece radius
	public static final int PANEL_SIZE = 2 * MARGIN + (SIZE - 1) * WIDTH;
	public static final Color BACKGROUND_COLOR = new Color(222, 184, 135);	// color of goldwood
	public static final Color DISPLAY_COLOR = new Color(96,40,30);
	
	// for tracking the game
	private GamePiece[][] board;
	private PieceColor current;
	private boolean gameover;
	private PieceColor winner;
	
	// for undo function
	private Stack<GamePiece> undoTrack;	// tracking pieces
	
	// for images
	private DrawingPanel panel;
	private Graphics g;
	
	// for mouse click function
	private int mouseSpotX;
	private int mouseSpotY;

	// for recording function
	private boolean recordingOn;
	private int stepTrack;
	private PrintStream tracker;
	
	// mouse click triggers a GameEvent and game over triggers a winEvent
	private GameEventListener listener;
	private WinEventListener winListener;
	
	// constructs a default game board with recoringOn = true
	public GameBoard() {
		this(true);
	}
	
	public GameBoard(boolean recordingOn) {
		board = new GamePiece[15][15]; // [x][y] 0 - 14
		current = BLACK;
		gameover = false;
		winner = null;
		
		undoTrack = new Stack<>();
		
		panel = new DrawingPanel(PANEL_SIZE, PANEL_SIZE + 70);
		g = panel.getGraphics();
		getImage();
		
		mouseSpotX = -1;
		mouseSpotY = -1;
		
		this.recordingOn = recordingOn;
		stepTrack = 1;
		if (recordingOn) {
			try {
				getTracker();
			} catch (FileNotFoundException e) {};
		}
		
		panel.onClick((x, y) -> {
			if (!gameover) {
				getClickSpot(x, y);
				if (!validPosition(mouseSpotX, mouseSpotY)) {
					System.out.println("Invalid position. Please try again...");
				} else {
					listener.onGameEvent(current, mouseSpotX, mouseSpotY);
				}
			}
		});
		
		panel.onKeyDown(ch -> {
			if (!gameover && ch == 'r') {
				try {
					removeLastPiece();
				} catch (EmptyStackException ex) {
					System.out.println("cannot undo: no pieces have been placed");
				}
			}
		});
	}
	
	public void setListener(GameEventListener s) {
		listener = s;
	}
	
	public void setWinListener(WinEventListener s) {
		winListener = s;
	}
	
	public GamePiece getPiece(int x, int y) {
		return board[x][y];
	}
	
	public void setPiece(int x, int y, PieceColor color) {
		setPiece(x, y, color, false, -1);
	}
	
	public void setPiece(int x, int y, PieceColor color, boolean showStepNum, int stepNum) {
		current = current == BLACK ? WHITE : BLACK;
		System.out.println(current + " piece set at: (" + x + ", " + y + ")");
		printMessage(current + " piece set at: (" + x + ", " + y + ")");
		board[x][y] = new GamePiece(color, x, y);
		board[x][y].getImage(g); 
		if (showStepNum)
			board[x][y].showStepNumber(stepNum, g);
		
		undoTrack.push(board[x][y]);	// for undo function
		if(recordingOn)	
			trackOutput(color, x, y);		// for recording
		
		gameover = board[x][y].hasFive(this);
		if (gameover) {
			winner = color;
			showWinner();
		}
	}

	public void removeLastPiece() {
		stepTrack--;
		GamePiece lastPiece = undoTrack.pop();
		lastPiece.removeImage(g);
		board[lastPiece.x][lastPiece.y] = null;
		current = lastPiece.color == BLACK ? BLACK : WHITE;
		if (recordingOn) 	tracker.println("undo");
		System.out.println("last piece withdrawn");
		printMessage("last piece withdrawn");
	}
	
	public void showWinner() {
		clearDisplayArea();
		g.setColor(DISPLAY_COLOR);
		g.setFont(new Font("Algerian", Font.ITALIC, 92));
		g.drawString(winner + " player wins", 60, MARGIN + 16 * WIDTH + 10);
		if (winListener != null)
			winListener.onWinEvent(winner);
	}
	
	private void printMessage(String message) {
		clearDisplayArea();
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 50));
		int x = MARGIN + 2 * WIDTH;
		if (message.substring(0, 5).equals("BLACK"))	x -= 14;
		g.drawString(message, x, MARGIN + 16 * WIDTH);
	}

	private void clearDisplayArea() {
		g.setColor(BACKGROUND_COLOR);
		g.fillRect(0, MARGIN + 15 * WIDTH, PANEL_SIZE, MARGIN);
	}
	
	private void trackOutput(PieceColor color, int x, int y) {
		tracker.println(stepTrack + "  " + color + " " + x + " " + y);
		stepTrack++;
	}
	
	private boolean validPosition(int x, int y) {
		return x >= 0 && x <= 14 && y >= 0 && y <= 14 && board[x][y] == null;
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
			g.drawString(number, MARGIN - 45, MARGIN + WIDTH * i + 7);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			String number = "" + i;
			g.drawString(number, MARGIN - 55, MARGIN + WIDTH * i + 7);
		}
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
	
	private void getClickSpot(int x, int y) {
		mouseSpotX = (int) (((double) x - MARGIN + (double) WIDTH / 2) / WIDTH);
		mouseSpotY = (int) (((double) y - MARGIN + (double) WIDTH / 2) / WIDTH);
	}
}
