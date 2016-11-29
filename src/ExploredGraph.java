import java.util.*;

/**
 * @author Bum Mook Oh 1239912
 * @author Billy Ding 0441796
 *
 *
 * Extra Credit Options Implemented, if any:
 * A5E1 the number of pegs can change.
 * By changing the global npegs value the
 * number of pegs will change accordingly.
 *
 * Solution to Assignment 5 in CSE 373, Autumn 2016
 * University of Washington.
 *
 * (Based on starter code v1.3. By Steve Tanimoto.)
 *
 * Java version 8 or higher is recommended.
 *
 */

// Here is the main application class:
public class ExploredGraph {
    Set<Vertex> Ve; // collection of explored vertices
    Set<Edge> Ee;   // collection of explored edges
    public final int npegs = 4; // number of pegs
    private Set<Operator> Op; // collection of operators

    // constructor for ExploredGraph
    // initializes the visited vertices and edges
    // creates operators for number of pegs
    public ExploredGraph() {
        Ve = new LinkedHashSet<Vertex>();
        Ee = new LinkedHashSet<Edge>();
        Op = new LinkedHashSet<Operator>();
        for (int i = 0; i < npegs; ++i) {
            for (int j = 0; j < npegs; ++j) {
                if (i != j) {
                    Op.add(new Operator(i, j));
                }
            }
        }
    }

    // initializes the visited vertices and edges
    public void initialize() {
        Ve = new LinkedHashSet<Vertex>();
        Ee = new LinkedHashSet<Edge>();
    }

    // returns the number of explored vertices
    public int nvertices() {
        return Ve.size();
    }

    // returns the number of explored edges
    public int nedges() {
        return Ee.size();
    }

    // Iterative Depth-First Search
    // This function will take in two vertices
    // It starts at the initial vertex (vi) and ends when
    // the destination vertex (vj) is found
    public void idfs(Vertex vi, Vertex vj) {
        initialize();
        Stack<Vertex> s = new Stack<Vertex>();
        s.push(vi);
        outerloop:
        while (!s.isEmpty()) {
            Vertex currVertex = s.pop();
            Ve.add(currVertex);
            System.out.println(currVertex);
            for (Operator op : Op) {
                if (op.precondition(currVertex)) {
                    Vertex foundVertex = op.transition(currVertex);
                    if (!Ve.contains(foundVertex)) {
                        s.push(foundVertex);
                        Ve.add(foundVertex);
                        Ee.add(new Edge(currVertex, foundVertex));
                        if (foundVertex.equals(vj)) {
                            System.out.println(foundVertex);
                            break outerloop;
                        }
                    }
                }
            }
        }
    }

    // Breadth-First Search
    // This function will take in two vertices
    // It starts at the initial vertex (vi) and ends when
    // the destination vertex (vj) is found
    public void bfs(Vertex vi, Vertex vj) {
        initialize();
        Queue<Vertex> q = new LinkedList<Vertex>();
        q.add(vi);
        outerloop:
        while (!q.isEmpty()) {
            Vertex currVertex = q.remove();
            if (!Ve.contains(currVertex)) {
                Ve.add(currVertex);
            }
            Vertex futureVertex;
            for (Operator op : Op) {
                if (op.precondition(currVertex)) {
                    futureVertex = op.transition(currVertex);
                    if (!q.contains(futureVertex) && !Ve.contains(futureVertex)) {
                        q.add(futureVertex);
                        Ee.add(new Edge(currVertex, futureVertex));
                        if (vj.equals(futureVertex)) {
                            Ve.add(futureVertex);
                            break outerloop;
                        }
                    }
                }
            }
        }
    }

    // This function returns an ArrayList that contains the path
    // of vertices from the most recent search call where the destination
    // vertex is the passed in vertex (vj)
    public ArrayList<Vertex> retrievePath(Vertex vj) {
        ArrayList<Vertex> backtrace = new ArrayList<Vertex>();
        Vertex currVertex = vj;
        backtrace.add(vj);
        while (true) {
            boolean found = false;
            for(Edge e : Ee){
                if(e.getEndpoint2().equals(currVertex)){
                    backtrace.add(e.vi);
                    currVertex = e.vi;
                    found = true;
                    break;
                }
            }
            if (!found){
                break;
            }
        }
        Collections.reverse(backtrace);
        return backtrace;
    }

