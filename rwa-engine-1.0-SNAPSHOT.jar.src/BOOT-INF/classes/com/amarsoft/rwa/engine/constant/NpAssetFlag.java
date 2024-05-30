/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NpAssetFlag
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NORMAL("1", "正常资产"),
/* 11 */   QUALIFIED_NP("2", "合格不良资产"),
/* 12 */   UNQUALIFIED_NP("3", "不合格不良资产");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   NpAssetFlag(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\NpAssetFlag.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */