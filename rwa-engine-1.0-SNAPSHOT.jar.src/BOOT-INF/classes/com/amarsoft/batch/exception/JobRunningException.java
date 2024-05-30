/*    */ package BOOT-INF.classes.com.amarsoft.batch.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class JobRunningException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8234237673124211L;
/*    */   
/*    */   public JobRunningException() {}
/*    */   
/*    */   public JobRunningException(String message) {
/* 17 */     super(message);
/*    */   }
/*    */   
/*    */   public JobRunningException(String message, Throwable cause) {
/* 21 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public JobRunningException(Throwable cause) {
/* 25 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\exception\JobRunningException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */