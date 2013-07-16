package com.test.mina.server.filter;

import java.nio.charset.CharsetEncoder;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.IoFilterAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.core.write.DefaultWriteRequest;
import org.apache.mina.core.write.WriteRequest;

/**
 * @desc 描述：收单到核心加密验证
 * 
 * @author weisd E-mail:weisd@junbao.net
 * @version 创建时间：2012-1-9 下午2:15:20
 */
public class Order2CoreDigestFilter extends IoFilterAdapter {

	@Override
	public void filterWrite(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		System.out.println("-----------filterWrite--------");
		
		IoBuffer by = (IoBuffer)writeRequest.getMessage();
		System.out.println(new String(by.array()));
		
//		IoBuffer newB = new IoBuffer();
		

        String value = "test";
        IoBuffer buf = IoBuffer.allocate(value.length())
                .setAutoExpand(true);
        
        buf.put(value.getBytes());
//        CharsetEncoder encoder = (CharsetEncoder) session.getAttribute(ENCODER);
//        buf.putString(value,encoder);
////        if (buf.position() > maxLineLength) {
////            throw new IllegalArgumentException("Line length: " + buf.position());
////        }
        buf.flip();
        System.out.println(new String(buf.array()));
		
		DefaultWriteRequest def = new DefaultWriteRequest(by, writeRequest.getFuture(), writeRequest.getDestination());
		
		nextFilter.filterWrite(session, def);
	}

	
	/**
	 * 补充信息返回加密
	 */
	@Override
	public void messageSent(NextFilter nextFilter, IoSession session, WriteRequest writeRequest) throws Exception {
		System.out.println("----------messageSent---------");
		nextFilter.messageSent(session, writeRequest);
	}

}
