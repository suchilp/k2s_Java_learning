package org.dfs;

import java.util.ArrayList;
import java.util.LinkedList;

public class DFSRecurssive {

    private int vertices;
    private LinkedList<Integer>[] adjList;

    DFSRecurssive(int V) {
        this.vertices = V;
        adjList = new LinkedList[V];
        for (int i = 0; i < V; i++) {
            adjList[i] = new LinkedList<>();
        }
    }

    public void addEdge(int v, int w) {
        adjList[v].add(w);
    }

    public void startDFS(int start) {
        boolean visited[] = new boolean[vertices];

        dfs(visited, start);
    }

    public void dfs(boolean visited[], int v) {
        visited[v] = true;
        System.out.println(v + "");
        for (int n : adjList[v]) {
            if (!visited[n]) {
                dfs(visited, n);
            }
        }
    }


    public static void main(String[] args) {
        DFSRecurssive g = new DFSRecurssive(5);
        g.addEdge(0, 1);
        g.addEdge(0, 3);
        g.addEdge(1, 2);
        g.addEdge(1, 4);
        g.addEdge(3, 4);
        System.out.println("DFS Traversal:");
        g.startDFS(0);

    }
}
