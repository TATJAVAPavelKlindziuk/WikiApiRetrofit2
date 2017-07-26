package com.klindziuk.retrofit.service;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.klindziuk.retrofit.parser.ParametersDomParser;

public final class ServiceManagerHelper {
	private static final Logger logger = Logger.getLogger(ServiceManagerHelper.class);
	private static final String WIKI_FILE_PATH = "xml/wiki.xml";
	private static final String PARSE_ERROR_MESSAGE = "Cannot parse file -"; 
	
	private ServiceManagerHelper() {}
		
	public static List<Map<String, String>> initialize(){
	ParametersDomParser parser = new ParametersDomParser();
	try {
		parser.parse(WIKI_FILE_PATH);
	} catch (FileNotFoundException fnfex) {
		logger.error(PARSE_ERROR_MESSAGE + WIKI_FILE_PATH,fnfex);
		fnfex.printStackTrace();
	}
	return parser.getRequestList();
	}
}
