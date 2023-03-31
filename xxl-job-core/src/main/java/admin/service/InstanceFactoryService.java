package admin.service;

import com.xxl.job.admin.core.model.XxlJobLog;

/**
 * @author wang_yangbj<br>
 * @version 1.0 2019年4月23日 下午6:20:54<br>
 */
public interface InstanceFactoryService {
	
	public XxlJobLog getJobLogByExecutorParam(String executorParam) ;
	
}
