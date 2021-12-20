package TYPES;

public class TYPE_ARRAY extends TYPE
{
    /**************************************************/
    /* Gather up all data members in one place        */
    /* Note that data members coming from the AST are */
    /* packed together with the class methods         */
    /**************************************************/
    public TYPE member_type;

    public boolean isArray() { return true;}

    /****************/
    /* CTROR(S) ... */
    /****************/
    public TYPE_ARRAY(String name,TYPE member_type)
    {
        this.name = name;
        this.member_type = member_type;
    }
}
