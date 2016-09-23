package simpleLanguage;

import java.util.ArrayList;

/**
 * @author PhiNotPi
 */
public class SimpleStack extends DataStructure {

  Class<?> type;
  private int minSizeEstimate = 0;
  private Integer maxSizeEstimate = 0;
  public ArrayList<DataStructure> knownData = new ArrayList<DataStructure>();

  SimpleStack(Class<?> type) {
    super();
    this.type = type;
  }

  public SimpleStack(Class<?> type, String label) {
    super(label);
    this.type = type;
  }

  public void subLen(Integer diff) {
    if (diff == null) {
      minSizeEstimate = 0;
    } else {
      minSizeEstimate -= diff;
      if (minSizeEstimate < 0) {
        minSizeEstimate = 0;
      }
      if (maxSizeEstimate != null) {
        maxSizeEstimate -= diff;
        if (maxSizeEstimate < 0) {
          maxSizeEstimate = 0;
        }
      }
    }
  }

  public void subMin(Integer diff) {
    if (diff == null) {
      minSizeEstimate = 0;
    } else {
      minSizeEstimate -= diff;
      if (minSizeEstimate < 0) {
        minSizeEstimate = 0;
      }
    }
  }

  public void addLen(Integer diff) {
    if (diff == null) {
      maxSizeEstimate = null;
    } else {
      minSizeEstimate += diff;
      if (maxSizeEstimate != null) {
        maxSizeEstimate += diff;
      }
    }
  }

  public void addMax(Integer diff) {
    if (diff == null) {
      maxSizeEstimate = null;
    } else {
      if (maxSizeEstimate != null) {
        maxSizeEstimate += diff;
      }
    }
  }

  public Status canPop(int count) {
    if (count <= minSizeEstimate) {
      return Status.OK;
    } else if (maxSizeEstimate == null) {
      return Status.WARN;
    } else if (count <= maxSizeEstimate) {
      return Status.OK;
    } else {
      return Status.ERROR;
    }
  }

}
