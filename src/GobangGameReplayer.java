import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GobangGameReplayer {
	
	public static final int DELAY_TIME = 500;
	
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
		
		String s = console.next();
		while (!isInteger(s) || Integer.parseInt(s) < 0 || Integer.parseInt(s) >= files.length) {
			System.out.println("no such recording. Try again.");
			s = console.next();
		}
		Scanner input = new Scanner(files[Integer.parseInt(s)]);
		System.out.println("starting to replay...");
		
		GameBoard board = new GameBoard(false);
		while (input.hasNextLine()) {
			String line = input.nextLine();
			if (!line.equals("undo")) {
				Scanner lineS = new Scanner(line);
				int step = lineS.nextInt();
				PieceColor color = lineS.next().equals("BLACK") ? PieceColor.BLACK : PieceColor.WHITE;
				int x = lineS.nextInt();
				int y = lineS.nextInt();
				board.setPiece(x, y, color, true, step);
			} else {
				board.removeLastPiece();
			}
			Thread.sleep(DELAY_TIME);
		}
		
		System.out.println("Thanks for watching");
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } 
	    return true;
	}

}
