/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_PARAM_TPL")
/*    */ public class ParamTemplateDo implements Serializable { private static final long serialVersionUID = 8234237673210005L; @TableField("PARAM_VERSION_NO")
/*    */   private String paramVersionNo; @TableField("PARAM_TEMPLATE_COL_NO")
/*    */   private String paramTemplateColNo; @TableField("PARAM_TEMPLATE")
/*    */   private String paramTemplate; @TableField("APPROACH")
/*    */   private String approach; @TableField("RULE_COL_CODE")
/*    */   private String ruleColCode;
/*  9 */   public void setParamVersionNo(String paramVersionNo) { this.paramVersionNo = paramVersionNo; } @TableField("RULE_COL_NAME") private String ruleColName; @TableField("RULE_COL_ATTR") private String ruleColAttr; @TableField("RULE_COL_TYPE") private String ruleColType; @TableField("COL_FORMAT") private String colFormat; @TableField("IS_LIST_SHOW") private String isListShow; @TableField("SORT_NO") private Integer sortNo; public void setParamTemplateColNo(String paramTemplateColNo) { this.paramTemplateColNo = paramTemplateColNo; } public void setParamTemplate(String paramTemplate) { this.paramTemplate = paramTemplate; } public void setApproach(String approach) { this.approach = approach; } public void setRuleColCode(String ruleColCode) { this.ruleColCode = ruleColCode; } public void setRuleColName(String ruleColName) { this.ruleColName = ruleColName; } public void setRuleColAttr(String ruleColAttr) { this.ruleColAttr = ruleColAttr; } public void setRuleColType(String ruleColType) { this.ruleColType = ruleColType; } public void setColFormat(String colFormat) { this.colFormat = colFormat; } public void setIsListShow(String isListShow) { this.isListShow = isListShow; } public void setSortNo(Integer sortNo) { this.sortNo = sortNo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ParamTemplateDo)) return false;  com.amarsoft.rwa.engine.entity.ParamTemplateDo other = (com.amarsoft.rwa.engine.entity.ParamTemplateDo)o; if (!other.canEqual(this)) return false;  Object this$sortNo = getSortNo(), other$sortNo = other.getSortNo(); if ((this$sortNo == null) ? (other$sortNo != null) : !this$sortNo.equals(other$sortNo)) return false;  Object this$paramVersionNo = getParamVersionNo(), other$paramVersionNo = other.getParamVersionNo(); if ((this$paramVersionNo == null) ? (other$paramVersionNo != null) : !this$paramVersionNo.equals(other$paramVersionNo)) return false;  Object this$paramTemplateColNo = getParamTemplateColNo(), other$paramTemplateColNo = other.getParamTemplateColNo(); if ((this$paramTemplateColNo == null) ? (other$paramTemplateColNo != null) : !this$paramTemplateColNo.equals(other$paramTemplateColNo)) return false;  Object this$paramTemplate = getParamTemplate(), other$paramTemplate = other.getParamTemplate(); if ((this$paramTemplate == null) ? (other$paramTemplate != null) : !this$paramTemplate.equals(other$paramTemplate)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$ruleColCode = getRuleColCode(), other$ruleColCode = other.getRuleColCode(); if ((this$ruleColCode == null) ? (other$ruleColCode != null) : !this$ruleColCode.equals(other$ruleColCode)) return false;  Object this$ruleColName = getRuleColName(), other$ruleColName = other.getRuleColName(); if ((this$ruleColName == null) ? (other$ruleColName != null) : !this$ruleColName.equals(other$ruleColName)) return false;  Object this$ruleColAttr = getRuleColAttr(), other$ruleColAttr = other.getRuleColAttr(); if ((this$ruleColAttr == null) ? (other$ruleColAttr != null) : !this$ruleColAttr.equals(other$ruleColAttr)) return false;  Object this$ruleColType = getRuleColType(), other$ruleColType = other.getRuleColType(); if ((this$ruleColType == null) ? (other$ruleColType != null) : !this$ruleColType.equals(other$ruleColType)) return false;  Object this$colFormat = getColFormat(), other$colFormat = other.getColFormat(); if ((this$colFormat == null) ? (other$colFormat != null) : !this$colFormat.equals(other$colFormat)) return false;  Object this$isListShow = getIsListShow(), other$isListShow = other.getIsListShow(); return !((this$isListShow == null) ? (other$isListShow != null) : !this$isListShow.equals(other$isListShow)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ParamTemplateDo; } public int hashCode() { int PRIME = 59; result = 1; Object $sortNo = getSortNo(); result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode()); Object $paramVersionNo = getParamVersionNo(); result = result * 59 + (($paramVersionNo == null) ? 43 : $paramVersionNo.hashCode()); Object $paramTemplateColNo = getParamTemplateColNo(); result = result * 59 + (($paramTemplateColNo == null) ? 43 : $paramTemplateColNo.hashCode()); Object $paramTemplate = getParamTemplate(); result = result * 59 + (($paramTemplate == null) ? 43 : $paramTemplate.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $ruleColCode = getRuleColCode(); result = result * 59 + (($ruleColCode == null) ? 43 : $ruleColCode.hashCode()); Object $ruleColName = getRuleColName(); result = result * 59 + (($ruleColName == null) ? 43 : $ruleColName.hashCode()); Object $ruleColAttr = getRuleColAttr(); result = result * 59 + (($ruleColAttr == null) ? 43 : $ruleColAttr.hashCode()); Object $ruleColType = getRuleColType(); result = result * 59 + (($ruleColType == null) ? 43 : $ruleColType.hashCode()); Object $colFormat = getColFormat(); result = result * 59 + (($colFormat == null) ? 43 : $colFormat.hashCode()); Object $isListShow = getIsListShow(); return result * 59 + (($isListShow == null) ? 43 : $isListShow.hashCode()); } public String toString() { return "ParamTemplateDo(paramVersionNo=" + getParamVersionNo() + ", paramTemplateColNo=" + getParamTemplateColNo() + ", paramTemplate=" + getParamTemplate() + ", approach=" + getApproach() + ", ruleColCode=" + getRuleColCode() + ", ruleColName=" + getRuleColName() + ", ruleColAttr=" + getRuleColAttr() + ", ruleColType=" + getRuleColType() + ", colFormat=" + getColFormat() + ", isListShow=" + getIsListShow() + ", sortNo=" + getSortNo() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamVersionNo() {
/* 15 */     return this.paramVersionNo;
/*    */   } public String getParamTemplateColNo() {
/* 17 */     return this.paramTemplateColNo;
/*    */   } public String getParamTemplate() {
/* 19 */     return this.paramTemplate;
/*    */   } public String getApproach() {
/* 21 */     return this.approach;
/*    */   } public String getRuleColCode() {
/* 23 */     return this.ruleColCode;
/*    */   } public String getRuleColName() {
/* 25 */     return this.ruleColName;
/*    */   } public String getRuleColAttr() {
/* 27 */     return this.ruleColAttr;
/*    */   } public String getRuleColType() {
/* 29 */     return this.ruleColType;
/*    */   } public String getColFormat() {
/* 31 */     return this.colFormat;
/*    */   } public String getIsListShow() {
/* 33 */     return this.isListShow;
/*    */   } public Integer getSortNo() {
/* 35 */     return this.sortNo;
/*    */   } }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ParamTemplateDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */