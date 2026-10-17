package algorithms.mst;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MstTest {

    private static Graph randomConnected(int n, int extra, Random rnd) {
        Graph g = new Graph(n, false);
        for (int i = 1; i < n; i++) {
            g.addEdge(rnd.nextInt(i), i, 1 + rnd.nextInt(50));
        }
        for (int k = 0; k < extra; k++) {
            g.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1 + rnd.nextInt(50));
        }
        return g;
    }

    @Test
    void kruskalAndPrimAgreeOnRandomConnectedGraphs() {
        Random rnd = new Random(7);
        for (int t = 0; t < 100; t++) {
            int n = 2 + rnd.nextInt(60);
            Graph g = randomConnected(n, rnd.nextInt(4 * n), rnd);
            MstResult k = Kruskal.run(g, new Metrics());
            MstResult p = Prim.run(g, new Metrics());
            assertEquals(k.totalWeight, p.totalWeight);
            assertEquals(n - 1, k.edgeCount);
            assertEquals(n - 1, p.edgeCount);
            assertEquals(1, k.components);
            assertEquals(1, p.components);
        }
    }

    @Test
    void disconnectedGraphGivesForest() {
        Graph g = new Graph(6, false);
        g.addEdge(0, 1, 3);
        g.addEdge(1, 2, 1);
        g.addEdge(0, 2, 7);
        g.addEdge(3, 4, 2);
        g.addEdge(3, 4, 5);
        g.addEdge(2, 2, 9);
        MstResult k = Kruskal.run(g, new Metrics());
        MstResult p = Prim.run(g, new Metrics());
        assertEquals(6, k.totalWeight);
        assertEquals(6, p.totalWeight);
        assertEquals(3, k.components);
        assertEquals(3, p.components);
        assertEquals(3, k.edgeCount);
        assertEquals(3, p.edgeCount);
    }

    @Test
    void emptyAndSingleVertexGraphsDoNotCrash() {
        Graph empty = new Graph(0, false);
        Graph single = new Graph(1, false);
        assertEquals(0, Kruskal.run(empty, new Metrics()).totalWeight);
        assertEquals(0, Prim.run(empty, new Metrics()).components);
        assertEquals(1, Kruskal.run(single, new Metrics()).components);
        assertEquals(1, Prim.run(single, new Metrics()).components);
    }
}