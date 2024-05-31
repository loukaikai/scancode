/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("CS_MONITOR_LOG")
/*    */ public class MonitorLogDo { @TableId("LOG_NO")
/*    */   private Long logNo; @TableField("MONITOR_TASK_ID")
/*    */   private String monitorTaskId; @TableField("RESULT_NO")
/*    */   private String resultNo;
/*    */   @TableField("DATA_DATE")
/*    */   private Date dataDate;
/*    */   @TableField("DATA_BATCH_NO")
/*    */   private String dataBatchNo;
/*    */   
/* 12 */   public void setLogNo(Long logNo) { this.logNo = logNo; } @TableField("START_TIME") private Timestamp startTime; @TableField("END_TIME") private Timestamp endTime; @TableField("CALC_TIME") private BigDecimal calcTime; @TableField("TASK_STATUS") private String taskStatus; @TableField("EXCEPTION_INFO") private String exceptionInfo; public void setMonitorTaskId(String monitorTaskId) { this.monitorTaskId = monitorTaskId; } public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setStartTime(Timestamp startTime) { this.startTime = startTime; } public void setEndTime(Timestamp endTime) { this.endTime = endTime; } public void setCalcTime(BigDecimal calcTime) { this.calcTime = calcTime; } public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; } public void setExceptionInfo(String exceptionInfo) { this.exceptionInfo = exceptionInfo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MonitorLogDo)) return false;  com.amarsoft.rwa.engine.entity.MonitorLogDo other = (com.amarsoft.rwa.engine.entity.MonitorLogDo)o; if (!other.canEqual(this)) return false;  Object this$logNo = getLogNo(), other$logNo = other.getLogNo(); if ((this$logNo == null) ? (other$logNo != null) : !this$logNo.equals(other$logNo)) return false;  Object this$monitorTaskId = getMonitorTaskId(), other$monitorTaskId = other.getMonitorTaskId(); if ((this$monitorTaskId == null) ? (other$monitorTaskId != null) : !this$monitorTaskId.equals(other$monitorTaskId)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$startTime = getStartTime(), other$startTime = other.getStartTime(); if ((this$startTime == null) ? (other$startTime != null) : !this$startTime.equals(other$startTime)) return false;  Object this$endTime = getEndTime(), other$endTime = other.getEndTime(); if ((this$endTime == null) ? (other$endTime != null) : !this$endTime.equals(other$endTime)) return false;  Object this$calcTime = getCalcTime(), other$calcTime = other.getCalcTime(); if ((this$calcTime == null) ? (other$calcTime != null) : !this$calcTime.equals(other$calcTime)) return false;  Object this$taskStatus = getTaskStatus(), other$taskStatus = other.getTaskStatus(); if ((this$taskStatus == null) ? (other$taskStatus != null) : !this$taskStatus.equals(other$taskStatus)) return false;  Object this$exceptionInfo = getExceptionInfo(), other$exceptionInfo = other.getExceptionInfo(); return !((this$exceptionInfo == null) ? (other$exceptionInfo != null) : !this$exceptionInfo.equals(other$exceptionInfo)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MonitorLogDo; } public int hashCode() { int PRIME = 59; result = 1; Object $logNo = getLogNo(); result = result * 59 + (($logNo == null) ? 43 : $logNo.hashCode()); Object $monitorTaskId = getMonitorTaskId(); result = result * 59 + (($monitorTaskId == null) ? 43 : $monitorTaskId.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $startTime = getStartTime(); result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode()); Object $endTime = getEndTime(); result = result * 59 + (($endTime == null) ? 43 : $endTime.hashCode()); Object $calcTime = getCalcTime(); result = result * 59 + (($calcTime == null) ? 43 : $calcTime.hashCode()); Object $taskStatus = getTaskStatus(); result = result * 59 + (($taskStatus == null) ? 43 : $taskStatus.hashCode()); Object $exceptionInfo = getExceptionInfo(); return result * 59 + (($exceptionInfo == null) ? 43 : $exceptionInfo.hashCode()); } public String toString() { return "MonitorLogDo(logNo=" + getLogNo() + ", monitorTaskId=" + getMonitorTaskId() + ", resultNo=" + getResultNo() + ", dataDate=" + getDataDate() + ", dataBatchNo=" + getDataBatchNo() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", calcTime=" + getCalcTime() + ", taskStatus=" + getTaskStatus() + ", exceptionInfo=" + getExceptionInfo() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getLogNo() {
/* 17 */     return this.logNo;
/*    */   } public String getMonitorTaskId() {
/* 19 */     return this.monitorTaskId;
/*    */   } public String getResultNo() {
/* 21 */     return this.resultNo;
/*    */   } public Date getDataDate() {
/* 23 */     return this.dataDate;
/*    */   } public String getDataBatchNo() {
/* 25 */     return this.dataBatchNo;
/*    */   } public Timestamp getStartTime() {
/* 27 */     return this.startTime;
/*    */   } public Timestamp getEndTime() {
/* 29 */     return this.endTime;
/*    */   } public BigDecimal getCalcTime() {
/* 31 */     return this.calcTime;
/*    */   } public String getTaskStatus() {
/* 33 */     return this.taskStatus;
/*    */   } public String getExceptionInfo() {
/* 35 */     return this.exceptionInfo;
/*    */   } }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MonitorLogDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */