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
	public int fieldsCount;
	public int methodCount;

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
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
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
			System.out.println("checking in searchInFathersVar - father " + currFather.name);
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				if(it.head.isVar()){
					tvar = (TYPE_CLASS_VAR_DEC) it.head;
					System.out.println("######found var name: " + tvar.name);
					if(name.equals(tvar.name)){
						return tvar;
					}
				}

			}

		}
		//no var with same name in ancestors
		return null;
	}

	public TYPE_ARRAY searchInFathersArr(String name, int row){
		TYPE_ARRAY tarr = null;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			System.out.println("checking in searchInFathersArr - father " + currFather.name);
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				if(it.head.isArray()){
					tarr = (TYPE_ARRAY) it.head;
					System.out.println("######found arr name: " + tarr.name);
					if(name.equals(tarr.name)){
						return tarr;
					}
				}

			}

		}
		// no arr with same name in ancestors
		return null;
	}

	public int getOffsetForVar(String name){
		int count;
		TYPE_CLASS_VAR_DEC tvar = null;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			count = 0;
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				if(it.head.isVar()){
					tvar = (TYPE_CLASS_VAR_DEC) it.head;
					System.out.println("######found var name: " + tvar.name);
					if(name.equals(tvar.name)){
						// result ancsFieldCount + offset in current class
						TYPE_CLASS currGrandpa = currFather.father;
						return count + currGrandpa.countFieldWithAncs();
					}
						count++;
				}

			}
		}
		//no var with same name in ancestors
		return -1;
	}

	public int getOffsetForMethod(String name){
		int count;
		TYPE_FUNCTION tfunc = null
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			count = 0;
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				if(it.head.isFunction()){
					tfunc = (TYPE_FUNCTION) it.head;
					System.out.println("######found func name: " + tfunc.name);
					if(name.equals(tfunc.name)){
						// result ancsFieldCount + offset in current class
						TYPE_CLASS currGrandpa = currFather.father;
						return count + currGrandpa.countMethodWithAncs();
					}
					count++;
				}

			}
		}
		//no method with same name in ancestors
		return -1;
	}

	public int countFieldWithAncs() {
		int result = this.fieldsCount;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			result += currFather.fieldsCount;
		}
		return result;
	}

	public int countMethodWithAncs() {
		int result = this.methodCount;
		for(TYPE_CLASS currFather = this.father; currFather != null; currFather = currFather.father){
			result += currFather.methodCount;
		}
		return result;
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
