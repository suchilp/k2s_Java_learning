package org.suchil.graphtheory;

import java.util.*;

public class DepthFirstSearchAdjacencyListRecursive {

    static class Edge {
        int from, to, cost;

        public Edge(int from, int to, int cost) {
            this.from = from;
            this.to = to;
            this.cost = cost;
        }
    }

    public static void main(String[] args) {

        int numNodes =8;
        Map<Integer, List<Edge>> graph = new HashMap<>();
        addDirectedEdge(graph, 1, 2, 1);
        addDirectedEdge(graph, 1, 2, 1); // Double edge
        addDirectedEdge(graph, 1, 3, 1);
        addDirectedEdge(graph, 2, 4, 1);
        addDirectedEdge(graph, 2, 5, 1);
        addDirectedEdge(graph, 3, 6, 1);
        addDirectedEdge(graph, 3, 7, 1);
        addDirectedEdge(graph, 2, 2, 1); // Self loop
        addDirectedEdge(graph, 2, 3, 1);
        addDirectedEdge(graph, 6, 2, 1);
        addDirectedEdge(graph, 1, 6, 1);

        boolean visited[] = new boolean[numNodes];
        dfs(1,visited,graph);
    }

    static void dfs(int at, boolean visited[], Map<Integer, List<Edge>> graph) {
        if (visited[at]) {
            return;
        }
        visited[at]=true;
        System.out.println(at);
        List<Edge> edges = graph.get(at);
        if(edges!=null) {
            for (Edge edge : edges) {
                dfs(edge.to, visited, graph);
            }
        }
    }

    private static void addDirectedEdge(Map<Integer, List<Edge>> graph, int from, int to, int cost) {
        List<Edge> list = graph.get(from);
        if (Objects.isNull(list)) {
            list = new ArrayList<>();
            graph.put(from, list);
        }
        list.add(new Edge(from, to, cost));
    }

}
