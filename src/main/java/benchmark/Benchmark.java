package benchmark;

import algorithms.mst.Kruskal;
import algorithms.mst.MstResult;
import algorithms.mst.Prim;
import algorithms.paths.DagShortestPath;
import algorithms.scc.Kosaraju;
import algorithms.topo.Kahn;
import algorithms.topo.TopoResult;
import algorithms.topo.TopoValidator;
import graph.Graph;
import metrics.Metrics;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Benchmark {
    private static final int[] SIZES = {1000, 3162, 10000, 31623, 100000};
    private static final int[] DENSITIES = {4, 20};
    private static final int RUNS = 5;

    private interface Task {
        long run(Metrics m);
    }

    public static void main(String[] args) throws IOException {
        Path out = Path.of(args.length > 0 ? args[0] : "results/results.csv");
        if (out.getParent() != null) {
            Files.createDirectories(out.getParent());
        }
        List<String> rows = new ArrayList<>();
        rows.add("algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result");

        for (int n : SIZES) {
            for (int d : DENSITIES) {
                int m = n * d;
                String label = n + "_" + (d == 4 ? "sparse" : "dense");
                Graph und = GraphGenerator.undirected(n, m);
                Graph dir = GraphGenerator.directed(n, m);
                Graph dag = GraphGenerator.dag(n, m);

                long[] mstWeights = new long[2];
                rows.add(measure("Kruskal", label, n, m, m1 -> {
                    MstResult r = Kruskal.run(und, m1);
                    mstWeights[0] = r.totalWeight;
                    return r.totalWeight;
                }));
                rows.add(measure("Prim", label, n, m, m1 -> {
                    MstResult r = Prim.run(und, m1);
                    mstWeights[1] = r.totalWeight;
                    return r.totalWeight;
                }));
                require(mstWeights[0] == mstWeights[1], label + ": Kruskal and Prim weights differ");
                require(mstWeights[0] == ReferenceValues.mst(n, m), label + ": MST weight differs from reference");

                long[] sccCount = new long[1];
                rows.add(measure("Kosaraju", label, n, m, m1 -> {
                    sccCount[0] = Kosaraju.run(dir, m1).count;
                    return sccCount[0];
                }));
                require(sccCount[0] == ReferenceValues.scc(n, m), label + ": SCC count differs from reference");

                TopoResult[] topo = new TopoResult[1];
                rows.add(measure("Kahn", label, n, m, m1 -> {
                    topo[0] = Kahn.run(dag, m1);
                    return topo[0].processed;
                }));
                require(!topo[0].hasCycle, label + ": generated DAG reported a cycle");
                require(TopoValidator.isValid(dag, topo[0].order), label + ": topological order is invalid");

                long[] sum = new long[1];
                rows.add(measure("DagShortestPath", label, n, m, m1 -> {
                    sum[0] = DagShortestPath.run(dag, topo[0], m1).sumDist;
                    return sum[0];
                }));
                require(sum[0] == ReferenceValues.dagSum(n, m), label + ": DAG distance sum differs from reference");

                System.out.println(label + " ok");
            }
        }
        Files.write(out, rows);
        System.out.println("written " + out + " (" + (rows.size() - 1) + " rows)");
    }

    private static String measure(String algo, String graph, int n, int m, Task task) {
        long[] times = new long[RUNS];
        Metrics metrics = new Metrics();
        long result = 0;
        for (int i = 0; i < RUNS; i++) {
            metrics = new Metrics();
            long t0 = System.nanoTime();
            result = task.run(metrics);
            times[i] = System.nanoTime() - t0;
        }
        Arrays.sort(times);
        double ms = times[RUNS / 2] / 1e6;
        if (algo.equals("Kosaraju")) {
            require(metrics.edgeScans == 2L * m, graph + ": Kosaraju must scan 2m edges");
        }
        if (algo.equals("Kahn") || algo.equals("DagShortestPath")) {
            require(metrics.edgeScans == m, graph + ": " + algo + " must scan m edges");
        }
        return String.format(Locale.ROOT, "%s,%s,%d,%d,%.3f,%d,%d,%d,%d,%d",
                algo, graph, n, m, ms, metrics.dsuOps, metrics.edgeScans,
                metrics.comparisons, metrics.maxDepth, result);
    }

    private static void require(boolean ok, String message) {
        if (!ok) {
            throw new IllegalStateException(message);
        }
    }
}