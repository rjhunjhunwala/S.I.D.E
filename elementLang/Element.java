package elementLang;

import java.util.ArrayList;

import simpleLanguage.Command;
import simpleLanguage.DataStructure;
import simpleLanguage.SimpleCmd;
import simpleLanguage.SimpleInt;
import simpleLanguage.SimpleLanguage;
import simpleLanguage.SimpleMap;
import simpleLanguage.SimpleScalar;
import simpleLanguage.SimpleStack;
import simpleLanguage.SimpleStkCmd;
import simpleLanguage.SimpleStr;
import simpleLanguage.Status;

/**
 * @author PhiNotPi
 */
public class Element extends SimpleLanguage {

  public static void main(String[] args) {
    Element e = new Element();
    ArrayList<Command> cmds = e.splitCommands("3'_\":`````");
    System.out.println(cmds);
    e.applyCmds(cmds);
    System.out.println(e.mainStack.knownData);
    System.out.println(e.ctrlStack.knownData);

    for (Command c : cmds) {
      System.out.print(c.token);
    }
    System.out.println();
    for (Command c : cmds) {
      for (int i = 0; i < c.token.length(); i++) {
        char out = '?';
        if (c.getStatus() == Status.OK) {
          out = '.';
        } else if (c.getStatus() == Status.WARN) {
          out = 'W';
        } else if (c.getStatus() == Status.ERROR) {
          out = 'E';
        }
        System.out.print(out);
      }
    }
  }

  SimpleStack mainStack = new SimpleStack(SimpleScalar.class, "main");
  SimpleStack ctrlStack = new SimpleStack(SimpleScalar.class, "ctrl");
  SimpleMap<SimpleScalar, SimpleScalar> varsMap = new SimpleMap<SimpleScalar, SimpleScalar>(
      "vars");

  Element() {
    addDS(mainStack);
    addDS(ctrlStack);
    addDS(varsMap);
  }

  @Override
  protected boolean isValidSyntax(String source) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  protected ArrayList<Command> splitCommands(String source) {
    ArrayList<Command> res = new ArrayList<Command>();
    String curLit = "";
    boolean isEsc = false;
    int lastCmd = -1;
    for (int i = 0; i < source.length(); i++) {
      if (isEsc
          || " ~`!@#$%^&*()_-+={[}]|\\:;\"'<,>.?/".indexOf(source.charAt(i)) == -1) {
        curLit += source.charAt(i);
        isEsc = false;
      } else {
        if (curLit.length() > 0) {
          res.add(new LiteralCmd(source.substring(lastCmd + 1, i + 1), curLit));
          lastCmd = i;
          curLit = "";
        }
        switch (source.charAt(i)) {
        case ' ':
          break;
        case '_':
          res.add(new InputCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case '`':
          res.add(new OutputCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case '"':
          res.add(new CMCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case '\'':
          res.add(new MCCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case '+':
          res.add(new AddCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case ':':
          res.add(new DupeCmd(source.substring(lastCmd + 1, i + 1)));
          lastCmd = i;
          break;
        case '\\':
          isEsc = true;
          break;
        default:
        }
      }
    }
    if (curLit.length() > 0) {
      res.add(new LiteralCmd(source.substring(lastCmd + 1, source.length()),
          curLit));
    }
    return res;
  }

  private static class InputCmd extends SimpleStkCmd {

    public InputCmd(String token) {
      super("input", token, "main");
    }

    @Override
    protected void calc(SimpleStack stack) {
      stack.addLen(1);
      stack.knownData.add(0, new SimpleScalar());
    }

  }

  private static class OutputCmd extends SimpleStkCmd {

    public OutputCmd(String token) {
      super("output", token, "main");
    }

    @Override
    protected void calc(SimpleStack stack) {
      errLv = stack.canPop(1);
      stack.subLen(1);
      if (stack.knownData.size() > 0) {
        stack.knownData.remove(0);
      }
    }

  }

  private static class AddCmd extends SimpleStkCmd {

    public AddCmd(String token) {
      super("add", token, "main");
    }

    @Override
    protected void calc(SimpleStack stack) {
      errLv = stack.canPop(2);
      stack.subLen(2);
      stack.addLen(1);
      if (stack.knownData.size() > 0) {
        stack.knownData.remove(0);
      }
    }

  }

  private static class LiteralCmd extends SimpleStkCmd {

    SimpleScalar lit;

    public LiteralCmd(String token, String lit) {
      super("literal", token, "main");
      this.lit = toLit(lit);
    }

    @Override
    protected void calc(SimpleStack stack) {
      stack.addLen(1);
      stack.knownData.add(0, lit);
    }

    static SimpleScalar toLit(String s) {
      try {
        return new SimpleInt(Integer.parseInt(s));
      } catch (Throwable e) {
      }
      return new SimpleStr(s);
    }

  }

  private static class DupeCmd extends SimpleStkCmd {

    public DupeCmd(String token) {
      super("dupe", token, "main");
    }

    @Override
    protected void calc(SimpleStack stack) {
      errLv = stack.canPop(2);
      stack.subLen(2);
      if (stack.knownData.size() > 0) {
        SimpleScalar mul = (SimpleScalar) stack.knownData.remove(0);
        SimpleScalar obj = new SimpleScalar();
        if (stack.knownData.size() > 0) {
          obj = (SimpleScalar) stack.knownData.remove(0); // thing to be
                                                          // multiplied
        }
        if (mul.intValue() == null) {
          stack.addLen(null);
          stack.knownData.clear();
        } else {
          int count = 0;
          if (mul.intValue() > 0) {
            count = mul.intValue();
          }
          for (int i = 0; i < count; i++) {
            stack.addLen(1);
            stack.knownData.add(0, obj);
          }
        }
      } else {
        stack.addLen(null);
        stack.knownData.clear();
      }
    }

  }

  private static class MCCmd extends SimpleCmd {

    public MCCmd(String token) {
      super("main->ctrl", token, "main", "ctrl");
    }

    @Override
    protected void calc(DataStructure[] DS) {
      SimpleStack main = (SimpleStack) DS[0];
      SimpleStack ctrl = (SimpleStack) DS[1];
      errLv = main.canPop(1);
      main.subLen(1);
      ctrl.addLen(1);
      if (main.knownData.size() > 0) {
        ctrl.knownData.add(0, main.knownData.remove(0));
      }
    }

  }

  private static class CMCmd extends SimpleCmd {

    public CMCmd(String token) {
      super("ctrl->main", token, "main", "ctrl");
    }

    @Override
    protected void calc(DataStructure[] DS) {
      SimpleStack main = (SimpleStack) DS[0];
      SimpleStack ctrl = (SimpleStack) DS[1];
      errLv = ctrl.canPop(1);
      ctrl.subLen(1);
      main.addLen(1);
      if (ctrl.knownData.size() > 0) {
        main.knownData.add(0, ctrl.knownData.remove(0));
      }
    }

  }

}
