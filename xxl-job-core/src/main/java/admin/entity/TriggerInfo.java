/**
 * 
 */
package admin.entity;

import java.io.Serializable;

/**
 * @author wang_yangbj<br>
 * @version 1.0
 * 2019年5月29日 下午6:44:56<br>
 */
public class TriggerInfo implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    
    private Integer jobId;
    private String  executorParam;
    public Integer getJobId() {
        return jobId;
    }
    public void setJobId(Integer jobId) {
        this.jobId = jobId;
    }
    public String getExecutorParam() {
        return executorParam;
    }
    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }
    
    

}
