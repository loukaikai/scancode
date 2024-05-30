/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Pattern;
/*    */ 
/*    */ public class JobRequest {
/*    */   @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "结果号输入异常")
/*    */   @NotNull(message = "结果号不能为空")
/*    */   private String resultNo;
/*    */   @NotNull(message = "计算方法不能为空")
/*    */   private String approach;
/*    */   
/* 13 */   public void setResultNo(String resultNo) { this.resultNo = resultNo; } @Pattern(regexp = "[0-9A-Za-z]{1,}", message = "数据批次号输入异常") @NotNull(message = "数据批次号不能为空") private String dataBatchNo; @NotNull(message = "作业不能为空") private String jobType; @Pattern(regexp = "[0-1]{1}", message = "是否重跑输入异常") private String isRerun; public void setApproach(String approach) { this.approach = approach; } public void setDataBatchNo(String dataBatchNo) { this.dataBatchNo = dataBatchNo; } public void setJobType(String jobType) { this.jobType = jobType; } public void setIsRerun(String isRerun) { this.isRerun = isRerun; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.JobRequest)) return false;  com.amarsoft.rwa.engine.entity.JobRequest other = (com.amarsoft.rwa.engine.entity.JobRequest)o; if (!other.canEqual(this)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$dataBatchNo = getDataBatchNo(), other$dataBatchNo = other.getDataBatchNo(); if ((this$dataBatchNo == null) ? (other$dataBatchNo != null) : !this$dataBatchNo.equals(other$dataBatchNo)) return false;  Object this$jobType = getJobType(), other$jobType = other.getJobType(); if ((this$jobType == null) ? (other$jobType != null) : !this$jobType.equals(other$jobType)) return false;  Object this$isRerun = getIsRerun(), other$isRerun = other.getIsRerun(); return !((this$isRerun == null) ? (other$isRerun != null) : !this$isRerun.equals(other$isRerun)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.JobRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $dataBatchNo = getDataBatchNo(); result = result * 59 + (($dataBatchNo == null) ? 43 : $dataBatchNo.hashCode()); Object $jobType = getJobType(); result = result * 59 + (($jobType == null) ? 43 : $jobType.hashCode()); Object $isRerun = getIsRerun(); return result * 59 + (($isRerun == null) ? 43 : $isRerun.hashCode()); } public String toString() { return "JobRequest(resultNo=" + getResultNo() + ", approach=" + getApproach() + ", dataBatchNo=" + getDataBatchNo() + ", jobType=" + getJobType() + ", isRerun=" + getIsRerun() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResultNo() {
/* 18 */     return this.resultNo;
/*    */   }
/*    */   public String getApproach() {
/* 21 */     return this.approach;
/*    */   }
/*    */   
/*    */   public String getDataBatchNo() {
/* 25 */     return this.dataBatchNo;
/*    */   }
/*    */   public String getJobType() {
/* 28 */     return this.jobType;
/*    */   }
/*    */   public String getIsRerun() {
/* 31 */     return this.isRerun;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\JobRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */