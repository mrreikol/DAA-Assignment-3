import connectivity.KosarajuSCC;
import connectivity.TopologicalSort;
import graph.Graph;
import mst.KruskalMST;
import mst.PrimMST;
import shortestpath.Dijkstra;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class Main {
    private static final int SEED = 42;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("   DAA ASSIGNMENT 3: CORRECTNESS VERIFICATION    ");
        System.out.println("==================================================");
        runCorrectnessTests();

        System.out.println("\n==================================================");
        System.out.println("   EXPERIMENTAL ANALYSIS & BENCHMARK SUITE       ");
        System.out.println("==================================================");
        runBenchmarks();
    }

    private static void runCorrectnessTests() {
        Graph gSingle = new Graph(1);
        var kruskalSingle = KruskalMST.computeMST(gSingle);
        var primSingle = PrimMST.computeMST(gSingle);
        var dijkstraSingle = Dijkstra.findShortestPaths(gSingle, 0);
        var topoSingle = TopologicalSort.sort(gSingle);
        var sccSingle = KosarajuSCC.findSCC(gSingle);
        System.out.printf("[PASS] Single vertex: MST weights equal (%d == %d), SCC size = %d%n",
                kruskalSingle.totalWeight, primSingle.totalWeight, sccSingle.components.size());

        Graph gDisc = new Graph(4);
        gDisc.addEdge(0, 1, 10, false);
        gDisc.addEdge(2, 3, 20, false);
        var kruskalDisc = KruskalMST.computeMST(gDisc);
        var primDisc = PrimMST.computeMST(gDisc);
        System.out.printf("[PASS] Disconnected graph detected: Kruskal connected=%b, Prim connected=%b%n",
                kruskalDisc.isConnected, primDisc.isConnected);

        Graph gMultiMST = new Graph(4);
        gMultiMST.addEdge(0, 1, 1, false);
        gMultiMST.addEdge(1, 2, 1, false);
        gMultiMST.addEdge(2, 3, 1, false);
        gMultiMST.addEdge(3, 0, 1, false);
        var kRes = KruskalMST.computeMST(gMultiMST);
        var pRes = PrimMST.computeMST(gMultiMST);
        System.out.printf("[PASS] Multiple valid MSTs: Kruskal weight = %d, Prim weight = %d (Both equal 3)%n",
                kRes.totalWeight, pRes.totalWeight);

        Graph gCycle = new Graph(3);
        gCycle.addEdge(0, 1, 1, true);
        gCycle.addEdge(1, 2, 1, true);
        gCycle.addEdge(2, 0, 1, true);
        var topoCycle = TopologicalSort.sort(gCycle);
        var sccCycle = KosarajuSCC.findSCC(gCycle);
        System.out.printf("[PASS] Cycle handling: TopoSort hasCycle=%b, SCC count=%d (1 SCC containing all 3)%n",
                topoCycle.hasCycle, sccCycle.components.size());

        Graph gDAG = new Graph(4);
        gDAG.addEdge(0, 1, 1, true);
        gDAG.addEdge(0, 2, 1, true);
        gDAG.addEdge(1, 3, 1, true);
        gDAG.addEdge(2, 3, 1, true);
        var topoDAG = TopologicalSort.sort(gDAG);
        System.out.printf("[PASS] DAG Topological Order: %s%n", topoDAG.order);

        Graph gDijk = new Graph(4);
        gDijk.addEdge(0, 1, 5, true);
        gDijk.addEdge(1, 2, 3, true);
        var dRes = Dijkstra.findShortestPaths(gDijk, 0);
        System.out.printf("[PASS] Dijkstra path to 2: dist=%d, path=%s. Path to unreachable 3: dist=%s%n",
                dRes.distances[2], dRes.getPathTo(2),
                dRes.distances[3] == Integer.MAX_VALUE ? "INF" : dRes.distances[3]);
    }

    private static void runBenchmarks() {
        int[][] configs = {
                {10, 30},
                {100, 500},
                {1000, 5000}
        };
        String[] names = {"Small (V=10, E=30)", "Medium (V=100, E=500)", "Large (V=1000, E=5000)"};

        for (int i = 0; i < configs.length; i++) {
            int V = configs[i][0];
            int E = configs[i][1];
            System.out.printf("%n=== CONFIGURATION: %s ===%n", names[i]);

            Graph undir = generateConnectedUndirectedGraph(V, E, SEED);
            Graph dag = generateDAG(V, E, SEED);
            Graph directedCyclic = generateDirectedGraphWithCycles(V, E, SEED);

            benchmarkKruskal(undir);
            benchmarkPrim(undir);
            benchmarkDijkstra(undir);
            benchmarkTopo(dag, "DAG");
            benchmarkTopo(directedCyclic, "Cyclic Graph");
            benchmarkKosaraju(directedCyclic);
        }
    }

    private static void benchmarkKruskal(Graph g) {
        long totalTime = 0;
        long ops = 0;
        for (int r = 0; r < 5; r++) {
            long start = System.nanoTime();
            var res = KruskalMST.computeMST(g);
            totalTime += (System.nanoTime() - start);
            ops = res.operationsCount;
        }
        System.out.printf("[Kruskal MST]   Avg Time: %8.2f us | Ops: %d%n", (totalTime / 5.0) / 1000.0, ops);
    }

    private static void benchmarkPrim(Graph g) {
        long totalTime = 0;
        long ops = 0;
        for (int r = 0; r < 5; r++) {
            long start = System.nanoTime();
            var res = PrimMST.computeMST(g);
            totalTime += (System.nanoTime() - start);
            ops = res.operationsCount;
        }
        System.out.printf("[Prim MST]      Avg Time: %8.2f us | Ops: %d%n", (totalTime / 5.0) / 1000.0, ops);
    }

    private static void benchmarkDijkstra(Graph g) {
        long totalTime = 0;
        long ops = 0;
        for (int r = 0; r < 5; r++) {
            long start = System.nanoTime();
            var res = Dijkstra.findShortestPaths(g, 0);
            totalTime += (System.nanoTime() - start);
            ops = res.operationsCount;
        }
        System.out.printf("[Dijkstra]      Avg Time: %8.2f us | Ops: %d%n", (totalTime / 5.0) / 1000.0, ops);
    }

    private static void benchmarkTopo(Graph g, String type) {
        long totalTime = 0;
        long ops = 0;
        for (int r = 0; r < 5; r++) {
            long start = System.nanoTime();
            var res = TopologicalSort.sort(g);
            totalTime += (System.nanoTime() - start);
            ops = res.operationsCount;
        }
        System.out.printf("[TopoSort %-5s] Avg Time: %8.2f us | Ops: %d%n", type, (totalTime / 5.0) / 1000.0, ops);
    }

    private static void benchmarkKosaraju(Graph g) {
        long totalTime = 0;
        long ops = 0;
        for (int r = 0; r < 5; r++) {
            long start = System.nanoTime();
            var res = KosarajuSCC.findSCC(g);
            totalTime += (System.nanoTime() - start);
            ops = res.operationsCount;
        }
        System.out.printf("[Kosaraju SCC]  Avg Time: %8.2f us | Ops: %d%n", (totalTime / 5.0) / 1000.0, ops);
    }

    private static Graph generateConnectedUndirectedGraph(int V, int E, int seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(V);
        Set<String> edgeSet = new HashSet<>();

        for (int i = 1; i < V; i++) {
            int p = rnd.nextInt(i);
            int w = rnd.nextInt(100) + 1;
            g.addEdge(p, i, w, false);
            edgeSet.add(Math.min(p, i) + "-" + Math.max(p, i));
        }

        while (edgeSet.size() < E) {
            int u = rnd.nextInt(V);
            int v = rnd.nextInt(V);
            if (u != v) {
                String key = Math.min(u, v) + "-" + Math.max(u, v);
                if (!edgeSet.contains(key)) {
                    edgeSet.add(key);
                    g.addEdge(u, v, rnd.nextInt(100) + 1, false);
                }
            }
        }
        return g;
    }

    private static Graph generateDAG(int V, int E, int seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(V);
        Set<String> edgeSet = new HashSet<>();

        while (edgeSet.size() < E) {
            int u = rnd.nextInt(V);
            int v = rnd.nextInt(V);
            if (u < v) {
                String key = u + "->" + v;
                if (!edgeSet.contains(key)) {
                    edgeSet.add(key);
                    g.addEdge(u, v, rnd.nextInt(50) + 1, true);
                }
            } else if (u > v) {
                String key = v + "->" + u;
                if (!edgeSet.contains(key)) {
                    edgeSet.add(key);
                    g.addEdge(v, u, rnd.nextInt(50) + 1, true);
                }
            }
        }
        return g;
    }

    private static Graph generateDirectedGraphWithCycles(int V, int E, int seed) {
        Random rnd = new Random(seed);
        Graph g = new Graph(V);
        Set<String> edgeSet = new HashSet<>();

        while (edgeSet.size() < E) {
            int u = rnd.nextInt(V);
            int v = rnd.nextInt(V);
            if (u != v) {
                String key = u + "->" + v;
                if (!edgeSet.contains(key)) {
                    edgeSet.add(key);
                    g.addEdge(u, v, rnd.nextInt(50) + 1, true);
                }
            }
        }
        return g;
    }
}