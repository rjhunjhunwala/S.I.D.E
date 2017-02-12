/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import IDE.Silos;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

/**
 * @author Rohans, PhiNotPi
 */
public class GUI {
 public static int command= 1;
	static ArrayList<String> lineRegexes = new ArrayList<>();
	static ArrayList<Color> lineColors = new ArrayList<>();
	static ArrayList<String> wordRegexes = new ArrayList<>();
	static ArrayList<Color> wordColors = new ArrayList<>();

	static final String[] LANGUAGES;
	static{
		LANGUAGES = file.getWordsFromFile("languages.txt");
	}
	static Form codeForm = new EditorForm();
	static Form helpForm = new HelpForm();
	static Form consoleForm = new ConsoleForm();
	static Form focusForm = codeForm;

	static Form focusForm() {
		return focusForm;
	}

	static void setFocusForm(Form f) {
		focusForm = f;
	}

	public static class Frame extends JFrame {

		Panel codePanel = new Panel(this, codeForm);
		JScrollPane codeScrollPane = new JScrollPane(codePanel);
		Panel helpPanel = new Panel(this, helpForm);
		JScrollPane helpScrollPane = new JScrollPane(helpPanel);
		JSplitPane lsplit = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
										codeScrollPane, helpScrollPane);
		Panel consolePanel = new Panel(this, consoleForm);
		JScrollPane consoleScrollPane = new JScrollPane(consolePanel);
		JSplitPane hsplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, lsplit,
										consoleScrollPane);

		public Frame() {
			super("IDE (f5=save+compile+run / f6=save)");
			this.setJMenuBar(buildMenuBar());
			this.setVisible(true);
			codeScrollPane.setBackground(Color.black);
			consoleScrollPane.setBackground(Color.black);
			hsplit.setBackground(Color.black);
			this.add(hsplit);
			this.pack();
			this.setBackground(Color.black);
			this.setExtendedState(JFrame.MAXIMIZED_BOTH);

			WindowListener exitListener = new WindowAdapter() {

				@Override
				public void windowClosing(WindowEvent e) {
					if (ConsoleForm.lastProcess != null) {
						ConsoleForm.lastProcess.destroy();
					}
					int dialogResult = JOptionPane.showConfirmDialog(null,
													"Would You Like to Save your Code first?", "Warning", 0);
					if (dialogResult == JOptionPane.YES_OPTION) {
						if (EditorForm.fileName != null && !EditorForm.fileName.equals("")) {
							file.writeToFile(EditorForm.fileName, codeForm
															.toString());
						} else {
							saveFileAs();
						}
					}
					System.exit(0);
				}
			};
			this.addWindowListener(exitListener);
			final Frame f = this;
			this.setFocusTraversalKeysEnabled(false);
			this.addKeyListener(new KeyListener() {
				@Override
				public void keyTyped(KeyEvent e) {
					char pushed = e.getKeyChar();
					if ((int) pushed == 22) {
						try {
							focusForm().applyHumanInput(
															(String) Toolkit.getDefaultToolkit().getSystemClipboard()
															.getData(DataFlavor.stringFlavor));
						} catch (UnsupportedFlavorException ex) {
							Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
						} catch (IOException ex) {
							Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
						}
					} else {
						focusForm().applyHumanInput(pushed);
					}
					f.repaint();
				}

				@Override
				public void keyPressed(KeyEvent e) {
					switch (e.getExtendedKeyCode()) {
						case 116: // f5
//							System.out.println("---code---");
//													System.out.print(codeForm.toString());
//													System.out.println("\n---code---");
							file.writeToFile(Silos.IDEFileName, codeForm
															.toString());
							if (EditorForm.fileName != null) {

								file.writeToFile(EditorForm.fileName, codeForm
																.toString());
							}
							try {

								consoleForm.clear();
								System.out.println(LANGUAGES[command] +" " + Silos.IDEFileName);
								Process runtime = Runtime.getRuntime().exec(
																LANGUAGES[command] +" testing.txt");

								consoleForm.addProc(runtime, f);
							} catch (Exception ex) {
								Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
							}
							break;
						case 117: // f6
							if (EditorForm.fileName != null) {
								file.writeToFile(EditorForm.fileName, codeForm
																.toString());
							}
							break;
						case 17: // ctrl button press; control *characters* handled separately
							break;
						case 37: // left arrow
							focusForm().humanCursorLeft();
							break;
						case 38: // up arrow
							focusForm().humanCursorUp();
							break;
						case 39: // right arrow
							focusForm().humanCursorRight();
							break;
						case 40: // down arrow
							focusForm().humanCursorDown();
							break;
						default:

					}

					f.repaint();
				}

				@Override
				public void keyReleased(KeyEvent e) {

				}

			});

		}

		@Override
		public void repaint() {
			codeScrollPane.setViewportView(codePanel);
			consoleScrollPane.setViewportView(consolePanel);
			super.repaint();
		}

	}

	public static void saveFileAs() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			File f = chooser.getSelectedFile();
			String fileName = "";
			try {
				fileName = f.getCanonicalPath();
			} catch (IOException ex) {
			}
			if (!"".equals(fileName)) {
				file.writeToFile(fileName, codeForm.toString());
				EditorForm.fileName = fileName;
			}
		}
	}

	public static class Panel extends JPanel {

		static int screenHeight, screenLength;
		Form form;
		Frame frame;

		public Panel(Frame frame, Form form) {
			this.setBackground(Color.black);
			this.frame = frame;
			this.form = form;
			addMouseListener(new MouseAdapter() {
				@Override
				public void mousePressed(MouseEvent e) {
					setFocusForm(getForm());
					getFrame().repaint();
				}

				@Override
				public void mouseReleased(MouseEvent e) {
				}
			});
		}

		Form getForm() {
			return form;
		}

		Frame getFrame() {
			return frame;
		}
public static final boolean USESCROLLBAR = false;
		@Override
		public Dimension getPreferredSize() {
			int max = this.getFontMetrics(form.font).stringWidth(
											new String(new char[80]).replace('\0', ' '));
			String[] lines = form.formatLines();
			for (int i = 0; i < lines.length; i++) {
				int cur = this.getFontMetrics(form.font).stringWidth(lines[i]);
				if (cur > max) {
					max = cur;
				}
			}
			return new Dimension(max + 3, USESCROLLBAR?lines.length*15:screenHeight);
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setFont(form.font);
			g.setColor(Color.lightGray);
			String[] lines = form.formatLines();
			int l = form.humanCursor.line * CHARHEIGHT + 350;
			for (int i = 0; i < lines.length; i++) {
				prettyPrint(l, lines[i], i, g);
			}
			if (focusForm().equals(form)) {
				g.setColor(Color.white);
			} else {
				g.setColor(Color.gray);
			}
			printCursor(g, l);
		}

		/**
		 * The following static block is used courtesy of stack overflow creative
		 * commons liscence http://stackoverflow.com/questions/3680221/how-can-i-get-
		 * the-monitor-size-in-java
		 */
		static {
			Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			double width = screenSize.getWidth();
			double height = screenSize.getHeight();
			screenHeight = (int) height + 2;
			// screenheight=768;
			screenLength = (int) width + 2;
			// screenlength=1024;
		}

		private void prettyPrint(int l, String line, int i, Graphics g) {
			int maxYHeight = this.getHeight();
			int y = i * CHARHEIGHT + CHARHEIGHT;
			if (l > maxYHeight) {
				y -= l;
				y += maxYHeight;
			}

			if (y > 0) {
				if (form.equals(codeForm)) {
					int lineMatch = -1;
					for (int in = 0; in < lineRegexes.size(); in++) {
						if (line.matches(lineRegexes.get(in))) {
							lineMatch = in;
							break;
						}
					}
					if (lineMatch != -1) {
						g.setColor(lineColors.get(lineMatch));
						g.drawString(line, 0, y);
					} else {
						int xDraw = 0;
						String[] words = line.split(" ");
						for (String w : words) {
							Color c = Color.white;
							int wordMatch = -1;
							for (int in = 0; in < wordRegexes.size(); in++) {
								if (w.matches(wordRegexes.get(in))) {
									wordMatch = in;
									break;
								}
							}
							if (wordMatch != -1) {
								c = wordColors.get(wordMatch);
							}
							g.setColor(c);
							g.drawString(w, xDraw, y);
							xDraw += w.length() * CHARWIDTH + CHARWIDTH;
						}
					}

				} else {
					g.drawString(line, 0, y);
				}
			}
		}

		private void printCursor(Graphics g, int l) {
			int col = form.humanColNum();
			int line = form.humanLineNum();
			int maxYHeight = getPreferredSize().height;
			int y = line * CHARHEIGHT;
			if (l > maxYHeight) {
				y -= l;
				y += maxYHeight;
			}
			g.drawLine(col * CHARWIDTH, y + 3, col * CHARWIDTH, y + 18);
			g.drawLine(col * CHARWIDTH - 2, y + 3, col * CHARWIDTH + 2, y + 3);
			g.drawLine(col * CHARWIDTH - 2, y + 18, col * CHARWIDTH + 2, y + 18);

		}
	}

	public static final int CHARWIDTH = 9;
	public static final int CHARHEIGHT = 15;
	public static String LANGUAGE = LANGUAGES[0];
