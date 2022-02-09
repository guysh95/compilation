package AST;

import TEMP.*;

public abstract class AST_VAR extends AST_Node
{
    public TEMP assignIRme(TEMP t1){return null;}

    public TEMP_LIST computeArrayOffsets() {return null;}

    public TEMP storeExp(TEMP_LIST exps, TEMP assigned) {return null;}

    public TEMP getVarTemp(TEMP_LIST exps) {return null;}
}
