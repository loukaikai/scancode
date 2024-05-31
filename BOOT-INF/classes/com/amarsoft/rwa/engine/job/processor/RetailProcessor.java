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
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateAssetDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
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
/*     */ public class RetailProcessor extends RwaProcessor {
/*  27 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.processor.RetailProcessor.class);
/*     */   
/*     */   private UnionType unionType;
/*     */   
/*     */   private List<Map<String, Object>> exposureList;
/*     */   private List<Map<String, Object>> collateralList;
/*     */   private List<Map<String, Object>> guaranteeList;
/*     */   private UnionDto unionDto;
/*     */   
/*     */   public RetailProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull UnionType unionType) {
/*  37 */     super(jobInfo, irbUncoveredProcess);
/*  38 */     this.unionType = unionType;
/*     */   }
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  43 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.EXPOSURE.getCode());
/*  44 */     this.collateralList = (List<Map<String, Object>>)item.get(InterfaceDataType.COLLATERAL.getCode());
/*  45 */     this.guaranteeList = (List<Map<String, Object>>)item.get(InterfaceDataType.GUARANTEE.getCode());
/*  46 */     this.unionDto = new UnionDto();
/*  47 */     this.unionDto.setId(DataUtils.getString(item, (ICodeEnum)RwaParam.ID));
/*  48 */     this.unionDto.setSize(DataUtils.getCollSize(new Collection[] { this.exposureList }));
/*  49 */     this.unionDto.setSchemeConfig(getSchemeConfig());
/*  50 */     this.unionDto.setUnionType(this.unionType);
/*  51 */     this.unionDto.setTaskType(getTaskType());
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  57 */     if (getApproach() == Approach.WA) {
/*  58 */       for (Map<String, Object> exposure : this.exposureList) {
/*  59 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, ExposureApproach.WA.getCode());
/*     */       }
/*  61 */       return ExposureApproach.WA;
/*     */     } 
/*  63 */     throw new JobParameterException("非法计算方法：" + getApproach());
/*     */   }
/*     */   
/*     */   public ExposureApproach confirmReExposureApproach() {
/*  67 */     return getConfirmApproach();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/*  73 */     this.unionDto.setApproach(confirmReExposureApproach());
/*     */     
/*  75 */     this.unionDto.setMitigationSize(DataUtils.getCollSize(new Collection[] { this.collateralList, this.guaranteeList }));
/*  76 */     if (this.unionDto.getMitigationSize() > 0) {
/*  77 */       RwaUtils.convert2Relevance((List)item.get(InterfaceDataType.RELEVANCE.getCode()), this.unionDto);
/*     */     } else {
/*  79 */       this.unionDto.setRelevanceSize(0);
/*     */     } 
/*     */     
/*  82 */     Map<String, Map<String, Object>> reeMap = null;
/*  83 */     if (RwaConfig.enableRealEstateLtvCalculate && 
/*  84 */       getSchemeConfig().getWaParamVersion().getCreditRule().isEnRetExpoRwMapping() && 
/*  85 */       !CollUtil.isEmpty(reeMap = RwaUtils.getRealEstateExposureMap(this.exposureList, this.unionDto.getExposureRelevanceMap(), this.unionDto.getMitigationRelevanceMap())))
/*     */     {
/*  87 */       RwaUtils.calculateLtv(reeMap, this.unionDto.getMitigationRelevanceMap(), this.collateralList);
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
/* 103 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 104 */       for (Map<String, Object> collateral : this.collateralList) {
/* 105 */         paramMappingCollateral(InterfaceDataType.COLLATERAL, collateral);
/*     */       }
/*     */     }
/*     */     
/* 109 */     if (!CollUtil.isEmpty(this.guaranteeList)) {
/* 110 */       for (Map<String, Object> guarantee : this.guaranteeList) {
/* 111 */         paramMappingGuarantee(InterfaceDataType.GUARANTEE, guarantee);
/*     */       }
/*     */     }
/*     */     
/* 115 */     Set<MitigateAssetDo> mitigateAssetDoSet = new TreeSet<>();
/* 116 */     for (Map<String, Object> exposure : this.exposureList) {
/* 117 */       paramMappingExposure(InterfaceDataType.EXPOSURE, exposure);
/* 118 */       if (StrUtil.equals(ExposureApproach.WA.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.APPROACH))) {
/* 119 */         MitigateAssetDo mitigateAssetDo = RwaConfig.getMitigateAsset(getSchemeConfig(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.ASSET_TYPE), ExposureApproach.WA.getCode());
/* 120 */         if (mitigateAssetDo != null) {
/* 121 */           mitigateAssetDoSet.add(mitigateAssetDo);
/*     */         }
/*     */       } 
/*     */     } 
/*     */     
/* 126 */     this.unionDto.setMitigateAssetDo(RwaConfig.getMitigateAsset(mitigateAssetDoSet, getSchemeConfig(), ExposureApproach.WA.getCode()));
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
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 138 */     RwaUtils.convert2Exposure(item, this.unionDto, true);
/* 139 */     RwaUtils.convert2Mitigation(item, this.unionDto);
/* 140 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[getConfirmApproach().ordinal()]) {
/*     */       case 1:
/* 142 */         WaCalculation.createCalculation(this.unionDto).execute();
/*     */         break;
/*     */       default:
/* 145 */         throw new JobParameterException("非法计算方法：" + getConfirmApproach());
/*     */     } 
/*     */     
/* 148 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 149 */     for (Map<String, Object> expo : this.exposureList) {
/* 150 */       ExposureDto exposureDto = (ExposureDto)this.unionDto.getExposureMap().get(DataUtils.getString(expo, (ICodeEnum)RwaParam.EXPOSURE_ID));
/* 151 */       if (exposureDto == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 155 */       if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnAmpCalc() && 
/* 156 */         StrUtil.equals(exposureDto.getAmpFlag(), Identity.YES.getCode())) {
/* 157 */         this.unionDto.getAmpResultList().add(exposureDto);
/* 158 */         if (StrUtil.equals(exposureDto.getIsTpCalc(), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */           
/* 162 */           exposureDto.setRw(NumberUtil.mul(exposureDto.getRw(), Double.valueOf(1.2D)));
/* 163 */           exposureDto.setWarw(NumberUtil.mul(exposureDto.getWarw(), Double.valueOf(1.2D)));
/* 164 */           exposureDto.setRwaMb(NumberUtil.mul(exposureDto.getRwaMb(), Double.valueOf(1.2D)));
/* 165 */           exposureDto.setRwaMa(NumberUtil.mul(exposureDto.getRwaMa(), Double.valueOf(1.2D)));
/* 166 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/* 167 */         } else if (!StrUtil.equals(exposureDto.getAmpApproach(), AmpApproach.OTHER.getCode()) && 
/* 168 */           NumberUtil.isGreater(exposureDto.getRwaMa(), RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw))) {
/*     */           
/* 170 */           exposureDto.setRwaAa(RwaMath.sub(RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw), exposureDto.getRwaMa()));
/*     */         } else {
/*     */           
/* 173 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */         } 
/*     */       } else {
/* 176 */         exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */       } 
/* 178 */       exposureDto.setRwaAdj(RwaMath.add(exposureDto.getRwaMa(), exposureDto.getRwaAa()));
/*     */       
/* 180 */       if (taskConfigDo.isEnEcCalc()) {
/* 181 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(expo);
/* 182 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 183 */         exposureDto.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 184 */         exposureDto.setEcDf(df);
/* 185 */         exposureDto.setEc(RwaMath.getEc(exposureDto.getRwaAdj(), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     } 
/* 188 */     return RwaUtils.beanToMap(this.unionDto);
/*     */   }
/*     */   
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure) {
/* 192 */     CreditRuleDo creditRuleDo = getSchemeConfig().getWaParamVersion().getCreditRule();
/* 193 */     String id = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID);
/*     */     
/* 195 */     ExposureBelong exposureBelong = (ExposureBelong)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_BELONG), ExposureBelong.class);
/* 196 */     BigDecimal ccf = null;
/* 197 */     if (exposureBelong == ExposureBelong.OFF) {
/*     */       
/* 199 */       DataUtils.mappingValue(exposure, (ICodeEnum)RwaParam.CCF, (ICodeEnum)RwaParam.CCF_IRB);
/* 200 */       ccf = RwaMapping.mappingExposureCcfWa(getSchemeConfig(), getJobId(), dataType, exposure, id);
/* 201 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CCF, ccf);
/*     */     } else {
/* 203 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CCF, null);
/*     */     } 
/*     */     
/* 206 */     BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*     */     
/* 208 */     if (balance == null) {
/* 209 */       balance = BigDecimal.ZERO;
/* 210 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, BigDecimal.ZERO);
/*     */     } 
/*     */     
/* 213 */     if (creditRuleDo.isEnAmpCalc() && StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.AMP_FLAG))) {
/*     */       
/* 215 */       BigDecimal lr = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR);
/* 216 */       if (RwaMath.isNullOrNegative(lr) || RwaMath.isZero(lr)) {
/* 217 */         lr = BigDecimal.ONE;
/* 218 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR, lr);
/*     */       } 
/* 220 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.NET_ASSET, balance);
/* 221 */       balance = RwaMath.mul(balance, lr);
/* 222 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/* 224 */     BigDecimal provision = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION);
/*     */     
/* 226 */     if (RwaMath.isNullOrNegative(provision)) {
/* 227 */       provision = BigDecimal.ZERO;
/* 228 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION, BigDecimal.ZERO);
/*     */     } 
/* 230 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_PROP, RwaMath.getProvisionProp(balance, ccf, provision));
/* 231 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_DED, RwaMath.getProvisionDed(balance, ccf, provision));
/*     */     
/* 233 */     String exposureTypeWa = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/* 234 */     Identity defaultFlag = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.DEFAULT_FLAG), Identity.class);
/*     */     
/* 236 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_CURRENCY_MISMATCH, 
/* 237 */         RwaUtils.confirmReCurrencyMismatch(DataUtils.getString(exposure, (ICodeEnum)RwaParam.CURRENCY), DataUtils.getString(exposure, (ICodeEnum)RwaParam.INCOME_CURRENCY)));
/*     */     
/* 239 */     if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnRetExpoRwMapping() && RwaUtils.isRealEstateExposure(exposureTypeWa)) {
/*     */       
/* 241 */       RwaMapping.mappingExposureLtvRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/* 242 */     } else if (StrUtil.equals(SpecExposureTypeWa.SME.getCode(), exposureTypeWa)) {
/* 243 */       RwaMapping.mappingExposureNrRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */     } else {
/* 245 */       RwaMapping.mappingExposureReRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paramMappingCollateral(InterfaceDataType dataType, Map<String, Object> collateral) {
/* 250 */     String id = DataUtils.getString(collateral, (ICodeEnum)RwaParam.COLLATERAL_ID);
/*     */     
/* 252 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.APPROACH, confirmReExposureApproach().getCode());
/* 253 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.MITIGATION_TYPE, MitigationType.COLLATERAL.getCode());
/*     */     
/* 255 */     BigDecimal collateralAmount = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT);
/* 256 */     if (collateralAmount == null || RwaMath.isZero(collateralAmount)) {
/* 257 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT, BigDecimal.ZERO);
/* 258 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 259 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_COLLATERAL);
/*     */       
/*     */       return;
/*     */     } 
/* 263 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 264 */     if (isApplyWa == Identity.YES) {
/* 265 */       BigDecimal rw = RwaMapping.mappingCollateralRw(getSchemeConfig(), collateral);
/* 266 */       Identity qualFlag = (rw == null) ? Identity.NO : Identity.YES;
/* 267 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlag.getCode());
/*     */     } else {
/* 269 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */     } 
/*     */   }
/*     */   
/*     */   public void paramMappingGuarantee(InterfaceDataType dataType, Map<String, Object> guarantee) {
/* 274 */     String id = DataUtils.getString(guarantee, (ICodeEnum)RwaParam.GUARANTEE_ID);
/*     */     
/* 276 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.APPROACH, confirmReExposureApproach().getCode());
/* 277 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.MITIGATION_TYPE, MitigationType.GUARANTEE.getCode());
/*     */     
/* 279 */     BigDecimal guaranteeAmount = DataUtils.getBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT);
/* 280 */     if (guaranteeAmount == null || RwaMath.isZero(guaranteeAmount)) {
/* 281 */       DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT, BigDecimal.ZERO);
/* 282 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 283 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_GUARANTEE);
/*     */       
/*     */       return;
/*     */     } 
/* 287 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 288 */     if (isApplyWa == Identity.YES) {
/* 289 */       BigDecimal rw = RwaMapping.mappingGuaranteeRw(getSchemeConfig(), guarantee);
/* 290 */       Identity qualFlag = (rw == null) ? Identity.NO : Identity.YES;
/* 291 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlag.getCode());
/*     */     } else {
/* 293 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */       
/*     */       return;
/*     */     } 
/* 297 */     if (StrUtil.equals(MitigationMainType.CREDIT_DERIVATIVE.getCode(), DataUtils.getString(guarantee, (ICodeEnum)RwaParam.MITIGATION_MAIN_TYPE))) {
/*     */       
/* 299 */       if (!getSchemeConfig().getWaParamVersion().getCreditRule().isCdgEnMiti() && confirmReExposureApproach() == ExposureApproach.WA) {
/* 300 */         DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/* 305 */       if (StrUtil.equals(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_COVER_DEBT_REST), Identity.NO.getCode())) {
/*     */         
/* 307 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, RwaConfig.getCreditRule(getSchemeConfig(), ExposureApproach.WA.getCode()).getCdgUdrHaircut());
/*     */       } else {
/* 309 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, BigDecimal.ZERO);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\RetailProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */