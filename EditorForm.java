import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;

/**
 * @author PhiNotPi
 */

public class EditorForm extends Form {

  public EditorForm() {
    lines.add(new ArrayList<Character>());
  }

  public EditorForm(int width) {
    lines.add(new ArrayList<Character>());
    this.width = width;
  }

  @Override
  void clear() {
    List<List<Character>> lines = new ArrayList<List<Character>>();
    lines.add(new ArrayList<Character>());
    curLineNum = 0;
    curColNum = 0;
    histColNum = 0;
  }

  @Override
  void addProc(Process proc, JFrame f) {
    // TODO Auto-generated method stub

  }

  @Override
  boolean canBackspace() {
    return true;
  }

  @Override
  boolean canInsert() {
    return true;
  }

}
