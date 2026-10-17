package algorithms.mst;

public class MstResult {
    public final long totalWeight;
    public final int edgeCount;
    public final int components;

    public MstResult(long totalWeight, int edgeCount, int components) {
        this.totalWeight = totalWeight;
        this.edgeCount = edgeCount;
        this.components = components;
    }
}