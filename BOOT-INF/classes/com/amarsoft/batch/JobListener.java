package BOOT-INF.classes.com.amarsoft.batch;

import com.amarsoft.batch.job.JobExecution;

public interface JobListener {
  void beforeJob(JobExecution paramJobExecution);
  
  void afterJob(JobExecution paramJobExecution);
  
  void onJobError(Throwable paramThrowable, JobExecution paramJobExecution);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\JobListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */