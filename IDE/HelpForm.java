package IDE;

import javax.swing.JFrame;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * 
 * @author rohan
 */
public class HelpForm extends Form {
  HelpForm() {
    super();
  }

  @Override
  public void applyHumanInput(char pushed) {
    // nil
  }

  @Override
  public String[] formatLines() {
    int l = -1;
    String[] code = GUI.codeForm.formatLines();
    for (int i = 0; i < GUI.lineRegexHelp.size(); i++) {
      if (code[GUI.codeForm.humanLineNum()].matches(GUI.lineRegexHelp.get(i))) {
        l = i;
        break;
      }
    }
    if (l == -1) {
      return new String[] {};
    } else {

      return GUI.help.get(l).split("\n");
    }
  }

  @Override
  boolean canBackspace(Cursor cur) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  boolean canInsert(Cursor cur) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  void clear() {
    // TODO Auto-generated method stub

  }

  @Override
  void addProc(Process proc, JFrame f) {
    // TODO Auto-generated method stub

  }
}
