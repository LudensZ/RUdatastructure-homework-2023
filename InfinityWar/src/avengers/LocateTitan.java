package avengers;
/**
 * 
 * Using the Adjacency Matrix of n vertices and starting from Earth (vertex 0), 
 * modify the edge weights using the functionality values of the vertices that each edge 
 * connects, and then determine the minimum cost to reach Titan (vertex n-1) from Earth (vertex 0).
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * LocateTitanInputFile name is passed through the command line as args[0]
 * Read from LocateTitanInputFile with the format:
 *    1. g (int): number of generators (vertices in the graph)
 *    2. g lines, each with 2 values, (int) generator number, (double) funcionality value
 *    3. g lines, each with g (int) edge values, referring to the energy cost to travel from 
 *       one generator to another 
 * Create an adjacency matrix for g generators.
 * 
 * Populate the adjacency matrix with edge values (the energy cost to travel from one 
 * generator to another).
 * 
 * Step 2:
 * Update the adjacency matrix to change EVERY edge weight (energy cost) by DIVIDING it 
 * by the functionality of BOTH vertices (generators) that the edge points to. Then, 
 * typecast this number to an integer (this is done to avoid precision errors). The result 
 * is an adjacency matrix representing the TOTAL COSTS to travel from one generator to another.
 * 
 * Step 3:
 * LocateTitanOutputFile name is passed through the command line as args[1]
 * Use Dijkstra’s Algorithm to find the path of minimum cost between Earth and Titan. 
 * Output this number into your output file!
 * 
 * Note: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, minCost represents the minimum cost to 
 *   travel from Earth to Titan):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(minCost);
 *  
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/LocateTitan locatetitan.in locatetitan.out
 * 
 * @author Yashas Ravi
 * 
 */

public class LocateTitan {
	
    public static void main (String [] args) {
    	
        if ( args.length < 2 ) {
            StdOut.println("Execute: java LocateTitan <INput file> <OUTput file>");
            return;
        }

    	// WRITE YOUR CODE HERE
        String LocateTitanInputFile = args[0];
        String LocateTitanOutputFile = args[1];

        StdIn.setFile(LocateTitanInputFile);
        int g = StdIn.readInt();

        double[] teleweight = new double[g];

        for(int i = 0; i < g; i++){
            int index = StdIn.readInt();
            double weight = StdIn.readDouble();
            teleweight[index] = weight;
        }

        int[][] portals = new int[g][g];
        for(int row = 0; row < g; row++){
            for(int col = 0; col < g; col++){
                int orginal = StdIn.readInt();
                portals[row][col] = (int)(orginal/(teleweight[row]*teleweight[col]));
                //System.out.print(portals[row][col] + " ");
            }
            //System.out.println();
        }

// Dijkstra below
        int[] minCost = new int[g];
        boolean[] Dijkstra = new boolean[g];

        for(int i = 0; i < g; i++){
            minCost[i] = Integer.MAX_VALUE;
            Dijkstra[i] = false;
        }  
        minCost[0] = 0;

        for(int i = 0; i< g; i++){
            
            //find minimum distance
            int min = Integer.MAX_VALUE;
            int minIndex = -1;
            for(int v = 0; v< minCost.length; v++){
            if(Dijkstra[v] == false && minCost[v] < min){
                min = minCost[v];
                minIndex = v;
            }
        }

            int currentsource = minIndex;
            Dijkstra[currentsource] = true;

            for(int j=0; j<g; j++){
                if (!Dijkstra[j] && portals[currentsource][j] != 0 && minCost[currentsource] != Integer.MAX_VALUE && minCost[currentsource] + portals[currentsource][j] < minCost[j]) {
                    minCost[j] = minCost[currentsource] + portals[currentsource][j];
                }
            }
        }

        StdOut.setFile(LocateTitanOutputFile);
        StdOut.print(minCost[g-1]);
    }

    
}


