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

    public AnnotAst(int type, int offset, String funcName, String className) {
        this.type = type;
        this.offset = offset;
        this.funcName = funcName;
        this.className = className;
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
}