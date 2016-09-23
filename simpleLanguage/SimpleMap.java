package simpleLanguage;

import java.util.HashMap;
import java.util.Map;

/**
 * @author PhiNotPi
 */
public class SimpleMap extends DataStructure {
  Map<DataStructure, DataStructure> knownData = new HashMap<DataStructure, DataStructure>();

  SimpleMap() {
    super();
  }

  public SimpleMap(String label) {
    super(label);
  }
  
  public SimpleMap clone(){
    SimpleMap res = new SimpleMap(label);
    for(DataStructure k : knownData.keySet()){
      DataStructure v = knownData.get(k);
      res.knownData.put(k.clone(), v.clone());
    }
    return res;
  }

}
