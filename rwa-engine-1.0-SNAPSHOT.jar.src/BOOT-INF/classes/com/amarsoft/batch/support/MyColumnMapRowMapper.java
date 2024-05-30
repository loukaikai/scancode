/*    */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*    */ 
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.ResultSetMetaData;
/*    */ import java.sql.SQLException;
/*    */ import java.util.Map;
/*    */ import org.springframework.jdbc.core.ColumnMapRowMapper;
/*    */ import org.springframework.jdbc.support.JdbcUtils;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MyColumnMapRowMapper
/*    */   extends ColumnMapRowMapper
/*    */ {
/*    */   protected Object getColumnValue(ResultSet rs, int index) throws SQLException {
/* 21 */     Object obj = super.getColumnValue(rs, index);
/* 22 */     if (obj instanceof String) {
/* 23 */       return ((String)obj).intern();
/*    */     }
/* 25 */     return obj;
/*    */   }
/*    */ 
/*    */   
/*    */   public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
/* 30 */     ResultSetMetaData rsmd = rs.getMetaData();
/* 31 */     int columnCount = rsmd.getColumnCount();
/* 32 */     Map<String, Object> mapOfColumnValues = createColumnMap(columnCount);
/*    */     
/* 34 */     for (int i = 1; i <= columnCount; i++) {
/* 35 */       String column = JdbcUtils.lookupColumnName(rsmd, i);
/* 36 */       mapOfColumnValues.putIfAbsent(getColumnKey(column), getColumnValue(rs, i));
/*    */     } 
/* 38 */     return mapOfColumnValues;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\MyColumnMapRowMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */