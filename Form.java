import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 * @author PhiNotPi
 */

public abstract class Form {

  final Font monospaced = new Font(Font.MONOSPACED, Font.PLAIN, 15);
  final Font font = monospaced;
  List<List<Character>> lines = new ArrayList<List<Character>>();
  int curLineNum = 0;
  int curColNum = 0;
  int histColNum = 0;
  int width = 80;

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

  void applyFormInput(String s) {
    for (Character pushed : s.toCharArray()) {
      applyFormInput(pushed);
    }
  }

  abstract void applyFormInput(char pushed);

  abstract void cursorLeft();

  abstract void cursorRight();

  abstract void cursorUp();

  abstract void cursorDown();

}
