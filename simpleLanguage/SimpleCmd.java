package simpleLanguage;

import java.util.Map;

/**
 * @author PhiNotPi
 */
public abstract class SimpleCmd extends Command {

  String[] DSnames;

  public SimpleCmd(String cmdName, String token, String... DSnames) {
    super(cmdName, token);
    this.DSnames = DSnames;
  }

  public void apply(Map<String, DataStructure> DSbyname) {
    DataStructure[] DSes = new DataStructure[DSnames.length];
    for (int i = 0; i < DSes.length; i++) {
      DataStructure ds = DSbyname.get(DSnames[i]);
      if (!(ds instanceof DataStructure)) {
        System.err.println("Error: cmd:" + cmdName + " ds:" + DSnames[i]);
      }
      DSes[i] = ds;
    }
    calc(DSes);
  }

  // abstract ArrayList<E> calc(ArrayList<E> in);
  protected abstract void calc(DataStructure[] DS);

}
