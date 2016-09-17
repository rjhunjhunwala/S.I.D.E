import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PhiNotPi
 */

public class ConsoleForm extends Form {

  public ConsoleForm() {
    lines.add(new ArrayList<Character>());
  }

  public ConsoleForm(int width) {
    lines.add(new ArrayList<Character>());
    this.width = width;
  }

  List<Character> curLine() {
    return lines.get(curLineNum);
  }

  public String toString() {
    String res = "";
    // res += curLineNum + " " + curColNum + " " + histColNum + "\n";
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

  // public String[] formatLines() {
  // ArrayList<String> res = new ArrayList<String>();
  //
  // return (String[]) res.toArray();
  // }

  void applyFormInput(String s) {
    for (Character pushed : s.toCharArray()) {
      applyFormInput(pushed);
    }
  }

  void applyFormInput(char pushed) {
    if ((int) pushed == 8) {
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
    } else if ((int) pushed == 127) {
      // add delete functionality
    } else if ((int) pushed == 10) {
      List<Character> newLine = curLine().subList(curColNum, curLine().size());
      lines.add(curLineNum + 1, new ArrayList<Character>(newLine));
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
    histColNum = curColNum;
  }

  void cursorLeft() {
    if (curColNum == 0 && curLineNum != 0) {
      curLineNum--;
      curColNum = curLine().size();
    } else if (curColNum > 0) {
      curColNum--;
    }
    histColNum = curColNum;
  }

  void cursorRight() {
    if (curColNum == curLine().size() && curLineNum < lines.size() - 1) {
      curLineNum++;
      curColNum = 0;
    } else if (curColNum < curLine().size()) {
      curColNum++;
    }
    histColNum = curColNum;
  }

  void cursorUp() {
    if (curLineNum > 0) {
      curLineNum--;
      if (histColNum > curLine().size()) {
        curColNum = curLine().size();
      } else {
        curColNum = histColNum;
      }
    }
  }

  void cursorDown() {
    if (curLineNum < lines.size() - 1) {
      curLineNum++;
      if (histColNum > curLine().size()) {
        curColNum = curLine().size();
      } else {
        curColNum = histColNum;
      }
    }
  }

}
