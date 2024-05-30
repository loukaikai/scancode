/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.StatusCodeEnum;
/*    */ 
/*    */ public class ServiceResult implements Serializable {
/*    */   public static final long serialVersionUID = 952707170223L;
/*    */   private int code;
/*    */   
/*  9 */   public void setCode(int code) { this.code = code; } private String message; private LocalDateTime time; private Object data; public void setMessage(String message) { this.message = message; } public void setTime(LocalDateTime time) { this.time = time; } public void setData(Object data) { this.data = data; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.config.ServiceResult)) return false;  com.amarsoft.rwa.engine.config.ServiceResult other = (com.amarsoft.rwa.engine.config.ServiceResult)o; if (!other.canEqual(this)) return false;  if (getCode() != other.getCode()) return false;  Object this$message = getMessage(), other$message = other.getMessage(); if ((this$message == null) ? (other$message != null) : !this$message.equals(other$message)) return false;  Object this$time = getTime(), other$time = other.getTime(); if ((this$time == null) ? (other$time != null) : !this$time.equals(other$time)) return false;  Object this$data = getData(), other$data = other.getData(); return !((this$data == null) ? (other$data != null) : !this$data.equals(other$data)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.config.ServiceResult; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getCode(); Object $message = getMessage(); result = result * 59 + (($message == null) ? 43 : $message.hashCode()); Object $time = getTime(); result = result * 59 + (($time == null) ? 43 : $time.hashCode()); Object $data = getData(); return result * 59 + (($data == null) ? 43 : $data.hashCode()); } public String toString() { return "ServiceResult(code=" + getCode() + ", message=" + getMessage() + ", time=" + getTime() + ", data=" + getData() + ")"; }
/*    */ 
/*    */   
/*    */   public int getCode()
/*    */   {
/* 14 */     return this.code;
/* 15 */   } public String getMessage() { return this.message; }
/* 16 */   public LocalDateTime getTime() { return this.time; } public Object getData() {
/* 17 */     return this.data;
/*    */   }
/*    */   public com.amarsoft.rwa.engine.config.ServiceResult fillCode(StatusCodeEnum code) {
/* 20 */     return fillCode(code.getCode(), code.getMessage());
/*    */   }
/*    */   
/*    */   public com.amarsoft.rwa.engine.config.ServiceResult fillCode(int code, String message) {
/* 24 */     setCode(code);
/* 25 */     setMessage(message);
/* 26 */     setTime(LocalDateTime.now());
/* 27 */     return this;
/*    */   }
/*    */   
/*    */   public com.amarsoft.rwa.engine.config.ServiceResult fillData(Object data) {
/* 31 */     setData(data);
/* 32 */     return this;
/*    */   }
/*    */   
/*    */   private static com.amarsoft.rwa.engine.config.ServiceResult result(StatusCodeEnum status) {
/* 36 */     com.amarsoft.rwa.engine.config.ServiceResult result = new com.amarsoft.rwa.engine.config.ServiceResult();
/* 37 */     result.fillCode(status);
/* 38 */     return result;
/*    */   }
/*    */   
/*    */   private static com.amarsoft.rwa.engine.config.ServiceResult result(int code, String message) {
/* 42 */     com.amarsoft.rwa.engine.config.ServiceResult result = new com.amarsoft.rwa.engine.config.ServiceResult();
/* 43 */     result.fillCode(code, message);
/* 44 */     return result;
/*    */   }
/*    */   
/*    */   public static com.amarsoft.rwa.engine.config.ServiceResult success() {
/* 48 */     return result(StatusCodeEnum.SUCCESS);
/*    */   }
/*    */   
/*    */   public static com.amarsoft.rwa.engine.config.ServiceResult success(Object object) {
/* 52 */     return result(StatusCodeEnum.SUCCESS).fillData(object);
/*    */   }
/*    */   
/*    */   public static com.amarsoft.rwa.engine.config.ServiceResult error(StatusCodeEnum status) {
/* 56 */     return result(status);
/*    */   }
/*    */   
/*    */   public static com.amarsoft.rwa.engine.config.ServiceResult error(StatusCodeEnum status, String message) {
/* 60 */     return error(status.getCode(), message);
/*    */   }
/*    */   
/*    */   public static com.amarsoft.rwa.engine.config.ServiceResult error(int code, String message) {
/* 64 */     return result(code, message);
/*    */   }
/*    */   
/*    */   public String simpleJson() {
/* 68 */     if (this.message == null) this.message = ""; 
/* 69 */     return "{\"code\":" + this.code + ",\"message\":\"" + this.message + ",\"time\":\"" + this.time + "\"}";
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\ServiceResult.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */