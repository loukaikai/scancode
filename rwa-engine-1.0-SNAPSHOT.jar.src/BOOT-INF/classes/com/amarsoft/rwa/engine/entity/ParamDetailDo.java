/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ @TableName("RWA_EP_PARAM_DETAIL")
/*    */ public class ParamDetailDo implements Serializable { private static final long serialVersionUID = 8234237673210006L;
/*    */   @TableField("PARAM_VERSION_NO")
/*    */   private String paramVersionNo;
/*    */   @TableField("PARAM_DETAIL_NO")
/*    */   private String paramDetailNo;
/*    */   
/*  9 */   public void setParamVersionNo(String paramVersionNo) { this.paramVersionNo = paramVersionNo; } @TableField("PARAM_TEMPLATE") private String paramTemplate; @TableField("SORT_NO") private Integer sortNo; @TableField("REMARK") private String remark; public void setParamDetailNo(String paramDetailNo) { this.paramDetailNo = paramDetailNo; } public void setParamTemplate(String paramTemplate) { this.paramTemplate = paramTemplate; } public void setSortNo(Integer sortNo) { this.sortNo = sortNo; } public void setRemark(String remark) { this.remark = remark; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ParamDetailDo)) return false;  com.amarsoft.rwa.engine.entity.ParamDetailDo other = (com.amarsoft.rwa.engine.entity.ParamDetailDo)o; if (!other.canEqual(this)) return false;  Object this$sortNo = getSortNo(), other$sortNo = other.getSortNo(); if ((this$sortNo == null) ? (other$sortNo != null) : !this$sortNo.equals(other$sortNo)) return false;  Object this$paramVersionNo = getParamVersionNo(), other$paramVersionNo = other.getParamVersionNo(); if ((this$paramVersionNo == null) ? (other$paramVersionNo != null) : !this$paramVersionNo.equals(other$paramVersionNo)) return false;  Object this$paramDetailNo = getParamDetailNo(), other$paramDetailNo = other.getParamDetailNo(); if ((this$paramDetailNo == null) ? (other$paramDetailNo != null) : !this$paramDetailNo.equals(other$paramDetailNo)) return false;  Object this$paramTemplate = getParamTemplate(), other$paramTemplate = other.getParamTemplate(); if ((this$paramTemplate == null) ? (other$paramTemplate != null) : !this$paramTemplate.equals(other$paramTemplate)) return false;  Object this$remark = getRemark(), other$remark = other.getRemark(); return !((this$remark == null) ? (other$remark != null) : !this$remark.equals(other$remark)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ParamDetailDo; } public int hashCode() { int PRIME = 59; result = 1; Object $sortNo = getSortNo(); result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode()); Object $paramVersionNo = getParamVersionNo(); result = result * 59 + (($paramVersionNo == null) ? 43 : $paramVersionNo.hashCode()); Object $paramDetailNo = getParamDetailNo(); result = result * 59 + (($paramDetailNo == null) ? 43 : $paramDetailNo.hashCode()); Object $paramTemplate = getParamTemplate(); result = result * 59 + (($paramTemplate == null) ? 43 : $paramTemplate.hashCode()); Object $remark = getRemark(); return result * 59 + (($remark == null) ? 43 : $remark.hashCode()); } public String toString() { return "ParamDetailDo(paramVersionNo=" + getParamVersionNo() + ", paramDetailNo=" + getParamDetailNo() + ", paramTemplate=" + getParamTemplate() + ", sortNo=" + getSortNo() + ", remark=" + getRemark() + ")"; }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamVersionNo() {
/* 15 */     return this.paramVersionNo;
/*    */   } public String getParamDetailNo() {
/* 17 */     return this.paramDetailNo;
/*    */   } public String getParamTemplate() {
/* 19 */     return this.paramTemplate;
/*    */   } public Integer getSortNo() {
/* 21 */     return this.sortNo;
/*    */   } public String getRemark() {
/* 23 */     return this.remark;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ParamDetailDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */