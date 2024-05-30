/*     */ package BOOT-INF.classes.com.amarsoft.batch.step;
/*     */ 
/*     */ import com.amarsoft.batch.ItemProcessListener;
/*     */ import com.amarsoft.batch.ItemProcessor;
/*     */ import com.amarsoft.batch.ItemReadListener;
/*     */ import com.amarsoft.batch.ItemReader;
/*     */ import com.amarsoft.batch.ItemWriteListener;
/*     */ import com.amarsoft.batch.ItemWriter;
/*     */ import com.amarsoft.batch.step.StepExecution;
/*     */ import com.amarsoft.batch.support.ChunkPolicy;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ 
/*     */ public class Step<I, O> {
/*     */   private long id;
/*     */   private String name;
/*     */   private StepExecution stepExecution;
/*     */   
/*  19 */   public void setId(long id) { this.id = id; } private ItemReader<? extends I> reader; private ItemWriter<? super O> writer; private ItemProcessor<? super I, ? extends O> processor; public void setName(String name) { this.name = name; } public void setStepExecution(StepExecution stepExecution) { this.stepExecution = stepExecution; } public void setReader(ItemReader<? extends I> reader) { this.reader = reader; } public void setWriter(ItemWriter<? super O> writer) { this.writer = writer; } public void setProcessor(ItemProcessor<? super I, ? extends O> processor) { this.processor = processor; } public void setChunkSize(int chunkSize) { this.chunkSize = chunkSize; } public void setChunkPolicy(ChunkPolicy chunkPolicy) { this.chunkPolicy = chunkPolicy; } public void setReadListener(ItemReadListener<I> readListener) { this.readListener = readListener; } public void setProcessListener(ItemProcessListener<I, O> processListener) { this.processListener = processListener; } public void setWriteListener(ItemWriteListener<O> writeListener) { this.writeListener = writeListener; } public void setCtReadCount(int ctReadCount) { this.ctReadCount = ctReadCount; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.batch.step.Step)) return false;  com.amarsoft.batch.step.Step<?, ?> other = (com.amarsoft.batch.step.Step<?, ?>)o; if (!other.canEqual(this)) return false;  if (getId() != other.getId()) return false;  if (getChunkSize() != other.getChunkSize()) return false;  if (getCtReadCount() != other.getCtReadCount()) return false;  Object this$name = getName(), other$name = other.getName(); if ((this$name == null) ? (other$name != null) : !this$name.equals(other$name)) return false;  Object this$stepExecution = getStepExecution(), other$stepExecution = other.getStepExecution(); if ((this$stepExecution == null) ? (other$stepExecution != null) : !this$stepExecution.equals(other$stepExecution)) return false;  Object<? extends I> this$reader = (Object<? extends I>)getReader(); Object<?> other$reader = (Object<?>)other.getReader(); if ((this$reader == null) ? (other$reader != null) : !this$reader.equals(other$reader)) return false;  Object<? super O> this$writer = (Object<? super O>)getWriter(); Object<?> other$writer = (Object<?>)other.getWriter(); if ((this$writer == null) ? (other$writer != null) : !this$writer.equals(other$writer)) return false;  Object<? super I, ? extends O> this$processor = (Object<? super I, ? extends O>)getProcessor(); Object<?, ?> other$processor = (Object<?, ?>)other.getProcessor(); if ((this$processor == null) ? (other$processor != null) : !this$processor.equals(other$processor)) return false;  Object this$chunkPolicy = getChunkPolicy(), other$chunkPolicy = other.getChunkPolicy(); if ((this$chunkPolicy == null) ? (other$chunkPolicy != null) : !this$chunkPolicy.equals(other$chunkPolicy)) return false;  Object<I> this$readListener = (Object<I>)getReadListener(); Object<?> other$readListener = (Object<?>)other.getReadListener(); if ((this$readListener == null) ? (other$readListener != null) : !this$readListener.equals(other$readListener)) return false;  Object<I, O> this$processListener = (Object<I, O>)getProcessListener(); Object<?, ?> other$processListener = (Object<?, ?>)other.getProcessListener(); if ((this$processListener == null) ? (other$processListener != null) : !this$processListener.equals(other$processListener)) return false;  Object<O> this$writeListener = (Object<O>)getWriteListener(); Object<?> other$writeListener = (Object<?>)other.getWriteListener(); return !((this$writeListener == null) ? (other$writeListener != null) : !this$writeListener.equals(other$writeListener)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.batch.step.Step; } public int hashCode() { int PRIME = 59; result = 1; long $id = getId(); result = result * 59 + (int)($id >>> 32L ^ $id); result = result * 59 + getChunkSize(); result = result * 59 + getCtReadCount(); Object $name = getName(); result = result * 59 + (($name == null) ? 43 : $name.hashCode()); Object $stepExecution = getStepExecution(); result = result * 59 + (($stepExecution == null) ? 43 : $stepExecution.hashCode()); Object<? extends I> $reader = (Object<? extends I>)getReader(); result = result * 59 + (($reader == null) ? 43 : $reader.hashCode()); Object<? super O> $writer = (Object<? super O>)getWriter(); result = result * 59 + (($writer == null) ? 43 : $writer.hashCode()); Object<? super I, ? extends O> $processor = (Object<? super I, ? extends O>)getProcessor(); result = result * 59 + (($processor == null) ? 43 : $processor.hashCode()); Object $chunkPolicy = getChunkPolicy(); result = result * 59 + (($chunkPolicy == null) ? 43 : $chunkPolicy.hashCode()); Object<I> $readListener = (Object<I>)getReadListener(); result = result * 59 + (($readListener == null) ? 43 : $readListener.hashCode()); Object<I, O> $processListener = (Object<I, O>)getProcessListener(); result = result * 59 + (($processListener == null) ? 43 : $processListener.hashCode()); Object<O> $writeListener = (Object<O>)getWriteListener(); return result * 59 + (($writeListener == null) ? 43 : $writeListener.hashCode()); } public String toString() { return "Step(id=" + getId() + ", name=" + getName() + ", stepExecution=" + getStepExecution() + ", reader=" + getReader() + ", writer=" + getWriter() + ", processor=" + getProcessor() + ", chunkSize=" + getChunkSize() + ", chunkPolicy=" + getChunkPolicy() + ", readListener=" + getReadListener() + ", processListener=" + getProcessListener() + ", writeListener=" + getWriteListener() + ", ctReadCount=" + getCtReadCount() + ")"; }
/*     */ 
/*     */   
/*  22 */   public long getId() { return this.id; }
/*  23 */   public String getName() { return this.name; }
/*  24 */   public StepExecution getStepExecution() { return this.stepExecution; }
/*  25 */   public ItemReader<? extends I> getReader() { return this.reader; }
/*  26 */   public ItemWriter<? super O> getWriter() { return this.writer; } public ItemProcessor<? super I, ? extends O> getProcessor() {
/*  27 */     return this.processor;
/*  28 */   } private int chunkSize = 0; private ChunkPolicy chunkPolicy; private ItemReadListener<I> readListener; private ItemProcessListener<I, O> processListener; private ItemWriteListener<O> writeListener; private int ctReadCount; public int getChunkSize() { return this.chunkSize; }
/*  29 */   public ChunkPolicy getChunkPolicy() { return this.chunkPolicy; }
/*  30 */   public ItemReadListener<I> getReadListener() { return this.readListener; }
/*  31 */   public ItemProcessListener<I, O> getProcessListener() { return this.processListener; } public ItemWriteListener<O> getWriteListener() {
/*  32 */     return this.writeListener;
/*     */   } public int getCtReadCount() {
/*  34 */     return this.ctReadCount;
/*     */   }
/*     */   
/*     */   public void execute(StepExecution stepExecution) throws Exception {
/*  38 */     stepExecution.setStartTime(LocalDateTime.now());
/*  39 */     if (this.chunkSize <= 0) {
/*  40 */       this.chunkSize = 10;
/*     */     }
/*  42 */     if (this.readListener == null) {
/*  43 */       this.readListener = (ItemReadListener<I>)new NullItemReadListener();
/*     */     }
/*  45 */     if (this.processListener == null) {
/*  46 */       this.processListener = (ItemProcessListener<I, O>)new NullItemProcessListener();
/*     */     }
/*  48 */     if (this.writeListener == null) {
/*  49 */       this.writeListener = (ItemWriteListener<O>)new NullItemWriteListener();
/*     */     }
/*     */     try {
/*  52 */       this.reader.open(stepExecution.getExecutionContext());
/*  53 */       this.writer.init();
/*     */ 
/*     */ 
/*     */       
/*     */       while (true) {
/*  58 */         List<I> itemList = read(stepExecution);
/*  59 */         if (itemList == null || itemList.size() == 0) {
/*     */           break;
/*     */         }
/*     */ 
/*     */         
/*  64 */         List<O> resultList = process(stepExecution, itemList);
/*     */ 
/*     */         
/*  67 */         write(stepExecution, resultList);
/*     */       } 
/*     */     } finally {
/*     */       
/*  71 */       this.reader.close();
/*  72 */       this.writer.close();
/*  73 */       stepExecution.setEndTime(LocalDateTime.now());
/*     */     } 
/*     */   }
/*     */   
/*     */   public I read() throws Exception {
/*     */     try {
/*  79 */       this.readListener.beforeRead();
/*  80 */       I item = (I)this.reader.read();
/*     */       
/*  82 */       if (item == null) {
/*  83 */         return null;
/*     */       }
/*  85 */       this.readListener.afterRead(item);
/*  86 */       return item;
/*  87 */     } catch (Exception e) {
/*  88 */       this.readListener.onReadError(e);
/*  89 */       throw e;
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<I> read(StepExecution stepExecution) throws Exception {
/*  94 */     List<I> list = new ArrayList<>();
/*     */     do {
/*  96 */       I item = read();
/*  97 */       if (item == null) {
/*     */         break;
/*     */       }
/* 100 */       list.add(item);
/* 101 */       this.ctReadCount++;
/* 102 */       stepExecution.setReadCount(stepExecution.getReadCount() + 1);
/* 103 */     } while ((this.chunkPolicy != null) ? (
/* 104 */       this.ctReadCount >= this.chunkPolicy.getChunkSize()) : (
/*     */ 
/*     */ 
/*     */       
/* 108 */       this.ctReadCount >= this.chunkSize));
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 113 */     this.ctReadCount = 0;
/* 114 */     return list;
/*     */   }
/*     */   
/*     */   public O process(I item) {
/* 118 */     O result = null;
/*     */     try {
/* 120 */       this.processListener.beforeProcess(item);
/* 121 */       result = (O)this.processor.process(item);
/* 122 */       this.processListener.afterProcess(item, result);
/* 123 */     } catch (Exception e) {
/* 124 */       this.processListener.onProcessError(item, e);
/*     */     } 
/* 126 */     return result;
/*     */   }
/*     */   
/*     */   public List<O> process(StepExecution stepExecution, List<I> itemList) {
/* 130 */     List<O> resultList = new ArrayList<>();
/* 131 */     for (I item : itemList) {
/* 132 */       resultList.add(process(item));
/*     */     }
/* 134 */     return resultList;
/*     */   }
/*     */   
/*     */   public void write(StepExecution stepExecution, List<O> resultList) {
/*     */     try {
/* 139 */       this.writeListener.beforeWrite(resultList);
/*     */       
/* 141 */       if (this.writer != null) {
/* 142 */         this.writer.write(resultList);
/*     */       }
/* 144 */       this.writeListener.afterWrite(resultList);
/* 145 */       stepExecution.setWriteCount(stepExecution.getWriteCount() + resultList.size());
/* 146 */     } catch (Exception e) {
/* 147 */       this.writeListener.onWriteError(e, resultList);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\step\Step.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */