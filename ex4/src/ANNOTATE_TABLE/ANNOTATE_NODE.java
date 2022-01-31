package ANNOTATE_TABLE;

public class ANNOTATE_NODE
{
    private ANNOTATE_NODE next;
    private int index;
    private String varName;

    public ANNOTATE_NODE(String varName, int index, ANNOTATE_NODE next) {
        this.varName = varName;
        this.index = index;
        this.next = next;
    }

    public void setNext(ANNOTATE_NODE next) {
        this.next = next;
    }

    public int getIndex(){
        return this.index;
    }

    public String getVarName() {
        return varName;
    }

    public ANNOTATE_NODE getNext() {
        return next;
    }
}