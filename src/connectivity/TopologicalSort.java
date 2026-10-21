package connectivity;
import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

public class TopologicalSort {
    public static class Result {
        public final List<Integer> order;
        public final boolean hasCycle;
        public final long operationsCount;

        public Result(List<Integer> order, boolean hasCycle, long operationsCount) {
            this.order = order;
            this.hasCycle = hasCycle;
            this.operationsCount = operationsCount;
        }
    }

    public static Result sort(Graph graph) {
        long ops = 0;
        int[] inDegree = new int[graph.vertices];

        for (int u = 0; u < graph.vertices; u++) {
            for (Edge e : graph.adj.get(u)) {
                inDegree[e.v]++;
                ops++;
            }
        }

        Queue<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < graph.vertices; i++) {
            ops++;
            if (inDegree[i] == 0) {
                queue.add(i);
            }
        }

        List<Integer> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            order.add(u);
            ops++;

            for (Edge e : graph.adj.get(u)) {
                ops++;
                inDegree[e.v]--;
                if (inDegree[e.v] == 0) {
                    queue.add(e.v);
                }
            }
        }

        boolean hasCycle = order.size() != graph.vertices;
        return new Result(hasCycle ? null : order, hasCycle, ops);
    }
}