/*      */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*      */ import cn.hutool.core.util.NumberUtil;
/*      */ import cn.hutool.core.util.StrUtil;
/*      */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*      */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*      */ import com.amarsoft.rwa.engine.constant.Identity;
/*      */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*      */ import com.amarsoft.rwa.engine.constant.ParamTemplate;
/*      */ import com.amarsoft.rwa.engine.constant.RuleConfigWay;
/*      */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*      */ import com.amarsoft.rwa.engine.entity.AmpUnderAssetResultDto;
/*      */ import com.amarsoft.rwa.engine.entity.ParamColumnDo;
/*      */ import com.amarsoft.rwa.engine.entity.ParamTemplateDo;
/*      */ import com.amarsoft.rwa.engine.entity.ParamVersionDo;
/*      */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*      */ import com.amarsoft.rwa.engine.exception.ParamMappingException;
/*      */ import com.amarsoft.rwa.engine.job.JobUtils;
/*      */ import com.amarsoft.rwa.engine.util.DataUtils;
/*      */ import com.amarsoft.rwa.engine.util.RwaMath;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ 
/*      */ public class RwaMapping {
/*   25 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.RwaMapping.class);
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> paramMapping(Map<String, Object> data, Map<String, String> dataConditionMap, SchemeConfigDo schemeConfig, ParamTemplate template) {
/*   30 */     return paramMapping(data, dataConditionMap, getParamVersion(schemeConfig, template), template);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Object> paramMapping(Map<String, Object> data, Map<String, String> dataConditionMap, ParamVersionDo paramVersion, ParamTemplate template) {
/*   35 */     if (paramVersion == null) {
/*   36 */       return data;
/*      */     }
/*   38 */     return paramMapping(data, dataConditionMap, (List<ParamDetailDo>)paramVersion.getParamDetailListMap().get(template.getCode()), paramVersion
/*   39 */         .getParamColumnListMap(), paramVersion.getParamTemplateDoMap());
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static ParamVersionDo getParamVersion(SchemeConfigDo schemeConfig, ParamTemplate template) {
/*   45 */     if (schemeConfig.getIrbParamVersion() != null && schemeConfig.getIrbParamVersion().getParamDetailListMap().containsKey(template.getCode())) {
/*   46 */       return schemeConfig.getIrbParamVersion();
/*      */     }
/*      */     
/*   49 */     return schemeConfig.getWaParamVersion();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Object> paramMapping(Map<String, Object> data, Map<String, String> dataConditionMap, List<ParamDetailDo> paramDetailDoList, Map<String, List<ParamColumnDo>> paramColumnListMap, Map<String, ParamTemplateDo> paramTemplateDoMap) {
/*   67 */     if (dataConditionMap != null) {
/*   68 */       for (String key : dataConditionMap.keySet()) {
/*   69 */         if (!StrUtil.equals(dataConditionMap.get(key), (String)data.get(key))) {
/*   70 */           return data;
/*      */         }
/*      */       } 
/*      */     }
/*      */     
/*   75 */     if (CollUtil.isEmpty(paramDetailDoList)) {
/*   76 */       return data;
/*      */     }
/*      */     
/*   79 */     for (ParamDetailDo detailDo : paramDetailDoList) {
/*      */       
/*   81 */       List<ParamColumnDo> paramColumnDoList = paramColumnListMap.get(detailDo.getParamDetailNo());
/*      */       
/*   83 */       if (judgmentCondition(data, paramColumnDoList, paramTemplateDoMap)) {
/*      */         
/*   85 */         mappingResult(data, paramColumnDoList, paramTemplateDoMap);
/*   86 */         return data;
/*      */       } 
/*      */     } 
/*      */     
/*   90 */     return data;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void mappingResult(Map<String, Object> data, List<ParamColumnDo> paramColumnDoList, Map<String, ParamTemplateDo> paramTemplateDoMap) {
/*  102 */     for (ParamColumnDo columnDo : paramColumnDoList) {
/*      */       BigDecimal num;
/*  104 */       ParamTemplateDo paramTemplateDo = paramTemplateDoMap.get(columnDo.getParamTemplateColNo());
/*  105 */       if (!StrUtil.equals(paramTemplateDo.getRuleColAttr(), RuleColumnAttr.RESULT.getCode())) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/*  110 */       if (StrUtil.isEmpty(columnDo.getRuleConfigContent())) {
/*  111 */         throw new ParamConfigException("参数配置异常，结果为空！ParamColumnDo=" + columnDo);
/*      */       }
/*      */       
/*  114 */       RuleColumnType ruleColumnType = (RuleColumnType)EnumUtils.getEnumByCode(paramTemplateDo.getRuleColType(), RuleColumnType.class);
/*  115 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$RuleColumnType[ruleColumnType.ordinal()]) {
/*      */         
/*      */         case 1:
/*  118 */           num = Convert.toBigDecimal(columnDo.getRuleConfigContent());
/*  119 */           if (num == null) {
/*  120 */             throw new ParamConfigException("参数配置异常，当前内容非数值，请检查配置！ParamColumnDo=" + columnDo);
/*      */           }
/*  122 */           DataUtils.setBigDecimal(data, columnDo.getRuleColCode(), num);
/*      */           continue;
/*      */       } 
/*      */ 
/*      */       
/*  127 */       DataUtils.setString(data, columnDo.getRuleColCode(), columnDo.getRuleConfigContent());
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean judgmentCondition(Map<String, Object> data, List<ParamColumnDo> paramColumnDoList, Map<String, ParamTemplateDo> paramTemplateDoMap) {
/*  142 */     if (CollUtil.isEmpty(paramColumnDoList)) {
/*  143 */       return false;
/*      */     }
/*      */     
/*  146 */     for (ParamColumnDo columnDo : paramColumnDoList) {
/*      */       
/*  148 */       ParamTemplateDo paramTemplateDo = paramTemplateDoMap.get(columnDo.getParamTemplateColNo());
/*  149 */       if (paramTemplateDo == null) {
/*  150 */         throw new ParamConfigException("没有参数对应的字段配置， 请检查！columnDo=" + JsonUtils.object2Json(columnDo));
/*      */       }
/*  152 */       if (!StrUtil.equals(paramTemplateDo.getRuleColAttr(), RuleColumnAttr.CONDITION.getCode())) {
/*      */         continue;
/*      */       }
/*      */       
/*      */       try {
/*  157 */         boolean flag = judgmentCondition(data, columnDo, paramTemplateDo);
/*      */         
/*  159 */         if (!flag) {
/*  160 */           return false;
/*      */         }
/*  162 */       } catch (Exception e) {
/*  163 */         throw new ParamConfigException("判断字段条件异常，请检查参数配置： [columnDo: " + columnDo + "][paramTemplateDo: " + paramTemplateDo + "]", e);
/*      */       } 
/*      */     } 
/*  166 */     return true;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean judgmentCondition(Map<String, Object> data, ParamColumnDo columnDo, ParamTemplateDo paramTemplateDo) {
/*  178 */     RuleConfigWay ruleConfigWay = (RuleConfigWay)EnumUtils.getEnumByCode(columnDo.getRuleConfigWay(), RuleConfigWay.class);
/*  179 */     RuleColumnType ruleColumnType = (RuleColumnType)EnumUtils.getEnumByCode(paramTemplateDo.getRuleColType(), RuleColumnType.class);
/*      */     
/*  181 */     if (ruleConfigWay == RuleConfigWay.CUSTOM) {
/*  182 */       if (judgmentConditionByCustom(data, columnDo)) {
/*  183 */         return true;
/*      */       }
/*  185 */       return false;
/*      */     } 
/*      */ 
/*      */     
/*  189 */     Object value = data.get(columnDo.getRuleColCode());
/*      */     
/*  191 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$RuleColumnType[ruleColumnType.ordinal()]) {
/*      */       case 2:
/*  193 */         return judgmentConditionByCode(value, columnDo, ruleConfigWay);
/*      */       case 1:
/*  195 */         return judgmentConditionByNumber(value, columnDo, ruleConfigWay, paramTemplateDo.getColFormat());
/*      */       case 3:
/*  197 */         return judgmentConditionByString(value, columnDo, ruleConfigWay);
/*      */     } 
/*  199 */     throw new ParamConfigException();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean judgmentConditionByCustom(Map<String, Object> data, ParamColumnDo columnDo) {
/*      */     try {
/*  214 */       AviatorUtils.execute(columnDo.getRuleConfigContent(), data);
/*  215 */     } catch (Exception e) {
/*  216 */       throw new ParamConfigException("自定义规则脚本执行异常，请检查配置！[ParamColumnDo=" + columnDo + "][data=" + data + "]", e);
/*      */     } 
/*  218 */     Object flag = data.get(RwaParam.RETURN_FLAG.getCode());
/*  219 */     if (flag == null) {
/*  220 */       throw new ParamConfigException("自定义规则脚本没有返回结果，请检查配置！[ParamColumnDo=" + columnDo + "][data=" + data + "]");
/*      */     }
/*  222 */     return ((Boolean)flag).booleanValue();
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean judgmentConditionByCode(Object value, ParamColumnDo columnDo, RuleConfigWay ruleConfigWay) {
/*  237 */     if (value == null || 
/*  238 */       StrUtil.isEmpty((String)value) || 
/*  239 */       StrUtil.isEmpty(columnDo.getRuleConfigContent()))
/*      */     {
/*  241 */       return false;
/*      */     }
/*      */     
/*  244 */     boolean flag = false;
/*      */     try {
/*  246 */       flag = DataUtils.isInList((String)value, columnDo.getRuleConfigContent());
/*  247 */     } catch (Exception e) {
/*  248 */       throw new RuntimeException("参数配置异常，请检查配置！ParamColumnDo=" + columnDo, e);
/*      */     } 
/*      */     
/*  251 */     if (ruleConfigWay == RuleConfigWay.UNEQUAL) {
/*  252 */       flag = !flag;
/*  253 */     } else if (ruleConfigWay != RuleConfigWay.EQUAL) {
/*  254 */       throw new RuntimeException("代码规则配置方式配置异常，请检查配置！ParamColumnDo=" + columnDo);
/*      */     } 
/*  256 */     return flag;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean judgmentConditionByString(Object value, ParamColumnDo columnDo, RuleConfigWay ruleConfigWay) {
/*  271 */     if (value == null || 
/*  272 */       StrUtil.isEmpty((String)value) || 
/*  273 */       StrUtil.isEmpty(columnDo.getRuleConfigContent()))
/*      */     {
/*  275 */       return false;
/*      */     }
/*      */     
/*  278 */     boolean flag = StrUtil.equals((String)value, columnDo.getRuleConfigContent());
/*  279 */     if (ruleConfigWay == RuleConfigWay.UNEQUAL) {
/*  280 */       flag = !flag;
/*  281 */     } else if (ruleConfigWay != RuleConfigWay.EQUAL) {
/*  282 */       throw new RuntimeException("字符串规则配置方式配置异常，请检查配置！ParamColumnDo=" + columnDo);
/*      */     } 
/*  284 */     return flag;
/*      */   }
/*      */   
/*      */   public static boolean judgmentConditionByNumber(Object value, ParamColumnDo columnDo, RuleConfigWay ruleConfigWay, String columnFormat) {
/*  288 */     boolean flag = false;
/*      */     
/*  290 */     BigDecimal num = Convert.toBigDecimal(value);
/*      */     
/*  292 */     if (num == null) {
/*  293 */       return false;
/*      */     }
/*      */     
/*  296 */     if (ruleConfigWay == RuleConfigWay.RANGE) {
/*      */       try {
/*  298 */         return isInRange(num, columnDo.getRuleConfigContent(), columnFormat);
/*  299 */       } catch (Exception e) {
/*  300 */         throw new RuntimeException("参数配置异常，请检查配置！ParamColumnDo=" + columnDo, e);
/*      */       } 
/*      */     }
/*      */     
/*  304 */     BigDecimal compareNum = Convert.toBigDecimal(columnDo.getRuleConfigContent());
/*      */     
/*  306 */     compareNum = convertUnit(compareNum, columnFormat);
/*  307 */     if (compareNum == null) {
/*  308 */       throw new RuntimeException("参数配置异常，当前内容非数值，请检查配置！ParamColumnDo=" + columnDo);
/*      */     }
/*  310 */     return numberCompare(ruleConfigWay, num, compareNum);
/*      */   }
/*      */   
/*      */   public static boolean numberCompare(RuleConfigWay ruleConfigWay, BigDecimal a, BigDecimal b) {
/*  314 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$RuleConfigWay[ruleConfigWay.ordinal()]) {
/*      */       case 1:
/*  316 */         return NumberUtil.equals(a, b);
/*      */       case 2:
/*  318 */         return !NumberUtil.equals(a, b);
/*      */       case 3:
/*  320 */         return NumberUtil.isGreater(a, b);
/*      */       case 4:
/*  322 */         return NumberUtil.isGreaterOrEqual(a, b);
/*      */       case 5:
/*  324 */         return NumberUtil.isLess(a, b);
/*      */       case 6:
/*  326 */         return NumberUtil.isLessOrEqual(a, b);
/*      */     } 
/*  328 */     throw new ParamConfigException();
/*      */   }
/*      */   
/*      */   public static BigDecimal convertUnit(BigDecimal num, String columnFormat) {
/*  332 */     if (StrUtil.isEmpty(columnFormat)) {
/*  333 */       return num;
/*      */     }
/*  335 */     if (StrUtil.equals(columnFormat, RuleColumnFormat.MONTH.getCode())) {
/*  336 */       num = RwaMath.div(num, Integer.valueOf(12));
/*  337 */     } else if (StrUtil.equals(columnFormat, RuleColumnFormat.DAY.getCode())) {
/*  338 */       num = RwaMath.div(num, Integer.valueOf(365));
/*      */     } 
/*  340 */     return num;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isInRange(BigDecimal num, String content, String columnFormat) {
/*  345 */     content = content.trim();
/*  346 */     if (content.length() < 5) {
/*  347 */       throw new RuntimeException("范围配置内容异常，存在非法字符，请检查配置！content=" + content);
/*      */     }
/*  349 */     String leftSymbol = content.substring(0, 1);
/*  350 */     String rightSymbol = content.substring(content.length() - 1, content.length());
/*  351 */     String[] rangeList = content.substring(1, content.length() - 1).split(",");
/*  352 */     if (rangeList.length != 2) {
/*  353 */       throw new RuntimeException("范围配置内容异常，存在非法字符，请检查配置！content=" + content);
/*      */     }
/*      */     
/*  356 */     BigDecimal begin = Convert.toBigDecimal(rangeList[0]);
/*  357 */     BigDecimal end = Convert.toBigDecimal(rangeList[1]);
/*  358 */     if (begin == null || end == null) {
/*  359 */       throw new RuntimeException("范围配置内容异常，存在非法字符，请检查配置！content=" + content);
/*      */     }
/*      */     
/*  362 */     begin = convertUnit(begin, columnFormat);
/*  363 */     end = convertUnit(end, columnFormat);
/*  364 */     boolean leftFlag = false;
/*  365 */     boolean rightFlag = false;
/*      */     
/*  367 */     if (StrUtil.equals("(", leftSymbol)) {
/*  368 */       leftFlag = NumberUtil.isGreater(num, begin);
/*  369 */     } else if (StrUtil.equals("[", leftSymbol)) {
/*  370 */       leftFlag = NumberUtil.isGreaterOrEqual(num, begin);
/*      */     } else {
/*  372 */       throw new RuntimeException("范围配置内容异常，存在非法字符，请检查配置！content=" + content);
/*      */     } 
/*      */     
/*  375 */     if (!leftFlag) {
/*  376 */       return false;
/*      */     }
/*  378 */     if (StrUtil.equals(")", rightSymbol)) {
/*  379 */       rightFlag = NumberUtil.isLess(num, end);
/*  380 */     } else if (StrUtil.equals("]", rightSymbol)) {
/*  381 */       rightFlag = NumberUtil.isLessOrEqual(num, end);
/*      */     } else {
/*  383 */       throw new RuntimeException("范围配置内容异常，存在非法字符，请检查配置！content=" + content);
/*      */     } 
/*  385 */     if (rightFlag) {
/*  386 */       return true;
/*      */     }
/*  388 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingExposureCcfWa(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id) {
/*  402 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.CCF);
/*  403 */     paramMapping(exposure, null, paramVersionDo, ParamTemplate.CCF);
/*      */     
/*  405 */     BigDecimal ccf = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.CCF);
/*  406 */     if (RwaMath.isNullOrNegative(ccf)) {
/*  407 */       if (RwaConfig.enableMappingExceptionOut) {
/*  408 */         throw new ParamMappingException("暴露权重法CCF映射异常！exposureId=" + id);
/*      */       }
/*  410 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.CCF_WA);
/*  411 */       ccf = paramVersionDo.getCreditRule().getDefaultCcf();
/*  412 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CCF, ccf);
/*  413 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.OFF_RPT_ITEM_WA, paramVersionDo.getCreditRule().getDefaultOffRptItem());
/*      */     } 
/*  415 */     return ccf;
/*      */   }
/*      */   
/*      */   public static BigDecimal mappingExposureNrRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, Identity defaultFlag) {
/*  419 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_NR);
/*  420 */     if (defaultFlag == Identity.YES && paramVersionDo.getCreditRule().isEnDftExpoRwMapping()) {
/*  421 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_DEFAULT);
/*      */     } else {
/*  423 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_NR);
/*      */     } 
/*      */     
/*  426 */     BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  427 */     if (rw == null) {
/*  428 */       if (RwaConfig.enableMappingExceptionOut) {
/*  429 */         throw new ParamMappingException("非零售暴露权重映射异常！exposureId=" + id);
/*      */       }
/*  431 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_EXPOSURE);
/*  432 */       rw = paramVersionDo.getCreditRule().getDefaultNrRw();
/*  433 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*  434 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getDefaultNrRptItem());
/*      */     } 
/*  436 */     return rw;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingExposureAmpAbaRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, List<Map<String, Object>> abaInfoList, String id) {
/*  441 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_ABA);
/*  442 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getAmpBasisRptItem());
/*      */     
/*  444 */     String exposureTypeWa = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/*  445 */     if (StrUtil.equals(exposureTypeWa, SpecExposureTypeWa.AMP_ADV.getCode())) {
/*  446 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, BigDecimal.ONE);
/*  447 */       return BigDecimal.ONE;
/*      */     } 
/*      */     
/*  450 */     BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*      */     
/*  452 */     if (RwaMath.isZero(balance)) {
/*  453 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*  454 */       return RwaMath.maxRw;
/*      */     } 
/*      */     
/*  457 */     BigDecimal rw = calculateAmpAbaRw(schemeConfig, jobId, InterfaceDataType.AMP_INFO, exposure, abaInfoList, balance);
/*  458 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*  459 */     if (rw == null) {
/*  460 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_AMP);
/*  461 */       if (RwaConfig.enableMappingExceptionOut) {
/*  462 */         throw new ParamMappingException("非零售暴露资产管理产品权重映射异常！exposureId=" + id);
/*      */       }
/*      */       
/*  465 */       rw = RwaMath.maxRw;
/*  466 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*      */     } 
/*  468 */     return rw;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal calculateAmpAbaRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, List<Map<String, Object>> abaInfoList, BigDecimal balance) {
/*  473 */     BigDecimal rwaTotal = BigDecimal.ZERO;
/*  474 */     BigDecimal cpTotal = BigDecimal.ZERO;
/*  475 */     BigDecimal cvaTotal = BigDecimal.ZERO;
/*  476 */     BigDecimal prlTotal = BigDecimal.ZERO;
/*  477 */     BigDecimal abCpTotal = BigDecimal.ZERO;
/*  478 */     BigDecimal eadCpTotal = BigDecimal.ZERO;
/*  479 */     BigDecimal ratioTotal = BigDecimal.ZERO;
/*  480 */     List<AmpUnderAssetResultDto> assetResultList = new ArrayList<>();
/*      */     
/*  482 */     for (Map<String, Object> abaInfo : abaInfoList) {
/*  483 */       String assetId = DataUtils.getString(abaInfo, (ICodeEnum)RwaParam.ABA_ASSET_ID);
/*  484 */       String assetType = DataUtils.getString(abaInfo, (ICodeEnum)RwaParam.ASSET_TYPE_ABA);
/*  485 */       mappingAmpAbaRw(schemeConfig, jobId, dataType, abaInfo, assetId, assetType);
/*      */     } 
/*      */     
/*  488 */     RwaParam ratioType = RwaParam.INVESTMENT_RATIO;
/*      */     
/*  490 */     if (DataUtils.getBigDecimal(abaInfoList.get(0), (ICodeEnum)ratioType) == null) {
/*  491 */       ratioType = RwaParam.MAX_INVEST_RATIO;
/*      */     }
/*      */ 
/*      */     
/*  495 */     RwaParam finalRatioType = ratioType;
/*  496 */     Collections.sort(abaInfoList, (Comparator<? super Map<String, Object>>)new Object(finalRatioType));
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  511 */     boolean isEnd = false;
/*  512 */     for (Map<String, Object> abaInfo : abaInfoList) {
/*  513 */       String assetId = DataUtils.getString(abaInfo, (ICodeEnum)RwaParam.ABA_ASSET_ID);
/*  514 */       String assetType = DataUtils.getString(abaInfo, (ICodeEnum)RwaParam.ASSET_TYPE_ABA);
/*  515 */       BigDecimal ratio = DataUtils.getBigDecimal(abaInfo, (ICodeEnum)ratioType);
/*  516 */       if (RwaMath.isNullOrNegative(ratio) || RwaMath.isZero(ratio)) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/*  521 */       if (NumberUtil.isGreaterOrEqual(RwaMath.add(ratioTotal, ratio), BigDecimal.ONE)) {
/*      */         
/*  523 */         ratio = RwaMath.sub(BigDecimal.ONE, ratioTotal);
/*  524 */         ratioTotal = BigDecimal.ONE;
/*      */         
/*  526 */         isEnd = true;
/*      */       } else {
/*  528 */         ratioTotal = RwaMath.add(ratioTotal, ratio);
/*      */       } 
/*      */       
/*  531 */       AmpUnderAssetResultDto assetResultDto = calculateAmpAbaResult(schemeConfig, abaInfo, assetId, assetType, DataUtils.getBigDecimal(abaInfo, (ICodeEnum)RwaParam.RW), balance, ratio);
/*  532 */       assetResultList.add(assetResultDto);
/*      */       
/*  534 */       rwaTotal = RwaMath.add(rwaTotal, assetResultDto.getRwa());
/*  535 */       if (StrUtil.equals(assetResultDto.getFlag(), Identity.YES.getCode())) {
/*  536 */         abCpTotal = RwaMath.add(abCpTotal, assetResultDto.getAb());
/*  537 */         prlTotal = RwaMath.add(prlTotal, assetResultDto.getPrl());
/*  538 */         eadCpTotal = RwaMath.add(eadCpTotal, assetResultDto.getEad());
/*  539 */         cpTotal = RwaMath.add(cpTotal, assetResultDto.getCp());
/*  540 */         cvaTotal = RwaMath.add(cvaTotal, assetResultDto.getCva());
/*      */       } 
/*      */       
/*  543 */       if (isEnd) {
/*      */         break;
/*      */       }
/*      */     } 
/*      */ 
/*      */     
/*  549 */     if (NumberUtil.isLess(ratioTotal, BigDecimal.ONE)) {
/*  550 */       BigDecimal ratio = RwaMath.sub(BigDecimal.ONE, ratioTotal);
/*      */       
/*  552 */       AmpUnderAssetResultDto assetResultDto = calculateAmpAbaResult(schemeConfig, null, "0", AbaAssetType.OTHER.getCode(), RwaMath.maxRw, balance, ratio);
/*  553 */       assetResultList.add(assetResultDto);
/*      */       
/*  555 */       rwaTotal = RwaMath.add(rwaTotal, assetResultDto.getRwa());
/*      */     } 
/*      */     
/*  558 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.AB_CP, abCpTotal);
/*  559 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.NOTIONAL_PRINCIPAL, prlTotal);
/*  560 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EAD_CP, eadCpTotal);
/*  561 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RWA_CP, cpTotal);
/*  562 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CVA, cvaTotal);
/*  563 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.AMP_UNDER_ASSET_RST, JsonUtils.object2Json(assetResultList));
/*      */     
/*  565 */     return RwaMath.div(rwaTotal, balance);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingAmpAbaRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> abaInfo, String assetId, String assetType) {
/*  570 */     BigDecimal assetRw = null;
/*      */     
/*  572 */     if (StrUtil.equals(assetType, AbaAssetType.ABS.getCode())) {
/*      */       
/*  574 */       DataUtils.setBigDecimal(abaInfo, (ICodeEnum)RwaParam.TRANCHE_MATURITY, RwaMath.getTrancheMaturity(DataUtils.getBigDecimal(abaInfo, (ICodeEnum)RwaParam.RESIDUAL_MATURITY)));
/*  575 */       assetRw = mappingAbsRw(schemeConfig, jobId, dataType, abaInfo, assetId);
/*      */     } else {
/*  577 */       ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_ABA);
/*  578 */       paramMapping(abaInfo, null, paramVersionDo, ParamTemplate.RW_ABA);
/*  579 */       assetRw = DataUtils.getBigDecimal(abaInfo, (ICodeEnum)RwaParam.RW);
/*  580 */       if (assetRw == null) {
/*  581 */         JobUtils.addErrorData(jobId, dataType, assetId, ExcDataCode.RW_AMP);
/*  582 */         if (RwaConfig.enableMappingExceptionOut) {
/*  583 */           throw new ParamMappingException("非零售暴露资产管理产品授权基础法权重映射异常！assetId=" + assetId);
/*      */         }
/*  585 */         assetRw = RwaMath.maxRw;
/*  586 */         DataUtils.setBigDecimal(abaInfo, (ICodeEnum)RwaParam.RW, assetRw);
/*      */       } 
/*      */     } 
/*  589 */     return assetRw;
/*      */   }
/*      */   
/*      */   public static AmpUnderAssetResultDto calculateAmpAbaResult(SchemeConfigDo schemeConfig, Map<String, Object> abaInfo, String assetId, String assetType, BigDecimal rw, BigDecimal balance, BigDecimal ratio) {
/*  593 */     BigDecimal ab = RwaMath.mul(balance, ratio);
/*  594 */     BigDecimal ead = ab;
/*      */     
/*  596 */     BigDecimal prl = null;
/*  597 */     BigDecimal rwa = BigDecimal.ZERO;
/*  598 */     BigDecimal cp = BigDecimal.ZERO;
/*  599 */     BigDecimal cva = BigDecimal.ZERO;
/*  600 */     String flag = Identity.NO.getCode();
/*  601 */     String mainType = null;
/*  602 */     if (StrUtil.isNotEmpty(assetType) && assetType.length() >= 3) {
/*  603 */       mainType = assetType.substring(0, 3);
/*      */     }
/*      */     
/*  606 */     if (NumberUtil.equals(rw, RwaMath.maxRw)) {
/*      */       
/*  608 */       rwa = RwaMath.mul(ead, rw);
/*  609 */     } else if (StrUtil.equals(mainType, AbaAssetType.DI.getCode())) {
/*      */       
/*  611 */       flag = Identity.YES.getCode();
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*  616 */       prl = DataUtils.getBigDecimal(abaInfo, (ICodeEnum)RwaParam.NOTIONAL_PRINCIPAL);
/*      */       
/*  618 */       if (RwaMath.isNullOrNegative(prl)) {
/*  619 */         prl = ab;
/*  620 */         DataUtils.setBigDecimal(abaInfo, (ICodeEnum)RwaParam.NOTIONAL_PRINCIPAL, prl);
/*      */       } 
/*  622 */       BigDecimal mtm = DataUtils.getBigDecimal(abaInfo, (ICodeEnum)RwaParam.MTM);
/*      */       
/*  624 */       if (RwaMath.isNullOrNegative(mtm)) {
/*  625 */         mtm = prl;
/*  626 */         DataUtils.setBigDecimal(abaInfo, (ICodeEnum)RwaParam.MTM, mtm);
/*      */       } 
/*      */       
/*  629 */       if (StrUtil.equals(schemeConfig.getDiEadApproach(), DiEadApproach.SA.getCode())) {
/*      */ 
/*      */         
/*  632 */         ead = RwaMath.getEadOfDi(mtm, RwaMath.mul(prl, new BigDecimal("0.15")));
/*      */       } else {
/*      */         
/*  635 */         ead = RwaMath.getEadOfDiCem(mtm, RwaMath.mul(prl, new BigDecimal("0.15")));
/*      */       } 
/*  637 */       cp = RwaMath.mul(ead, rw);
/*      */       
/*  639 */       cva = RwaMath.mul(cp, new BigDecimal("1.5"));
/*  640 */       rwa = RwaMath.add(cp, cva);
/*  641 */     } else if (StrUtil.equals(mainType, AbaAssetType.SFT.getCode()) || StrUtil.equals(mainType, AbaAssetType.CCP.getCode())) {
/*      */       
/*  643 */       flag = Identity.YES.getCode();
/*  644 */       cp = RwaMath.mul(ead, rw);
/*  645 */       rwa = cp;
/*      */     } else {
/*  647 */       rwa = RwaMath.mul(ead, rw);
/*      */     } 
/*      */     
/*  650 */     return new AmpUnderAssetResultDto(assetId, assetType, flag, ratio, ab, prl, ead, rw, rwa, cp, cva);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingExposureAmpOther1250Rw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id) {
/*  655 */     ParamVersionDo paramVersionDo = schemeConfig.getWaParamVersion();
/*      */     
/*  657 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getAmp1250RptItem());
/*  658 */     BigDecimal rw = RwaMath.maxRw;
/*  659 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*  660 */     return rw;
/*      */   }
/*      */   
/*      */   public static BigDecimal mappingExposureLtvRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, Identity defaultFlag) {
/*  664 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_REE);
/*      */     
/*  666 */     if (!RwaConfig.enableRealEstateLtvCalculate || 
/*  667 */       StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.IS_PRUDENTIAL), Identity.NO.getCode()) || 
/*  668 */       !StrUtil.isEmpty(DataUtils.getString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE))) {
/*      */ 
/*      */       
/*  671 */       if (defaultFlag == Identity.YES && paramVersionDo.getCreditRule().isEnDftExpoRwMapping()) {
/*  672 */         paramMapping(exposure, null, schemeConfig, ParamTemplate.RW_DEFAULT);
/*      */       } else {
/*  674 */         paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_REE);
/*      */       } 
/*  676 */     } else if (defaultFlag == Identity.YES && paramVersionDo.getCreditRule().isEnDftExpoRwMapping()) {
/*      */       
/*  678 */       BigDecimal rltv = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RLTV);
/*  679 */       if (rltv != null) {
/*  680 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.RESIDENTIAL.getCode());
/*  681 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.LTV, rltv);
/*      */       } 
/*  683 */       paramMapping(exposure, null, schemeConfig, ParamTemplate.RW_DEFAULT);
/*      */     }
/*      */     else {
/*      */       
/*  687 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.RESIDENTIAL.getCode());
/*  688 */       DataUtils.mappingValue(exposure, (ICodeEnum)RwaParam.RLTV, (ICodeEnum)RwaParam.LTV);
/*  689 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_REE);
/*  690 */       BigDecimal rwRre = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  691 */       String rptRre = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA);
/*      */       
/*  693 */       DataUtils.remove(exposure, (ICodeEnum)RwaParam.RW);
/*  694 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.COMMERCIAL.getCode());
/*  695 */       DataUtils.mappingValue(exposure, (ICodeEnum)RwaParam.CLTV, (ICodeEnum)RwaParam.LTV);
/*  696 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_REE);
/*  697 */       BigDecimal rwCre = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  698 */       String rptCre = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA);
/*      */       
/*  700 */       if (rwRre == null || rwCre == null) {
/*      */         
/*  702 */         DataUtils.remove(exposure, (ICodeEnum)RwaParam.RW);
/*  703 */         DataUtils.remove(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA);
/*  704 */       } else if (NumberUtil.isLessOrEqual(rwRre, rwCre)) {
/*      */         
/*  706 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.RESIDENTIAL.getCode());
/*  707 */         DataUtils.mappingValue(exposure, (ICodeEnum)RwaParam.RLTV, (ICodeEnum)RwaParam.LTV);
/*  708 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rwRre);
/*  709 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, rptRre);
/*      */       } 
/*      */     } 
/*      */     
/*  713 */     BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  714 */     String rptItem = null;
/*  715 */     if (rw == null) {
/*  716 */       if (RwaConfig.enableMappingExceptionOut) {
/*  717 */         throw new ParamMappingException("房地产暴露权重映射异常！exposureId=" + id);
/*      */       }
/*  719 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_EXPOSURE);
/*  720 */       if (StrUtil.equals(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA), SpecExposureTypeWa.RRE.getCode())) {
/*  721 */         rw = paramVersionDo.getCreditRule().getDefaultReRw();
/*  722 */         rptItem = paramVersionDo.getCreditRule().getDefaultReRptItem();
/*      */       } else {
/*  724 */         rw = paramVersionDo.getCreditRule().getDefaultNrRw();
/*  725 */         rptItem = paramVersionDo.getCreditRule().getDefaultNrRptItem();
/*      */       } 
/*  727 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*  728 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, rptItem);
/*      */     } 
/*  730 */     return rw;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingExposureReRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, Identity defaultFlag) {
/*  735 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_RETAIL);
/*      */     
/*  737 */     if (defaultFlag == Identity.YES && paramVersionDo.getCreditRule().isEnDftExpoRwMapping()) {
/*  738 */       paramMapping(exposure, null, schemeConfig, ParamTemplate.RW_DEFAULT);
/*      */     } else {
/*  740 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_RETAIL);
/*      */     } 
/*      */     
/*  743 */     BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  744 */     if (rw == null) {
/*  745 */       if (RwaConfig.enableMappingExceptionOut) {
/*  746 */         throw new ParamMappingException("零售暴露权重映射异常！exposureId=" + id);
/*      */       }
/*  748 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_RETAIL);
/*  749 */       rw = paramVersionDo.getCreditRule().getDefaultReRw();
/*  750 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/*  751 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getDefaultReRptItem());
/*      */     } 
/*  753 */     return rw;
/*      */   }
/*      */   
/*      */   public static Integer mappingExposureTm(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, Integer defaultParam) {
/*  757 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.TM_EXPOSURE);
/*  758 */     paramMapping(exposure, null, paramVersionDo, ParamTemplate.TM_EXPOSURE);
/*  759 */     Integer tm = DataUtils.getInt(exposure, (ICodeEnum)RwaParam.TM);
/*  760 */     if (tm == null) {
/*  761 */       if (RwaConfig.enableMappingExceptionOut) {
/*  762 */         throw new ParamMappingException("暴露最低持有期映射异常！exposureId=" + id);
/*      */       }
/*  764 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.TM_FIRB);
/*  765 */       tm = defaultParam;
/*      */       
/*  767 */       DataUtils.setInt(exposure, (ICodeEnum)RwaParam.TM, tm);
/*      */     } 
/*  769 */     return tm;
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingHc(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> collateral, String id) {
/*  774 */     DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.MITIGATION_SMALL_TYPE, (ICodeEnum)RwaParam.INSTRUMENTS_TYPE);
/*  775 */     DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.RESIDUAL_MATURITY, (ICodeEnum)RwaParam.SECU_ISSUE_MATURITY);
/*      */     
/*  777 */     paramMapping(collateral, null, schemeConfig, ParamTemplate.SH);
/*  778 */     return DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.SH);
/*      */   }
/*      */   
/*      */   public static BigDecimal mappingCollateralRw(SchemeConfigDo schemeConfig, Map<String, Object> collateral) {
/*  782 */     paramMapping(collateral, null, schemeConfig, ParamTemplate.RW_COLLATERAL);
/*  783 */     return DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.RW);
/*      */   }
/*      */   
/*      */   public static BigDecimal mappingGuaranteeRw(SchemeConfigDo schemeConfig, Map<String, Object> collateral) {
/*  787 */     paramMapping(collateral, null, schemeConfig, ParamTemplate.RW_GUARANTEE);
/*  788 */     return DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.RW);
/*      */   }
/*      */   
/*      */   public static BigDecimal mappingAbsCcf(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/*  792 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.CCF_ABS);
/*  793 */     paramMapping(data, null, paramVersionDo, ParamTemplate.CCF_ABS);
/*      */     
/*  795 */     BigDecimal ccf = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.CCF);
/*  796 */     if (ccf == null) {
/*  797 */       if (RwaConfig.enableMappingExceptionOut) {
/*  798 */         throw new ParamMappingException("资产证券化CCF映射异常！exposureId=" + id);
/*      */       }
/*  800 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.CCF_ABS);
/*  801 */       ccf = paramVersionDo.getCreditRule().getDefaultCcf();
/*  802 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.CCF, ccf);
/*      */     } 
/*  804 */     return ccf;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingAbsRwSa(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/*  817 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.B2_RW_ABS_SA);
/*  818 */     paramMapping(data, null, paramVersionDo, ParamTemplate.B2_RW_ABS_SA);
/*  819 */     DataUtils.setString(data, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getAbsRptItem());
/*  820 */     BigDecimal rw = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW);
/*  821 */     if (rw == null) {
/*  822 */       if (RwaConfig.enableMappingExceptionOut) {
/*  823 */         throw new ParamMappingException("资产证券化标准法RW映射异常！exposureId=" + id);
/*      */       }
/*  825 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_ABS);
/*  826 */       rw = RwaMath.maxRw;
/*  827 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW, rw);
/*      */     } 
/*  829 */     return rw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingAbsRwRba(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/*  842 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.B2_RW_ABS_RBA);
/*  843 */     paramMapping(data, null, paramVersionDo, ParamTemplate.B2_RW_ABS_RBA);
/*  844 */     BigDecimal rw = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW);
/*  845 */     if (rw == null) {
/*  846 */       if (RwaConfig.enableMappingExceptionOut) {
/*  847 */         throw new ParamMappingException("资产证券化评级基础法RW映射异常！exposureId=" + id);
/*      */       }
/*  849 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_ABS);
/*  850 */       rw = RwaMath.maxRw;
/*  851 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW, rw);
/*      */     } 
/*  853 */     return rw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingAbsRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/*  867 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_ABS);
/*  868 */     paramMapping(data, null, paramVersionDo, ParamTemplate.RW_ABS);
/*      */     
/*  870 */     DataUtils.setString(data, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getAbsRptItem());
/*  871 */     BigDecimal rwPty1 = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW_PTY1);
/*  872 */     BigDecimal rwPty5 = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW_PTY5);
/*  873 */     BigDecimal rwNpty1 = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW_NPTY1);
/*  874 */     BigDecimal rwNpty5 = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.RW_NPTY5);
/*      */     
/*  876 */     if (rwPty1 == null || rwPty5 == null || rwNpty1 == null || rwNpty5 == null) {
/*  877 */       if (RwaConfig.enableMappingExceptionOut) {
/*  878 */         throw new ParamMappingException("资产证券化外部评级法权重映射异常！exposureId=" + id);
/*      */       }
/*  880 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_ABS);
/*  881 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW_BEFORE, RwaMath.maxRw);
/*  882 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW, RwaMath.maxRw);
/*  883 */       return RwaMath.maxRw;
/*      */     } 
/*      */     
/*  886 */     TrancheLevel trancheLevel = (TrancheLevel)EnumUtils.getEnumByCode(DataUtils.getString(data, (ICodeEnum)RwaParam.TRANCHE_LEVEL), TrancheLevel.class);
/*  887 */     BigDecimal trancheMaturity = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.TRANCHE_MATURITY);
/*      */     
/*  889 */     BigDecimal rwPt = getAbsRw(trancheMaturity, rwPty1, rwPty5);
/*  890 */     if (trancheLevel == TrancheLevel.PRIORITY) {
/*  891 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW_BEFORE, rwPt);
/*  892 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW, rwPt);
/*  893 */       return rwPt;
/*      */     } 
/*  895 */     BigDecimal t = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.T);
/*  896 */     if (t == null)
/*      */     {
/*  898 */       t = NumberUtil.toBigDecimal("0.01");
/*      */     }
/*      */     
/*  901 */     BigDecimal rw = getAbsRw(trancheMaturity, rwNpty1, rwNpty5);
/*  902 */     DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW_BEFORE, rw);
/*      */ 
/*      */     
/*  905 */     BigDecimal nptRwAc = NumberUtil.sub(Integer.valueOf(1), NumberUtil.min(new BigDecimal[] { t, BigDecimal.valueOf(0.5D) }));
/*  906 */     DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.NPT_RW_AC, nptRwAc);
/*  907 */     rw = NumberUtil.mul(rw, nptRwAc);
/*      */     
/*  909 */     rw = NumberUtil.max(new BigDecimal[] { rwPt, rw });
/*  910 */     rw = NumberUtil.max(new BigDecimal[] { BigDecimal.valueOf(0.15D), rw });
/*  911 */     DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.RW, rw);
/*  912 */     return rw;
/*      */   }
/*      */   
/*      */   public static BigDecimal getDefaultRwLowerLimit(TrancheLevel trancheLevel, String isComplianceStc) {
/*  916 */     if (trancheLevel == TrancheLevel.PRIORITY && StrUtil.equals(Identity.YES.getCode(), isComplianceStc)) {
/*  917 */       return NumberUtil.toBigDecimal("0.1");
/*      */     }
/*  919 */     return NumberUtil.toBigDecimal("0.15");
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal getAbsRw(BigDecimal trancheMaturity, BigDecimal rwY1, BigDecimal rwY5) {
/*  924 */     if (trancheMaturity == null)
/*      */     {
/*  926 */       trancheMaturity = BigDecimal.ONE;
/*      */     }
/*  928 */     if (NumberUtil.equals(rwY1, rwY5))
/*      */     {
/*  930 */       return rwY1; } 
/*  931 */     if (NumberUtil.isLessOrEqual(trancheMaturity, BigDecimal.ONE))
/*      */     {
/*  933 */       return rwY1; } 
/*  934 */     if (NumberUtil.isGreaterOrEqual(trancheMaturity, BigDecimal.valueOf(5L)))
/*      */     {
/*  936 */       return rwY5;
/*      */     }
/*      */     
/*  939 */     BigDecimal rwDiff = RwaMath.sub(rwY5, rwY1);
/*  940 */     if (NumberUtil.isLess(rwDiff, BigDecimal.ZERO)) {
/*  941 */       throw new ParamConfigException("ABS风险权重参数配置异常， 5年期风险权重必须>=1年期风险权重");
/*      */     }
/*  943 */     return RwaMath.add(rwY1, RwaMath.mul(new Number[] { RwaMath.sub(trancheMaturity, Integer.valueOf(1)), Double.valueOf(0.25D), rwDiff }));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void mappingAbsSf(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/*  948 */     paramMapping(data, null, schemeConfig, ParamTemplate.SF_ABS);
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingHe(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, BigDecimal defaultParam) {
/*  953 */     if (StrUtil.isEmpty(DataUtils.getString(exposure, (ICodeEnum)RwaParam.INSTRUMENTS_TYPE))) {
/*  954 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.SH, BigDecimal.ZERO);
/*      */     } else {
/*  956 */       paramMapping(exposure, null, schemeConfig, ParamTemplate.SH);
/*      */     } 
/*  958 */     BigDecimal sh = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SH);
/*  959 */     if (sh == null) {
/*  960 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.HE);
/*  961 */       sh = defaultParam;
/*  962 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.SH, sh);
/*      */     } 
/*  964 */     return sh;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingExposureCpRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> exposure, String id, Identity defaultFlag) {
/*  979 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_CP);
/*  980 */     if (StrUtil.equals(QualCcp.QUAL_CCP.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.QUAL_CCP_FLAG))) {
/*      */       
/*  982 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, paramVersionDo.getCreditRule().getCcpRw());
/*  983 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getCcpRptItem());
/*  984 */       return paramVersionDo.getCreditRule().getCcpRw();
/*      */     } 
/*  986 */     if (defaultFlag == Identity.YES && paramVersionDo.getCreditRule().isEnDftExpoRwMapping()) {
/*  987 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_DEFAULT);
/*      */     } else {
/*  989 */       paramMapping(exposure, null, paramVersionDo, ParamTemplate.RW_CP);
/*      */     } 
/*      */     
/*  992 */     BigDecimal rw = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.RW);
/*  993 */     if (rw == null) {
/*  994 */       if (RwaConfig.enableMappingExceptionOut) {
/*  995 */         throw new ParamMappingException("交易对手权重映射异常！Id=" + id);
/*      */       }
/*  997 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_EXPOSURE);
/*  998 */       rw = paramVersionDo.getCreditRule().getDefaultNrRw();
/*  999 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RW, rw);
/* 1000 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getDefaultNrRptItem());
/*      */     } 
/* 1002 */     return rw;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingCollateralCpRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> collateral, String id) {
/* 1015 */     DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.ISSUER_TYPE, (ICodeEnum)RwaParam.CLIENT_TYPE);
/* 1016 */     DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.ISSUER_IG_FLAG, (ICodeEnum)RwaParam.IG_FLAG);
/* 1017 */     DataUtils.mappingValue(collateral, (ICodeEnum)RwaParam.ISSUER_SCRA_RESULT, (ICodeEnum)RwaParam.SCRA_RESULT);
/* 1018 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_CP);
/* 1019 */     paramMapping(collateral, null, paramVersionDo, ParamTemplate.RW_CP);
/*      */     
/* 1021 */     BigDecimal rw = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.RW);
/* 1022 */     if (rw == null) {
/* 1023 */       if (RwaConfig.enableMappingExceptionOut) {
/* 1024 */         throw new ParamMappingException("证券融资交易抵质押品发行人权重映射异常！collateralId=" + id);
/*      */       }
/* 1026 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_COLLATERAL);
/* 1027 */       rw = paramVersionDo.getCreditRule().getDefaultNrRw();
/* 1028 */       DataUtils.setBigDecimal(collateral, (ICodeEnum)RwaParam.RW, rw);
/* 1029 */       DataUtils.setString(collateral, (ICodeEnum)RwaParam.EXPOSURE_RPT_ITEM_WA, paramVersionDo.getCreditRule().getDefaultNrRptItem());
/*      */     } 
/* 1031 */     return rw;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void paramMappingDerivatives(Map<String, Object> exposure, DiEadApproach diEadApproach, SchemeConfigDo schemeConfig) {
/* 1036 */     if (diEadApproach == DiEadApproach.SA) {
/*      */       
/* 1038 */       paramMapping(exposure, null, schemeConfig, ParamTemplate.SP_DI);
/* 1039 */       if (DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SUPERVISION_FACTOR) == null) {
/* 1040 */         throw new ParamMappingException("监管因子映射异常：exposure=" + JsonUtils.object2Json(exposure));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/* 1045 */       if (DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.SUPERVISION_VOLATILITY) == null) {
/* 1046 */         throw new ParamMappingException("监管波动率映射异常：exposure=" + JsonUtils.object2Json(exposure));
/*      */       }
/*      */     } else {
/*      */       
/* 1050 */       paramMapping(exposure, null, schemeConfig, ParamTemplate.ADDON_DI);
/* 1051 */       if (DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ADDON_FACTOR) == null) {
/* 1052 */         throw new ParamMappingException("Add-on附加系数映射异常：exposure=" + JsonUtils.object2Json(exposure));
/*      */       }
/*      */ 
/*      */       
/* 1056 */       if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.CENTRAL_CLEAR_FLAG)) && 
/* 1057 */         StrUtil.equals(TradingRole.MEMBER.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.TRADING_ROLE)) && 
/* 1058 */         !StrUtil.equals(QualCcp.QUAL_CCP.getCode(), DataUtils.getString(exposure, (ICodeEnum)RwaParam.QUAL_CCP_FLAG))) {
/*      */ 
/*      */         
/* 1061 */         paramMapping(exposure, null, schemeConfig, ParamTemplate.HE_DI);
/* 1062 */         if (DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.EAD_HAIRCUT) == null)
/*      */         {
/* 1064 */           DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.EAD_HAIRCUT, BigDecimal.ONE);
/*      */         }
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static BigDecimal mappingFactorLine(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/* 1072 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.FL_SFT);
/* 1073 */     paramMapping(data, null, paramVersionDo, ParamTemplate.FL_SFT);
/* 1074 */     BigDecimal f = DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.FACTOR_LINE);
/* 1075 */     if (f == null) {
/* 1076 */       if (RwaConfig.enableMappingExceptionOut) {
/* 1077 */         throw new ParamMappingException("证券融资交易折扣底线映射异常！Id=" + id);
/*      */       }
/* 1079 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.F_SFT);
/* 1080 */       f = BigDecimal.valueOf(0.1D);
/* 1081 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.FACTOR_LINE, f);
/*      */     } 
/* 1083 */     return f;
/*      */   }
/*      */   
/*      */   public static void mappingCvaRw(SchemeConfigDo schemeConfig, String jobId, InterfaceDataType dataType, Map<String, Object> data, String id) {
/* 1087 */     ParamVersionDo paramVersionDo = getParamVersion(schemeConfig, ParamTemplate.RW_CVA);
/* 1088 */     paramMapping(data, null, paramVersionDo, ParamTemplate.RW_CVA);
/* 1089 */     if (DataUtils.getBigDecimal(data, (ICodeEnum)RwaParam.CP_RW) == null) {
/* 1090 */       if (RwaConfig.enableMappingExceptionOut) {
/* 1091 */         throw new ParamMappingException("信用估值调整交易对手权重映射异常！Id=" + id);
/*      */       }
/* 1093 */       JobUtils.addErrorData(jobId, dataType, id, ExcDataCode.RW_CVA);
/* 1094 */       DataUtils.setBigDecimal(data, (ICodeEnum)RwaParam.CP_RW, paramVersionDo.getCreditRule().getDefaultCvaCpRw());
/*      */     } 
/*      */   }
/*      */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\RwaMapping.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */