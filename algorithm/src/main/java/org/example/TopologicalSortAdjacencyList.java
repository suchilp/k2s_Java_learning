package org.example;

import java.util.*;

public class TopologicalSortAdjacencyList {

    //Helper class for describe edges in the graph
    static class Edge {
        int from, to, weight;

        public Edge(int from, int to, int weight) {
            this.from = from;
            this.to = to;
            this.weight = weight;
        }
    }

    public static int dfs(int node, Map<Integer, List<Edge>> graph, int numberOfNodes, boolean visited[], int ordering[], int lastOrderToFirst) {
        visited[node] = true;
        List<Edge> edges = graph.get(node);
        for (Edge edge : edges) {
            if (!visited[edge.to]) {
                lastOrderToFirst = dfs(edge.to, graph, numberOfNodes, visited, ordering, lastOrderToFirst);
            }
        }
        ordering[lastOrderToFirst] = node;
        return lastOrderToFirst - 1;
    }

    public static int[] topologicalSort(Map<Integer, List<Edge>> graph, int numberOfNodes) {
        int ordering[] = new int[numberOfNodes];
        boolean visited[] = new boolean[numberOfNodes];
        int lastOrderToFirst = numberOfNodes - 1;
        for (int i = 0; i < numberOfNodes; i++) {
            if (!visited[i]) {
                lastOrderToFirst = dfs(i, graph, numberOfNodes, visited, ordering, lastOrderToFirst);
            }
        }
        return ordering;

    }

    public static void dfsUsingStack(int node, Map<Integer, List<Edge>> graph, int numberOfNodes, boolean visited[], Stack<Integer> stack) {
        visited[node] = true;
        List<Edge> edges = graph.get(node);
        for (Edge edge : edges) {
            if (!visited[edge.to]) {
                dfsUsingStack(edge.to, graph, numberOfNodes, visited, stack);
            }
        }
        stack.push(node);

    }

    public static int[] topologicalSortStack(Map<Integer, List<Edge>> graph, int numberOfNodes) {
        int ordering[] = new int[numberOfNodes];
        boolean visited[] = new boolean[numberOfNodes];
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < numberOfNodes; i++) {
            if (!visited[i]) {
                dfsUsingStack(i, graph, numberOfNodes, visited, stack);
            }
        }
        for (int i = 0; i < numberOfNodes; i++) {
            ordering[i] = stack.pop();
        }
        return ordering;

    }

    public static Integer[] directedAcyclicGraph(Map<Integer, List<Edge>> graph, int start, int noOfNodes) {
        int[] topSort = topologicalSort(graph, noOfNodes);
        Integer[] distance = new Integer[noOfNodes];
        distance[start] = 0;

        for (int i = 0; i < noOfNodes; i++) {
            int nodeIndex = topSort[i];
            if (Objects.nonNull(distance[nodeIndex])) {
                List<Edge> adjacentList = graph.get(nodeIndex);
                if (adjacentList != null) {
                    for (Edge e : adjacentList) {
                        int newDistance = e.weight + distance[nodeIndex];
                        if (distance[e.to] == null) {
                            distance[e.to] = newDistance;
                        } else {
                            distance[e.to] = Math.min(newDistance, distance[e.to]);
                        }
                    }
                }
            }

        }


        return distance;

    }


    public static void main(String[] args) {
        //Graph Setup
        final int N = 7;
        Map<Integer, List<Edge>> graph = new HashMap<>();
        for (int i = 0; i < N; i++) {
            graph.put(i, new ArrayList<>());
        }

        graph.get(0).add(new Edge(0, 1, 3));
        graph.get(0).add(new Edge(0, 2, 2));
        graph.get(0).add(new Edge(0, 5, 3));
        graph.get(1).add(new Edge(1, 3, 1));
        graph.get(1).add(new Edge(1, 2, 6));
        graph.get(2).add(new Edge(2, 3, 1));
        graph.get(2).add(new Edge(2, 4, 10));
        graph.get(3).add(new Edge(3, 4, 5));
        graph.get(5).add(new Edge(5, 4, 7));

        int[] ordering = topologicalSort(graph, N);
        System.out.println(Arrays.toString(ordering));

        int[] orderingStack = topologicalSortStack(graph, N);
        System.out.println(Arrays.toString(orderingStack));

        //find all the shortest path from starting node 0

        Integer[] dists = directedAcyclicGraph(graph, 0, N);
        System.out.println(Arrays.toString(dists));
// Find the shortest path from 0 to 4 which is 8.0
        System.out.println(dists[4]);

        // Find the shortest path from 0 to 6 which
        // is null since 6 is not reachable!
        System.out.println(dists[6]);
    }
}
