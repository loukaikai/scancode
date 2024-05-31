/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureBelong;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.constant.UnionType;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateAssetDo;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class NonRetailProcessor extends RwaProcessor {
/*  27 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.processor.NonRetailProcessor.class);
/*     */   
/*     */   private UnionType unionType;
/*     */   
/*     */   private List<Map<String, Object>> exposureList;
/*     */   private List<Map<String, Object>> relevanceList;
/*     */   private List<Map<String, Object>> collateralList;
/*     */   private List<Map<String, Object>> guaranteeList;
/*     */   private UnionDto unionDto;
/*     */   
/*     */   public NonRetailProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull UnionType unionType) {
/*  38 */     super(jobInfo, irbUncoveredProcess);
/*  39 */     this.unionType = unionType;
/*     */   }
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  44 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.EXPOSURE.getCode());
/*  45 */     this.relevanceList = (List<Map<String, Object>>)item.get(InterfaceDataType.RELEVANCE.getCode());
/*  46 */     this.collateralList = (List<Map<String, Object>>)item.get(InterfaceDataType.COLLATERAL.getCode());
/*  47 */     this.guaranteeList = (List<Map<String, Object>>)item.get(InterfaceDataType.GUARANTEE.getCode());
/*  48 */     this.unionDto = new UnionDto();
/*  49 */     this.unionDto.setId(DataUtils.getString(item, (ICodeEnum)RwaParam.ID));
/*  50 */     this.unionDto.setSize(DataUtils.getCollSize(new Collection[] { this.exposureList }));
/*  51 */     this.unionDto.setSchemeConfig(getSchemeConfig());
/*  52 */     this.unionDto.setUnionType(this.unionType);
/*  53 */     this.unionDto.setTaskType(getTaskType());
/*     */   }
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  58 */     if (getApproach() == Approach.WA) {
/*  59 */       return ExposureApproach.WA;
/*     */     }
/*  61 */     throw new JobParameterException("非法计算方法：" + getApproach());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/*  67 */     this.unionDto.setApproach(getConfirmApproach());
/*     */     
/*  69 */     this.unionDto.setMitigationSize(DataUtils.getCollSize(new Collection[] { this.collateralList, this.guaranteeList }));
/*  70 */     if (this.unionDto.getMitigationSize() > 0) {
/*  71 */       RwaUtils.convert2Relevance(this.relevanceList, this.unionDto);
/*     */     } else {
/*  73 */       this.unionDto.setRelevanceSize(0);
/*     */     } 
/*     */     
/*  76 */     Map<String, Map<String, Object>> reeMap = null;
/*  77 */     if (RwaConfig.enableRealEstateLtvCalculate && 
/*  78 */       getSchemeConfig().getWaParamVersion().getCreditRule().isEnRetExpoRwMapping() && 
/*  79 */       !CollUtil.isEmpty(reeMap = RwaUtils.getRealEstateExposureMap(this.exposureList, this.unionDto.getExposureRelevanceMap(), this.unionDto.getMitigationRelevanceMap())))
/*     */     {
/*  81 */       RwaUtils.calculateLtv(reeMap, this.unionDto.getMitigationRelevanceMap(), this.collateralList);
/*     */     }
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
/* 102 */     Set<MitigateAssetDo> mitigateAssetDoSet = new TreeSet<>();
/* 103 */     for (Map<String, Object> exposure : this.exposureList) {
/* 104 */       paramMappingExposure(InterfaceDataType.EXPOSURE, exposure);
/*     */       
/* 106 */       MitigateAssetDo mitigateAssetDo = RwaConfig.getMitigateAsset(getSchemeConfig(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.ASSET_TYPE), getConfirmApproach().getCode());
/* 107 */       if (mitigateAssetDo != null) {
/* 108 */         mitigateAssetDoSet.add(mitigateAssetDo);
/*     */       }
/*     */     } 
/*     */     
/* 112 */     this.unionDto.setMitigateAssetDo(RwaConfig.getMitigateAsset(mitigateAssetDoSet, getSchemeConfig(), getConfirmApproach().getCode()));
/*     */     
/* 114 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 115 */       for (Map<String, Object> collateral : this.collateralList) {
/* 116 */         paramMappingCollateral(InterfaceDataType.COLLATERAL, collateral);
/*     */       }
/*     */     }
/*     */     
/* 120 */     if (!CollUtil.isEmpty(this.guaranteeList)) {
/* 121 */       for (Map<String, Object> guarantee : this.guaranteeList) {
/* 122 */         paramMappingGuarantee(InterfaceDataType.GUARANTEE, guarantee);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 133 */     RwaUtils.convert2Exposure(item, this.unionDto, false);
/* 134 */     RwaUtils.convert2Mitigation(item, this.unionDto);
/* 135 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[getConfirmApproach().ordinal()]) {
/*     */       case 1:
/* 137 */         WaCalculation.createCalculation(this.unionDto).execute();
/*     */         break;
/*     */       default:
/* 140 */         throw new JobParameterException("非法计算方法：" + getConfirmApproach());
/*     */     } 
/*     */     
/* 143 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 144 */     for (Map<String, Object> expo : this.exposureList) {
/* 145 */       ExposureDto exposureDto = (ExposureDto)this.unionDto.getExposureMap().get(DataUtils.getString(expo, (ICodeEnum)RwaParam.EXPOSURE_ID));
/* 146 */       if (exposureDto == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 150 */       if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnAmpCalc() && 
/* 151 */         StrUtil.equals(exposureDto.getAmpFlag(), Identity.YES.getCode())) {
/* 152 */         this.unionDto.getAmpResultList().add(exposureDto);
/* 153 */         if (StrUtil.equals(exposureDto.getIsTpCalc(), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */           
/* 157 */           exposureDto.setRw(NumberUtil.mul(exposureDto.getRw(), Double.valueOf(1.2D)));
/* 158 */           exposureDto.setWarw(NumberUtil.mul(exposureDto.getWarw(), Double.valueOf(1.2D)));
/* 159 */           exposureDto.setRwaMb(NumberUtil.mul(exposureDto.getRwaMb(), Double.valueOf(1.2D)));
/* 160 */           exposureDto.setRwaMa(NumberUtil.mul(exposureDto.getRwaMa(), Double.valueOf(1.2D)));
/* 161 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/* 162 */         } else if (!StrUtil.equals(exposureDto.getAmpApproach(), AmpApproach.OTHER.getCode()) && 
/* 163 */           NumberUtil.isGreater(exposureDto.getRwaMa(), RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw))) {
/*     */           
/* 165 */           exposureDto.setRwaAa(RwaMath.sub(RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw), exposureDto.getRwaMa()));
/*     */         } else {
/*     */           
/* 168 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */         } 
/*     */       } else {
/* 171 */         exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */       } 
/* 173 */       exposureDto.setRwaAdj(RwaMath.add(exposureDto.getRwaMa(), exposureDto.getRwaAa()));
/*     */       
/* 175 */       if (taskConfigDo.isEnEcCalc()) {
/* 176 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(expo);
/* 177 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 178 */         exposureDto.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 179 */         exposureDto.setEcDf(df);
/* 180 */         exposureDto.setEc(RwaMath.getEc(exposureDto.getRwaAdj(), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     } 
/* 183 */     return RwaUtils.beanToMap(this.unionDto);
/*     */   }
/*     */   
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure) {
/* 187 */     CreditRuleDo creditRuleDo = getSchemeConfig().getWaParamVersion().getCreditRule();
/* 188 */     String id = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID);
/*     */     
/* 190 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 192 */     ExposureBelong exposureBelong = (ExposureBelong)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_BELONG), ExposureBelong.class);
/* 193 */     BigDecimal ccf = null;
/* 194 */     if (exposureBelong == ExposureBelong.OFF) {
/* 195 */       ccf = RwaMapping.mappingExposureCcfWa(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */     }
/*     */ 
/*     */     
/* 199 */     BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*     */     
/* 201 */     if (balance == null) {
/* 202 */       balance = BigDecimal.ZERO;
/* 203 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/*     */     
/* 206 */     if (creditRuleDo.isEnAmpCalc() && StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.AMP_FLAG))) {
/*     */       
/* 208 */       BigDecimal lr = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR);
/* 209 */       if (RwaMath.isNullOrNegative(lr) || RwaMath.isZero(lr)) {
/* 210 */         lr = BigDecimal.ONE;
/* 211 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR, lr);
/*     */       } 
/* 213 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.NET_ASSET, balance);
/* 214 */       balance = RwaMath.mul(balance, lr);
/* 215 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/*     */     
/* 218 */     BigDecimal provision = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION);
/*     */     
/* 220 */     if (RwaMath.isNullOrNegative(provision)) {
/* 221 */       provision = BigDecimal.ZERO;
/* 222 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION, BigDecimal.ZERO);
/*     */     } 
/* 224 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_PROP, RwaMath.getProvisionProp(balance, ccf, provision));
/* 225 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_DED, RwaMath.getProvisionDed(balance, ccf, provision));
/*     */     
/* 227 */     Identity defaultFlag = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.DEFAULT_FLAG), Identity.class);
/* 228 */     BigDecimal rw = null;
/* 229 */     String exposureTypeWa = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/* 230 */     if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnRetExpoRwMapping() && RwaUtils.isRealEstateExposure(exposureTypeWa)) {
/*     */       
/* 232 */       rw = RwaMapping.mappingExposureLtvRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */     } else {
/*     */       
/* 235 */       rw = RwaMapping.mappingExposureNrRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */     } 
/*     */     
/* 238 */     RwaMapping.mappingExposureTm(getSchemeConfig(), getJobId(), dataType, exposure, id, creditRuleDo.getDefaultTm());
/*     */   }
/*     */   
/*     */   public void paramMappingCollateral(InterfaceDataType dataType, Map<String, Object> collateral) {
/* 242 */     String id = DataUtils.getString(collateral, (ICodeEnum)RwaParam.COLLATERAL_ID);
/*     */     
/* 244 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/* 245 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.MITIGATION_TYPE, MitigationType.COLLATERAL.getCode());
/*     */     
/* 247 */     BigDecimal collateralAmount = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT);
/* 248 */     if (collateralAmount == null || RwaMath.isZero(collateralAmount)) {
/* 249 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT, BigDecimal.ZERO);
/* 250 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 251 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/* 252 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_COLLATERAL);
/*     */       
/*     */       return;
/*     */     } 
/* 256 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 257 */     if (isApplyWa == Identity.YES) {
/* 258 */       BigDecimal rw = RwaMapping.mappingCollateralRw(getSchemeConfig(), collateral);
/* 259 */       Identity qualFlag = (rw == null) ? Identity.NO : Identity.YES;
/* 260 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlag.getCode());
/*     */     } else {
/* 262 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paramMappingGuarantee(InterfaceDataType dataType, Map<String, Object> guarantee) {
/* 267 */     String id = DataUtils.getString(guarantee, (ICodeEnum)RwaParam.GUARANTEE_ID);
/*     */     
/* 269 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/* 270 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.MITIGATION_TYPE, MitigationType.GUARANTEE.getCode());
/*     */     
/* 272 */     BigDecimal guaranteeAmount = DataUtils.getBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT);
/* 273 */     if (guaranteeAmount == null || RwaMath.isZero(guaranteeAmount)) {
/* 274 */       DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT, BigDecimal.ZERO);
/* 275 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 276 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/* 277 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_GUARANTEE);
/*     */       
/*     */       return;
/*     */     } 
/* 281 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 282 */     if (isApplyWa == Identity.YES) {
/* 283 */       BigDecimal rw = RwaMapping.mappingGuaranteeRw(getSchemeConfig(), guarantee);
/* 284 */       Identity qualFlag = (rw == null) ? Identity.NO : Identity.YES;
/* 285 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlag.getCode());
/*     */     } else {
/* 287 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */     } 
/*     */     
/* 290 */     if (StrUtil.equals(MitigationMainType.CREDIT_DERIVATIVE.getCode(), DataUtils.getString(guarantee, (ICodeEnum)RwaParam.MITIGATION_MAIN_TYPE))) {
/*     */       
/* 292 */       if (!getSchemeConfig().getWaParamVersion().getCreditRule().isCdgEnMiti() && getConfirmApproach() == ExposureApproach.WA) {
/* 293 */         DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */         
/*     */         return;
/*     */       } 
/* 297 */       if (!getSchemeConfig().getIrbParamVersion().getCreditRule().isCdgEnMiti() && getConfirmApproach() != ExposureApproach.WA) {
/* 298 */         DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 303 */       if (StrUtil.equals(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_COVER_DEBT_REST), Identity.NO.getCode())) {
/*     */         
/* 305 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, RwaConfig.getCreditRule(getSchemeConfig(), getConfirmApproach().getCode()).getCdgUdrHaircut());
/*     */       } else {
/* 307 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, BigDecimal.ZERO);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\NonRetailProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */