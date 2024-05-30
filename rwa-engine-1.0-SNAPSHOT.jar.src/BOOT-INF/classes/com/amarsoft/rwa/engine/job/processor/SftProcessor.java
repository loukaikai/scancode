/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ 
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.SftCollateralDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftNettingDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftUnionDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class SftProcessor extends RwaProcessor {
/*     */   private Identity nettingFlag;
/*     */   private CreditRuleDo waCreditRule;
/*     */   private BookType bookType;
/*     */   private boolean isSpecialApproach;
/*     */   
/*  28 */   public Identity getNettingFlag() { return this.nettingFlag; } private QualCcp qualCcp; private Map<String, Object> netting; private Map<String, Object> exposure; private List<Map<String, Object>> exposureList; private List<Map<String, Object>> collateralList; public void setNettingFlag(Identity nettingFlag) {
/*  29 */     this.nettingFlag = nettingFlag;
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
/*     */   public SftProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull Identity nettingFlag) {
/*  41 */     super(jobInfo, irbUncoveredProcess);
/*  42 */     this.nettingFlag = nettingFlag;
/*  43 */     this.waCreditRule = getSchemeConfig().getWaParamVersion().getCreditRule();
/*     */   }
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  48 */     this.isSpecialApproach = false;
/*  49 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.SFT_EXPOSURE.getCode());
/*  50 */     this.collateralList = (List<Map<String, Object>>)item.get(InterfaceDataType.SFT_COLLATERAL.getCode());
/*     */     
/*  52 */     if (this.nettingFlag == Identity.YES) {
/*  53 */       this.netting = DataUtils.getFirstItem(item.get(InterfaceDataType.SFT_NETTING.getCode()));
/*  54 */       this.exposure = null;
/*  55 */       this.qualCcp = (QualCcp)EnumUtils.getEnumByCode(DataUtils.getString(this.netting, (ICodeEnum)RwaParam.QUAL_CCP_FLAG), QualCcp.class);
/*  56 */       this.bookType = BookType.TRADING;
/*     */     } else {
/*  58 */       this.netting = null;
/*  59 */       this.exposure = DataUtils.getFirstItem(this.exposureList);
/*  60 */       this.qualCcp = (QualCcp)EnumUtils.getEnumByCode(DataUtils.getString(this.exposure, (ICodeEnum)RwaParam.QUAL_CCP_FLAG), QualCcp.class);
/*  61 */       this.bookType = (BookType)EnumUtils.getEnumByCode(DataUtils.getString(this.exposure, (ICodeEnum)RwaParam.BOOK_TYPE), BookType.class);
/*  62 */       if (this.bookType == BookType.BANK) {
/*  63 */         this.isSpecialApproach = true;
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  70 */     if (getApproach() == Approach.WA || this.isSpecialApproach) {
/*  71 */       return ExposureApproach.WA;
/*     */     }
/*  73 */     throw new JobParameterException("非法计算方法：" + getApproach());
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
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/*  88 */     paramMappingExposure(InterfaceDataType.SFT_NETTING, this.netting, DataUtils.getString(this.netting, (ICodeEnum)RwaParam.NETTING_ID));
/*     */     
/*  90 */     for (Map<String, Object> data : this.exposureList) {
/*  91 */       String id = DataUtils.getString(data, (ICodeEnum)RwaParam.EXPOSURE_ID);
/*     */       
/*  93 */       if (this.nettingFlag == Identity.NO) {
/*  94 */         paramMappingExposure(InterfaceDataType.SFT_EXPOSURE, data, id);
/*     */       } else {
/*     */         
/*  97 */         DataUtils.setString(data, this.netting, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA);
/*  98 */         DataUtils.setBigDecimal(data, this.netting, (ICodeEnum)RwaParam.RW);
/*  99 */         DataUtils.setBigDecimal(data, this.netting, (ICodeEnum)RwaParam.TM);
/*     */       } 
/* 101 */       if (!this.isSpecialApproach) {
/* 102 */         paramMappingSft(InterfaceDataType.SFT_EXPOSURE, data, id);
/*     */       }
/*     */     } 
/*     */     
/* 106 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 107 */       for (Map<String, Object> collateral : this.collateralList) {
/* 108 */         paramMappingCollateral(InterfaceDataType.SFT_COLLATERAL, collateral, DataUtils.getString(collateral, (ICodeEnum)RwaParam.COLLATERAL_ID), this.isSpecialApproach);
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 115 */     SftUnionDto unionDto = initUnion(DataUtils.getString(item, (ICodeEnum)RwaParam.ID), getJobInfo().getDataDate(), getSchemeConfig(), getConfirmApproach(), this.exposureList.size(), this.nettingFlag, this.isSpecialApproach);
/*     */     
/* 117 */     if (this.nettingFlag == Identity.YES) {
/* 118 */       convert2Netting(unionDto, this.netting);
/*     */     }
/*     */     
/* 121 */     convert2Exposure(unionDto, this.exposureList);
/*     */     
/* 123 */     convert2Collateral(unionDto, this.collateralList);
/* 124 */     SftCalculation.createCalculation(unionDto).execute();
/*     */     
/* 126 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 127 */     if (taskConfigDo.isEnEcCalc()) {
/* 128 */       if (this.nettingFlag == Identity.YES) {
/* 129 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(this.netting);
/* 130 */         SftNettingDto nettingDto = unionDto.getNettingDto();
/* 131 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 132 */         nettingDto.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 133 */         nettingDto.setEcDf(df);
/*     */         
/* 135 */         nettingDto.setEc(RwaMath.getEc(nettingDto.getRwa(), df, taskConfigDo.getEcOf()));
/*     */       } else {
/* 137 */         SftExposureDto expo = unionDto.getExposureList().get(0);
/* 138 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(this.exposure);
/* 139 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 140 */         expo.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 141 */         expo.setEcDf(df);
/*     */         
/* 143 */         expo.setEc(RwaMath.getEc(RwaMath.add(expo.getRwa(), expo.getCollRwa()), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     }
/* 146 */     return RwaUtils.beanToMap(unionDto);
/*     */   }
/*     */   
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure, String id) {
/* 150 */     if (exposure == null) {
/*     */       return;
/*     */     }
/*     */     
/* 154 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 156 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_PROP, BigDecimal.ZERO);
/*     */     
/* 158 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.CLAIMS_LEVEL, ClaimsLevel.SENIOR.getCode());
/*     */     
/* 160 */     String exposureTypeIrb = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_IRB);
/*     */     
/* 162 */     Identity defaultFlag = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.DEFAULT_FLAG), Identity.class);
/*     */     
/* 164 */     RwaMapping.mappingExposureCpRw(getSchemeConfig(), getJobId(), dataType, exposure, id, defaultFlag);
/*     */ 
/*     */     
/* 167 */     if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.CENTRAL_CLEAR_FLAG)) && 
/* 168 */       StrUtil.equals(TradingRole.CUSTOMER.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.TRADING_ROLE))) {
/*     */       
/* 170 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, this.waCreditRule.getCcpRw());
/* 171 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, this.waCreditRule.getCcpRptItem());
/*     */     } 
/*     */     
/* 174 */     RwaMapping.mappingExposureTm(getSchemeConfig(), getJobId(), dataType, exposure, id, Integer.valueOf(5));
/*     */   }
/*     */ 
/*     */   
/*     */   public void paramMappingSft(InterfaceDataType dataType, Map<String, Object> exposure, String id) {
/* 179 */     RwaMapping.mappingHe(getSchemeConfig(), getJobId(), dataType, exposure, id, this.waCreditRule.getDefaultExpoSh());
/*     */     
/* 181 */     BigDecimal rwExposure = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/* 182 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, null);
/* 183 */     DataUtils.mappingValue(exposure, (ICodeEnum)RwaParam.INSTRUMENTS_TYPE, (ICodeEnum)RwaParam.MITIGATION_SMALL_TYPE);
/* 184 */     BigDecimal rw = RwaMapping.mappingCollateralRw(getSchemeConfig(), exposure);
/* 185 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwExposure);
/* 186 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW_FI, rw);
/*     */     
/* 188 */     CreditRuleDo creditRuleDo = RwaConfig.getCreditRule(getSchemeConfig(), getConfirmApproach().getCode());
/* 189 */     if (creditRuleDo.isEnSftFactorLine()) {
/* 190 */       RwaMapping.mappingFactorLine(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void paramMappingCollateral(InterfaceDataType dataType, Map<String, Object> collateral, String id, boolean isSpecialApproach) {
/* 196 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 198 */     BigDecimal collateralAmount = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT);
/* 199 */     if (collateralAmount == null || RwaMath.isZero(collateralAmount)) {
/* 200 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.COLLATERAL_AMOUNT, BigDecimal.ZERO);
/* 201 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 202 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/* 203 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_COLLATERAL);
/*     */       
/*     */       return;
/*     */     } 
/* 207 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 208 */     Identity qualFlagWa = isApplyWa;
/*     */     
/* 210 */     if (isApplyWa == Identity.YES) {
/* 211 */       BigDecimal rw = RwaMapping.mappingCollateralRw(getSchemeConfig(), collateral);
/* 212 */       if (rw == null) {
/* 213 */         qualFlagWa = Identity.NO;
/*     */       }
/*     */     } 
/* 216 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlagWa.getCode());
/*     */     
/* 218 */     if (isSpecialApproach) {
/*     */       
/* 220 */       if (qualFlagWa == Identity.NO) {
/*     */ 
/*     */         
/* 223 */         RwaMapping.mappingCollateralCpRw(getSchemeConfig(), getJobId(), dataType, collateral, id);
/* 224 */         DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, (ICodeEnum)RwaParam.MITIGATION_RPT_ITEM_WA);
/*     */       } 
/*     */       
/*     */       return;
/*     */     } 
/* 229 */     if (!StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_APPLY_FIRB))) {
/* 230 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */       
/*     */       return;
/*     */     } 
/* 234 */     BigDecimal sh = RwaMapping.mappingHc(getSchemeConfig(), getJobId(), dataType, collateral, id);
/* 235 */     if (sh == null) {
/*     */       
/* 237 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */       return;
/*     */     } 
/* 240 */     DataUtils.setString(collateral, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.YES.getCode());
/*     */     
/* 242 */     CreditRuleDo creditRuleDo = RwaConfig.getCreditRule(getSchemeConfig(), getConfirmApproach().getCode());
/* 243 */     if (creditRuleDo.isEnSftFactorLine()) {
/* 244 */       RwaMapping.mappingFactorLine(getSchemeConfig(), getJobId(), dataType, collateral, id);
/*     */     }
/*     */   }
/*     */   
/*     */   public SftUnionDto initUnion(String id, Date dataDate, SchemeConfigDo schemeConfig, ExposureApproach approach, int size, Identity nettingFlag, boolean isSpecialApproach) {
/* 249 */     SftUnionDto unionDto = new SftUnionDto();
/* 250 */     unionDto.setTaskType(getTaskType());
/* 251 */     unionDto.setId(id);
/* 252 */     unionDto.setDataDate(dataDate);
/* 253 */     unionDto.setSchemeConfig(schemeConfig);
/* 254 */     unionDto.setApproach(approach);
/* 255 */     unionDto.setSize(size);
/* 256 */     unionDto.setNettingFlag(nettingFlag);
/* 257 */     unionDto.setSpecialApproach(isSpecialApproach);
/* 258 */     return unionDto;
/*     */   }
/*     */   
/*     */   public SftUnionDto convert2Netting(SftUnionDto unionDto, Map<String, Object> netting) throws InstantiationException, IllegalAccessException {
/* 262 */     SftNettingDto nettingDto = RwaUtils.convert2SftNetting(netting);
/* 263 */     unionDto.setNettingDto(nettingDto);
/* 264 */     unionDto.setNettingList(CollUtil.toList((Object[])new SftNettingDto[] { nettingDto }));
/* 265 */     return unionDto;
/*     */   }
/*     */   
/*     */   public SftUnionDto convert2Exposure(SftUnionDto unionDto, List<Map<String, Object>> dataList) throws InstantiationException, IllegalAccessException {
/* 269 */     List<SftExposureDto> list = new ArrayList<>();
/* 270 */     for (Map<String, Object> data : dataList) {
/* 271 */       SftExposureDto exposureDto = RwaUtils.convert2SftExposure(data);
/* 272 */       exposureDto.setCollEad(BigDecimal.ZERO);
/* 273 */       exposureDto.setCollRwa(BigDecimal.ZERO);
/* 274 */       list.add(exposureDto);
/*     */     } 
/* 276 */     unionDto.setExposureList(list);
/* 277 */     unionDto.setExposureDto(list.get(0));
/* 278 */     return unionDto;
/*     */   }
/*     */   
/*     */   public SftUnionDto convert2Collateral(SftUnionDto unionDto, List<Map<String, Object>> dataList) throws InstantiationException, IllegalAccessException {
/* 282 */     List<SftCollateralDto> list = new ArrayList<>();
/* 283 */     Map<String, List<SftCollateralDto>> collateralListMap = new HashMap<>();
/* 284 */     if (!CollUtil.isEmpty(this.collateralList)) {
/* 285 */       for (Map<String, Object> data : dataList) {
/* 286 */         SftCollateralDto collateralDto = RwaUtils.convert2SftCollateral(data);
/* 287 */         list.add(collateralDto);
/*     */         
/* 289 */         List<SftCollateralDto> tempList = collateralListMap.get(collateralDto.getExposureId());
/* 290 */         if (tempList == null) {
/* 291 */           tempList = new ArrayList<>();
/* 292 */           collateralListMap.put(collateralDto.getExposureId(), tempList);
/*     */         } 
/* 294 */         tempList.add(collateralDto);
/*     */       } 
/*     */     }
/* 297 */     unionDto.setCollateralList(list);
/* 298 */     unionDto.setCollateralListMap(collateralListMap);
/* 299 */     return unionDto;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\SftProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */