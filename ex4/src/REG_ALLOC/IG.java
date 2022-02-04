package REG_ALLOC;

import IR.*;
import TEMP.*;
import java.util.*;

/*      NOT IN USE
public class IG_node{
    //variables
    int tmpSerial;
    Set<Integer> neighbors = new HashSet<Integer>();
    int numEdges = 0;

    // create IG node
    public IG_node(int tmpSerial){
        this.tmpSerial = tmpSerial;
    }
}

 */

public class IG{

    // private IG_node[] vertices;
    private int V;
    private HashSet<Integer> adj[];
    private static IG instance = null;


    private IG(int numTemps){
        this.V = numTemps;
        this.adj = new HashSet[numTemps];
        for (int i=0; i<numTemps; ++i)
            this.adj[i] = new HashSet<Integer>();
        //add edges
        iterateCFG();

        /*
        IG_node[] vertices = new IG_node[numTemps+1]; //because it starts from 0 and counts the last one
        for(int i=0; i++; i<=numTemps){
            IG_node curr = new IG_node(i);
            vertices[i] = curr;
        }
        this.vertices = vertices;
        */
    }


    public static IG getInstance()
    {
        if (instance == null)
        {
            /*******************************/
            /* [0] The instance itself ... */
            /*******************************/
            System.out.println("We are in IG, 1");
            int numTemps = TEMP_FACTORY.getInstance().getNumTemps();
            System.out.println("num of temps in use are: " + numTemps);
            instance = new IG(numTemps);
        }
        return instance;
    }


    private void addEdge(int v, int w){
        //Function to add an edge into the graph
        this.adj[v].add(w);
        this.adj[w].add(v); //Graph is undirected


        /*
        boolean f1 = v1.neighbors.add(v2);
        if(f1) { v1.numEdges++; } // increment only if added edge
        boolean f2 = v2.neighbors.add(v1);
        if(f2) { v2.numEdges++; }

         */
    }

    private void iterateCFG(){
        CFG_node head = CFG.getInstance().getCFGHead();
        for(CFG_node curr=head; curr.next!=null; curr=curr.next){
            if(!curr.inTemps.isEmpty()) {
                Integer[] relationed = curr.inTemps.toArray(new Integer[0]); //returns the set as an array
                for (int i = 0; i < relationed.length - 1; i++) {
                    //IG_node v1 = IG.getInstance().vertices[relationed[i]]
                    int v = relationed[i];
                    for (int k = i + 1; i < relationed.length;  k++) {
                        //IG_node v2 = IG.getInstance().vertices[relationed[k]]
                        int w = relationed[k];
                        addEdge(v, w);
                    }
                }
            }
        }
    }

    private void checkIfAllFalse(boolean[] array){
        for(boolean b : array) if(b) return;
        System.out.println("Cant color this");
        System.exit(1);
    }

    // algorithm from: https://www.geeksforgeeks.org/graph-coloring-set-2-greedy-algorithm/
    public int[] coloring(){
        int numColors = 10;
        int result[] = new int[this.V];

        // Initialize all vertices as unassigned
        Arrays.fill(result, -1);
        // Assign the first color to first vertex
        result[0]  = 0;

        // A temporary array to store the available colors. False
        // value of available[cr] would mean that the color cr is
        // assigned to one of its adjacent vertices
        boolean available[] = new boolean[numColors];

        // Initially, all colors are available
        Arrays.fill(available, true);

        // Assign colors to remaining V-1 vertices
        for (int u = 1; u < V; u++)
        {

            // Process all adjacent vertices and flag their colors
            // as unavailable
            Iterator<Integer> it = adj[u].iterator() ;
            while (it.hasNext())
            {
                int i = it.next();
                if (result[i] != -1)
                    available[result[i]] = false;
            }

            //this will check if all vals are false - then there is no coloring for this vertex
            checkIfAllFalse(available);

            // Find the first available color
            int cr;
            for (cr = 0; cr < numColors; cr++){
                if (available[cr])
                    break;
            }

            result[u] = cr; // Assign the found color

            // Reset the values back to true for the next iteration
            Arrays.fill(available, true);
        }

        return result;

    }
}

