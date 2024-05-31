/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ public enum StatusCodeEnum
/*    */ {
/*  5 */   SUCCESS(0, ""),
/*  6 */   JOB_PARAM_EXCEPTION(10001, "计算参数异常"),
/*  7 */   UNKNOWN_EXCEPTION(99999, "未知异常");
/*    */   
/*    */   private int code;
/*    */   private String message;
/*    */   
/*    */   StatusCodeEnum(int code, String message) {
/* 13 */     this.code = code;
/* 14 */     this.message = message;
/*    */   }
/*    */   
/*    */   public void setCode(int code) {
/* 18 */     this.code = code;
/*    */   }
/*    */   
/*    */   public int getCode() {
/* 22 */     return this.code;
/*    */   }
/*    */   
/*    */   public void setMessage(String message) {
/* 26 */     this.message = message;
/*    */   }
/*    */   
/*    */   public String getMessage() {
/* 30 */     return this.message;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\StatusCodeEnum.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */