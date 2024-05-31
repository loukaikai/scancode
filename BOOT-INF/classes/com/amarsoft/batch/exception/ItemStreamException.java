/*    */ package BOOT-INF.classes.com.amarsoft.batch.exception;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemStreamException
/*    */   extends RuntimeException
/*    */ {
/*    */   private static final long serialVersionUID = 8234237673124212L;
/*    */   
/*    */   public ItemStreamException(String message) {
/* 13 */     super(message);
/*    */   }
/*    */   
/*    */   public ItemStreamException(String msg, Throwable nested) {
/* 17 */     super(msg, nested);
/*    */   }
/*    */   
/*    */   public ItemStreamException(Throwable nested) {
/* 21 */     super(nested);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\exception\ItemStreamException.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */