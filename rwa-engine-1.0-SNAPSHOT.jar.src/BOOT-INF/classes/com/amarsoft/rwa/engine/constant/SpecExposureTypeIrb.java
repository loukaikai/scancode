/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SpecExposureTypeIrb
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   DI_CVA("7051", "衍生工具信用估值调整"),
/* 11 */   SFT_CVA("7052", "证券融资交易信用估值调整"),
/* 12 */   DI_SUBMIT_COLL("7053", "衍生工具提交押品"),
/* 13 */   SFT_SUBMIT_COLL("7054", "证券融资交易提交押品"),
/* 14 */   SFT_BANK_COLL("7055", "证券融资交易银行账簿押品");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   SpecExposureTypeIrb(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\SpecExposureTypeIrb.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */