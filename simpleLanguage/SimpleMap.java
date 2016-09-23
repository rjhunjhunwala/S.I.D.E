package simpleLanguage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PhiNotPi
 */
public class SimpleMap<K, V> extends DataStructure {
  Map<K, V> knownData = new HashMap<K, V>();

  SimpleMap() {
    super();
  }

  public SimpleMap(String label) {
    super(label);
  }

}
