package algorithms.topo;

import graph.Edge;
import graph.Graph;

import java.util.Arrays;

public class TopoValidator {

    public static boolean isValid(Graph g, int[] order) {
        int n = g.n();
        if (order.length != n) {
            return false;
        }
        int[] pos = new int[n];
        Arrays.fill(pos, -1);
        for (int i = 0; i < n; i++) {
            int v = order[i];
            if (v < 0 || v >= n || pos[v] != -1) {
                return false;
            }
            pos[v] = i;
        }
        for (Edge e : g.edges()) {
            if (pos[e.u] >= pos[e.v]) {
                return false;
            }
        }
        return true;
    }
}