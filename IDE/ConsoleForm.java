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

  Cursor progCursor = new Cursor();
  OutputStream STDIN;
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
  if(lastProcess!=null){
			lastProcess.destroyForcibly();
		}
			lines = new ArrayList<List<Character>>();
    lines.add(new ArrayList<Character>());
    humanCursor = new Cursor();
    progCursor = new Cursor();
  }
static Process lastProcess = null;
  @Override
  void addProc(final Process proc, final JFrame f) {
   lastProcess = proc; 
  //STDIN = new OutputStream();
			new Thread("STDOUT") {
      public void run() {
        try {
          int next = 0;
          do {
            next = proc.getInputStream().read();
            if (next >= 0) {
              applyFormInput(progCursor, (char) next,
                  new Cursor[] { humanCursor });
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
              applyFormInput(progCursor, (char) next,
                  new Cursor[] { humanCursor });
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
  STDIN = (proc.getOutputStream());
		}

  @Override
  boolean canBackspace(Cursor cur) {
    return cur.compareTo(progCursor) > 0;
  }

  @Override
  boolean canInsert(Cursor cur) {
    return cur.compareTo(progCursor) >= 0;
  }

  void updatePrompt(Cursor cur) {
    progCursor.match(cur);
    humanCursor.floor(progCursor);
  }

  @Override
  void applyHumanInput(char pushed) {

    if (STDIN != null) {
     			System.out.println(pushed);
					applyFormInput(humanCursor, pushed, new Cursor[] {});
      if ((int) pushed == inputSep) {
        ArrayList<Byte> input = inputAccumulator();
        byte[] in = new byte[input.size()];
        for (int i = 0; i < in.length; i++) {
          in[i] = input.get(i);
        }
        try {
          STDIN.write(in);
          STDIN.flush();
        } catch (IOException e) {
          STDIN = null;
          while (canBackspace(humanCursor)) {
            applyFormInput(humanCursor, (char) 8, new Cursor[] {});
          }
        }
        updatePrompt(humanCursor);
      }
    }
  }

  ArrayList<Byte> inputAccumulator() {
    ArrayList<Byte> res = new ArrayList<Byte>();
    for (Cursor cur = progCursor.clone(); cur.compareTo(humanCursor) < 0; cursorRight(cur)) {
      res.add((byte) (char) curChar(cur));
    }
    return res;
  }

}
