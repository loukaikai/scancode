/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ProvideMitigationType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NON("0", "无"),
/* 11 */   SOV_GUARANTEE("21", "主权保证"),
/* 12 */   FINANCIAL_COLLATERAL("31", "金融质押品"),
/* 13 */   ACCOUNTS_RECEIVABLE("32", "应收账款"),
/* 14 */   REAL_ESTATE("33", "商用房地产/居住用房地产"),
/* 15 */   OTHER_COLLATERAL("34", "其他抵质押品"),
/* 16 */   OTHER_Mitigation("99", "其他缓释工具");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ProvideMitigationType(String code, String name) {
/* 23 */     this.code = code;
/* 24 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 29 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 33 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ProvideMitigationType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */