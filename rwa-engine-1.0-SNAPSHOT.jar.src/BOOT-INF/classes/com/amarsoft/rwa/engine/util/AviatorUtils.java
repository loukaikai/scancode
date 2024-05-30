/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*    */ 
/*    */ import cn.hutool.core.collection.ListUtil;
/*    */ import com.googlecode.aviator.AviatorEvaluator;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AviatorUtils
/*    */ {
/*    */   public static Object execute(String exp, Map<String, Object> env) {
/* 17 */     if (exp == null || exp.isEmpty()) {
/* 18 */       return env;
/*    */     }
/* 20 */     return AviatorEvaluator.compile(exp, true).execute(env);
/*    */   }
/*    */   
/*    */   public static Map<String, Object> createSimpleMap(Object... params) {
/* 24 */     if (params == null || params.length % 2 != 0) {
/* 25 */       throw new RuntimeException("参数异常，生成简单Map必须为1个key对1个value；params=" + ListUtil.toList(params));
/*    */     }
/* 27 */     int size = params.length / 2;
/* 28 */     Map<String, Object> map = new HashMap<>();
/* 29 */     for (int i = 0; i < size; i++) {
/* 30 */       map.put(params[i * 2].toString(), params[i * 2 + 1]);
/*    */     }
/* 32 */     return map;
/*    */   }
/*    */   
/*    */   public static Object execute(String exp, Object... params) {
/* 36 */     Map<String, Object> env = createSimpleMap(params);
/* 37 */     return execute(exp, env);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\AviatorUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */