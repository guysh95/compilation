package VTableMap;

public class VTableMap
{
    VTableNode head = null;



    public void enter(String className, String vTableLabel)
    {
        VTableNode newVT = new VTableNode(null, className, vTableLabel);
        if (this.head == null) {
            this.head = newVT;
        }
        else {
            this.head.next = newVT;
        }
    }

    public VTableNode findVTable(String className)
    {
        VTableNode p = this.head;
        while (p != null)
        {
            if (p.className.equals(className))
            {
                return p;
            }
            p = p.next;
        }
        return null;
    }

    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static VTableMap instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected VTableMap() {}


    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static VTableMap getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new VTableMap();

        }
        return instance;
    }

}