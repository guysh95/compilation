package VTableMap;

public class VTableNode
{
    public VTableNode next;
    public String className;
    public String vTableLabel;



    public VTableNode(VTableNode next, String className, String vTableLabel) {
        this.next = next;
        this.className = className;
        this.vTableLabel = vTableLabel;

    }
}