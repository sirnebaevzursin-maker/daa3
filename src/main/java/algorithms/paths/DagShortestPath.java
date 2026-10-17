package algorithms.paths;

import algorithms.topo.TopoResult;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.Arrays;

public class DagShortestPath {
    public static final long INF = Long.MAX_VALUE / 4;

    public static PathResult run(Graph g, TopoResult topo, Metrics m) {
        if (topo.hasCycle) {
            throw new IllegalArgumentException("graph has a cycle");
        }
        int n = g.n();
        long[] dist = new long[n];
        int[] parent = new int[n];
        Arrays.fill(dist, INF);
        Arrays.fill(parent, -1);
        int[] in = g.inDegrees();
        for (int v = 0; v < n; v++) {
            if (in[v] == 0) {
                dist[v] = 0;
            }
        }
        for (int u : topo.order) {
            for (Edge e : g.adj(u)) {
                m.edgeScans++;
                m.comparisons++;
                long cand = dist[u] + e.w;
                if (cand < dist[e.v]) {
                    dist[e.v] = cand;
                    parent[e.v] = u;
                }
            }
        }
        long sum = 0;
        for (int v = 0; v < n; v++) {
            if (dist[v] < INF) {
                sum += dist[v];
            }
        }
        return new PathResult(dist, parent, sum);
    }
}