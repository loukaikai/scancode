/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.UnionType;
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class ThreadGroupDto implements Serializable {
/*    */   private static final long serialVersionUID = 8234237673290003L;
/*    */   private UnionType unionType;
/*    */   private Integer threadId;
/*    */   private String beginId;
/*    */   private String endId;
/*    */   private int size;
/*    */   
/*    */   public void setUnionType(UnionType unionType) {
/* 15 */     this.unionType = unionType; } public void setThreadId(Integer threadId) { this.threadId = threadId; } public void setBeginId(String beginId) { this.beginId = beginId; } public void setEndId(String endId) { this.endId = endId; } public void setSize(int size) { this.size = size; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.ThreadGroupDto)) return false;  com.amarsoft.rwa.engine.entity.ThreadGroupDto other = (com.amarsoft.rwa.engine.entity.ThreadGroupDto)o; if (!other.canEqual(this)) return false;  if (getSize() != other.getSize()) return false;  Object this$threadId = getThreadId(), other$threadId = other.getThreadId(); if ((this$threadId == null) ? (other$threadId != null) : !this$threadId.equals(other$threadId)) return false;  Object this$unionType = getUnionType(), other$unionType = other.getUnionType(); if ((this$unionType == null) ? (other$unionType != null) : !this$unionType.equals(other$unionType)) return false;  Object this$beginId = getBeginId(), other$beginId = other.getBeginId(); if ((this$beginId == null) ? (other$beginId != null) : !this$beginId.equals(other$beginId)) return false;  Object this$endId = getEndId(), other$endId = other.getEndId(); return !((this$endId == null) ? (other$endId != null) : !this$endId.equals(other$endId)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.ThreadGroupDto; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getSize(); Object $threadId = getThreadId(); result = result * 59 + (($threadId == null) ? 43 : $threadId.hashCode()); Object $unionType = getUnionType(); result = result * 59 + (($unionType == null) ? 43 : $unionType.hashCode()); Object $beginId = getBeginId(); result = result * 59 + (($beginId == null) ? 43 : $beginId.hashCode()); Object $endId = getEndId(); return result * 59 + (($endId == null) ? 43 : $endId.hashCode()); } public String toString() { return "ThreadGroupDto(unionType=" + getUnionType() + ", threadId=" + getThreadId() + ", beginId=" + getBeginId() + ", endId=" + getEndId() + ", size=" + getSize() + ")"; } public ThreadGroupDto(UnionType unionType, Integer threadId, String beginId, String endId, int size) {
/* 16 */     this.unionType = unionType; this.threadId = threadId; this.beginId = beginId; this.endId = endId; this.size = size;
/*    */   }
/*    */   
/*    */   public ThreadGroupDto() {}
/*    */   
/*    */   public UnionType getUnionType() {
/* 22 */     return this.unionType;
/* 23 */   } public Integer getThreadId() { return this.threadId; }
/* 24 */   public String getBeginId() { return this.beginId; }
/* 25 */   public String getEndId() { return this.endId; } public int getSize() {
/* 26 */     return this.size;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\ThreadGroupDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */