package org.jmhsieh.graphs;

import org.jmhsieh.graphs.FordFulkerson;
import org.jmhsieh.graphs.Graph;
import org.jmhsieh.graphs.FordFulkerson.Solver;
import org.jmhsieh.graphs.Graph.V;

import junit.framework.TestCase;


public class TestFordFulkerson extends TestCase {

  public void testFordFulkerson() {
    Graph<String, Integer> g = new Graph<String, Integer>();

    V<String> n1 = g.newNode("s");
    V<String> n2 = g.newNode("o");
    V<String> n3 = g.newNode("p");
    V<String> n4 = g.newNode("q");
    V<String> n5 = g.newNode("r");
    V<String> n6 = g.newNode("t");

    g.newEdge(3, n1, n2);
    g.newEdge(3, n1, n3);
    g.newEdge(2, n2, n3);
    g.newEdge(3, n2, n4);
    g.newEdge(2, n3, n5);
    g.newEdge(3, n5, n6);
    g.newEdge(4, n4, n5);
    g.newEdge(2, n4, n6);

    FordFulkerson<String> ff = new FordFulkerson<String>(g);

    Solver<String> sol = ff.solve(n1, n6);
    assertEquals(5, sol.capacity());
  }
}
