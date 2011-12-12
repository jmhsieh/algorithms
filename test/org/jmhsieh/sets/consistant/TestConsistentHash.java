package org.jmhsieh.sets.consistant;

import java.util.Arrays;
import java.util.List;

import org.jmhsieh.sets.consistant.ConsistentHash;
import org.jmhsieh.sets.consistant.ConsistentLists;
import org.jmhsieh.sets.consistant.MoveHandler;

import junit.framework.TestCase;

public class TestConsistentHash extends TestCase {

  // String[] items = { "machine A", "machine B", "machine C", "machine D",
  // "machine E" };

  List<String> machines = Arrays.asList("machine A", "machine B", "machine C",
      "machine D", "machine E");

  public void testConsistentHash() {
    int replicas = 50;
    ConsistentHash<String> hash = new ConsistentHash<String>(replicas, machines);

    System.out.println("Before: ");
    for (int i = 0; i < 20; i++) {
      System.out.print(hash.get(i) + ", ");
    }
    System.out.println();

    System.out.println("after adding a machine: ");
    hash.add("machine F");
    for (int i = 0; i < 20; i++) {
      System.out.print(hash.get(i) + ", ");
    }
    System.out.println();

  }

  public void testConsistentLists() {
    int replicas = 50;
    ConsistentLists<String, String> lists = new ConsistentLists<String, String>(
        replicas);

    lists.addMoveListener(new MoveHandler<String, String>() {

      @Override
      public void moved(String from, String to, List<String> values) {
        System.out.printf("from %s to %s : values %s \n", from, to, values);

      }

      @Override
      public void rebuild(String key, List<String> allVals) {
        System.out.println("Rebuild: " + key);
      }
    });

    for (String m : machines) {
      lists.addKey(m);
    }

    System.out.println("Before: ");
    for (int i = 0; i < 20; i++) {
      lists.addValue("i" + i);
    }

    int sum = 0;
    int buckets = 0;
    for (List<String> vs : lists.getBuckets().values()) {
      sum += vs.size();
      buckets++;
    }
    assertEquals(buckets, 5);
    assertEquals(sum, 20);

    System.out.println("adding a machine F: ");
    lists.addKey("machine F");
    System.out.println("Lists values:\n" + lists);

    sum = 0;
    buckets = 0;
    for (List<String> vs : lists.getBuckets().values()) {
      sum += vs.size();
      buckets++;
    }
    assertEquals(buckets, 6);
    assertEquals(sum, 20);

    System.out.println("removing machine B: ");
    lists.removeKey("machine B");
    System.out.println("Lists values:\n" + lists);

    sum = 0;
    buckets = 0;
    for (List<String> vs : lists.getBuckets().values()) {
      sum += vs.size();
      buckets++;
    }
    assertEquals(buckets, 5);
    assertEquals(sum, 20);

    System.out.println("removing machine D: ");
    lists.removeKey("machine D");
    System.out.println("Lists values:\n" + lists);
    sum = 0;
    buckets = 0;
    for (List<String> vs : lists.getBuckets().values()) {
      sum += vs.size();
      buckets++;
    }
    assertEquals(sum, 20);
    assertEquals(buckets, 4);

  }
}
