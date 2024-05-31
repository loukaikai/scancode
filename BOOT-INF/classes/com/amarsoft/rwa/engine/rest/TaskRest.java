/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.rest;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.LockType;
/*     */ import com.amarsoft.rwa.engine.constant.StatusCodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.entity.CommonRequest;
/*     */ import com.amarsoft.rwa.engine.entity.IdRequest;
/*     */ import com.amarsoft.rwa.engine.entity.JobQueryRequest;
/*     */ import com.amarsoft.rwa.engine.entity.MonitorRequest;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataPeriodDo;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataRequest;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskRequest;
/*     */ import com.amarsoft.rwa.engine.exception.JobParameterException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.validation.annotation.Validated;
/*     */ import org.springframework.web.bind.annotation.RequestBody;
/*     */ import org.springframework.web.bind.annotation.RequestMapping;
/*     */ import org.springframework.web.bind.annotation.ResponseBody;
/*     */ 
/*     */ @RestController
/*     */ @CacheConfig
/*     */ @RequestMapping({"/task/"})
/*     */ public class TaskRest {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.rest.TaskRest.class);
/*     */   
/*     */   @Autowired
/*     */   private ParamService paramService;
/*     */   
/*     */   @Autowired
/*     */   private ResultService resultService;
/*     */   @Autowired
/*     */   private TaskLogService taskLogService;
/*     */   @Autowired
/*     */   private ConfigService configService;
/*     */   @Autowired
/*     */   private MonitorService monitorService;
/*     */   @Autowired
/*     */   private LockService lockService;
/*     */   @Autowired
/*     */   private TaskService taskService;
/*     */   @Autowired
/*     */   private JobService jobService;
/*     */   @Autowired
/*     */   private CacheService cacheService;
/*     */   
/*     */   @RequestMapping({"/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult groupTask(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/*  60 */     Lock lock = this.lockService.getCallApiLock(new String[] { riskDataRequest.getDataBatchNo() });
/*  61 */     lock.lock();
/*     */     
/*     */     try {
/*  64 */       this.jobService.checkGroupRelatedJob(riskDataRequest.getDataBatchNo());
/*  65 */       boolean rerun = StrUtil.equals(Identity.YES.getCode(), riskDataRequest.getIsRerun());
/*  66 */       RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(riskDataRequest.getDataBatchNo());
/*     */       
/*  68 */       if (!rerun && StrUtil.equals(Identity.YES.getCode(), dataPeriodDo.getGroupFlag())) {
/*  69 */         log.error("分组任务[{}]已完成", riskDataRequest.getDataBatchNo());
/*  70 */         return ServiceResult.success();
/*     */       } 
/*     */       
/*  73 */       this.jobService.addGroupTask(riskDataRequest.getDataBatchNo());
/*     */       
/*  75 */       this.resultService.updateGroupFlag(riskDataRequest.getDataBatchNo(), Identity.NO.getCode());
/*     */       
/*  77 */       this.taskService.asyncExecuteGroupTask(dataPeriodDo.getDataDate(), dataPeriodDo.getDataBatchNo());
/*     */     } finally {
/*  79 */       lock.unlock();
/*     */     } 
/*  81 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult rwaTask(@Validated @RequestBody TaskRequest req) throws Exception {
/*  87 */     Lock lock = this.lockService.getCallApiLock(new String[] { req.getResultNo() });
/*  88 */     lock.lock();
/*     */     
/*     */     try {
/*  91 */       TaskInfoDto taskInfo = this.taskService.initTaskInfo(req);
/*     */       
/*  93 */       this.jobService.addResultJob(taskInfo.getResultNo(), taskInfo.getDataBatchNo());
/*     */       
/*  95 */       this.taskService.asyncExecuteRwaTaskFull(taskInfo);
/*     */     } finally {
/*  97 */       lock.unlock();
/*     */     } 
/*  99 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/rwaList"})
/*     */   @ResponseBody
/*     */   public ServiceResult rwaTaskList(@Validated @RequestBody List<TaskRequest> taskList) throws Exception {
/* 105 */     String id = this.taskService.initTaskList(taskList);
/*     */     
/* 107 */     Lock lock = null;
/* 108 */     for (TaskRequest req : taskList) {
/* 109 */       lock = this.lockService.getCallApiLock(new String[] { req.getResultNo() });
/* 110 */       lock.lock();
/*     */       try {
/* 112 */         TaskInfoDto taskInfo = this.taskService.initTaskInfo(req);
/* 113 */         this.jobService.addResultJob(taskInfo.getResultNo(), taskInfo.getDataBatchNo());
/* 114 */         this.taskService.asyncExecuteRwaTaskFull(taskInfo);
/* 115 */       } catch (Exception e) {
/* 116 */         log.error("初始化任务异常， 任务调用参数异常", e);
/*     */       } finally {
/* 118 */         lock.unlock();
/*     */       } 
/*     */     } 
/* 121 */     return ServiceResult.success(id);
/*     */   }
/*     */   
/*     */   @RequestMapping({"/rwaListStatus"})
/*     */   @ResponseBody
/*     */   public ServiceResult rwaTaskListStatus(@Validated @RequestBody CommonRequest req) throws Exception {
/* 127 */     return ServiceResult.success(this.cacheService.getCache(CacheId.TASK_STATUS.getCode(), req.getValue()));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/single"})
/*     */   @ResponseBody
/*     */   public ServiceResult singleTask(@Validated @RequestBody IdRequest idRequest) throws Exception {
/* 134 */     Lock lock = this.lockService.getCallApiLock(LockType.ST, new String[] { idRequest.getId() });
/* 135 */     lock.lock();
/*     */     
/*     */     try {
/* 138 */       TaskConfigDo taskConfigDo = this.taskService.getTaskConfig(idRequest.getId());
/* 139 */       if (taskConfigDo == null || !DataUtils.isInList(taskConfigDo.getTaskType(), new String[] { TaskType.SINGLE.getCode(), TaskType.SINGLE2.getCode() })) {
/* 140 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "没有关联的单笔测算任务");
/*     */       }
/* 142 */       taskConfigDo.setDataBatchNo(taskConfigDo.getTaskId());
/*     */       
/* 144 */       if (StrUtil.isEmpty(taskConfigDo.getApproach()) || StrUtil.isEmpty(taskConfigDo.getIsCalcCva()) || 
/* 145 */         StrUtil.isEmpty(taskConfigDo.getSchemeId()) || 
/* 146 */         StrUtil.isEmpty(taskConfigDo.getDataBatchNo()) || taskConfigDo.getDataDate() == null)
/*     */       {
/* 148 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "单笔测算任务配置异常");
/*     */       }
/*     */       
/* 151 */       CalculateStatus calculateStatus = CalculateStatus.CALCULATE;
/* 152 */       this.taskService.updateTaskStatus(taskConfigDo, calculateStatus);
/*     */       
/* 154 */       SchemeConfigDo schemeConfigDo = this.paramService.initSchemeConfig(taskConfigDo.getSchemeId());
/*     */       
/* 156 */       TaskType taskType = (TaskType)EnumUtils.getEnumByCode(taskConfigDo.getTaskType(), TaskType.class);
/* 157 */       calculateStatus = this.taskService.executeGroupTask(taskType, taskConfigDo.getDataDate(), taskConfigDo.getDataBatchNo());
/* 158 */       if (calculateStatus != CalculateStatus.COMPLETE) {
/* 159 */         this.taskService.updateTaskStatus(taskConfigDo, calculateStatus);
/* 160 */         return ServiceResult.success("分组异常");
/*     */       } 
/*     */       
/* 163 */       calculateStatus = this.taskService.executeRwaTask(this.taskService.convert2TaskInfo(taskConfigDo));
/* 164 */       this.taskService.updateTaskStatus(taskConfigDo, calculateStatus);
/* 165 */       return ServiceResult.success(calculateStatus.getName());
/*     */     } finally {
/* 167 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/imt"})
/*     */   @ResponseBody
/*     */   public ServiceResult immediateTask(@Validated @RequestBody IdRequest idRequest) throws Exception {
/* 175 */     Lock lock = this.lockService.getCallApiLock(LockType.IMT, new String[] { idRequest.getId() });
/* 176 */     lock.lock();
/*     */     
/*     */     try {
/* 179 */       TaskConfigDo taskConfigDo = this.taskService.getTaskConfig(idRequest.getId());
/* 180 */       if (taskConfigDo == null || !DataUtils.isInList(taskConfigDo.getTaskType(), new String[] { TaskType.IMTASK.getCode(), TaskType.IMTASK2.getCode() })) {
/* 181 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "没有关联的即时任务");
/*     */       }
/*     */       
/* 184 */       if (StrUtil.isEmpty(taskConfigDo.getApproach()) || StrUtil.isEmpty(taskConfigDo.getIsCalcCva()) || 
/* 185 */         StrUtil.isEmpty(taskConfigDo.getSchemeId()) || 
/* 186 */         StrUtil.isEmpty(taskConfigDo.getDataBatchNo()) || taskConfigDo.getDataDate() == null)
/*     */       {
/* 188 */         return ServiceResult.error(StatusCodeEnum.JOB_PARAM_EXCEPTION, "即时任务配置异常");
/*     */       }
/* 190 */       taskConfigDo.setTaskRangeMap(this.taskService.getTaskRangeMap(taskConfigDo.getTaskId()));
/*     */       
/* 192 */       this.jobService.checkRwaRelatedJob(taskConfigDo.getTaskId(), taskConfigDo.getDataBatchNo());
/* 193 */       this.jobService.addResultJob(taskConfigDo.getTaskId(), taskConfigDo.getDataBatchNo());
/*     */       
/* 195 */       this.taskService.updateTaskStatus(taskConfigDo, CalculateStatus.CALCULATE);
/* 196 */       this.taskService.asyncExecuteImmediateTask(taskConfigDo);
/*     */     } finally {
/* 198 */       lock.unlock();
/*     */     } 
/* 200 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/config/refreshEngine"})
/*     */   @ResponseBody
/*     */   public ServiceResult refreshEngineConfig(@RequestBody CommonRequest commonRequest) throws Exception {
/* 206 */     this.configService.initEngineConfig();
/* 207 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/clear/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult clearGroup(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/* 213 */     this.jobService.removeGroupTask(riskDataRequest.getDataBatchNo());
/* 214 */     this.jobService.removeGroupJob(riskDataRequest.getDataBatchNo());
/* 215 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/clear/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult clearRwa(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 221 */     this.jobService.removeRwaJob(jobQueryRequest.getResultNo());
/* 222 */     ThreadUtil.safeSleep(1000L);
/* 223 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/clear/param"})
/*     */   @ResponseBody
/*     */   public ServiceResult clearParam(@Validated @RequestBody IdRequest idRequest) throws Exception {
/* 229 */     this.paramService.clearSchemeConfig(idRequest.getId());
/* 230 */     ThreadUtil.safeSleep(1000L);
/* 231 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/clear/all"})
/*     */   @ResponseBody
/*     */   public ServiceResult clearAll() throws Exception {
/* 237 */     this.paramService.clearAllConfig();
/* 238 */     this.jobService.clearAll();
/* 239 */     ThreadUtil.safeSleep(1000L);
/* 240 */     return ServiceResult.success();
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/stop/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult stopGroup(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/* 247 */     if (this.jobService.existsGroupTask(riskDataRequest.getDataBatchNo())) {
/* 248 */       this.jobService.addStopGroupTask(riskDataRequest.getDataBatchNo());
/*     */       
/* 250 */       return ServiceResult.success("1");
/*     */     } 
/*     */     
/* 253 */     return queryGroupStatus(riskDataRequest);
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/stop/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult stopRwa(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 260 */     if (this.jobService.existsResultJobByResultNo(jobQueryRequest.getResultNo())) {
/* 261 */       this.jobService.addStopRwaTask(jobQueryRequest.getResultNo());
/* 262 */       TaskLogDo taskLogDo = this.taskLogService.getRwaTaskLog(null, jobQueryRequest.getResultNo(), null);
/*     */       
/* 264 */       this.jobService.waitJobFinishAndGetStatLog(taskLogDo.getLogNo(), true);
/* 265 */       log.info("任务[{}]停止成功！", jobQueryRequest.getResultNo());
/* 266 */       return ServiceResult.success("1");
/*     */     } 
/*     */     
/* 269 */     return queryRwaStatus(jobQueryRequest);
/*     */   }
/*     */   
/*     */   @RequestMapping({"/query/status/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult queryGroupStatus(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/* 275 */     Lock lock = this.lockService.getCallApiLock(new String[] { riskDataRequest.getDataBatchNo() });
/* 276 */     lock.lock();
/*     */     
/*     */     try {
/* 279 */       if (this.jobService.existsGroupTask(riskDataRequest.getDataBatchNo())) {
/* 280 */         return ServiceResult.success(CalculateStatus.CALCULATE.getCode());
/*     */       }
/*     */       
/* 283 */       RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(riskDataRequest.getDataBatchNo());
/* 284 */       if (dataPeriodDo == null) {
/* 285 */         throw new JobParameterException("查询异常， 没有对应的数据期次，请确定计算参数是否准确[dataBatchNo=" + riskDataRequest.getDataBatchNo() + "]");
/*     */       }
/*     */       
/* 288 */       if (StrUtil.equals(Identity.YES.getCode(), dataPeriodDo.getGroupFlag())) {
/* 289 */         return ServiceResult.success(CalculateStatus.COMPLETE.getCode());
/*     */       }
/*     */       
/* 292 */       TaskLogDo taskLog = this.taskLogService.getTaskLog(dataPeriodDo.getDataDate(), dataPeriodDo.getDataBatchNo(), TaskType.GROUP);
/* 293 */       if (taskLog == null) {
/* 294 */         return ServiceResult.success("0");
/*     */       }
/* 296 */       return ServiceResult.success(taskLog.getTaskStatus());
/*     */     } finally {
/* 298 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   @RequestMapping({"/query/status/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult queryRwaStatus(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 305 */     Lock lock = this.lockService.getCallApiLock(new String[] { jobQueryRequest.getResultNo() });
/* 306 */     lock.lock();
/*     */     try {
/* 308 */       return ServiceResult.success(this.taskService.getRwaTaskStatus(jobQueryRequest.getResultNo(), null));
/*     */     } finally {
/* 310 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/query/info/group"})
/*     */   @ResponseBody
/*     */   public ServiceResult queryGroupInfo(@Validated @RequestBody RiskDataRequest riskDataRequest) throws Exception {
/* 318 */     Map<String, JobLogDo> jobMap = this.jobService.getGroupJobMap(riskDataRequest.getDataBatchNo());
/* 319 */     RiskDataPeriodDo dataPeriodDo = this.resultService.getRiskDataPeriod(riskDataRequest.getDataBatchNo());
/* 320 */     TaskLogDo taskLogDo = this.taskLogService.getTaskLog(dataPeriodDo.getDataDate(), dataPeriodDo.getDataBatchNo(), TaskType.GROUP);
/*     */     
/* 322 */     if (taskLogDo == null) {
/* 323 */       throw new JobParameterException("查询异常， 没有该数据期次分组任务，请确定计算参数是否准确[dataBatchNo=" + riskDataRequest.getDataBatchNo() + "]");
/*     */     }
/*     */     
/* 326 */     if (!this.jobService.existsGroupTask(riskDataRequest.getDataBatchNo()) && 
/* 327 */       StrUtil.equals(Identity.YES.getCode(), dataPeriodDo.getGroupFlag())) {
/* 328 */       return ServiceResult.success(taskLogDo);
/*     */     }
/*     */     
/* 331 */     return ServiceResult.success(this.taskService.calculateGroupTaskInfo(jobMap.values(), taskLogDo));
/*     */   }
/*     */ 
/*     */   
/*     */   @RequestMapping({"/query/info/rwa"})
/*     */   @ResponseBody
/*     */   public ServiceResult queryRwaInfo(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 338 */     Map<String, JobInfoDto> rwaJobMap = this.jobService.getRwaJobMap(jobQueryRequest.getResultNo());
/* 339 */     TaskLogDo taskLogDo = this.taskLogService.getRwaTaskLog(null, jobQueryRequest.getResultNo(), null);
/*     */     
/* 341 */     if (taskLogDo == null) {
/* 342 */       throw new JobParameterException("查询异常， 没有该期次RWA任务，请确定计算参数是否准确[resultNo=" + jobQueryRequest.getResultNo() + "]");
/*     */     }
/*     */     
/* 345 */     if (!this.jobService.existsResultJobByResultNo(jobQueryRequest.getResultNo())) {
/* 346 */       return ServiceResult.success(taskLogDo);
/*     */     }
/*     */     
/* 349 */     taskLogDo.setJobLogList(this.taskLogService.getJobLogList(taskLogDo.getLogNo()));
/*     */     
/* 351 */     return ServiceResult.success(this.taskService.calculateRwaTaskInfo(rwaJobMap.values(), taskLogDo));
/*     */   }
/*     */   
/*     */   @RequestMapping({"/monitor"})
/*     */   @ResponseBody
/*     */   public ServiceResult monitorTask(@Validated @RequestBody MonitorRequest monitorRequest) throws Exception {
/* 357 */     this.monitorService.monitor(monitorRequest.getTaskId(), monitorRequest.getResultNo());
/* 358 */     return ServiceResult.success();
/*     */   }
/*     */   
/*     */   @RequestMapping({"/gc"})
/*     */   @ResponseBody
/*     */   public ServiceResult gc(@Validated @RequestBody CommonRequest commonRequest) throws Exception {
/* 364 */     return ServiceResult.success(Long.valueOf(JobUtils.gc(false)));
/*     */   }
/*     */   
/*     */   @RequestMapping({"/stop/wait"})
/*     */   @ResponseBody
/*     */   public ServiceResult stopWaitTask(@Validated @RequestBody CommonRequest commonRequest) throws Exception {
/* 370 */     Long logNo = Convert.toLong(commonRequest.getValue());
/* 371 */     if (logNo == null) {
/* 372 */       throw new JobParameterException("非法停止任务ID[" + commonRequest.getValue() + "]");
/*     */     }
/* 374 */     return ServiceResult.success(JobUtils.addStopWaitTask(logNo.longValue()));
/*     */   }
/*     */   
/*     */   @RequestMapping({"/redis/keys"})
/*     */   @ResponseBody
/*     */   public ServiceResult redisKeys(@Validated @RequestBody CommonRequest req) throws Exception {
/* 380 */     return ServiceResult.success(this.cacheService.findKeys(req.getValue()));
/*     */   }
/*     */   
/*     */   @RequestMapping({"/redis/del"})
/*     */   @ResponseBody
/*     */   public ServiceResult redisDel(@Validated @RequestBody CommonRequest req) throws Exception {
/* 386 */     return ServiceResult.success(Long.valueOf(this.cacheService.del(new String[] { req.getValue() })));
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\rest\TaskRest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */