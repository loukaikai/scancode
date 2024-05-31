/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.JobType;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.entity.ExcDataDo;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*     */ import com.amarsoft.rwa.engine.entity.ThreadGroupDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.IdWorker;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import com.baomidou.mybatisplus.core.conditions.Wrapper;
/*     */ import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/*     */ import java.lang.invoke.SerializedLambda;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ 
/*     */ @Service
/*     */ public class TaskLogService {
/*  32 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.TaskLogService.class);
/*     */   
/*     */   @Autowired
/*     */   private TaskLogMapper taskLogMapper;
/*     */   
/*     */   @Autowired
/*     */   private JobLogMapper jobLogMapper;
/*     */   @Autowired
/*     */   private CommonService commonService;
/*     */   
/*     */   public void endTaskLog(TaskLogDo logDo, CalculateStatus calculateStatus, String exceptionInfo) {
/*  43 */     logDo.setExceptionInfo(null);
/*  44 */     logDo.setEndTime(DataUtils.nowTimestamp());
/*  45 */     logDo.setCalcTime(DataUtils.timeBetween(logDo.getStartTime(), logDo.getEndTime()));
/*  46 */     logDo.setTaskStatus(calculateStatus.getCode());
/*  47 */     logDo.setExceptionInfo(exceptionInfo);
/*  48 */     updateTaskLog(logDo);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void exceptionTaskLog(TaskLogDo logDo, Throwable throwable) {
/*  57 */     endTaskLog(logDo, CalculateStatus.EXCEPTION, DataUtils.stackTraceToString(throwable));
/*     */   }
/*     */   
/*     */   public List<TaskLogDo> getTaskLogList(Date dataDate, String dataBatchNo, TaskType taskType) {
/*  61 */     return this.taskLogMapper.selectList((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/*  62 */         .eq(TaskLogDo::getDataDate, dataDate))
/*  63 */         .eq(TaskLogDo::getDataBatchNo, dataBatchNo))
/*  64 */         .eq(TaskLogDo::getTaskType, taskType.getCode()));
/*     */   }
/*     */ 
/*     */   
/*     */   public TaskLogDo getTaskLog(Date dataDate, String dataBatchNo, TaskType taskType) {
/*  69 */     List<TaskLogDo> taskLogList = getTaskLogList(dataDate, dataBatchNo, taskType);
/*  70 */     if (CollUtil.isEmpty(taskLogList)) {
/*  71 */       return null;
/*     */     }
/*  73 */     return taskLogList.get(0);
/*     */   }
/*     */   
/*     */   public TaskLogDo getTaskLog(long logNo) {
/*  77 */     return (TaskLogDo)this.taskLogMapper.selectById(Long.valueOf(logNo));
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteGroupTaskLog(Date dataDate, String dataBatchNo) {
/*  82 */     deleteTaskLog(getTaskLogList(dataDate, dataBatchNo, TaskType.GROUP));
/*     */   }
/*     */ 
/*     */   
/*     */   public TaskLogDo initGroupTaskLog(Date dataDate, String dataBatchNo) {
/*  87 */     deleteGroupTaskLog(dataDate, dataBatchNo);
/*     */     
/*  89 */     return insertGroupTaskLog(dataDate, dataBatchNo);
/*     */   }
/*     */   
/*     */   public void deleteTaskLog(List<TaskLogDo> taskLogDoList) {
/*  93 */     if (CollUtil.isEmpty(taskLogDoList)) {
/*     */       return;
/*     */     }
/*  96 */     List<Long> ids = new ArrayList<>();
/*  97 */     for (TaskLogDo taskLogDo : taskLogDoList) {
/*  98 */       ids.add(taskLogDo.getLogNo());
/*     */       
/* 100 */       this.jobLogMapper.delete((Wrapper)(new LambdaQueryWrapper()).eq(JobLogDo::getLogNo, taskLogDo.getLogNo()));
/*     */     } 
/*     */     
/* 103 */     this.taskLogMapper.deleteBatchIds(ids);
/*     */   }
/*     */   
/*     */   public TaskLogDo insertGroupTaskLog(Date dataDate, String dataBatchNo) {
/* 107 */     TaskLogDo taskLogDo = new TaskLogDo();
/* 108 */     taskLogDo.setLogNo(IdWorker.getId());
/* 109 */     taskLogDo.setDataDate(dataDate);
/* 110 */     taskLogDo.setDataBatchNo(dataBatchNo);
/* 111 */     taskLogDo.setTaskType(TaskType.GROUP.getCode());
/* 112 */     taskLogDo.setStartTime(DataUtils.nowTimestamp());
/* 113 */     taskLogDo.setTaskStatus(CalculateStatus.CALCULATE.getCode());
/* 114 */     this.taskLogMapper.insert(taskLogDo);
/* 115 */     return taskLogDo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void initGroupJobLog(Date dataDate, String dataBatchNo, Long logNo) {
/* 120 */     insertGroupJobLog(JobType.NR_GROUP, dataDate, dataBatchNo, logNo);
/* 121 */     insertGroupJobLog(JobType.RE_GROUP, dataDate, dataBatchNo, logNo);
/*     */   }
/*     */   
/*     */   public int deleteGroupJobLog(JobType jobType, String dataBatchNo) {
/* 125 */     return this.jobLogMapper.delete((Wrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/* 126 */         .eq(JobLogDo::getJobType, jobType.getCode())).eq(JobLogDo::getDataBatchNo, dataBatchNo));
/*     */   }
/*     */ 
/*     */   
/*     */   public JobLogDo insertGroupJobLog(JobType jobType, Date dataDate, String dataBatchNo, Long logNo) {
/* 131 */     JobLogDo jobLogDo = new JobLogDo();
/* 132 */     jobLogDo.setJobId(IdWorker.getIdStr());
/* 133 */     jobLogDo.setJobType(jobType.getCode());
/* 134 */     jobLogDo.setLogNo(logNo);
/* 135 */     jobLogDo.setTaskType(TaskType.GROUP.getCode());
/* 136 */     jobLogDo.setDataDate(dataDate);
/* 137 */     jobLogDo.setDataBatchNo(dataBatchNo);
/* 138 */     jobLogDo.setCalculateStatus(CalculateStatus.CREATED.getCode());
/*     */     
/* 140 */     jobLogDo.setCalculateNum(Integer.valueOf(7));
/* 141 */     insertJobLog(jobLogDo);
/* 142 */     return jobLogDo;
/*     */   }
/*     */ 
/*     */   
/*     */   public void setServerInfo(JobLogDo jobLogDo) {
/* 147 */     Map<String, String> serverInfo = DataUtils.getServerInfo();
/* 148 */     if (!CollUtil.isEmpty(serverInfo)) {
/* 149 */       jobLogDo.setServerIp(serverInfo.get("ip"));
/* 150 */       jobLogDo.setServerName(serverInfo.get("name"));
/*     */     } 
/*     */   }
/*     */   
/*     */   public List<JobLogDo> getJobLogList(@NotNull Long logNo) {
/* 155 */     return getJobLogList(logNo, null);
/*     */   }
/*     */   
/*     */   public List<JobLogDo> getJobLogList(@NotNull Long logNo, JobType jobType) {
/* 159 */     LambdaQueryWrapper<JobLogDo> queryWrapper = (LambdaQueryWrapper<JobLogDo>)(new LambdaQueryWrapper()).eq(JobLogDo::getLogNo, logNo);
/* 160 */     if (jobType != null) {
/* 161 */       queryWrapper.eq(JobLogDo::getJobType, jobType.getCode());
/*     */     }
/* 163 */     queryWrapper.orderByAsc(JobLogDo::getJobId);
/* 164 */     return this.jobLogMapper.selectList((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public List<JobLogDo> getJobLogList(List<Long> ids) {
/* 168 */     return this.jobLogMapper.selectBatchIds(ids);
/*     */   }
/*     */   
/*     */   public void updateTaskLog(TaskLogDo taskLogDo) {
/* 172 */     this.taskLogMapper.updateById(taskLogDo);
/*     */   }
/*     */   
/*     */   public void insertJobLog(JobLogDo jobLogDo) {
/* 176 */     this.jobLogMapper.insert(jobLogDo);
/*     */   }
/*     */   
/*     */   public void endJobLog(JobLogDo logDo, Timestamp endTime, CalculateStatus calculateStatus, String exceptionInfo) {
/* 180 */     if (endTime == null) {
/* 181 */       endTime = DataUtils.nowTimestamp();
/*     */     }
/* 183 */     if (calculateStatus == null) {
/* 184 */       calculateStatus = CalculateStatus.COMPLETE;
/*     */     }
/*     */     
/* 187 */     logDo.setNormalCalcCnt(Integer.valueOf(logDo.getCtCalculateCount().get()));
/* 188 */     logDo.setNormalSkipCnt(Integer.valueOf(logDo.getCtSkipCount().get()));
/* 189 */     logDo.setExceptionSkipCnt(Integer.valueOf(logDo.getCtExceptionCount().get()));
/*     */     
/* 191 */     logDo.setEndTime(endTime);
/* 192 */     if (logDo.getStartTime() == null)
/*     */     {
/* 194 */       logDo.setStartTime(endTime);
/*     */     }
/* 196 */     logDo.setCalcTime(DataUtils.timeBetween(logDo.getStartTime(), logDo.getEndTime()));
/* 197 */     logDo.setCalculateStatus(calculateStatus.getCode());
/* 198 */     logDo.setExceptionInfo(exceptionInfo);
/* 199 */     this.jobLogMapper.updateById(logDo);
/*     */   }
/*     */   
/*     */   public void closeJobLog(JobLogDo logDo) {
/* 203 */     endJobLog(logDo, DataUtils.nowTimestamp(), CalculateStatus.COMPLETE, null);
/*     */   }
/*     */   
/*     */   public void closeJobLog(JobLogDo logDo, Timestamp endTime) {
/* 207 */     endJobLog(logDo, endTime, CalculateStatus.COMPLETE, null);
/*     */   }
/*     */   
/*     */   public void closeJobLog(JobLogDo logDo, Timestamp endTime, String info) {
/* 211 */     endJobLog(logDo, endTime, CalculateStatus.COMPLETE, info);
/*     */   }
/*     */   
/*     */   public void stopJobLog(JobLogDo logDo) {
/* 215 */     endJobLog(logDo, DataUtils.nowTimestamp(), CalculateStatus.STOP, null);
/*     */   }
/*     */   
/*     */   public void stopJobLog(JobLogDo logDo, String info) {
/* 219 */     endJobLog(logDo, DataUtils.nowTimestamp(), CalculateStatus.STOP, info);
/*     */   }
/*     */   
/*     */   public void stopJobLog(JobLogDo logDo, Timestamp endTime) {
/* 223 */     endJobLog(logDo, endTime, CalculateStatus.STOP, null);
/*     */   }
/*     */   
/*     */   public void exceptionJobLog(JobLogDo logDo, Throwable throwable) {
/* 227 */     endJobLog(logDo, DataUtils.nowTimestamp(), CalculateStatus.EXCEPTION, DataUtils.stackTraceToString(throwable));
/*     */   }
/*     */   
/*     */   public void exceptionJobLog(JobLogDo logDo, String info) {
/* 231 */     endJobLog(logDo, DataUtils.nowTimestamp(), CalculateStatus.EXCEPTION, info);
/*     */   }
/*     */   
/*     */   public void exceptionJobLog(JobLogDo logDo, Timestamp endTime, Throwable throwable) {
/* 235 */     endJobLog(logDo, endTime, CalculateStatus.EXCEPTION, DataUtils.stackTraceToString(throwable));
/*     */   }
/*     */   
/*     */   public void exceptionJobLog(String jobId, String info) {
/* 239 */     exceptionJobLog(getJobLog(jobId), info);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public List<JobLogDo> getJobList(String resultNo, CalculateStatus calculateStatus) {
/* 245 */     LambdaQueryWrapper<JobLogDo> queryWrapper = (LambdaQueryWrapper<JobLogDo>)((LambdaQueryWrapper)(new LambdaQueryWrapper()).eq(JobLogDo::getResultNo, resultNo)).eq(JobLogDo::getCalculateStatus, calculateStatus.getCode());
/* 246 */     return this.jobLogMapper.selectList((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public String getRwaTaskStatus(String resultNo, Timestamp startTime) {
/* 250 */     TaskLogDo taskLogDo = getRwaTaskLog(null, resultNo, startTime);
/* 251 */     if (taskLogDo == null) {
/* 252 */       return "0";
/*     */     }
/* 254 */     return taskLogDo.getTaskStatus();
/*     */   }
/*     */ 
/*     */   
/*     */   public TaskLogDo getRwaTaskLog(TaskType taskType, String resultNo, Timestamp startTime) {
/* 259 */     LambdaQueryWrapper<TaskLogDo> queryWrapper = (LambdaQueryWrapper<TaskLogDo>)(new LambdaQueryWrapper()).eq(TaskLogDo::getResultNo, resultNo);
/* 260 */     if (taskType != null) {
/* 261 */       queryWrapper.eq(TaskLogDo::getTaskType, taskType.getCode());
/*     */     }
/* 263 */     if (startTime != null) {
/* 264 */       queryWrapper.ge(TaskLogDo::getStartTime, startTime);
/*     */     }
/* 266 */     return (TaskLogDo)this.taskLogMapper.selectOne((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public TaskLogDo insertRwaTaskLog(TaskType taskType, String taskId, String resultNo, String schemeId, Date dataDate, String dataBatchNo) {
/* 270 */     TaskLogDo taskLogDo = new TaskLogDo();
/* 271 */     taskLogDo.setLogNo(IdWorker.getId());
/* 272 */     taskLogDo.setTaskId(taskId);
/* 273 */     taskLogDo.setResultNo(resultNo);
/* 274 */     taskLogDo.setDataDate(dataDate);
/* 275 */     taskLogDo.setDataBatchNo(dataBatchNo);
/* 276 */     taskLogDo.setTaskType(taskType.getCode());
/* 277 */     taskLogDo.setSchemeId(schemeId);
/* 278 */     taskLogDo.setStartTime(DataUtils.nowTimestamp());
/* 279 */     taskLogDo.setTaskStatus(CalculateStatus.CALCULATE.getCode());
/* 280 */     this.taskLogMapper.insert(taskLogDo);
/* 281 */     return taskLogDo;
/*     */   }
/*     */ 
/*     */   
/*     */   public List<JobLogDo> getRwaJobList(@NotNull String resultNo, String jobType) {
/* 286 */     LambdaQueryWrapper<JobLogDo> queryWrapper = (LambdaQueryWrapper<JobLogDo>)(new LambdaQueryWrapper()).eq(JobLogDo::getResultNo, resultNo);
/* 287 */     if (!StrUtil.isEmpty(jobType)) {
/* 288 */       queryWrapper.eq(JobLogDo::getJobType, jobType);
/*     */     }
/* 290 */     return this.jobLogMapper.selectList((Wrapper)queryWrapper);
/*     */   }
/*     */ 
/*     */   
/*     */   public TaskLogDo initTaskLog(TaskInfoDto taskInfo) {
/* 295 */     deleteRwaTaskLog(taskInfo.getResultNo());
/* 296 */     deleteRwaJobLog(taskInfo.getResultNo());
/*     */     
/* 298 */     TaskLogDo taskLogDo = insertRwaTaskLog(taskInfo.getTaskType(), taskInfo.getTaskId(), taskInfo.getResultNo(), taskInfo.getSchemeId(), taskInfo.getDataDate(), taskInfo.getDataBatchNo());
/*     */     
/* 300 */     taskInfo.setLogNo(taskLogDo.getLogNo());
/* 301 */     taskInfo.setTaskLogDo(taskLogDo);
/* 302 */     return taskLogDo;
/*     */   }
/*     */   
/*     */   public int deleteRwaTaskLog(@NotNull String resultNo) {
/* 306 */     return this.taskLogMapper.delete((Wrapper)(new LambdaQueryWrapper()).eq(TaskLogDo::getResultNo, resultNo));
/*     */   }
/*     */   
/*     */   @Async("itlTaskExecutor")
/*     */   public JobLogDo asyncInitJobLog(JobInfoDto jobInfo) {
/* 311 */     return initJobLog(jobInfo);
/*     */   }
/*     */   
/*     */   public JobLogDo initJobLog(JobInfoDto jobInfo) {
/* 315 */     JobLogDo jobLogDo = new JobLogDo();
/* 316 */     jobLogDo.setJobId(IdWorker.getIdStr());
/* 317 */     jobLogDo.setJobType(jobInfo.getJobType().getCode());
/* 318 */     jobLogDo.setLogNo(jobInfo.getLogNo());
/* 319 */     jobLogDo.setTaskType(jobInfo.getTaskType().getCode());
/* 320 */     jobLogDo.setResultNo(jobInfo.getResultNo());
/* 321 */     jobLogDo.setDataDate(jobInfo.getDataDate());
/* 322 */     jobLogDo.setDataBatchNo(jobInfo.getDataBatchNo());
/*     */     
/* 324 */     if (jobInfo.getSubThreadGroup() != null) {
/* 325 */       jobLogDo.setCalculateParam(JsonUtils.object2Json(jobInfo.getSubThreadGroup()));
/*     */     }
/* 327 */     jobLogDo.setCalculateStatus(CalculateStatus.CREATED.getCode());
/* 328 */     jobLogDo.setCalculateNum(Integer.valueOf(getCalculateSize(jobInfo)));
/* 329 */     jobInfo.setJobId(jobLogDo.getJobId());
/* 330 */     jobInfo.setJobLog(jobLogDo);
/* 331 */     return jobLogDo;
/*     */   }
/*     */   
/*     */   public JobLogDo beginJobLog(JobLogDo jobLogDo) {
/* 335 */     setServerInfo(jobLogDo);
/* 336 */     jobLogDo.setStartTime(DataUtils.nowTimestamp());
/* 337 */     jobLogDo.setCalculateStatus(CalculateStatus.CALCULATE.getCode());
/* 338 */     updateJobLog(jobLogDo);
/* 339 */     return jobLogDo;
/*     */   }
/*     */   
/*     */   public int deleteResultJobLog(String resultNo) {
/* 343 */     return deleteRwaJobLog(resultNo, JobType.RESULT);
/*     */   }
/*     */   
/*     */   public JobLogDo initResultJobLog(TaskLogDo taskLogDo) {
/* 347 */     JobLogDo jobLogDo = new JobLogDo();
/* 348 */     jobLogDo.setJobId(IdWorker.getIdStr());
/* 349 */     jobLogDo.setJobType(JobType.RESULT.getCode());
/* 350 */     jobLogDo.setLogNo(taskLogDo.getLogNo());
/* 351 */     jobLogDo.setTaskType(taskLogDo.getTaskType());
/* 352 */     jobLogDo.setResultNo(taskLogDo.getResultNo());
/* 353 */     jobLogDo.setDataDate(taskLogDo.getDataDate());
/* 354 */     jobLogDo.setDataBatchNo(taskLogDo.getDataBatchNo());
/* 355 */     jobLogDo.setCalculateStatus(CalculateStatus.CALCULATE.getCode());
/* 356 */     jobLogDo.setCalculateNum(Integer.valueOf(0));
/* 357 */     jobLogDo.setStartTime(DataUtils.nowTimestamp());
/* 358 */     setServerInfo(jobLogDo);
/* 359 */     insertJobLog(jobLogDo);
/* 360 */     return jobLogDo;
/*     */   }
/*     */   
/*     */   public int deleteRwaJobLog(@NotNull String resultNo) {
/* 364 */     return deleteRwaJobLog(resultNo, null);
/*     */   }
/*     */   
/*     */   public int deleteRwaJobLog(@NotNull String resultNo, JobType jobType) {
/* 368 */     LambdaQueryWrapper<JobLogDo> queryWrapper = (LambdaQueryWrapper<JobLogDo>)(new LambdaQueryWrapper()).eq(JobLogDo::getResultNo, resultNo);
/* 369 */     if (jobType != null) {
/* 370 */       queryWrapper.eq(JobLogDo::getJobType, jobType.getCode());
/*     */     }
/* 372 */     return this.jobLogMapper.delete((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public JobLogDo getJobLog(String jobId) {
/* 376 */     return (JobLogDo)this.jobLogMapper.selectById(jobId);
/*     */   }
/*     */   
/*     */   public int updateJobLog(JobLogDo jobLogDo) {
/* 380 */     return this.jobLogMapper.updateById(jobLogDo);
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCalculateSize(JobInfoDto jobInfo) {
/* 385 */     if (RwaUtils.isSingle(jobInfo.getTaskType())) {
/* 386 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobInfo.getJobType().ordinal()]) {
/*     */         case 1:
/*     */         case 2:
/* 389 */           return getSingleExposureCount(jobInfo.getDataBatchNo(), jobInfo.getJobType());
/*     */         case 3:
/* 391 */           return getSingleExposureCount(jobInfo.getDataBatchNo(), jobInfo.getJobType()) + this.commonService.getCountByDataBatchNo("RWA_ESI_ABS_Exposure", jobInfo.getDataBatchNo());
/*     */         case 4:
/* 393 */           return getSingleAmpExposureCount(jobInfo.getDataBatchNo(), jobInfo.getJobType());
/*     */         case 5:
/* 395 */           return this.commonService.getCountByDataBatchNo("RWA_ESI_DI_Exposure", jobInfo.getDataBatchNo());
/*     */         case 6:
/* 397 */           return this.commonService.getCountByDataBatchNo("RWA_ESI_SFT_Exposure", jobInfo.getDataBatchNo());
/*     */         case 7:
/* 399 */           return this.commonService.getCountByDataBatchNo("RWA_ESI_CCP_DF", jobInfo.getDataBatchNo());
/*     */       } 
/* 401 */       return 0;
/*     */     } 
/*     */ 
/*     */     
/* 405 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobInfo.getJobType().ordinal()]) {
/*     */       case 1:
/*     */       case 2:
/* 408 */         return getCountBySubThread(jobInfo.getJobType(), jobInfo.getDataBatchNo(), jobInfo.getSubThreadGroup(), jobInfo.getRangeList());
/*     */       case 3:
/* 410 */         return getAbsCount(jobInfo.getDataBatchNo(), jobInfo.getRangeList());
/*     */       case 4:
/* 412 */         return getAmpCount(jobInfo.getDataBatchNo(), jobInfo.getRangeList());
/*     */       case 5:
/* 414 */         return getDiCount(jobInfo.getDataBatchNo(), jobInfo.getRangeList());
/*     */       case 6:
/* 416 */         return getSftCount(jobInfo.getDataBatchNo(), jobInfo.getRangeList());
/*     */       case 7:
/* 418 */         return this.commonService.getCountByDataBatchNo("RWA_EI_CCP_DF", jobInfo.getDataBatchNo());
/*     */     } 
/* 420 */     return 0;
/*     */   }
/*     */ 
/*     */   
/*     */   public int getCountBySubThread(JobType jobType, String dataBatchNo, ThreadGroupDto subThreadGroup, List<TaskRangeDo> rangeList) {
/* 425 */     String exposureTable, groupTable, sql = "select g.group_id, e.exposure_id from #{exposure_table} e left join #{group_table} g   on g.data_batch_no = #{dataBatchNo} and g.exposure_id = e.exposure_id where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = '0'";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 431 */     if (jobType == JobType.NR) {
/* 432 */       exposureTable = "RWA_EI_NR_Exposure";
/* 433 */       groupTable = "RWA_ER_NR_ExpoGroup";
/* 434 */     } else if (jobType == JobType.RE) {
/* 435 */       exposureTable = "RWA_EI_RE_Exposure";
/* 436 */       groupTable = "RWA_ER_RE_ExpoGroup";
/*     */     } else {
/* 438 */       throw new RuntimeException();
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 446 */     sql = SqlBuilder.create(sql).setTable("exposure_table", exposureTable).setTable("group_table", groupTable).condition("", RwaUtils.generateConditionSql(rangeList, null)).setString("dataBatchNo", dataBatchNo).isNull("g", "group_id", (subThreadGroup.getUnionType() == UnionType.EXPOSURE)).build();
/* 447 */     return getCountBySubThreadGroup(sql, subThreadGroup);
/*     */   }
/*     */   
/*     */   public int getCountBySubThreadGroup(String sql, ThreadGroupDto subThreadGroup) {
/* 451 */     SqlBuilder.IdParamType idParamType = SqlBuilder.confirmIdParamType(subThreadGroup.getBeginId(), subThreadGroup.getEndId());
/* 452 */     String associatedField = "t." + subThreadGroup.getUnionType().getCode() + "_id";
/* 453 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$IdParamType[idParamType.ordinal()]) {
/*     */       case 1:
/* 455 */         sql = SqlBuilder.create("select count(1) AS CNT from (" + sql + ") t ").build();
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 2:
/* 461 */         sql = SqlBuilder.create("select count(1) AS CNT from (" + sql + ") t where " + associatedField + " >= #{beginId} and " + associatedField + " < #{endId} ").setString("beginId", subThreadGroup.getBeginId()).setString("endId", subThreadGroup.getEndId()).build();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/* 466 */         sql = SqlBuilder.create("select count(1) AS CNT from (" + sql + ") t where " + associatedField + " = #{beginId}").setString("beginId", subThreadGroup.getBeginId()).build();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 471 */         sql = SqlBuilder.create("select count(1) AS CNT from (" + sql + ") t where " + associatedField + " >= #{beginId} ").setString("beginId", subThreadGroup.getBeginId()).build();
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 476 */         sql = SqlBuilder.create("select count(1) AS CNT from (" + sql + ") t where " + associatedField + " < #{endId} ").setString("endId", subThreadGroup.getEndId()).build();
/*     */         break;
/*     */     } 
/* 479 */     return this.commonService.getInt(sql);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public int getAbsCount(String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 485 */     String noeSql = "select count(1) AS CNT from RWA_EI_ABS_Exposure a where a.data_batch_no = #{dataBatchNo} and a.book_type = '1' and exists(select 1 from RWA_EI_ABS_Product p  where p.abs_product_id = a.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = '0') ";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 491 */     noeSql = SqlBuilder.create(noeSql).condition("", RwaUtils.generateConditionSql(rangeList, Identity.NO)).setString("dataBatchNo", dataBatchNo).build();
/*     */     
/* 493 */     String oeSql = "select count(1) AS CNT from RWA_EI_ABS_Exposure a join RWA_EI_ABS_Product p   on p.abs_product_id = a.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = '1' where a.data_batch_no = #{dataBatchNo} and a.book_type = '1' ";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 499 */     oeSql = SqlBuilder.create(oeSql).condition("", RwaUtils.generateConditionSql(rangeList, Identity.YES)).setString("dataBatchNo", dataBatchNo).build();
/* 500 */     int aeCount = this.commonService.getInt(noeSql) + this.commonService.getInt(oeSql);
/*     */     
/* 502 */     String nrSql = "select count(1) AS CNT from RWA_EI_NR_Exposure e where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = '1'";
/*     */ 
/*     */     
/* 505 */     nrSql = SqlBuilder.create(nrSql).condition("", RwaUtils.generateConditionSql(rangeList, null)).setString("dataBatchNo", dataBatchNo).build();
/*     */     
/* 507 */     String reSql = "select count(1) AS CNT from RWA_EI_RE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = '1'";
/*     */ 
/*     */     
/* 510 */     reSql = SqlBuilder.create(reSql).condition("", RwaUtils.generateConditionSql(rangeList, null)).setString("dataBatchNo", dataBatchNo).build();
/* 511 */     int uaeCount = this.commonService.getInt(nrSql) + this.commonService.getInt(reSql);
/* 512 */     return aeCount + uaeCount;
/*     */   }
/*     */   
/*     */   public int getAmpCount(String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 516 */     String sql = "select count(1) AS CNT from RWA_EI_AMP_Exposure e where e.data_batch_no = #{dataBatchNo} and e.credit_risk_data_type = #{dataType} ";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 522 */     sql = SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).setString("dataType", (ICodeEnum)JobType.AMP).condition("", RwaUtils.generateConditionSql(rangeList, null)).build();
/* 523 */     return this.commonService.getInt(sql);
/*     */   }
/*     */   
/*     */   public int getDiCount(String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 527 */     String sql = "select count(1) AS CNT from RWA_EI_DI_Exposure e left join RWA_EI_DI_Netting n   on n.data_batch_no = #{dataBatchNo} and n.netting_id = e.netting_id where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
/*     */ 
/*     */ 
/*     */     
/* 531 */     int nc = this.commonService.getInt(SqlBuilder.create(sql)
/* 532 */         .setString("dataBatchNo", dataBatchNo)
/* 533 */         .setString("nettingFlag", (ICodeEnum)Identity.NO)
/* 534 */         .condition("", RwaUtils.generateConditionSql(rangeList, Identity.NO))
/* 535 */         .build());
/* 536 */     int ec = this.commonService.getInt(SqlBuilder.create(sql)
/* 537 */         .setString("dataBatchNo", dataBatchNo)
/* 538 */         .setString("nettingFlag", (ICodeEnum)Identity.YES)
/* 539 */         .condition("", RwaUtils.generateConditionSql(rangeList, Identity.YES))
/* 540 */         .build());
/* 541 */     return nc + ec;
/*     */   }
/*     */   
/*     */   public int getSftCount(String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 545 */     String sql = "select count(1) AS CNT from RWA_EI_SFT_Exposure e left join RWA_EI_SFT_Netting n   on n.data_batch_no = #{dataBatchNo} and n.netting_id = e.netting_id where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
/*     */ 
/*     */ 
/*     */     
/* 549 */     int nc = this.commonService.getInt(SqlBuilder.create(sql)
/* 550 */         .setString("dataBatchNo", dataBatchNo)
/* 551 */         .setString("nettingFlag", (ICodeEnum)Identity.NO)
/* 552 */         .condition("", RwaUtils.generateConditionSql(rangeList, Identity.NO))
/* 553 */         .build());
/* 554 */     int ec = this.commonService.getInt(SqlBuilder.create(sql)
/* 555 */         .setString("dataBatchNo", dataBatchNo)
/* 556 */         .setString("nettingFlag", (ICodeEnum)Identity.YES)
/* 557 */         .condition("", RwaUtils.generateConditionSql(rangeList, Identity.YES))
/* 558 */         .build());
/* 559 */     return nc + ec;
/*     */   }
/*     */   
/*     */   public int getSingleExposureCount(String dataBatchNo, JobType jobType) {
/* 563 */     String sql = "select count(1) AS CNT from RWA_ESI_GE_Exposure where data_batch_no = #{dataBatchNo} and credit_risk_data_type = #{dataType}";
/* 564 */     return this.commonService.getInt(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).setString("dataType", (ICodeEnum)jobType).build());
/*     */   }
/*     */   
/*     */   public int getSingleAmpExposureCount(String dataBatchNo, JobType jobType) {
/* 568 */     String sql = "select count(1) AS CNT from RWA_ESI_AMP_Exposure where data_batch_no = #{dataBatchNo} and credit_risk_data_type = #{dataType}";
/* 569 */     return this.commonService.getInt(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).setString("dataType", (ICodeEnum)jobType).build());
/*     */   }
/*     */   
/*     */   public void batchInsertErrorData(List<ExcDataDo> list, String resultNo, JobType jobType, int batchSize) {
/* 573 */     if (CollUtil.isEmpty(list)) {
/*     */       return;
/*     */     }
/* 576 */     String sql = "insert into RWA_EL_EDATA(serial_no, result_no, job_id, job_type, belong_table, data_id, error_code) values(?,?,?,?,?,?,?)";
/* 577 */     int[][] result = this.commonService.getJdbcTemplate().batchUpdate(sql, list, batchSize, (ParameterizedPreparedStatementSetter)new Object(this, resultNo, jobType));
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\TaskLogService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */