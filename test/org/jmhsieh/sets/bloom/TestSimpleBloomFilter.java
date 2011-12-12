package org.jmhsieh.sets.bloom;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Random;

import org.jmhsieh.sets.bloom.SimpleBloomFilter;

import junit.framework.TestCase;


/**
 * This code may be used, modified, and redistributed provided that the author
 * tag below remains intact.
 * 
 * @author Ian Clarke <ian@uprizer.com>
 */

public class TestSimpleBloomFilter extends TestCase {
  public void testBloomFilter() {
    DecimalFormat df = new DecimalFormat("0.00000");
    Random r = new Random(124445l);
    // int bfSize = 400000;
    int bfSize = 400000;
    System.out.println("Testing " + bfSize + " bit SimpleBloomFilter");
    for (int i = 5; i < 10; i++) {
      int addCount = 10000 * (i + 1);
      SimpleBloomFilter<Integer> bf = new SimpleBloomFilter<Integer>(bfSize,
          addCount);
      HashSet<Integer> added = new HashSet<Integer>();
      for (int x = 0; x < addCount; x++) {
        int num = r.nextInt();
        added.add(num);
      }
      bf.addAll(added);
      assertTrue("Assert that there are no false negatives", bf
          .containsAll(added));

      int falsePositives = 0;
      for (int x = 0; x < addCount; x++) {
        int num = r.nextInt();

        // Ensure that this random number hasn't been added already
        if (added.contains(num)) {
          continue;
        }

        // If necessary, record a false positive
        if (bf.contains(num)) {
          falsePositives++;
        }
      }
      double expectedFP = bf.expectedFalsePositiveProbability();
      double actualFP = (double) falsePositives / (double) addCount;
      System.out.println("Got " + falsePositives + " false positives out of "
          + addCount + " added items, rate = " + df.format(actualFP)
          + ", expected = " + df.format(expectedFP));
      double ratio = expectedFP / actualFP;
      assertTrue(
          "Assert that the actual false positive rate doesn't deviate by more than 10% from what was predicted",
          ratio > 0.9 && ratio < 1.1);
    }
  }

  public void testBloomFilter4M() {
    DecimalFormat df = new DecimalFormat("0.00000");
    Random r = new Random(124445l);
    int bfSize = 4000000;
    System.out.println("Testing " + bfSize + " bit SimpleBloomFilter");
    for (int i = 5; i < 10; i++) {
      int addCount = 10000 * (i + 1);
      SimpleBloomFilter<Integer> bf = new SimpleBloomFilter<Integer>(bfSize,
          addCount);
      HashSet<Integer> added = new HashSet<Integer>();
      for (int x = 0; x < addCount; x++) {
        int num = r.nextInt();
        added.add(num);
      }
      bf.addAll(added);
      assertTrue("Assert that there are no false negatives", bf
          .containsAll(added));

      int falsePositives = 0;
      for (int x = 0; x < addCount; x++) {
        int num = r.nextInt();

        // Ensure that this random number hasn't been added already
        if (added.contains(num)) {
          continue;
        }

        // If necessary, record a false positive
        if (bf.contains(num)) {
          falsePositives++;
        }
      }
      double expectedFP = bf.expectedFalsePositiveProbability();
      double actualFP = (double) falsePositives / (double) addCount;
      System.out.println("Got " + falsePositives + " false positives out of "
          + addCount + " added items, rate = " + df.format(actualFP)
          + ", expected = " + df.format(expectedFP));
      if (falsePositives == 0)
        continue;
      double ratio = expectedFP / actualFP;
      assertTrue(
          "Assert that the actual false positive rate doesn't deviate by more than 10% from what was predicted",
          ratio > 0.9 && ratio < 1.1);
    }
  }

  // These take too much memory

  //
  // public void testBloomFilter40M() {
  // DecimalFormat df = new DecimalFormat("0.00000");
  // Random r = new Random(124445l);
  // int bfSize = 40000000;
  // System.out.println("Testing " + bfSize + " bit SimpleBloomFilter");
  // for (int i = 5; i < 10; i++) {
  // int addCount = 1000000 * (i + 1);
  // SimpleBloomFilter<Integer> bf = new SimpleBloomFilter<Integer>(
  // bfSize, addCount);
  // HashSet<Integer> added = new HashSet<Integer>();
  // for (int x = 0; x < addCount; x++) {
  // int num = r.nextInt();
  // added.add(num);
  // }
  // bf.addAll(added);
  // assertTrue("Assert that there are no false negatives", bf
  // .containsAll(added));
  //
  // int falsePositives = 0;
  // for (int x = 0; x < addCount; x++) {
  // int num = r.nextInt();
  //
  // // Ensure that this random number hasn't been added already
  // if (added.contains(num)) {
  // continue;
  // }
  //
  // // If necessary, record a false positive
  // if (bf.contains(num)) {
  // falsePositives++;
  // }
  // }
  // double expectedFP = bf.expectedFalsePositiveProbability();
  // double actualFP = (double) falsePositives / (double) addCount;
  // System.out.println("Got " + falsePositives
  // + " false positives out of " + addCount
  // + " added items, rate = " + df.format(actualFP)
  // + ", expected = " + df.format(expectedFP));
  // if (falsePositives == 0)
  // continue;
  // double ratio = expectedFP / actualFP;
  // assertTrue(
  // "Assert that the actual false positive rate doesn't deviate by more than 10% from what was predicted",
  // ratio > 0.9 && ratio < 1.1);
  // }
  // }
  //
  // public void testBloomFilter400M() {
  // DecimalFormat df = new DecimalFormat("0.00000");
  // Random r = new Random(124445l);
  // int bfSize = 400000000;
  // System.out.println("Testing " + bfSize + " bit SimpleBloomFilter");
  // for (int i = 5; i < 10; i++) {
  // int addCount = 1000000 * (i + 1);
  // SimpleBloomFilter<Integer> bf = new SimpleBloomFilter<Integer>(
  // bfSize, addCount);
  // HashSet<Integer> added = new HashSet<Integer>();
  // for (int x = 0; x < addCount; x++) {
  // int num = r.nextInt();
  // added.add(num);
  // }
  // bf.addAll(added);
  // assertTrue("Assert that there are no false negatives", bf
  // .containsAll(added));
  //
  // int falsePositives = 0;
  // for (int x = 0; x < addCount; x++) {
  // int num = r.nextInt();
  //
  // // Ensure that this random number hasn't been added already
  // if (added.contains(num)) {
  // continue;
  // }
  //
  // // If necessary, record a false positive
  // if (bf.contains(num)) {
  // falsePositives++;
  // }
  // }
  // double expectedFP = bf.expectedFalsePositiveProbability();
  // double actualFP = (double) falsePositives / (double) addCount;
  // System.out.println("Got " + falsePositives
  // + " false positives out of " + addCount
  // + " added items, rate = " + df.format(actualFP)
  // + ", expected = " + df.format(expectedFP));
  // if (falsePositives <= 1 )
  // continue;
  // double ratio = expectedFP / actualFP;
  // assertTrue(
  // "Assert that the actual false positive rate doesn't deviate by more than 10% from what was predicted",
  // ratio > 0.9 && ratio < 1.1);
  // }
  // }

}
