package org.jmhsieh.graphs;

import java.util.PriorityQueue;

import org.jmhsieh.graphs.Graph.E;
import org.jmhsieh.graphs.Graph.V;
import org.jmhsieh.sets.unionfind.UnionFind;


/**
 * Kruskal's Algorithm: Min spanning tree using Union Find algorithm.
 */
public class Kruskals<T> {
  static class Solver<T> {
    Graph<T, Integer> g;
    PriorityQueue<E<T, Integer>> pq;
    Graph<T, Integer> mst = new Graph<T, Integer>();
    UnionFind<V<T>> clusters = new UnionFind<V<T>>();

    Solver(Graph<T, Integer> gr) {
      this.g = gr;
    }

    public void solve() {
      int maxedges = g.getVerticies().size();
      for (V<T> v : g.getVerticies()) {
        clusters.addValue(v);
      }

      for (E<T, Integer> e : g.getEdges()) {
        pq.add(e);
      }

      while (mst.getEdges().size() < maxedges) {
        E<T, Integer> e = pq.poll();
        V<T> x = clusters.find(e.getDest());
        V<T> y = clusters.find(e.getSrc());

        if (x != y) {
          mst.newEdge(e.value(), e.getSrc(), e.getDest());
          clusters.union(x, y);
        }
      }
    }

    public Graph<T, Integer> minSpanTree() {
      return mst;
    }
  };

  Solver<T> solver(Graph<T, Integer> g) {
    Solver<T> s = new Solver<T>(g);
    s.solve();
    return s;
  }
}
