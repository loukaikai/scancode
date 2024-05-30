/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum MitigationMainType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   FINANCIAL_COLLATERAL("31", "金融质押品"),
/* 11 */   ACCOUNTS_RECEIVABLE("32", "应收账款"),
/* 12 */   REAL_ESTATE("33", "商用房地产/居住用房地产"),
/* 13 */   OTHER_COLLATERAL("34", "其他抵质押品"),
/* 14 */   GUARANTEE("21", "保证"),
/* 15 */   CREDIT_DERIVATIVE("22", "信用衍生保证"),
/* 16 */   NETTING_CLAT("10", "净额结算");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   MitigationMainType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\MitigationMainType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */