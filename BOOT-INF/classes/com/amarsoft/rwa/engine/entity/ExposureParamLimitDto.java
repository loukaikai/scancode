/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ public class ExposureParamLimitDto {
/*    */   private String limitNo;
/*    */   private String exposureType;
/*    */   private String provideMitigationType;
/*    */   private BigDecimal limit;
/*    */   
/*    */   public void setLimitNo(String limitNo) {
/* 12 */     this.limitNo = limitNo; } public void setExposureType(String exposureType) { this.exposureType = exposureType; } public void setProvideMitigationType(String provideMitigationType) { this.provideMitigationType = provideMitigationType; } public void setLimit(BigDecimal limit) { this.limit = limit; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ExposureParamLimitDto)) return false;  com.amarsoft.rwa.engine.entity.ExposureParamLimitDto other = (com.amarsoft.rwa.engine.entity.ExposureParamLimitDto)o; if (!other.canEqual(this)) return false;  Object this$limitNo = getLimitNo(), other$limitNo = other.getLimitNo(); if ((this$limitNo == null) ? (other$limitNo != null) : !this$limitNo.equals(other$limitNo)) return false;  Object this$exposureType = getExposureType(), other$exposureType = other.getExposureType(); if ((this$exposureType == null) ? (other$exposureType != null) : !this$exposureType.equals(other$exposureType)) return false;  Object this$provideMitigationType = getProvideMitigationType(), other$provideMitigationType = other.getProvideMitigationType(); if ((this$provideMitigationType == null) ? (other$provideMitigationType != null) : !this$provideMitigationType.equals(other$provideMitigationType)) return false;  Object this$limit = getLimit(), other$limit = other.getLimit(); return !((this$limit == null) ? (other$limit != null) : !this$limit.equals(other$limit)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ExposureParamLimitDto; } public int hashCode() { int PRIME = 59; result = 1; Object $limitNo = getLimitNo(); result = result * 59 + (($limitNo == null) ? 43 : $limitNo.hashCode()); Object $exposureType = getExposureType(); result = result * 59 + (($exposureType == null) ? 43 : $exposureType.hashCode()); Object $provideMitigationType = getProvideMitigationType(); result = result * 59 + (($provideMitigationType == null) ? 43 : $provideMitigationType.hashCode()); Object $limit = getLimit(); return result * 59 + (($limit == null) ? 43 : $limit.hashCode()); } public String toString() { return "ExposureParamLimitDto(limitNo=" + getLimitNo() + ", exposureType=" + getExposureType() + ", provideMitigationType=" + getProvideMitigationType() + ", limit=" + getLimit() + ")"; }
/*    */ 
/*    */   
/* 15 */   public String getLimitNo() { return this.limitNo; }
/* 16 */   public String getExposureType() { return this.exposureType; }
/* 17 */   public String getProvideMitigationType() { return this.provideMitigationType; } public BigDecimal getLimit() {
/* 18 */     return this.limit;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ExposureParamLimitDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */