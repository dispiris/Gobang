package gobang;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import components.GameBoard;
import components.MessageBoard;
import utility.PieceColor;

public class GobangGameReplayer extends Thread {
	
	public static final int DELAY_TIME = 500;
	
	@Override
	public void run() {
		System.out.println("Recordings available:");
		File dir = new File("replays");
		File[] files = dir.listFiles();
		for (int i = 0; i < files.length; i++) {
			System.out.println("  " + i + " - " + files[i].getName());
		}
		System.out.println("Which one do you want to replay?");
		@SuppressWarnings("resource")
		Scanner console = new Scanner(System.in);
		String s = console.next();
		while (!isInteger(s) || Integer.parseInt(s) < 0 || Integer.parseInt(s) >= files.length) {
			System.out.println("no such recording. Try again.");
			s = console.next();
		}
		
		var fr = new JFrame("Gobang game");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setLocationByPlatform(true);
		
		JPanel panel = new JPanel();
		
		var messBoard = new MessageBoard("replay");
		panel.add(messBoard);
		
		fr.setContentPane(panel);
		fr.pack();
		fr.setResizable(false);
		fr.setVisible(true);
		
		var board = new GameBoard(true);
		board.setMessListener(messBoard);
		panel.add(board);
		
		Timer timer = new Timer(4000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				fr.validate();
				panel.setPreferredSize(new Dimension(1040, 1190));
				fr.pack();				
			}
		});
		timer.setRepeats(false);
		timer.start();
		
		try {
			Thread.sleep(4500);
		} catch (InterruptedException e1) {
		}
		
		try {
			@SuppressWarnings("resource")
			Scanner input = new Scanner(files[Integer.parseInt(s)]);
			System.out.println("starting to replay...");
			while (input.hasNextLine()) {
				String line = input.nextLine();
				if (!line.equals("undo")) {
					@SuppressWarnings("resource")
					Scanner lineS = new Scanner(line);
					int step = lineS.nextInt();
					PieceColor color = lineS.next().equals("BLACK") ? PieceColor.BLACK : PieceColor.WHITE;
					int x = lineS.nextInt();
					int y = lineS.nextInt();
					board.setPiece(x, y, color, true, step);
				} else {
					board.removeLastPiece();
				}
				
				try {
					Thread.sleep(DELAY_TIME);
				} catch (InterruptedException e) {
				} 
			}
		} catch (Exception ex) {} 
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } 
	    return true;
	}

//	public static void main(String[] args) {
//		var a = new GobangGameReplayer();
//		a.start();
//	}
	
}
