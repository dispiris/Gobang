import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GobangGameReplayer {
	
	public static final int DELAY_TIME = 300;
	
	@SuppressWarnings("resource")
	public static void main(String[] args) throws FileNotFoundException, InterruptedException {
		System.out.println("Recordings available:");
		File dir = new File("replays");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println("  " + i + " - " + files[i].getName());
		}
		System.out.println("Which one do you want to replay?");
		Scanner console = new Scanner(System.in);
		int index = console.nextInt();
		while (index >= files.length) {
			System.out.println("no such recording. Try again.");
			index = console.nextInt();
		}
		Scanner input = new Scanner(files[index]);
		System.out.println("starting to replay...");
		Thread.sleep(1000);
		
		GameBoard board = new GameBoard(false);
		while (!board.gameover()) {
			Scanner line = new Scanner(input.nextLine());
			if (!line.next().equals("undo")) {
				PieceColor color = line.next().equals("BLACK") ? PieceColor.BLACK : PieceColor.WHITE;
				System.out.println("debugging: " + color);
				int x = line.nextInt();
				int y = line.nextInt();
				board.setPiece(x, y, color);
			} else {
				board.removeLastPiece();
			}
			Thread.sleep(DELAY_TIME);
		}
		
		board.showWinner();
		System.out.println("Thanks for watching. The replay will exit in 10 seconds");
		Thread.sleep(10000);
		System.exit(0);
	}

}
