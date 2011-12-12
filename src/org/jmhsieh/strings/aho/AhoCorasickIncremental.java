package org.jmhsieh.strings.aho;

import java.util.Iterator;

import org.jmhsieh.structs.Queue;


/**
 * <p>
 * An implementation of the Aho-Corasick string searching automaton. This
 * implementation of the <a
 * href="http://portal.acm.org/citation.cfm?id=360855&dl=ACM&coll=GUIDE"
 * target="_blank">Aho-Corasick</a> algorithm is optimized to work with bytes.
 * </p>
 * 
 * <p>
 * Example usage: <code><pre>
 * AhoCorasick tree = new AhoCorasick();
 * tree.add(&quot;hello&quot;.getBytes(), &quot;hello&quot;);
 * tree.add(&quot;world&quot;.getBytes(), &quot;world&quot;);
 * tree.prepare();
 * 
 * Iterator searcher = tree.search(&quot;hello world&quot;.getBytes());
 * while (searcher.hasNext()) {
 *   SearchResult result = searcher.next();
 *   System.out.println(result.getOutputs());
 *   System.out.println(&quot;Found at index: &quot; + result.getLastIndex());
 * }
 * </pre></code>
 * </p>
 */
public class AhoCorasickIncremental<T> implements AhoCorasickInterface<T> {
  State<T> root;
  boolean prepared;

  public AhoCorasickIncremental() {
    this.root = new State<T>(0);
    this.prepared = false;
  }

  /**
   * Adds a new keyword with the given output. During search, if the keyword is
   * matched, output will be one of the yielded elements in
   * SearchResults.getOutputs().
   */
  public void add(byte[] keyword, T output) {
    if (this.prepared)
      throw new IllegalStateException(
          "can't add keywords after prepare() is called");
    State<T> lastState = this.root.extendAll(keyword);
    lastState.addOutput(output);
  }

  /**
   * Prepares the automaton for searching. This must be called before any
   * searching().
   */
  public void prepare() {
    this.prepareFailTransitions();
    this.prepared = true;
  }

  /**
   * Starts a new search, and returns an Iterator of SearchResults.
   */
  public Iterator<SearchResult<T>> search(byte[] bytes) {
    return new Searcher<T>(this, this.startSearch(bytes));
  }

  /**
   * DANGER DANGER: dense algorithm code ahead. Very order dependent.
   * Initializes the fail transitions of all states except for the root.
   */
  private void prepareFailTransitions() {
    Queue<State<T>> q = new Queue<State<T>>();
    for (int i = 0; i < 256; i++)
      if (this.root.get((byte) i) != null) {
        this.root.get((byte) i).setFail(this.root);
        q.add(this.root.get((byte) i));
      }
    this.prepareRoot();
    while (!q.isEmpty()) {
      State<T> state = q.pop();
      byte[] keys = state.keys();
      for (int i = 0; i < keys.length; i++) {
        State<T> r = state;
        byte a = keys[i];
        State<T> s = r.get(a);
        if (s != root) {
          q.add(s);
        } else {
          // System.out.println("got root");
        }

        // figure out states to back track to
        r = r.getFail();
        while (r.get(a) == null)
          r = r.getFail();
        s.setFail(r.get(a));

        // 
        s.getOutputs().addAll(r.get(a).getOutputs());
      }
    }
  }

  /**
   * Sets all the out transitions of the root to itself, if no transition yet
   * exists at this point.
   */
  private void prepareRoot() {
    for (int i = 0; i < 256; i++)
      if (this.root.get((byte) i) == null)
        this.root.put((byte) i, this.root);
  }

  /**
   * Returns the root of the tree. Package protected, since the user probably
   * shouldn't touch this.
   */
  State<T> getRoot() {
    return this.root;
  }

  /**
   * Begins a new search using the raw interface. Package protected.
   */
  SearchResult<T> startSearch(byte[] bytes) {
    if (!this.prepared)
      throw new IllegalStateException("can't start search until prepare()");
    return continueSearch(new SearchResult<T>(this.root, bytes, 0));
  }

  /**
   * Continues the search, given the initial state described by the lastResult.
   * Package protected.
   */
  public SearchResult<T> continueSearch(SearchResult<T> lastResult) {
    byte[] bytes = lastResult.bytes;
    State<T> state = lastResult.lastMatchedState;
    for (int i = lastResult.lastIndex; i < bytes.length; i++) {
      byte b = bytes[i];
      while (state.get(b) == null)
        state = state.getFail();
      state = state.get(b);
      if (state.getOutputs().size() > 0)
        return new SearchResult<T>(state, bytes, i + 1);
    }
    return null;
  }

  /**
   * This is my hack.
   */
  public void unprepare() {
    if (!prepared) {
      // already unprepared, don't do anything (or maybe report error?)
      return;
    }
    prepared = false;

    // undo first level self links
    for (int i = 0; i < 256; i++) {
      if (this.root.get((byte) i) == root)
        this.root.put((byte) i, null);
    }

    // remove all fail links
    Queue<State<T>> q = new Queue<State<T>>();
    q.add(this.root);
    while (!q.isEmpty()) {
      State<T> state = q.pop();
      state.setFail(null);
      byte[] keys = state.keys();
      for (int i = 0; i < keys.length; i++) {
        byte a = keys[i];
        State<T> s = state.get(a);
        if (s != root) {
          q.add(s);
        }
      }
    }
  }

}
