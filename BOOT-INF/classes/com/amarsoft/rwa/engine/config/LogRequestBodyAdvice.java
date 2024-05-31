/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*    */ import java.io.IOException;
/*    */ import java.lang.reflect.Method;
/*    */ import java.lang.reflect.Type;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.MethodParameter;
/*    */ import org.springframework.http.HttpInputMessage;
/*    */ import org.springframework.http.converter.HttpMessageConverter;
/*    */ import org.springframework.web.bind.annotation.ControllerAdvice;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
/*    */ 
/*    */ @ControllerAdvice
/*    */ public class LogRequestBodyAdvice implements RequestBodyAdvice {
/* 17 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.config.LogRequestBodyAdvice.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 23 */     return true;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 30 */     return body;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
/* 37 */     return inputMessage;
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
/* 44 */     Method method = parameter.getMethod();
/* 45 */     String classMappingUri = getClassMappingUri(method.getDeclaringClass());
/* 46 */     if (classMappingUri.endsWith("/")) {
/* 47 */       classMappingUri = classMappingUri.substring(0, classMappingUri.length() - 1);
/*    */     }
/* 49 */     String methodMappingUri = getMethodMappingUri(method);
/* 50 */     if (!methodMappingUri.startsWith("/")) {
/* 51 */       methodMappingUri = "/" + methodMappingUri;
/*    */     }
/* 53 */     log.debug("uri={} | requestBody={}", classMappingUri + methodMappingUri, JsonUtils.object2Json(body));
/* 54 */     return body;
/*    */   }
/*    */   
/*    */   private String getMethodMappingUri(Method method) {
/* 58 */     RequestMapping methodDeclaredAnnotation = method.<RequestMapping>getDeclaredAnnotation(RequestMapping.class);
/* 59 */     return (methodDeclaredAnnotation == null) ? "" : getMaxLength(methodDeclaredAnnotation.value());
/*    */   }
/*    */   
/*    */   private String getClassMappingUri(Class<?> declaringClass) {
/* 63 */     RequestMapping classDeclaredAnnotation = declaringClass.<RequestMapping>getDeclaredAnnotation(RequestMapping.class);
/* 64 */     return (classDeclaredAnnotation == null) ? "" : getMaxLength(classDeclaredAnnotation.value());
/*    */   }
/*    */   
/*    */   private String getMaxLength(String[] strings) {
/* 68 */     String methodMappingUri = "";
/* 69 */     for (String string : strings) {
/* 70 */       if (string.length() > methodMappingUri.length()) {
/* 71 */         methodMappingUri = string;
/*    */       }
/*    */     } 
/* 74 */     return methodMappingUri;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\LogRequestBodyAdvice.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */