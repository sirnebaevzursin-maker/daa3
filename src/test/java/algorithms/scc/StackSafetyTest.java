package algorithms.scc;

import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StackSafetyTest {

    @Test
    void pathOfHundredThousandVerticesDoesNotOverflow() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i + 1 < n; i++) {
            g.addEdge(i, i + 1, 1);
        }
        Metrics metrics = new Metrics();
        SccResult r = assertDoesNotThrow(() -> Kosaraju.run(g, metrics));
        assertEquals(n, r.count);
        assertTrue(metrics.maxDepth >= n);
    }

    @Test
    void bigCycleIsOneComponent() {
        int n = 100_000;
        Graph g = new Graph(n, true);
        for (int i = 0; i < n; i++) {
            g.addEdge(i, (i + 1) % n, 1);
        }
        SccResult r = assertDoesNotThrow(() -> Kosaraju.run(g, new Metrics()));
        assertEquals(1, r.count);
    }
}