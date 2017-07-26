package com.klindziuk.retrofit.parser;


import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DOM parser XML-file
 */
@SuppressWarnings("restriction")
public class ParametersDomParser {
	private static final Logger logger = Logger.getLogger(ParametersDomParser.class);
	private static final String REQUEST = "request";
	private static final String PARAMETER = "parameter";
	private static final String NAME = "name";
	private static final String VALUE = "value";
	private static final String COMMAND_MESSAGE = "Adding new parameters to map - ";
	private static final String INVALID_PATH_EXCEPTION_MESSAGE = "System cannot find file or path.";
	private static final String SAX_EXCEPTION_MESSAGE = "Cannot parse file.Bad document format of file - ";
	private static final String IO_EXCEPTION_MESSAGE = "Error during reading file - ";
	private List<Map<String, String>> requestList;
	private Map<String, String> commandMap;

	public List<Map<String, String>> getRequestList() {
		return requestList;
	}

	/**
	 * parse XML-file and fill map with parameters
	 * 
	 * @param filePath
	 * @throws FileNotFoundException
	 * @throws SAXException if problem with parsing
	 * @throws IOException if problem with file
	 */
	public void parse(String filePath) throws FileNotFoundException {
		if (null == filePath || filePath.isEmpty()) {
			throw new IllegalArgumentException(INVALID_PATH_EXCEPTION_MESSAGE);
		}
		requestList = new ArrayList<>();
		try {
			Document document = getDocument(filePath);
			Element root = document.getDocumentElement();
			NodeList partNodes = root.getElementsByTagName(REQUEST);
			for (int i = 0; i < partNodes.getLength(); i++) {
				Element partElement = (Element) partNodes.item(i);
				commandMap = new HashMap<>();
				NodeList paramNodes = partElement.getElementsByTagName(PARAMETER);
				fillParameters(paramNodes);
				requestList.add(commandMap);
			}
		} catch (SAXException saxex) {
			logger.error(SAX_EXCEPTION_MESSAGE + filePath, saxex);
		} catch (IOException ioex) {
			logger.error(IO_EXCEPTION_MESSAGE + filePath, ioex);
		}
	}

	private void fillParameters(NodeList paramNodes) {
		String name = "";
		String value = "";
		for (int j = 0; j < paramNodes.getLength(); j++) {
			Element paramElement = (Element) paramNodes.item(j);
			name = paramElement.getAttribute(NAME);
			value = paramElement.getAttribute(VALUE);
			commandMap.put(name, value);
			logger.info(COMMAND_MESSAGE + name + " : " + value);
		}
	}

	/**
	 * get DOM document
	 *
	 * @return document
	 * @throws SAXException if problem with parsing
	 * @throws IOException if problem with file
	 */
	private static Document getDocument(String filePath) throws SAXException, IOException {
		DOMParser parserDOM = new DOMParser();
		parserDOM.parse(filePath);
		Document document = parserDOM.getDocument();
		return document;
	}
}
