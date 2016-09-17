/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
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
		switch(e.getExtendedKeyCode()){
				case 116:

				case 117:
				case 17:
					return;
				default:

			}					
					
					char pushed = e.getKeyChar();
					if (pushed =='\b') {
						if (program.length() > 0) {
							program = program.substring(0, program.length() - 1);
						}
					} else {
		if(Character.isBmpCodePoint(pushed))
						program += pushed;
					}
					f.repaint();
				}

				@Override
				public void keyPressed(KeyEvent e) {
				switch(e.getExtendedKeyCode()){
				case 116:
					//f5
		file.writeStringArrayToFile("THIS IS A TEMPORARY FILE",program.split("\n"));
				{
					try {
						//		Silos.main("THIS IS A TEMPORARY FILE");
						Process p = Runtime.getRuntime().exec("java Silos.java \"THIS IS A TEMPORARY FILE\"");
					} catch (IOException ex) {
						Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
					}
				}
		break;
				case 117:
					//f6
					break;
				case 17:
			{
				try {
					program += (String) Toolkit.getDefaultToolkit()
						.getSystemClipboard().getData(DataFlavor.stringFlavor);
				} catch (UnsupportedFlavorException ex) {
					Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
				} catch (IOException ex) {
					Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
				}
			} 
			}					
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

	public static void runGUI()  {
    Silos.safeModeEnabled=false;
		try {
			Thread.sleep(10);
		} catch (InterruptedException ex) {
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
		Frame mainFrame = new Frame();
		
	}
}