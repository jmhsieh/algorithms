package org.jmhsieh.strings.rabinkarp;

/**
 * Implementation of a rolling hash function. We have a prime A, and a byte
 * array b. We take n characters such that:
 * 
 * val = A^(n-1)*b[0] + A^(n-2)*b[1] + * A^(n-3)*b[2] + ... + A^1 b[1] + A^0
 * b[n]
 * 
 * We roll the hash by taking: subtracting the most significant, multiplying by
 * A and then adding the new lsb.
 * 
 * val' = A (val - A^(n-1)*b[0]) + b[n+1]
 * 
 * We then make sure this operation happens modulo some other the capacity of
 * the table.
 * 
 * @author jmhsieh
 */
public class PolynomialRollingHash {
	int FACTOR = 65521; // a popular large prime number

	int n; // nGram Size
	int cap; // hashtable size
	int nthmod; // FACTOR ^ (n-1) % size

	byte[] ngram;
	int idx = 0;
	int val;

	public PolynomialRollingHash(int n, int cap, int factor) {
		this.FACTOR = factor;
		this.n = n;
		this.cap = cap;
		ngram = new byte[n];
		nthmod = 0;
		for (int i = 0; i < ngram.length; i++) {
			ngram[i] = 0;
			if (nthmod == 0)
				nthmod = 1; // TODO this may be a problem
			else
				nthmod *= FACTOR;
			nthmod %= cap;
		}
	}

	public PolynomialRollingHash(int n, int cap) {
		this(n, cap, 31);
	}

	public int hash(byte b) {
		byte old = ngram[idx];
		val -= old * nthmod;
		val *= FACTOR;
		val += b;
		val %= cap;
		// force to be positive
		// TODO do test to make sure this is ok, or if we limit table size.
		val = (val < 0) ? val + cap : val;

		// track the current n-gram so that we can remove it later.
		ngram[idx] = b;
		idx++;
		idx %= ngram.length;

		return val;
	}

	public int hashN(byte[] b, int n) {
		if (b.length < n)
			throw new RuntimeException("invalid size");

		for (int i = 0; i < n; i++) {
			hash(b[i]);
		}
		return val;
	}

	public byte[] getNGram() {
		byte[] b = new byte[n];
		for (int i = 0; i < n; i++) {
			b[i] = ngram[(idx + i) % n];
		}
		return b;
	}
}
