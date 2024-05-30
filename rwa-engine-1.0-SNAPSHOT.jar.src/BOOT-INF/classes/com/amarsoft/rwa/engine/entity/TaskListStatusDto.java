/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ 
/*    */ public class TaskListStatusDto {
/*    */   private String id;
/*    */   private String status;
/*    */   private Timestamp start;
/*    */   private Timestamp end;
/*    */   private int size;
/*    */   private Map<String, String> statusMap;
/*    */   private Map<String, Integer> statusDist;
/*    */   
/* 13 */   public void setId(String id) { this.id = id; } public void setStatus(String status) { this.status = status; } public void setStart(Timestamp start) { this.start = start; } public void setEnd(Timestamp end) { this.end = end; } public void setSize(int size) { this.size = size; } public void setStatusMap(Map<String, String> statusMap) { this.statusMap = statusMap; } public void setStatusDist(Map<String, Integer> statusDist) { this.statusDist = statusDist; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.TaskListStatusDto)) return false;  com.amarsoft.rwa.engine.entity.TaskListStatusDto other = (com.amarsoft.rwa.engine.entity.TaskListStatusDto)o; if (!other.canEqual(this)) return false;  if (getSize() != other.getSize()) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$status = getStatus(), other$status = other.getStatus(); if ((this$status == null) ? (other$status != null) : !this$status.equals(other$status)) return false;  Object this$start = getStart(), other$start = other.getStart(); if ((this$start == null) ? (other$start != null) : !this$start.equals(other$start)) return false;  Object this$end = getEnd(), other$end = other.getEnd(); if ((this$end == null) ? (other$end != null) : !this$end.equals(other$end)) return false;  Object<String, String> this$statusMap = (Object<String, String>)getStatusMap(), other$statusMap = (Object<String, String>)other.getStatusMap(); if ((this$statusMap == null) ? (other$statusMap != null) : !this$statusMap.equals(other$statusMap)) return false;  Object<String, Integer> this$statusDist = (Object<String, Integer>)getStatusDist(), other$statusDist = (Object<String, Integer>)other.getStatusDist(); return !((this$statusDist == null) ? (other$statusDist != null) : !this$statusDist.equals(other$statusDist)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.TaskListStatusDto; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getSize(); Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $status = getStatus(); result = result * 59 + (($status == null) ? 43 : $status.hashCode()); Object $start = getStart(); result = result * 59 + (($start == null) ? 43 : $start.hashCode()); Object $end = getEnd(); result = result * 59 + (($end == null) ? 43 : $end.hashCode()); Object<String, String> $statusMap = (Object<String, String>)getStatusMap(); result = result * 59 + (($statusMap == null) ? 43 : $statusMap.hashCode()); Object<String, Integer> $statusDist = (Object<String, Integer>)getStatusDist(); return result * 59 + (($statusDist == null) ? 43 : $statusDist.hashCode()); } public String toString() { return "TaskListStatusDto(id=" + getId() + ", status=" + getStatus() + ", start=" + getStart() + ", end=" + getEnd() + ", size=" + getSize() + ", statusMap=" + getStatusMap() + ", statusDist=" + getStatusDist() + ")"; }
/*    */ 
/*    */   
/* 16 */   public String getId() { return this.id; }
/* 17 */   public String getStatus() { return this.status; }
/* 18 */   public Timestamp getStart() { return this.start; }
/* 19 */   public Timestamp getEnd() { return this.end; }
/* 20 */   public int getSize() { return this.size; }
/* 21 */   public Map<String, String> getStatusMap() { return this.statusMap; } public Map<String, Integer> getStatusDist() {
/* 22 */     return this.statusDist;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\TaskListStatusDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */