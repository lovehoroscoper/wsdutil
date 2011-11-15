//package com.weisd.quartz;
//
//import java.text.DateFormat;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//import org.quartz.CronTrigger;
//import org.quartz.JobDetail;
//import org.quartz.Scheduler;
//import org.quartz.SchedulerException;
//import org.quartz.SchedulerFactory;
//import org.quartz.impl.StdSchedulerFactory;
//
//import com.hisunsray.commons.res.Config;
//
///**
// * 定时任务管理类
// * 
// * @author：wjjava
// * @since：2011-9-2 下午03:07:02
// * @version:1.0
// */
//public class QuartzManager {
//	//private static Logger logger = Logger.getLogger(QuartzManager.class);
//
//	private static SchedulerFactory gSchedulerFactory = new StdSchedulerFactory();
//	public static final String JOB_GROUP_NAME = "MQ_JOBGROUP_NAME";
//	public static final String TRIGGER_GROUP_NAME = "MQ_TRIGGERGROUP_NAME";
//	private static QuartzManager manager = null;
//
//	private QuartzManager() {
//	}
//
//	public static QuartzManager getInstance() {
//		if (null == manager)
//			manager = new QuartzManager();
//		return manager;
//	}
//
//	public static SchedulerFactory getgSchedulerFactory() {
//		return gSchedulerFactory;
//	}
//
//	public static void setgSchedulerFactory(SchedulerFactory gSchedulerFactory) {
//		QuartzManager.gSchedulerFactory = gSchedulerFactory;
//	}
//
//	/**
//	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名
//	 * 
//	 * @param jobName
//	 *            任务名
//	 * @param jobClass
//	 *            任务
//	 * @param time
//	 *            时间设置，参考quartz说明文档
//	 * @throws SchedulerException
//	 */
//	public static void addJob(String jobName, TaskBean taskBean) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			JobDetail jobDetail = new JobDetail();// 任务名，任务组，任务执行类
//			jobDetail.setName(jobName);
//			jobDetail.setGroup(JOB_GROUP_NAME);
//			jobDetail.setJobClass(QueryTaskJob.class);
//			jobDetail.getJobDataMap().put("taskBean", taskBean);
//			// 触发器
//			CronTrigger trigger = new CronTrigger(jobName, TRIGGER_GROUP_NAME);// 触发器名,触发器组
//			trigger.setCronExpression("0 0/2 * * * ?");// 查询间隔时长
//			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//			Date d = df.parse("2011-09-29 18:11:00");
//			trigger.setStartTime(d);
//			sched.scheduleJob(jobDetail, trigger);
//			// 启动
//			if (!sched.isShutdown()) {
//				sched.start();
//			}
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 添加一个定时任务
//	 * 
//	 * @param jobName
//	 *            任务名
//	 * @param jobGroupName
//	 *            任务组名
//	 * @param triggerName
//	 *            触发器名
//	 * @param triggerGroupName
//	 *            触发器组名
//	 * @param jobClass
//	 *            任务
//	 * @param time
//	 *            时间设置，参考quartz说明文档
//	 * @throws SchedulerException
//	 */
//	public static void addJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName, String jobClass, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			JobDetail jobDetail = new JobDetail(jobName, jobGroupName, Class.forName(jobClass));// 任务名，任务组，任务执行类
//			// 触发器
//			CronTrigger trigger = new CronTrigger(triggerName, triggerGroupName);// 触发器名,触发器组
//			trigger.setCronExpression(time);// 触发器时间设定
//			sched.scheduleJob(jobDetail, trigger);
//			// 启动
//			if (!sched.isShutdown()) {
//				sched.start();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
//	 * 
//	 * @param jobName
//	 * @param time
//	 */
//	@SuppressWarnings({ "rawtypes", "unused" })
//	public static void modifyJobTime(String jobName, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(jobName, TRIGGER_GROUP_NAME);
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				JobDetail jobDetail = sched.getJobDetail(jobName, JOB_GROUP_NAME);
//				Class objJobClass = jobDetail.getJobClass();
//				String jobClass = objJobClass.getName();
//				removeJob(jobName);
//
//				// addJob(jobName, jobClass, time);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 修改一个任务的触发时间
//	 * 
//	 * @param triggerName
//	 * @param triggerGroupName
//	 * @param time
//	 */
//	public static void modifyJobTime(String triggerName, String triggerGroupName, String time) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			CronTrigger trigger = (CronTrigger) sched.getTrigger(triggerName, triggerGroupName);
//			if (trigger == null) {
//				return;
//			}
//			String oldTime = trigger.getCronExpression();
//			if (!oldTime.equalsIgnoreCase(time)) {
//				CronTrigger ct = (CronTrigger) trigger;
//				// 修改时间
//				ct.setCronExpression(time);
//				// 重启触发器
//				sched.resumeTrigger(triggerName, triggerGroupName);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
//	 * 
//	 * @param jobName
//	 */
//	public static void removeJob(String jobName) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			sched.pauseTrigger(jobName, TRIGGER_GROUP_NAME);// 停止触发器
//			sched.unscheduleJob(jobName, TRIGGER_GROUP_NAME);// 移除触发器
//			sched.deleteJob(jobName, JOB_GROUP_NAME);// 删除任务
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 移除一个任务
//	 * 
//	 * @param jobName
//	 * @param jobGroupName
//	 * @param triggerName
//	 * @param triggerGroupName
//	 */
//	public static void removeJob(String jobName, String jobGroupName, String triggerName, String triggerGroupName) {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			sched.pauseTrigger(triggerName, triggerGroupName);// 停止触发器
//			sched.unscheduleJob(triggerName, triggerGroupName);// 移除触发器
//			sched.deleteJob(jobName, jobGroupName);// 删除任务
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 启动所有定时任务
//	 */
//	public static void startJobs() {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			sched.start();
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//
//	/**
//	 * 关闭所有定时任务
//	 */
//	public static void shutdownJobs() {
//		try {
//			Scheduler sched = gSchedulerFactory.getScheduler();
//			if (!sched.isShutdown()) {
//				sched.shutdown();
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new RuntimeException(e);
//		}
//	}
//}
