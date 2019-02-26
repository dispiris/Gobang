/*
 * Author: Xinkai Zhang
 * Version: 1.0
 * Features:
 * 		players can place their pieces by mouse clicks
 * 		uses Thread.sleep to wait for instructions so not efficient and might
 * 			cause little delay
 * 		no multi-thread code
 */
public class GobangMouseGameLauncher {
	
	public static void main(String[] args) throws InterruptedException {
		GameBoard board = new GameBoard();
		boolean gameover = false;
		while (!gameover) {
			if (turn(PieceColor.BLACK, board)) {
				gameover = true;
				break;
			}
			if (turn(PieceColor.WHITE, board)) {
				gameover = true;
			}
		}
		board.showWinner();
	}
	
	public static boolean turn(PieceColor color, GameBoard board) throws InterruptedException {
		System.out.println(color + " turn: ");
		
		while (!GameBoard.mouseClicked) {
			Thread.sleep(50);
		}
		int x = GameBoard.getMouseSpotX();
		int y = GameBoard.getMouseSpotY();
		GameBoard.mouseClicked = false;
		
		while (!board.validPosition(x, y)) {
			System.out.println("Invalid position. Please try again...");
			System.out.println(color + " turn: ");
			while (!GameBoard.mouseClicked) {
				Thread.sleep(50);
			}
			x = GameBoard.getMouseSpotX();
			y = GameBoard.getMouseSpotY();
			GameBoard.mouseClicked = false;
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
