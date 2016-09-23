package simpleLanguage;

public class SimpleReal extends SimpleScalar {

  public SimpleReal(Double d) {
    super();
    setRealValue(d);
  }
  
  public SimpleReal clone(){
    return new SimpleReal(realValue());
  }

}