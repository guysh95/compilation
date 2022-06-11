package TYPES;

public class TYPE_NIL extends TYPE{

    private static TYPE_NIL instance = null;
    public boolean isClass() { return true;}
    public boolean isArray() { return true;}

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_NIL() {}

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_NIL getInstance()
    {
        if (instance == null)
        {
            instance = new TYPE_NIL();
            instance.name = "nil";
        }
        return instance;
    }
}