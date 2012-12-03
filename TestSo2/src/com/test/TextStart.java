package com.test;

import org.apache.log4j.Logger;

public class TextStart {

	private static Logger logger = Logger.getLogger(TextStart.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		logger.info("init  ----------start------");
		So so = new So();
		so.initial();
		logger.info("init  ----------end------");
	}
}
