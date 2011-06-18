package com.test.mina;


public class SysDefine {
	private static MessageSyncQueue msgQueue = null;
	
	public synchronized static MessageSyncQueue getMsgQueue() {
		if (null == msgQueue) {
			msgQueue = new MessageSyncQueue();
		}
		return msgQueue;
	}
	public synchronized static MessageSyncQueue getMsgQueue(int max,int min) {
		if (null == msgQueue) {
			msgQueue = new MessageSyncQueue(max,min);
		}
		return msgQueue;
	}
	public static void setMsgQueue(MessageSyncQueue msgQueue) {
		SysDefine.msgQueue = msgQueue;
	}

}
