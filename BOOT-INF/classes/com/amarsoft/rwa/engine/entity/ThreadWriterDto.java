/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ public class ThreadWriterDto
/*    */ {
/*    */   private int threadId;
/*    */   private int begin;
/*    */   private int end;
/*    */   
/*    */   public void setThreadId(int threadId) {
/* 10 */     this.threadId = threadId; } public void setBegin(int begin) { this.begin = begin; } public void setEnd(int end) { this.end = end; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ThreadWriterDto)) return false;  com.amarsoft.rwa.engine.entity.ThreadWriterDto other = (com.amarsoft.rwa.engine.entity.ThreadWriterDto)o; return !other.canEqual(this) ? false : ((getThreadId() != other.getThreadId()) ? false : ((getBegin() != other.getBegin()) ? false : (!(getEnd() != other.getEnd())))); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ThreadWriterDto; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getThreadId(); result = result * 59 + getBegin(); return result * 59 + getEnd(); } public String toString() { return "ThreadWriterDto(threadId=" + getThreadId() + ", begin=" + getBegin() + ", end=" + getEnd() + ")"; }
/*    */ 
/*    */   
/* 13 */   public int getThreadId() { return this.threadId; }
/* 14 */   public int getBegin() { return this.begin; } public int getEnd() {
/* 15 */     return this.end;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ThreadWriterDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */