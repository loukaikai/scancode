/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UnderAssetType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NORMAL_NR("11", "正常非零售资产"),
/* 11 */   QUALIFIED_NP_NR("12", "合格不良非零售资产"),
/* 12 */   UNQUALIFIED_NP_NR("13", "不合格不良非零售资产"),
/* 13 */   NORMAL_RE("21", "正常零售资产"),
/* 14 */   QUALIFIED_NP_RE("22", "合格不良零售资产"),
/* 15 */   UNQUALIFIED_NP_RE("23", "不合格不良零售资产");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   UnderAssetType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\UnderAssetType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */