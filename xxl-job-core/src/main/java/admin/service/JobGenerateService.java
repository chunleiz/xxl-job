/**
 * 
 */
package admin.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.job.admin.entity.InstanceEntity;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.route.ExecutorRouteStrategyEnum;
import org.springframework.stereotype.Component;

/**
 * @author wang_yangbj<br>
 * @version 1.0 2019年5月5日 下午6:21:23<br>
 */
@Component
public class JobGenerateService {
    ObjectMapper mapper = new ObjectMapper();
    private static final int JOB_GROUP = 2;
    private static final String JOB_CRON = "0 0 0 29 2 ?";
    private static final String GLUE_TYPE = "BEAN";
//    private static final String EXECUTOR_BLOCK_STRATEGY = "SERIAL_EXECUTION";

    // 修改执行器执行策略改为 丢弃后续调度	DISCARD_LATER	调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败
    private static final String EXECUTOR_BLOCK_STRATEGY = "DISCARD_LATER";
    private static final int EXECUTOR_TIMEOUT = 0;
    private static final int EXECUTOR_FAIL_RETRY_COUNT = 0;

    public XxlJobInfo genJobInfo(InstanceEntity instanceEntity, String executorHandler) throws JsonProcessingException {
        String param = mapper.writeValueAsString(instanceEntity);
        return genJobInfo(instanceEntity, executorHandler, param);
    }

    public XxlJobInfo genJobInfo(InstanceEntity instanceEntity, String executorHandler, String param) {
        return genJobInfo(instanceEntity, executorHandler, param, EXECUTOR_TIMEOUT);

    }
    public XxlJobInfo genJobInfo(InstanceEntity instanceEntity, String executorHandler, String param, int timeOut) {
        XxlJobInfo jobInfo = new XxlJobInfo();
        jobInfo.setJobGroup(JOB_GROUP);
        jobInfo.setJobDesc(instanceEntity.getInstanceId());
        jobInfo.setExecutorRouteStrategy(ExecutorRouteStrategyEnum.ROUND.toString());
        jobInfo.setJobCron(JOB_CRON);
        jobInfo.setGlueType(GLUE_TYPE);
        jobInfo.setExecutorHandler(executorHandler);
        jobInfo.setExecutorBlockStrategy(EXECUTOR_BLOCK_STRATEGY);
        jobInfo.setExecutorTimeout(EXECUTOR_TIMEOUT);
        jobInfo.setExecutorFailRetryCount(EXECUTOR_FAIL_RETRY_COUNT);
        jobInfo.setAuthor(instanceEntity.getRequestId().toString());
        jobInfo.setGlueRemark("csf_manual");
        jobInfo.setExecutorParam(param);
        return jobInfo;

    }
}
