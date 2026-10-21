package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    public final int vertices;
    public final List<List<Edge>> adj;
    public final List<Edge> edges;

    public Graph(int vertices) {
        this.vertices = vertices;
        this.adj = new ArrayList<>(vertices);
        for (int i = 0; i < vertices; i++) {
            adj.add(new ArrayList<>());
        }
        this.edges = new ArrayList<>();
    }

    public void addEdge(int u, int v, int weight, boolean directed) {
        Edge edge = new Edge(u, v, weight);
        adj.get(u).add(edge);
        edges.add(edge);
        if (!directed) {
            adj.get(v).add(new Edge(v, u, weight));
        }
    }

    public Graph getTranspose() {
        Graph transpose = new Graph(vertices);
        for (Edge e : edges) {
            transpose.addEdge(e.v, e.u, e.weight, true);
        }
        return transpose;
    }
}