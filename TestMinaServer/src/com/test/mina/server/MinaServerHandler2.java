package com.test.mina.server;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

/**
 * 
 * 
 */
public class MinaServerHandler2 extends IoHandlerAdapter {


	private static Logger logger = Logger.getLogger(MinaServerHandler2.class);

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		
		logger.info("-------MinaServerHandler2------messageReceived---------" + (String)message);
		


	}
	
//	@Override
//	public void messageSent(IoSession session, Object message) throws Exception {
//		logger.info("--------MinaServerHandler2---------------:" + message);
//		super
//
//	}
	
	
}
