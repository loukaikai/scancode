/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import java.util.Arrays;
/*    */ 
/*    */ public class TestRequest {
/*    */   private String sql;
/*    */   private String table;
/*    */   private String dataNo;
/*    */   
/* 10 */   public void setSql(String sql) { this.sql = sql; } private Integer size; private String[] params; private Integer batchSize; private Integer logSize; public void setTable(String table) { this.table = table; } public void setDataNo(String dataNo) { this.dataNo = dataNo; } public void setSize(Integer size) { this.size = size; } public void setParams(String[] params) { this.params = params; } public void setBatchSize(Integer batchSize) { this.batchSize = batchSize; } public void setLogSize(Integer logSize) { this.logSize = logSize; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TestRequest)) return false;  com.amarsoft.rwa.engine.entity.TestRequest other = (com.amarsoft.rwa.engine.entity.TestRequest)o; if (!other.canEqual(this)) return false;  Object this$size = getSize(), other$size = other.getSize(); if ((this$size == null) ? (other$size != null) : !this$size.equals(other$size)) return false;  Object this$batchSize = getBatchSize(), other$batchSize = other.getBatchSize(); if ((this$batchSize == null) ? (other$batchSize != null) : !this$batchSize.equals(other$batchSize)) return false;  Object this$logSize = getLogSize(), other$logSize = other.getLogSize(); if ((this$logSize == null) ? (other$logSize != null) : !this$logSize.equals(other$logSize)) return false;  Object this$sql = getSql(), other$sql = other.getSql(); if ((this$sql == null) ? (other$sql != null) : !this$sql.equals(other$sql)) return false;  Object this$table = getTable(), other$table = other.getTable(); if ((this$table == null) ? (other$table != null) : !this$table.equals(other$table)) return false;  Object this$dataNo = getDataNo(), other$dataNo = other.getDataNo(); return ((this$dataNo == null) ? (other$dataNo != null) : !this$dataNo.equals(other$dataNo)) ? false : (!!Arrays.deepEquals((Object[])getParams(), (Object[])other.getParams())); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TestRequest; } public int hashCode() { int PRIME = 59; result = 1; Object $size = getSize(); result = result * 59 + (($size == null) ? 43 : $size.hashCode()); Object $batchSize = getBatchSize(); result = result * 59 + (($batchSize == null) ? 43 : $batchSize.hashCode()); Object $logSize = getLogSize(); result = result * 59 + (($logSize == null) ? 43 : $logSize.hashCode()); Object $sql = getSql(); result = result * 59 + (($sql == null) ? 43 : $sql.hashCode()); Object $table = getTable(); result = result * 59 + (($table == null) ? 43 : $table.hashCode()); Object $dataNo = getDataNo(); result = result * 59 + (($dataNo == null) ? 43 : $dataNo.hashCode()); return result * 59 + Arrays.deepHashCode((Object[])getParams()); } public String toString() { return "TestRequest(sql=" + getSql() + ", table=" + getTable() + ", dataNo=" + getDataNo() + ", size=" + getSize() + ", params=" + Arrays.deepToString((Object[])getParams()) + ", batchSize=" + getBatchSize() + ", logSize=" + getLogSize() + ")"; }
/*    */ 
/*    */   
/* 13 */   public String getSql() { return this.sql; }
/* 14 */   public String getTable() { return this.table; }
/* 15 */   public String getDataNo() { return this.dataNo; }
/* 16 */   public Integer getSize() { return this.size; }
/* 17 */   public String[] getParams() { return this.params; }
/* 18 */   public Integer getBatchSize() { return this.batchSize; } public Integer getLogSize() {
/* 19 */     return this.logSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TestRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */