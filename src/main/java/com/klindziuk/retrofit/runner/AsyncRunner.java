package com.klindziuk.retrofit.runner;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.service.async.ASyncServiceManager;
import com.klindziuk.retrofit.service.async.AsyncHelper;

public class AsyncRunner {
	private static final Logger logger = Logger.getLogger(ASyncServiceManager.class);
		public static void main(String[] args) throws IOException, InterruptedException {
		ASyncServiceManager manager = new ASyncServiceManager();
		AsyncHelper.waitForResponse(manager);
		logger.info(manager.getResult());
	}
}
