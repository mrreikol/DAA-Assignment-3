import os
import matplotlib.pyplot as plt
import numpy as np

output_dir = os.path.join("results", "plots")
os.makedirs(output_dir, exist_ok=True)

vertices = [10, 100, 1000]
edges = [30, 500, 5000]

times = {
    "Kruskal MST": [38.4, 312.1, 3420.5],
    "Prim MST": [24.1, 225.8, 2890.2],
    "Dijkstra": [19.5, 198.3, 2410.6],
    "Kosaraju SCC": [16.3, 148.7, 1720.4],
    "TopoSort (DAG)": [8.2, 68.4, 794.6]
}

# 1. Execution Time vs Vertices
plt.figure(figsize=(8, 5))
for algo, t in times.items():
    plt.plot(vertices, t, marker='o', linewidth=2, label=algo)
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Number of Vertices (V)')
plt.ylabel('Execution Time (µs)')
plt.title('Execution Time vs. Number of Vertices')
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig(os.path.join(output_dir, 'time_vs_vertices.png'), dpi=300)
plt.close()

# 2. Execution Time vs Edges
plt.figure(figsize=(8, 5))
for algo, t in times.items():
    plt.plot(edges, t, marker='s', linewidth=2, label=algo)
plt.xscale('log')
plt.yscale('log')
plt.xlabel('Number of Edges (E)')
plt.ylabel('Execution Time (µs)')
plt.title('Execution Time vs. Number of Edges')
plt.grid(True, which="both", ls="--", alpha=0.5)
plt.legend()
plt.tight_layout()
plt.savefig(os.path.join(output_dir, 'time_vs_edges.png'), dpi=300)
plt.close()

# 3. Kruskal vs Prim
fig, ax1 = plt.subplots(figsize=(8, 5))
x = np.arange(3)
width = 0.35
ax1.bar(x - width/2, times["Kruskal MST"], width, label='Kruskal', color='#4C72B0')
ax1.bar(x + width/2, times["Prim MST"], width, label='Prim', color='#55A868')
ax1.set_xlabel('Graph Size')
ax1.set_ylabel('Execution Time (µs)')
ax1.set_title('MST Comparison: Kruskal vs Prim')
ax1.set_xticks(x)
ax1.set_xticklabels(['Small', 'Medium', 'Large'])
ax1.legend()
ax1.grid(axis='y', linestyle='--', alpha=0.7)
plt.tight_layout()
plt.savefig(os.path.join(output_dir, 'kruskal_vs_prim.png'), dpi=300)
plt.close()

print(f"Done! All charts are generated in: {output_dir}")
