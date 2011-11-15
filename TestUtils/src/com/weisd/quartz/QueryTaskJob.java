//package com.weisd.quartz;
//
//import org.apache.log4j.Logger;
//import org.quartz.Job;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//
//public class QueryTaskJob implements Job {
//	private static Logger log = Logger.getLogger(QueryTaskJob.class);
//	private TaskBean taskBean;
//
//	public QueryTaskJob() {
//	}
//
//	@SuppressWarnings("static-access")
//	public void execute(JobExecutionContext context) throws JobExecutionException {
//
//		log.info("次通知超过限定次数");
//	}
//
//	public TaskBean getTaskBean() {
//		return taskBean;
//	}
//
//	public void setTaskBean(TaskBean taskBean) {
//		this.taskBean = taskBean;
//	}
//
//}
