/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum InstrumentsType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CASH("3111", "现金"),
/* 11 */   MARGIN("3112", "保证金"),
/* 12 */   GOLD("3113", "黄金"),
/* 13 */   BDR("3114", "本行存单"),
/* 14 */   BOND("3124", "债券"),
/* 15 */   ABS("3141", "资产证券化");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   InstrumentsType(String code, String name) {
/* 22 */     this.code = code;
/* 23 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 28 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 32 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\InstrumentsType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */