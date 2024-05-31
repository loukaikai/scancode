/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum MitigateMethod
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   SORT("1", "排序法"),
/* 11 */   PROPORTIONAL("2", "比例法"),
/* 12 */   MIX("3", "混合法");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   MitigateMethod(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\MitigateMethod.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */