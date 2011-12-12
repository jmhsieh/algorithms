package org.jmhsieh.strings.rabinkarp;

import java.util.Arrays;

public class NGram {
	byte[] data;

	public NGram(byte[] data) {
		this(data.length, data);
	}
	
	public NGram(int size, byte[] data) {
		this.data = new byte[size];
		for (int i = 0; i < this.data.length; i++) {
			this.data[i] = data[i];
		}
	}

	@Override
  public int hashCode() {
		return Arrays.hashCode(data);
	}

	@Override
  public boolean equals(Object o) {
		if (o instanceof NGram) {
			NGram ng = (NGram) o;
			return Arrays.equals(data, ng.data);
		}
		return false;
	}
	
	public byte[] getData() { return data ; }

}
