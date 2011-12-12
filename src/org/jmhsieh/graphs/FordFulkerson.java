package org.jmhsieh.graphs;

import java.util.ArrayList;
import java.util.List;

import org.jmhsieh.graphs.Graph.E;
import org.jmhsieh.graphs.Graph.V;


/**
 * Ford Fulkerson Algorithm: This is a solver for the classic min-cut max-flow
 * problem
 */
public class FordFulkerson<T> {

  final Graph<T, Integer> g;

  public FordFulkerson(Graph<T, Integer> gr) {
    this.g = gr;
  }

  public static class Solver<T> {
    final Graph<T, Integer> g;
    final V<T> source;
    final V<T> sink;

    // solutions
    Graph<T, Integer> adj;
    Graph<T, Integer> flow;
    int capacity;

    Solver(Graph<T, Integer> gr, V<T> src, V<T> sink) {
      this.g = gr;

      this.source = src;
      this.sink = sink;
    }

    void init() {
      adj = new Graph<T, Integer>();
      for (E<T, Integer> e : g.node2edges.values()) {
        adj.newEdge(e.value(), e.getSrc(), e.getDest());
        adj.newEdge(0, e.getDest(), e.getSrc());
      }

      flow = new Graph<T, Integer>();
      for (E<T, Integer> e : g.node2edges.values()) {
        flow.newEdge(0, e.getSrc(), e.getDest());
        flow.newEdge(0, e.getDest(), e.getSrc());
      }
    }

    List<E<T, Integer>> findPath(V<T> src, V<T> sink, List<E<T, Integer>> path) {
      if (src == sink) {
        return path;
      }
      List<E<T, Integer>> result = null;
      for (E<T, Integer> e : adj.node2edges.get(src)) {
        V<T> vertex = e.getDest();
        int capacity = e.value();
        int residual = capacity - flow.getEdge(src, vertex).value();
        e.setValue(residual);
        if (residual > 0 && !path.contains(e)) {
          List<E<T, Integer>> p2 = new ArrayList<E<T, Integer>>(path);
          p2.add(e);
          // path.add(e);
          result = findPath(vertex, sink, p2);
          if (result != null)
            return result;
        }
      }
      return null;
    }

    Solver<T> solve(V<T> source, V<T> sink) {
      List<E<T, Integer>> path = findPath(source, sink,
          new ArrayList<E<T, Integer>>());
      System.out.println("Path: " + path.toString());

      while (path != null) {
        E<T, Integer> minflow = null;
        for (E<T, Integer> e : path) {
          if (minflow == null) {
            minflow = e;
            continue;
          }
          minflow = (minflow.value() > e.value()) ? e : minflow;
        }

        for (E<T, Integer> e : path) {
          E<T, Integer> ef = flow.getEdge(e.getSrc(), e.getDest());
          ef.setValue(ef.value() + minflow.value());

          E<T, Integer> residual = flow.getEdge(e.getDest(), e.getSrc());
          residual.setValue(ef.value() - minflow.value());
        }

        path = findPath(source, sink, new ArrayList<E<T, Integer>>());
        if (path != null) {
          System.out.println("Path: " + path.toString());
        }
      }
      return this;
    }

    int capacity() {
      int sum = 0;
      for (E<T, Integer> e : flow.getEdges(source)) {
        sum += e.value();
      }
      return sum;
    }
  }

  Solver<T> solve(V<T> source, V<T> sink) {
    Solver<T> s = new Solver<T>(g, source, sink);
    s.init();
    return s.solve(source, sink);
  }
}
