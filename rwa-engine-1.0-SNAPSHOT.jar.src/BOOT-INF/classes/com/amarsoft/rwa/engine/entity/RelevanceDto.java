/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ public class RelevanceDto {
/*    */   private String groupId;
/*    */   private String exposureId;
/*    */   private String mitigationId;
/*    */   private String mitigationType;
/*    */   private String isPositiveCorrelation;
/*    */   
/* 10 */   public void setGroupId(String groupId) { this.groupId = groupId; } public void setExposureId(String exposureId) { this.exposureId = exposureId; } public void setMitigationId(String mitigationId) { this.mitigationId = mitigationId; } public void setMitigationType(String mitigationType) { this.mitigationType = mitigationType; } public void setIsPositiveCorrelation(String isPositiveCorrelation) { this.isPositiveCorrelation = isPositiveCorrelation; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.RelevanceDto)) return false;  com.amarsoft.rwa.engine.entity.RelevanceDto other = (com.amarsoft.rwa.engine.entity.RelevanceDto)o; if (!other.canEqual(this)) return false;  Object this$groupId = getGroupId(), other$groupId = other.getGroupId(); if ((this$groupId == null) ? (other$groupId != null) : !this$groupId.equals(other$groupId)) return false;  Object this$exposureId = getExposureId(), other$exposureId = other.getExposureId(); if ((this$exposureId == null) ? (other$exposureId != null) : !this$exposureId.equals(other$exposureId)) return false;  Object this$mitigationId = getMitigationId(), other$mitigationId = other.getMitigationId(); if ((this$mitigationId == null) ? (other$mitigationId != null) : !this$mitigationId.equals(other$mitigationId)) return false;  Object this$mitigationType = getMitigationType(), other$mitigationType = other.getMitigationType(); if ((this$mitigationType == null) ? (other$mitigationType != null) : !this$mitigationType.equals(other$mitigationType)) return false;  Object this$isPositiveCorrelation = getIsPositiveCorrelation(), other$isPositiveCorrelation = other.getIsPositiveCorrelation(); return !((this$isPositiveCorrelation == null) ? (other$isPositiveCorrelation != null) : !this$isPositiveCorrelation.equals(other$isPositiveCorrelation)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.RelevanceDto; } public int hashCode() { int PRIME = 59; result = 1; Object $groupId = getGroupId(); result = result * 59 + (($groupId == null) ? 43 : $groupId.hashCode()); Object $exposureId = getExposureId(); result = result * 59 + (($exposureId == null) ? 43 : $exposureId.hashCode()); Object $mitigationId = getMitigationId(); result = result * 59 + (($mitigationId == null) ? 43 : $mitigationId.hashCode()); Object $mitigationType = getMitigationType(); result = result * 59 + (($mitigationType == null) ? 43 : $mitigationType.hashCode()); Object $isPositiveCorrelation = getIsPositiveCorrelation(); return result * 59 + (($isPositiveCorrelation == null) ? 43 : $isPositiveCorrelation.hashCode()); } public String toString() { return "RelevanceDto(groupId=" + getGroupId() + ", exposureId=" + getExposureId() + ", mitigationId=" + getMitigationId() + ", mitigationType=" + getMitigationType() + ", isPositiveCorrelation=" + getIsPositiveCorrelation() + ")"; }
/*    */ 
/*    */   
/* 13 */   public String getGroupId() { return this.groupId; }
/* 14 */   public String getExposureId() { return this.exposureId; }
/* 15 */   public String getMitigationId() { return this.mitigationId; }
/* 16 */   public String getMitigationType() { return this.mitigationType; } public String getIsPositiveCorrelation() {
/* 17 */     return this.isPositiveCorrelation;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\RelevanceDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */