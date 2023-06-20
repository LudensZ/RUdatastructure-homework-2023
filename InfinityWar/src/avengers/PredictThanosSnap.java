package avengers;
import java.nio.file.attribute.GroupPrincipal;
import java.util.*;
/**
 * Given an adjacency matrix, use a random() function to remove half of the nodes. 
 * Then, write into the output file a boolean (true or false) indicating if 
 * the graph is still connected.
 * 
 * Steps to implement this class main method:
 * 
 * Step 1:
 * PredictThanosSnapInputFile name is passed through the command line as args[0]
 * Read from PredictThanosSnapInputFile with the format:
 *    1. seed (long): a seed for the random number generator
 *    2. p (int): number of people (vertices in the graph)
 *    2. p lines, each with p edges: 1 means there is a direct edge between two vertices, 0 no edge
 * 
 * Note: the last p lines of the PredictThanosSnapInputFile is an ajacency matrix for
 * an undirected graph. 
 * 
 * The matrix below has two edges 0-1, 0-2 (each edge appear twice in the matrix, 0-1, 1-0, 0-2, 2-0).
 * 
 * 0 1 1 0
 * 1 0 0 0
 * 1 0 0 0
 * 0 0 0 0
 * 
 * Step 2:
 * Delete random vertices from the graph. You can use the following pseudocode.
 * StdRandom.setSeed(seed);
 * for (all vertices, go from vertex 0 to the final vertex) { 
 *     if (StdRandom.uniform() <= 0.5) { 
 *          delete vertex;
 *     }
 * }
 * Answer the following question: is the graph (after deleting random vertices) connected?
 * Output true (connected graph), false (unconnected graph) to the output file.
 * 
 * Note 1: a connected graph is a graph where there is a path between EVERY vertex on the graph.
 * 
 * Note 2: use the StdIn/StdOut libraries to read/write from/to file.
 * 
 *   To read from a file use StdIn:
 *     StdIn.setFile(inputfilename);
 *     StdIn.readInt();
 *     StdIn.readDouble();
 * 
 *   To write to a file use StdOut (here, isConnected is true if the graph is connected,
 *   false otherwise):
 *     StdOut.setFile(outputfilename);
 *     StdOut.print(isConnected);
 * 
 * @author Yashas Ravi
 * Compiling and executing:
 *    1. Make sure you are in the ../InfinityWar directory
 *    2. javac -d bin src/avengers/*.java
 *    3. java -cp bin avengers/PredictThanosSnap predictthanossnap.in predictthanossnap.out
*/

public class PredictThanosSnap {
	 
    public static void main (String[] args) {
 
        if ( args.length < 2 ) {
            StdOut.println("Execute: java PredictThanosSnap <INput file> <OUTput file>");
            return;
        }

        String predictthanossnapInputFile = args[0];
        String predictthanossnapOutputFile = args[1];

        StdIn.setFile(predictthanossnapInputFile);
        long seed = StdIn.readLong();
        StdRandom.setSeed(seed);

        int p = StdIn.readInt();
        int[][] graph = new int[p][p];
        for(int row = 0; row < p; row++){
            for(int col = 0; col < p; col++){
                graph[row][col] = StdIn.readInt();
            }
        }

        ArrayList<Integer> deleted = new ArrayList<>();

        for(int vec = 0; vec<p; vec++){
//           if(deleted.size() != p/2+1){
                if(StdRandom.uniform() <= 0.5){
                deleted.add(vec);
                    for(int i = 0; i < p; i++){
                        graph[vec][i] = 0;
                        graph[i][vec] = 0;
                    }
//                }
            }   
        }
        
        for(int i = 0; i < deleted.size(); i++){
            // Remove row
            for (int row = deleted.get(i)-i; row < p - 1; row++) {
                for (int col = 0; col < p; col++) {
                    graph[row][col] = graph[row+1][col];
                }
            }

            // Remove column
            for (int col = deleted.get(i)-i; col < p - 1; col++) {
                for (int row = 0; row < p; row++) {
                    graph[row][col] = graph[row][col+1];
                }
            }

            // Update indices
            for (int row = 0; row < p-1; row++) {
                if (row >= p) {
                    graph[row] = graph[row + 1];
                }
                for (int col = 0; col < p-1; col++) {
                    if (col >= p) {
                        graph[row][col] = graph[row][col + 1];
                    }
                }
            }
            p--;
        }

        //dfs without recur
        boolean[] visited = new boolean[p];
        Stack<Integer> stack = new Stack<>();
        stack.push(0);
        while (!stack.isEmpty()) {
            int occur = stack.pop();
            if (!visited[occur]) {
                visited[occur] = true;
                for (int i = p - 1; i >= 0; i--) {
                    if (graph[occur][i] == 1 && !visited[i]) {
                        stack.push(i);
                    }
                }
            }
        }

        StdOut.setFile(predictthanossnapOutputFile);

        for(int i = 0; i<visited.length;i++){
            if(visited[i] ==false){
                StdOut.print(false);
                return;
            }
        }
        StdOut.print(true);
        return;
    	// WRITE YOUR CODE HERE

    }
}
