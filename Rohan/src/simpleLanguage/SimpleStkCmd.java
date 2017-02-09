package simpleLanguage;

/**
 * @author PhiNotPi
 */
public abstract class SimpleStkCmd extends Command {

  String stackName;

  public SimpleStkCmd(String cmdName, String token, String stackName) {
    super(cmdName, token);
    this.stackName = stackName;
  }

  public void apply(SimpleLanguage sl) {
    DataStructure ds = sl.DSbyname.get(stackName);
    if (!(ds instanceof SimpleStack)) {
      System.err.println("Error: cmd:" + cmdName + " stk:" + stackName);
      return;
    }
    SimpleStack stack = (SimpleStack) ds;
    calc(stack);
  }

  // abstract ArrayList<E> calc(ArrayList<E> in);
  protected abstract void calc(SimpleStack stack);

}
