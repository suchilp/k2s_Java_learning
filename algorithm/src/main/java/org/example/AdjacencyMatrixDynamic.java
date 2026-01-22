package org.example;

public class AdjacencyMatrixDynamic {
    public static void main(String[] args) {

        int V = 5;
        int[][] graph = new int[V][V];
        int[][] edges = {
                {0, 1}, {0, 2,}, {0, 3},
                {1, 2}, {1, 4},
                {2, 3}, {2, 4},
                {3, 4},
                {4, 3}, {4, 1}, {4, 2}
        };
        for (int[] e : edges) {
            addEdge(graph, e[0], e[1]);
        }
        // print matrix
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                System.out.print(graph[i][j] + " ");
            }
            System.out.println("");
        }
    }

    static void addEdge(int[][] graph, int src, int dest) {
        graph[src][dest] = 1;

    }
}