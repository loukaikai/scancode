/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_TASK_RANGE")
/*    */ public class TaskRangeDo implements Serializable { private static final long serialVersionUID = 8234237673290004L; @TableId("SERIAL_NO")
/*    */   private String serialNo;
/*    */   @TableField("TASK_ID")
/*    */   private String taskId;
/*    */   @TableField("CREDIT_RISK_DATA_TYPE")
/*    */   private String creditRiskDataType;
/*    */   
/* 10 */   public void setSerialNo(String serialNo) { this.serialNo = serialNo; } @TableField("NETTING_FLAG") private String nettingFlag; @TableField("IS_ORIGINATOR") private String isOriginator; @TableField("ORG_ID") private String orgId; @TableField("INDUSTRY_ID") private String industryId; @TableField("ASSET_TYPE") private String assetType; public void setTaskId(String taskId) { this.taskId = taskId; } public void setCreditRiskDataType(String creditRiskDataType) { this.creditRiskDataType = creditRiskDataType; } public void setNettingFlag(String nettingFlag) { this.nettingFlag = nettingFlag; } public void setIsOriginator(String isOriginator) { this.isOriginator = isOriginator; } public void setOrgId(String orgId) { this.orgId = orgId; } public void setIndustryId(String industryId) { this.industryId = industryId; } public void setAssetType(String assetType) { this.assetType = assetType; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskRangeDo)) return false;  com.amarsoft.rwa.engine.entity.TaskRangeDo other = (com.amarsoft.rwa.engine.entity.TaskRangeDo)o; if (!other.canEqual(this)) return false;  Object this$serialNo = getSerialNo(), other$serialNo = other.getSerialNo(); if ((this$serialNo == null) ? (other$serialNo != null) : !this$serialNo.equals(other$serialNo)) return false;  Object this$taskId = getTaskId(), other$taskId = other.getTaskId(); if ((this$taskId == null) ? (other$taskId != null) : !this$taskId.equals(other$taskId)) return false;  Object this$creditRiskDataType = getCreditRiskDataType(), other$creditRiskDataType = other.getCreditRiskDataType(); if ((this$creditRiskDataType == null) ? (other$creditRiskDataType != null) : !this$creditRiskDataType.equals(other$creditRiskDataType)) return false;  Object this$nettingFlag = getNettingFlag(), other$nettingFlag = other.getNettingFlag(); if ((this$nettingFlag == null) ? (other$nettingFlag != null) : !this$nettingFlag.equals(other$nettingFlag)) return false;  Object this$isOriginator = getIsOriginator(), other$isOriginator = other.getIsOriginator(); if ((this$isOriginator == null) ? (other$isOriginator != null) : !this$isOriginator.equals(other$isOriginator)) return false;  Object this$orgId = getOrgId(), other$orgId = other.getOrgId(); if ((this$orgId == null) ? (other$orgId != null) : !this$orgId.equals(other$orgId)) return false;  Object this$industryId = getIndustryId(), other$industryId = other.getIndustryId(); if ((this$industryId == null) ? (other$industryId != null) : !this$industryId.equals(other$industryId)) return false;  Object this$assetType = getAssetType(), other$assetType = other.getAssetType(); return !((this$assetType == null) ? (other$assetType != null) : !this$assetType.equals(other$assetType)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskRangeDo; } public int hashCode() { int PRIME = 59; result = 1; Object $serialNo = getSerialNo(); result = result * 59 + (($serialNo == null) ? 43 : $serialNo.hashCode()); Object $taskId = getTaskId(); result = result * 59 + (($taskId == null) ? 43 : $taskId.hashCode()); Object $creditRiskDataType = getCreditRiskDataType(); result = result * 59 + (($creditRiskDataType == null) ? 43 : $creditRiskDataType.hashCode()); Object $nettingFlag = getNettingFlag(); result = result * 59 + (($nettingFlag == null) ? 43 : $nettingFlag.hashCode()); Object $isOriginator = getIsOriginator(); result = result * 59 + (($isOriginator == null) ? 43 : $isOriginator.hashCode()); Object $orgId = getOrgId(); result = result * 59 + (($orgId == null) ? 43 : $orgId.hashCode()); Object $industryId = getIndustryId(); result = result * 59 + (($industryId == null) ? 43 : $industryId.hashCode()); Object $assetType = getAssetType(); return result * 59 + (($assetType == null) ? 43 : $assetType.hashCode()); } public String toString() { return "TaskRangeDo(serialNo=" + getSerialNo() + ", taskId=" + getTaskId() + ", creditRiskDataType=" + getCreditRiskDataType() + ", nettingFlag=" + getNettingFlag() + ", isOriginator=" + getIsOriginator() + ", orgId=" + getOrgId() + ", industryId=" + getIndustryId() + ", assetType=" + getAssetType() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNo() {
/* 17 */     return this.serialNo;
/*    */   } public String getTaskId() {
/* 19 */     return this.taskId;
/*    */   } public String getCreditRiskDataType() {
/* 21 */     return this.creditRiskDataType;
/*    */   } public String getNettingFlag() {
/* 23 */     return this.nettingFlag;
/*    */   } public String getIsOriginator() {
/* 25 */     return this.isOriginator;
/*    */   } public String getOrgId() {
/* 27 */     return this.orgId;
/*    */   } public String getIndustryId() {
/* 29 */     return this.industryId;
/*    */   } public String getAssetType() {
/* 31 */     return this.assetType;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskRangeDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */