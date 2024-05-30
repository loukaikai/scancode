/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ 
/*    */ public class ColumnScopeDto implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673210012L;
/*    */   private String colCode;
/*    */   
/*  8 */   public void setColCode(String colCode) { this.colCode = colCode; } private List<String> colId; private List<String> colName; public void setColId(List<String> colId) { this.colId = colId; } public void setColName(List<String> colName) { this.colName = colName; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ColumnScopeDto)) return false;  com.amarsoft.rwa.engine.entity.ColumnScopeDto other = (com.amarsoft.rwa.engine.entity.ColumnScopeDto)o; if (!other.canEqual(this)) return false;  Object this$colCode = getColCode(), other$colCode = other.getColCode(); if ((this$colCode == null) ? (other$colCode != null) : !this$colCode.equals(other$colCode)) return false;  Object<String> this$colId = (Object<String>)getColId(), other$colId = (Object<String>)other.getColId(); if ((this$colId == null) ? (other$colId != null) : !this$colId.equals(other$colId)) return false;  Object<String> this$colName = (Object<String>)getColName(), other$colName = (Object<String>)other.getColName(); return !((this$colName == null) ? (other$colName != null) : !this$colName.equals(other$colName)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ColumnScopeDto; } public int hashCode() { int PRIME = 59; result = 1; Object $colCode = getColCode(); result = result * 59 + (($colCode == null) ? 43 : $colCode.hashCode()); Object<String> $colId = (Object<String>)getColId(); result = result * 59 + (($colId == null) ? 43 : $colId.hashCode()); Object<String> $colName = (Object<String>)getColName(); return result * 59 + (($colName == null) ? 43 : $colName.hashCode()); } public String toString() { return "ColumnScopeDto(colCode=" + getColCode() + ", colId=" + getColId() + ", colName=" + getColName() + ")"; }
/*    */ 
/*    */   
/*    */   public String getColCode()
/*    */   {
/* 13 */     return this.colCode;
/* 14 */   } public List<String> getColId() { return this.colId; } public List<String> getColName() {
/* 15 */     return this.colName;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ColumnScopeDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */