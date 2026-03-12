package org.dfs;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class DFSStack {
    public static void main(String[] args) {
        List<List<Integer>> graph = new ArrayList<>(); // adjacency List
        for (int i = 0; i < 5; i++) {
            graph.add(new ArrayList<>());
        }
        graph.get(0).add(1);
        graph.get(0).add(3);
        graph.get(1).add(2);
        graph.get(1).add(4);
        graph.get(3).add(4);
        dfs(0, graph);
    }

    static void dfs(int start, List<List<Integer>> graph) {
        boolean[] visited = new boolean[graph.size()];
        Stack<Integer> stack = new Stack<>();
        stack.push(start);
        while (!stack.isEmpty()) {
            int node = stack.pop();
            if (!visited[node]) {
                visited[node] = true;
                System.out.println(node + " ");
            }
            for (int neighbor : graph.get(node)) {
                if (!visited[neighbor]) {
                    stack.push(neighbor);
                }
            }
        }

    }
}
