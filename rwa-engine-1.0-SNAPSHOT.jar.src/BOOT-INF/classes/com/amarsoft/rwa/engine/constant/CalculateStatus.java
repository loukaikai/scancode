/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CalculateStatus
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CREATED("10", "新建"),
/* 11 */   CALCULATE("11", "计算"),
/* 12 */   COMPLETE("21", "完成"),
/* 13 */   STOP("22", "停止"),
/* 14 */   EXCEPTION("29", "异常");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   CalculateStatus(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\CalculateStatus.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */