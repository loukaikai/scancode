/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.rest;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.JobType;
/*     */ import com.amarsoft.rwa.engine.constant.StatusCodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.JobQueryRequest;
/*     */ import com.amarsoft.rwa.engine.entity.JobRequest;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataPeriodDo;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataRequest;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*     */ import com.amarsoft.rwa.engine.job.JobUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import java.util.List;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.slf4j.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ 
/*     */ @RestController
/*     */ @CacheConfig
/*     */ @RequestMapping({"/job/"})
/*     */ public class JobRest {
/*  31 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.rest.JobRest.class);
/*     */   
/*     */   @Autowired
/*     */   private TaskService taskService;
/*     */   
/*     */   @Autowired
/*     */   private JobService jobService;
/*     */   
/*     */   @Autowired
/*     */   private ParamService paramService;
/*     */   @Autowired
/*     */   private ResultService resultService;
/*     */   @Autowired
/*     */   private TaskLogService taskLogService;
/*     */   @Autowired
/*     */   private LockService lockService;
/*     */   
/*     */   @RequestMapping({"/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult groupJob(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/*  51 */     Lock lock = this.lockService.getCallApiLock(new String[] { riskDataRequest.getDataBatchNo() });
/*  52 */     lock.lock();
/*     */     try {
/*  54 */       RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(riskDataRequest.getDataBatchNo());
/*  55 */       this.jobService.existsGroupTask(riskDataRequest.getDataBatchNo());
/*     */       
/*  57 */       List<TaskLogDo> taskLogDoList = this.taskLogService.getTaskLogList(dataPeriodDo.getDataDate(), dataPeriodDo.getDataBatchNo(), TaskType.GROUP);
/*  58 */       if (CollUtil.isEmpty(taskLogDoList)) {
/*  59 */         log.error("分组任务[{}]调度异常: 未调用分组任务，请确认调用流程", riskDataRequest.getDataBatchNo());
/*  60 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "分组任务[" + riskDataRequest.getDataBatchNo() + "]调度异常: 未调用分组任务，请确认调用流程");
/*     */       } 
/*  62 */       JobType jobType = StrUtil.equals(Identity.YES.getCode(), riskDataRequest.getIsRetail()) ? JobType.RE_GROUP : JobType.NR_GROUP;
/*  63 */       this.jobService.addGroupTask(riskDataRequest.getDataBatchNo());
/*  64 */       this.jobService.asyncRunGroupJob(TaskType.RWA, jobType, dataPeriodDo.getDataDate(), dataPeriodDo.getDataBatchNo(), taskLogDoList.get(0));
/*     */     } finally {
/*  66 */       lock.unlock();
/*     */     } 
/*  68 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult rwaJob(@Validated @RequestBody JobRequest jobRequest) throws Exception {
/*  74 */     Lock lock = this.lockService.getCallApiLock(new String[] { jobRequest.getResultNo() });
/*  75 */     lock.lock();
/*     */     
/*     */     try {
/*  78 */       TaskLogDo taskLogDo = this.taskLogService.getRwaTaskLog(TaskType.RWA, jobRequest.getResultNo(), null);
/*  79 */       if (taskLogDo == null) {
/*  80 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "RWA作业[" + jobRequest.getResultNo() + "]调度异常: 未调用RWA任务，请确认调用流程");
/*     */       }
/*     */       
/*  83 */       JobType jobType = (JobType)EnumUtils.getEnumByCode(jobRequest.getJobType(), JobType.class);
/*  84 */       if (jobType == JobType.NR_GROUP || jobType == JobType.RE_GROUP) {
/*  85 */         log.error("调度异常： 作业类型不能为分组作业[request=" + jobRequest + "]");
/*  86 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "调度异常： 作业类型不能为分组作业[request=" + jobRequest + "]");
/*     */       } 
/*     */       
/*  89 */       this.jobService.checkRwaRelatedJob(jobRequest.getResultNo(), jobRequest.getDataBatchNo(), jobType);
/*     */ 
/*     */       
/*  92 */       RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(jobRequest.getDataBatchNo());
/*  93 */       this.taskService.checkRiskDataPeriod(dataPeriodDo, jobRequest.getApproach());
/*     */       
/*  95 */       String schemeId = this.paramService.getSchemeId(dataPeriodDo, jobRequest.getApproach());
/*  96 */       SchemeConfigDo schemeConfigDo = this.paramService.initSchemeConfig(schemeId);
/*     */       
/*  98 */       this.jobService.asyncRunRwaJob(jobType, this.taskService.convert2TaskInfo(taskLogDo, jobRequest.getApproach()));
/*     */     } finally {
/* 100 */       lock.unlock();
/*     */     } 
/* 102 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/result"})
/*     */   @ResponseBody
/*     */   public ServiceResult result(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 108 */     Lock lock = this.lockService.getCallApiLock(new String[] { jobQueryRequest.getResultNo() });
/* 109 */     lock.lock();
/*     */     
/*     */     try {
/* 112 */       TaskLogDo taskLogDo = this.taskLogService.getRwaTaskLog(TaskType.RWA, jobQueryRequest.getResultNo(), null);
/* 113 */       if (taskLogDo == null) {
/* 114 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "调度异常： 任务未开始执行，请检查调用是否准确[resultNo=" + jobQueryRequest.getResultNo() + "]");
/*     */       }
/*     */       
/* 117 */       this.jobService.checkRwaRelatedJob(taskLogDo.getResultNo(), taskLogDo.getDataBatchNo());
/*     */ 
/*     */       
/* 120 */       this.jobService.addResultJob(taskLogDo.getResultNo(), taskLogDo.getDataBatchNo());
/* 121 */       SchemeConfigDo schemeConfigDo = this.paramService.getSchemeConfig(taskLogDo.getSchemeId());
/* 122 */       this.jobService.asyncInsertRwaResult(this.taskService.convert2TaskInfo(taskLogDo, schemeConfigDo.getApproach()));
/*     */     } finally {
/* 124 */       lock.unlock();
/*     */     } 
/* 126 */     return ServiceResult.success();
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/query/status/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult queryJobStatus(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 133 */     JobType jobType = (JobType)EnumUtils.getEnumByCode(jobQueryRequest.getJobType(), JobType.class);
/* 134 */     List<JobLogDo> jobLogDoList = this.taskLogService.getRwaJobList(jobQueryRequest.getResultNo(), jobType.getCode());
/* 135 */     if (CollUtil.isEmpty(jobLogDoList))
/*     */     {
/* 137 */       return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "没有关联的作业， 请确认后查询");
/*     */     }
/* 139 */     CalculateStatus calculateStatus = JobUtils.getJobCalculateStatus(jobLogDoList);
/* 140 */     return ServiceResult.success(calculateStatus.getCode());
/*     */   }
/*     */   
/*     */   @RequestMapping({"/rwaConsumer"})
/*     */   @ResponseBody
/*     */   public ServiceResult rwaJobConsumer(@Validated @RequestBody JobInfoDto jobInfo) throws Exception {
/* 146 */     this.jobService.asyncExecuteRwaJob(jobInfo);
/* 147 */     return ServiceResult.success();
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\rest\JobRest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */