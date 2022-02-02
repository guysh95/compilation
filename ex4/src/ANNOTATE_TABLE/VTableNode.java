package VTableMap;

public class VTableNode
{
    public VTableNode next;
    public String className;
    public String vTableLabel;
    // public TYPE_CLASS thisClass;
    // public TYPE_CLASS extendedClass;


    public VTableNode(VTableMap.VTableNode next, String className, String vTableLabel, TYPE_CLASS thisClass, TYPE_CLASS extendedClass) {
        this.next = next;
        this.className = className;
        this.vTableLabel = vTableLabel;
        // this.thisClass = thisClass;
        // this.extendedClass = extendedClass;
    }
}