package mst;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PrimMST {
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
        if (graph.vertices == 0) {
            return new Result(new ArrayList<>(), 0, true, 0);
        }

        long ops = 0;
        boolean[] inMST = new boolean[graph.vertices];
        PriorityQueue<Edge> pq = new PriorityQueue<>();
        List<Edge> mstEdges = new ArrayList<>();
        int totalWeight = 0;

        inMST[0] = true;
        for (Edge e : graph.adj.get(0)) {
            pq.add(e);
            ops++;
        }

        while (!pq.isEmpty() && mstEdges.size() < graph.vertices - 1) {
            Edge e = pq.poll();
            ops++;

            if (inMST[e.u] && inMST[e.v]) {
                continue;
            }

            int nextVertex = inMST[e.u] ? e.v : e.u;
            inMST[nextVertex] = true;
            mstEdges.add(e);
            totalWeight += e.weight;

            for (Edge nextEdge : graph.adj.get(nextVertex)) {
                ops++;
                int neighbor = (nextEdge.u == nextVertex) ? nextEdge.v : nextEdge.u;
                if (!inMST[neighbor]) {
                    pq.add(nextEdge);
                }
            }
        }

        boolean isConnected = (graph.vertices <= 1) || (mstEdges.size() == graph.vertices - 1);
        return new Result(mstEdges, totalWeight, isConnected, ops);
    }
}