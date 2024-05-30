/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Pattern;
/*    */ 
/*    */ public class MonitorRequest {
/*    */   @NotNull(message = "任务ID不能为空")
/*    */   private String taskId;
/*    */   @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "结果号输入异常")
/*    */   @NotNull(message = "结果号不能为空")
/*    */   private String resultNo;
/*    */   
/* 13 */   public void setTaskId(String taskId) { this.taskId = taskId; } public void setResultNo(String resultNo) { this.resultNo = resultNo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MonitorRequest)) return false;  com.amarsoft.rwa.engine.entity.MonitorRequest other = (com.amarsoft.rwa.engine.entity.MonitorRequest)o; if (!other.canEqual(this)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); return !((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MonitorRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $resultNo = getResultNo(); return result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); } public String toString() { return "MonitorRequest(taskId=" + getTaskId() + ", resultNo=" + getResultNo() + ")"; }
/*    */ 
/*    */   
/*    */   public String getTaskId() {
/* 17 */     return this.taskId;
/*    */   }
/*    */   
/*    */   public String getResultNo() {
/* 21 */     return this.resultNo;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MonitorRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */