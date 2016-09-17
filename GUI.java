/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package IDE;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Rohans
 */
public class GUI {

	static String program = "";

	public static class Frame extends JFrame {

		public Frame() {
			super("IDE (f5=save+compile+run/f6=save)");
			this.setVisible(true);
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			final Frame f = this;
			this.addKeyListener(
				new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char pushed = e.getKeyChar();
					String disallowedCharacters = "\b";
					if (disallowedCharacters.contains(pushed + "")) {
						if (program.length() > 0) {
							program = program.substring(0, program.length() - 1);
						}
					} else {
						program += pushed;
					}
					f.repaint();
				}

				@Override
				public void keyPressed(KeyEvent e) {
				}

				@Override
				public void keyReleased(KeyEvent e) {

				}

			}
			);
			this.add(new Panel());
			this.pack();
			this.setBackground(Color.black);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}

	public static class Panel extends JPanel {

		static int screenHeight, screenLength;

		public Panel() {
			this.setBackground(Color.black);
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(screenLength, screenHeight);
		}

		@Override
		public void paintComponent(Graphics g) {
			g.setColor(Color.WHITE);
			String[] lines = program.split("\n");
			for (int i = 0; i < lines.length; i++) {
				prettyPrint(lines[i], i, g);
			}
		}

		/**
		 * The following static block is used courtesy of stack overflow creative
		 * commons liscence
		 * http://stackoverflow.com/questions/3680221/how-can-i-get-the-monitor-size-in-java
		 */
		static {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double width = screenSize.getWidth();
			double height = screenSize.getHeight();
			screenHeight = (int) height + 2;
//screenheight=768;		
			screenLength = (int) width + 2;
			//screenlength=1024;
		}

		private void prettyPrint(String line, int i, Graphics g) {
			g.drawString(line, 0, i * 15 + 10);
		}
	}

	public static void runGUI() {
		Frame mainFrame = new Frame();
	}
}
