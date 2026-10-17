package algorithms.scc;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.ArrayDeque;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SccTest {

    private static boolean[][] reachability(Graph g) {
        int n = g.n();
        boolean[][] reach = new boolean[n][n];
        for (int s = 0; s < n; s++) {
            ArrayDeque<Integer> queue = new ArrayDeque<>();
            reach[s][s] = true;
            queue.add(s);
            while (!queue.isEmpty()) {
                int u = queue.poll();
                for (Edge e : g.adj(u)) {
                    if (!reach[s][e.v]) {
                        reach[s][e.v] = true;
                        queue.add(e.v);
                    }
                }
            }
        }
        return reach;
    }

    @Test
    void matchesBruteForceOnRandomGraphs() {
        Random rnd = new Random(11);
        for (int t = 0; t < 60; t++) {
            int n = 1 + rnd.nextInt(200);
            int m = rnd.nextInt(3 * n + 1);
            Graph g = new Graph(n, true);
            for (int k = 0; k < m; k++) {
                g.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1 + rnd.nextInt(10));
            }
            SccResult r = Kosaraju.run(g, new Metrics());
            boolean[][] reach = reachability(g);
            int[] comp = r.component;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    boolean same = reach[i][j] && reach[j][i];
                    assertEquals(same, comp[i] == comp[j]);
                }
            }
        }
    }

    @Test
    void cycleIsOneComponent() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 2, 1);
        g.addEdge(2, 0, 1);
        g.addEdge(2, 3, 1);
        assertEquals(2, Kosaraju.run(g, new Metrics()).count);
    }

    @Test
    void scansTwiceTheEdges() {
        Random rnd = new Random(5);
        int n = 300;
        int m = 1200;
        Graph g = new Graph(n, true);
        for (int k = 0; k < m; k++) {
            g.addEdge(rnd.nextInt(n), rnd.nextInt(n), 1);
        }
        Metrics metrics = new Metrics();
        Kosaraju.run(g, metrics);
        assertEquals(2L * m, metrics.edgeScans);
    }
}