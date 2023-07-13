package com.base.component.xxljob.beans;

import java.util.Date;

/**
 * @author start 
 */
public class XxlJobInfo {
	/**
	 * 主键ID
	 */
	private int id;
	/**
	 * 执行器主键ID
	 */
	private int jobGroup;
	/**
	 * 任务执行CRON表达式
	 */
	private String jobCron;
	private String jobDesc;
	
	private Date addTime;
	private Date updateTime;
	/**
	 * 负责人
	 */
	private String author;
	/**
	 * 报警邮件
	 */
	private String alarmEmail;

	/**
	 * 执行器路由策略
	 */
	private String executorRouteStrategy;
	/**
	 * 执行器，任务Handler名称
	 */
	private String executorHandler;
	/**
	 * 执行器，任务参数
	 */
	private String executorParam;
	/**
	 * 阻塞处理策略
	 */
	private String executorBlockStrategy;
	/**
	 * 任务执行超时时间，单位秒
	 */
	private int executorTimeout;
	/**
	 * 失败重试次数
	 */
	private int executorFailRetryCount;

	/**
	 * GLUE类型	#com.xxl.job.core.glue.GlueTypeEnum
	 */
	private String glueType;
	/**
	 * GLUE源代码
	 */
	private String glueSource;
	/**
	 * GLUE备注
	 */
	private String glueRemark;
	/**
	 * GLUE更新时间
	 */
	private Date glueUpdatetime;

	/**
	 * 子任务ID，多个逗号分隔
	 */
	private String childJobId;

	/**
	 * 调度状态：0-停止，1-运行
	 */
	private int triggerStatus;
	/**
	 * 上次调度时间
	 */
	private long triggerLastTime;
	/**
	 * 下次调度时间
	 */
	private long triggerNextTime;


	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getJobGroup() {
		return jobGroup;
	}

	public void setJobGroup(int jobGroup) {
		this.jobGroup = jobGroup;
	}

	public String getJobCron() {
		return jobCron;
	}

	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
	}

	public String getJobDesc() {
		return jobDesc;
	}

	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}

	public Date getAddTime() {
		return addTime;
	}

	public void setAddTime(Date addTime) {
		this.addTime = addTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getAlarmEmail() {
		return alarmEmail;
	}

	public void setAlarmEmail(String alarmEmail) {
		this.alarmEmail = alarmEmail;
	}

	public String getExecutorRouteStrategy() {
		return executorRouteStrategy;
	}

	public void setExecutorRouteStrategy(String executorRouteStrategy) {
		this.executorRouteStrategy = executorRouteStrategy;
	}

	public String getExecutorHandler() {
		return executorHandler;
	}

	public void setExecutorHandler(String executorHandler) {
		this.executorHandler = executorHandler;
	}

	public String getExecutorParam() {
		return executorParam;
	}

	public void setExecutorParam(String executorParam) {
		this.executorParam = executorParam;
	}

	public String getExecutorBlockStrategy() {
		return executorBlockStrategy;
	}

	public void setExecutorBlockStrategy(String executorBlockStrategy) {
		this.executorBlockStrategy = executorBlockStrategy;
	}

	public int getExecutorTimeout() {
		return executorTimeout;
	}

	public void setExecutorTimeout(int executorTimeout) {
		this.executorTimeout = executorTimeout;
	}

	public int getExecutorFailRetryCount() {
		return executorFailRetryCount;
	}

	public void setExecutorFailRetryCount(int executorFailRetryCount) {
		this.executorFailRetryCount = executorFailRetryCount;
	}

	public String getGlueType() {
		return glueType;
	}

	public void setGlueType(String glueType) {
		this.glueType = glueType;
	}

	public String getGlueSource() {
		return glueSource;
	}

	public void setGlueSource(String glueSource) {
		this.glueSource = glueSource;
	}

	public String getGlueRemark() {
		return glueRemark;
	}

	public void setGlueRemark(String glueRemark) {
		this.glueRemark = glueRemark;
	}

	public Date getGlueUpdatetime() {
		return glueUpdatetime;
	}

	public void setGlueUpdatetime(Date glueUpdatetime) {
		this.glueUpdatetime = glueUpdatetime;
	}

	public String getChildJobId() {
		return childJobId;
	}

	public void setChildJobId(String childJobId) {
		this.childJobId = childJobId;
	}

	public int getTriggerStatus() {
		return triggerStatus;
	}

	public void setTriggerStatus(int triggerStatus) {
		this.triggerStatus = triggerStatus;
	}

	public long getTriggerLastTime() {
		return triggerLastTime;
	}

	public void setTriggerLastTime(long triggerLastTime) {
		this.triggerLastTime = triggerLastTime;
	}

	public long getTriggerNextTime() {
		return triggerNextTime;
	}

	public void setTriggerNextTime(long triggerNextTime) {
		this.triggerNextTime = triggerNextTime;
	}
}
