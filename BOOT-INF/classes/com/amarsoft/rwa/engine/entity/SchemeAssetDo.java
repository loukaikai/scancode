/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.baomidou.mybatisplus.annotation.TableField;
/*    */ import com.baomidou.mybatisplus.annotation.TableName;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ @TableName("RWA_EP_SCHEME_APPROACH")
/*    */ public class SchemeAssetDo implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673210002L;
/*    */   @TableId("APPROACH_NO")
/*    */   private String approachNo;
/*    */   @TableField("SCHEME_ID")
/*    */   private String schemeId;
/*    */   
/* 15 */   public void setApproachNo(String approachNo) { this.approachNo = approachNo; } @TableField("ASSET_TYPE") private String assetType; @TableField("EXPOSURE_TYPE") private String exposureType; @TableField("APPROACH") private String approach; public void setSchemeId(String schemeId) { this.schemeId = schemeId; } public void setAssetType(String assetType) { this.assetType = assetType; } public void setExposureType(String exposureType) { this.exposureType = exposureType; } public void setApproach(String approach) { this.approach = approach; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.SchemeAssetDo)) return false;  com.amarsoft.rwa.engine.entity.SchemeAssetDo other = (com.amarsoft.rwa.engine.entity.SchemeAssetDo)o; if (!other.canEqual(this)) return false;  Object this$approachNo = getApproachNo(), other$approachNo = other.getApproachNo(); if ((this$approachNo == null) ? (other$approachNo != null) : !this$approachNo.equals(other$approachNo)) return false;  Object this$schemeId = getSchemeId(), other$schemeId = other.getSchemeId(); if ((this$schemeId == null) ? (other$schemeId != null) : !this$schemeId.equals(other$schemeId)) return false;  Object this$assetType = getAssetType(), other$assetType = other.getAssetType(); if ((this$assetType == null) ? (other$assetType != null) : !this$assetType.equals(other$assetType)) return false;  Object this$exposureType = getExposureType(), other$exposureType = other.getExposureType(); if ((this$exposureType == null) ? (other$exposureType != null) : !this$exposureType.equals(other$exposureType)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); return !((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.SchemeAssetDo; } public int hashCode() { int PRIME = 59; result = 1; Object $approachNo = getApproachNo(); result = result * 59 + (($approachNo == null) ? 43 : $approachNo.hashCode()); Object $schemeId = getSchemeId(); result = result * 59 + (($schemeId == null) ? 43 : $schemeId.hashCode()); Object $assetType = getAssetType(); result = result * 59 + (($assetType == null) ? 43 : $assetType.hashCode()); Object $exposureType = getExposureType(); result = result * 59 + (($exposureType == null) ? 43 : $exposureType.hashCode()); Object $approach = getApproach(); return result * 59 + (($approach == null) ? 43 : $approach.hashCode()); } public String toString() { return "SchemeAssetDo(approachNo=" + getApproachNo() + ", schemeId=" + getSchemeId() + ", assetType=" + getAssetType() + ", exposureType=" + getExposureType() + ", approach=" + getApproach() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getApproachNo() {
/* 21 */     return this.approachNo;
/*    */   } public String getSchemeId() {
/* 23 */     return this.schemeId;
/*    */   } public String getAssetType() {
/* 25 */     return this.assetType;
/*    */   } public String getExposureType() {
/* 27 */     return this.exposureType;
/*    */   } public String getApproach() {
/* 29 */     return this.approach;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\SchemeAssetDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */