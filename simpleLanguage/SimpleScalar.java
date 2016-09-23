package simpleLanguage;

/**
 * @author PhiNotPi
 */
public class SimpleScalar extends DataStructure {

  private Double realValue = null;
  private Integer intValue = null;
  private String strValue = null;

  public SimpleScalar() {
  }

  void setRealValue(Double d) {
    realValue = d;
    intValue = null;
    strValue = null;
  }

  public Double realValue() {
    if (realValue != null) {
      return realValue;
    }
    if (intValue != null) {
      return intValue.doubleValue();
    }
    try {
      return Double.parseDouble(strValue);
    } catch (Exception e) {
      return null;
    }
  }

  void setIntValue(Integer i) {
    realValue = null;
    intValue = i;
    strValue = null;
  }

  public Integer intValue() {
    if (intValue != null) {
      return intValue;
    }
    if (realValue != null) {
      return realValue.intValue();
    }
    try {
      return Integer.parseInt(strValue);
    } catch (Exception e) {
      return null;
    }
  }

  void setStrValue(String s) {
    realValue = null;
    intValue = null;
    strValue = s;
  }

  public String strValue() {
    if (strValue != null) {
      return strValue;
    }
    if (intValue != null) {
      return "" + intValue;
    }
    if (realValue != null) {
      return "" + realValue;
    }
    return null;
  }

}
