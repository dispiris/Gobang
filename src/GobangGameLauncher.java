/**	{@code GobangMouseGameLauncher} is used to launch the Gobang game
 * @author Xinkai Zhang
 * @version 1.3
 * Features:
 * 		players can withdraw the last piece by pressing 'r'
 * 		the game can be saved into a file which can be later replayed
 */

public class GobangGameLauncher implements GameEventListener, WinEventListener {
	
	private GameBoard board = new GameBoard();
	{
		board.setListener(this);
		board.setWinListener(this);
	}
	
	public static void main(String[] args) throws InterruptedException {
		System.out.println("Welcome to the Gobang game v1.3");
		System.out.println("You can undo the last piece by pressing 'r'.");
		new GobangGameLauncher();
	}
	
	@Override
	public void onGameEvent(PieceColor color, int xPos, int yPos) {
		board.setPiece(xPos, yPos, color);
	}

	@Override
	public void onWinEvent(PieceColor winner)  {
		System.out.println(winner + " player wins! Thanks for playing.");
	}
	
}
