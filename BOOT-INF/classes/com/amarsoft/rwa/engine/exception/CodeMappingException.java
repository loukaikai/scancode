/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.exception;
/*    */ 
/*    */ 
/*    */ public class CodeMappingException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8234237673124311L;
/*    */   
/*    */   public CodeMappingException() {}
/*    */   
/*    */   public CodeMappingException(String message) {
/* 12 */     super(message);
/*    */   }
/*    */   
/*    */   public CodeMappingException(String message, Throwable cause) {
/* 16 */     super(message, cause);
/*    */   }
/*    */   
/*    */   public CodeMappingException(Throwable cause) {
/* 20 */     super(cause);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\exception\CodeMappingException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */