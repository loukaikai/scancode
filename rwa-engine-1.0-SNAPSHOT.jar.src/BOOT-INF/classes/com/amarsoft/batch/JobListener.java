package BOOT-INF.classes.com.amarsoft.batch;

import com.amarsoft.batch.job.JobExecution;

public interface JobListener {
  void beforeJob(JobExecution paramJobExecution);
  
  void afterJob(JobExecution paramJobExecution);
  
  void onJobError(Throwable paramThrowable, JobExecution paramJobExecution);
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\JobListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */