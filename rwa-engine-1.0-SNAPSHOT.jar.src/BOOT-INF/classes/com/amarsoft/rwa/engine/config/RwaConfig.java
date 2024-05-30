/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.Approach;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationMainType;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationType;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.ColumnRuleDto;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.FormulaDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateAssetDo;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSchemeDo;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeAssetDo;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.time.DayOfWeek;
/*     */ import java.time.LocalDate;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class RwaConfig {
/*  31 */   public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
/*     */ 
/*     */ 
/*     */   
/*     */   public static String headOrgId;
/*     */ 
/*     */ 
/*     */   
/*     */   public static Map<String, String> holidayConfigMap;
/*     */ 
/*     */ 
/*     */   
/*  43 */   public static Map<String, SchemeConfigDo> schemeConfigDoMap = new ConcurrentHashMap<>();
/*     */ 
/*     */ 
/*     */   
/*     */   private static List<EcFactorDo> ecFactorList;
/*     */ 
/*     */ 
/*     */   
/*     */   private static Map<String, ColumnRuleDto> ecColumnRuleMap;
/*     */ 
/*     */   
/*     */   public static String rwLineAdjustMode;
/*     */ 
/*     */   
/*     */   public static String ecCalculationMode;
/*     */ 
/*     */   
/*     */   public static String waMitigateMode;
/*     */ 
/*     */   
/*     */   public static String rmbCurrencyCode;
/*     */ 
/*     */   
/*     */   public static boolean enableMappingExceptionOut;
/*     */ 
/*     */   
/*     */   public static boolean enablePriorityMitigateByProportional;
/*     */ 
/*     */   
/*     */   public static boolean enableConsolidatedSubWaCalculate;
/*     */ 
/*     */   
/*     */   public static boolean enableRealEstateLtvCalculate;
/*     */ 
/*     */   
/*     */   public static boolean enableRetailIrbProportional;
/*     */ 
/*     */   
/*     */   public static String diCvaAssetType;
/*     */ 
/*     */   
/*     */   public static String sftCvaAssetType;
/*     */ 
/*     */   
/*     */   public static String cvaIndustryId;
/*     */ 
/*     */   
/*     */   public static String cvaBusinessLine;
/*     */ 
/*     */ 
/*     */   
/*     */   public static void putSchemeConfig(String key, SchemeConfigDo schemeConfigDo) {
/*  95 */     schemeConfigDoMap.put(key, schemeConfigDo);
/*     */   }
/*     */   
/*     */   public static SchemeConfigDo getSchemeConfig(String schemeId) {
/*  99 */     return schemeConfigDoMap.get(schemeId);
/*     */   }
/*     */   
/*     */   public static BigDecimal confirmReLgdLowerLimit(SchemeConfigDo schemeConfig, String exposureType, BigDecimal lgd) {
/* 103 */     Map<String, BigDecimal> reLgdLimitMap = schemeConfig.getIrbParamVersion().getReLgdLimitMap();
/* 104 */     if (CollUtil.isEmpty(reLgdLimitMap)) {
/* 105 */       return lgd;
/*     */     }
/* 107 */     BigDecimal limit = reLgdLimitMap.get(exposureType);
/* 108 */     if (limit == null || NumberUtil.isGreaterOrEqual(lgd, limit)) {
/* 109 */       return lgd;
/*     */     }
/* 111 */     return limit;
/*     */   }
/*     */   
/*     */   public static BigDecimal confirmNrLgdLowerLimit(SchemeConfigDo schemeConfig, String exposureType, String provideMitigationType, BigDecimal lgd) {
/* 115 */     Map<String, BigDecimal> nrLgdLimitMap = schemeConfig.getIrbParamVersion().getNrLgdLimitMap();
/* 116 */     if (CollUtil.isEmpty(nrLgdLimitMap)) {
/* 117 */       return lgd;
/*     */     }
/* 119 */     BigDecimal limit = nrLgdLimitMap.get(DataUtils.generateKey(new String[] { exposureType, provideMitigationType }));
/* 120 */     if (limit == null || NumberUtil.isGreaterOrEqual(lgd, limit)) {
/* 121 */       return lgd;
/*     */     }
/* 123 */     return limit;
/*     */   }
/*     */   
/*     */   public static FormulaDto getFormula(SchemeConfigDo schemeConfig, ExposureApproach approach, String exposureType, Map<String, Object> exposure) {
/* 127 */     Map<String, Map<String, FormulaDto>> formulaExposureMap = schemeConfig.getIrbParamVersion().getFormulaExposureMap();
/* 128 */     Map<String, FormulaDto> map = formulaExposureMap.get(approach.getCode());
/*     */     
/* 130 */     FormulaDto formulaDto = map.get(exposureType);
/* 131 */     if (formulaDto != null) {
/* 132 */       return formulaDto;
/*     */     }
/*     */     
/* 135 */     if (StrUtil.equals(ExposureSubtype.BANKS.getCode(), exposureType)) {
/* 136 */       String sibFlag = DataUtils.getString(exposure, (ICodeEnum)RwaParam.SIB_FLAG);
/*     */       
/* 138 */       sibFlag = StrUtil.isEmpty(sibFlag) ? Identity.NO.getCode() : sibFlag;
/* 139 */       formulaDto = map.get(DataUtils.generateKey(new String[] { exposureType, sibFlag }));
/*     */     } 
/*     */     
/* 142 */     if (StrUtil.equals(ExposureSubtype.REL.getCode(), exposureType)) {
/* 143 */       String isVolatility = DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_VOLATILITY);
/*     */       
/* 145 */       isVolatility = StrUtil.isEmpty(isVolatility) ? Identity.NO.getCode() : isVolatility;
/* 146 */       formulaDto = map.get(DataUtils.generateKey(new String[] { exposureType, isVolatility }));
/*     */     } 
/* 148 */     if (formulaDto == null) {
/* 149 */       throw new ParamConfigException("计算公式为空， exposure=" + JsonUtils.object2Json(exposure));
/*     */     }
/* 151 */     return formulaDto;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static FormulaDto getFormulaById(SchemeConfigDo schemeConfig, String formulaNo) {
/* 161 */     return (FormulaDto)schemeConfig.getIrbParamVersion().getFormulaIdMap().get(formulaNo);
/*     */   }
/*     */   
/*     */   public static ExposureApproach getSchemeApproach(SchemeConfigDo schemeConfigDo, String assetType, String exposureTypeIrb) {
/* 165 */     if (schemeConfigDo.getSchemeAssetMap() == null) {
/* 166 */       return null;
/*     */     }
/* 168 */     SchemeAssetDo schemeAssetDo = (SchemeAssetDo)schemeConfigDo.getSchemeAssetMap().get(DataUtils.generateKey(new String[] { assetType, exposureTypeIrb }));
/* 169 */     if (schemeAssetDo == null) {
/* 170 */       schemeAssetDo = (SchemeAssetDo)schemeConfigDo.getSchemeAssetMap().get(assetType);
/*     */     }
/* 172 */     if (schemeAssetDo == null) {
/* 173 */       return null;
/*     */     }
/*     */     try {
/* 176 */       return (ExposureApproach)EnumUtils.getEnumByCode(schemeAssetDo.getApproach(), ExposureApproach.class);
/* 177 */     } catch (Exception e) {
/* 178 */       throw new ParamConfigException("方案资产方法配置异常！schemeAssetDo=" + JsonUtils.object2Json(schemeAssetDo), e);
/*     */     } 
/*     */   }
/*     */   
/*     */   private static MitigateAssetDo getMitigateAsset(MitigateSchemeDo mitigateSchemeDo, String assetType) {
/* 183 */     if (mitigateSchemeDo == null) {
/* 184 */       return null;
/*     */     }
/* 186 */     if (CollUtil.isEmpty(mitigateSchemeDo.getMitigateAssetDoMap())) {
/* 187 */       return null;
/*     */     }
/* 189 */     return (MitigateAssetDo)mitigateSchemeDo.getMitigateAssetDoMap().get(assetType);
/*     */   }
/*     */   
/*     */   public static boolean isWaApproach(String approach) {
/* 193 */     Approach wa = Approach.WA;
/* 194 */     if (StrUtil.equals(approach.substring(0, wa.getCode().length()), wa.getCode())) {
/* 195 */       return true;
/*     */     }
/* 197 */     return false;
/*     */   }
/*     */   
/*     */   public static MitigateAssetDo getMitigateAsset(SchemeConfigDo schemeConfig, String assetType, String approach) {
/* 201 */     if (isWaApproach(approach)) {
/* 202 */       return getMitigateAsset(schemeConfig.getWaMitigateScheme(), assetType);
/*     */     }
/* 204 */     return getMitigateAsset(schemeConfig.getIrbMitigateScheme(), assetType);
/*     */   }
/*     */ 
/*     */   
/*     */   public static MitigateAssetDo getDefaultMitigateAsset(SchemeConfigDo schemeConfig, String approach) {
/* 209 */     if (isWaApproach(approach)) {
/* 210 */       return schemeConfig.getWaMitigateScheme().getDefaultMitigateAsset();
/*     */     }
/* 212 */     return schemeConfig.getIrbMitigateScheme().getDefaultMitigateAsset();
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static MitigateAssetDo getMitigateAsset(Set<MitigateAssetDo> mitigateAssetDoSet, SchemeConfigDo schemeConfig, String approach) {
/* 218 */     if (CollUtil.isEmpty(mitigateAssetDoSet)) {
/* 219 */       return getDefaultMitigateAsset(schemeConfig, approach);
/*     */     }
/*     */     
/* 222 */     if (mitigateAssetDoSet.size() == 1) {
/* 223 */       return mitigateAssetDoSet.iterator().next();
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 229 */     if (enablePriorityMitigateByProportional) {
/* 230 */       for (MitigateAssetDo mitigateAssetDo : mitigateAssetDoSet) {
/* 231 */         if (mitigateAssetDo != null && StrUtil.equals(mitigateAssetDo.getMitigateMethod(), MitigateMethod.PROPORTIONAL.getCode())) {
/* 232 */           return mitigateAssetDo;
/*     */         }
/*     */       } 
/*     */     }
/*     */     
/* 237 */     return getDefaultMitigateAsset(schemeConfig, approach);
/*     */   }
/*     */   
/*     */   public static boolean isRwFirstOfMitigationSort(List<MitigateSortDo> sortList) {
/* 241 */     if (CollUtil.isEmpty(sortList)) {
/* 242 */       return true;
/*     */     }
/* 244 */     MitigateSortDo sortDo = sortList.get(0);
/* 245 */     if (StrUtil.equals(sortDo.getSortParam(), RwaParam.RW.getField()) && 
/* 246 */       StrUtil.equals(sortDo.getSortType(), SortType.DESC.getCode())) {
/* 247 */       return true;
/*     */     }
/* 249 */     return false;
/*     */   }
/*     */   
/*     */   public static String getExposureMainType(String exposureType) {
/* 253 */     if (StrUtil.isEmpty(exposureType)) {
/* 254 */       return null;
/*     */     }
/* 256 */     return exposureType.substring(0, 1);
/*     */   }
/*     */   
/*     */   public static void setHolidayConfigMap(Map<String, String> map) {
/* 260 */     holidayConfigMap = map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public static boolean isHoliday(LocalDate date) {
/* 269 */     String flag = null;
/*     */     
/* 271 */     if (CollUtil.isEmpty(holidayConfigMap) || StrUtil.isEmpty(flag = holidayConfigMap.get(date.format(DATE_FORMATTER)))) {
/* 272 */       DayOfWeek week = date.getDayOfWeek();
/* 273 */       return (week == DayOfWeek.SATURDAY || week == DayOfWeek.SUNDAY);
/*     */     } 
/*     */     
/* 276 */     return StrUtil.equals(flag, Identity.YES.getCode());
/*     */   }
/*     */   
/*     */   public static void setEcFactorList(List<EcFactorDo> list) {
/* 280 */     ecFactorList = new ArrayList<>(list);
/*     */   }
/*     */   
/*     */   public static List<EcFactorDo> getEcFactorList() {
/* 284 */     return ecFactorList;
/*     */   }
/*     */   
/*     */   public static void setEcColumnRuleMap(Map<String, ColumnRuleDto> map) {
/* 288 */     ecColumnRuleMap = new HashMap<>(map);
/*     */   }
/*     */   
/*     */   public static Map<String, ColumnRuleDto> getEcColumnRuleMap() {
/* 292 */     return ecColumnRuleMap;
/*     */   }
/*     */   
/*     */   public static CreditRuleDo getCreditRule(SchemeConfigDo schemeConfigDo, String approach) {
/* 296 */     if (StrUtil.isEmpty(approach)) {
/* 297 */       approach = schemeConfigDo.getApproach();
/*     */     } else {
/* 299 */       approach = approach.substring(0, 1);
/*     */     } 
/* 301 */     if (StrUtil.equals(approach, Approach.IRB.getCode())) {
/* 302 */       return schemeConfigDo.getIrbParamVersion().getCreditRule();
/*     */     }
/* 304 */     return schemeConfigDo.getWaParamVersion().getCreditRule();
/*     */   }
/*     */   
/*     */   public static CreditRuleDo getCreditRule(String schemeId, String approach) {
/* 308 */     return getCreditRule(getSchemeConfig(schemeId), approach);
/*     */   }
/*     */   
/*     */   public static boolean isEnRm(CreditRuleDo creditRuleDo, String mitigationMainType) {
/* 312 */     if (StrUtil.equals(mitigationMainType.substring(0, 1), MitigationType.COLLATERAL.getCode()))
/* 313 */       return creditRuleDo.isCollEnRm(); 
/* 314 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.GUARANTEE.getCode()))
/* 315 */       return creditRuleDo.isGuarEnRm(); 
/* 316 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/* 317 */       return creditRuleDo.isCdgEnRm();
/*     */     }
/* 319 */     throw new ParamConfigException("缓释工具大类异常！mitigationMainType=" + JsonUtils.object2Json(mitigationMainType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnMmm(CreditRuleDo creditRuleDo, String mitigationMainType) {
/* 324 */     if (StrUtil.equals(mitigationMainType.substring(0, 1), MitigationType.COLLATERAL.getCode()))
/* 325 */       return creditRuleDo.isCollEnMmm(); 
/* 326 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.GUARANTEE.getCode()))
/* 327 */       return creditRuleDo.isGuarEnMmm(); 
/* 328 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/* 329 */       return creditRuleDo.isCdgEnMmm();
/*     */     }
/* 331 */     throw new ParamConfigException("缓释工具大类异常！mitigationMainType=" + JsonUtils.object2Json(mitigationMainType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnMitiExcRm(CreditRuleDo creditRuleDo, String mitigationMainType) {
/* 336 */     if (StrUtil.equals(mitigationMainType.substring(0, 1), MitigationType.COLLATERAL.getCode()))
/* 337 */       return creditRuleDo.isCollEnMitiExcRm(); 
/* 338 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.GUARANTEE.getCode()))
/* 339 */       return creditRuleDo.isGuarEnMitiExcRm(); 
/* 340 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/* 341 */       return creditRuleDo.isCdgEnMitiExcRm();
/*     */     }
/* 343 */     throw new ParamConfigException("缓释工具大类异常！mitigationMainType=" + JsonUtils.object2Json(mitigationMainType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnCmm(CreditRuleDo creditRuleDo, String mitigationMainType) {
/* 348 */     if (StrUtil.equals(mitigationMainType.substring(0, 1), MitigationType.COLLATERAL.getCode()))
/* 349 */       return creditRuleDo.isCollEnCmm(); 
/* 350 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.GUARANTEE.getCode()))
/* 351 */       return creditRuleDo.isGuarEnCmm(); 
/* 352 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/* 353 */       return creditRuleDo.isCdgEnCmm();
/*     */     }
/* 355 */     throw new ParamConfigException("缓释工具大类异常！mitigationMainType=" + JsonUtils.object2Json(mitigationMainType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isEnHfxAdj(CreditRuleDo creditRuleDo, String mitigationMainType) {
/* 360 */     if (StrUtil.equals(mitigationMainType.substring(0, 1), MitigationType.COLLATERAL.getCode()))
/* 361 */       return creditRuleDo.isCollEnHfxAdj(); 
/* 362 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.GUARANTEE.getCode()))
/* 363 */       return creditRuleDo.isGuarEnHfxAdj(); 
/* 364 */     if (StrUtil.equals(mitigationMainType, MitigationMainType.CREDIT_DERIVATIVE.getCode())) {
/* 365 */       return creditRuleDo.isCdgEnHfxAdj();
/*     */     }
/* 367 */     throw new ParamConfigException("缓释工具大类异常！mitigationMainType=" + JsonUtils.object2Json(mitigationMainType));
/*     */   }
/*     */ 
/*     */   
/*     */   public static boolean isRmb(String currency) {
/* 372 */     return rmbCurrencyCode.contains(currency);
/*     */   }
/*     */   
/*     */   public static int getWorkDays(Date start, Date end) {
/* 376 */     if (start == null || end == null || start.compareTo(end) >= 0) {
/* 377 */       return 0;
/*     */     }
/* 379 */     return getExactWorkDays(DataUtils.toLocalDate(start), DataUtils.toLocalDate(end));
/*     */   }
/*     */   
/*     */   public static int getExactWorkDays(LocalDate start, LocalDate end) {
/* 383 */     if (start == null || end == null || start.compareTo(end) >= 0) {
/* 384 */       return 0;
/*     */     }
/*     */ 
/*     */ 
/*     */     
/* 389 */     int years = (int)ChronoUnit.YEARS.between(start, end);
/* 390 */     if (years > 0)
/*     */     {
/* 392 */       end = end.minusYears(years);
/*     */     }
/* 394 */     int days = getDefaultWorkDays(start, end);
/* 395 */     if (days >= 250) {
/*     */ 
/*     */ 
/*     */       
/* 399 */       end = end.minusYears(1L);
/* 400 */       int md = getDefaultWorkDays(end, start);
/* 401 */       days = 250 - md;
/*     */     } 
/* 403 */     return years * 250 + days;
/*     */   }
/*     */   
/*     */   public static int getDefaultWorkDays(LocalDate start, LocalDate end) {
/* 407 */     if (start == null || end == null || start.compareTo(end) >= 0) {
/* 408 */       return 0;
/*     */     }
/* 410 */     long between = ChronoUnit.DAYS.between(start, end);
/* 411 */     LocalDate date = start;
/* 412 */     int work = 0;
/*     */     
/* 414 */     for (int i = 0; i < between; i++) {
/* 415 */       if (!isHoliday(date)) {
/* 416 */         work++;
/*     */       }
/* 418 */       date = date.plusDays(1L);
/*     */     } 
/* 420 */     return work;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\RwaConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */