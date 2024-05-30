/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.calculation;
/*     */ 
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InstrumentsType;
/*     */ import com.amarsoft.rwa.engine.constant.SftType;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.SftCollateralDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftNettingDto;
/*     */ import com.amarsoft.rwa.engine.entity.SftUnionDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ 
/*     */ public class SftCalculation {
/*     */   private SftUnionDto unionDto;
/*     */   
/*  25 */   public SftUnionDto getUnionDto() { return this.unionDto; } private boolean isB3; private CreditRuleDo creditRuleDo; public void setUnionDto(SftUnionDto unionDto) {
/*  26 */     this.unionDto = unionDto;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   private SftCalculation(SftUnionDto unionDto) {
/*  32 */     this.unionDto = unionDto;
/*  33 */     this.isB3 = RwaUtils.isB3(unionDto.getTaskType());
/*  34 */     this.creditRuleDo = RwaConfig.getCreditRule(unionDto.getSchemeConfig(), unionDto.getApproach().getCode());
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.calculation.SftCalculation createCalculation(SftUnionDto unionDto) {
/*  38 */     return new com.amarsoft.rwa.engine.calculation.SftCalculation(unionDto);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  43 */     if (this.unionDto.isSpecialApproach()) {
/*  44 */       calculateMaturity(this.unionDto.getExposureDto(), this.unionDto.getExposureDto().getTm());
/*  45 */       specialCalculate(this.unionDto.getExposureDto(), this.unionDto.getCollateralList());
/*     */       
/*     */       return;
/*     */     } 
/*  49 */     SftNettingDto nettingDto = this.unionDto.getNettingDto();
/*  50 */     String currency = null;
/*  51 */     Integer tm = null;
/*  52 */     if (this.unionDto.getNettingFlag() == Identity.YES) {
/*  53 */       currency = nettingDto.getCurrency();
/*  54 */       tm = nettingDto.getTm();
/*     */     } 
/*     */ 
/*     */     
/*  58 */     boolean isZeroHaircut = true;
/*  59 */     BigDecimal heAmount = BigDecimal.ZERO;
/*  60 */     BigDecimal fcValue = BigDecimal.ZERO;
/*  61 */     BigDecimal hcAmount = BigDecimal.ZERO;
/*  62 */     BigDecimal hfxAmount = BigDecimal.ZERO;
/*     */ 
/*     */     
/*  65 */     Integer securityCnt = Integer.valueOf(0);
/*  66 */     BigDecimal maxSecurityAmount = BigDecimal.ZERO;
/*     */     
/*  68 */     BigDecimal factorExposure = BigDecimal.ZERO;
/*     */     
/*  70 */     BigDecimal factorAe = BigDecimal.ZERO;
/*     */     
/*  72 */     BigDecimal factorCollateral = BigDecimal.ZERO;
/*     */     
/*  74 */     BigDecimal factorAmount = BigDecimal.ZERO;
/*     */     
/*  76 */     for (SftExposureDto exposure : this.unionDto.getExposureList()) {
/*     */       
/*  78 */       if (this.unionDto.getNettingFlag() == Identity.NO) {
/*  79 */         currency = exposure.getCurrency();
/*  80 */         tm = exposure.getTm();
/*     */       } 
/*     */       
/*  83 */       calculateMaturity(exposure, tm);
/*     */       
/*  85 */       List<SftCollateralDto> collateralDtoList = (List<SftCollateralDto>)this.unionDto.getCollateralListMap().get(exposure.getExposureId());
/*  86 */       calculateExposure(exposure, currency, tm, collateralDtoList);
/*     */       
/*  88 */       if (this.unionDto.getNettingFlag() == Identity.NO) {
/*     */         
/*  90 */         calculateExposureResult(exposure, collateralDtoList);
/*     */         
/*     */         return;
/*     */       } 
/*     */       
/*  95 */       nettingDto.setWeightingMaturity(RwaMath.add(nettingDto.getWeightingMaturity(), RwaMath.mul(Integer.valueOf(exposure.getTeMaturity()), exposure.getAssetBalance())));
/*     */ 
/*     */       
/*  98 */       if (exposure.isNoMitigation() || StrUtil.equals(exposure.getIsZeroHaircut(), Identity.NO.getCode())) {
/*  99 */         isZeroHaircut = false;
/*     */       }
/*     */       
/* 102 */       if (this.creditRuleDo.isEnSftFactorLine() && StrUtil.equals(exposure.getHaircutLineAdjustFlag(), Identity.YES.getCode())) {
/* 103 */         BigDecimal hfAmount = getHfExposureAmount(exposure);
/* 104 */         factorExposure = RwaMath.add(factorExposure, hfAmount);
/* 105 */         factorAe = RwaMath.add(factorAe, RwaMath.adjustExposure(hfAmount, exposure.getFactorLine()));
/* 106 */         factorCollateral = RwaMath.add(factorCollateral, exposure.getHfCollAmount());
/* 107 */         factorAmount = RwaMath.add(factorAmount, exposure.getFactorAmount());
/*     */       } 
/*     */       
/* 110 */       heAmount = RwaMath.add(heAmount, RwaMath.mul(exposure.getAssetBalance(), exposure.getHe()));
/* 111 */       fcValue = RwaMath.add(fcValue, exposure.getFcValue());
/* 112 */       hcAmount = RwaMath.add(hcAmount, exposure.getHcAmount());
/* 113 */       hfxAmount = RwaMath.add(hfxAmount, exposure.getHfxAmount());
/*     */       
/* 115 */       if (this.isB3) {
/*     */ 
/*     */         
/* 118 */         if (RwaUtils.isSecurity(exposure.getInstrumentsType())) {
/* 119 */           maxSecurityAmount = NumberUtil.max(new BigDecimal[] { maxSecurityAmount, exposure.getAssetBalance() });
/*     */         }
/*     */         
/* 122 */         if (CollUtil.isEmpty(collateralDtoList)) {
/*     */           continue;
/*     */         }
/* 125 */         for (SftCollateralDto collateralDto : collateralDtoList) {
/* 126 */           if (RwaUtils.isSecurity(collateralDto.getMitigationSmallType())) {
/* 127 */             maxSecurityAmount = NumberUtil.max(new BigDecimal[] { maxSecurityAmount, collateralDto.getCollateralAmount() });
/*     */           }
/*     */         } 
/*     */       } 
/*     */     } 
/*     */ 
/*     */     
/* 134 */     nettingDto.setIsZeroHaircut(Identity.NO.getCode());
/* 135 */     nettingDto.setHeAmount(heAmount);
/* 136 */     nettingDto.setHe(RwaMath.div(heAmount, nettingDto.getAssetBalance()));
/* 137 */     nettingDto.setFcValue(fcValue);
/* 138 */     nettingDto.setFcHaircut(RwaMath.div(hcAmount, fcValue));
/* 139 */     nettingDto.setHcAmount(hcAmount);
/* 140 */     nettingDto.setHfx(RwaMath.div(hfxAmount, fcValue));
/* 141 */     nettingDto.setHfxAmount(hfxAmount);
/*     */     
/* 143 */     if (this.creditRuleDo.isEnSftFactorLine() && RwaMath.isPositive(factorExposure)) {
/*     */ 
/*     */       
/* 146 */       nettingDto.setEffectiveFactorLine(RwaMath.getEffectiveFactor(RwaMath.div(factorAe, factorExposure), RwaMath.div(factorAmount, factorCollateral)));
/*     */ 
/*     */       
/* 149 */       nettingDto.setEffectiveHaircut(RwaMath.getEffectiveHaircut(factorAe, factorAmount));
/*     */ 
/*     */       
/* 152 */       if (NumberUtil.isLess(nettingDto.getEffectiveHaircut(), nettingDto.getEffectiveFactorLine())) {
/*     */         
/* 154 */         nettingDto.setIsTouchLine(Identity.YES.getCode());
/* 155 */         nettingDto.setMitigatedEa(nettingDto.getAssetBalance());
/* 156 */         nettingDto.setRwa(RwaMath.getRwa(this.unionDto.getApproach(), nettingDto.getMitigatedEa(), nettingDto.getRw(), nettingDto.getKcr()));
/*     */         return;
/*     */       } 
/*     */     } 
/* 160 */     nettingDto.setIsTouchLine(Identity.NO.getCode());
/*     */     
/* 162 */     if (isZeroHaircut) {
/* 163 */       nettingDto.setIsZeroHaircut(Identity.YES.getCode());
/* 164 */       nettingDto.setFcHaircut(BigDecimal.ZERO);
/* 165 */       nettingDto.setHcAmount(BigDecimal.ZERO);
/* 166 */       nettingDto.setHfx(BigDecimal.ZERO);
/* 167 */       nettingDto.setHfxAmount(BigDecimal.ZERO);
/*     */     } 
/*     */     
/* 170 */     if (this.isB3) {
/*     */       
/* 172 */       nettingDto.setNetExposure(RwaMath.abs(RwaMath.sub(heAmount, hcAmount)));
/*     */       
/* 174 */       nettingDto.setGrossExposure(RwaMath.add(heAmount, hcAmount));
/*     */       
/* 176 */       BigDecimal maxSecurityD10 = RwaMath.div(maxSecurityAmount, Integer.valueOf(10));
/* 177 */       for (SftExposureDto exposureDto : this.unionDto.getExposureList()) {
/* 178 */         if (RwaUtils.isSecurity(exposureDto.getInstrumentsType()) && NumberUtil.isGreaterOrEqual(exposureDto.getAssetBalance(), maxSecurityD10)) {
/* 179 */           Integer integer1 = securityCnt, integer2 = securityCnt = Integer.valueOf(securityCnt.intValue() + 1);
/*     */         }
/*     */       } 
/* 182 */       if (!CollUtil.isEmpty(this.unionDto.getCollateralList())) {
/* 183 */         for (SftCollateralDto collateralDto : this.unionDto.getCollateralList()) {
/* 184 */           if (RwaUtils.isSecurity(collateralDto.getMitigationSmallType()) && NumberUtil.isGreaterOrEqual(collateralDto.getCollateralAmount(), maxSecurityD10)) {
/* 185 */             Integer integer1 = securityCnt, integer2 = securityCnt = Integer.valueOf(securityCnt.intValue() + 1);
/*     */           }
/*     */         } 
/*     */       }
/*     */       
/* 190 */       if (securityCnt.intValue() == 0) {
/* 191 */         securityCnt = Integer.valueOf(1);
/*     */       }
/* 193 */       nettingDto.setMaxSecurityAmount(maxSecurityAmount);
/* 194 */       nettingDto.setNettingSecurityCnt(securityCnt);
/*     */       
/* 196 */       nettingDto.setMitigatedEa(RwaMath.getMitigatedEa(nettingDto.getAssetBalance(), nettingDto.getFcValue(), nettingDto
/* 197 */             .getNetExposure(), nettingDto.getGrossExposure(), nettingDto.getNettingSecurityCnt().intValue(), nettingDto.getHfxAmount()));
/*     */     } else {
/*     */       
/* 200 */       nettingDto.setMitigatedEa(RwaMath.getMitigatedEa(nettingDto.getAssetBalance(), nettingDto.getHe(), nettingDto.getFcValue(), nettingDto.getFcHaircut(), nettingDto.getHfx()));
/*     */     } 
/*     */     
/* 203 */     nettingDto.setRwa(RwaMath.getRwa(this.unionDto.getApproach(), nettingDto.getMitigatedEa(), nettingDto.getRw(), nettingDto.getKcr()));
/*     */     
/* 205 */     nettingDto.setNettingMaturity(RwaMath.getEffectiveMaturity(RwaMath.div(nettingDto.getWeightingMaturity(), nettingDto.getAssetBalance()), nettingDto.getTm()));
/*     */     
/* 207 */     nettingDto.setDiscountFactor(RwaMath.getDiscountFactor(nettingDto.getNettingMaturity(), nettingDto.getTm()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void specialCalculate(SftExposureDto exposure, List<SftCollateralDto> collateralList) {
/* 215 */     for (SftCollateralDto collateral : collateralList) {
/* 216 */       if (this.isB3) {
/*     */         
/* 218 */         if (!StrUtil.equals(collateral.getQualFlagWa(), Identity.YES.getCode())) {
/* 219 */           collateral.setExposure(BigDecimal.ZERO);
/* 220 */           collateral.setRwa(BigDecimal.ZERO);
/*     */           
/*     */           continue;
/*     */         } 
/* 224 */         BigDecimal rw = confirmRwLine(exposure, collateral);
/* 225 */         collateral.setRw(rw);
/*     */         
/* 227 */         if (NumberUtil.isGreaterOrEqual(rw, exposure.getRw())) {
/* 228 */           collateral.setExposure(BigDecimal.ZERO);
/* 229 */           collateral.setRwa(BigDecimal.ZERO);
/*     */           continue;
/*     */         } 
/*     */       } 
/* 233 */       collateral.setExposure(collateral.getCollateralAmount());
/* 234 */       collateral.setRwa(RwaMath.mul(collateral.getExposure(), collateral.getRw()));
/* 235 */       exposure.setCollEad(RwaMath.add(exposure.getCollEad(), collateral.getExposure()));
/* 236 */       exposure.setCollRwa(RwaMath.add(exposure.getCollRwa(), collateral.getRwa()));
/*     */     } 
/*     */     
/* 239 */     exposure.setFcValue(exposure.getCollEad());
/* 240 */     exposure.setMitigatedEa(RwaMath.getMitigatedEad(exposure.getAssetBalance(), exposure.getCollEad()));
/* 241 */     exposure.setRwa(RwaMath.mul(exposure.getRw(), exposure.getMitigatedEa()));
/*     */   }
/*     */ 
/*     */   
/*     */   public BigDecimal confirmRwLine(SftExposureDto exposureDto, SftCollateralDto collateralDto) {
/* 246 */     if (!RwaUtils.isH0AssetB3(collateralDto.getSupervisionClass())) {
/* 247 */       return collateralDto.getRw();
/*     */     }
/*     */     
/* 250 */     if (RwaMath.isCurrencyMismatch(exposureDto.getCurrency(), collateralDto.getCurrency())) {
/* 251 */       return this.creditRuleDo.getCollRwLine();
/*     */     }
/*     */     
/* 254 */     if (!RwaUtils.isH0Transaction(exposureDto.getTsMaturity(), exposureDto.getRevaFrequency().intValue(), collateralDto.getRevaFrequency().intValue())) {
/* 255 */       return this.creditRuleDo.getCollRwLine();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 262 */     if (!StrUtil.equals(exposureDto.getCoreMarketPartyFlag(), Identity.YES.getCode())) {
/* 263 */       return this.creditRuleDo.getCollRepoMidRw();
/*     */     }
/* 265 */     return collateralDto.getRw();
/*     */   }
/*     */ 
/*     */   
/*     */   public SftExposureDto calculateMaturity(SftExposureDto exposure, Integer tm) {
/* 270 */     exposure.setTsMaturity(RwaConfig.getWorkDays(exposure.getStartDate(), exposure.getDueDate()));
/*     */     
/* 272 */     exposure.setTeMaturity(RwaConfig.getWorkDays(this.unionDto.getDataDate(), exposure.getDueDate()));
/*     */     
/* 274 */     exposure.setTransactionMaturity(RwaMath.getEffectiveMaturity(exposure.getTeMaturity(), tm.intValue()));
/*     */     
/* 276 */     exposure.setDiscountFactor(RwaMath.getDiscountFactor(exposure.getTransactionMaturity(), tm));
/* 277 */     return exposure;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public SftExposureDto calculateExposure(SftExposureDto exposure, String currency, Integer tm, List<SftCollateralDto> collateralList) {
/* 283 */     exposure.setIsZeroHaircut(Identity.NO.getCode());
/*     */     
/* 285 */     exposure.setHe(RwaMath.adjustHaircut(exposure.getShe(), exposure.getRevaFrequency(), tm));
/*     */     
/* 287 */     int h0Condition1 = 0;
/*     */     
/* 289 */     int h0Condition2 = 0;
/*     */     
/* 291 */     int h0Condition3 = 0;
/*     */     
/* 293 */     BigDecimal collAmount = BigDecimal.ZERO;
/* 294 */     BigDecimal hcAmount = BigDecimal.ZERO;
/* 295 */     BigDecimal hfxAmount = BigDecimal.ZERO;
/*     */     
/* 297 */     exposure.setIsTouchLine(Identity.NO.getCode());
/*     */     
/* 299 */     BigDecimal hfCollAmount = BigDecimal.ZERO;
/*     */     
/* 301 */     BigDecimal factorAmount = BigDecimal.ZERO;
/* 302 */     int nonGovBondCount = 0;
/*     */     
/* 304 */     if (RwaUtils.isNotGovBond(exposure.getInstrumentsType(), exposure.getIssuerType())) {
/* 305 */       nonGovBondCount++;
/*     */     }
/*     */     
/* 308 */     if (!CollUtil.isEmpty(collateralList)) {
/* 309 */       for (SftCollateralDto collateral : collateralList) {
/* 310 */         if (StrUtil.equals(collateral.getQualFlagFirb(), Identity.YES.getCode())) {
/*     */           
/* 312 */           if (!RwaUtils.isH0Asset(this.unionDto.getTaskType(), collateral.getSupervisionClass())) {
/* 313 */             h0Condition1++;
/*     */           }
/*     */           
/* 316 */           if (RwaUtils.isNotGovBond(collateral.getMitigationSmallType(), collateral.getIssuerType())) {
/* 317 */             nonGovBondCount++;
/*     */           }
/*     */           
/* 320 */           collateral.setHaircut(RwaMath.adjustHaircut(collateral.getSh(), collateral.getRevaFrequency(), tm));
/* 321 */           collateral.setHfx(RwaMath.getHfx(currency, collateral.getCurrency(), collateral.getRevaFrequency(), tm, this.unionDto
/* 322 */                 .getSchemeConfig().getWaParamVersion().getCreditRule().getShfx()));
/*     */           
/* 324 */           if (!NumberUtil.equals(collateral.getHfx(), BigDecimal.ZERO)) {
/* 325 */             h0Condition2++;
/*     */           }
/*     */           
/* 328 */           if (!RwaUtils.isH0Transaction(exposure.getTsMaturity(), exposure.getRevaFrequency().intValue(), collateral.getRevaFrequency().intValue())) {
/* 329 */             h0Condition3++;
/*     */           }
/*     */           
/* 332 */           collAmount = RwaMath.add(collAmount, collateral.getCollateralAmount());
/* 333 */           hcAmount = RwaMath.add(hcAmount, RwaMath.mul(collateral.getCollateralAmount(), collateral.getHaircut()));
/* 334 */           hfxAmount = RwaMath.add(hfxAmount, RwaMath.mul(collateral.getCollateralAmount(), collateral.getHfx()));
/*     */ 
/*     */           
/* 337 */           BigDecimal hfAmount = getHfCollateralAmount(exposure, collateral);
/* 338 */           hfCollAmount = RwaMath.add(hfCollAmount, hfAmount);
/* 339 */           factorAmount = RwaMath.add(factorAmount, RwaMath.adjustExposure(hfAmount, collateral.getFactorLine())); continue;
/*     */         } 
/* 341 */         h0Condition1++;
/*     */       } 
/*     */     }
/*     */ 
/*     */     
/* 346 */     if (RwaMath.isZero(collAmount)) {
/* 347 */       exposure.setFcValue(BigDecimal.ZERO);
/* 348 */       exposure.setNoMitigation(true);
/* 349 */       exposure.setHaircutLineAdjustFlag(Identity.NO.getCode());
/* 350 */       return exposure;
/*     */     } 
/*     */     
/* 353 */     if (h0Condition1 == 0 && h0Condition2 == 0 && h0Condition3 == 0 && 
/* 354 */       StrUtil.equals(exposure.getCoreMarketPartyFlag(), Identity.YES.getCode()))
/*     */     {
/* 356 */       if (!this.isB3 && RwaUtils.isPse(exposure.getClientType())) {
/* 357 */         exposure.setIsZeroHaircut(Identity.NO.getCode());
/*     */       } else {
/* 359 */         exposure.setIsZeroHaircut(Identity.YES.getCode());
/*     */       } 
/*     */     }
/*     */     
/* 363 */     exposure.setFcValue(collAmount);
/* 364 */     exposure.setHcAmount(hcAmount);
/* 365 */     exposure.setHfxAmount(hfxAmount);
/* 366 */     exposure.setHfCollAmount(hfCollAmount);
/* 367 */     exposure.setFactorAmount(factorAmount);
/*     */     
/* 369 */     exposure.setHaircutLineAdjustFlag(confirmHaircutLineAdjustFlag(exposure, nonGovBondCount).getCode());
/* 370 */     return exposure;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getHfCollateralAmount(SftExposureDto exposure, SftCollateralDto collateral) {
/* 380 */     if (StrUtil.equals(exposure.getSftType(), SftType.REPO.getCode()) && 
/* 381 */       DataUtils.isInList(collateral.getMitigationSmallType(), new ICodeEnum[] { (ICodeEnum)InstrumentsType.CASH, (ICodeEnum)InstrumentsType.MARGIN }))
/*     */     {
/* 383 */       if (RwaMath.isPositive(exposure.getLendAmt())) {
/* 384 */         return exposure.getLendAmt();
/*     */       }
/*     */     }
/*     */     
/* 388 */     return collateral.getCollateralAmount();
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public BigDecimal getHfExposureAmount(SftExposureDto exposure) {
/* 396 */     if (StrUtil.equals(exposure.getSftType(), SftType.REPO.getCode()) && 
/* 397 */       DataUtils.isInList(exposure.getInstrumentsType(), new ICodeEnum[] { (ICodeEnum)InstrumentsType.CASH, (ICodeEnum)InstrumentsType.MARGIN }))
/*     */     {
/* 399 */       if (RwaMath.isPositive(exposure.getLendAmt())) {
/* 400 */         return exposure.getLendAmt();
/*     */       }
/*     */     }
/* 403 */     return exposure.getAssetBalance();
/*     */   }
/*     */ 
/*     */   
/*     */   public SftExposureDto calculateExposureResult(SftExposureDto exposure, List<SftCollateralDto> collateralList) {
/* 408 */     if (exposure.isNoMitigation()) {
/* 409 */       exposure.setFcHaircut(BigDecimal.ZERO);
/* 410 */       exposure.setHfx(BigDecimal.ZERO);
/* 411 */       return noMitigationCalculate(exposure, Identity.NO);
/*     */     } 
/*     */ 
/*     */     
/* 415 */     if (this.creditRuleDo.isEnSftFactorLine() && StrUtil.equals(exposure.getHaircutLineAdjustFlag(), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 420 */       exposure.setEffectiveFactorLine(RwaMath.getEffectiveFactor(RwaMath.adjustExposureHaircut(exposure.getFactorLine()), 
/* 421 */             RwaMath.div(exposure.getFactorAmount(), exposure.getHfCollAmount())));
/*     */ 
/*     */ 
/*     */       
/* 425 */       exposure.setEffectiveHaircut(RwaMath.getEffectiveHaircut(getHfExposureAmount(exposure), exposure.getHfCollAmount()));
/*     */ 
/*     */       
/* 428 */       if (exposure.getEffectiveHaircut().compareTo(exposure.getEffectiveFactorLine()) < 0) {
/*     */         
/* 430 */         exposure.setFcHaircut(RwaMath.div(exposure.getHcAmount(), exposure.getFcValue()));
/* 431 */         exposure.setHfx(RwaMath.div(exposure.getHfxAmount(), exposure.getFcValue()));
/* 432 */         return noMitigationCalculate(exposure, Identity.YES);
/*     */       } 
/*     */     } 
/*     */     
/* 436 */     if (StrUtil.equals(exposure.getIsZeroHaircut(), Identity.YES.getCode())) {
/* 437 */       exposure.setHcAmount(BigDecimal.ZERO);
/* 438 */       exposure.setFcHaircut(BigDecimal.ZERO);
/* 439 */       exposure.setHfx(BigDecimal.ZERO);
/* 440 */       for (SftCollateralDto collateral : collateralList)
/*     */       {
/* 442 */         collateral.setIsZeroHaircut(Identity.YES.getCode());
/*     */       }
/*     */     } else {
/* 445 */       exposure.setFcHaircut(RwaMath.div(exposure.getHcAmount(), exposure.getFcValue()));
/* 446 */       exposure.setHfx(RwaMath.div(exposure.getHfxAmount(), exposure.getFcValue()));
/*     */     } 
/*     */     
/* 449 */     exposure.setMitigatedEa(RwaMath.getMitigatedEa(exposure.getAssetBalance(), exposure.getHe(), exposure.getFcValue(), exposure.getFcHaircut(), exposure.getHfx()));
/* 450 */     exposure.setRwa(RwaMath.getRwa(this.unionDto.getApproach(), exposure.getMitigatedEa(), exposure.getRw(), exposure.getKcr()));
/* 451 */     return exposure;
/*     */   }
/*     */ 
/*     */   
/*     */   public SftExposureDto noMitigationCalculate(SftExposureDto exposure, Identity isTouchLine) {
/* 456 */     exposure.setIsTouchLine(isTouchLine.getCode());
/* 457 */     exposure.setMitigatedEa(RwaMath.adjustExposure(exposure.getAssetBalance(), exposure.getHe()));
/* 458 */     exposure.setRwa(RwaMath.getRwa(this.unionDto.getApproach(), exposure.getMitigatedEa(), exposure.getRw(), exposure.getKcr()));
/* 459 */     return exposure;
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
/*     */   public Identity confirmHaircutLineAdjustFlag(SftExposureDto exposureDto, int nonGovBondCount) {
/* 471 */     if (!this.creditRuleDo.isEnSftFactorLine()) {
/* 472 */       return Identity.NO;
/*     */     }
/*     */     
/* 475 */     if (StrUtil.equals(exposureDto.getCentralClearFlag(), Identity.YES.getCode())) {
/* 476 */       return Identity.NO;
/*     */     }
/*     */     
/* 479 */     if (nonGovBondCount > 0 && RwaUtils.isNonBank(exposureDto.getClientType())) {
/* 480 */       return Identity.YES;
/*     */     }
/* 482 */     return Identity.NO;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\calculation\SftCalculation.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */