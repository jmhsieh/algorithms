package org.jmhsieh.strings.aho;

abstract interface AhoCorasickInterface<T> {
  SearchResult<T> continueSearch(SearchResult<T> lastResult);
}
