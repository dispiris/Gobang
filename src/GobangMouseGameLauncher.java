/*
 * Author: Xinkai Zhang
 * Version: 1.1
 * Features:
 * 		players can now withdraw / undo the last piece by pressing 'r'
 */

public class GobangMouseGameLauncher {
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Welcome to the Gobang game. You can undo the last piece by pressing 'r'.");
		GameBoard board = new GameBoard();
		boolean gameover = false;
		while (!gameover) {
			gameover = turn(board);
		}
		board.showWinner();
		System.out.println("Thanks for playing. The game will exit in 10 seconds");
		Thread.sleep(10000);
		System.exit(0);
	}
	
	public static boolean turn(GameBoard board) throws InterruptedException {
		PieceColor color = board.current;
		System.out.println(color + " turn: ");
		
		while (!board.mouseClicked) {
			if (board.lastPieceRemoved)	{
				board.lastPieceRemoved = false;
				return false;
			}
			Thread.sleep(50);
		}
		int x = board.getMouseSpotX();
		int y = board.getMouseSpotY();
		board.mouseClicked = false;
		
		while (!board.validPosition(x, y)) {
			System.out.println("Invalid position. Please try again...");
			System.out.println(color + " turn: ");
			while (!board.mouseClicked) {
				Thread.sleep(50);
			}
			x = board.getMouseSpotX();
			y = board.getMouseSpotY();
			board.mouseClicked = false;
		}
		board.setPiece(x, y, color);
		
		if (board.checkWinner(color)) {
			System.out.println(color + " WINS !!!");
			return true;
		}
		return false;
	}
	
//	public static void undo(GameBoard board) throws InterruptedException {
//		
//	}
}
