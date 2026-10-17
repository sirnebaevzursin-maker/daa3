package algorithms.topo;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayDeque;
import java.util.Arrays;

public class Kahn {

    public static TopoResult run(Graph g, Metrics m) {
        int n = g.n();
        int[] in = g.inDegrees();
        ArrayDeque<Integer> queue = new ArrayDeque<>();
        for (int v = 0; v < n; v++) {
            if (in[v] == 0) {
                queue.add(v);
            }
        }
        int[] order = new int[n];
        int processed = 0;
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order[processed++] = u;
            for (Edge e : g.adj(u)) {
                m.edgeScans++;
                if (--in[e.v] == 0) {
                    queue.add(e.v);
                }
            }
        }
        if (processed < n) {
            return new TopoResult(new int[0], true, processed);
        }
        return new TopoResult(Arrays.copyOf(order, processed), false, processed);
    }
}