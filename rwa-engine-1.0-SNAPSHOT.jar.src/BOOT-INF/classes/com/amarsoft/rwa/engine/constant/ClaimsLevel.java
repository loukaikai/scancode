/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClaimsLevel
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   SENIOR("1", "高级债权"),
/* 11 */   SUBPRIME("2", "次级债权");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   ClaimsLevel(String code, String name) {
/* 17 */     this.code = code;
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 23 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ClaimsLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */