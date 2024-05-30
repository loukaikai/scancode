/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum LockType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   EXE("EXE", "执行"),
/* 11 */   ST("ST", "单笔测算"),
/* 12 */   IMT("IMT", "即时任务"),
/* 13 */   JOB("JOB", "作业消费"),
/* 14 */   PROC("PROC", "存储过程"),
/* 15 */   CACHE("CACHE", "缓存"),
/*    */   
/* 17 */   TL("TL", "任务列表");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   LockType(String code, String name) {
/* 24 */     this.code = code;
/* 25 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\LockType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */