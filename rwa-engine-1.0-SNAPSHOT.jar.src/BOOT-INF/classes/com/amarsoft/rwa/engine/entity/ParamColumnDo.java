/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_PARAM_COLUMN")
/*    */ public class ParamColumnDo implements Serializable { private static final long serialVersionUID = 8234237673210007L; @TableField("PARAM_VERSION_NO")
/*    */   private String paramVersionNo; @TableField("PARAM_DETAIL_NO")
/*    */   private String paramDetailNo;
/*    */   @TableField("PARAM_TEMPLATE_COL_NO")
/*    */   private String paramTemplateColNo;
/*    */   
/*  9 */   public void setParamVersionNo(String paramVersionNo) { this.paramVersionNo = paramVersionNo; } @TableField("PARAM_TEMPLATE") private String paramTemplate; @TableField("RULE_COL_CODE") private String ruleColCode; @TableField("RULE_CONFIG_WAY") private String ruleConfigWay; @TableField("RULE_CONFIG_CONTENT") private String ruleConfigContent; @TableField("RULE_NAME") private String ruleName; public void setParamDetailNo(String paramDetailNo) { this.paramDetailNo = paramDetailNo; } public void setParamTemplateColNo(String paramTemplateColNo) { this.paramTemplateColNo = paramTemplateColNo; } public void setParamTemplate(String paramTemplate) { this.paramTemplate = paramTemplate; } public void setRuleColCode(String ruleColCode) { this.ruleColCode = ruleColCode; } public void setRuleConfigWay(String ruleConfigWay) { this.ruleConfigWay = ruleConfigWay; } public void setRuleConfigContent(String ruleConfigContent) { this.ruleConfigContent = ruleConfigContent; } public void setRuleName(String ruleName) { this.ruleName = ruleName; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ParamColumnDo)) return false;  com.amarsoft.rwa.engine.entity.ParamColumnDo other = (com.amarsoft.rwa.engine.entity.ParamColumnDo)o; if (!other.canEqual(this)) return false;  Object this$paramVersionNo = getParamVersionNo(), other$paramVersionNo = other.getParamVersionNo(); if ((this$paramVersionNo == null) ? (other$paramVersionNo != null) : !this$paramVersionNo.equals(other$paramVersionNo)) return false;  Object this$paramDetailNo = getParamDetailNo(), other$paramDetailNo = other.getParamDetailNo(); if ((this$paramDetailNo == null) ? (other$paramDetailNo != null) : !this$paramDetailNo.equals(other$paramDetailNo)) return false;  Object this$paramTemplateColNo = getParamTemplateColNo(), other$paramTemplateColNo = other.getParamTemplateColNo(); if ((this$paramTemplateColNo == null) ? (other$paramTemplateColNo != null) : !this$paramTemplateColNo.equals(other$paramTemplateColNo)) return false;  Object this$paramTemplate = getParamTemplate(), other$paramTemplate = other.getParamTemplate(); if ((this$paramTemplate == null) ? (other$paramTemplate != null) : !this$paramTemplate.equals(other$paramTemplate)) return false;  Object this$ruleColCode = getRuleColCode(), other$ruleColCode = other.getRuleColCode(); if ((this$ruleColCode == null) ? (other$ruleColCode != null) : !this$ruleColCode.equals(other$ruleColCode)) return false;  Object this$ruleConfigWay = getRuleConfigWay(), other$ruleConfigWay = other.getRuleConfigWay(); if ((this$ruleConfigWay == null) ? (other$ruleConfigWay != null) : !this$ruleConfigWay.equals(other$ruleConfigWay)) return false;  Object this$ruleConfigContent = getRuleConfigContent(), other$ruleConfigContent = other.getRuleConfigContent(); if ((this$ruleConfigContent == null) ? (other$ruleConfigContent != null) : !this$ruleConfigContent.equals(other$ruleConfigContent)) return false;  Object this$ruleName = getRuleName(), other$ruleName = other.getRuleName(); return !((this$ruleName == null) ? (other$ruleName != null) : !this$ruleName.equals(other$ruleName)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ParamColumnDo; } public int hashCode() { int PRIME = 59; result = 1; Object $paramVersionNo = getParamVersionNo(); result = result * 59 + (($paramVersionNo == null) ? 43 : $paramVersionNo.hashCode()); Object $paramDetailNo = getParamDetailNo(); result = result * 59 + (($paramDetailNo == null) ? 43 : $paramDetailNo.hashCode()); Object $paramTemplateColNo = getParamTemplateColNo(); result = result * 59 + (($paramTemplateColNo == null) ? 43 : $paramTemplateColNo.hashCode()); Object $paramTemplate = getParamTemplate(); result = result * 59 + (($paramTemplate == null) ? 43 : $paramTemplate.hashCode()); Object $ruleColCode = getRuleColCode(); result = result * 59 + (($ruleColCode == null) ? 43 : $ruleColCode.hashCode()); Object $ruleConfigWay = getRuleConfigWay(); result = result * 59 + (($ruleConfigWay == null) ? 43 : $ruleConfigWay.hashCode()); Object $ruleConfigContent = getRuleConfigContent(); result = result * 59 + (($ruleConfigContent == null) ? 43 : $ruleConfigContent.hashCode()); Object $ruleName = getRuleName(); return result * 59 + (($ruleName == null) ? 43 : $ruleName.hashCode()); } public String toString() { return "ParamColumnDo(paramVersionNo=" + getParamVersionNo() + ", paramDetailNo=" + getParamDetailNo() + ", paramTemplateColNo=" + getParamTemplateColNo() + ", paramTemplate=" + getParamTemplate() + ", ruleColCode=" + getRuleColCode() + ", ruleConfigWay=" + getRuleConfigWay() + ", ruleConfigContent=" + getRuleConfigContent() + ", ruleName=" + getRuleName() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamVersionNo() {
/* 15 */     return this.paramVersionNo;
/*    */   } public String getParamDetailNo() {
/* 17 */     return this.paramDetailNo;
/*    */   } public String getParamTemplateColNo() {
/* 19 */     return this.paramTemplateColNo;
/*    */   } public String getParamTemplate() {
/* 21 */     return this.paramTemplate;
/*    */   } public String getRuleColCode() {
/* 23 */     return this.ruleColCode;
/*    */   } public String getRuleConfigWay() {
/* 25 */     return this.ruleConfigWay;
/*    */   } public String getRuleConfigContent() {
/* 27 */     return this.ruleConfigContent;
/*    */   } public String getRuleName() {
/* 29 */     return this.ruleName;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ParamColumnDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */