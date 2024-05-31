/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.DiEadApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*     */ import com.amarsoft.rwa.engine.constant.QualCcp;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.DiCollateralDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiNettingDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiUnionDto;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class DiProcessor extends RwaProcessor {
/*  30 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.processor.DiProcessor.class);
/*     */   private DiEadApproach diEadApproach; private Identity nettingFlag; private CreditRuleDo waCreditRule; private QualCcp qualCcp; private Map<String, Object> netting; private Map<String, Object> exposure; private List<Map<String, Object>> exposureList;
/*     */   private List<Map<String, Object>> collateralList;
/*     */   
/*  34 */   public Identity getNettingFlag() { return this.nettingFlag; } public void setNettingFlag(Identity nettingFlag) {
/*  35 */     this.nettingFlag = nettingFlag;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public DiProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull Identity nettingFlag) {
/*  45 */     super(jobInfo, irbUncoveredProcess);
/*  46 */     this.nettingFlag = nettingFlag;
/*  47 */     this.waCreditRule = getSchemeConfig().getWaParamVersion().getCreditRule();
/*  48 */     this.diEadApproach = (DiEadApproach)EnumUtils.getEnumByCode(getSchemeConfig().getDiEadApproach(), DiEadApproach.class);
/*     */   }
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  53 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.DI_EXPOSURE.getCode());
/*  54 */     this.collateralList = (List<Map<String, Object>>)item.get(InterfaceDataType.DI_COLLATERAL.getCode());
/*     */     
/*  56 */     if (this.nettingFlag == Identity.YES) {
/*  57 */       this.netting = DataUtils.getFirstItem(item.get(InterfaceDataType.DI_NETTING.getCode()));
/*  58 */       this.exposure = null;
/*  59 */       this.qualCcp = (QualCcp)EnumUtils.getEnumByCode(DataUtils.getString(this.netting, (ICodeEnum)RwaParam.QUAL_CCP_FLAG), QualCcp.class);
/*     */     } else {
/*  61 */       this.netting = null;
/*  62 */       this.exposure = DataUtils.getFirstItem(this.exposureList);
/*  63 */       this.qualCcp = (QualCcp)EnumUtils.getEnumByCode(DataUtils.getString(this.exposure, (ICodeEnum)RwaParam.QUAL_CCP_FLAG), QualCcp.class);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  69 */     if (getApproach() == Approach.WA) {
/*  70 */       return ExposureApproach.WA;
/*     */     }
/*  72 */     throw new JobParameterException("非法计算方法：" + getApproach());
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
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/*  90 */     paramMappingExposure(InterfaceDataType.DI_NETTING, this.netting, DataUtils.getString(this.netting, (ICodeEnum)RwaParam.NETTING_ID));
/*     */     
/*  92 */     for (Map<String, Object> data : this.exposureList) {
/*     */       
/*  94 */       RwaMapping.paramMappingDerivatives(data, this.diEadApproach, getSchemeConfig());
/*     */       
/*  96 */       if (this.nettingFlag == Identity.NO) {
/*  97 */         paramMappingExposure(InterfaceDataType.DI_EXPOSURE, data, DataUtils.getString(data, (ICodeEnum)RwaParam.EXPOSURE_ID));
/*     */         continue;
/*     */       } 
/* 100 */       DataUtils.setString(data, this.netting, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA);
/* 101 */       DataUtils.setBigDecimal(data, this.netting, (ICodeEnum)RwaParam.RW);
/* 102 */       DataUtils.setBigDecimal(data, this.netting, (ICodeEnum)RwaParam.TM);
/*     */     } 
/*     */ 
/*     */     
/* 106 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 107 */       for (Map<String, Object> collateral : this.collateralList) {
/* 108 */         paramMappingCollateral(InterfaceDataType.DI_COLLATERAL, collateral, DataUtils.getString(collateral, (ICodeEnum)RwaParam.COLLATERAL_ID));
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 115 */     DiUnionDto unionDto = initUnion(DataUtils.getString(item, (ICodeEnum)RwaParam.ID), getJobInfo().getDataDate(), getSchemeConfig(), getConfirmApproach(), this.exposureList.size(), this.nettingFlag);
/*     */     
/* 117 */     if (this.nettingFlag == Identity.YES) {
/* 118 */       convert2Netting(unionDto, this.netting);
/*     */     }
/*     */     
/* 121 */     convert2Exposure(unionDto, this.exposureList);
/*     */     
/* 123 */     convert2Collateral(unionDto, this.collateralList);
/* 124 */     if (this.diEadApproach == DiEadApproach.SA)
/*     */     {
/* 126 */       throw new ParamConfigException("非法计算方法！");
/*     */     }
/*     */     
/* 129 */     DiCalculation2.createCalculation(unionDto).execute();
/*     */ 
/*     */     
/* 132 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 133 */     if (taskConfigDo.isEnEcCalc()) {
/* 134 */       if (this.nettingFlag == Identity.YES) {
/* 135 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(this.netting);
/* 136 */         DiNettingDto nettingDto = unionDto.getNettingDto();
/* 137 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 138 */         nettingDto.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 139 */         nettingDto.setEcDf(df);
/* 140 */         nettingDto.setEc(RwaMath.getEc(nettingDto.getRwa(), df, taskConfigDo.getEcOf()));
/*     */       } else {
/* 142 */         DiExposureDto expo = unionDto.getExposureList().get(0);
/* 143 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(this.exposure);
/* 144 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 145 */         expo.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 146 */         expo.setEcDf(df);
/* 147 */         expo.setEc(RwaMath.getEc(expo.getRwa(), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     }
/* 150 */     return RwaUtils.beanToMap(unionDto);
/*     */   }
/*     */   
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure, String id) {
/* 154 */     if (exposure == null) {
/*     */       return;
/*     */     }
/*     */     
/* 158 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 160 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_PROP, BigDecimal.ZERO);
/*     */     
/* 162 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.CLAIMS_LEVEL, ClaimsLevel.SENIOR.getCode());
/*     */     
/* 164 */     String exposureTypeIrb = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_IRB);
/*     */     
/* 166 */     Identity defaultFlag = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.DEFAULT_FLAG), Identity.class);
/*     */     
/* 168 */     RwaMapping.mappingExposureCpRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */ 
/*     */     
/* 171 */     if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.CENTRAL_CLEAR_FLAG)) && 
/* 172 */       StrUtil.equals(TradingRole.CUSTOMER.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.TRADING_ROLE))) {
/*     */       
/* 174 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, this.waCreditRule.getCcpRw());
/* 175 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, this.waCreditRule.getCcpRptItem());
/*     */     } 
/*     */     
/* 178 */     RwaMapping.mappingExposureTm(getSchemeConfig(), getJobId(), dataType, exposure, id, Integer.valueOf(10));
/*     */   }
/*     */   
/*     */   public void paramMappingCollateral(InterfaceDataType dataType, Map<String, Object> collateral, String id) {
/* 182 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 184 */     BigDecimal collateralAmount = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT);
/* 185 */     if (!RwaMath.isPositive(collateralAmount)) {
/* 186 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT, BigDecimal.ZERO);
/* 187 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 188 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/* 189 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.EXPOSURE, BigDecimal.ZERO);
/* 190 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_COLLATERAL);
/*     */       
/*     */       return;
/*     */     } 
/* 194 */     if (StrUtil.equals(DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_OUR_BANK_SUBMIT), Identity.YES.getCode())) {
/* 195 */       RwaMapping.mappingHe(getSchemeConfig(), getJobId(), dataType, collateral, id, this.waCreditRule.getDefaultExpoSh());
/*     */       
/* 197 */       if (StrUtil.equals(DataUtils.getString(collateral, (ICodeEnum)RwaParam.BANKRUPTCY_SEPARATION_FLAG), Identity.YES.getCode())) {
/*     */         
/* 199 */         DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.EXPOSURE, BigDecimal.ZERO);
/*     */       } else {
/*     */         
/* 202 */         RwaMapping.mappingExposureCpRw(getSchemeConfig(), getJobId(), dataType, collateral, id, Identity.NO);
/* 203 */         DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.EXPOSURE, collateralAmount);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 208 */     if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_APPLY_FIRB))) {
/* 209 */       BigDecimal sh = RwaMapping.mappingHc(getSchemeConfig(), getJobId(), dataType, collateral, id);
/* 210 */       if (sh == null) {
/* 211 */         DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */       } else {
/* 213 */         DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.YES.getCode());
/*     */       } 
/*     */     } else {
/* 216 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */     } 
/*     */   }
/*     */   
/*     */   public DiUnionDto initUnion(String id, Date dataDate, SchemeConfigDo schemeConfig, ExposureApproach approach, int size, Identity nettingFlag) {
/* 221 */     DiUnionDto unionDto = new DiUnionDto();
/* 222 */     unionDto.setId(id);
/* 223 */     unionDto.setDataDate(dataDate);
/* 224 */     unionDto.setSchemeConfig(schemeConfig);
/* 225 */     unionDto.setApproach(approach);
/* 226 */     unionDto.setSize(size);
/* 227 */     unionDto.setNettingFlag(nettingFlag);
/* 228 */     return unionDto;
/*     */   }
/*     */   
/*     */   public DiUnionDto convert2Netting(DiUnionDto unionDto, Map<String, Object> netting) throws InstantiationException, IllegalAccessException {
/* 232 */     DiNettingDto nettingDto = RwaUtils.convert2DiNetting(netting);
/* 233 */     nettingDto.setDiEadApproach(this.diEadApproach.getCode());
/*     */ 
/*     */     
/* 236 */     unionDto.setNettingDto(nettingDto);
/* 237 */     unionDto.setNettingList(CollUtil.toList((Object[])new DiNettingDto[] { nettingDto }));
/* 238 */     return unionDto;
/*     */   }
/*     */   
/*     */   public DiUnionDto convert2Exposure(DiUnionDto unionDto, List<Map<String, Object>> dataList) throws InstantiationException, IllegalAccessException {
/* 242 */     List<DiExposureDto> list = new ArrayList<>();
/* 243 */     for (Map<String, Object> data : dataList) {
/* 244 */       DiExposureDto exposureDto = RwaUtils.convert2DiExposure(data);
/* 245 */       exposureDto.setDiEadApproach(this.diEadApproach.getCode());
/*     */ 
/*     */       
/* 248 */       list.add(exposureDto);
/*     */     } 
/* 250 */     unionDto.setExposureList(list);
/* 251 */     return unionDto;
/*     */   }
/*     */   
/*     */   public DiUnionDto convert2Collateral(DiUnionDto unionDto, List<Map<String, Object>> dataList) throws InstantiationException, IllegalAccessException {
/* 255 */     List<DiCollateralDto> list = new ArrayList<>();
/* 256 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 257 */       for (Map<String, Object> data : dataList) {
/* 258 */         list.add(RwaUtils.convert2DiCollateral(data));
/*     */       }
/*     */     }
/* 261 */     unionDto.setCollateralList(list);
/* 262 */     return unionDto;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\DiProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */