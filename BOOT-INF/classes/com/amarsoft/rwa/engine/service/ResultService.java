/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.CvaType;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureBelong;
/*     */ import com.amarsoft.rwa.engine.constant.JobType;
/*     */ import com.amarsoft.rwa.engine.constant.SpecExposureTypeWa;
/*     */ import com.amarsoft.rwa.engine.constant.TaskType;
/*     */ import com.amarsoft.rwa.engine.entity.CreditDimResultDo;
/*     */ import com.amarsoft.rwa.engine.entity.CreditMitigationResultDo;
/*     */ import com.amarsoft.rwa.engine.entity.CreditOrgRwaDo;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRwaDo;
/*     */ import com.amarsoft.rwa.engine.entity.ExcDataDo;
/*     */ import com.amarsoft.rwa.engine.entity.RiskDataPeriodDo;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskInfoDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import java.lang.invoke.SerializedLambda;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collection;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.locks.Lock;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
/*     */ 
/*     */ @Service
/*     */ public class ResultService {
/*  35 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.ResultService.class);
/*     */   
/*     */   @Autowired
/*     */   private CreditRwaMapper creditRwaMapper;
/*     */   
/*     */   @Autowired
/*     */   private CreditOrgRwaMapper creditOrgRwaMapper;
/*     */   
/*     */   @Autowired
/*     */   private CreditDimResultMapper creditDimResultMapper;
/*     */   @Autowired
/*     */   private CreditMitigationResultMapper mitigationResultMapper;
/*     */   @Autowired
/*     */   private RiskDataPeriodMapper riskDataPeriodMapper;
/*     */   @Autowired
/*     */   private ExcDataMapper excDataMapper;
/*     */   @Autowired
/*     */   private LockService lockService;
/*     */   @Autowired
/*     */   private CommonService commonService;
/*     */   public static Map<String, String> resultTableColumnMap;
/*     */   
/*     */   public void initTableByResult(String id, String tableName) {
/*  58 */     Lock lock = this.lockService.getProcLock();
/*  59 */     lock.lock();
/*     */     
/*     */     try {
/*  62 */       if (this.commonService.initTable(tableName, id) == 0)
/*     */       {
/*  64 */         this.commonService.deleteByResultNo(tableName, id);
/*     */       }
/*  66 */     } catch (Exception e) {
/*  67 */       throw new RuntimeException("初始化表异常， 请检查， [id=" + id + "][" + tableName + "]", e);
/*     */     } finally {
/*  69 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initTableByData(String id, String tableName) {
/*  74 */     Lock lock = this.lockService.getProcLock();
/*  75 */     lock.lock();
/*     */     
/*     */     try {
/*  78 */       if (this.commonService.initTable(tableName, id) == 0)
/*     */       {
/*  80 */         this.commonService.deleteByDataBatchNo(tableName, id);
/*     */       }
/*  82 */     } catch (Exception e) {
/*  83 */       throw new RuntimeException("初始化表异常， 请检查， [id=" + id + "][" + tableName + "]", e);
/*     */     } finally {
/*  85 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initTable(String id, String... tableNames) {
/*  90 */     for (String tableName : tableNames) {
/*  91 */       initTableByResult(id, tableName);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteByResultNo(String resultNo, String... tableNames) {
/*  96 */     for (String tableName : tableNames) {
/*  97 */       this.commonService.deleteByResultNo(tableName, resultNo);
/*     */     }
/*     */   }
/*     */   
/*     */   public void deleteByDataNo(String dataNo, String... tableNames) {
/* 102 */     for (String tableName : tableNames) {
/* 103 */       this.commonService.deleteByDataBatchNo(tableName, dataNo);
/*     */     }
/*     */   }
/*     */   
/*     */   public void createSubTable(String jobId, JobType jobType) {
/* 108 */     Lock lock = this.lockService.getProcLock();
/* 109 */     lock.lock();
/*     */     try {
/* 111 */       if (jobType == JobType.NR) {
/* 112 */         createSubTableByNr(jobId);
/* 113 */       } else if (jobType == JobType.RE) {
/* 114 */         createSubTableByRe(jobId);
/*     */       } else {
/* 116 */         throw new JobParameterException("仅非零售及零售能创建子表");
/*     */       } 
/* 118 */     } catch (Exception e) {
/* 119 */       throw new RuntimeException("创建临时子表异常： jobType=" + jobType + " jobId=" + jobId, e);
/*     */     } finally {
/* 121 */       lock.unlock();
/*     */     } 
/*     */   }
/*     */   
/*     */   public void createSubTableByNr(String jobId) {
/* 126 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_nrd")).setTable("table", "T_NRD_" + jobId).build());
/* 127 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_nre")).setTable("table", "T_NRE_" + jobId).build());
/* 128 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_nrm")).setTable("table", "T_NRM_" + jobId).build());
/* 129 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_amp")).setTable("table", "T_AMP_" + jobId).build());
/*     */   }
/*     */   
/*     */   public void createSubTableByRe(String jobId) {
/* 133 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_red")).setTable("table", "T_RED_" + jobId).build());
/* 134 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_ree")).setTable("table", "T_REE_" + jobId).build());
/* 135 */     this.commonService.execute(SqlBuilder.create(resultTableColumnMap.get("t_rem")).setTable("table", "T_REM_" + jobId).build());
/*     */   }
/*     */   
/*     */   public void insertResultTable(String tempTable, String resultTable) {
/*     */     try {
/* 140 */       String columns = getResultTableColumns(resultTable);
/* 141 */       String sql = "insert into #{resultTable}(#{resultColumns}) select #{resultColumns} from #{tempTable}";
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 146 */       sql = SqlBuilder.create(sql).setTable("resultTable", resultTable).setTable("tempTable", tempTable).setKeyword("resultColumns", columns).build();
/* 147 */       this.commonService.execute(sql);
/* 148 */     } catch (Exception e) {
/* 149 */       log.warn("临时结果表[" + tempTable + "]数据插入正式结果表[" + resultTable + "]异常！", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public String getResultTableColumns(String resultTable) {
/* 154 */     String cols = resultTableColumnMap.get(resultTable.toLowerCase());
/* 155 */     if (StrUtil.isEmpty(cols)) {
/* 156 */       throw new RuntimeException("异常代码！结果表字段配置异常！resultTable=" + resultTable);
/*     */     }
/* 158 */     return cols;
/*     */   }
/*     */   
/*     */   public void dropTempTable(String tempTable) {
/*     */     try {
/* 163 */       String dropSql = "drop table " + tempTable;
/* 164 */       this.commonService.execute(dropSql);
/* 165 */     } catch (Exception e) {
/*     */       
/* 167 */       log.warn("删除临时子表异常： table=" + tempTable, e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void temp2Result(JobType jobType, String jobId) {
/* 172 */     if (jobType == JobType.NR) {
/* 173 */       insertResultTable("T_NRD_" + jobId, "rwa_er_nr_detail");
/* 174 */       insertResultTable("T_NRE_" + jobId, "rwa_er_nr_exposure");
/* 175 */       insertResultTable("T_NRM_" + jobId, "rwa_er_nr_mitigation");
/* 176 */       insertResultTable("T_AMP_" + jobId, "rwa_er_amp_exposure");
/* 177 */     } else if (jobType == JobType.RE) {
/* 178 */       insertResultTable("T_RED_" + jobId, "rwa_er_re_detail");
/* 179 */       insertResultTable("T_REE_" + jobId, "rwa_er_re_exposure");
/* 180 */       insertResultTable("T_REM_" + jobId, "rwa_er_re_mitigation");
/*     */     } 
/*     */   }
/*     */   
/*     */   public void dropTempResultTable(String jobType, String jobId) {
/* 185 */     if (StrUtil.equals(jobType, JobType.NR.getCode())) {
/* 186 */       dropTempTable("T_NRD_" + jobId);
/* 187 */       dropTempTable("T_NRE_" + jobId);
/* 188 */       dropTempTable("T_NRM_" + jobId);
/* 189 */     } else if (StrUtil.equals(jobType, JobType.RE.getCode())) {
/* 190 */       dropTempTable("T_RED_" + jobId);
/* 191 */       dropTempTable("T_REE_" + jobId);
/* 192 */       dropTempTable("T_REM_" + jobId);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initSingleGroupResult(String dataBatchNo) {
/* 197 */     deleteByDataNo(dataBatchNo, new String[] { "RWA_ESR_GE_ExpoGroup", "RWA_ESR_GE_ExpoGroup_TMP", "RWA_ESR_GE_MitiGroup", "RWA_ESR_GE_MitiGroup_TMP" });
/* 198 */     initSingleGroupId("RWA_ESI_GE_Exposure", dataBatchNo);
/* 199 */     initSingleGroupId("RWA_ESI_GE_EMRelevance", dataBatchNo);
/* 200 */     initSingleMitigationGroupId("RWA_ESI_GE_Collateral", dataBatchNo);
/* 201 */     initSingleMitigationGroupId("RWA_ESI_GE_Guarantee", dataBatchNo);
/*     */   }
/*     */   
/*     */   public void initSingleGroupId(String tableName, String dataBatchNo) {
/* 205 */     String sql = "update #{tableName} set group_id = null where data_batch_no = #{dataBatchNo}";
/* 206 */     this.commonService.execute(SqlBuilder.create(sql).setTable("tableName", tableName).setString("dataBatchNo", dataBatchNo).build());
/*     */   }
/*     */   
/*     */   public void initSingleMitigationGroupId(String tableName, String dataBatchNo) {
/* 210 */     String sql = "update #{tableName} set group_id = null, exposure_count = null, exposure_id = null where data_batch_no = #{dataBatchNo}";
/* 211 */     this.commonService.execute(SqlBuilder.create(sql).setTable("tableName", tableName).setString("dataBatchNo", dataBatchNo).build());
/*     */   }
/*     */   
/*     */   public void initNrGroupResult(TaskType taskType, String dataBatchNo) {
/* 215 */     initTableByData(dataBatchNo, "RWA_ER_NR_ExpoGroup");
/* 216 */     initTableByData(dataBatchNo, "RWA_ER_NR_ExpoGroup_TMP");
/* 217 */     initTableByData(dataBatchNo, "RWA_ER_NR_MitiGroup");
/* 218 */     initTableByData(dataBatchNo, "RWA_ER_NR_MitiGroup_TMP");
/* 219 */     initTableByData(dataBatchNo, "RWA_ER_NR_Group");
/*     */   }
/*     */   
/*     */   public void initReGroupResult(TaskType taskType, String dataBatchNo) {
/* 223 */     initTableByData(dataBatchNo, "RWA_ER_RE_ExpoGroup");
/* 224 */     initTableByData(dataBatchNo, "RWA_ER_RE_ExpoGroup_TMP");
/* 225 */     initTableByData(dataBatchNo, "RWA_ER_RE_MitiGroup");
/* 226 */     initTableByData(dataBatchNo, "RWA_ER_RE_MitiGroup_TMP");
/* 227 */     initTableByData(dataBatchNo, "RWA_ER_RE_Group");
/*     */   }
/*     */ 
/*     */   
/*     */   public void initRwaResult(TaskType taskType, String resultNo) {
/* 232 */     if (RwaUtils.isSingle(taskType)) {
/* 233 */       initSingleTaskResult(resultNo);
/*     */       return;
/*     */     } 
/* 236 */     initTable(resultNo, new String[] { "RWA_EL_EDATA" });
/* 237 */     deleteNrRwaResult(taskType, resultNo);
/* 238 */     deleteReRwaResult(taskType, resultNo);
/* 239 */     deleteAbsRwaResult(taskType, resultNo);
/* 240 */     deleteAmpRwaResult(taskType, resultNo, JobType.AMP);
/* 241 */     deleteDiRwaResult(taskType, resultNo);
/* 242 */     deleteSftRwaResult(taskType, resultNo);
/* 243 */     deleteCcpRwaResult(taskType, resultNo);
/* 244 */     deleteStatRwaResult(taskType, resultNo);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void initSingleTaskResult(String resultNo) {
/* 250 */     deleteByResultNo(resultNo, new String[] { "RWA_ESR_GE_Detail", "RWA_ESR_GE_Exposure", "RWA_ESR_GE_Mitigation", "RWA_ESR_Amp_Exposure", "RWA_ESR_Amp_Product", "RWA_ESR_ABS_Exposure", "RWA_ESR_ABS_Product", "RWA_ESR_DI_Netting", "RWA_ESR_DI_Exposure", "RWA_ESR_DI_Intermediate", "RWA_ESR_DI_Collateral", "RWA_ESR_SFT_Netting", "RWA_ESR_SFT_Exposure", "RWA_ESR_SFT_Collateral", "RWA_ESR_CVA", "RWA_ESR_CCP_DF", "RWA_ER_Credit" });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int deleteErrorData(String resultNo, JobType jobType) {
/* 258 */     return this.excDataMapper.delete((Wrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/* 259 */         .eq(ExcDataDo::getResultNo, resultNo)).eq(ExcDataDo::getJobType, jobType.getCode()));
/*     */   }
/*     */ 
/*     */   
/*     */   public void deleteRwaResult(TaskType taskType, String resultNo, JobType jobType) {
/* 264 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobType.ordinal()]) {
/*     */       case 1:
/* 266 */         deleteNrRwaResult(taskType, resultNo);
/*     */         break;
/*     */       case 2:
/* 269 */         deleteReRwaResult(taskType, resultNo);
/*     */         break;
/*     */       case 3:
/* 272 */         deleteAbsRwaResult(taskType, resultNo);
/*     */         break;
/*     */       case 4:
/* 275 */         deleteAmpRwaResult(taskType, resultNo, jobType);
/*     */         break;
/*     */       case 5:
/* 278 */         deleteDiRwaResult(taskType, resultNo);
/*     */         break;
/*     */       case 6:
/* 281 */         deleteSftRwaResult(taskType, resultNo);
/*     */         break;
/*     */       case 7:
/* 284 */         deleteCcpRwaResult(taskType, resultNo);
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteNrRwaResult(TaskType taskType, String resultNo) {
/* 290 */     initTable(resultNo, new String[] { "RWA_ER_NR_Detail", "RWA_ER_NR_Exposure", "RWA_ER_NR_Mitigation" });
/* 291 */     deleteAmpRwaResult(taskType, resultNo, JobType.NR);
/*     */   }
/*     */   
/*     */   public void deleteReRwaResult(TaskType taskType, String resultNo) {
/* 295 */     initTable(resultNo, new String[] { "RWA_ER_RE_Detail", "RWA_ER_RE_Exposure", "RWA_ER_RE_Mitigation" });
/* 296 */     deleteAmpRwaResult(taskType, resultNo, JobType.RE);
/*     */   }
/*     */   
/*     */   public void deleteAbsRwaResult(TaskType taskType, String resultNo) {
/* 300 */     deleteByResultNo(resultNo, new String[] { "RWA_ER_ABS_Exposure", "RWA_ER_ABS_Product", "RWA_ER_ABS_Detail", "RWA_ER_ABS_Mitigation" });
/* 301 */     deleteByResultNo(resultNo, new String[] { "RWA_ER_ABS_UA_Detail", "RWA_ER_ABS_UA_Expo", "RWA_ER_ABS_UA_Miti" });
/* 302 */     deleteAmpRwaResult(taskType, resultNo, JobType.ABS);
/*     */   }
/*     */   
/*     */   public void deleteAmpRwaResult(TaskType taskType, String resultNo, JobType jobType) {
/* 306 */     String sql = "delete from #{tableName} where result_no = #{resultNo} and credit_risk_data_type = #{dataType}";
/* 307 */     if (RwaUtils.isSingle(taskType)) {
/* 308 */       this.commonService.delete(SqlBuilder.create(sql).setTable("tableName", "RWA_ESR_Amp_Exposure")
/* 309 */           .setString("resultNo", resultNo).setString("dataType", jobType.getCode()).build());
/*     */     } else {
/* 311 */       this.commonService.delete(SqlBuilder.create(sql).setTable("tableName", "RWA_ER_Amp_Exposure")
/* 312 */           .setString("resultNo", resultNo).setString("dataType", jobType.getCode()).build());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void deleteDiRwaResult(TaskType taskType, String resultNo) {
/* 317 */     deleteByResultNo(resultNo, new String[] { "RWA_ER_DI_Exposure", "RWA_ER_DI_Netting", "RWA_ER_DI_Collateral", "RWA_ER_DI_Intermediate" });
/* 318 */     this.creditRwaMapper.deleteCvaResult(resultNo, CvaType.DI.getCode());
/*     */   }
/*     */   
/*     */   public void deleteSftRwaResult(TaskType taskType, String resultNo) {
/* 322 */     deleteByResultNo(resultNo, new String[] { "RWA_ER_SFT_Netting", "RWA_ER_SFT_Exposure", "RWA_ER_SFT_Collateral" });
/* 323 */     this.creditRwaMapper.deleteCvaResult(resultNo, CvaType.SFT.getCode());
/*     */   }
/*     */   
/*     */   public void deleteCcpRwaResult(TaskType taskType, String resultNo) {
/* 327 */     deleteByResultNo(resultNo, new String[] { "RWA_ER_CCP_DF" });
/*     */   }
/*     */   
/*     */   public void deleteStatRwaResult(TaskType taskType, String resultNo) {
/* 331 */     deleteByResultNo(resultNo, new String[] { "RWA_GR_Mitigation", "RWA_GR_Credit_Dim", "RWA_ER_Credit_Org", "RWA_ER_Credit", "RWA_ER_Amp_Product" });
/*     */   }
/*     */   
/*     */   public RiskDataPeriodDo getRiskDataPeriod(String dataBatchNo) {
/* 335 */     RiskDataPeriodDo dataPeriodDo = (RiskDataPeriodDo)this.riskDataPeriodMapper.selectById(dataBatchNo);
/* 336 */     if (dataPeriodDo == null)
/*     */     {
/* 338 */       throw new JobParameterException("非法数据批次-没有对应的数据批次");
/*     */     }
/* 340 */     return dataPeriodDo;
/*     */   }
/*     */   
/*     */   public int updateGroupFlag(String dataBatchNo, String groupFlag) {
/* 344 */     return this.riskDataPeriodMapper.updateGroupFlag(dataBatchNo, groupFlag);
/*     */   }
/*     */   
/*     */   public void initGroupResult(TaskType taskType, String dataBatchNo) {
/* 348 */     initNrGroupResult(taskType, dataBatchNo);
/* 349 */     initReGroupResult(taskType, dataBatchNo);
/*     */   }
/*     */   
/*     */   public void initGroupResult(TaskType taskType, String dataBatchNo, JobType jobType) {
/* 353 */     if (jobType == JobType.NR_GROUP) {
/* 354 */       initNrGroupResult(taskType, dataBatchNo);
/* 355 */     } else if (jobType == JobType.RE_GROUP) {
/* 356 */       initReGroupResult(taskType, dataBatchNo);
/*     */     } else {
/* 358 */       throw new ParamConfigException("作业类型[" + jobType + "]调用错误");
/*     */     } 
/*     */   }
/*     */   
/*     */   public CreditRwaDo getRwaResult(String resultNo) {
/* 363 */     return (CreditRwaDo)this.creditRwaMapper.selectById(resultNo);
/*     */   }
/*     */   
/*     */   public int updateCreditRwa(CreditRwaDo rwaDo) {
/* 367 */     return this.creditRwaMapper.updateById(rwaDo);
/*     */   }
/*     */   public CreditRwaDo insertRwaResult(TaskInfoDto taskInfoDto) {
/*     */     Map<String, CreditOrgRwaDo> orgRwaDoMap;
/* 371 */     TaskConfigDo taskConfigDo = taskInfoDto.getTaskConfigDo();
/* 372 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(taskInfoDto.getSchemeId());
/* 373 */     String absRptItem = schemeConfigDo.getWaParamVersion().getCreditRule().getAbsRptItem();
/*     */ 
/*     */     
/* 376 */     if (RwaUtils.isSingle(taskInfoDto.getTaskType())) {
/* 377 */       Map<String, CreditDimResultDo> dimResultMap = getSingleCreditDimResult(taskInfoDto.getResultNo(), taskInfoDto.getDataBatchNo(), taskConfigDo, schemeConfigDo);
/* 378 */       orgRwaDoMap = getCreditOrgResult(taskInfoDto.getResultNo(), absRptItem, dimResultMap.values());
/*     */       
/* 380 */       List<CreditOrgRwaDo> ampList = this.creditOrgRwaMapper.ampSingleResultList(taskInfoDto.getResultNo());
/* 381 */       setAmpResult(orgRwaDoMap, ampList);
/*     */     } else {
/*     */       
/* 384 */       if (schemeConfigDo.getWaParamVersion().getCreditRule().isEnAmpCalc()) {
/* 385 */         insertAmpProductResult(taskInfoDto.getResultNo(), taskInfoDto.getDataBatchNo());
/*     */       }
/* 387 */       Map<String, CreditDimResultDo> dimResultMap = insertCreditDimResult(taskInfoDto.getResultNo(), taskInfoDto.getDataBatchNo(), taskConfigDo, schemeConfigDo);
/*     */       
/* 389 */       insertCreditMitigationResult(taskInfoDto.getResultNo());
/*     */       
/* 391 */       orgRwaDoMap = insertCreditOrgResult(taskInfoDto.getResultNo(), absRptItem, dimResultMap.values());
/*     */     } 
/*     */     
/* 394 */     return insertTotalResult(taskInfoDto, schemeConfigDo, orgRwaDoMap.values());
/*     */   }
/*     */   
/*     */   public int insertAmpProductResult(String resultNo, String dataBatchNo) {
/* 398 */     String sql = "insert into RWA_ER_Amp_Product(result_no, amp_id, amp_belong, amp_name, amp_type, is_tp_calc, total_asset, net_asset, lr, holding_share, investment_amt, pene_wa_ncp_ab, pene_wa_ncp_ead, pene_wa_ncp_rwa, pene_irb_ncp_ab, pene_irb_ncp_ead, pene_irb_ncp_rwa, pene_wa_cp_ab, pene_wa_cp_prin, pene_wa_cp_ead, pene_wa_cp_rwa, pene_wa_cva, pene_irb_cp_ab, pene_irb_cp_prin, pene_irb_cp_ead, pene_irb_cp_rwa, pene_irb_cva, pene_ab, pene_ead, pene_rwa, aba_ab, aba_ead, aba_rwa, f1250_ab, f1250_ead, f1250_rwa, rwa_bad, rwa_adj, rwa_aa) select #{resultNo} as result_no, t.amp_id, t.amp_belong, f.amp_name, f.amp_type, f.is_tp_calc, f.total_asset, f.net_asset, f.lr, case when f.net_asset > 0 then t.investment_amt / f.net_asset end as holding_share, t.investment_amt, t.pene_wa_ncp_ab, t.pene_wa_ncp_ead, t.pene_wa_ncp_rwa, t.pene_irb_ncp_ab, t.pene_irb_ncp_ead, t.pene_irb_ncp_rwa, t.pene_wa_cp_ab, t.pene_wa_cp_prin, t.pene_wa_cp_ead, t.pene_wa_cp_rwa, t.pene_wa_cva, t.pene_irb_cp_ab, t.pene_irb_cp_prin, t.pene_irb_cp_ead, t.pene_irb_cp_rwa, t.pene_irb_cva, t.pene_ab, t.pene_ead, t.pene_rwa, t.aba_ab, t.aba_ead, t.aba_rwa, t.f1250_ab, t.f1250_ead, t.f1250_rwa, t.rwa_bad, t.rwa_adj, t.rwa_aa from (select r.amp_id, r.amp_belong, sum(r.hold_net_asset) as investment_amt, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '1%' then r.asset_balance else 0 end) as pene_wa_ncp_ab, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '1%' then r.ead else 0 end) as pene_wa_ncp_ead, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '1%' then r.rwa else 0 end) as pene_wa_ncp_rwa, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '2%' then r.asset_balance else 0 end) as pene_irb_ncp_ab, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '2%' then r.ead else 0 end) as pene_irb_ncp_ead, sum(case when r.amp_approach = '10' and r.exposure_belong <> '3' and r.approach like '2%' then r.rwa else 0 end) as pene_irb_ncp_rwa, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '1%' then r.asset_balance else 0 end) as pene_wa_cp_ab, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '1%' then r.notional_principal else 0 end) as pene_wa_cp_prin, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '1%' then r.ead else 0 end) as pene_wa_cp_ead, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '1%' then r.rwa_cp else 0 end) as pene_wa_cp_rwa, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '1%' then r.cva else 0 end) as pene_wa_cva, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '2%' then r.asset_balance else 0 end) as pene_irb_cp_ab, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '2%' then r.notional_principal else 0 end) as pene_irb_cp_prin, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '2%' then r.ead else 0 end) as pene_irb_cp_ead, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '2%' then r.rwa_cp else 0 end) as pene_irb_cp_rwa, sum(case when r.amp_approach = '10' and r.exposure_belong = '3' and r.approach like '2%' then r.cva else 0 end) as pene_irb_cva, sum(case when r.amp_approach = '10' then r.asset_balance else 0 end) as pene_ab, sum(case when r.amp_approach = '10' then r.ead else 0 end) as pene_ead, sum(case when r.amp_approach = '10' then r.rwa else 0 end) as pene_rwa, sum(case when r.amp_approach = '20' then r.asset_balance else 0 end) as aba_ab, sum(case when r.amp_approach = '20' then r.ead else 0 end) as aba_ead, sum(case when r.amp_approach = '20' then r.rwa else 0 end) as aba_rwa, sum(case when r.amp_approach = '30' then r.asset_balance else 0 end) as f1250_ab, sum(case when r.amp_approach = '30' then r.ead else 0 end) as f1250_ead, sum(case when r.amp_approach = '30' then r.rwa else 0 end) as f1250_rwa, sum(r.rwa) as rwa_bad, sum(r.rwa_adj) as rwa_adj, sum(r.rwa_aa) as rwa_aa from rwa_er_amp_exposure r where r.result_no = #{resultNo} and length(r.amp_id) > 0 and r.amp_book_type = '1' group by r.amp_id, r.amp_belong) t left join rwa_ei_amp_info f on f.data_batch_no = #{dataBatchNo} and f.amp_id = t.amp_id";
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
/* 446 */     return this.commonService.insert(SqlBuilder.create(sql).setString("resultNo", resultNo).setString("dataBatchNo", dataBatchNo).build());
/*     */   }
/*     */   
/*     */   public void batchInsertOrgResult(String resultNo, Collection<CreditOrgRwaDo> results, int batchSize) {
/* 450 */     String sql = "insert into RWA_ER_Credit_Org(result_no, org_id, asset_balance, exposure, rc, rwa, rwa_wa, rwa_irb, rwa_on, rwa_on_wa, rwa_on_irb, rwa_off, rwa_off_wa, rwa_off_irb, rwa_cp, rwa_cp_wa, rwa_cp_irb, cva, cva_di, cva_sft, rwa_abs, rwa_abs_on, rwa_abs_off, ec, rwa_amp, rwa_amp_pene, rwa_amp_aba, rwa_amp_f1250, rwa_amp_cp, cva_amp, rwa_amp_aa) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 456 */     int[][] result = this.commonService.getJdbcTemplate().batchUpdate(sql, results, batchSize, (ParameterizedPreparedStatementSetter)new Object(this, resultNo));
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
/*     */   public void batchInsertDimResult(String resultNo, Collection<CreditDimResultDo> results, int batchSize) {
/* 495 */     String sql = "insert into RWA_GR_Credit_Dim(serial_no, result_no, approach, org_id, industry_id, business_line, asset_type, exposure_type_wa, exposure_rpt_item_wa, rw, exposure_type_irb, pd, exposure_belong, asset_balance, exposure, rwa_mb, rwa_ma, ela, ec, provision, ead_um) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     */ 
/*     */ 
/*     */     
/* 499 */     int[][] result = this.commonService.getJdbcTemplate().batchUpdate(sql, results, batchSize, (ParameterizedPreparedStatementSetter)new Object(this, resultNo));
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
/*     */   public void batchInsertMitigationResult(String resultNo, Collection<CreditMitigationResultDo> results, int batchSize) {
/* 528 */     String sql = "insert into RWA_GR_Mitigation(serial_no, result_no, approach, qual_flag, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, rw, pd, lgd, exposure_type_wa, exposure_type_irb, exposure_belong, mitigation_amount, mitigated_amount, covered_ea, mitigated_effect) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 533 */     int[][] result = this.commonService.getJdbcTemplate().batchUpdate(sql, results, batchSize, (ParameterizedPreparedStatementSetter)new Object(this, resultNo));
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
/*     */   public Map<String, CreditDimResultDo> insertCreditDimResult(String resultNo, String dataBatchNo, TaskConfigDo taskConfigDo, SchemeConfigDo schemeConfigDo) {
/* 558 */     CreditRuleDo waCreditRule = schemeConfigDo.getWaParamVersion().getCreditRule();
/* 559 */     Approach approach = (Approach)EnumUtils.getEnumByCode(schemeConfigDo.getApproach(), Approach.class);
/*     */ 
/*     */     
/* 562 */     List<CreditDimResultDo> ampLraDimResultList = null;
/*     */ 
/*     */ 
/*     */     
/* 566 */     List<CreditDimResultDo> ampWaNpDimResultList = null;
/*     */     
/* 568 */     if (waCreditRule.isEnAmpCalc()) {
/* 569 */       ampWaNpDimResultList = this.creditDimResultMapper.ampWaNpDimResultList(resultNo, dataBatchNo);
/* 570 */       ampLraDimResultList = this.creditDimResultMapper.ampLraDimResultList(resultNo, dataBatchNo, waCreditRule.getAmpLraRptItem());
/*     */     } 
/*     */     
/* 573 */     List<CreditDimResultDo> nrWaDimResultList = this.creditDimResultMapper.nrWaDimResultList(resultNo, dataBatchNo);
/*     */     
/* 575 */     List<CreditDimResultDo> reWaDimResultList = this.creditDimResultMapper.reWaDimResultList(resultNo, dataBatchNo);
/*     */     
/* 577 */     List<CreditDimResultDo> absDimResultList = null;
/* 578 */     if (waCreditRule.isEnAbsCalc()) {
/* 579 */       absDimResultList = this.creditDimResultMapper.absDimResultList(resultNo, dataBatchNo, waCreditRule.getAbsRptItem());
/*     */     }
/*     */     
/* 582 */     List<CreditDimResultDo> diWaDimResultList = null;
/* 583 */     if (waCreditRule.isEnDiCalc()) {
/* 584 */       diWaDimResultList = this.creditDimResultMapper.diWaDimResultList(resultNo, dataBatchNo);
/*     */     }
/*     */     
/* 587 */     List<CreditDimResultDo> sftWaDimResultList = null;
/*     */     
/* 589 */     List<CreditDimResultDo> sftCollDimResultList = null;
/* 590 */     if (waCreditRule.isEnSftCalc()) {
/* 591 */       sftWaDimResultList = this.creditDimResultMapper.sftWaDimResultList(resultNo, dataBatchNo);
/* 592 */       sftCollDimResultList = this.creditDimResultMapper.sftBankCollDimResultList(resultNo, dataBatchNo, SpecExposureTypeWa.SFT_BANK_COLL
/* 593 */           .getCode(), SpecExposureTypeIrb.SFT_BANK_COLL.getCode());
/*     */     } 
/*     */     
/* 596 */     List<CreditDimResultDo> ccpDimResultList = null;
/* 597 */     if (waCreditRule.isEnCcpCalc()) {
/* 598 */       ccpDimResultList = this.creditDimResultMapper.ccpDimResultList(resultNo, dataBatchNo);
/*     */     }
/*     */     
/* 601 */     Map<String, CreditDimResultDo> resultDoMap = groupDimResultList(null, (List<CreditDimResultDo>[])new List[] { nrWaDimResultList, ampWaNpDimResultList, ampLraDimResultList, reWaDimResultList, absDimResultList, diWaDimResultList, sftWaDimResultList, sftCollDimResultList, ccpDimResultList });
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 606 */     CreditDimResultDo diCva = getCvaCreditDimResult(resultNo, taskConfigDo, schemeConfigDo, CvaType.DI);
/* 607 */     if (diCva != null) {
/* 608 */       resultDoMap.put(CvaType.DI.getCode(), diCva);
/*     */     }
/* 610 */     CreditDimResultDo sftCva = getCvaCreditDimResult(resultNo, taskConfigDo, schemeConfigDo, CvaType.SFT);
/* 611 */     if (sftCva != null) {
/* 612 */       resultDoMap.put(CvaType.SFT.getCode(), sftCva);
/*     */     }
/* 614 */     batchInsertDimResult(resultNo, resultDoMap.values(), 1000);
/* 615 */     return resultDoMap;
/*     */   }
/*     */   public CreditDimResultDo getCvaCreditDimResult(String resultNo, TaskConfigDo taskConfigDo, SchemeConfigDo schemeConfigDo, CvaType cvaType) {
/*     */     String exposureRptItemWa, assetType, exposureTypeWa, exposureTypeIrb;
/* 619 */     BigDecimal cva = ((JobService)SpringUtil.getBean(JobService.class)).getCva(resultNo, cvaType);
/* 620 */     if (!RwaMath.isPositive(cva)) {
/* 621 */       return null;
/*     */     }
/* 623 */     CreditRuleDo creditRuleDo = schemeConfigDo.getWaParamVersion().getCreditRule();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 629 */     if (cvaType == CvaType.DI) {
/* 630 */       exposureRptItemWa = creditRuleDo.getDiCvaRptItem();
/* 631 */       assetType = RwaConfig.diCvaAssetType;
/* 632 */       exposureTypeWa = SpecExposureTypeWa.DI_CVA.getCode();
/* 633 */       exposureTypeIrb = SpecExposureTypeIrb.DI_CVA.getCode();
/*     */     } else {
/* 635 */       exposureRptItemWa = creditRuleDo.getSftCvaRptItem();
/* 636 */       assetType = RwaConfig.sftCvaAssetType;
/* 637 */       exposureTypeWa = SpecExposureTypeWa.SFT_CVA.getCode();
/* 638 */       exposureTypeIrb = SpecExposureTypeIrb.SFT_CVA.getCode();
/*     */     } 
/* 640 */     BigDecimal ec = BigDecimal.ZERO;
/* 641 */     if (taskConfigDo.getEcOf() != null) {
/* 642 */       ec = RwaMath.mul(cva, taskConfigDo.getEcOf());
/*     */     }
/* 644 */     return new CreditDimResultDo(null, null, ExposureApproach.WA.getCode(), RwaConfig.headOrgId, RwaConfig.cvaIndustryId, RwaConfig.cvaBusinessLine, assetType, exposureTypeWa, exposureRptItemWa, null, exposureTypeIrb, null, ExposureBelong.CCP
/*     */         
/* 646 */         .getCode(), BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, cva, cva, BigDecimal.ZERO, ec);
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, CreditDimResultDo> getSingleCreditDimResult(String resultNo, String dataBatchNo, TaskConfigDo taskConfigDo, SchemeConfigDo schemeConfigDo) {
/* 651 */     CreditRuleDo waCreditRule = schemeConfigDo.getWaParamVersion().getCreditRule();
/* 652 */     List<CreditDimResultDo> geSingleDimResultList = this.creditDimResultMapper.geSingleDimResultList(resultNo, dataBatchNo, RwaConfig.headOrgId);
/* 653 */     List<CreditDimResultDo> absSingleDimResultList = null;
/* 654 */     if (waCreditRule.isEnAbsCalc()) {
/* 655 */       absSingleDimResultList = this.creditDimResultMapper.absSingleDimResultList(resultNo, RwaConfig.headOrgId, waCreditRule.getAbsRptItem());
/*     */     }
/* 657 */     List<CreditDimResultDo> ampWaNpSingleDimResultList = null;
/* 658 */     List<CreditDimResultDo> ampLraSingleDimResultList = null;
/* 659 */     if (waCreditRule.isEnAmpCalc()) {
/* 660 */       ampWaNpSingleDimResultList = this.creditDimResultMapper.ampWaNpSingleDimResultList(resultNo);
/* 661 */       ampLraSingleDimResultList = this.creditDimResultMapper.ampLraSingleDimResultList(resultNo, waCreditRule.getAmpLraRptItem());
/*     */     } 
/* 663 */     List<CreditDimResultDo> diSingleDimResultList = null;
/* 664 */     if (waCreditRule.isEnDiCalc()) {
/* 665 */       diSingleDimResultList = this.creditDimResultMapper.diSingleDimResultList(resultNo, RwaConfig.headOrgId);
/*     */     }
/* 667 */     List<CreditDimResultDo> sftSingleDimResultList = null;
/* 668 */     if (waCreditRule.isEnSftCalc()) {
/* 669 */       sftSingleDimResultList = this.creditDimResultMapper.sftSingleDimResultList(resultNo, RwaConfig.headOrgId);
/*     */     }
/* 671 */     List<CreditDimResultDo> ccpSingleDimResultList = null;
/* 672 */     if (waCreditRule.isEnCcpCalc()) {
/* 673 */       ccpSingleDimResultList = this.creditDimResultMapper.ccpSingleDimResultList(resultNo, RwaConfig.headOrgId);
/*     */     }
/*     */     
/* 676 */     Map<String, CreditDimResultDo> resultDoMap = groupDimResultList(null, (List<CreditDimResultDo>[])new List[] { geSingleDimResultList, absSingleDimResultList, ampWaNpSingleDimResultList, ampLraSingleDimResultList, diSingleDimResultList, sftSingleDimResultList, ccpSingleDimResultList });
/*     */ 
/*     */     
/* 679 */     CreditDimResultDo diCva = getCvaCreditDimResult(resultNo, taskConfigDo, schemeConfigDo, CvaType.DI);
/* 680 */     if (diCva != null) {
/* 681 */       resultDoMap.put(CvaType.DI.getCode(), diCva);
/*     */     }
/* 683 */     CreditDimResultDo sftCva = getCvaCreditDimResult(resultNo, taskConfigDo, schemeConfigDo, CvaType.SFT);
/* 684 */     if (sftCva != null) {
/* 685 */       resultDoMap.put(CvaType.SFT.getCode(), sftCva);
/*     */     }
/* 687 */     return resultDoMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<String, CreditMitigationResultDo> insertCreditMitigationResult(String resultNo) {
/* 692 */     List<CreditMitigationResultDo> nrWaDetailResultList = this.mitigationResultMapper.nrWaDetailResultList(resultNo);
/* 693 */     List<CreditMitigationResultDo> reWaDetailResultList = this.mitigationResultMapper.reWaDetailResultList(resultNo);
/*     */     
/* 695 */     List<CreditMitigationResultDo> nrWaTotalResultList = this.mitigationResultMapper.nrWaTotalResultList(resultNo);
/* 696 */     List<CreditMitigationResultDo> reWaTotalResultList = this.mitigationResultMapper.reWaTotalResultList(resultNo);
/*     */     
/* 698 */     Map<String, CreditMitigationResultDo> resultDoMap = groupMitigationResultList(null, (List<CreditMitigationResultDo>[])new List[] { nrWaDetailResultList, reWaDetailResultList, nrWaTotalResultList, reWaTotalResultList });
/*     */ 
/*     */ 
/*     */     
/* 702 */     batchInsertMitigationResult(resultNo, resultDoMap.values(), 1000);
/* 703 */     return resultDoMap;
/*     */   }
/*     */   
/*     */   public Map<String, CreditDimResultDo> groupDimResultList(Map<String, CreditDimResultDo> resultMap, List<CreditDimResultDo>... resultLists) {
/* 707 */     if (resultMap == null) {
/* 708 */       resultMap = new HashMap<>();
/*     */     }
/* 710 */     if (resultLists.length == 0) {
/* 711 */       return resultMap;
/*     */     }
/* 713 */     for (List<CreditDimResultDo> list : resultLists) {
/* 714 */       if (!CollUtil.isEmpty(list))
/*     */       {
/*     */         
/* 717 */         for (CreditDimResultDo resultDo : list) {
/* 718 */           String key = getDimResultKey(resultDo);
/* 719 */           CreditDimResultDo r = resultMap.get(key);
/* 720 */           if (r == null) {
/* 721 */             resultMap.put(key, resultDo); continue;
/*     */           } 
/* 723 */           sumResult(r, resultDo);
/*     */         } 
/*     */       }
/*     */     } 
/* 727 */     return resultMap;
/*     */   }
/*     */   
/*     */   public String getDimResultKey(CreditDimResultDo dimResultDo) {
/* 731 */     return DataUtils.generateKey(new String[] { dimResultDo.getApproach(), dimResultDo.getOrgId(), dimResultDo.getIndustryId(), dimResultDo
/* 732 */           .getBusinessLine(), dimResultDo.getAssetType(), dimResultDo.getExposureTypeWa(), dimResultDo
/* 733 */           .getExposureRptItemWa(), DataUtils.getRoundName(dimResultDo.getRw(), 6), dimResultDo
/* 734 */           .getExposureTypeIrb(), DataUtils.getRoundName(dimResultDo.getPd(), 6), dimResultDo
/* 735 */           .getExposureBelong() });
/*     */   }
/*     */   
/*     */   public CreditDimResultDo sumResult(CreditDimResultDo r, CreditDimResultDo dimResultDo) {
/* 739 */     r.setAssetBalance(RwaMath.add(r.getAssetBalance(), dimResultDo.getAssetBalance()));
/* 740 */     r.setExposure(RwaMath.add(r.getExposure(), dimResultDo.getExposure()));
/* 741 */     r.setRwaMb(RwaMath.add(r.getRwaMb(), dimResultDo.getRwaMb()));
/* 742 */     r.setRwaMa(RwaMath.add(r.getRwaMa(), dimResultDo.getRwaMa()));
/* 743 */     r.setEla(RwaMath.add(r.getEla(), dimResultDo.getEla()));
/* 744 */     r.setEc(RwaMath.add(r.getEc(), dimResultDo.getEc()));
/* 745 */     r.setProvision(RwaMath.add(r.getProvision(), dimResultDo.getProvision()));
/* 746 */     r.setEadUm(RwaMath.add(r.getEadUm(), dimResultDo.getEadUm()));
/* 747 */     return r;
/*     */   }
/*     */   
/*     */   public Map<String, CreditMitigationResultDo> groupMitigationResultList(Map<String, CreditMitigationResultDo> resultMap, List<CreditMitigationResultDo>... resultLists) {
/* 751 */     if (resultMap == null) {
/* 752 */       resultMap = new HashMap<>();
/*     */     }
/* 754 */     if (resultLists.length == 0) {
/* 755 */       return resultMap;
/*     */     }
/* 757 */     for (List<CreditMitigationResultDo> list : resultLists) {
/* 758 */       if (!CollUtil.isEmpty(list))
/*     */       {
/*     */         
/* 761 */         for (CreditMitigationResultDo resultDo : list) {
/* 762 */           String key = getMitigationResultKey(resultDo);
/* 763 */           CreditMitigationResultDo r = resultMap.get(key);
/* 764 */           if (r == null) {
/* 765 */             resultMap.put(key, resultDo); continue;
/*     */           } 
/* 767 */           sumResult(r, resultDo);
/*     */         } 
/*     */       }
/*     */     } 
/* 771 */     return resultMap;
/*     */   }
/*     */   
/*     */   public String getMitigationResultKey(CreditMitigationResultDo mitigationResultDo) {
/* 775 */     return DataUtils.generateKey(new String[] { mitigationResultDo.getApproach(), mitigationResultDo.getQualFlag(), mitigationResultDo
/* 776 */           .getMitigationMainType(), mitigationResultDo.getMitigationSmallType(), mitigationResultDo.getMitigationRptItemWa(), 
/* 777 */           DataUtils.getRoundName(mitigationResultDo.getRw(), 6), DataUtils.getRoundName(mitigationResultDo.getPd(), 6), 
/* 778 */           DataUtils.getRoundName(mitigationResultDo.getLgd(), 6), mitigationResultDo
/* 779 */           .getExposureTypeWa(), mitigationResultDo.getExposureTypeIrb(), mitigationResultDo.getExposureBelong() });
/*     */   }
/*     */ 
/*     */   
/*     */   public CreditMitigationResultDo sumResult(CreditMitigationResultDo r, CreditMitigationResultDo m) {
/* 784 */     r.setMitigationAmount(RwaMath.add(r.getMitigationAmount(), m.getMitigationAmount()));
/* 785 */     r.setMitigatedAmount(RwaMath.add(r.getMitigatedAmount(), m.getMitigatedAmount()));
/* 786 */     r.setCoveredEa(RwaMath.add(r.getCoveredEa(), m.getCoveredEa()));
/* 787 */     r.setMitigatedEffect(RwaMath.add(r.getMitigatedEffect(), m.getMitigatedEffect()));
/* 788 */     return r;
/*     */   }
/*     */   
/*     */   public Map<String, CreditOrgRwaDo> getCreditOrgResult(String resultNo, String absRptItem, Collection<CreditDimResultDo> resultDos) {
/* 792 */     Map<String, CreditOrgRwaDo> orgResultMap = new HashMap<>();
/* 793 */     for (CreditDimResultDo resultDo : resultDos) {
/* 794 */       CreditOrgRwaDo orgResult = orgResultMap.get(resultDo.getOrgId());
/* 795 */       if (orgResult == null) {
/* 796 */         orgResult = new CreditOrgRwaDo();
/* 797 */         orgResult.setOrgId(resultDo.getOrgId());
/* 798 */         orgResultMap.put(orgResult.getOrgId(), orgResult);
/*     */       } 
/* 800 */       sumResult(orgResult, resultDo, absRptItem);
/*     */     } 
/* 802 */     return orgResultMap;
/*     */   }
/*     */   
/*     */   public Map<String, CreditOrgRwaDo> insertCreditOrgResult(String resultNo, String absRptItem, Collection<CreditDimResultDo> resultDos) {
/* 806 */     Map<String, CreditOrgRwaDo> orgResultMap = getCreditOrgResult(resultNo, absRptItem, resultDos);
/*     */     
/* 808 */     List<CreditOrgRwaDo> ampResultList = this.creditOrgRwaMapper.ampResultList(resultNo);
/* 809 */     setAmpResult(orgResultMap, ampResultList);
/*     */     
/* 811 */     batchInsertOrgResult(resultNo, orgResultMap.values(), 1000);
/* 812 */     return orgResultMap;
/*     */   }
/*     */   
/*     */   public void setAmpResult(Map<String, CreditOrgRwaDo> orgResultMap, List<CreditOrgRwaDo> ampResultList) {
/* 816 */     for (CreditOrgRwaDo ampRwa : ampResultList) {
/* 817 */       CreditOrgRwaDo orgRwaDo = orgResultMap.get(ampRwa.getOrgId());
/* 818 */       if (orgRwaDo == null) {
/* 819 */         orgResultMap.put(ampRwa.getOrgId(), ampRwa);
/*     */         continue;
/*     */       } 
/* 822 */       orgRwaDo.setRwaAmp(ampRwa.getRwaAmp());
/* 823 */       orgRwaDo.setRwaAmpPene(ampRwa.getRwaAmpPene());
/* 824 */       orgRwaDo.setRwaAmpAba(ampRwa.getRwaAmpAba());
/* 825 */       orgRwaDo.setRwaAmpF1250(ampRwa.getRwaAmpF1250());
/* 826 */       orgRwaDo.setRwaAmpCp(ampRwa.getRwaAmpCp());
/* 827 */       orgRwaDo.setCvaAmp(ampRwa.getCvaAmp());
/* 828 */       orgRwaDo.setRwaAmpAa(ampRwa.getRwaAmpAa());
/*     */     } 
/*     */   }
/*     */   
/*     */   public CreditOrgRwaDo sumResult(CreditOrgRwaDo orgRwaDo, CreditDimResultDo resultDo, String absRptItem) {
/* 833 */     if (resultDo == null) {
/* 834 */       return orgRwaDo;
/*     */     }
/*     */     
/* 837 */     if (StrUtil.equals(resultDo.getExposureTypeWa(), SpecExposureTypeWa.DI_CVA.getCode())) {
/*     */       
/* 839 */       orgRwaDo.setCvaDi(resultDo.getRwaMa());
/* 840 */       orgRwaDo.setCva(RwaMath.add(orgRwaDo.getCva(), resultDo.getRwaMa()));
/* 841 */       orgRwaDo.setRwaWa(RwaMath.add(orgRwaDo.getRwaWa(), resultDo.getRwaMa()));
/* 842 */       orgRwaDo.setRwa(RwaMath.add(orgRwaDo.getRwa(), resultDo.getRwaMa()));
/* 843 */       orgRwaDo.setEc(RwaMath.add(orgRwaDo.getEc(), resultDo.getEc()));
/* 844 */       return orgRwaDo;
/* 845 */     }  if (StrUtil.equals(resultDo.getExposureTypeWa(), SpecExposureTypeWa.SFT_CVA.getCode())) {
/*     */       
/* 847 */       orgRwaDo.setCvaSft(resultDo.getRwaMa());
/* 848 */       orgRwaDo.setCva(RwaMath.add(orgRwaDo.getCva(), resultDo.getRwaMa()));
/* 849 */       orgRwaDo.setRwaWa(RwaMath.add(orgRwaDo.getRwaWa(), resultDo.getRwaMa()));
/* 850 */       orgRwaDo.setRwa(RwaMath.add(orgRwaDo.getRwa(), resultDo.getRwaMa()));
/* 851 */       orgRwaDo.setEc(RwaMath.add(orgRwaDo.getEc(), resultDo.getEc()));
/* 852 */       return orgRwaDo;
/*     */     } 
/*     */     
/* 855 */     orgRwaDo.setAssetBalance(RwaMath.add(orgRwaDo.getAssetBalance(), resultDo.getAssetBalance()));
/* 856 */     orgRwaDo.setExposure(RwaMath.add(orgRwaDo.getExposure(), resultDo.getExposure()));
/* 857 */     orgRwaDo.setRwa(RwaMath.add(orgRwaDo.getRwa(), resultDo.getRwaMa()));
/* 858 */     orgRwaDo.setEc(RwaMath.add(orgRwaDo.getEc(), resultDo.getEc()));
/* 859 */     if (RwaUtils.isIrb(resultDo.getApproach())) {
/* 860 */       orgRwaDo.setRwaIrb(RwaMath.add(orgRwaDo.getRwaIrb(), resultDo.getRwaMa()));
/*     */     } else {
/* 862 */       orgRwaDo.setRwaWa(RwaMath.add(orgRwaDo.getRwaWa(), resultDo.getRwaMa()));
/*     */     } 
/*     */     
/* 865 */     if (StrUtil.equals(resultDo.getExposureRptItemWa(), absRptItem)) {
/* 866 */       orgRwaDo.setRwaAbs(RwaMath.add(orgRwaDo.getRwaAbs(), resultDo.getRwaMa()));
/* 867 */       if (StrUtil.equals(ExposureBelong.OFF.getCode(), resultDo.getExposureBelong())) {
/* 868 */         orgRwaDo.setRwaAbsOff(RwaMath.add(orgRwaDo.getRwaAbsOff(), resultDo.getRwaMa()));
/*     */       } else {
/* 870 */         orgRwaDo.setRwaAbsOn(RwaMath.add(orgRwaDo.getRwaAbsOn(), resultDo.getRwaMa()));
/*     */       } 
/* 872 */       return orgRwaDo;
/*     */     } 
/*     */     
/* 875 */     if (StrUtil.equals(ExposureBelong.CCP.getCode(), resultDo.getExposureBelong())) {
/*     */       
/* 877 */       orgRwaDo.setRwaCp(RwaMath.add(orgRwaDo.getRwaCp(), resultDo.getRwaMa()));
/* 878 */       if (RwaUtils.isIrb(resultDo.getApproach())) {
/* 879 */         orgRwaDo.setRwaCpIrb(RwaMath.add(orgRwaDo.getRwaCpIrb(), resultDo.getRwaMa()));
/*     */       } else {
/* 881 */         orgRwaDo.setRwaCpWa(RwaMath.add(orgRwaDo.getRwaCpWa(), resultDo.getRwaMa()));
/*     */       } 
/* 883 */     } else if (StrUtil.equals(ExposureBelong.OFF.getCode(), resultDo.getExposureBelong())) {
/*     */       
/* 885 */       orgRwaDo.setRwaOff(RwaMath.add(orgRwaDo.getRwaOff(), resultDo.getRwaMa()));
/* 886 */       if (RwaUtils.isIrb(resultDo.getApproach())) {
/* 887 */         orgRwaDo.setRwaOffIrb(RwaMath.add(orgRwaDo.getRwaOffIrb(), resultDo.getRwaMa()));
/*     */       } else {
/* 889 */         orgRwaDo.setRwaOffWa(RwaMath.add(orgRwaDo.getRwaOffWa(), resultDo.getRwaMa()));
/*     */       } 
/*     */     } else {
/*     */       
/* 893 */       orgRwaDo.setRwaOn(RwaMath.add(orgRwaDo.getRwaOn(), resultDo.getRwaMa()));
/* 894 */       if (RwaUtils.isIrb(resultDo.getApproach())) {
/* 895 */         orgRwaDo.setRwaOnIrb(RwaMath.add(orgRwaDo.getRwaOnIrb(), resultDo.getRwaMa()));
/*     */       } else {
/* 897 */         orgRwaDo.setRwaOnWa(RwaMath.add(orgRwaDo.getRwaOnWa(), resultDo.getRwaMa()));
/*     */       } 
/*     */     } 
/* 900 */     return orgRwaDo;
/*     */   }
/*     */ 
/*     */   
/*     */   public CreditRwaDo insertTotalResult(TaskInfoDto taskInfoDto, SchemeConfigDo schemeConfigDo, Collection<CreditOrgRwaDo> orgRwaDos) {
/* 905 */     CreditRwaDo rwaDo = new CreditRwaDo();
/* 906 */     rwaDo.setResultNo(taskInfoDto.getResultNo());
/* 907 */     rwaDo.setDataDate(taskInfoDto.getDataDate());
/* 908 */     rwaDo.setDataBatchNo(taskInfoDto.getDataBatchNo());
/* 909 */     rwaDo.setTaskType(taskInfoDto.getTaskType().getCode());
/* 910 */     rwaDo.setTaskId(taskInfoDto.getTaskId());
/* 911 */     rwaDo.setSchemeId(schemeConfigDo.getSchemeId());
/*     */     
/* 913 */     if (!RwaUtils.isSingle(taskInfoDto.getTaskType())) {
/* 914 */       RiskDataPeriodDo periodDo = getRiskDataPeriod(taskInfoDto.getDataBatchNo());
/* 915 */       rwaDo.setCorporationId(periodDo.getCorporationId());
/* 916 */       rwaDo.setBankTranche(periodDo.getBankTranche());
/*     */     } 
/*     */     
/* 919 */     rwaDo.setConsolidateFlag(taskInfoDto.getTaskConfigDo().getConsolidateFlag());
/* 920 */     rwaDo.setWaParamVersionNo(schemeConfigDo.getWaParamVersionNo());
/* 921 */     rwaDo.setIrbParamVersionNo(schemeConfigDo.getIrbParamVersionNo());
/* 922 */     rwaDo.setApproach(schemeConfigDo.getApproach());
/* 923 */     rwaDo.setDiEadApproach(schemeConfigDo.getDiEadApproach());
/*     */     
/* 925 */     for (CreditOrgRwaDo orgRwaDo : orgRwaDos) {
/* 926 */       sumResult(rwaDo, orgRwaDo);
/*     */     }
/*     */     
/* 929 */     if (rwaDo.getRwa() != null) {
/* 930 */       rwaDo.setRc(RwaMath.div(rwaDo.getRwa(), RwaMath.kFactor));
/*     */     }
/* 932 */     this.creditRwaMapper.insert(rwaDo);
/* 933 */     return rwaDo;
/*     */   }
/*     */   
/*     */   public CreditRwaDo sumResult(CreditRwaDo rwaDo, CreditOrgRwaDo orgRwaDo) {
/* 937 */     if (rwaDo == null) {
/* 938 */       rwaDo = new CreditRwaDo();
/*     */     }
/* 940 */     rwaDo.setAssetBalance(NumberUtil.add(rwaDo.getAssetBalance(), orgRwaDo.getAssetBalance()));
/* 941 */     rwaDo.setExposure(NumberUtil.add(rwaDo.getExposure(), orgRwaDo.getExposure()));
/* 942 */     rwaDo.setRwa(NumberUtil.add(rwaDo.getRwa(), orgRwaDo.getRwa()));
/* 943 */     rwaDo.setEc(NumberUtil.add(rwaDo.getEc(), orgRwaDo.getEc()));
/* 944 */     rwaDo.setRwaWa(NumberUtil.add(rwaDo.getRwaWa(), orgRwaDo.getRwaWa()));
/* 945 */     rwaDo.setRwaIrb(NumberUtil.add(rwaDo.getRwaIrb(), orgRwaDo.getRwaIrb()));
/* 946 */     rwaDo.setRwaOn(NumberUtil.add(rwaDo.getRwaOn(), orgRwaDo.getRwaOn()));
/* 947 */     rwaDo.setRwaOnWa(NumberUtil.add(rwaDo.getRwaOnWa(), orgRwaDo.getRwaOnWa()));
/* 948 */     rwaDo.setRwaOnIrb(NumberUtil.add(rwaDo.getRwaOnIrb(), orgRwaDo.getRwaOnIrb()));
/* 949 */     rwaDo.setRwaOff(NumberUtil.add(rwaDo.getRwaOff(), orgRwaDo.getRwaOff()));
/* 950 */     rwaDo.setRwaOffWa(NumberUtil.add(rwaDo.getRwaOffWa(), orgRwaDo.getRwaOffWa()));
/* 951 */     rwaDo.setRwaOffIrb(NumberUtil.add(rwaDo.getRwaOffIrb(), orgRwaDo.getRwaOffIrb()));
/* 952 */     rwaDo.setRwaCp(NumberUtil.add(rwaDo.getRwaCp(), orgRwaDo.getRwaCp()));
/* 953 */     rwaDo.setRwaCpWa(NumberUtil.add(rwaDo.getRwaCpWa(), orgRwaDo.getRwaCpWa()));
/* 954 */     rwaDo.setRwaCpIrb(NumberUtil.add(rwaDo.getRwaCpIrb(), orgRwaDo.getRwaCpIrb()));
/* 955 */     rwaDo.setRwaAbs(NumberUtil.add(rwaDo.getRwaAbs(), orgRwaDo.getRwaAbs()));
/* 956 */     rwaDo.setRwaAbsOn(NumberUtil.add(rwaDo.getRwaAbsOn(), orgRwaDo.getRwaAbsOn()));
/* 957 */     rwaDo.setRwaAbsOff(NumberUtil.add(rwaDo.getRwaAbsOff(), orgRwaDo.getRwaAbsOff()));
/* 958 */     rwaDo.setCva(NumberUtil.add(rwaDo.getCva(), orgRwaDo.getCva()));
/* 959 */     rwaDo.setCvaDi(NumberUtil.add(rwaDo.getCvaDi(), orgRwaDo.getCvaDi()));
/* 960 */     rwaDo.setCvaSft(NumberUtil.add(rwaDo.getCvaSft(), orgRwaDo.getCvaSft()));
/*     */     
/* 962 */     rwaDo.setRwaAmp(NumberUtil.add(rwaDo.getRwaAmp(), orgRwaDo.getRwaAmp()));
/* 963 */     rwaDo.setRwaAmpPene(NumberUtil.add(rwaDo.getRwaAmpPene(), orgRwaDo.getRwaAmpPene()));
/* 964 */     rwaDo.setRwaAmpAba(NumberUtil.add(rwaDo.getRwaAmpAba(), orgRwaDo.getRwaAmpAba()));
/* 965 */     rwaDo.setRwaAmpF1250(NumberUtil.add(rwaDo.getRwaAmpF1250(), orgRwaDo.getRwaAmpF1250()));
/* 966 */     rwaDo.setRwaAmpCp(NumberUtil.add(rwaDo.getRwaAmpCp(), orgRwaDo.getRwaAmpCp()));
/* 967 */     rwaDo.setCvaAmp(NumberUtil.add(rwaDo.getCvaAmp(), orgRwaDo.getCvaAmp()));
/* 968 */     rwaDo.setRwaAmpAa(NumberUtil.add(rwaDo.getRwaAmpAa(), orgRwaDo.getRwaAmpAa()));
/* 969 */     return rwaDo;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\ResultService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */