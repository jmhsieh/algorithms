package org.jmhsieh.graphs;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jmhsieh.graphs.Graph.E;
import org.jmhsieh.graphs.Graph.V;
import org.jmhsieh.structs.Pair;
import org.jmhsieh.util.F.Fun;

import com.google.common.base.Preconditions;

/**
 * This is an implementation of Dijkstra's shortest path algo on my generic
 * graph classes.
 * 
 * Edges must have weight greater than or equal to 0F
 */
public class Dijkstras<T> {

  final Graph<T, Integer> g;

  public Dijkstras(Graph<T, Integer> gr) {
    this.g = gr;
  }

  static class CostPQ<T> extends PriorityQueue<Pair<Integer, V<T>>> {

    private static final long serialVersionUID = -1502837497833590580L;

    CostPQ() {
      super(11, new Comparator<Pair<Integer, V<T>>>() {

        @Override
        public int compare(Pair<Integer, V<T>> p1, Pair<Integer, V<T>> p2) {

          int diff = p1.getLeft() - p2.getLeft();
          if (diff != 0)
            return diff;

          // if they are the same, pick arbitrarily (but consistently)
          return p1.hashCode() - p2.hashCode();
        }
      });
    }
  }

  public static class PartialSoln<T> extends Pair<E<T, Integer>, Integer> {
    public PartialSoln(E<T, Integer> left, Integer right) {
      super(left, right);
    }

    int cost() {
      return getRight();
    }

    E<T, Integer> edge() {
      return getLeft();
    }
  }

  public static class Solver<T> {
    final Graph<T, Integer> g;
    final Map<V<T>, PartialSoln<T>> minCost = new HashMap<V<T>, PartialSoln<T>>();
    final CostPQ<T> pq = new CostPQ<T>();

    private Solver(Graph<T, Integer> g) {
      this.g = g;
    }

    private PartialSoln<T> solution = null;

    private PartialSoln<T> shortestPath(PartialSoln<T> sol, V<T> source,
        V<T> dest) {

      if (source == dest) {
        return sol;
      }

      for (E<T, Integer> e : g.getEdges(source)) {
        int newcost = sol.cost() + e.value();
        V<T> n = e.getDest();
        PartialSoln<T> old = minCost.get(n);
        if (old == null || old.cost() > newcost) {
          pq.add(new Pair<Integer, V<T>>(newcost, n));
          minCost.put(n, new PartialSoln<T>(e, newcost));
        }
      }

      Pair<Integer, V<T>> nd = pq.poll();
      PartialSoln<T> s = minCost.get(nd.getRight());
      return shortestPath(s, nd.getRight(), dest);

    }

    private void solve(V<T> src, V<T> dst) {
      solution = shortestPath(new PartialSoln<T>(null, 0), src, dst);
    }

    public List<E<T, Integer>> path() {

      LinkedList<E<T, Integer>> list = new LinkedList<E<T, Integer>>();

      PartialSoln<T> s = solution;

      while (s != null) {
        E<T, Integer> e = s.edge();
        list.addFirst(e);

        V<T> n = e.getSrc();
        if (n == null) {
          break;
        }
        s = minCost.get(n);

      }

      return list;
    }

    public List<V<T>> nodes() {
      LinkedList<V<T>> list = new LinkedList<V<T>>();

      PartialSoln<T> s = solution;
      boolean first = true;
      while (s != null) {
        E<T, Integer> e = s.edge();
        V<T> n = e.getSrc();
        if (n == null) {
          break;
        }

        if (first) {
          list.add(e.getDest());
          first = false;
        }

        list.addFirst(e.getSrc());

        s = minCost.get(n);
      }

      return list;
    }

    public int cost() {
      return solution.cost();
    }

  }

  boolean hasNegativeWeight() {
    return g.forallEdges(new Fun<Integer, Boolean>() {

      @Override
      public Boolean call(Integer t) {
        return t < 0;
      }
    });
  }

  public Solver<T> solve(V<T> src, V<T> dst) {
    Preconditions.checkState(!hasNegativeWeight());

    Solver<T> s = new Solver<T>(g);
    s.solve(src, dst);
    return s;
  }

}
