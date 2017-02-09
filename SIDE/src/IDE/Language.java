package IDE;

import java.util.ArrayList;

public abstract class Language {

  abstract protected boolean isValidSyntax(String source);

  abstract protected ArrayList<String> splitTokens(String source);
  
  abstract protected String getLangInfo();
  
  protected abstract Language clone();

}
