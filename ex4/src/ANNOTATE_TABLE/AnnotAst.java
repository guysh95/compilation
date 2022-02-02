package ANNOTATE_TABLE;

public class AnnotAst
{
    public int type;
    /**
     * type vals and meaning:
     * 0 - global
     * 1 - local funtion var
     * 2 - param var
     * 3 - class field
     *
     */
    private int offset;
    private String funcName;
    private String className;

    public AnnotAst(int offset, String funcName, String className) {
        this.offset = offset;
        this.funcName = funcName;
        this.className = className;
    }

    public AnnotAst() {
        this.offset = 0;
        this.funcName = null;
        this.className = null;
    }

    public void setGlobal() {
        this.type = 0;
    }

    public void setLocal() {
        this.type = 1;
    }

    public void setParam() {
        this.type = 2;
    }

    public void setField() {
        this.type = 3;
    }

    public String getFuncName() {
        return funcName;
    }

    public void setFuncName(String funcName) {
        this.funcName = funcName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isGlobal() {
        return (this.type == 0)
    }

    public boolean isLocal() {
        return (this.type == 1)
    }
    public boolean isParam() {
        return (this.type == 2)
    }
    public boolean isField() {
        return (this.type == 3)
    }
}