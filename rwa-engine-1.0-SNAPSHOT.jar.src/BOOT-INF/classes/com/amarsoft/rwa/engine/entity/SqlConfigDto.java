/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ public class SqlConfigDto {
/*    */   private String reportNo;
/*    */   private String resultNo;
/*    */   private String sqlId;
/*    */   private String sqlType;
/*    */   private String sqlSource;
/*    */   private String sql;
/*    */   
/* 10 */   public void setReportNo(String reportNo) { this.reportNo = reportNo; } public void setResultNo(String resultNo) { this.resultNo = resultNo; } public void setSqlId(String sqlId) { this.sqlId = sqlId; } public void setSqlType(String sqlType) { this.sqlType = sqlType; } public void setSqlSource(String sqlSource) { this.sqlSource = sqlSource; } public void setSql(String sql) { this.sql = sql; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.SqlConfigDto)) return false;  com.amarsoft.rwa.engine.entity.SqlConfigDto other = (com.amarsoft.rwa.engine.entity.SqlConfigDto)o; if (!other.canEqual(this)) return false;  Object this$reportNo = getReportNo(), other$reportNo = other.getReportNo(); if ((this$reportNo == null) ? (other$reportNo != null) : !this$reportNo.equals(other$reportNo)) return false;  Object this$resultNo = getResultNo(), other$resultNo = other.getResultNo(); if ((this$resultNo == null) ? (other$resultNo != null) : !this$resultNo.equals(other$resultNo)) return false;  Object this$sqlId = getSqlId(), other$sqlId = other.getSqlId(); if ((this$sqlId == null) ? (other$sqlId != null) : !this$sqlId.equals(other$sqlId)) return false;  Object this$sqlType = getSqlType(), other$sqlType = other.getSqlType(); if ((this$sqlType == null) ? (other$sqlType != null) : !this$sqlType.equals(other$sqlType)) return false;  Object this$sqlSource = getSqlSource(), other$sqlSource = other.getSqlSource(); if ((this$sqlSource == null) ? (other$sqlSource != null) : !this$sqlSource.equals(other$sqlSource)) return false;  Object this$sql = getSql(), other$sql = other.getSql(); return !((this$sql == null) ? (other$sql != null) : !this$sql.equals(other$sql)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.SqlConfigDto; } public int hashCode() { int PRIME = 59; result = 1; Object $reportNo = getReportNo(); result = result * 59 + (($reportNo == null) ? 43 : $reportNo.hashCode()); Object $resultNo = getResultNo(); result = result * 59 + (($resultNo == null) ? 43 : $resultNo.hashCode()); Object $sqlId = getSqlId(); result = result * 59 + (($sqlId == null) ? 43 : $sqlId.hashCode()); Object $sqlType = getSqlType(); result = result * 59 + (($sqlType == null) ? 43 : $sqlType.hashCode()); Object $sqlSource = getSqlSource(); result = result * 59 + (($sqlSource == null) ? 43 : $sqlSource.hashCode()); Object $sql = getSql(); return result * 59 + (($sql == null) ? 43 : $sql.hashCode()); } public String toString() { return "SqlConfigDto(reportNo=" + getReportNo() + ", resultNo=" + getResultNo() + ", sqlId=" + getSqlId() + ", sqlType=" + getSqlType() + ", sqlSource=" + getSqlSource() + ", sql=" + getSql() + ")"; }
/*    */ 
/*    */   
/* 13 */   public String getReportNo() { return this.reportNo; }
/* 14 */   public String getResultNo() { return this.resultNo; }
/* 15 */   public String getSqlId() { return this.sqlId; }
/* 16 */   public String getSqlType() { return this.sqlType; }
/* 17 */   public String getSqlSource() { return this.sqlSource; } public String getSql() {
/* 18 */     return this.sql;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\SqlConfigDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */