package org.example;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyListDynamic {
    int V = 5;
    private List<List<Integer>> adjList;

    AdjacencyListDynamic() {
        adjList = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            adjList.add(new ArrayList<>());
        }
    }

    public static void main(String[] args) {
        AdjacencyListDynamic obj = new AdjacencyListDynamic();
        int[][] edges = {
                {0, 1}, {0, 2,}, {0, 3},
                {1, 2}, {1, 4},
                {2, 3}, {2, 4},
                {3, 4},
                {4, 3}, {4, 1}, {4, 2}
        };
        for (int[] e : edges) {
            obj.addEdge(e[0], e[1]);
        }
        obj.printGraph();
    }

    void printGraph() {
        for (int i = 0; i < V; i++) {
            System.out.println(adjList.get(i));
        }
    }

    void addEdge(int src, int dest) {
        adjList.get(src).add(dest);
    }
}