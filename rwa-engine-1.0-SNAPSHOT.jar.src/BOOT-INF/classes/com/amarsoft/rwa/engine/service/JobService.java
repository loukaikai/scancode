/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.thread.ThreadUtil;
/*     */ import cn.hutool.extra.spring.SpringUtil;
/*     */ import com.amarsoft.batch.exception.JobRunningException;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.Approach;
/*     */ import com.amarsoft.rwa.engine.constant.CacheId;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.CvaType;
/*     */ import com.amarsoft.rwa.engine.constant.JobType;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.constant.UnionType;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*     */ import com.amarsoft.rwa.engine.entity.ThreadGroupDto;
/*     */ import com.amarsoft.rwa.engine.exception.JobParameterException;
/*     */ import com.amarsoft.rwa.engine.job.GroupJob;
/*     */ import com.amarsoft.rwa.engine.job.JobUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.TreeMap;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ 
/*     */ @Service
/*     */ public class JobService {
/*  39 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.JobService.class);
/*     */   
/*     */   @Autowired
/*     */   private TaskLogService taskLogService;
/*     */   
/*     */   @Autowired
/*     */   private RwaJobBuilder rwaJobBuilder;
/*     */   @Autowired
/*     */   private ResultService resultService;
/*     */   @Autowired
/*     */   private ParamService paramService;
/*     */   @Autowired
/*     */   private CommonService commonService;
/*     */   @Autowired
/*     */   private CacheService cacheService;
/*     */   @Value("${rwa.group-analyze}")
/*     */   private boolean groupAnalyze;
/*     */   @Value("${rwa.writer-sub-table}")
/*     */   private boolean enableWriterSubTable;
/*     */   
/*     */   @Async("jobTaskExecutor")
/*     */   public void asyncExecuteGroupJob(TaskType taskType, JobType jobType, String dataBatchNo, Long logNo) {
/*  61 */     executeGroupJob(taskType, jobType, dataBatchNo, logNo);
/*     */   }
/*     */   
/*     */   public CalculateStatus executeGroupJob(TaskType taskType, JobType jobType, String dataBatchNo, Long logNo) {
/*  65 */     GroupJob groupJob = new GroupJob(taskType, jobType, dataBatchNo, logNo, this.groupAnalyze);
/*  66 */     groupJob.execute(this.taskLogService, this.commonService);
/*  67 */     return groupJob.getStatus();
/*     */   }
/*     */   
/*     */   @Async("jobTaskExecutor")
/*     */   public void asyncRunGroupJob(TaskType taskType, JobType jobType, Date dataDate, String dataBatchNo, TaskLogDo taskLogDo) {
/*  72 */     log.info("单独分组作业[{}-{}]-开始运行", jobType.getName(), dataBatchNo);
/*     */ 
/*     */     
/*     */     try {
/*  76 */       this.resultService.updateGroupFlag(dataBatchNo, Identity.NO.getCode());
/*     */       
/*  78 */       this.taskLogService.deleteGroupJobLog(jobType, dataBatchNo);
/*  79 */       this.taskLogService.insertGroupJobLog(jobType, dataDate, dataBatchNo, taskLogDo.getLogNo());
/*  80 */       this.resultService.initGroupResult(taskType, dataBatchNo, jobType);
/*     */       
/*  82 */       CalculateStatus calculateStatus = executeGroupJob(taskType, jobType, dataBatchNo, taskLogDo.getLogNo());
/*  83 */       if (calculateStatus == CalculateStatus.COMPLETE) {
/*  84 */         this.resultService.updateGroupFlag(dataBatchNo, Identity.YES.getCode());
/*     */       }
/*  86 */       log.info("单独分组作业[{}-{}]-{}完成", new Object[] { jobType.getName(), dataBatchNo, calculateStatus.getName() });
/*  87 */       this.taskLogService.endTaskLog(taskLogDo, calculateStatus, null);
/*  88 */     } catch (Exception e) {
/*  89 */       log.info("单独分组作业[" + jobType.getName() + "-" + dataBatchNo + "]-异常", e);
/*  90 */       this.taskLogService.exceptionTaskLog(taskLogDo, e);
/*     */     } finally {
/*  92 */       com.amarsoft.rwa.engine.service.JobService jobService = (com.amarsoft.rwa.engine.service.JobService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.JobService.class);
/*  93 */       jobService.removeGroupTask(dataBatchNo);
/*  94 */       jobService.removeGroupJob(dataBatchNo);
/*  95 */       jobService.removeStopGroupTask(dataBatchNo);
/*     */     } 
/*     */   }
/*     */   
/*     */   @Async("jobTaskExecutor")
/*     */   public void asyncExecuteRwaJob(JobInfoDto jobInfo) {
/* 101 */     log.info("作业计算[JOBS-{}]-开始", jobInfo.getJobId());
/* 102 */     CalculateStatus status = executeRwaJob(jobInfo);
/* 103 */     log.info("作业计算[JOBS-{}]-{}", jobInfo.getJobId(), status.getName());
/*     */   }
/*     */   
/*     */   public CalculateStatus executeRwaJob(JobInfoDto jobInfo) {
/* 107 */     RwaConfig.setHolidayConfigMap(this.paramService.getHolidayConfigMap());
/* 108 */     TaskConfigDo taskConfigDo = jobInfo.getTaskConfigDo();
/* 109 */     SchemeConfigDo schemeConfigDo = this.paramService.initSchemeConfig(jobInfo.getSchemeId());
/* 110 */     if (taskConfigDo.isEnEcCalc()) {
/* 111 */       RwaConfig.setEcColumnRuleMap(this.paramService.getEcColumnRuleConfig());
/* 112 */       RwaConfig.setEcFactorList(this.paramService.getEcFactorList());
/*     */     } 
/*     */     
/* 115 */     if (jobInfo.isSubTable())
/*     */     {
/* 117 */       this.resultService.createSubTable(jobInfo.getJobId(), jobInfo.getJobType());
/*     */     }
/*     */     
/* 120 */     JobExecution jobExecution = JobRunner.run(this.rwaJobBuilder.createJob(jobInfo), null);
/* 121 */     return (CalculateStatus)EnumUtils.getEnumByCode(jobInfo.getJobLog().getCalculateStatus(), CalculateStatus.class);
/*     */   }
/*     */   
/*     */   @Async("jobTaskExecutor")
/*     */   public void asyncRunRwaJob(JobType jobType, TaskInfoDto taskInfoDto) {
/* 126 */     com.amarsoft.rwa.engine.service.JobService jobService = (com.amarsoft.rwa.engine.service.JobService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.JobService.class);
/*     */     
/* 128 */     TaskType taskType = TaskType.RWA;
/* 129 */     Approach approach = (Approach)EnumUtils.getEnumByCode(taskInfoDto.getApproach(), Approach.class);
/*     */     
/* 131 */     this.resultService.deleteErrorData(taskInfoDto.getResultNo(), jobType);
/* 132 */     this.resultService.deleteRwaResult(taskType, taskInfoDto.getResultNo(), jobType);
/* 133 */     this.taskLogService.deleteRwaJobLog(taskInfoDto.getResultNo(), jobType);
/*     */     
/* 135 */     List<JobInfoDto> jobList = getJobInfoList(taskInfoDto, jobType, approach);
/*     */     
/* 137 */     jobList = initJobLog(taskInfoDto.getTaskLogDo(), jobList);
/*     */     
/* 139 */     if (CollUtil.isEmpty(jobList)) {
/*     */       return;
/*     */     }
/*     */     
/* 143 */     for (JobInfoDto jobInfo : jobList) {
/* 144 */       jobService.asyncExecuteRwaJob(jobInfo);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public CalculateStatus insertRwaResult(TaskInfoDto taskInfoDto) {
/* 152 */     CalculateStatus calculateStatus = CalculateStatus.COMPLETE;
/*     */     
/* 154 */     TaskLogDo taskLogDo = taskInfoDto.getTaskLogDo();
/* 155 */     JobLogDo jobLogDo = this.taskLogService.initResultJobLog(taskLogDo);
/*     */     try {
/* 157 */       this.resultService.insertRwaResult(taskInfoDto);
/* 158 */       this.taskLogService.closeJobLog(jobLogDo);
/* 159 */     } catch (Exception e) {
/* 160 */       calculateStatus = CalculateStatus.EXCEPTION;
/* 161 */       log.error("结果统计异常[" + taskInfoDto.getResultNo() + "]!", e);
/* 162 */       JobRunningException jobRunningException = new JobRunningException("结果统计异常[" + taskInfoDto.getResultNo() + "]!", e);
/* 163 */       this.taskLogService.exceptionJobLog(jobLogDo, (Throwable)jobRunningException);
/*     */     } 
/* 165 */     return calculateStatus;
/*     */   }
/*     */   
/*     */   @Async("jobTaskExecutor")
/*     */   public void asyncInsertRwaResult(TaskInfoDto taskInfo) {
/*     */     try {
/* 171 */       String fullName = "结果统计(" + taskInfo.getResultNo() + ")-" + taskInfo.getDataBatchNo();
/* 172 */       log.info("RWA计算[{}]: 开始运行", fullName);
/*     */       
/* 174 */       this.taskLogService.deleteResultJobLog(taskInfo.getResultNo());
/*     */       
/* 176 */       Map<String, List<JobLogDo>> statJobLog = waitJobFinishAndGetStatLog(taskInfo.getLogNo(), false);
/* 177 */       CalculateStatus calculateStatus = JobUtils.getCalculateStatus(statJobLog.keySet());
/* 178 */       if (calculateStatus == CalculateStatus.COMPLETE) {
/*     */         
/* 180 */         this.resultService.deleteStatRwaResult(taskInfo.getTaskType(), taskInfo.getResultNo());
/* 181 */         calculateStatus = insertRwaResult(taskInfo);
/*     */       } 
/* 183 */       this.taskLogService.endTaskLog(taskInfo.getTaskLogDo(), calculateStatus, null);
/* 184 */       log.info("RWA计算[{}]: {}运行", fullName, calculateStatus.getName());
/*     */     } finally {
/* 186 */       com.amarsoft.rwa.engine.service.JobService jobService = (com.amarsoft.rwa.engine.service.JobService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.JobService.class);
/*     */       
/* 188 */       jobService.removeResultJob(taskInfo.getResultNo());
/* 189 */       jobService.removeCva(taskInfo.getResultNo(), CvaType.DI);
/* 190 */       jobService.removeCva(taskInfo.getResultNo(), CvaType.SFT);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JobInfoDto> initJobLog(TaskLogDo taskLogDo, List<JobInfoDto> jobList) {
/* 196 */     for (JobInfoDto jobInfo : jobList) {
/* 197 */       if (JobUtils.enableThreadTaskInit) {
/* 198 */         this.taskLogService.asyncInitJobLog(jobInfo); continue;
/*     */       } 
/* 200 */       this.taskLogService.initJobLog(jobInfo);
/*     */     } 
/*     */ 
/*     */     
/* 204 */     waitJobInit(jobList);
/*     */     
/* 206 */     taskLogDo.setJobLogList(new ArrayList());
/* 207 */     List<JobInfoDto> list = new ArrayList<>();
/* 208 */     for (JobInfoDto jobInfo : jobList) {
/* 209 */       if (jobInfo.getJobLog().getCalculateNum().intValue() <= 0) {
/*     */         continue;
/*     */       }
/* 212 */       list.add(jobInfo);
/* 213 */       taskLogDo.getJobLogList().add(jobInfo.getJobLog());
/* 214 */       this.taskLogService.insertJobLog(jobInfo.getJobLog());
/*     */     } 
/* 216 */     return list;
/*     */   }
/*     */   
/*     */   public Collection<JobLogDo> waitJobInit(List<JobInfoDto> jobList) {
/* 220 */     Set<JobLogDo> jobLogDoSet = new HashSet<>();
/*     */ 
/*     */     
/* 223 */     long interval = 1000L;
/*     */     
/*     */     while (true) {
/* 226 */       for (JobInfoDto jobInfoDto : jobList) {
/* 227 */         if (jobInfoDto.getJobLog() == null) {
/*     */           break;
/*     */         }
/* 230 */         jobLogDoSet.add(jobInfoDto.getJobLog());
/*     */       } 
/*     */       
/* 233 */       if (jobLogDoSet.size() == jobList.size()) {
/* 234 */         return jobLogDoSet;
/*     */       }
/*     */       
/* 237 */       ThreadUtil.safeSleep(interval);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public String getThreadGroupSql(JobType jobType, UnionType unionType, String dataBatchNo) {
/* 243 */     String sql = "select g.sort_no, g.calculate_id, g.calculate_num from #{group_table} g where g.data_batch_no = #{dataBatchNo} and g.calculate_type = #{calculateType} order by g.sort_no";
/*     */     
/* 245 */     if (jobType == JobType.NR) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 250 */       sql = SqlBuilder.create(sql).setTable("group_table", "RWA_ER_NR_Group").setString("dataBatchNo", dataBatchNo).setString("calculateType", unionType.getCode()).build();
/* 251 */     } else if (jobType == JobType.RE) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 256 */       sql = SqlBuilder.create(sql).setTable("group_table", "RWA_ER_RE_Group").setString("dataBatchNo", dataBatchNo).setString("calculateType", unionType.getCode()).build();
/*     */     } else {
/* 258 */       throw new RuntimeException("配置异常，无法计算非零售及零售除外的分组！");
/*     */     } 
/* 260 */     return sql;
/*     */   }
/*     */   
/*     */   public String getCalculateIdByRow(String sql, int row) {
/* 264 */     sql = "select calculate_id from (" + sql + ") t where sort_no = " + row;
/* 265 */     return this.commonService.getString(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public Collection<ThreadGroupDto> getBigGroupThreads(JobType jobType, String dataBatchNo, int threadCount, int startThreadId) {
/* 271 */     TreeMap<Integer, ThreadGroupDto> map = new TreeMap<>();
/* 272 */     String sql = getThreadGroupSql(jobType, UnionType.GROUP, dataBatchNo);
/* 273 */     this.commonService.getJdbcTemplate().query(sql, (RowCallbackHandler)new Object(this, map, startThreadId, threadCount));
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 299 */     log.debug("{}分组数据线程： {}", jobType.getName(), JsonUtils.object2Json(map));
/* 300 */     return map.values();
/*     */   }
/*     */ 
/*     */   
/*     */   public Collection<ThreadGroupDto> getThreadGroupList(JobType jobType, UnionType unionType, String dataBatchNo, int threadCount, int startThreadId) {
/* 305 */     if (JobUtils.enableBigGroup && jobType == JobType.NR && unionType == UnionType.GROUP) {
/* 306 */       return getBigGroupThreads(jobType, dataBatchNo, JobUtils.bigGroupCount.intValue(), startThreadId);
/*     */     }
/* 308 */     String sql = getThreadGroupSql(jobType, unionType, dataBatchNo);
/* 309 */     List<ThreadGroupDto> list = new ArrayList<>();
/* 310 */     int total = this.commonService.getCount(sql);
/* 311 */     if (total == 0 || total <= threadCount) {
/* 312 */       list.add(new ThreadGroupDto(unionType, Integer.valueOf(startThreadId), null, null, 0));
/* 313 */       return list;
/*     */     } 
/* 315 */     int row = 1;
/* 316 */     int threadId = startThreadId;
/*     */     
/* 318 */     String beginId = getCalculateIdByRow(sql, row);
/* 319 */     String endId = null;
/*     */     while (true) {
/* 321 */       ThreadGroupDto groupDto = new ThreadGroupDto(unionType, Integer.valueOf(threadId), beginId, null, 0);
/* 322 */       list.add(groupDto);
/* 323 */       threadId++;
/*     */       
/* 325 */       row += threadCount;
/* 326 */       if (row > total) {
/*     */         break;
/*     */       }
/*     */       
/* 330 */       endId = getCalculateIdByRow(sql, row);
/* 331 */       groupDto.setEndId(endId);
/*     */       
/* 333 */       beginId = endId;
/*     */     } 
/*     */     
/* 336 */     return list;
/*     */   }
/*     */   
/*     */   public boolean isSubTableInsert(TaskType taskType) {
/* 340 */     if (this.enableWriterSubTable && !RwaUtils.isSingle(taskType)) {
/* 341 */       return true;
/*     */     }
/* 343 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JobInfoDto> getJobInfoList(TaskInfoDto taskInfo, JobType jobType, Approach approach, List<ThreadGroupDto> threadGroupList, List<TaskRangeDo> rangeList) {
/* 348 */     boolean isSubTable = isSubTableInsert(taskInfo.getTaskType());
/* 349 */     List<JobInfoDto> list = new ArrayList<>();
/* 350 */     for (ThreadGroupDto threadGroupDto : threadGroupList) {
/* 351 */       list.add(new JobInfoDto(taskInfo.getTaskType(), jobType, taskInfo.getResultNo(), taskInfo.getDataBatchNo(), taskInfo.getDataDate(), approach, taskInfo
/* 352 */             .getSchemeId(), taskInfo.getTaskId(), taskInfo.getTaskConfigDo(), taskInfo.getLogNo(), isSubTable, threadGroupDto, rangeList, null, null));
/*     */     }
/*     */     
/* 355 */     return list;
/*     */   }
/*     */   public List<JobInfoDto> getJobInfoList(TaskInfoDto taskInfo, JobType jobType, Approach approach) {
/*     */     List<ThreadGroupDto> threadGroupList;
/*     */     List<JobInfoDto> list;
/* 360 */     List<TaskRangeDo> rangeList = null;
/* 361 */     if (RwaUtils.isImt(taskInfo.getTaskType())) {
/*     */       
/* 363 */       if (!taskInfo.getTaskConfigDo().getCalculationRange().contains(jobType.getCode())) {
/* 364 */         return null;
/*     */       }
/*     */       
/* 367 */       rangeList = (List<TaskRangeDo>)taskInfo.getTaskConfigDo().getTaskRangeMap().get(jobType.getCode());
/*     */     } 
/* 369 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobType.ordinal()]) {
/*     */       
/*     */       case 1:
/*     */       case 2:
/* 373 */         if (RwaUtils.isSingle(taskInfo.getTaskType())) {
/* 374 */           List<JobInfoDto> list1 = new ArrayList<>();
/* 375 */           list1.add(new JobInfoDto(taskInfo.getTaskType(), jobType, taskInfo.getResultNo(), taskInfo.getDataBatchNo(), taskInfo.getDataDate(), approach, taskInfo
/* 376 */                 .getSchemeId(), taskInfo.getTaskId(), taskInfo.getTaskConfigDo(), taskInfo.getLogNo(), false, new ThreadGroupDto(UnionType.GROUP, 
/* 377 */                   Integer.valueOf(1), null, null, 0), rangeList, null, null));
/* 378 */           list1.add(new JobInfoDto(taskInfo.getTaskType(), jobType, taskInfo.getResultNo(), taskInfo.getDataBatchNo(), taskInfo.getDataDate(), approach, taskInfo
/* 379 */                 .getSchemeId(), taskInfo.getTaskId(), taskInfo.getTaskConfigDo(), taskInfo.getLogNo(), false, new ThreadGroupDto(UnionType.EXPOSURE, 
/* 380 */                   Integer.valueOf(2), null, null, 0), rangeList, null, null));
/* 381 */           return list1;
/*     */         } 
/*     */         
/* 384 */         threadGroupList = new ArrayList<>();
/* 385 */         threadGroupList.addAll(getThreadGroupList(jobType, UnionType.GROUP, taskInfo.getDataBatchNo(), JobUtils.singleThreadCount.intValue(), 1));
/* 386 */         threadGroupList.addAll(getThreadGroupList(jobType, UnionType.EXPOSURE, taskInfo.getDataBatchNo(), JobUtils.singleThreadCount.intValue(), threadGroupList.size() + 1));
/* 387 */         return getJobInfoList(taskInfo, jobType, approach, threadGroupList, rangeList);
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/*     */       case 6:
/*     */       case 7:
/* 393 */         list = new ArrayList<>();
/* 394 */         list.add(new JobInfoDto(taskInfo.getTaskType(), jobType, taskInfo.getResultNo(), taskInfo.getDataBatchNo(), taskInfo.getDataDate(), approach, taskInfo
/* 395 */               .getSchemeId(), taskInfo.getTaskId(), taskInfo.getTaskConfigDo(), taskInfo.getLogNo(), false, null, rangeList, null, null));
/*     */         
/* 397 */         return list;
/*     */     } 
/* 399 */     throw new RuntimeException();
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, List<JobLogDo>> waitJobFinishAndGetStatLog(Long logNo, boolean isStop) {
/* 404 */     if (isStop) ThreadUtil.safeSleep(1000L); 
/* 405 */     List<JobLogDo> jobLogDoList = null;
/* 406 */     Map<String, List<JobLogDo>> statJobLog = null;
/*     */     
/* 408 */     long interval = Math.max(10000L, JobUtils.taskPollingInterval);
/*     */     
/*     */     while (true) {
/* 411 */       if (JobUtils.isStopWaitTask(logNo.longValue())) {
/* 412 */         JobUtils.removeStopWaitTask(logNo.longValue());
/* 413 */         throw new JobStopException("等待任务执行完成被打断");
/*     */       } 
/* 415 */       jobLogDoList = this.taskLogService.getJobLogList(logNo);
/* 416 */       statJobLog = JobUtils.statJobLog(jobLogDoList);
/* 417 */       if (isStop && JobUtils.isInit(statJobLog.keySet())) {
/*     */         break;
/*     */       }
/* 420 */       if (JobUtils.isEnd(statJobLog.keySet())) {
/*     */         break;
/*     */       }
/* 423 */       ThreadUtil.safeSleep(interval);
/*     */     } 
/* 425 */     return statJobLog;
/*     */   }
/*     */   
/*     */   public List<Integer> nextWorkerId() {
/* 429 */     Integer wid = (Integer)this.cacheService.getCache(CacheId.WORKER_ID.getCode(), "wid");
/* 430 */     Integer cid = (Integer)this.cacheService.getCache(CacheId.WORKER_ID.getCode(), "cid");
/*     */     
/* 432 */     if (wid == null) {
/* 433 */       wid = Integer.valueOf(0);
/*     */     }
/* 435 */     if (cid == null) {
/* 436 */       cid = Integer.valueOf(0);
/*     */     }
/*     */     
/* 439 */     if (cid.intValue() >= 31) {
/* 440 */       Integer integer1 = wid, integer2 = wid = Integer.valueOf(wid.intValue() + 1);
/* 441 */       cid = Integer.valueOf(0);
/*     */     } else {
/* 443 */       Integer integer1 = cid, integer2 = cid = Integer.valueOf(cid.intValue() + 1);
/*     */     } 
/* 445 */     if (wid.intValue() >= 32) {
/* 446 */       wid = Integer.valueOf(0);
/*     */     }
/* 448 */     List<Integer> next = new ArrayList<>();
/* 449 */     next.add(wid);
/* 450 */     next.add(cid);
/* 451 */     this.cacheService.putCache(CacheId.WORKER_ID.getCode(), "wid", wid);
/* 452 */     this.cacheService.putCache(CacheId.WORKER_ID.getCode(), "cid", cid);
/* 453 */     return next;
/*     */   }
/*     */   
/*     */   public synchronized void addGroupTask(String dataBatchNo) {
/* 457 */     JobUtils.addGroupTask(dataBatchNo);
/* 458 */     this.cacheService.putCache(CacheId.GROUP_TASK.getCode(), dataBatchNo, dataBatchNo);
/* 459 */     log.info("新增缓存-分组任务[{}]", dataBatchNo);
/*     */   }
/*     */   
/*     */   public void removeGroupTask(String dataBatchNo) {
/* 463 */     JobUtils.removeGroupTask(dataBatchNo);
/* 464 */     this.cacheService.deleteCache(CacheId.GROUP_TASK.getCode(), dataBatchNo);
/* 465 */     log.info("清理缓存-分组任务[{}]", dataBatchNo);
/*     */   }
/*     */   
/*     */   public boolean existsGroupTask(String dataBatchNo) {
/* 469 */     return this.cacheService.hasCacheKey(CacheId.GROUP_TASK.getCode(), dataBatchNo);
/*     */   }
/*     */   
/*     */   public synchronized void addStopGroupTask(String dataBatchNo) {
/* 473 */     JobUtils.addStopGroupTask(dataBatchNo);
/* 474 */     this.cacheService.putCache(CacheId.STOP_GROUP.getCode(), dataBatchNo, dataBatchNo);
/* 475 */     log.info("新增缓存-停止分组任务[{}]", dataBatchNo);
/*     */   }
/*     */   
/*     */   public void removeStopGroupTask(String dataBatchNo) {
/* 479 */     JobUtils.removeStopGroupTask(dataBatchNo);
/* 480 */     this.cacheService.deleteCache(CacheId.STOP_GROUP.getCode(), dataBatchNo);
/* 481 */     log.info("清理缓存-停止分组任务[{}]", dataBatchNo);
/*     */   }
/*     */   
/*     */   public synchronized void syncStopGroupTask() {
/* 485 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.STOP_GROUP.getCode());
/* 486 */     if (CollUtil.isEmpty(keys)) {
/*     */       return;
/*     */     }
/* 489 */     for (String key : keys) {
/* 490 */       String task = this.cacheService.getMainKey(key);
/* 491 */       if (JobUtils.getStopGroupTaskSet().add(task)) {
/* 492 */         log.info("同步缓存-停止分组任务[{}]", task);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public Map<String, JobLogDo> getGroupJobMap(String dataBatchNo) {
/* 498 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.GROUP_JOB.getCode(), dataBatchNo);
/* 499 */     if (CollUtil.isEmpty(keys)) {
/* 500 */       return null;
/*     */     }
/* 502 */     Map<String, JobLogDo> map = new HashMap<>();
/* 503 */     for (String key : keys) {
/* 504 */       JobLogDo job = (JobLogDo)this.cacheService.getValue(key);
/* 505 */       map.put(job.getJobId(), job);
/*     */     } 
/* 507 */     return map;
/*     */   }
/*     */   
/*     */   public synchronized void addGroupJob(JobLogDo jobLogDo) {
/* 511 */     JobUtils.putGroupJob(jobLogDo);
/* 512 */     this.cacheService.putCache(CacheId.GROUP_JOB.getCode(), jobLogDo.getDataBatchNo(), jobLogDo.getJobId(), jobLogDo);
/* 513 */     log.info("新增缓存-分组作业[{}-{}-{}]", new Object[] { jobLogDo.getJobId(), jobLogDo.getDataBatchNo(), jobLogDo.getJobType() });
/*     */   }
/*     */   
/*     */   public void putGroupJob(JobLogDo jobLogDo) {
/* 517 */     this.cacheService.putCache(CacheId.GROUP_JOB.getCode(), jobLogDo.getDataBatchNo(), jobLogDo.getJobId(), jobLogDo);
/* 518 */     log.info("更新缓存-分组作业[{}-{}-{}]", new Object[] { jobLogDo.getJobId(), jobLogDo.getDataBatchNo(), jobLogDo.getJobType() });
/*     */   }
/*     */   
/*     */   public void removeGroupJob(JobLogDo jobLogDo) {
/* 522 */     JobUtils.removeGroupJob(jobLogDo);
/* 523 */     this.cacheService.deleteCache(CacheId.GROUP_JOB.getCode(), jobLogDo.getDataBatchNo(), jobLogDo.getJobId());
/* 524 */     log.info("清理缓存-分组作业[{}-{}-{}]", new Object[] { jobLogDo.getJobId(), jobLogDo.getDataBatchNo(), jobLogDo.getJobType() });
/*     */   }
/*     */   
/*     */   public void removeGroupJob(String dataBatchNo) {
/* 528 */     JobUtils.removeGroupJob(dataBatchNo);
/* 529 */     this.cacheService.clearCache(CacheId.GROUP_JOB.getCode(), dataBatchNo);
/* 530 */     log.info("清理缓存-全部分组作业[{}]", dataBatchNo);
/*     */   }
/*     */   
/*     */   public Map<String, JobInfoDto> getRwaJobMap(String resultNo) {
/* 534 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.RWA_JOB.getCode(), resultNo);
/* 535 */     if (CollUtil.isEmpty(keys)) {
/* 536 */       return null;
/*     */     }
/* 538 */     Map<String, JobInfoDto> map = new HashMap<>();
/* 539 */     for (String key : keys) {
/* 540 */       JobInfoDto job = (JobInfoDto)this.cacheService.getValue(key);
/* 541 */       map.put(job.getJobId(), job);
/*     */     } 
/* 543 */     return map;
/*     */   }
/*     */   
/*     */   public boolean existsRwaJob(String resultNo, String jobId) {
/* 547 */     return this.cacheService.hasCacheKey(CacheId.RWA_JOB.getCode(), resultNo, jobId);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized void addRwaJob(JobInfoDto jobInfo) {
/* 561 */     JobUtils.putRwaJob(jobInfo);
/* 562 */     this.cacheService.putCache(CacheId.RWA_JOB.getCode(), jobInfo.getResultNo(), jobInfo.getJobId(), jobInfo);
/* 563 */     log.info("新增缓存-RWA作业[{}-{}-{}-{}]", new Object[] { jobInfo.getResultNo(), jobInfo.getDataBatchNo(), jobInfo.getJobType(), jobInfo.getJobId() });
/*     */   }
/*     */   
/*     */   public void putRwaJob(JobInfoDto jobInfo) {
/* 567 */     this.cacheService.putCache(CacheId.RWA_JOB.getCode(), jobInfo.getResultNo(), jobInfo.getJobId(), jobInfo);
/* 568 */     JobLogDo jobLogDo = jobInfo.getJobLog();
/* 569 */     log.info("更新缓存-RWA作业[{}-{}-{}-{}]: ---> 已计算[{}/{}]", new Object[] { jobLogDo
/* 570 */           .getResultNo(), jobLogDo.getDataBatchNo(), jobLogDo.getJobType(), jobLogDo.getJobId(), jobLogDo
/* 571 */           .getCtCount(), jobLogDo.getCalculateNum() });
/*     */   }
/*     */   
/*     */   public void removeRwaJob(JobInfoDto jobInfo) {
/* 575 */     JobUtils.removeRwaJob(jobInfo);
/* 576 */     this.cacheService.deleteCache(CacheId.RWA_JOB.getCode(), jobInfo.getResultNo(), jobInfo.getJobId());
/* 577 */     log.info("清理缓存-RWA作业[{}-{}-{}-{}]", new Object[] { jobInfo.getResultNo(), jobInfo.getDataBatchNo(), jobInfo.getJobType(), jobInfo.getJobId() });
/*     */   }
/*     */   
/*     */   public void removeRwaJob(String resultNo) {
/* 581 */     JobUtils.removeRwaJob(resultNo);
/* 582 */     this.cacheService.clearCache(CacheId.RWA_JOB.getCode(), resultNo);
/* 583 */     log.info("清理缓存-全部RWA作业[{}]", resultNo);
/*     */   }
/*     */   
/*     */   public synchronized void addStopRwaTask(String resultNo) {
/* 587 */     JobUtils.addStopRwaTask(resultNo);
/* 588 */     this.cacheService.putCache(CacheId.STOP_RWA.getCode(), resultNo, resultNo);
/* 589 */     log.info("新增缓存-停止RWA任务[{}]", resultNo);
/*     */   }
/*     */   
/*     */   public void removeStopRwaTask(String resultNo) {
/* 593 */     JobUtils.removeStopRwaTask(resultNo);
/* 594 */     this.cacheService.deleteCache(CacheId.STOP_RWA.getCode(), resultNo);
/* 595 */     log.info("清理缓存-停止RWA任务[{}]", resultNo);
/*     */   }
/*     */   
/*     */   public synchronized void syncStopRwaTask() {
/* 599 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.STOP_RWA.getCode());
/* 600 */     if (CollUtil.isEmpty(keys)) {
/*     */       return;
/*     */     }
/* 603 */     for (String key : keys) {
/* 604 */       String task = this.cacheService.getMainKey(key);
/* 605 */       if (JobUtils.getStopRwaTaskSet().add(task)) {
/* 606 */         log.info("同步缓存-停止RWA任务[{}]", task);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public synchronized void addResultJob(String resultNo, String dataBatchNo) {
/* 612 */     this.cacheService.putCache(CacheId.RESULT_JOB.getCode(), resultNo, dataBatchNo);
/* 613 */     log.info("新增缓存-结果作业[{}-{}]", resultNo, dataBatchNo);
/*     */   }
/*     */   
/*     */   public void removeResultJob(String resultNo) {
/* 617 */     this.cacheService.deleteCache(CacheId.RESULT_JOB.getCode(), resultNo);
/* 618 */     log.info("清理缓存-结果作业[{}]", resultNo);
/*     */   }
/*     */   
/*     */   public void putCva(String resultNo, CvaType cvaType, BigDecimal cva) {
/* 622 */     this.cacheService.putCache(CacheId.CVA.getCode(), resultNo, cvaType.getCode(), cva);
/* 623 */     log.info("新增缓存-CVA结果[{}-{}]: {}", new Object[] { resultNo, cvaType, cva });
/*     */   }
/*     */   
/*     */   public void removeCva(String resultNo, CvaType cvaType) {
/* 627 */     this.cacheService.deleteCache(CacheId.CVA.getCode(), resultNo, cvaType.getCode());
/* 628 */     log.info("清理缓存-CVA结果[{}-{}]", resultNo, cvaType);
/*     */   }
/*     */   
/*     */   public void clearCva(String resultNo) {
/* 632 */     this.cacheService.clearCache(CacheId.CVA.getCode(), resultNo);
/* 633 */     log.info("清理缓存-CVA结果[{}]", resultNo);
/*     */   }
/*     */   
/*     */   public BigDecimal getCva(String resultNo, CvaType cvaType) {
/* 637 */     return (BigDecimal)this.cacheService.getCache(CacheId.CVA.getCode(), resultNo, cvaType.getCode());
/*     */   }
/*     */   
/*     */   public boolean existsResultJobByResultNo(String resultNo) {
/* 641 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.RESULT_JOB.getCode(), resultNo);
/* 642 */     return !CollUtil.isEmpty(keys);
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean existsResultJobByDataBatchNo(String dataBatchNo) {
/* 647 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.RESULT_JOB.getCode(), dataBatchNo);
/* 648 */     return !CollUtil.isEmpty(keys);
/*     */   }
/*     */   
/*     */   public void checkGroupJob(String dataBatchNo) {
/* 652 */     if (existsGroupTask(dataBatchNo)) {
/* 653 */       throw new JobParameterException("存在同期次的分组任务，请确认调用期次是否准确[dataBatchNo=" + dataBatchNo + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkGroupRelatedJob(String dataBatchNo) {
/* 659 */     checkGroupJob(dataBatchNo);
/*     */     
/* 661 */     if (existsResultJobByDataBatchNo(dataBatchNo)) {
/* 662 */       throw new JobParameterException("存在同期次的结果统计作业，请确认调用期次是否准确[dataBatchNo=" + dataBatchNo + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkRwaRelatedJob(String resultNo, String dataBatchNo) {
/* 668 */     checkGroupJob(dataBatchNo);
/*     */     
/* 670 */     if (existsResultJobByResultNo(resultNo)) {
/* 671 */       throw new JobParameterException("存在同期次的结果统计作业，请确认调用期次是否准确[resultNo=" + resultNo + "]");
/*     */     }
/* 673 */     Map<String, JobInfoDto> map = getRwaJobMap(resultNo);
/* 674 */     if (!CollUtil.isEmpty(map)) {
/* 675 */       throw new JobParameterException("存在同期次的RWA作业，请确认调用期次是否准确[resultNo=" + resultNo + "]");
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void checkRwaRelatedJob(String resultNo, String dataBatchNo, JobType jobType) {
/* 681 */     checkGroupJob(dataBatchNo);
/*     */     
/* 683 */     if (existsResultJobByResultNo(resultNo)) {
/* 684 */       throw new JobParameterException("存在同期次的结果统计作业，请确认调用期次是否准确[resultNo=" + resultNo + "]");
/*     */     }
/* 686 */     Map<String, JobInfoDto> map = ((com.amarsoft.rwa.engine.service.JobService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.JobService.class)).getRwaJobMap(resultNo);
/* 687 */     for (JobInfoDto jobInfoDto : map.values()) {
/* 688 */       if (jobInfoDto.getJobType() == jobType) {
/* 689 */         throw new JobParameterException("存在同期次的RWA作业，请确认调用期次是否准确[resultNo=" + resultNo + "][" + jobType.getName() + "]");
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public synchronized void clearAll() {
/* 696 */     this.cacheService.clearCache(CacheId.GROUP_TASK.getCode());
/* 697 */     this.cacheService.clearCache(CacheId.GROUP_JOB.getCode());
/* 698 */     this.cacheService.clearCache(CacheId.STOP_GROUP.getCode());
/* 699 */     this.cacheService.clearCache(CacheId.STOP_RWA.getCode());
/* 700 */     this.cacheService.clearCache(CacheId.RWA_JOB.getCode());
/* 701 */     this.cacheService.clearCache(CacheId.RESULT_JOB.getCode());
/* 702 */     this.cacheService.clearCache(CacheId.CVA.getCode());
/* 703 */     this.cacheService.clearCache(CacheId.RUNNING_MSG.getCode());
/* 704 */     log.info("清理全部作业相关缓存完成");
/*     */   }
/*     */   
/*     */   public synchronized void addRunningMsg(String jobId, String value) {
/* 708 */     this.cacheService.putCache(CacheId.RUNNING_MSG.getCode(), jobId, value);
/* 709 */     log.info("新增缓存-消费作业[{}]", jobId);
/*     */   }
/*     */   
/*     */   public void removeRunningMsg(String jobId) {
/* 713 */     this.cacheService.deleteCache(CacheId.RUNNING_MSG.getCode(), jobId);
/* 714 */     log.info("清理缓存-消费作业[{}]", jobId);
/*     */   }
/*     */   
/*     */   public boolean existsRunningMsg(String jobId) {
/* 718 */     Set<String> keys = this.cacheService.getCacheKeys(CacheId.RUNNING_MSG.getCode(), jobId);
/* 719 */     return !CollUtil.isEmpty(keys);
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\JobService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */