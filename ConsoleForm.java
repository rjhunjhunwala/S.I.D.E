import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;

/**
 * @author PhiNotPi
 */

public class ConsoleForm extends Form {

  int promptCol = 0;
  int promptLine = 0;
  Cursor progCursor = new Cursor();
  OutputStream STDIN;
  ArrayList<Byte> inputAccumulator = new ArrayList<Byte>();
  int inputSep = 10; // newline

  public ConsoleForm() {
    lines.add(new ArrayList<Character>());
  }

  public ConsoleForm(int width) {
    lines.add(new ArrayList<Character>());
    this.width = width;
  }

  @Override
  void clear() {
    lines = new ArrayList<List<Character>>();
    lines.add(new ArrayList<Character>());
    humanCursor = new Cursor();
    progCursor = new Cursor();
    promptLine = 0;
    promptCol = 0;
    inputAccumulator = new ArrayList<Byte>();
  }

  @Override
  void addProc(final Process proc, final JFrame f) {
    STDIN = proc.getOutputStream();
    new Thread("STDOUT") {
      public void run() {
        try {
          int next = 0;
          do {
            next = proc.getInputStream().read();
            if (next >= 0) {
              applyFormInput(progCursor, (char) next);
              updatePrompt(progCursor);
              if (proc.getInputStream().available() == 0) {
                f.repaint();
              }
            }
          } while (next >= 0);
        } catch (Exception e) {
          e.printStackTrace();
        }
        f.repaint();
      }
    }.start();
    new Thread("STDERR") {
      public void run() {
        try {
          int next = 0;
          do {
            next = proc.getErrorStream().read();
            if (next >= 0) {
              applyFormInput(progCursor, (char) next);
              updatePrompt(progCursor);
              if (proc.getErrorStream().available() == 0) {
                f.repaint();
              }
            }
          } while (next >= 0);
        } catch (Exception e) {
          e.printStackTrace();
        }
        f.repaint();
      }
    }.start();
  }

  @Override
  boolean canBackspace(Cursor cur) {
    return cur.line > promptLine
        || (cur.line == promptLine && cur.col > promptCol);
  }

  @Override
  boolean canInsert(Cursor cur) {
    return cur.line > promptLine
        || (cur.line == promptLine && cur.col >= promptCol);
  }

  void updatePrompt(Cursor cur) {
    progCursor.match(cur);
    humanCursor.floor(progCursor);
    this.promptLine = progCursor.line;
    this.promptCol = progCursor.col;
  }

  @Override
  void applyHumanInput(char pushed) {
    if (STDIN != null) {
      applyFormInput(humanCursor, pushed);
      inputAccumulator.add((byte) pushed);
      if ((int) pushed == inputSep) {
        byte[] in = new byte[inputAccumulator.size()];
        for (int i = 0; i < in.length; i++) {
          in[i] = inputAccumulator.get(i);
        }
        try {
          STDIN.write(in);
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        inputAccumulator.clear();
        updatePrompt(humanCursor);
      }
    }
  }

}
