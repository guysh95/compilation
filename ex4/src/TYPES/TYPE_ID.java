package TYPES;

public class TYPE_ID extends TYPE
{
    /**************************************/
    /* USUAL SINGLETON IMPLEMENTATION ... */
    /**************************************/
    private static TYPE_ID instance = null;

    /*****************************/
    /* PREVENT INSTANTIATION ... */
    /*****************************/
    protected TYPE_ID() {}

    public boolean isClass() { return true;};

    /******************************/
    /* GET SINGLETON INSTANCE ... */
    /******************************/
    public static TYPE_ID getInstance(TYPE scope)
    {
        if (instance == null)
        {
            instance = new TYPE_ID();
            instance.name = scope.name;
        }
        return instance;
    }
}
