/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class CommonRequest
/*    */ {
/*    */   private String value;
/*    */   
/*    */   public String toString() {
/* 10 */     return "CommonRequest(value=" + getValue() + ")"; } public int hashCode() { int PRIME = 59; result = 1; Object $value = getValue(); return result * 59 + (($value == null) ? 43 : $value.hashCode()); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.CommonRequest; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.CommonRequest)) return false;  com.amarsoft.rwa.engine.entity.CommonRequest other = (com.amarsoft.rwa.engine.entity.CommonRequest)o; if (!other.canEqual(this)) return false;  Object this$value = getValue(), other$value = other.getValue(); return !((this$value == null) ? (other$value != null) : !this$value.equals(other$value)); } public void setValue(String value) { this.value = value; }
/*    */   
/*    */   public String getValue() {
/* 13 */     return this.value;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\CommonRequest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */