/*    */ package org.springframework.boot.loader;
/*    */ 
/*    */ import org.springframework.boot.loader.archive.Archive;
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
/*    */ 
/*    */ 
/*    */ 
/*    */ public class WarLauncher
/*    */   extends ExecutableArchiveLauncher
/*    */ {
/*    */   public WarLauncher() {}
/*    */   
/*    */   protected WarLauncher(Archive archive) {
/* 37 */     super(archive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPostProcessingClassPathArchives() {
/* 42 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isNestedArchive(Archive.Entry entry) {
/* 47 */     if (entry.isDirectory()) {
/* 48 */       return entry.getName().equals("WEB-INF/classes/");
/*    */     }
/* 50 */     return (entry.getName().startsWith("WEB-INF/lib/") || entry.getName().startsWith("WEB-INF/lib-provided/"));
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArchiveEntryPathPrefix() {
/* 55 */     return "WEB-INF/";
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 59 */     (new WarLauncher()).launch(args);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\WarLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */