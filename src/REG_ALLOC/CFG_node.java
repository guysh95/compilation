package REG_ALLOC;

import IR.*;
import TEMP.*;
import java.util.*;


public class CFG_node
{
    // variables
    public IRcommand cmd;
    public CFG_node prev = null;
    public CFG_node next = null;
    public CFG_node jumpTo = null;
    public CFG_node jumpFrom = null;
    public Set<Integer> inTemps = new HashSet<Integer> ();
    public Set<Integer> outTemps = new HashSet<Integer> ();

    // create node
    public CFG_node(IRcommand cmd){
        this.cmd = cmd;
    }
}