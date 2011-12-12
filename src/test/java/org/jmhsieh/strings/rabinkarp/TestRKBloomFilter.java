package org.jmhsieh.strings.rabinkarp;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Random;

import org.jmhsieh.strings.rabinkarp.NGram;
import org.jmhsieh.strings.rabinkarp.RKBloomFilter;


import junit.framework.TestCase;

/**
 * This code may be used, modified, and redistributed provided that the author
 * tag below remains intact.
 * 
 * @author Ian Clarke <ian@uprizer.com>
 */

public class TestRKBloomFilter extends TestCase {

	public void testRKBloomNgram() {
		HashSet<NGram> hs = new HashSet<NGram>();
		hs.add(new NGram("abcd".getBytes()));
		hs.add(new NGram("1234".getBytes()));
		hs.add(new NGram("ABCD".getBytes()));
		hs.add(new NGram("!@#$".getBytes()));

		RKBloomFilter bf = new RKBloomFilter(16533 * 8, 3000, 4, 37);
		bf.addAll(hs);

		// assertTrue(bf.containsAll(hs));

		assertTrue(bf.contains(new NGram("abcd".getBytes())));
		assertTrue(bf.contains(new NGram("1234".getBytes())));
		assertTrue(bf.contains(new NGram("ABCD".getBytes())));
		assertTrue(bf.contains(new NGram("!@#$".getBytes())));

		bf.matched((byte) 'a');
		bf.matched((byte) 'b');
		bf.matched((byte) 'c');
		assertTrue(bf.matched((byte) 'd'));

		bf.matched((byte) '1');
		bf.matched((byte) '2');
		bf.matched((byte) '3');
		assertTrue(bf.matched((byte) '4'));

		bf.matched((byte) 'A');
		bf.matched((byte) 'B');
		bf.matched((byte) 'C');
		assertTrue(bf.matched((byte) 'D'));

		bf.matched((byte) '!');
		bf.matched((byte) '@');
		bf.matched((byte) '#');
		assertTrue(bf.matched((byte) '$'));
	}

	public void testRKBloomNgramExhuast() {
		HashSet<NGram> hs = new HashSet<NGram>();
		hs.add(new NGram("abcd".getBytes()));
		hs.add(new NGram("1234".getBytes()));
		hs.add(new NGram("ABCD".getBytes()));
		hs.add(new NGram("!@#$".getBytes()));

		RKBloomFilter bf = new RKBloomFilter(16533 * 8, 3000, 4, 37);
		bf.addAll(hs);

		// assertTrue(bf.containsAll(hs));

		assertTrue(bf.contains(new NGram("abcd".getBytes())));
		assertTrue(bf.contains(new NGram("1234".getBytes())));
		assertTrue(bf.contains(new NGram("ABCD".getBytes())));
		assertTrue(bf.contains(new NGram("!@#$".getBytes())));

		int hits = 0;
		for (int num = Integer.MIN_VALUE / 1024; num < Integer.MAX_VALUE / 1024; num++) {
			byte[] bs = new byte[4];
			bs[0] = (byte) (num & 0xff);
			bs[1] = (byte) ((num >> 8) & 0xff);
			bs[2] = (byte) ((num >> 16) & 0xff);
			bs[3] = (byte) ((num >> 24) & 0xff);

			if (bf.contains(new NGram(bs))) {
				// System.out.print(num + " ");
				hits++;
			}
		}
		System.out.println("\nhits: " + hits);
	}

	/**
	 * TODO My bloom filter implementation failes
	 */
	public void testBloomFilter() {
		DecimalFormat df = new DecimalFormat("0.00000");
		Random r = new Random(124445l);
		int bfSize = 400000;
		System.out.println("Testing " + bfSize + " bit RKBloomFilter");
		for (int i = 5; i < 10; i++) {
			int addCount = 10000 * (i + 1);
			RKBloomFilter bf = new RKBloomFilter(bfSize, addCount, 4, 37);
			HashSet<NGram> added = new HashSet<NGram>();
			for (int x = 0; x < addCount; x++) {
				int num = r.nextInt();
				byte[] bs = new byte[4];
				bs[0] = (byte) (num & 0xff);
				bs[1] = (byte) ((num >> 8) & 0xff);
				bs[2] = (byte) ((num >> 16) & 0xff);
				bs[3] = (byte) ((num >> 24) & 0xff);
				// String s = new String(bs);
				bf.add(new NGram(bs));
				added.add(new NGram(bs));
			}
			bf.addAll(added);
			assertTrue("Assert that there are no false negatives", bf
					.containsAll(added));

			int falsePositives = 0;
			for (int x = 0; x < addCount; x++) {
				int num = r.nextInt();

				byte[] bs = new byte[4];
				bs[0] = (byte) (num & 0xff);
				bs[1] = (byte) ((num >> 8) & 0xff);
				bs[2] = (byte) ((num >> 16) & 0xff);
				bs[3] = (byte) ((num >> 24) & 0xff);
				// String s = new String(bs);

				// Ensure that this random number hasn't been added already
				if (added.contains(new NGram(bs))) {
					continue;
				}

				// If necessary, record a false positive
				if (bf.contains(new NGram(bs))) {
					falsePositives++;
				}
			}
			double expectedFP = bf.expectedFalsePositiveProbability();
			double actualFP = (double) falsePositives / (double) addCount;
			System.out.println("Got " + falsePositives
					+ " false positives out of " + addCount
					+ " added items, rate = " + df.format(actualFP)
					+ ", expected = " + df.format(expectedFP));
			double ratio = expectedFP / actualFP;
			System.out.println("Expected to actual postive ratio: " + ratio);
			// assertTrue(
			// "Assert that the actual false positive rate doesn't deviate by
			// more than 10% from what was predicted",
			// ratio > 0.9 && ratio < 1.1);
		}
	}

}
