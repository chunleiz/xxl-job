package admin.entity;

import java.io.Serializable;
import java.util.Date;


/**
 * @author wang_yangbj<br>
 * @version 1.0
 * 2019年6月6日 下午2:10:05<br>
 */
public class XxlJobInfoCore implements Serializable {
	
	/**
     * 
     */
    private static final long serialVersionUID = -1395890100338986876L;

    private int id;				// 主键ID	    (JobKey.name)
	
	private String jobCron;		// 任务执行CRON表达式 【base on quartz】
	private String instanceId;     // 实例id(inspur)
	private String creator;		// 负责人 //creator(inspur)
	
	private Date addTime;
    private Date updateTime;

//	private String executorRouteStrategy = "ROUND";	// 执行器路由策略---轮询
	private String executorHandler;		    // 执行器，任务Handler名称
	private String executorParam;		    // 执行器，任务参数
	private int executorTimeout;     		// 任务执行超时时间，单位秒
	private int executorFailRetryCount;		// 失败重试次数
	
	private String jobCronType;		// 定时任务类型(inspur)
	private String jobStatus;      // 任务状态 【base on quartz】

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getJobCron() {
		return jobCron;
	}

	public void setJobCron(String jobCron) {
		this.jobCron = jobCron;
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

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getJobCronType() {
        return jobCronType;
    }

    public void setJobCronType(String jobCronType) {
        this.jobCronType = jobCronType;
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

    public String getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
    }

}
