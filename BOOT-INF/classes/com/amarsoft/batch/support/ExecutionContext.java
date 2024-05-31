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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\ExecutionContext.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */