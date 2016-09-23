package simpleLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import IDE.Language;

/**
 * @author PhiNotPi
 */
public abstract class SimpleLanguage extends Language {

  public final Map<String, DataStructure> DSbyname = new HashMap<String, DataStructure>();
  final ArrayList<DataStructure> DS = new ArrayList<DataStructure>();

  protected void clear() {
    DSbyname.clear();
    DS.clear();
  }

  // Map<String, String> supType = new HashMap<String, String>();

  protected void addDS(DataStructure ds) {
    if (ds != null) {
      DS.add(ds);
      if (ds.label != null) {
        DSbyname.put(ds.label, ds);
      }
    }
  }

  protected ArrayList<String> splitTokens(String source) {
    ArrayList<Command> cmds = splitCommands(source);
    ArrayList<String> tokens = new ArrayList<String>();
    for (Command c : cmds) {
      tokens.add(c.token);
    }
    return tokens;
  }

  protected abstract ArrayList<Command> splitCommands(String source);

  protected void applyCmds(ArrayList<Command> cmds) {
    for (Command c : cmds) {
      c.apply(this);
    }
  }

}
