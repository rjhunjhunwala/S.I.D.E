package simpleLanguage;

public class SimpleStr extends SimpleScalar {

  public SimpleStr(String s) {
    super();
    setStrValue(s);
  }
  
  public SimpleStr clone(){
    return new SimpleStr(strValue());
  }

}
