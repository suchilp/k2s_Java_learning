package org.example;

public class BFSDemo {
    public static void main(String[] args) {

//        AdjListGraph g= new AdjListGraph(6);
//        g.addEdge(0, 1);
//        g.addEdge(0, 2);
//        g.addEdge(1, 3);
//        g.addEdge(1, 4);
//        g.addEdge(2, 5);
//
//        System.out.println("BFS Traversal:");
//        g.bfs(0);
        AdjListGraph g= new AdjListGraph(3);
        g.addEdge(0, 1);
        g.addEdge(0, 2);
        g.addEdge(1, 2);
        g.bfs(0);
    }
}
