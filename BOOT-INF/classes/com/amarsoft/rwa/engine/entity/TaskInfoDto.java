/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*    */ import com.amarsoft.rwa.engine.constant.TaskType;
/*    */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*    */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*    */ 
/*    */ public class TaskInfoDto {
/*    */   private TaskType taskType;
/*    */   private String resultNo;
/*    */   private String dataBatchNo;
/*    */   private Date dataDate;
/*    */   private String approach;
/*    */   private String isGroup;
/*    */   
/* 16 */   public void setTaskType(TaskType taskType) { this.taskType = taskType; } private String schemeId; private String taskId; private TaskConfigDo taskConfigDo; private Long logNo; private TaskLogDo taskLogDo; private CalculateStatus status; public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setApproach(String approach) { this.approach = approach; } public void setIsGroup(String isGroup) { this.isGroup = isGroup; } public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setTaskId(String taskId) { this.taskId = taskId; } public void setTaskConfigDo(TaskConfigDo taskConfigDo) { this.taskConfigDo = taskConfigDo; } public void setLogNo(Long logNo) { this.logNo = logNo; } public void setTaskLogDo(TaskLogDo taskLogDo) { this.taskLogDo = taskLogDo; } public void setStatus(CalculateStatus status) { this.status = status; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskInfoDto)) return false;  com.amarsoft.rwa.engine.entity.TaskInfoDto other = (com.amarsoft.rwa.engine.entity.TaskInfoDto)o; if (!other.canEqual(this)) return false;  Object this$logNo = getLogNo(), other$logNo = other.getLogNo(); if ((this$logNo == null) ? (other$logNo != null) : !this$logNo.equals(other$logNo)) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$isGroup = getIsGroup(), other$isGroup = other.getIsGroup(); if ((this$isGroup == null) ? (other$isGroup != null) : !this$isGroup.equals(other$isGroup)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$taskConfigDo = getTaskConfigDo(), other$taskConfigDo = other.getTaskConfigDo(); if ((this$taskConfigDo == null) ? (other$taskConfigDo != null) : !this$taskConfigDo.equals(other$taskConfigDo)) return false;  Object this$taskLogDo = getTaskLogDo(), other$taskLogDo = other.getTaskLogDo(); if ((this$taskLogDo == null) ? (other$taskLogDo != null) : !this$taskLogDo.equals(other$taskLogDo)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); return !((this$status == null) ? (other$status != null) : !this$status.equals(other$status)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskInfoDto; } public int hashCode() { int PRIME = 59; result = 1; Object $logNo = getLogNo(); result = result * 59 + (($logNo == null) ? 43 : $logNo.hashCode()); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $isGroup = getIsGroup(); result = result * 59 + (($isGroup == null) ? 43 : $isGroup.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $taskConfigDo = getTaskConfigDo(); result = result * 59 + (($taskConfigDo == null) ? 43 : $taskConfigDo.hashCode()); Object $taskLogDo = getTaskLogDo(); result = result * 59 + (($taskLogDo == null) ? 43 : $taskLogDo.hashCode()); Object $status = getStatus(); return result * 59 + (($status == null) ? 43 : $status.hashCode()); } public String toString() { return "TaskInfoDto(taskType=" + getTaskType() + ", resultNo=" + getResultNo() + ", dataBatchNo=" + getDataBatchNo() + ", dataDate=" + getDataDate() + ", approach=" + getApproach() + ", isGroup=" + getIsGroup() + ", schemeId=" + getSchemeId() + ", taskId=" + getTaskId() + ", taskConfigDo=" + getTaskConfigDo() + ", logNo=" + getLogNo() + ", taskLogDo=" + getTaskLogDo() + ", status=" + getStatus() + ")"; } public TaskInfoDto(TaskType taskType, String resultNo, String dataBatchNo, Date dataDate, String approach, String isGroup, String schemeId, String taskId, TaskConfigDo taskConfigDo, Long logNo, TaskLogDo taskLogDo, CalculateStatus status) {
/* 17 */     this.taskType = taskType; this.resultNo = resultNo; this.dataBatchNo = dataBatchNo; this.dataDate = dataDate; this.approach = approach; this.isGroup = isGroup; this.schemeId = schemeId; this.taskId = taskId; this.taskConfigDo = taskConfigDo; this.logNo = logNo; this.taskLogDo = taskLogDo; this.status = status;
/*    */   }
/*    */   public TaskInfoDto() {}
/*    */   
/* 21 */   public TaskType getTaskType() { return this.taskType; }
/* 22 */   public String getResultNo() { return this.resultNo; }
/* 23 */   public String getDataBatchNo() { return this.dataBatchNo; }
/* 24 */   public Date getDataDate() { return this.dataDate; }
/* 25 */   public String getApproach() { return this.approach; }
/* 26 */   public String getIsGroup() { return this.isGroup; }
/* 27 */   public String getSchemeId() { return this.schemeId; }
/* 28 */   public String getTaskId() { return this.taskId; }
/* 29 */   public TaskConfigDo getTaskConfigDo() { return this.taskConfigDo; }
/* 30 */   public Long getLogNo() { return this.logNo; }
/* 31 */   public TaskLogDo getTaskLogDo() { return this.taskLogDo; } public CalculateStatus getStatus() {
/* 32 */     return this.status;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskInfoDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */