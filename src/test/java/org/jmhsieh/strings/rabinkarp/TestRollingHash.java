package org.jmhsieh.strings.rabinkarp;

import org.jmhsieh.strings.rabinkarp.PolynomialRollingHash;

import junit.framework.TestCase;

public class TestRollingHash extends TestCase {
	public void testRHIdempotent() {
		int ngram=3;
		int cap = 201;
		PolynomialRollingHash rh = new PolynomialRollingHash(ngram,cap);
		
		int h1 = rh.hashN("abc".getBytes(),3);
		System.out.println("h1 = "+ h1); 
		int h2 = rh.hashN("abc".getBytes(),3);
		System.out.println("h2 = " + h2);
		assertEquals(h1,h2);		
	}

	public void testRHIdempotent2() {
		int ngram=3; 
		int cap = 201;
		PolynomialRollingHash rh = new PolynomialRollingHash(ngram,cap);
				
		int h1 = rh.hashN("abc".getBytes(),3);
		System.out.println("h1 = "+ h1); 
			
		rh.hash((byte)'a');
		rh.hash((byte)'b');
		int h2 = rh.hash((byte)'c');
		System.out.println("h2 = " + h2);
		assertEquals(h1,h2);		
	}

}
