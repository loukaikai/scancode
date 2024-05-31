/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DerivativeAssetType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   INTEREST_RATE("1", "利率"),
/* 11 */   EXCHANGE("2", "外汇"),
/* 12 */   CREDIT("3", "信用"),
/* 13 */   EQUITY("4", "股权"),
/* 14 */   COMMODITY("5", "商品");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   DerivativeAssetType(String code, String name) {
/* 21 */     this.code = code;
/* 22 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 27 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 31 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\DerivativeAssetType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */