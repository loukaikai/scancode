/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_TASK")
/*    */ public class TaskConfigDo implements Serializable { private static final long serialVersionUID = 8234237673290001L; @TableId("TASK_ID")
/*    */   private String taskId; @TableField("TASK_NAME")
/*    */   private String taskName; @TableField("TASK_TYPE")
/*    */   private String taskType; @TableField("RISK_TYPE")
/*    */   private String riskType; @TableField("BANK_TRANCHE")
/*    */   private String bankTranche;
/*    */   @TableField("APPROACH")
/*    */   private String approach;
/*    */   @TableField("CONSOLIDATE_FLAG")
/*    */   private String consolidateFlag;
/*    */   
/* 14 */   public void setTaskId(String taskId) { this.taskId = taskId; } @TableField("IS_CALC_CVA") private String isCalcCva; @TableField("SCHEME_ID") private String schemeId; @TableField("EN_EC_CALC") private boolean enEcCalc; @TableField("EC_OF") private BigDecimal ecOf; @TableField("DATA_BATCH_NO") private String dataBatchNo; @TableField("DATA_DATE") private Date dataDate; @TableField("CALCULATION_RANGE") private String calculationRange; @TableField("TASK_STATUS") private String taskStatus; @TableField(exist = false) private Map<String, List<TaskRangeDo>> taskRangeMap; public void setTaskName(String taskName) { this.taskName = taskName; } public void setTaskType(String taskType) { this.taskType = taskType; } public void setRiskType(String riskType) { this.riskType = riskType; } public void setBankTranche(String bankTranche) { this.bankTranche = bankTranche; } public void setApproach(String approach) { this.approach = approach; } public void setConsolidateFlag(String consolidateFlag) { this.consolidateFlag = consolidateFlag; } public void setIsCalcCva(String isCalcCva) { this.isCalcCva = isCalcCva; } public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setEnEcCalc(boolean enEcCalc) { this.enEcCalc = enEcCalc; } public void setEcOf(BigDecimal ecOf) { this.ecOf = ecOf; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setCalculationRange(String calculationRange) { this.calculationRange = calculationRange; } public void setTaskStatus(String taskStatus) { this.taskStatus = taskStatus; } public void setTaskRangeMap(Map<String, List<TaskRangeDo>> taskRangeMap) { this.taskRangeMap = taskRangeMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskConfigDo)) return false;  com.amarsoft.rwa.engine.entity.TaskConfigDo other = (com.amarsoft.rwa.engine.entity.TaskConfigDo)o; if (!other.canEqual(this)) return false;  if (isEnEcCalc() != other.isEnEcCalc()) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$taskName = getTaskName(), other$taskName = other.getTaskName(); if ((this$taskName == null) ? (other$taskName != null) : !this$taskName.equals(other$taskName)) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$riskType = getRiskType(), other$riskType = other.getRiskType(); if ((this$riskType == null) ? (other$riskType != null) : !this$riskType.equals(other$riskType)) return false;  Object this$bankTranche = getBankTranche(), other$bankTranche = other.getBankTranche(); if ((this$bankTranche == null) ? (other$bankTranche != null) : !this$bankTranche.equals(other$bankTranche)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$consolidateFlag = getConsolidateFlag(), other$consolidateFlag = other.getConsolidateFlag(); if ((this$consolidateFlag == null) ? (other$consolidateFlag != null) : !this$consolidateFlag.equals(other$consolidateFlag)) return false;  Object this$isCalcCva = getIsCalcCva(), other$isCalcCva = other.getIsCalcCva(); if ((this$isCalcCva == null) ? (other$isCalcCva != null) : !this$isCalcCva.equals(other$isCalcCva)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$ecOf = getEcOf(), other$ecOf = other.getEcOf(); if ((this$ecOf == null) ? (other$ecOf != null) : !this$ecOf.equals(other$ecOf)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$calculationRange = getCalculationRange(), other$calculationRange = other.getCalculationRange(); if ((this$calculationRange == null) ? (other$calculationRange != null) : !this$calculationRange.equals(other$calculationRange)) return false;  Object this$taskStatus = getTaskStatus(), other$taskStatus = other.getTaskStatus(); if ((this$taskStatus == null) ? (other$taskStatus != null) : !this$taskStatus.equals(other$taskStatus)) return false;  Object<String, List<TaskRangeDo>> this$taskRangeMap = (Object<String, List<TaskRangeDo>>)getTaskRangeMap(), other$taskRangeMap = (Object<String, List<TaskRangeDo>>)other.getTaskRangeMap(); return !((this$taskRangeMap == null) ? (other$taskRangeMap != null) : !this$taskRangeMap.equals(other$taskRangeMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskConfigDo; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + (isEnEcCalc() ? 79 : 97); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $taskName = getTaskName(); result = result * 59 + (($taskName == null) ? 43 : $taskName.hashCode()); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $riskType = getRiskType(); result = result * 59 + (($riskType == null) ? 43 : $riskType.hashCode()); Object $bankTranche = getBankTranche(); result = result * 59 + (($bankTranche == null) ? 43 : $bankTranche.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $consolidateFlag = getConsolidateFlag(); result = result * 59 + (($consolidateFlag == null) ? 43 : $consolidateFlag.hashCode()); Object $isCalcCva = getIsCalcCva(); result = result * 59 + (($isCalcCva == null) ? 43 : $isCalcCva.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $ecOf = getEcOf(); result = result * 59 + (($ecOf == null) ? 43 : $ecOf.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $calculationRange = getCalculationRange(); result = result * 59 + (($calculationRange == null) ? 43 : $calculationRange.hashCode()); Object $taskStatus = getTaskStatus(); result = result * 59 + (($taskStatus == null) ? 43 : $taskStatus.hashCode()); Object<String, List<TaskRangeDo>> $taskRangeMap = (Object<String, List<TaskRangeDo>>)getTaskRangeMap(); return result * 59 + (($taskRangeMap == null) ? 43 : $taskRangeMap.hashCode()); } public String toString() { return "TaskConfigDo(taskId=" + getTaskId() + ", taskName=" + getTaskName() + ", taskType=" + getTaskType() + ", riskType=" + getRiskType() + ", bankTranche=" + getBankTranche() + ", approach=" + getApproach() + ", consolidateFlag=" + getConsolidateFlag() + ", isCalcCva=" + getIsCalcCva() + ", schemeId=" + getSchemeId() + ", enEcCalc=" + isEnEcCalc() + ", ecOf=" + getEcOf() + ", dataBatchNo=" + getDataBatchNo() + ", dataDate=" + getDataDate() + ", calculationRange=" + getCalculationRange() + ", taskStatus=" + getTaskStatus() + ", taskRangeMap=" + getTaskRangeMap() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getTaskId() {
/* 21 */     return this.taskId;
/*    */   } public String getTaskName() {
/* 23 */     return this.taskName;
/*    */   } public String getTaskType() {
/* 25 */     return this.taskType;
/*    */   } public String getRiskType() {
/* 27 */     return this.riskType;
/*    */   } public String getBankTranche() {
/* 29 */     return this.bankTranche;
/*    */   } public String getApproach() {
/* 31 */     return this.approach;
/*    */   } public String getConsolidateFlag() {
/* 33 */     return this.consolidateFlag;
/*    */   } public String getIsCalcCva() {
/* 35 */     return this.isCalcCva;
/*    */   } public String getSchemeId() {
/* 37 */     return this.schemeId;
/*    */   } public boolean isEnEcCalc() {
/* 39 */     return this.enEcCalc;
/*    */   } public BigDecimal getEcOf() {
/* 41 */     return this.ecOf;
/*    */   } public String getDataBatchNo() {
/* 43 */     return this.dataBatchNo;
/*    */   } public Date getDataDate() {
/* 45 */     return this.dataDate;
/*    */   } public String getCalculationRange() {
/* 47 */     return this.calculationRange;
/*    */   } public String getTaskStatus() {
/* 49 */     return this.taskStatus;
/*    */   } public Map<String, List<TaskRangeDo>> getTaskRangeMap() {
/* 51 */     return this.taskRangeMap;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskConfigDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */