package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.EmptyStackException;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

import utility.Images;
import utility.MessageEventListener;
import utility.PieceColor;

@SuppressWarnings("serial")
public class AIGameBoard extends JPanel implements MouseListener, KeyListener {
	
	public static final PieceColor BLACK = PieceColor.BLACK;
	public static final PieceColor WHITE = PieceColor.WHITE;
	public static final int MARGIN = 100; // 100 pixels for margin
	public static final int WIDTH = 60; // 100 pixels wide between lines
	public static final int RADIUS = 20; // 20 pixels for piece radius
	public static final Color BACKGROUND_COLOR = new Color(222, 184, 135);	// color of goldwood
	// finish the static initialization of board image to prevent flash
	private static final BufferedImage BOARD_IMAGE = Images.BOARD_IMAGE;
	
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
	
//	private int nextX;
//	private int nextY;
	// for AI function
	private Tuple[][][] tuples;
	
	private MessageEventListener messListener;
	
	
	public AIGameBoard(boolean isReplay) {
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
		
		tuples = new Tuple[19][19][4];
		for (int x = -2; x < 17; x++) {
			for (int y = -2; y < 17; y++) {
				tuples[x+2][y+2][0] = new Tuple(x, y, 1);
				tuples[x+2][y+2][1] = new Tuple(x, y, 2);
				tuples[x+2][y+2][2] = new Tuple(x, y, 3);
				tuples[x+2][y+2][3] = new Tuple(x, y, 4);
			}
		}
	}
	
	public void setMessListener(MessageEventListener s) {
		messListener = s;
	}
	
	public GamePiece getPiece(int x, int y) {
		return board[x][y];
	}
	
	public void setPiece(int x, int y, PieceColor color) {
		setPiece(x, y, color, false);
	}
	
	public void setAIPiece() {
		int xTrack = -1;
		int yTrack = -1;
		int scoreTrack = -1;
		for (int x = 0; x < 15; x++) {
			for (int y = 0; y < 15; y++) {
				if (validPosition(x, y)) {
					int total = 0;
					List<Tuple> tuples = getRelatedTuples(x, y);
					for (Tuple tuple : tuples) {
						total += tuple.getScore();
					}
					if (total > scoreTrack) {
						scoreTrack = total;
						xTrack = x;
						yTrack = y;
					} 
				}
			}
		}
		setPiece(xTrack, yTrack, current);
	}
	
	private List<Tuple> getRelatedTuples(int x, int y) {
		List<Tuple> list = new LinkedList<>();
		for (int i = x - 2; i <= x + 2; i++) {
			list.add(tuples[i+2][y+2][0]);
		}
		for (int i = y - 2; i <= y + 2; i++) {
			list.add(tuples[x+2][i+2][1]);
		}
		list.add(tuples[x+2+2][y-2+2][2]);
		list.add(tuples[x+1+2][y-1+2][2]);
		list.add(tuples[x+2][y+2][2]);
		list.add(tuples[x-1+2][y+1+2][2]);
		list.add(tuples[x-2+2][y+2+2][2]);
		
		list.add(tuples[x-2+2][y-2+2][3]);
		list.add(tuples[x-1+2][y-1+2][3]);
		list.add(tuples[x+2][y+2][3]);
		list.add(tuples[x+1+2][y+1+2][3]);
		list.add(tuples[x+2+2][y+2+2][3]);
		return list;
	}
	
	public void setPiece(int x, int y, PieceColor color, boolean showStepNum) {
		messListener.onMessageEvent("      " + current + " piece set at: (" + x + ", " + y + ")", false);
		
		board[x][y] = new GamePiece(color, x, y, stepTrack);
		stepTrack++;
		repaint();
		undoTrack.push(board[x][y]);	// for undo function
		if(recordingOn)	
			tracker.println(stepTrack + "  " + color + " " + x + " " + y);//for recording
		
		gameover = board[x][y].hasFive(board);
		if (gameover) {
			winner = color;
			if (winner == WHITE) {
				messListener.onMessageEvent("           YOU  LOSE", true);
			} else {
				messListener.onMessageEvent("           YOU   WIN", true);
			}
		}
		current = current == BLACK ? WHITE : BLACK;
		
		List<Tuple> tuples = getRelatedTuples(x, y);
		for (Tuple tuple : tuples) {
			tuple.add(color);
		}
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
		File file = new File(dir, dateFormat.format(date) + " (PvE).txt");
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
		if (!gameover && current == BLACK) {
			int mouseSpotX = (int) (((double) x - MARGIN + (double) WIDTH / 2) / WIDTH);
			int mouseSpotY = (int) (((double) y - MARGIN + (double) WIDTH / 2) / WIDTH);
			if (!validPosition(mouseSpotX, mouseSpotY)) {
				messListener.onMessageEvent("Invalid position. Please try again...", false);
			} else {
				setPiece(mouseSpotX, mouseSpotY, current);
				Timer timer = new Timer(1000, new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						if (!gameover)
							setAIPiece();
					}
				});
				timer.setRepeats(false);
				timer.start();
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (!gameover && e.getKeyCode() == KeyEvent.VK_R) {
			removeLastPiece();
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
	
	
	
//	static int evaluate(GamePiece[][] board) {
//		int whiteScore = 0;
//		for (GamePiece[] pieces : board) {
//			for (GamePiece piece : pieces) {
//				if (piece != null) {
//					PieceColor color = piece.getColor();
//					if (piece.hasFive(board)) {
//						whiteScore += (color == WHITE ? 100000 : -100000);
//					} else if (piece.hasFourAlive(board)) {
//						whiteScore += (color == WHITE ? 10000 : -10000);
//					} else if (piece.hasFourDead(board)) {
//						whiteScore += (color == WHITE ? 1000 : -1000);
//					} else if (piece.hasThreeAlive(board)){
//						whiteScore += (color == WHITE ? 1000 : -1000);
//					} else if (piece.hasThreeDead(board)) {
//						whiteScore += (color == WHITE ? 100 : -100);
//					} else if (piece.hasTwoAlive(board)) {
//						whiteScore += (color == WHITE ? 100 : -100);
//					} else if (piece.hasTwoDead(board)) {
//						whiteScore += (color == WHITE ? 10 : -10);
//					} else if (piece.hasOneAlive(board)){
//						whiteScore += (color == WHITE ? 10 : -10);
//					} else if (piece.hasOneDead(board)){
//						whiteScore += (color == WHITE ? 1 : -1);
//					} else {
//						whiteScore += (color == WHITE ? 0 : -0);
//					}
//				}
//			}
//		}
//		return whiteScore;
//	}
//	
//	/*
//	 * 
//	 */
//	
//	private int evaluateRow(int y) {
//		GamePiece[] row = new GamePiece[15];
//		for (int i = 0; i < 15; i++) {
//			row[i] = board[i][y];
//		}
//		List<Integer> temp = new ArrayList<>();
//		for (int i = 0; i < 15; i++) {
//			if (row[i] != null && row[i].color == BLACK) {
//				temp.clear();
//			} else {
//				if (row[i] == null) {
//					temp.add(0);
//				} else {
//					temp.add(1);
//				}
//				if (temp.size() >= 5) {
//					checkList(temp);
//				}
//			}
//		}
//	}
//	
//	private int rules(PieceColor a, PieceColor b, PieceColor c, PieceColor d, PieceColor e, PieceColor f) {
//		
//	}
	
//	public void setWhitePiece() {
//		Node minimax = minimax(board, 3, -10000000, 10000000, true, null);
//		System.out.println("piece set at" + minimax.x + " " + minimax.y + " " + minimax.score);
//		setPiece(minimax.x, minimax.y, current, false);
//		
//	}
	
//	private static class Node {
//		int score;
//		int x;
//		int y;
//		
//		public Node(int score, int x, int y) {
//			this.score = score;
//			this.x = x;
//			this.y = y;
//		}
//	}
	
//	// maxPlayer = whitePlayer
//	private Node minimax(GamePiece[][] board, int depth, int alpha, int beta, boolean maxPlayer, GamePiece lastPiece) {
//		if (depth == 0 || (lastPiece != null && lastPiece.hasFive(board))) {
//			int eval = evaluate(board);
//			return new Node(eval, lastPiece.x, lastPiece.y);
//		}
//		if (maxPlayer) {
//			
//			Node maxEval = lastPiece == null ? new Node(-10000000, -1, -1) : new Node(-10000000, lastPiece.x, lastPiece.y);
//			var ite = new BoardIterator(board, WHITE);
//			while (ite.hasNext()) {
//				int[] pos = ite.next();
//				int x = pos[0];
//				int y = pos[1];
//				board[x][y] = new GamePiece(WHITE, x, y, -1);
//				Node evalCombi = minimax(board, depth - 1, alpha, beta, false, board[x][y]);
//				int eval = evalCombi.score;
//				board[x][y] = null;
//				if (eval > maxEval.score) {
//					maxEval = evalCombi;
//				}
//				alpha = Math.max(alpha, eval);
//				if (beta <= alpha) {
//					break;
//				}
//			}
//			return maxEval;
//		} else {
//			Node minEval = new Node(10000000, lastPiece.x, lastPiece.y);
//			var ite = new BoardIterator(board, BLACK);
//			while (ite.hasNext()) {
//				int[] pos = ite.next();
//				int x = pos[0];
//				int y = pos[1];
//				board[x][y] = new GamePiece(BLACK, x, y, -1);
//				Node evalCombi = minimax(board, depth - 1, alpha, beta, true, board[x][y]);
//				board[x][y] = null;
//				int eval = evalCombi.score;
//				if (eval < minEval.score) {
//					minEval = evalCombi;
//				}
//				beta = Math.min(beta, eval);
//				if (beta <= alpha) {
//					break;
//				}
//			}
//			return minEval;
//		}
//	}
	
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
	
	private static class Tuple {
		
		private int black;
		private int white;
		private int score;
		private boolean dummy;
		
		public Tuple(int x, int y, int type) {
			black = 0;
			white = 0;
			score = 7;
			if (type == 1) {
				if (x <= 1 || x >= 13) {
					dummy = true;
				} else {
					dummy = false;
				}
			} else if (type == 2) {
				if (y <= 1 || y >= 13) {
					dummy = true;
				} else {
					dummy = false;
				}
			} else if (type == 3 || type == 4) {
				if (x <= 1 || x >= 14 || y <= 1 || y >= 13) {
					dummy = true;
				} else {
					dummy = false;
				}
			}
		}
		
		public int getScore() {
			if (dummy) {
				return 0;
			}
			return score;
		}
		
		public void add(PieceColor color) {
			if (!dummy) {
				checkAddable();
				if (color == PieceColor.BLACK) {
					black++;
				} else {
					white++;
				}
				evaluate();
			}
		}
		
		private void evaluate() {
			if(black == 0 && white == 0){
				score = 7;
			} else if(black > 0 && white > 0){
				score = 0;
				dummy = true;
			} else if(white == 1){
				score = 35;
			} else if(white == 2){
				score = 800;
			} else if(white == 3){
				score = 15000;
			} else if(white == 4){
				score = 800000;
			} else if(white == 5){
			} else if(black == 1){
				score = 15;
			} else if(black == 2){
				score = 400;
			} else if(black == 3){
				score = 1800;
			} else if(black == 4){
				score = 100000;
			} else if(black == 5){
			} else {
				throw new IllegalStateException("unknown exception");
			}
		}
		
		public void checkAddable() {
			if (black + white >= 5) {
				throw new IllegalStateException("more than 5 pieces" + black + white);
			}
		}
	}
	
}
