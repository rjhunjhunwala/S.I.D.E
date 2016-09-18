import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/**
 * @author PhiNotPi
 */

public abstract class Form {
  static final int TAB_WIDTH = 4;
  final Font monospaced = new Font(Font.MONOSPACED, Font.PLAIN, 15);
  final Font font = monospaced;
  List<List<Character>> lines = new ArrayList<List<Character>>();
  Cursor humanCursor = new Cursor();
  int width = 80;

  public static List<List<Character>> getLinesFromString(String s) {
    List<List<Character>> out = new ArrayList<>();
    String[] lineArray = s.split("\n");
    for (String line : lineArray) {
      out.add(new ArrayList<Character>());
      for (char c : line.toCharArray()) {
        out.get(out.size() - 1).add(c);
      }
    }
    return out;
  }

  static class Cursor {
    int line = 0;
    int col = 0;
    int histcol = 0; // based on fancy formatting

    Cursor() {
    }

    Cursor(int l, int c) {
      line = l;
      col = c;
    }

    void match(Cursor cur) {
      line = cur.line;
      col = cur.col;
      histcol = cur.histcol;
    }

    void floor(Cursor cur) {
      if (line < cur.line || (line == cur.line && col < cur.col)) {
        match(cur);
      }
    }

  }

  int humanColNum() {
    return fancyColNum(humanCursor.line, humanCursor.col);
  }

  int fancyColNum(int line, int col) {
    int width = 0;
    for (int c = 0; c < col; c++) {
      char ch = lines.get(line).get(c);
      if ((int) ch == 9) {
        do {
          width++;
        } while (width % TAB_WIDTH != 0);
      } else {
        width++;
      }
    }
    return width;
  }

  int humanLineNum() {
    return fancyLineNum(humanCursor.line);
  }

  int fancyLineNum(int line) {
    return line;
  }

  int defancyColNum(int line, int goal) {
    // inverse of fancyColNum, used for vertical movement
    int width = 0;
    int c = 0;
    while (width < goal && c < lines.get(line).size()) {
      char ch = lines.get(line).get(c);
      if ((int) ch == 9) {
        do {
          width++;
        } while (width % TAB_WIDTH != 0);
      } else {
        width++;
      }
      c++;
    }
    if (width > goal) {
      c--;
    }
    return c;
  }

  int defancyLineNum(int line) {
    return line;
  }

  List<Character> curLine(Cursor cur) {
    return lines.get(cur.line);
  }

  public String toString() {
    String res = "";
    for (int l = 0; l < lines.size(); l++) {
      if (l > 0) {
        res += "\n";
      }
      for (int c = 0; c < lines.get(l).size(); c++) {
        res += lines.get(l).get(c);
      }
    }
    return res;
  }

  abstract boolean canBackspace(Cursor cur);

  abstract boolean canInsert(Cursor cur);

  public String[] formatLines() {
    ArrayList<String> res = new ArrayList<String>();
    for (int l = 0; l < lines.size(); l++) {
      int width = 0;
      String ln = "";
      for (int c = 0; c < lines.get(l).size(); c++) {
        char ch = lines.get(l).get(c);
        if ((int) ch == 9) {
          ln += (char) -21;
          width++;
          while (width % TAB_WIDTH != 0) {
            ln += " ";
            width++;
          }
        } else {
          ln += ch;
          width++;
        }
      }
      res.add(ln);
    }
    return (String[]) res.toArray(new String[] {});
  }

  void applyFormInput(Cursor cur, String s) {
    for (Character pushed : s.toCharArray()) {
      applyFormInput(cur, pushed);
    }
  }

  void applyFormInput(Cursor cur, char pushed) {
    if ((int) pushed == 8) {
      if (canBackspace(cur)) {
        if (cur.col == 0 && cur.line != 0) {
          int newColNum = lines.get(cur.line - 1).size();
          lines.get(cur.line - 1).addAll(curLine(cur));
          lines.remove(cur.line);
          cur.line--;
          cur.col = newColNum;
        } else if (cur.col > 0) {
          curLine(cur).remove(cur.col - 1);
          cur.col--;
        }
      }
    } else if (canInsert(cur)) {
      if ((int) pushed == 127) {
        // add delete functionality
      } else if ((int) pushed == 10) {
        List<Character> newLine = curLine(cur).subList(cur.col,
            curLine(cur).size());
        lines.add(cur.line + 1, new ArrayList<Character>(newLine));
        newLine.clear();
        cur.line++;
        cur.col = 0;
      } else if ((int) pushed == 9 || (int) pushed >= 32) {
        curLine(cur).add(cur.col, pushed);
        cur.col++;
      } else {
        // curLine().add(cur.col, '?');
        // applyFormInput(";"+(int)pushed);
        // cur.col++;
      }
    }
    cur.histcol = fancyColNum(cur.line, cur.col);
  }

  void applyHumanInput(String s) {
    for (Character pushed : s.toCharArray()) {
      applyHumanInput(pushed);
    }
  }

  abstract void applyHumanInput(char pushed);

  void humanCursorLeft() {
    cursorLeft(humanCursor);
  }

  void cursorLeft(Cursor cur) {
    if (cur.col == 0 && cur.line != 0) {
      cur.line--;
      cur.col = curLine(cur).size();
    } else if (cur.col > 0) {
      cur.col--;
    }
    cur.histcol = fancyColNum(cur.line, cur.col);
  }

  void humanCursorRight() {
    cursorRight(humanCursor);
  }

  void cursorRight(Cursor cur) {
    if (cur.col == curLine(cur).size() && cur.line < lines.size() - 1) {
      cur.line++;
      cur.col = 0;
    } else if (cur.col < curLine(cur).size()) {
      cur.col++;
    }
    cur.histcol = fancyColNum(cur.line, cur.col);
  }

  void humanCursorUp() {
    cursorUp(humanCursor);
  }

  void cursorUp(Cursor cur) {
    if (cur.line > 0) {
      cur.line--;
      int targCol = defancyColNum(cur.line, cur.histcol);
      if (targCol > curLine(cur).size()) {
        cur.col = curLine(cur).size();
      } else {
        cur.col = targCol;
      }
    }
  }

  void humanCursorDown() {
    cursorDown(humanCursor);
  }

  void cursorDown(Cursor cur) {
    if (cur.line < lines.size() - 1) {
      cur.line++;
      int targCol = defancyColNum(cur.line, cur.histcol);
      if (targCol > curLine(cur).size()) {
        cur.col = curLine(cur).size();
      } else {
        cur.col = targCol;
      }
    }
  }

  void cursorEnd(Cursor cur) {
    cur.col = lines.get(cur.line).size();
  }

  void cursorHome(Cursor cur) {
    cur.col = 0;
  }

  abstract void clear();

  abstract void addProc(Process proc, JFrame f);

}
