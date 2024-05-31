/*    */ package BOOT-INF.classes.com.amarsoft.batch.step;
/*    */ 
/*    */ import com.amarsoft.batch.ItemProcessListener;
/*    */ import com.amarsoft.batch.ItemProcessor;
/*    */ import com.amarsoft.batch.ItemReadListener;
/*    */ import com.amarsoft.batch.ItemReader;
/*    */ import com.amarsoft.batch.ItemWriteListener;
/*    */ import com.amarsoft.batch.ItemWriter;
/*    */ import com.amarsoft.batch.step.Step;
/*    */ import com.amarsoft.batch.support.ChunkPolicy;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class StepBuilder<I, O>
/*    */ {
/* 16 */   private Step<I, O> step = new Step();
/*    */ 
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder name(String name) {
/* 20 */     this.step.setName(name);
/* 21 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder chunk(int chunkSize) {
/* 25 */     this.step.setChunkSize(chunkSize);
/* 26 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder chunk(ChunkPolicy chunkPolicy) {
/* 30 */     this.step.setChunkPolicy(chunkPolicy);
/* 31 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder reader(ItemReader<I> reader) {
/* 35 */     this.step.setReader(reader);
/* 36 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder readListener(ItemReadListener<I> readListener) {
/* 40 */     this.step.setReadListener(readListener);
/* 41 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder processor(ItemProcessor<I, O> processor) {
/* 45 */     this.step.setProcessor(processor);
/* 46 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder processListener(ItemProcessListener<I, O> processListener) {
/* 50 */     this.step.setProcessListener(processListener);
/* 51 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder writer(ItemWriter<O> writer) {
/* 55 */     this.step.setWriter(writer);
/* 56 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.batch.step.StepBuilder writeListener(ItemWriteListener<O> writeListener) {
/* 60 */     this.step.setWriteListener(writeListener);
/* 61 */     return this;
/*    */   }
/*    */ 
/*    */   
/*    */   public Step build() {
/* 66 */     return this.step;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\step\StepBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */