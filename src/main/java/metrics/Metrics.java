package metrics;

public class Metrics {
    public long dsuOps;
    public long edgeScans;
    public long comparisons;
    public int maxDepth;

    public void reset() {
        dsuOps = 0;
        edgeScans = 0;
        comparisons = 0;
        maxDepth = 0;
    }

    public void updateDepth(int depth) {
        if (depth > maxDepth) {
            maxDepth = depth;
        }
    }

    @Override
    public String toString() {
        return "dsuOps=" + dsuOps + ", edgeScans=" + edgeScans
                + ", comparisons=" + comparisons + ", maxDepth=" + maxDepth;
    }
}