package algorithms.topo;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TopoTest {

    private static Graph randomDag(int n, int m, Random rnd) {
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) {
            rank[i] = i;
        }
        for (int i = n - 1; i > 0; i--) {
            int j = rnd.nextInt(i + 1);
            int t = rank[i];
            rank[i] = rank[j];
            rank[j] = t;
        }
        Graph g = new Graph(n, true);
        for (int k = 0; k < m; k++) {
            int a = rnd.nextInt(n);
            int b = rnd.nextInt(n);
            if (a == b) {
                continue;
            }
            if (rank[a] > rank[b]) {
                int t = a;
                a = b;
                b = t;
            }
            g.addEdge(a, b, 1 + rnd.nextInt(20));
        }
        return g;
    }

    @Test
    void validOrderOnRandomDags() {
        Random rnd = new Random(3);
        for (int t = 0; t < 50; t++) {
            int n = 1 + rnd.nextInt(100);
            Graph g = randomDag(n, rnd.nextInt(4 * n), rnd);
            Metrics metrics = new Metrics();
            TopoResult r = Kahn.run(g, metrics);
            assertFalse(r.hasCycle);
            assertEquals(n, r.processed);
            assertTrue(TopoValidator.isValid(g, r.order));
            assertEquals(g.m(), metrics.edgeScans);
        }
    }

    @Test
    void cycleIsDetected() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 1, 1);
        g.addEdge(2, 3, 1);
        TopoResult r = Kahn.run(g, new Metrics());
        assertTrue(r.hasCycle);
        assertEquals(0, r.order.length);
        assertTrue(r.processed < 4);
    }

    @Test
    void selfLoopIsACycle() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 0, 1);
        g.addEdge(0, 1, 1);
        assertTrue(Kahn.run(g, new Metrics()).hasCycle);
    }

    @Test
    void validatorRejectsWrongOrder() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        assertTrue(TopoValidator.isValid(g, new int[]{0, 1, 2}));
        assertFalse(TopoValidator.isValid(g, new int[]{2, 1, 0}));
        assertFalse(TopoValidator.isValid(g, new int[]{0, 1}));
        assertFalse(TopoValidator.isValid(g, new int[]{0, 0, 2}));
    }
}