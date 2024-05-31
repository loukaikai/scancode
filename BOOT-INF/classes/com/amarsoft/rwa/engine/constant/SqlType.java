/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SqlType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   ITEM("101", "项目模式查询"),
/* 11 */   ROW("102", "行模式查询"),
/* 12 */   Column("103", "列模式查询");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   SqlType(String code, String name) {
/* 19 */     this.code = code;
/* 20 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 25 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\SqlType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */