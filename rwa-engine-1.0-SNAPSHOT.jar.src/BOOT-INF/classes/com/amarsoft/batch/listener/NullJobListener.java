package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.JobListener;
import com.amarsoft.batch.job.JobExecution;

public class NullJobListener implements JobListener {
  public void beforeJob(JobExecution jobExecution) {}
  
  public void afterJob(JobExecution jobExecution) {}
  
  public void onJobError(Throwable e, JobExecution jobExecution) {}
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullJobListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */