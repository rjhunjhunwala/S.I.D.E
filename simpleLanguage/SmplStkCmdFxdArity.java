package simpleLanguage;

public abstract class SmplStkCmdFxdArity extends SimpleStkCmd {

    int inArity = 0;
    
    public SmplStkCmdFxdArity(int inArity, String cmdName, String token, String stackName) {
      super(cmdName, token, stackName);
      this.inArity = inArity;
    }

    @Override
    protected void calc(SimpleStack stack) {
      errLv = stack.canPop(inArity);
      stack.subLen(inArity);
      
      DataStructure[] input = new DataStructure[inArity];
      int i = 0;
      while(i < inArity){
        if (stack.knownData.size() > 0) {
          input[i] = stack.knownData.remove(0);
        }
        if(input[i] == null){
          break;
        }
        i++;
      }
      
      DataStructure[] output = getDefaultOut();
      
      if(i == inArity){
        output = getRes(input);
      }

      for(int j = 0; j < output.length; j++){
        stack.knownData.add(j, output[j]);
        stack.addLen(1);
      }
    }

    protected abstract DataStructure[] getRes(DataStructure[] input);
    protected abstract DataStructure[] getDefaultOut();
    
  }