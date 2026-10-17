package algorithms.mst;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.PriorityQueue;

public class Prim {

    public static MstResult run(Graph g, Metrics m) {
        int n = g.n();
        boolean[] inTree = new boolean[n];
        PriorityQueue<Edge> pq = new PriorityQueue<>((a, b) -> {
            m.comparisons++;
            return Integer.compare(a.w, b.w);
        });
        long total = 0;
        int used = 0;
        int components = 0;
        for (int s = 0; s < n; s++) {
            if (inTree[s]) {
                continue;
            }
            components++;
            inTree[s] = true;
            for (Edge e : g.adj(s)) {
                m.edgeScans++;
                if (!inTree[e.v]) {
                    pq.add(e);
                }
            }
            while (!pq.isEmpty()) {
                Edge e = pq.poll();
                if (inTree[e.v]) {
                    continue;
                }
                inTree[e.v] = true;
                total += e.w;
                used++;
                for (Edge f : g.adj(e.v)) {
                    m.edgeScans++;
                    if (!inTree[f.v]) {
                        pq.add(f);
                    }
                }
            }
        }
        return new MstResult(total, used, components);
    }
}