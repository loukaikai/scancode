/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ public class ColumnRuleDto implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673210013L;
/*    */   private String ruleColCode;
/*    */   private String ruleColName;
/*    */   private String ruleColAttr;
/*    */   
/*  8 */   public void setRuleColCode(String ruleColCode) { this.ruleColCode = ruleColCode; } private String ruleColType; private String colFormat; private String mappingMode; private Integer sortNo; private Map<String, String> codeMap; public void setRuleColName(String ruleColName) { this.ruleColName = ruleColName; } public void setRuleColAttr(String ruleColAttr) { this.ruleColAttr = ruleColAttr; } public void setRuleColType(String ruleColType) { this.ruleColType = ruleColType; } public void setColFormat(String colFormat) { this.colFormat = colFormat; } public void setMappingMode(String mappingMode) { this.mappingMode = mappingMode; } public void setSortNo(Integer sortNo) { this.sortNo = sortNo; } public void setCodeMap(Map<String, String> codeMap) { this.codeMap = codeMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ColumnRuleDto)) return false;  com.amarsoft.rwa.engine.entity.ColumnRuleDto other = (com.amarsoft.rwa.engine.entity.ColumnRuleDto)o; if (!other.canEqual(this)) return false;  Object this$sortNo = getSortNo(), other$sortNo = other.getSortNo(); if ((this$sortNo == null) ? (other$sortNo != null) : !this$sortNo.equals(other$sortNo)) return false;  Object this$ruleColCode = getRuleColCode(), other$ruleColCode = other.getRuleColCode(); if ((this$ruleColCode == null) ? (other$ruleColCode != null) : !this$ruleColCode.equals(other$ruleColCode)) return false;  Object this$ruleColName = getRuleColName(), other$ruleColName = other.getRuleColName(); if ((this$ruleColName == null) ? (other$ruleColName != null) : !this$ruleColName.equals(other$ruleColName)) return false;  Object this$ruleColAttr = getRuleColAttr(), other$ruleColAttr = other.getRuleColAttr(); if ((this$ruleColAttr == null) ? (other$ruleColAttr != null) : !this$ruleColAttr.equals(other$ruleColAttr)) return false;  Object this$ruleColType = getRuleColType(), other$ruleColType = other.getRuleColType(); if ((this$ruleColType == null) ? (other$ruleColType != null) : !this$ruleColType.equals(other$ruleColType)) return false;  Object this$colFormat = getColFormat(), other$colFormat = other.getColFormat(); if ((this$colFormat == null) ? (other$colFormat != null) : !this$colFormat.equals(other$colFormat)) return false;  Object this$mappingMode = getMappingMode(), other$mappingMode = other.getMappingMode(); if ((this$mappingMode == null) ? (other$mappingMode != null) : !this$mappingMode.equals(other$mappingMode)) return false;  Object<String, String> this$codeMap = (Object<String, String>)getCodeMap(), other$codeMap = (Object<String, String>)other.getCodeMap(); return !((this$codeMap == null) ? (other$codeMap != null) : !this$codeMap.equals(other$codeMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ColumnRuleDto; } public int hashCode() { int PRIME = 59; result = 1; Object $sortNo = getSortNo(); result = result * 59 + (($sortNo == null) ? 43 : $sortNo.hashCode()); Object $ruleColCode = getRuleColCode(); result = result * 59 + (($ruleColCode == null) ? 43 : $ruleColCode.hashCode()); Object $ruleColName = getRuleColName(); result = result * 59 + (($ruleColName == null) ? 43 : $ruleColName.hashCode()); Object $ruleColAttr = getRuleColAttr(); result = result * 59 + (($ruleColAttr == null) ? 43 : $ruleColAttr.hashCode()); Object $ruleColType = getRuleColType(); result = result * 59 + (($ruleColType == null) ? 43 : $ruleColType.hashCode()); Object $colFormat = getColFormat(); result = result * 59 + (($colFormat == null) ? 43 : $colFormat.hashCode()); Object $mappingMode = getMappingMode(); result = result * 59 + (($mappingMode == null) ? 43 : $mappingMode.hashCode()); Object<String, String> $codeMap = (Object<String, String>)getCodeMap(); return result * 59 + (($codeMap == null) ? 43 : $codeMap.hashCode()); } public String toString() { return "ColumnRuleDto(ruleColCode=" + getRuleColCode() + ", ruleColName=" + getRuleColName() + ", ruleColAttr=" + getRuleColAttr() + ", ruleColType=" + getRuleColType() + ", colFormat=" + getColFormat() + ", mappingMode=" + getMappingMode() + ", sortNo=" + getSortNo() + ", codeMap=" + getCodeMap() + ")"; }
/*    */ 
/*    */   
/*    */   public String getRuleColCode()
/*    */   {
/* 13 */     return this.ruleColCode;
/* 14 */   } public String getRuleColName() { return this.ruleColName; }
/* 15 */   public String getRuleColAttr() { return this.ruleColAttr; }
/* 16 */   public String getRuleColType() { return this.ruleColType; }
/* 17 */   public String getColFormat() { return this.colFormat; }
/* 18 */   public String getMappingMode() { return this.mappingMode; }
/* 19 */   public Integer getSortNo() { return this.sortNo; } public Map<String, String> getCodeMap() {
/* 20 */     return this.codeMap;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ColumnRuleDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */