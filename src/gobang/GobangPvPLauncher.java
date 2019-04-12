package gobang;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import components.GameBoard;
import components.MessageBoard;

public class GobangPvPLauncher extends Thread {
	
	private GameBoard board;
	private MessageBoard messBoard;

	@Override
	public void run() {
		var fr = new JFrame("Gobang PvP Game");
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setLocationByPlatform(true);
		
		JPanel panel = new JPanel();
		
		
		messBoard = new MessageBoard("launch");
		panel.add(messBoard);
		
		
		fr.setContentPane(panel);
		fr.pack();
		fr.setResizable(false);
		fr.setVisible(true);
		
		board = new GameBoard(false);
		board.setMessListener(messBoard);
		panel.add(board);
		
		Timer timer = new Timer(4000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				board.setFocusable(true);
				board.requestFocusInWindow();
				fr.validate();
				panel.setPreferredSize(new Dimension(1040, 1190));
				fr.pack();	
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

//	 user should launch the game thru main program
//	public static void main(String[] args) {
//		var test = new GobangPvPLauncher();
//		test.start();
//	}
}
