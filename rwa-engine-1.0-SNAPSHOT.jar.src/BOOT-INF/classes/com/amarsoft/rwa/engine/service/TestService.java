/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*    */ 
/*    */ import cn.hutool.core.util.StrUtil;
/*    */ import com.amarsoft.rwa.engine.service.CommonService;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import java.text.DecimalFormat;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.jdbc.support.JdbcUtils;
/*    */ import org.springframework.scheduling.annotation.Async;
/*    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*    */ import org.springframework.stereotype.Service;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Service
/*    */ public class TestService
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.TestService.class);
/*    */ 
/*    */   
/*    */   @Autowired
/*    */   private CommonService commonService;
/*    */   
/* 29 */   private final DecimalFormat decimalFormat = new DecimalFormat("0000000000");
/* 30 */   private int batchSize = 10000; public int getBatchSize() { return this.batchSize; } public void setBatchSize(int batchSize) {
/* 31 */     this.batchSize = batchSize;
/*    */   }
/* 33 */   private int logSize = 10000; public int getLogSize() { return this.logSize; } public void setLogSize(int logSize) {
/* 34 */     this.logSize = logSize;
/*    */   }
/*    */   
/*    */   public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int coreSize, String namePrefix) {
/* 38 */     ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
/* 39 */     taskExecutor.setMaxPoolSize(coreSize);
/* 40 */     taskExecutor.setCorePoolSize(coreSize);
/* 41 */     taskExecutor.setThreadNamePrefix(namePrefix);
/*    */ 
/*    */     
/* 44 */     taskExecutor.initialize();
/* 45 */     return taskExecutor;
/*    */   }
/*    */   
/*    */   @Async("myTaskExecutor")
/*    */   public void commonInsert(String sql, String table, String dataNo, int size, String... params) throws SQLException {
/* 50 */     PreparedStatement pst = null;
/*    */     try {
/* 52 */       pst = this.commonService.getJdbcTemplate().getDataSource().getConnection().prepareStatement(sql);
/* 53 */       int n = 0;
/* 54 */       for (int i = 1; i <= size; i++) {
/* 55 */         pst.setString(1, dataNo);
/* 56 */         int j = 2;
/* 57 */         for (String param : params) {
/* 58 */           pst.setString(j, (StrUtil.isEmpty(param) ? "test" : param) + this.decimalFormat.format(i));
/* 59 */           j++;
/*    */         } 
/* 61 */         pst.addBatch();
/* 62 */         n++;
/* 63 */         if (n >= this.batchSize) {
/* 64 */           pst.executeBatch();
/* 65 */           n = 0;
/*    */         } 
/* 67 */         if (i % this.logSize == 0) {
/* 68 */           log.info("表[{}]已插入： {}", table, Integer.valueOf(i));
/*    */         }
/*    */       } 
/* 71 */       if (n >= 0) {
/* 72 */         pst.executeBatch();
/* 73 */         n = 0;
/*    */       } 
/* 75 */       log.info("表[{}][{}]插入完成", table, Integer.valueOf(size));
/* 76 */     } catch (SQLException e) {
/* 77 */       log.error("插入" + table + "[" + size + "]异常", e);
/* 78 */       JdbcUtils.closeStatement(pst);
/*    */     } 
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\TestService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */