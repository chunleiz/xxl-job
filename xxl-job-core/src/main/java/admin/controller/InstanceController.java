/**
 * 
 */
package admin.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inspur.job.admin.entity.InstanceEntity;
import com.inspur.job.admin.entity.ResourceList;
import com.inspur.job.admin.entity.ResponseMessage;
import com.inspur.job.admin.enums.ExecutorHandler;
import com.inspur.job.admin.service.JobGenerateService;
import com.xxl.job.admin.controller.annotation.PermessionLimit;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.dao.XxlJobLogDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhaoshan<br>
 * @version 1.0 2018年9月18日 上午11:09:55<br>
 */
@RestController
@RequestMapping("/instance-factory/v2/instances")
public class InstanceController {
    private static final Log logger = LogFactory.getLog(InstanceController.class);
    ObjectMapper mapper = new ObjectMapper();
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private JobGenerateService jobGenerateService;
    @Resource
    private XxlJobLogDao xxlJobLogDao;

    /**
     * create url: /rds/instance-factory/v1/instances
     * 
     * @param instanceEntity
     * @return response
     */
    @RequestMapping(method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public String create(@RequestBody InstanceEntity instanceEntity , @RequestParam(value = "asyn", required = false, defaultValue = "true") Boolean asyn) throws Exception {
        logger.info("received post request from user.");
        logger.debug("instanceId = " + instanceEntity.getInstanceId());
        logger.debug("templateId = " + instanceEntity.getTemplateId());
        logger.debug("templateVersion = " + instanceEntity.getTemplateVersion());

        ResponseMessage response = new ResponseMessage(instanceEntity.getRequestId().toString());
        
        if (asyn == false) {
            XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity, ExecutorHandler.createJobHandler.toString());
            ReturnT<String> jobId = xxlJobService.add(jobInfo);
            ReturnT<String> result = JobTriggerPoolHelper.triggerSync(Integer.parseInt(jobId.getContent()),
                    TriggerTypeEnum.MANUAL, -1, null, jobInfo.getExecutorParam());
            logger.debug("Msg:" + result.getMsg());
            logger.debug("Content:" + result.getContent());
            response.setResult(result.getMsg());
        } else {
            XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity, ExecutorHandler.createJobHandler.toString());
            ReturnT<String> jobId = xxlJobService.add(jobInfo);
            JobTriggerPoolHelper.trigger(Integer.parseInt(jobId.getContent()), TriggerTypeEnum.MANUAL, -1, null,
                    jobInfo.getExecutorParam());
        }
        
        String ret = null;
        ret = mapper.writeValueAsString(response);
        return ret;
    }

    /**
     * update url: /rds/instance-factory/v1/instances/{instanceId}
     * 
     * @param instanceId
     * @param instanceEntity
     * @return response
     */
    @RequestMapping(value = "/{instanceId}", method = RequestMethod.PUT)
    @PermessionLimit(limit = false)
    public String update(@PathVariable("instanceId") String instanceId, @RequestBody InstanceEntity instanceEntity)
            throws Exception {
        logger.info("received put request from user.");
        logger.debug("instanceId = " + instanceId);
        logger.debug("templateId = " + instanceEntity.getTemplateId());
        logger.debug("templateVersion = " + instanceEntity.getTemplateVersion());
        instanceEntity.setInstanceId(instanceId);
        XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity, ExecutorHandler.updateJobHandler.toString());
        ReturnT<String> jobId = xxlJobService.add(jobInfo);
        JobTriggerPoolHelper.trigger(Integer.parseInt(jobId.getContent()), TriggerTypeEnum.MANUAL, -1, null,
                jobInfo.getExecutorParam());
        ResponseMessage response = new ResponseMessage(instanceEntity.getRequestId().toString());
        String ret = null;
        ret = mapper.writeValueAsString(response);
        return ret;
    }

    /**
     * delete url DELETE /rds/instance-factory/v1/instances/{instanceId}
     * 
     * @param instanceId
     *            instanceId for a group of vm
     * @param routingKey
     *            the topic for RabbitMQ
     * @param softDelete
     *            whether or not to delete all resource
     * @param resourceList
     *            if is a soft delete ,list the resource to delete or remain
     * @return
     */
    @RequestMapping(value = "/{instanceId}", method = RequestMethod.DELETE)
    @PermessionLimit(limit = false)
    public String delete(@PathVariable("instanceId") String instanceId,
            @RequestParam(value = "routingKey", required = true) String routingKey,
            @RequestParam(value = "progressRoutingKey", required = false) String progressRoutingKey,
            @RequestParam(value = "osToken", required = false) String osToken,
            @RequestParam(value = "tenantName", required = false) String tenantName) throws Exception {
        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setInstanceId(instanceId);
        instanceEntity.setRoutingKey(routingKey);
        logger.info("received delete request from user.");
        logger.debug("instanceId = " + instanceId);
        logger.debug("topic = " + routingKey);
        if (null != osToken) {
            logger.debug("osToken = " + osToken);
        }
        if (null != tenantName) {
            logger.debug("tenantName = " + tenantName);
        }
        if (null != progressRoutingKey) {
            logger.debug("progressRoutingKey = " + progressRoutingKey);
            instanceEntity.setProgressRoutingKey(progressRoutingKey);
        }
        Map<String, String> request = new HashMap<String, String>();
        request.put("instanceEntity", mapper.writeValueAsString(instanceEntity));
        request.put("tokenId", osToken);
        request.put("tenantName", tenantName);
        XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity,
                ExecutorHandler.hardDeleteJobHandler.toString(), mapper.writeValueAsString(request));
        ReturnT<String> jobId = xxlJobService.add(jobInfo);
        JobTriggerPoolHelper.trigger(Integer.parseInt(jobId.getContent()), TriggerTypeEnum.MANUAL, -1, null,
                jobInfo.getExecutorParam());

        ResponseMessage response = new ResponseMessage(instanceEntity.getRequestId().toString());
        String ret = null;
        ret = mapper.writeValueAsString(response);
        return ret;
    }

    /**
     * put url PUT /instance-factory/v1/instances/{instanceId}/actions/softdelete
     * 
     * @param instanceId
     *            instanceId for a group of vm
     * @param routingKey
     *            the topic for RabbitMQ
     * @param softDelete
     *            whether or not to delete all resource
     * @param resourceList
     *            if is a soft delete ,list the resource to delete or remain
     * @return
     */
    @RequestMapping(value = "/{instanceId}/actions/softdelete", method = RequestMethod.PUT)
    @PermessionLimit(limit = false)
    public String softDelete(@PathVariable("instanceId") String instanceId,
            @RequestParam(value = "routingKey", required = true) String routingKey,
            @RequestParam(value = "progressRoutingKey", required = false) String progressRoutingKey,
            @RequestParam(value = "osToken", required = false) String osToken,
            @RequestParam(value = "tenantName", required = false) String tenantName,
            @RequestBody ResourceList resourceList) throws Exception {
        logger.info("received soft delete request from user.");
        logger.debug("instanceId = " + instanceId);
        logger.debug("topic = " + routingKey);
        if (null != osToken) {
            logger.debug("osToken = " + osToken);
        }
        if (null != tenantName) {
            logger.debug("tenantName = " + tenantName);
        }
        InstanceEntity instanceEntity = new InstanceEntity();
        instanceEntity.setInstanceId(instanceId);
        instanceEntity.setRoutingKey(routingKey);
        if (null != progressRoutingKey) {
            logger.debug("progressRoutingKey = " + progressRoutingKey);
            instanceEntity.setProgressRoutingKey(progressRoutingKey);
        }
        Map<String, String> request = new HashMap<String, String>();
        request.put("instanceEntity", mapper.writeValueAsString(instanceEntity));
        request.put("tokenId", osToken);
        request.put("tenantName", tenantName);
        request.put("resourceList", mapper.writeValueAsString(resourceList));
        XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity,
                ExecutorHandler.softDeleteJobHandler.toString(), mapper.writeValueAsString(request));
        ReturnT<String> jobId = xxlJobService.add(jobInfo);
        JobTriggerPoolHelper.trigger(Integer.parseInt(jobId.getContent()), TriggerTypeEnum.MANUAL, -1, null,
                jobInfo.getExecutorParam());
        ResponseMessage response = new ResponseMessage(instanceEntity.getRequestId().toString());
        String ret = null;
        ret = mapper.writeValueAsString(response);
        return ret;
    }

    /**
     * PUT /instance-factory/v1/instance/{instanceId}/actions/operate operate
     * restapi
     * 
     * @param instanceId
     *            instanceId
     * @param instanceEntity
     *            ansibleParameter is required
     * @param asyn
     *            Does it return asynchronously?
     * @return
     */
    @SuppressWarnings("static-access")
    @RequestMapping(value = "/{instanceId}/actions/operate", method = RequestMethod.PUT)
    @PermessionLimit(limit = false)
    public String operate(@PathVariable("instanceId") String instanceId, @RequestBody InstanceEntity instanceEntity,
            @RequestParam(value = "asyn", required = true) Boolean asyn,
            @RequestParam(value = "needLock", required = false, defaultValue = "true") Boolean needLock)
            throws Exception {
        logger.info("received put operate request from user.");
        logger.debug("instanceId = " + instanceId);
        logger.debug("templateId = " + instanceEntity.getTemplateId());
        logger.debug("templateVersion = " + instanceEntity.getTemplateVersion());
        logger.debug("asyn = " + asyn);
        if (null != needLock) {
            logger.debug("needLock = " + needLock);
        }
        instanceEntity.setInstanceId(instanceId);
        ResponseMessage response = new ResponseMessage(instanceEntity.getRequestId().toString());
        Map<String, String> request = new HashMap<String, String>();
        request.put("instanceEntity", mapper.writeValueAsString(instanceEntity));
        request.put("needLock", needLock.toString());

        if (asyn == false) {
            XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity,
                    ExecutorHandler.operationJobHandler.toString(), mapper.writeValueAsString(request), 300);
            ReturnT<String> jobId = xxlJobService.add(jobInfo);
            ReturnT<String> result = JobTriggerPoolHelper.triggerSync(Integer.parseInt(jobId.getContent()),
                    TriggerTypeEnum.MANUAL, -1, null, jobInfo.getExecutorParam());
            logger.debug("Msg:" + result.getMsg());
            logger.debug("Content:" + result.getContent());
            response.setResult(result.getMsg());
        } else {
            XxlJobInfo jobInfo = jobGenerateService.genJobInfo(instanceEntity,
                    ExecutorHandler.operationJobHandler.toString(), mapper.writeValueAsString(request));
            ReturnT<String> jobId = xxlJobService.add(jobInfo);
            JobTriggerPoolHelper.trigger(Integer.parseInt(jobId.getContent()), TriggerTypeEnum.MANUAL, -1, null,
                    jobInfo.getExecutorParam());
        }
        String ret = null;
        ret = mapper.writeValueAsString(response);
        return ret;
    }

}
