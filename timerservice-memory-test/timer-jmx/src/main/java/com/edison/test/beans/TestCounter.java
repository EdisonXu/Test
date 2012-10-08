package com.edison.test.beans;

import java.util.concurrent.atomic.AtomicInteger;

public class TestCounter {

	private static AtomicInteger counter = new AtomicInteger(0);

	public static AtomicInteger getCounter() {
		return counter;
	}

	public static void setCounter(AtomicInteger counter) {
		TestCounter.counter = counter;
	}
}
