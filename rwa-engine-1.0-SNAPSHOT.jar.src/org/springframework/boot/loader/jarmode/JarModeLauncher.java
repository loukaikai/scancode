/*    */ package org.springframework.boot.loader.jarmode;
/*    */ 
/*    */ import java.util.List;
/*    */ import org.springframework.core.io.support.SpringFactoriesLoader;
/*    */ import org.springframework.util.ClassUtils;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public final class JarModeLauncher
/*    */ {
/* 32 */   static final String DISABLE_SYSTEM_EXIT = JarModeLauncher.class.getName() + ".DISABLE_SYSTEM_EXIT";
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public static void main(String[] args) {
/* 38 */     String mode = System.getProperty("jarmode");
/* 39 */     List<JarMode> candidates = SpringFactoriesLoader.loadFactories(JarMode.class, 
/* 40 */         ClassUtils.getDefaultClassLoader());
/* 41 */     for (JarMode candidate : candidates) {
/* 42 */       if (candidate.accepts(mode)) {
/* 43 */         candidate.run(mode, args);
/*    */         return;
/*    */       } 
/*    */     } 
/* 47 */     System.err.println("Unsupported jarmode '" + mode + "'");
/* 48 */     if (!Boolean.getBoolean(DISABLE_SYSTEM_EXIT))
/* 49 */       System.exit(1); 
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\jarmode\JarModeLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */