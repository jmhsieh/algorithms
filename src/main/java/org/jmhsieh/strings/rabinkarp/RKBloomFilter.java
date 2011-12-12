package org.jmhsieh.strings.rabinkarp;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;


/**
 * BUGGY! This has been hacked up by jmhsieh and doesn't seem to work.
 * 
 * A simple Bloom Filter (see http://en.wikipedia.org/wiki/Bloom_filter) that
 * uses java.util.Random as a primitive hash function, and which implements
 * Java's Set interface for convenience.
 * 
 * Only the add(), addAll(), contains(), and containsAll() methods are
 * implemented. Calling any other method will yield an
 * UnsupportedOperationException.
 * 
 * This code may be used, modified, and redistributed provided that the author
 * tag below remains intact.
 * 
 * @author Ian Clarke <ian@uprizer.com>
 * 
 * @param <E>
 *            The type of object the BloomFilter should contain
 */
public class RKBloomFilter implements Set<NGram>, Serializable {
	private static final long serialVersionUID = 3527833617516722215L;
	protected int k;
	BitSet bitSet;
	int bitArraySize, expectedElements, ngramSize, factor;
	// 2147483647 is prime and < MAX_INT, we need this because a mod is done
	// in the rolling hash.
	int rhCap = 2147483647;
	// int rhCap = 214748329. // claimed elsewhere..
	// another one near the 16 bit limit: 62851
	// int rhCap = 16531;
	PolynomialRollingHash rh;

	/**
	 * Construct a SimpleBloomFilter. You must specify the number of bits in the
	 * Bloom Filter, and also you should specify the number of items you expect
	 * to add. The latter is used to choose some optimal internal values to
	 * minimize the false-positive rate (which can be estimated with
	 * expectedFalsePositiveRate()).
	 * 
	 * @param bitArraySize
	 *            The number of bits in the bit array (often called 'm' in the
	 *            context of bloom filters).
	 * @param expectedElements
	 *            The typical number of items you expect to be added to the
	 *            SimpleBloomFilter (often called 'n').
	 */
	public RKBloomFilter(int bitArraySize, int expectedElements, int ngramSize,
			int factor) {
		this.bitArraySize = bitArraySize;
		this.expectedElements = expectedElements;
		this.k = (int) Math.ceil((bitArraySize / expectedElements)
				* Math.log(2.0));
		bitSet = new BitSet(bitArraySize);
		this.rh = new PolynomialRollingHash(ngramSize, rhCap, factor);
		this.ngramSize = ngramSize;
		this.factor = factor;
	}

	/**
	 * Calculates the approximate probability of the contains() method returning
	 * true for an object that had not previously been inserted into the bloom
	 * filter. This is known as the "false positive probability".
	 * 
	 * @return The estimated false positive rate
	 */
	public double expectedFalsePositiveProbability() {
		return Math.pow((1 - Math.exp(-k * (double) expectedElements
				/ (double) bitArraySize)), k);
	}

	/*
	 * @return This method will always return false
	 * 
	 * @see java.util.Set#add(java.lang.Object)
	 */
	public boolean add(NGram s) {
		// TODO the extra mod operation limits the randomness!!
		int hash = rh.hashN(s.data, ngramSize);
		Random r = new Random(hash);
		// Random r = new Random(o.hashCode());
		for (int x = 0; x < k; x++) {
			int i = r.nextInt(bitArraySize);
			bitSet.set(i, true);
		}
		return false;
	}

	/**
	 * @return This method will always return false
	 */
	public boolean addAll(Collection<? extends NGram> c) {
		for (NGram o : c) {
			add(o);
		}
		return false;
	}

	/**
	 * Clear the Bloom Filter
	 */
	public void clear() {
		for (int x = 0; x < bitSet.length(); x++) {
			bitSet.set(x, false);
		}
	}

	/**
	 * @return False indicates that o was definitely not added to this Bloom
	 *         Filter, true indicates that it probably was. The probability can
	 *         be estimated using the expectedFalsePositiveProbability() method.
	 */
	public boolean contains(Object o) {

		// if (!(o instanceof byte[])) {
		// return false;
		// }
		// byte[] s = (byte[]) o;
		if (!(o instanceof NGram)) {
			return false;
		}
		NGram s = (NGram) o;
		PolynomialRollingHash rhg = new PolynomialRollingHash(ngramSize, rhCap, factor);
		int h = rhg.hashN(s.data, ngramSize);
		Random r = new Random(h);
		for (int x = 0; x < k; x++) {
			int i = r.nextInt(bitArraySize);
			if (!bitSet.get(i))
				return false;
		}
		return true;
	}

	/**
	 * Special helper function that takes the rolling has value instead an
	 * object to lookup and item.
	 * 
	 * @param rh
	 * @return
	 */
	private boolean rhContains(int rh) {
		Random r = new Random(rh);
		for (int x = 0; x < k; x++) {
			if (!bitSet.get(r.nextInt(bitArraySize)))
				return false;
		}
		return true;
	}

	public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o))
				return false;
		}
		return true;
	}

	/**
	 * Not implemented
	 */
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public Iterator<NGram> iterator() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public int size() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	/**
	 * Not implemented
	 */
	public <T> T[] toArray(T[] a) {
		throw new UnsupportedOperationException();
	}

	// null if no match, else bytes if is match.
	public boolean matched(byte b) {
		// lookups++;
		int h = rh.hash(b);
		return rhContains(h);
	}

}
