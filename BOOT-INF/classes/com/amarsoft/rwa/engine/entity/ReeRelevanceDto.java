/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ReeRelevanceDto
/*    */ {
/*    */   private String exposureId;
/*    */   private BigDecimal balance;
/*    */   
/*    */   public void setExposureId(String exposureId) {
/* 14 */     this.exposureId = exposureId; } public void setBalance(BigDecimal balance) { this.balance = balance; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ReeRelevanceDto)) return false;  com.amarsoft.rwa.engine.entity.ReeRelevanceDto other = (com.amarsoft.rwa.engine.entity.ReeRelevanceDto)o; if (!other.canEqual(this)) return false;  Object this$exposureId = getExposureId(), other$exposureId = other.getExposureId(); if ((this$exposureId == null) ? (other$exposureId != null) : !this$exposureId.equals(other$exposureId)) return false;  Object this$balance = getBalance(), other$balance = other.getBalance(); return !((this$balance == null) ? (other$balance != null) : !this$balance.equals(other$balance)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ReeRelevanceDto; } public int hashCode() { int PRIME = 59; result = 1; Object $exposureId = getExposureId(); result = result * 59 + (($exposureId == null) ? 43 : $exposureId.hashCode()); Object $balance = getBalance(); return result * 59 + (($balance == null) ? 43 : $balance.hashCode()); } public String toString() { return "ReeRelevanceDto(exposureId=" + getExposureId() + ", balance=" + getBalance() + ")"; }
/*    */    public ReeRelevanceDto() {} public ReeRelevanceDto(String exposureId, BigDecimal balance) {
/* 16 */     this.exposureId = exposureId; this.balance = balance;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getExposureId() {
/* 21 */     return this.exposureId; } public BigDecimal getBalance() {
/* 22 */     return this.balance;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ReeRelevanceDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */