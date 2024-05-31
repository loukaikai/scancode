/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SpecExposureTypeWa
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   SME("5030", "标准小微企业"),
/*    */   
/* 12 */   CRE("7040", "企业房地产抵押贷款"),
/* 13 */   RRE("7050", "个人房地产抵押贷款"),
/*    */   
/* 15 */   AMP_ADV("9043", "垫支资管产品"),
/*    */   
/* 17 */   DI_CVA("9051", "衍生工具信用估值调整"),
/* 18 */   SFT_CVA("9052", "证券融资交易信用估值调整"),
/* 19 */   DI_SUBMIT_COLL("9053", "衍生工具提交押品"),
/* 20 */   SFT_SUBMIT_COLL("9054", "证券融资交易提交押品"),
/* 21 */   SFT_BANK_COLL("9055", "证券融资交易银行账簿押品");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   SpecExposureTypeWa(String code, String name) {
/* 28 */     this.code = code;
/* 29 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 34 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 38 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\SpecExposureTypeWa.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */