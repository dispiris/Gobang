package utility;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Images {
	
	public static final int SIZE = 15; 
	public static final int MARGIN = 100; 
	public static final int WIDTH = 60; 
	public static final int RADIUS = 20; 
	public static final int PANEL_SIZE = 2 * MARGIN + (SIZE - 1) * WIDTH;
	public static final Color BACKGROUND_COLOR = new Color(222, 184, 135);	// color of goldwood
	
	public static final BufferedImage BOARD_IMAGE;
	static {
		BOARD_IMAGE = new BufferedImage(PANEL_SIZE, PANEL_SIZE, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = BOARD_IMAGE.createGraphics();
		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g2d.setColor(BACKGROUND_COLOR);
		g2d.fill(new Rectangle2D.Double(0, 0, PANEL_SIZE, PANEL_SIZE));
		g2d.setColor(Color.BLACK);

		// four boundary lines
		g2d.setStroke(new BasicStroke(5));
		g2d.draw(new Line2D.Double(MARGIN, MARGIN, MARGIN + WIDTH * 14, MARGIN));
		g2d.draw(new Line2D.Double(MARGIN, MARGIN + WIDTH * (SIZE - 1), MARGIN + WIDTH * 14, MARGIN + WIDTH * (SIZE - 1)));
		g2d.draw(new Line2D.Double(MARGIN, MARGIN, MARGIN, MARGIN + WIDTH * 14));
		g2d.draw(new Line2D.Double(MARGIN + WIDTH * (SIZE - 1), MARGIN, MARGIN + WIDTH * (SIZE - 1), MARGIN + WIDTH * 14));
		
		g2d.setStroke(new BasicStroke(2));
		for (int i = 1; i < SIZE - 1; i++) {
			g2d.draw(new Line2D.Double(MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14, MARGIN + WIDTH * i));
		}
		for (int i = 1; i < SIZE - 1; i++) {
			g2d.draw(new Line2D.Double(MARGIN + WIDTH * i, MARGIN, MARGIN + WIDTH * i, MARGIN + WIDTH * 14));
		}
		
		g2d.setColor(Color.BLACK);
		g2d.fillOval(MARGIN + 7 * WIDTH - 8, MARGIN + 7 * WIDTH - 8, 16, 16);
		g2d.fillOval(MARGIN + 3 * WIDTH - 8, MARGIN + 3 * WIDTH - 8, 16, 16);
		g2d.fillOval(MARGIN + 11 * WIDTH - 8, MARGIN + 11 * WIDTH - 8, 16, 16);
		g2d.fillOval(MARGIN + 3 * WIDTH - 8, MARGIN + 11 * WIDTH - 8, 16, 16);
		g2d.fillOval(MARGIN + 11 * WIDTH - 8, MARGIN + 3 * WIDTH - 8, 16, 16);
		
		g2d.setFont(new Font("TimesRoman", Font.BOLD, 25));
		for (int i = 0; i <= 9; i++) {
			g2d.drawString("" + i, MARGIN + WIDTH * i - 7, MARGIN - 35);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			g2d.drawString("" + i, MARGIN + WIDTH * i - 15, MARGIN - 35);
		}
		for (int i = 0; i <= 9; i++) {
			g2d.drawString("" + i, MARGIN - 45, MARGIN + WIDTH * i + 7);
		}
		for (int i = 10; i <= SIZE - 1; i++) {
			g2d.drawString("" + i, MARGIN - 55, MARGIN + WIDTH * i + 7);
		}
		g2d.dispose();
	}
	
}
