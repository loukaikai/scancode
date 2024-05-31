/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*    */ 
/*    */ import com.amarsoft.batch.ItemProcessListener;
/*    */ import com.amarsoft.rwa.engine.job.JobUtils;
/*    */ import java.time.LocalDateTime;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RwaProcessListener<T, S>
/*    */   implements ItemProcessListener<T, S>
/*    */ {
/*    */   private String jobId;
/*    */   private LocalDateTime start;
/*    */   
/*    */   public RwaProcessListener(String jobId) {
/* 18 */     this.jobId = jobId;
/*    */   }
/*    */ 
/*    */   
/*    */   public void beforeProcess(T item) {
/* 23 */     this.start = LocalDateTime.now();
/*    */   }
/*    */ 
/*    */   
/*    */   public void afterProcess(T item, S result) {
/* 28 */     JobUtils.addProcessorTime(this.jobId, this.start, LocalDateTime.now());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onProcessError(T item, Throwable e) {
/* 33 */     JobUtils.addProcessorTime(this.jobId, this.start, LocalDateTime.now());
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */