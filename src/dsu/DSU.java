package dsu;

public class DSU {
    private final int[] parent;
    private final int[] rank;
    private int components;
    public long findCount = 0;
    public long unionCount = 0;

    public DSU(int n) {
        parent = new int[n];
        rank = new int[n];
        components = n;
        for (int i = 0; i < n; i++) {
            parent[i] = i;
            rank[i] = 0;
        }
    }

    public int find(int i) {
        findCount++;
        if (parent[i] == i) {
            return i;
        }
        parent[i] = find(parent[i]);
        return parent[i];
    }

    public boolean union(int i, int j) {
        unionCount++;
        int rootI = find(i);
        int rootJ = find(j);

        if (rootI == rootJ) {
            return false;
        }

        if (rank[rootI] < rank[rootJ]) {
            parent[rootI] = rootJ;
        } else if (rank[rootI] > rank[rootJ]) {
            parent[rootJ] = rootI;
        } else {
            parent[rootJ] = rootI;
            rank[rootI]++;
        }
        components--;
        return true;
    }

    public int getComponents() {
        return components;
    }
}