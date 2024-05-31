/*    */ package BOOT-INF.classes.com.amarsoft.batch.job;
/*    */ 
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import com.amarsoft.batch.JobListener;
/*    */ import com.amarsoft.batch.job.Job;
/*    */ import com.amarsoft.batch.listener.NullJobListener;
/*    */ import com.amarsoft.batch.step.Step;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Collection;
/*    */ import org.springframework.core.task.TaskExecutor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JobBuilder
/*    */ {
/*    */   private Job job;
/*    */   
/*    */   public JobBuilder() {
/* 22 */     this.job = new Job();
/* 23 */     this.job.setStepList(new ArrayList());
/*    */   }
/*    */   
/*    */   public Job build() {
/* 27 */     if (this.job.getStepList().size() == 0) {
/* 28 */       throw new RuntimeException("job building exception: no step");
/*    */     }
/* 30 */     if (this.job.getJobListener() == null) {
/* 31 */       this.job.setJobListener((JobListener)new NullJobListener());
/*    */     }
/* 33 */     return this.job;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder name(String name) {
/* 37 */     this.job.setName(name);
/* 38 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder taskExecutor(TaskExecutor taskExecutor) {
/* 42 */     this.job.setTaskExecutor(taskExecutor);
/* 43 */     return this;
/*    */   }
/*    */   
/*    */   private com.amarsoft.batch.job.JobBuilder step(Step step) {
/* 47 */     this.job.getStepList().add(CollUtil.toList((Object[])new Step[] { step }));
/* 48 */     return this;
/*    */   }
/*    */   
/*    */   private com.amarsoft.batch.job.JobBuilder step(Collection<Step> steps) {
/* 52 */     this.job.getStepList().add(steps);
/* 53 */     return this;
/*    */   }
/*    */   
/*    */   private com.amarsoft.batch.job.JobBuilder step(Step[] steps) {
/* 57 */     this.job.getStepList().add(CollUtil.toList((Object[])steps));
/* 58 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder next(Step step) {
/* 62 */     return step(step);
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder next(Collection<Step> steps) {
/* 66 */     return step(steps);
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder next(Step... steps) {
/* 70 */     return step(steps);
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.job.JobBuilder listener(JobListener jobListener) {
/* 74 */     this.job.setJobListener(jobListener);
/* 75 */     return this;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\job\JobBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */