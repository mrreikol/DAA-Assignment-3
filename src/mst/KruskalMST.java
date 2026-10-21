package mst;
import dsu.DSU;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KruskalMST {
    public static class Result {
        public final List<Edge> mstEdges;
        public final int totalWeight;
        public final boolean isConnected;
        public final long operationsCount;

        public Result(List<Edge> mstEdges, int totalWeight, boolean isConnected, long operationsCount) {
            this.mstEdges = mstEdges;
            this.totalWeight = totalWeight;
            this.isConnected = isConnected;
            this.operationsCount = operationsCount;
        }
    }

    public static Result computeMST(Graph graph) {
        long ops = 0;
        List<Edge> allEdges = new ArrayList<>(graph.edges);
        Collections.sort(allEdges);
        ops += (long) allEdges.size() * (long) (Math.log(allEdges.size() + 1) / Math.log(2));

        DSU dsu = new DSU(graph.vertices);
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        for (Edge e : allEdges) {
            ops++;
            if (dsu.union(e.u, e.v)) {
                mstEdges.add(e);
                totalWeight += e.weight;
                if (mstEdges.size() == graph.vertices - 1) {
                    break;
                }
            }
        }

        ops += (dsu.findCount + dsu.unionCount);
        boolean isConnected = (graph.vertices <= 1) || (mstEdges.size() == graph.vertices - 1);
        return new Result(mstEdges, totalWeight, isConnected, ops);
    }
}