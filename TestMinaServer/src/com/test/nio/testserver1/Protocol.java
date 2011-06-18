package com.test.nio.testserver1;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;

public interface Protocol {

	void handleAccept(SelectionKey skey) throws IOException;
	
	void handleRead(SelectionKey skey) throws IOException;
	
	void handlWrite(SelectionKey skey, ByteBuffer bf) throws IOException;
}
