package AST;

import TEMP.*;

public abstract class AST_VAR extends AST_Node
{
    public TEMP assignIRme(TEMP t1){return null;}

    public TEMP_LIST beforeAssign() {return null;}

    public TEMP afterAssign(TEMP_LIST exps) {return null;}
}
