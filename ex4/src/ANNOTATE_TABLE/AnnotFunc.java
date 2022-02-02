package ANNOTATE_TABLE;

public class AnnotFunc
{
    private int numParams;
    private int numLocals;

    public AnnotFunc(int numParams, int numLocals) {
        this.numParams = numParams;
        this.numLocals = numLocals;
    }

    public int getNumParams() {
        return numParams;
    }

    public void setNumParams(int numParams) {
        this.numParams = numParams;
    }

    public int getNumLocals() {
        return numLocals;
    }

    public void setNumLocals(int numLocals) {
        this.numLocals = numLocals;
    }
}