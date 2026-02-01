package org.example;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class AdjListGraph {

    private int V;
    private List<List<Integer>> edges;

    AdjListGraph(int V) {
        this.V = V;
        edges = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            edges.add(i, new ArrayList<>());
        }
    }

    public void addEdge(int src, int dest) {
        edges.get(src).add(dest);

        //for undirected graph
        //edge.get(dest).add(src);
    }

    void bfs(int start) {

        boolean[] visited = new boolean[V];
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(start);
        while (!queue.isEmpty()) {
            int node = queue.poll();
            visited[node] = true;
            System.out.println(node);
            for (int neighbor : edges.get(node)) {
                if (!visited[neighbor]) {
                    visited[neighbor] = true;
                    queue.offer(neighbor);
                }
            }

        }

    }
}
