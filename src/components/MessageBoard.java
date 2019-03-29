package components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;
import javax.swing.Timer;

import utility.MessageEventListener;

@SuppressWarnings("serial")
public class MessageBoard extends JTextField implements MessageEventListener {

	public static final Color WINNER_DISPLAY_COLOR = new Color(96,40,30);
	
	public MessageBoard(String func) {
		super();
		setBackground(new Color(222, 184, 135));
		setPreferredSize(new Dimension(1040, 150));
		setFont(new Font("Arial", Font.PLAIN, 50));
		setEditable(false);
		if (func.equals("launch")) {
			setText("          Welcome to Gobang game v1.4");
			var timer = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setText("   you can take back last piece by pressing R");
				}
			});
			timer.setRepeats(false);
			timer.start();
			var timer2 = new Timer(4000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setText("            waiting for the BLACK player...");
				}
			});
			timer2.setRepeats(false);
			timer2.start();
		} else if (func.equals("replay")) {
			setText("          Welcome to Gobang game v1.4");
			var timer = new Timer(2000, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					setText("      starting to replay...");
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	@Override
	public void onMessageEvent(String mess, boolean isWinnerInformation) {
		if (isWinnerInformation) {
			setForeground(WINNER_DISPLAY_COLOR);
			setFont(new Font("Algerian", Font.ITALIC, 100));
		}
		setText(mess);
	}
	
}
