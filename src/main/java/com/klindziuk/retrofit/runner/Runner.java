package com.klindziuk.retrofit.runner;

import java.io.FileNotFoundException;

import com.klindziuk.retrofit.parser.ParametersDomParser;
import com.klindziuk.retrofit.service.WikiApiServiceManager;

public class Runner {
	private static final String WIKI_FILE_PATH = "xml/wiki.xml";
	
	public static void main(String[] args) throws FileNotFoundException{
				ParametersDomParser parser = new ParametersDomParser();
				parser.parse(WIKI_FILE_PATH);
				WikiApiServiceManager manager = new WikiApiServiceManager();
				manager.setRequestList(parser.getRequestList());
				manager.sendRequests();	
			}
}
