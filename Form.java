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
  int curLineNum = 0;
  int curColNum = 0;
  int histColNum = 0; // based on fancy formatting
  int width = 80;
public static List<List<Character>> getLinesFromString(String s){
	List<List<Character>> out = new ArrayList<>();
	String[] lineArray = s.split("\n");
	for(String line:lineArray){
	 out.add(new ArrayList<Character>());
		for(char c:line.toCharArray()){
			out.get(out.size()-1).add(c);
		}
	}
	return out;
}
  int fancyColNum() {
    int width = 0;
    for (int c = 0; c < curColNum; c++) {
      char ch = curLine().get(c);
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

  int fancyLineNum() {
    return curLineNum;
  }

  int defancyColNum(int goal) {
    // inverse of fancyColNum, used for vertical movement
    int width = 0;
    int c = 0;
    while (width < goal && c < curLine().size()) {
      char ch = curLine().get(c);
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

  int defancyLineNum() {
    return curLineNum;
  }

  List<Character> curLine() {
    return lines.get(curLineNum);
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

  abstract boolean canBackspace();

  abstract boolean canInsert();

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

  void applyFormInput(String s) {
    for (Character pushed : s.toCharArray()) {
      applyFormInput(pushed);
    }
  }

  void applyFormInput(char pushed) {
    if ((int) pushed == 8) {
      if (canBackspace()) {
        if (curColNum == 0 && curLineNum != 0) {
          int newColNum = lines.get(curLineNum - 1).size();
          lines.get(curLineNum - 1).addAll(curLine());
          lines.remove(curLineNum);
          curLineNum--;
          curColNum = newColNum;
        } else if (curColNum > 0) {
          curLine().remove(curColNum - 1);
          curColNum--;
        }
      }
    } else if (canInsert()) {
      if ((int) pushed == 127) {
        // add delete functionality
      } else if ((int) pushed == 10) {
        List<Character> newLine = curLine()
            .subList(curColNum, curLine().size());
        lines.add(curLineNum + 1, new ArrayList<Character>(newLine));
        newLine.clear();
        curLineNum++;
        curColNum = 0;
      } else if ((int) pushed == 9 || (int) pushed >= 32) {
        curLine().add(curColNum, pushed);
        curColNum++;
      } else {
        // curLine().add(curColNum, '?');
        // applyFormInput(";"+(int)pushed);
        // curColNum++;
      }
    }
    histColNum = fancyColNum();
  }

  void cursorLeft() {
    if (curColNum == 0 && curLineNum != 0) {
      curLineNum--;
      curColNum = curLine().size();
    } else if (curColNum > 0) {
      curColNum--;
    }
    histColNum = fancyColNum();
  }

  void cursorRight() {
    if (curColNum == curLine().size() && curLineNum < lines.size() - 1) {
      curLineNum++;
      curColNum = 0;
    } else if (curColNum < curLine().size()) {
      curColNum++;
    }
    histColNum = fancyColNum();
  }

  void cursorUp() {
    if (curLineNum > 0) {
      curLineNum--;
      int targCol = defancyColNum(histColNum);
      if (targCol > curLine().size()) {
        curColNum = curLine().size();
      } else {
        curColNum = targCol;
      }
    }
  }

  void cursorDown() {
    if (curLineNum < lines.size() - 1) {
      curLineNum++;
      int targCol = defancyColNum(histColNum);
      if (targCol > curLine().size()) {
        curColNum = curLine().size();
      } else {
        curColNum = targCol;
      }
    }
  }

  void cursorEnd() {
    curColNum = lines.get(curLineNum).size();
  }

  void cursorHome() {
    curColNum = 0;
  }

  abstract void clear();

  abstract void addProc(Process proc, JFrame f);

}
