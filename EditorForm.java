import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

/**
 * @author PhiNotPi
 */

public class EditorForm extends Form {

  public static String fileName;

  public EditorForm() {
    String program = "";

    JFileChooser chooser = new JFileChooser();
    int returnVal = chooser.showOpenDialog(null);
    if (returnVal == JFileChooser.APPROVE_OPTION) {
      File f = chooser.getSelectedFile();
      try {
        fileName = f.getCanonicalPath();
      } catch (IOException ex) {
        Logger.getLogger(EditorForm.class.getName())
            .log(Level.SEVERE, null, ex);
      }
      String[] lineArray = file.getWordsFromFile(f);
      for (int i = 0; i < lineArray.length; i++) {
        program += i == 0 ? lineArray[i] : "\n" + lineArray[i];
      }
      lines = Form.getLinesFromString(program);
      lines.add(new ArrayList<Character>());
    } else {
      lines = new ArrayList<List<Character>>();
      lines.add(new ArrayList<Character>());
    }
  }

  public EditorForm(int width) {
    lines.add(new ArrayList<Character>());
    this.width = width;
  }

  @Override
  void clear() {
    List<List<Character>> lines = new ArrayList<List<Character>>();
    lines.add(new ArrayList<Character>());
    humanCursor = new Cursor();
  }

  @Override
  void addProc(Process proc, JFrame f) {
    // TODO Auto-generated method stub
  }

  @Override
  boolean canBackspace(Cursor cur) {
    return true;
  }

  @Override
  boolean canInsert(Cursor cur) {
    return true;
  }

  @Override
  void applyHumanInput(char pushed) {
    applyFormInput(humanCursor, pushed, new Cursor[] {});
  }

}
