/*    */ package BOOT-INF.classes.com.amarsoft.batch.exception;
/*    */ 
/*    */ 
/*    */ public class JobStopException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8234237673124313L;
/*    */   
/*    */   public JobStopException() {}
/*    */   
/*    */   public JobStopException(String message) {
/* 12 */     super(message);
/*    */   }
/*    */   
/*    */   public JobStopException(String message, Throwable cause) {
/* 16 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public JobStopException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\exception\JobStopException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */