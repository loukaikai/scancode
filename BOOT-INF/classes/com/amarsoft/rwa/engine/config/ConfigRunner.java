/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import cn.hutool.core.io.IORuntimeException;
/*    */ import cn.hutool.core.io.IoUtil;
/*    */ import com.amarsoft.rwa.engine.service.ConfigService;
/*    */ import com.amarsoft.rwa.engine.service.JobService;
/*    */ import com.amarsoft.rwa.engine.service.LockService;
/*    */ import com.amarsoft.rwa.engine.service.ResultService;
/*    */ import com.amarsoft.rwa.engine.util.FileUtil;
/*    */ import com.amarsoft.rwa.engine.util.IdWorker;
/*    */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*    */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*    */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*    */ import com.fasterxml.jackson.core.type.TypeReference;
/*    */ import java.util.HashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.locks.Lock;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.boot.ApplicationArguments;
/*    */ import org.springframework.boot.ApplicationRunner;
/*    */ import org.springframework.core.annotation.Order;
/*    */ import org.springframework.stereotype.Component;
/*    */ 
/*    */ @Component
/*    */ @Order(1)
/*    */ public class ConfigRunner implements ApplicationRunner {
/* 31 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.config.ConfigRunner.class);
/*    */   
/*    */   @Autowired
/*    */   private ConfigService configService;
/*    */   
/*    */   @Autowired
/*    */   private LockService lockService;
/*    */   
/*    */   @Autowired
/*    */   private JobService jobService;
/*    */   @Value("${rwa.db-type}")
/*    */   private String dbType;
/*    */   @Value("${rwa.writer-sub-table}")
/*    */   private boolean enableWriterSubTable;
/*    */   
/*    */   public void run(ApplicationArguments args) throws Exception {
/* 47 */     IdWorker.idWorker = initIdWorker();
/* 48 */     SqlBuilder.setCtDbType(this.dbType);
/*    */     
/* 50 */     this.configService.initEngineConfig();
/*    */     
/* 52 */     if (this.enableWriterSubTable) {
/* 53 */       initSubTableSqlConfig();
/*    */     }
/*    */     
/* 56 */     initEcColumnsConfig();
/*    */   }
/*    */   
/*    */   public void initSubTableSqlConfig() {
/* 60 */     String colConfig = IoUtil.readUtf8(FileUtil.getFileInputStream("table_columns_config.json"));
/* 61 */     ResultService.resultTableColumnMap = (Map)JsonUtils.json2Object(colConfig, (TypeReference)new Object(this));
/* 62 */     log.info("初始化结果子表完成");
/*    */   }
/*    */   
/*    */   public void initEcColumnsConfig() {
/*    */     try {
/* 67 */       String colConfig = IoUtil.readUtf8(FileUtil.getFileInputStream("ec_columns.json"));
/* 68 */       RwaUtils.ecColumnMap = (Map)JsonUtils.json2Object(colConfig, (TypeReference)new Object(this));
/* 69 */       log.info("初始化经济资本字段配置完成");
/* 70 */     } catch (IORuntimeException e) {
/* 71 */       log.info("无经济资本字段配置");
/* 72 */       RwaUtils.ecColumnMap = new HashMap<>();
/*    */     } 
/*    */   }
/*    */   
/*    */   public IdWorker initIdWorker() {
/* 77 */     Lock lock = this.lockService.getProcLock();
/* 78 */     lock.lock();
/*    */     try {
/* 80 */       List<Integer> workers = this.jobService.nextWorkerId();
/* 81 */       if (CollUtil.isEmpty(workers) || workers.size() < 2) {
/* 82 */         return new IdWorker();
/*    */       }
/* 84 */       return new IdWorker(((Integer)workers.get(0)).intValue(), ((Integer)workers.get(1)).intValue());
/*    */     } finally {
/*    */       
/* 87 */       lock.unlock();
/*    */     } 
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\ConfigRunner.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */