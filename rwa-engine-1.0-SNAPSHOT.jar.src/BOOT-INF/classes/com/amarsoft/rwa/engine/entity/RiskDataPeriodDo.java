/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EL_RISK")
/*    */ public class RiskDataPeriodDo { @TableId("DATA_BATCH_NO")
/*    */   private String dataBatchNo; @TableField("DATA_DATE")
/*    */   private Date dataDate; @TableField("CORPORATION_ID")
/*    */   private String corporationId; @TableField("BANK_TRANCHE")
/*    */   private String bankTranche; @TableField("VALIDATE_STATUS")
/*    */   private String validateStatus;
/*    */   
/* 10 */   public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } @TableField("GROUP_FLAG") private String groupFlag; @TableField("WA_CONFIRM_FLAG") private String waConfirmFlag; @TableField("IRB_CONFIRM_FLAG") private String irbConfirmFlag; @TableField("WA_SCHEME_ID") private String waSchemeId; @TableField("IRB_SCHEME_ID") private String irbSchemeId; public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setCorporationId(String corporationId) { this.corporationId = corporationId; } public void setBankTranche(String bankTranche) { this.bankTranche = bankTranche; } public void setValidateStatus(String validateStatus) { this.validateStatus = validateStatus; } public void setGroupFlag(String groupFlag) { this.groupFlag = groupFlag; } public void setWaConfirmFlag(String waConfirmFlag) { this.waConfirmFlag = waConfirmFlag; } public void setIrbConfirmFlag(String irbConfirmFlag) { this.irbConfirmFlag = irbConfirmFlag; } public void setWaSchemeId(String waSchemeId) { this.waSchemeId = waSchemeId; } public void setIrbSchemeId(String irbSchemeId) { this.irbSchemeId = irbSchemeId; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.RiskDataPeriodDo)) return false;  com.amarsoft.rwa.engine.entity.RiskDataPeriodDo other = (com.amarsoft.rwa.engine.entity.RiskDataPeriodDo)o; if (!other.canEqual(this)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$corporationId = getCorporationId(), other$corporationId = other.getCorporationId(); if ((this$corporationId == null) ? (other$corporationId != null) : !this$corporationId.equals(other$corporationId)) return false;  Object this$bankTranche = getBankTranche(), other$bankTranche = other.getBankTranche(); if ((this$bankTranche == null) ? (other$bankTranche != null) : !this$bankTranche.equals(other$bankTranche)) return false;  Object this$validateStatus = getValidateStatus(), other$validateStatus = other.getValidateStatus(); if ((this$validateStatus == null) ? (other$validateStatus != null) : !this$validateStatus.equals(other$validateStatus)) return false;  Object this$groupFlag = getGroupFlag(), other$groupFlag = other.getGroupFlag(); if ((this$groupFlag == null) ? (other$groupFlag != null) : !this$groupFlag.equals(other$groupFlag)) return false;  Object this$waConfirmFlag = getWaConfirmFlag(), other$waConfirmFlag = other.getWaConfirmFlag(); if ((this$waConfirmFlag == null) ? (other$waConfirmFlag != null) : !this$waConfirmFlag.equals(other$waConfirmFlag)) return false;  Object this$irbConfirmFlag = getIrbConfirmFlag(), other$irbConfirmFlag = other.getIrbConfirmFlag(); if ((this$irbConfirmFlag == null) ? (other$irbConfirmFlag != null) : !this$irbConfirmFlag.equals(other$irbConfirmFlag)) return false;  Object this$waSchemeId = getWaSchemeId(), other$waSchemeId = other.getWaSchemeId(); if ((this$waSchemeId == null) ? (other$waSchemeId != null) : !this$waSchemeId.equals(other$waSchemeId)) return false;  Object this$irbSchemeId = getIrbSchemeId(), other$irbSchemeId = other.getIrbSchemeId(); return !((this$irbSchemeId == null) ? (other$irbSchemeId != null) : !this$irbSchemeId.equals(other$irbSchemeId)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.RiskDataPeriodDo; } public int hashCode() { int PRIME = 59; result = 1; Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $corporationId = getCorporationId(); result = result * 59 + (($corporationId == null) ? 43 : $corporationId.hashCode()); Object $bankTranche = getBankTranche(); result = result * 59 + (($bankTranche == null) ? 43 : $bankTranche.hashCode()); Object $validateStatus = getValidateStatus(); result = result * 59 + (($validateStatus == null) ? 43 : $validateStatus.hashCode()); Object $groupFlag = getGroupFlag(); result = result * 59 + (($groupFlag == null) ? 43 : $groupFlag.hashCode()); Object $waConfirmFlag = getWaConfirmFlag(); result = result * 59 + (($waConfirmFlag == null) ? 43 : $waConfirmFlag.hashCode()); Object $irbConfirmFlag = getIrbConfirmFlag(); result = result * 59 + (($irbConfirmFlag == null) ? 43 : $irbConfirmFlag.hashCode()); Object $waSchemeId = getWaSchemeId(); result = result * 59 + (($waSchemeId == null) ? 43 : $waSchemeId.hashCode()); Object $irbSchemeId = getIrbSchemeId(); return result * 59 + (($irbSchemeId == null) ? 43 : $irbSchemeId.hashCode()); } public String toString() { return "RiskDataPeriodDo(dataBatchNo=" + getDataBatchNo() + ", dataDate=" + getDataDate() + ", corporationId=" + getCorporationId() + ", bankTranche=" + getBankTranche() + ", validateStatus=" + getValidateStatus() + ", groupFlag=" + getGroupFlag() + ", waConfirmFlag=" + getWaConfirmFlag() + ", irbConfirmFlag=" + getIrbConfirmFlag() + ", waSchemeId=" + getWaSchemeId() + ", irbSchemeId=" + getIrbSchemeId() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDataBatchNo() {
/* 15 */     return this.dataBatchNo;
/*    */   } public Date getDataDate() {
/* 17 */     return this.dataDate;
/*    */   } public String getCorporationId() {
/* 19 */     return this.corporationId;
/*    */   } public String getBankTranche() {
/* 21 */     return this.bankTranche;
/*    */   } public String getValidateStatus() {
/* 23 */     return this.validateStatus;
/*    */   } public String getGroupFlag() {
/* 25 */     return this.groupFlag;
/*    */   } public String getWaConfirmFlag() {
/* 27 */     return this.waConfirmFlag;
/*    */   } public String getIrbConfirmFlag() {
/* 29 */     return this.irbConfirmFlag;
/*    */   } public String getWaSchemeId() {
/* 31 */     return this.waSchemeId;
/*    */   } public String getIrbSchemeId() {
/* 33 */     return this.irbSchemeId;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\RiskDataPeriodDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */