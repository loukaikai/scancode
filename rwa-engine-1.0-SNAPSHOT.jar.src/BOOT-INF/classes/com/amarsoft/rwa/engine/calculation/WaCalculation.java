/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.calculation;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.MitigateSortType;
/*     */ import com.amarsoft.rwa.engine.constant.MitigatedFlag;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationMainType;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationType;
/*     */ import com.amarsoft.rwa.engine.constant.UnmitigatedReason;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigationDetailDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigationDto;
/*     */ import com.amarsoft.rwa.engine.entity.UnionDto;
/*     */ import com.amarsoft.rwa.engine.util.IdWorker;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ 
/*     */ public class WaCalculation {
/*  25 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.calculation.WaCalculation.class);
/*     */   private UnionDto unionDto; private MitigateMethod mitigateMethod; private Map<String, Integer> mitigateLogCountMap; private CreditRuleDo creditRule;
/*     */   
/*  28 */   public UnionDto getUnionDto() { return this.unionDto; } public void setUnionDto(UnionDto unionDto) {
/*  29 */     this.unionDto = unionDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   private WaCalculation(UnionDto unionDto) {
/*  36 */     this.unionDto = unionDto;
/*  37 */     this.mitigateLogCountMap = new HashMap<>(unionDto.getMitigationSize());
/*  38 */     this.creditRule = unionDto.getSchemeConfig().getWaParamVersion().getCreditRule();
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.calculation.WaCalculation createCalculation(UnionDto unionDto) {
/*  42 */     return new com.amarsoft.rwa.engine.calculation.WaCalculation(unionDto);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  47 */     initExposureIsAlone();
/*     */     
/*  49 */     Collections.sort(this.unionDto.getExposureDtoList(), (Comparator<?>)new ExposureComparator(this.unionDto
/*  50 */           .getApproach(), RwaUtils.getMitigateSortList(this.unionDto.getMitigateAssetDo(), MitigateSortType.EXPOSURE)));
/*     */     
/*  52 */     int ne = 1;
/*  53 */     for (ExposureDto exposureDto : this.unionDto.getExposureDtoList()) {
/*  54 */       this.unionDto.getExposureResultList().add(initExposureResult(exposureDto, ne++));
/*     */       
/*  56 */       RwaUtils.initUnifyExposureListMap(this.unionDto.getUnifyExposureListMap(), exposureDto, this.unionDto.getApproach());
/*     */     } 
/*     */     
/*  59 */     if (!CollUtil.isEmpty(this.unionDto.getMitigationDtoList())) {
/*  60 */       Collections.sort(this.unionDto.getMitigationDtoList(), (Comparator<?>)new MitigationComparator(this.unionDto
/*  61 */             .getApproach(), RwaUtils.getMitigateSortList(this.unionDto.getMitigateAssetDo(), MitigateSortType.MITIGATION), null));
/*     */       
/*  63 */       int nm = 1;
/*  64 */       for (MitigationDto mitigationDto : this.unionDto.getMitigationDtoList()) {
/*  65 */         initMitigationResult(mitigationDto, nm++);
/*     */       }
/*     */     } 
/*     */     
/*  69 */     this.mitigateMethod = RwaUtils.getMitigateMethod(this.unionDto.getMitigateAssetDo());
/*  70 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$MitigateMethod[this.mitigateMethod.ordinal()]) {
/*     */       case 1:
/*  72 */         mitigateByProportional();
/*     */         break;
/*     */       case 2:
/*  75 */         mitigateBySort();
/*     */         break;
/*     */       case 3:
/*  78 */         mitigateByMix();
/*     */         break;
/*     */     } 
/*     */   }
/*     */   
/*     */   public void initExposureIsAlone() {
/*  84 */     if (CollUtil.isEmpty(this.unionDto.getMitigationDtoList())) {
/*     */       return;
/*     */     }
/*  87 */     for (MitigationDto mitigationDto : this.unionDto.getMitigationDtoList()) {
/*  88 */       if (!StrUtil.equals(mitigationDto.getQualFlagWa(), Identity.YES.getCode()) || 
/*  89 */         RwaMath.isZero(mitigationDto.getMitigationAmount()) || 
/*  90 */         RwaMath.isNullOrNegative(mitigationDto.getResidualMaturity()) || 
/*  91 */         !StrUtil.equals(mitigationDto.getIsAlone(), Identity.YES.getCode())) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/*  96 */       ExposureDto exposureDto = RwaUtils.getRelExposure(this.unionDto, mitigationDto.getMitigationId());
/*  97 */       if (NumberUtil.isGreater(exposureDto.getRw(), mitigationDto.getRw()) && exposureDto
/*  98 */         .getResidualMaturity() != null && 
/*  99 */         NumberUtil.isGreater(mitigationDto.getResidualMaturity(), exposureDto.getResidualMaturity())) {
/* 100 */         exposureDto.setIsAlone(Identity.YES.getCode());
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   public void mitigateByMix() {
/* 106 */     for (ExposureDto exposureDto : this.unionDto.getExposureDtoList()) {
/*     */       
/* 108 */       if (mitigateBySelf(exposureDto)) {
/* 109 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 113 */       if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/*     */         continue;
/*     */       }
/*     */       
/* 117 */       Map<BigDecimal, MitigationDto> expoMitigationDtoMap = RwaUtils.getRelMitigationMap(this.unionDto.getMitigationMap(), this.unionDto.getExposureRelevanceMap(), exposureDto.getExposureId(), false, null);
/*     */       
/* 119 */       if (CollUtil.isEmpty(expoMitigationDtoMap)) {
/*     */         
/* 121 */         addMitigationDetail(getMitigationDetail(exposureDto));
/* 122 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 126 */       List<ExposureDto> unifyExposureList = RwaUtils.getUnifyExposureList(this.unionDto.getUnifyExposureListMap(), exposureDto, this.unionDto.getApproach());
/*     */       
/* 128 */       if (CollUtil.isEmpty(unifyExposureList)) {
/*     */         
/* 130 */         mitigateBySort(exposureDto, expoMitigationDtoMap);
/*     */         
/* 132 */         calculateExposureIndex(exposureDto);
/*     */         continue;
/*     */       } 
/* 135 */       mitigateByMix(exposureDto, unifyExposureList, expoMitigationDtoMap);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mitigateByMix(ExposureDto exposureDto, List<ExposureDto> unifyExposureList, Map<BigDecimal, MitigationDto> expoMitigationDtoMap) {
/* 142 */     for (BigDecimal sortNo : expoMitigationDtoMap.keySet()) {
/* 143 */       MitigationDto mitigationDto = expoMitigationDtoMap.get(sortNo);
/*     */       
/* 145 */       if (this.unionDto.getDetailResultMap().containsKey(DataUtils.generateKey(new String[] { exposureDto.getExposureId(), mitigationDto.getMitigationId() }))) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 150 */       UnmitigatedReason unmitigatedReason = checkMitigate(exposureDto, mitigationDto);
/* 151 */       if (unmitigatedReason != null) {
/*     */         
/* 153 */         addMitigationDetail(getMitigationDetailByFailed(exposureDto, mitigationDto, unmitigatedReason));
/*     */         continue;
/*     */       } 
/* 156 */       Map<BigDecimal, ExposureDto> mitiUnifyExposureMap = null;
/*     */       
/* 158 */       if (!StrUtil.equals(Identity.YES.getCode(), mitigationDto.getIsAlone()) && (
/* 159 */         mitiUnifyExposureMap = RwaUtils.getMitiUnifyExposureMap(unifyExposureList, RwaUtils.getRelExposureMap(this.unionDto, mitigationDto.getMitigationId()))).size() > 0) {
/*     */         
/* 161 */         mitiUnifyExposureMap.put(exposureDto.getSortNo(), exposureDto);
/*     */         
/* 163 */         mitigateByProportional(mitiUnifyExposureMap, mitigationDto);
/*     */         
/* 165 */         mitigationDto.setCurrentAmount(mitigationDto.getUnmitigatedAmount());
/*     */         
/* 167 */         if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/*     */           return;
/*     */         }
/*     */         continue;
/*     */       } 
/* 172 */       if (mitigate(exposureDto, mitigationDto)) {
/* 173 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         return;
/*     */       } 
/* 177 */       if (RwaConfig.isRwFirstOfMitigationSort(RwaUtils.getMitigateSortList(this.unionDto.getMitigateAssetDo(), MitigateSortType.MITIGATION)) && 
/* 178 */         NumberUtil.isGreaterOrEqual(mitigationDto.getRw(), exposureDto.getRw())) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */ 
/*     */     
/* 184 */     if (!RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 185 */       addMitigationDetail(getMitigationDetail(exposureDto));
/*     */     }
/* 187 */     calculateExposureIndex(exposureDto);
/*     */   }
/*     */   
/*     */   public void mitigateBySort() {
/* 191 */     for (ExposureDto exposureDto : this.unionDto.getExposureDtoList()) {
/*     */       
/* 193 */       if (mitigateBySelf(exposureDto)) {
/* 194 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 198 */       Map<BigDecimal, MitigationDto> expoMitigationDtoMap = RwaUtils.getRelMitigationMap(this.unionDto.getMitigationMap(), this.unionDto.getExposureRelevanceMap(), exposureDto.getExposureId(), false, null);
/*     */       
/* 200 */       if (CollUtil.isEmpty(expoMitigationDtoMap)) {
/*     */         
/* 202 */         addMitigationDetail(getMitigationDetail(exposureDto));
/* 203 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 207 */       mitigateBySort(exposureDto, expoMitigationDtoMap);
/*     */       
/* 209 */       calculateExposureIndex(exposureDto);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void mitigateBySort(ExposureDto exposureDto, Map<BigDecimal, MitigationDto> expoMitigationDtoMap) {
/* 215 */     for (BigDecimal sortNo : expoMitigationDtoMap.keySet()) {
/* 216 */       MitigationDto mitigationDto = expoMitigationDtoMap.get(sortNo);
/*     */       
/* 218 */       if (this.unionDto.getDetailResultMap().containsKey(DataUtils.generateKey(new String[] { exposureDto.getExposureId(), mitigationDto.getMitigationId() }))) {
/*     */         continue;
/*     */       }
/*     */ 
/*     */       
/* 223 */       UnmitigatedReason unmitigatedReason = checkMitigate(exposureDto, mitigationDto);
/* 224 */       if (unmitigatedReason != null) {
/*     */         
/* 226 */         addMitigationDetail(getMitigationDetailByFailed(exposureDto, mitigationDto, unmitigatedReason));
/*     */         
/*     */         continue;
/*     */       } 
/* 230 */       if (mitigate(exposureDto, mitigationDto)) {
/*     */         return;
/*     */       }
/*     */       
/* 234 */       if (RwaConfig.isRwFirstOfMitigationSort(RwaUtils.getMitigateSortList(this.unionDto.getMitigateAssetDo(), MitigateSortType.MITIGATION)) && 
/* 235 */         NumberUtil.isGreaterOrEqual(mitigationDto.getRw(), exposureDto.getRw())) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 240 */     if (!RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 241 */       addMitigationDetail(getMitigationDetail(exposureDto));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public void mitigateByProportional() {
/* 247 */     List<MitigationDto> cdGuaranteeList = new ArrayList<>();
/*     */     
/* 249 */     for (MitigationDto mitigationDto : this.unionDto.getMitigationDtoList()) {
/*     */       
/* 251 */       if (NumberUtil.isGreater(mitigationDto.getCurrentAmount(), BigDecimal.ZERO) && 
/* 252 */         StrUtil.equals(mitigationDto.getQualFlagWa(), Identity.YES.getCode())) {
/*     */ 
/*     */         
/* 255 */         if (RwaUtils.isB3(this.unionDto.getTaskType()) && StrUtil.equals(mitigationDto.getMitigationMainType(), MitigationMainType.CREDIT_DERIVATIVE.getCode()) && 
/* 256 */           NumberUtil.isGreater(mitigationDto.getPayDefaultThreshold(), BigDecimal.ZERO)) {
/*     */           
/* 258 */           cdGuaranteeList.add(mitigationDto);
/*     */           
/*     */           continue;
/*     */         } 
/* 262 */         Map<BigDecimal, ExposureDto> exposureDtoMap = RwaUtils.getRelExposureMap(this.unionDto, mitigationDto.getMitigationId());
/*     */         
/* 264 */         mitigateByProportional(exposureDtoMap, mitigationDto);
/*     */       } 
/*     */     } 
/*     */     
/* 268 */     for (ExposureDto exposureDto : this.unionDto.getExposureDtoList()) {
/*     */       
/* 270 */       if (mitigateBySelf(exposureDto)) {
/* 271 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 275 */       if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/*     */         continue;
/*     */       }
/*     */       
/* 279 */       if (!CollUtil.isEmpty(cdGuaranteeList)) {
/* 280 */         Map<BigDecimal, MitigationDto> expoMitigationDtoMap = RwaUtils.getRelMitigationMap(this.unionDto.getMitigationMap(), this.unionDto.getExposureRelevanceMap(), exposureDto.getExposureId(), false, EnumUtils.getCodes((ICodeEnum[])new MitigationMainType[] { MitigationMainType.CREDIT_DERIVATIVE }));
/* 281 */         for (BigDecimal sortNo : expoMitigationDtoMap.keySet()) {
/* 282 */           MitigationDto mitigationDto = expoMitigationDtoMap.get(sortNo);
/* 283 */           if (!cdGuaranteeList.contains(mitigationDto)) {
/*     */             continue;
/*     */           }
/*     */           
/* 287 */           UnmitigatedReason unmitigatedReason = checkMitigate(exposureDto, mitigationDto);
/* 288 */           if (unmitigatedReason != null) {
/*     */             
/* 290 */             addMitigationDetail(getMitigationDetailByFailed(exposureDto, mitigationDto, unmitigatedReason));
/*     */             
/*     */             continue;
/*     */           } 
/* 294 */           if (mitigate(exposureDto, mitigationDto)) {
/*     */             break;
/*     */           }
/*     */           
/* 298 */           if (RwaConfig.isRwFirstOfMitigationSort(RwaUtils.getMitigateSortList(this.unionDto.getMitigateAssetDo(), MitigateSortType.MITIGATION)) && 
/* 299 */             NumberUtil.isGreaterOrEqual(mitigationDto.getRw(), exposureDto.getRw())) {
/*     */             break;
/*     */           }
/*     */         } 
/*     */       } 
/*     */       
/* 305 */       if (!RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 306 */         addMitigationDetail(getMitigationDetail(exposureDto));
/*     */       }
/* 308 */       calculateExposureIndex(exposureDto);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void mitigateByProportional(Map<BigDecimal, ExposureDto> exposureDtoMap, MitigationDto mitigationDto) {
/* 315 */     List<ExposureDto> exposureDtoList = new ArrayList<>();
/* 316 */     for (BigDecimal sortNo : exposureDtoMap.keySet()) {
/* 317 */       ExposureDto exposureDto = exposureDtoMap.get(sortNo);
/*     */       
/* 319 */       if (mitigateBySelf(exposureDto)) {
/* 320 */         calculateExposureIndex(exposureDto);
/*     */         
/*     */         continue;
/*     */       } 
/* 324 */       if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/*     */         continue;
/*     */       }
/*     */       
/* 328 */       UnmitigatedReason unmitigatedReason = checkMitigate(exposureDto, mitigationDto);
/* 329 */       if (unmitigatedReason != null) {
/*     */         
/* 331 */         addMitigationDetail(getMitigationDetailByFailed(exposureDto, mitigationDto, unmitigatedReason));
/*     */         
/*     */         continue;
/*     */       } 
/* 335 */       exposureDtoList.add(exposureDto);
/* 336 */       mitigationDto.setTotalExposureAmount(RwaMath.add(mitigationDto.getTotalExposureAmount(), exposureDto.getCurrentAmount()));
/*     */     } 
/*     */     
/* 339 */     for (ExposureDto exposureDto : exposureDtoList) {
/*     */       
/* 341 */       BigDecimal prop = RwaMath.div(exposureDto.getCurrentAmount(), mitigationDto.getTotalExposureAmount());
/*     */       
/* 343 */       mitigationDto.setCurrentAmount(RwaMath.mul(prop, mitigationDto.getMitigationAmount()));
/*     */       
/* 345 */       if (mitigate(exposureDto, mitigationDto)) {
/* 346 */         calculateExposureIndex(exposureDto);
/*     */       }
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mitigateBySelf(ExposureDto exposureDto) {
/* 353 */     if (exposureDto.isSelfMitigate()) {
/* 354 */       return false;
/*     */     }
/* 356 */     exposureDto.setSelfMitigate(true);
/*     */     
/* 358 */     if (RwaMath.isNegative(exposureDto.getEad())) {
/* 359 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 360 */       return true;
/*     */     } 
/*     */     
/* 363 */     if (RwaMath.isPositive(exposureDto.getProvisionDed())) {
/* 364 */       addMitigationDetail(getMitigationDetailByProvision(exposureDto));
/*     */       
/* 366 */       if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 367 */         return true;
/*     */       }
/* 369 */     } else if (RwaMath.isZero(exposureDto.getEad())) {
/*     */       
/* 371 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 372 */       return true;
/*     */     } 
/*     */     
/* 375 */     if (RwaMath.isZero(exposureDto.getRw())) {
/*     */       
/* 377 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 378 */       return true;
/*     */     } 
/*     */     
/* 381 */     if (exposureDto.getResidualMaturity() == null) {
/* 382 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 383 */       return true;
/*     */     } 
/*     */     
/* 386 */     if (!this.creditRule.isEnMitiExcExpoMaturity() && RwaMath.isZero(exposureDto.getResidualMaturity())) {
/* 387 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 388 */       return true;
/*     */     } 
/* 390 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmitigatedReason checkMitigate(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 395 */     if (!RwaUtils.isB3(this.unionDto.getTaskType()) || !StrUtil.equals(mitigationDto.getIsFullCoverEm(), Identity.YES.getCode())) {
/*     */       
/* 397 */       if (RwaConfig.isEnRm(this.creditRule, mitigationDto.getMitigationMainType())) {
/*     */         
/* 399 */         if (mitigationDto.getResidualMaturity() == null) {
/* 400 */           return UnmitigatedReason.WA_MM_NULL;
/*     */         }
/*     */         
/* 403 */         if (!RwaConfig.isEnMitiExcRm(this.creditRule, mitigationDto.getMitigationMainType()) && 
/* 404 */           RwaMath.isZero(mitigationDto.getResidualMaturity())) {
/* 405 */           return UnmitigatedReason.WA_MM_ZERO;
/*     */         }
/*     */         
/* 408 */         if (NumberUtil.isLess(mitigationDto.getResidualMaturity(), exposureDto.getResidualMaturity())) {
/* 409 */           return UnmitigatedReason.WA_MM_LESS;
/*     */         }
/*     */       } 
/*     */       
/* 413 */       if (RwaConfig.isEnMmm(this.creditRule, mitigationDto.getMitigationMainType())) {
/*     */         
/* 415 */         if (mitigationDto.getResidualMaturity() == null) {
/* 416 */           return UnmitigatedReason.WA_MM_NULL;
/*     */         }
/*     */         
/* 419 */         if (!RwaConfig.isEnMitiExcRm(this.creditRule, mitigationDto.getMitigationMainType()) && 
/* 420 */           RwaMath.isZero(mitigationDto.getResidualMaturity())) {
/* 421 */           return UnmitigatedReason.WA_MM_ZERO;
/*     */         }
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 427 */         mitigationDto.setHt(RwaMath.getHt(exposureDto.getResidualMaturity(), mitigationDto.getResidualMaturity(), mitigationDto.getOriginalMaturity()));
/* 428 */         if (mitigationDto.getHt().compareTo(BigDecimal.valueOf(-1L)) == 0)
/*     */         {
/* 430 */           return UnmitigatedReason.IRB_MM_OM; } 
/* 431 */         if (mitigationDto.getHt().compareTo(BigDecimal.valueOf(-2L)) == 0)
/*     */         {
/* 433 */           return UnmitigatedReason.IRB_MM_RM; } 
/* 434 */         if (mitigationDto.getHt().compareTo(BigDecimal.ZERO) <= 0)
/*     */         {
/* 436 */           return UnmitigatedReason.IRB_MM_ZERO;
/*     */         }
/*     */       } 
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 443 */     if (this.creditRule.isCdgEnPayThreshold() && 
/* 444 */       StrUtil.equals(mitigationDto.getMitigationMainType(), MitigationMainType.CREDIT_DERIVATIVE.getCode()) && 
/* 445 */       NumberUtil.isGreater(mitigationDto.getPayDefaultThreshold(), BigDecimal.ZERO) && 
/* 446 */       !mitigationDto.isUseMitigateLimitOfCdg())
/*     */     {
/*     */       
/* 449 */       if (isMitigateFailedOfCreditDerivative(exposureDto, mitigationDto)) {
/* 450 */         return UnmitigatedReason.WA_CDG;
/*     */       }
/*     */     }
/*     */     
/* 454 */     BigDecimal rw = mitigationDto.getRw();
/*     */     
/* 456 */     if (this.creditRule.isCollEnRwLine() && 
/* 457 */       StrUtil.equals(mitigationDto.getMitigationType(), MitigationType.COLLATERAL.getCode())) {
/* 458 */       rw = confirmRwLine(exposureDto, mitigationDto);
/*     */     }
/*     */     
/* 461 */     if (NumberUtil.isGreaterOrEqual(rw, exposureDto.getRw())) {
/* 462 */       mitigationDto.setIsExemptRwLine(Identity.NO.getCode());
/* 463 */       mitigationDto.setRwAdjust(rw);
/* 464 */       return UnmitigatedReason.WA_RW;
/*     */     } 
/* 466 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean mitigate(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 471 */     mitigationDto.setIsExemptRwLine(Identity.NO.getCode());
/* 472 */     mitigationDto.setValueFc(BigDecimal.ZERO);
/* 473 */     mitigationDto.setRwAdjust(mitigationDto.getRw());
/* 474 */     MitigationType mitigationType = (MitigationType)EnumUtils.getEnumByCode(mitigationDto.getMitigationType(), MitigationType.class);
/*     */     
/* 476 */     if (this.creditRule.isCollEnRwLine() && mitigationType == MitigationType.COLLATERAL) {
/* 477 */       mitigationDto.setRwAdjust(confirmRwLine(exposureDto, mitigationDto));
/*     */       
/* 479 */       if (NumberUtil.isLess(mitigationDto.getRwAdjust(), this.creditRule.getCollRwLine())) {
/* 480 */         mitigationDto.setIsExemptRwLine(Identity.YES.getCode());
/*     */       }
/*     */     } 
/*     */     
/* 484 */     if (RwaConfig.isEnCmm(this.creditRule, mitigationDto.getMitigationMainType())) {
/* 485 */       mitigationDto.setHfx(getHfx(exposureDto, mitigationDto));
/*     */     }
/*     */     
/* 488 */     if ((!RwaUtils.isB3(this.unionDto.getTaskType()) || !StrUtil.equals(mitigationDto.getIsFullCoverEm(), Identity.YES.getCode())) && 
/* 489 */       RwaConfig.isEnMmm(this.creditRule, mitigationDto.getMitigationMainType())) {
/* 490 */       mitigationDto.setHt(RwaMath.getHt(exposureDto.getResidualMaturity(), mitigationDto.getResidualMaturity(), mitigationDto.getOriginalMaturity()));
/*     */     }
/*     */     
/* 493 */     if (StrUtil.equals(mitigationDto.getMitigationMainType(), MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/*     */       
/* 495 */       if (this.creditRule.isCdgEnPayThreshold() && 
/* 496 */         NumberUtil.isGreater(mitigationDto.getPayDefaultThreshold(), BigDecimal.ZERO) && 
/* 497 */         !mitigationDto.isUseMitigateLimitOfCdg()) {
/*     */         
/* 499 */         mitigationDto.setUseMitigateLimitOfCdg(true);
/* 500 */         addMitigationDetail(getMitigationDetailByCdg(exposureDto, mitigationDto));
/*     */         
/* 502 */         exposureDto.setCurrentAmount(RwaMath.sub(exposureDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/* 503 */         mitigationDto.setCurrentAmount(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/*     */       } 
/*     */       
/* 506 */       mitigationDto.setCurrentMitigated(NumberUtil.min(new BigDecimal[] { exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount() }));
/* 507 */       mitigationDto.setCurrentCovered(RwaMath.getCdgCovered(mitigationDto.getCurrentMitigated(), mitigationDto.getHc(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */     } else {
/*     */       
/* 510 */       if (StrUtil.equals(RwaConfig.waMitigateMode, MitigateMode.AFTER.getCode())) {
/*     */         
/* 512 */         mitigationDto.setCurrentMitigated(NumberUtil.min(new BigDecimal[] { exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount() }));
/*     */       } else {
/*     */         
/* 515 */         mitigationDto.setCurrentMitigated(RwaMath.getMitigateAmount(exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */       } 
/* 517 */       mitigationDto.setCurrentCovered(RwaMath.getMitigateCovered(mitigationDto.getCurrentMitigated(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */     } 
/*     */     
/* 520 */     mitigationDto.setResidualAmount(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getCurrentMitigated()));
/*     */     
/* 522 */     exposureDto.setResidualAmount(RwaMath.sub(exposureDto.getCurrentAmount(), mitigationDto.getCurrentCovered()));
/*     */     
/* 524 */     addMitigationDetail(getMitigationDetail(exposureDto, mitigationDto));
/*     */     
/* 526 */     mitigationDto.setCurrentAmount(mitigationDto.getResidualAmount());
/*     */     
/* 528 */     exposureDto.setCurrentAmount(exposureDto.getResidualAmount());
/* 529 */     return RwaMath.isZero(exposureDto.getCurrentAmount());
/*     */   }
/*     */   
/*     */   public BigDecimal getHfx(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 533 */     if (RwaMath.isCurrencyMismatch(exposureDto.getCurrency(), mitigationDto.getCurrency())) {
/*     */       
/* 535 */       if (RwaConfig.isEnHfxAdj(this.creditRule, mitigationDto.getMitigationMainType())) {
/*     */         
/* 537 */         Integer frequency = mitigationDto.getRevaFrequency();
/* 538 */         if (StrUtil.equals(mitigationDto.getMitigationType(), MitigationType.GUARANTEE.getCode())) {
/* 539 */           frequency = exposureDto.getRevaFrequency();
/*     */         }
/* 541 */         return RwaMath.adjustHaircut(this.creditRule.getShfx(), frequency, exposureDto.getTm());
/*     */       } 
/* 543 */       return this.creditRule.getShfx();
/*     */     } 
/*     */     
/* 546 */     return BigDecimal.ZERO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMitigateFailedOfCreditDerivative(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 557 */     if (NumberUtil.isGreaterOrEqual(mitigationDto.getPayDefaultThreshold(), exposureDto.getCurrentAmount())) {
/* 558 */       return true;
/*     */     }
/*     */     
/* 561 */     BigDecimal l = mitigationDto.getPayDefaultThreshold();
/*     */     
/* 563 */     BigDecimal rwa = RwaMath.mul(l, RwaMath.maxRw);
/*     */     
/* 565 */     BigDecimal ra = RwaMath.sub(exposureDto.getCurrentAmount(), l);
/*     */     
/* 567 */     BigDecimal rm = RwaMath.sub(mitigationDto.getCurrentAmount(), l);
/*     */     
/* 569 */     if (this.creditRule.isGuarEnCmm()) {
/* 570 */       mitigationDto.setHfx(getHfx(exposureDto, mitigationDto));
/*     */     }
/*     */     
/* 573 */     BigDecimal ma = NumberUtil.min(new BigDecimal[] { ra, rm });
/*     */     
/* 575 */     BigDecimal mca = RwaMath.getCdgCovered(ma, mitigationDto.getHc(), mitigationDto.getHfx(), mitigationDto.getHt());
/*     */     
/* 577 */     rwa = RwaMath.add(rwa, RwaMath.mul(mca, mitigationDto.getRw()));
/*     */     
/* 579 */     if (NumberUtil.isGreaterOrEqual(rwa, RwaMath.mul(RwaMath.add(l, mca), exposureDto.getRw()))) {
/* 580 */       return true;
/*     */     }
/* 582 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public ExposureDto initExposureResult(ExposureDto exposureDto, int i) {
/* 587 */     exposureDto.setSortNo(BigDecimal.valueOf(i));
/*     */     
/* 589 */     if (StrUtil.equals(exposureDto.getSprvTranType(), SprvTranType.REPO.getCode())) {
/* 590 */       exposureDto.setTsMaturity(RwaConfig.getWorkDays(exposureDto.getStartDate(), exposureDto.getDueDate()));
/*     */     }
/*     */     
/* 593 */     exposureDto.setEad(RwaMath.getEadExistsNeg(exposureDto.getAssetBalance(), exposureDto.getCcf(), exposureDto.getProvisionDed()));
/*     */     
/* 595 */     if (RwaMath.isZero(exposureDto.getAssetBalance())) {
/* 596 */       exposureDto.setCurrentAmount(exposureDto.getAssetBalance());
/* 597 */     } else if (StrUtil.equals(ExposureBelong.OFF.getCode(), exposureDto.getExposureBelong())) {
/* 598 */       if (RwaMath.isRatioZero(exposureDto.getCcf())) {
/* 599 */         exposureDto.setCurrentAmount(BigDecimal.ZERO);
/*     */       } else {
/* 601 */         exposureDto.setCurrentAmount(RwaMath.div(exposureDto.getEad(), exposureDto.getCcf()));
/*     */       } 
/*     */     } else {
/* 604 */       exposureDto.setCurrentAmount(exposureDto.getEad());
/*     */     } 
/*     */     
/* 607 */     exposureDto.setRwaMb(RwaMath.mul(exposureDto.getEad(), exposureDto.getRw()));
/*     */     
/* 609 */     exposureDto.setCoveredEa(BigDecimal.ZERO);
/* 610 */     exposureDto.setUncoveredEa(BigDecimal.ZERO);
/* 611 */     exposureDto.setRwaMa(BigDecimal.ZERO);
/* 612 */     exposureDto.setRwaUm(BigDecimal.ZERO);
/* 613 */     return exposureDto;
/*     */   }
/*     */ 
/*     */   
/*     */   public MitigationDto initMitigationResult(MitigationDto mitigationDto, int i) {
/* 618 */     mitigationDto.setSortNo(BigDecimal.valueOf(i));
/*     */     
/* 620 */     if (RwaMath.isNullOrNegative(mitigationDto.getPayDefaultThreshold())) {
/* 621 */       mitigationDto.setPayDefaultThreshold(BigDecimal.ZERO);
/*     */     }
/*     */     
/* 624 */     mitigationDto.setUnmitigatedAmount(mitigationDto.getMitigationAmount());
/* 625 */     mitigationDto.setMitigatedAmount(BigDecimal.ZERO);
/* 626 */     mitigationDto.setCoveredEa(BigDecimal.ZERO);
/* 627 */     mitigationDto.setMitigatedEffect(BigDecimal.ZERO);
/* 628 */     return mitigationDto;
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal confirmRwLine(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 633 */     if (!StrUtil.equals(mitigationDto.getMitigationType(), MitigationType.COLLATERAL.getCode())) {
/* 634 */       return mitigationDto.getRw();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 640 */     if (!RwaUtils.isH0AssetB3ByFcType(mitigationDto.getRwValueFcType())) {
/* 641 */       return mitigationDto.getRw();
/*     */     }
/*     */     
/* 644 */     if (RwaMath.isCurrencyMismatch(exposureDto.getCurrency(), mitigationDto.getCurrency())) {
/* 645 */       return this.creditRule.getCollRwLine();
/*     */     }
/*     */     
/* 648 */     if (StrUtil.equals(exposureDto.getSprvTranType(), SprvTranType.REPO.getCode())) {
/*     */ 
/*     */       
/* 651 */       if (StrUtil.equals(RwaConfig.rwLineAdjustMode, RwLineAdjustMode.EXCLUSION.getCode()))
/*     */       {
/* 653 */         return confirmRwLineByRepo(exposureDto, mitigationDto);
/*     */       }
/*     */       
/* 656 */       BigDecimal rwAdj = confirmRwLineByCommon(exposureDto, mitigationDto);
/* 657 */       if (NumberUtil.isLess(rwAdj, this.creditRule.getCollRwLine()))
/*     */       {
/* 659 */         return rwAdj;
/*     */       }
/*     */       
/* 662 */       return confirmRwLineByRepo(exposureDto, mitigationDto);
/*     */     } 
/*     */ 
/*     */     
/* 666 */     return confirmRwLineByCommon(exposureDto, mitigationDto);
/*     */   }
/*     */   
/*     */   public BigDecimal confirmRwLineByCommon(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 670 */     if (mitigationDto.getIsOverExposure() == null) {
/*     */ 
/*     */       
/* 673 */       if (RwaMath.isPositive(NumberUtil.toBigDecimal(mitigationDto.getRwValueFcType())))
/*     */       {
/*     */         
/* 676 */         if (!isOverExposure(mitigationDto)) {
/* 677 */           return this.creditRule.getCollRwLine();
/*     */         }
/*     */       }
/* 680 */     } else if (!mitigationDto.getIsOverExposure().booleanValue()) {
/*     */       
/* 682 */       return this.creditRule.getCollRwLine();
/*     */     } 
/*     */     
/* 685 */     return mitigationDto.getRw();
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal confirmRwLineByRepo(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 690 */     if (!RwaUtils.isH0Transaction(exposureDto.getTsMaturity(), exposureDto.getRevaFrequency().intValue(), mitigationDto.getRevaFrequency().intValue())) {
/* 691 */       return this.creditRule.getCollRwLine();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 698 */     if (!StrUtil.equals(exposureDto.getCoreMarketPartyFlag(), Identity.YES.getCode())) {
/* 699 */       return this.creditRule.getCollRepoMidRw();
/*     */     }
/* 701 */     return mitigationDto.getRw();
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isOverExposure(MitigationDto mitigationDto) {
/* 706 */     Map<BigDecimal, ExposureDto> exposureDtoMap = RwaUtils.getRelExposureMap(this.unionDto, mitigationDto.getMitigationId());
/* 707 */     BigDecimal balance = BigDecimal.ZERO;
/* 708 */     for (ExposureDto exposureDto : exposureDtoMap.values()) {
/* 709 */       balance = RwaMath.add(balance, exposureDto.getAssetBalance());
/*     */     }
/* 711 */     mitigationDto.setIsOverExposure(Boolean.valueOf(NumberUtil.isGreaterOrEqual(mitigationDto.getMitigationAmount(), RwaMath.mul(balance, Double.valueOf(1.25D)))));
/* 712 */     return mitigationDto.getIsOverExposure().booleanValue();
/*     */   }
/*     */   
/*     */   public void addMitigationDetail(MitigationDetailDto detailDto) {
/* 716 */     if (detailDto == null) {
/*     */       return;
/*     */     }
/* 719 */     this.unionDto.getDetailResultList().add(detailDto);
/* 720 */     if (!StrUtil.isEmpty(detailDto.getMitigationId())) {
/* 721 */       this.unionDto.getDetailResultMap().put(DataUtils.generateKey(new String[] { detailDto.getExposureId(), detailDto.getMitigationId() }, ), detailDto);
/*     */     }
/* 723 */     RwaUtils.aggregate2ExposureResult(detailDto, (ExposureDto)this.unionDto
/* 724 */         .getExposureMap().get(detailDto.getExposureId()), (MitigationDto)this.unionDto
/* 725 */         .getMitigationMap().get(detailDto.getMitigationId()));
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
/*     */   public MitigationDetailDto getMitigationDetailByFailed(ExposureDto exposureDto, MitigationDto mitigationDto, UnmitigatedReason reason) {
/* 737 */     if (RwaUtils.isLargeOfMitigateLogCount(mitigationDto.getMitigationId(), reason.getCode(), this.unionDto.getDetailResultList().size(), this.mitigateLogCountMap)) {
/* 738 */       return null;
/*     */     }
/* 740 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 741 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 742 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 743 */     detailDto.setIsResult(Identity.NO.getCode());
/* 744 */     detailDto.setMitigatedFlag(MitigatedFlag.NO.getCode());
/* 745 */     detailDto.setFmReason(reason.getCode());
/* 746 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 747 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 748 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 749 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 750 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 751 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 752 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 753 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 754 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 755 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 756 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 757 */     detailDto.setMitigationUseAmount(BigDecimal.ZERO);
/* 758 */     detailDto.setMitigationResiValue(mitigationDto.getCurrentAmount());
/* 759 */     detailDto.setMitigationShc(mitigationDto.getShc());
/* 760 */     detailDto.setMitigationHc(mitigationDto.getHc());
/* 761 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 762 */     detailDto.setHfx(mitigationDto.getHfx());
/* 763 */     detailDto.setHt(mitigationDto.getHt());
/* 764 */     detailDto.setIsExemptRwLine(mitigationDto.getIsExemptRwLine());
/* 765 */     detailDto.setValueFc(mitigationDto.getValueFc());
/* 766 */     detailDto.setCoveredEa(BigDecimal.ZERO);
/* 767 */     detailDto.setMitigatedEa(BigDecimal.ZERO);
/* 768 */     detailDto.setMitigatedRw(mitigationDto.getRwAdjust());
/* 769 */     detailDto.setRwaMb(BigDecimal.ZERO);
/* 770 */     detailDto.setRwaMa(BigDecimal.ZERO);
/* 771 */     return detailDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetailByProvision(ExposureDto exposureDto) {
/* 780 */     if (exposureDto.isProvisionDedFlag()) {
/* 781 */       return null;
/*     */     }
/* 783 */     exposureDto.setProvisionDedFlag(true);
/* 784 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 785 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 786 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 787 */     detailDto.setIsResult(Identity.YES.getCode());
/* 788 */     detailDto.setIsAlone(null);
/*     */     
/* 790 */     detailDto.setMitigatedFlag(MitigatedFlag.PROVISION.getCode());
/*     */     
/* 792 */     detailDto.setUncoveredEa(exposureDto.getAssetBalance());
/* 793 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/*     */     
/* 795 */     if (exposureDto.getCcf() == null) {
/* 796 */       detailDto.setCoveredEa(exposureDto.getProvisionDed());
/*     */     } else {
/* 798 */       detailDto.setCoveredEa(RwaMath.div(exposureDto.getProvisionDed(), exposureDto.getCcf()));
/*     */     } 
/* 800 */     detailDto.setMitigatedEa(BigDecimal.ZERO);
/* 801 */     detailDto.setMitigatedRw(exposureDto.getRw());
/* 802 */     detailDto.setRwaMb(RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 803 */     detailDto.setRwaMa(detailDto.getRwaMb());
/* 804 */     return detailDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetail(ExposureDto exposureDto) {
/* 813 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 814 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 815 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 816 */     detailDto.setIsResult(Identity.YES.getCode());
/* 817 */     detailDto.setMitigatedFlag(MitigatedFlag.NO.getCode());
/* 818 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 819 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/* 820 */     detailDto.setIsAlone(null);
/*     */ 
/*     */     
/* 823 */     if (StrUtil.equals(exposureDto.getExposureBelong(), ExposureBelong.OFF.getCode()) && 
/* 824 */       RwaMath.isZero(exposureDto.getCcf())) {
/* 825 */       detailDto.setCoveredEa(exposureDto.getAssetBalance());
/*     */     } else {
/* 827 */       detailDto.setCoveredEa(exposureDto.getCurrentAmount());
/*     */     } 
/* 829 */     detailDto.setMitigatedEa(RwaMath.getEadExistsNeg(detailDto.getCoveredEa(), detailDto.getCcf(), null));
/* 830 */     detailDto.setMitigatedRw(detailDto.getRw());
/* 831 */     detailDto.setRwaMb(RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 832 */     detailDto.setRwaMa(detailDto.getRwaMb());
/* 833 */     return detailDto;
/*     */   }
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetailByCdg(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 838 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 839 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 840 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 841 */     detailDto.setIsResult(Identity.YES.getCode());
/* 842 */     detailDto.setMitigatedFlag(MitigatedFlag.MITIGATED.getCode());
/* 843 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 844 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 845 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 846 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 847 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 848 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 849 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 850 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 851 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 852 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 853 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 854 */     detailDto.setMitigationUseAmount(mitigationDto.getPayDefaultThreshold());
/* 855 */     detailDto.setMitigationResiValue(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/* 856 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 857 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/* 858 */     detailDto.setCoveredEa(mitigationDto.getPayDefaultThreshold());
/* 859 */     detailDto.setMitigatedEa(RwaMath.getEad(detailDto.getCoveredEa(), detailDto.getCcf()));
/* 860 */     detailDto.setMitigatedRw(RwaMath.maxRw);
/* 861 */     detailDto.setRwaMb(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getRw()));
/* 862 */     detailDto.setRwaMa(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 863 */     return detailDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetail(ExposureDto exposureDto, MitigationDto mitigationDto) {
/* 873 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 874 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 875 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 876 */     detailDto.setIsResult(Identity.YES.getCode());
/* 877 */     detailDto.setMitigatedFlag(MitigatedFlag.MITIGATED.getCode());
/* 878 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 879 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 880 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 881 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 882 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 883 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 884 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 885 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 886 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 887 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 888 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 889 */     detailDto.setMitigationUseAmount(mitigationDto.getCurrentMitigated());
/* 890 */     detailDto.setMitigationResiValue(mitigationDto.getResidualAmount());
/* 891 */     detailDto.setMitigationShc(mitigationDto.getShc());
/* 892 */     detailDto.setMitigationHc(mitigationDto.getHc());
/* 893 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 894 */     detailDto.setHfx(mitigationDto.getHfx());
/* 895 */     detailDto.setHt(mitigationDto.getHt());
/* 896 */     detailDto.setIsExemptRwLine(mitigationDto.getIsExemptRwLine());
/* 897 */     detailDto.setValueFc(mitigationDto.getValueFc());
/* 898 */     detailDto.setCoveredEa(mitigationDto.getCurrentCovered());
/* 899 */     detailDto.setMitigatedEa(RwaMath.getEad(detailDto.getCoveredEa(), detailDto.getCcf()));
/* 900 */     detailDto.setMitigatedRw(mitigationDto.getRwAdjust());
/* 901 */     detailDto.setRwaMb(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getRw()));
/* 902 */     detailDto.setRwaMa(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 903 */     return detailDto;
/*     */   }
/*     */   
/*     */   public void calculateExposureIndex(ExposureDto exposureDto) {
/* 907 */     exposureDto.setCurrentAmount(BigDecimal.ZERO);
/* 908 */     exposureDto.setWarw(RwaMath.getWarw(exposureDto.getEad(), exposureDto.getRwaMb(), exposureDto.getRwaMa(), exposureDto.getRw()));
/* 909 */     if (NumberUtil.isGreater(exposureDto.getWarw(), exposureDto.getRw())) {
/* 910 */       log.warn("暴露平均waRW数据异常， exposure={}", exposureDto);
/* 911 */       exposureDto.setWarw(exposureDto.getRw());
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\calculation\WaCalculation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */