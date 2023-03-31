package admin.controller;

import com.inspur.job.admin.entity.TriggerInfo;
import com.inspur.job.admin.entity.XxlJobInfoCore;
import com.inspur.job.admin.service.CronJobService;
import com.xxl.job.admin.controller.annotation.PermessionLimit;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.admin.core.thread.JobTriggerPoolHelper;
import com.xxl.job.admin.core.trigger.TriggerTypeEnum;
import com.xxl.job.admin.dao.XxlJobGroupDao;
import com.xxl.job.admin.service.XxlJobService;
import com.xxl.job.core.biz.model.ReturnT;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpHeaders;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author wang_yangbj<br>
 * @version 1.0 2019年3月29日 下午6:20:54<br>
 */
@RestController
@RequestMapping("/xxl-job/v1")
public class CronJobController {

    @Resource
    private XxlJobGroupDao xxlJobGroupDao;
    @Resource
    private XxlJobService xxlJobService;
    @Resource
    private CronJobService cronJobService;

    @ApiOperation(value = "新建定时任务，并启动 ", notes = "")
    @RequestMapping(value = "/cronJobs", method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public ReturnT<String> createCronJob(@RequestHeader HttpHeaders headers,@RequestBody @Validated XxlJobInfoCore jobInfo) {
        String appname = headers.getFirst("appname");
        XxlJobInfo xxlJobInfo = cronJobService.genXxlJobInfo(jobInfo,appname);
        ReturnT<String> jobId = xxlJobService.add(xxlJobInfo);
        ReturnT<String> startReturn = xxlJobService.start(Integer.parseInt(jobId.getContent()));
        startReturn.setContent(jobId.getContent());
        return startReturn;
    }

    @ApiOperation(value = "删除定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/{cronJobId}", method = RequestMethod.DELETE)
    @PermessionLimit(limit = false)
    public ReturnT<String> delete(@RequestHeader HttpHeaders headers,@PathVariable int cronJobId) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return xxlJobService.remove(cronJobId);
    }
    
    @ApiOperation(value = "停止定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/{cronJobId}/action/stop", method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public ReturnT<String> stop(@RequestHeader HttpHeaders headers,@PathVariable int cronJobId) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return xxlJobService.stop(cronJobId);
    }
    
    @ApiOperation(value = "更新定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/{cronJobId}/action/update", method = RequestMethod.PATCH)
    @PermessionLimit(limit = false)
    public ReturnT<String> update(@RequestHeader HttpHeaders headers,@RequestBody @Validated XxlJobInfoCore jobInfo) {
        String appname = headers.getFirst("appname");
        XxlJobInfo xxlJobInfo = cronJobService.genXxlJobInfo(jobInfo,appname);
        return xxlJobService.update(xxlJobInfo);
    }
    
    @ApiOperation(value = "触发定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/{cronJobId}/action/trigger", method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public ReturnT<String> triggerCronJob(@RequestHeader HttpHeaders headers,@PathVariable int cronJobId,@RequestBody @Validated TriggerInfo triggerInfo) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        JobTriggerPoolHelper.trigger(cronJobId, TriggerTypeEnum.MANUAL, -1, null, triggerInfo.getExecutorParam());
        return ReturnT.SUCCESS;
    }

    @ApiOperation(value = "新建定时任务，不启动 ", notes = "")
    @PermessionLimit(limit = false)
    @RequestMapping(value = "/cronJobs/action/add", method = RequestMethod.POST)
    public ReturnT<String> addCronJob(@RequestHeader HttpHeaders headers,@RequestBody @Validated XxlJobInfoCore jobInfo) {
        String appname = headers.getFirst("appname");
        XxlJobInfo xxlJobInfo = cronJobService.genXxlJobInfo(jobInfo,appname);
        return xxlJobService.add(xxlJobInfo);
    }
    
    @ApiOperation(value = "启动定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/{cronJobId}/action/start", method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public ReturnT<String> start(@RequestHeader HttpHeaders headers,@PathVariable int cronJobId) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return xxlJobService.start(cronJobId);
    }

    @ApiOperation(value = "获取定时任务列表", notes = "")
    @PermessionLimit(limit = false)
    @RequestMapping(value = "/cronJobs", method = RequestMethod.GET)
    public List<XxlJobInfoCore> list(@RequestHeader HttpHeaders headers,
                                     @RequestParam(value = "filter", defaultValue = "", required = false) String filter) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return cronJobService.list(filter,appname);
    }
    
    @ApiOperation(value = "获取定时任务", notes = "")
    @PermessionLimit(limit = false)
    @RequestMapping(value = "/cronJobs/{cronJobId}", method = RequestMethod.GET)
    public XxlJobInfoCore getCronJob(@RequestHeader HttpHeaders headers,@PathVariable Integer cronJobId) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return cronJobService.getXxlJobInfoCore(cronJobId);
    }
    
    @ApiOperation(value = "批量删除定时任务 ", notes = "")
    @RequestMapping(value = "/cronJobs/action/delete", method = RequestMethod.POST)
    @PermessionLimit(limit = false)
    public ReturnT<List<Integer>> batchDelete(@RequestHeader HttpHeaders headers,@RequestBody  List<Integer> cronJobIds) {
        String appname = headers.getFirst("appname");
        cronJobService.getJobGroup(appname);
        return cronJobService.batchDelete(cronJobIds);
    }
}
