/*      */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*      */ import cn.hutool.core.util.StrUtil;
/*      */ import com.amarsoft.batch.ItemPstSetter;
/*      */ import com.amarsoft.batch.JobListener;
/*      */ import com.amarsoft.batch.job.Job;
/*      */ import com.amarsoft.batch.job.JobBuilder;
/*      */ import com.amarsoft.batch.support.ChunkPolicy;
/*      */ import com.amarsoft.batch.support.ItemPst;
/*      */ import com.amarsoft.batch.support.MultiTableCursorItemReader;
/*      */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*      */ import com.amarsoft.rwa.engine.constant.CvaType;
/*      */ import com.amarsoft.rwa.engine.constant.EcColumn;
/*      */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*      */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*      */ import com.amarsoft.rwa.engine.constant.Identity;
/*      */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*      */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*      */ import com.amarsoft.rwa.engine.constant.JobType;
/*      */ import com.amarsoft.rwa.engine.constant.ResultDataType;
/*      */ import com.amarsoft.rwa.engine.constant.TaskType;
/*      */ import com.amarsoft.rwa.engine.constant.UnionType;
/*      */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*      */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*      */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*      */ import com.amarsoft.rwa.engine.entity.ThreadGroupDto;
/*      */ import com.amarsoft.rwa.engine.job.RwaJobListener;
/*      */ import com.amarsoft.rwa.engine.job.RwaWriter;
/*      */ import com.amarsoft.rwa.engine.job.processor.RwaProcessor;
/*      */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*      */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import javax.sql.DataSource;
/*      */ 
/*      */ @Component
/*      */ public class RwaJobBuilder {
/*   38 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.RwaJobBuilder.class);
/*      */ 
/*      */   
/*      */   @Autowired
/*      */   private DataSource dataSource;
/*      */ 
/*      */   
/*      */   public MultiTableCursorItemReader<Map<String, Object>> createReader(String jobId, String name, DataSource dataSource, LinkedHashMap<String, String> sqlMap, String associatedField) {
/*   46 */     return createReader(jobId, name, dataSource, sqlMap, associatedField, null, null);
/*      */   }
/*      */   
/*      */   public MultiTableCursorItemReader<Map<String, Object>> createReader(String jobId, String name, DataSource dataSource, LinkedHashMap<String, String> sqlMap, UnionType unionType) {
/*   50 */     String associatedField = unionType.getCode() + "_id";
/*   51 */     return createReader(jobId, name, dataSource, sqlMap, associatedField, null, null);
/*      */   }
/*      */   
/*      */   public MultiTableCursorItemReader<Map<String, Object>> createReader(String jobId, String name, DataSource dataSource, LinkedHashMap<String, String> sqlMap, ThreadGroupDto threadGroupDto) {
/*   55 */     String associatedField = threadGroupDto.getUnionType().getCode() + "_id";
/*   56 */     return createReader(jobId, name, dataSource, sqlMap, associatedField, threadGroupDto.getBeginId(), threadGroupDto.getEndId());
/*      */   }
/*      */ 
/*      */   
/*      */   public MultiTableCursorItemReader<Map<String, Object>> createReader(String jobId, String name, DataSource dataSource, LinkedHashMap<String, String> sqlMap, String associatedField, String beginId, String endId) {
/*   61 */     name = name + "-reader";
/*   62 */     MultiTableCursorItemReader<Map<String, Object>> reader = new MultiTableCursorItemReader();
/*   63 */     reader.setDataSource(dataSource);
/*   64 */     reader.setJobId(jobId);
/*   65 */     reader.setName(name);
/*   66 */     reader.setSqlMap(sqlMap);
/*   67 */     reader.setAssociatedField(associatedField.toUpperCase());
/*   68 */     reader.setIgnoreWarnings(true);
/*   69 */     reader.setRowGetter((MultiTableRowGetter)new ColumnMapMtRowGetter());
/*   70 */     reader.setBeginId(beginId);
/*   71 */     reader.setEndId(endId);
/*   72 */     return reader;
/*      */   }
/*      */   
/*      */   public RwaWriter<Map<String, Object>> createWriter(String jobId, DataSource dataSource, boolean isCountNumber, Map<String, ItemPst> pstMap) {
/*   76 */     RwaWriter<Map<String, Object>> writer = new RwaWriter(jobId, dataSource, pstMap, isCountNumber);
/*   77 */     return writer;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public Step createRwaStep(String name, String jobId, MultiTableCursorItemReader<Map<String, Object>> reader, RwaProcessor processor, RwaWriter<Map<String, Object>> writer) {
/*   83 */     ChunkPolicy policy = new ChunkPolicy(JobUtils.chunkSize.intValue());
/*   84 */     return (new StepBuilder()).name(name + "-step")
/*   85 */       .chunk(policy)
/*   86 */       .reader((ItemReader)reader)
/*   87 */       .readListener((ItemReadListener)new RwaReadListener(jobId, policy, JobUtils.chunkSize.intValue()))
/*   88 */       .processor((ItemProcessor)processor)
/*   89 */       .processListener((ItemProcessListener)new RwaProcessListener(jobId))
/*   90 */       .writer((ItemWriter)writer)
/*   91 */       .writeListener((ItemWriteListener)new RwaWriteListener(jobId, writer.isCountNumber()))
/*   92 */       .build();
/*      */   }
/*      */   
/*      */   public Job createJob(JobInfoDto jobInfo) {
/*   96 */     Job job = null;
/*   97 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobInfo.getJobType().ordinal()]) {
/*      */       case 1:
/*   99 */         job = nrJob(jobInfo);
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
/*  122 */         return job;case 2: job = reJob(jobInfo); return job;case 3: job = absJob(jobInfo); return job;case 4: job = ampJob(jobInfo); return job;case 5: job = diJob(jobInfo); return job;case 6: job = sftJob(jobInfo); return job;case 7: job = dfJob(jobInfo); return job;
/*      */     } 
/*      */     throw new JobParameterException("非法作业类型, jobType=" + jobInfo.getJobType());
/*      */   } public Job nrJob(JobInfoDto jobInfo) {
/*  126 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType() + "-" + jobInfo.getSubThreadGroup().getUnionType() + "-" + jobInfo.getSubThreadGroup().getThreadId();
/*  127 */     return (new JobBuilder()).name(name + "-job")
/*  128 */       .next(createRwaStep(name, jobInfo.getJobId(), 
/*  129 */           createReader(jobInfo.getJobId(), name, this.dataSource, 
/*  130 */             getNrSqlMap(jobInfo, jobInfo.getSubThreadGroup().getUnionType(), Identity.NO), jobInfo.getSubThreadGroup()), (RwaProcessor)new NonRetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, jobInfo
/*  131 */             .getSubThreadGroup().getUnionType()), 
/*  132 */           createWriter(jobInfo.getJobId(), this.dataSource, true, getNrResultSetterMap(jobInfo))))
/*      */       
/*  134 */       .listener((JobListener)new RwaJobListener(jobInfo))
/*  135 */       .build();
/*      */   }
/*      */   
/*      */   public String getNrExposureSql(TaskType taskType, UnionType unionType, String dataBatchNo, String consolidateFlag, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  139 */     String sql = null;
/*  140 */     if (RwaUtils.isSingle(taskType)) {
/*  141 */       sql = "select e.group_id, e.exposure_id, e.client_id, e.org_id, e.area_type, e.asset_type, e.instruments_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.sprv_tran_type, e.reva_frequency, e.currency, e.asset_balance, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.risk_classify, e.exposure_status, e.provision, e.off_business_type, e.off_business_subtype, e.uncond_cancel_flag, e.ccf_airb, e.default_flag, e.beel, e.default_lgd, e.pd, e.lgd_airb, e.maturity_airb, e.ead_airb, e.claims_level, e.loan_bond_flag, e.hml_flag, e.mort_add_loan_flag, e.lc_trade_type, e.gov_bond_type, e.cb_comm_trade_flag, e.pf_phase, e.sprv_rating, e.is_volatility, e.is_prudential, e.is_virtual_dependency, e.ltv, e.amp_flag, e.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc, e.secu_issue_rating, e.secu_issue_maturity, e.real_property_flag, e.debt_asset_time_type, e.equity_invest_object, e.equity_invest_cause, e.is_depend_bank_prof, e.is_dvp, e.delayed_trad_days, e.abs_ua_flag, e.abs_product_id, e.client_type, e.or_rating, e.client_external_rating, e.investment_grade_flag, e.scra_result, e.is_consolidated_sub, e.sib_flag, e.annual_sale, e.core_market_party_flag, e.is_main_bank, e.customer_size from RWA_ESI_GE_Exposure e left join RWA_ESI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = e.exposure_id where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}";
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
/*  161 */       return SqlBuilder.create(sql)
/*  162 */         .setString("dataBatchNo", dataBatchNo)
/*  163 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  164 */         .setString("dataType", JobType.NR.getCode())
/*  165 */         .isNull("e", "group_id", (unionType == UnionType.EXPOSURE))
/*  166 */         .build();
/*      */     } 
/*  168 */     sql = "select g.group_id, e.exposure_id, e.contract_id, e.client_id, e.org_id, e.area_type, e.industry_id, e.business_line, #{ec} e.asset_type, e.instruments_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.sprv_tran_type, e.reva_frequency, e.currency, e.asset_balance, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.risk_classify, e.exposure_status, e.provision, e.off_business_type, e.off_business_subtype, e.uncond_cancel_flag, e.ccf_level, e.ccf_airb, e.default_flag, e.beel, e.default_lgd, e.model_id, e.rating, e.pd, e.lgd_level, e.lgd_airb, e.maturity_airb, e.ead_airb, e.claims_level, e.loan_bond_flag, e.hml_flag, e.mort_add_loan_flag, e.lc_trade_type, e.gov_bond_type, e.cb_comm_trade_flag, e.pf_phase, e.sprv_rating, e.is_volatility, e.is_prudential, e.is_virtual_dependency, e.ltv, e.amp_flag, e.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc, e.rating_duration_type, e.secu_issue_rating, e.secu_issue_maturity, e.real_property_flag, e.debt_asset_time_type, e.equity_invest_object, e.equity_invest_cause, e.is_depend_bank_prof, e.is_dvp, e.delayed_trad_days, e.abs_ua_flag, e.abs_product_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.core_market_party_flag, c.is_main_bank, c.customer_size from RWA_EI_NR_Exposure e left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = e.client_id left join RWA_EI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = e.exposure_id left join RWA_ER_NR_ExpoGroup g   on g.data_batch_no = #{dataBatchNo} and g.exposure_id = e.exposure_id where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = #{absUaFlag} ";
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
/*  191 */     return SqlBuilder.create(sql)
/*  192 */       .condition("", RwaUtils.generateConditionSql(rangeList, null))
/*  193 */       .setString("dataBatchNo", dataBatchNo)
/*  194 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  195 */       .isNull("g", "group_id", (unionType == UnionType.EXPOSURE))
/*      */       
/*  197 */       .condition(SqlBuilder.createQueryCondition(null, "e", "is_include_cons", null, consolidateFlag, 
/*  198 */           StrUtil.equals(consolidateFlag, Identity.YES.getCode())))
/*  199 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.NRE)).clear("ec")
/*  200 */       .build();
/*      */   }
/*      */   
/*      */   public String getNrRelevanceSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  204 */     String sql = null;
/*  205 */     if (RwaUtils.isSingle(taskType)) {
/*  206 */       sql = "select r.exposure_id, r.mitigation_id, r.mitigation_type, r.is_positive_correlation, r.group_id from RWA_ESI_GE_EMRelevance r where r.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo}  and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType})";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  211 */       return SqlBuilder.create(sql)
/*  212 */         .setString("dataBatchNo", dataBatchNo)
/*  213 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  214 */         .setString("dataType", JobType.NR.getCode())
/*  215 */         .isNull("r", "group_id", (unionType == UnionType.EXPOSURE))
/*  216 */         .build();
/*      */     } 
/*  218 */     sql = "select r.exposure_id, r.mitigation_id, r.mitigation_type, r.is_positive_correlation, g.group_id from RWA_EI_NR_EMRelevance r left join RWA_ER_NR_ExpoGroup g   on g.data_batch_no = #{dataBatchNo} and g.exposure_id = r.exposure_id where r.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_NR_Exposure e where e.data_batch_no = #{dataBatchNo}  and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range})";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  225 */     return SqlBuilder.create(sql)
/*  226 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/*  227 */       .setString("dataBatchNo", dataBatchNo)
/*  228 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  229 */       .isNull("g", "group_id", (unionType == UnionType.EXPOSURE))
/*  230 */       .build();
/*      */   }
/*      */   
/*      */   public String getNrCollateralSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  234 */     String sql = null;
/*  235 */     if (RwaUtils.isSingle(taskType)) {
/*  236 */       if (unionType == UnionType.GROUP) {
/*  237 */         sql = "select cl.group_id, cl.exposure_count, cl.collateral_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.is_main_index, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_qual_nr, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, cl.issuer_id as client_id, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub, cl.issuer_is_main_bank from RWA_ESI_GE_Collateral cl where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_EMRelevance r  join RWA_ESI_GE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id  and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = cl.collateral_id) and cl.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  254 */         sql = "select cl.exposure_id, cl.exposure_count, cl.collateral_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.is_main_index, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_qual_nr, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, cl.issuer_id as client_id, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub, cl.issuer_is_main_bank from RWA_ESI_GE_Collateral cl where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = cl.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType} ) and cl.group_id is null";
/*      */       } 
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
/*  268 */       return SqlBuilder.create(sql)
/*  269 */         .setString("dataBatchNo", dataBatchNo)
/*  270 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  271 */         .setString("dataType", JobType.NR.getCode())
/*  272 */         .build();
/*      */     } 
/*  274 */     if (unionType == UnionType.GROUP) {
/*  275 */       sql = "select g.group_id, g.exposure_count, cl.collateral_id, cl.issuer_id, cl.provider_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.is_main_index, cl.rating_duration_type, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_qual_nr, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, c.client_id, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.is_main_bank as issuer_is_main_bank from RWA_EI_NR_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id left join RWA_ER_NR_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = cl.collateral_id where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_NR_EMRelevance r  join RWA_EI_NR_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = cl.collateral_id) and g.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  295 */       sql = "select g.exposure_id, g.exposure_count, cl.collateral_id, cl.issuer_id, cl.provider_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.is_main_index, cl.rating_duration_type, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_qual_nr, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, c.client_id, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.is_main_bank as issuer_is_main_bank from RWA_EI_NR_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id left join RWA_ER_NR_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = cl.collateral_id where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_NR_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}) and g.group_id is null";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  313 */     return SqlBuilder.create(sql)
/*  314 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/*  315 */       .setString("dataBatchNo", dataBatchNo)
/*  316 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  317 */       .build();
/*      */   }
/*      */   
/*      */   public String getNrGuaranteeSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  321 */     String sql = null;
/*  322 */     if (RwaUtils.isSingle(taskType)) {
/*  323 */       if (unionType == UnionType.GROUP) {
/*  324 */         sql = "select gt.group_id, gt.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.is_apply_firb, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.start_date, gt.due_date, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, gt.pd, gt.guarantor_id as client_id, gt.client_type, gt.or_rating, gt.client_external_rating, gt.investment_grade_flag, gt.scra_result, gt.is_consolidated_sub, gt.sib_flag, gt.annual_sale, gt.guarantor_expo_type, gt.is_main_bank from RWA_ESI_GE_Guarantee gt where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_EMRelevance r  join RWA_ESI_GE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id  and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = gt.guarantee_id) and gt.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  339 */         sql = "select gt.exposure_id, gt.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.is_apply_firb, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.start_date, gt.due_date, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, gt.pd, gt.guarantor_id as client_id, gt.client_type, gt.or_rating, gt.client_external_rating, gt.investment_grade_flag, gt.scra_result, gt.is_consolidated_sub, gt.sib_flag, gt.annual_sale, gt.guarantor_expo_type, gt.is_main_bank from RWA_ESI_GE_Guarantee gt where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = gt.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType} ) and gt.group_id is null";
/*      */       } 
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
/*  351 */       return SqlBuilder.create(sql)
/*  352 */         .setString("dataBatchNo", dataBatchNo)
/*  353 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  354 */         .setString("dataType", JobType.NR.getCode())
/*  355 */         .build();
/*      */     } 
/*  357 */     if (unionType == UnionType.GROUP) {
/*  358 */       sql = "select g.group_id, g.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.is_apply_firb, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.start_date, gt.due_date, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, c.model_id, c.rating, c.pd, c.client_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.exposure_type_irb as guarantor_expo_type, c.is_main_bank from RWA_EI_NR_Guarantee gt left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = gt.guarantor_id left join RWA_ER_NR_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = gt.guarantee_id where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_NR_EMRelevance r  join RWA_EI_NR_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = gt.guarantee_id) and g.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  376 */       sql = "select g.exposure_id, g.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.is_apply_firb, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.start_date, gt.due_date, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, c.model_id, c.rating, c.pd, c.client_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.exposure_type_irb as guarantor_expo_type, c.is_main_bank from RWA_EI_NR_Guarantee gt left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = gt.guarantor_id left join RWA_ER_NR_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = gt.guarantee_id where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_NR_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}) and g.group_id is null";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  392 */     return SqlBuilder.create(sql)
/*  393 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/*  394 */       .setString("dataBatchNo", dataBatchNo)
/*  395 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  396 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getNrSqlMap(JobInfoDto jobInfo, UnionType unionType, Identity absUaFlag) {
/*  400 */     TaskType taskType = jobInfo.getTaskType();
/*  401 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(jobInfo.getSchemeId());
/*  402 */     String dataBatchNo = jobInfo.getDataBatchNo();
/*  403 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/*  404 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/*      */     
/*  406 */     sqlMap.put(InterfaceDataType.EXPOSURE.getCode(), getNrExposureSql(taskType, unionType, dataBatchNo, jobInfo.getTaskConfigDo().getConsolidateFlag(), absUaFlag, rangeList));
/*      */     
/*  408 */     sqlMap.put(InterfaceDataType.RELEVANCE.getCode(), getNrRelevanceSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/*      */     
/*  410 */     sqlMap.put(InterfaceDataType.COLLATERAL.getCode(), getNrCollateralSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/*      */     
/*  412 */     sqlMap.put(InterfaceDataType.GUARANTEE.getCode(), getNrGuaranteeSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/*  413 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public Map<String, ItemPst> getNrResultSetterMap(JobInfoDto jobInfo) {
/*  417 */     TaskType taskType = jobInfo.getTaskType();
/*  418 */     String resultNo = jobInfo.getResultNo();
/*  419 */     String dataBatchNo = jobInfo.getDataBatchNo();
/*  420 */     String jobId = jobInfo.getJobId();
/*  421 */     boolean isSubTable = jobInfo.isSubTable();
/*  422 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/*  423 */     pstSetterMap.put(ResultDataType.EXPOSURE.getCode(), getNrExposureSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/*  424 */     pstSetterMap.put(ResultDataType.DETAIL.getCode(), getNrDetailSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/*  425 */     pstSetterMap.put(ResultDataType.MITIGATION.getCode(), getNrMitigationSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/*  426 */     pstSetterMap.put(ResultDataType.AMP.getCode(), getAmpExposureSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId, jobInfo.getJobType()));
/*  427 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public ItemPst getNrDetailSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/*  431 */     String sql = "insert into #{result_table}(result_no, detail_no, data_batch_no, approach, exposure_id, contract_id, client_id, client_type, org_id, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, currency, asset_balance, original_maturity, residual_maturity, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ccf, risk_classify, provision, ltv, ead, she, he, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, kcr, rw, is_result, mitigated_flag, fm_reason, uncovered_ea, mitigation_id, is_alone, mitigation_client_id, mitigation_client_cate, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, mitigation_orig_value, mitigation_ct_value, mitigation_ct_kcr, mitigation_use_amount, mitigation_resi_value, mitigation_shc, mitigation_hc, mitigation_orig_rw, hfx, ht, is_exempt_rw_line, value_fc, covered_ea, mitigated_ea, mitigated_model_id, mitigated_rating, mitigated_pd, mitigated_lgd, mitigated_correlation, mitigated_bma, mitigated_kcr, mitigated_rw, el, ela, rwa_mb, rwa_ma, formula_no, group_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,         ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/*  443 */     String tableName = null;
/*  444 */     if (RwaUtils.isSingle(taskType)) {
/*  445 */       tableName = "RWA_ESR_GE_Detail";
/*  446 */     } else if (isSubTable) {
/*  447 */       tableName = "T_NRD_" + jobId;
/*      */     } else {
/*  449 */       tableName = "RWA_ER_NR_Detail";
/*      */     } 
/*  451 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/*  452 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */   
/*      */   public ItemPst getNrExposureSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/*  544 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, contract_id, client_id, client_type, approach, org_id, amp_flag, amp_id, amp_book_type, amp_belong, amp_lr, is_tp_calc, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, currency, risk_classify, exposure_status, provision, claims_level, reva_frequency, tm, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ccf, sprv_rating, ltv, provision_prop, provision_ded, ead, she, he, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, covered_ea, uncovered_ea, el, ela, walgd, wakcr, warw, rwa_mb, rwa_ma, rwa_um, formula_no, group_id, sort_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  554 */     String tableName = null;
/*  555 */     if (RwaUtils.isSingle(taskType)) {
/*  556 */       tableName = "RWA_ESR_GE_Exposure";
/*  557 */     } else if (isSubTable) {
/*  558 */       tableName = "T_NRE_" + jobId;
/*      */     } else {
/*  560 */       tableName = "RWA_ER_NR_Exposure";
/*      */     } 
/*  562 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/*  563 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getNrMitigationSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/*  645 */     String sql = "insert into #{result_table}(result_no, mitigation_id, data_batch_no, client_id, client_type, approach, is_alone, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, guarantor_expo_type, mitigation_amount, currency, reva_frequency, original_maturity, residual_maturity, guar_residual_maturity, sh, min_collateral_level, over_collateral_level, model_id, rating, pd, lgd, kcr, rw, unmitigated_amount, mitigated_amount, covered_ea, mitigated_effect, group_id, exposure_id, sort_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  651 */     String tableName = null;
/*  652 */     if (RwaUtils.isSingle(taskType)) {
/*  653 */       tableName = "RWA_ESR_GE_Mitigation";
/*  654 */     } else if (isSubTable) {
/*  655 */       tableName = "T_NRM_" + jobId;
/*      */     } else {
/*  657 */       tableName = "RWA_ER_NR_Mitigation";
/*      */     } 
/*  659 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/*  660 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getAmpExposureSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId, JobType jobType) {
/*  708 */     String sql = "insert into #{result_table}(result_no, exposure_id, credit_risk_data_type, data_batch_no, contract_id, client_id, client_type, approach, org_id, amp_id, amp_type, amp_approach, amp_book_type, amp_belong, amp_lr, is_tp_calc, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, notional_principal, currency, provision, exposure_rpt_item_wa, off_rpt_item_wa, ccf, provision_prop, provision_ded, ead, default_flag, rw, warw, rwa, rwa_cp, cva, rwa_aa, rwa_adj, amp_under_asset_rst, hold_net_asset, group_id, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  715 */     String tableName = null;
/*  716 */     if (RwaUtils.isSingle(taskType)) {
/*  717 */       tableName = "RWA_ESR_Amp_Exposure";
/*  718 */     } else if (isSubTable) {
/*  719 */       tableName = "T_AMP_" + jobId;
/*      */     } else {
/*  721 */       tableName = "RWA_ER_Amp_Exposure";
/*      */     } 
/*  723 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/*  724 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, jobType, dataBatchNo));
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
/*      */   public ItemPst getAbsAmpExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/*  783 */     String sql = "insert into #{result_table}(result_no, exposure_id, credit_risk_data_type, data_batch_no, contract_id, client_id, client_type, approach, org_id, amp_id, amp_type, amp_approach, amp_book_type, amp_belong, amp_lr, is_tp_calc, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, notional_principal, currency, provision, exposure_rpt_item_wa, off_rpt_item_wa, ccf, provision_prop, provision_ded, ead, default_flag, rw, warw, rwa, rwa_cp, cva, rwa_aa, rwa_adj, amp_under_asset_rst, hold_net_asset, group_id, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  790 */     String tableName = null;
/*  791 */     if (RwaUtils.isSingle(taskType)) {
/*  792 */       tableName = "RWA_ESR_Amp_Exposure";
/*      */     } else {
/*  794 */       tableName = "RWA_ER_Amp_Exposure";
/*      */     } 
/*  796 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/*  797 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public Job reJob(JobInfoDto jobInfo) {
/*  856 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType() + "-" + jobInfo.getSubThreadGroup().getUnionType() + "-" + jobInfo.getSubThreadGroup().getThreadId();
/*  857 */     return (new JobBuilder()).name(name + "-job")
/*  858 */       .next(createRwaStep(name, jobInfo.getJobId(), 
/*  859 */           createReader(jobInfo.getJobId(), name, this.dataSource, 
/*  860 */             getReSqlMap(jobInfo, jobInfo.getSubThreadGroup().getUnionType(), Identity.NO), jobInfo.getSubThreadGroup()), (RwaProcessor)new RetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, jobInfo
/*  861 */             .getSubThreadGroup().getUnionType()), 
/*  862 */           createWriter(jobInfo.getJobId(), this.dataSource, true, getReResultSetterMap(jobInfo))))
/*      */       
/*  864 */       .listener((JobListener)new RwaJobListener(jobInfo))
/*  865 */       .build();
/*      */   }
/*      */   
/*      */   public String getReExposureSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  869 */     String sql = null;
/*  870 */     if (RwaUtils.isSingle(taskType)) {
/*  871 */       sql = "select e.group_id, e.exposure_id, e.client_id, e.org_id, e.area_type, e.asset_type, e.supervision_retail_flag, e.qual_trader_flag, e.customer_size, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.currency, e.income_currency, e.asset_balance, e.total_limit, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.risk_classify, e.exposure_status, e.provision, e.off_business_type, e.off_business_subtype, e.uncond_cancel_flag, e.default_flag, e.beel, e.default_lgd, e.pd, e.lgd_airb as lgd, e.ccf_airb as ccf, e.ead_airb as ead_irb, e.hml_flag, e.mort_add_loan_flag, e.is_prudential, e.is_virtual_dependency, e.ltv, e.abs_ua_flag, e.abs_product_id, e.amp_flag, e.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc from RWA_ESI_GE_Exposure e left join RWA_ESI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = e.exposure_id where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}";
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
/*  884 */       return SqlBuilder.create(sql)
/*  885 */         .setString("dataBatchNo", dataBatchNo)
/*  886 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  887 */         .setString("dataType", JobType.RE.getCode())
/*  888 */         .isNull("e", "group_id", (unionType == UnionType.EXPOSURE))
/*  889 */         .build();
/*      */     } 
/*  891 */     sql = "select g.group_id, e.exposure_id, e.contract_id, e.client_id, e.org_id, e.area_type, e.industry_id, e.business_line, #{ec} e.asset_type, e.supervision_retail_flag, e.qual_trader_flag, e.customer_size, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.currency, e.income_currency, e.asset_balance, e.total_limit, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.risk_classify, e.exposure_status, e.provision, e.off_business_type, e.off_business_subtype, e.uncond_cancel_flag, e.default_flag, e.beel, e.default_lgd, e.pd_model_id, e.pd_pool_id, e.pd, e.lgd_model_id, e.lgd_pool_id, e.lgd, e.ccf_model_id, e.ccf_pool_id, e.ccf, e.ead_irb, e.hml_flag, e.mort_add_loan_flag, e.is_owner_occupied, e.is_prudential, e.is_virtual_dependency, e.ltv, e.abs_ua_flag, e.abs_product_id, e.amp_flag, e.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc from RWA_EI_RE_Exposure e left join RWA_EI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = e.exposure_id left join RWA_ER_RE_ExpoGroup g   on g.data_batch_no = #{dataBatchNo} and g.exposure_id = e.exposure_id where e.data_batch_no = #{dataBatchNo} and e.abs_ua_flag = #{absUaFlag}";
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
/*  906 */     return SqlBuilder.create(sql)
/*  907 */       .condition("", RwaUtils.generateConditionSql(rangeList, null))
/*  908 */       .setString("dataBatchNo", dataBatchNo)
/*  909 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  910 */       .isNull("g", "group_id", (unionType == UnionType.EXPOSURE))
/*  911 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.REE)).clear("ec")
/*  912 */       .build();
/*      */   }
/*      */   
/*      */   public String getReRelevanceSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  916 */     String sql = null;
/*  917 */     if (RwaUtils.isSingle(taskType)) {
/*  918 */       sql = "select r.exposure_id, r.mitigation_id, r.mitigation_type, r.is_positive_correlation, r.group_id from RWA_ESI_GE_EMRelevance r where r.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo}  and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType})";
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  923 */       return SqlBuilder.create(sql)
/*  924 */         .setString("dataBatchNo", dataBatchNo)
/*  925 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  926 */         .setString("dataType", JobType.RE.getCode())
/*  927 */         .isNull("r", "group_id", (unionType == UnionType.EXPOSURE))
/*  928 */         .build();
/*      */     } 
/*  930 */     sql = "select r.exposure_id, r.mitigation_id, r.mitigation_type, r.is_positive_correlation, g.group_id from RWA_EI_RE_EMRelevance r left join RWA_ER_RE_ExpoGroup g   on g.data_batch_no = #{dataBatchNo} and g.exposure_id = r.exposure_id where r.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_RE_Exposure e where e.data_batch_no = #{dataBatchNo}  and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range})";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  937 */     return SqlBuilder.create(sql)
/*  938 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/*  939 */       .setString("dataBatchNo", dataBatchNo)
/*  940 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  941 */       .isNull("g", "group_id", (unionType == UnionType.EXPOSURE))
/*  942 */       .build();
/*      */   }
/*      */   
/*      */   public String getReCollateralSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/*  946 */     String sql = null;
/*  947 */     if (RwaUtils.isSingle(taskType)) {
/*  948 */       if (unionType == UnionType.GROUP) {
/*  949 */         sql = "select cl.group_id, cl.exposure_count, cl.collateral_id, cl.issuer_id, cl.is_apply_wa, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, cl.issuer_id as client_id, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub, cl.issuer_is_main_bank from RWA_ESI_GE_Collateral cl where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_EMRelevance r  join RWA_ESI_GE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id  and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = cl.collateral_id) and cl.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*  965 */         sql = "select cl.exposure_id, cl.exposure_count, cl.collateral_id, cl.issuer_id, cl.is_apply_wa, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, cl.issuer_id as client_id, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub, cl.issuer_is_main_bank from RWA_ESI_GE_Collateral cl where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = cl.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType} ) and cl.group_id is null";
/*      */       } 
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
/*  978 */       return SqlBuilder.create(sql)
/*  979 */         .setString("dataBatchNo", dataBatchNo)
/*  980 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/*  981 */         .setString("dataType", JobType.RE.getCode())
/*  982 */         .build();
/*      */     } 
/*  984 */     if (unionType == UnionType.GROUP) {
/*  985 */       sql = "select g.group_id, g.exposure_count, cl.collateral_id, cl.issuer_id, cl.provider_id, cl.is_apply_wa, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, c.client_id, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.is_main_bank as issuer_is_main_bank from RWA_EI_RE_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id left join RWA_ER_RE_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = cl.collateral_id where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_RE_EMRelevance r  join RWA_EI_RE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = cl.collateral_id) and g.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1004 */       sql = "select g.exposure_id, g.exposure_count, cl.collateral_id, cl.issuer_id, cl.provider_id, cl.is_apply_wa, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.guar_residual_maturity, cl.real_estate_type, cl.is_prudential, cl.initial_value, cl.is_full_cover_em, c.client_id, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.is_main_bank as issuer_is_main_bank from RWA_EI_RE_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id left join RWA_ER_RE_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = cl.collateral_id where cl.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_RE_Exposure e  where e.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}) and g.group_id is null";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1021 */     return SqlBuilder.create(sql)
/* 1022 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/* 1023 */       .setString("dataBatchNo", dataBatchNo)
/* 1024 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/* 1025 */       .build();
/*      */   }
/*      */   
/*      */   public String getReGuaranteeSql(TaskType taskType, UnionType unionType, String dataBatchNo, Identity absUaFlag, List<TaskRangeDo> rangeList) {
/* 1029 */     String sql = null;
/* 1030 */     if (RwaUtils.isSingle(taskType)) {
/* 1031 */       if (unionType == UnionType.GROUP) {
/* 1032 */         sql = "select gt.group_id, gt.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, gt.guarantor_id as client_id, gt.client_type, gt.or_rating, gt.client_external_rating, gt.investment_grade_flag, gt.scra_result, gt.is_consolidated_sub, gt.sib_flag, gt.annual_sale, gt.guarantor_expo_type, gt.is_main_bank from RWA_ESI_GE_Guarantee gt where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_EMRelevance r  join RWA_ESI_GE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id  and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = gt.guarantee_id) and gt.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       }
/*      */       else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1047 */         sql = "select gt.exposure_id, gt.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, gt.guarantor_id as client_id, gt.client_type, gt.or_rating, gt.client_external_rating, gt.investment_grade_flag, gt.scra_result, gt.is_consolidated_sub, gt.sib_flag, gt.annual_sale, gt.guarantor_expo_type, gt.is_main_bank from RWA_ESI_GE_Guarantee gt where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_ESI_GE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = gt.exposure_id and e.abs_ua_flag = #{absUaFlag} and e.credit_risk_data_type = #{dataType} ) and gt.group_id is null";
/*      */       } 
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
/* 1059 */       return SqlBuilder.create(sql)
/* 1060 */         .setString("dataBatchNo", dataBatchNo)
/* 1061 */         .setString("absUaFlag", (ICodeEnum)absUaFlag)
/* 1062 */         .setString("dataType", JobType.RE.getCode())
/* 1063 */         .build();
/*      */     } 
/* 1065 */     if (unionType == UnionType.GROUP) {
/* 1066 */       sql = "select g.group_id, g.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, c.client_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.exposure_type_irb as guarantor_expo_type, c.is_main_bank from RWA_EI_RE_Guarantee gt left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = gt.guarantor_id left join RWA_ER_RE_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = gt.guarantee_id where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_RE_EMRelevance r  join RWA_EI_RE_Exposure e  on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}  where r.data_batch_no = #{dataBatchNo} and r.mitigation_id = gt.guarantee_id) and g.group_id is not null";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1084 */       sql = "select g.exposure_id, g.exposure_count, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, c.client_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.exposure_type_irb as guarantor_expo_type, c.is_main_bank from RWA_EI_RE_Guarantee gt left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = gt.guarantor_id left join RWA_ER_RE_MitiGroup g   on g.data_batch_no = #{dataBatchNo} and g.mitigation_id = gt.guarantee_id where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_RE_Exposure e where e.data_batch_no = #{dataBatchNo} and e.exposure_id = g.exposure_id and e.abs_ua_flag = #{absUaFlag} #{range}) and g.group_id is null ";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1100 */     return SqlBuilder.create(sql)
/* 1101 */       .condition("range", RwaUtils.generateConditionSql(rangeList, null))
/* 1102 */       .setString("dataBatchNo", dataBatchNo)
/* 1103 */       .setString("absUaFlag", (ICodeEnum)absUaFlag)
/* 1104 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getReSqlMap(JobInfoDto jobInfo, UnionType unionType, Identity absUaFlag) {
/* 1108 */     TaskType taskType = jobInfo.getTaskType();
/* 1109 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1110 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/* 1111 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/* 1112 */     sqlMap.put(InterfaceDataType.EXPOSURE.getCode(), getReExposureSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/* 1113 */     sqlMap.put(InterfaceDataType.RELEVANCE.getCode(), getReRelevanceSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/* 1114 */     sqlMap.put(InterfaceDataType.COLLATERAL.getCode(), getReCollateralSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/* 1115 */     sqlMap.put(InterfaceDataType.GUARANTEE.getCode(), getReGuaranteeSql(taskType, unionType, dataBatchNo, absUaFlag, rangeList));
/* 1116 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public Map<String, ItemPst> getReResultSetterMap(JobInfoDto jobInfo) {
/* 1120 */     TaskType taskType = jobInfo.getTaskType();
/* 1121 */     String resultNo = jobInfo.getResultNo();
/* 1122 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1123 */     String jobId = jobInfo.getJobId();
/* 1124 */     boolean isSubTable = jobInfo.isSubTable();
/* 1125 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 1126 */     pstSetterMap.put(ResultDataType.EXPOSURE.getCode(), getReExposureSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/* 1127 */     pstSetterMap.put(ResultDataType.DETAIL.getCode(), getReDetailSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/* 1128 */     pstSetterMap.put(ResultDataType.MITIGATION.getCode(), getReMitigationSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId));
/* 1129 */     pstSetterMap.put(ResultDataType.AMP.getCode(), getAmpExposureSetter(taskType, resultNo, dataBatchNo, isSubTable, jobId, jobInfo.getJobType()));
/* 1130 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public ItemPst getReDetailSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/* 1134 */     String sql = "insert into #{result_table}(result_no, detail_no, data_batch_no, approach, exposure_id, contract_id, client_id, org_id, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, currency, asset_balance, original_maturity, residual_maturity, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ccf, risk_classify, provision, ltv, ead, default_flag, beel, default_lgd, pd, lgd, correlation, kcr, rw, is_result, mitigated_flag, fm_reason, uncovered_ea, mitigation_id, is_alone, mitigation_client_id, mitigation_client_cate, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, mitigation_orig_value, mitigation_ct_value, mitigation_use_amount, mitigation_resi_value, mitigation_orig_rw, hfx, is_exempt_rw_line, value_fc, covered_ea, mitigated_ea, mitigated_rw, el, ela, rwa_mb, rwa_ma, formula_no, group_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 1145 */     String tableName = null;
/* 1146 */     if (RwaUtils.isSingle(taskType)) {
/* 1147 */       tableName = "RWA_ESR_GE_Detail";
/* 1148 */     } else if (isSubTable) {
/* 1149 */       tableName = "T_RED_" + jobId;
/*      */     } else {
/* 1151 */       tableName = "RWA_ER_RE_Detail";
/*      */     } 
/* 1153 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 1154 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getReExposureSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/* 1229 */     if (RwaUtils.isSingle(taskType)) {
/* 1230 */       return getSingleReExposureSetter(resultNo, dataBatchNo);
/*      */     }
/* 1232 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, approach, contract_id, client_id, org_id, amp_flag, amp_id, amp_book_type, amp_belong, amp_lr, is_tp_calc, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, currency, risk_classify, exposure_status, provision, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ltv, provision_prop, provision_ded, ead, default_flag, beel, default_lgd, pd_model_id, pd_pool_id, pd, lgd_model_id, lgd_pool_id, lgd, ccf_model_id, ccf_pool_id, ccf, correlation, kcr, rw, covered_ea, uncovered_ea, el, ela, warw, rwa_mb, rwa_ma, rwa_um, formula_no, group_id, sort_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1242 */     String tableName = null;
/* 1243 */     if (isSubTable) {
/* 1244 */       tableName = "T_REE_" + jobId;
/*      */     } else {
/* 1246 */       tableName = "RWA_ER_RE_Exposure";
/*      */     } 
/* 1248 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 1249 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getSingleReExposureSetter(String resultNo, String dataBatchNo) {
/* 1324 */     String sql = "insert into RWA_ESR_GE_Exposure(result_no, exposure_id, data_batch_no, approach, contract_id, client_id, org_id, amp_flag, amp_id, amp_book_type, amp_belong, amp_lr, is_tp_calc, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, currency, risk_classify, exposure_status, provision, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ltv, provision_prop, provision_ded, ead, default_flag, beel, default_lgd, pd, lgd, ccf, correlation, kcr, rw, covered_ea, uncovered_ea, el, ela, warw, rwa_mb, rwa_ma, rwa_um, formula_no, group_id, sort_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1334 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemPst getReMitigationSetter(TaskType taskType, String resultNo, String dataBatchNo, boolean isSubTable, String jobId) {
/* 1403 */     String sql = "insert into #{result_table}(result_no, mitigation_id, data_batch_no, client_id, client_type, approach, is_alone, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, guarantor_expo_type, mitigation_amount, currency, original_maturity, residual_maturity, guar_residual_maturity, sh, rw, unmitigated_amount, mitigated_amount, covered_ea, mitigated_effect, group_id, exposure_id, sort_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1408 */     String tableName = null;
/* 1409 */     if (RwaUtils.isSingle(taskType)) {
/* 1410 */       tableName = "RWA_ESR_GE_Mitigation";
/* 1411 */     } else if (isSubTable) {
/* 1412 */       tableName = "T_REM_" + jobId;
/*      */     } else {
/* 1414 */       tableName = "RWA_ER_RE_Mitigation";
/*      */     } 
/* 1416 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 1417 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Job absJob(JobInfoDto jobInfo) {
/* 1457 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType();
/* 1458 */     IrbUncoveredProcess irbUncoveredProcess = IrbUncoveredProcess.CALCULATE;
/*      */     
/* 1460 */     JobBuilder jobBuilder = (new JobBuilder()).name(name + "-job");
/*      */     
/* 1462 */     if (containAbsOriginator(jobInfo.getRangeList(), Identity.NO)) {
/* 1463 */       jobBuilder.next(createRwaStep(name + "-abs-np", jobInfo.getJobId(), 
/* 1464 */             createReader(jobInfo.getJobId(), name + "-abs-np", this.dataSource, getAbsSqlMap(jobInfo, Identity.NO), "abs_product_id"), (RwaProcessor)new AbsProcessor(jobInfo, irbUncoveredProcess, Identity.NO), 
/*      */             
/* 1466 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 1469 */     if (containAbsOriginator(jobInfo.getRangeList(), Identity.YES)) {
/* 1470 */       jobBuilder.next(createRwaStep(name + "-abs-uanr-group", jobInfo.getJobId(), 
/* 1471 */             createReader(jobInfo.getJobId(), name + "-abs-uanr-group", this.dataSource, getNrSqlMap(jobInfo, UnionType.GROUP, Identity.YES), UnionType.GROUP), (RwaProcessor)new NonRetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, UnionType.GROUP), 
/*      */             
/* 1473 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsUaResultSetterMap(jobInfo))))
/* 1474 */         .next(createRwaStep(name + "-abs-uanr-expo", jobInfo.getJobId(), 
/* 1475 */             createReader(jobInfo.getJobId(), name + "-abs-uanr-expo", this.dataSource, getNrSqlMap(jobInfo, UnionType.EXPOSURE, Identity.YES), UnionType.EXPOSURE), (RwaProcessor)new NonRetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, UnionType.EXPOSURE), 
/*      */             
/* 1477 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsUaResultSetterMap(jobInfo))))
/* 1478 */         .next(createRwaStep(name + "-abs-uare-group", jobInfo.getJobId(), 
/* 1479 */             createReader(jobInfo.getJobId(), name + "-abs-uare-group", this.dataSource, getReSqlMap(jobInfo, UnionType.GROUP, Identity.YES), UnionType.GROUP), (RwaProcessor)new RetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, UnionType.GROUP), 
/*      */             
/* 1481 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsUaResultSetterMap(jobInfo))))
/* 1482 */         .next(createRwaStep(name + "-abs-uare-expo", jobInfo.getJobId(), 
/* 1483 */             createReader(jobInfo.getJobId(), name + "-abs-uare-expo", this.dataSource, getReSqlMap(jobInfo, UnionType.EXPOSURE, Identity.YES), UnionType.EXPOSURE), (RwaProcessor)new RetailProcessor(jobInfo, IrbUncoveredProcess.CALCULATE, UnionType.EXPOSURE), 
/*      */             
/* 1485 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsUaResultSetterMap(jobInfo))))
/*      */         
/* 1487 */         .next(createRwaStep(name + "-abs-pool", jobInfo.getJobId(), 
/* 1488 */             createReader(jobInfo.getJobId(), name + "-abs-pool", this.dataSource, getAbsSqlMap(jobInfo, Identity.YES), "abs_product_id"), (RwaProcessor)new AbsProcessor(jobInfo, irbUncoveredProcess, Identity.YES), 
/*      */             
/* 1490 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getAbsResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 1493 */     return jobBuilder.listener((JobListener)new RwaJobListener(jobInfo)).build();
/*      */   }
/*      */   
/*      */   public Job ampJob(JobInfoDto jobInfo) {
/* 1497 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType();
/* 1498 */     return (new JobBuilder()).name(name + "-job")
/* 1499 */       .next(createRwaStep(name, jobInfo.getJobId(), 
/* 1500 */           createReader(jobInfo.getJobId(), name, this.dataSource, getAmpSqlMap(jobInfo), "exposure_id"), (RwaProcessor)new AmpProcessor(jobInfo, IrbUncoveredProcess.CALCULATE), 
/*      */           
/* 1502 */           createWriter(jobInfo.getJobId(), this.dataSource, true, getAmpResultSetterMap(jobInfo))))
/*      */       
/* 1504 */       .listener((JobListener)new RwaJobListener(jobInfo))
/* 1505 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getAmpSqlMap(JobInfoDto jobInfo) {
/* 1509 */     TaskType taskType = jobInfo.getTaskType();
/* 1510 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1511 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/* 1512 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/*      */     
/* 1514 */     sqlMap.put(InterfaceDataType.EXPOSURE.getCode(), getAmpExposureSql(taskType, dataBatchNo, rangeList));
/*      */     
/* 1516 */     sqlMap.put(InterfaceDataType.AMP_INFO.getCode(), getAmpAmpAbaInfo(taskType, dataBatchNo, rangeList));
/* 1517 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public Map<String, ItemPst> getAmpResultSetterMap(JobInfoDto jobInfo) {
/* 1521 */     TaskType taskType = jobInfo.getTaskType();
/* 1522 */     String resultNo = jobInfo.getResultNo();
/* 1523 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1524 */     String jobId = jobInfo.getJobId();
/* 1525 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 1526 */     pstSetterMap.put(ResultDataType.EXPOSURE.getCode(), getAmpExposureSetter(taskType, resultNo, dataBatchNo, false, jobId, jobInfo.getJobType()));
/* 1527 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public String getAmpExposureSql(TaskType taskType, String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 1531 */     String sql = null;
/* 1532 */     if (RwaUtils.isSingle(taskType)) {
/* 1533 */       sql = "select e.exposure_id, e.amp_id, e.amp_approach, e.amp_type, e.amp_lr, e.is_tp_calc, e.book_type as amp_book_type, e.exposure_belong as amp_belong, e.org_id, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.currency, e.asset_balance, e.provision, e.default_flag from RWA_ESI_AMP_Exposure e where e.data_batch_no = #{dataBatchNo} and e.credit_risk_data_type = #{dataType} ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1540 */       return SqlBuilder.create(sql)
/* 1541 */         .setString("dataBatchNo", dataBatchNo)
/* 1542 */         .setString("dataType", JobType.AMP.getCode())
/* 1543 */         .build();
/*      */     } 
/* 1545 */     sql = "select e.exposure_id, e.amp_id, e.amp_approach, e.amp_type, e.amp_lr, e.is_tp_calc, #{ec} e.book_type as amp_book_type, e.exposure_belong as amp_belong, e.org_id, e.area_code, e.industry_id, e.business_line, e.business_type_code, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.sprv_tran_type, e.reva_frequency, e.currency, e.asset_balance, e.provision, e.original_maturity, e.residual_maturity, e.default_flag from RWA_EI_AMP_Exposure e where e.data_batch_no = #{dataBatchNo} and e.credit_risk_data_type = #{dataType} ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1552 */     return SqlBuilder.create(sql)
/* 1553 */       .condition("", RwaUtils.generateConditionSql(rangeList, null))
/* 1554 */       .setString("dataBatchNo", dataBatchNo)
/* 1555 */       .setString("dataType", JobType.AMP.getCode())
/* 1556 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.AMPE)).clear("ec")
/* 1557 */       .build();
/*      */   }
/*      */   
/*      */   public String getAmpAmpAbaInfo(TaskType taskType, String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 1561 */     String sql = null;
/* 1562 */     if (RwaUtils.isSingle(taskType)) {
/* 1563 */       sql = "select e.exposure_id, f.aba_asset_id, f.amp_id, f.asset_type_aba, f.min_invest_ratio, f.max_invest_ratio, f.investment_ratio, f.is_compliance_stc, f.tranche_level, f.thickness, f.external_rating, f.residual_maturity, f.notional_principal, f.mtm from RWA_ESI_Amp_Aba f join RWA_ESI_Amp_Exposure e   on e.data_batch_no = #{dataBatchNo} and f.amp_id = e.amp_id and e.credit_risk_data_type = #{dataType} where f.data_batch_no = #{dataBatchNo} ";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 1569 */       return SqlBuilder.create(sql)
/* 1570 */         .setString("dataBatchNo", dataBatchNo)
/* 1571 */         .setString("dataType", JobType.AMP.getCode())
/* 1572 */         .build();
/*      */     } 
/* 1574 */     sql = "select e.exposure_id, f.aba_asset_id, f.amp_id, f.asset_type_aba, f.min_invest_ratio, f.max_invest_ratio, f.investment_ratio, f.is_compliance_stc, f.tranche_level, f.thickness, f.external_rating, f.residual_maturity, f.notional_principal, f.mtm from RWA_EI_Amp_Aba f join RWA_EI_Amp_Exposure e   on e.data_batch_no = #{dataBatchNo} and f.amp_id = e.amp_id and e.credit_risk_data_type = #{dataType} where f.data_batch_no = #{dataBatchNo}";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1580 */     return SqlBuilder.create(sql)
/* 1581 */       .condition("", RwaUtils.generateConditionSql(rangeList, null))
/* 1582 */       .setString("dataBatchNo", dataBatchNo)
/* 1583 */       .setString("dataType", JobType.AMP.getCode())
/* 1584 */       .build();
/*      */   }
/*      */   
/*      */   public boolean containAbsOriginator(List<TaskRangeDo> rangeList, Identity isOriginator) {
/* 1588 */     if (CollUtil.isEmpty(rangeList)) {
/* 1589 */       return true;
/*      */     }
/* 1591 */     for (TaskRangeDo rangeDo : rangeList) {
/* 1592 */       if (StrUtil.equals(rangeDo.getCreditRiskDataType(), JobType.ABS.getCode()) && 
/* 1593 */         StrUtil.equals(rangeDo.getIsOriginator(), isOriginator.getCode())) {
/* 1594 */         return true;
/*      */       }
/*      */     } 
/* 1597 */     return false;
/*      */   }
/*      */   
/*      */   public String getAbsExposureSql(TaskType taskType, String dataBatchNo, Identity isOriginator, List<TaskRangeDo> rangeList) {
/* 1601 */     String sql = null;
/*      */     
/* 1603 */     if (RwaUtils.isSingle(taskType)) {
/* 1604 */       sql = "select a.abs_exposure_id, a.abs_product_id, a.securities_code, a.org_id, a.asset_type, a.exposure_type_wa, a.exposure_type_irb, a.exposure_belong, '2' as sprv_tran_type, 1 as reva_frequency, a.reabs_flag, a.bank_business_role, a.off_abs_biz_type, a.is_nfs_reflect_rating, a.qual_facility_flag, a.is_preferential_payment, a.is_uncond_cancel, a.is_compliance_stc, a.tranche_sn, a.tranche_level, a.tranche_starting_point, a.tranche_separation_point, a.thickness, a.external_rating, a.original_maturity, a.residual_maturity, a.asset_balance, a.tranche_prop, a.currency, a.provision, a.amp_flag, a.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc from RWA_ESI_ABS_Exposure a join RWA_ESI_ABS_Product p   on p.abs_product_id = a.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = #{isOriginator} left join RWA_ESI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = a.abs_exposure_id where a.data_batch_no = #{dataBatchNo}";
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
/* 1618 */       return SqlBuilder.create(sql)
/* 1619 */         .setString("dataBatchNo", dataBatchNo)
/* 1620 */         .setString("isOriginator", (ICodeEnum)isOriginator)
/* 1621 */         .build();
/*      */     } 
/*      */     
/* 1624 */     sql = "select a.abs_exposure_id, a.abs_product_id, a.securities_code, a.securities_name, a.org_id, a.business_line, #{ec} a.asset_type, a.exposure_type_wa, a.exposure_type_irb, a.exposure_belong, a.book_type, a.sprv_tran_type, a.reva_frequency, a.reabs_flag, a.bank_business_role, a.off_abs_biz_type, a.is_nfs_reflect_rating, a.qual_facility_flag, a.is_preferential_payment, a.is_uncond_cancel, a.is_compliance_stc, a.tranche_sn, a.tranche_level, a.tranche_starting_point, a.tranche_separation_point, a.thickness, a.rating_duration_type, a.external_rating, a.original_maturity, a.residual_maturity, a.asset_balance, a.tranche_prop, a.currency, a.provision, a.amp_flag, a.amp_id, m.amp_type, m.amp_approach, m.book_type as amp_book_type, m.exposure_belong as amp_belong, m.amp_lr, m.is_tp_calc from RWA_EI_ABS_Exposure a join RWA_EI_ABS_Product p   on p.abs_product_id = a.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = #{isOriginator} left join RWA_EI_Amp_Exposure m   on m.data_batch_no = #{dataBatchNo} and m.exposure_id = a.abs_exposure_id where a.data_batch_no = #{dataBatchNo} and a.book_type = '1' ";
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
/* 1638 */     return SqlBuilder.create(sql)
/* 1639 */       .condition("range", RwaUtils.generateConditionSql(rangeList, isOriginator))
/* 1640 */       .setString("dataBatchNo", dataBatchNo)
/* 1641 */       .setString("isOriginator", (ICodeEnum)isOriginator)
/* 1642 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.ABSE)).clear("ec")
/* 1643 */       .build();
/*      */   }
/*      */   
/*      */   public String getAbsProductSql(TaskType taskType, String dataBatchNo, String resultNo, Identity isOriginator, List<TaskRangeDo> rangeList) {
/* 1647 */     String sql = null;
/* 1648 */     if (isOriginator == Identity.YES) {
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
/* 1671 */       sql = "select p.abs_product_id, p.abs_originator_id, p.org_id, p.under_asset_type, p.np_asset_flag, p.is_originator, p.is_comp_requ, p.reabs_flag, p.is_irb_calc, p.ap_en, ap.ap_ab, ap.ap_ead, ap.ap_rwa, ap.ap_max_maturity, ap.ap_airb_ab, ap.ap_airb_ead, ap.ap_airb_rwa, ap.ap_airb_ela, ap.ap_airb_prov, ap.ap_firb_ab, ap.ap_firb_ead, ap.ap_firb_rwa, ap.ap_firb_ela, ap.ap_firb_prov, ap.ap_wa_ab, ap.ap_wa_ead, ap.ap_wa_rwa, ap.ap_wa_prov, ap.ap_wa_max_rw, ap.ap_ode_ab, ap.ap_ode_ead, ap.ap_unke_ab, ap.ap_unke_ead, apd.irb_ead2_total, apd.irb_ead_total, apd.irb_lgd_ead_total from #{absProductTable} p left join (select ue.abs_product_id, sum(ue.asset_balance) as ap_ab, sum(ue.ead) as ap_ead, sum(ue.rwa_ma) as ap_rwa, max(ue.residual_maturity) as ap_max_maturity, sum(case when ue.approach in (#{airb}, #{rirb}) then ue.asset_balance else 0 end) as ap_airb_ab, sum(case when ue.approach in (#{airb}, #{rirb}) then ue.ead else 0 end) as ap_airb_ead, sum(case when ue.approach in (#{airb}, #{rirb}) then ue.rwa_ma else 0 end) as ap_airb_rwa, sum(case when ue.approach in (#{airb}, #{rirb}) then ue.ela else 0 end) as ap_airb_ela, sum(case when ue.approach in (#{airb}, #{rirb}) then ue.provision_ded else 0 end) as ap_airb_prov, sum(case when ue.approach = #{firb} then ue.asset_balance else 0 end) as ap_firb_ab, sum(case when ue.approach = #{firb} then ue.ead else 0 end) as ap_firb_ead, sum(case when ue.approach = #{firb} then ue.rwa_ma else 0 end) as ap_firb_rwa, sum(case when ue.approach = #{firb} then ue.ela else 0 end) as ap_firb_ela, sum(case when ue.approach = #{firb} then ue.provision_ded else 0 end) as ap_firb_prov, sum(case when ue.approach = #{wa} then ue.asset_balance else 0 end) as ap_wa_ab, sum(case when ue.approach = #{wa} then ue.ead else 0 end) as ap_wa_ead, sum(case when ue.approach = #{wa} then ue.rwa_ma else 0 end) as ap_wa_rwa, sum(case when ue.approach = #{wa} then ue.provision_ded else 0 end) as ap_wa_prov, max(case when ue.approach = #{wa} then ue.rw end) as ap_wa_max_rw, sum(case when ue.exposure_status in ('29') then ue.asset_balance else 0 end) as ap_ode_ab, sum(case when ue.exposure_status in ('29') then ue.ead else 0 end) as ap_ode_ead, sum(case when ue.exposure_status is null or ue.exposure_status = '' then ue.asset_balance else 0 end) as ap_unke_ab, sum(case when ue.exposure_status is null or ue.exposure_status = '' then ue.ead else 0 end) as ap_unke_ead from #{uaExposureTable} ue where ue.result_no = #{resultNo} group by ue.abs_product_id) ap  on ap.abs_product_id = p.abs_product_id left join (select t.abs_product_id, sum(ead * ead) as irb_ead2_total, sum(ead) as irb_ead_total, sum(lgd_ead) as irb_lgd_ead_total from (select ud.abs_product_id, ud.client_id, sum(ud.mitigated_ea) as ead, sum(ud.mitigated_lgd * ud.mitigated_ea) as lgd_ead from #{uaDetailTable} ud where ud.result_no = #{resultNo} and ud.is_result = '1' and ud.approach in(#{firb}, #{airb}, #{rirb}) group by ud.abs_product_id, ud.client_id) t group by t.abs_product_id) apd  on apd.abs_product_id = p.abs_product_id where p.data_batch_no = #{dataBatchNo} and p.is_originator = '1'";
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
/* 1716 */       String absProductTable = null;
/* 1717 */       String uaExposureTable = null;
/* 1718 */       String uaDetailTable = null;
/* 1719 */       if (RwaUtils.isSingle(taskType)) {
/* 1720 */         absProductTable = "RWA_ESI_ABS_Product";
/* 1721 */         uaExposureTable = "RWA_ESR_GE_Exposure";
/* 1722 */         uaDetailTable = "RWA_ESR_GE_Detail";
/* 1723 */         return SqlBuilder.create(sql)
/* 1724 */           .setTable("absProductTable", absProductTable)
/* 1725 */           .setTable("uaExposureTable", uaExposureTable)
/* 1726 */           .setTable("uaDetailTable", uaDetailTable)
/* 1727 */           .setString("dataBatchNo", dataBatchNo)
/* 1728 */           .setString("resultNo", resultNo)
/* 1729 */           .setString("firb", (ICodeEnum)ExposureApproach.FIRB)
/* 1730 */           .setString("airb", (ICodeEnum)ExposureApproach.AIRB)
/* 1731 */           .setString("rirb", (ICodeEnum)ExposureApproach.RIRB)
/* 1732 */           .setString("wa", (ICodeEnum)ExposureApproach.WA)
/* 1733 */           .build();
/*      */       } 
/* 1735 */       absProductTable = "RWA_EI_ABS_Product";
/* 1736 */       uaExposureTable = "RWA_ER_ABS_UA_Expo";
/* 1737 */       uaDetailTable = "RWA_ER_ABS_UA_Detail";
/*      */       
/* 1739 */       return SqlBuilder.create(sql)
/* 1740 */         .condition("", RwaUtils.generateConditionSql(rangeList, isOriginator))
/* 1741 */         .setTable("absProductTable", absProductTable)
/* 1742 */         .setTable("uaExposureTable", uaExposureTable)
/* 1743 */         .setTable("uaDetailTable", uaDetailTable)
/* 1744 */         .setString("dataBatchNo", dataBatchNo)
/* 1745 */         .setString("resultNo", resultNo)
/* 1746 */         .setString("firb", (ICodeEnum)ExposureApproach.FIRB)
/* 1747 */         .setString("airb", (ICodeEnum)ExposureApproach.AIRB)
/* 1748 */         .setString("rirb", (ICodeEnum)ExposureApproach.RIRB)
/* 1749 */         .setString("wa", (ICodeEnum)ExposureApproach.WA)
/* 1750 */         .build();
/*      */     } 
/* 1752 */     if (RwaUtils.isSingle(taskType)) {
/* 1753 */       sql = "select p.abs_product_id, p.abs_originator_id, p.org_id, p.under_asset_type, p.np_asset_flag, p.is_originator, p.is_comp_requ, p.reabs_flag, p.is_irb_calc, p.ap_en from RWA_ESI_ABS_Product p where p.data_batch_no = #{dataBatchNo} and p.is_originator = '0'";
/*      */     
/*      */     }
/*      */     else {
/*      */       
/* 1758 */       sql = "select p.abs_product_id, p.abs_originator_id, p.org_id, p.under_asset_type, p.np_asset_flag, p.is_originator, p.is_comp_requ, p.reabs_flag, p.is_irb_calc, p.ap_en from RWA_EI_ABS_Product p where p.data_batch_no = #{dataBatchNo} and p.is_originator = '0'";
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1763 */     return SqlBuilder.create(sql)
/* 1764 */       .setString("dataBatchNo", dataBatchNo)
/* 1765 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getAbsRelevanceSql(TaskType taskType, String dataBatchNo) {
/* 1770 */     String sql = null;
/* 1771 */     sql = "select r.exposure_id, r.mitigation_id, r.mitigation_type, r.is_positive_correlation, r.abs_product_id from RWA_EI_ABS_EMRelevance r where r.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_ABS_Product p  where p.abs_product_id = r.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = #{isOriginator})";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1776 */     return SqlBuilder.create(sql)
/* 1777 */       .setString("dataBatchNo", dataBatchNo)
/* 1778 */       .setString("isOriginator", (ICodeEnum)Identity.YES)
/* 1779 */       .build();
/*      */   }
/*      */   
/*      */   public String getAbsGuaranteeSql(TaskType taskType, String dataBatchNo) {
/* 1783 */     String sql = null;
/* 1784 */     sql = "select gt.abs_product_id, gt.guarantee_id, gt.guarantor_id, gt.is_apply_wa, gt.is_apply_firb, gt.mitigation_main_type, gt.mitigation_small_type, gt.guarantee_amount, gt.currency, gt.original_maturity, gt.residual_maturity, gt.residual_maturity as guar_residual_maturity, gt.is_cover_debt_rest, gt.pay_default_threshold, c.model_id, c.rating, c.pd, c.client_id, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.exposure_type_irb as guarantor_expo_type from RWA_EI_ABS_Guarantee gt left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = gt.guarantor_id where gt.data_batch_no = #{dataBatchNo} and exists(select 1 from RWA_EI_ABS_Product p  where p.abs_product_id = gt.abs_product_id and p.data_batch_no = #{dataBatchNo} and p.is_originator = #{isOriginator})";
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
/* 1797 */     return SqlBuilder.create(sql)
/* 1798 */       .setString("dataBatchNo", dataBatchNo)
/* 1799 */       .setString("isOriginator", (ICodeEnum)Identity.YES)
/* 1800 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getAbsSqlMap(JobInfoDto jobInfo, Identity isOriginator) {
/* 1804 */     TaskType taskType = jobInfo.getTaskType();
/* 1805 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1806 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/* 1807 */     String resultNo = jobInfo.getResultNo();
/* 1808 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/* 1809 */     sqlMap.put(InterfaceDataType.ABS_EXPOSURE.getCode(), getAbsExposureSql(taskType, dataBatchNo, isOriginator, rangeList));
/* 1810 */     sqlMap.put(InterfaceDataType.ABS_PRODUCT.getCode(), getAbsProductSql(taskType, dataBatchNo, resultNo, isOriginator, rangeList));
/*      */     
/* 1812 */     if (RwaUtils.isSingle(taskType)) {
/* 1813 */       return sqlMap;
/*      */     }
/* 1815 */     if (isOriginator == Identity.YES) {
/* 1816 */       sqlMap.put(InterfaceDataType.ABS_RELEVANCE.getCode(), getAbsRelevanceSql(taskType, dataBatchNo));
/* 1817 */       sqlMap.put(InterfaceDataType.ABS_GUARANTEE.getCode(), getAbsGuaranteeSql(taskType, dataBatchNo));
/*      */     } 
/* 1819 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public Map<String, ItemPst> getAbsResultSetterMap(JobInfoDto jobInfo) {
/* 1823 */     TaskType taskType = jobInfo.getTaskType();
/* 1824 */     String resultNo = jobInfo.getResultNo();
/* 1825 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1826 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 1827 */     pstSetterMap.put(ResultDataType.ABS_EXPOSURE.getCode(), getAbsExposureSetter(taskType, resultNo, dataBatchNo));
/* 1828 */     pstSetterMap.put(ResultDataType.ABS_PRODUCT.getCode(), getAbsProductSetter(taskType, resultNo, dataBatchNo));
/* 1829 */     pstSetterMap.put(ResultDataType.AMP.getCode(), getAbsAmpExposureSetter(taskType, resultNo, dataBatchNo));
/*      */     
/* 1831 */     if (RwaUtils.isSingle(taskType)) {
/* 1832 */       return pstSetterMap;
/*      */     }
/* 1834 */     pstSetterMap.put(ResultDataType.ABS_DETAIL.getCode(), getAbsDetailSetter(taskType, resultNo, dataBatchNo));
/* 1835 */     pstSetterMap.put(ResultDataType.ABS_MITIGATION.getCode(), getAbsMitigationSetter(taskType, resultNo, dataBatchNo));
/* 1836 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public Map<String, ItemPst> getAbsUaResultSetterMap(JobInfoDto jobInfo) {
/* 1840 */     TaskType taskType = jobInfo.getTaskType();
/* 1841 */     String resultNo = jobInfo.getResultNo();
/* 1842 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 1843 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 1844 */     pstSetterMap.put(ResultDataType.EXPOSURE.getCode(), getAbsUaExposureSetter(taskType, resultNo, dataBatchNo));
/* 1845 */     pstSetterMap.put(ResultDataType.DETAIL.getCode(), getAbsUaDetailSetter(taskType, resultNo, dataBatchNo));
/* 1846 */     pstSetterMap.put(ResultDataType.MITIGATION.getCode(), getAbsUaMitigationSetter(taskType, resultNo, dataBatchNo));
/* 1847 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public ItemPst getAbsUaDetailSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 1851 */     String sql = "insert into #{result_table}(result_no, detail_no, data_batch_no, approach, exposure_id, contract_id, client_id, client_type, org_id, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, currency, asset_balance, original_maturity, residual_maturity, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ccf, risk_classify, provision, ltv, ead, she, he, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, kcr, rw, is_result, mitigated_flag, fm_reason, uncovered_ea, mitigation_id, is_alone, mitigation_client_id, mitigation_client_cate, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, mitigation_orig_value, mitigation_ct_value, mitigation_ct_kcr, mitigation_use_amount, mitigation_resi_value, mitigation_shc, mitigation_hc, mitigation_orig_rw, hfx, ht, is_exempt_rw_line, value_fc, covered_ea, mitigated_ea, mitigated_model_id, mitigated_rating, mitigated_pd, mitigated_lgd, mitigated_correlation, mitigated_bma, mitigated_kcr, mitigated_rw, el, ela, rwa_mb, rwa_ma, formula_no, group_id) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 1863 */     String tableName = null;
/* 1864 */     if (RwaUtils.isSingle(taskType)) {
/* 1865 */       tableName = "RWA_ESR_GE_Detail";
/*      */     } else {
/* 1867 */       tableName = "RWA_ER_ABS_UA_Detail";
/*      */     } 
/* 1869 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 1870 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */   
/*      */   public ItemPst getAbsUaExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 1962 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, contract_id, client_id, client_type, approach, org_id, abs_ua_flag, abs_product_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, original_maturity, residual_maturity, asset_balance, currency, risk_classify, provision, exposure_status, claims_level, reva_frequency, tm, exposure_rpt_item_wa, off_rpt_item_wa, off_business_type, off_business_subtype, ccf, ltv, provision_prop, provision_ded, ead, she, he, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, covered_ea, uncovered_ea, el, ela, walgd, wakcr, warw, rwa_mb, rwa_ma, rwa_um, formula_no, group_id, sort_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1971 */     String tableName = null;
/* 1972 */     if (RwaUtils.isSingle(taskType)) {
/* 1973 */       tableName = "RWA_ESR_GE_Exposure";
/*      */     } else {
/* 1975 */       tableName = "RWA_ER_ABS_UA_Expo";
/*      */     } 
/* 1977 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 1978 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getAbsUaMitigationSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2050 */     String sql = "insert into #{result_table}(result_no, mitigation_id, data_batch_no, client_id, client_type, approach, is_alone, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, guarantor_expo_type, mitigation_amount, currency, reva_frequency, original_maturity, residual_maturity, guar_residual_maturity, sh, min_collateral_level, over_collateral_level, model_id, rating, pd, lgd, kcr, rw, unmitigated_amount, mitigated_amount, covered_ea, mitigated_effect, group_id, exposure_id, sort_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2056 */     String tableName = null;
/* 2057 */     if (RwaUtils.isSingle(taskType)) {
/* 2058 */       tableName = "RWA_ESR_GE_Mitigation";
/*      */     } else {
/* 2060 */       tableName = "RWA_ER_ABS_UA_Miti";
/*      */     } 
/* 2062 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2063 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getAbsExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2111 */     String sql = "insert into #{result_table}(result_no, abs_exposure_id, data_batch_no, abs_product_id, amp_flag, amp_id, amp_book_type, amp_belong, amp_lr, is_tp_calc, securities_code, securities_name, org_id, approach, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, reabs_flag, is_originator, bank_business_role, off_abs_biz_type, qual_facility_flag, is_compliance_stc, tranche_level, rating_duration_type, external_rating, is_approved_rating, original_maturity, residual_maturity, tranche_maturity, asset_balance, tranche_prop, currency, provision, provision_ded, ead, ccf, tm, reva_frequency, tranche_starting_point, tranche_separation_point, thickness, sfa, sfb, sfc, sfd, sfe, sfp, vara, varu, varl, kssfa, rw_before, npt_rw_ac, rw, covered_ea, uncovered_ea, warw, rwa_mb, rwa_ma, rwa_aa, rwa_adj, sort_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 2122 */     String tableName = null;
/* 2123 */     if (RwaUtils.isSingle(taskType)) {
/* 2124 */       tableName = "RWA_ESR_ABS_Exposure";
/*      */     } else {
/* 2126 */       tableName = "RWA_ER_ABS_Exposure";
/*      */     } 
/* 2128 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2129 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getAbsProductSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2208 */     String sql = "insert into #{result_table}(result_no, abs_product_id, data_batch_no, approach, org_id, under_asset_type, np_asset_flag, is_originator, is_comp_requ, ap_airb_prop, ap_airb_ab, ap_airb_ead, ap_airb_rwa, ap_airb_ela, ap_airb_prov, ap_firb_prop, ap_firb_ab, ap_firb_ead, ap_firb_rwa, ap_firb_ela, ap_firb_prov, ap_wa_prop, ap_wa_ab, ap_wa_ead, ap_wa_rwa, ap_wa_prov, ap_wa_max_rw, ap_wa_arw, ap_irb_prop, ap_ab, ap_ead, ap_rwa, ap_ode_ab, ap_ode_ead, ap_unke_ab, ap_unke_ead, ap_max_maturity, ap_en, ap_lgd, ap_warw, ap_type, is_decentralized, ap_kirb, ap_ksa, ap_ka, max_tranche_prop, product_ab, product_ead, product_rwa_mb, product_rwa_ma, product_rwa_limit, product_rwa_aa, rwa_adj) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2217 */     String tableName = null;
/* 2218 */     if (RwaUtils.isSingle(taskType)) {
/* 2219 */       tableName = "RWA_ESR_ABS_Product";
/*      */     } else {
/* 2221 */       tableName = "RWA_ER_ABS_Product";
/*      */     } 
/* 2223 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2224 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemPst getAbsDetailSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2288 */     String sql = "insert into #{result_table}(result_no, detail_no, data_batch_no, approach, abs_exposure_id, abs_product_id, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_belong, book_type, currency, asset_balance, original_maturity, residual_maturity, off_abs_biz_type, ccf, provision, ead, she, he, rw, is_result, mitigated_flag, fm_reason, uncovered_ea, mitigation_id, mitigation_client_id, mitigation_client_cate, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, mitigation_orig_value, mitigation_ct_value, mitigation_ct_kcr, mitigation_use_amount, mitigation_resi_value, mitigation_shc, mitigation_hc, mitigation_orig_rw, hfx, ht, is_exempt_rw_line, value_fc, covered_ea, mitigated_ea, mitigated_model_id, mitigated_rating, mitigated_pd, mitigated_lgd, mitigated_correlation, mitigated_bma, mitigated_kcr, mitigated_rw, rwa_mb, rwa_ma, formula_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,        ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 2299 */     sql = SqlBuilder.create(sql).setTable("result_table", "RWA_ER_ABS_Detail").build();
/* 2300 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemPst getAbsMitigationSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2369 */     String sql = "insert into #{result_table}(result_no, mitigation_id, data_batch_no, abs_product_id, client_id, client_type, approach, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, mitigation_rpt_item_wa, guarantor_expo_type, mitigation_amount, currency, reva_frequency, original_maturity, residual_maturity, sh, model_id, rating, pd, lgd, kcr, rw, unmitigated_amount, mitigated_amount, covered_ea, mitigated_effect, sort_no) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2375 */     sql = SqlBuilder.create(sql).setTable("result_table", "RWA_ER_ABS_Mitigation").build();
/* 2376 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public boolean containNetting(List<TaskRangeDo> rangeList, Identity nettingFlag) {
/* 2419 */     if (CollUtil.isEmpty(rangeList)) {
/* 2420 */       return true;
/*      */     }
/* 2422 */     for (TaskRangeDo rangeDo : rangeList) {
/* 2423 */       if (StrUtil.equals(rangeDo.getNettingFlag(), nettingFlag.getCode())) {
/* 2424 */         return true;
/*      */       }
/*      */     } 
/* 2427 */     return false;
/*      */   }
/*      */   
/*      */   public Job diJob(JobInfoDto jobInfo) {
/* 2431 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType();
/* 2432 */     IrbUncoveredProcess irbUncoveredProcess = IrbUncoveredProcess.CALCULATE;
/* 2433 */     CvaType cvaType = CvaType.DI;
/* 2434 */     JobBuilder jobBuilder = (new JobBuilder()).name(name + "-job");
/*      */     
/* 2436 */     if (containNetting(jobInfo.getRangeList(), Identity.YES)) {
/* 2437 */       jobBuilder.next(createRwaStep(name + "-din", jobInfo.getJobId(), 
/* 2438 */             createReader(jobInfo.getJobId(), name + "-din", this.dataSource, getDiSqlMap(jobInfo, Identity.YES), "netting_id"), (RwaProcessor)new DiProcessor(jobInfo, irbUncoveredProcess, Identity.YES), 
/*      */             
/* 2440 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getDiResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 2443 */     if (containNetting(jobInfo.getRangeList(), Identity.NO)) {
/* 2444 */       jobBuilder.next(createRwaStep(name + "-die", jobInfo.getJobId(), 
/* 2445 */             createReader(jobInfo.getJobId(), name + "-die", this.dataSource, getDiSqlMap(jobInfo, Identity.NO), "exposure_id"), (RwaProcessor)new DiProcessor(jobInfo, irbUncoveredProcess, Identity.NO), 
/*      */             
/* 2447 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getDiResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 2450 */     if (StrUtil.equals(jobInfo.getTaskConfigDo().getIsCalcCva(), Identity.YES.getCode())) {
/* 2451 */       jobBuilder.next(createRwaStep(name + "-dicva" + cvaType, jobInfo.getJobId(), 
/* 2452 */             createReader(jobInfo.getJobId(), name + "-dicva" + cvaType, this.dataSource, getCvaSqlMap(jobInfo, cvaType), "cva_type"), (RwaProcessor)new CvaProcessor(jobInfo, irbUncoveredProcess, cvaType), 
/*      */             
/* 2454 */             createWriter(jobInfo.getJobId(), this.dataSource, false, getCvaResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 2457 */     return jobBuilder.listener((JobListener)new RwaJobListener(jobInfo)).build();
/*      */   }
/*      */   
/*      */   public String getDiNettingSql(TaskType taskType, String consolidateFlag, String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 2461 */     String sql = null;
/* 2462 */     if (RwaUtils.isSingle(taskType)) {
/* 2463 */       sql = "select n.netting_id, n.client_id, n.org_id, n.asset_type, n.exposure_type_wa, n.exposure_type_irb, n.exposure_belong, n.sprv_tran_type, t.transactions_num, t.original_maturity, t.residual_maturity, t.mtm, t.book_value, n.currency, n.margin_trading_flag, n.margin_agreement_id, n.margin_mtm_interval, n.mta, n.th, n.nica, n.is_controversial, n.default_flag, n.beel, n.default_lgd, n.pd, n.client_name, n.client_type, n.or_rating, n.client_external_rating, n.investment_grade_flag, n.scra_result, n.is_consolidated_sub, n.sib_flag, n.annual_sale, n.qual_ccp_flag from RWA_ESI_DI_Netting n left join (select e.netting_id, count(1) as transactions_num,  max(e.original_maturity) as original_maturity, max(e.residual_maturity) as residual_maturity,  sum(e.mtm) as mtm, sum(e.book_value) as book_value  from RWA_ESI_DI_Exposure e  where e.data_batch_no = #{dataBatchNo} and e.netting_flag = '1'  group by e.netting_id) t   on t.netting_id = n.netting_id where n.data_batch_no = #{dataBatchNo}";
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
/* 2479 */       return SqlBuilder.create(sql)
/* 2480 */         .setString("dataBatchNo", dataBatchNo)
/* 2481 */         .build();
/*      */     } 
/* 2483 */     sql = "select n.netting_id, n.client_id, n.org_id, n.asset_type, n.industry_id, n.business_line, #{ec} n.exposure_type_wa, n.exposure_type_irb, n.exposure_belong, n.book_type, n.sprv_tran_type, t.transactions_num, t.original_maturity, t.residual_maturity, t.mtm, t.book_value, n.currency, n.margin_trading_flag, n.margin_agreement_id, n.margin_mtm_interval, n.mta, n.th, n.nica, n.is_controversial, n.default_flag, n.beel, n.default_lgd, n.model_id, n.rating, n.pd, c.client_name, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.qual_ccp_flag from RWA_EI_DI_Netting n left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = n.client_id left join (select e.netting_id, count(1) as transactions_num,  max(e.original_maturity) as original_maturity, max(e.residual_maturity) as residual_maturity,  sum(e.mtm) as mtm, sum(e.book_value) as book_value  from RWA_EI_DI_Exposure e  where e.data_batch_no = #{dataBatchNo} and e.netting_flag = '1'  group by e.netting_id) t   on t.netting_id = n.netting_id where n.data_batch_no = #{dataBatchNo}";
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
/* 2501 */     return SqlBuilder.create(sql)
/* 2502 */       .condition("", RwaUtils.generateConditionSql(rangeList, Identity.YES))
/* 2503 */       .setString("dataBatchNo", dataBatchNo)
/*      */       
/* 2505 */       .condition(SqlBuilder.createQueryCondition(null, "n", "is_include_cons", null, consolidateFlag, 
/* 2506 */           StrUtil.equals(consolidateFlag, Identity.YES.getCode())))
/* 2507 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.DIN)).clear("ec")
/* 2508 */       .build();
/*      */   }
/*      */   
/*      */   public String getDiExposureSql(TaskType taskType, String consolidateFlag, String dataBatchNo, Identity nettingFlag, List<TaskRangeDo> rangeList) {
/* 2512 */     String sql = null;
/* 2513 */     if (RwaUtils.isSingle(taskType)) {
/* 2514 */       sql = "select e.exposure_id, e.netting_id, e.netting_flag, e.client_id, e.org_id, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.sprv_tran_type, e.central_clear_flag, e.ccp_id, e.trading_role, e.margin_trading_flag, e.margin_agreement_id, e.margin_mtm_interval, e.mta, e.margin_risk_period, e.th, e.nica, e.currency, e.book_value, e.mtm, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.derivative_asset_type, e.pm_trading_flag, e.credit_derivative_type, e.qual_ref_asset_flag, e.offset_comb, e.ref_entity_code, e.ref_entity_type, e.ref_entity_rating, e.commodity_maintype, e.commodity_subtype, e.trading_direction, e.unpaid_fee, e.notional_principal1, e.currency1, e.notional_principal2, e.currency2, e.option_cdo_flag, e.cdo_level_attachment_point, e.cdo_level_decoupling_point, e.option_type, e.underlying_price, e.executive_price, e.exercise_date, e.option_premium_status, e.default_flag, e.beel, e.default_lgd, e.pd, e.is_controversial, e.client_type, e.or_rating, e.client_external_rating, e.investment_grade_flag, e.scra_result, e.is_consolidated_sub, e.sib_flag, e.annual_sale, e.qual_ccp_flag from RWA_ESI_DI_Exposure e where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
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
/* 2528 */       return SqlBuilder.create(sql)
/* 2529 */         .setString("dataBatchNo", dataBatchNo)
/* 2530 */         .setString("nettingFlag", nettingFlag.getCode())
/* 2531 */         .build();
/*      */     } 
/* 2533 */     sql = "select e.exposure_id, e.netting_id, e.netting_flag, e.client_id, e.org_id, e.asset_type, e.industry_id, e.business_line, #{ec} e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.sprv_tran_type, e.central_clear_flag, e.ccp_id, e.trading_role, e.margin_trading_flag, e.margin_agreement_id, e.margin_mtm_interval, e.mta, e.margin_risk_period, e.th, e.nica, e.currency, e.book_value, e.mtm, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.derivative_asset_type, e.pm_trading_flag, e.credit_derivative_type, e.qual_ref_asset_flag, e.offset_comb, e.ref_entity_code, e.ref_entity_type, e.ref_entity_rating, e.commodity_maintype, e.commodity_subtype, e.trading_direction, e.unpaid_fee, e.notional_principal1, e.currency1, e.notional_principal2, e.currency2, e.option_cdo_flag, e.cdo_level_attachment_point, e.cdo_level_decoupling_point, e.option_type, e.underlying_price, e.executive_price, e.exercise_date, e.option_premium_status, e.default_flag, e.beel, e.default_lgd, e.model_id, e.rating, e.pd, e.is_controversial, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.qual_ccp_flag from RWA_EI_DI_Exposure e left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = e.client_id left join RWA_EI_DI_Netting n   on n.data_batch_no = #{dataBatchNo} and n.netting_id = e.netting_id where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
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
/* 2551 */     return SqlBuilder.create(sql)
/* 2552 */       .condition("", RwaUtils.generateConditionSql(rangeList, nettingFlag))
/* 2553 */       .setString("dataBatchNo", dataBatchNo)
/* 2554 */       .setString("nettingFlag", nettingFlag.getCode())
/*      */       
/* 2556 */       .condition(SqlBuilder.createQueryCondition(null, "e", "is_include_cons", null, consolidateFlag, 
/* 2557 */           StrUtil.equals(consolidateFlag, Identity.YES.getCode())))
/* 2558 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.DIE)).clear("ec")
/* 2559 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDiCollateralSql(TaskType taskType, String dataBatchNo, Identity nettingFlag, List<TaskRangeDo> rangeList) {
/* 2564 */     String sql = null;
/* 2565 */     if (RwaUtils.isSingle(taskType)) {
/* 2566 */       sql = "select cl.collateral_id, cl.netting_flag, cl.netting_id, cl.exposure_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.is_main_index, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_our_bank_submit, cl.bankruptcy_separation_flag, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub, cl.holder_id as client_id, cl.client_type, cl.or_rating, cl.client_external_rating, cl.investment_grade_flag, cl.scra_result, cl.is_consolidated_sub, cl.qual_ccp_flag from RWA_ESI_DI_Collateral cl where cl.data_batch_no = #{dataBatchNo} and cl.netting_flag = #{nettingFlag}";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 2578 */       sql = "select cl.collateral_id, cl.netting_flag, cl.netting_id, cl.exposure_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.gov_bond_type, cl.collateral_amount, cl.currency, cl.start_date, cl.due_date, cl.original_maturity, cl.residual_maturity, cl.is_main_index, cl.rating_duration_type, cl.financial_coll_rating as secu_issue_rating, cl.reva_frequency, cl.is_our_bank_submit, cl.bankruptcy_separation_flag, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.qual_ccp_flag as issuer_qual_ccp_flag, cn.client_id, cn.client_type, cn.regist_state, cn.or_rating, cn.client_external_rating, cn.investment_grade_flag, cn.scra_result, cn.is_consolidated_sub, cn.qual_ccp_flag from RWA_EI_DI_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id left join RWA_EI_Client cn   on cn.data_batch_no = #{dataBatchNo} and cn.client_id = cl.holder_id where cl.data_batch_no = #{dataBatchNo} and cl.netting_flag = #{nettingFlag}";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2596 */     return SqlBuilder.create(sql)
/* 2597 */       .setString("dataBatchNo", dataBatchNo)
/* 2598 */       .setString("nettingFlag", nettingFlag.getCode())
/* 2599 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getDiSqlMap(JobInfoDto jobInfo, Identity nettingFlag) {
/* 2603 */     TaskType taskType = jobInfo.getTaskType();
/* 2604 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(jobInfo.getSchemeId());
/* 2605 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 2606 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/* 2607 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/*      */     
/* 2609 */     sqlMap.put(InterfaceDataType.DI_EXPOSURE.getCode(), getDiExposureSql(taskType, jobInfo.getTaskConfigDo().getConsolidateFlag(), dataBatchNo, nettingFlag, rangeList));
/*      */     
/* 2611 */     sqlMap.put(InterfaceDataType.DI_COLLATERAL.getCode(), getDiCollateralSql(taskType, dataBatchNo, nettingFlag, rangeList));
/* 2612 */     if (nettingFlag == Identity.YES)
/*      */     {
/* 2614 */       sqlMap.put(InterfaceDataType.DI_NETTING.getCode(), getDiNettingSql(taskType, jobInfo.getTaskConfigDo().getConsolidateFlag(), dataBatchNo, rangeList));
/*      */     }
/* 2616 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public ItemPst getDiExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2620 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, di_ead_approach, approach, netting_id, netting_flag, client_id, client_type, annual_sale, qual_ccp_flag, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_rpt_item_wa, exposure_belong, book_type, central_clear_flag, ccp_id, trading_role, margin_trading_flag, margin_agreement_id, margin_mtm_interval, mta, th, nica, currency, book_value, original_maturity, residual_maturity, derivative_asset_type, pm_trading_flag, credit_derivative_type, offset_comb, commodity_subtype, trading_direction, unpaid_fee, option_cdo_flag, option_premium_status, is_controversial, ir_time_type, notional_principal, ts_maturity, te_maturity, exercise_maturity, margin_risk_period, nmt_mf, mt_mf, supervision_duration, adjust_notional, addon_factor, supervision_factor, supervision_coefficient, supervision_volatility, supervision_delta, ead_haircut, nmt_effective_notional, mt_effective_notional, nmt_add_on, mt_add_on, mtm, nca, bmr_max_ead, nmt_rc, mt_rc, nmt_multiplier, mt_multiplier, nmt_pfe, mt_pfe, nmt_ead, mt_ead, ead, mitigated_ead, tm, transaction_maturity, discount_factor, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, rwa, formula_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 2636 */     String tableName = null;
/* 2637 */     if (RwaUtils.isSingle(taskType)) {
/* 2638 */       tableName = "RWA_ESR_DI_Exposure";
/*      */     } else {
/* 2640 */       tableName = "RWA_ER_DI_Exposure";
/*      */     } 
/* 2642 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2643 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getDiIntermediateSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2750 */     String sql = "insert into #{result_table}(result_no, int_result_no, int_result_type, data_batch_no, netting_id, client_id, margin_trading_flag, derivative_asset_type, offset_comb, commodity_subtype, supervision_factor, supervision_coefficient, nmt_d1_effective_notional, nmt_d2_effective_notional, nmt_d3_effective_notional, mt_d1_effective_notional, mt_d2_effective_notional, mt_d3_effective_notional, nmt_effective_notional, mt_effective_notional, nmt_add_on, mt_add_on) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2756 */     String tableName = null;
/* 2757 */     if (RwaUtils.isSingle(taskType)) {
/* 2758 */       tableName = "RWA_ESR_DI_Intermediate";
/*      */     } else {
/* 2760 */       tableName = "RWA_ER_DI_Intermediate";
/*      */     } 
/* 2762 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2763 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   
/*      */   public ItemPst getDiNettingSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2796 */     String sql = "insert into #{result_table}(result_no, netting_id, data_batch_no, di_ead_approach, approach, client_id, client_name, client_type, annual_sale, qual_ccp_flag, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_rpt_item_wa, exposure_belong, book_type, currency, book_value, notional_principal, transactions_num, original_maturity, residual_maturity, margin_trading_flag, margin_agreement_id, margin_mtm_interval, mta, th, nica, is_controversial, nmt_effective_notional, mt_effective_notional, nmt_add_on, mt_add_on, mtm, grc, ngr, nca, bmr_max_ead, nmt_rc, mt_rc, nmt_multiplier, mt_multiplier, nmt_pfe, mt_pfe, nmt_ead, mt_ead, anet, ead, mitigated_ead, tm, netting_maturity, discount_factor, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, rwa, formula_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
/* 2807 */     String tableName = null;
/* 2808 */     if (RwaUtils.isSingle(taskType)) {
/* 2809 */       tableName = "RWA_ESR_DI_Netting";
/*      */     } else {
/* 2811 */       tableName = "RWA_ER_DI_Netting";
/*      */     } 
/* 2813 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2814 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getDiCollateralSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 2897 */     String sql = "insert into #{result_table}(result_no, collateral_id, data_batch_no, netting_flag, netting_id, exposure_id, client_id, client_type, qual_ccp_flag, approach, issuer_id, issuer_type, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, collateral_amount, currency, reva_frequency, original_maturity, residual_maturity, is_our_bank_submit, bankruptcy_separation_flag, sh, haircut, hfx, ahc_amount, sort_no, supervision_class) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2903 */     String tableName = null;
/* 2904 */     if (RwaUtils.isSingle(taskType)) {
/* 2905 */       tableName = "RWA_ESR_DI_Collateral";
/*      */     } else {
/* 2907 */       tableName = "RWA_ER_DI_Collateral";
/*      */     } 
/* 2909 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 2910 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public Map<String, ItemPst> getDiResultSetterMap(JobInfoDto jobInfo) {
/* 2955 */     TaskType taskType = jobInfo.getTaskType();
/* 2956 */     String resultNo = jobInfo.getResultNo();
/* 2957 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 2958 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 2959 */     pstSetterMap.put(ResultDataType.DI_EXPOSURE.getCode(), getDiExposureSetter(taskType, resultNo, dataBatchNo));
/* 2960 */     pstSetterMap.put(ResultDataType.DI_NETTING.getCode(), getDiNettingSetter(taskType, resultNo, dataBatchNo));
/* 2961 */     pstSetterMap.put(ResultDataType.DI_INTERMEDIATE.getCode(), getDiIntermediateSetter(taskType, resultNo, dataBatchNo));
/* 2962 */     pstSetterMap.put(ResultDataType.DI_COLLATERAL.getCode(), getDiCollateralSetter(taskType, resultNo, dataBatchNo));
/* 2963 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public String getCvaSql(TaskType taskType, String resultNo, String dataBatchNo, CvaType cvaType, String diEadApproach) {
/* 2967 */     if (cvaType != CvaType.DI) {
/* 2968 */       throw new ParamConfigException("仅支持衍生工具CVA计算");
/*      */     }
/*      */     
/* 2971 */     String sql = "";
/* 2972 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$TaskType[taskType.ordinal()]) {
/*      */       
/*      */       case 1:
/* 2975 */         sql = "select #{cvaType} as cva_type, t.client_id, null as org_id, t.client_type, t.cp_cva_industry, t.cp_cva_credit, sum(t.notional_principal) as notional_principal, sum(t.ead) as ead, sum(t.as_ead) as as_ead from (select e.client_id, e.client_type, ei.cp_cva_industry, ei.cp_cva_credit, e.notional_principal, e.#{ead} as ead, e.transaction_maturity / 250 * e.discount_factor * e.#{ead} as as_ead from rwa_esr_di_exposure e join rwa_esi_di_exposure ei on ei.data_batch_no = #{dataBatchNo} and ei.exposure_id = e.exposure_id where e.result_no = #{resultNo} and e.netting_flag = '0' and e.qual_ccp_flag != '2' union all select n.client_id, n.client_type, ni.cp_cva_industry, ni.cp_cva_credit, n.notional_principal, n.#{ead} as ead, n.netting_maturity / 250 * n.discount_factor * n.#{ead} as as_ead from rwa_esr_di_netting n join rwa_esi_di_netting ni on ni.data_batch_no = #{dataBatchNo} and ni.netting_id = n.netting_id where n.result_no = #{resultNo} and n.qual_ccp_flag != '2') t group by t.client_id, t.client_type, t.cp_cva_industry, t.cp_cva_credit";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 2:
/* 2994 */         sql = "select #{cvaType} as cva_type, r.client_id, r.ead, r.notional_principal, m.mwp, null as org_id, r.client_type, r.client_external_rating from(select t.client_id, t.client_type, t.client_external_rating, sum(t.notional_principal) as notional_principal, sum(t.ead) as ead from(select e.client_id, e.client_type, ei.client_external_rating, e.notional_principal, e.mitigated_ead as ead from rwa_esr_di_exposure e join rwa_esi_di_exposure ei on ei.data_batch_no = #{dataBatchNo} and ei.exposure_id = e.exposure_id where e.result_no = #{resultNo} and e.netting_flag = '0' and e.qual_ccp_flag != '2' union all select e.client_id, e.client_type, ei.client_external_rating, e.notional_principal, e.mitigated_ead as ead from rwa_esr_di_netting e join rwa_esi_di_netting ei on ei.data_batch_no = #{dataBatchNo} and ei.netting_id = e.netting_id where e.result_no = #{resultNo} and e.qual_ccp_flag != '2' ) t group by t.client_id, t.client_type, t.client_external_rating ) r left join (select e.client_id, sum(e.notional_principal * e.transaction_maturity) as mwp from rwa_esr_di_exposure e where e.result_no = #{resultNo} and e.qual_ccp_flag != '2' group by e.client_id) m on m.client_id = r.client_id";
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
/*      */       case 3:
/*      */       case 4:
/* 3023 */         sql = "select #{cvaType} as cva_type, r.client_id, r.ead, r.notional_principal, r.as_ead, c.org_id, c.client_type, c.cp_cva_industry, c.cp_cva_credit from (select t.client_id, sum(t.notional_principal) as notional_principal, sum(t.ead) as ead, sum(t.as_ead) as as_ead from (select e.client_id, e.notional_principal, e.#{ead} as ead, e.transaction_maturity / 250 * e.discount_factor * e.#{ead} as as_ead from rwa_er_di_exposure e where e.result_no = #{resultNo} and e.netting_flag = '0' and e.qual_ccp_flag != '2' union all select n.client_id, n.notional_principal, n.#{ead} as ead, n.netting_maturity / 250 * n.discount_factor * n.#{ead} as as_ead from rwa_er_di_netting n where n.result_no = #{resultNo} and n.qual_ccp_flag != '2') t group by t.client_id) r left join rwa_ei_client c on c.data_batch_no = #{dataBatchNo} and c.client_id = r.client_id ";
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       case 5:
/*      */       case 6:
/* 3043 */         sql = "select #{cvaType} as cva_type, r.client_id, r.ead, r.notional_principal, m.mwp, c.org_id, c.client_type, c.client_external_rating from (select t.client_id, sum(t.notional_principal) as notional_principal, sum(t.ead) as ead from(select e.client_id, sum(e.notional_principal) as notional_principal, sum(e.mitigated_ead) as ead from rwa_er_di_exposure e where e.result_no = #{resultNo} and e.netting_flag = '0' and e.qual_ccp_flag != '2' group by e.client_id union all select e.client_id, sum(e.notional_principal) as notional_principal, sum(e.mitigated_ead) as ead from rwa_er_di_netting e where e.result_no = #{resultNo} and e.qual_ccp_flag != '2' group by e.client_id) t group by t.client_id) r left join rwa_ei_client c on c.data_batch_no = #{dataBatchNo} and c.client_id = r.client_id left join (select e.client_id, sum(e.notional_principal * e.transaction_maturity) as mwp from rwa_er_di_exposure e where e.result_no = #{resultNo} and e.qual_ccp_flag != '2' group by e.client_id) m on m.client_id = r.client_id";
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
/*      */       default:
/* 3068 */         throw new ParamConfigException("异常任务类型");
/*      */     } 
/* 3070 */     String ead = "ead";
/* 3071 */     if (StrUtil.equals(diEadApproach, DiEadApproach.CEM.getCode())) {
/* 3072 */       ead = "mitigated_ead";
/*      */     }
/* 3074 */     return SqlBuilder.create(sql)
/* 3075 */       .setString("resultNo", resultNo)
/* 3076 */       .setString("dataBatchNo", dataBatchNo)
/* 3077 */       .setString("cvaType", (ICodeEnum)cvaType)
/* 3078 */       .setKeyword("ead", ead)
/* 3079 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getCvaSqlMap(JobInfoDto jobInfo, CvaType cvaType) {
/* 3083 */     TaskType taskType = jobInfo.getTaskType();
/* 3084 */     String resultNo = jobInfo.getResultNo();
/* 3085 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3086 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/* 3087 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(jobInfo.getSchemeId());
/* 3088 */     sqlMap.put(InterfaceDataType.CVA.getCode(), getCvaSql(taskType, resultNo, dataBatchNo, cvaType, schemeConfigDo.getDiEadApproach()));
/* 3089 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public ItemPst getCvaSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 3093 */     String sql = "insert into #{result_table}(result_no, cva_type, client_id, org_id, data_batch_no, client_type, notional_principal, ead, cp_maturity, discount_factor, as_ead, cp_rw, scva) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */     
/* 3097 */     String tableName = null;
/* 3098 */     if (RwaUtils.isSingle(taskType)) {
/* 3099 */       tableName = "RWA_ESR_CVA";
/*      */     } else {
/* 3101 */       tableName = "RWA_ER_CVA";
/*      */     } 
/* 3103 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 3104 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public Map<String, ItemPst> getCvaResultSetterMap(JobInfoDto jobInfo) {
/* 3126 */     TaskType taskType = jobInfo.getTaskType();
/* 3127 */     String resultNo = jobInfo.getResultNo();
/* 3128 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3129 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 3130 */     pstSetterMap.put(InterfaceDataType.CVA.getCode(), getCvaSetter(taskType, resultNo, dataBatchNo));
/* 3131 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public Job sftJob(JobInfoDto jobInfo) {
/* 3135 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType();
/* 3136 */     IrbUncoveredProcess irbUncoveredProcess = IrbUncoveredProcess.CALCULATE;
/* 3137 */     JobBuilder jobBuilder = (new JobBuilder()).name(name + "-job");
/*      */     
/* 3139 */     if (containNetting(jobInfo.getRangeList(), Identity.YES)) {
/* 3140 */       jobBuilder.next(createRwaStep(name + "-sftn", jobInfo.getJobId(), 
/* 3141 */             createReader(jobInfo.getJobId(), name + "-sftn", this.dataSource, getSftSqlMap(jobInfo, Identity.YES), "netting_id"), (RwaProcessor)new SftProcessor(jobInfo, irbUncoveredProcess, Identity.YES), 
/*      */             
/* 3143 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getSftResultSetterMap(jobInfo))));
/*      */     }
/*      */     
/* 3146 */     if (containNetting(jobInfo.getRangeList(), Identity.NO)) {
/* 3147 */       jobBuilder.next(createRwaStep(name + "-sfte", jobInfo.getJobId(), 
/* 3148 */             createReader(jobInfo.getJobId(), name + "-sfte", this.dataSource, getSftSqlMap(jobInfo, Identity.NO), "exposure_id"), (RwaProcessor)new SftProcessor(jobInfo, irbUncoveredProcess, Identity.NO), 
/*      */             
/* 3150 */             createWriter(jobInfo.getJobId(), this.dataSource, true, getSftResultSetterMap(jobInfo))));
/*      */     }
/* 3152 */     return jobBuilder.listener((JobListener)new RwaJobListener(jobInfo)).build();
/*      */   }
/*      */   
/*      */   public String getSftNettingSql(TaskType taskType, String consolidateFlag, String dataBatchNo, List<TaskRangeDo> rangeList) {
/* 3156 */     String sql = null;
/* 3157 */     if (RwaUtils.isSingle(taskType)) {
/* 3158 */       sql = "select n.netting_id, n.client_id, n.org_id, n.asset_type, n.exposure_type_wa, n.exposure_type_irb, n.exposure_belong, n.sprv_tran_type, n.currency, n.default_flag, n.beel, n.default_lgd, n.pd, t.transactions_num, t.original_maturity, t.residual_maturity, n.client_name, n.client_type, n.or_rating, n.client_external_rating, n.investment_grade_flag, n.scra_result, n.is_consolidated_sub, n.sib_flag, n.annual_sale, n.qual_ccp_flag from rwa_esi_sft_netting n left join (select e.netting_id, count(1) as transactions_num, max(e.original_maturity) as original_maturity, max(e.residual_maturity) as residual_maturity from rwa_esi_sft_exposure e where e.data_batch_no = #{dataBatchNo} and e.netting_flag = '1' group by e.netting_id) t on t.netting_id = n.netting_id where n.data_batch_no = #{dataBatchNo} ";
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
/* 3172 */       return SqlBuilder.create(sql)
/* 3173 */         .setString("dataBatchNo", dataBatchNo)
/* 3174 */         .build();
/*      */     } 
/* 3176 */     sql = "select n.netting_id, n.client_id, n.org_id, n.asset_type, n.industry_id, n.business_line, #{ec} n.exposure_type_wa, n.exposure_type_irb, n.exposure_belong, n.book_type, n.sprv_tran_type, n.currency, n.default_flag, n.beel, n.default_lgd, n.model_id, n.rating, n.pd, t.transactions_num, t.original_maturity, t.residual_maturity, c.client_name, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.qual_ccp_flag from rwa_ei_sft_netting n left join rwa_ei_client c on c.data_batch_no = #{dataBatchNo} and c.client_id = n.client_id left join (select e.netting_id, count(1) as transactions_num, max(e.original_maturity) as original_maturity, max(e.residual_maturity) as residual_maturity from rwa_ei_sft_exposure e where e.data_batch_no = #{dataBatchNo} and e.netting_flag = '1' group by e.netting_id) t on t.netting_id = n.netting_id where n.data_batch_no = #{dataBatchNo} ";
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
/* 3192 */     return SqlBuilder.create(sql)
/* 3193 */       .condition("", RwaUtils.generateConditionSql(rangeList, Identity.YES))
/* 3194 */       .setString("dataBatchNo", dataBatchNo)
/*      */       
/* 3196 */       .condition(SqlBuilder.createQueryCondition(null, "n", "is_include_cons", null, consolidateFlag, 
/* 3197 */           StrUtil.equals(consolidateFlag, Identity.YES.getCode())))
/* 3198 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.SFTN)).clear("ec")
/* 3199 */       .build();
/*      */   }
/*      */   
/*      */   public String getSftExposureSql(TaskType taskType, String consolidateFlag, String dataBatchNo, Identity nettingFlag, List<TaskRangeDo> rangeList) {
/* 3203 */     String sql = null;
/* 3204 */     if (RwaUtils.isSingle(taskType)) {
/* 3205 */       sql = "select e.exposure_id, e.netting_id, e.netting_flag, e.client_id, e.org_id, e.instruments_type, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.sprv_tran_type, e.central_clear_flag, e.ccp_id, e.trading_role, e.currency, e.notional_principal, e.asset_balance, e.repo_amt, e.lend_amt, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.sft_type, e.repo_direction, e.gov_bond_type, e.secu_issue_rating, e.secu_issue_maturity, e.is_main_index, e.is_floating_rate, e.reva_frequency, e.default_flag, e.beel, e.default_lgd, e.pd, e.client_type, e.or_rating, e.client_external_rating, e.investment_grade_flag, e.scra_result, e.is_consolidated_sub, e.sib_flag, e.annual_sale, e.qual_ccp_flag, e.core_market_party_flag, e.secu_issuer_id as issuer_id, e.issuer_type, e.issuer_or_rating, e.issuer_ig_flag, e.issuer_scra_result from RWA_ESI_SFT_Exposure e where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
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
/* 3218 */       return SqlBuilder.create(sql)
/* 3219 */         .setString("dataBatchNo", dataBatchNo)
/* 3220 */         .setString("nettingFlag", nettingFlag.getCode())
/* 3221 */         .build();
/*      */     } 
/* 3223 */     sql = "select e.exposure_id, e.netting_id, e.netting_flag, e.client_id, e.org_id, e.industry_id, e.business_line, #{ec} e.instruments_type, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.sprv_tran_type, e.central_clear_flag, e.ccp_id, e.trading_role, e.currency, e.notional_principal, e.asset_balance, e.repo_amt, e.lend_amt, e.start_date, e.due_date, e.original_maturity, e.residual_maturity, e.sft_type, e.repo_direction, e.gov_bond_type, e.rating_duration_type, e.secu_issue_rating, e.secu_issue_maturity, e.is_main_index, e.is_floating_rate, e.reva_frequency, e.default_flag, e.beel, e.default_lgd, e.model_id, e.rating, e.pd, c.client_type, c.regist_state, c.or_rating, c.client_external_rating, c.investment_grade_flag, c.scra_result, c.is_consolidated_sub, c.sib_flag, c.annual_sale, c.qual_ccp_flag, c.core_market_party_flag, ic.client_id as issuer_id, ic.client_type as issuer_type, ic.regist_state as issuer_regist_state, ic.or_rating as issuer_or_rating, ic.client_external_rating as issuer_external_rating, ic.investment_grade_flag as issuer_ig_flag, ic.scra_result as issuer_scra_result, ic.is_consolidated_sub as issuer_is_sub from RWA_EI_SFT_Exposure e left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = e.client_id left join RWA_EI_Client ic   on ic.data_batch_no = #{dataBatchNo} and ic.client_id = e.secu_issuer_id left join rwa_ei_sft_netting n   on n.data_batch_no = #{dataBatchNo} and n.netting_id = e.netting_id where e.data_batch_no = #{dataBatchNo} and e.netting_flag = #{nettingFlag}";
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
/* 3244 */     return SqlBuilder.create(sql)
/* 3245 */       .condition("", RwaUtils.generateConditionSql(rangeList, nettingFlag))
/* 3246 */       .setString("dataBatchNo", dataBatchNo)
/* 3247 */       .setString("nettingFlag", nettingFlag.getCode())
/*      */       
/* 3249 */       .condition(SqlBuilder.createQueryCondition(null, "e", "is_include_cons", null, consolidateFlag, 
/* 3250 */           StrUtil.equals(consolidateFlag, Identity.YES.getCode())))
/* 3251 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.SFTE)).clear("ec")
/* 3252 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getSftCollateralSql(TaskType taskType, String dataBatchNo, Identity nettingFlag, List<TaskRangeDo> rangeList) {
/* 3257 */     String sql = null;
/* 3258 */     if (RwaUtils.isSingle(taskType)) {
/* 3259 */       sql = "select cl.collateral_id, cl.netting_flag, cl.netting_id, cl.exposure_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.collateral_amount, cl.currency, cl.original_maturity, cl.residual_maturity, cl.gov_bond_type, cl.is_main_index, cl.is_floating_rate, cl.reva_frequency, cl.financial_coll_rating as secu_issue_rating, cl.issuer_type, cl.issuer_or_rating, cl.issuer_external_rating, cl.issuer_ig_flag, cl.issuer_scra_result, cl.issuer_is_sub from RWA_ESI_SFT_Collateral cl where cl.data_batch_no = #{dataBatchNo} and cl.netting_flag = #{nettingFlag}";
/*      */ 
/*      */ 
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3269 */       sql = "select cl.collateral_id, cl.netting_flag, cl.netting_id, cl.exposure_id, cl.issuer_id, cl.is_apply_wa, cl.is_apply_firb, cl.mitigation_main_type, cl.mitigation_small_type, cl.collateral_amount, cl.currency, cl.original_maturity, cl.residual_maturity, cl.gov_bond_type, cl.is_main_index, cl.is_floating_rate, cl.rating_duration_type, cl.reva_frequency, cl.financial_coll_rating as secu_issue_rating, c.client_type as issuer_type, c.regist_state as issuer_regist_state, c.or_rating as issuer_or_rating, c.client_external_rating as issuer_external_rating, c.investment_grade_flag as issuer_ig_flag, c.scra_result as issuer_scra_result, c.is_consolidated_sub as issuer_is_sub, c.qual_ccp_flag as issuer_qual_ccp_flag from RWA_EI_SFT_Collateral cl left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = cl.issuer_id where cl.data_batch_no = #{dataBatchNo} and cl.netting_flag = #{nettingFlag}";
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
/*      */ 
/*      */ 
/*      */     
/* 3283 */     return SqlBuilder.create(sql)
/* 3284 */       .setString("dataBatchNo", dataBatchNo)
/* 3285 */       .setString("nettingFlag", nettingFlag.getCode())
/* 3286 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getSftSqlMap(JobInfoDto jobInfo, Identity nettingFlag) {
/* 3290 */     TaskType taskType = jobInfo.getTaskType();
/* 3291 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(jobInfo.getSchemeId());
/* 3292 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3293 */     List<TaskRangeDo> rangeList = jobInfo.getRangeList();
/* 3294 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/*      */     
/* 3296 */     sqlMap.put(InterfaceDataType.SFT_EXPOSURE.getCode(), getSftExposureSql(taskType, jobInfo.getTaskConfigDo().getConsolidateFlag(), dataBatchNo, nettingFlag, rangeList));
/*      */     
/* 3298 */     sqlMap.put(InterfaceDataType.SFT_COLLATERAL.getCode(), getSftCollateralSql(taskType, dataBatchNo, nettingFlag, rangeList));
/*      */     
/* 3300 */     if (nettingFlag == Identity.YES) {
/* 3301 */       sqlMap.put(InterfaceDataType.SFT_NETTING.getCode(), getSftNettingSql(taskType, jobInfo.getTaskConfigDo().getConsolidateFlag(), dataBatchNo, rangeList));
/*      */     }
/* 3303 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public ItemPst getSftNettingSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 3307 */     String sql = "insert into #{result_table}(result_no, netting_id, data_batch_no, approach, client_id, client_name, client_type, annual_sale, qual_ccp_flag, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_rpt_item_wa, exposure_belong, book_type, currency, asset_balance, original_maturity, residual_maturity, is_zero_haircut, haircut_line_adjust_flag, effective_factor_line, effective_haircut, is_touch_line, he, he_amount, fc_value, fc_haircut, hc_amount, hfx, hfx_amount, net_exposure, gross_exposure, netting_security_cnt, max_security_amount, mitigated_ea, tm, netting_maturity, discount_factor, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, rwa, formula_no, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3316 */     String tableName = null;
/* 3317 */     if (RwaUtils.isSingle(taskType)) {
/* 3318 */       tableName = "RWA_ESR_SFT_Netting";
/*      */     } else {
/* 3320 */       tableName = "RWA_ER_SFT_Netting";
/*      */     } 
/* 3322 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 3323 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public ItemPst getSftExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 3391 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, approach, netting_id, netting_flag, client_id, client_type, annual_sale, qual_ccp_flag, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_rpt_item_wa, exposure_belong, book_type, central_clear_flag, ccp_id, trading_role, currency, notional_principal, asset_balance, repo_amt, lend_amt, original_maturity, residual_maturity, repo_direction, reva_frequency, issuer_id, issuer_type, haircut_line_adjust_flag, factor_line, effective_factor_line, effective_haircut, is_touch_line, core_market_party_flag, is_zero_haircut, she, he, fc_value, fc_haircut, hfx, mitigated_ea, tm, transaction_maturity, discount_factor, default_flag, beel, default_lgd, model_id, rating, pd, lgd, maturity, correlation, bma, kcr, rw, rwa, formula_no, coll_ead, coll_rwa, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3401 */     String tableName = null;
/* 3402 */     if (RwaUtils.isSingle(taskType)) {
/* 3403 */       tableName = "RWA_ESR_SFT_Exposure";
/*      */     } else {
/* 3405 */       tableName = "RWA_ER_SFT_Exposure";
/*      */     } 
/* 3407 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 3408 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */   public ItemPst getSftCollateralSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 3485 */     String sql = "insert into #{result_table}(result_no, collateral_id, data_batch_no, approach, netting_flag, netting_id, exposure_id, issuer_id, issuer_type, is_apply_wa, is_apply_firb, qual_flag_wa, qual_flag_firb, mitigation_main_type, mitigation_small_type, collateral_amount, currency, original_maturity, residual_maturity, reva_frequency, is_zero_haircut, factor_line, sh, haircut, hfx, supervision_class, exposure, rw, rwa) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3491 */     String tableName = null;
/* 3492 */     if (RwaUtils.isSingle(taskType)) {
/* 3493 */       tableName = "RWA_ESR_SFT_Collateral";
/*      */     } else {
/* 3495 */       tableName = "RWA_ER_SFT_Collateral";
/*      */     } 
/* 3497 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 3498 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ItemPst> getSftResultSetterMap(JobInfoDto jobInfo) {
/* 3538 */     TaskType taskType = jobInfo.getTaskType();
/* 3539 */     String resultNo = jobInfo.getResultNo();
/* 3540 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3541 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 3542 */     pstSetterMap.put(ResultDataType.SFT_EXPOSURE.getCode(), getSftExposureSetter(taskType, resultNo, dataBatchNo));
/* 3543 */     pstSetterMap.put(ResultDataType.SFT_COLLATERAL.getCode(), getSftCollateralSetter(taskType, resultNo, dataBatchNo));
/* 3544 */     pstSetterMap.put(ResultDataType.SFT_NETTING.getCode(), getSftNettingSetter(taskType, resultNo, dataBatchNo));
/* 3545 */     return pstSetterMap;
/*      */   }
/*      */   
/*      */   public Job dfJob(JobInfoDto jobInfo) {
/* 3549 */     String name = jobInfo.getJobId() + "-" + jobInfo.getJobType();
/* 3550 */     IrbUncoveredProcess irbUncoveredProcess = IrbUncoveredProcess.CALCULATE;
/* 3551 */     return (new JobBuilder()).name(name + "-job")
/* 3552 */       .next(createRwaStep(name + "-df", jobInfo.getJobId(), 
/* 3553 */           createReader(jobInfo.getJobId(), name + "-df", this.dataSource, getDfSqlMap(jobInfo), "exposure_id"), (RwaProcessor)new DfProcessor(jobInfo, irbUncoveredProcess), 
/*      */           
/* 3555 */           createWriter(jobInfo.getJobId(), this.dataSource, true, getDfResultSetterMap(jobInfo))))
/*      */       
/* 3557 */       .listener((JobListener)new RwaJobListener(jobInfo))
/* 3558 */       .build();
/*      */   }
/*      */ 
/*      */   
/*      */   public String getDfExposureSql(TaskType taskType, String dataBatchNo) {
/* 3563 */     String sql = null;
/* 3564 */     if (RwaUtils.isSingle(taskType)) {
/* 3565 */       sql = "select e.exposure_id, e.client_id, e.org_id, e.asset_type, e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.currency, e.df_pruduct, e.df_prepaid, e.df_unpaid, e.k_ccd, e.df_cm, e.df_ccd, e.qual_ccp_flag from RWA_ESI_CCP_DF e where e.data_batch_no = #{dataBatchNo}";
/*      */     
/*      */     }
/*      */     else {
/*      */ 
/*      */       
/* 3571 */       sql = "select e.exposure_id, e.client_id, e.org_id, e.asset_type, e.industry_id, e.business_line, #{ec} e.exposure_type_wa, e.exposure_type_irb, e.exposure_belong, e.book_type, e.currency, e.df_pruduct, e.df_prepaid, e.df_unpaid, e.k_ccd, e.df_cm, e.df_ccd, c.client_type, c.investment_grade_flag, c.qual_ccp_flag from RWA_EI_CCP_DF e left join RWA_EI_Client c   on c.data_batch_no = #{dataBatchNo} and c.client_id = e.client_id where e.data_batch_no = #{dataBatchNo}";
/*      */     } 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3580 */     return SqlBuilder.create(sql)
/* 3581 */       .setString("dataBatchNo", dataBatchNo)
/* 3582 */       .setKeyword("ec", RwaUtils.getEcColumn(EcColumn.DFE)).clear("ec")
/* 3583 */       .build();
/*      */   }
/*      */   
/*      */   public LinkedHashMap<String, String> getDfSqlMap(JobInfoDto jobInfo) {
/* 3587 */     TaskType taskType = jobInfo.getTaskType();
/* 3588 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3589 */     LinkedHashMap<String, String> sqlMap = new LinkedHashMap<>();
/* 3590 */     sqlMap.put(InterfaceDataType.CCP_DF.getCode(), getDfExposureSql(taskType, dataBatchNo));
/* 3591 */     return sqlMap;
/*      */   }
/*      */   
/*      */   public ItemPst getDfExposureSetter(TaskType taskType, String resultNo, String dataBatchNo) {
/* 3595 */     String sql = "insert into #{result_table}(result_no, exposure_id, data_batch_no, approach, client_id, client_type, qual_ccp_flag, org_id, asset_type, exposure_type_wa, exposure_type_irb, exposure_rpt_item_wa, exposure_belong, book_type, currency, df_pruduct, df_prepaid, df_unpaid, k_ccd, df_cm, df_ccd, df, kcr, rc, rwa, ec_param_info, ec_df, ec) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 3600 */     String tableName = null;
/* 3601 */     if (RwaUtils.isSingle(taskType)) {
/* 3602 */       tableName = "RWA_ESR_CCP_DF";
/*      */     } else {
/* 3604 */       tableName = "RWA_ER_CCP_DF";
/*      */     } 
/* 3606 */     sql = SqlBuilder.create(sql).setTable("result_table", tableName).build();
/* 3607 */     return ItemPst.createSetter(sql, (ItemPstSetter)new Object(this, resultNo, dataBatchNo));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, ItemPst> getDfResultSetterMap(JobInfoDto jobInfo) {
/* 3644 */     TaskType taskType = jobInfo.getTaskType();
/* 3645 */     String resultNo = jobInfo.getResultNo();
/* 3646 */     String dataBatchNo = jobInfo.getDataBatchNo();
/* 3647 */     Map<String, ItemPst> pstSetterMap = new LinkedHashMap<>();
/* 3648 */     pstSetterMap.put(InterfaceDataType.CCP_DF.getCode(), getDfExposureSetter(taskType, resultNo, dataBatchNo));
/* 3649 */     return pstSetterMap;
/*      */   }
/*      */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaJobBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */