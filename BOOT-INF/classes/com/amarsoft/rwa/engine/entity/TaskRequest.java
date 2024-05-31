/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ public class TaskRequest { @Pattern(regexp = "[0-9A-Za-z]{1,}", message = "数据批次号输入异常")
/*    */   @NotNull(message = "数据批次号不能为空")
/*    */   private String dataBatchNo; @NotNull(message = "计算方法不能为空")
/*    */   private String approach;
/*    */   @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "结果号输入异常")
/*    */   @NotNull(message = "结果号不能为空")
/*    */   private String resultNo;
/*    */   @Pattern(regexp = "[0-1]{1}", message = "是否分组输入异常")
/*    */   private String isGroup;
/*    */   @Pattern(regexp = "[0-1]{1}", message = "是否重跑输入异常")
/*    */   private String isRerun;
/*    */   
/* 14 */   public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } private Date dataDate; private String schemeId; private String taskType; private String taskId; private TaskInfoDto taskInfo; public void setApproach(String approach) { this.approach = approach; } public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setIsGroup(String isGroup) { this.isGroup = isGroup; } public void setIsRerun(String isRerun) { this.isRerun = isRerun; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setTaskType(String taskType) { this.taskType = taskType; } public void setTaskId(String taskId) { this.taskId = taskId; } public void setTaskInfo(TaskInfoDto taskInfo) { this.taskInfo = taskInfo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskRequest)) return false;  com.amarsoft.rwa.engine.entity.TaskRequest other = (com.amarsoft.rwa.engine.entity.TaskRequest)o; if (!other.canEqual(this)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$isGroup = getIsGroup(), other$isGroup = other.getIsGroup(); if ((this$isGroup == null) ? (other$isGroup != null) : !this$isGroup.equals(other$isGroup)) return false;  Object this$isRerun = getIsRerun(), other$isRerun = other.getIsRerun(); if ((this$isRerun == null) ? (other$isRerun != null) : !this$isRerun.equals(other$isRerun)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$taskInfo = getTaskInfo(), other$taskInfo = other.getTaskInfo(); return !((this$taskInfo == null) ? (other$taskInfo != null) : !this$taskInfo.equals(other$taskInfo)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $isGroup = getIsGroup(); result = result * 59 + (($isGroup == null) ? 43 : $isGroup.hashCode()); Object $isRerun = getIsRerun(); result = result * 59 + (($isRerun == null) ? 43 : $isRerun.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $taskInfo = getTaskInfo(); return result * 59 + (($taskInfo == null) ? 43 : $taskInfo.hashCode()); } public String toString() { return "TaskRequest(dataBatchNo=" + getDataBatchNo() + ", approach=" + getApproach() + ", resultNo=" + getResultNo() + ", isGroup=" + getIsGroup() + ", isRerun=" + getIsRerun() + ", dataDate=" + getDataDate() + ", schemeId=" + getSchemeId() + ", taskType=" + getTaskType() + ", taskId=" + getTaskId() + ", taskInfo=" + getTaskInfo() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDataBatchNo() {
/* 19 */     return this.dataBatchNo;
/*    */   } public String getApproach() {
/* 21 */     return this.approach;
/*    */   }
/*    */   public String getResultNo() {
/* 24 */     return this.resultNo;
/*    */   } public String getIsGroup() {
/* 26 */     return this.isGroup;
/*    */   }
/* 28 */   public String getIsRerun() { return this.isRerun; }
/* 29 */   public Date getDataDate() { return this.dataDate; }
/* 30 */   public String getSchemeId() { return this.schemeId; }
/* 31 */   public String getTaskType() { return this.taskType; }
/* 32 */   public String getTaskId() { return this.taskId; } public TaskInfoDto getTaskInfo() {
/* 33 */     return this.taskInfo;
/*    */   } }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */