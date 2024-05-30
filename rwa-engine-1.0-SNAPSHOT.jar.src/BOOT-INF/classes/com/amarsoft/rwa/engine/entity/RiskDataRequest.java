/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ 
/*    */ public class RiskDataRequest {
/*    */   @Pattern(regexp = "[0-9A-Za-z]{1,}", message = "数据批次号输入异常")
/*    */   @NotNull(message = "数据批次号不能为空")
/*    */   private String dataBatchNo;
/*    */   @Pattern(regexp = "[0-1]{1}", message = "是否零售输入异常")
/*    */   private String isRetail;
/*    */   @Pattern(regexp = "[0-1]{1}", message = "是否重跑输入异常")
/*    */   private String isRerun;
/*    */   
/* 13 */   public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setIsRetail(String isRetail) { this.isRetail = isRetail; } public void setIsRerun(String isRerun) { this.isRerun = isRerun; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.RiskDataRequest)) return false;  com.amarsoft.rwa.engine.entity.RiskDataRequest other = (com.amarsoft.rwa.engine.entity.RiskDataRequest)o; if (!other.canEqual(this)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$isRetail = getIsRetail(), other$isRetail = other.getIsRetail(); if ((this$isRetail == null) ? (other$isRetail != null) : !this$isRetail.equals(other$isRetail)) return false;  Object this$isRerun = getIsRerun(), other$isRerun = other.getIsRerun(); return !((this$isRerun == null) ? (other$isRerun != null) : !this$isRerun.equals(other$isRerun)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.RiskDataRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $isRetail = getIsRetail(); result = result * 59 + (($isRetail == null) ? 43 : $isRetail.hashCode()); Object $isRerun = getIsRerun(); return result * 59 + (($isRerun == null) ? 43 : $isRerun.hashCode()); } public String toString() { return "RiskDataRequest(dataBatchNo=" + getDataBatchNo() + ", isRetail=" + getIsRetail() + ", isRerun=" + getIsRerun() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getDataBatchNo() {
/* 18 */     return this.dataBatchNo;
/*    */   }
/*    */   public String getIsRetail() {
/* 21 */     return this.isRetail;
/*    */   }
/*    */   public String getIsRerun() {
/* 24 */     return this.isRerun;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\RiskDataRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */