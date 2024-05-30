/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExposureType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   SOVEREIGNS("1", "主权"),
/* 11 */   BANKS("2", "金融机构"),
/* 12 */   CORPORATE("3", "公司"),
/* 13 */   RETAIL("4", "零售"),
/* 14 */   EQUITY("5", "股权"),
/* 15 */   ABS("6", "资产证券化"),
/* 16 */   OTHER("7", "其他");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ExposureType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ExposureType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */