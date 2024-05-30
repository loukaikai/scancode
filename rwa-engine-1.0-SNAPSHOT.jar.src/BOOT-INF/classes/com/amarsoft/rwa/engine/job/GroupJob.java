/*      */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*      */ import cn.hutool.core.collection.CollUtil;
/*      */ import cn.hutool.core.util.StrUtil;
/*      */ import cn.hutool.extra.spring.SpringUtil;
/*      */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*      */ import com.amarsoft.rwa.engine.constant.JobType;
/*      */ import com.amarsoft.rwa.engine.constant.TaskType;
/*      */ import com.amarsoft.rwa.engine.constant.UnionType;
/*      */ import com.amarsoft.rwa.engine.entity.JobLogDo;
/*      */ import com.amarsoft.rwa.engine.entity.RelevanceDto;
/*      */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*      */ import com.amarsoft.rwa.engine.job.JobUtils;
/*      */ import com.amarsoft.rwa.engine.service.CommonService;
/*      */ import com.amarsoft.rwa.engine.service.JobService;
/*      */ import com.amarsoft.rwa.engine.service.LockService;
/*      */ import com.amarsoft.rwa.engine.service.TaskLogService;
/*      */ import com.amarsoft.rwa.engine.util.DataUtils;
/*      */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*      */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*      */ import java.text.DecimalFormat;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ import java.util.concurrent.locks.Lock;
/*      */ import javax.validation.constraints.NotNull;
/*      */ import org.slf4j.Logger;
/*      */ import org.springframework.jdbc.core.JdbcTemplate;
/*      */ import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
/*      */ import org.springframework.util.StringUtils;
/*      */ 
/*      */ public class GroupJob {
/*      */   private TaskType taskType;
/*      */   private JobType jobType;
/*      */   private String dataBatchNo;
/*      */   private Long logNo;
/*      */   private boolean groupAnalyze;
/*   39 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.GroupJob.class); private JobLogDo jobLogDo; private JobService jobService; private LockService lockService; private String jobFullName;
/*      */   
/*      */   public TaskType getTaskType() {
/*   42 */     return this.taskType; } public void setTaskType(TaskType taskType) {
/*   43 */     this.taskType = taskType;
/*      */   }
/*   45 */   public JobType getJobType() { return this.jobType; } public void setJobType(JobType jobType) {
/*   46 */     this.jobType = jobType;
/*      */   }
/*   48 */   public String getDataBatchNo() { return this.dataBatchNo; } public void setDataBatchNo(String dataBatchNo) {
/*   49 */     this.dataBatchNo = dataBatchNo;
/*      */   }
/*   51 */   public Long getLogNo() { return this.logNo; } public void setLogNo(Long logNo) {
/*   52 */     this.logNo = logNo;
/*      */   }
/*   54 */   public boolean isGroupAnalyze() { return this.groupAnalyze; } public void setGroupAnalyze(boolean groupAnalyze) {
/*   55 */     this.groupAnalyze = groupAnalyze;
/*      */   }
/*   57 */   public JobLogDo getJobLogDo() { return this.jobLogDo; } public void setJobLogDo(JobLogDo jobLogDo) {
/*   58 */     this.jobLogDo = jobLogDo;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*   63 */   private CalculateStatus calculateStatus = CalculateStatus.CREATED;
/*      */ 
/*      */ 
/*      */   
/*   67 */   private final DecimalFormat decimalFormat = new DecimalFormat("0000000000");
/*      */   
/*      */   public GroupJob(TaskType taskType, JobType jobType, String dataBatchNo, Long logNo, boolean groupAnalyze) {
/*   70 */     this.taskType = taskType;
/*   71 */     this.jobType = jobType;
/*   72 */     this.dataBatchNo = dataBatchNo;
/*   73 */     this.logNo = logNo;
/*   74 */     this.groupAnalyze = groupAnalyze;
/*   75 */     this.jobService = (JobService)SpringUtil.getBean(JobService.class);
/*   76 */     this.lockService = (LockService)SpringUtil.getBean(LockService.class);
/*   77 */     if (RwaUtils.isSingle(taskType)) {
/*   78 */       this.jobFullName = taskType.getName() + "-" + dataBatchNo;
/*      */     } else {
/*   80 */       this.jobFullName = jobType.getName() + "-" + dataBatchNo;
/*      */     } 
/*      */   }
/*      */   
/*      */   private void before(TaskLogService taskLogService) throws JobStopException {
/*   85 */     this.calculateStatus = CalculateStatus.CALCULATE;
/*   86 */     this.jobLogDo = taskLogService.getJobLogList(this.logNo, this.jobType).get(0);
/*   87 */     taskLogService.beginJobLog(this.jobLogDo);
/*   88 */     log.info("数据分组[{}]: 开始运行", this.jobFullName);
/*      */     
/*   90 */     this.jobService.addGroupJob(this.jobLogDo);
/*      */   }
/*      */   
/*      */   private void afterReturning(TaskLogService taskLogService) {
/*   94 */     this.calculateStatus = CalculateStatus.COMPLETE;
/*   95 */     taskLogService.closeJobLog(this.jobLogDo);
/*   96 */     log.info("数据分组[{}]: 运行完成", this.jobFullName);
/*      */   }
/*      */   
/*      */   private void afterThrowing(TaskLogService taskLogService, Throwable throwable) {
/*  100 */     if (throwable instanceof JobStopException) {
/*      */       
/*  102 */       this.calculateStatus = CalculateStatus.STOP;
/*  103 */       taskLogService.stopJobLog(this.jobLogDo, throwable.getMessage());
/*  104 */       log.error("数据分组[" + this.jobFullName + "]: 运行停止", throwable);
/*      */     } else {
/*      */       
/*  107 */       this.calculateStatus = CalculateStatus.EXCEPTION;
/*  108 */       taskLogService.exceptionJobLog(this.jobLogDo, throwable);
/*  109 */       log.error("数据分组[" + this.jobFullName + "]: 运行异常", throwable);
/*      */     } 
/*      */   }
/*      */   
/*      */   private void afterFinally() {}
/*      */   
/*      */   public com.amarsoft.rwa.engine.job.GroupJob execute(TaskLogService taskLogService, CommonService commonService)
/*      */   {
/*      */     
/*  118 */     try { before(taskLogService);
/*  119 */       group(commonService);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  125 */       return this; } catch (Exception e) { return this; }
/*      */     finally
/*      */     { Exception exception = null;
/*      */       afterFinally(); }
/*      */      } public CalculateStatus getStatus() {
/*  130 */     return this.calculateStatus;
/*      */   }
/*      */   
/*      */   public String getPrefix() {
/*  134 */     if (RwaUtils.isSingle(this.taskType))
/*  135 */       return "SE"; 
/*  136 */     if (this.jobType == JobType.NR_GROUP) {
/*  137 */       return "NR";
/*      */     }
/*  139 */     return "RE";
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public void group(CommonService commonService) {
/*  145 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-1");
/*  146 */     int cr = insertExposureGroup(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  147 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  148 */     log.info("数据分组[{}]: 初始化暴露分组结果[{}]", this.jobFullName, Integer.valueOf(cr));
/*  149 */     this.jobService.putGroupJob(this.jobLogDo);
/*  150 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */     
/*  153 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-2");
/*  154 */     int mr = insertMitigationGroup(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  155 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  156 */     log.info("数据分组[{}]: 初始化缓释物分组结果[{}]", this.jobFullName, Integer.valueOf(mr));
/*  157 */     this.jobService.putGroupJob(this.jobLogDo);
/*  158 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */     
/*  161 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-3");
/*  162 */     int re = insertM2mExposureTmp(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  163 */     Map<String, Object> map = selectAllM2m(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  164 */     Map<String, RelevanceDto> relevanceMap = (Map<String, RelevanceDto>)map.get("relevanceMap");
/*  165 */     Map<String, List<RelevanceDto>> exposureMap = (Map<String, List<RelevanceDto>>)map.get("exposureMap");
/*  166 */     int exposureSize = exposureMap.size();
/*  167 */     Map<String, List<RelevanceDto>> mitigationMap = (Map<String, List<RelevanceDto>>)map.get("mitigationMap");
/*  168 */     int mitigationSize = mitigationMap.size();
/*  169 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  170 */     log.info("数据分组[{}]: 获取多对多分组数据[{}]", this.jobFullName, Integer.valueOf(relevanceMap.size()));
/*  171 */     this.jobService.putGroupJob(this.jobLogDo);
/*  172 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */     
/*  175 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-4");
/*  176 */     List<RelevanceDto> groupList = groupData(exposureMap, mitigationMap, "M" + getPrefix());
/*  177 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  178 */     log.info("数据分组[{}]: 多对多分组计算完成[{}]", this.jobFullName, Integer.valueOf(groupList.size()));
/*  179 */     this.jobService.putGroupJob(this.jobLogDo);
/*  180 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */     
/*  183 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-5");
/*      */     
/*  185 */     int egr = batchInsertExposureGroup(commonService, this.taskType, this.jobType, groupList, exposureSize, this.dataBatchNo);
/*      */     
/*  187 */     batchUpdateExposureGroup(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  188 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  189 */     log.info("数据分组[{}]: 暴露分组结果更新[{}]", this.jobFullName, Integer.valueOf(egr));
/*  190 */     this.jobService.putGroupJob(this.jobLogDo);
/*  191 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */     
/*  194 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-6");
/*      */     
/*  196 */     int mgr = batchInsertMitigationGroup(commonService, this.taskType, this.jobType, groupList, mitigationSize, this.dataBatchNo);
/*      */     
/*  198 */     batchUpdateMitigationGroup(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*  199 */     this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*      */     
/*  201 */     log.info("数据分组[{}]: 缓释物分组结果更新[{}]", this.jobFullName, Integer.valueOf(mgr));
/*  202 */     this.jobService.putGroupJob(this.jobLogDo);
/*  203 */     this.jobService.syncStopGroupTask();
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  208 */     JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-7");
/*  209 */     if (RwaUtils.isSingle(this.taskType)) {
/*  210 */       int r1 = updateSingleExposureGroupResult(commonService, this.jobType, this.dataBatchNo);
/*  211 */       int r2 = updateSingleRelevanceGroupResult(commonService, this.jobType, this.dataBatchNo);
/*  212 */       int r3 = updateSingleCollateralGroupResult(commonService, this.jobType, this.dataBatchNo);
/*  213 */       int r4 = updateSingleGuaranteeGroupResult(commonService, this.jobType, this.dataBatchNo);
/*  214 */       this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  215 */       log.info("数据分组[{}]: 更新单笔测算分组结果[{},{},{},{}]", new Object[] { this.jobFullName, Integer.valueOf(r1), Integer.valueOf(r2), Integer.valueOf(r3), Integer.valueOf(r4) });
/*      */     } else {
/*  217 */       int r = insertGroupResult(commonService, this.taskType, this.jobType, this.dataBatchNo);
/*      */       
/*  219 */       if (isGroupAnalyze()) {
/*  220 */         String table1 = null;
/*  221 */         String table2 = null;
/*  222 */         String table3 = null;
/*  223 */         if (this.jobType == JobType.NR_GROUP) {
/*  224 */           table1 = "RWA_ER_NR_Group";
/*  225 */           table2 = "RWA_ER_NR_ExpoGroup";
/*  226 */           table3 = "RWA_ER_NR_MitiGroup";
/*      */         } else {
/*  228 */           table1 = "RWA_ER_RE_Group";
/*  229 */           table2 = "RWA_ER_RE_ExpoGroup";
/*  230 */           table3 = "RWA_ER_RE_MitiGroup";
/*      */         } 
/*  232 */         commonService.analyzeTable(table1, this.dataBatchNo);
/*  233 */         commonService.analyzeTable(table2, this.dataBatchNo);
/*  234 */         commonService.analyzeTable(table3, this.dataBatchNo);
/*      */       } 
/*  236 */       this.jobLogDo.getCtCalculateCount().addAndGet(1);
/*  237 */       log.info("数据分组[{}]: 插入分组计算结果表[{}]", this.jobFullName, Integer.valueOf(r));
/*      */     } 
/*  239 */     this.jobService.putGroupJob(this.jobLogDo);
/*  240 */     this.jobService.syncStopGroupTask();
/*      */   }
/*      */   
/*      */   public void initTable(CommonService commonService, String tableName, String dataBatchNo) {
/*  244 */     Lock lock = this.lockService.getProcLock();
/*  245 */     lock.lock();
/*      */     
/*      */     try {
/*  248 */       if (commonService.initTable(tableName, dataBatchNo) == 0)
/*      */       {
/*  250 */         commonService.deleteByDataBatchNo(tableName, dataBatchNo);
/*      */       }
/*  252 */     } catch (Exception e) {
/*  253 */       throw new RuntimeException("初始化表异常， 请检查， [id=" + dataBatchNo + "][" + tableName + "]", e);
/*      */     } finally {
/*  255 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   public void clearTable(CommonService commonService, String tableName, String dataBatchNo) {
/*  260 */     Lock lock = this.lockService.getProcLock();
/*  261 */     lock.lock();
/*      */     
/*      */     try {
/*  264 */       if (commonService.clearTable(tableName, dataBatchNo) == 0)
/*      */       {
/*  266 */         commonService.deleteByDataBatchNo(tableName, dataBatchNo);
/*      */       }
/*  268 */     } catch (Exception e) {
/*  269 */       throw new RuntimeException("清理表异常， 请检查， [id=" + dataBatchNo + "][" + tableName + "]", e);
/*      */     } finally {
/*  271 */       lock.unlock();
/*      */     } 
/*      */   }
/*      */   
/*      */   public int insertExposureGroup(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  276 */     String sql = "insert into #{result_table} (data_batch_no, exposure_id, mitigation_count) select r.data_batch_no, r.exposure_id, count(1) as mitigation_count from #{from_table} r where r.data_batch_no = #{dataBatchNo} group by r.data_batch_no, r.exposure_id";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  281 */     String resultTable = null;
/*  282 */     String relevanceTable = null;
/*  283 */     if (RwaUtils.isSingle(taskType)) {
/*  284 */       resultTable = "RWA_ESR_GE_ExpoGroup";
/*  285 */       relevanceTable = "RWA_ESI_GE_EMRelevance";
/*  286 */     } else if (jobType == JobType.NR_GROUP) {
/*  287 */       resultTable = "RWA_ER_NR_ExpoGroup";
/*  288 */       relevanceTable = "RWA_EI_NR_EMRelevance";
/*      */     } else {
/*  290 */       resultTable = "RWA_ER_RE_ExpoGroup";
/*  291 */       relevanceTable = "RWA_EI_RE_EMRelevance";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  297 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("from_table", relevanceTable).setString("dataBatchNo", dataBatchNo).build();
/*  298 */     int r = commonService.insert(DataUtils.createSqlMap(sql));
/*      */     
/*  300 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  301 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*  303 */     return r;
/*      */   }
/*      */ 
/*      */   
/*      */   public int insertMitigationGroup(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  308 */     String sql = "insert into #{result_table}(data_batch_no, mitigation_id, mitigation_type, exposure_count, exposure_id) select t.data_batch_no, t.mitigation_id, t.mitigation_type, t.exposure_count, r2.exposure_id from (select r.data_batch_no, r.mitigation_id, r.mitigation_type, count(1) as exposure_count from #{from_table} r where r.data_batch_no = #{dataBatchNo} group by r.data_batch_no, r.mitigation_id, r.mitigation_type) t left join #{from_table} r2 on t.exposure_count = 1 and t.mitigation_id = r2.mitigation_id and r2.data_batch_no = #{dataBatchNo} ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  317 */     String resultTable = null;
/*  318 */     String relevanceTable = null;
/*  319 */     if (RwaUtils.isSingle(taskType)) {
/*  320 */       resultTable = "RWA_ESR_GE_MitiGroup";
/*  321 */       relevanceTable = "RWA_ESI_GE_EMRelevance";
/*  322 */     } else if (jobType == JobType.NR_GROUP) {
/*  323 */       resultTable = "RWA_ER_NR_MitiGroup";
/*  324 */       relevanceTable = "RWA_EI_NR_EMRelevance";
/*      */     } else {
/*  326 */       resultTable = "RWA_ER_RE_MitiGroup";
/*  327 */       relevanceTable = "RWA_EI_RE_EMRelevance";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  333 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("from_table", relevanceTable).setString("dataBatchNo", dataBatchNo).build();
/*  334 */     int r = commonService.insert(DataUtils.createSqlMap(sql));
/*      */     
/*  336 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  337 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*  339 */     return r;
/*      */   }
/*      */ 
/*      */   
/*      */   public int insertM2mExposureTmp(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  344 */     String sql = "insert into #{result_table} (data_batch_no, exposure_id) select #{dataBatchNo} as data_batch_no, t.exposure_id from (select distinct rm.exposure_id from #{table_relevance} rm join #{table_rm} m on m.data_batch_no = #{dataBatchNo} and rm.mitigation_id = m.mitigation_id where rm.data_batch_no = #{dataBatchNo} and m.exposure_count > 1) t";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  351 */     String resultTable = null;
/*  352 */     String relevanceTable = null;
/*  353 */     String mitigationTable = null;
/*  354 */     if (RwaUtils.isSingle(taskType)) {
/*  355 */       resultTable = "RWA_ESR_GE_ExpoGroup_TMP";
/*  356 */       relevanceTable = "RWA_ESI_GE_EMRelevance";
/*  357 */       mitigationTable = "RWA_ESR_GE_MitiGroup";
/*  358 */     } else if (jobType == JobType.NR_GROUP) {
/*  359 */       resultTable = "RWA_ER_NR_ExpoGroup_TMP";
/*  360 */       relevanceTable = "RWA_EI_NR_EMRelevance";
/*  361 */       mitigationTable = "RWA_ER_NR_MitiGroup";
/*      */     } else {
/*  363 */       resultTable = "RWA_ER_RE_ExpoGroup_TMP";
/*  364 */       relevanceTable = "RWA_EI_RE_EMRelevance";
/*  365 */       mitigationTable = "RWA_ER_RE_MitiGroup";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  372 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("table_relevance", relevanceTable).setTable("table_rm", mitigationTable).setString("dataBatchNo", dataBatchNo).build();
/*  373 */     int r = commonService.insert(sql);
/*  374 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  375 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*  377 */     return r;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, Object> selectAllM2m(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  385 */     String sql = "select r.exposure_id, r.mitigation_id from #{table_relevance} r join #{table_exposure} e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.data_batch_no = #{dataBatchNo} ";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  390 */     String countSql = "select r.exposure_id, r.mitigation_id from #{table_relevance} r where r.data_batch_no = #{dataBatchNo} ";
/*      */     
/*  392 */     String exposureTable = null;
/*  393 */     String relevanceTable = null;
/*  394 */     String mitigationTable = null;
/*  395 */     if (RwaUtils.isSingle(taskType)) {
/*  396 */       exposureTable = "RWA_ESR_GE_ExpoGroup_TMP";
/*  397 */       relevanceTable = "RWA_ESI_GE_EMRelevance";
/*  398 */       mitigationTable = "RWA_ESR_GE_MitiGroup";
/*  399 */     } else if (jobType == JobType.NR_GROUP) {
/*  400 */       exposureTable = "RWA_ER_NR_ExpoGroup_TMP";
/*  401 */       relevanceTable = "RWA_EI_NR_EMRelevance";
/*  402 */       mitigationTable = "RWA_ER_NR_MitiGroup";
/*      */     } else {
/*  404 */       exposureTable = "RWA_ER_RE_ExpoGroup_TMP";
/*  405 */       relevanceTable = "RWA_EI_RE_EMRelevance";
/*  406 */       mitigationTable = "RWA_ER_RE_MitiGroup";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  412 */     sql = SqlBuilder.create(sql).setTable("table_exposure", exposureTable).setTable("table_relevance", relevanceTable).setString("dataBatchNo", dataBatchNo).build();
/*      */ 
/*      */ 
/*      */     
/*  416 */     countSql = SqlBuilder.create(countSql).setTable("table_relevance", relevanceTable).setString("dataBatchNo", dataBatchNo).build();
/*      */     
/*  418 */     int exposureCount = getM2mExposureCount(commonService, countSql);
/*  419 */     Map<String, List<RelevanceDto>> exposureMap = new HashMap<>(exposureCount);
/*  420 */     int mitigationCount = getM2mMitigationCount(commonService, countSql);
/*  421 */     Map<String, List<RelevanceDto>> mitigationMap = new HashMap<>(mitigationCount);
/*  422 */     int relevanceCount = getRelevanceCount(commonService, countSql);
/*  423 */     Map<String, RelevanceDto> relevanceMap = new HashMap<>(relevanceCount);
/*      */     
/*  425 */     selectAllM2m(commonService.getJdbcTemplate(), sql, relevanceMap, exposureMap, mitigationMap);
/*      */     
/*  427 */     sql = "select rm.exposure_id, rm.mitigation_id from #{table_relevance} rm left join #{table_rm} m on m.data_batch_no = #{dataBatchNo} and rm.mitigation_id = m.mitigation_id where rm.data_batch_no = #{dataBatchNo} and m.exposure_count > 1";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  436 */     sql = SqlBuilder.create(sql).setTable("table_relevance", relevanceTable).setTable("table_rm", mitigationTable).setString("dataBatchNo", dataBatchNo).build();
/*      */     
/*  438 */     selectAllM2m(commonService.getJdbcTemplate(), sql, relevanceMap, exposureMap, mitigationMap);
/*      */     
/*  440 */     Map<String, Object> map = new HashMap<>();
/*  441 */     map.put("exposureMap", exposureMap);
/*  442 */     map.put("mitigationMap", mitigationMap);
/*  443 */     map.put("relevanceMap", relevanceMap);
/*  444 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void selectAllM2m(JdbcTemplate jdbcTemplate, String sql, Map<String, RelevanceDto> relevanceMap, Map<String, List<RelevanceDto>> exposureMap, Map<String, List<RelevanceDto>> mitigationMap) {
/*  451 */     jdbcTemplate.query(sql, (RowCallbackHandler)new Object(this, relevanceMap, exposureMap, mitigationMap));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public int getM2mExposureCount(CommonService commonService, String sql) {
/*  483 */     sql = "select count(distinct exposure_id) AS CNT from (" + sql + ") t";
/*  484 */     return commonService.getInt(DataUtils.createSqlMap(sql));
/*      */   }
/*      */   
/*      */   public int getM2mMitigationCount(CommonService commonService, String sql) {
/*  488 */     sql = "select count(distinct mitigation_id) AS CNT from (" + sql + ") t";
/*  489 */     return commonService.getInt(DataUtils.createSqlMap(sql));
/*      */   }
/*      */   
/*      */   public int getRelevanceCount(CommonService commonService, String sql) {
/*  493 */     sql = "select count(*) AS CNT from (" + sql + ") t";
/*  494 */     return commonService.getInt(DataUtils.createSqlMap(sql));
/*      */   }
/*      */   
/*      */   public int batchInsertExposureGroup(CommonService commonService, TaskType taskType, JobType jobType, Collection<RelevanceDto> resultList, int exposureSize, String dataBatchNo) {
/*  498 */     if (CollUtil.isEmpty(resultList)) {
/*  499 */       return 0;
/*      */     }
/*  501 */     if (exposureSize == 0) {
/*  502 */       throw new RuntimeException("[" + jobType.getCode() + "]插入暴露分组结果异常， 暴露数为0");
/*      */     }
/*      */     
/*  505 */     Map<String, RelevanceDto> map = new HashMap<>(exposureSize);
/*  506 */     for (RelevanceDto relevanceDto : resultList) {
/*  507 */       if (StrUtil.isNotEmpty(relevanceDto.getGroupId())) {
/*  508 */         map.putIfAbsent(relevanceDto.getExposureId(), relevanceDto);
/*      */       }
/*      */     } 
/*  511 */     String sql = "insert into #{result_table} (data_batch_no, exposure_id, group_id) values (?, ?, ?)";
/*  512 */     String resultTable = null;
/*  513 */     String exposureTable = null;
/*  514 */     if (RwaUtils.isSingle(taskType)) {
/*  515 */       resultTable = "RWA_ESR_GE_ExpoGroup_TMP";
/*  516 */       exposureTable = "RWA_ESR_GE_ExpoGroup";
/*  517 */     } else if (jobType == JobType.NR_GROUP) {
/*  518 */       resultTable = "RWA_ER_NR_ExpoGroup_TMP";
/*  519 */       exposureTable = "RWA_ER_NR_ExpoGroup";
/*      */     } else {
/*  521 */       resultTable = "RWA_ER_RE_ExpoGroup_TMP";
/*  522 */       exposureTable = "RWA_ER_RE_ExpoGroup";
/*      */     } 
/*      */     
/*  525 */     initTable(commonService, resultTable, dataBatchNo);
/*  526 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).build();
/*  527 */     int[][] result = commonService.getJdbcTemplate().batchUpdate(sql, map.values(), 1000, (ParameterizedPreparedStatementSetter)new Object(this, dataBatchNo));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  535 */     log.debug("数据分组[{}]: 多对多暴露分组结果写入[{}]", this.jobFullName, Integer.valueOf(result.length));
/*      */     
/*  537 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  538 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*      */     
/*  541 */     String prefix = "O" + getPrefix();
/*  542 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  546 */         sql = "insert into #{result_table} (data_batch_no, exposure_id, group_id) select e.data_batch_no, e.exposure_id,  concat(#{prefix}, TRIM(TO_CHAR(row_number() over(order by e.exposure_id), '0000000000'))) as group_id from #{exposureTable} e where e.data_batch_no = #{dataBatchNo} and not exists(select 1 from #{result_table} t where t.data_batch_no = #{dataBatchNo} and t.exposure_id = e.exposure_id)";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/*  555 */         sql = "insert into #{result_table} (data_batch_no, exposure_id, group_id) select e.data_batch_no, e.exposure_id,  concat(#{prefix}, LPAD(row_number() over(order by e.exposure_id), 10, '0')) as group_id from #{exposureTable} e where e.data_batch_no = #{dataBatchNo} and not exists(select 1 from #{result_table} t where t.data_batch_no = #{dataBatchNo} and t.exposure_id = e.exposure_id)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  562 */         if (StrUtil.equals(commonService.getSsv(), "o")) {
/*  563 */           sql = "insert into #{result_table} (data_batch_no, exposure_id, group_id) select t.data_batch_no, t.exposure_id, concat(#{prefix}, LPAD((@row_number := @row_number + 1), 10, '0')) as group_id  from (select e.data_batch_no, e.exposure_id from #{exposureTable} e where e.data_batch_no = #{dataBatchNo} and e.exposure_id not in (select t.exposure_id from #{result_table} t where t.data_batch_no = #{dataBatchNo}) ) t, (select @row_number := 0) b order by t.exposure_id ";
/*      */         }
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  576 */         sql = "insert into #{result_table} (data_batch_no, exposure_id, group_id) select e.data_batch_no, e.exposure_id,  concat(#{prefix}, LPAD(row_number() over(order by e.exposure_id)::text, 10, '0')) as group_id from #{exposureTable} e where e.data_batch_no = #{dataBatchNo} and not exists(select 1 from #{result_table} t where t.data_batch_no = #{dataBatchNo} and t.exposure_id = e.exposure_id)";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/*  585 */         throw new ParamConfigException("非法数据库类型");
/*      */     } 
/*  587 */     int r = commonService.insert(SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("exposureTable", exposureTable)
/*  588 */         .setString("dataBatchNo", dataBatchNo).setString("prefix", prefix).build());
/*  589 */     log.debug("数据分组[{}]: 一对多暴露分组结果写入[{}]", this.jobFullName, Integer.valueOf(r));
/*      */     
/*  591 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  592 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*  594 */     return map.size() + r;
/*      */   }
/*      */   
/*      */   public int batchUpdateExposureGroup(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  598 */     String sql = null;
/*  599 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  603 */         sql = "merge into #{result_table} g using #{tmp_table} t on (g.exposure_id = t.exposure_id and g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo}) when matched then update set g.group_id = t.group_id where g.data_batch_no = #{dataBatchNo}";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/*  612 */         sql = "update #{result_table} g, #{tmp_table} t set g.group_id = t.group_id where g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo} and g.exposure_id = t.exposure_id";
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  618 */         sql = "update #{result_table} g set group_id = t.group_id from #{tmp_table} t where g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo} and g.exposure_id = t.exposure_id";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/*  625 */         throw new ParamConfigException("非法数据库类型");
/*      */     } 
/*  627 */     String resultTable = "";
/*  628 */     String tmpTable = "";
/*  629 */     if (RwaUtils.isSingle(taskType)) {
/*  630 */       resultTable = "RWA_ESR_GE_ExpoGroup";
/*  631 */       tmpTable = "RWA_ESR_GE_ExpoGroup_TMP";
/*  632 */     } else if (jobType == JobType.NR_GROUP) {
/*  633 */       resultTable = "RWA_ER_NR_ExpoGroup";
/*  634 */       tmpTable = "RWA_ER_NR_ExpoGroup_TMP";
/*      */     } else {
/*  636 */       resultTable = "RWA_ER_RE_ExpoGroup";
/*  637 */       tmpTable = "RWA_ER_RE_ExpoGroup_TMP";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  643 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("tmp_table", tmpTable).setString("dataBatchNo", dataBatchNo).build();
/*  644 */     int r = commonService.update(sql);
/*      */     
/*  646 */     clearTable(commonService, tmpTable, dataBatchNo);
/*  647 */     return r;
/*      */   }
/*      */   
/*      */   public int batchInsertMitigationGroup(CommonService commonService, TaskType taskType, JobType jobType, List<RelevanceDto> resultList, int mitigationSize, String dataBatchNo) {
/*  651 */     if (CollUtil.isEmpty(resultList)) {
/*  652 */       return 0;
/*      */     }
/*  654 */     if (mitigationSize == 0) {
/*  655 */       throw new RuntimeException("[" + jobType.getCode() + "]更新分组结果异常， 缓释物数为0");
/*      */     }
/*  657 */     Map<String, RelevanceDto> map = new HashMap<>(mitigationSize);
/*  658 */     for (RelevanceDto relevanceDto : resultList) {
/*  659 */       if (StrUtil.isNotEmpty(relevanceDto.getGroupId())) {
/*  660 */         map.putIfAbsent(relevanceDto.getMitigationId(), relevanceDto);
/*      */       }
/*      */     } 
/*  663 */     String sql = "insert into #{result_table} (data_batch_no, mitigation_id, group_id) values (?, ?, ?)";
/*  664 */     String resultTable = null;
/*  665 */     String mitigationTable = null;
/*  666 */     String exposureTable = "";
/*  667 */     if (RwaUtils.isSingle(taskType)) {
/*  668 */       resultTable = "RWA_ESR_GE_MitiGroup_TMP";
/*  669 */       mitigationTable = "RWA_ESR_GE_MitiGroup";
/*  670 */       exposureTable = "RWA_ESR_GE_ExpoGroup";
/*  671 */     } else if (jobType == JobType.NR_GROUP) {
/*  672 */       resultTable = "RWA_ER_NR_MitiGroup_TMP";
/*  673 */       mitigationTable = "RWA_ER_NR_MitiGroup";
/*  674 */       exposureTable = "RWA_ER_NR_ExpoGroup";
/*      */     } else {
/*  676 */       resultTable = "RWA_ER_RE_MitiGroup_TMP";
/*  677 */       mitigationTable = "RWA_ER_RE_MitiGroup";
/*  678 */       exposureTable = "RWA_ER_RE_ExpoGroup";
/*      */     } 
/*  680 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).build();
/*  681 */     int[][] result = commonService.getJdbcTemplate().batchUpdate(sql, map.values(), 1000, (ParameterizedPreparedStatementSetter)new Object(this, dataBatchNo));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  690 */     log.debug("数据分组[{}]: 多对多缓释分组结果写入[{}]", this.jobFullName, Integer.valueOf(result.length));
/*      */     
/*  692 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  693 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*      */     
/*  696 */     sql = "insert into #{result_table} (data_batch_no, mitigation_id, group_id) select m.data_batch_no, m.mitigation_id, e.group_id from #{mitigationTable} m left join #{exposureTable} e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = m.exposure_id where m.data_batch_no = #{dataBatchNo} and not exists(select 1 from #{result_table} t where t.data_batch_no = #{dataBatchNo} and t.mitigation_id = m.mitigation_id)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  702 */     if (StrUtil.equals(commonService.getSsv(), "o")) {
/*  703 */       sql = "insert into #{result_table} (data_batch_no, mitigation_id, group_id) select m.data_batch_no, m.mitigation_id, e.group_id from #{mitigationTable} m left join #{exposureTable} e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = m.exposure_id where m.data_batch_no = #{dataBatchNo} and m.mitigation_id not in (select t.mitigation_id from #{result_table} t where t.data_batch_no = #{dataBatchNo} )";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  710 */     int r = commonService.insert(SqlBuilder.create(sql)
/*  711 */         .setTable("result_table", resultTable)
/*  712 */         .setTable("exposureTable", exposureTable)
/*  713 */         .setTable("mitigationTable", mitigationTable)
/*  714 */         .setString("dataBatchNo", dataBatchNo).build());
/*  715 */     log.debug("数据分组[{}]: 一对多缓释分组结果写入[{}]", this.jobFullName, Integer.valueOf(r));
/*      */     
/*  717 */     if (!RwaUtils.isSingle(taskType) && isGroupAnalyze()) {
/*  718 */       commonService.analyzeTable(resultTable, dataBatchNo);
/*      */     }
/*  720 */     return map.size() + r;
/*      */   }
/*      */   
/*      */   public int batchUpdateMitigationGroup(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  724 */     String sql = null;
/*  725 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  729 */         sql = "merge into #{result_table} g using #{tmp_table} t on (g.mitigation_id = t.mitigation_id and g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo}) when matched then update set g.group_id = t.group_id where g.data_batch_no = #{dataBatchNo}";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 4:
/*  738 */         sql = "update #{result_table} g, #{tmp_table} t set g.group_id = t.group_id where g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo} and g.mitigation_id = t.mitigation_id";
/*      */         break;
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*  744 */         sql = "update #{result_table} g set group_id = t.group_id from #{tmp_table} t where g.data_batch_no = #{dataBatchNo} and t.data_batch_no = #{dataBatchNo} and g.mitigation_id = t.mitigation_id";
/*      */         break;
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       default:
/*  751 */         throw new ParamConfigException("非法数据库类型");
/*      */     } 
/*  753 */     String resultTable = "";
/*  754 */     String tmpTable = "";
/*  755 */     if (RwaUtils.isSingle(taskType)) {
/*  756 */       resultTable = "RWA_ESR_GE_MitiGroup";
/*  757 */       tmpTable = "RWA_ESR_GE_MitiGroup_TMP";
/*  758 */     } else if (jobType == JobType.NR_GROUP) {
/*  759 */       resultTable = "RWA_ER_NR_MitiGroup";
/*  760 */       tmpTable = "RWA_ER_NR_MitiGroup_TMP";
/*      */     } else {
/*  762 */       resultTable = "RWA_ER_RE_MitiGroup";
/*  763 */       tmpTable = "RWA_ER_RE_MitiGroup_TMP";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  769 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("tmp_table", tmpTable).setString("dataBatchNo", dataBatchNo).build();
/*  770 */     int r = commonService.update(sql);
/*      */     
/*  772 */     clearTable(commonService, tmpTable, dataBatchNo);
/*  773 */     return r;
/*      */   }
/*      */   
/*      */   public int updateSingleExposureGroupResult(CommonService commonService, JobType jobType, String dataBatchNo) {
/*  777 */     String sql = null;
/*  778 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  782 */         sql = "merge into RWA_ESI_GE_Exposure e using RWA_ESR_GE_ExpoGroup g on (e.exposure_id = g.exposure_id and e.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo}) when matched then update set e.group_id = g.group_id where e.data_batch_no = #{dataBatchNo}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  806 */         return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 4: sql = "update RWA_ESI_GE_Exposure e, RWA_ESR_GE_ExpoGroup g set e.group_id = g.group_id where e.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 5: sql = "update RWA_ESI_GE_Exposure e set group_id = g.group_id from RWA_ESR_GE_ExpoGroup g where e.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());
/*      */     } 
/*      */     throw new ParamConfigException("非法数据库类型");
/*      */   } public int updateSingleRelevanceGroupResult(CommonService commonService, JobType jobType, String dataBatchNo) {
/*  810 */     String sql = null;
/*  811 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  815 */         sql = "merge into RWA_ESI_GE_EMRelevance r using RWA_ESR_GE_MitiGroup g on (r.mitigation_id = g.mitigation_id and r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo}) when matched then update set r.group_id = g.group_id where r.data_batch_no = #{dataBatchNo}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  838 */         return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 4: sql = "update RWA_ESI_GE_EMRelevance r, RWA_ESR_GE_MitiGroup g set r.group_id = g.group_id where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.mitigation_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 5: sql = "update RWA_ESI_GE_EMRelevance r set group_id = g.group_id from RWA_ESR_GE_MitiGroup g where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.mitigation_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());
/*      */     } 
/*      */     throw new ParamConfigException("非法数据库类型");
/*      */   } public int updateSingleGuaranteeGroupResult(CommonService commonService, JobType jobType, String dataBatchNo) {
/*  842 */     String sql = null;
/*  843 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  847 */         sql = "merge into RWA_ESI_GE_Guarantee r using RWA_ESR_GE_MitiGroup g on (r.guarantee_id = g.mitigation_id and r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo}) when matched then update set r.group_id = g.group_id, r.exposure_count = g.exposure_count, r.exposure_id = g.exposure_id where r.data_batch_no = #{dataBatchNo}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  871 */         return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 4: sql = "update RWA_ESI_GE_Guarantee r, RWA_ESR_GE_MitiGroup g set r.group_id = g.group_id, r.exposure_count = g.exposure_count, r.exposure_id = g.exposure_id where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.guarantee_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 5: sql = "update RWA_ESI_GE_Guarantee r set group_id = g.group_id, exposure_count = g.exposure_count, exposure_id = g.exposure_id from RWA_ESR_GE_MitiGroup g where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.guarantee_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());
/*      */     } 
/*      */     throw new ParamConfigException("非法数据库类型");
/*      */   } public int updateSingleCollateralGroupResult(CommonService commonService, JobType jobType, String dataBatchNo) {
/*  875 */     String sql = null;
/*  876 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*  880 */         sql = "merge into RWA_ESI_GE_Collateral r using RWA_ESR_GE_MitiGroup g on (r.collateral_id = g.mitigation_id and r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo}) when matched then update set r.group_id = g.group_id, r.exposure_count = g.exposure_count, r.exposure_id = g.exposure_id where r.data_batch_no = #{dataBatchNo}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  904 */         return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 4: sql = "update RWA_ESI_GE_Collateral r, RWA_ESR_GE_MitiGroup g set r.group_id = g.group_id, r.exposure_count = g.exposure_count, r.exposure_id = g.exposure_id where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.collateral_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());case 5: sql = "update RWA_ESI_GE_Collateral r set group_id = g.group_id, exposure_count = g.exposure_count, exposure_id = g.exposure_id from RWA_ESR_GE_MitiGroup g where r.data_batch_no = #{dataBatchNo} and g.data_batch_no = #{dataBatchNo} and r.collateral_id = g.mitigation_id"; return commonService.update(SqlBuilder.create(sql).setString("dataBatchNo", dataBatchNo).build());
/*      */     } 
/*      */     throw new ParamConfigException("非法数据库类型");
/*      */   } public int insertGroupResult(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  908 */     return insertGroupResultOfGroup(commonService, taskType, jobType, dataBatchNo) + insertGroupResultOfExposure(commonService, taskType, jobType, dataBatchNo);
/*      */   }
/*      */ 
/*      */   
/*      */   public int insertGroupResultOfGroup(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  913 */     String sql = "insert into #{result_table}(data_batch_no, calculate_type, sort_no, calculate_id, calculate_num) select #{dataBatchNo}, #{calculateType}, row_number() over(order by t.calculate_id) as sort_no, t.calculate_id, t.calculate_num from (select g.group_id as calculate_id, sum(mitigation_count) as calculate_num from #{group_table} g where g.data_batch_no = #{dataBatchNo} group by g.group_id) t";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  922 */     if (StrUtil.equals(commonService.getSsv(), "o")) {
/*  923 */       sql = "insert into #{result_table}(data_batch_no, calculate_type, sort_no, calculate_id, calculate_num) select #{dataBatchNo}, #{calculateType}, (@row_number := @row_number + 1) AS sort_no, t.calculate_id, t.calculate_num from (select g.group_id as calculate_id, sum(mitigation_count) as calculate_num from #{group_table} g where g.data_batch_no = #{dataBatchNo} group by g.group_id) t, (select @row_number := 0) b order by t.calculate_id";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  933 */     String resultTable = null;
/*  934 */     String groupTable = null;
/*  935 */     if (jobType == JobType.NR_GROUP) {
/*  936 */       resultTable = "RWA_ER_NR_Group";
/*  937 */       groupTable = "RWA_ER_NR_ExpoGroup";
/*      */     } else {
/*  939 */       resultTable = "RWA_ER_RE_Group";
/*  940 */       groupTable = "RWA_ER_RE_ExpoGroup";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  947 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("group_table", groupTable).setString("dataBatchNo", dataBatchNo).setString("calculateType", UnionType.GROUP.getCode()).build();
/*  948 */     return commonService.insert(DataUtils.createSqlMap(sql));
/*      */   }
/*      */ 
/*      */   
/*      */   public int insertGroupResultOfExposure(CommonService commonService, TaskType taskType, JobType jobType, String dataBatchNo) {
/*  953 */     String sql = "insert into #{result_table}(data_batch_no, calculate_type, sort_no, calculate_id, calculate_num) select #{dataBatchNo}, #{calculateType}, row_number() over(order by t.calculate_id) as sort_no, t.calculate_id, t.calculate_num from (select c.exposure_id as calculate_id, 1 as calculate_num from #{exposure_table} c where c.data_batch_no = #{dataBatchNo} and not exists( select 1 from #{group_table} g where g.data_batch_no = #{dataBatchNo} and g.exposure_id = c.exposure_id ) ) t";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  962 */     if (StrUtil.equals(commonService.getSsv(), "o")) {
/*  963 */       sql = "insert into #{result_table}(data_batch_no, calculate_type, sort_no, calculate_id, calculate_num) select #{dataBatchNo}, #{calculateType}, (@row_number := @row_number + 1) AS sort_no, t.calculate_id, t.calculate_num from (select c.exposure_id as calculate_id, 1 as calculate_num from #{exposure_table} c where c.data_batch_no = #{dataBatchNo} and c.exposure_id not in (select g.exposure_id from #{group_table} g where g.data_batch_no = #{dataBatchNo}) ) t, (select @row_number := 0) b order by t.calculate_id";
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  974 */     String resultTable = null;
/*  975 */     String exposureTable = null;
/*  976 */     String groupTable = null;
/*  977 */     if (jobType == JobType.NR_GROUP) {
/*  978 */       resultTable = "RWA_ER_NR_Group";
/*  979 */       exposureTable = "RWA_EI_NR_Exposure";
/*  980 */       groupTable = "RWA_ER_NR_ExpoGroup";
/*      */     } else {
/*  982 */       resultTable = "RWA_ER_RE_Group";
/*  983 */       exposureTable = "RWA_EI_RE_Exposure";
/*  984 */       groupTable = "RWA_ER_RE_ExpoGroup";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  992 */     sql = SqlBuilder.create(sql).setTable("result_table", resultTable).setTable("exposure_table", exposureTable).setTable("group_table", groupTable).setString("dataBatchNo", dataBatchNo).setString("calculateType", UnionType.EXPOSURE.getCode()).build();
/*  993 */     return commonService.insert(DataUtils.createSqlMap(sql));
/*      */   }
/*      */   
/*      */   public List<RelevanceDto> groupData(Map<String, List<RelevanceDto>> exposureMap, Map<String, List<RelevanceDto>> mitigationMap, String prefix) {
/*  997 */     List<RelevanceDto> groupList = new ArrayList<>();
/*  998 */     int num = 0;
/*      */     
/* 1000 */     while (exposureMap.keySet().size() > 0) {
/* 1001 */       JobUtils.checkStopGroupTask(this.dataBatchNo, this.jobFullName + "-4");
/*      */       
/* 1003 */       Set<RelevanceDto> groupSet = groupDataByExposureId(exposureMap, mitigationMap, new HashSet<>(), prefix, ++num, null);
/* 1004 */       groupList.addAll(groupSet);
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1009 */     return groupList;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<RelevanceDto> groupDataByExposureId(@NotNull Map<String, List<RelevanceDto>> exposureMap, @NotNull Map<String, List<RelevanceDto>> mitigationMap, @NotNull Set<RelevanceDto> groupSet, String prefix, int groupNum, String exposureId) {
/* 1015 */     if (!StringUtils.hasLength(exposureId))
/*      */     {
/* 1017 */       exposureId = exposureMap.keySet().iterator().next();
/*      */     }
/* 1019 */     if (!StringUtils.hasLength(exposureId)) {
/* 1020 */       return groupSet;
/*      */     }
/*      */     
/* 1023 */     List<RelevanceDto> list = exposureMap.get(exposureId);
/* 1024 */     if (CollUtil.isEmpty(list)) {
/* 1025 */       return groupSet;
/*      */     }
/*      */     
/* 1028 */     exposureMap.remove(exposureId);
/*      */     
/* 1030 */     int n = groupSet.size();
/*      */     
/* 1032 */     for (RelevanceDto g : list) {
/* 1033 */       groupSet.add(g);
/* 1034 */       if (!StringUtils.hasLength(g.getGroupId())) {
/* 1035 */         g.setGroupId(getGroupId(prefix, groupNum));
/*      */       }
/*      */ 
/*      */       
/* 1039 */       if (groupSet.size() > n) {
/*      */         
/* 1041 */         n = groupSet.size();
/*      */         
/* 1043 */         groupSet = groupDataByMitigationId(exposureMap, mitigationMap, groupSet, prefix, groupNum, g.getMitigationId());
/*      */       } 
/*      */     } 
/* 1046 */     return groupSet;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   private Set<RelevanceDto> groupDataByMitigationId(@NotNull Map<String, List<RelevanceDto>> exposureMap, @NotNull Map<String, List<RelevanceDto>> mitigationMap, @NotNull Set<RelevanceDto> groupSet, String prefix, int groupNum, String mitigationId) {
/* 1052 */     if (!StringUtils.hasLength(mitigationId))
/*      */     {
/* 1054 */       mitigationId = mitigationMap.keySet().iterator().next();
/*      */     }
/* 1056 */     if (!StringUtils.hasLength(mitigationId)) {
/* 1057 */       return groupSet;
/*      */     }
/*      */     
/* 1060 */     List<RelevanceDto> list = mitigationMap.get(mitigationId);
/* 1061 */     if (CollUtil.isEmpty(list)) {
/* 1062 */       return groupSet;
/*      */     }
/*      */     
/* 1065 */     mitigationMap.remove(mitigationId);
/*      */     
/* 1067 */     int n = groupSet.size();
/*      */     
/* 1069 */     for (RelevanceDto g : list) {
/* 1070 */       groupSet.add(g);
/* 1071 */       if (!StringUtils.hasLength(g.getGroupId())) {
/* 1072 */         g.setGroupId(getGroupId(prefix, groupNum));
/*      */       }
/* 1074 */       if (groupSet.size() > n) {
/*      */         
/* 1076 */         n = groupSet.size();
/*      */         
/* 1078 */         groupSet = groupDataByExposureId(exposureMap, mitigationMap, groupSet, prefix, groupNum, g.getExposureId());
/*      */       } 
/*      */     } 
/* 1081 */     return groupSet;
/*      */   }
/*      */ 
/*      */   
/*      */   public String getGroupId(@NotNull String prefix, int groupNum) {
/* 1086 */     return prefix + this.decimalFormat.format(groupNum);
/*      */   }
/*      */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\GroupJob.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */