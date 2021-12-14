package TYPES;
import AST.*;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	public TYPE_LIST data_members;

	public boolean isClass() { return true;}

	public TYPE findInClass(String name){
		for(TYPE_LIST it=this.data_members; it != null; it = it.tail){
			if(it.head.name.equals(name)){
				return it.head;
			}
		}
		return null;
	}

	public TYPE_FUNCTION searchInFathersFunc(String name, int row){
		TYPE_FUNCTION tfunc = null;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			for(TYPE_LIST it = father.data_members; it != null; it = it.tail){
				if(name.equals(it.head.name)){
					if(it.head.isFunction()){
						tfunc = (TYPE_FUNCTION) it.head;
						return tfunc;
					}  else {
						System.out.println("#ERROR reached in searchinfathersfunc to something that is not func");
						throw new lineException(Integer.toString(row));
					}
				}
			}

		}
		//no func with same name in ancestors
		return null;
	}

	public TYPE_CLASS_VAR_DEC searchInFathersVar(String name, int row){
		TYPE_CLASS_VAR_DEC tvar = null;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			for(TYPE_LIST it = father.data_members; it != null; it = it.tail){
				if(name.equals(it.head.name)){
					if(it.head.isVar()){
						tvar = (TYPE_CLASS_VAR_DEC) it.head;
						return tvar;
					}  else {
						System.out.println("#ERROR reached in searchinfathervar to something that is not var");
						throw new lineException(Integer.toString(row));
					}
				}
			}

		}
		//no var with same name in ancestors
		return null;
	}

	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_LIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}



}
