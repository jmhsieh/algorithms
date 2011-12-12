package org.jmhsieh.sets.bloom;

import java.io.Serializable;
import java.util.BitSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * A simple Bloom Filter (see http://en.wikipedia.org/wiki/Bloom_filter) that
 * uses java.util.Random as a primitive hash function, and which implements
 * Java's Set interface for convenience.
 * 
 * Only the add(), addAll(), contains(), and containsAll() methods are
 * implemented. Calling any other method will yield an 
 * UnsupportedOperationException.
 * 
 * This code may be used, modified, and redistributed provided that the 
 * author tag below remains intact.
 * 
 * @author Ian Clarke <ian@uprizer.com>
 * 
 * @param <E>
 *            The type of object the BloomFilter should contain
 */
public class SimpleBloomFilter<E> implements Set<E>, Serializable {
	private static final long serialVersionUID = 3527833617516722215L;
	protected int k;
	BitSet bitSet;
	int bitArraySize, expectedElements;

	/**
	 * Construct a SimpleBloomFilter. You must specify the number of bits in the
	 * Bloom Filter, and also you should specify the number of items you
	 * expect to add. The latter is used to choose some optimal internal values to
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
	public SimpleBloomFilter(int bitArraySize, int expectedElements) {
		this.bitArraySize = bitArraySize;
		this.expectedElements = expectedElements;
		this.k = (int) Math.ceil((bitArraySize / expectedElements)
				* Math.log(2.0));
		bitSet = new BitSet(bitArraySize);
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
	public boolean add(E o) {
		Random r = new Random(o.hashCode());
		for (int x = 0; x < k; x++) {
			bitSet.set(r.nextInt(bitArraySize), true);
		}
		return false;
	}

	/**
	 * @return This method will always return false
	 */
	public boolean addAll(Collection<? extends E> c) {
		for (E o : c) {
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
	 * @return False indicates that o was definitely not added to this Bloom Filter, 
	 *         true indicates that it probably was.  The probability can be estimated
	 *         using the expectedFalsePositiveProbability() method.
	 */
	public boolean contains(Object o) {
		Random r = new Random(o.hashCode());
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
	public Iterator<E> iterator() {
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
}
