package simpleLanguage;

/**
 * @author PhiNotPi
 */
public abstract class Command {

  String cmdName;
  public String token;
  protected Status errLv = Status.OK;

  Command(String cmdName, String token) {
    this.cmdName = cmdName;
    this.token = token;
  }

  public Status getStatus() {
    return errLv;
  }

  public abstract void apply(SimpleLanguage sl);

}
