/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("CS_MONITOR_WARING")
/*    */ public class MonitorWaringDo { @TableId("SERIAL_NO")
/*    */   private String serialNo; @TableField("MONITOR_RULE_ID")
/*    */   private String monitorRuleId; @TableField("MONITOR_TASK_ID")
/*    */   private String monitorTaskId;
/*    */   
/*  8 */   public void setSerialNo(String serialNo) { this.serialNo = serialNo; } @TableField("THRESHOLD_TRG_WAY") private String thresholdTrgWay; @TableField("THRESHOLD_CONFIG") private String thresholdConfig; @TableField("WARNING_DEGREE") private String warningDegree; public void setMonitorRuleId(String monitorRuleId) { this.monitorRuleId = monitorRuleId; } public void setMonitorTaskId(String monitorTaskId) { this.monitorTaskId = monitorTaskId; } public void setThresholdTrgWay(String thresholdTrgWay) { this.thresholdTrgWay = thresholdTrgWay; } public void setThresholdConfig(String thresholdConfig) { this.thresholdConfig = thresholdConfig; } public void setWarningDegree(String warningDegree) { this.warningDegree = warningDegree; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MonitorWaringDo)) return false;  com.amarsoft.rwa.engine.entity.MonitorWaringDo other = (com.amarsoft.rwa.engine.entity.MonitorWaringDo)o; if (!other.canEqual(this)) return false;  Object this$serialNo = getSerialNo(), other$serialNo = other.getSerialNo(); if ((this$serialNo == null) ? (other$serialNo != null) : !this$serialNo.equals(other$serialNo)) return false;  Object this$monitorRuleId = getMonitorRuleId(), other$monitorRuleId = other.getMonitorRuleId(); if ((this$monitorRuleId == null) ? (other$monitorRuleId != null) : !this$monitorRuleId.equals(other$monitorRuleId)) return false;  Object this$monitorTaskId = getMonitorTaskId(), other$monitorTaskId = other.getMonitorTaskId(); if ((this$monitorTaskId == null) ? (other$monitorTaskId != null) : !this$monitorTaskId.equals(other$monitorTaskId)) return false;  Object this$thresholdTrgWay = getThresholdTrgWay(), other$thresholdTrgWay = other.getThresholdTrgWay(); if ((this$thresholdTrgWay == null) ? (other$thresholdTrgWay != null) : !this$thresholdTrgWay.equals(other$thresholdTrgWay)) return false;  Object this$thresholdConfig = getThresholdConfig(), other$thresholdConfig = other.getThresholdConfig(); if ((this$thresholdConfig == null) ? (other$thresholdConfig != null) : !this$thresholdConfig.equals(other$thresholdConfig)) return false;  Object this$warningDegree = getWarningDegree(), other$warningDegree = other.getWarningDegree(); return !((this$warningDegree == null) ? (other$warningDegree != null) : !this$warningDegree.equals(other$warningDegree)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MonitorWaringDo; } public int hashCode() { int PRIME = 59; result = 1; Object $serialNo = getSerialNo(); result = result * 59 + (($serialNo == null) ? 43 : $serialNo.hashCode()); Object $monitorRuleId = getMonitorRuleId(); result = result * 59 + (($monitorRuleId == null) ? 43 : $monitorRuleId.hashCode()); Object $monitorTaskId = getMonitorTaskId(); result = result * 59 + (($monitorTaskId == null) ? 43 : $monitorTaskId.hashCode()); Object $thresholdTrgWay = getThresholdTrgWay(); result = result * 59 + (($thresholdTrgWay == null) ? 43 : $thresholdTrgWay.hashCode()); Object $thresholdConfig = getThresholdConfig(); result = result * 59 + (($thresholdConfig == null) ? 43 : $thresholdConfig.hashCode()); Object $warningDegree = getWarningDegree(); return result * 59 + (($warningDegree == null) ? 43 : $warningDegree.hashCode()); } public String toString() { return "MonitorWaringDo(serialNo=" + getSerialNo() + ", monitorRuleId=" + getMonitorRuleId() + ", monitorTaskId=" + getMonitorTaskId() + ", thresholdTrgWay=" + getThresholdTrgWay() + ", thresholdConfig=" + getThresholdConfig() + ", warningDegree=" + getWarningDegree() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNo() {
/* 13 */     return this.serialNo;
/*    */   } public String getMonitorRuleId() {
/* 15 */     return this.monitorRuleId;
/*    */   } public String getMonitorTaskId() {
/* 17 */     return this.monitorTaskId;
/*    */   } public String getThresholdTrgWay() {
/* 19 */     return this.thresholdTrgWay;
/*    */   } public String getThresholdConfig() {
/* 21 */     return this.thresholdConfig;
/*    */   } public String getWarningDegree() {
/* 23 */     return this.warningDegree;
/*    */   } }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MonitorWaringDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */