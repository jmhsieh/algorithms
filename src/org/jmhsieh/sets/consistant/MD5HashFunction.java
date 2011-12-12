package org.jmhsieh.sets.consistant;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


public class MD5HashFunction implements HashFunction {
  MessageDigest digest;

  MD5HashFunction() {
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  // MD5HashFunction(String algo) throws NoSuchAlgorithmException {
  // digest = MessageDigest.getInstance(algo);
  //  }

  public int hash(Object s) {
    digest.reset();
    byte[] hash = digest.digest(s.toString().getBytes());

    // HACK just take the first 4 digits and make it an integer.
    // apparently this is what other algorithms use to turn it into an int
    // value.

    // http://github.com/dustin/java-memcached-client/blob/9b2b4be73ee4a74bf6d0cf47f89c33753a5b5329/src/main/java/net/spy/memcached/HashAlgorithm.java
    int h0 = hash[0];
    int h1 = hash[1] << 8;
    int h2 = hash[2] << 16;
    int h3 = hash[3] << 24;

    int val = h0 + h1 + h2 + h3;
    return val;
  }

}
