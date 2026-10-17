package algorithms.dsu;

import metrics.Metrics;

public class DSU {
    private int[] parent;
    private int[] rank;
    private int count;
    private final Metrics metrics;
    private long findCalls;
    private long unionCalls;

    public DSU(Metrics metrics) {
        this.metrics = metrics;
    }

    public DSU(int n, Metrics metrics) {
        this(metrics);
        makeSet(n);
    }

    public void makeSet(int n) {
        parent = new int[n];
        rank = new int[n];
        for (int i = 0; i < n; i++) {
            parent[i] = i;
        }
        count = n;
    }

    private int root(int x) {
        int r = x;
        while (parent[r] != r) {
            r = parent[r];
        }
        while (parent[x] != r) {
            int next = parent[x];
            parent[x] = r;
            x = next;
        }
        return r;
    }

    public int find(int x) {
        findCalls++;
        metrics.dsuOps++;
        return root(x);
    }

    public boolean union(int x, int y) {
        int a = root(x);
        int b = root(y);
        if (a == b) {
            return false;
        }
        if (rank[a] < rank[b]) {
            int t = a;
            a = b;
            b = t;
        }
        parent[b] = a;
        if (rank[a] == rank[b]) {
            rank[a]++;
        }
        count--;
        unionCalls++;
        metrics.dsuOps++;
        return true;
    }

    public int components() {
        return count;
    }

    public long findCalls() {
        return findCalls;
    }

    public long unionCalls() {
        return unionCalls;
    }
}