//(String) JOptionPane.showInputDialog(
//		null, "Choose a language to develop in.", "Which language?",
//		JOptionPane.QUESTION_MESSAGE, null, LANGUAGES, "SILOS");

	public static void runGUI() {
		initializeStaticAnalysis(1);
		mainFrame = new Frame();
	}
	public static ArrayList<String> lineRegexHelp = new ArrayList<>();
	public static ArrayList<String> help = new ArrayList<>();

	public static void initializeStaticAnalysis(int index) {
		command = index;
		lineRegexes = new ArrayList<>();
		lineColors = new ArrayList<>();
		wordRegexes = new ArrayList<>();
		wordColors = new ArrayList<>();
		help = new ArrayList<>();
		lineRegexHelp = new ArrayList<>();
		String[] highlights = file.getWordsFromFile(LANGUAGE + ".COLOR.txt");
		boolean onLines = true;
		int counter = -1;
		if (highlights != null) {
			for (String highlight : highlights) {
				if (highlight.equals("END_LINES")) {
					onLines = false;
					continue;
				}
				counter++;
				boolean isEven = counter % 2 == 0;
				int x = 0, y = 0, z = 0;
				Color c = null;
				if (!isEven) {
					String[] split = highlight.split(" ");
					x = Integer.parseInt(split[0]);
					y = Integer.parseInt(split[1]);
					z = Integer.parseInt(split[2]);
					c = new Color(x, y, z);
				}
				if (onLines) {
					if (isEven) {
						lineRegexes.add(highlight);
					} else {
						lineColors.add(c);
					}

				} else if (isEven) {
					wordRegexes.add(highlight);
				} else {
					wordColors.add(c);
				}
			}

		}
		String[] docs = file.getWordsFromFile(LANGUAGE + ".DOCS.txt");

		if (docs != null) {
			for (int i = 0; i < docs.length-1; i++) {
				lineRegexHelp.add(docs[i]);
				help.add(docs[i + 1].replaceAll("NEWLINE", "\n"));
			}
		}
	}
	public static Frame mainFrame;

	public static JMenuBar buildMenuBar() {
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("File");
		menuBar.add(menu);

		JMenuItem menuItem;

		menuItem = new JMenuItem("New");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				codeForm.humanCursor.col = 0;
				codeForm.humanCursor.line = 0;
				codeForm.lines = new ArrayList<>();
				codeForm.lines.add(new ArrayList<Character>());
				EditorForm.fileName = null;
				saveFileAs();
			}
		});

		menu.add(menuItem);
		menuItem = new JMenuItem("Open");

		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String program = "";
				codeForm.humanCursor.col = 0;
				codeForm.humanCursor.line = 0;
				JFileChooser chooser = new JFileChooser();
				int returnVal = chooser.showOpenDialog(null);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File f = chooser.getSelectedFile();
					try {
						EditorForm.fileName = f.getCanonicalPath();
					} catch (IOException ex) {
						Logger.getLogger(EditorForm.class.getName())
														.log(Level.SEVERE, null, ex);
					}
					String[] lineArray = file.getWordsFromFile(f);
					for (int i = 0; i < lineArray.length; i++) {
						program += i == 0 ? lineArray[i] : "\n" + lineArray[i];
					}
					codeForm.lines = Form.getLinesFromString(program);
					codeForm.lines.add(new ArrayList<Character>());
				} else {
					codeForm.lines = new ArrayList<List<Character>>();
					codeForm.lines.add(new ArrayList<Character>());
				}

			}
		}
		);

		menu.add(menuItem);
		menuItem = new JMenuItem("Save");
		menuItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (EditorForm.fileName != null && !EditorForm.fileName.equals("")) {
					file.writeToFile(EditorForm.fileName, codeForm
													.toString());
				} else {
					saveFileAs();
				}
			}

		}
		);

		menu.add(menuItem);
		menuItem = new JMenuItem("Save As...");

		menuItem.addActionListener(
										new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent e
											) {
												saveFileAs();
											}

										}
		);
		menu.add(menuItem);
		menuItem = new JMenuItem("Exit");

		menuItem.addActionListener(
										new ActionListener() {

											@Override
											public void actionPerformed(ActionEvent e
											) {
												mainFrame.dispatchEvent(new WindowEvent(mainFrame, WindowEvent.WINDOW_CLOSING));
											}

										}
		);
		menu.add(menuItem);

		menu = new JMenu("Language");
		menuBar.add(menu);
		class LanguageButtonListener implements ActionListener {

			final String NAME;
final int index;
			
			LanguageButtonListener(String name, int in) {
				NAME = name;
				index = in;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				LANGUAGE = NAME;
				initializeStaticAnalysis(index);
			}

		}
		for (int i = 0;i<LANGUAGES.length;i+=2) {
			String s = LANGUAGES[i];
			menuItem = new JMenuItem(s);
			menuItem.addActionListener(new LanguageButtonListener(s,i+1));
			menu.add(menuItem);
		}
		menu = new JMenu("Advanced");
	menuItem = new JMenuItem("Additional languages");
	menuItem.addActionListener(new ActionListener(){
	@Override
	public void actionPerformed(ActionEvent e){
		try {
			java.awt.Desktop.getDesktop().browse(URI.create("https://tio.run/nexus/"+JOptionPane.showInputDialog(mainFrame, "Pick a language from TIO\n(Try it online!)", "Choose additional language",JOptionPane.INFORMATION_MESSAGE)));
		} catch (IOException ex) {
			Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
	});
	menu.add(menuItem);
		menuBar.add(menu);
	
		return menuBar;
	}

}
