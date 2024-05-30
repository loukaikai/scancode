/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*    */ 
/*    */ import java.io.File;
/*    */ import java.io.FileInputStream;
/*    */ import java.io.FileNotFoundException;
/*    */ import java.io.IOException;
/*    */ import java.io.InputStream;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.core.io.ClassPathResource;
/*    */ import org.springframework.util.ResourceUtils;
/*    */ 
/*    */ public class FileUtil {
/* 14 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.FileUtil.class);
/*    */ 
/*    */   
/*    */   public static File getFile(String filePath) {
/* 18 */     File file = getFileByFull(filePath);
/* 19 */     if (file != null) {
/* 20 */       return file;
/*    */     }
/*    */     
/* 23 */     file = getFileByConfig(filePath);
/* 24 */     if (file != null) {
/* 25 */       return file;
/*    */     }
/*    */     
/* 28 */     file = getFileByUser(filePath);
/* 29 */     if (file != null) {
/* 30 */       return file;
/*    */     }
/*    */     
/* 33 */     file = getFileByClassPath(filePath);
/* 34 */     if (file != null) {
/* 35 */       return file;
/*    */     }
/*    */     
/* 38 */     return null;
/*    */   }
/*    */   
/*    */   public static File getFileByFull(String filePath) {
/*    */     try {
/* 43 */       File file = new File(filePath);
/* 44 */       if (file.exists()) {
/* 45 */         return file;
/*    */       }
/* 47 */     } catch (Exception exception) {}
/*    */     
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public static File getFileByUser(String filePath) {
/* 53 */     String userDir = System.getProperty("user.dir");
/* 54 */     String path = userDir + "/" + filePath;
/* 55 */     return getFileByFull(path);
/*    */   }
/*    */   
/*    */   public static File getFileByConfig(String filePath) {
/* 59 */     String userDir = System.getProperty("user.dir");
/* 60 */     String path = userDir + "/config/" + filePath;
/* 61 */     return getFileByFull(path);
/*    */   }
/*    */   
/*    */   public static File getFileByClassPath(String filePath) {
/*    */     try {
/* 66 */       return ResourceUtils.getFile("classpath:" + filePath);
/* 67 */     } catch (FileNotFoundException fileNotFoundException) {
/*    */       
/* 69 */       return null;
/*    */     } 
/*    */   }
/*    */   public static InputStream getFileInputStream(String filePath) {
/* 73 */     File file = getFile(filePath);
/*    */     try {
/* 75 */       if (file == null)
/*    */       {
/* 77 */         return (new ClassPathResource(filePath)).getInputStream();
/*    */       }
/* 79 */       return new FileInputStream(file);
/*    */     }
/* 81 */     catch (IOException iOException) {
/*    */       
/* 83 */       return null;
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\FileUtil.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */