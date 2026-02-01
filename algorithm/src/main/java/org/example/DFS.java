package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DFS {

    class Graph {
        private int V;
        private List<List<Integer>> edges;

        Graph(int V) {
            this.V = V;
            edges = new ArrayList<>();
            for (int i = 0; i < V; i++) {
                edges.add(new ArrayList<>());
            }
        }

        public void addEdge(int src, int dest) {
            edges.get(src).add(dest);
        }

        public void recursiveDFS(int start) {
            boolean[] visited = new boolean[V];
            dfsUtil(start, visited);
        }

        public void dfsUtil(int node, boolean[] visited) {
            visited[node] = true;
            System.out.println(node + " ");
            for (int neighbor : edges.get(node)) {
                if (!visited[neighbor]) {
                    dfsUtil(neighbor, visited);
                }
            }
        }

        public void iterativeDFS(int start) {
            boolean[] visited = new boolean[V];
            Stack<Integer> s = new Stack<>();
            s.push(start);
            while (!s.isEmpty()) {
                int node = s.pop();
                if (!visited[node]) {
                    visited[node] = true;
                    System.out.println(node + " ");
                    for (int neighbor : edges.get(node)) {
                        if (!visited[neighbor]) {
                            s.push(neighbor);
                        }
                    }

                }
            }

        }

    }


}
