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

        //check if change was made - save it before change
        Set<Integer> beforeChange = new HashSet<Integer> ();
        beforeChange.addAll(curr.inTemps);
        //System.out.println("work on " + curr.cmd +" that has in temps *before change* : "+ beforeChange);


        // update out fields for current node
        if (curr.next != null){
            curr.outTemps.clear();
            curr.outTemps.addAll(curr.next.inTemps); // add in from next
            if (curr.jumpTo != null) {
                    curr.outTemps.addAll(curr.jumpTo.inTemps); //add in from jump
            }
        } else {
            if (curr.jumpTo != null) {
                curr.outTemps.clear();
                curr.outTemps.addAll(curr.jumpTo.inTemps); // add in from next
            }
        }


        // update in fields for current node
        Set<Integer> liveTemps = curr.cmd.getLiveTemps();
        Set<Integer> deadTemps = curr.cmd.getDeadTemps();
        curr.inTemps = curr.outTemps;
        //System.out.println("before live and dead:  " + curr.inTemps);
        if(liveTemps != null) { curr.inTemps.addAll(liveTemps); }
        if(deadTemps != null) { curr.inTemps.removeAll(deadTemps); }

        //check if change was made
        if(!(beforeChange.equals(curr.inTemps))){
            k = 1;
        }

        //System.out.println("work on " + curr.cmd +" that has in temps *after change*: "+ curr.inTemps + " and k is: "+k);
        // run recursive for prev and jumpFrom
        k += livenessRec(curr.prev);
        if(curr.jumpFrom != null) {
            k += livenessRec(curr.jumpFrom);
        }
        return k;

    }


    public void runAnalysis(){
        int flag = 1;
        int counter = 0;
        while (flag != 0){  // will be 0 only after an analysis run without changes
            //System.out.println(String.format("start liveness abaylsis num %d", counter++));
            flag = livenessRec(this.tail);

        }
        System.out.println("CFG live analysis results:");
        for(CFG_node curr = head; curr!=null; curr=curr.next){
            System.out.println("&&  "+curr.cmd +" " +curr.inTemps);
        }

    }
}