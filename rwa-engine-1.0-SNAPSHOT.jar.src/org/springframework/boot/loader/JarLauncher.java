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
/*    */ public class JarLauncher
/*    */   extends ExecutableArchiveLauncher
/*    */ {
/*    */   static final Archive.EntryFilter NESTED_ARCHIVE_ENTRY_FILTER;
/*    */   
/*    */   static {
/* 35 */     NESTED_ARCHIVE_ENTRY_FILTER = (entry -> entry.isDirectory() ? entry.getName().equals("BOOT-INF/classes/") : entry.getName().startsWith("BOOT-INF/lib/"));
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public JarLauncher() {}
/*    */ 
/*    */ 
/*    */   
/*    */   protected JarLauncher(Archive archive) {
/* 46 */     super(archive);
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isPostProcessingClassPathArchives() {
/* 51 */     return false;
/*    */   }
/*    */ 
/*    */   
/*    */   protected boolean isNestedArchive(Archive.Entry entry) {
/* 56 */     return NESTED_ARCHIVE_ENTRY_FILTER.matches(entry);
/*    */   }
/*    */ 
/*    */   
/*    */   protected String getArchiveEntryPathPrefix() {
/* 61 */     return "BOOT-INF/";
/*    */   }
/*    */   
/*    */   public static void main(String[] args) throws Exception {
/* 65 */     (new JarLauncher()).launch(args);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\JarLauncher.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */