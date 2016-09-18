/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JScrollPane;

/**
 * @author Rohans, PhiNotPi
 */
public class GUI {

  static final String[] LANGUAGES = new String[] { "SILOS" };
  static Form codeForm = new EditorForm();
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
    Panel consolePanel = new Panel(this, consoleForm);
    JScrollPane consoleScrollPane = new JScrollPane(consolePanel);
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
        codeScrollPane, consoleScrollPane);

    public Frame() {
      super("IDE (f5=save+compile+run/f6=save)");
      this.setVisible(true);
      this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
            file.writeStringArrayToFile(Silos.IDEFileName, focusForm()
                .toString().split("\n"));
            if (EditorForm.fileName != null)
              file.writeStringArrayToFile(EditorForm.fileName, focusForm()
                  .toString().split("\n"));
            try {
              consoleForm.clear();
              Process runtime = Runtime.getRuntime().exec(
                  "java -jar " + LANGUAGE + ".jar " + Silos.IDEFileName);
              consoleForm.addProc(runtime, f);
            } catch (Exception ex) {
              Logger.getLogger(GUI.class.getName()).log(Level.SEVERE, null, ex);
            }
            break;
          case 117: // f6
            if (EditorForm.fileName != null)
              file.writeStringArrayToFile(EditorForm.fileName, focusForm()
                  .toString().split("\n"));
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
            // focusForm().applyFormInput(":"+e.getExtendedKeyCode());
          }

          f.repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) {

        }

      });

      codeScrollPane.setBackground(Color.black);
      consoleScrollPane.setBackground(Color.black);
      split.setBackground(Color.black);
      this.add(split);
      this.pack();
      this.setBackground(Color.black);
      this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    }

    @Override
    public void repaint() {
      codeScrollPane.setViewportView(codePanel);
      consoleScrollPane.setViewportView(consolePanel);
      super.repaint();
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
      return new Dimension(max + 3, screenHeight);
    }

    @Override
    public void paintComponent(Graphics g) {

      g.setFont(form.font);
      g.setColor(Color.lightGray);
      String[] lines = form.formatLines();
      for (int i = 0; i < lines.length; i++) {
        prettyPrint(lines[i], i, g);
      }
      if (focusForm().equals(form)) {
        g.setColor(Color.white);
      } else {
        g.setColor(Color.gray);
      }
      printCursor(g);
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
      g.drawString(line, 0, i * 15 + 15);
    }

    private void printCursor(Graphics g) {
      int col = form.humanColNum();
      int line = form.humanLineNum();
      g.drawLine(col * 9, line * 15 + 3, col * 9, line * 15 + 18);
      g.drawLine(col * 9 - 2, line * 15 + 3, col * 9 + 2, line * 15 + 3);
      g.drawLine(col * 9 - 2, line * 15 + 18, col * 9 + 2, line * 15 + 18);
    }
  }

  public static final String LANGUAGE = (String) JOptionPane.showInputDialog(
      null, "Choose a language to develop in.", "Which language?",
      JOptionPane.QUESTION_MESSAGE, null, LANGUAGES, "SILOS");

  public static void runGUI() {

    Frame mainFrame = new Frame();
  }
}
