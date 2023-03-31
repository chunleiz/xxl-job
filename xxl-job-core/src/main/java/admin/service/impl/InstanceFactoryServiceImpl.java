package admin.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.job.admin.service.InstanceFactoryService;
import com.xxl.job.admin.core.model.XxlJobLog;
import com.xxl.job.admin.dao.XxlJobLogDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author wang_yangbj<br>
 * @version 1.0 2019年4月23日 下午6:20:54<br>
 */
@Service
public class InstanceFactoryServiceImpl implements InstanceFactoryService {
	private static Logger logger = LoggerFactory.getLogger(InstanceFactoryServiceImpl.class);
    ObjectMapper mapper = new ObjectMapper();
	@Resource
	public XxlJobLogDao xxlJobLogDao;

    @Override
    public XxlJobLog getJobLogByExecutorParam(String executorParam) {
        XxlJobLog xxlJobLog;
        xxlJobLog = xxlJobLogDao.findJobLogByExecutorParam(executorParam);
        return xxlJobLog;
    }
}