    // This function returns an ArrayList that contains the shortest path
    // from the initial vertex (vi) to the destination vertex (vj)
    public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
        bfs(vi, vj);
        return retrievePath(vj);
    }

    // This function returns the Set of visited vertices
    public Set<Vertex> getVertices() {
        return Ve;
    }

    // This function returns the Set of visited edges
    public Set<Edge> getEdges() {
        return Ee;
    }

    // main method
    public static void main(String[] args) {
        ExploredGraph eg = new ExploredGraph();
        Vertex v1 = eg.new Vertex("[[4,3,2,1],[],[],[]]");
        Vertex v2 = eg.new Vertex("[[],[],[],[4,3,2,1]]");
        System.out.println(eg.shortestPath(v1, v2));
    }

    // Each vertex will hold a Towers-of-Hanoi state.
    // There will be n pegs in this version.
    // You can change n by changing the class constant npegs
    // Constructor that takes a string such as "[[4,3,2,1],[],[]]"
    class Vertex {
        ArrayList<Stack<Integer>> pegs;
        public Vertex(String vString) {
            String[] parts = vString.split("\\],\\[");
            pegs = new ArrayList<Stack<Integer>>(npegs);
            for (int i=0; i < npegs; i++) {
                pegs.add(new Stack<Integer>());
                try {
                    parts[i]=parts[i].replaceAll("\\[","");
                    parts[i]=parts[i].replaceAll("\\]","");
                    List<String> al = new ArrayList<String>(Arrays.asList(parts[i].split(",")));
                    //System.out.println("ArrayList al is: "+al);
                    Iterator<String> it = al.iterator();
                    while (it.hasNext()) {
                        String item = it.next();
                        if (!item.equals("")) {
                            //System.out.println("item is: "+item);
                            pegs.get(i).push(Integer.parseInt(item));
                        }
                    }
                }
                catch (NumberFormatException nfe) { nfe.printStackTrace(); }
            }
        }

        // This function creates a String such as "[[4,3,2,1],[],[]]" to represent
        // the current Towers-of-Hanoi state
        @Override
        public String toString() {
            String ans = "[";
            for (int i = 0; i < npegs; i++) {
                ans += pegs.get(i).toString().replace(" ", "");
                if (i < npegs - 1) {
                    ans += ",";
                }
            }
            ans += "]";
            return ans;
        }

        // This function compares the toString methods of objects and
        // returns true if they are equal and false if they are not
        @Override
        public boolean equals(Object other) {
            return this.toString().equals(other.toString());
        }

        // This function overrides the default hashCode and returns the value for the Towers-of-Hanoi
        @Override
        public int hashCode(){
            return pegs.hashCode();
        }
    }

    // an edge is the connection between 2 vertices
    class Edge {
        private Vertex vi;
        private Vertex vj;

        // initializes 2 vertices
        public Edge(Vertex vi, Vertex vj) {
            this.vi = vi;
            this.vj = vj;
        }

        // returns a String that shows which two vertices the edge consists of
        @Override
        public String toString() {
            return "Edge from " + vi.toString() + " to " + vj.toString();
        }

        // returns first vertex
        public Vertex getEndpoint1() {
            return vi;
        }

        // returns second vertex
        public Vertex getEndpoint2() {
            return vj;
        }
    }

    //The operator class controls which disk should move to which peg
    class Operator {
        private int i;
        private int j;

        // Constructor for operators.
        public Operator(int i, int j) {
            this.i = i;
            this.j = j;
        }

        // a predicate that takes a vertex as its argument and returns true
        // if and only if it is permissible to apply this operator's transition function to the vertex
        public boolean precondition(Vertex v) {
            if (!v.pegs.get(i).isEmpty()) {
                if (v.pegs.get(j).isEmpty() || v.pegs.get(i).peek() < v.pegs.get(j).peek()) {
                    return true;
                }
            }
            return false;
        }

        // a function that takes a vertex as its argument and returns a new vertex.
        // The old and new vertices represent an edge of the graph
        public Vertex transition(Vertex v) {
            Vertex returnVertex = new Vertex(v.toString());
            returnVertex.pegs.get(j).push(returnVertex.pegs.get(i).pop());
            return returnVertex;
        }

        // returns a String indicating which pegs the transition function will modify
        @Override
        public String toString() {
            return i+","+j;
        }
    }
}