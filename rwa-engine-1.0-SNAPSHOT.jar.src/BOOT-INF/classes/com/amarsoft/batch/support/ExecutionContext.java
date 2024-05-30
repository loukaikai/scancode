/*    */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ExecutionContext
/*    */   implements Serializable
/*    */ {
/*    */   private Map<String, Object> map;
/*    */   
/*    */   public ExecutionContext() {
/* 17 */     this.map = new ConcurrentHashMap<>();
/*    */   }
/*    */   
/*    */   public ExecutionContext(Map<String, Object> map) {
/* 21 */     this.map = new ConcurrentHashMap<>(map);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\ExecutionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */