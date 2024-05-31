/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EnableStatus
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   DISABLE("0", "禁用"),
/* 11 */   ENABLE("1", "启用"),
/* 12 */   DEACTIVATE("2", "停用");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   EnableStatus(String code, String name) {
/* 18 */     this.code = code;
/* 19 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 24 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 28 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\EnableStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */