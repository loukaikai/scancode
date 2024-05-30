/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import javax.validation.constraints.Pattern;
/*    */ 
/*    */ public class JobQueryRequest {
/*    */   @Pattern(regexp = "[0-9A-Za-z]{10,}", message = "结果号输入异常")
/*    */   @NotNull(message = "结果号不能为空")
/*    */   private String resultNo;
/*    */   private String jobType;
/*    */   @Pattern(regexp = "[0-1]{1}", message = "是否重跑输入异常")
/*    */   private String isRerun;
/*    */   
/* 13 */   public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setJobType(String jobType) { this.jobType = jobType; } public void setIsRerun(String isRerun) { this.isRerun = isRerun; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.JobQueryRequest)) return false;  com.amarsoft.rwa.engine.entity.JobQueryRequest other = (com.amarsoft.rwa.engine.entity.JobQueryRequest)o; if (!other.canEqual(this)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$jobType = getJobType(), other$jobType = other.getJobType(); if ((this$jobType == null) ? (other$jobType != null) : !this$jobType.equals(other$jobType)) return false;  Object this$isRerun = getIsRerun(), other$isRerun = other.getIsRerun(); return !((this$isRerun == null) ? (other$isRerun != null) : !this$isRerun.equals(other$isRerun)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.JobQueryRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $jobType = getJobType(); result = result * 59 + (($jobType == null) ? 43 : $jobType.hashCode()); Object $isRerun = getIsRerun(); return result * 59 + (($isRerun == null) ? 43 : $isRerun.hashCode()); } public String toString() { return "JobQueryRequest(resultNo=" + getResultNo() + ", jobType=" + getJobType() + ", isRerun=" + getIsRerun() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getResultNo() {
/* 18 */     return this.resultNo;
/*    */   } public String getJobType() {
/* 20 */     return this.jobType;
/*    */   }
/*    */   public String getIsRerun() {
/* 23 */     return this.isRerun;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\JobQueryRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */