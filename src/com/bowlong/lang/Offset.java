package com.bowlong.lang;

public final class Offset {
	public int reader;
	public int writer;

	public final String toString() {
		return PStr.begin("[reader:").a(reader).a(",writer:").a(writer).end("]");
	}
}
