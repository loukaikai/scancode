/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RuleColumnType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CODE("1", "代码"),
/* 11 */   NUMBER("2", "数值"),
/* 12 */   STRING("3", "字符串");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   RuleColumnType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\RuleColumnType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */