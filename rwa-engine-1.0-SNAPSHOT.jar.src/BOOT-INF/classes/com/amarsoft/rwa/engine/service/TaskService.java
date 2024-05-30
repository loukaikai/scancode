/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*     */ import com.amarsoft.rwa.engine.constant.Approach;
/*     */ import com.amarsoft.rwa.engine.constant.CacheId;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.JobType;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataPeriodDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskListStatusDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskRequest;
/*     */ import com.amarsoft.rwa.engine.exception.JobParameterException;
/*     */ import com.amarsoft.rwa.engine.job.JobUtils;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import java.lang.invoke.SerializedLambda;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.concurrent.CountDownLatch;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ 
/*     */ @Service
/*     */ public class TaskService {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.TaskService.class);
/*     */   
/*     */   @Autowired
/*     */   private CommonService commonService;
/*     */   
/*     */   @Autowired
/*     */   private TaskConfigMapper taskConfigMapper;
/*     */   
/*     */   @Autowired
/*     */   private TaskRangeMapper taskRangeMapper;
/*     */   @Autowired
/*     */   private TaskLogService taskLogService;
/*     */   @Autowired
/*     */   private ResultService resultService;
/*     */   @Autowired
/*     */   private JobService jobService;
/*     */   @Autowired
/*     */   private ParamService paramService;
/*     */   @Autowired
/*     */   private LockService lockService;
/*     */   @Autowired
/*     */   private CacheService cacheService;
/*     */   @Autowired
/*     */   private RestTemplate restTemplate;
/*     */   
/*     */   public TaskInfoDto initTaskInfo(TaskRequest req) {
/*  67 */     TaskType taskType = TaskType.RWA;
/*  68 */     if (StrUtil.isNotEmpty(req.getTaskType())) {
/*  69 */       taskType = (TaskType)EnumUtils.getEnumByCode(req.getTaskType(), TaskType.class);
/*     */     }
/*     */     
/*  72 */     this.jobService.checkRwaRelatedJob(req.getResultNo(), req.getDataBatchNo());
/*     */     
/*  74 */     RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(req.getDataBatchNo());
/*  75 */     req.setDataDate(dataPeriodDo.getDataDate());
/*  76 */     boolean isGroup = StrUtil.equals(req.getIsGroup(), Identity.YES.getCode());
/*     */     
/*  78 */     checkRiskDataPeriodConfirm(dataPeriodDo, req.getApproach());
/*     */     
/*  80 */     if (!isGroup) {
/*  81 */       checkRiskDataPeriodGroup(dataPeriodDo);
/*     */     }
/*     */     
/*  84 */     boolean rerun = StrUtil.equals(Identity.YES.getCode(), req.getIsRerun());
/*  85 */     if (!rerun) {
/*  86 */       JobUtils.checkRwaJobLog(this.taskLogService.getRwaJobList(req.getResultNo(), null));
/*     */     }
/*  88 */     TaskConfigDo taskConfigDo = initTaskConfig(req.getTaskId(), taskType, req.getApproach());
/*     */     
/*  90 */     if (StrUtil.isEmpty(req.getSchemeId()) && StrUtil.isEmpty(taskConfigDo.getSchemeId())) {
/*     */       
/*  92 */       req.setSchemeId(this.paramService.getSchemeId(dataPeriodDo, req.getApproach()));
/*  93 */     } else if (StrUtil.isEmpty(req.getSchemeId())) {
/*     */       
/*  95 */       req.setSchemeId(taskConfigDo.getSchemeId());
/*     */     } 
/*  97 */     SchemeConfigDo schemeConfigDo = this.paramService.initSchemeConfig(req.getSchemeId());
/*  98 */     if (schemeConfigDo == null) {
/*  99 */       throw new JobParameterException("非法计量参数-计算方案编号: 无效编号");
/*     */     }
/* 101 */     if (!StrUtil.equals(req.getApproach(), schemeConfigDo.getApproach())) {
/* 102 */       throw new JobParameterException("非法计量参数-计算方案编号: 计算方法不一致");
/*     */     }
/*     */     
/* 105 */     this.paramService.clearHolidayConfig();
/* 106 */     ThreadUtil.safeSleep(100L);
/* 107 */     RwaConfig.setHolidayConfigMap(this.paramService.getHolidayConfigMap());
/*     */     
/* 109 */     if (taskConfigDo.isEnEcCalc()) {
/* 110 */       this.paramService.clearEcConfig();
/* 111 */       ThreadUtil.safeSleep(100L);
/* 112 */       RwaConfig.setEcColumnRuleMap(this.paramService.getEcColumnRuleConfig());
/* 113 */       RwaConfig.setEcFactorList(this.paramService.getEcFactorList());
/*     */     } 
/* 115 */     req.setTaskInfo(convert2TaskInfo(req, taskType, taskConfigDo));
/* 116 */     return req.getTaskInfo();
/*     */   }
/*     */   
/*     */   public TaskConfigDo initTaskConfig(String taskId, TaskType taskType, String approach) {
/* 120 */     TaskConfigDo taskConfigDo = null;
/* 121 */     if (StrUtil.isEmpty(taskId)) {
/* 122 */       taskConfigDo = new TaskConfigDo();
/* 123 */       taskConfigDo.setTaskType(taskType.getCode());
/* 124 */       taskConfigDo.setConsolidateFlag(Identity.NO.getCode());
/* 125 */       taskConfigDo.setIsCalcCva(Identity.YES.getCode());
/* 126 */       taskConfigDo.setEnEcCalc(false);
/*     */     } else {
/* 128 */       taskConfigDo = getTaskConfig(taskId);
/* 129 */       if (!StrUtil.equals(taskType.getCode(), taskConfigDo.getTaskType())) {
/* 130 */         throw new JobParameterException("任务[" + taskId + "]配置的任务类型[" + taskConfigDo.getTaskType() + "]与接口参数[" + taskType.getCode() + "]不一致");
/*     */       }
/* 132 */       if (!StrUtil.equals(approach, taskConfigDo.getApproach())) {
/* 133 */         throw new JobParameterException("任务[" + taskId + "]配置的计算方法[" + taskConfigDo.getApproach() + "]与接口参数[" + approach + "]不一致");
/*     */       }
/* 135 */       if (taskConfigDo.isEnEcCalc() && 
/* 136 */         taskConfigDo.getEcOf() == null) {
/* 137 */         throw new JobParameterException("任务[" + taskId + "]启用经济资本计算但未配置经济资本占用系数");
/*     */       }
/*     */     } 
/*     */     
/* 141 */     return taskConfigDo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void checkRiskDataPeriod(RiskDataPeriodDo dataPeriodDo, String approach) {
/* 150 */     checkRiskDataPeriodConfirm(dataPeriodDo, approach);
/* 151 */     checkRiskDataPeriodGroup(dataPeriodDo);
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkRiskDataPeriodConfirm(RiskDataPeriodDo dataPeriodDo, String approach) {
/* 156 */     String confirmFlag = StrUtil.equals(approach, Approach.WA.getCode()) ? dataPeriodDo.getWaConfirmFlag() : dataPeriodDo.getIrbConfirmFlag();
/* 157 */     if (!StrUtil.equals(Identity.YES.getCode(), confirmFlag)) {
/* 158 */       throw new JobParameterException("非法数据批次-数据未确认");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkRiskDataPeriodGroup(RiskDataPeriodDo dataPeriodDo) {
/* 164 */     if (!StrUtil.equals(Identity.YES.getCode(), dataPeriodDo.getGroupFlag())) {
/* 165 */       throw new JobParameterException("非法数据批次[" + dataPeriodDo.getDataBatchNo() + "]-数据未分组");
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   @Async("myTaskExecutor")
/*     */   public void asyncExecuteGroupTask(Date dataDate, String dataBatchNo) {
/* 176 */     executeGroupTask(TaskType.RWA, dataDate, dataBatchNo);
/*     */   }
/*     */   
/*     */   public CalculateStatus executeGroupTask(TaskType taskType, Date dataDate, String dataBatchNo) {
/* 180 */     log.info("分组任务[{}-{}]-开始", taskType.getName(), dataBatchNo);
/*     */ 
/*     */     
/* 183 */     TaskLogDo taskLogDo = this.taskLogService.initGroupTaskLog(dataDate, dataBatchNo);
/* 184 */     CalculateStatus calculateStatus = CalculateStatus.CALCULATE;
/*     */     
/*     */     try {
/* 187 */       if (RwaUtils.isSingle(taskType)) {
/*     */         
/* 189 */         this.taskLogService.insertGroupJobLog(JobType.NR_GROUP, dataDate, dataBatchNo, taskLogDo.getLogNo());
/* 190 */         this.resultService.initSingleGroupResult(dataBatchNo);
/*     */         
/* 192 */         this.jobService.executeGroupJob(taskType, JobType.NR_GROUP, dataBatchNo, taskLogDo.getLogNo());
/*     */       } else {
/*     */         
/* 195 */         this.taskLogService.initGroupJobLog(dataDate, dataBatchNo, taskLogDo.getLogNo());
/* 196 */         this.resultService.initGroupResult(taskType, dataBatchNo);
/* 197 */         this.jobService.asyncExecuteGroupJob(taskType, JobType.NR_GROUP, dataBatchNo, taskLogDo.getLogNo());
/* 198 */         this.jobService.asyncExecuteGroupJob(taskType, JobType.RE_GROUP, dataBatchNo, taskLogDo.getLogNo());
/*     */       } 
/*     */       
/* 201 */       Map<String, List<JobLogDo>> statJobLog = this.jobService.waitJobFinishAndGetStatLog(taskLogDo.getLogNo(), false);
/* 202 */       calculateStatus = JobUtils.getCalculateStatus(statJobLog.keySet());
/*     */       
/* 204 */       if (calculateStatus == CalculateStatus.COMPLETE && 
/* 205 */         !RwaUtils.isSingle(taskType)) {
/* 206 */         this.resultService.updateGroupFlag(dataBatchNo, Identity.YES.getCode());
/*     */       }
/*     */ 
/*     */       
/* 210 */       this.taskLogService.endTaskLog(taskLogDo, calculateStatus, null);
/* 211 */       log.info("分组任务[{}-{}]-{}", new Object[] { taskType.getName(), dataBatchNo, calculateStatus.getName() });
/* 212 */     } catch (Exception e) {
/* 213 */       log.info("分组任务[" + taskType.getName() + "-" + dataBatchNo + "]-异常", e);
/* 214 */       calculateStatus = CalculateStatus.EXCEPTION;
/* 215 */       this.taskLogService.exceptionTaskLog(taskLogDo, e);
/*     */     } finally {
/* 217 */       this.jobService.removeGroupTask(dataBatchNo);
/* 218 */       this.jobService.removeGroupJob(dataBatchNo);
/* 219 */       this.jobService.removeStopGroupTask(dataBatchNo);
/* 220 */       JobUtils.gc(false);
/*     */     } 
/* 222 */     return calculateStatus;
/*     */   }
/*     */   
/*     */   @Async("myTaskExecutor")
/*     */   public void asyncExecuteImmediateTask(TaskConfigDo taskConfigDo) {
/* 227 */     SchemeConfigDo schemeConfigDo = this.paramService.initSchemeConfig(taskConfigDo.getSchemeId());
/* 228 */     TaskInfoDto taskInfo = convert2TaskInfo(taskConfigDo);
/* 229 */     CalculateStatus calculateStatus = executeRwaTask(taskInfo);
/* 230 */     updateTaskStatus(taskConfigDo, calculateStatus);
/*     */   }
/*     */   
/*     */   public TaskInfoDto convert2TaskInfo(TaskRequest req, TaskType taskType, TaskConfigDo taskConfigDo) {
/* 234 */     TaskInfoDto taskInfo = new TaskInfoDto();
/* 235 */     taskInfo.setTaskType(taskType);
/* 236 */     taskInfo.setResultNo(req.getResultNo());
/* 237 */     taskInfo.setDataBatchNo(req.getDataBatchNo());
/* 238 */     taskInfo.setDataDate(req.getDataDate());
/* 239 */     taskInfo.setApproach(req.getApproach());
/* 240 */     taskInfo.setIsGroup(req.getIsGroup());
/* 241 */     taskInfo.setSchemeId(req.getSchemeId());
/* 242 */     taskInfo.setTaskId(req.getTaskId());
/* 243 */     taskInfo.setTaskConfigDo(taskConfigDo);
/* 244 */     return taskInfo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public String initTaskList(List<TaskRequest> taskList) {
/* 254 */     Lock lock = this.lockService.getCallApiLock(LockType.TL, new String[] { "0" });
/* 255 */     lock.lock();
/* 256 */     TaskListStatusDto statusDto = null;
/*     */     try {
/* 258 */       Set<String> keys = this.cacheService.findKeys(CacheId.TASK_LIST.getCode());
/* 259 */       if (CollUtil.isNotEmpty(keys)) {
/* 260 */         throw new JobParameterException("当前已存在批量任务列表， 不能同时多次调用！");
/*     */       }
/* 262 */       statusDto = new TaskListStatusDto();
/* 263 */       statusDto.setId(IdWorker.getIdStr());
/* 264 */       statusDto.setStatus(CalculateStatus.CREATED.getCode());
/* 265 */       statusDto.setStart(DataUtils.nowTimestamp());
/* 266 */       statusDto.setSize(taskList.size());
/* 267 */       statusDto.setStatusMap(new LinkedHashMap<>());
/* 268 */       this.cacheService.putCache(CacheId.TASK_LIST.getCode(), statusDto.getId());
/* 269 */       this.cacheService.putCache(CacheId.TASK_STATUS.getCode(), statusDto.getId(), statusDto);
/*     */     } finally {
/* 271 */       lock.unlock();
/*     */     } 
/*     */     
/* 274 */     ((com.amarsoft.rwa.engine.service.TaskService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.TaskService.class)).pollTaskListStatus(statusDto, taskList);
/* 275 */     return statusDto.getId();
/*     */   }
/*     */ 
/*     */   
/*     */   @Async("myTaskExecutor")
/*     */   public void pollTaskListStatus(TaskListStatusDto statusDto, List<TaskRequest> taskList) {
/* 281 */     long interval = Math.max(30000L, JobUtils.taskPollingInterval);
/*     */     while (true) {
/* 283 */       TreeMap<String, Integer> dist = new TreeMap<>();
/* 284 */       for (TaskRequest req : taskList) {
/* 285 */         String sts = null;
/* 286 */         if (req.getTaskInfo() == null) {
/*     */           
/* 288 */           sts = CalculateStatus.EXCEPTION.getCode();
/*     */         } else {
/* 290 */           sts = getRwaTaskStatus(req.getResultNo(), statusDto.getStart());
/*     */         } 
/* 292 */         statusDto.getStatusMap().put(req.getResultNo(), sts);
/* 293 */         DataUtils.putCountMap(dist, sts);
/*     */       } 
/* 295 */       statusDto.setStatus(JobUtils.getCalculateStatus(statusDto.getStatusMap().values()).getCode());
/* 296 */       statusDto.setStatusDist(dist);
/*     */       
/* 298 */       this.cacheService.putCache(CacheId.TASK_STATUS.getCode(), statusDto.getId(), statusDto);
/*     */       
/* 300 */       if (JobUtils.isEnd(statusDto.getStatus())) {
/* 301 */         statusDto.setEnd(DataUtils.nowTimestamp());
/* 302 */         this.cacheService.putCache(CacheId.TASK_STATUS.getCode(), statusDto.getId(), statusDto);
/*     */         break;
/*     */       } 
/* 305 */       ThreadUtil.safeSleep(interval);
/*     */     } 
/*     */     
/* 308 */     this.cacheService.deleteCache(CacheId.TASK_LIST.getCode(), statusDto.getId());
/*     */   }
/*     */ 
/*     */   
/*     */   public String getRwaTaskStatus(String resultNo, Timestamp startTime) {
/* 313 */     if (this.jobService.existsResultJobByResultNo(resultNo)) {
/* 314 */       return CalculateStatus.CALCULATE.getCode();
/*     */     }
/*     */     
/* 317 */     return this.taskLogService.getRwaTaskStatus(resultNo, startTime);
/*     */   }
/*     */   
/*     */   @Async("myTaskExecutor")
/*     */   public void asyncExecuteRwaTaskFull(TaskInfoDto taskInfo) {
/* 322 */     executeRwaTaskFull(taskInfo);
/*     */   }
/*     */ 
/*     */   
/*     */   public CalculateStatus executeRwaTaskFull(TaskInfoDto taskInfo) {
/*     */     try {
/* 328 */       if (StrUtil.equals(taskInfo.getIsGroup(), Identity.YES.getCode())) {
/* 329 */         this.resultService.updateGroupFlag(taskInfo.getDataBatchNo(), Identity.NO.getCode());
/* 330 */         CalculateStatus calculateStatus = executeGroupTask(taskInfo.getTaskType(), taskInfo.getDataDate(), taskInfo.getDataBatchNo());
/* 331 */         if (calculateStatus != CalculateStatus.COMPLETE) {
/* 332 */           taskInfo.setStatus(calculateStatus);
/* 333 */           return calculateStatus;
/*     */         } 
/*     */       } 
/*     */       
/* 337 */       CalculateStatus status = executeRwaTask(taskInfo);
/* 338 */       taskInfo.setStatus(status);
/* 339 */       return status;
/*     */     } finally {
/* 341 */       this.jobService.removeResultJob(taskInfo.getResultNo());
/*     */     } 
/*     */   }
/*     */   
/*     */   public CalculateStatus executeRwaTask(TaskInfoDto taskInfo) {
/* 346 */     String taskName = taskInfo.getTaskType().getName() + "(" + taskInfo.getResultNo() + "-" + taskInfo.getDataBatchNo() + "-" + taskInfo.getApproach() + ")";
/* 347 */     log.info("RWA计算[{}]: 开始运行", taskName);
/* 348 */     List<JobInfoDto> jobList = null;
/* 349 */     CalculateStatus calculateStatus = CalculateStatus.CALCULATE;
/* 350 */     boolean isSubTableInsert = this.jobService.isSubTableInsert(taskInfo.getTaskType());
/*     */     
/*     */     try {
/* 353 */       this.taskLogService.initTaskLog(taskInfo);
/*     */       
/* 355 */       this.resultService.initRwaResult(taskInfo.getTaskType(), taskInfo.getResultNo());
/*     */       
/* 357 */       jobList = getJobList(taskInfo);
/*     */       
/* 359 */       jobList = this.jobService.initJobLog(taskInfo.getTaskLogDo(), jobList);
/*     */       
/* 361 */       taskInfo.getTaskLogDo().setTaskInitTime(DataUtils.nowTimestamp());
/* 362 */       this.taskLogService.updateTaskLog(taskInfo.getTaskLogDo());
/*     */       
/* 364 */       if (RwaUtils.isSingle(taskInfo.getTaskType())) {
/*     */         
/* 366 */         for (JobInfoDto jobInfo : jobList) {
/* 367 */           this.jobService.executeRwaJob(jobInfo);
/*     */         }
/*     */       } else {
/*     */         
/* 371 */         distributeJobs(jobList);
/*     */       } 
/*     */       
/* 374 */       Map<String, List<JobLogDo>> statJobLog = this.jobService.waitJobFinishAndGetStatLog(taskInfo.getLogNo(), false);
/*     */       
/* 376 */       taskInfo.getTaskLogDo().setJobCalcTime(DataUtils.nowTimestamp());
/* 377 */       this.taskLogService.updateTaskLog(taskInfo.getTaskLogDo());
/*     */       
/* 379 */       if (isSubTableInsert) {
/* 380 */         daemonInsertResult(taskInfo.getResultNo(), new JobType[] { JobType.NR, JobType.RE });
/* 381 */         taskInfo.getTaskLogDo().setStWriteTime(DataUtils.nowTimestamp());
/* 382 */         this.taskLogService.updateTaskLog(taskInfo.getTaskLogDo());
/*     */       } 
/* 384 */       calculateStatus = JobUtils.getCalculateStatus(statJobLog.keySet());
/*     */       
/* 386 */       if (calculateStatus == CalculateStatus.COMPLETE) {
/* 387 */         calculateStatus = this.jobService.insertRwaResult(taskInfo);
/*     */       }
/*     */       
/* 390 */       this.taskLogService.endTaskLog(taskInfo.getTaskLogDo(), calculateStatus, null);
/* 391 */       log.info("RWA计算[{}]: {}运行", taskName, calculateStatus.getName());
/* 392 */     } catch (Exception e) {
/* 393 */       log.error("RWA计算[" + taskName + "]: 运行异常", e);
/*     */       
/* 395 */       this.jobService.addStopRwaTask(taskInfo.getResultNo());
/*     */       
/* 397 */       this.jobService.waitJobFinishAndGetStatLog(taskInfo.getLogNo(), true);
/* 398 */       this.taskLogService.exceptionTaskLog(taskInfo.getTaskLogDo(), e);
/* 399 */       calculateStatus = CalculateStatus.EXCEPTION;
/*     */     } finally {
/*     */       
/* 402 */       this.jobService.removeRwaJob(taskInfo.getResultNo());
/* 403 */       this.jobService.removeResultJob(taskInfo.getResultNo());
/* 404 */       this.jobService.removeStopRwaTask(taskInfo.getResultNo());
/* 405 */       this.jobService.clearCva(taskInfo.getResultNo());
/*     */       
/* 407 */       if (isSubTableInsert) {
/* 408 */         dropTempTable(taskInfo.getResultNo(), null);
/* 409 */         log.info("RWA计算[{}}]: 删除结果临时表完成！", taskName);
/*     */       } 
/*     */     } 
/*     */     
/* 413 */     return calculateStatus;
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void distributeJobs(List<JobInfoDto> jobList) {
/* 418 */     if (JobUtils.enableLocalThreadCalculate) {
/* 419 */       for (JobInfoDto jobInfo : jobList) {
/* 420 */         this.jobService.asyncExecuteRwaJob(jobInfo);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 425 */     List<String> urlList = this.commonService.getActiveServices(true);
/* 426 */     if (CollUtil.isEmpty(urlList)) {
/* 427 */       for (JobInfoDto jobInfo : jobList) {
/* 428 */         this.jobService.asyncExecuteRwaJob(jobInfo);
/*     */       }
/*     */       
/*     */       return;
/*     */     } 
/* 433 */     int size = urlList.size();
/* 434 */     int i = 0;
/* 435 */     for (JobInfoDto jobInfo : jobList) {
/* 436 */       ThreadUtil.safeSleep(100L);
/* 437 */       distributeJob(jobInfo, urlList.get(i++ % size));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void distributeJob(JobInfoDto jobInfo, String url) {
/* 443 */     log.debug("------------------->: 作业url-{}", url);
/* 444 */     if (!url.endsWith("/")) {
/* 445 */       url = url + "/";
/*     */     }
/* 447 */     String job = "job/rwaConsumer";
/* 448 */     HttpHeaders headers = new HttpHeaders();
/* 449 */     headers.setContentType(MediaType.APPLICATION_JSON);
/* 450 */     HttpEntity<JobInfoDto> request = new HttpEntity(jobInfo, (MultiValueMap)headers);
/* 451 */     ServiceResult result = (ServiceResult)this.restTemplate.postForObject(url + job, request, ServiceResult.class, new Object[0]);
/* 452 */     log.debug("-------------------<< result : {}", JsonUtils.object2Json(result));
/*     */   }
/*     */   
/*     */   public List<JobInfoDto> getJobList(TaskInfoDto taskInfo) {
/* 456 */     Approach approach = (Approach)EnumUtils.getEnumByCode(taskInfo.getApproach(), Approach.class);
/*     */     
/* 458 */     CreditRuleDo ruleDo = RwaConfig.getCreditRule(taskInfo.getSchemeId(), Approach.WA.getCode());
/* 459 */     List<JobInfoDto> list = new ArrayList<>();
/*     */     
/* 461 */     addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.NR, approach));
/*     */     
/* 463 */     addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.RE, approach));
/*     */     
/* 465 */     if (ruleDo.isEnAbsCalc()) {
/* 466 */       addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.ABS, approach));
/*     */     }
/*     */     
/* 469 */     if (ruleDo.isEnAmpCalc()) {
/* 470 */       addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.AMP, approach));
/*     */     }
/*     */     
/* 473 */     if (ruleDo.isEnDiCalc()) {
/* 474 */       addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.DI, approach));
/*     */     }
/*     */     
/* 477 */     if (ruleDo.isEnSftCalc()) {
/* 478 */       addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.SFT, approach));
/*     */     }
/*     */     
/* 481 */     if (ruleDo.isEnCcpCalc()) {
/* 482 */       addJobInfo(list, this.jobService.getJobInfoList(taskInfo, JobType.CCP, approach));
/*     */     }
/* 484 */     return list;
/*     */   }
/*     */   
/*     */   public void addJobInfo(List<JobInfoDto> list, List<JobInfoDto> subList) {
/* 488 */     if (CollUtil.isEmpty(subList)) {
/*     */       return;
/*     */     }
/* 491 */     list.addAll(subList);
/*     */   }
/*     */   
/*     */   public void daemonInsertResult(String resultNo, JobType... jobTypes) throws InterruptedException {
/* 495 */     ThreadPoolTaskExecutor taskExecutor = JobUtils.getThreadPoolTaskExecutor(2, 2, "daemon-insert-" + resultNo + "-");
/* 496 */     int size = jobTypes.length;
/* 497 */     CountDownLatch latch = new CountDownLatch(size);
/* 498 */     for (JobType jobType : jobTypes) {
/*     */       
/* 500 */       taskExecutor.execute(() -> {
/*     */             try {
/*     */               temp2Result(resultNo, jobType);
/*     */             } finally {
/*     */               latch.countDown();
/*     */             } 
/*     */           });
/*     */     } 
/* 508 */     latch.await();
/* 509 */     taskExecutor.shutdown();
/*     */   }
/*     */ 
/*     */   
/*     */   public void temp2Result(String resultNo, JobType jobType) {
/* 514 */     long interval = Math.max(3000L, JobUtils.taskPollingInterval);
/*     */     while (true) {
/* 516 */       List<JobLogDo> jobLogDoList = this.taskLogService.getRwaJobList(resultNo, jobType.getCode());
/*     */       
/* 518 */       if (isOverOfTempInsert(jobType, jobLogDoList)) {
/*     */         break;
/*     */       }
/* 521 */       ThreadUtil.safeSleep(interval);
/*     */     } 
/*     */   }
/*     */   
/*     */   public boolean isOverOfTempInsert(JobType jobType, List<JobLogDo> jobLogDoList) {
/* 526 */     if (CollUtil.isEmpty(jobLogDoList))
/*     */     {
/* 528 */       return true;
/*     */     }
/* 530 */     int n = 0;
/* 531 */     for (JobLogDo jobLogDo : jobLogDoList) {
/*     */       
/* 533 */       if (JobUtils.isEnd(jobLogDo.getCalculateStatus())) {
/* 534 */         if (StrUtil.equals(jobLogDo.getRecordFlag(), Identity.YES.getCode())) {
/*     */           continue;
/*     */         }
/* 537 */         this.resultService.temp2Result(jobType, jobLogDo.getJobId());
/* 538 */         jobLogDo.setRecordFlag(Identity.YES.getCode());
/* 539 */         this.taskLogService.updateJobLog(jobLogDo);
/* 540 */         log.info("任务[{}]作业[{}-{}]插入结果完成!", new Object[] { jobLogDo.getResultNo(), jobLogDo.getJobType(), jobLogDo.getJobId() });
/*     */         
/*     */         continue;
/*     */       } 
/*     */       
/* 545 */       n++;
/*     */     } 
/* 547 */     return (n == 0);
/*     */   }
/*     */   
/*     */   public void dropTempTable(String resultNo, String jobType) {
/* 551 */     List<JobLogDo> jobLogDoList = this.taskLogService.getRwaJobList(resultNo, jobType);
/* 552 */     if (CollUtil.isEmpty(jobLogDoList)) {
/*     */       return;
/*     */     }
/* 555 */     for (JobLogDo jobLogDo : jobLogDoList) {
/* 556 */       this.resultService.dropTempResultTable(jobLogDo.getJobType(), jobLogDo.getJobId());
/*     */     }
/*     */   }
/*     */   
/*     */   public TaskLogDo calculateRwaTaskInfo(Collection<JobInfoDto> jobInfos, TaskLogDo taskLogDo) {
/* 561 */     TaskLogDo info = new TaskLogDo();
/* 562 */     info.setLogNo(taskLogDo.getLogNo());
/* 563 */     info.setResultNo(taskLogDo.getResultNo());
/* 564 */     info.setDataBatchNo(taskLogDo.getDataBatchNo());
/* 565 */     info.setDataDate(taskLogDo.getDataDate());
/* 566 */     info.setTaskType(taskLogDo.getTaskType());
/* 567 */     info.setTaskId(taskLogDo.getTaskId());
/* 568 */     info.setSchemeId(taskLogDo.getSchemeId());
/* 569 */     info.setStartTime(taskLogDo.getStartTime());
/*     */     
/* 571 */     Map<String, JobLogDo> jobLogMap = (Map<String, JobLogDo>)taskLogDo.getJobLogList().stream().collect(Collectors.toMap(JobLogDo::getJobId, Function.identity()));
/*     */ 
/*     */ 
/*     */     
/* 575 */     Map<String, Integer> jobStatusMap = new TreeMap<>();
/* 576 */     info.setJobStatusMap(jobStatusMap);
/*     */     
/* 578 */     for (JobInfoDto jobInfo : jobInfos) {
/* 579 */       JobLogDo jobLogDo = jobInfo.getJobLog();
/*     */       
/* 581 */       info.setCalculateNum(info.getCalculateNum() + jobLogDo.getCalculateNum().intValue());
/*     */       
/* 583 */       info.setNormalCalcCnt(info.getNormalCalcCnt() + jobLogDo.getCtCalculateCount().get());
/* 584 */       info.setNormalSkipCnt(info.getNormalSkipCnt() + jobLogDo.getCtSkipCount().get());
/* 585 */       info.setExceptionSkipCnt(info.getExceptionSkipCnt() + jobLogDo.getCtExceptionCount().get());
/*     */       
/* 587 */       info.setTotalCalcCnt(info.getTotalCalcCnt() + jobLogDo.getCtCount().get());
/*     */       
/* 589 */       DataUtils.putCountMap(jobStatusMap, jobLogDo.getCalculateStatus());
/*     */       
/* 591 */       jobLogMap.remove(jobLogDo.getJobId());
/*     */     } 
/*     */     
/* 594 */     for (JobLogDo jobLogDo : jobLogMap.values()) {
/*     */       
/* 596 */       info.setCalculateNum(info.getCalculateNum() + jobLogDo.getCalculateNum().intValue());
/*     */       
/* 598 */       int count = 0;
/* 599 */       if (jobLogDo.getNormalCalcCnt() != null) {
/* 600 */         count += jobLogDo.getNormalCalcCnt().intValue();
/* 601 */         info.setNormalCalcCnt(info.getNormalCalcCnt() + jobLogDo.getNormalCalcCnt().intValue());
/*     */       } 
/* 603 */       if (jobLogDo.getNormalSkipCnt() != null) {
/* 604 */         count += jobLogDo.getNormalSkipCnt().intValue();
/* 605 */         info.setNormalSkipCnt(info.getNormalSkipCnt() + jobLogDo.getNormalSkipCnt().intValue());
/*     */       } 
/* 607 */       if (jobLogDo.getExceptionSkipCnt() != null) {
/* 608 */         count += jobLogDo.getExceptionSkipCnt().intValue();
/* 609 */         info.setExceptionSkipCnt(info.getExceptionSkipCnt() + jobLogDo.getExceptionSkipCnt().intValue());
/*     */       } 
/*     */       
/* 612 */       info.setTotalCalcCnt(info.getTotalCalcCnt() + count);
/*     */       
/* 614 */       DataUtils.putCountMap(jobStatusMap, jobLogDo.getCalculateStatus());
/*     */     } 
/*     */     
/* 617 */     info.setTaskStatus(JobUtils.getCalculateStatus(jobStatusMap.keySet()).getCode());
/* 618 */     if (JobUtils.isEnd(info.getTaskStatus())) {
/* 619 */       info.setEndTime(taskLogDo.getEndTime());
/*     */     } else {
/* 621 */       info.setEndTime(DataUtils.nowTimestamp());
/*     */     } 
/* 623 */     info.setCalcTime(DataUtils.timeBetween(info.getStartTime(), info.getEndTime()));
/*     */     
/* 625 */     info.setProgress(RwaMath.mul(RwaMath.div(Integer.valueOf(info.getTotalCalcCnt()), Integer.valueOf(info.getCalculateNum())), RwaMath.sub(BigDecimal.ONE, JobUtils.rwaResultTimePercent)));
/* 626 */     return info;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public TaskLogDo calculateGroupTaskInfo(Collection<JobLogDo> jobLogs, TaskLogDo taskLogDo) {
/* 635 */     TaskLogDo info = new TaskLogDo();
/* 636 */     info.setLogNo(taskLogDo.getLogNo());
/* 637 */     info.setResultNo(taskLogDo.getResultNo());
/* 638 */     info.setDataBatchNo(taskLogDo.getDataBatchNo());
/* 639 */     info.setDataDate(taskLogDo.getDataDate());
/* 640 */     info.setTaskType(taskLogDo.getTaskType());
/* 641 */     info.setTaskId(taskLogDo.getTaskId());
/* 642 */     info.setStartTime(taskLogDo.getStartTime());
/* 643 */     info.setCalculateNum(14);
/*     */     
/* 645 */     Map<String, Integer> jobStatusMap = new HashMap<>();
/* 646 */     info.setJobStatusMap(jobStatusMap);
/*     */     
/* 648 */     BigDecimal progress = BigDecimal.ZERO;
/* 649 */     for (JobLogDo jobLogDo : jobLogs) {
/* 650 */       DataUtils.putCountMap(jobStatusMap, jobLogDo.getCalculateStatus());
/* 651 */       info.setTotalCalcCnt(info.getTotalCalcCnt() + jobLogDo.getCtCount().get());
/* 652 */       progress = RwaMath.add(progress, calculateGroupProgress(jobLogDo.getJobType(), jobLogDo.getCtCount().get(), JobUtils.groupTimePercent));
/*     */     } 
/* 654 */     info.setProgress(progress);
/*     */     
/* 656 */     info.setTaskStatus(JobUtils.getCalculateStatus(jobStatusMap.keySet()).getCode());
/* 657 */     if (JobUtils.isEnd(info.getTaskStatus())) {
/* 658 */       info.setEndTime(taskLogDo.getEndTime());
/*     */     } else {
/* 660 */       info.setEndTime(DataUtils.nowTimestamp());
/*     */     } 
/* 662 */     info.setCalcTime(DataUtils.timeBetween(info.getStartTime(), info.getEndTime()));
/* 663 */     return info;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal calculateGroupProgress(String jobType, int calcCnt, List<BigDecimal> groupTimePercent) {
/* 668 */     if (CollUtil.isEmpty(groupTimePercent) || groupTimePercent.size() < 14) {
/* 669 */       return RwaMath.div(Integer.valueOf(calcCnt), Integer.valueOf(7));
/*     */     }
/* 671 */     int start = 0;
/* 672 */     if (StrUtil.equals(jobType, JobType.RE_GROUP.getCode())) {
/* 673 */       start = 7;
/*     */     }
/* 675 */     BigDecimal progress = BigDecimal.ZERO;
/* 676 */     for (int i = 0; i < calcCnt; i++) {
/* 677 */       progress = RwaMath.add(progress, groupTimePercent.get(start + i));
/*     */     }
/* 679 */     return progress;
/*     */   }
/*     */   
/*     */   public TaskConfigDo getTaskConfig(String taskId) {
/* 683 */     return (TaskConfigDo)this.taskConfigMapper.selectById(taskId);
/*     */   }
/*     */   
/*     */   public void updateTaskConfig(TaskConfigDo taskConfigDo) {
/* 687 */     this.taskConfigMapper.updateById(taskConfigDo);
/*     */   }
/*     */   
/*     */   public void updateTaskStatus(TaskConfigDo taskConfigDo, CalculateStatus calculateStatus) {
/* 691 */     taskConfigDo.setTaskStatus(calculateStatus.getCode());
/* 692 */     updateTaskConfig(taskConfigDo);
/*     */   }
/*     */   
/*     */   public TaskInfoDto convert2TaskInfo(TaskLogDo taskLogDo, String approach) {
/* 696 */     TaskType taskType = (TaskType)EnumUtils.getEnumByCode(taskLogDo.getTaskType(), TaskType.class);
/* 697 */     TaskConfigDo taskConfigDo = initTaskConfig(taskLogDo.getTaskId(), taskType, approach);
/* 698 */     return new TaskInfoDto(taskType, taskLogDo.getResultNo(), taskLogDo.getDataBatchNo(), taskLogDo.getDataDate(), approach, Identity.NO
/* 699 */         .getCode(), taskLogDo.getSchemeId(), taskLogDo.getTaskId(), taskConfigDo, taskLogDo.getLogNo(), taskLogDo, null);
/*     */   }
/*     */   
/*     */   public TaskInfoDto convert2TaskInfo(TaskConfigDo taskConfigDo) {
/* 703 */     TaskType taskType = (TaskType)EnumUtils.getEnumByCode(taskConfigDo.getTaskType(), TaskType.class);
/* 704 */     return new TaskInfoDto(taskType, taskConfigDo.getTaskId(), taskConfigDo.getDataBatchNo(), taskConfigDo.getDataDate(), taskConfigDo.getApproach(), Identity.NO
/* 705 */         .getCode(), taskConfigDo.getSchemeId(), taskConfigDo.getTaskId(), taskConfigDo, null, null, null);
/*     */   }
/*     */   
/*     */   public List<TaskRangeDo> selectTaskRangeList(String taskId) {
/* 709 */     LambdaQueryWrapper<TaskRangeDo> queryWrapper = new LambdaQueryWrapper();
/* 710 */     queryWrapper.eq(TaskRangeDo::getTaskId, taskId);
/* 711 */     return this.taskRangeMapper.selectList((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public Map<String, List<TaskRangeDo>> getTaskRangeMap(String taskId) {
/* 715 */     List<TaskRangeDo> list = selectTaskRangeList(taskId);
/* 716 */     if (CollUtil.isEmpty(list)) {
/* 717 */       return new HashMap<>();
/*     */     }
/* 719 */     return (Map<String, List<TaskRangeDo>>)list.stream().collect(Collectors.groupingBy(TaskRangeDo::getCreditRiskDataType));
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\TaskService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */