package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.Stack;

import javax.swing.JPanel;

import utility.MessageEventListener;
import utility.PieceColor;

@SuppressWarnings("serial")
public class GameBoard extends JPanel implements MouseListener, KeyListener {
	
	public static final PieceColor BLACK = PieceColor.BLACK;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final int SIZE = 15; // 15 * 15 board
	public static final int MARGIN = 100; // 100 pixels for margin
	public static final int WIDTH = 60; // 100 pixels wide between lines
	public static final int RADIUS = 20; // 20 pixels for piece radius
	public static final int PANEL_SIZE = 2 * MARGIN + (SIZE - 1) * WIDTH;
	public static final Color BACKGROUND_COLOR = new Color(222, 184, 135);	// color of goldwood
	
	// for determine the function of the board
	private boolean isReplay;
	
	// for tracking the game
	private GamePiece[][] board;
	private PieceColor current;
	private boolean gameover;
	private PieceColor winner;
	
	// for undo function
	private Stack<GamePiece> undoTrack;	// tracking pieces
	
	// for recording function
	private boolean recordingOn;
	private int stepTrack;
	private PrintStream tracker;
	
	private MessageEventListener messListener;
	
	public static final BufferedImage BOARD_IMAGE;
	static {
		BOARD_IMAGE = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = BOARD_IMAGE.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(BACKGROUND_COLOR);
		g2d.fill(new Rectangle2D.Double(0, 0, PANEL_SIZE, PANEL_SIZE));
		g2d.setColor(Color.BLACK);
		for (int i = 0; i < SIZE; i++) {
			g2d.draw(new Line2D.Double(MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14, MARGIN + WIDTH * i));
		}
		for (int i = 0; i < SIZE; i++) {
			g2d.draw(new Line2D.Double(MARGIN + WIDTH * i, MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14));
		}
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 25));
		for (int i = 0; i <= 9; i++) {
			g2d.drawString("" + i, MARGIN + WIDTH * i - 7, MARGIN - 35);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			g2d.drawString("" + i, MARGIN + WIDTH * i - 15, MARGIN - 35);
		}
		for (int i = 0; i <= 9; i++) {
			g2d.drawString("" + i, MARGIN - 45, MARGIN + WIDTH * i + 7);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			g2d.drawString("" + i, MARGIN - 55, MARGIN + WIDTH * i + 7);
		}
		g2d.dispose();
	}
	
	public GameBoard(boolean isReplay) {
		setPreferredSize(new Dimension(1040, 1040));
		setOpaque(true);
		this.isReplay = isReplay;
		recordingOn = isReplay ? false : true;
		board = new GamePiece[15][15]; // [x][y] 0 - 14
		current = BLACK;
		gameover = false;
		winner = null;
		
		undoTrack = new Stack<>();
		
		stepTrack = 1;
		if (recordingOn) {
			try {
				getTracker();
			} catch (FileNotFoundException e) {};
		}
		
		addMouseListener(this);
		addKeyListener(this);
	}
	
	public void setMessListener(MessageEventListener s) {
		messListener = s;
	}
	
	public GamePiece getPiece(int x, int y) {
		return board[x][y];
	}
	
	public void setPiece(int x, int y, PieceColor color) {
		setPiece(x, y, color, false, -1);
	}
	
	public void setPiece(int x, int y, PieceColor color, boolean showStepNum, int stepNum) {
		messListener.onMessageEvent("      " + current + " piece set at: (" + x + ", " + y + ")", false);
		
		board[x][y] = new GamePiece(color, x, y, stepTrack);
		stepTrack++;
		repaint();
		undoTrack.push(board[x][y]);	// for undo function
		if(recordingOn)	
			tracker.println(stepTrack + "  " + color + " " + x + " " + y);//for recording
		
		gameover = board[x][y].hasFive(this);
		if (gameover) {
			winner = color;
			messListener.onMessageEvent(" " + winner + " player wins", true);
		}
		current = current == BLACK ? WHITE : BLACK;
	}
	
	public boolean gameover() {
		return gameover;
	}
	
	private boolean validPosition(int x, int y) {
		return x >= 0 && x <= 14 && y >= 0 && y <= 14 && board[x][y] == null;
	}

	@Override
	public void paintComponent(Graphics g) {
		g.drawImage(BOARD_IMAGE, 0, 0, null);
		for (GamePiece[] pieces : board) {
			for (GamePiece piece : pieces) {
				if (piece != null) {
					piece.getImage((Graphics2D)g);
					if (isReplay) piece.showStepNum(g);
				}
			}
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
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Point p = e.getPoint();
		int x = (int) p.getX();
		int y = (int) p.getY();
		if (!gameover) {
			int mouseSpotX = (int) (((double) x - MARGIN + (double) WIDTH / 2) / WIDTH);
			int mouseSpotY = (int) (((double) y - MARGIN + (double) WIDTH / 2) / WIDTH);
			if (!validPosition(mouseSpotX, mouseSpotY)) {
				messListener.onMessageEvent("Invalid position. Please try again...", false);
			} else {
				setPiece(mouseSpotX, mouseSpotY, current);
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameover && e.getKeyCode() == KeyEvent.VK_R) {
			removeLastPiece();
		}
	}

	public void removeLastPiece() {
		try {
			GamePiece lastPiece = undoTrack.pop();
			messListener.onMessageEvent("      " + lastPiece.color + " piece withdrawn", false);
			board[lastPiece.x][lastPiece.y] = null;
			current = lastPiece.color == BLACK ? BLACK : WHITE;
			repaint();
			if (recordingOn) 	tracker.println("undo");
			stepTrack--;
		} catch (EmptyStackException e1) {
			System.out.println("cannot undo: no pieces have been placed");
			messListener.onMessageEvent("   " + "cannot undo: no piece has been placed yet", false);
		}
	}
	
	@Override
	public void mousePressed(MouseEvent e) {}

	@Override
	public void mouseReleased(MouseEvent e) {}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyReleased(KeyEvent e) {}
	
}
