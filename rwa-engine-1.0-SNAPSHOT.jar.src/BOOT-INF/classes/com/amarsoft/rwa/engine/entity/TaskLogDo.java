/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EL_TASK")
/*    */ public class TaskLogDo { @TableId("LOG_NO")
/*    */   private Long logNo; @TableField("RESULT_NO")
/*    */   private String resultNo; @TableField("DATA_DATE")
/*    */   private Date dataDate; @TableField("DATA_BATCH_NO")
/*    */   private String dataBatchNo; @TableField("TASK_TYPE")
/*    */   private String taskType; @TableField("TASK_ID")
/*    */   private String taskId; @TableField("SCHEME_ID")
/*    */   private String schemeId; @TableField("START_TIME")
/*    */   private Timestamp startTime; @TableField("END_TIME")
/*    */   private Timestamp endTime; @TableField("CALC_TIME")
/*    */   private BigDecimal calcTime; @TableField("TASK_INIT_TIME")
/* 14 */   private Timestamp taskInitTime; public void setLogNo(Long logNo) { this.logNo = logNo; } @TableField("JOB_CALC_TIME") private Timestamp jobCalcTime; @TableField("ST_WRITE_TIME") private Timestamp stWriteTime; @TableField("TASK_STATUS") private String taskStatus; @TableField("EXCEPTION_INFO") private String exceptionInfo; @TableField(exist = false) private List<JobLogDo> jobLogList; @TableField(exist = false) private int calculateNum; @TableField(exist = false) private int normalCalcCnt; @TableField(exist = false) private int normalSkipCnt; @TableField(exist = false) private int exceptionSkipCnt; @TableField(exist = false) private int totalCalcCnt; @TableField(exist = false) private BigDecimal progress; @TableField(exist = false) private Map<String, Integer> jobStatusMap; public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setTaskType(String taskType) { this.taskType = taskType; } public void setTaskId(String taskId) { this.taskId = taskId; } public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setStartTime(Timestamp startTime) { this.startTime = startTime; } public void setEndTime(Timestamp endTime) { this.endTime = endTime; } public void setCalcTime(BigDecimal calcTime) { this.calcTime = calcTime; } public void setTaskInitTime(Timestamp taskInitTime) { this.taskInitTime = taskInitTime; } public void setJobCalcTime(Timestamp jobCalcTime) { this.jobCalcTime = jobCalcTime; } public void setStWriteTime(Timestamp stWriteTime) { this.stWriteTime = stWriteTime; } public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; } public void setExceptionInfo(String exceptionInfo) { this.exceptionInfo = exceptionInfo; } public void setJobLogList(List<JobLogDo> jobLogList) { this.jobLogList = jobLogList; } public void setCalculateNum(int calculateNum) { this.calculateNum = calculateNum; } public void setNormalCalcCnt(int normalCalcCnt) { this.normalCalcCnt = normalCalcCnt; } public void setNormalSkipCnt(int normalSkipCnt) { this.normalSkipCnt = normalSkipCnt; } public void setExceptionSkipCnt(int exceptionSkipCnt) { this.exceptionSkipCnt = exceptionSkipCnt; } public void setTotalCalcCnt(int totalCalcCnt) { this.totalCalcCnt = totalCalcCnt; } public void setProgress(BigDecimal progress) { this.progress = progress; } public void setJobStatusMap(Map<String, Integer> jobStatusMap) { this.jobStatusMap = jobStatusMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskLogDo)) return false;  com.amarsoft.rwa.engine.entity.TaskLogDo other = (com.amarsoft.rwa.engine.entity.TaskLogDo)o; if (!other.canEqual(this)) return false;  if (getCalculateNum() != other.getCalculateNum()) return false;  if (getNormalCalcCnt() != other.getNormalCalcCnt()) return false;  if (getNormalSkipCnt() != other.getNormalSkipCnt()) return false;  if (getExceptionSkipCnt() != other.getExceptionSkipCnt()) return false;  if (getTotalCalcCnt() != other.getTotalCalcCnt()) return false;  Object this$logNo = getLogNo(), other$logNo = other.getLogNo(); if ((this$logNo == null) ? (other$logNo != null) : !this$logNo.equals(other$logNo)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$startTime = getStartTime(), other$startTime = other.getStartTime(); if ((this$startTime == null) ? (other$startTime != null) : !this$startTime.equals(other$startTime)) return false;  Object this$endTime = getEndTime(), other$endTime = other.getEndTime(); if ((this$endTime == null) ? (other$endTime != null) : !this$endTime.equals(other$endTime)) return false;  Object this$calcTime = getCalcTime(), other$calcTime = other.getCalcTime(); if ((this$calcTime == null) ? (other$calcTime != null) : !this$calcTime.equals(other$calcTime)) return false;  Object this$taskInitTime = getTaskInitTime(), other$taskInitTime = other.getTaskInitTime(); if ((this$taskInitTime == null) ? (other$taskInitTime != null) : !this$taskInitTime.equals(other$taskInitTime)) return false;  Object this$jobCalcTime = getJobCalcTime(), other$jobCalcTime = other.getJobCalcTime(); if ((this$jobCalcTime == null) ? (other$jobCalcTime != null) : !this$jobCalcTime.equals(other$jobCalcTime)) return false;  Object this$stWriteTime = getStWriteTime(), other$stWriteTime = other.getStWriteTime(); if ((this$stWriteTime == null) ? (other$stWriteTime != null) : !this$stWriteTime.equals(other$stWriteTime)) return false;  Object this$taskStatus = getTaskStatus(), other$taskStatus = other.getTaskStatus(); if ((this$taskStatus == null) ? (other$taskStatus != null) : !this$taskStatus.equals(other$taskStatus)) return false;  Object this$exceptionInfo = getExceptionInfo(), other$exceptionInfo = other.getExceptionInfo(); if ((this$exceptionInfo == null) ? (other$exceptionInfo != null) : !this$exceptionInfo.equals(other$exceptionInfo)) return false;  Object<JobLogDo> this$jobLogList = (Object<JobLogDo>)getJobLogList(), other$jobLogList = (Object<JobLogDo>)other.getJobLogList(); if ((this$jobLogList == null) ? (other$jobLogList != null) : !this$jobLogList.equals(other$jobLogList)) return false;  Object this$progress = getProgress(), other$progress = other.getProgress(); if ((this$progress == null) ? (other$progress != null) : !this$progress.equals(other$progress)) return false;  Object<String, Integer> this$jobStatusMap = (Object<String, Integer>)getJobStatusMap(), other$jobStatusMap = (Object<String, Integer>)other.getJobStatusMap(); return !((this$jobStatusMap == null) ? (other$jobStatusMap != null) : !this$jobStatusMap.equals(other$jobStatusMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskLogDo; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getCalculateNum(); result = result * 59 + getNormalCalcCnt(); result = result * 59 + getNormalSkipCnt(); result = result * 59 + getExceptionSkipCnt(); result = result * 59 + getTotalCalcCnt(); Object $logNo = getLogNo(); result = result * 59 + (($logNo == null) ? 43 : $logNo.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $startTime = getStartTime(); result = result * 59 + (($startTime == null) ? 43 : $startTime.hashCode()); Object $endTime = getEndTime(); result = result * 59 + (($endTime == null) ? 43 : $endTime.hashCode()); Object $calcTime = getCalcTime(); result = result * 59 + (($calcTime == null) ? 43 : $calcTime.hashCode()); Object $taskInitTime = getTaskInitTime(); result = result * 59 + (($taskInitTime == null) ? 43 : $taskInitTime.hashCode()); Object $jobCalcTime = getJobCalcTime(); result = result * 59 + (($jobCalcTime == null) ? 43 : $jobCalcTime.hashCode()); Object $stWriteTime = getStWriteTime(); result = result * 59 + (($stWriteTime == null) ? 43 : $stWriteTime.hashCode()); Object $taskStatus = getTaskStatus(); result = result * 59 + (($taskStatus == null) ? 43 : $taskStatus.hashCode()); Object $exceptionInfo = getExceptionInfo(); result = result * 59 + (($exceptionInfo == null) ? 43 : $exceptionInfo.hashCode()); Object<JobLogDo> $jobLogList = (Object<JobLogDo>)getJobLogList(); result = result * 59 + (($jobLogList == null) ? 43 : $jobLogList.hashCode()); Object $progress = getProgress(); result = result * 59 + (($progress == null) ? 43 : $progress.hashCode()); Object<String, Integer> $jobStatusMap = (Object<String, Integer>)getJobStatusMap(); return result * 59 + (($jobStatusMap == null) ? 43 : $jobStatusMap.hashCode()); } public String toString() { return "TaskLogDo(logNo=" + getLogNo() + ", resultNo=" + getResultNo() + ", dataDate=" + getDataDate() + ", dataBatchNo=" + getDataBatchNo() + ", taskType=" + getTaskType() + ", taskId=" + getTaskId() + ", schemeId=" + getSchemeId() + ", startTime=" + getStartTime() + ", endTime=" + getEndTime() + ", calcTime=" + getCalcTime() + ", taskInitTime=" + getTaskInitTime() + ", jobCalcTime=" + getJobCalcTime() + ", stWriteTime=" + getStWriteTime() + ", taskStatus=" + getTaskStatus() + ", exceptionInfo=" + getExceptionInfo() + ", jobLogList=" + getJobLogList() + ", calculateNum=" + getCalculateNum() + ", normalCalcCnt=" + getNormalCalcCnt() + ", normalSkipCnt=" + getNormalSkipCnt() + ", exceptionSkipCnt=" + getExceptionSkipCnt() + ", totalCalcCnt=" + getTotalCalcCnt() + ", progress=" + getProgress() + ", jobStatusMap=" + getJobStatusMap() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getLogNo() {
/* 19 */     return this.logNo;
/*    */   } public String getResultNo() {
/* 21 */     return this.resultNo;
/*    */   } public Date getDataDate() {
/* 23 */     return this.dataDate;
/*    */   } public String getDataBatchNo() {
/* 25 */     return this.dataBatchNo;
/*    */   } public String getTaskType() {
/* 27 */     return this.taskType;
/*    */   } public String getTaskId() {
/* 29 */     return this.taskId;
/*    */   } public String getSchemeId() {
/* 31 */     return this.schemeId;
/*    */   } public Timestamp getStartTime() {
/* 33 */     return this.startTime;
/*    */   } public Timestamp getEndTime() {
/* 35 */     return this.endTime;
/*    */   } public BigDecimal getCalcTime() {
/* 37 */     return this.calcTime;
/*    */   } public Timestamp getTaskInitTime() {
/* 39 */     return this.taskInitTime;
/*    */   } public Timestamp getJobCalcTime() {
/* 41 */     return this.jobCalcTime;
/*    */   } public Timestamp getStWriteTime() {
/* 43 */     return this.stWriteTime;
/*    */   } public String getTaskStatus() {
/* 45 */     return this.taskStatus;
/*    */   } public String getExceptionInfo() {
/* 47 */     return this.exceptionInfo;
/*    */   } public List<JobLogDo> getJobLogList() {
/* 49 */     return this.jobLogList;
/*    */   } public int getCalculateNum() {
/* 51 */     return this.calculateNum;
/*    */   } public int getNormalCalcCnt() {
/* 53 */     return this.normalCalcCnt;
/*    */   } public int getNormalSkipCnt() {
/* 55 */     return this.normalSkipCnt;
/*    */   } public int getExceptionSkipCnt() {
/* 57 */     return this.exceptionSkipCnt;
/*    */   } public int getTotalCalcCnt() {
/* 59 */     return this.totalCalcCnt;
/*    */   } public BigDecimal getProgress() {
/* 61 */     return this.progress;
/*    */   } public Map<String, Integer> getJobStatusMap() {
/* 63 */     return this.jobStatusMap;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskLogDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */