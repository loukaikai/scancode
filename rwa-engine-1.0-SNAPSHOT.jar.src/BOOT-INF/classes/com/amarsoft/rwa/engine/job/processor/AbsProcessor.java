/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ApType;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.NpAssetFlag;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.constant.TrancheLevel;
/*     */ import com.amarsoft.rwa.engine.entity.AbsExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.AbsUnionDto;
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
/*     */ public class AbsProcessor extends RwaProcessor {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.processor.AbsProcessor.class);
/*     */   
/*     */   private Identity isOriginator;
/*     */   
/*     */   private NpAssetFlag npAssetFlag;
/*     */   private ApType apType;
/*     */   private Map<String, Object> product;
/*     */   private List<Map<String, Object>> exposureList;
/*     */   private String absApproachConfig;
/*     */   
/*     */   public AbsProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull Identity isOriginator) {
/*  36 */     super(jobInfo, irbUncoveredProcess);
/*  37 */     this.isOriginator = isOriginator;
/*  38 */     this.absApproachConfig = RwaConfig.getCreditRule(getSchemeConfig(), getApproach().getCode()).getAbsApproach();
/*  39 */     if (StrUtil.isEmpty(this.absApproachConfig)) {
/*  40 */       this.absApproachConfig = ExposureApproach.ABSERA.getCode();
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  46 */     this.product = DataUtils.getFirstItem(item.get(InterfaceDataType.ABS_PRODUCT.getCode()));
/*  47 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.ABS_EXPOSURE.getCode());
/*     */     
/*  49 */     if (this.product == null) {
/*  50 */       this.npAssetFlag = null;
/*     */     } else {
/*     */       try {
/*  53 */         this.npAssetFlag = (NpAssetFlag)EnumUtils.getEnumByCode(DataUtils.getString(this.product, (ICodeEnum)RwaParam.NP_ASSET_FLAG), NpAssetFlag.class);
/*  54 */       } catch (Exception e) {
/*  55 */         this.npAssetFlag = null;
/*  56 */         log.warn("资产证券化产品不良资产标识异常，请检查 product = {}", JsonUtils.object2Json(this.product));
/*     */       } 
/*     */     } 
/*     */     
/*  60 */     if (this.isOriginator == Identity.YES) {
/*     */       
/*  62 */       if (this.product == null) {
/*     */         
/*  64 */         this.apType = ApType.NON;
/*  65 */         DataUtils.setString(this.product, (ICodeEnum)RwaParam.AP_TYPE, this.apType.getCode());
/*     */         return;
/*     */       } 
/*  68 */       BigDecimal apAb = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_AB);
/*  69 */       BigDecimal apWaAb = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WA_AB);
/*     */       
/*  71 */       if (RwaMath.isZeroOrNull(apAb) || StrUtil.equals(DataUtils.getString(this.product, (ICodeEnum)RwaParam.IS_COMP_REQU), Identity.NO.getCode()) || (
/*  72 */         getApproach() == Approach.WA && RwaMath.isZeroOrNull(apWaAb))) {
/*  73 */         this.apType = ApType.NON;
/*  74 */         DataUtils.setString(this.product, (ICodeEnum)RwaParam.AP_TYPE, this.apType.getCode());
/*     */         
/*     */         return;
/*     */       } 
/*  78 */       BigDecimal apAirbAb = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_AIRB_AB);
/*  79 */       BigDecimal apAirbProp = BigDecimal.ZERO;
/*  80 */       if (!RwaMath.isZeroOrNull(apAirbAb)) {
/*  81 */         apAirbProp = RwaMath.div(apAirbAb, apAb);
/*     */       }
/*  83 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_AIRB_PROP, apAirbProp);
/*     */       
/*  85 */       BigDecimal apFirbAb = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_FIRB_AB);
/*  86 */       BigDecimal apFirbProp = BigDecimal.ZERO;
/*  87 */       if (!RwaMath.isZeroOrNull(apFirbAb)) {
/*  88 */         apFirbProp = RwaMath.div(apFirbAb, apAb);
/*     */       }
/*  90 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_FIRB_PROP, apFirbProp);
/*     */       
/*  92 */       BigDecimal apWaProp = BigDecimal.ZERO;
/*  93 */       if (!RwaMath.isZeroOrNull(apWaAb)) {
/*  94 */         apWaProp = RwaMath.div(apWaAb, apAb);
/*     */       }
/*  96 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WA_PROP, apWaProp);
/*     */       
/*  98 */       BigDecimal apWaEad = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WA_EAD);
/*  99 */       if (!RwaMath.isZeroOrNull(apWaEad)) {
/* 100 */         DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WA_ARW, RwaMath.div(DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WA_RWA), apWaEad));
/*     */       }
/*     */ 
/*     */       
/* 104 */       BigDecimal apEad = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_EAD);
/* 105 */       if (!RwaMath.isZeroOrNull(apEad)) {
/* 106 */         BigDecimal bigDecimal1 = RwaMath.add(DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_AIRB_ELA), DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_FIRB_ELA));
/* 107 */         BigDecimal bigDecimal2 = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_RWA);
/* 108 */         DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_WARW, RwaMath.div(RwaMath.add(bigDecimal2, RwaMath.mul(bigDecimal1, RwaMath.kFactor)), apEad));
/*     */       } 
/*     */       
/* 111 */       BigDecimal rwa = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_RWA);
/* 112 */       BigDecimal ela = RwaMath.add(DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_AIRB_ELA), DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_FIRB_ELA));
/* 113 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.PRODUCT_RWA_LIMIT, RwaMath.getAbsProductRwaLimit(rwa, ela));
/*     */ 
/*     */       
/* 116 */       BigDecimal irbProp = RwaMath.add(apAirbProp, apFirbProp);
/* 117 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_IRB_PROP, irbProp);
/* 118 */       BigDecimal prop = NumberUtil.toBigDecimal("0.95");
/* 119 */       if (!RwaUtils.isB3(getTaskType()))
/*     */       {
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
/* 132 */         prop = NumberUtil.toBigDecimal("0.5");
/*     */       }
/* 134 */       if (NumberUtil.isGreaterOrEqual(apAirbProp, prop)) {
/* 135 */         this.apType = ApType.AIRB;
/* 136 */       } else if (NumberUtil.isGreaterOrEqual(irbProp, prop)) {
/* 137 */         this.apType = ApType.FIRB;
/*     */       } else {
/* 139 */         this.apType = ApType.WA;
/*     */       } 
/* 141 */       DataUtils.setString(this.product, (ICodeEnum)RwaParam.AP_TYPE, this.apType.getCode());
/*     */     } else {
/*     */       
/* 144 */       this.apType = ApType.NON;
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/* 150 */     Set<ExposureApproach> approachSet = new HashSet<>();
/* 151 */     for (Map<String, Object> exposure : this.exposureList) {
/* 152 */       approachSet.add(confirmAbsExposureApproach(exposure));
/*     */     }
/*     */     
/* 155 */     return confirmExposureApproach(approachSet);
/*     */   }
/*     */ 
/*     */   
/*     */   public ExposureApproach confirmAbsExposureApproach(Map<String, Object> exposure) throws Exception {
/* 160 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_APPROVED_RATING, confirmIsApprovedRating(exposure).getCode());
/*     */     
/* 162 */     if (this.isOriginator == Identity.NO) {
/*     */       
/* 164 */       if (!RwaUtils.isB3(getTaskType())) {
/* 165 */         if (getApproach() == Approach.WA)
/*     */         {
/* 167 */           return ExposureApproach.ABSSA;
/*     */         }
/* 169 */         if (StrUtil.equals(DataUtils.getString(this.product, (ICodeEnum)RwaParam.IS_IRB_CALC), Identity.YES.getCode()))
/*     */         {
/* 171 */           return ExposureApproach.ABSRBA;
/*     */         }
/*     */         
/* 174 */         return ExposureApproach.ABSSA;
/*     */       } 
/*     */ 
/*     */ 
/*     */       
/* 179 */       return ExposureApproach.ABSERA;
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
/* 200 */     if (this.apType == ApType.NON) {
/*     */       
/* 202 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_APPROVED_RATING, Identity.NO.getCode());
/* 203 */       return ExposureApproach.ABS1250;
/*     */     } 
/*     */     try {
/* 206 */       TrancheLevel trancheLevel = (TrancheLevel)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.TRANCHE_LEVEL), TrancheLevel.class);
/* 207 */     } catch (Exception e) {
/*     */       
/* 209 */       log.warn("资产证券化暴露档次级别异常，请检查 exposure = {}", JsonUtils.object2Json(exposure));
/*     */       
/* 211 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_APPROVED_RATING, Identity.NO.getCode());
/* 212 */       return ExposureApproach.ABS1250;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 217 */     if (!RwaUtils.isB3(getTaskType())) {
/* 218 */       if (getApproach() == Approach.WA || this.apType == ApType.WA || 
/* 219 */         !StrUtil.equals(DataUtils.getString(this.product, (ICodeEnum)RwaParam.IS_IRB_CALC), Identity.YES.getCode()))
/*     */       {
/*     */         
/* 222 */         return ExposureApproach.ABSSA;
/*     */       }
/* 224 */       return ExposureApproach.ABSRBA;
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 231 */     if (this.npAssetFlag == null || StrUtil.equals(this.absApproachConfig, ExposureApproach.ABSERA.getCode())) {
/* 232 */       return ExposureApproach.ABSERA;
/*     */     }
/*     */     
/* 235 */     Identity isApprovedRating = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_APPROVED_RATING), Identity.class);
/*     */     
/* 237 */     if (getApproach() == Approach.WA || this.apType == ApType.WA || 
/* 238 */       !StrUtil.equals(this.absApproachConfig, ExposureApproach.ABSIRB.getCode()) || 
/* 239 */       !StrUtil.equals(DataUtils.getString(this.product, (ICodeEnum)RwaParam.IS_IRB_CALC), Identity.YES.getCode()) || (
/* 240 */       isNpl(this.npAssetFlag) && this.apType == ApType.FIRB)) {
/*     */       
/* 242 */       if (isApprovedRating == Identity.YES) {
/* 243 */         return ExposureApproach.ABSERA;
/*     */       }
/* 245 */       return ExposureApproach.ABSSA;
/*     */     } 
/*     */ 
/*     */     
/* 249 */     return ExposureApproach.ABSIRB;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isNpl(NpAssetFlag assetFlag) {
/* 254 */     if (assetFlag == NpAssetFlag.QUALIFIED_NP || assetFlag == NpAssetFlag.UNQUALIFIED_NP) {
/* 255 */       return true;
/*     */     }
/* 257 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExposureApproach confirmExposureApproach(Set<ExposureApproach> exposureApproachSet) {
/* 262 */     if (exposureApproachSet.size() == 1) {
/* 263 */       return exposureApproachSet.iterator().next();
/*     */     }
/*     */ 
/*     */     
/* 267 */     if (exposureApproachSet.contains(ExposureApproach.ABS1250)) {
/* 268 */       return ExposureApproach.ABS1250;
/*     */     }
/*     */     
/* 271 */     if (exposureApproachSet.contains(ExposureApproach.ABSSA)) {
/* 272 */       return ExposureApproach.ABSSA;
/*     */     }
/*     */     
/* 275 */     if (exposureApproachSet.contains(ExposureApproach.ABSERA)) {
/* 276 */       return ExposureApproach.ABSERA;
/*     */     }
/*     */     
/* 279 */     if (exposureApproachSet.contains(ExposureApproach.ABSRBA)) {
/* 280 */       return ExposureApproach.ABSRBA;
/*     */     }
/*     */     
/* 283 */     return ExposureApproach.ABSIRB;
/*     */   }
/*     */   
/*     */   public Identity confirmIsApprovedRating(Map<String, Object> exposure) {
/* 287 */     String rating = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXTERNAL_RATING);
/* 288 */     if (StrUtil.isEmpty(rating)) {
/* 289 */       log.warn("资产证券化暴露外部评级为空， 请检查 exposure = {}", JsonUtils.object2Json(exposure));
/* 290 */       rating = ExternalRating.LONG_UNRATED.getCode();
/* 291 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXTERNAL_RATING, rating);
/*     */     } 
/*     */ 
/*     */     
/* 295 */     if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_NFS_REFLECT_RATING)) || 
/* 296 */       StrUtil.equals(rating, ExternalRating.LONG_UNRATED.getCode()) || 
/* 297 */       StrUtil.equals(rating, ExternalRating.SHORT_UNRATED.getCode())) {
/* 298 */       return Identity.NO;
/*     */     }
/* 300 */     return Identity.YES;
/*     */   }
/*     */ 
/*     */   
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/* 305 */     if (this.product != null) {
/* 306 */       DataUtils.setString(this.product, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     }
/*     */     
/* 309 */     if (getConfirmApproach() == ExposureApproach.ABSIRB && RwaUtils.isB3(getTaskType())) {
/*     */ 
/*     */ 
/*     */       
/* 313 */       BigDecimal irbEadTotal = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.IRB_EAD_TOTAL);
/* 314 */       BigDecimal apEn = RwaMath.div(NumberUtil.pow(irbEadTotal, 2), DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.IRB_EAD2_TOTAL));
/* 315 */       BigDecimal apLgd = RwaMath.div(DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.IRB_LGD_EAD_TOTAL), irbEadTotal);
/* 316 */       String isDecentralized = Identity.NO.getCode();
/* 317 */       if (apEn != null && NumberUtil.isGreaterOrEqual(apEn, BigDecimal.valueOf(25L))) {
/* 318 */         isDecentralized = Identity.YES.getCode();
/*     */       }
/*     */       
/* 321 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_EN, apEn);
/* 322 */       DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_LGD, apLgd);
/* 323 */       DataUtils.setString(this.product, (ICodeEnum)RwaParam.IS_DECENTRALIZED, isDecentralized);
/*     */       
/* 325 */       calculateAbsKirb(this.product);
/* 326 */     } else if (getConfirmApproach() == ExposureApproach.ABSSA && RwaUtils.isB3(getTaskType())) {
/*     */       
/* 328 */       calculateAbsKa(this.product);
/* 329 */     } else if (getConfirmApproach() == ExposureApproach.ABSRBA && this.product != null) {
/*     */       
/* 331 */       BigDecimal apEn = DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.AP_EN);
/* 332 */       if (this.isOriginator == Identity.YES) {
/*     */         
/* 334 */         apEn = RwaMath.div(NumberUtil.pow(DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.IRB_EAD_TOTAL), 2), DataUtils.getBigDecimal(this.product, (ICodeEnum)RwaParam.IRB_EAD2_TOTAL));
/* 335 */         DataUtils.setBigDecimal(this.product, (ICodeEnum)RwaParam.AP_EN, apEn);
/*     */       } 
/*     */       
/* 338 */       String isDecentralized = Identity.NO.getCode();
/* 339 */       if (apEn != null && NumberUtil.isGreaterOrEqual(apEn, BigDecimal.valueOf(6L))) {
/* 340 */         isDecentralized = Identity.YES.getCode();
/*     */       }
/* 342 */       DataUtils.setString(this.product, (ICodeEnum)RwaParam.IS_DECENTRALIZED, isDecentralized);
/*     */     } 
/*     */     
/* 345 */     for (Map<String, Object> exposure : this.exposureList) {
/* 346 */       paramMappingExposure(InterfaceDataType.ABS_EXPOSURE, exposure, this.product);
/*     */     }
/*     */ 
/*     */     
/* 350 */     List<Map<String, Object>> guaranteeList = (List<Map<String, Object>>)item.get(InterfaceDataType.ABS_GUARANTEE.getCode());
/* 351 */     if (!CollUtil.isEmpty(guaranteeList)) {
/* 352 */       for (Map<String, Object> guarantee : guaranteeList) {
/* 353 */         paramMappingGuarantee(InterfaceDataType.ABS_GUARANTEE, guarantee);
/*     */       }
/*     */     }
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
/*     */   public BigDecimal calculateAbsKirb(Map<String, Object> product) {
/* 367 */     BigDecimal rwa = RwaMath.add(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_AIRB_RWA), DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_FIRB_RWA));
/* 368 */     BigDecimal ela = RwaMath.add(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_AIRB_ELA), DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_FIRB_ELA));
/* 369 */     BigDecimal ead = RwaMath.add(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_AIRB_EAD), DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_FIRB_EAD));
/* 370 */     BigDecimal kirb = RwaMath.getAbsKirb(rwa, ela, ead);
/* 371 */     DataUtils.setBigDecimal(product, (ICodeEnum)RwaParam.AP_KIRB, kirb);
/*     */     
/* 373 */     BigDecimal k = kirb;
/*     */     
/* 375 */     if (!RwaMath.isZeroOrNull(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_AB))) {
/* 376 */       BigDecimal ksa = RwaMath.div(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_ARW), RwaMath.kFactor);
/* 377 */       DataUtils.setBigDecimal(product, (ICodeEnum)RwaParam.AP_KSA, ksa);
/* 378 */       BigDecimal d = RwaMath.div(ead, DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_EAD));
/* 379 */       k = RwaMath.adjustAbsKirb(kirb, ksa, d);
/*     */     } 
/* 381 */     DataUtils.setBigDecimal(product, (ICodeEnum)RwaParam.AP_KA, k);
/* 382 */     return k;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal calculateAbsKa(Map<String, Object> product) {
/* 392 */     BigDecimal ksa = RwaMath.div(DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_ARW), RwaMath.kFactor);
/* 393 */     DataUtils.setBigDecimal(product, (ICodeEnum)RwaParam.AP_KSA, ksa);
/* 394 */     BigDecimal apAb = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_AB);
/* 395 */     BigDecimal apOdeAb = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_ODE_AB);
/* 396 */     BigDecimal apUnkeAb = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_UNKE_AB);
/*     */     
/* 398 */     BigDecimal w = RwaMath.div(apOdeAb, apAb);
/*     */     
/* 400 */     BigDecimal un = RwaMath.div(apUnkeAb, apAb);
/*     */ 
/*     */ 
/*     */     
/* 404 */     BigDecimal ka = RwaMath.getAbsKa(ksa, w);
/* 405 */     if (!RwaMath.isZeroOrNull(un)) {
/*     */       
/* 407 */       if (NumberUtil.isGreater(un, NumberUtil.toBigDecimal("0.05")))
/*     */       {
/*     */ 
/*     */ 
/*     */         
/* 412 */         return null;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 418 */       BigDecimal apEad = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_EAD);
/* 419 */       BigDecimal apOdeEad = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_ODE_EAD);
/* 420 */       BigDecimal apUnkeEad = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_UNKE_EAD);
/* 421 */       ka = RwaMath.adjustAbsKa(ka, apEad, apOdeEad, apUnkeEad);
/*     */     } 
/* 423 */     DataUtils.setBigDecimal(product, (ICodeEnum)RwaParam.AP_KA, ka);
/* 424 */     return ka;
/*     */   }
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure, Map<String, Object> product) {
/*     */     String underAssetType;
/* 428 */     CreditRuleDo creditRuleDo = getSchemeConfig().getWaParamVersion().getCreditRule();
/* 429 */     String id = DataUtils.getString(exposure, (ICodeEnum)RwaParam.ABS_EXPOSURE_ID);
/* 430 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/*     */     
/* 432 */     ExposureBelong exposureBelong = (ExposureBelong)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_BELONG), ExposureBelong.class);
/* 433 */     BigDecimal ccf = null;
/* 434 */     if (exposureBelong == ExposureBelong.OFF) {
/* 435 */       ccf = RwaMapping.mappingAbsCcf(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */     }
/*     */ 
/*     */     
/* 439 */     BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*     */     
/* 441 */     if (balance == null) {
/* 442 */       balance = BigDecimal.ZERO;
/* 443 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, BigDecimal.ZERO);
/*     */     } 
/*     */     
/* 446 */     if (creditRuleDo.isEnAmpCalc() && StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.AMP_FLAG))) {
/*     */       
/* 448 */       BigDecimal lr = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR);
/* 449 */       if (RwaMath.isNullOrNegative(lr) || RwaMath.isZero(lr)) {
/* 450 */         lr = BigDecimal.ONE;
/* 451 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR, lr);
/*     */       } 
/* 453 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.NET_ASSET, balance);
/* 454 */       balance = RwaMath.mul(balance, lr);
/* 455 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/* 457 */     BigDecimal provision = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION);
/*     */     
/* 459 */     if (RwaMath.isNullOrNegative(provision)) {
/* 460 */       provision = BigDecimal.ZERO;
/* 461 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION, BigDecimal.ZERO);
/*     */     } 
/* 463 */     BigDecimal ead = RwaMath.getAbsEad(balance, ccf, provision);
/* 464 */     BigDecimal provisionDed = RwaMath.getAbsProvisionDed(ead, balance, provision);
/* 465 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EAD, ead);
/* 466 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_DED, provisionDed);
/*     */ 
/*     */     
/* 469 */     RwaMapping.mappingExposureTm(getSchemeConfig(), getJobId(), dataType, exposure, id, getSchemeConfig().getWaParamVersion().getCreditRule().getDefaultTm());
/*     */ 
/*     */ 
/*     */     
/* 473 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_ORIGINATOR, (ICodeEnum)this.isOriginator);
/* 474 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, creditRuleDo.getAbsRptItem());
/*     */     
/* 476 */     Identity isApprovedRating = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_APPROVED_RATING), Identity.class);
/*     */     
/* 478 */     if (!RwaUtils.isB3(getTaskType())) {
/* 479 */       paramMappingExposureRwOfB2(dataType, exposure, product, id, isApprovedRating);
/*     */       
/*     */       return;
/*     */     } 
/*     */     
/* 484 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_MATURITY, RwaMath.getTrancheMaturity(DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RESIDUAL_MATURITY)));
/*     */     
/* 486 */     if (getConfirmApproach() == ExposureApproach.ABS1250) {
/* 487 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW_BEFORE, RwaMath.maxRw);
/* 488 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */       
/*     */       return;
/*     */     } 
/* 492 */     BigDecimal d = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_SEPARATION_POINT);
/*     */     
/* 494 */     BigDecimal a = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_STARTING_POINT);
/*     */     
/* 496 */     TrancheLevel trancheLevel = (TrancheLevel)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.TRANCHE_LEVEL), TrancheLevel.class);
/*     */     
/* 498 */     if (getConfirmApproach() == ExposureApproach.ABSERA) {
/* 499 */       if (isApprovedRating == Identity.NO) {
/*     */         
/* 501 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW_BEFORE, RwaMath.maxRw);
/* 502 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */       } else {
/*     */         
/* 505 */         BigDecimal bigDecimal = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.T);
/* 506 */         bigDecimal = confirmThickness(d, a, bigDecimal);
/* 507 */         RwaMapping.mappingAbsRw(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */       } 
/* 509 */       adjustExposureRwByEra(exposure, trancheLevel, product);
/*     */       
/*     */       return;
/*     */     } 
/* 513 */     BigDecimal t = confirmThickness(d, a, null);
/*     */     
/* 515 */     if (t == null) {
/* 516 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW_BEFORE, RwaMath.maxRw);
/* 517 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */       
/*     */       return;
/*     */     } 
/* 521 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.T, t);
/*     */     
/* 523 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[getConfirmApproach().ordinal()]) {
/*     */       
/*     */       case 1:
/* 526 */         underAssetType = DataUtils.getString(product, (ICodeEnum)RwaParam.UNDER_ASSET_TYPE);
/* 527 */         if (StrUtil.isEmpty(underAssetType)) {
/*     */           
/* 529 */           DataUtils.setString(exposure, (ICodeEnum)RwaParam.AP_RETAIL_FLAG, "1");
/*     */         } else {
/* 531 */           DataUtils.setString(exposure, (ICodeEnum)RwaParam.AP_RETAIL_FLAG, underAssetType.substring(0, 1));
/*     */         } 
/*     */         
/* 534 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_DECENTRALIZED, DataUtils.getString(product, (ICodeEnum)RwaParam.IS_DECENTRALIZED));
/*     */         
/* 536 */         RwaMapping.mappingAbsSf(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */         
/* 538 */         calculateRwByAbsirb(exposure, product);
/*     */         break;
/*     */       
/*     */       case 2:
/* 542 */         calculateRwByAbssa(exposure, product);
/*     */         break;
/*     */       default:
/* 545 */         throw new RuntimeException("异常ABS计算方法");
/*     */     } 
/*     */     
/* 548 */     adjustExposureRw(exposure, trancheLevel, product);
/*     */   }
/*     */   
/*     */   public BigDecimal confirmThickness(BigDecimal d, BigDecimal a, BigDecimal t) {
/* 552 */     if (RwaMath.isPositive(t)) {
/* 553 */       return t;
/*     */     }
/* 555 */     if (RwaMath.isNullOrNegative(d) || RwaMath.isNullOrNegative(a) || NumberUtil.isGreaterOrEqual(a, d))
/*     */     {
/* 557 */       return NumberUtil.toBigDecimal("0.01");
/*     */     }
/* 559 */     return RwaMath.getAbsT(d, a);
/*     */   }
/*     */   
/*     */   public void paramMappingExposureRwOfB2(InterfaceDataType dataType, Map<String, Object> exposure, Map<String, Object> product, String id, Identity isApprovedRating) {
/* 563 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[getConfirmApproach().ordinal()]) {
/*     */       case 3:
/* 565 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */         return;
/*     */       
/*     */       case 2:
/* 569 */         if (isApprovedRating == Identity.NO) {
/*     */ 
/*     */           
/* 572 */           if (this.isOriginator == Identity.YES && 
/* 573 */             NumberUtil.equals(DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_SN), BigDecimal.ONE)) {
/*     */             
/* 575 */             DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WARW));
/*     */           
/*     */           }
/* 578 */           else if (this.isOriginator == Identity.YES && StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.OFF_ABS_BIZ_TYPE), OffAbsBizType.LF.getCode()) && 
/* 579 */             StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.QUAL_FACILITY_FLAG), Identity.YES.getCode())) {
/* 580 */             DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_MAX_RW));
/*     */           }
/*     */           else {
/*     */             
/* 584 */             DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */           } 
/*     */         } else {
/* 587 */           RwaMapping.mappingAbsRwSa(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */         } 
/*     */         return;
/*     */       
/*     */       case 4:
/* 592 */         if (isApprovedRating == Identity.NO) {
/*     */           
/* 594 */           if (this.isOriginator == Identity.YES && StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.OFF_ABS_BIZ_TYPE), OffAbsBizType.LF.getCode()) && 
/* 595 */             StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.QUAL_FACILITY_FLAG), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */             
/* 599 */             BigDecimal rw = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_MAX_RW);
/* 600 */             if (rw == null) {
/*     */               
/* 602 */               DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */             } else {
/*     */               
/* 605 */               DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/* 606 */               if (NumberUtil.isLessOrEqual(DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ORIGINAL_MATURITY), BigDecimal.ONE)) {
/*     */                 
/* 608 */                 BigDecimal ccf = NumberUtil.toBigDecimal("0.5");
/* 609 */                 DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CCF, ccf);
/*     */                 
/* 611 */                 BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/* 612 */                 BigDecimal provision = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION);
/* 613 */                 BigDecimal ead = RwaMath.getAbsEad(balance, ccf, provision);
/* 614 */                 BigDecimal provisionDed = RwaMath.getAbsProvisionDed(ead, balance, provision);
/* 615 */                 DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EAD, ead);
/* 616 */                 DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_DED, provisionDed);
/*     */               } 
/*     */             } 
/*     */           } else {
/* 620 */             DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*     */           } 
/*     */         } else {
/*     */           
/* 624 */           if (product == null) {
/* 625 */             DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_DECENTRALIZED, (ICodeEnum)Identity.NO);
/*     */           } else {
/* 627 */             DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_DECENTRALIZED, DataUtils.getString(product, (ICodeEnum)RwaParam.IS_DECENTRALIZED));
/*     */           } 
/* 629 */           RwaMapping.mappingAbsRwRba(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */         } 
/*     */         return;
/*     */     } 
/* 633 */     throw new RuntimeException("异常ABS计算方法");
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal getRwUpperLimit(Map<String, Object> product) {
/* 638 */     if (this.apType == ApType.AIRB || this.apType == ApType.FIRB) {
/* 639 */       return DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WARW);
/*     */     }
/* 641 */     return DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_WA_ARW);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void adjustExposureRwByEra(Map<String, Object> exposure, TrancheLevel trancheLevel, Map<String, Object> product) {
/* 647 */     BigDecimal rwLowerLimit = RwaMapping.getDefaultRwLowerLimit(trancheLevel, DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_COMPLIANCE_STC));
/*     */     
/* 649 */     if (this.npAssetFlag == NpAssetFlag.QUALIFIED_NP || this.npAssetFlag == NpAssetFlag.UNQUALIFIED_NP) {
/* 650 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW), BigDecimal.ONE }));
/* 651 */     } else if (this.isOriginator == Identity.YES && trancheLevel == TrancheLevel.PRIORITY) {
/*     */ 
/*     */       
/* 654 */       BigDecimal rwUpperLimit = getRwUpperLimit(product);
/* 655 */       BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/* 656 */       if (NumberUtil.isLess(rwUpperLimit, DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW))) {
/* 657 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwUpperLimit);
/* 658 */       } else if (NumberUtil.isLessOrEqual(rwUpperLimit, rwLowerLimit)) {
/*     */         
/* 660 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwUpperLimit);
/*     */       } else {
/* 662 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { rw, rwLowerLimit }));
/*     */       } 
/*     */     } else {
/*     */       
/* 666 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW), rwLowerLimit }));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void adjustExposureRw(Map<String, Object> exposure, TrancheLevel trancheLevel, Map<String, Object> product) {
/* 671 */     BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/* 672 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW_BEFORE, rw);
/* 673 */     if (isNpl(this.npAssetFlag)) {
/* 674 */       if (trancheLevel == TrancheLevel.PRIORITY && this.npAssetFlag == NpAssetFlag.QUALIFIED_NP) {
/*     */         
/* 676 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, BigDecimal.ONE);
/*     */       } else {
/*     */         
/* 679 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { BigDecimal.ONE, rw }));
/*     */       } 
/*     */     } else {
/*     */       
/* 683 */       BigDecimal rwLowerLimit = RwaMapping.getDefaultRwLowerLimit(trancheLevel, DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_COMPLIANCE_STC));
/* 684 */       if (trancheLevel == TrancheLevel.PRIORITY) {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 690 */         BigDecimal rwUpperLimit = getRwUpperLimit(product);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 696 */         if (NumberUtil.isLess(rwUpperLimit, rw)) {
/*     */           
/* 698 */           DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwUpperLimit);
/* 699 */         } else if (NumberUtil.isLessOrEqual(rwUpperLimit, rwLowerLimit)) {
/*     */           
/* 701 */           DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwUpperLimit);
/*     */         } else {
/*     */           
/* 704 */           DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { rw, rwLowerLimit }));
/*     */         } 
/*     */       } else {
/*     */         
/* 708 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, NumberUtil.max(new BigDecimal[] { rw, rwLowerLimit }));
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal calculateRwByAbsirb(Map<String, Object> exposure, Map<String, Object> product) {
/* 715 */     BigDecimal kirb = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_KIRB);
/* 716 */     BigDecimal k = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_KA);
/*     */     
/* 718 */     BigDecimal d = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_SEPARATION_POINT);
/* 719 */     BigDecimal a = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_STARTING_POINT);
/*     */     
/* 721 */     if (k == null || NumberUtil.isLessOrEqual(d, k)) {
/* 722 */       BigDecimal bigDecimal = RwaMath.maxRw;
/* 723 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, bigDecimal);
/* 724 */       return bigDecimal;
/*     */     } 
/*     */     
/* 727 */     BigDecimal n = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_EN);
/* 728 */     BigDecimal lgd = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_LGD);
/* 729 */     BigDecimal m = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_MATURITY);
/* 730 */     BigDecimal sfa = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SFA);
/* 731 */     BigDecimal sfb = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SFB);
/* 732 */     BigDecimal sfc = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SFC);
/* 733 */     BigDecimal sfd = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SFD);
/* 734 */     BigDecimal sfe = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SFE);
/* 735 */     BigDecimal sfp = RwaMath.getAbsIrbSfp(kirb, n, lgd, m, DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_COMPLIANCE_STC), sfa, sfb, sfc, sfd, sfe);
/* 736 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.SFP, sfp);
/*     */     
/* 738 */     BigDecimal varu = RwaMath.getAbsU(d, k);
/* 739 */     BigDecimal varl = RwaMath.getAbsL(a, k);
/* 740 */     BigDecimal vara = RwaMath.getAbsA(k, varl, sfp);
/* 741 */     BigDecimal kssfa = RwaMath.getAbsKssfa(vara, varu, varl);
/* 742 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARA, vara);
/* 743 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARU, varu);
/* 744 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARL, varl);
/* 745 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.KSSFA, kssfa);
/*     */     
/* 747 */     BigDecimal rw = RwaMath.getAbsRw(d, a, k, kssfa);
/* 748 */     rw = NumberUtil.min(new BigDecimal[] { rw, RwaMath.maxRw });
/* 749 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/* 750 */     return rw;
/*     */   }
/*     */   
/*     */   public BigDecimal calculateRwByAbssa(Map<String, Object> exposure, Map<String, Object> product) {
/* 754 */     BigDecimal k = DataUtils.getBigDecimal(product, (ICodeEnum)RwaParam.AP_KA);
/*     */     
/* 756 */     BigDecimal d = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_SEPARATION_POINT);
/* 757 */     BigDecimal a = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.TRANCHE_STARTING_POINT);
/*     */     
/* 759 */     if (k == null || NumberUtil.isLessOrEqual(d, k)) {
/* 760 */       BigDecimal bigDecimal = RwaMath.maxRw;
/* 761 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, bigDecimal);
/* 762 */       return bigDecimal;
/*     */     } 
/*     */     
/* 765 */     BigDecimal sfp = RwaMath.getAbsSaSfp(DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_COMPLIANCE_STC));
/* 766 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.SFP, sfp);
/*     */     
/* 768 */     BigDecimal varu = RwaMath.getAbsU(d, k);
/* 769 */     BigDecimal varl = RwaMath.getAbsL(a, k);
/* 770 */     BigDecimal vara = RwaMath.getAbsA(k, varl, sfp);
/* 771 */     BigDecimal kssfa = RwaMath.getAbsKssfa(vara, varu, varl);
/* 772 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARA, vara);
/* 773 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARU, varu);
/* 774 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.VARL, varl);
/* 775 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.KSSFA, kssfa);
/*     */     
/* 777 */     BigDecimal rw = RwaMath.getAbsRw(d, a, k, kssfa);
/* 778 */     rw = NumberUtil.min(new BigDecimal[] { rw, RwaMath.maxRw });
/* 779 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/* 780 */     return rw;
/*     */   }
/*     */   
/*     */   public void paramMappingGuarantee(InterfaceDataType dataType, Map<String, Object> guarantee) {
/* 784 */     String id = DataUtils.getString(guarantee, (ICodeEnum)RwaParam.GUARANTEE_ID);
/*     */     
/* 786 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/* 787 */     DataUtils.setString(guarantee, (ICodeEnum)RwaParam.MITIGATION_TYPE, MitigationType.GUARANTEE.getCode());
/*     */     
/* 789 */     BigDecimal guaranteeAmount = DataUtils.getBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT);
/* 790 */     if (guaranteeAmount == null || RwaMath.isZero(guaranteeAmount)) {
/* 791 */       DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.GUARANTEE_AMOUNT, BigDecimal.ZERO);
/* 792 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/* 793 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/* 794 */       JobUtils.addErrorData(getJobId(), dataType, id, ExcDataCode.AMT_GUARANTEE);
/*     */       
/*     */       return;
/*     */     } 
/* 798 */     Identity isApplyWa = (Identity)EnumUtils.getEnumByCode(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_APPLY_WA), Identity.class);
/* 799 */     if (isApplyWa == Identity.YES) {
/* 800 */       BigDecimal rw = RwaMapping.mappingGuaranteeRw(getSchemeConfig(), guarantee);
/* 801 */       Identity qualFlag = (rw == null) ? Identity.NO : Identity.YES;
/* 802 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, qualFlag.getCode());
/*     */     } else {
/* 804 */       DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */     } 
/*     */     
/* 807 */     if (StrUtil.equals(MitigationMainType.CREDIT_DERIVATIVE.getCode(), DataUtils.getString(guarantee, (ICodeEnum)RwaParam.MITIGATION_MAIN_TYPE))) {
/*     */       
/* 809 */       if (!getSchemeConfig().getWaParamVersion().getCreditRule().isCdgEnMiti()) {
/* 810 */         DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_WA, Identity.NO.getCode());
/*     */       }
/*     */       
/* 813 */       if (!getSchemeConfig().getIrbParamVersion().getCreditRule().isCdgEnMiti()) {
/* 814 */         DataUtils.setString(guarantee, (ICodeEnum)RwaParam.QUAL_FLAG_FIRB, Identity.NO.getCode());
/*     */       }
/*     */ 
/*     */       
/* 818 */       if (StrUtil.equals(DataUtils.getString(guarantee, (ICodeEnum)RwaParam.IS_COVER_DEBT_REST), Identity.NO.getCode())) {
/*     */         
/* 820 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, RwaConfig.getCreditRule(getSchemeConfig(), getConfirmApproach().getCode()).getCdgUdrHaircut());
/*     */       } else {
/* 822 */         DataUtils.setBigDecimal(guarantee, (ICodeEnum)RwaParam.SH, BigDecimal.ZERO);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 830 */     AbsUnionDto unionDto = RwaUtils.initUnion(item, getSchemeConfig(), getConfirmApproach(), this.isOriginator);
/* 831 */     unionDto.setTaskType(getTaskType());
/*     */     
/* 833 */     RwaUtils.convert2AbsExposure(this.exposureList, unionDto);
/*     */     
/* 835 */     RwaUtils.convert2AbsProduct(this.product, unionDto);
/*     */     
/* 837 */     RwaUtils.convert2AbsMitigation(item, unionDto);
/*     */     
/* 839 */     AbsCalculation.createCalculation(unionDto).execute();
/*     */     
/* 841 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 842 */     for (Map<String, Object> expo : this.exposureList) {
/* 843 */       AbsExposureDto exposureDto = (AbsExposureDto)unionDto.getExposureMap().get(DataUtils.getString(expo, (ICodeEnum)RwaParam.ABS_EXPOSURE_ID));
/* 844 */       if (exposureDto == null) {
/*     */         continue;
/*     */       }
/*     */       
/* 848 */       if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnAmpCalc() && 
/* 849 */         StrUtil.equals(exposureDto.getAmpFlag(), Identity.YES.getCode())) {
/* 850 */         unionDto.getAmpResultList().add(exposureDto);
/* 851 */         if (StrUtil.equals(exposureDto.getIsTpCalc(), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */ 
/*     */           
/* 856 */           exposureDto.setRw(NumberUtil.mul(exposureDto.getRw(), Double.valueOf(1.2D)));
/* 857 */           exposureDto.setWarw(NumberUtil.mul(exposureDto.getWarw(), Double.valueOf(1.2D)));
/* 858 */           exposureDto.setRwaMb(NumberUtil.mul(exposureDto.getRwaMb(), Double.valueOf(1.2D)));
/* 859 */           exposureDto.setRwaMa(NumberUtil.mul(exposureDto.getRwaMa(), Double.valueOf(1.2D)));
/* 860 */           exposureDto.setRwaAdj(NumberUtil.mul(exposureDto.getRwaAdj(), Double.valueOf(1.2D)));
/* 861 */           exposureDto.setRwaAmpAa(BigDecimal.ZERO);
/* 862 */         } else if (!StrUtil.equals(exposureDto.getAmpApproach(), AmpApproach.OTHER.getCode()) && 
/* 863 */           NumberUtil.isGreater(exposureDto.getRwaMa(), RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw))) {
/*     */           
/* 865 */           exposureDto.setRwaAmpAa(RwaMath.sub(RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw), exposureDto.getRwaMa()));
/*     */         } else {
/*     */           
/* 868 */           exposureDto.setRwaAmpAa(BigDecimal.ZERO);
/*     */         } 
/*     */       } else {
/* 871 */         exposureDto.setRwaAmpAa(BigDecimal.ZERO);
/*     */       } 
/* 873 */       exposureDto.setRwaAmpAdj(RwaMath.add(exposureDto.getRwaAdj(), exposureDto.getRwaAmpAa()));
/*     */       
/* 875 */       if (taskConfigDo.isEnEcCalc()) {
/* 876 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(expo);
/* 877 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 878 */         exposureDto.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 879 */         exposureDto.setEcDf(df);
/* 880 */         exposureDto.setEc(RwaMath.getEc(exposureDto.getRwaAmpAdj(), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     } 
/*     */     
/* 884 */     return beanToMap(unionDto);
/*     */   }
/*     */   
/*     */   public Map<String, Object> beanToMap(AbsUnionDto unionDto) {
/* 888 */     Map<String, Object> map = new HashMap<>();
/* 889 */     map.put("ID", unionDto.getId());
/* 890 */     map.put("SIZE", Integer.valueOf(unionDto.getSize()));
/* 891 */     map.put("schemeId", unionDto.getSchemeConfig());
/* 892 */     map.put("approach", unionDto.getApproach());
/* 893 */     map.put("mitigationSize", Integer.valueOf(unionDto.getMitigationSize()));
/* 894 */     map.put("relevanceSize", Integer.valueOf(unionDto.getRelevanceSize()));
/* 895 */     map.put(ResultDataType.ABS_EXPOSURE.getCode(), unionDto.getExposureList());
/* 896 */     map.put(ResultDataType.ABS_PRODUCT.getCode(), unionDto.getProductList());
/* 897 */     map.put(ResultDataType.ABS_DETAIL.getCode(), unionDto.getDetailList());
/* 898 */     map.put(ResultDataType.ABS_MITIGATION.getCode(), unionDto.getMitigationList());
/* 899 */     map.put(ResultDataType.AMP.getCode(), unionDto.getAmpResultList());
/* 900 */     return map;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\AbsProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */