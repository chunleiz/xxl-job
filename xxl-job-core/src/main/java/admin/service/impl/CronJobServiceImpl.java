/**
 * 
 */
package admin.service.impl;

import com.inspur.job.admin.entity.XxlJobInfoCore;
import com.inspur.job.admin.service.CronJobService;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.schedule.XxlJobDynamicScheduler;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.dao.XxlJobInfoDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author wang_yangbj<br>
 * @version 1.0
 * 2019年6月6日 下午4:09:04<br>
 */
@Service
public class CronJobServiceImpl implements CronJobService {
    
    private static final String EXECUTOR_ROUTE_STRATEGY = "ROUND";
    private static final String GLUE_TYPE = "BEAN";
//    private static final String EXECUTOR_BLOCK_STRATEGY = "SERIAL_EXECUTION";
    // 修改执行器执行策略改为 丢弃后续调度	DISCARD_LATER	调度请求进入单机执行器后，发现执行器存在运行的调度任务，本次请求将会被丢弃并标记为失败
    private static final String EXECUTOR_BLOCK_STRATEGY = "DISCARD_LATER";
    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobInfoDao xxlJobInfoDao;
    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private CronJobService cronJobService;
    
    @Override
    public XxlJobInfo genXxlJobInfo(XxlJobInfoCore xxlJobInfoCore,String appname) {
            
        XxlJobInfo xxlJobInfo = new XxlJobInfo();
        BeanUtils.copyProperties(xxlJobInfoCore, xxlJobInfo);
        xxlJobInfo.setAuthor(xxlJobInfoCore.getCreator());
        xxlJobInfo.setExecutorRouteStrategy(EXECUTOR_ROUTE_STRATEGY);
        xxlJobInfo.setGlueType(GLUE_TYPE);
        xxlJobInfo.setExecutorBlockStrategy(EXECUTOR_BLOCK_STRATEGY);
        xxlJobInfo.setJobDesc(xxlJobInfoCore.getInstanceId());
        xxlJobInfo.setGlueRemark(xxlJobInfoCore.getJobCronType());
        XxlJobGroup xxlJobGroup = getJobGroup(appname);

        if(xxlJobGroup != null) {
            xxlJobInfo.setJobGroup(xxlJobGroup.getId());
        }
        return xxlJobInfo;
    }

    @Override
    public XxlJobGroup getJobGroup(String appname) {
        return xxlJobGroupDao.findByAppName(appname);
    }

    @Override
    public List<XxlJobInfoCore> list(String filter,String appname) {
        XxlJobGroup xxlJobGroup = cronJobService.getJobGroup(appname);
        List<XxlJobInfoCore> xxlJobInfoCores = new ArrayList<>();
//        List<XxlJobInfo> xxlJobInfos= xxlJobInfoDao.getJobsByJobDesc(filter);
        List<XxlJobInfo> xxlJobInfos= xxlJobInfoDao.getJobsByJobDescAndGroup(filter,xxlJobGroup.getId());
        for(XxlJobInfo xxlJobInfo : xxlJobInfos) {
            XxlJobInfoCore xxlJobInfoCore = new XxlJobInfoCore();
            XxlJobDynamicScheduler.fillJobInfo(xxlJobInfo);
            BeanUtils.copyProperties(xxlJobInfo, xxlJobInfoCore);
            xxlJobInfoCore.setInstanceId(xxlJobInfo.getJobDesc());
            xxlJobInfoCore.setCreator(xxlJobInfo.getAuthor());
            xxlJobInfoCore.setJobCronType(xxlJobInfo.getGlueRemark());
            xxlJobInfoCores.add(xxlJobInfoCore);
        }
        return xxlJobInfoCores;
    }

    @Override
    public XxlJobInfoCore getXxlJobInfoCore(Integer cronJobId) {
        XxlJobInfoCore xxlJobInfoCore = new XxlJobInfoCore();
        XxlJobInfo xxlJobInfo = xxlJobInfoDao.loadById(cronJobId);
        XxlJobDynamicScheduler.fillJobInfo(xxlJobInfo);
        BeanUtils.copyProperties(xxlJobInfo, xxlJobInfoCore);
        xxlJobInfoCore.setCreator(xxlJobInfo.getAuthor());
        xxlJobInfoCore.setJobCronType(xxlJobInfo.getGlueRemark());
        xxlJobInfoCore.setInstanceId(xxlJobInfo.getJobDesc());
        return xxlJobInfoCore;
    }

    @Override
    public ReturnT<List<Integer>> batchDelete(List<Integer> cronJobIds) {
        ReturnT<List<Integer>> delFailedcronJobIds = new ReturnT<List<Integer>>();
        for(Integer cronJobId : cronJobIds) {
            try {
                ReturnT<String> result = xxlJobService.remove(cronJobId);
                if(result.getCode() == ReturnT.FAIL_CODE) {
                    delFailedcronJobIds.getContent().add(cronJobId);
                }
            } catch (Exception e) {
                delFailedcronJobIds.getContent().add(cronJobId);
            }
        }
        if(null == delFailedcronJobIds.getContent() || delFailedcronJobIds.getContent().size() == 0) {
            delFailedcronJobIds.setCode(ReturnT.SUCCESS_CODE);
        }else {
            delFailedcronJobIds.setCode(ReturnT.FAIL_CODE);
            delFailedcronJobIds.setMsg("定时任务删除失败！！！");
        }
        return delFailedcronJobIds;
    }
}
