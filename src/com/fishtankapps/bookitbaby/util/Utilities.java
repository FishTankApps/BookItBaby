package com.fishtankapps.bookitbaby.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Utilities {

	private Utilities() {}
	
	
	public static String millisToString(long millis) {
		long seconds = (long) Math.ceil(millis/1000.0);
		long minutes = seconds / 60;
		long secondsLeft = seconds % 60;
		
		return minutes + ":" + ((secondsLeft < 10) ? "0" : "") + secondsLeft;
	}
	
	public static <T> T[] shuffleArray(T[] array) {
		List<T> list = Arrays.asList(array);
		Collections.shuffle(list);
		
		return list.toArray(array);
	}
}
