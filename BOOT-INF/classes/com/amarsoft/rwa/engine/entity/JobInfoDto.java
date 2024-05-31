/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.Approach;
/*    */ import com.amarsoft.rwa.engine.constant.JobType;
/*    */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*    */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*    */ import com.amarsoft.rwa.engine.entity.ThreadGroupDto;
/*    */ 
/*    */ public class JobInfoDto implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673290005L;
/*    */   private TaskType taskType;
/*    */   private JobType jobType;
/*    */   private String resultNo;
/*    */   private String dataBatchNo;
/*    */   @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
/*    */   @DateTimeFormat(pattern = "yyyy-MM-dd")
/*    */   private Date dataDate;
/*    */   private Approach approach;
/*    */   private String schemeId;
/*    */   
/* 21 */   public void setTaskType(TaskType taskType) { this.taskType = taskType; } private String taskId; private TaskConfigDo taskConfigDo; private Long logNo; private boolean isSubTable; private ThreadGroupDto subThreadGroup; private List<TaskRangeDo> rangeList; private String jobId; private JobLogDo jobLog; public void setJobType(JobType jobType) { this.jobType = jobType; } public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8") public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setApproach(Approach approach) { this.approach = approach; } public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setTaskId(String taskId) { this.taskId = taskId; } public void setTaskConfigDo(TaskConfigDo taskConfigDo) { this.taskConfigDo = taskConfigDo; } public void setLogNo(Long logNo) { this.logNo = logNo; } public void setSubTable(boolean isSubTable) { this.isSubTable = isSubTable; } public void setSubThreadGroup(ThreadGroupDto subThreadGroup) { this.subThreadGroup = subThreadGroup; } public void setRangeList(List<TaskRangeDo> rangeList) { this.rangeList = rangeList; } public void setJobId(String jobId) { this.jobId = jobId; } public void setJobLog(JobLogDo jobLog) { this.jobLog = jobLog; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.JobInfoDto)) return false;  com.amarsoft.rwa.engine.entity.JobInfoDto other = (com.amarsoft.rwa.engine.entity.JobInfoDto)o; if (!other.canEqual(this)) return false;  if (isSubTable() != other.isSubTable()) return false;  Object this$logNo = getLogNo(), other$logNo = other.getLogNo(); if ((this$logNo == null) ? (other$logNo != null) : !this$logNo.equals(other$logNo)) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$jobType = getJobType(), other$jobType = other.getJobType(); if ((this$jobType == null) ? (other$jobType != null) : !this$jobType.equals(other$jobType)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$taskConfigDo = getTaskConfigDo(), other$taskConfigDo = other.getTaskConfigDo(); if ((this$taskConfigDo == null) ? (other$taskConfigDo != null) : !this$taskConfigDo.equals(other$taskConfigDo)) return false;  Object this$subThreadGroup = getSubThreadGroup(), other$subThreadGroup = other.getSubThreadGroup(); if ((this$subThreadGroup == null) ? (other$subThreadGroup != null) : !this$subThreadGroup.equals(other$subThreadGroup)) return false;  Object<TaskRangeDo> this$rangeList = (Object<TaskRangeDo>)getRangeList(), other$rangeList = (Object<TaskRangeDo>)other.getRangeList(); if ((this$rangeList == null) ? (other$rangeList != null) : !this$rangeList.equals(other$rangeList)) return false;  Object this$jobId = getJobId(), other$jobId = other.getJobId(); if ((this$jobId == null) ? (other$jobId != null) : !this$jobId.equals(other$jobId)) return false;  Object this$jobLog = getJobLog(), other$jobLog = other.getJobLog(); return !((this$jobLog == null) ? (other$jobLog != null) : !this$jobLog.equals(other$jobLog)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.JobInfoDto; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isSubTable() ? 79 : 97); Object $logNo = getLogNo(); result = result * 59 + (($logNo == null) ? 43 : $logNo.hashCode()); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $jobType = getJobType(); result = result * 59 + (($jobType == null) ? 43 : $jobType.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $taskConfigDo = getTaskConfigDo(); result = result * 59 + (($taskConfigDo == null) ? 43 : $taskConfigDo.hashCode()); Object $subThreadGroup = getSubThreadGroup(); result = result * 59 + (($subThreadGroup == null) ? 43 : $subThreadGroup.hashCode()); Object<TaskRangeDo> $rangeList = (Object<TaskRangeDo>)getRangeList(); result = result * 59 + (($rangeList == null) ? 43 : $rangeList.hashCode()); Object $jobId = getJobId(); result = result * 59 + (($jobId == null) ? 43 : $jobId.hashCode()); Object $jobLog = getJobLog(); return result * 59 + (($jobLog == null) ? 43 : $jobLog.hashCode()); } public String toString() { return "JobInfoDto(taskType=" + getTaskType() + ", jobType=" + getJobType() + ", resultNo=" + getResultNo() + ", dataBatchNo=" + getDataBatchNo() + ", dataDate=" + getDataDate() + ", approach=" + getApproach() + ", schemeId=" + getSchemeId() + ", taskId=" + getTaskId() + ", taskConfigDo=" + getTaskConfigDo() + ", logNo=" + getLogNo() + ", isSubTable=" + isSubTable() + ", subThreadGroup=" + getSubThreadGroup() + ", rangeList=" + getRangeList() + ", jobId=" + getJobId() + ", jobLog=" + getJobLog() + ")"; } public JobInfoDto(TaskType taskType, JobType jobType, String resultNo, String dataBatchNo, Date dataDate, Approach approach, String schemeId, String taskId, TaskConfigDo taskConfigDo, Long logNo, boolean isSubTable, ThreadGroupDto subThreadGroup, List<TaskRangeDo> rangeList, String jobId, JobLogDo jobLog) {
/* 22 */     this.taskType = taskType; this.jobType = jobType; this.resultNo = resultNo; this.dataBatchNo = dataBatchNo; this.dataDate = dataDate; this.approach = approach; this.schemeId = schemeId; this.taskId = taskId; this.taskConfigDo = taskConfigDo; this.logNo = logNo; this.isSubTable = isSubTable; this.subThreadGroup = subThreadGroup; this.rangeList = rangeList; this.jobId = jobId; this.jobLog = jobLog;
/*    */   }
/*    */   
/*    */   public JobInfoDto() {}
/*    */   
/*    */   public TaskType getTaskType() {
/* 28 */     return this.taskType;
/* 29 */   } public JobType getJobType() { return this.jobType; }
/* 30 */   public String getResultNo() { return this.resultNo; } public String getDataBatchNo() {
/* 31 */     return this.dataBatchNo;
/*    */   }
/*    */   
/* 34 */   public Date getDataDate() { return this.dataDate; }
/* 35 */   public Approach getApproach() { return this.approach; }
/* 36 */   public String getSchemeId() { return this.schemeId; }
/* 37 */   public String getTaskId() { return this.taskId; }
/* 38 */   public TaskConfigDo getTaskConfigDo() { return this.taskConfigDo; }
/* 39 */   public Long getLogNo() { return this.logNo; }
/* 40 */   public boolean isSubTable() { return this.isSubTable; }
/* 41 */   public ThreadGroupDto getSubThreadGroup() { return this.subThreadGroup; }
/* 42 */   public List<TaskRangeDo> getRangeList() { return this.rangeList; }
/* 43 */   public String getJobId() { return this.jobId; } public JobLogDo getJobLog() {
/* 44 */     return this.jobLog;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\JobInfoDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */