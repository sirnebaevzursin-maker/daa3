package algorithms.scc;

import graph.Edge;
import graph.Graph;
import metrics.Metrics;

import java.util.List;

public class Kosaraju {

    public static SccResult run(Graph g, Metrics m) {
        int n = g.n();
        int[] order = new int[n];
        int finished = 0;
        boolean[] visited = new boolean[n];
        int[] stack = new int[n];
        int[] next = new int[n];

        for (int s = 0; s < n; s++) {
            if (visited[s]) {
                continue;
            }
            int sp = 0;
            stack[sp++] = s;
            visited[s] = true;
            m.updateDepth(sp);
            while (sp > 0) {
                int u = stack[sp - 1];
                List<Edge> adj = g.adj(u);
                if (next[u] < adj.size()) {
                    Edge e = adj.get(next[u]++);
                    m.edgeScans++;
                    if (!visited[e.v]) {
                        visited[e.v] = true;
                        stack[sp++] = e.v;
                        m.updateDepth(sp);
                    }
                } else {
                    sp--;
                    order[finished++] = u;
                }
            }
        }

        Graph rev = g.reverse();
        int[] component = new int[n];
        boolean[] seen = new boolean[n];
        int count = 0;
        for (int i = n - 1; i >= 0; i--) {
            int s = order[i];
            if (seen[s]) {
                continue;
            }
            int sp = 0;
            stack[sp++] = s;
            seen[s] = true;
            component[s] = count;
            m.updateDepth(sp);
            while (sp > 0) {
                int u = stack[--sp];
                for (Edge e : rev.adj(u)) {
                    m.edgeScans++;
                    if (!seen[e.v]) {
                        seen[e.v] = true;
                        component[e.v] = count;
                        stack[sp++] = e.v;
                        m.updateDepth(sp);
                    }
                }
            }
            count++;
        }
        return new SccResult(component, count);
    }
}