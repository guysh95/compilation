package ANNOTATE_TABLE;

public class ANNOTATE_TABLE
{
    private ANNOTATE_NODE localHead = null;
    private ANNOTATE_NODE paramHead = null;
    private String scopeName;
    private int scopeLayer = 0;
    private int localCounter = 1;
    private int paramCounter = 1;
    public boolean addingParams = true;

    private ANNOTATE_CLIST classHead = null;
    private int classCounter = 1;

    private void enterSpecific(String varName, int counter, ANNOTATE_NODE head){
        ANNOTATE_NODE newNode = new ANNOTATE_NODE(varName, counter, null);
        if (this.head != null)
            this.head.setNext(newNode);
        else
            this.head = newNode;
    }

    public void enter(String varName){
        if(this.addingParams){
            enterSpecific(varName, this.paramCounter, this.paramHead);
            this.paramCounter++;
        }
        else{
            enterSpecific(varName, this.localCounter, this.localHead);
            this.localCounter++;
        }
    }

    public void startScope(String scopeName) {
        this.scopeName = scopeName;
    }

    public void endScope(boolean isClass){
        if (isClass) {
            ANNOTATE_CLIST newClass = new ANNOTATE_CLIST(this.scopeName, this.classCounter, this.localHead);
            this.classCounter++;
            if (this.classHead == null)
                this.classHead = newClass;
            else
                this.classHead.setNext(newClass);
        }

        this.localHead = null;
        this.paramHead = null;
        this.localCounter = 1;
        this.paramCounter = 1;
        this.addingParams = true;
    }

    /**
     * I = local/param offset
     * return -I if varName is parameter
     * return I if varName is local
     * return 0 if not found
     */
    public int findInScope(String varName){
        ANNOTATE_NODE p = paramHead;
        while(p != null){
            if(p.getVarName().equals(varName)){
                return (-1 * p.getIndex());
            }
            p = p.getNext();
        }
        p = localHead;
        while(p != null){
            if(p.getVarName().equals(varName)){
                return p.getIndex();
            }
            p = p.getNext();
        }
        return 0;
    }

    public int findInClasses(String className, String varName){
        ANNOTATE_CLIST classPointer = this.classHead;
        while (classPointer != null) {
            if (classPointer.getClassName().equals(className)) {
                ANNOTATE_NODE varPointer = classPointer.getCFields();
                while (varPointer != null){
                    if (varPointer.getName().equals(varName)){
                        return varPointer.getIndex();
                    }
                    varPointer = varPointer.getNext();
                }
            }
            classPointer = classPointer.getNext();
        }
    }

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static ANNOTATE_TABLE instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected ANNOTATE_TABLE() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static ANNOTATE_TABLE getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new ANNOTATE_TABLE();

        }
        return instance;
    }

}