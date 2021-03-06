package TYPES;
import AST.*;
import java.util.*;

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
		System.out.println("Debug --->> getOffsetForVar for " + name + " in " + this.name);
		for(TYPE_CLASS currFather = this; currFather != null; currFather = currFather.father){
			count = currFather.fieldsCount;
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				if(it.head.isVar()){
					tvar = (TYPE_CLASS_VAR_DEC) it.head;
					System.out.println("Debug --->> getOffsetForVar, current is " + tvar.name);
					System.out.println("Debug --->> getOffsetForVar, current counter is " + count);
					if(name.equals(tvar.name)){
						// result ancsFieldCount + offset in current class
						TYPE_CLASS currGrandpa = currFather.father;
						if (currGrandpa != null){
							count += currGrandpa.countFieldWithAncs();
						}
						System.out.println("Debug --->> getOffsetForVar result for " + name + " in " + this.name);
						System.out.println("Debug --->> getOffsetForVar counter is " + count );
						return count;
					}
						count--;
				}

			}
		}
		//no var with same name in ancestors
		return -1;
	}

	public int getOffsetForMethod_ver2(String name){
		int count;
		TYPE_FUNCTION tfunc = null;
		System.out.println("getOffsetForMethod ---> looking for " + name);
		for(TYPE_CLASS currFather = this; currFather != null; currFather = currFather.father){
			count = currFather.methodCount;
			System.out.println("getOffsetForMethod ---> B looking for " + name);
			for(TYPE_LIST it = currFather.data_members; it != null; it = it.tail){
				System.out.println("getOffsetForMethod ---> C looking for " + name + " and current is " + it.head.name);
				System.out.println("getOffsetForMethod ---> C looking for " + name + " and current is " + it.head.isFunction());
				if(it.head.isFunction()){
					tfunc = (TYPE_FUNCTION) it.head;
					System.out.println("getOffsetForMethod ---> current is " + tfunc.name);
					System.out.println("getOffsetForMethod ---> current counter is " + count);
					if(name.equals(tfunc.name)){
						// result ancsFieldCount + offset in current class
						TYPE_CLASS currGrandpa = currFather.father;
						if (currGrandpa != null) {
							count += currGrandpa.countMethodWithAncs();
						}
						System.out.println("getOffsetForMethod ---> answer is " + count);
						return count - 1;
					}
					count--;
				}

			}
		}
		//no method with same name in ancestors
		return -1;
	}

	public int getOffsetForMethod(String name){
		Stack<TYPE_CLASS> allClasses = new Stack<TYPE_CLASS>();
		TYPE_CLASS p = this;
		while (p != null) {
			allClasses.push(p);
			p = p.father;
		}
		Stack<String> methodNames = new Stack<String>();
		String curr_name;
		int count = 0;
		while (!allClasses.empty()) {
			p = allClasses.pop();
			for(TYPE_LIST ptr = p.data_members; ptr != null; ptr = ptr.tail){
				if (ptr.head.isFunction()) {
					methodNames.push(ptr.head.name);
				}
			}
			while(!methodNames.empty()) {
				curr_name = methodNames.pop();
				if (name.equals(curr_name)){
					return count;
				}
				count++;
			}
		}
		// method not found
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
