/*    */ package BOOT-INF.classes.com.amarsoft.batch.step;
/*    */ 
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ public class StepExecution {
/*    */   private String name;
/*    */   private String status;
/*    */   private JobExecution jobExecution;
/*    */   private int readCount;
/*    */   private int writeCount;
/*    */   private int commitCount;
/*    */   private int rollbackCount;
/*    */   
/* 14 */   public void setName(String name) { this.name = name; } private int readSkipCount; private int processSkipCount; private int writeSkipCount; private LocalDateTime startTime; private LocalDateTime endTime; private ExecutionContext executionContext; private Throwable throwable; public void setStatus(String status) { this.status = status; } public void setJobExecution(JobExecution jobExecution) { this.jobExecution = jobExecution; } public void setReadCount(int readCount) { this.readCount = readCount; } public void setWriteCount(int writeCount) { this.writeCount = writeCount; } public void setCommitCount(int commitCount) { this.commitCount = commitCount; } public void setRollbackCount(int rollbackCount) { this.rollbackCount = rollbackCount; } public void setReadSkipCount(int readSkipCount) { this.readSkipCount = readSkipCount; } public void setProcessSkipCount(int processSkipCount) { this.processSkipCount = processSkipCount; } public void setWriteSkipCount(int writeSkipCount) { this.writeSkipCount = writeSkipCount; } public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; } public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; } public void setExecutionContext(ExecutionContext executionContext) { this.executionContext = executionContext; } public void setThrowable(Throwable throwable) { this.throwable = throwable; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.batch.step.StepExecution)) return false;  com.amarsoft.batch.step.StepExecution other = (com.amarsoft.batch.step.StepExecution)o; if (!other.canEqual(this)) return false;  if (getReadCount() != other.getReadCount()) return false;  if (getWriteCount() != other.getWriteCount()) return false;  if (getCommitCount() != other.getCommitCount()) return false;  if (getRollbackCount() != other.getRollbackCount()) return false;  if (getReadSkipCount() != other.getReadSkipCount()) return false;  if (getProcessSkipCount() != other.getProcessSkipCount()) return false;  if (getWriteSkipCount() != other.getWriteSkipCount()) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object this$jobExecution = getJobExecution(), other$jobExecution = other.getJobExecution(); if ((this$jobExecution == null) ? (other$jobExecution != null) : !this$jobExecution.equals(other$jobExecution)) return false;  Object this$startTime = getStartTime(), other$startTime = other.getStartTime(); if ((this$startTime == null) ? (other$startTime != null) : !this$startTime.equals(other$startTime)) return false;  Object this$endTime = getEndTime(), other$endTime = other.getEndTime(); if ((this$endTime == null) ? (other$endTime != null) : !this$endTime.equals(other$endTime)) return false;  Object this$executionContext = getExecutionContext(), other$executionContext = other.getExecutionContext(); if ((this$executionContext == null) ? (other$executionContext != null) : !this$executionContext.equals(other$executionContext)) return false;  Object this$throwable = getThrowable(), other$throwable = other.getThrowable(); return !((this$throwable == null) ? (other$throwable != null) : !this$throwable.equals(other$throwable)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.batch.step.StepExecution; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getReadCount(); result = result * 59 + getWriteCount(); result = result * 59 + getCommitCount(); result = result * 59 + getRollbackCount(); result = result * 59 + getReadSkipCount(); result = result * 59 + getProcessSkipCount(); result = result * 59 + getWriteSkipCount(); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object $jobExecution = getJobExecution(); result = result * 59 + (($jobExecution == null) ? 43 : $jobExecution.hashCode()); Object $startTime = getStartTime(); result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode()); Object $endTime = getEndTime(); result = result * 59 + (($endTime == null) ? 43 : $endTime.hashCode()); Object $executionContext = getExecutionContext(); result = result * 59 + (($executionContext == null) ? 43 : $executionContext.hashCode()); Object $throwable = getThrowable(); return result * 59 + (($throwable == null) ? 43 : $throwable.hashCode()); } public String toString() { return "StepExecution(name=" + getName() + ", status=" + getStatus() + ", jobExecution=" + getJobExecution() + ", readCount=" + getReadCount() + ", writeCount=" + getWriteCount() + ", commitCount=" + getCommitCount() + ", rollbackCount=" + getRollbackCount() + ", readSkipCount=" + getReadSkipCount() + ", processSkipCount=" + getProcessSkipCount() + ", writeSkipCount=" + getWriteSkipCount() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", executionContext=" + getExecutionContext() + ", throwable=" + getThrowable() + ")"; }
/*    */ 
/*    */   
/* 17 */   public String getName() { return this.name; }
/* 18 */   public String getStatus() { return this.status; }
/* 19 */   public JobExecution getJobExecution() { return this.jobExecution; }
/* 20 */   public int getReadCount() { return this.readCount; }
/* 21 */   public int getWriteCount() { return this.writeCount; }
/* 22 */   public int getCommitCount() { return this.commitCount; }
/* 23 */   public int getRollbackCount() { return this.rollbackCount; }
/* 24 */   public int getReadSkipCount() { return this.readSkipCount; }
/* 25 */   public int getProcessSkipCount() { return this.processSkipCount; }
/* 26 */   public int getWriteSkipCount() { return this.writeSkipCount; }
/* 27 */   public LocalDateTime getStartTime() { return this.startTime; }
/* 28 */   public LocalDateTime getEndTime() { return this.endTime; }
/* 29 */   public ExecutionContext getExecutionContext() { return this.executionContext; } public Throwable getThrowable() {
/* 30 */     return this.throwable;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\step\StepExecution.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */