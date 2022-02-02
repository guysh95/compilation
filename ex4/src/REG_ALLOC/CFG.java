package REG_ALLOC;

import IR.*;
import TEMP.*;
import java.util.*;


public class CFG
{
    // variables
    private CFG_node head = null;
    private CFG_node tail = null;
    private static CFG instance = null;


    public static CFG getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            instance = new CFG();
        }
        return instance;
    }

    private void setCFGHead(CFG_node head){
        this.head = head;
        this.tail = head;
    }

    public CFG_node getCFGHead(){
        return this.head;
    }

    public CFG_node getCFGTail(){
        return this.tail;
    }

    public void addCFGNode(CFG_node node){
        // add new node to the graph and set it with the tail node
        // will be executed only when adding IRcommand to the IR list
        if (this.head == null){
            setCFGHead(node);
        } else {
            this.tail.next = node;
            node.prev = this.tail;
            this.tail = node;
        }
    }


    // backword liveness analysis
    public int livenessRec(CFG_node curr){
        // init
        int k = 0;

        // stop condition
        if (curr.prev == null) {    // means that we are in the start of the code (head node and no jump)
            return k;
        }

        // update out fields for current node
        if (curr != this.tail){
            curr.outTemps = curr.next.inTemps; // add in from next
            if (curr.jumpFrom != null) {
                    curr.outTemps.addAll(curr.jumpTo.inTemps); //add in from jump
            }
        } else {
            if (curr.jumpFrom != null) {
                curr.outTemps = curr.jumpFrom.inTemps; //add in from jump
            }
        }
        //todo: need to think on another way to check it
        //check if change was made
        if(curr.inTemps != curr.outTemps){
            k = 1;  // means that a change happend in the analysis
        }

        // update in fields for current node
        Set<Integer> liveTemps = curr.cmd.getLiveTemps();
        Set<Integer> deadTemps = curr.cmd.getDeadTemps();
        curr.inTemps = curr.outTemps;
        if(liveTemps != null) { curr.inTemps.addAll(liveTemps); }
        if(deadTemps != null) { curr.inTemps.removeAll(deadTemps); }

        // run recursive for prev and jumpFrom
        k += livenessRec(curr.prev);
        if(curr.jumpFrom != null) {
            k += livenessRec(curr.jumpFrom);
        }
        return k;

    }


    public void runAnalysis(){
        int flag = 1;
        while (flag != 0){  // will be 0 only after an analysis run without changes
            flag = livenessRec(this.tail);
        }
    }
}