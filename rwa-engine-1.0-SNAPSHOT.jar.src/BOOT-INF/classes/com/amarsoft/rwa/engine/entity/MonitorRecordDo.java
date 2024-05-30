/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;@TableName("CS_MONITOR_RECORD")
/*    */ public class MonitorRecordDo { @TableId("ID")
/*    */   private Long id; @TableField("RESULT_NO")
/*    */   private String resultNo; @TableField("DATA_DATE")
/*    */   private Date dataDate; @TableField("DATA_BATCH_NO")
/*    */   private String dataBatchNo; @TableField("MONITOR_TASK_ID")
/*    */   private String monitorTaskId; @TableField("MONITOR_TASK_TYPE")
/*    */   private String monitorTaskType; @TableField("MONITOR_RULE_ID")
/*    */   private String monitorRuleId; @TableField("MONITOR_RULE_NAME")
/*    */   private String monitorRuleName; @TableField("MONITOR_RULE_TYPE")
/*    */   private String monitorRuleType; @TableField("MONITOR_INDEX_CODE")
/* 12 */   private String monitorIndexCode; public void setId(Long id) { this.id = id; } @TableField("MONITOR_INDEX_NAME") private String monitorIndexName; @TableField("CUSTOM_INDEX") private String customIndex; @TableField("MONITOR_DIM") private String monitorDim; @TableField("MONITOR_SCOPE_CODE") private String monitorScopeCode; @TableField("MONITOR_SCOPE_NAME") private String monitorScopeName; @TableField("INDEX_VALUE") private BigDecimal indexValue; @TableField("THRESHOLD_TRG_WAY") private String thresholdTrgWay; @TableField("THRESHOLD_CONFIG") private String thresholdConfig; @TableField("WARNING_DEGREE") private String warningDegree; @TableField("RECORD_TIME") private Timestamp recordTime; public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setMonitorTaskId(String monitorTaskId) { this.monitorTaskId = monitorTaskId; } public void setMonitorTaskType(String monitorTaskType) { this.monitorTaskType = monitorTaskType; } public void setMonitorRuleId(String monitorRuleId) { this.monitorRuleId = monitorRuleId; } public void setMonitorRuleName(String monitorRuleName) { this.monitorRuleName = monitorRuleName; } public void setMonitorRuleType(String monitorRuleType) { this.monitorRuleType = monitorRuleType; } public void setMonitorIndexCode(String monitorIndexCode) { this.monitorIndexCode = monitorIndexCode; } public void setMonitorIndexName(String monitorIndexName) { this.monitorIndexName = monitorIndexName; } public void setCustomIndex(String customIndex) { this.customIndex = customIndex; } public void setMonitorDim(String monitorDim) { this.monitorDim = monitorDim; } public void setMonitorScopeCode(String monitorScopeCode) { this.monitorScopeCode = monitorScopeCode; } public void setMonitorScopeName(String monitorScopeName) { this.monitorScopeName = monitorScopeName; } public void setIndexValue(BigDecimal indexValue) { this.indexValue = indexValue; } public void setThresholdTrgWay(String thresholdTrgWay) { this.thresholdTrgWay = thresholdTrgWay; } public void setThresholdConfig(String thresholdConfig) { this.thresholdConfig = thresholdConfig; } public void setWarningDegree(String warningDegree) { this.warningDegree = warningDegree; } public void setRecordTime(Timestamp recordTime) { this.recordTime = recordTime; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MonitorRecordDo)) return false;  com.amarsoft.rwa.engine.entity.MonitorRecordDo other = (com.amarsoft.rwa.engine.entity.MonitorRecordDo)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$monitorTaskId = getMonitorTaskId(), other$monitorTaskId = other.getMonitorTaskId(); if ((this$monitorTaskId == null) ? (other$monitorTaskId != null) : !this$monitorTaskId.equals(other$monitorTaskId)) return false;  Object this$monitorTaskType = getMonitorTaskType(), other$monitorTaskType = other.getMonitorTaskType(); if ((this$monitorTaskType == null) ? (other$monitorTaskType != null) : !this$monitorTaskType.equals(other$monitorTaskType)) return false;  Object this$monitorRuleId = getMonitorRuleId(), other$monitorRuleId = other.getMonitorRuleId(); if ((this$monitorRuleId == null) ? (other$monitorRuleId != null) : !this$monitorRuleId.equals(other$monitorRuleId)) return false;  Object this$monitorRuleName = getMonitorRuleName(), other$monitorRuleName = other.getMonitorRuleName(); if ((this$monitorRuleName == null) ? (other$monitorRuleName != null) : !this$monitorRuleName.equals(other$monitorRuleName)) return false;  Object this$monitorRuleType = getMonitorRuleType(), other$monitorRuleType = other.getMonitorRuleType(); if ((this$monitorRuleType == null) ? (other$monitorRuleType != null) : !this$monitorRuleType.equals(other$monitorRuleType)) return false;  Object this$monitorIndexCode = getMonitorIndexCode(), other$monitorIndexCode = other.getMonitorIndexCode(); if ((this$monitorIndexCode == null) ? (other$monitorIndexCode != null) : !this$monitorIndexCode.equals(other$monitorIndexCode)) return false;  Object this$monitorIndexName = getMonitorIndexName(), other$monitorIndexName = other.getMonitorIndexName(); if ((this$monitorIndexName == null) ? (other$monitorIndexName != null) : !this$monitorIndexName.equals(other$monitorIndexName)) return false;  Object this$customIndex = getCustomIndex(), other$customIndex = other.getCustomIndex(); if ((this$customIndex == null) ? (other$customIndex != null) : !this$customIndex.equals(other$customIndex)) return false;  Object this$monitorDim = getMonitorDim(), other$monitorDim = other.getMonitorDim(); if ((this$monitorDim == null) ? (other$monitorDim != null) : !this$monitorDim.equals(other$monitorDim)) return false;  Object this$monitorScopeCode = getMonitorScopeCode(), other$monitorScopeCode = other.getMonitorScopeCode(); if ((this$monitorScopeCode == null) ? (other$monitorScopeCode != null) : !this$monitorScopeCode.equals(other$monitorScopeCode)) return false;  Object this$monitorScopeName = getMonitorScopeName(), other$monitorScopeName = other.getMonitorScopeName(); if ((this$monitorScopeName == null) ? (other$monitorScopeName != null) : !this$monitorScopeName.equals(other$monitorScopeName)) return false;  Object this$indexValue = getIndexValue(), other$indexValue = other.getIndexValue(); if ((this$indexValue == null) ? (other$indexValue != null) : !this$indexValue.equals(other$indexValue)) return false;  Object this$thresholdTrgWay = getThresholdTrgWay(), other$thresholdTrgWay = other.getThresholdTrgWay(); if ((this$thresholdTrgWay == null) ? (other$thresholdTrgWay != null) : !this$thresholdTrgWay.equals(other$thresholdTrgWay)) return false;  Object this$thresholdConfig = getThresholdConfig(), other$thresholdConfig = other.getThresholdConfig(); if ((this$thresholdConfig == null) ? (other$thresholdConfig != null) : !this$thresholdConfig.equals(other$thresholdConfig)) return false;  Object this$warningDegree = getWarningDegree(), other$warningDegree = other.getWarningDegree(); if ((this$warningDegree == null) ? (other$warningDegree != null) : !this$warningDegree.equals(other$warningDegree)) return false;  Object this$recordTime = getRecordTime(), other$recordTime = other.getRecordTime(); return !((this$recordTime == null) ? (other$recordTime != null) : !this$recordTime.equals(other$recordTime)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MonitorRecordDo; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $monitorTaskId = getMonitorTaskId(); result = result * 59 + (($monitorTaskId == null) ? 43 : $monitorTaskId.hashCode()); Object $monitorTaskType = getMonitorTaskType(); result = result * 59 + (($monitorTaskType == null) ? 43 : $monitorTaskType.hashCode()); Object $monitorRuleId = getMonitorRuleId(); result = result * 59 + (($monitorRuleId == null) ? 43 : $monitorRuleId.hashCode()); Object $monitorRuleName = getMonitorRuleName(); result = result * 59 + (($monitorRuleName == null) ? 43 : $monitorRuleName.hashCode()); Object $monitorRuleType = getMonitorRuleType(); result = result * 59 + (($monitorRuleType == null) ? 43 : $monitorRuleType.hashCode()); Object $monitorIndexCode = getMonitorIndexCode(); result = result * 59 + (($monitorIndexCode == null) ? 43 : $monitorIndexCode.hashCode()); Object $monitorIndexName = getMonitorIndexName(); result = result * 59 + (($monitorIndexName == null) ? 43 : $monitorIndexName.hashCode()); Object $customIndex = getCustomIndex(); result = result * 59 + (($customIndex == null) ? 43 : $customIndex.hashCode()); Object $monitorDim = getMonitorDim(); result = result * 59 + (($monitorDim == null) ? 43 : $monitorDim.hashCode()); Object $monitorScopeCode = getMonitorScopeCode(); result = result * 59 + (($monitorScopeCode == null) ? 43 : $monitorScopeCode.hashCode()); Object $monitorScopeName = getMonitorScopeName(); result = result * 59 + (($monitorScopeName == null) ? 43 : $monitorScopeName.hashCode()); Object $indexValue = getIndexValue(); result = result * 59 + (($indexValue == null) ? 43 : $indexValue.hashCode()); Object $thresholdTrgWay = getThresholdTrgWay(); result = result * 59 + (($thresholdTrgWay == null) ? 43 : $thresholdTrgWay.hashCode()); Object $thresholdConfig = getThresholdConfig(); result = result * 59 + (($thresholdConfig == null) ? 43 : $thresholdConfig.hashCode()); Object $warningDegree = getWarningDegree(); result = result * 59 + (($warningDegree == null) ? 43 : $warningDegree.hashCode()); Object $recordTime = getRecordTime(); return result * 59 + (($recordTime == null) ? 43 : $recordTime.hashCode()); } public String toString() { return "MonitorRecordDo(id=" + getId() + ", resultNo=" + getResultNo() + ", dataDate=" + getDataDate() + ", dataBatchNo=" + getDataBatchNo() + ", monitorTaskId=" + getMonitorTaskId() + ", monitorTaskType=" + getMonitorTaskType() + ", monitorRuleId=" + getMonitorRuleId() + ", monitorRuleName=" + getMonitorRuleName() + ", monitorRuleType=" + getMonitorRuleType() + ", monitorIndexCode=" + getMonitorIndexCode() + ", monitorIndexName=" + getMonitorIndexName() + ", customIndex=" + getCustomIndex() + ", monitorDim=" + getMonitorDim() + ", monitorScopeCode=" + getMonitorScopeCode() + ", monitorScopeName=" + getMonitorScopeName() + ", indexValue=" + getIndexValue() + ", thresholdTrgWay=" + getThresholdTrgWay() + ", thresholdConfig=" + getThresholdConfig() + ", warningDegree=" + getWarningDegree() + ", recordTime=" + getRecordTime() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public Long getId() {
/* 17 */     return this.id;
/*    */   } public String getResultNo() {
/* 19 */     return this.resultNo;
/*    */   } public Date getDataDate() {
/* 21 */     return this.dataDate;
/*    */   } public String getDataBatchNo() {
/* 23 */     return this.dataBatchNo;
/*    */   } public String getMonitorTaskId() {
/* 25 */     return this.monitorTaskId;
/*    */   } public String getMonitorTaskType() {
/* 27 */     return this.monitorTaskType;
/*    */   } public String getMonitorRuleId() {
/* 29 */     return this.monitorRuleId;
/*    */   } public String getMonitorRuleName() {
/* 31 */     return this.monitorRuleName;
/*    */   } public String getMonitorRuleType() {
/* 33 */     return this.monitorRuleType;
/*    */   } public String getMonitorIndexCode() {
/* 35 */     return this.monitorIndexCode;
/*    */   } public String getMonitorIndexName() {
/* 37 */     return this.monitorIndexName;
/*    */   } public String getCustomIndex() {
/* 39 */     return this.customIndex;
/*    */   } public String getMonitorDim() {
/* 41 */     return this.monitorDim;
/*    */   } public String getMonitorScopeCode() {
/* 43 */     return this.monitorScopeCode;
/*    */   } public String getMonitorScopeName() {
/* 45 */     return this.monitorScopeName;
/*    */   } public BigDecimal getIndexValue() {
/* 47 */     return this.indexValue;
/*    */   } public String getThresholdTrgWay() {
/* 49 */     return this.thresholdTrgWay;
/*    */   } public String getThresholdConfig() {
/* 51 */     return this.thresholdConfig;
/*    */   } public String getWarningDegree() {
/* 53 */     return this.warningDegree;
/*    */   } public Timestamp getRecordTime() {
/* 55 */     return this.recordTime;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MonitorRecordDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */