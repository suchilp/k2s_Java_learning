package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TopologicalSortDFS {

    public static void main(String[] args) {

        //set up vertices
        int V = 6;
        //initialize the outer array
        List<List<Integer>> graph = new ArrayList<>();
        for (int i = 0; i < V; i++) {
            graph.add(new ArrayList<>()); //initialize the inner array
        }
        TopologicalSortDFS topologicalSortDFS = new TopologicalSortDFS();
        topologicalSortDFS.addEdge(graph, 5, 2);
        topologicalSortDFS.addEdge(graph, 5, 0);
        topologicalSortDFS.addEdge(graph, 4, 0);
        topologicalSortDFS.addEdge(graph, 4, 1);
        topologicalSortDFS.addEdge(graph, 2, 3);
        topologicalSortDFS.addEdge(graph, 3, 1);

        boolean[] visited = new boolean[V];
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i < V; i++) {
            if (!visited[i]) {
                topoSort(i, visited, stack, graph);
            }
        }

        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }

    }

    public static void topoSort(int node, boolean[] visited, Stack<Integer> stack, List<List<Integer>> graph) {

        visited[node] = true;
        for (int neighbor : graph.get(node)) {
            if (!visited[neighbor]) {
                topoSort(neighbor, visited, stack, graph);
            }
        }

        stack.push(node);

    }

    public void addEdge(List<List<Integer>> graph, int src, int dest) {
        graph.get(src).add(dest);
    }
}
