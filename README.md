# Assignment 3: Graphs – DSU, MST, SCC and Shortest Paths

Daa City Network & Job Scheduler.

**Author:** <<Sirnebayev Zhursin, SE-2509>>

The project implements a graph class (adjacency list, directed or undirected) and six algorithms written from scratch: DSU with union by rank and path compression, Kruskal, Prim, Kosaraju (iterative DFS), Kahn, and shortest paths in a DAG. The benchmark counts the work of every algorithm (`dsuOps`, `edgeScans`, `comparisons`, `maxDepth`) and checks the results against the reference values of the assignment. The analysis is in [REPORT.md](REPORT.md).

## Requirements

- JDK 17 or newer (developed with JDK 25)
- Maven 3.8 or newer (IntelliJ IDEA bundles it)
- Python 3 with `matplotlib` (only for the plots)

## Build

```
mvn compile
```

## Run the tests

```
mvn test
```

22 JUnit 5 tests: DSU against a naive reference, Kruskal against Prim on 100 random graphs, Kosaraju against brute force, topological order and cycle detection, DAG shortest paths against Bellman-Ford with negative weights, stack safety on 100 000 vertices, and the input graphs from `data/`. The tests read `data/` by a relative path, so run them from the project root.

## Run the benchmark

```
mvn compile exec:java
```

Or run the class `benchmark.Benchmark` from the IDE. It generates ten graphs with `new Random(42)` (n = 1 000, 3 162, 10 000, 31 623, 100 000, each sparse with m = 4n and dense with m = 20n), runs the five algorithms on the matching graph kinds (undirected for MST, directed for Kosaraju, DAG for Kahn and the shortest paths), takes the median of 5 runs for the time and writes `results/results.csv` (50 rows, columns `algorithm,graph,n,m,time_ms,dsu_ops,edge_scans,comparisons,max_depth,result`). The run stops with an error if Kruskal and Prim disagree, if a topological order is invalid, if the edge-scan identities (2m for Kosaraju, m for Kahn and the DAG paths) break, or if any result differs from the reference table. The counters are the same on every machine; only `time_ms` changes.

## Build the plots

```
pip install matplotlib
python scripts/plot.py
```

Reads `results/results.csv` and writes three PNG files to `results/plots/`; it also prints the log-log slopes of `edgeScans`.

## Input format

A text file in `data/`: the first line is `n m directed|undirected`, then `m` lines `u v w` (vertices are numbered from 0). Provided graphs: `connected.txt`, `disconnected.txt` (an isolated vertex, a self-loop and parallel edges) and `cyclic.txt` (directed, with a cycle).

## Project layout

```
src/main/java/   graph, metrics, algorithms (dsu, mst, scc, topo, paths), benchmark
src/test/java/   JUnit 5 tests
data/            input graphs
results/         results.csv and plots/
scripts/         plot.py
REPORT.md        report
```

## AI use acknowledgement

<<Check that this matches what you really did, and change it if not.>>

I used an AI assistant (Claude, by Anthropic) while working on this assignment: to plan the project structure, to draft the source code and the JUnit tests, to explain the algorithms, and to draft the README and the report. I built the project in the IDE, ran the tests and the benchmark myself, checked the output against the reference table of the assignment, and compared the numbers in the report with my own `results.csv`. I have read all the code and can explain how each class works and why the algorithms have the complexity given in the report.