package com.lithouse.writer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WriterExecutor {
	private static final ExecutorService threadPool = Executors.newFixedThreadPool( 10 );
	
	public static void shutdown ( ) {
		threadPool.shutdown ( );
	}
	
	public static void submit ( Runnable task ) {
		threadPool.submit ( task );	
	}
}
