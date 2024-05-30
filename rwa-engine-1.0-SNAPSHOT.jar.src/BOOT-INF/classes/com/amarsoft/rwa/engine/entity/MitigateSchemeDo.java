/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ @TableName("RWA_EP_MITI_SCHEME")
/*    */ public class MitigateSchemeDo implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673220001L;
/*    */   @TableId("MITIGATE_SCHEME_NO")
/*    */   private String mitigateSchemeNo;
/*    */   @TableField("APPROACH")
/*    */   private String approach;
/*    */   
/* 11 */   public void setMitigateSchemeNo(String mitigateSchemeNo) { this.mitigateSchemeNo = mitigateSchemeNo; } @TableField("MITIGATE_SCHEME_NAME") private String mitigateSchemeName; @TableField(exist = false) private MitigateAssetDo defaultMitigateAsset; @TableField(exist = false) private Map<String, MitigateAssetDo> mitigateAssetDoMap; public void setApproach(String approach) { this.approach = approach; } public void setMitigateSchemeName(String mitigateSchemeName) { this.mitigateSchemeName = mitigateSchemeName; } public void setDefaultMitigateAsset(MitigateAssetDo defaultMitigateAsset) { this.defaultMitigateAsset = defaultMitigateAsset; } public void setMitigateAssetDoMap(Map<String, MitigateAssetDo> mitigateAssetDoMap) { this.mitigateAssetDoMap = mitigateAssetDoMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MitigateSchemeDo)) return false;  com.amarsoft.rwa.engine.entity.MitigateSchemeDo other = (com.amarsoft.rwa.engine.entity.MitigateSchemeDo)o; if (!other.canEqual(this)) return false;  Object this$mitigateSchemeNo = getMitigateSchemeNo(), other$mitigateSchemeNo = other.getMitigateSchemeNo(); if ((this$mitigateSchemeNo == null) ? (other$mitigateSchemeNo != null) : !this$mitigateSchemeNo.equals(other$mitigateSchemeNo)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$mitigateSchemeName = getMitigateSchemeName(), other$mitigateSchemeName = other.getMitigateSchemeName(); if ((this$mitigateSchemeName == null) ? (other$mitigateSchemeName != null) : !this$mitigateSchemeName.equals(other$mitigateSchemeName)) return false;  Object this$defaultMitigateAsset = getDefaultMitigateAsset(), other$defaultMitigateAsset = other.getDefaultMitigateAsset(); if ((this$defaultMitigateAsset == null) ? (other$defaultMitigateAsset != null) : !this$defaultMitigateAsset.equals(other$defaultMitigateAsset)) return false;  Object<String, MitigateAssetDo> this$mitigateAssetDoMap = (Object<String, MitigateAssetDo>)getMitigateAssetDoMap(), other$mitigateAssetDoMap = (Object<String, MitigateAssetDo>)other.getMitigateAssetDoMap(); return !((this$mitigateAssetDoMap == null) ? (other$mitigateAssetDoMap != null) : !this$mitigateAssetDoMap.equals(other$mitigateAssetDoMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MitigateSchemeDo; } public int hashCode() { int PRIME = 59; result = 1; Object $mitigateSchemeNo = getMitigateSchemeNo(); result = result * 59 + (($mitigateSchemeNo == null) ? 43 : $mitigateSchemeNo.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $mitigateSchemeName = getMitigateSchemeName(); result = result * 59 + (($mitigateSchemeName == null) ? 43 : $mitigateSchemeName.hashCode()); Object $defaultMitigateAsset = getDefaultMitigateAsset(); result = result * 59 + (($defaultMitigateAsset == null) ? 43 : $defaultMitigateAsset.hashCode()); Object<String, MitigateAssetDo> $mitigateAssetDoMap = (Object<String, MitigateAssetDo>)getMitigateAssetDoMap(); return result * 59 + (($mitigateAssetDoMap == null) ? 43 : $mitigateAssetDoMap.hashCode()); } public String toString() { return "MitigateSchemeDo(mitigateSchemeNo=" + getMitigateSchemeNo() + ", approach=" + getApproach() + ", mitigateSchemeName=" + getMitigateSchemeName() + ", defaultMitigateAsset=" + getDefaultMitigateAsset() + ", mitigateAssetDoMap=" + getMitigateAssetDoMap() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getMitigateSchemeNo() {
/* 17 */     return this.mitigateSchemeNo;
/*    */   } public String getApproach() {
/* 19 */     return this.approach;
/*    */   } public String getMitigateSchemeName() {
/* 21 */     return this.mitigateSchemeName;
/*    */   } public MitigateAssetDo getDefaultMitigateAsset() {
/* 23 */     return this.defaultMitigateAsset;
/*    */   } public Map<String, MitigateAssetDo> getMitigateAssetDoMap() {
/* 25 */     return this.mitigateAssetDoMap;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MitigateSchemeDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */