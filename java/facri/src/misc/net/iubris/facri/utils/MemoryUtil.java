package net.iubris.facri.utils;

public class MemoryUtil {
	
	private static final long MEGABYTE = 1024L * 1024L;

	public static long bytesToMegabytes(long bytes) {
		return bytes / MEGABYTE;
	}
	
	public static long runGarbageCollector() {
		// Get the Java runtime
		Runtime runtime = Runtime.getRuntime();
		// Run the garbage collector
		runtime.gc();
		// Calculate the used memory
		long memory = runtime.totalMemory() - runtime.freeMemory();
//System.out.println("Used memory is bytes: " + memory);
		long bytesToMegabytes = bytesToMegabytes(memory);
//System.out.println("Used memory is megabytes: " + bytesToMegabytes);
		return bytesToMegabytes;
	}

}
