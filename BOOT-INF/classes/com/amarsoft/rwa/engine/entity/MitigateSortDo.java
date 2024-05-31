/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.constant.SortType;
/*    */ import com.baomidou.mybatisplus.annotation.TableField;
/*    */ import com.baomidou.mybatisplus.annotation.TableName;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ @TableName("RWA_EP_MITI_SORT")
/*    */ public class MitigateSortDo implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673220003L;
/*    */   @TableId("SERIAL_NO")
/*    */   private String serialNo;
/*    */   @TableField("MITIGATE_METHOD_NO")
/*    */   private String mitigateMethodNo;
/*    */   @TableField("MITIGATE_SCHEME_NO")
/*    */   private String mitigateSchemeNo;
/*    */   
/* 19 */   public void setSerialNo(String serialNo) { this.serialNo = serialNo; } @TableField("APPROACH") private String approach; @TableField("MITIGATE_SORT_TYPE") private String mitigateSortType; @TableField("SORT_PARAM") private String sortParam; @TableField("SORT_TYPE") private String sortType; @TableField("SORT_NO") private Integer sortNo; public void setMitigateMethodNo(String mitigateMethodNo) { this.mitigateMethodNo = mitigateMethodNo; } public void setMitigateSchemeNo(String mitigateSchemeNo) { this.mitigateSchemeNo = mitigateSchemeNo; } public void setApproach(String approach) { this.approach = approach; } public void setMitigateSortType(String mitigateSortType) { this.mitigateSortType = mitigateSortType; } public void setSortParam(String sortParam) { this.sortParam = sortParam; } public void setSortType(String sortType) { this.sortType = sortType; } public void setSortNo(Integer sortNo) { this.sortNo = sortNo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.MitigateSortDo)) return false;  com.amarsoft.rwa.engine.entity.MitigateSortDo other = (com.amarsoft.rwa.engine.entity.MitigateSortDo)o; if (!other.canEqual(this)) return false;  Object this$sortNo = getSortNo(), other$sortNo = other.getSortNo(); if ((this$sortNo == null) ? (other$sortNo != null) : !this$sortNo.equals(other$sortNo)) return false;  Object this$serialNo = getSerialNo(), other$serialNo = other.getSerialNo(); if ((this$serialNo == null) ? (other$serialNo != null) : !this$serialNo.equals(other$serialNo)) return false;  Object this$mitigateMethodNo = getMitigateMethodNo(), other$mitigateMethodNo = other.getMitigateMethodNo(); if ((this$mitigateMethodNo == null) ? (other$mitigateMethodNo != null) : !this$mitigateMethodNo.equals(other$mitigateMethodNo)) return false;  Object this$mitigateSchemeNo = getMitigateSchemeNo(), other$mitigateSchemeNo = other.getMitigateSchemeNo(); if ((this$mitigateSchemeNo == null) ? (other$mitigateSchemeNo != null) : !this$mitigateSchemeNo.equals(other$mitigateSchemeNo)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$mitigateSortType = getMitigateSortType(), other$mitigateSortType = other.getMitigateSortType(); if ((this$mitigateSortType == null) ? (other$mitigateSortType != null) : !this$mitigateSortType.equals(other$mitigateSortType)) return false;  Object this$sortParam = getSortParam(), other$sortParam = other.getSortParam(); if ((this$sortParam == null) ? (other$sortParam != null) : !this$sortParam.equals(other$sortParam)) return false;  Object this$sortType = getSortType(), other$sortType = other.getSortType(); return !((this$sortType == null) ? (other$sortType != null) : !this$sortType.equals(other$sortType)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.MitigateSortDo; } public int hashCode() { int PRIME = 59; result = 1; Object $sortNo = getSortNo(); result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode()); Object $serialNo = getSerialNo(); result = result * 59 + (($serialNo == null) ? 43 : $serialNo.hashCode()); Object $mitigateMethodNo = getMitigateMethodNo(); result = result * 59 + (($mitigateMethodNo == null) ? 43 : $mitigateMethodNo.hashCode()); Object $mitigateSchemeNo = getMitigateSchemeNo(); result = result * 59 + (($mitigateSchemeNo == null) ? 43 : $mitigateSchemeNo.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $mitigateSortType = getMitigateSortType(); result = result * 59 + (($mitigateSortType == null) ? 43 : $mitigateSortType.hashCode()); Object $sortParam = getSortParam(); result = result * 59 + (($sortParam == null) ? 43 : $sortParam.hashCode()); Object $sortType = getSortType(); return result * 59 + (($sortType == null) ? 43 : $sortType.hashCode()); } public String toString() { return "MitigateSortDo(serialNo=" + getSerialNo() + ", mitigateMethodNo=" + getMitigateMethodNo() + ", mitigateSchemeNo=" + getMitigateSchemeNo() + ", approach=" + getApproach() + ", mitigateSortType=" + getMitigateSortType() + ", sortParam=" + getSortParam() + ", sortType=" + getSortType() + ", sortNo=" + getSortNo() + ")"; } public MitigateSortDo(String serialNo, String mitigateMethodNo, String mitigateSchemeNo, String approach, String mitigateSortType, String sortParam, String sortType, Integer sortNo) {
/* 20 */     this.serialNo = serialNo; this.mitigateMethodNo = mitigateMethodNo; this.mitigateSchemeNo = mitigateSchemeNo; this.approach = approach; this.mitigateSortType = mitigateSortType; this.sortParam = sortParam; this.sortType = sortType; this.sortNo = sortNo;
/*    */   }
/*    */ 
/*    */   
/*    */   public MitigateSortDo() {}
/*    */   
/*    */   public MitigateSortDo(RwaParam sortParam, SortType sortType, Integer sortNo) {
/* 27 */     this.sortParam = sortParam.getField();
/* 28 */     this.sortType = sortType.getCode();
/* 29 */     this.sortNo = sortNo;
/*    */   }
/*    */   
/*    */   public String getSerialNo() {
/* 33 */     return this.serialNo;
/*    */   } public String getMitigateMethodNo() {
/* 35 */     return this.mitigateMethodNo;
/*    */   } public String getMitigateSchemeNo() {
/* 37 */     return this.mitigateSchemeNo;
/*    */   } public String getApproach() {
/* 39 */     return this.approach;
/*    */   } public String getMitigateSortType() {
/* 41 */     return this.mitigateSortType;
/*    */   } public String getSortParam() {
/* 43 */     return this.sortParam;
/*    */   } public String getSortType() {
/* 45 */     return this.sortType;
/*    */   } public Integer getSortNo() {
/* 47 */     return this.sortNo;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\MitigateSortDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */