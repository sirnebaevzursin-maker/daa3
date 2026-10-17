import csv
import math
import os
import sys

import matplotlib
matplotlib.use("Agg")
import matplotlib.pyplot as plt

CSV_PATH = sys.argv[1] if len(sys.argv) > 1 else "results/results.csv"
OUT_DIR = sys.argv[2] if len(sys.argv) > 2 else "results/plots"
os.makedirs(OUT_DIR, exist_ok=True)

rows = []
with open(CSV_PATH, newline="") as f:
    for r in csv.DictReader(f):
        for k in ("n", "m", "dsu_ops", "edge_scans", "comparisons", "max_depth"):
            r[k] = int(r[k])
        r["density"] = r["graph"].split("_")[1]
        rows.append(r)

ALGOS = ["Kruskal", "Prim", "Kosaraju", "Kahn", "DagShortestPath"]
COLORS = {"Kruskal": "tab:blue", "Prim": "tab:orange", "Kosaraju": "tab:green",
          "Kahn": "tab:red", "DagShortestPath": "tab:purple"}
STYLES = {"sparse": "-", "dense": "--"}
MARKERS = {"sparse": "o", "dense": "s"}


def series(algo, density):
    s = sorted((r for r in rows if r["algorithm"] == algo and r["density"] == density),
               key=lambda r: r["n"])
    return s


def slope(xs, ys):
    lx = [math.log10(x) for x in xs]
    ly = [math.log10(y) for y in ys]
    mx = sum(lx) / len(lx)
    my = sum(ly) / len(ly)
    return sum((a - mx) * (b - my) for a, b in zip(lx, ly)) / sum((a - mx) ** 2 for a in lx)


fig, ax = plt.subplots(figsize=(8, 6))
for algo in ALGOS:
    for d in ("sparse", "dense"):
        s = series(algo, d)
        ax.plot([r["n"] for r in s], [r["edge_scans"] for r in s], STYLES[d],
                marker=MARKERS[d], color=COLORS[algo], label=f"{algo} ({d})")
ax.set_xscale("log")
ax.set_yscale("log")
ax.set_xlabel("n (vertices)")
ax.set_ylabel("edgeScans")
ax.set_title("Edge scans vs n (log-log)")
ax.grid(True, which="both", alpha=0.3)
ax.legend(fontsize=8, ncol=2)
fig.tight_layout()
fig.savefig(os.path.join(OUT_DIR, "plot1_operations_vs_n.png"), dpi=150)
plt.close(fig)

fig, ax = plt.subplots(figsize=(8, 6))
for algo in ALGOS:
    for d in ("sparse", "dense"):
        s = series(algo, d)
        ax.plot([r["n"] for r in s], [r["edge_scans"] / r["m"] for r in s], STYLES[d],
                marker=MARKERS[d], color=COLORS[algo], label=f"{algo} ({d})")
ax.set_xscale("log")
ax.set_xlabel("n (vertices)")
ax.set_ylabel("edgeScans / m")
ax.set_title("Scans per edge vs n")
ax.set_ylim(0, 2.4)
ax.grid(True, which="both", alpha=0.3)
ax.legend(fontsize=8, ncol=2)
fig.tight_layout()
fig.savefig(os.path.join(OUT_DIR, "plot2_scans_per_edge.png"), dpi=150)
plt.close(fig)

fig, ax = plt.subplots(figsize=(8, 6))
for d in ("sparse", "dense"):
    s = series("Kruskal", d)
    ax.plot([r["n"] for r in s], [r["edge_scans"] / r["m"] for r in s], STYLES[d],
            marker=MARKERS[d], label=f"Kruskal ({d})")
ax.set_xscale("log")
ax.set_xlabel("n (vertices)")
ax.set_ylabel("edgeScans / m")
ax.set_title("Share of edges Kruskal examines")
ax.set_ylim(0, 1.05)
ax.grid(True, which="both", alpha=0.3)
ax.legend()
fig.tight_layout()
fig.savefig(os.path.join(OUT_DIR, "plot3_kruskal_share.png"), dpi=150)
plt.close(fig)

print("slopes of edgeScans vs n (log-log):")
for algo in ALGOS:
    for d in ("sparse", "dense"):
        s = series(algo, d)
        print(f"  {algo:16s} {d:6s} {slope([r['n'] for r in s], [r['edge_scans'] for r in s]):.3f}")
print("plots saved to", OUT_DIR)