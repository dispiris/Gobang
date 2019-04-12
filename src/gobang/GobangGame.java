package gobang;
/** {@code GobangGame} is the general game which contains multiple part
 * @author Xinkai Zhang
 * @version 1.5
 */

import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class GobangGame {
	
	private JFrame frame;
	private JPanel panel;
	private JTextField text;
	private JButton pvpBut;
	private JButton pveBut;
	private JButton replayerBut;
	private JButton functionBut;
	
	private static final Font DEFAULT_FONT = new Font("Arial", Font.ITALIC, 40);
	
	public GobangGame() {
		frame = new JFrame("Gobang game v1.5");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel = new JPanel(new GridLayout(5, 1));
		text = new JTextField("   Welcome to Gobang game v1.5   ");
		text.setHorizontalAlignment(SwingConstants.LEFT);
		text.setEditable(false);
		text.setFont(new Font("Arial", Font.PLAIN, 50));
		panel.add(text);
		
		pvpBut = new JButton("1. start a new PvP game");
		pvpBut.setHorizontalAlignment(SwingConstants.LEFT);
		pvpBut.setFont(DEFAULT_FONT);
		pvpBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Welcome to the Gobang game v1.5");
				System.out.println("You can undo the last piece by pressing 'r'.");
				var launcher = new GobangPvPLauncher();
				launcher.start();
				frame.setVisible(false);
			}
		});
		
		pveBut = new JButton("2. start a new PvE game");
		pveBut.setHorizontalAlignment(SwingConstants.LEFT);
		pveBut.setFont(DEFAULT_FONT);
		pveBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Welcome to the Gobang game v1.5");
				System.out.println("You can undo the last piece by pressing 'r'.");
				var launcher = new GobangPvELauncher();
				launcher.start();
				frame.setVisible(false);
			}
		});
		
		replayerBut = new JButton("3. replay past game");
		replayerBut.setHorizontalAlignment(SwingConstants.LEFT);
		replayerBut.setFont(DEFAULT_FONT);
		replayerBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					var replayer = new GobangGameReplayer();
					replayer.start();
					frame.setVisible(false);
				} catch (Exception ex) {
				}
			}
		});
		
		functionBut = new JButton("4. more functions coming soon");
		functionBut.setHorizontalAlignment(SwingConstants.LEFT);
		functionBut.setFont(DEFAULT_FONT);
		
		panel.add(pvpBut);
		panel.add(pveBut);
		panel.add(replayerBut);
		panel.add(functionBut);
		frame.add(panel);
		frame.pack();
		frame.setVisible(true);
	}
	
	public static void main(String[] args) {
		new GobangGame();
	}
}
