package algorithms.paths;

import algorithms.topo.Kahn;
import algorithms.topo.TopoResult;
import graph.Edge;
import graph.Graph;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DagShortestPathTest {

    private static long[] bellmanFord(Graph g) {
        int n = g.n();
        long[] d = new long[n];
        Arrays.fill(d, DagShortestPath.INF);
        int[] in = g.inDegrees();
        for (int v = 0; v < n; v++) {
            if (in[v] == 0) {
                d[v] = 0;
            }
        }
        for (int it = 0; it < n - 1; it++) {
            for (Edge e : g.edges()) {
                if (d[e.u] < DagShortestPath.INF && d[e.u] + e.w < d[e.v]) {
                    d[e.v] = d[e.u] + e.w;
                }
            }
        }
        return d;
    }

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
            g.addEdge(a, b, rnd.nextInt(21) - 10);
        }
        return g;
    }

    @Test
    void matchesBellmanFordWithNegativeWeights() {
        Random rnd = new Random(21);
        for (int t = 0; t < 100; t++) {
            int n = 1 + rnd.nextInt(30);
            Graph g = randomDag(n, rnd.nextInt(4 * n), rnd);
            TopoResult topo = Kahn.run(g, new Metrics());
            Metrics metrics = new Metrics();
            PathResult r = DagShortestPath.run(g, topo, metrics);
            assertArrayEquals(bellmanFord(g), r.dist);
            assertEquals(g.m(), metrics.edgeScans);
        }
    }

    @Test
    void restoresPath() {
        Graph g = new Graph(3, true);
        g.addEdge(0, 1, 2);
        g.addEdge(1, 2, -5);
        g.addEdge(0, 2, 1);
        TopoResult topo = Kahn.run(g, new Metrics());
        PathResult r = DagShortestPath.run(g, topo, new Metrics());
        assertArrayEquals(new long[]{0, 2, -3}, r.dist);
        assertEquals(List.of(0, 1, 2), r.pathTo(2));
        assertEquals(-1, r.sumDist);
    }

    @Test
    void everySourceStartsAtZero() {
        Graph g = new Graph(4, true);
        g.addEdge(0, 2, 5);
        g.addEdge(1, 2, 3);
        g.addEdge(2, 3, 1);
        TopoResult topo = Kahn.run(g, new Metrics());
        PathResult r = DagShortestPath.run(g, topo, new Metrics());
        assertArrayEquals(new long[]{0, 0, 3, 4}, r.dist);
    }

    @Test
    void refusesGraphWithCycle() {
        Graph g = new Graph(2, true);
        g.addEdge(0, 1, 1);
        g.addEdge(1, 0, 1);
        TopoResult topo = Kahn.run(g, new Metrics());
        assertThrows(IllegalArgumentException.class,
                () -> DagShortestPath.run(g, topo, new Metrics()));
    }
}