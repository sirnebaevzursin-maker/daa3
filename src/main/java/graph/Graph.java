package graph;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private final int n;
    private final boolean directed;
    private final List<Edge>[] adj;
    private final List<Edge> edges = new ArrayList<>();

    @SuppressWarnings("unchecked")
    public Graph(int n, boolean directed) {
        this.n = n;
        this.directed = directed;
        this.adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }
    }

    public void addEdge(int u, int v, int w) {
        Edge e = new Edge(u, v, w);
        edges.add(e);
        adj[u].add(e);
        if (!directed && u != v) {
            adj[v].add(new Edge(v, u, w));
        }
    }

    public int n() {
        return n;
    }

    public int m() {
        return edges.size();
    }

    public boolean isDirected() {
        return directed;
    }

    public List<Edge> adj(int u) {
        return adj[u];
    }

    public List<Edge> edges() {
        return edges;
    }

    public Graph reverse() {
        Graph r = new Graph(n, true);
        for (Edge e : edges) {
            r.addEdge(e.v, e.u, e.w);
        }
        return r;
    }

    public int[] inDegrees() {
        int[] in = new int[n];
        for (Edge e : edges) {
            in[e.v]++;
        }
        return in;
    }
}