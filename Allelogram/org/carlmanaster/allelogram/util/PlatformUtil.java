package org.carlmanaster.allelogram.util;

public class PlatformUtil {
	public static boolean isMac() {
		return System.getProperty("os.name").startsWith("Mac");
	}
}
