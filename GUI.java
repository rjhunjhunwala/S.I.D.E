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
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * 
 * @author Rohans, PhiNotPi
 */
public class GUI {

  static List<List<Character>> progLines = new ArrayList<List<Character>>();
  static int curLineNum = 0;
  static int curColNum = 0;
  static {
    progLines.add(new ArrayList<Character>());
  }

  static List<Character> curLine() {
    return progLines.get(curLineNum);
  }

  static String program() {
    String res = "";
    for (int l = 0; l < progLines.size(); l++) {
      if (l > 0) {
        res += "\n";
      }
      for (int c = 0; c < progLines.get(l).size(); c++) {
        res += progLines.get(l).get(c);
      }
    }
    return res;
  }

  static void applyFormInput(String s) {
    for (Character pushed : s.toCharArray()) {
      applyFormInput(pushed);
    }
  }

  static void applyFormInput(char pushed) {
    if ((int) pushed == 8) {
      if (curColNum == 0 && curLineNum != 0) {
        int newColNum = progLines.get(curLineNum - 1).size();
        progLines.get(curLineNum - 1).addAll(curLine());
        progLines.remove(curLineNum);
        curLineNum--;
        curColNum = newColNum;
      } else if (curColNum > 0) {
        curLine().remove(curColNum - 1);
        curColNum--;
      }
    } else if ((int) pushed == 127) {
      // add delete functionality
    } else if ((int) pushed == 10) {
      List<Character> newLine = curLine().subList(curColNum, curLine().size());
      progLines.add(curLineNum + 1, new ArrayList<Character>(newLine));
      newLine.clear();
      curLineNum++;
      curColNum = 0;
    } else if ((int) pushed >= 32) {
      curLine().add(curColNum, pushed);
      curColNum++;
    } else {
      curLine().add(curColNum, '?');
      curColNum++;
    }
  }

  static void cursorLeft() {
    if (curColNum == 0 && curLineNum != 0) {
      curLineNum--;
      curColNum = curLine().size();
    } else if (curColNum > 0) {
      curColNum--;
    }
  }

  static void cursorRight() {
    if (curColNum == curLine().size() && curLineNum != progLines.size()) {
      curLineNum++;
      curColNum = 0;
    } else if (curColNum < curLine().size()) {
      curColNum++;
    }
  }

  public static class Frame extends JFrame {

    public Frame() {
      super("IDE (f5=save+compile+run/f6=save)");
      this.setVisible(true);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      final Frame f = this;
      this.addKeyListener(new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {
          char pushed = e.getKeyChar();
          applyFormInput(pushed);
          f.repaint();
        }

        @Override
        public void keyPressed(KeyEvent e) {
          switch (e.getExtendedKeyCode()) {
          case 116: // f5
            file.writeStringArrayToFile("THIS IS A TEMPORARY FILE", program()
                .split("\n"));
            Silos.main("THIS IS A TEMPORARY FILE");
            break;
          case 117: // f6
            break;
          case 17:
            try {
              applyFormInput((String) Toolkit.getDefaultToolkit()
                  .getSystemClipboard().getData(DataFlavor.stringFlavor));
            } catch (UnsupportedFlavorException ex) {
              Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
              Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
          case 37: // left arrow
            cursorLeft();
            break;
          case 39: // left arrow
            cursorRight();
            break;
          }

          f.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

      });
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
      String[] lines = program().split("\n");
      for (int i = 0; i < lines.length; i++) {
        prettyPrint(lines[i], i, g);
      }
    }

    /**
     * The following static block is used courtesy of stack overflow creative
     * commons liscence
     * http://stackoverflow.com/questions/3680221/how-can-i-get-
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

    private void prettyPrint(String line, int i, Graphics g) {
      g.drawString(line, 0, i * 15 + 10);
    }
  }

  public static void runGUI() {
    Silos.safeModeEnabled = false;
    Frame mainFrame = new Frame();

  }
}
