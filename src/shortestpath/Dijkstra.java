package shortestpath;
import graph.Edge;
import graph.Graph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

public class Dijkstra {
    public static class Node implements Comparable<Node> {
        public final int vertex;
        public final int dist;

        public Node(int vertex, int dist) {
            this.vertex = vertex;
            this.dist = dist;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.dist, other.dist);
        }
    }

    public static class Result {
        public final int[] distances;
        public final int[] parent;
        public final int source;
        public final long operationsCount;

        public Result(int[] distances, int[] parent, int source, long operationsCount) {
            this.distances = distances;
            this.parent = parent;
            this.source = source;
            this.operationsCount = operationsCount;
        }

        public List<Integer> getPathTo(int target) {
            if (distances[target] == Integer.MAX_VALUE) {
                return Collections.emptyList();
            }
            List<Integer> path = new ArrayList<>();
            for (int at = target; at != -1; at = parent[at]) {
                path.add(at);
            }
            Collections.reverse(path);
            return path;
        }
    }

    public static Result findShortestPaths(Graph graph, int source) {
        long ops = 0;
        int n = graph.vertices;
        int[] dist = new int[n];
        int[] parent = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        Arrays.fill(parent, -1);

        PriorityQueue<Node> pq = new PriorityQueue<>();
        dist[source] = 0;
        pq.add(new Node(source, 0));
        ops++;

        while (!pq.isEmpty()) {
            Node current = pq.poll();
            int u = current.vertex;
            ops++;

            if (current.dist > dist[u]) {
                continue;
            }

            for (Edge e : graph.adj.get(u)) {
                ops++;
                if (e.weight < 0) {
                    throw new IllegalArgumentException("Dijkstra does not support negative edge weights.");
                }

                if (dist[u] != Integer.MAX_VALUE && dist[u] + e.weight < dist[e.v]) {
                    dist[e.v] = dist[u] + e.weight;
                    parent[e.v] = u;
                    pq.add(new Node(e.v, dist[e.v]));
                    ops++;
                }
            }
        }

        return new Result(dist, parent, source, ops);
    }
}