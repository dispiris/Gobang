package gobang;
/** {@code GobangGame} is the general game which contains multiple part
 * @author Xinkai Zhang
 * @version 1.4
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
	private JButton launcherBut;
	private JButton replayerBut;
	private JButton functionBut;
	
	private static final Font DEFAULT_FONT = new Font("Arial", Font.ITALIC, 40);
	
	public GobangGame() {
		frame = new JFrame("Gobang game v1.4");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		panel = new JPanel(new GridLayout(4, 1));
		text = new JTextField("   Welcome to Gobang game v1.4   ");
		text.setHorizontalAlignment(SwingConstants.LEFT);
		text.setEditable(false);
		text.setFont(new Font("Arial", Font.PLAIN, 50));
		panel.add(text);
		
		launcherBut = new JButton("1. start new PvP game");
		launcherBut.setHorizontalAlignment(SwingConstants.LEFT);
		launcherBut.setFont(DEFAULT_FONT);
		launcherBut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("Welcome to the Gobang game v1.4");
				System.out.println("You can undo the last piece by pressing 'r'.");
				var launcher = new GobangGameLauncher();
				launcher.start();
				frame.setVisible(false);
			}
		});
		
		replayerBut = new JButton("2. replay past game");
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
		
		functionBut = new JButton("3. more functions coming soon");
		functionBut.setHorizontalAlignment(SwingConstants.LEFT);
		functionBut.setFont(DEFAULT_FONT);
		
		panel.add(launcherBut);
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
