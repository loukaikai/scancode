/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AbaAssetType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   ABS("401", "资产证券化"),
/* 11 */   DI("405", "衍生工具"),
/* 12 */   SFT("406", "证券融资交易"),
/* 13 */   CCP("407", "中央交易对手"),
/* 14 */   OTHER("999", "其他工具");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   AbaAssetType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\AbaAssetType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */