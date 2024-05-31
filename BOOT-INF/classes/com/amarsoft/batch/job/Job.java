/*     */ package BOOT-INF.classes.com.amarsoft.batch.job;
/*     */ 
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.batch.JobListener;
/*     */ import com.amarsoft.batch.exception.JobStopException;
/*     */ import com.amarsoft.batch.job.JobExecution;
/*     */ import com.amarsoft.batch.step.Step;
/*     */ import com.amarsoft.batch.step.StepExecution;
/*     */ import java.util.Collection;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.core.task.TaskExecutor;
/*     */ 
/*     */ 
/*     */ public class Job
/*     */ {
/*     */   public void setName(String name) {
/*  21 */     this.name = name; } public void setStepList(List<Collection<Step>> stepList) { this.stepList = stepList; } public void setJobListener(JobListener jobListener) { this.jobListener = jobListener; } public void setTaskExecutor(TaskExecutor taskExecutor) { this.taskExecutor = taskExecutor; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.batch.job.Job)) return false;  com.amarsoft.batch.job.Job other = (com.amarsoft.batch.job.Job)o; if (!other.canEqual(this)) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object<Collection<Step>> this$stepList = (Object<Collection<Step>>)getStepList(), other$stepList = (Object<Collection<Step>>)other.getStepList(); if ((this$stepList == null) ? (other$stepList != null) : !this$stepList.equals(other$stepList)) return false;  Object this$jobListener = getJobListener(), other$jobListener = other.getJobListener(); if ((this$jobListener == null) ? (other$jobListener != null) : !this$jobListener.equals(other$jobListener)) return false;  Object this$taskExecutor = getTaskExecutor(), other$taskExecutor = other.getTaskExecutor(); return !((this$taskExecutor == null) ? (other$taskExecutor != null) : !this$taskExecutor.equals(other$taskExecutor)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.batch.job.Job; } public int hashCode() { int PRIME = 59; result = 1; Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object<Collection<Step>> $stepList = (Object<Collection<Step>>)getStepList(); result = result * 59 + (($stepList == null) ? 43 : $stepList.hashCode()); Object $jobListener = getJobListener(); result = result * 59 + (($jobListener == null) ? 43 : $jobListener.hashCode()); Object $taskExecutor = getTaskExecutor(); return result * 59 + (($taskExecutor == null) ? 43 : $taskExecutor.hashCode()); } public String toString() { return "Job(name=" + getName() + ", stepList=" + getStepList() + ", jobListener=" + getJobListener() + ", taskExecutor=" + getTaskExecutor() + ")"; }
/*  22 */    private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.job.Job.class);
/*     */   private String name; private List<Collection<Step>> stepList; private JobListener jobListener; private TaskExecutor taskExecutor;
/*     */   
/*  25 */   public String getName() { return this.name; }
/*  26 */   public List<Collection<Step>> getStepList() { return this.stepList; }
/*  27 */   public JobListener getJobListener() { return this.jobListener; } public TaskExecutor getTaskExecutor() {
/*  28 */     return this.taskExecutor;
/*     */   }
/*     */   public void execute(JobExecution jobExecution) throws Exception {
/*  31 */     for (Iterator<Collection<Step>> iterator = this.stepList.iterator(); iterator.hasNext(); ) { Collection<Step> steps = iterator.next();
/*  32 */       if (steps == null || steps.size() == 0) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  37 */       if (this.taskExecutor == null || steps.size() == 1) {
/*  38 */         for (Step step : steps) {
/*  39 */           executeStep(step, jobExecution);
/*     */         }
/*     */       } else {
/*     */         
/*     */         try {
/*  44 */           CountDownLatch latch = new CountDownLatch(steps.size());
/*  45 */           for (Step step : steps) {
/*  46 */             this.taskExecutor.execute(() -> {
/*     */                   try {
/*     */                     executeStep(step, jobExecution);
/*     */                   } finally {
/*     */                     latch.countDown();
/*     */                   } 
/*     */                 });
/*     */           } 
/*  54 */           latch.await();
/*  55 */         } catch (InterruptedException e) {
/*  56 */           throw e;
/*     */         } 
/*     */       } 
/*     */       
/*  60 */       boolean isStopException = true;
/*  61 */       for (Step step : steps) {
/*  62 */         if (StrUtil.equals("0", step.getStepExecution().getStatus())) {
/*  63 */           jobExecution.setStatus("0");
/*  64 */           if (step.getStepExecution().getThrowable() == null || 
/*  65 */             !(step.getStepExecution().getThrowable() instanceof JobStopException)) {
/*  66 */             isStopException = false;
/*     */             
/*  68 */             jobExecution.addStepExceptionInfo(step.getStepExecution().getName(), step.getStepExecution().getThrowable());
/*     */           } 
/*     */         } 
/*     */       } 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*  76 */       if (StrUtil.equals("0", jobExecution.getStatus())) {
/*  77 */         if (isStopException) {
/*  78 */           jobExecution.initExceptionInfo("作业被停止");
/*  79 */           jobExecution.setThrowable((Throwable)new JobStopException());
/*     */         } 
/*     */         break;
/*     */       }  }
/*     */   
/*     */   }
/*     */   
/*     */   private StepExecution executeStep(Step step, JobExecution jobExecution) {
/*  87 */     StepExecution stepExecution = new StepExecution();
/*  88 */     stepExecution.setName(step.getName());
/*  89 */     stepExecution.setJobExecution(jobExecution);
/*  90 */     step.setStepExecution(stepExecution);
/*  91 */     jobExecution.putStepExecution(stepExecution);
/*     */ 
/*     */     
/*     */     try {
/*  95 */       step.execute(stepExecution);
/*  96 */       stepExecution.setStatus("1");
/*  97 */     } catch (Exception e) {
/*  98 */       log.error("step[" + step.getName() + "]执行异常：", e);
/*  99 */       stepExecution.setStatus("0");
/* 100 */       stepExecution.setThrowable(e);
/*     */     } 
/* 102 */     return stepExecution;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\job\Job.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */