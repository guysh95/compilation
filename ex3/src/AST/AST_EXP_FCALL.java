package AST;

import TYPES.*;
import SYMBOL_TABLE.*;

public class AST_EXP_FCALL extends AST_EXP {
    /****************/
    /* DATA MEMBERS */
    /****************/
    public AST_VAR caller;
    public String fieldName;
    public AST_EXPLIST explist;
    public int row;

    /******************/
    /* CONSTRUCTOR(S) */
    /******************/
    public AST_EXP_FCALL(AST_VAR caller, String fieldName, AST_EXPLIST explist, int row) {
            /******************************/
            /* SET A UNIQUE SERIAL NUMBER */
            /******************************/
            SerialNumber = AST_Node_Serial_Number.getFresh();

            /***************************************/
            /* PRINT CORRESPONDING DERIVATION RULE */
            /***************************************/
            if (explist != null) {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ( expList )\n", fieldName);
                else System.out.format("====================== exp -> ID(%s) ( expList )\n", fieldName);
            }
            else {
                if (caller != null) System.out.format("====================== exp -> var . ID(%s) ()\n", fieldName);
                else System.out.format("====================== exp -> ID(%s) ()\n", fieldName);
            }

            /*******************************/
            /* COPY INPUT DATA NENBERS ... */
            /*******************************/
            this.caller = caller;
            this.fieldName = fieldName;
            this.explist = explist;
            this.row = row;
    }

    /******************************************************/
    /* The printing message for a function call AST node */
    /******************************************************/
    public void PrintMe()
    {
        /**************************************/
        /* AST NODE TYPE = AST EXP FUNCTION CALL */
        /**************************************/
        System.out.print("AST EXP FUNC CALL\n");

        /*************************************/
        /* RECURSIVELY PRINT CALLER + expList ... */
        /*************************************/
        if (caller != null) {
            caller.PrintMe();
            if (fieldName!= null) System.out.format(".ID( %s )", fieldName);
        }
        else System.out.format("ID( %s )", fieldName);
        if (explist != null) explist.PrintMe();

        /**********************************/
        /* PRINT to AST GRAPHVIZ DOT file */
        /**********************************/
        AST_GRAPHVIZ.getInstance().logNode(
                SerialNumber,
                String.format("FUNC\nCALL\nID(%s)\n", fieldName));

        /****************************************/
        /* PRINT Edges to AST GRAPHVIZ DOT file */
        /****************************************/
        if (caller != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,caller.SerialNumber);
        if (explist != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,explist.SerialNumber);
    }

    /* public TYPE semantme(scope) {
        TYPE t = null;
        TYPE tc = null;
        TYPE tfunc = null;



        // when there is var - check that the var is class type and that the function within class scope
        if (caller != null){
            t = caller.semantme(scope);


            if (t.isClass() == false)
            {
                System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);

            }
            else
            {
                tc = (TYPE_CLASS) t;
            }


            for (TYPE_LIST it=tc.data_members;it != null;it=it.tail)
            {
                if (it.head.name == fieldName)
                {
                    tfunc = it.head;
                }
            }
        }

        // when there is no var - check that the function exists


    } */
    public TYPE SemantMe(String scope) {
        TYPE t1 = null;
        TYPE_CLASS tc = null;
        TYPE t2 = null;
        TYPE_FUNCTION tfunc = null;
        System.out.println("We are in AST_EXP_FCALL for " + fieldName);
        // only if we have caller
        if (caller != null){
            System.out.println("caller exists for: "+ fieldName);
            t1 = caller.SemantMe(scope);
            if (t1.isClass() == false)
            {
                System.out.format(">> ERROR [%d:%d] access %s field of a non-class variable\n",6,6,fieldName);
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            } else {
                tc = (TYPE_CLASS) t1;
            }
            //check if func in class or its super classes
            for (TYPE_LIST it=tc.data_members;it != null;it=it.tail)
            {
                System.out.println(it.head);
                if(it.head.isFunction()){
                    tfunc = (TYPE_FUNCTION) it.head;
                    System.out.println("casted class func");
                    System.out.println("checking the following data member from class:" +tfunc.name);
                    System.out.println("fieldname is: "+ fieldName);
                    if (tfunc.name.equals(fieldName))
                    {
                        System.out.println("found field in class " +tc.name);

                        return tfunc.returnType;
                    }
                }

            }
            tfunc = tc.searchInFathersFunc(fieldName, this.row);
            if (tfunc == null){
                System.out.format(">> ERROR no %s field on the superclasses/class\n",fieldName);
                throw new lineException(Integer.toString(this.row));

            }

            /*if (t2 == null){
                System.out.format(">> ERROR no %s field on the scope\n",fieldName);
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            }

            if (!t2.isFunction()) {
                System.out.format(">> ERROR provided explist although this is not a function");
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            }*/
            if (explist != null) {
                TYPE_LIST tparams = explist.getTypes(scope);
                System.out.println("checking params for func call now");
                if(!sameParams(tfunc.params, tparams)){
                    System.out.format(">> ERROR params not matching in exp_fcall");
                    throw new lineException(Integer.toString(this.row));
                }
            } else {
                if (tfunc.params != null){
                    System.out.format(">> ERROR params not matching in exp_fcall");
                    throw new lineException(Integer.toString(this.row));
                }
            }

            System.out.println("we have same params for func call!");
            return tfunc.returnType;

        } else { // caller is null
            System.out.println("Caller is null for " + fieldName);
            t2 = SYMBOL_TABLE.getInstance().find(fieldName);
            System.out.println("scope is " + scope);
            System.out.println(fieldName + " type is " + t2);
            if(!t2.isFunction()){
                System.out.format(">> ERROR provided explist although this is not a function");
                throw new lineException(Integer.toString(this.row));
                //System.exit(0);
            }
            tfunc = (TYPE_FUNCTION) t2;
            if (explist != null) {

                if(!sameParams(tfunc.params, explist.getTypes(scope))){
                    System.out.format(">> ERROR params not matching in exp_fcall");
                    throw new lineException(Integer.toString(this.row));
                }
            } else{
                if (tfunc.params != null){
                    System.out.format(">> ERROR params not matching in exp_fcall");
                    throw new lineException(Integer.toString(this.row));
                }
            }

            return tfunc.returnType;
        }
    }


    public boolean sameParams(TYPE_LIST funcParams, TYPE_LIST callerParams){
        TYPE t1 = null;
        TYPE t2 = null;

        for(TYPE_LIST it= callerParams; it != null; it=it.tail){
            System.out.println("started sameParams loop");
            if (funcParams == null) {
                System.out.print(">> ERROR in samefunc - no same amount of params");
                return false;
            }
            t1 = funcParams.head;
            t2 = it.head;
            if(t1 != t2){
                System.out.print(">> ERROR1 in samefunc - no same params types");
                return false;
            }
            funcParams = funcParams.tail;
        }
        System.out.println("finished sameParams loop");
        if (funcParams != null) { //still more params
            return false;
        }
        //all checks passed

        return true;
    }
}
