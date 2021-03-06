# Searching Implicit Graphs
## Overview

Graphs are often presented explicitly in courses on data structures and algorithms. For example, the undirected graph `G = (V, E)`, where `V = {v0, v1, v2}` and `E = {(v0, v1), (v1, v2)}` is presented with an expression that gives each vertex and edge its own representation. In real applications, it is often the case that a graph is presented only implicitly; its vertices and edges have to be constructed by code as an algorithm runs. In this assignment, you'll work with one family of such implicit graphs.

A problem-space graph is an implicit graph whose vertices represent "states" corresponding to possible situations that can be reached in the course of solving a problem. The edges represent possible transitions from one state to another according to a set of "operators." Such a graph is typically given by providing a start vertex v0 (corresponding to the problem's "initial state," s0) together with a set of operators `{ ϕ0, ϕ1, ..., ϕm-1}`. Each operator ϕi consists of two parts: 
preconditioni: a predicate that takes a vertex as its argument and returns true iff and only if it is permissable to apply this operator's transition function to the vertex. 
transitioni: a function that takes a vertex as its argument and returns a new vertex. The old and new vertices (call them vx and vy) represent an edge of the graph.

Typical tasks associated with problem-space graphs are (a) solving the problem, (b) exploring all or a portion of the space by "visiting" all or some of its vertices, (c) finding shortest paths between given pairs of vertices, (d) measuring properties of the graph such as average degree of a vertex, (e) finding the diameter (length of a longest shortest path between two of its vertices), and (f) building a visual display of the graph.

The basic functionality that you'll provide in this assignment will handle the finding of shortest paths, using breadth-first search (BFS). This can be used, in principle, to solve problems as well. In addition to breadth-first search, you'll implement one or more additional algorithms that will serve to compare BFS to alternatives.

```java
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
```

We'll work with a problem space based on a popular puzzle, but we won't stop there. We'll consider a couple of "extensions" to the puzzle that will make this assignment not just an exercise but also more of an exploration in its own right. The puzzle is really a family of puzzles. Known as the Towers of Hanoi, it involves three pegs and a set of disks of different sizes. The player (solver) of the puzzle starts with all the disks on the first peg, the disks being arranged with the largest-diameter one at the bottom and sizes decreasing going up the stack. The objective is to move all the disks to the third peg, respecting the following constraints: only one disk may be moved at a time. Only a disk that is topmost in its stack of disks may be moved. It must then be moved either to a peg containing no disks or to a peg where the topmost disk is of a larger diameter than its own. The reason this is a family of puzzles rather than a single one is because the number of disks involved is a parameter to the initialization of the puzzle. For example, an instance of the puzzle with 5 disks is a little more difficult to solve than one with only 4 disks. An instance having 100 disks, while solvable in theory, is impossible in practice, assuming it takes at least one femtosecond to make a move.

Our graph is defined implicitly as follows. The starting vertex v0 represents the initial state of the puzzle. We have six operators: `{ ϕ0,1, ϕ0,2, ϕ1,0, ϕ1,2, ϕ2,0, ϕ2,1}`. Here, operator ϕi,j could be interpreted as "Try to move a disk from peg i to peg j." We are using 0-based indexing for the pegs so peg 0 is the first peg, etc.

Here is the ExploredGraph.java starter code file. The starter code includes basic definitions for the classes and gives, for the Vertex class, a constructor that accepts a string of the form below.

`Vertex v0 = new Vertex( "[[4,3,2,1],[],[]]" )`;
All of your code will be added in this file. To help you find places where you should add code, there are several "// Implement this" comments. In addition to the code you add, provide line-by-line comments in your code and per-method comments for those methods that you implement. These per-method comments will normally be about one to three lines of text, as needed to describe the purpose of the method. The comments do not have to be in JavaDoc format. In these per-method comments, you should say what the input arguments represent and what the return value represents.

![alt text](https://courses.cs.washington.edu/courses/cse373/16au/A/A5/TOHGraph4.png "Logo Title Text 1")

## Basic Code Specification

### Vertex
Vertex: a class to represent vertices of the graph. Since each vertex represents a state of a Towers-of-Hanoi puzzle, it must contain the basic information about which disk is on which peg. It should have a toString() method that returns a string of the form "`[[4,3,2,1],[],[]]`". The starter code includes one constructor and the toString method.

### Edge
Edge: a class to represent edges of the graph. Provide a constructor, a toString method, and methods to retrieve endpoint1 and endpoint2 of the edge (call them getEndpoint1() and getEndpoint2()). If you do the extra credit A6E2, also provide methods setEdgeCost(int c) and getEdgeCost().

>Note: The toString method should use the following format: "Edge from `[[4,3,2,1],[],[]]` to `[[4,3,2],[1],[]]`". Notice that although we sometimes consider edges of these graphs to be undirected, we will consider our edges here to actually be directed. Thus, there may be an edge in our graph whose string representation is "Edge from `[[4,3,2],[1],[]]` to `[[4,3,2,1],[],[]]`", but we will consider these to be distinct.

### Operator
Operator: a class to represent operators for the problem. There should be methods to construct operators, access their components, and apply their components. **getPrecondition()** should return a function that can be applied to a vertex to find out whether the operator's transition function is applicable to the vertex. getTransition() should return a function that can be applied to a vertex (provided that the precondition is true) to get a "successor" vertex -- the result of making the move. and use them. precondition(Vertex v) should return true if the vertex v satisfies the precondition for the operators (and so it would be OK to apply the operator's transition method. 
```java
public boolean precondition(Vertex v) {
    if (!v.pegs.get(i).isEmpty()) {
        if (v.pegs.get(j).isEmpty() || v.pegs.get(i).peek() < v.pegs.get(j).peek()) {
            return true;
        }
    }
    return false;
}
```
In the Towers of Hanoi, this means that it would possible and legal to move a disk from peg i to peg j). **transition(Vertex v)** should return a new vertex that represents the state reached by applying the operator. Thus it should actually make the move of a disk from peg i to peg j. It is important that the vertex returned be a new instance of class Vertex, and not just v with modifications. 
```java
public Vertex transition(Vertex v) {
    Vertex returnVertex = new Vertex(v.toString());
    returnVertex.pegs.get(j).push(returnVertex.pegs.get(i).pop());
    return returnVertex;
}
```
**toString()** should return a string that describes this operator, clearly differentiating it from the others.
```java
// returns a String indicating which pegs the transition function will modify
@Override
public String toString() {
    return i+","+j;
}
```

### ExploredGraph
ExploredGraph: a class that holds a collection of vertices and a collection of edges. It will be used to store the portion of the problem-space graph that has been made explicit by the program so far. It should have the following methods. initialize(v) should set up an instance of this class, and insert the starting vertex v into its set of vertices. Typically, v will be the start vertex, but your method should allow any legal vertex for the problem-space graph. **nvertices()** should return an int giving the number of vertices currently in the explored graph structure. nedges() should return an int giving the number of edges currently in the explored graph structure. **bfs(vi, vj)** should run a breadth-first search starting at vi and continue until reaching vj. **idfs(vi, vj)** should run an iterative depth-first search starting at vi and stopping either when reaching vj or running out of options or resources.
```java
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
```
**retrievePath(vj)** should use the path links established by the most recent call to bfs or other search method, and it should return the path to vj. The path should end at vj, and that might require reversing the list of vertices obtained by the backtrace. 
```java
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
```
**shortestPath(vi, vj)** should use bfs and return a list of vertices that starts with vi and ends with vj representing a shortest path in the problem-space graph from vi to vj. This can be implemented using a combination of bfs and retrievePath.
```java
public ArrayList<Vertex> shortestPath(Vertex vi, Vertex vj) {
    bfs(vi, vj);
    return retrievePath(vj);
}
```

