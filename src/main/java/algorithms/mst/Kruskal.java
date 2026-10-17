package algorithms.mst;

import algorithms.dsu.DSU;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.ArrayList;
import java.util.List;

public class Kruskal {

    public static MstResult run(Graph g, Metrics m) {
        List<Edge> sorted = new ArrayList<>(g.edges());
        sorted.sort((a, b) -> {
            m.comparisons++;
            return Integer.compare(a.w, b.w);
        });
        DSU dsu = new DSU(g.n(), m);
        long total = 0;
        int used = 0;
        for (Edge e : sorted) {
            m.edgeScans++;
            if (dsu.find(e.u) != dsu.find(e.v)) {
                dsu.union(e.u, e.v);
                total += e.w;
                used++;
                if (used == g.n() - 1) {
                    break;
                }
            }
        }
        return new MstResult(total, used, dsu.components());
    }
}