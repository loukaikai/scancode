/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*    */ 
/*    */ import cn.hutool.extra.spring.SpringUtil;
/*    */ import com.amarsoft.batch.ItemWriteListener;
/*    */ import com.amarsoft.batch.exception.JobStopException;
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.constant.ProcessStatus;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.job.JobUtils;
/*    */ import com.amarsoft.rwa.engine.service.JobService;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ public class RwaWriteListener<S extends Map<String, Object>>
/*    */   implements ItemWriteListener<S>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.RwaWriteListener.class);
/*    */   
/*    */   private String jobId;
/*    */   
/*    */   private boolean isCountNumber;
/*    */   private LocalDateTime start;
/*    */   private JobService jobService;
/* 30 */   private long lastSyncTime = System.currentTimeMillis();
/*    */   
/*    */   public RwaWriteListener(String jobId, boolean isCountNumber) {
/* 33 */     this.jobId = jobId;
/* 34 */     this.isCountNumber = isCountNumber;
/* 35 */     this.jobService = (JobService)SpringUtil.getBean(JobService.class);
/*    */   }
/*    */ 
/*    */   
/*    */   public void beforeWrite(List<? extends S> resultList) {
/* 40 */     this.start = LocalDateTime.now();
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterWrite(List<? extends S> resultList) {
/* 46 */     for (int i = 0; i < resultList.size(); i++) {
/* 47 */       Map map = (Map)resultList.get(i);
/*    */       
/* 49 */       ProcessStatus status = (ProcessStatus)EnumUtils.getEnumByCode(DataUtils.getString(map, (ICodeEnum)RwaParam.PROCESS_STATUS), ProcessStatus.class);
/* 50 */       addCalculateCount(status, DataUtils.getInt(map, (ICodeEnum)RwaParam.SIZE).intValue());
/*    */     } 
/* 52 */     JobUtils.addWriterTime(this.jobId, this.start, LocalDateTime.now());
/*    */     
/* 54 */     if (this.isCountNumber && JobUtils.isSync(this.lastSyncTime)) {
/* 55 */       this.jobService.putRwaJob(JobUtils.getJobInfo(this.jobId));
/* 56 */       this.jobService.syncStopRwaTask();
/* 57 */       this.lastSyncTime = System.currentTimeMillis();
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   public void onWriteError(Throwable e, List<? extends S> resultList) {
/* 63 */     if (e instanceof JobStopException)
/* 64 */       throw (JobStopException)e; 
/* 65 */     if (e instanceof org.redisson.client.RedisTimeoutException) {
/*    */       
/* 67 */       log.warn("同步作业异常", e);
/*    */     } else {
/*    */       
/* 70 */       log.error("写入异常", e);
/*    */       
/* 72 */       for (int i = 0; i < resultList.size(); i++) {
/* 73 */         Map map = (Map)resultList.get(i);
/* 74 */         ProcessStatus status = (ProcessStatus)EnumUtils.getEnumByCode(DataUtils.getString(map, (ICodeEnum)RwaParam.PROCESS_STATUS), ProcessStatus.class);
/* 75 */         if (status == ProcessStatus.SKIP) {
/* 76 */           addCalculateCount(status, DataUtils.getInt(map, (ICodeEnum)RwaParam.SIZE).intValue());
/*    */         } else {
/* 78 */           addCalculateCount(ProcessStatus.EXCEPTION, DataUtils.getInt(map, (ICodeEnum)RwaParam.SIZE).intValue());
/*    */         } 
/*    */       } 
/* 81 */       JobUtils.addWriterTime(this.jobId, this.start, LocalDateTime.now());
/*    */     } 
/*    */   }
/*    */ 
/*    */   
/*    */   private void addCalculateCount(ProcessStatus status, int size) {
/* 87 */     if (this.isCountNumber)
/* 88 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ProcessStatus[status.ordinal()]) {
/*    */         case 1:
/* 90 */           JobUtils.addCalculateCount(this.jobId, size);
/*    */           break;
/*    */         case 2:
/* 93 */           JobUtils.addSkipCount(this.jobId, size);
/*    */           break;
/*    */         case 3:
/* 96 */           JobUtils.addExceptionCount(this.jobId, size);
/*    */           break;
/*    */       }  
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaWriteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */