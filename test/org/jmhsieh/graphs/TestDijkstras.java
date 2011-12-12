package org.jmhsieh.graphs;

import java.util.List;

import org.jmhsieh.graphs.Dijkstras;
import org.jmhsieh.graphs.Graph;
import org.jmhsieh.graphs.Dijkstras.Solver;
import org.jmhsieh.graphs.Graph.V;

import junit.framework.TestCase;


public class TestDijkstras extends TestCase {

  public void testSolver() {
    Graph<String, Integer> g = new Graph<String, Integer>();
    V<String> n1 = g.newNode("A");
    V<String> n2 = g.newNode("B");
    V<String> n3 = g.newNode("C");
    V<String> n4 = g.newNode("D");
    V<String> n5 = g.newNode("E");
    V<String> n6 = g.newNode("F");
    V<String> n7 = g.newNode("G");

    // the solution
    g.newEdge(10, n1, n2); // a->b
    g.newEdge(10, n2, n6); // b->f
    g.newEdge(10, n6, n7); // f->g

    // non solutions.
    g.newEdge(15, n1, n3);
    g.newEdge(16, n1, n4);
    g.newEdge(8, n4, n5);
    g.newEdge(80, n5, n6);
    g.newEdge(90, n3, n7);
    g.newEdge(100, n4, n7);

    Dijkstras<String> d = new Dijkstras<String>(g);
    Solver<String> s = d.solve(n1, n7);
    assertEquals(s.cost(), 30);

    List<V<String>> nodes = s.nodes();
    String[] ans = { "A", "B", "F", "G" };
    int i = 0;
    for (V<String> n : nodes) {
      assertEquals(ans[i], n.value());
      i++;
    }
  }
}
