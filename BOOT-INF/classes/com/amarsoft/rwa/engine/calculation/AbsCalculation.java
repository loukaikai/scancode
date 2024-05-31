/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.calculation;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureBelong;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.MitigatedFlag;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationMainType;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationType;
/*     */ import com.amarsoft.rwa.engine.constant.UnmitigatedReason;
/*     */ import com.amarsoft.rwa.engine.entity.AbsExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.AbsProductDto;
/*     */ import com.amarsoft.rwa.engine.entity.AbsUnionDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigationDetailDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigationDto;
/*     */ import com.amarsoft.rwa.engine.entity.compare.AbsExposureComparator;
/*     */ import com.amarsoft.rwa.engine.util.IdWorker;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Collections;
/*     */ import java.util.Comparator;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.springframework.beans.BeanUtils;
/*     */ 
/*     */ public class AbsCalculation {
/*  30 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.calculation.AbsCalculation.class);
/*     */   private AbsUnionDto unionDto; private CreditRuleDo creditRule;
/*     */   
/*  33 */   public AbsUnionDto getUnionDto() { return this.unionDto; } public void setUnionDto(AbsUnionDto unionDto) {
/*  34 */     this.unionDto = unionDto;
/*     */   }
/*     */ 
/*     */   
/*     */   private AbsCalculation(AbsUnionDto unionDto) {
/*  39 */     this.unionDto = unionDto;
/*  40 */     this.creditRule = RwaConfig.getCreditRule(unionDto.getSchemeConfig(), unionDto.getApproach().getCode());
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.calculation.AbsCalculation createCalculation(AbsUnionDto unionDto) {
/*  44 */     return new com.amarsoft.rwa.engine.calculation.AbsCalculation(unionDto);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  49 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[this.unionDto.getApproach().ordinal()]) {
/*     */       
/*     */       case 1:
/*  52 */         directCalculationResult();
/*     */         return;
/*     */ 
/*     */       
/*     */       case 2:
/*  57 */         directCalculationResult();
/*     */         return;
/*     */ 
/*     */       
/*     */       case 3:
/*  62 */         directCalculationResult();
/*     */         
/*  64 */         if (this.unionDto.getIsOriginator() == Identity.NO) {
/*     */           return;
/*     */         }
/*     */         break;
/*     */ 
/*     */ 
/*     */       
/*     */       case 4:
/*     */       case 5:
/*  73 */         mitigate();
/*     */         break;
/*     */       
/*     */       default:
/*  77 */         throw new RuntimeException("异常计算方法");
/*     */     } 
/*     */ 
/*     */     
/*  81 */     adjustProductLimit();
/*     */   }
/*     */   
/*     */   public void directCalculationResult() {
/*  85 */     BigDecimal ab = BigDecimal.ZERO;
/*  86 */     BigDecimal ead = BigDecimal.ZERO;
/*  87 */     BigDecimal rwa = BigDecimal.ZERO;
/*  88 */     for (AbsExposureDto exposureDto : this.unionDto.getExposureList()) {
/*     */       
/*  90 */       exposureDto.setCoveredEa(BigDecimal.ZERO);
/*  91 */       exposureDto.setUncoveredEa(exposureDto.getCurrentAmount());
/*  92 */       exposureDto.setWarw(exposureDto.getRw());
/*  93 */       exposureDto.setRwaMb(RwaMath.mul(exposureDto.getEad(), exposureDto.getRw()));
/*  94 */       exposureDto.setRwaMa(exposureDto.getRwaMb());
/*  95 */       exposureDto.setRwaAa(BigDecimal.ZERO);
/*  96 */       exposureDto.setRwaAdj(exposureDto.getRwaMb());
/*     */       
/*  98 */       ab = RwaMath.add(ab, exposureDto.getAssetBalance());
/*  99 */       ead = RwaMath.add(ead, exposureDto.getEad());
/* 100 */       rwa = RwaMath.add(rwa, exposureDto.getRwaMb());
/*     */       
/* 102 */       if (NumberUtil.isGreater(exposureDto.getProvisionDed(), BigDecimal.ZERO)) {
/* 103 */         this.unionDto.getDetailList().add(getMitigationDetailByProvision(exposureDto));
/*     */       }
/*     */       
/* 106 */       if (NumberUtil.isGreater(exposureDto.getCurrentAmount(), BigDecimal.ZERO)) {
/* 107 */         this.unionDto.getDetailList().add(getMitigationDetail(exposureDto));
/*     */       }
/*     */     } 
/* 110 */     if (this.unionDto.getProduct() != null) {
/* 111 */       this.unionDto.getProduct().setProductAb(ab);
/* 112 */       this.unionDto.getProduct().setProductEad(ead);
/* 113 */       this.unionDto.getProduct().setProductRwaMb(rwa);
/* 114 */       this.unionDto.getProduct().setProductRwaMa(rwa);
/* 115 */       this.unionDto.getProduct().setProductRwaAa(BigDecimal.ZERO);
/* 116 */       this.unionDto.getProduct().setRwaAdj(rwa);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void mitigate() {
/* 125 */     Collections.sort(this.unionDto.getExposureList(), (Comparator<?>)new AbsExposureComparator());
/*     */     
/* 127 */     if (!CollUtil.isEmpty(this.unionDto.getMitigationList())) {
/* 128 */       Collections.sort(this.unionDto.getMitigationList(), (Comparator<?>)new MitigationComparator(ExposureApproach.WA, null, null));
/*     */ 
/*     */       
/* 131 */       int nm = 1;
/* 132 */       for (MitigationDto mitigationDto : this.unionDto.getMitigationList()) {
/* 133 */         mitigationDto.setSortNo(BigDecimal.valueOf(nm++));
/*     */       }
/*     */     } 
/*     */     
/* 137 */     int en = 1;
/* 138 */     for (AbsExposureDto exposureDto : this.unionDto.getExposureList()) {
/*     */       
/* 140 */       initExposure(exposureDto, en++);
/*     */       
/* 142 */       Map<BigDecimal, MitigationDto> expoMitigationDtoMap = RwaUtils.getRelMitigationMap(this.unionDto.getMitigationMap(), this.unionDto.getExposureRelevanceMap(), exposureDto.getAbsExposureId(), false, null);
/*     */       
/* 144 */       mitigate(exposureDto, expoMitigationDtoMap);
/*     */       
/* 146 */       exposureDto.setWarw(RwaMath.getWarw(exposureDto.getEad(), exposureDto.getRwaMb(), exposureDto.getRwaMa(), exposureDto.getRw()));
/* 147 */       this.unionDto.getProduct().setProductAb(RwaMath.add(this.unionDto.getProduct().getProductAb(), exposureDto.getAssetBalance()));
/* 148 */       this.unionDto.getProduct().setProductEad(RwaMath.add(this.unionDto.getProduct().getProductEad(), exposureDto.getEad()));
/* 149 */       this.unionDto.getProduct().setProductRwaMb(RwaMath.add(this.unionDto.getProduct().getProductRwaMb(), exposureDto.getRwaMb()));
/* 150 */       this.unionDto.getProduct().setProductRwaMa(RwaMath.add(this.unionDto.getProduct().getProductRwaMa(), exposureDto.getRwaMa()));
/*     */     } 
/* 152 */     this.unionDto.getProduct().setProductRwaAa(BigDecimal.ZERO);
/* 153 */     this.unionDto.getProduct().setRwaAdj(this.unionDto.getProduct().getProductRwaMa());
/*     */   }
/*     */   
/*     */   public AbsExposureDto initExposure(AbsExposureDto exposureDto, int n) {
/* 157 */     exposureDto.setSortNo(Integer.valueOf(n));
/* 158 */     exposureDto.setRwaMb(RwaMath.mul(exposureDto.getEad(), exposureDto.getRw()));
/* 159 */     exposureDto.setRwaMa(BigDecimal.ZERO);
/* 160 */     exposureDto.setRwaAa(BigDecimal.ZERO);
/* 161 */     exposureDto.setRwaUm(BigDecimal.ZERO);
/* 162 */     exposureDto.setCoveredEa(BigDecimal.ZERO);
/* 163 */     exposureDto.setUncoveredEa(BigDecimal.ZERO);
/* 164 */     return exposureDto;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mitigateBySelf(AbsExposureDto exposureDto) {
/* 170 */     if (RwaMath.isPositive(exposureDto.getProvisionDed())) {
/* 171 */       addMitigationDetail(getMitigationDetailByProvision(exposureDto));
/*     */       
/* 173 */       if (RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 174 */         return true;
/*     */       }
/* 176 */     } else if (RwaMath.isZero(exposureDto.getEad())) {
/*     */       
/* 178 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 179 */       return true;
/*     */     } 
/*     */     
/* 182 */     if (RwaMath.isZero(exposureDto.getRw())) {
/*     */       
/* 184 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 185 */       return true;
/*     */     } 
/*     */     
/* 188 */     if (exposureDto.getResidualMaturity() == null) {
/* 189 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 190 */       return true;
/*     */     } 
/*     */     
/* 193 */     if (!this.creditRule.isEnMitiExcExpoMaturity() && RwaMath.isZero(exposureDto.getResidualMaturity())) {
/* 194 */       addMitigationDetail(getMitigationDetail(exposureDto));
/* 195 */       return true;
/*     */     } 
/* 197 */     return false;
/*     */   }
/*     */ 
/*     */   
/*     */   public void mitigate(AbsExposureDto exposureDto, Map<BigDecimal, MitigationDto> expoMitigationDtoMap) {
/* 202 */     if (mitigateBySelf(exposureDto)) {
/*     */       return;
/*     */     }
/*     */     
/* 206 */     if (CollUtil.isEmpty(expoMitigationDtoMap)) {
/*     */       
/* 208 */       addMitigationDetail(getMitigationDetail(exposureDto));
/*     */       
/*     */       return;
/*     */     } 
/* 212 */     for (BigDecimal sortNo : expoMitigationDtoMap.keySet()) {
/* 213 */       MitigationDto mitigationDto = expoMitigationDtoMap.get(sortNo);
/*     */       
/* 215 */       UnmitigatedReason unmitigatedReason = checkMitigate(exposureDto, mitigationDto);
/* 216 */       if (unmitigatedReason != null) {
/*     */         
/* 218 */         addMitigationDetail(getMitigationDetailByFailed(exposureDto, mitigationDto, unmitigatedReason));
/*     */         continue;
/*     */       } 
/* 221 */       if (mitigate(exposureDto, mitigationDto)) {
/*     */         break;
/*     */       }
/*     */       
/* 225 */       if (NumberUtil.isGreaterOrEqual(mitigationDto.getRw(), exposureDto.getRw())) {
/*     */         break;
/*     */       }
/*     */     } 
/*     */     
/* 230 */     if (!RwaMath.isZero(exposureDto.getCurrentAmount())) {
/* 231 */       addMitigationDetail(getMitigationDetail(exposureDto));
/*     */     }
/*     */   }
/*     */ 
/*     */   
/*     */   public UnmitigatedReason checkMitigate(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 237 */     if (this.creditRule.isAbsEnMmm()) {
/*     */       
/* 239 */       if (mitigationDto.getResidualMaturity() == null) {
/* 240 */         return UnmitigatedReason.WA_MM_NULL;
/*     */       }
/*     */       
/* 243 */       if (!this.creditRule.isAbsEnMitiExcRm() && RwaMath.isZero(mitigationDto.getResidualMaturity())) {
/* 244 */         return UnmitigatedReason.WA_MM_ZERO;
/*     */       }
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 250 */       mitigationDto.setHt(RwaMath.getHt(exposureDto.getResidualMaturity(), mitigationDto.getResidualMaturity(), mitigationDto.getOriginalMaturity()));
/* 251 */       if (mitigationDto.getHt().compareTo(BigDecimal.valueOf(-1L)) == 0)
/*     */       {
/* 253 */         return UnmitigatedReason.IRB_MM_OM; } 
/* 254 */       if (mitigationDto.getHt().compareTo(BigDecimal.valueOf(-2L)) == 0)
/*     */       {
/* 256 */         return UnmitigatedReason.IRB_MM_RM; } 
/* 257 */       if (mitigationDto.getHt().compareTo(BigDecimal.ZERO) <= 0)
/*     */       {
/* 259 */         return UnmitigatedReason.IRB_MM_ZERO;
/*     */       }
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 265 */     if (this.creditRule.isCdgEnPayThreshold() && 
/* 266 */       StrUtil.equals(mitigationDto.getMitigationMainType(), MitigationMainType.CREDIT_DERIVATIVE.getCode()) && 
/* 267 */       NumberUtil.isGreater(mitigationDto.getPayDefaultThreshold(), BigDecimal.ZERO) && 
/* 268 */       !mitigationDto.isUseMitigateLimitOfCdg())
/*     */     {
/*     */       
/* 271 */       if (isMitigateFailedOfCreditDerivative(exposureDto, mitigationDto)) {
/* 272 */         return UnmitigatedReason.WA_CDG;
/*     */       }
/*     */     }
/*     */     
/* 276 */     if (NumberUtil.isGreaterOrEqual(mitigationDto.getRw(), exposureDto.getRw())) {
/* 277 */       mitigationDto.setRwAdjust(mitigationDto.getRw());
/* 278 */       return UnmitigatedReason.WA_RW;
/*     */     } 
/* 280 */     return null;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean mitigate(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 287 */     mitigationDto.setRwAdjust(mitigationDto.getRw());
/* 288 */     mitigationDto.setIsExemptRwLine(Identity.NO.getCode());
/* 289 */     mitigationDto.setValueFc(BigDecimal.ZERO);
/*     */     
/* 291 */     if (this.creditRule.isAbsEnCmm()) {
/* 292 */       mitigationDto.setHfx(getHfx(exposureDto, mitigationDto));
/*     */     }
/*     */     
/* 295 */     if (this.creditRule.isAbsEnMmm()) {
/* 296 */       mitigationDto.setHt(RwaMath.getHt(exposureDto.getResidualMaturity(), mitigationDto.getResidualMaturity(), mitigationDto.getOriginalMaturity()));
/*     */     }
/*     */     
/* 299 */     if (StrUtil.equals(mitigationDto.getMitigationMainType(), MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/*     */       
/* 301 */       if (this.creditRule.isCdgEnPayThreshold() && 
/* 302 */         NumberUtil.isGreater(mitigationDto.getPayDefaultThreshold(), BigDecimal.ZERO) && 
/* 303 */         !mitigationDto.isUseMitigateLimitOfCdg()) {
/*     */         
/* 305 */         mitigationDto.setUseMitigateLimitOfCdg(true);
/* 306 */         addMitigationDetail(getMitigationDetailByCdg(exposureDto, mitigationDto));
/*     */         
/* 308 */         exposureDto.setCurrentAmount(RwaMath.sub(exposureDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/* 309 */         mitigationDto.setCurrentAmount(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/*     */       } 
/*     */       
/* 312 */       mitigationDto.setCurrentMitigated(NumberUtil.min(new BigDecimal[] { exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount() }));
/* 313 */       mitigationDto.setCurrentCovered(RwaMath.getCdgCovered(mitigationDto.getCurrentMitigated(), mitigationDto.getHc(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */     } else {
/*     */       
/* 316 */       if (StrUtil.equals(RwaConfig.waMitigateMode, MitigateMode.AFTER.getCode())) {
/*     */         
/* 318 */         mitigationDto.setCurrentMitigated(NumberUtil.min(new BigDecimal[] { exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount() }));
/*     */       } else {
/*     */         
/* 321 */         mitigationDto.setCurrentMitigated(RwaMath.getMitigateAmount(exposureDto.getCurrentAmount(), mitigationDto.getCurrentAmount(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */       } 
/* 323 */       mitigationDto.setCurrentCovered(RwaMath.getMitigateCovered(mitigationDto.getCurrentMitigated(), mitigationDto.getHfx(), mitigationDto.getHt()));
/*     */     } 
/*     */     
/* 326 */     mitigationDto.setResidualAmount(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getCurrentMitigated()));
/*     */     
/* 328 */     exposureDto.setResidualAmount(RwaMath.sub(exposureDto.getCurrentAmount(), mitigationDto.getCurrentCovered()));
/*     */     
/* 330 */     addMitigationDetail(getMitigationDetail(exposureDto, mitigationDto));
/*     */     
/* 332 */     mitigationDto.setCurrentAmount(mitigationDto.getResidualAmount());
/*     */     
/* 334 */     exposureDto.setCurrentAmount(exposureDto.getResidualAmount());
/* 335 */     return RwaMath.isZero(exposureDto.getCurrentAmount());
/*     */   }
/*     */   
/*     */   public void addMitigationDetail(MitigationDetailDto detailDto) {
/* 339 */     if (detailDto == null) {
/*     */       return;
/*     */     }
/* 342 */     this.unionDto.getDetailList().add(detailDto);
/* 343 */     aggregate2ExposureResult(detailDto, this.unionDto.getExposureMap(), this.unionDto.getMitigationMap());
/*     */   }
/*     */   
/*     */   public BigDecimal getHfx(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 347 */     if (RwaMath.isCurrencyMismatch(exposureDto.getCurrency(), mitigationDto.getCurrency())) {
/*     */       
/* 349 */       if (RwaConfig.isEnHfxAdj(this.creditRule, mitigationDto.getMitigationMainType())) {
/*     */         
/* 351 */         Integer frequency = mitigationDto.getRevaFrequency();
/* 352 */         if (StrUtil.equals(mitigationDto.getMitigationType(), MitigationType.GUARANTEE.getCode())) {
/* 353 */           frequency = exposureDto.getRevaFrequency();
/*     */         }
/* 355 */         return RwaMath.adjustHaircut(this.creditRule.getShfx(), frequency, exposureDto.getTm());
/*     */       } 
/* 357 */       return this.creditRule.getShfx();
/*     */     } 
/*     */     
/* 360 */     return BigDecimal.ZERO;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public boolean isMitigateFailedOfCreditDerivative(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 371 */     if (NumberUtil.isGreaterOrEqual(mitigationDto.getPayDefaultThreshold(), exposureDto.getCurrentAmount())) {
/* 372 */       return true;
/*     */     }
/*     */     
/* 375 */     BigDecimal l = mitigationDto.getPayDefaultThreshold();
/*     */     
/* 377 */     BigDecimal ca = l;
/*     */     
/* 379 */     BigDecimal rwa = RwaMath.mul(l, RwaMath.maxRw);
/*     */     
/* 381 */     BigDecimal ra = RwaMath.sub(exposureDto.getCurrentAmount(), l);
/*     */     
/* 383 */     BigDecimal rm = RwaMath.sub(mitigationDto.getCurrentAmount(), l);
/*     */     
/* 385 */     if (this.creditRule.isGuarEnCmm()) {
/* 386 */       mitigationDto.setHfx(getHfx(exposureDto, mitigationDto));
/*     */     }
/*     */     
/* 389 */     BigDecimal ma = NumberUtil.min(new BigDecimal[] { ra, rm });
/*     */     
/* 391 */     BigDecimal mca = RwaMath.getCdgCovered(ma, mitigationDto.getHc(), mitigationDto.getHfx(), mitigationDto.getHt());
/*     */     
/* 393 */     ca = RwaMath.add(ca, mca);
/* 394 */     rwa = RwaMath.add(rwa, RwaMath.mul(mca, mitigationDto.getRw()));
/*     */     
/* 396 */     if (NumberUtil.isGreaterOrEqual(rwa, RwaMath.mul(ca, exposureDto.getRw()))) {
/* 397 */       return true;
/*     */     }
/* 399 */     return false;
/*     */   }
/*     */   
/*     */   public void aggregate2ExposureResult(MitigationDetailDto detailDto, Map<String, AbsExposureDto> exposureMap, Map<String, MitigationDto> mitigationMap) {
/*     */     MitigationDto mitigationDto;
/* 404 */     if (detailDto == null || StrUtil.equals(detailDto.getIsResult(), Identity.NO.getCode()) || 
/* 405 */       StrUtil.equals(detailDto.getMitigatedFlag(), MitigatedFlag.PROVISION.getCode())) {
/*     */       return;
/*     */     }
/*     */     
/* 409 */     AbsExposureDto exposureDto = exposureMap.get(detailDto.getExposureId());
/* 410 */     MitigatedFlag mitigatedFlag = (MitigatedFlag)EnumUtils.getEnumByCode(detailDto.getMitigatedFlag(), MitigatedFlag.class);
/* 411 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$MitigatedFlag[mitigatedFlag.ordinal()]) {
/*     */       
/*     */       case 1:
/* 414 */         exposureDto.setUncoveredEa(RwaMath.add(exposureDto.getUncoveredEa(), detailDto.getCoveredEa()));
/* 415 */         exposureDto.setRwaUm(RwaMath.add(exposureDto.getRwaUm(), detailDto.getRwaMa()));
/*     */         break;
/*     */       
/*     */       case 2:
/* 419 */         exposureDto.setCoveredEa(RwaMath.add(exposureDto.getCoveredEa(), detailDto.getCoveredEa()));
/*     */         
/* 421 */         mitigationDto = mitigationMap.get(detailDto.getMitigationId());
/*     */         
/* 423 */         mitigationDto.setMitigatedAmount(RwaMath.add(mitigationDto.getMitigatedAmount(), detailDto.getMitigationUseAmount()));
/* 424 */         mitigationDto.setCoveredEa(RwaMath.add(mitigationDto.getCoveredEa(), detailDto.getCoveredEa()));
/* 425 */         mitigationDto.setMitigatedEffect(RwaMath.add(mitigationDto
/* 426 */               .getMitigatedEffect(), RwaMath.sub(detailDto.getRwaMb(), detailDto.getRwaMa())));
/*     */ 
/*     */         
/* 429 */         mitigationDto.setUnmitigatedAmount(RwaMath.sub(mitigationDto.getMitigationAmount(), mitigationDto.getMitigatedAmount()));
/*     */         break;
/*     */     } 
/*     */     
/* 433 */     exposureDto.setRwaMa(RwaMath.add(exposureDto.getRwaMa(), detailDto.getRwaMa()));
/* 434 */     exposureDto.setRwaAdj(exposureDto.getRwaMa());
/*     */   }
/*     */   
/*     */   public MitigationDetailDto getMitigationDetailByProvision(AbsExposureDto exposureDto) {
/* 438 */     if (RwaMath.isZeroOrNull(exposureDto.getProvisionDed())) {
/* 439 */       return null;
/*     */     }
/* 441 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 442 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 443 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 444 */     detailDto.setExposureId(exposureDto.getAbsExposureId());
/* 445 */     detailDto.setOffBusinessType(exposureDto.getOffAbsBizType());
/* 446 */     detailDto.setIsResult(Identity.YES.getCode());
/* 447 */     detailDto.setMitigatedFlag(MitigatedFlag.PROVISION.getCode());
/*     */     
/* 449 */     detailDto.setUncoveredEa(exposureDto.getAssetBalance());
/* 450 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/*     */     
/* 452 */     detailDto.setCoveredEa(exposureDto.getProvisionDed());
/* 453 */     detailDto.setMitigatedEa(BigDecimal.ZERO);
/* 454 */     detailDto.setMitigatedRw(exposureDto.getRw());
/* 455 */     detailDto.setRwaMb(RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 456 */     detailDto.setRwaMa(detailDto.getRwaMb());
/* 457 */     return detailDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetail(AbsExposureDto exposureDto) {
/* 466 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 467 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 468 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 469 */     detailDto.setExposureId(exposureDto.getAbsExposureId());
/* 470 */     detailDto.setOffBusinessType(exposureDto.getOffAbsBizType());
/* 471 */     detailDto.setIsResult(Identity.YES.getCode());
/* 472 */     detailDto.setMitigatedFlag(MitigatedFlag.NO.getCode());
/* 473 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 474 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/*     */     
/* 476 */     if (StrUtil.equals(exposureDto.getExposureBelong(), ExposureBelong.OFF.getCode()) && 
/* 477 */       RwaMath.isZero(exposureDto.getCcf())) {
/* 478 */       detailDto.setCoveredEa(exposureDto.getAssetBalance());
/*     */     } else {
/* 480 */       detailDto.setCoveredEa(exposureDto.getCurrentAmount());
/*     */     } 
/* 482 */     detailDto.setMitigatedEa(RwaMath.getEad(detailDto.getCoveredEa(), detailDto.getCcf()));
/* 483 */     detailDto.setMitigatedRw(detailDto.getRw());
/* 484 */     detailDto.setRwaMb(RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 485 */     detailDto.setRwaMa(detailDto.getRwaMb());
/* 486 */     return detailDto;
/*     */   }
/*     */   
/*     */   public MitigationDetailDto getMitigationDetailByFailed(AbsExposureDto exposureDto, MitigationDto mitigationDto, UnmitigatedReason reason) {
/* 490 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 491 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 492 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 493 */     detailDto.setExposureId(exposureDto.getAbsExposureId());
/* 494 */     detailDto.setOffBusinessType(exposureDto.getOffAbsBizType());
/* 495 */     detailDto.setIsResult(Identity.NO.getCode());
/* 496 */     detailDto.setMitigatedFlag(MitigatedFlag.NO.getCode());
/* 497 */     detailDto.setFmReason(reason.getCode());
/* 498 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 499 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 500 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 501 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 502 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 503 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 504 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 505 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 506 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 507 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 508 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 509 */     detailDto.setMitigationUseAmount(BigDecimal.ZERO);
/* 510 */     detailDto.setMitigationResiValue(mitigationDto.getCurrentAmount());
/* 511 */     detailDto.setMitigationShc(mitigationDto.getShc());
/* 512 */     detailDto.setMitigationHc(mitigationDto.getHc());
/* 513 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 514 */     detailDto.setHfx(mitigationDto.getHfx());
/* 515 */     detailDto.setHt(mitigationDto.getHt());
/* 516 */     detailDto.setIsExemptRwLine(mitigationDto.getIsExemptRwLine());
/* 517 */     detailDto.setValueFc(mitigationDto.getValueFc());
/* 518 */     detailDto.setCoveredEa(BigDecimal.ZERO);
/* 519 */     detailDto.setMitigatedEa(BigDecimal.ZERO);
/* 520 */     detailDto.setMitigatedRw(mitigationDto.getRwAdjust());
/* 521 */     detailDto.setRwaMb(BigDecimal.ZERO);
/* 522 */     detailDto.setRwaMa(BigDecimal.ZERO);
/* 523 */     return detailDto;
/*     */   }
/*     */ 
/*     */   
/*     */   public MitigationDetailDto getMitigationDetailByCdg(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 528 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 529 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 530 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 531 */     detailDto.setIsResult(Identity.YES.getCode());
/* 532 */     detailDto.setMitigatedFlag(MitigatedFlag.MITIGATED.getCode());
/* 533 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 534 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 535 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 536 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 537 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 538 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 539 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 540 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 541 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 542 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 543 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 544 */     detailDto.setMitigationUseAmount(mitigationDto.getPayDefaultThreshold());
/* 545 */     detailDto.setMitigationResiValue(RwaMath.sub(mitigationDto.getCurrentAmount(), mitigationDto.getPayDefaultThreshold()));
/* 546 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 547 */     detailDto.setIsExemptRwLine(Identity.NO.getCode());
/* 548 */     detailDto.setCoveredEa(mitigationDto.getPayDefaultThreshold());
/* 549 */     detailDto.setMitigatedEa(RwaMath.getEad(detailDto.getCoveredEa(), detailDto.getCcf()));
/* 550 */     detailDto.setMitigatedRw(RwaMath.maxRw);
/* 551 */     detailDto.setRwaMb(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getRw()));
/* 552 */     detailDto.setRwaMa(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 553 */     return detailDto;
/*     */   }
/*     */   
/*     */   public MitigationDetailDto getMitigationDetail(AbsExposureDto exposureDto, MitigationDto mitigationDto) {
/* 557 */     MitigationDetailDto detailDto = new MitigationDetailDto();
/* 558 */     BeanUtils.copyProperties(exposureDto, detailDto);
/* 559 */     detailDto.setDetailNo(IdWorker.getIdStr());
/* 560 */     detailDto.setExposureId(exposureDto.getAbsExposureId());
/* 561 */     detailDto.setOffBusinessType(exposureDto.getOffAbsBizType());
/* 562 */     detailDto.setIsResult(Identity.YES.getCode());
/* 563 */     detailDto.setMitigatedFlag(MitigatedFlag.MITIGATED.getCode());
/* 564 */     detailDto.setUncoveredEa(exposureDto.getCurrentAmount());
/* 565 */     detailDto.setMitigationId(mitigationDto.getMitigationId());
/* 566 */     detailDto.setIsAlone(mitigationDto.getIsAlone());
/* 567 */     detailDto.setMitigationType(mitigationDto.getMitigationType());
/* 568 */     detailDto.setMitigationClientId(mitigationDto.getClientId());
/* 569 */     detailDto.setMitigationClientType(mitigationDto.getClientType());
/* 570 */     detailDto.setMitigationMainType(mitigationDto.getMitigationMainType());
/* 571 */     detailDto.setMitigationSmallType(mitigationDto.getMitigationSmallType());
/* 572 */     detailDto.setMitigationRptItemWa(mitigationDto.getMitigationRptItemWa());
/* 573 */     detailDto.setMitigationOrigValue(mitigationDto.getMitigationAmount());
/* 574 */     detailDto.setMitigationCtValue(mitigationDto.getCurrentAmount());
/* 575 */     detailDto.setMitigationUseAmount(mitigationDto.getCurrentMitigated());
/* 576 */     detailDto.setMitigationResiValue(mitigationDto.getResidualAmount());
/* 577 */     detailDto.setMitigationShc(mitigationDto.getShc());
/* 578 */     detailDto.setMitigationHc(mitigationDto.getHc());
/* 579 */     detailDto.setMitigationOrigRw(mitigationDto.getRw());
/* 580 */     detailDto.setHfx(mitigationDto.getHfx());
/* 581 */     detailDto.setHt(mitigationDto.getHt());
/* 582 */     detailDto.setIsExemptRwLine(mitigationDto.getIsExemptRwLine());
/* 583 */     detailDto.setValueFc(mitigationDto.getValueFc());
/* 584 */     detailDto.setCoveredEa(mitigationDto.getCurrentCovered());
/* 585 */     detailDto.setMitigatedEa(RwaMath.getEad(detailDto.getCoveredEa(), detailDto.getCcf()));
/* 586 */     detailDto.setMitigatedRw(mitigationDto.getRwAdjust());
/* 587 */     detailDto.setRwaMb(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getRw()));
/* 588 */     detailDto.setRwaMa(NumberUtil.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedRw()));
/* 589 */     return detailDto;
/*     */   }
/*     */   
/*     */   public void adjustProductLimit() {
/* 593 */     if (this.unionDto.getProduct() == null || this.unionDto.getIsOriginator() == Identity.NO) {
/*     */       return;
/*     */     }
/* 596 */     AbsProductDto productDto = this.unionDto.getProduct();
/*     */     
/* 598 */     BigDecimal rwaLimit = productDto.getApRwa();
/* 599 */     if (RwaUtils.isB3(this.unionDto.getTaskType())) {
/*     */       
/* 601 */       Map<String, BigDecimal> tranchePropMap = new HashMap<>();
/* 602 */       for (AbsExposureDto exposureDto : this.unionDto.getExposureList()) {
/*     */         
/* 604 */         if (StrUtil.isEmpty(exposureDto.getSecuritiesCode())) {
/* 605 */           log.warn("数据异常， ABS证券代码为空");
/*     */           continue;
/*     */         } 
/* 608 */         add2ResultMap(tranchePropMap, exposureDto.getSecuritiesCode(), exposureDto.getTrancheProp());
/*     */       } 
/*     */ 
/*     */       
/* 612 */       BigDecimal maxTrancheProp = getMaxTrancheProp(tranchePropMap);
/* 613 */       if (RwaMath.isZero(maxTrancheProp))
/*     */       {
/* 615 */         maxTrancheProp = BigDecimal.ONE;
/*     */       }
/* 617 */       productDto.setMaxTrancheProp(maxTrancheProp);
/*     */       
/* 619 */       rwaLimit = RwaMath.mul(productDto.getProductRwaLimit(), maxTrancheProp);
/*     */     } 
/* 621 */     productDto.setProductRwaLimit(rwaLimit);
/*     */     
/* 623 */     if (rwaLimit == null || NumberUtil.isLessOrEqual(productDto.getProductRwaMa(), rwaLimit)) {
/*     */       return;
/*     */     }
/*     */     
/* 627 */     BigDecimal adjustRwa = RwaMath.sub(productDto.getProductRwaMa(), rwaLimit);
/* 628 */     productDto.setProductRwaAa(adjustRwa);
/* 629 */     productDto.setRwaAdj(rwaLimit);
/*     */     
/* 631 */     BigDecimal onEad = BigDecimal.ZERO;
/* 632 */     BigDecimal offEad = BigDecimal.ZERO;
/* 633 */     BigDecimal onRwa = BigDecimal.ZERO;
/* 634 */     BigDecimal offRwa = BigDecimal.ZERO;
/* 635 */     BigDecimal onRwaAdj = BigDecimal.ZERO;
/* 636 */     BigDecimal offRwaAdj = BigDecimal.ZERO;
/* 637 */     for (AbsExposureDto exposureDto : this.unionDto.getExposureList()) {
/* 638 */       if (StrUtil.equals(exposureDto.getExposureBelong(), ExposureBelong.OFF.getCode())) {
/* 639 */         offEad = RwaMath.add(offEad, exposureDto.getEad());
/* 640 */         offRwa = RwaMath.add(offRwa, exposureDto.getRwaMa()); continue;
/*     */       } 
/* 642 */       onEad = RwaMath.add(onEad, exposureDto.getEad());
/* 643 */       onRwa = RwaMath.add(onRwa, exposureDto.getRwaMa());
/*     */     } 
/*     */ 
/*     */     
/* 647 */     onRwaAdj = RwaMath.mul(adjustRwa, RwaMath.div(onEad, productDto.getProductEad()));
/* 648 */     offRwaAdj = RwaMath.mul(adjustRwa, RwaMath.div(offEad, productDto.getProductEad()));
/* 649 */     for (AbsExposureDto exposureDto : this.unionDto.getExposureList()) {
/*     */       
/* 651 */       if (StrUtil.equals(exposureDto.getExposureBelong(), ExposureBelong.OFF.getCode())) {
/* 652 */         exposureDto.setRwaAa(RwaMath.mul(offRwaAdj, RwaMath.div(exposureDto.getRwaMa(), offRwa)));
/*     */       } else {
/* 654 */         exposureDto.setRwaAa(RwaMath.mul(onRwaAdj, RwaMath.div(exposureDto.getRwaMa(), onRwa)));
/*     */       } 
/* 656 */       exposureDto.setRwaAdj(RwaMath.sub(exposureDto.getRwaMa(), exposureDto.getRwaAa()));
/*     */     } 
/*     */   }
/*     */   
/*     */   public void add2ResultMap(Map<String, BigDecimal> map, String code, BigDecimal result) {
/* 661 */     if (map.get(code) == null) {
/* 662 */       map.put(code, result);
/*     */     } else {
/* 664 */       map.put(code, RwaMath.add(map.get(code), result));
/*     */     } 
/*     */   }
/*     */   
/*     */   public BigDecimal getMaxTrancheProp(Map<String, BigDecimal> map) {
/* 669 */     BigDecimal prop = BigDecimal.ZERO;
/* 670 */     for (BigDecimal value : map.values()) {
/* 671 */       prop = NumberUtil.max(new BigDecimal[] { prop, value });
/*     */     } 
/* 673 */     return prop;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\calculation\AbsCalculation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */