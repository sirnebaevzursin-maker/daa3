package algorithms.topo;

public class TopoResult {
    public final int[] order;
    public final boolean hasCycle;
    public final int processed;

    public TopoResult(int[] order, boolean hasCycle, int processed) {
        this.order = order;
        this.hasCycle = hasCycle;
        this.processed = processed;
    }
}