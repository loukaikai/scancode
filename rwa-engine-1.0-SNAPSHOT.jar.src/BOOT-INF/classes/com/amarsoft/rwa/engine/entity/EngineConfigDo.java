/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.baomidou.mybatisplus.annotation.TableField;
/*    */ import com.baomidou.mybatisplus.annotation.TableId;
/*    */ import com.baomidou.mybatisplus.annotation.TableName;
/*    */ 
/*    */ @TableName("RWA_EP_Engine")
/*    */ public class EngineConfigDo {
/*    */   @TableId("param_id")
/*    */   private String paramId;
/*    */   
/*    */   public void setParamId(String paramId) {
/* 13 */     this.paramId = paramId; } @TableField("param_name") private String paramName; @TableField("param_config") private String paramConfig; public void setParamName(String paramName) { this.paramName = paramName; } public void setParamConfig(String paramConfig) { this.paramConfig = paramConfig; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.EngineConfigDo)) return false;  com.amarsoft.rwa.engine.entity.EngineConfigDo other = (com.amarsoft.rwa.engine.entity.EngineConfigDo)o; if (!other.canEqual(this)) return false;  Object this$paramId = getParamId(), other$paramId = other.getParamId(); if ((this$paramId == null) ? (other$paramId != null) : !this$paramId.equals(other$paramId)) return false;  Object this$paramName = getParamName(), other$paramName = other.getParamName(); if ((this$paramName == null) ? (other$paramName != null) : !this$paramName.equals(other$paramName)) return false;  Object this$paramConfig = getParamConfig(), other$paramConfig = other.getParamConfig(); return !((this$paramConfig == null) ? (other$paramConfig != null) : !this$paramConfig.equals(other$paramConfig)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.EngineConfigDo; } public int hashCode() { int PRIME = 59; result = 1; Object $paramId = getParamId(); result = result * 59 + (($paramId == null) ? 43 : $paramId.hashCode()); Object $paramName = getParamName(); result = result * 59 + (($paramName == null) ? 43 : $paramName.hashCode()); Object $paramConfig = getParamConfig(); return result * 59 + (($paramConfig == null) ? 43 : $paramConfig.hashCode()); } public String toString() { return "EngineConfigDo(paramId=" + getParamId() + ", paramName=" + getParamName() + ", paramConfig=" + getParamConfig() + ")"; }
/*    */ 
/*    */ 
/*    */   
/*    */   public String getParamId() {
/* 18 */     return this.paramId;
/*    */   } public String getParamName() {
/* 20 */     return this.paramName;
/*    */   } public String getParamConfig() {
/* 22 */     return this.paramConfig;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\EngineConfigDo.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */