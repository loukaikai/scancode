/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ 
/*    */ import com.googlecode.aviator.AviatorEvaluator;
/*    */ import com.googlecode.aviator.Options;
/*    */ import com.googlecode.aviator.runtime.type.AviatorFunction;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.boot.ApplicationArguments;
/*    */ import org.springframework.boot.ApplicationRunner;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Component
/*    */ @Order(2)
/*    */ public class AviatorRunner
/*    */   implements ApplicationRunner
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.config.AviatorRunner.class);
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void run(ApplicationArguments args) throws Exception {
/* 33 */     AviatorEvaluator.setOption(Options.ALWAYS_PARSE_FLOATING_POINT_NUMBER_INTO_DECIMAL, Boolean.valueOf(true));
/* 34 */     AviatorEvaluator.setOption(Options.ALWAYS_PARSE_INTEGRAL_NUMBER_INTO_DECIMAL, Boolean.valueOf(true));
/* 35 */     AviatorEvaluator.addStaticFunctions("strUtils", Class.forName("org.springframework.util.StringUtils"));
/* 36 */     AviatorEvaluator.addInstanceFunctions("str", String.class);
/* 37 */     AviatorEvaluator.addFunction((AviatorFunction)new AddFunction(this));
/* 38 */     AviatorEvaluator.addFunction((AviatorFunction)new SubFunction(this));
/* 39 */     AviatorEvaluator.addFunction((AviatorFunction)new MulFunction(this));
/* 40 */     AviatorEvaluator.addFunction((AviatorFunction)new DivFunction(this));
/* 41 */     log.info("初始化AviatorEvaluator完成");
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\AviatorRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */