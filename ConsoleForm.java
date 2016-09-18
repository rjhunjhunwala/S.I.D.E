import java.io.IOException;
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
    curLineNum = 0;
    curColNum = 0;
    histColNum = 0;
    promptLine = 0;
    promptCol = 0;
  }

  @Override
  void addProc(final Process proc, final JFrame f) {
    new Thread("STDOUT") {
      public void run() {
        try {
          int next = 0;
          do {
            next = proc.getInputStream().read();
            if (next >= 0) {
              applyFormInput((char) next);
              updatePrompt();
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
              applyFormInput((char) next);
              updatePrompt();
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
  boolean canBackspace() {
    return curLineNum > promptLine
        || (curLineNum == promptLine && curColNum > promptCol);
  }

  @Override
  boolean canInsert() {
    return curLineNum > promptLine
        || (curLineNum == promptLine && curColNum >= promptCol);
  }

  void updatePrompt() {
    this.promptLine = curLineNum;
    this.promptCol = curColNum;
  }

}
