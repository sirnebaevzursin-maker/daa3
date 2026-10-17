package graph;

import algorithms.mst.Kruskal;
import algorithms.mst.MstResult;
import algorithms.mst.Prim;
import algorithms.scc.Kosaraju;
import algorithms.topo.Kahn;
import metrics.Metrics;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DataGraphsTest {

    @Test
    void connectedGraph() throws IOException {
        Graph g = GraphReader.read("data/connected.txt");
        assertEquals(5, g.n());
        assertEquals(6, g.m());
        MstResult k = Kruskal.run(g, new Metrics());
        MstResult p = Prim.run(g, new Metrics());
        assertEquals(11, k.totalWeight);
        assertEquals(11, p.totalWeight);
        assertEquals(1, k.components);
        assertEquals(1, p.components);
    }

    @Test
    void disconnectedGraph() throws IOException {
        Graph g = GraphReader.read("data/disconnected.txt");
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
    void cyclicGraph() throws IOException {
        Graph g = GraphReader.read("data/cyclic.txt");
        assertTrue(g.isDirected());
        assertTrue(Kahn.run(g, new Metrics()).hasCycle);
        assertEquals(3, Kosaraju.run(g, new Metrics()).count);
    }

    @Test
    void connectedGraphHasNoCycleInDirectedForm() throws IOException {
        Graph g = GraphReader.read("data/cyclic.txt");
        assertFalse(Kahn.run(g, new Metrics()).order.length > 0);
    }
}