package com.test.mina;

import java.util.concurrent.ArrayBlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.test.vo.TaskBean;


public class MessageSyncQueue {

	@SuppressWarnings("unused")
	private static Log log = LogFactory.getLog(MessageSyncQueue.class);

	private static int MAX = 20000;
	private static int MIN = 50;

	private ArrayBlockingQueue<TaskBean> msgQueue_m = null;

	public MessageSyncQueue(int max,int min) {
		this.MAX = max;
		this.MIN = min;
		msgQueue_m = new ArrayBlockingQueue<TaskBean>(MAX);
	}
	public MessageSyncQueue() {
		msgQueue_m = new ArrayBlockingQueue<TaskBean>(MAX);
	}

	// 增加一个元素
	public synchronized boolean push(TaskBean msg) {
		return msgQueue_m.offer(msg);
	}

	// 
	public synchronized TaskBean pop() {
		TaskBean msg = null;
		if (!isEmpty()) {
			msg = msgQueue_m.poll();
		}
		return msg;
	}

	// 队列是否为空
	public synchronized boolean isEmpty() {
		return msgQueue_m.isEmpty();
	}

	public synchronized int getCount() {
		return msgQueue_m.size();
	}

	// 是否允许继续向队列中插入数据
	public synchronized boolean isToAlert() {
		return msgQueue_m.size() >= MAX;
	}

}
