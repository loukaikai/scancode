/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.MitigateSortType;
/*    */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*    */ import com.baomidou.mybatisplus.annotation.TableField;
/*    */ import com.baomidou.mybatisplus.annotation.TableName;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ 
/*    */ @TableName("RWA_EP_MITI_ASSET")
/*    */ public class MitigateAssetDo implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673220002L;
/*    */   @TableId("MITIGATE_METHOD_NO")
/*    */   private String mitigateMethodNo;
/*    */   @TableField("MITIGATE_SCHEME_NO")
/*    */   private String mitigateSchemeNo;
/*    */   @TableField("APPROACH")
/*    */   private String approach;
/*    */   
/* 20 */   public void setMitigateMethodNo(String mitigateMethodNo) { this.mitigateMethodNo = mitigateMethodNo; } @TableField("IS_DEFAULT") private String isDefault; @TableField("ASSET_TYPE") private String assetType; @TableField("MITIGATE_METHOD") private String mitigateMethod; @TableField(exist = false) private Map<MitigateSortType, List<MitigateSortDo>> mitigateSortListMap; @TableField(exist = false) private Map<String, Integer> mitigationTypeSortMap; public void setMitigateSchemeNo(String mitigateSchemeNo) { this.mitigateSchemeNo = mitigateSchemeNo; } public void setApproach(String approach) { this.approach = approach; } public void setIsDefault(String isDefault) { this.isDefault = isDefault; } public void setAssetType(String assetType) { this.assetType = assetType; } public void setMitigateMethod(String mitigateMethod) { this.mitigateMethod = mitigateMethod; } public void setMitigateSortListMap(Map<MitigateSortType, List<MitigateSortDo>> mitigateSortListMap) { this.mitigateSortListMap = mitigateSortListMap; } public void setMitigationTypeSortMap(Map<String, Integer> mitigationTypeSortMap) { this.mitigationTypeSortMap = mitigationTypeSortMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MitigateAssetDo)) return false;  com.amarsoft.rwa.engine.entity.MitigateAssetDo other = (com.amarsoft.rwa.engine.entity.MitigateAssetDo)o; if (!other.canEqual(this)) return false;  Object this$mitigateMethodNo = getMitigateMethodNo(), other$mitigateMethodNo = other.getMitigateMethodNo(); if ((this$mitigateMethodNo == null) ? (other$mitigateMethodNo != null) : !this$mitigateMethodNo.equals(other$mitigateMethodNo)) return false;  Object this$mitigateSchemeNo = getMitigateSchemeNo(), other$mitigateSchemeNo = other.getMitigateSchemeNo(); if ((this$mitigateSchemeNo == null) ? (other$mitigateSchemeNo != null) : !this$mitigateSchemeNo.equals(other$mitigateSchemeNo)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$isDefault = getIsDefault(), other$isDefault = other.getIsDefault(); if ((this$isDefault == null) ? (other$isDefault != null) : !this$isDefault.equals(other$isDefault)) return false;  Object this$assetType = getAssetType(), other$assetType = other.getAssetType(); if ((this$assetType == null) ? (other$assetType != null) : !this$assetType.equals(other$assetType)) return false;  Object this$mitigateMethod = getMitigateMethod(), other$mitigateMethod = other.getMitigateMethod(); if ((this$mitigateMethod == null) ? (other$mitigateMethod != null) : !this$mitigateMethod.equals(other$mitigateMethod)) return false;  Object<MitigateSortType, List<MitigateSortDo>> this$mitigateSortListMap = (Object<MitigateSortType, List<MitigateSortDo>>)getMitigateSortListMap(), other$mitigateSortListMap = (Object<MitigateSortType, List<MitigateSortDo>>)other.getMitigateSortListMap(); if ((this$mitigateSortListMap == null) ? (other$mitigateSortListMap != null) : !this$mitigateSortListMap.equals(other$mitigateSortListMap)) return false;  Object<String, Integer> this$mitigationTypeSortMap = (Object<String, Integer>)getMitigationTypeSortMap(), other$mitigationTypeSortMap = (Object<String, Integer>)other.getMitigationTypeSortMap(); return !((this$mitigationTypeSortMap == null) ? (other$mitigationTypeSortMap != null) : !this$mitigationTypeSortMap.equals(other$mitigationTypeSortMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MitigateAssetDo; } public int hashCode() { int PRIME = 59; result = 1; Object $mitigateMethodNo = getMitigateMethodNo(); result = result * 59 + (($mitigateMethodNo == null) ? 43 : $mitigateMethodNo.hashCode()); Object $mitigateSchemeNo = getMitigateSchemeNo(); result = result * 59 + (($mitigateSchemeNo == null) ? 43 : $mitigateSchemeNo.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $isDefault = getIsDefault(); result = result * 59 + (($isDefault == null) ? 43 : $isDefault.hashCode()); Object $assetType = getAssetType(); result = result * 59 + (($assetType == null) ? 43 : $assetType.hashCode()); Object $mitigateMethod = getMitigateMethod(); result = result * 59 + (($mitigateMethod == null) ? 43 : $mitigateMethod.hashCode()); Object<MitigateSortType, List<MitigateSortDo>> $mitigateSortListMap = (Object<MitigateSortType, List<MitigateSortDo>>)getMitigateSortListMap(); result = result * 59 + (($mitigateSortListMap == null) ? 43 : $mitigateSortListMap.hashCode()); Object<String, Integer> $mitigationTypeSortMap = (Object<String, Integer>)getMitigationTypeSortMap(); return result * 59 + (($mitigationTypeSortMap == null) ? 43 : $mitigationTypeSortMap.hashCode()); } public String toString() { return "MitigateAssetDo(mitigateMethodNo=" + getMitigateMethodNo() + ", mitigateSchemeNo=" + getMitigateSchemeNo() + ", approach=" + getApproach() + ", isDefault=" + getIsDefault() + ", assetType=" + getAssetType() + ", mitigateMethod=" + getMitigateMethod() + ", mitigateSortListMap=" + getMitigateSortListMap() + ", mitigationTypeSortMap=" + getMitigationTypeSortMap() + ")"; } public MitigateAssetDo(String mitigateMethodNo, String mitigateSchemeNo, String approach, String isDefault, String assetType, String mitigateMethod, Map<MitigateSortType, List<MitigateSortDo>> mitigateSortListMap, Map<String, Integer> mitigationTypeSortMap) {
/* 21 */     this.mitigateMethodNo = mitigateMethodNo; this.mitigateSchemeNo = mitigateSchemeNo; this.approach = approach; this.isDefault = isDefault; this.assetType = assetType; this.mitigateMethod = mitigateMethod; this.mitigateSortListMap = mitigateSortListMap; this.mitigationTypeSortMap = mitigationTypeSortMap;
/*    */   }
/*    */ 
/*    */   
/*    */   public MitigateAssetDo() {}
/*    */   
/*    */   public String getMitigateMethodNo() {
/* 28 */     return this.mitigateMethodNo;
/*    */   } public String getMitigateSchemeNo() {
/* 30 */     return this.mitigateSchemeNo;
/*    */   } public String getApproach() {
/* 32 */     return this.approach;
/*    */   } public String getIsDefault() {
/* 34 */     return this.isDefault;
/*    */   } public String getAssetType() {
/* 36 */     return this.assetType;
/*    */   } public String getMitigateMethod() {
/* 38 */     return this.mitigateMethod;
/*    */   } public Map<MitigateSortType, List<MitigateSortDo>> getMitigateSortListMap() {
/* 40 */     return this.mitigateSortListMap;
/*    */   } public Map<String, Integer> getMitigationTypeSortMap() {
/* 42 */     return this.mitigationTypeSortMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MitigateAssetDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */