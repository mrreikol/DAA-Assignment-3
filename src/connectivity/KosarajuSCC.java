package connectivity;
import graph.Edge;
import graph.Graph;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class KosarajuSCC {
    public static class Result {
        public final List<List<Integer>> components;
        public final long operationsCount;

        public Result(List<List<Integer>> components, long operationsCount) {
            this.components = components;
            this.operationsCount = operationsCount;
        }
    }

    public static Result findSCC(Graph graph) {
        long[] ops = new long[]{0};
        boolean[] visited = new boolean[graph.vertices];
        Deque<Integer> stack = new ArrayDeque<>();

        for (int i = 0; i < graph.vertices; i++) {
            ops[0]++;
            if (!visited[i]) {
                dfsOrder(graph, i, visited, stack, ops);
            }
        }

        Graph transpose = graph.getTranspose();
        ops[0] += graph.edges.size();

        boolean[] visitedT = new boolean[graph.vertices];
        List<List<Integer>> components = new ArrayList<>();

        while (!stack.isEmpty()) {
            int u = stack.pop();
            ops[0]++;
            if (!visitedT[u]) {
                List<Integer> component = new ArrayList<>();
                dfsCollect(transpose, u, visitedT, component, ops);
                components.add(component);
            }
        }

        return new Result(components, ops[0]);
    }

    private static void dfsOrder(Graph g, int u, boolean[] visited, Deque<Integer> stack, long[] ops) {
        visited[u] = true;
        for (Edge e : g.adj.get(u)) {
            ops[0]++;
            if (!visited[e.v]) {
                dfsOrder(g, e.v, visited, stack, ops);
            }
        }
        stack.push(u);
    }

    private static void dfsCollect(Graph g, int u, boolean[] visited, List<Integer> component, long[] ops) {
        visited[u] = true;
        component.add(u);
        for (Edge e : g.adj.get(u)) {
            ops[0]++;
            if (!visited[e.v]) {
                dfsCollect(g, e.v, visited, component, ops);
            }
        }
    }
}