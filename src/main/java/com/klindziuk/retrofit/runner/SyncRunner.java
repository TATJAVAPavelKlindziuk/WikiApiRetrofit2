package com.klindziuk.retrofit.runner;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.service.SyncServiceManager;

public class SyncRunner {
	private static final Logger logger = Logger.getLogger(SyncServiceManager.class);
	
	public static void main(String[] args) throws IOException  {
		SyncServiceManager manager = SyncServiceManager.getInstance();
		manager.sendRequest();
		logger.info(SyncServiceManager.getResponseList().toString());
	}
}
