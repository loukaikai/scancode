/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*    */ import org.slf4j.Logger;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.MediaType;
/*    */ import org.springframework.http.server.ServerHttpRequest;
/*    */ import org.springframework.http.server.ServerHttpResponse;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;
/*    */ 
/*    */ @ControllerAdvice
/*    */ public class LogResponseBodyAdvice implements ResponseBodyAdvice {
/* 13 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.config.LogResponseBodyAdvice.class);
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supports(MethodParameter returnType, Class converterType) {
/* 18 */     return true;
/*    */   }
/*    */ 
/*    */   
/*    */   public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
/* 23 */     if (body == null) {
/* 24 */       log.debug("uri={} | responseBody={null}", request.getURI().getPath());
/* 25 */       return null;
/*    */     } 
/*    */     try {
/* 28 */       log.debug("uri={} | responseBody={}", request.getURI().getPath(), JsonUtils.object2Json(body));
/* 29 */     } catch (Exception e) {
/* 30 */       log.debug("uri={} | responseBody={}", request.getURI().getPath(), body);
/*    */     } 
/* 32 */     return body;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\LogResponseBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */