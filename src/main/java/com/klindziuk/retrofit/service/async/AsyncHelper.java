package com.klindziuk.retrofit.service.async;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public final class AsyncHelper {
	private static final Logger logger = Logger.getLogger(AsyncHelper.class);
	private static final String ERROR_MESSAGE = "Thread was interrupted";
	private static final int FREEZETIME = 6;
	
	private AsyncHelper() {}
	
	public static void waitForResponse(ASyncServiceManager manager) {
		Thread t = new Thread(manager);
		t.start();
		try {
		    TimeUnit.SECONDS.sleep(FREEZETIME);
		} catch (InterruptedException inex) {
		logger.error(ERROR_MESSAGE + Thread.currentThread().getName(), inex);	
		  
		}
	}
}
