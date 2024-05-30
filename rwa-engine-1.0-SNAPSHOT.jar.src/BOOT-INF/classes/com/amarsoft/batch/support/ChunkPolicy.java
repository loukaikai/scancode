/*    */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*    */ 
/*    */ 
/*    */ public class ChunkPolicy
/*    */ {
/*    */   public static final int DEFAULT_CHUNK_SIZE = 10;
/*    */   private int chunkSize;
/*    */   
/*    */   public void setChunkSize(int chunkSize) {
/* 10 */     this.chunkSize = chunkSize; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.batch.support.ChunkPolicy)) return false;  com.amarsoft.batch.support.ChunkPolicy other = (com.amarsoft.batch.support.ChunkPolicy)o; return !other.canEqual(this) ? false : (!(getChunkSize() != other.getChunkSize())); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.batch.support.ChunkPolicy; } public int hashCode() { int PRIME = 59; result = 1; return result * 59 + getChunkSize(); } public String toString() { return "ChunkPolicy(chunkSize=" + getChunkSize() + ")"; }
/*    */ 
/*    */   
/*    */   public int getChunkSize() {
/* 14 */     return this.chunkSize;
/*    */   }
/*    */   public ChunkPolicy() {
/* 17 */     this(10);
/*    */   }
/*    */   
/*    */   public ChunkPolicy(int chunkSize) {
/* 21 */     this.chunkSize = chunkSize;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\ChunkPolicy.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */