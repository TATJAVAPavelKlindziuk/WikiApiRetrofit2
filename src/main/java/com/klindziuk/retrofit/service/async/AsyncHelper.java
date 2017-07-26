package com.klindziuk.retrofit.service.async;

import java.util.concurrent.TimeUnit;

public final class AsyncHelper {
	private AsyncHelper() {}
	
	public static void waitForResponse(ASyncServiceManager manager) {
		Thread t = new Thread(manager);
		t.start();
		try {
		    TimeUnit.SECONDS.sleep(6);
		} catch (InterruptedException e) {
		    e.printStackTrace();
		}
	}
}
