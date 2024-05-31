/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*     */ 
/*     */ import cn.hutool.extra.spring.SpringUtil;
/*     */ import com.amarsoft.batch.JobListener;
/*     */ import com.amarsoft.batch.exception.JobStopException;
/*     */ import com.amarsoft.batch.job.JobExecution;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.job.JobUtils;
/*     */ import com.amarsoft.rwa.engine.service.JobService;
/*     */ import com.amarsoft.rwa.engine.service.TaskLogService;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.sql.Timestamp;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class RwaJobListener
/*     */   implements JobListener
/*     */ {
/*     */   private JobInfoDto jobInfo;
/*     */   private JobLogDo jobLogDo;
/*     */   private TaskLogService taskLogService;
/*  24 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.RwaJobListener.class); private JobService jobService; private String jobFullName;
/*     */   
/*     */   public JobInfoDto getJobInfo() {
/*  27 */     return this.jobInfo; } public void setJobInfo(JobInfoDto jobInfo) {
/*  28 */     this.jobInfo = jobInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public RwaJobListener(JobInfoDto jobInfo) {
/*  36 */     this.jobInfo = jobInfo;
/*  37 */     this.taskLogService = (TaskLogService)SpringUtil.getBean(TaskLogService.class);
/*  38 */     this.jobService = (JobService)SpringUtil.getBean(JobService.class);
/*  39 */     this.jobFullName = jobInfo.getTaskType().getName() + "-" + jobInfo.getJobType().getName() + "(" + jobInfo.getResultNo() + ")-" + jobInfo.getDataBatchNo() + "-" + jobInfo.getApproach().getName();
/*  40 */     if (jobInfo.getSubThreadGroup() != null) {
/*  41 */       this.jobFullName += "[" + jobInfo.getSubThreadGroup().getThreadId() + "]";
/*     */     }
/*  43 */     this.jobLogDo = jobInfo.getJobLog();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void beforeJob(JobExecution jobExecution) {
/*  49 */     this.taskLogService.beginJobLog(this.jobLogDo);
/*     */     
/*  51 */     this.jobService.addRwaJob(this.jobInfo);
/*     */     
/*  53 */     if (JobUtils.isStopRwaTask(this.jobInfo.getResultNo())) {
/*  54 */       throw new JobStopException("任务[" + this.jobInfo.getResultNo() + "]被停止");
/*     */     }
/*  56 */     log.info("RWA计算[{}]: 开始执行[{}]", this.jobFullName, this.jobLogDo.getCalculateNum());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void afterJob(JobExecution jobExecution) {
/*  62 */     Timestamp endTime = DataUtils.nowTimestamp();
/*  63 */     long readerTime = this.jobLogDo.getReaderTime().get();
/*  64 */     long processorTime = this.jobLogDo.getProcessorTime().get();
/*  65 */     long writerTime = this.jobLogDo.getWriterTime().get();
/*  66 */     log.info("RWA计算[{}]: 耗时比例[{}]-其中reader时间[{}]processor时间[{}]writer时间[{}]", new Object[] { this.jobFullName, 
/*  67 */           JobUtils.getSpecificValue(readerTime, processorTime, writerTime), 
/*  68 */           DataUtils.getDuration(readerTime), DataUtils.getDuration(processorTime), DataUtils.getDuration(writerTime) });
/*  69 */     log.info("RWA计算[{}]: 正常计算[{}]正常跳过[{}]异常计算[{}]", new Object[] { this.jobFullName, 
/*  70 */           Integer.valueOf(this.jobLogDo.getCtCalculateCount().get()), Integer.valueOf(this.jobLogDo.getCtSkipCount().get()), Integer.valueOf(this.jobLogDo.getCtExceptionCount().get()) });
/*  71 */     log.info("RWA计算[{}]: 运行完成， 总计算数[{}/{}]总计算时间[{}]", new Object[] { this.jobFullName, 
/*  72 */           Integer.valueOf(this.jobLogDo.getCtCount().get()), this.jobLogDo.getCalculateNum(), DataUtils.getDuration(this.jobLogDo.getStartTime(), endTime) });
/*     */     
/*  74 */     insertErrorData();
/*  75 */     String errorDataInfo = JobUtils.getErrorDataInfo(this.jobLogDo.getJobId());
/*  76 */     JobUtils.printErrorData(this.jobInfo.getJobId());
/*  77 */     JobUtils.clearErrorData(this.jobInfo.getJobId());
/*     */     
/*  79 */     this.taskLogService.closeJobLog(this.jobLogDo, endTime, errorDataInfo);
/*     */     
/*  81 */     this.jobService.putRwaJob(this.jobInfo);
/*  82 */     this.jobService.syncStopRwaTask();
/*     */     
/*  84 */     JobUtils.removeRwaJob(this.jobInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public void onJobError(Throwable e, JobExecution jobExecution) {
/*  89 */     if (e instanceof JobStopException) {
/*     */       
/*  91 */       log.error("RWA计算[" + this.jobFullName + "]: 运行停止", e);
/*  92 */       this.taskLogService.stopJobLog(this.jobLogDo);
/*     */     } else {
/*     */       
/*  95 */       log.error("RWA计算[" + this.jobFullName + "]: 运行异常", e);
/*  96 */       this.taskLogService.exceptionJobLog(this.jobLogDo, e);
/*     */     } 
/*  98 */     JobUtils.clearErrorData(this.jobInfo.getJobId());
/*     */     
/* 100 */     this.jobService.putRwaJob(this.jobInfo);
/* 101 */     this.jobService.syncStopRwaTask();
/*     */     
/* 103 */     JobUtils.removeRwaJob(this.jobInfo);
/*     */   }
/*     */   
/*     */   public void insertErrorData() {
/* 107 */     if (RwaUtils.isSingle(getJobInfo().getTaskType())) {
/*     */       return;
/*     */     }
/* 110 */     this.taskLogService.batchInsertErrorData(JobUtils.getErrorDataList(this.jobInfo.getJobId()), this.jobInfo.getResultNo(), this.jobInfo.getJobType(), 10000);
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaJobListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */