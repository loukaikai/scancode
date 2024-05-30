/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ 
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*     */ import com.amarsoft.rwa.engine.constant.QualCcp;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.job.processor.RwaProcessor;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class DfProcessor
/*     */   extends RwaProcessor
/*     */ {
/*     */   private CreditRuleDo creditRule;
/*     */   
/*     */   public DfProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess) {
/*  31 */     super(jobInfo, irbUncoveredProcess);
/*  32 */     this.creditRule = getSchemeConfig().getWaParamVersion().getCreditRule();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {}
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  41 */     return ExposureApproach.WA;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void paramMapping(Map<String, Object> item) throws Exception {}
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/*  50 */     List<Map<String, Object>> exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.CCP_DF.getCode());
/*     */     
/*  52 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/*  53 */     for (Map<String, Object> exposure : exposureList) {
/*     */       
/*  55 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */       
/*  57 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, this.creditRule.getCcpRptItem());
/*     */       
/*  59 */       BigDecimal df = RwaMath.add(DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.DF_PREPAID), DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.DF_UNPAID));
/*  60 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.DF, df);
/*  61 */       BigDecimal kcr = BigDecimal.ONE;
/*  62 */       BigDecimal rwa = null;
/*     */ 
/*     */       
/*  65 */       if (StrUtil.equals(QualCcp.QUAL_CCP.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.QUAL_CCP_FLAG))) {
/*     */         
/*  67 */         BigDecimal kccd = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.K_CCD);
/*  68 */         BigDecimal dfccd = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.DF_CCD);
/*  69 */         BigDecimal dfcm = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.DF_CM);
/*     */         
/*  71 */         BigDecimal rc = getK(kccd, dfccd, dfcm, df);
/*  72 */         kcr = RwaMath.div(rc, df);
/*  73 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RC, rc);
/*  74 */         rwa = RwaMath.mul(rc, RwaMath.maxRw);
/*     */       } else {
/*     */         
/*  77 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RC, df);
/*  78 */         rwa = RwaMath.mul(df, RwaMath.maxRw);
/*     */       } 
/*  80 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.K, kcr);
/*  81 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RWA, rwa);
/*     */       
/*  83 */       if (taskConfigDo.isEnEcCalc()) {
/*  84 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(exposure);
/*  85 */         BigDecimal ecDf = RwaMath.getEcDf(mappingEcFactor);
/*  86 */         BigDecimal ec = RwaMath.getEc(rwa, ecDf, taskConfigDo.getEcOf());
/*  87 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.EC_PARAM_INFO, RwaUtils.getEcParamInfo(mappingEcFactor));
/*  88 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EC_DF, ecDf);
/*  89 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EC, ec);
/*     */       } 
/*     */     } 
/*  92 */     return item;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getK(BigDecimal kccd, BigDecimal dfccd, BigDecimal dfcm, BigDecimal df) {
/*  98 */     BigDecimal k1 = RwaMath.mul(kccd, RwaMath.div(df, RwaMath.add(dfccd, dfcm)));
/*  99 */     BigDecimal k2 = RwaMath.mul(new Number[] { Double.valueOf(0.08D), Double.valueOf(0.02D), df });
/* 100 */     return NumberUtil.max(new BigDecimal[] { k1, k2 });
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\DfProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */