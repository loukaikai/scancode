/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum MitigationType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   ONSHEETNET("1", "表内净额结算"),
/* 11 */   GUARANTEE("2", "保证"),
/* 12 */   COLLATERAL("3", "抵质押品");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   MitigationType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\MitigationType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */