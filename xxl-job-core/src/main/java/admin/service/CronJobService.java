/**
 * 
 */
package admin.service;

import com.inspur.job.admin.entity.XxlJobInfoCore;
import com.xxl.job.admin.core.model.XxlJobGroup;
import com.xxl.job.admin.core.model.XxlJobInfo;
import com.xxl.job.core.biz.model.ReturnT;

import java.util.List;

/**
 * @author wang_yangbj<br>
 * @version 1.0
 * 2019年6月6日 上午11:30:14<br>
 */
public interface CronJobService {

    public XxlJobInfo genXxlJobInfo (XxlJobInfoCore xxlJobInfoCore,String appname);
    
    public XxlJobGroup getJobGroup(String appname);

    public List<XxlJobInfoCore> list(String filter,String appname);
    
    public XxlJobInfoCore getXxlJobInfoCore (Integer cronJobId);
    
    public ReturnT<List<Integer>> batchDelete (List<Integer> cronJobIds);
    
}
