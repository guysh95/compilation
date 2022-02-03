package ANNOTATE_TABLE;

public class ANNOTATE_CLIST
{
    private ANNOTATE_NODE cFields;
    private int index;
    private String className;
    private ANNOTATE_CLIST next;

    public ANNOTATE_CLIST(String className, int index, ANNOTATE_NODE cFields) {
        this.className = className;
        this.index = index;
        this.cFields = cFields;
        this.next = null;
    }

    public void setNext(ANNOTATE_CLIST next) {
        this.next = next;
    }

    public int getIndex(){
        return this.index;
    }

    public String getClassName() {
        return className;
    }

    public ANNOTATE_NODE getCFields() {
        return cFields;
    }
}