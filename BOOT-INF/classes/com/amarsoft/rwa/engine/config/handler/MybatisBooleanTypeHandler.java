/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config.handler;
/*    */ 
/*    */ import java.sql.CallableStatement;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.ResultSet;
/*    */ import java.sql.SQLException;
/*    */ import org.apache.ibatis.type.BaseTypeHandler;
/*    */ import org.apache.ibatis.type.JdbcType;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class MybatisBooleanTypeHandler
/*    */   extends BaseTypeHandler<Boolean>
/*    */ {
/*    */   public void setNonNullParameter(PreparedStatement pst, int i, Boolean value, JdbcType jdbcType) throws SQLException {
/* 20 */     if (value.booleanValue()) {
/* 21 */       pst.setInt(i, 1);
/*    */     } else {
/* 23 */       pst.setInt(i, 0);
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean getNullableResult(ResultSet resultSet, String columnName) throws SQLException {
/* 29 */     String v = resultSet.getString(columnName);
/* 30 */     if (v == null || v.length() == 0) {
/* 31 */       return Boolean.valueOf(false);
/*    */     }
/* 33 */     if (v.equals("1")) {
/* 34 */       return Boolean.valueOf(true);
/*    */     }
/* 36 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean getNullableResult(ResultSet resultSet, int columnIndex) throws SQLException {
/* 41 */     String v = resultSet.getString(columnIndex);
/* 42 */     if (v == null || v.length() == 0) {
/* 43 */       return Boolean.valueOf(false);
/*    */     }
/* 45 */     if (v.equals("1")) {
/* 46 */       return Boolean.valueOf(true);
/*    */     }
/* 48 */     return Boolean.valueOf(false);
/*    */   }
/*    */ 
/*    */   
/*    */   public Boolean getNullableResult(CallableStatement callableStatement, int columnIndex) throws SQLException {
/* 53 */     String v = callableStatement.getString(columnIndex);
/* 54 */     if (v == null || v.length() == 0) {
/* 55 */       return Boolean.valueOf(false);
/*    */     }
/* 57 */     if (v.equals("1")) {
/* 58 */       return Boolean.valueOf(true);
/*    */     }
/* 60 */     return Boolean.valueOf(false);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\handler\MybatisBooleanTypeHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */