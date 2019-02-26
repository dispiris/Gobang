// old keyboard game launcher, no longer maintained

import java.util.Scanner;

public class GobangGameLauncher {
	
	private static boolean gameover;
	
	public static void main(String[] args) {
		Scanner console = new Scanner(System.in);
		GameBoard board = new GameBoard();
		gameover = false;
		while (!gameover) {
			turn(PieceColor.BLACK, console, board);
			if (gameover) { break; }
			turn(PieceColor.WHITE, console, board);
		}
		board.showWinner();
	}
	
	public static void turn(PieceColor color, Scanner console, GameBoard board) {
		System.out.println(color + " turn: ");
		int x = console.nextInt();
		int y = console.nextInt();
		while (!board.validPosition(x, y)) {
			System.out.println("Invalid position. Please try again...");
			System.out.println(color + " turn: ");
			x = console.nextInt();
			y = console.nextInt();
		}
		board.setPiece(x, y, color);
		if (board.checkWinner()) {
			gameover = true;
			System.out.println(color + " WINS !!!");
		}
	}
	
}
