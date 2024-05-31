/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SprvTranType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   REPO("1", "回购交易"),
/* 11 */   CAPITAL_MARKET("2", "其他资本市场交易"),
/* 12 */   COLLATERAL_LOAN("3", "抵押贷款");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   SprvTranType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\SprvTranType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */