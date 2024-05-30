/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import javax.validation.constraints.NotNull;
/*    */ import javax.validation.constraints.Pattern;
/*    */ 
/*    */ public class IdRequest
/*    */ {
/*    */   @Pattern(regexp = "[0-9A-Za-z]{1,}", message = "ID输入异常")
/*    */   @NotNull(message = "ID不能为空")
/*    */   private String id;
/*    */   
/*    */   public String toString() {
/* 13 */     return "IdRequest(id=" + getId() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); return result * 59 + (($id == null) ? 43 : $id.hashCode()); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.IdRequest; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.IdRequest)) return false;  com.amarsoft.rwa.engine.entity.IdRequest other = (com.amarsoft.rwa.engine.entity.IdRequest)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); return !((this$id == null) ? (other$id != null) : !this$id.equals(other$id)); } public void setId(String id) { this.id = id; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getId() {
/* 18 */     return this.id;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\IdRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */