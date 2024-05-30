/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*     */ 
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.OptionType;
/*     */ import com.amarsoft.rwa.engine.constant.TradingDirection;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.FormulaDto;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.apache.commons.math3.distribution.NormalDistribution;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RwaMath
/*     */ {
/*  26 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.RwaMath.class);
/*     */ 
/*     */   
/*  29 */   public static BigDecimal amountZeroCompare = NumberUtil.toBigDecimal("0.01");
/*  30 */   public static BigDecimal ratioZeroCompare = NumberUtil.toBigDecimal("0.000001");
/*     */   public static final int DEFAULT_DIV_SCALE = 16;
/*     */   public static final int DEFAULT_ROUND_SCALE = 2;
/*     */   public static final int DEFAULT_MITIGATE_SCALE = 2;
/*  34 */   public static BigDecimal kFactor = NumberUtil.toBigDecimal("12.5");
/*  35 */   public static BigDecimal maxRw = NumberUtil.toBigDecimal("12.5");
/*     */ 
/*     */ 
/*     */   
/*  39 */   private static NormalDistribution normalDistribution = new NormalDistribution();
/*     */   
/*     */   public static BigDecimal nvl(BigDecimal value, @NotNull BigDecimal defaultValue) {
/*  42 */     if (value == null) {
/*  43 */       return defaultValue;
/*     */     }
/*  45 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int nvl(Integer value, @NotNull int defaultValue) {
/*  50 */     if (value == null) {
/*  51 */       return defaultValue;
/*     */     }
/*  53 */     return value.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal add(Number v1, Number v2) {
/*  58 */     return NumberUtil.add(v1, v2);
/*     */   }
/*     */   
/*     */   public static BigDecimal addByAbs(Number v1, Number v2) {
/*  62 */     v1 = (v1 == null) ? Integer.valueOf(0) : v1;
/*  63 */     v2 = (v2 == null) ? Integer.valueOf(0) : v2;
/*  64 */     if (v1.doubleValue() < 0.0D) {
/*  65 */       v1 = abs(v1);
/*     */     }
/*  67 */     if (v2.doubleValue() < 0.0D) {
/*  68 */       v2 = abs(v2);
/*     */     }
/*  70 */     return add(v1, v2);
/*     */   }
/*     */   
/*     */   public static BigDecimal add(Number... values) {
/*  74 */     return NumberUtil.add(values);
/*     */   }
/*     */   
/*     */   public static BigDecimal sub(Number v1, Number v2) {
/*  78 */     return NumberUtil.sub(v1, v2);
/*     */   }
/*     */   
/*     */   public static BigDecimal sub(Number... values) {
/*  82 */     return NumberUtil.sub(values);
/*     */   }
/*     */   
/*     */   public static BigDecimal mul(Number... values) {
/*  86 */     return NumberUtil.mul(values);
/*     */   }
/*     */   
/*     */   public static BigDecimal mul(Number v1, Number v2) {
/*  90 */     return NumberUtil.mul(v1, v2);
/*     */   }
/*     */   
/*     */   public static BigDecimal div(Number v1, Number v2) {
/*  94 */     return NumberUtil.div(v1, v2, 16);
/*     */   }
/*     */   
/*     */   public static BigDecimal div(Number v1, Number v2, int scale) {
/*  98 */     return NumberUtil.div(v1, v2, scale);
/*     */   }
/*     */   
/*     */   public static BigDecimal abs(@NotNull Number v) {
/* 102 */     if (v.doubleValue() < 0.0D) {
/* 103 */       return mul(Integer.valueOf(-1), v);
/*     */     }
/* 105 */     return NumberUtil.toBigDecimal(v);
/*     */   }
/*     */   
/*     */   public static BigDecimal round(BigDecimal value) {
/* 109 */     return NumberUtil.round(value, 2);
/*     */   }
/*     */   
/*     */   public static BigDecimal round(BigDecimal value, int scale) {
/* 113 */     return NumberUtil.round(value, scale);
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isZero(BigDecimal amt) {
/* 118 */     return (amt != null && NumberUtil.isLess(amt, ratioZeroCompare));
/*     */   }
/*     */   
/*     */   public static boolean isZeroOrNull(BigDecimal b) {
/* 122 */     return (b == null || b.compareTo(BigDecimal.ZERO) == 0);
/*     */   }
/*     */   
/*     */   public static boolean isNullOrNegative(BigDecimal b) {
/* 126 */     return (b == null || NumberUtil.isLess(b, BigDecimal.ZERO));
/*     */   }
/*     */   
/*     */   public static boolean isNegative(BigDecimal b) {
/* 130 */     return (b != null && NumberUtil.isLess(b, BigDecimal.ZERO));
/*     */   }
/*     */   
/*     */   public static boolean isPositive(BigDecimal b) {
/* 134 */     return (b != null && NumberUtil.isGreater(b, BigDecimal.ZERO));
/*     */   }
/*     */   
/*     */   public static boolean isRatioZero(BigDecimal amt) {
/* 138 */     return NumberUtil.isLess(amt, ratioZeroCompare);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double normal(BigDecimal x) {
/* 148 */     return normalDistribution.cumulativeProbability(x.doubleValue());
/*     */   }
/*     */   
/*     */   public static double normal(double x) {
/* 152 */     return normalDistribution.cumulativeProbability(x);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static double normalInverse(BigDecimal x) {
/* 162 */     return normalDistribution.inverseCumulativeProbability(x.doubleValue());
/*     */   }
/*     */   
/*     */   public static double normalInverse(double x) {
/* 166 */     return normalDistribution.inverseCumulativeProbability(x);
/*     */   }
/*     */   
/*     */   public static String getFormulaKey(FormulaDto formula, BigDecimal... params) {
/* 170 */     if (formula == null) {
/* 171 */       throw new ParamConfigException("计算公式为空！");
/*     */     }
/* 173 */     StringBuilder sb = new StringBuilder();
/* 174 */     sb.append(formula.getFormulaNo());
/* 175 */     for (BigDecimal param : params) {
/* 176 */       sb.append(":").append(getRoundName(param, 6));
/*     */     }
/* 178 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static String getRoundName(BigDecimal bd, int scale) {
/* 182 */     if (bd == null) {
/* 183 */       return null;
/*     */     }
/* 185 */     return round(bd, scale).stripTrailingZeros().toPlainString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getScva(Number a, BigDecimal rw, BigDecimal ead) {
/* 190 */     return mul(new Number[] { div(Integer.valueOf(1), a), rw, ead });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getCva(Number x, BigDecimal p, BigDecimal scva1, BigDecimal scva2) {
/* 197 */     return mul(new Number[] { Double.valueOf(12.5D), x, Double.valueOf(Math.sqrt(Math.pow(p.doubleValue() * scva1.doubleValue(), 2.0D) + (1.0D - Math.pow(p.doubleValue(), 2.0D)) * scva2.doubleValue())) });
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
/*     */   public static BigDecimal getHt(BigDecimal erm, BigDecimal mrm, BigDecimal mom) {
/* 212 */     if (erm == null || isZero(erm))
/*     */     {
/* 214 */       return BigDecimal.ONE;
/*     */     }
/* 216 */     BigDecimal ht = BigDecimal.ONE;
/* 217 */     if (erm.compareTo(mrm) > 0) {
/* 218 */       if (mom == null || mom.compareTo(BigDecimal.ONE) < 0)
/*     */       {
/* 220 */         return BigDecimal.valueOf(-1L); } 
/* 221 */       if (mrm.compareTo(BigDecimal.valueOf(0.25D)) < 0)
/*     */       {
/* 223 */         return BigDecimal.valueOf(-2L);
/*     */       }
/* 225 */       BigDecimal T = NumberUtil.min(new BigDecimal[] { erm, BigDecimal.valueOf(5L) });
/* 226 */       BigDecimal t = NumberUtil.min(new BigDecimal[] { mrm, T });
/*     */       
/* 228 */       return div(sub(t, BigDecimal.valueOf(0.25D)), sub(T, BigDecimal.valueOf(0.25D)));
/*     */     } 
/*     */     
/* 231 */     return ht;
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
/*     */   public static BigDecimal adjustHaircut(BigDecimal haircut, Integer frequency, Integer tm) {
/* 243 */     if (frequency == null) {
/* 244 */       frequency = Integer.valueOf(1);
/*     */     }
/* 246 */     if (tm == null) {
/* 247 */       tm = Integer.valueOf(20);
/*     */     }
/*     */     
/* 250 */     return mul(haircut, Double.valueOf(Math.sqrt(div(add(new Number[] { frequency, tm, Integer.valueOf(-1) }, ), Integer.valueOf(10)).doubleValue())));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal adjustExposure(@NotNull BigDecimal e, BigDecimal he) {
/* 255 */     return mul(e, adjustExposureHaircut(he));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal reverseAdjustExposure(@NotNull BigDecimal e, BigDecimal he) {
/* 260 */     return div(e, adjustExposureHaircut(he));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal adjustMitigation(@NotNull BigDecimal m, BigDecimal hc, BigDecimal hfx) {
/* 265 */     return mul(m, adjustMitigationHaircut(hc, hfx));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal reverseAdjustMitigation(@NotNull BigDecimal m, BigDecimal hc, BigDecimal hfx) {
/* 270 */     return div(m, adjustMitigationHaircut(hc, hfx));
/*     */   }
/*     */   
/*     */   public static BigDecimal adjustExposureHaircut(BigDecimal he) {
/* 274 */     return add(Integer.valueOf(1), getHaircut(he));
/*     */   }
/*     */   
/*     */   public static BigDecimal adjustMitigationHaircut(BigDecimal hc, BigDecimal hfx) {
/* 278 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(Integer.valueOf(1), add(getHaircut(hc), getHaircut(hfx))) });
/*     */   }
/*     */   
/*     */   public static BigDecimal getHaircut(BigDecimal haircut) {
/* 282 */     if (haircut == null || isZero(haircut)) {
/* 283 */       return BigDecimal.ZERO;
/*     */     }
/* 285 */     return haircut;
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
/*     */   public static BigDecimal getMitigateAmount(@NotNull BigDecimal e, BigDecimal he, @NotNull BigDecimal c, BigDecimal hc, BigDecimal hfx) {
/* 302 */     BigDecimal ce = NumberUtil.min(new BigDecimal[] { adjustExposure(e, he), adjustMitigation(c, hc, hfx) });
/*     */     
/* 304 */     return reverseAdjustMitigation(ce, hc, hfx);
/*     */   }
/*     */   
/*     */   public static BigDecimal checkHt(BigDecimal ht) {
/* 308 */     if (ht == null) {
/* 309 */       ht = BigDecimal.ONE;
/*     */     }
/* 311 */     return ht;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getMitigateAmount(BigDecimal e, BigDecimal c, BigDecimal hfx, BigDecimal ht) {
/* 316 */     BigDecimal h = sub(Integer.valueOf(1), getHaircut(hfx));
/* 317 */     ht = checkHt(ht);
/*     */     
/* 319 */     BigDecimal ca = NumberUtil.min(new BigDecimal[] { e, mul(new Number[] { c, h, ht }) });
/* 320 */     return div(div(ca, h), ht);
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigateCovered(BigDecimal m, BigDecimal hfx, BigDecimal ht) {
/* 324 */     return mul(new Number[] { m, adjustMitigationHaircut(null, hfx), checkHt(ht) });
/*     */   }
/*     */   
/*     */   public static BigDecimal getCdgCovered(BigDecimal m, BigDecimal hcd, BigDecimal hfx, BigDecimal ht) {
/* 328 */     return mul(new Number[] { m, adjustMitigationHaircut(hcd, null), adjustMitigationHaircut(null, hfx), checkHt(ht) });
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigateEadOfB2(@NotNull BigDecimal e, BigDecimal he, @NotNull BigDecimal c, BigDecimal hc, BigDecimal hfx) {
/* 332 */     BigDecimal ead = NumberUtil.sub(adjustExposure(e, he), adjustMitigation(c, hc, hfx));
/* 333 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, ead });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal reverseMitigateByFinancialCollateral(@NotNull BigDecimal em, @NotNull BigDecimal e, BigDecimal he, BigDecimal hc, BigDecimal hfx) {
/* 338 */     BigDecimal ce = NumberUtil.sub(adjustExposure(e, he), em);
/* 339 */     return reverseAdjustMitigation(ce, hc, hfx);
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
/*     */   public static BigDecimal getMitigateAmount(@NotNull BigDecimal e, @NotNull BigDecimal c, BigDecimal hfx) {
/* 354 */     BigDecimal h = sub(Integer.valueOf(1), getHaircut(hfx));
/*     */     
/* 356 */     BigDecimal ca = NumberUtil.min(new BigDecimal[] { e, mul(c, h) });
/* 357 */     return div(ca, h);
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigateAmountOfB2Coll(BigDecimal e, BigDecimal el, BigDecimal c, BigDecimal ocl) {
/* 361 */     if (isZeroOrNull(ocl)) {
/* 362 */       ocl = BigDecimal.ONE;
/*     */     }
/* 364 */     return NumberUtil.min(new BigDecimal[] { e, el, div(c, ocl) });
/*     */   }
/*     */   
/*     */   public static BigDecimal getCollateralCovered(BigDecimal m, BigDecimal hfx, BigDecimal ht) {
/* 368 */     if (isZeroOrNull(ht)) {
/* 369 */       ht = BigDecimal.ONE;
/*     */     }
/* 371 */     return mul(new Number[] { m, sub(Integer.valueOf(1), getHaircut(hfx)), ht });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getEadExistsNeg(BigDecimal e, BigDecimal ccf, BigDecimal p) {
/* 382 */     if (p == null) {
/* 383 */       p = BigDecimal.ZERO;
/*     */     }
/* 385 */     if (ccf == null) {
/* 386 */       return sub(e, p);
/*     */     }
/* 388 */     return sub(mul(e, ccf), p);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getEad(BigDecimal e, BigDecimal ccf, BigDecimal p) {
/* 394 */     if (ccf == null)
/* 395 */       return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(e, p) }); 
/* 396 */     if (isRatioZero(ccf))
/*     */     {
/* 398 */       return BigDecimal.ZERO;
/*     */     }
/* 400 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(mul(e, ccf), p) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEad(BigDecimal e, BigDecimal ccf) {
/* 405 */     return getEad(e, ccf, BigDecimal.ZERO);
/*     */   }
/*     */   
/*     */   public static BigDecimal getAbsEad(BigDecimal e, BigDecimal ccf, BigDecimal p) {
/* 409 */     BigDecimal ead = NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(e, p) });
/* 410 */     if (ccf == null) {
/* 411 */       return ead;
/*     */     }
/* 413 */     return mul(ead, ccf);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsProvisionDed(BigDecimal ead, BigDecimal e, BigDecimal p) {
/* 418 */     if (isZero(ead)) {
/* 419 */       return e;
/*     */     }
/* 421 */     return p;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getProvisionProp(BigDecimal balance, BigDecimal ccf, BigDecimal provision) {
/* 426 */     if (balance.compareTo(BigDecimal.ZERO) < 0) {
/* 427 */       return BigDecimal.ZERO;
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 434 */     if (isZero(provision))
/* 435 */       return BigDecimal.ZERO; 
/* 436 */     if (isZero(balance)) {
/* 437 */       return BigDecimal.ONE;
/*     */     }
/* 439 */     return NumberUtil.min(new BigDecimal[] { div(provision, balance), BigDecimal.ONE });
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getProvisionDed(BigDecimal balance, BigDecimal ccf, BigDecimal provision) {
/* 445 */     if (isNegative(balance) || isZero(balance) || isZero(provision)) {
/* 446 */       return BigDecimal.ZERO;
/*     */     }
/* 448 */     if (ccf == null) {
/* 449 */       ccf = BigDecimal.ONE;
/*     */     }
/*     */     
/* 452 */     if (isZero(ccf)) {
/* 453 */       return BigDecimal.ZERO;
/*     */     }
/*     */     
/* 456 */     BigDecimal ead = getEad(balance, ccf, provision);
/* 457 */     return sub(mul(balance, ccf), ead);
/*     */   }
/*     */   
/*     */   public static BigDecimal getCcfConversion(BigDecimal ccf, BigDecimal b) {
/* 461 */     if (ccf == null) {
/* 462 */       return b;
/*     */     }
/* 464 */     return mul(ccf, b);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getRwa(BigDecimal ead, BigDecimal k) {
/* 469 */     return mul(new Number[] { ead, k, kFactor });
/*     */   }
/*     */   
/*     */   public static BigDecimal getRwa(ExposureApproach approach, BigDecimal ead, BigDecimal rw, BigDecimal k) {
/* 473 */     if (approach == ExposureApproach.WA || k == null) {
/* 474 */       return mul(ead, rw);
/*     */     }
/* 476 */     return mul(new Number[] { ead, k, kFactor });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getWarw(BigDecimal ead, BigDecimal rwaMb, BigDecimal rwaMa, BigDecimal rw) {
/* 481 */     if (rwaMb == null || rwaMa == null || ead == null || rwaMb.compareTo(rwaMa) == 0)
/* 482 */       return rw; 
/* 483 */     if (isZero(ead) || isZero(rw)) {
/* 484 */       return rw;
/*     */     }
/* 486 */     return round(div(rwaMa, ead), 16);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEl(String defaultFlag, BigDecimal beel, BigDecimal pd, BigDecimal lgd) {
/* 491 */     if (StrUtil.equals(Identity.YES.getCode(), defaultFlag)) {
/* 492 */       return beel;
/*     */     }
/* 494 */     return mul(pd, lgd);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal adjustParam(BigDecimal source, BigDecimal adjustProportion) {
/* 499 */     BigDecimal after = NumberUtil.mul(source, NumberUtil.add(BigDecimal.ONE, adjustProportion));
/* 500 */     if (isZero(after)) {
/* 501 */       after = BigDecimal.ZERO;
/*     */     }
/* 503 */     return after;
/*     */   }
/*     */   
/*     */   public static boolean isCurrencyMismatch(String exposureCurrency, String mitigationCurrency) {
/* 507 */     return !StrUtil.equals(exposureCurrency, mitigationCurrency);
/*     */   }
/*     */   
/*     */   public static BigDecimal getHfx(String exposureCurrency, String mitigationCurrency, Integer frequency, Integer tm, BigDecimal shfx) {
/* 511 */     if (isCurrencyMismatch(exposureCurrency, mitigationCurrency)) {
/* 512 */       return adjustHaircut(shfx, frequency, tm);
/*     */     }
/* 514 */     return BigDecimal.ZERO;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEffectiveMaturity(BigDecimal m, Integer tm) {
/* 519 */     return getEffectiveMaturity(m.doubleValue(), tm.intValue());
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getEffectiveMaturity(double m, int tm) {
/* 527 */     return NumberUtil.toBigDecimal(Double.valueOf(NumberUtil.max(new double[] { m, tm })));
/*     */   }
/*     */   
/*     */   public static BigDecimal getDiscountFactor(BigDecimal m, Integer tm) {
/* 531 */     return getDiscountFactor(m.doubleValue(), tm.intValue());
/*     */   }
/*     */   
/*     */   public static BigDecimal getDiscountFactor(double m, int tm) {
/* 535 */     if (m < tm) {
/* 536 */       m = tm;
/*     */     }
/*     */ 
/*     */     
/* 540 */     return NumberUtil.toBigDecimal(Double.valueOf((1.0D - Math.exp(-0.05D * m / 250.0D)) / 0.05D * m / 250.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getNmtMf(int m, int mhp) {
/* 549 */     if (m < mhp) {
/* 550 */       m = mhp;
/*     */     }
/*     */ 
/*     */     
/* 554 */     return NumberUtil.toBigDecimal(Double.valueOf(Math.sqrt(div(Integer.valueOf(Math.min(m, 250)), Integer.valueOf(250)).doubleValue())));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getMtMf(int mpor) {
/* 564 */     return NumberUtil.toBigDecimal(Double.valueOf(Math.sqrt(mpor / 250.0D) * 3.0D / 2.0D));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getSupervisionDuration(int s, int e, int mhp) {
/* 574 */     if (s > 0 && s < mhp) {
/* 575 */       s = mhp;
/*     */     }
/* 577 */     if (e < mhp) {
/* 578 */       e = mhp;
/*     */     }
/*     */     
/* 581 */     return NumberUtil.toBigDecimal(Double.valueOf((Math.exp(-0.05D * s / 250.0D) - Math.exp(-0.05D * e / 250.0D)) / 0.05D));
/*     */   }
/*     */   
/*     */   public static BigDecimal getSupervisionDuration(BigDecimal s, BigDecimal e, Integer tm) {
/* 585 */     return getSupervisionDuration(s.intValue(), e.intValue(), tm.intValue());
/*     */   }
/*     */   
/*     */   public static BigDecimal getDelta(TradingDirection tradingDirection) {
/* 589 */     if (tradingDirection == TradingDirection.BUY) {
/* 590 */       return BigDecimal.ONE;
/*     */     }
/* 592 */     return BigDecimal.valueOf(-1L);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getDeltaOfCdo(TradingDirection tradingDirection, BigDecimal a, BigDecimal d) {
/* 597 */     Double delta = Double.valueOf(15.0D / (1.0D + 14.0D * a.doubleValue()) / (1.0D + 14.0D * d.doubleValue()));
/* 598 */     if (tradingDirection == TradingDirection.SELL) {
/* 599 */       delta = Double.valueOf(-delta.doubleValue());
/*     */     }
/* 601 */     return NumberUtil.toBigDecimal(delta);
/*     */   }
/*     */   
/*     */   public static BigDecimal getDefaultDeltaOfOption(TradingDirection tradingDirection, OptionType optionType) {
/* 605 */     if (optionType == OptionType.CALL && tradingDirection == TradingDirection.SELL)
/*     */     {
/* 607 */       return BigDecimal.valueOf(-1L); } 
/* 608 */     if (optionType == OptionType.PUT && tradingDirection == TradingDirection.BUY)
/*     */     {
/* 610 */       return BigDecimal.valueOf(-1L);
/*     */     }
/* 612 */     return BigDecimal.ONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getDeltaOfOption(TradingDirection tradingDirection, TradingDirection confirmDirection, OptionType optionType, BigDecimal p, BigDecimal k, BigDecimal t, BigDecimal s) {
/* 617 */     if (isNullOrNegative(p) || isZeroOrNull(p) || isNullOrNegative(k) || isZeroOrNull(k) || 
/* 618 */       isNullOrNegative(t) || isZeroOrNull(t) || isNullOrNegative(s) || isZeroOrNull(s)) {
/* 619 */       throw new RuntimeException("数据异常");
/*     */     }
/* 621 */     t = div(t, Integer.valueOf(250));
/*     */     
/* 623 */     if (tradingDirection == confirmDirection) {
/* 624 */       return getDeltaOfOption(tradingDirection, optionType, p, k, t, s);
/*     */     }
/*     */     
/* 627 */     if (optionType == OptionType.CALL) {
/* 628 */       return getDeltaOfOption(tradingDirection, OptionType.PUT, k, p, t, s);
/*     */     }
/* 630 */     return getDeltaOfOption(tradingDirection, OptionType.CALL, k, p, t, s);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getDeltaOfOption(TradingDirection tradingDirection, OptionType optionType, BigDecimal p, BigDecimal k, BigDecimal t, BigDecimal s) {
/* 635 */     Double delta = Double.valueOf(0.0D);
/* 636 */     if (optionType == OptionType.CALL) {
/*     */       
/* 638 */       delta = Double.valueOf(normal((Math.log(div(p, k).doubleValue()) + 0.5D * Math.pow(s.doubleValue(), 2.0D) * t.doubleValue()) / s.doubleValue() * Math.sqrt(t.doubleValue())));
/*     */     } else {
/*     */       
/* 641 */       delta = Double.valueOf(-normal(-(Math.log(div(p, k).doubleValue()) + 0.5D * Math.pow(s.doubleValue(), 2.0D) * t.doubleValue()) / s.doubleValue() * Math.sqrt(t.doubleValue())));
/*     */     } 
/* 643 */     if (tradingDirection == TradingDirection.SELL) {
/* 644 */       delta = Double.valueOf(-delta.doubleValue());
/*     */     }
/* 646 */     return NumberUtil.toBigDecimal(delta);
/*     */   }
/*     */   
/*     */   public static BigDecimal getEffectiveNotional(BigDecimal delta, BigDecimal d, BigDecimal mf) {
/* 650 */     return mul(new Number[] { delta, d, mf });
/*     */   }
/*     */   
/*     */   public static BigDecimal getIrEffectiveNotional(BigDecimal d1, BigDecimal d2, BigDecimal d3) {
/* 654 */     d1 = nvl(d1, BigDecimal.ZERO);
/* 655 */     d2 = nvl(d2, BigDecimal.ZERO);
/* 656 */     d3 = nvl(d3, BigDecimal.ZERO);
/*     */     
/* 658 */     return NumberUtil.toBigDecimal(Double.valueOf(Math.sqrt(add(new Number[] { NumberUtil.pow(d1, 2), NumberUtil.pow(d2, 2), NumberUtil.pow(d3, 2), 
/* 659 */                 mul(new Number[] { Double.valueOf(1.4D), d1, d2 }), mul(new Number[] { Double.valueOf(1.4D), d2, d3 }), mul(new Number[] { Double.valueOf(0.6D), d1, d3 }) }).doubleValue())));
/*     */   }
/*     */   
/*     */   public static BigDecimal getAddOn(BigDecimal effectiveNotional, BigDecimal sf) {
/* 663 */     return mul(effectiveNotional, sf);
/*     */   }
/*     */   
/*     */   public static BigDecimal getStatisticsAddOn(BigDecimal addOn1, BigDecimal addOn2) {
/* 667 */     addOn1 = nvl(addOn1, BigDecimal.ZERO);
/* 668 */     addOn2 = nvl(addOn2, BigDecimal.ZERO);
/*     */     
/* 670 */     return NumberUtil.toBigDecimal(Double.valueOf(Math.sqrt(Math.pow(addOn1.doubleValue(), 2.0D) + addOn2.doubleValue())));
/*     */   }
/*     */   
/*     */   public static BigDecimal getNmtRc(BigDecimal v, BigDecimal c) {
/* 674 */     v = nvl(v, BigDecimal.ZERO);
/* 675 */     c = nvl(c, BigDecimal.ZERO);
/* 676 */     return NumberUtil.max(new BigDecimal[] { sub(v, c), BigDecimal.ZERO });
/*     */   }
/*     */   
/*     */   public static BigDecimal getMtRc(BigDecimal v, BigDecimal c, BigDecimal b) {
/* 680 */     v = nvl(v, BigDecimal.ZERO);
/* 681 */     c = nvl(c, BigDecimal.ZERO);
/* 682 */     b = nvl(b, BigDecimal.ZERO);
/* 683 */     return NumberUtil.max(new BigDecimal[] { sub(v, c), BigDecimal.ZERO, b });
/*     */   }
/*     */   
/*     */   public static BigDecimal getBmrMaxEad(BigDecimal th, BigDecimal mta, BigDecimal nica) {
/* 687 */     th = nvl(th, BigDecimal.ZERO);
/* 688 */     mta = nvl(mta, BigDecimal.ZERO);
/* 689 */     nica = nvl(nica, BigDecimal.ZERO);
/* 690 */     return sub(add(th, mta), nica);
/*     */   }
/*     */   
/*     */   public static BigDecimal getMultiplier(BigDecimal v, BigDecimal c, BigDecimal addOn) {
/* 694 */     if (addOn.compareTo(BigDecimal.ZERO) == 0) {
/* 695 */       return BigDecimal.ZERO;
/*     */     }
/* 697 */     v = nvl(v, BigDecimal.ZERO);
/* 698 */     c = nvl(c, BigDecimal.ZERO);
/*     */ 
/*     */     
/* 701 */     return NumberUtil.toBigDecimal(Double.valueOf(Math.min(1.0D, 0.05D + 0.95D * Math.exp((v.doubleValue() - c.doubleValue()) / 1.9D * Math.abs(addOn.doubleValue())))));
/*     */   }
/*     */   
/*     */   public static BigDecimal getPfe(BigDecimal multiplier, BigDecimal addOn) {
/* 705 */     return mul(multiplier, abs(Double.valueOf(addOn.doubleValue())));
/*     */   }
/*     */   
/*     */   public static BigDecimal getEadOfDi(BigDecimal rc, BigDecimal pfe) {
/* 709 */     return mul(Double.valueOf(1.4D), add(NumberUtil.max(new BigDecimal[] { rc, BigDecimal.ZERO }, ), pfe));
/*     */   }
/*     */   
/*     */   public static BigDecimal getEadOfDiCem(BigDecimal mtm, BigDecimal addOn) {
/* 713 */     return add(NumberUtil.max(new BigDecimal[] { mtm, BigDecimal.ZERO }, ), addOn);
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigatedEad(BigDecimal e, BigDecimal c) {
/* 717 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(e, c) });
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getNgr(BigDecimal nrc, BigDecimal grc) {
/* 728 */     if (isNullOrNegative(nrc) || isNullOrNegative(grc)) {
/* 729 */       return BigDecimal.ONE;
/*     */     }
/* 731 */     if (isZero(grc)) {
/* 732 */       return BigDecimal.ONE;
/*     */     }
/* 734 */     return div(nrc, grc);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getAnet(BigDecimal a, BigDecimal ngr) {
/* 745 */     return add(mul(NumberUtil.toBigDecimal("0.4"), a), mul(new Number[] { NumberUtil.toBigDecimal("0.6"), ngr, a }));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getNettingEad(BigDecimal nrc, BigDecimal anet) {
/* 755 */     return add(NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, nrc }, ), anet);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAddOnP1(BigDecimal addOn, BigDecimal p) {
/* 760 */     return mul(addOn, p);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAddOnP2(BigDecimal addOn, BigDecimal p) {
/* 765 */     return mul(sub(Integer.valueOf(1), NumberUtil.pow(p, 2)), NumberUtil.pow(addOn, 2));
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getEffectiveFactor(BigDecimal fe, BigDecimal fc) {
/* 771 */     return sub(div(fc, fe), Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   public static BigDecimal getEffectiveHaircut(BigDecimal e, BigDecimal c) {
/* 775 */     return sub(div(c, e), Integer.valueOf(1));
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigatedEa(BigDecimal e, BigDecimal he, BigDecimal c, BigDecimal hc, BigDecimal hfx) {
/* 779 */     BigDecimal ea = sub(adjustExposure(e, he), adjustMitigation(c, hc, hfx));
/* 780 */     if (isZero(ea)) {
/* 781 */       return BigDecimal.ZERO;
/*     */     }
/*     */     
/* 784 */     return NumberUtil.min(new BigDecimal[] { e, ea });
/*     */   }
/*     */   
/*     */   public static BigDecimal getMitigatedEa(BigDecimal e, BigDecimal c, BigDecimal netExposure, BigDecimal grossExposure, int n, BigDecimal hfxAmt) {
/* 788 */     BigDecimal ea = add(new Number[] { e, mul(Integer.valueOf(-1), c), 
/* 789 */           mul(NumberUtil.toBigDecimal("0.4"), netExposure), 
/* 790 */           mul(new Number[] { NumberUtil.toBigDecimal("0.6"), grossExposure, Double.valueOf(1.0D / Math.sqrt(n)) }), hfxAmt });
/* 791 */     if (isZero(ea)) {
/* 792 */       return BigDecimal.ZERO;
/*     */     }
/*     */     
/* 795 */     return NumberUtil.min(new BigDecimal[] { e, ea });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getTrancheMaturity(BigDecimal m) {
/* 800 */     if (m == null || m.compareTo(BigDecimal.ONE) <= 0) {
/* 801 */       return BigDecimal.ONE;
/*     */     }
/*     */     
/* 804 */     return NumberUtil.min(new BigDecimal[] { add(BigDecimal.ONE, mul(sub(m, BigDecimal.ONE), NumberUtil.toBigDecimal("0.8"))), BigDecimal.valueOf(5L) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsT(BigDecimal d, BigDecimal a) {
/* 809 */     return sub(d, a);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsKa(BigDecimal ksa, BigDecimal w) {
/* 814 */     return add(mul(sub(Integer.valueOf(1), w), ksa), mul(w, NumberUtil.toBigDecimal("0.5")));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal adjustAbsKa(BigDecimal ka, BigDecimal eadTotal, BigDecimal eadOde, BigDecimal eadUnke) {
/* 819 */     return add(mul(div(eadOde, eadTotal), ka), div(eadUnke, eadTotal));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsA(BigDecimal k, BigDecimal l, BigDecimal p) {
/* 824 */     return mul(Integer.valueOf(-1), div(l, mul(p, k)));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsL(BigDecimal a, BigDecimal k) {
/* 829 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(a, k) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsU(BigDecimal d, BigDecimal k) {
/* 834 */     return sub(d, k);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsKssfa(BigDecimal a, BigDecimal u, BigDecimal l) {
/* 840 */     BigDecimal d = mul(a, sub(u, l));
/* 841 */     if (NumberUtil.equals(d, BigDecimal.ZERO)) {
/* 842 */       return BigDecimal.ONE;
/*     */     }
/* 844 */     return div(Double.valueOf(Math.exp(mul(a, u).doubleValue()) - Math.exp(mul(a, l).doubleValue())), d);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsRw(BigDecimal d, BigDecimal a, BigDecimal k, BigDecimal kssfa) {
/* 849 */     if (NumberUtil.isGreaterOrEqual(a, k)) {
/* 850 */       return mul(kssfa, kFactor);
/*     */     }
/*     */     
/* 853 */     return add(mul(div(sub(k, a), sub(d, a)), kFactor), mul(new Number[] { div(sub(d, k), sub(d, a)), kFactor, kssfa }));
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsKirb(BigDecimal rwa, BigDecimal ela, BigDecimal ead) {
/* 858 */     return div(add(div(rwa, kFactor), ela), ead);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal adjustAbsKirb(BigDecimal kirb, BigDecimal ksa, BigDecimal d) {
/* 863 */     return add(mul(d, kirb), mul(sub(Integer.valueOf(1), d), ksa));
/*     */   }
/*     */   
/*     */   public static BigDecimal getAbsSaSfp(String isComplianceStc) {
/* 867 */     if (StrUtil.equals(isComplianceStc, Identity.YES.getCode())) {
/* 868 */       return NumberUtil.toBigDecimal("0.5");
/*     */     }
/* 870 */     return BigDecimal.ONE;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getAbsIrbSfp(BigDecimal kirb, BigDecimal n, BigDecimal lgd, BigDecimal m, String isStc, BigDecimal a, BigDecimal b, BigDecimal c, BigDecimal d, BigDecimal e) {
/* 875 */     BigDecimal x = BigDecimal.ONE;
/* 876 */     if (StrUtil.equals(isStc, Identity.YES.getCode())) {
/* 877 */       x = NumberUtil.toBigDecimal("0.5");
/*     */     }
/*     */     
/* 880 */     return NumberUtil.max(new BigDecimal[] { NumberUtil.toBigDecimal("0.3"), 
/* 881 */           mul(x, add(new Number[] {
/* 882 */                 a, div(b, n), mul(c, kirb), mul(d, lgd), mul(e, m)
/*     */               })) });
/*     */   }
/*     */   
/*     */   public static BigDecimal getAbsProductRwaLimit(BigDecimal rwa, BigDecimal ela) {
/* 887 */     return add(rwa, mul(ela, kFactor));
/*     */   }
/*     */   
/*     */   public static BigDecimal k2rw(BigDecimal k) {
/* 891 */     return mul(k, kFactor);
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEcDf(List<EcFactorDo> factorDoList) {
/* 896 */     if (StrUtil.equals(RwaConfig.ecCalculationMode, "1")) {
/* 897 */       return getEcDfByMul(factorDoList);
/*     */     }
/* 899 */     return getEcDfByAdd(factorDoList);
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static BigDecimal getEcDfByAdd(List<EcFactorDo> factorDoList) {
/* 905 */     BigDecimal overlayFactor = BigDecimal.ZERO;
/*     */     
/* 907 */     BigDecimal maxFactor = null;
/* 908 */     for (EcFactorDo ecFactorDo : factorDoList) {
/*     */       
/* 910 */       if (NumberUtil.isGreaterOrEqual(ecFactorDo.getEcAdjFactor(), BigDecimal.ONE)) {
/* 911 */         return BigDecimal.ZERO;
/*     */       }
/*     */       
/* 914 */       if (StrUtil.equals(ecFactorDo.getIsAllowOverlay(), Identity.YES.getCode())) {
/*     */         
/* 916 */         overlayFactor = add(overlayFactor, ecFactorDo.getEcAdjFactor());
/*     */         continue;
/*     */       } 
/* 919 */       maxFactor = (maxFactor == null) ? ecFactorDo.getEcAdjFactor() : NumberUtil.max(new BigDecimal[] { maxFactor, ecFactorDo.getEcAdjFactor() });
/*     */     } 
/*     */ 
/*     */     
/* 923 */     if (maxFactor != null) {
/* 924 */       overlayFactor = add(overlayFactor, maxFactor);
/*     */     }
/*     */     
/* 927 */     return NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, sub(Integer.valueOf(1), overlayFactor) });
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEcDfByMul(List<EcFactorDo> factorDoList) {
/* 932 */     BigDecimal overlayFactor = BigDecimal.ONE;
/*     */     
/* 934 */     BigDecimal maxFactor = null;
/* 935 */     for (EcFactorDo ecFactorDo : factorDoList) {
/*     */       
/* 937 */       if (NumberUtil.isGreaterOrEqual(ecFactorDo.getEcAdjFactor(), BigDecimal.ONE)) {
/* 938 */         return BigDecimal.ZERO;
/*     */       }
/*     */       
/* 941 */       if (StrUtil.equals(ecFactorDo.getIsAllowOverlay(), Identity.YES.getCode())) {
/*     */         
/* 943 */         overlayFactor = mul(overlayFactor, sub(Integer.valueOf(1), ecFactorDo.getEcAdjFactor()));
/*     */         continue;
/*     */       } 
/* 946 */       maxFactor = (maxFactor == null) ? ecFactorDo.getEcAdjFactor() : NumberUtil.max(new BigDecimal[] { maxFactor, ecFactorDo.getEcAdjFactor() });
/*     */     } 
/*     */ 
/*     */     
/* 950 */     if (maxFactor != null) {
/* 951 */       overlayFactor = mul(overlayFactor, sub(Integer.valueOf(1), maxFactor));
/*     */     }
/* 953 */     return overlayFactor;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal getEc(BigDecimal rwa, BigDecimal ecDf, BigDecimal ecOf) {
/* 958 */     if (isNullOrNegative(ecDf)) {
/* 959 */       ecDf = BigDecimal.ONE;
/*     */     }
/*     */     
/* 962 */     if (isNullOrNegative(ecOf) || isZero(ecOf)) {
/* 963 */       ecOf = div(BigDecimal.ONE, kFactor);
/*     */     }
/* 965 */     if (rwa == null) {
/* 966 */       rwa = BigDecimal.ZERO;
/*     */     }
/* 968 */     return mul(new Number[] { rwa, ecDf, ecOf });
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\RwaMath.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */