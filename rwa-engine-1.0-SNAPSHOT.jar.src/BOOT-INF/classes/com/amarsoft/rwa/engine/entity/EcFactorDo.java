/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_EC_FACTOR")
/*    */ public class EcFactorDo implements Serializable { private static final long serialVersionUID = 8234237673210011L;
/*    */   @TableId("PARAM_ID")
/*    */   private String paramId;
/*    */   @TableField("EC_ADJ_DIM")
/*    */   private String ecAdjDim;
/*    */   @TableField("EC_ADJ_SCOPE")
/*    */   private String ecAdjScope;
/*    */   @TableField("EC_ADJ_FACTOR")
/*    */   private BigDecimal ecAdjFactor;
/*    */   
/* 13 */   public void setParamId(String paramId) { this.paramId = paramId; } @TableField("IS_ALLOW_OVERLAY") private String isAllowOverlay; @TableField("EFFECTIVE_DATE") private Date effectiveDate; @TableField("PARAM_STATUS") private String paramStatus; @TableField("CHK_STATUS") private String chkStatus; @TableField(exist = false) private TreeMap<String, ColumnScopeDto> adjScopeMap; @TableField(exist = false) private String scopeValue; public void setEcAdjDim(String ecAdjDim) { this.ecAdjDim = ecAdjDim; } public void setEcAdjScope(String ecAdjScope) { this.ecAdjScope = ecAdjScope; } public void setEcAdjFactor(BigDecimal ecAdjFactor) { this.ecAdjFactor = ecAdjFactor; } public void setIsAllowOverlay(String isAllowOverlay) { this.isAllowOverlay = isAllowOverlay; } public void setEffectiveDate(Date effectiveDate) { this.effectiveDate = effectiveDate; } public void setParamStatus(String paramStatus) { this.paramStatus = paramStatus; } public void setChkStatus(String chkStatus) { this.chkStatus = chkStatus; } public void setAdjScopeMap(TreeMap<String, ColumnScopeDto> adjScopeMap) { this.adjScopeMap = adjScopeMap; } public void setScopeValue(String scopeValue) { this.scopeValue = scopeValue; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.EcFactorDo)) return false;  com.amarsoft.rwa.engine.entity.EcFactorDo other = (com.amarsoft.rwa.engine.entity.EcFactorDo)o; if (!other.canEqual(this)) return false;  Object this$paramId = getParamId(), other$paramId = other.getParamId(); if ((this$paramId == null) ? (other$paramId != null) : !this$paramId.equals(other$paramId)) return false;  Object this$ecAdjDim = getEcAdjDim(), other$ecAdjDim = other.getEcAdjDim(); if ((this$ecAdjDim == null) ? (other$ecAdjDim != null) : !this$ecAdjDim.equals(other$ecAdjDim)) return false;  Object this$ecAdjScope = getEcAdjScope(), other$ecAdjScope = other.getEcAdjScope(); if ((this$ecAdjScope == null) ? (other$ecAdjScope != null) : !this$ecAdjScope.equals(other$ecAdjScope)) return false;  Object this$ecAdjFactor = getEcAdjFactor(), other$ecAdjFactor = other.getEcAdjFactor(); if ((this$ecAdjFactor == null) ? (other$ecAdjFactor != null) : !this$ecAdjFactor.equals(other$ecAdjFactor)) return false;  Object this$isAllowOverlay = getIsAllowOverlay(), other$isAllowOverlay = other.getIsAllowOverlay(); if ((this$isAllowOverlay == null) ? (other$isAllowOverlay != null) : !this$isAllowOverlay.equals(other$isAllowOverlay)) return false;  Object this$effectiveDate = getEffectiveDate(), other$effectiveDate = other.getEffectiveDate(); if ((this$effectiveDate == null) ? (other$effectiveDate != null) : !this$effectiveDate.equals(other$effectiveDate)) return false;  Object this$paramStatus = getParamStatus(), other$paramStatus = other.getParamStatus(); if ((this$paramStatus == null) ? (other$paramStatus != null) : !this$paramStatus.equals(other$paramStatus)) return false;  Object this$chkStatus = getChkStatus(), other$chkStatus = other.getChkStatus(); if ((this$chkStatus == null) ? (other$chkStatus != null) : !this$chkStatus.equals(other$chkStatus)) return false;  Object<String, ColumnScopeDto> this$adjScopeMap = (Object<String, ColumnScopeDto>)getAdjScopeMap(), other$adjScopeMap = (Object<String, ColumnScopeDto>)other.getAdjScopeMap(); if ((this$adjScopeMap == null) ? (other$adjScopeMap != null) : !this$adjScopeMap.equals(other$adjScopeMap)) return false;  Object this$scopeValue = getScopeValue(), other$scopeValue = other.getScopeValue(); return !((this$scopeValue == null) ? (other$scopeValue != null) : !this$scopeValue.equals(other$scopeValue)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.EcFactorDo; } public int hashCode() { int PRIME = 59; result = 1; Object $paramId = getParamId(); result = result * 59 + (($paramId == null) ? 43 : $paramId.hashCode()); Object $ecAdjDim = getEcAdjDim(); result = result * 59 + (($ecAdjDim == null) ? 43 : $ecAdjDim.hashCode()); Object $ecAdjScope = getEcAdjScope(); result = result * 59 + (($ecAdjScope == null) ? 43 : $ecAdjScope.hashCode()); Object $ecAdjFactor = getEcAdjFactor(); result = result * 59 + (($ecAdjFactor == null) ? 43 : $ecAdjFactor.hashCode()); Object $isAllowOverlay = getIsAllowOverlay(); result = result * 59 + (($isAllowOverlay == null) ? 43 : $isAllowOverlay.hashCode()); Object $effectiveDate = getEffectiveDate(); result = result * 59 + (($effectiveDate == null) ? 43 : $effectiveDate.hashCode()); Object $paramStatus = getParamStatus(); result = result * 59 + (($paramStatus == null) ? 43 : $paramStatus.hashCode()); Object $chkStatus = getChkStatus(); result = result * 59 + (($chkStatus == null) ? 43 : $chkStatus.hashCode()); Object<String, ColumnScopeDto> $adjScopeMap = (Object<String, ColumnScopeDto>)getAdjScopeMap(); result = result * 59 + (($adjScopeMap == null) ? 43 : $adjScopeMap.hashCode()); Object $scopeValue = getScopeValue(); return result * 59 + (($scopeValue == null) ? 43 : $scopeValue.hashCode()); } public String toString() { return "EcFactorDo(paramId=" + getParamId() + ", ecAdjDim=" + getEcAdjDim() + ", ecAdjScope=" + getEcAdjScope() + ", ecAdjFactor=" + getEcAdjFactor() + ", isAllowOverlay=" + getIsAllowOverlay() + ", effectiveDate=" + getEffectiveDate() + ", paramStatus=" + getParamStatus() + ", chkStatus=" + getChkStatus() + ", adjScopeMap=" + getAdjScopeMap() + ", scopeValue=" + getScopeValue() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamId() {
/* 20 */     return this.paramId;
/*    */   } public String getEcAdjDim() {
/* 22 */     return this.ecAdjDim;
/*    */   } public String getEcAdjScope() {
/* 24 */     return this.ecAdjScope;
/*    */   } public BigDecimal getEcAdjFactor() {
/* 26 */     return this.ecAdjFactor;
/*    */   } public String getIsAllowOverlay() {
/* 28 */     return this.isAllowOverlay;
/*    */   } public Date getEffectiveDate() {
/* 30 */     return this.effectiveDate;
/*    */   } public String getParamStatus() {
/* 32 */     return this.paramStatus;
/*    */   } public String getChkStatus() {
/* 34 */     return this.chkStatus;
/*    */   } public TreeMap<String, ColumnScopeDto> getAdjScopeMap() {
/* 36 */     return this.adjScopeMap;
/*    */   } public String getScopeValue() {
/* 38 */     return this.scopeValue;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\EcFactorDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */