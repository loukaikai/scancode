/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.batch.exception.JobStopException;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.ExcDataCode;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.entity.ExcDataDo;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*     */ import com.amarsoft.rwa.engine.exception.JobParameterException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.IdWorker;
/*     */ import java.math.BigDecimal;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*     */ 
/*     */ public class JobUtils {
/*  32 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.JobUtils.class);
/*     */ 
/*     */   
/*  35 */   public static Set<String> ctGroupTaskSet = new HashSet<>();
/*  36 */   public static Set<String> stopGroupTaskSet = new HashSet<>();
/*  37 */   public static Map<String, Map<String, JobLogDo>> groupJobMap = new ConcurrentHashMap<>();
/*  38 */   private static Map<String, String> resultJobMap = new ConcurrentHashMap<>();
/*  39 */   private static Set<String> stopRwaTaskSet = new HashSet<>();
/*  40 */   private static Map<String, Map<String, JobInfoDto>> rwaJobMap = new ConcurrentHashMap<>();
/*  41 */   private static Map<String, String> rwaJobRelationMap = new ConcurrentHashMap<>();
/*     */   
/*  43 */   private static Map<Long, String> stopWaitMap = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  48 */   private static Map<String, List<ExcDataDo>> excDataMap = new ConcurrentHashMap<>();
/*  49 */   private static Map<String, Map<String, ExcDataDo>> excDataStatMap = new ConcurrentHashMap<>();
/*     */   
/*  51 */   public static Integer chunkSize = Integer.valueOf(1000);
/*  52 */   public static Integer batchSize = Integer.valueOf(2000);
/*  53 */   public static Integer singleThreadCount = Integer.valueOf(50000);
/*  54 */   public static Boolean writerAssertUpdates = Boolean.valueOf(true);
/*     */   public static boolean enableBigGroup = true;
/*  56 */   public static Integer bigGroupCount = Integer.valueOf(200000);
/*     */ 
/*     */   
/*     */   public static boolean enableThreadTaskInit = true;
/*     */   
/*  61 */   public static long taskPollingInterval = 60000L;
/*  62 */   public static int syncIntervalTime = 3;
/*     */   
/*     */   public static List<BigDecimal> groupTimePercent;
/*     */   
/*     */   public static BigDecimal rwaResultTimePercent;
/*  67 */   public static long lastGcTime = System.currentTimeMillis();
/*  68 */   public static int gcIntervalTime = 30;
/*     */   
/*     */   public static boolean enableLocalThreadCalculate = false;
/*     */   
/*     */   public static String getResultNo(String jobId) {
/*  73 */     return rwaJobRelationMap.get(jobId);
/*     */   }
/*     */   
/*     */   public static Set<String> getGroupTaskSet() {
/*  77 */     return ctGroupTaskSet;
/*     */   }
/*     */   
/*     */   public static Set<String> getStopGroupTaskSet() {
/*  81 */     return stopGroupTaskSet;
/*     */   }
/*     */   
/*     */   public static Map<String, Map<String, JobLogDo>> getGroupJobMap() {
/*  85 */     return groupJobMap;
/*     */   }
/*     */   
/*     */   public static boolean addGroupTask(String dataBatchNo) {
/*  89 */     return getGroupTaskSet().add(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static boolean removeGroupTask(String dataBatchNo) {
/*  93 */     return getGroupTaskSet().remove(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static boolean addStopGroupTask(String dataBatchNo) {
/*  97 */     return getStopGroupTaskSet().add(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static boolean removeStopGroupTask(String dataBatchNo) {
/* 101 */     return getStopGroupTaskSet().remove(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static boolean isStopGroupTask(String dataBatchNo) {
/* 105 */     return getStopGroupTaskSet().contains(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static void checkStopGroupTask(String dataBatchNo, String stopInfo) throws JobStopException {
/* 109 */     if (isStopGroupTask(dataBatchNo)) {
/* 110 */       throw new JobStopException(stopInfo);
/*     */     }
/*     */   }
/*     */   
/*     */   public static void putGroupJob(JobLogDo jobLogDo) {
/* 115 */     if (getGroupJobMap().get(jobLogDo.getDataBatchNo()) == null) {
/* 116 */       getGroupJobMap().put(jobLogDo.getDataBatchNo(), new ConcurrentHashMap<>());
/*     */     }
/* 118 */     ((Map<String, JobLogDo>)getGroupJobMap().get(jobLogDo.getDataBatchNo())).put(jobLogDo.getJobId(), jobLogDo);
/*     */   }
/*     */   
/*     */   public static void removeGroupJob(JobLogDo jobLogDo) {
/* 122 */     if (getGroupJobMap().get(jobLogDo.getDataBatchNo()) == null) {
/* 123 */       getGroupJobMap().put(jobLogDo.getDataBatchNo(), new ConcurrentHashMap<>());
/*     */     }
/* 125 */     ((Map)getGroupJobMap().get(jobLogDo.getDataBatchNo())).remove(jobLogDo.getJobId());
/*     */   }
/*     */   
/*     */   public static void removeGroupJob(String dataBatchNo) {
/* 129 */     getGroupJobMap().remove(dataBatchNo);
/*     */   }
/*     */   
/*     */   public static Map<String, Map<String, JobInfoDto>> getRwaJobMap() {
/* 133 */     return rwaJobMap;
/*     */   }
/*     */   
/*     */   public static void putRwaJob(JobInfoDto jobInfo) {
/* 137 */     if (getRwaJobMap().get(jobInfo.getResultNo()) == null) {
/* 138 */       getRwaJobMap().put(jobInfo.getResultNo(), new ConcurrentHashMap<>());
/*     */     }
/* 140 */     rwaJobRelationMap.put(jobInfo.getJobId(), jobInfo.getResultNo());
/* 141 */     ((Map<String, JobInfoDto>)getRwaJobMap().get(jobInfo.getResultNo())).put(jobInfo.getJobId(), jobInfo);
/*     */   }
/*     */   
/*     */   public static void removeRwaJob(JobInfoDto jobInfo) {
/* 145 */     if (getRwaJobMap().get(jobInfo.getResultNo()) == null) {
/* 146 */       getRwaJobMap().put(jobInfo.getResultNo(), new ConcurrentHashMap<>());
/*     */     }
/* 148 */     ((Map)getRwaJobMap().get(jobInfo.getResultNo())).remove(jobInfo.getJobId());
/*     */   }
/*     */   
/*     */   public static void removeRwaJob(String resultNo) {
/* 152 */     Map<String, JobInfoDto> jobMap = getRwaJobMap().get(resultNo);
/* 153 */     if (CollUtil.isEmpty(jobMap)) {
/*     */       return;
/*     */     }
/* 156 */     getRwaJobMap().remove(resultNo);
/*     */   }
/*     */   
/*     */   public static boolean existJob(JobInfoDto jobInfo) {
/* 160 */     return getRwaJobMap().containsKey(jobInfo.getJobId());
/*     */   }
/*     */   
/*     */   public static Set<String> getStopRwaTaskSet() {
/* 164 */     return stopRwaTaskSet;
/*     */   }
/*     */   
/*     */   public static void addStopRwaTask(String resultNo) {
/* 168 */     getStopRwaTaskSet().add(resultNo);
/*     */   }
/*     */   
/*     */   public static void removeStopRwaTask(String resultNo) {
/* 172 */     getStopRwaTaskSet().remove(resultNo);
/*     */   }
/*     */   
/*     */   public static boolean isStopRwaJob(String jobId) {
/* 176 */     return isStopRwaTask(getResultNo(jobId));
/*     */   }
/*     */   
/*     */   public static boolean isStopRwaTask(String resultNo) {
/* 180 */     return getStopRwaTaskSet().contains(resultNo);
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<String, String> getResultJobMap() {
/* 185 */     return resultJobMap;
/*     */   }
/*     */   
/*     */   public static void addResultJob(String resultNo, String dataBatchNo) {
/* 189 */     getResultJobMap().put(resultNo, dataBatchNo);
/*     */   }
/*     */   
/*     */   public static void removeResultJob(String resultNo) {
/* 193 */     getResultJobMap().remove(resultNo);
/*     */   }
/*     */   
/*     */   public static void addErrorData(String jobId, InterfaceDataType dataType, String dataId, ExcDataCode excCode) {
/* 197 */     ExcDataDo data = new ExcDataDo();
/* 198 */     data.setSerialNo(IdWorker.getIdStr());
/* 199 */     data.setJobId(jobId);
/* 200 */     data.setBelongTable(dataType.getCode());
/* 201 */     data.setDataId(dataId);
/* 202 */     data.setExcCode(Convert.toInt(excCode.getCode()));
/* 203 */     addErrorData2Map(jobId, data);
/*     */   }
/*     */   
/*     */   public static synchronized void addErrorData2Map(String jobId, ExcDataDo data) {
/* 207 */     if (excDataMap.containsKey(jobId)) {
/*     */       
/* 209 */       ((List<ExcDataDo>)excDataMap.get(jobId)).add(data);
/*     */     } else {
/* 211 */       List<ExcDataDo> list = new ArrayList<>();
/* 212 */       list.add(data);
/* 213 */       excDataMap.put(jobId, list);
/*     */     } 
/*     */     
/* 216 */     addStatisticsErrData(jobId, data);
/*     */   }
/*     */   
/*     */   public static void addStatisticsErrData(String jobId, ExcDataDo data) {
/* 220 */     Map<String, ExcDataDo> map = excDataStatMap.get(jobId);
/* 221 */     if (map == null) {
/* 222 */       map = new HashMap<>();
/* 223 */       excDataStatMap.put(jobId, map);
/*     */     } 
/* 225 */     String key = DataUtils.generateKey(new String[] { data.getBelongTable(), data.getExcCode().toString() });
/* 226 */     ExcDataDo excDataDo = map.get(key);
/* 227 */     if (excDataDo == null) {
/* 228 */       excDataDo = new ExcDataDo();
/* 229 */       excDataDo.setBelongTable(data.getBelongTable());
/* 230 */       excDataDo.setExcCode(data.getExcCode());
/* 231 */       excDataDo.setCount(Integer.valueOf(1));
/* 232 */       map.put(key, excDataDo);
/*     */       return;
/*     */     } 
/* 235 */     excDataDo.setCount(Integer.valueOf(excDataDo.getCount().intValue() + 1));
/*     */   }
/*     */   
/*     */   public static void printErrorData(String jobId) {
/* 239 */     Map<String, ExcDataDo> map = excDataStatMap.get(jobId);
/* 240 */     if (CollUtil.isEmpty(map)) {
/*     */       return;
/*     */     }
/* 243 */     log.warn("作业[{}]: 映射异常\n{}", jobId, getErrorDataInfo(jobId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static String getErrorDataInfo(String jobId) {
/* 250 */     Map<String, ExcDataDo> map = excDataStatMap.get(jobId);
/* 251 */     if (CollUtil.isEmpty(map)) {
/* 252 */       return "";
/*     */     }
/* 254 */     StringBuilder sb = new StringBuilder();
/* 255 */     for (ExcDataDo excDataDo : map.values()) {
/* 256 */       sb.append("    接口表[").append(excDataDo.getBelongTable())
/* 257 */         .append("]-异常代码[").append(excDataDo.getExcCode())
/* 258 */         .append("]: 异常数据量[").append(excDataDo.getCount()).append("]\n");
/*     */     }
/* 260 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static List<ExcDataDo> getErrorDataList(String jobId) {
/* 264 */     return excDataMap.get(jobId);
/*     */   }
/*     */   
/*     */   public static void clearErrorData(String jobId) {
/* 268 */     excDataStatMap.remove(jobId);
/* 269 */     excDataMap.remove(jobId);
/*     */   }
/*     */ 
/*     */   
/*     */   public static JobInfoDto getJobInfo(String jobId) {
/* 274 */     JobInfoDto jobInfo = (JobInfoDto)((Map)getRwaJobMap().get(getResultNo(jobId))).get(jobId);
/* 275 */     if (jobInfo == null) {
/* 276 */       throw new RuntimeException("计算日常， 作业信息为空[" + jobId + "]");
/*     */     }
/* 278 */     return jobInfo;
/*     */   }
/*     */   
/*     */   public static JobLogDo getJobLog(String jobId) {
/* 282 */     JobLogDo jobLogDo = getJobInfo(jobId).getJobLog();
/* 283 */     if (jobLogDo == null) {
/* 284 */       throw new RuntimeException("计算异常, 作业日志为空[" + jobId + "]");
/*     */     }
/* 286 */     return jobLogDo;
/*     */   }
/*     */   
/*     */   public static void checkRwaJobLog(List<JobLogDo> jobLogDoList) {
/* 290 */     if (!CollUtil.isEmpty(jobLogDoList)) {
/* 291 */       CalculateStatus calculateStatus = getJobCalculateStatus(jobLogDoList);
/* 292 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$CalculateStatus[calculateStatus.ordinal()]) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/* 296 */           throw new JobParameterException("当前任务[" + calculateStatus.getName() + "], 请确认调用期次是否准确！");
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Map<String, List<JobLogDo>> statJobLog(Collection<JobLogDo> jobLogDos) {
/* 302 */     return (Map<String, List<JobLogDo>>)jobLogDos.stream().collect(Collectors.groupingBy(JobLogDo::getCalculateStatus));
/*     */   }
/*     */   
/*     */   public static CalculateStatus getCalculateStatus(Collection<String> statusColl) {
/* 306 */     if (statusColl.contains("0") || statusColl.contains(CalculateStatus.CALCULATE.getCode()) || statusColl.contains(CalculateStatus.CREATED.getCode()))
/* 307 */       return CalculateStatus.CALCULATE; 
/* 308 */     if (statusColl.contains(CalculateStatus.STOP.getCode()))
/* 309 */       return CalculateStatus.STOP; 
/* 310 */     if (statusColl.contains(CalculateStatus.EXCEPTION.getCode())) {
/* 311 */       return CalculateStatus.EXCEPTION;
/*     */     }
/* 313 */     return CalculateStatus.COMPLETE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static CalculateStatus getJobCalculateStatus(Collection<JobLogDo> jobLogDos) {
/* 318 */     return getCalculateStatus(statJobLog(jobLogDos).keySet());
/*     */   }
/*     */   
/*     */   public static boolean isEnd(String calculateStatus) {
/* 322 */     if (StrUtil.equals(calculateStatus, CalculateStatus.COMPLETE.getCode()) || 
/* 323 */       StrUtil.equals(calculateStatus, CalculateStatus.STOP.getCode()) || 
/* 324 */       StrUtil.equals(calculateStatus, CalculateStatus.EXCEPTION.getCode())) {
/* 325 */       return true;
/*     */     }
/* 327 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isEnd(Set<String> statusSet) {
/* 331 */     return (getCalculateStatus(statusSet) != CalculateStatus.CALCULATE);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isInit(Set<String> statusSet) {
/* 336 */     if (statusSet.size() == 1 && statusSet.contains(CalculateStatus.CREATED.getCode())) {
/* 337 */       return true;
/*     */     }
/* 339 */     return false;
/*     */   }
/*     */   
/*     */   public static long addReaderTime(String jobId, LocalDateTime start, LocalDateTime end) {
/* 343 */     return getJobLog(jobId).getReaderTime().addAndGet(DataUtils.timeBt(start, end));
/*     */   }
/*     */   
/*     */   public static long addProcessorTime(String jobId, LocalDateTime start, LocalDateTime end) {
/* 347 */     return getJobLog(jobId).getProcessorTime().addAndGet(DataUtils.timeBt(start, end));
/*     */   }
/*     */   
/*     */   public static long addWriterTime(String jobId, LocalDateTime start, LocalDateTime end) {
/* 351 */     return getJobLog(jobId).getWriterTime().addAndGet(DataUtils.timeBt(start, end));
/*     */   }
/*     */   
/*     */   public static int addCalculateCount(String jobId, int count) {
/* 355 */     JobLogDo jobLogDo = getJobLog(jobId);
/* 356 */     jobLogDo.getCtCount().addAndGet(count);
/* 357 */     return jobLogDo.getCtCalculateCount().addAndGet(count);
/*     */   }
/*     */   
/*     */   public static int addSkipCount(String jobId, int count) {
/* 361 */     JobLogDo jobLogDo = getJobLog(jobId);
/* 362 */     jobLogDo.getCtCount().addAndGet(count);
/* 363 */     return jobLogDo.getCtSkipCount().addAndGet(count);
/*     */   }
/*     */   
/*     */   public static int addExceptionCount(String jobId, int count) {
/* 367 */     JobLogDo jobLogDo = getJobLog(jobId);
/* 368 */     jobLogDo.getCtCount().addAndGet(count);
/* 369 */     return jobLogDo.getCtExceptionCount().addAndGet(count);
/*     */   }
/*     */   
/*     */   public static String getSpecificValue(long readerTime, long processorTime, long writerTime) {
/* 373 */     long total = readerTime + processorTime + writerTime;
/* 374 */     if (total == 0L) {
/* 375 */       return "reader:processor:writer 0:0:0";
/*     */     }
/* 377 */     int rv = NumberUtil.round(NumberUtil.div((float)readerTime, (float)total) * 100.0D, 0).intValue();
/* 378 */     int pv = NumberUtil.round(NumberUtil.div((float)processorTime, (float)total) * 100.0D, 0).intValue();
/* 379 */     int wv = NumberUtil.round(NumberUtil.div((float)writerTime, (float)total) * 100.0D, 0).intValue();
/* 380 */     return "reader:processor:writer " + rv + ":" + pv + ":" + wv;
/*     */   }
/*     */   
/*     */   public static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(int coreSize, int queue, String namePrefix) {
/* 384 */     ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
/* 385 */     taskExecutor.setMaxPoolSize(coreSize);
/* 386 */     taskExecutor.setCorePoolSize(coreSize);
/* 387 */     taskExecutor.setQueueCapacity(queue);
/* 388 */     taskExecutor.setThreadNamePrefix(namePrefix);
/*     */ 
/*     */     
/* 391 */     taskExecutor.initialize();
/* 392 */     return taskExecutor;
/*     */   }
/*     */   
/*     */   public static synchronized long gc(boolean isCheck) {
/* 396 */     if (isCheck) {
/* 397 */       long now = System.currentTimeMillis();
/* 398 */       if (now - lastGcTime <= (gcIntervalTime * 1000)) {
/* 399 */         return 0L;
/*     */       }
/*     */     } 
/* 402 */     int mb = 1048576;
/*     */     
/* 404 */     long totalMemory = Runtime.getRuntime().totalMemory() / mb;
/* 405 */     long freeMemory = Runtime.getRuntime().freeMemory() / mb;
/* 406 */     long useMemory = totalMemory - freeMemory;
/* 407 */     long maxMemory = Runtime.getRuntime().maxMemory() / mb;
/* 408 */     log.info("GC前>>>当前内存使用情况： 总内存[{}MB] | 使用内存[{}MB] | 空闲内存[{}MB] | 最大内存[{}MB]", new Object[] { Long.valueOf(totalMemory), Long.valueOf(useMemory), Long.valueOf(freeMemory), Long.valueOf(maxMemory) });
/* 409 */     Runtime.getRuntime().gc();
/*     */     
/* 411 */     totalMemory = Runtime.getRuntime().totalMemory() / mb;
/* 412 */     freeMemory = Runtime.getRuntime().freeMemory() / mb;
/* 413 */     useMemory = totalMemory - freeMemory;
/* 414 */     maxMemory = Runtime.getRuntime().maxMemory() / mb;
/* 415 */     log.info("GC后<<<当前内存使用情况： 总内存[{}MB] | 使用内存[{}MB] | 空闲内存[{}MB] | 最大内存[{}MB]", new Object[] { Long.valueOf(totalMemory), Long.valueOf(useMemory), Long.valueOf(freeMemory), Long.valueOf(maxMemory) });
/* 416 */     lastGcTime = System.currentTimeMillis();
/* 417 */     return useMemory;
/*     */   }
/*     */   
/*     */   public static boolean isSync(long lastSyncTime) {
/* 421 */     long now = System.currentTimeMillis();
/* 422 */     if (now - lastSyncTime >= (syncIntervalTime * 1000)) {
/* 423 */       return true;
/*     */     }
/* 425 */     return false;
/*     */   }
/*     */   
/*     */   public static String addStopWaitTask(long logNo) {
/* 429 */     return stopWaitMap.putIfAbsent(Long.valueOf(logNo), Identity.YES.getCode());
/*     */   }
/*     */   
/*     */   public static void removeStopWaitTask(long logNo) {
/* 433 */     stopWaitMap.remove(Long.valueOf(logNo));
/*     */   }
/*     */   
/*     */   public static boolean isStopWaitTask(long logNo) {
/* 437 */     return stopWaitMap.containsKey(Long.valueOf(logNo));
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\JobUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */