/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*    */ import com.amarsoft.rwa.engine.exception.CodeMappingException;
/*    */ import com.amarsoft.rwa.engine.exception.JobParameterException;
/*    */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*    */ import com.amarsoft.rwa.engine.exception.SqlBuildException;
/*    */ import javax.servlet.http.HttpServletRequest;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.stereotype.Controller;
/*    */ import org.springframework.validation.BindException;
/*    */ import org.springframework.validation.ObjectError;
/*    */ import org.springframework.web.bind.annotation.ExceptionHandler;
/*    */ import org.springframework.web.bind.annotation.ResponseBody;
/*    */ 
/*    */ @Controller
/*    */ @ControllerAdvice
/*    */ public class AppExceptionHandler {
/* 19 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.config.AppExceptionHandler.class);
/*    */ 
/*    */ 
/*    */   
/*    */   @ExceptionHandler({SqlBuildException.class})
/*    */   @ResponseBody
/*    */   public ServiceResult sqlBuildHandler(HttpServletRequest req, SqlBuildException e) throws Exception {
/* 26 */     log.error("uri=" + req.getRequestURI() + " | [10005][SQL创建异常]", (Throwable)e);
/* 27 */     return ServiceResult.error(10005, "[SQL创建异常]" + e.getMessage());
/*    */   }
/*    */   
/*    */   @ExceptionHandler({CodeMappingException.class})
/*    */   @ResponseBody
/*    */   public ServiceResult codeMappingHandler(HttpServletRequest req, CodeMappingException e) throws Exception {
/* 33 */     log.error("uri=" + req.getRequestURI() + " | [10004][配置或数据异常]", (Throwable)e);
/* 34 */     return ServiceResult.error(10004, "[配置或数据异常]" + e.getMessage());
/*    */   }
/*    */   
/*    */   @ExceptionHandler({ParamConfigException.class})
/*    */   @ResponseBody
/*    */   public ServiceResult paramConfigHandler(HttpServletRequest req, ParamConfigException e) throws Exception {
/* 40 */     log.error("uri=" + req.getRequestURI() + " | [10003][参数配置异常]", (Throwable)e);
/* 41 */     return ServiceResult.error(10003, "[参数配置异常]" + e.getMessage());
/*    */   }
/*    */   
/*    */   @ExceptionHandler({JobParameterException.class})
/*    */   @ResponseBody
/*    */   public ServiceResult jobParameterHandler(HttpServletRequest req, JobParameterException e) throws Exception {
/* 47 */     log.error("uri=" + req.getRequestURI() + " | [10002][JOB调用参数异常]", (Throwable)e);
/* 48 */     return ServiceResult.error(10002, "[JOB调用参数异常]" + e.getMessage());
/*    */   }
/*    */   
/*    */   @ExceptionHandler({BindException.class})
/*    */   @ResponseBody
/*    */   public ServiceResult bindExceptionHandler(HttpServletRequest req, BindException e) throws Exception {
/* 54 */     log.error("uri=" + req.getRequestURI() + " | [10001][接口参数异常]", (Throwable)e);
/* 55 */     return ServiceResult.error(10001, "[接口参数异常]" + ((ObjectError)e.getAllErrors().get(0)).getDefaultMessage());
/*    */   }
/*    */   
/*    */   @ExceptionHandler({Exception.class})
/*    */   @ResponseBody
/*    */   public ServiceResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
/* 61 */     log.error("uri=" + req.getRequestURI() + " | [99999][未知异常]", e);
/* 62 */     return ServiceResult.error(99999, "系统出错了");
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\AppExceptionHandler.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */