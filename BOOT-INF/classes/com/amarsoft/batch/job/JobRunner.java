/*    */ package BOOT-INF.classes.com.amarsoft.batch.job;
/*    */ 
/*    */ import cn.hutool.core.util.StrUtil;
/*    */ import com.amarsoft.batch.JobListener;
/*    */ import com.amarsoft.batch.exception.JobRunningException;
/*    */ import com.amarsoft.batch.job.Job;
/*    */ import com.amarsoft.batch.job.JobExecution;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class JobRunner
/*    */ {
/* 15 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.job.JobRunner.class);
/*    */ 
/*    */   
/*    */   public static JobExecution run(Job job, Map<String, Object> jobParameter) {
/* 19 */     if (job == null) {
/* 20 */       throw new JobRunningException("job is null");
/*    */     }
/* 22 */     JobExecution jobExecution = new JobExecution();
/* 23 */     jobExecution.setName(job.getName());
/* 24 */     jobExecution.setJobParameter(jobParameter);
/* 25 */     JobListener jobListener = job.getJobListener();
/*    */     try {
/* 27 */       jobListener.beforeJob(jobExecution);
/* 28 */       job.execute(jobExecution);
/* 29 */     } catch (Exception e) {
/* 30 */       log.error("job[" + jobExecution.getName() + "]执行异常：", e);
/* 31 */       jobExecution.setThrowable(e);
/* 32 */       jobExecution.setStatus("0");
/*    */     } 
/*    */     
/*    */     try {
/* 36 */       if (StrUtil.equals("0", jobExecution.getStatus())) {
/* 37 */         if (StrUtil.isNotEmpty(jobExecution.getExceptionInfo())) {
/* 38 */           log.error(jobExecution.getExceptionInfo().toString());
/*    */         }
/* 40 */         jobListener.onJobError(jobExecution.getThrowable(), jobExecution);
/*    */       } else {
/* 42 */         jobListener.afterJob(jobExecution);
/*    */       } 
/* 44 */     } catch (Exception e) {
/* 45 */       log.warn("job[" + jobExecution.getName() + "]监听执行异常:", e);
/*    */     } 
/* 47 */     return jobExecution;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\job\JobRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */