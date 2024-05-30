/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TrancheLevel
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   PRIORITY("1", "优先档"),
/* 11 */   MIDDLE("2", "中间档"),
/* 12 */   SUBPRIME("3", "次级档");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   TrancheLevel(String code, String name) {
/* 18 */     this.code = code;
/* 19 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 24 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 28 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\TrancheLevel.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */