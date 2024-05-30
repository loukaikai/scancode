/*    */ package BOOT-INF.classes.com.amarsoft.batch.job;
/*    */ 
/*    */ import com.amarsoft.batch.step.StepExecution;
/*    */ import java.util.Date;
/*    */ import java.util.Map;
/*    */ 
/*    */ public class JobExecution {
/*    */   private long id;
/*    */   private String name;
/*    */   private String status;
/*    */   private Map<String, Object> jobParameter;
/*    */   private Date startTime;
/*    */   private Date createTime;
/*    */   private Date endTime;
/*    */   
/* 16 */   public void setId(long id) { this.id = id; } public void setName(String name) { this.name = name; } public void setStatus(String status) { this.status = status; } public void setJobParameter(Map<String, Object> jobParameter) { this.jobParameter = jobParameter; } public void setStartTime(Date startTime) { this.startTime = startTime; } public void setCreateTime(Date createTime) { this.createTime = createTime; } public void setEndTime(Date endTime) { this.endTime = endTime; } public void setStepExecutionMap(Map<String, StepExecution> stepExecutionMap) { this.stepExecutionMap = stepExecutionMap; } public void setThrowable(Throwable throwable) { this.throwable = throwable; } public void setExceptionInfo(StringBuilder exceptionInfo) { this.exceptionInfo = exceptionInfo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.batch.job.JobExecution)) return false;  com.amarsoft.batch.job.JobExecution other = (com.amarsoft.batch.job.JobExecution)o; if (!other.canEqual(this)) return false;  if (getId() != other.getId()) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object<String, Object> this$jobParameter = (Object<String, Object>)getJobParameter(), other$jobParameter = (Object<String, Object>)other.getJobParameter(); if ((this$jobParameter == null) ? (other$jobParameter != null) : !this$jobParameter.equals(other$jobParameter)) return false;  Object this$startTime = getStartTime(), other$startTime = other.getStartTime(); if ((this$startTime == null) ? (other$startTime != null) : !this$startTime.equals(other$startTime)) return false;  Object this$createTime = getCreateTime(), other$createTime = other.getCreateTime(); if ((this$createTime == null) ? (other$createTime != null) : !this$createTime.equals(other$createTime)) return false;  Object this$endTime = getEndTime(), other$endTime = other.getEndTime(); if ((this$endTime == null) ? (other$endTime != null) : !this$endTime.equals(other$endTime)) return false;  Object<String, StepExecution> this$stepExecutionMap = (Object<String, StepExecution>)getStepExecutionMap(), other$stepExecutionMap = (Object<String, StepExecution>)other.getStepExecutionMap(); if ((this$stepExecutionMap == null) ? (other$stepExecutionMap != null) : !this$stepExecutionMap.equals(other$stepExecutionMap)) return false;  Object this$throwable = getThrowable(), other$throwable = other.getThrowable(); if ((this$throwable == null) ? (other$throwable != null) : !this$throwable.equals(other$throwable)) return false;  Object this$exceptionInfo = getExceptionInfo(), other$exceptionInfo = other.getExceptionInfo(); return !((this$exceptionInfo == null) ? (other$exceptionInfo != null) : !this$exceptionInfo.equals(other$exceptionInfo)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.batch.job.JobExecution; } public int hashCode() { int PRIME = 59; result = 1; long $id = getId(); result = result * 59 + (int)($id >>> 32L ^ $id); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object<String, Object> $jobParameter = (Object<String, Object>)getJobParameter(); result = result * 59 + (($jobParameter == null) ? 43 : $jobParameter.hashCode()); Object $startTime = getStartTime(); result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode()); Object $createTime = getCreateTime(); result = result * 59 + (($createTime == null) ? 43 : $createTime.hashCode()); Object $endTime = getEndTime(); result = result * 59 + (($endTime == null) ? 43 : $endTime.hashCode()); Object<String, StepExecution> $stepExecutionMap = (Object<String, StepExecution>)getStepExecutionMap(); result = result * 59 + (($stepExecutionMap == null) ? 43 : $stepExecutionMap.hashCode()); Object $throwable = getThrowable(); result = result * 59 + (($throwable == null) ? 43 : $throwable.hashCode()); Object $exceptionInfo = getExceptionInfo(); return result * 59 + (($exceptionInfo == null) ? 43 : $exceptionInfo.hashCode()); } public String toString() { return "JobExecution(id=" + getId() + ", name=" + getName() + ", status=" + getStatus() + ", jobParameter=" + getJobParameter() + ", startTime=" + getStartTime() + ", createTime=" + getCreateTime() + ", endTime=" + getEndTime() + ", stepExecutionMap=" + getStepExecutionMap() + ", throwable=" + getThrowable() + ", exceptionInfo=" + getExceptionInfo() + ")"; }
/*    */ 
/*    */   
/* 19 */   public long getId() { return this.id; }
/* 20 */   public String getName() { return this.name; }
/* 21 */   public String getStatus() { return this.status; }
/* 22 */   public Map<String, Object> getJobParameter() { return this.jobParameter; }
/* 23 */   public Date getStartTime() { return this.startTime; }
/* 24 */   public Date getCreateTime() { return this.createTime; } public Date getEndTime() {
/* 25 */     return this.endTime;
/* 26 */   } private Throwable throwable; private Map<String, StepExecution> stepExecutionMap = new ConcurrentHashMap<>(); private StringBuilder exceptionInfo; public Map<String, StepExecution> getStepExecutionMap() { return this.stepExecutionMap; }
/* 27 */   public Throwable getThrowable() { return this.throwable; } public StringBuilder getExceptionInfo() {
/* 28 */     return this.exceptionInfo;
/*    */   }
/*    */   public void putStepExecution(StepExecution stepExecution) {
/* 31 */     if (this.stepExecutionMap.containsKey(stepExecution.getName())) {
/* 32 */       throw new RuntimeException("存在相同的step name， 请检查代码配置关系！");
/*    */     }
/* 34 */     this.stepExecutionMap.put(stepExecution.getName(), stepExecution);
/*    */   }
/*    */   
/*    */   public StepExecution getStepExecution(String name) {
/* 38 */     return this.stepExecutionMap.get(name);
/*    */   }
/*    */   
/*    */   public void initExceptionInfo(String info) {
/* 42 */     if (this.exceptionInfo == null) {
/* 43 */       this.exceptionInfo = new StringBuilder();
/* 44 */       this.exceptionInfo.append("job[").append(this.name).append("]异常:");
/* 45 */       if (StrUtil.isNotEmpty(info)) {
/* 46 */         this.exceptionInfo.append(info);
/*    */       }
/* 48 */       this.exceptionInfo.append("\n");
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addStepExceptionInfo(String name, Throwable t) {
/* 53 */     initExceptionInfo(null);
/* 54 */     this.exceptionInfo.append("step[").append(name).append("]");
/* 55 */     if (t == null) {
/* 56 */       this.exceptionInfo.append("执行异常！\n");
/*    */     } else {
/* 58 */       this.exceptionInfo.append("执行异常: ").append(t.getMessage()).append("\n");
/*    */     } 
/*    */   }
/*    */   
/*    */   public void addJobExceptionInfo(String name, Throwable t) {
/* 63 */     initExceptionInfo(null);
/* 64 */     this.exceptionInfo.append("job[").append(name).append("]");
/* 65 */     if (t == null) {
/* 66 */       this.exceptionInfo.append("执行异常！\n");
/*    */     } else {
/* 68 */       this.exceptionInfo.append("执行异常: ").append(t.getMessage()).append("\n");
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\job\JobExecution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */