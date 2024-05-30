/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EL_EDATA")
/*    */ public class ExcDataDo {
/*    */   @TableId("serial_no")
/*    */   private String serialNo;
/*    */   @TableField("result_no")
/*    */   private String resultNo;
/*    */   @TableField("job_id")
/*    */   private String jobId;
/*    */   @TableField("job_type")
/*    */   private String jobType;
/*    */   
/* 13 */   public void setSerialNo(String serialNo) { this.serialNo = serialNo; } @TableField("belong_table") private String belongTable; @TableField("data_id") private String dataId; @TableField("error_code") private Integer excCode; @TableField(exist = false) private Integer count; public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setJobId(String jobId) { this.jobId = jobId; } public void setJobType(String jobType) { this.jobType = jobType; } public void setBelongTable(String belongTable) { this.belongTable = belongTable; } public void setDataId(String dataId) { this.dataId = dataId; } public void setExcCode(Integer excCode) { this.excCode = excCode; } public void setCount(Integer count) { this.count = count; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ExcDataDo)) return false;  com.amarsoft.rwa.engine.entity.ExcDataDo other = (com.amarsoft.rwa.engine.entity.ExcDataDo)o; if (!other.canEqual(this)) return false;  Object this$excCode = getExcCode(), other$excCode = other.getExcCode(); if ((this$excCode == null) ? (other$excCode != null) : !this$excCode.equals(other$excCode)) return false;  Object this$count = getCount(), other$count = other.getCount(); if ((this$count == null) ? (other$count != null) : !this$count.equals(other$count)) return false;  Object this$serialNo = getSerialNo(), other$serialNo = other.getSerialNo(); if ((this$serialNo == null) ? (other$serialNo != null) : !this$serialNo.equals(other$serialNo)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$jobId = getJobId(), other$jobId = other.getJobId(); if ((this$jobId == null) ? (other$jobId != null) : !this$jobId.equals(other$jobId)) return false;  Object this$jobType = getJobType(), other$jobType = other.getJobType(); if ((this$jobType == null) ? (other$jobType != null) : !this$jobType.equals(other$jobType)) return false;  Object this$belongTable = getBelongTable(), other$belongTable = other.getBelongTable(); if ((this$belongTable == null) ? (other$belongTable != null) : !this$belongTable.equals(other$belongTable)) return false;  Object this$dataId = getDataId(), other$dataId = other.getDataId(); return !((this$dataId == null) ? (other$dataId != null) : !this$dataId.equals(other$dataId)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ExcDataDo; } public int hashCode() { int PRIME = 59; result = 1; Object $excCode = getExcCode(); result = result * 59 + (($excCode == null) ? 43 : $excCode.hashCode()); Object $count = getCount(); result = result * 59 + (($count == null) ? 43 : $count.hashCode()); Object $serialNo = getSerialNo(); result = result * 59 + (($serialNo == null) ? 43 : $serialNo.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $jobId = getJobId(); result = result * 59 + (($jobId == null) ? 43 : $jobId.hashCode()); Object $jobType = getJobType(); result = result * 59 + (($jobType == null) ? 43 : $jobType.hashCode()); Object $belongTable = getBelongTable(); result = result * 59 + (($belongTable == null) ? 43 : $belongTable.hashCode()); Object $dataId = getDataId(); return result * 59 + (($dataId == null) ? 43 : $dataId.hashCode()); } public String toString() { return "ExcDataDo(serialNo=" + getSerialNo() + ", resultNo=" + getResultNo() + ", jobId=" + getJobId() + ", jobType=" + getJobType() + ", belongTable=" + getBelongTable() + ", dataId=" + getDataId() + ", excCode=" + getExcCode() + ", count=" + getCount() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getSerialNo() {
/* 18 */     return this.serialNo;
/*    */   } public String getResultNo() {
/* 20 */     return this.resultNo;
/*    */   } public String getJobId() {
/* 22 */     return this.jobId;
/*    */   } public String getJobType() {
/* 24 */     return this.jobType;
/*    */   } public String getBelongTable() {
/* 26 */     return this.belongTable;
/*    */   } public String getDataId() {
/* 28 */     return this.dataId;
/*    */   } public Integer getExcCode() {
/* 30 */     return this.excCode;
/*    */   } public Integer getCount() {
/* 32 */     return this.count;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ExcDataDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */