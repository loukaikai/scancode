/*      */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*      */ import cn.hutool.core.collection.CollUtil;
/*      */ import cn.hutool.core.util.StrUtil;
/*      */ import com.amarsoft.rwa.engine.constant.ExposureType;
/*      */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*      */ import com.amarsoft.rwa.engine.constant.Identity;
/*      */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*      */ import com.amarsoft.rwa.engine.constant.TaskType;
/*      */ import com.amarsoft.rwa.engine.entity.AbsExposureDto;
/*      */ import com.amarsoft.rwa.engine.entity.AbsUnionDto;
/*      */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*      */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*      */ import com.amarsoft.rwa.engine.entity.MitigationDetailDto;
/*      */ import com.amarsoft.rwa.engine.entity.MitigationDto;
/*      */ import com.amarsoft.rwa.engine.entity.RelevanceDto;
/*      */ import com.amarsoft.rwa.engine.entity.TaskRangeDo;
/*      */ import com.amarsoft.rwa.engine.entity.UnionDto;
/*      */ import com.amarsoft.rwa.engine.util.DataUtils;
/*      */ import com.amarsoft.rwa.engine.util.RwaMath;
/*      */ import java.math.BigDecimal;
/*      */ import java.util.ArrayList;
/*      */ import java.util.HashMap;
/*      */ import java.util.List;
/*      */ import java.util.Map;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class RwaUtils {
/*   28 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.RwaUtils.class);
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*   34 */   public static Set<String> pseSet = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*   38 */   public static Set<String> sovSet = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*   42 */   public static Set<String> nbfSet = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*   46 */   public static Set<String> b2h0AssetSet = new HashSet<>();
/*      */ 
/*      */ 
/*      */   
/*   50 */   public static Set<String> b3h0AssetSet = new HashSet<>();
/*      */ 
/*      */   
/*      */   public static Map<String, String> ecColumnMap;
/*      */ 
/*      */ 
/*      */   
/*      */   static {
/*   58 */     sovSet.add(ClientType.SOV_SOV.getCode());
/*   59 */     sovSet.add(ClientType.SOV_CB.getCode());
/*      */     
/*   61 */     sovSet.add(ClientType.SOV_MOF.getCode());
/*   62 */     sovSet.add(ClientType.FSOV_SOV.getCode());
/*   63 */     sovSet.add(ClientType.FSOV_CB.getCode());
/*      */ 
/*      */     
/*   66 */     pseSet.add(ClientType.PSE_SOV_PL.getCode());
/*   67 */     pseSet.add(ClientType.PSE_SOV_RFCT.getCode());
/*   68 */     pseSet.add(ClientType.PSE_SOV_OTHER.getCode());
/*   69 */     pseSet.add(ClientType.PSE_FSOV.getCode());
/*      */     
/*   71 */     nbfSet.add(ClientType.NBF_AMC.getCode());
/*   72 */     nbfSet.add(ClientType.NBF_OTHER.getCode());
/*   73 */     nbfSet.add(ClientType.NBF_OS.getCode());
/*      */ 
/*      */ 
/*      */     
/*   77 */     b2h0AssetSet.add("1.1");
/*   78 */     b2h0AssetSet.add("2.1");
/*   79 */     b2h0AssetSet.add("2.2");
/*   80 */     b2h0AssetSet.add("2.3");
/*      */     
/*   82 */     b3h0AssetSet.add("1.1");
/*   83 */     b3h0AssetSet.add("2.1");
/*   84 */     b3h0AssetSet.add("2.2");
/*   85 */     b3h0AssetSet.add("2.3");
/*   86 */     b3h0AssetSet.add("2.9");
/*   87 */     b3h0AssetSet.add("3.1.1");
/*   88 */     b3h0AssetSet.add("4");
/*   89 */     b3h0AssetSet.add("6.1");
/*      */   }
/*      */   
/*      */   public static String getEcColumn(EcColumn ecColumn) {
/*   93 */     if (CollUtil.isEmpty(ecColumnMap) || ecColumn == null) {
/*   94 */       return "";
/*      */     }
/*   96 */     return ecColumnMap.get(ecColumn.getCode());
/*      */   }
/*      */   
/*      */   public static boolean isB3(TaskType taskType) {
/*  100 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$TaskType[taskType.ordinal()]) {
/*      */       case 1:
/*      */       case 2:
/*      */       case 3:
/*      */       case 4:
/*  105 */         return true;
/*      */       case 5:
/*      */       case 6:
/*      */       case 7:
/*      */       case 8:
/*  110 */         return false;
/*      */     } 
/*  112 */     throw new ParamConfigException("异常计算任务[taskType=" + taskType + "]");
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSingle(TaskType taskType) {
/*  117 */     if (taskType == TaskType.SINGLE || taskType == TaskType.SINGLE2) {
/*  118 */       return true;
/*      */     }
/*  120 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isImt(TaskType taskType) {
/*  124 */     if (taskType == TaskType.IMTASK || taskType == TaskType.IMTASK2) {
/*  125 */       return true;
/*      */     }
/*  127 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isIrb(String approach) {
/*  131 */     if (StrUtil.isEmpty(approach)) {
/*  132 */       return false;
/*      */     }
/*  134 */     return StrUtil.equals(approach.substring(0, 1), Approach.IRB.getCode());
/*      */   }
/*      */   
/*      */   public static String generateConditionSql(List<TaskRangeDo> rangeList, Identity flag) {
/*  138 */     if (CollUtil.isEmpty(rangeList)) {
/*  139 */       return null;
/*      */     }
/*      */     
/*  142 */     JobType jobType = (JobType)EnumUtils.getEnumByCode(((TaskRangeDo)rangeList.get(0)).getCreditRiskDataType(), JobType.class);
/*  143 */     StringBuilder sql = new StringBuilder();
/*  144 */     for (TaskRangeDo rangeDo : rangeList) {
/*  145 */       StringBuilder cond = null;
/*  146 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$JobType[jobType.ordinal()]) {
/*      */         
/*      */         case 1:
/*      */         case 2:
/*      */         case 3:
/*  151 */           cond = sumConditionSql(new StringBuilder[] {
/*  152 */                 generateConditionSql("e", RwaParam.ORG_ID, rangeDo.getOrgId()), 
/*  153 */                 generateConditionSql("e", RwaParam.INDUSTRY_ID, rangeDo.getIndustryId()), 
/*  154 */                 generateConditionSql("e", RwaParam.ASSET_TYPE, rangeDo.getAssetType())
/*      */               });
/*      */           break;
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         case 4:
/*  162 */           if (flag == null && StrUtil.equals(rangeDo.getIsOriginator(), Identity.YES.getCode())) {
/*  163 */             cond = generateConditionSql("p", RwaParam.ORG_ID, rangeDo.getOrgId()); break;
/*  164 */           }  if (flag == Identity.YES && StrUtil.equals(rangeDo.getIsOriginator(), flag.getCode())) {
/*  165 */             cond = generateConditionSql("p", RwaParam.ORG_ID, rangeDo.getOrgId()); break;
/*  166 */           }  if (flag == Identity.NO && StrUtil.equals(rangeDo.getIsOriginator(), flag.getCode())) {
/*  167 */             cond = generateConditionSql("a", RwaParam.ORG_ID, rangeDo.getOrgId());
/*      */           }
/*      */           break;
/*      */         
/*      */         case 5:
/*      */         case 6:
/*  173 */           if (flag == Identity.YES && StrUtil.equals(rangeDo.getNettingFlag(), flag.getCode())) {
/*  174 */             cond = sumConditionSql(new StringBuilder[] {
/*  175 */                   generateConditionSql("n", RwaParam.ORG_ID, rangeDo.getOrgId()), 
/*  176 */                   generateConditionSql("n", RwaParam.INDUSTRY_ID, rangeDo.getIndustryId()) }); break;
/*      */           } 
/*  178 */           if (flag == Identity.NO && StrUtil.equals(rangeDo.getNettingFlag(), flag.getCode())) {
/*  179 */             cond = sumConditionSql(new StringBuilder[] {
/*  180 */                   generateConditionSql("e", RwaParam.ORG_ID, rangeDo.getOrgId()), 
/*  181 */                   generateConditionSql("e", RwaParam.INDUSTRY_ID, rangeDo.getIndustryId()), 
/*  182 */                   generateConditionSql("e", RwaParam.ASSET_TYPE, rangeDo.getAssetType())
/*      */                 });
/*      */           }
/*      */           break;
/*      */         default:
/*  187 */           throw new ParamConfigException("计算范围配置异常， 数据类型设置异常，TaskRangeDo=" + JsonUtils.object2Json(rangeDo));
/*      */       } 
/*      */       
/*  190 */       if (StrUtil.isEmpty(cond)) {
/*      */         
/*  192 */         if (jobType != JobType.DI && jobType != JobType.SFT && jobType != JobType.ABS) {
/*  193 */           throw new ParamConfigException("计算范围配置异常， 条件设置异常，TaskRangeDo=" + JsonUtils.object2Json(rangeDo));
/*      */         }
/*      */         continue;
/*      */       } 
/*  197 */       if (sql.length() > 0) {
/*  198 */         sql.append(" or ");
/*      */       }
/*  200 */       sql.append(" (").append(cond).append(") ");
/*      */     } 
/*      */     
/*  203 */     if (StrUtil.isEmpty(sql)) {
/*  204 */       return null;
/*      */     }
/*      */ 
/*      */     
/*  208 */     if (jobType == JobType.ABS && flag == null) {
/*  209 */       return " and exists(select 1 from RWA_EI_ABS_Product p where p.data_batch_no = #{dataBatchNo} and p.is_originator = '1' and " + 
/*  210 */         sql + 
/*  211 */         ") ";
/*      */     }
/*      */     
/*  214 */     return " and (" + sql + ") ";
/*      */   }
/*      */   
/*      */   public static StringBuilder sumConditionSql(StringBuilder... conds) {
/*  218 */     StringBuilder sql = new StringBuilder();
/*  219 */     for (StringBuilder cond : conds) {
/*  220 */       if (!StrUtil.isEmpty(cond)) {
/*      */ 
/*      */         
/*  223 */         if (sql.length() > 0) {
/*  224 */           sql.append(" and ");
/*      */         }
/*  226 */         sql.append(cond);
/*      */       } 
/*  228 */     }  return sql;
/*      */   }
/*      */ 
/*      */   
/*      */   public static StringBuilder generateConditionSql(String tableAlias, RwaParam param, String value) {
/*  233 */     if (StrUtil.isEmpty(value)) {
/*  234 */       return null;
/*      */     }
/*  236 */     if (StrUtil.isEmpty(tableAlias)) {
/*  237 */       tableAlias = "e";
/*      */     }
/*  239 */     StringBuilder sql = new StringBuilder(" ");
/*  240 */     sql.append(tableAlias).append(".").append(param.getCode()).append(" like '").append(value).append("%' ");
/*  241 */     return sql;
/*      */   }
/*      */   
/*      */   public static Map<String, RelevanceDto> getRelevanceMap(Map<String, Map<String, RelevanceDto>> exposureRelevanceMap, String exposureId) {
/*  245 */     if (CollUtil.isEmpty(exposureRelevanceMap)) {
/*  246 */       return null;
/*      */     }
/*  248 */     return exposureRelevanceMap.get(exposureId);
/*      */   }
/*      */   
/*      */   public static RelevanceDto getRelevance(Map<String, Map<String, RelevanceDto>> exposureRelevanceMap, String exposureId, String mitigationId) {
/*  252 */     Map<String, RelevanceDto> map = getRelevanceMap(exposureRelevanceMap, exposureId);
/*  253 */     if (CollUtil.isEmpty(map)) {
/*  254 */       return null;
/*      */     }
/*  256 */     return map.get(mitigationId);
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<BigDecimal, MitigationDto> getRelMitigationMap(Map<String, MitigationDto> mitigationMap, Map<String, Map<String, RelevanceDto>> exposureRelevanceMap, String exposureId, boolean isIrb, Set<String> mitigationMainTypeSet) {
/*  262 */     return getRelMitigationMap(mitigationMap, getRelevanceMap(exposureRelevanceMap, exposureId), isIrb, mitigationMainTypeSet);
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<BigDecimal, MitigationDto> getRelMitigationMap(Map<String, MitigationDto> mitigationMap, Map<String, RelevanceDto> relevanceMap, boolean isIrb, Set<String> mitigationMainTypeSet) {
/*  267 */     Map<BigDecimal, MitigationDto> map = new TreeMap<>();
/*  268 */     if (CollUtil.isEmpty(mitigationMap) || CollUtil.isEmpty(relevanceMap)) {
/*  269 */       return map;
/*      */     }
/*      */     
/*  272 */     for (RelevanceDto relevanceDto : relevanceMap.values()) {
/*  273 */       MitigationDto mitigationDto = mitigationMap.get(relevanceDto.getMitigationId());
/*      */       
/*  275 */       if (mitigationDto != null && NumberUtil.isGreater(mitigationDto.getUnmitigatedAmount(), BigDecimal.ZERO)) {
/*      */         
/*  277 */         if (StrUtil.equals(relevanceDto.getIsPositiveCorrelation(), Identity.YES.getCode())) {
/*      */           continue;
/*      */         }
/*      */         
/*  281 */         if (!CollUtil.isEmpty(mitigationMainTypeSet) && !mitigationMainTypeSet.contains(mitigationDto.getMitigationMainType())) {
/*      */           continue;
/*      */         }
/*      */         
/*  285 */         if (isIrb) {
/*  286 */           if (StrUtil.equals(mitigationDto.getQualFlagFirb(), Identity.YES.getCode()))
/*  287 */             map.put(mitigationDto.getSortNo(), mitigationDto); 
/*      */           continue;
/*      */         } 
/*  290 */         if (StrUtil.equals(mitigationDto.getQualFlagWa(), Identity.YES.getCode())) {
/*  291 */           map.put(mitigationDto.getSortNo(), mitigationDto);
/*      */         }
/*      */       } 
/*      */     } 
/*      */     
/*  296 */     return map;
/*      */   }
/*      */   
/*      */   public static Map<BigDecimal, ExposureDto> getRelExposureMap(UnionDto unionDto, String mitigationId) {
/*  300 */     Map<BigDecimal, ExposureDto> map = new TreeMap<>();
/*  301 */     List<RelevanceDto> list = (List<RelevanceDto>)unionDto.getMitigationRelevanceMap().get(mitigationId);
/*  302 */     if (!CollUtil.isEmpty(list)) {
/*  303 */       for (RelevanceDto relevanceDto : list) {
/*  304 */         ExposureDto exposureDto = (ExposureDto)unionDto.getExposureMap().get(relevanceDto.getExposureId());
/*  305 */         if (exposureDto != null) {
/*  306 */           map.put(exposureDto.getSortNo(), exposureDto);
/*      */         }
/*      */       } 
/*      */     }
/*  310 */     return map;
/*      */   }
/*      */   
/*      */   public static ExposureDto getRelExposure(UnionDto unionDto, String mitigationId) {
/*  314 */     List<RelevanceDto> list = (List<RelevanceDto>)unionDto.getMitigationRelevanceMap().get(mitigationId);
/*  315 */     return (ExposureDto)unionDto.getExposureMap().get(((RelevanceDto)list.get(0)).getExposureId());
/*      */   }
/*      */   
/*      */   public static Map<String, List<ExposureDto>> initUnifyExposureListMap(Map<String, List<ExposureDto>> unifyExposureListMap, ExposureDto exposureDto, ExposureApproach approach) {
/*  319 */     String key = getUnifyExposureKey(exposureDto, approach);
/*  320 */     List<ExposureDto> list = unifyExposureListMap.get(key);
/*  321 */     if (list == null) {
/*  322 */       list = new ArrayList<>();
/*  323 */       unifyExposureListMap.put(key, list);
/*      */     } 
/*  325 */     list.add(exposureDto);
/*  326 */     return unifyExposureListMap;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getUnifyExposureKey(ExposureDto exposureDto, ExposureApproach approach) {
/*  331 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$ExposureApproach[approach.ordinal()]) {
/*      */       case 1:
/*  333 */         return DataUtils.getRoundName(exposureDto.getRw(), 6);
/*      */       case 2:
/*  335 */         return DataUtils.getRoundName(exposureDto.getKcr(), 16);
/*      */     } 
/*  337 */     throw new ParamConfigException("非法计算方法");
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<ExposureDto> getUnifyExposureList(Map<String, List<ExposureDto>> unifyExposureListMap, ExposureDto crtExposureDto, ExposureApproach approach) {
/*  342 */     List<ExposureDto> unifyExposureList = unifyExposureListMap.get(getUnifyExposureKey(crtExposureDto, approach));
/*  343 */     List<ExposureDto> list = new ArrayList<>();
/*  344 */     for (ExposureDto exposureDto : unifyExposureList) {
/*  345 */       if (NumberUtil.isGreater(exposureDto.getSortNo(), crtExposureDto.getSortNo()) && !RwaMath.isZero(exposureDto.getCurrentAmount()))
/*      */       {
/*      */         
/*  348 */         list.add(exposureDto);
/*      */       }
/*      */     } 
/*  351 */     return list;
/*      */   }
/*      */   
/*      */   public static Map<BigDecimal, ExposureDto> getMitiUnifyExposureMap(List<ExposureDto> unifyExposureList, Map<BigDecimal, ExposureDto> exposureDtoMap) {
/*  355 */     Map<BigDecimal, ExposureDto> map = new TreeMap<>();
/*  356 */     for (ExposureDto exposureDto : exposureDtoMap.values()) {
/*  357 */       if (exposureDto != null && unifyExposureList.contains(exposureDto)) {
/*  358 */         map.put(exposureDto.getSortNo(), exposureDto);
/*      */       }
/*      */     } 
/*  361 */     return map;
/*      */   }
/*      */   
/*      */   public static List<MitigateSortDo> getMitigateSortList(MitigateAssetDo mitigateAssetDo, MitigateSortType mitigateSortType) {
/*  365 */     if (mitigateAssetDo == null) {
/*  366 */       return null;
/*      */     }
/*  368 */     return (List<MitigateSortDo>)mitigateAssetDo.getMitigateSortListMap().get(mitigateSortType);
/*      */   }
/*      */   
/*      */   public static Map<String, Integer> getMitigationTypeSortMap(MitigateAssetDo mitigateAssetDo) {
/*  372 */     if (mitigateAssetDo == null) {
/*  373 */       return null;
/*      */     }
/*  375 */     return mitigateAssetDo.getMitigationTypeSortMap();
/*      */   }
/*      */   
/*      */   public static MitigateMethod getMitigateMethod(MitigateAssetDo mitigateAssetDo) {
/*  379 */     if (mitigateAssetDo == null || StrUtil.isEmpty(mitigateAssetDo.getMitigateMethod())) {
/*  380 */       return MitigateMethod.SORT;
/*      */     }
/*  382 */     return (MitigateMethod)EnumUtils.getEnumByCode(mitigateAssetDo.getMitigateMethod(), MitigateMethod.class);
/*      */   }
/*      */   
/*      */   public static boolean isNonRetailExposure(String exposureTypeIrb) {
/*  386 */     return isNonRetailExposure((ExposureType)EnumUtils.getEnumByCode(RwaConfig.getExposureMainType(exposureTypeIrb), ExposureType.class));
/*      */   }
/*      */   
/*      */   public static boolean isNonRetailExposure(ExposureType exposureType) {
/*  390 */     if (exposureType == ExposureType.SOVEREIGNS || exposureType == ExposureType.BANKS || exposureType == ExposureType.CORPORATE)
/*      */     {
/*  392 */       return true;
/*      */     }
/*  394 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isRetailExposure(String exposureTypeIrb) {
/*  398 */     return isRetailExposure((ExposureType)EnumUtils.getEnumByCode(RwaConfig.getExposureMainType(exposureTypeIrb), ExposureType.class));
/*      */   }
/*      */   
/*      */   public static boolean isRetailExposure(ExposureType exposureType) {
/*  402 */     return (exposureType == ExposureType.RETAIL);
/*      */   }
/*      */   
/*      */   public static boolean isOtherExposure(String exposureTypeIrb) {
/*  406 */     return isOtherExposure((ExposureType)EnumUtils.getEnumByCode(RwaConfig.getExposureMainType(exposureTypeIrb), ExposureType.class));
/*      */   }
/*      */   
/*      */   public static boolean isOtherExposure(ExposureType exposureType) {
/*  410 */     if (exposureType == ExposureType.EQUITY || exposureType == ExposureType.ABS || exposureType == ExposureType.OTHER) {
/*  411 */       return true;
/*      */     }
/*  413 */     return false;
/*      */   }
/*      */   
/*      */   public static boolean isLargeEnt(BigDecimal s) {
/*  417 */     if (s == null) {
/*  418 */       return false;
/*      */     }
/*  420 */     return NumberUtil.isGreater(s, BigDecimal.valueOf(3.0E9D));
/*      */   }
/*      */ 
/*      */   
/*      */   public static void aggregate2ExposureResult(MitigationDetailDto detailDto, ExposureDto exposureDto, MitigationDto mitigationDto) {
/*  425 */     if (StrUtil.equals(detailDto.getIsResult(), Identity.NO.getCode())) {
/*      */       return;
/*      */     }
/*      */     
/*  429 */     MitigatedFlag mitigatedFlag = (MitigatedFlag)EnumUtils.getEnumByCode(detailDto.getMitigatedFlag(), MitigatedFlag.class);
/*  430 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$MitigatedFlag[mitigatedFlag.ordinal()]) {
/*      */       
/*      */       case 1:
/*  433 */         exposureDto.setUncoveredEa(RwaMath.add(exposureDto.getUncoveredEa(), detailDto.getCoveredEa()));
/*  434 */         exposureDto.setRwaUm(RwaMath.add(exposureDto.getRwaUm(), detailDto.getRwaMa()));
/*      */         
/*  436 */         exposureDto.setWlgdEad(RwaMath.add(exposureDto.getWlgdEad(), RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedLgd())));
/*      */         break;
/*      */       
/*      */       case 2:
/*  440 */         exposureDto.setCoveredEa(RwaMath.add(exposureDto.getCoveredEa(), detailDto.getCoveredEa()));
/*      */         
/*  442 */         exposureDto.setWlgdEad(RwaMath.add(exposureDto.getWlgdEad(), RwaMath.mul(detailDto.getMitigatedEa(), detailDto.getMitigatedLgd())));
/*      */         
/*  444 */         mitigationDto.setMitigatedAmount(RwaMath.add(mitigationDto.getMitigatedAmount(), detailDto.getMitigationUseAmount()));
/*  445 */         mitigationDto.setCoveredEa(RwaMath.add(mitigationDto.getCoveredEa(), detailDto.getCoveredEa()));
/*  446 */         mitigationDto.setMitigatedEffect(RwaMath.add(mitigationDto
/*  447 */               .getMitigatedEffect(), RwaMath.sub(detailDto.getRwaMb(), detailDto.getRwaMa())));
/*      */ 
/*      */         
/*  450 */         mitigationDto.setUnmitigatedAmount(RwaMath.sub(mitigationDto
/*  451 */               .getMitigationAmount(), mitigationDto.getMitigatedAmount()));
/*      */         break;
/*      */     } 
/*      */ 
/*      */     
/*  456 */     exposureDto.setRwaMa(RwaMath.add(exposureDto.getRwaMa(), detailDto.getRwaMa()));
/*  457 */     exposureDto.setEla(RwaMath.add(exposureDto.getEla(), detailDto.getEla()));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isLargeOfMitigateLogCount(String mitigationId, String reason, int detailSize, Map<String, Integer> mitigateLogCountMap) {
/*  462 */     String key = DataUtils.generateKey(new String[] { mitigationId, reason });
/*  463 */     Integer count = mitigateLogCountMap.get(key);
/*  464 */     if (detailSize >= 40000 && count != null && count.intValue() > 3) {
/*  465 */       return true;
/*      */     }
/*  467 */     mitigateLogCountMap.put(key, Integer.valueOf(RwaMath.nvl(count, 0) + 1));
/*  468 */     return false;
/*      */   }
/*      */   
/*      */   public static BigDecimal getValueFc(String rwValueFcType) {
/*  472 */     return RwaMath.div(NumberUtil.toBigDecimal(rwValueFcType), Integer.valueOf(100));
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSov(String clientType) {
/*  477 */     return sovSet.contains(clientType);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isPse(String clientType) {
/*  482 */     return pseSet.contains(clientType);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNonBank(String clientType) {
/*  487 */     return nbfSet.contains(clientType);
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isNotGovBond(String instrumentsType, String clientType) {
/*  492 */     if (InstrumentsType.BOND.getCode().equals(instrumentsType))
/*      */     {
/*  494 */       return !isSov(clientType);
/*      */     }
/*      */     
/*  497 */     if (InstrumentsType.CASH.getCode().equals(instrumentsType)) {
/*  498 */       return false;
/*      */     }
/*      */     
/*  501 */     return true;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isSecurity(String instrumentsType) {
/*  506 */     if (StrUtil.equals(instrumentsType, InstrumentsType.CASH.getCode()) || 
/*  507 */       StrUtil.equals(instrumentsType, InstrumentsType.MARGIN.getCode()) || 
/*  508 */       StrUtil.equals(instrumentsType, InstrumentsType.GOLD.getCode()) || 
/*  509 */       StrUtil.equals(instrumentsType, InstrumentsType.BDR.getCode()))
/*      */     {
/*  511 */       return false;
/*      */     }
/*  513 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean isH0AssetB3ByFcType(String rwValueFcType) {
/*  517 */     if (StrUtil.isEmpty(rwValueFcType) || 
/*  518 */       RwaMath.isNullOrNegative(NumberUtil.toBigDecimal(rwValueFcType))) {
/*  519 */       return false;
/*      */     }
/*  521 */     return true;
/*      */   }
/*      */   
/*      */   public static boolean isH0AssetB3(String rptItemNo) {
/*  525 */     return b3h0AssetSet.contains(rptItemNo);
/*      */   }
/*      */   
/*      */   public static boolean isH0AssetB2(String rptItemNo) {
/*  529 */     return b2h0AssetSet.contains(rptItemNo);
/*      */   }
/*      */   
/*      */   public static boolean isH0Asset(TaskType taskType, String rptItemNo) {
/*  533 */     if (isB3(taskType)) {
/*  534 */       return isH0AssetB3(rptItemNo);
/*      */     }
/*  536 */     return isH0AssetB2(rptItemNo);
/*      */   }
/*      */   
/*      */   public static boolean isH0Transaction(int tsMaturity, BigDecimal exposureFrequency, BigDecimal collateralFrequency) {
/*  540 */     if (exposureFrequency == null || collateralFrequency == null) {
/*  541 */       return false;
/*      */     }
/*  543 */     return isH0Transaction(tsMaturity, exposureFrequency.intValue(), collateralFrequency.intValue());
/*      */   }
/*      */   
/*      */   public static boolean isH0Transaction(int tsMaturity, int exposureFrequency, int collateralFrequency) {
/*  547 */     if (tsMaturity <= 7 || (exposureFrequency == 1 && collateralFrequency == 1)) {
/*  548 */       return true;
/*      */     }
/*  550 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static String confirmReCurrencyMismatch(String currency, String incomeCurrency) {
/*  555 */     if (StrUtil.isEmpty(currency) || StrUtil.isEmpty(incomeCurrency) || StrUtil.equals(currency, incomeCurrency)) {
/*  556 */       return Identity.NO.getCode();
/*      */     }
/*  558 */     return Identity.YES.getCode();
/*      */   }
/*      */   
/*      */   public static ExposureDto convert2NrExposure(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/*  562 */     Map<String, String> mappings = new HashMap<>();
/*  563 */     DataUtils.setMapping(mappings, "group_id");
/*  564 */     DataUtils.setMapping(mappings, "approach");
/*  565 */     DataUtils.setMapping(mappings, "exposure_id");
/*  566 */     DataUtils.setMapping(mappings, "contract_id");
/*  567 */     DataUtils.setMapping(mappings, "client_id");
/*  568 */     DataUtils.setMapping(mappings, "client_type");
/*  569 */     DataUtils.setMapping(mappings, "annual_sale");
/*  570 */     DataUtils.setMapping(mappings, "core_market_party_flag");
/*  571 */     DataUtils.setMapping(mappings, "org_id");
/*  572 */     DataUtils.setMapping(mappings, "asset_type");
/*  573 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/*  574 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/*  575 */     DataUtils.setMapping(mappings, "exposure_main_type_irb");
/*  576 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/*  577 */     DataUtils.setMapping(mappings, "off_rpt_item_wa");
/*  578 */     DataUtils.setMapping(mappings, "sprv_rating");
/*  579 */     DataUtils.setMapping(mappings, "exposure_belong");
/*  580 */     DataUtils.setMapping(mappings, "book_type");
/*  581 */     DataUtils.setMapping(mappings, "sprv_tran_type");
/*  582 */     DataUtils.setMapping(mappings, "sib_flag");
/*  583 */     DataUtils.setMapping(mappings, "reva_frequency");
/*  584 */     DataUtils.setMapping(mappings, "currency");
/*  585 */     DataUtils.setMapping(mappings, "asset_balance");
/*  586 */     DataUtils.setMapping(mappings, "start_date");
/*  587 */     DataUtils.setMapping(mappings, "due_date");
/*  588 */     DataUtils.setMapping(mappings, "original_maturity");
/*  589 */     DataUtils.setMapping(mappings, "residual_maturity");
/*  590 */     DataUtils.setMapping(mappings, "provision");
/*  591 */     DataUtils.setMapping(mappings, "provision_prop");
/*  592 */     DataUtils.setMapping(mappings, "provision_ded");
/*  593 */     DataUtils.setMapping(mappings, "claims_level");
/*  594 */     DataUtils.setMapping(mappings, "risk_classify");
/*  595 */     DataUtils.setMapping(mappings, "exposure_status");
/*  596 */     DataUtils.setMapping(mappings, "off_business_type");
/*  597 */     DataUtils.setMapping(mappings, "off_business_subtype");
/*  598 */     DataUtils.setMapping(mappings, "ltv");
/*  599 */     DataUtils.setMapping(mappings, "amp_flag");
/*  600 */     DataUtils.setMapping(mappings, "amp_id");
/*  601 */     DataUtils.setMapping(mappings, "amp_type");
/*  602 */     DataUtils.setMapping(mappings, "amp_approach");
/*  603 */     DataUtils.setMapping(mappings, "amp_book_type");
/*  604 */     DataUtils.setMapping(mappings, "amp_belong");
/*  605 */     DataUtils.setMapping(mappings, "amp_lr");
/*  606 */     DataUtils.setMapping(mappings, "is_tp_calc");
/*  607 */     DataUtils.setMapping(mappings, "abs_ua_flag");
/*  608 */     DataUtils.setMapping(mappings, "abs_product_id");
/*  609 */     DataUtils.setMapping(mappings, "ccf");
/*  610 */     DataUtils.setMapping(mappings, "rw");
/*  611 */     DataUtils.setMapping(mappings, "default_flag");
/*  612 */     DataUtils.setMapping(mappings, "beel");
/*  613 */     DataUtils.setMapping(mappings, "default_lgd");
/*  614 */     DataUtils.setMapping(mappings, "model_id");
/*  615 */     DataUtils.setMapping(mappings, "rating");
/*  616 */     DataUtils.setMapping(mappings, "pd");
/*  617 */     DataUtils.setMapping(mappings, "lgd");
/*  618 */     DataUtils.setMapping(mappings, "maturity");
/*  619 */     DataUtils.setMapping(mappings, "tm");
/*  620 */     DataUtils.setMapping(mappings, "sh", "she");
/*  621 */     DataUtils.setMapping(mappings, "he");
/*  622 */     DataUtils.setMapping(mappings, "sa");
/*  623 */     DataUtils.setMapping(mappings, "rel");
/*  624 */     DataUtils.setMapping(mappings, "bma");
/*  625 */     DataUtils.setMapping(mappings, "kcr");
/*  626 */     DataUtils.setMapping(mappings, "el");
/*  627 */     DataUtils.setMapping(mappings, "formula_no");
/*  628 */     DataUtils.setMapping(mappings, "ead_airb", "eadIrb");
/*  629 */     DataUtils.setMapping(mappings, "net_asset");
/*  630 */     DataUtils.setMapping(mappings, "amp_under_asset_rst");
/*  631 */     ExposureDto exposureDto = (ExposureDto)DataUtils.dataMapping(ExposureDto.class, data, mappings);
/*  632 */     exposureDto.setExposure(data);
/*  633 */     exposureDto.setIsAlone(Identity.NO.getCode());
/*  634 */     return exposureDto;
/*      */   }
/*      */   
/*      */   public static ExposureDto convert2RetailExposure(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/*  638 */     String approach = DataUtils.getString(data, (ICodeEnum)RwaParam.APPROACH);
/*  639 */     Map<String, String> mappings = new HashMap<>();
/*  640 */     DataUtils.setMapping(mappings, "group_id");
/*  641 */     DataUtils.setMapping(mappings, "approach");
/*  642 */     DataUtils.setMapping(mappings, "exposure_id");
/*  643 */     DataUtils.setMapping(mappings, "contract_id");
/*  644 */     DataUtils.setMapping(mappings, "client_id");
/*  645 */     DataUtils.setMapping(mappings, "org_id");
/*  646 */     DataUtils.setMapping(mappings, "asset_type");
/*  647 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/*  648 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/*  649 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/*  650 */     DataUtils.setMapping(mappings, "off_rpt_item_wa");
/*  651 */     DataUtils.setMapping(mappings, "exposure_belong");
/*  652 */     DataUtils.setMapping(mappings, "book_type");
/*  653 */     DataUtils.setMapping(mappings, "currency");
/*  654 */     DataUtils.setMapping(mappings, "asset_balance");
/*  655 */     DataUtils.setMapping(mappings, "total_limit");
/*  656 */     DataUtils.setMapping(mappings, "original_maturity");
/*  657 */     DataUtils.setMapping(mappings, "residual_maturity");
/*  658 */     DataUtils.setMapping(mappings, "provision");
/*  659 */     DataUtils.setMapping(mappings, "provision_prop");
/*  660 */     DataUtils.setMapping(mappings, "provision_ded");
/*  661 */     DataUtils.setMapping(mappings, "risk_classify");
/*  662 */     DataUtils.setMapping(mappings, "exposure_status");
/*  663 */     DataUtils.setMapping(mappings, "off_business_type");
/*  664 */     DataUtils.setMapping(mappings, "off_business_subtype");
/*  665 */     DataUtils.setMapping(mappings, "ltv");
/*  666 */     DataUtils.setMapping(mappings, "abs_ua_flag");
/*  667 */     DataUtils.setMapping(mappings, "abs_product_id");
/*  668 */     DataUtils.setMapping(mappings, "amp_flag");
/*  669 */     DataUtils.setMapping(mappings, "amp_id");
/*  670 */     DataUtils.setMapping(mappings, "amp_type");
/*  671 */     DataUtils.setMapping(mappings, "amp_approach");
/*  672 */     DataUtils.setMapping(mappings, "amp_book_type");
/*  673 */     DataUtils.setMapping(mappings, "amp_belong");
/*  674 */     DataUtils.setMapping(mappings, "amp_lr");
/*  675 */     DataUtils.setMapping(mappings, "is_tp_calc");
/*  676 */     DataUtils.setMapping(mappings, "net_asset");
/*  677 */     DataUtils.setMapping(mappings, "ccf");
/*  678 */     DataUtils.setMapping(mappings, "rw");
/*  679 */     DataUtils.setMapping(mappings, "default_flag");
/*      */     
/*  681 */     if (StrUtil.equals(ExposureApproach.RIRB.getCode(), approach)) {
/*  682 */       DataUtils.setMapping(mappings, "beel");
/*  683 */       DataUtils.setMapping(mappings, "default_lgd");
/*  684 */       DataUtils.setMapping(mappings, "pd_model_id");
/*  685 */       DataUtils.setMapping(mappings, "pd_pool_id");
/*  686 */       DataUtils.setMapping(mappings, "pd");
/*  687 */       DataUtils.setMapping(mappings, "lgd_model_id");
/*  688 */       DataUtils.setMapping(mappings, "lgd_pool_id");
/*  689 */       DataUtils.setMapping(mappings, "lgd");
/*  690 */       DataUtils.setMapping(mappings, "ccf_model_id");
/*  691 */       DataUtils.setMapping(mappings, "ccf_pool_id");
/*  692 */       DataUtils.setMapping(mappings, "rel");
/*  693 */       DataUtils.setMapping(mappings, "kcr");
/*  694 */       DataUtils.setMapping(mappings, "formula_no");
/*  695 */       DataUtils.setMapping(mappings, "ead_irb");
/*      */     } 
/*  697 */     ExposureDto exposureDto = (ExposureDto)DataUtils.dataMapping(ExposureDto.class, data, mappings);
/*  698 */     exposureDto.setExposure(data);
/*  699 */     exposureDto.setIsAlone(Identity.NO.getCode());
/*  700 */     return exposureDto;
/*      */   }
/*      */   
/*      */   public static String getIsAlone(BigDecimal exposureCount) {
/*  704 */     if (NumberUtil.equals(exposureCount, BigDecimal.ONE)) {
/*  705 */       return Identity.YES.getCode();
/*      */     }
/*  707 */     return Identity.NO.getCode();
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Object> beanToMap(UnionDto unionDto) {
/*  712 */     Map<String, Object> map = new HashMap<>();
/*  713 */     map.put("ID", unionDto.getId());
/*  714 */     map.put("SIZE", Integer.valueOf(unionDto.getSize()));
/*  715 */     map.put("schemeId", unionDto.getSchemeConfig().getSchemeId());
/*  716 */     map.put("approach", unionDto.getApproach());
/*  717 */     map.put("mitigationSize", Integer.valueOf(unionDto.getMitigationSize()));
/*  718 */     map.put("relevanceSize", Integer.valueOf(unionDto.getRelevanceSize()));
/*  719 */     map.put(ResultDataType.DETAIL.getCode(), unionDto.getDetailResultList());
/*  720 */     map.put(ResultDataType.EXPOSURE.getCode(), unionDto.getExposureResultList());
/*  721 */     map.put(ResultDataType.MITIGATION.getCode(), unionDto.getMitigationDtoList());
/*  722 */     map.put(ResultDataType.AMP.getCode(), unionDto.getAmpResultList());
/*  723 */     return map;
/*      */   }
/*      */   
/*      */   public static Map<String, Object> beanToMap(DiUnionDto unionDto) {
/*  727 */     Map<String, Object> map = new HashMap<>();
/*  728 */     map.put("ID", unionDto.getId());
/*  729 */     map.put("SIZE", Integer.valueOf(unionDto.getSize()));
/*  730 */     map.put("schemeId", unionDto.getSchemeConfig().getSchemeId());
/*  731 */     map.put("approach", unionDto.getApproach().getCode());
/*  732 */     map.put("nettingFlag", unionDto.getNettingFlag().getCode());
/*  733 */     map.put(ResultDataType.DI_NETTING.getCode(), unionDto.getNettingList());
/*  734 */     map.put(ResultDataType.DI_EXPOSURE.getCode(), unionDto.getExposureList());
/*  735 */     map.put(ResultDataType.DI_COLLATERAL.getCode(), unionDto.getCollateralList());
/*  736 */     map.put(ResultDataType.DI_INTERMEDIATE.getCode(), unionDto.getIntermediateList());
/*  737 */     return map;
/*      */   }
/*      */   
/*      */   public static Map<String, Object> beanToMap(SftUnionDto unionDto) {
/*  741 */     Map<String, Object> map = new HashMap<>();
/*  742 */     map.put("ID", unionDto.getId());
/*  743 */     map.put("SIZE", Integer.valueOf(unionDto.getSize()));
/*  744 */     map.put("schemeId", unionDto.getSchemeConfig().getSchemeId());
/*  745 */     map.put("approach", unionDto.getApproach().getCode());
/*  746 */     map.put("nettingFlag", unionDto.getNettingFlag().getCode());
/*  747 */     map.put(ResultDataType.SFT_NETTING.getCode(), unionDto.getNettingList());
/*  748 */     map.put(ResultDataType.SFT_EXPOSURE.getCode(), unionDto.getExposureList());
/*  749 */     map.put(ResultDataType.SFT_COLLATERAL.getCode(), unionDto.getCollateralList());
/*  750 */     return map;
/*      */   }
/*      */   
/*      */   public static UnionDto convert2Exposure(Map<String, Object> item, UnionDto unionDto, boolean isRetail) throws InstantiationException, IllegalAccessException {
/*  754 */     List<Map<String, Object>> list = (List<Map<String, Object>>)item.get(InterfaceDataType.EXPOSURE.getCode());
/*  755 */     return convert2Exposure(list, unionDto, isRetail);
/*      */   }
/*      */   
/*      */   public static UnionDto convert2Exposure(List<Map<String, Object>> list, UnionDto unionDto, boolean isRetail) throws InstantiationException, IllegalAccessException {
/*  759 */     List<ExposureDto> exposureDtoList = new ArrayList<>(list.size());
/*  760 */     Map<String, ExposureDto> exposureDtoMap = new HashMap<>(list.size());
/*  761 */     for (Map<String, Object> exposure : list) {
/*  762 */       ExposureDto exposureDto = convert2Exposure(exposure, isRetail);
/*  763 */       exposureDtoList.add(exposureDto);
/*  764 */       exposureDtoMap.put(exposureDto.getExposureId(), exposureDto);
/*      */     } 
/*  766 */     unionDto.setSize(list.size());
/*  767 */     unionDto.setExposureDtoList(exposureDtoList);
/*  768 */     unionDto.setExposureMap(exposureDtoMap);
/*  769 */     unionDto.setExposureResultList(new ArrayList(list.size()));
/*  770 */     unionDto.setAmpResultList(new ArrayList());
/*      */     
/*  772 */     unionDto.setDetailResultList(new ArrayList(list.size() * 2));
/*  773 */     unionDto.setDetailResultMap(new HashMap<>(list.size() * 2));
/*  774 */     unionDto.setUnifyExposureListMap(new HashMap<>(list.size()));
/*  775 */     return unionDto;
/*      */   }
/*      */   
/*      */   public static ExposureDto convert2Exposure(Map<String, Object> exposure, boolean isRetail) throws InstantiationException, IllegalAccessException {
/*  779 */     if (isRetail) {
/*  780 */       return convert2RetailExposure(exposure);
/*      */     }
/*  782 */     return convert2NrExposure(exposure);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static UnionDto convert2Mitigation(Map<String, Object> item, UnionDto unionDto) throws InstantiationException, IllegalAccessException {
/*  789 */     if (unionDto.getMitigationSize() == 0 || unionDto.getRelevanceSize() == 0) {
/*      */ 
/*      */       
/*  792 */       unionDto.setMitigationMap(new HashMap<>());
/*  793 */       return unionDto;
/*      */     } 
/*      */     
/*  796 */     List<Map<String, Object>> collateralList = (List<Map<String, Object>>)item.get(InterfaceDataType.COLLATERAL.getCode());
/*  797 */     List<Map<String, Object>> guaranteeList = (List<Map<String, Object>>)item.get(InterfaceDataType.GUARANTEE.getCode());
/*      */     
/*  799 */     List<MitigationDto> mitigationDtoList = new ArrayList<>(unionDto.getMitigationSize());
/*  800 */     Map<String, MitigationDto> mitigationDtoMap = new HashMap<>(unionDto.getMitigationSize());
/*  801 */     if (!CollUtil.isEmpty(collateralList)) {
/*  802 */       for (Map<String, Object> collateral : collateralList) {
/*  803 */         MitigationDto mitigationDto = convert2Collateral(collateral);
/*  804 */         mitigationDtoList.add(mitigationDto);
/*  805 */         mitigationDtoMap.put(mitigationDto.getMitigationId(), mitigationDto);
/*      */       } 
/*      */     }
/*  808 */     if (!CollUtil.isEmpty(guaranteeList)) {
/*  809 */       for (Map<String, Object> guarantee : guaranteeList) {
/*  810 */         MitigationDto mitigationDto = convert2Guarantee(guarantee);
/*  811 */         mitigationDtoList.add(mitigationDto);
/*  812 */         mitigationDtoMap.put(mitigationDto.getMitigationId(), mitigationDto);
/*      */       } 
/*      */     }
/*      */     
/*  816 */     unionDto.setMitigationDtoList(mitigationDtoList);
/*  817 */     unionDto.setMitigationMap(mitigationDtoMap);
/*  818 */     return unionDto;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public static UnionDto convert2Relevance(List<Map<String, Object>> relevanceList, UnionDto unionDto) throws InstantiationException, IllegalAccessException {
/*  824 */     LocalDateTime start = LocalDateTime.now();
/*      */     
/*  826 */     Map<String, Map<String, RelevanceDto>> exposureRelevanceMap = new HashMap<>(unionDto.getSize());
/*      */     
/*  828 */     Map<String, List<RelevanceDto>> mitigationRelevanceMap = new HashMap<>(unionDto.getMitigationSize());
/*  829 */     for (Map<String, Object> relevance : relevanceList) {
/*  830 */       RelevanceDto relevanceDto = convert2Relevance(relevance);
/*      */       
/*  832 */       Map<String, RelevanceDto> tempMitigationMap = exposureRelevanceMap.get(relevanceDto.getExposureId());
/*  833 */       if (tempMitigationMap == null) {
/*  834 */         tempMitigationMap = new HashMap<>(unionDto.getMitigationSize());
/*  835 */         exposureRelevanceMap.put(relevanceDto.getExposureId(), tempMitigationMap);
/*      */       } 
/*  837 */       tempMitigationMap.put(relevanceDto.getMitigationId(), relevanceDto);
/*      */       
/*  839 */       List<RelevanceDto> tempExposureList = mitigationRelevanceMap.get(relevanceDto.getMitigationId());
/*  840 */       if (tempExposureList == null) {
/*  841 */         tempExposureList = new ArrayList<>();
/*  842 */         mitigationRelevanceMap.put(relevanceDto.getMitigationId(), tempExposureList);
/*      */       } 
/*  844 */       tempExposureList.add(relevanceDto);
/*      */     } 
/*  846 */     unionDto.setExposureRelevanceMap(exposureRelevanceMap);
/*  847 */     unionDto.setMitigationRelevanceMap(mitigationRelevanceMap);
/*  848 */     unionDto.setRelevanceSize(relevanceList.size());
/*  849 */     unionDto.setDetailResultList(new ArrayList(relevanceList.size()));
/*      */     
/*  851 */     if (relevanceList.size() >= 100000) {
/*  852 */       log.debug("---> data[{}] relevance[{}] convert2RelevanceMap costs[{}]", new Object[] { unionDto
/*  853 */             .getId(), Integer.valueOf(relevanceList.size()), Duration.between(start, LocalDateTime.now()) });
/*      */     }
/*  855 */     return unionDto;
/*      */   }
/*      */   
/*      */   public static boolean existsQualMitigationWa(List<Map<String, Object>> list) {
/*  859 */     if (CollUtil.isEmpty(list)) {
/*  860 */       return false;
/*      */     }
/*  862 */     for (Map<String, Object> m : list) {
/*  863 */       if (StrUtil.equals(Identity.YES.getCode(), DataUtils.getString(m, (ICodeEnum)RwaParam.QUAL_FLAG_WA))) {
/*  864 */         return true;
/*      */       }
/*      */     } 
/*  867 */     return false;
/*      */   }
/*      */   
/*      */   public static int updateMitigationQualFlagWa(List<Map<String, Object>> list, Identity identity) {
/*  871 */     if (CollUtil.isEmpty(list)) {
/*  872 */       return 0;
/*      */     }
/*  874 */     for (Map<String, Object> m : list) {
/*  875 */       DataUtils.setString(m, (ICodeEnum)RwaParam.QUAL_FLAG_WA, (ICodeEnum)identity);
/*      */     }
/*  877 */     return list.size();
/*      */   }
/*      */   
/*      */   public static AbsUnionDto initUnion(Map<String, Object> item, SchemeConfigDo schemeConfig, ExposureApproach approach, Identity isOriginator) {
/*  881 */     AbsUnionDto unionDto = new AbsUnionDto();
/*  882 */     unionDto.setId(DataUtils.getString(item, (ICodeEnum)RwaParam.ID));
/*  883 */     unionDto.setSchemeConfig(schemeConfig);
/*  884 */     unionDto.setApproach(approach);
/*  885 */     unionDto.setIsOriginator(isOriginator);
/*  886 */     return unionDto;
/*      */   }
/*      */   
/*      */   public static AbsExposureDto convert2AbsExposure(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/*  890 */     Map<String, String> mappings = new HashMap<>();
/*  891 */     DataUtils.setMapping(mappings, "abs_exposure_id");
/*  892 */     DataUtils.setMapping(mappings, "abs_product_id");
/*  893 */     DataUtils.setMapping(mappings, "amp_flag");
/*  894 */     DataUtils.setMapping(mappings, "amp_id");
/*  895 */     DataUtils.setMapping(mappings, "amp_type");
/*  896 */     DataUtils.setMapping(mappings, "amp_approach");
/*  897 */     DataUtils.setMapping(mappings, "amp_book_type");
/*  898 */     DataUtils.setMapping(mappings, "amp_belong");
/*  899 */     DataUtils.setMapping(mappings, "amp_lr");
/*  900 */     DataUtils.setMapping(mappings, "is_tp_calc");
/*  901 */     DataUtils.setMapping(mappings, "net_asset");
/*  902 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/*  903 */     DataUtils.setMapping(mappings, "securities_code");
/*  904 */     DataUtils.setMapping(mappings, "securities_name");
/*  905 */     DataUtils.setMapping(mappings, "org_id");
/*  906 */     DataUtils.setMapping(mappings, "approach");
/*  907 */     DataUtils.setMapping(mappings, "asset_type");
/*  908 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/*  909 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/*  910 */     DataUtils.setMapping(mappings, "exposure_belong");
/*  911 */     DataUtils.setMapping(mappings, "book_type");
/*  912 */     DataUtils.setMapping(mappings, "reabs_flag");
/*  913 */     DataUtils.setMapping(mappings, "is_originator");
/*  914 */     DataUtils.setMapping(mappings, "bank_business_role");
/*  915 */     DataUtils.setMapping(mappings, "off_abs_biz_type");
/*  916 */     DataUtils.setMapping(mappings, "qual_facility_flag");
/*  917 */     DataUtils.setMapping(mappings, "is_compliance_stc");
/*  918 */     DataUtils.setMapping(mappings, "tranche_sn");
/*  919 */     DataUtils.setMapping(mappings, "tranche_level");
/*  920 */     DataUtils.setMapping(mappings, "rating_duration_type");
/*  921 */     DataUtils.setMapping(mappings, "external_rating");
/*  922 */     DataUtils.setMapping(mappings, "is_approved_rating");
/*  923 */     DataUtils.setMapping(mappings, "original_maturity");
/*  924 */     DataUtils.setMapping(mappings, "residual_maturity");
/*  925 */     DataUtils.setMapping(mappings, "tranche_maturity");
/*  926 */     DataUtils.setMapping(mappings, "asset_balance");
/*  927 */     DataUtils.setMapping(mappings, "currency");
/*  928 */     DataUtils.setMapping(mappings, "provision");
/*  929 */     DataUtils.setMapping(mappings, "provision_ded");
/*  930 */     DataUtils.setMapping(mappings, "ead");
/*  931 */     DataUtils.setMapping(mappings, "ccf");
/*  932 */     DataUtils.setMapping(mappings, "tm");
/*  933 */     DataUtils.setMapping(mappings, "reva_frequency");
/*  934 */     DataUtils.setMapping(mappings, "tranche_starting_point");
/*  935 */     DataUtils.setMapping(mappings, "tranche_separation_point");
/*  936 */     DataUtils.setMapping(mappings, "thickness");
/*  937 */     DataUtils.setMapping(mappings, "tranche_prop");
/*  938 */     DataUtils.setMapping(mappings, "sfa");
/*  939 */     DataUtils.setMapping(mappings, "sfb");
/*  940 */     DataUtils.setMapping(mappings, "sfc");
/*  941 */     DataUtils.setMapping(mappings, "sfd");
/*  942 */     DataUtils.setMapping(mappings, "sfe");
/*  943 */     DataUtils.setMapping(mappings, "sfp");
/*  944 */     DataUtils.setMapping(mappings, "vara");
/*  945 */     DataUtils.setMapping(mappings, "varu");
/*  946 */     DataUtils.setMapping(mappings, "varl");
/*  947 */     DataUtils.setMapping(mappings, "kssfa");
/*  948 */     DataUtils.setMapping(mappings, "rw_before");
/*  949 */     DataUtils.setMapping(mappings, "npt_rw_ac");
/*  950 */     DataUtils.setMapping(mappings, "rw");
/*  951 */     DataUtils.setMapping(mappings, "maturity");
/*  952 */     AbsExposureDto exposureDto = (AbsExposureDto)DataUtils.dataMapping(AbsExposureDto.class, data, mappings);
/*  953 */     exposureDto.setCurrentAmount(RwaMath.sub(exposureDto.getAssetBalance(), exposureDto.getProvisionDed()));
/*  954 */     return exposureDto;
/*      */   }
/*      */   
/*      */   public static AbsProductDto convert2AbsProduct(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/*  958 */     if (data == null) {
/*  959 */       return null;
/*      */     }
/*  961 */     Map<String, String> mappings = new HashMap<>();
/*  962 */     DataUtils.setMapping(mappings, "abs_product_id");
/*  963 */     DataUtils.setMapping(mappings, "approach");
/*  964 */     DataUtils.setMapping(mappings, "org_id");
/*  965 */     DataUtils.setMapping(mappings, "under_asset_type");
/*  966 */     DataUtils.setMapping(mappings, "np_asset_flag");
/*  967 */     DataUtils.setMapping(mappings, "is_originator");
/*  968 */     DataUtils.setMapping(mappings, "is_comp_requ");
/*  969 */     DataUtils.setMapping(mappings, "ap_airb_prop");
/*  970 */     DataUtils.setMapping(mappings, "ap_airb_ab");
/*  971 */     DataUtils.setMapping(mappings, "ap_airb_ead");
/*  972 */     DataUtils.setMapping(mappings, "ap_airb_rwa");
/*  973 */     DataUtils.setMapping(mappings, "ap_airb_ela");
/*  974 */     DataUtils.setMapping(mappings, "ap_airb_prov");
/*  975 */     DataUtils.setMapping(mappings, "ap_firb_prop");
/*  976 */     DataUtils.setMapping(mappings, "ap_firb_ab");
/*  977 */     DataUtils.setMapping(mappings, "ap_firb_ead");
/*  978 */     DataUtils.setMapping(mappings, "ap_firb_rwa");
/*  979 */     DataUtils.setMapping(mappings, "ap_firb_ela");
/*  980 */     DataUtils.setMapping(mappings, "ap_firb_prov");
/*  981 */     DataUtils.setMapping(mappings, "ap_wa_prop");
/*  982 */     DataUtils.setMapping(mappings, "ap_wa_ab");
/*  983 */     DataUtils.setMapping(mappings, "ap_wa_ead");
/*  984 */     DataUtils.setMapping(mappings, "ap_wa_rwa");
/*  985 */     DataUtils.setMapping(mappings, "ap_wa_prov");
/*  986 */     DataUtils.setMapping(mappings, "ap_wa_max_rw");
/*  987 */     DataUtils.setMapping(mappings, "ap_wa_arw");
/*  988 */     DataUtils.setMapping(mappings, "ap_irb_prop");
/*  989 */     DataUtils.setMapping(mappings, "ap_ab");
/*  990 */     DataUtils.setMapping(mappings, "ap_ead");
/*  991 */     DataUtils.setMapping(mappings, "ap_rwa");
/*  992 */     DataUtils.setMapping(mappings, "ap_ode_ab");
/*  993 */     DataUtils.setMapping(mappings, "ap_ode_ead");
/*  994 */     DataUtils.setMapping(mappings, "ap_unke_ab");
/*  995 */     DataUtils.setMapping(mappings, "ap_unke_ead");
/*  996 */     DataUtils.setMapping(mappings, "ap_max_maturity");
/*  997 */     DataUtils.setMapping(mappings, "ap_en");
/*  998 */     DataUtils.setMapping(mappings, "ap_lgd");
/*  999 */     DataUtils.setMapping(mappings, "ap_warw");
/* 1000 */     DataUtils.setMapping(mappings, "ap_type");
/* 1001 */     DataUtils.setMapping(mappings, "is_decentralized");
/* 1002 */     DataUtils.setMapping(mappings, "ap_kirb");
/* 1003 */     DataUtils.setMapping(mappings, "ap_ksa");
/* 1004 */     DataUtils.setMapping(mappings, "ap_ka");
/* 1005 */     DataUtils.setMapping(mappings, "product_rwa_limit");
/* 1006 */     AbsProductDto productDto = (AbsProductDto)DataUtils.dataMapping(AbsProductDto.class, data, mappings);
/* 1007 */     return productDto;
/*      */   }
/*      */   
/*      */   public static MitigationDto convert2Collateral(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1011 */     Map<String, String> mappings = new HashMap<>();
/* 1012 */     DataUtils.setMapping(mappings, "group_id");
/* 1013 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1014 */     DataUtils.setMapping(mappings, "exposure_count");
/* 1015 */     DataUtils.setMapping(mappings, "approach");
/* 1016 */     DataUtils.setMapping(mappings, "COLLATERAL_ID", "mitigationId");
/* 1017 */     DataUtils.setMapping(mappings, "issuer_id", "clientId");
/* 1018 */     DataUtils.setMapping(mappings, "issuer_type", "clientType");
/* 1019 */     DataUtils.setMapping(mappings, "is_apply_wa");
/* 1020 */     DataUtils.setMapping(mappings, "is_apply_firb");
/* 1021 */     DataUtils.setMapping(mappings, "qual_flag_wa");
/* 1022 */     DataUtils.setMapping(mappings, "qual_flag_firb");
/* 1023 */     DataUtils.setMapping(mappings, "mitigation_type");
/* 1024 */     DataUtils.setMapping(mappings, "mitigation_main_type");
/* 1025 */     DataUtils.setMapping(mappings, "mitigation_small_type");
/* 1026 */     DataUtils.setMapping(mappings, "mitigation_rpt_item_wa");
/* 1027 */     DataUtils.setMapping(mappings, "COLLATERAL_AMOUNT", "mitigationAmount");
/* 1028 */     DataUtils.setMapping(mappings, "currency");
/* 1029 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1030 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1031 */     DataUtils.setMapping(mappings, "guar_residual_maturity");
/* 1032 */     DataUtils.setMapping(mappings, "reva_frequency");
/* 1033 */     DataUtils.setMapping(mappings, "is_full_cover_em");
/* 1034 */     DataUtils.setMapping(mappings, "rw_value_fc_type");
/* 1035 */     DataUtils.setMapping(mappings, "rw");
/* 1036 */     DataUtils.setMapping(mappings, "lgd");
/* 1037 */     DataUtils.setMapping(mappings, "min_collateral_level");
/* 1038 */     DataUtils.setMapping(mappings, "over_collateral_level");
/* 1039 */     DataUtils.setMapping(mappings, "sh", "shc");
/* 1040 */     MitigationDto mitigationDto = (MitigationDto)DataUtils.dataMapping(MitigationDto.class, data, mappings);
/* 1041 */     mitigationDto.setCurrentAmount(mitigationDto.getMitigationAmount());
/* 1042 */     mitigationDto.setHc(mitigationDto.getShc());
/* 1043 */     mitigationDto.setIsAlone(getIsAlone(mitigationDto.getExposureCount()));
/* 1044 */     return mitigationDto;
/*      */   }
/*      */   
/*      */   public static MitigationDto convert2Guarantee(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1048 */     Map<String, String> mappings = new HashMap<>();
/* 1049 */     DataUtils.setMapping(mappings, "group_id");
/* 1050 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1051 */     DataUtils.setMapping(mappings, "exposure_count");
/* 1052 */     DataUtils.setMapping(mappings, "approach");
/* 1053 */     DataUtils.setMapping(mappings, "GUARANTEE_ID", "mitigationId");
/* 1054 */     DataUtils.setMapping(mappings, "client_id");
/* 1055 */     DataUtils.setMapping(mappings, "client_type");
/* 1056 */     DataUtils.setMapping(mappings, "annual_sale");
/* 1057 */     DataUtils.setMapping(mappings, "is_apply_wa");
/* 1058 */     DataUtils.setMapping(mappings, "is_apply_firb");
/* 1059 */     DataUtils.setMapping(mappings, "qual_flag_wa");
/* 1060 */     DataUtils.setMapping(mappings, "qual_flag_firb");
/* 1061 */     DataUtils.setMapping(mappings, "mitigation_type");
/* 1062 */     DataUtils.setMapping(mappings, "mitigation_main_type");
/* 1063 */     DataUtils.setMapping(mappings, "mitigation_small_type");
/* 1064 */     DataUtils.setMapping(mappings, "mitigation_rpt_item_wa");
/* 1065 */     DataUtils.setMapping(mappings, "guarantor_expo_type");
/* 1066 */     DataUtils.setMapping(mappings, "exposure_main_type_irb");
/* 1067 */     DataUtils.setMapping(mappings, "sib_flag");
/* 1068 */     DataUtils.setMapping(mappings, "GUARANTEE_AMOUNT", "mitigationAmount");
/* 1069 */     DataUtils.setMapping(mappings, "currency");
/* 1070 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1071 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1072 */     DataUtils.setMapping(mappings, "guar_residual_maturity");
/* 1073 */     DataUtils.setMapping(mappings, "pay_default_threshold");
/* 1074 */     DataUtils.setMapping(mappings, "model_id");
/* 1075 */     DataUtils.setMapping(mappings, "rating");
/* 1076 */     DataUtils.setMapping(mappings, "pd");
/* 1077 */     DataUtils.setMapping(mappings, "lgd");
/* 1078 */     DataUtils.setMapping(mappings, "rel");
/* 1079 */     DataUtils.setMapping(mappings, "bma");
/* 1080 */     DataUtils.setMapping(mappings, "kcr");
/* 1081 */     DataUtils.setMapping(mappings, "rw");
/* 1082 */     DataUtils.setMapping(mappings, "formula_no");
/* 1083 */     DataUtils.setMapping(mappings, "sh", "shc");
/* 1084 */     MitigationDto mitigationDto = (MitigationDto)DataUtils.dataMapping(MitigationDto.class, data, mappings);
/* 1085 */     mitigationDto.setCurrentAmount(mitigationDto.getMitigationAmount());
/* 1086 */     mitigationDto.setIsAlone(getIsAlone(mitigationDto.getExposureCount()));
/* 1087 */     mitigationDto.setUnmitigatedAmount(mitigationDto.getMitigatedAmount());
/* 1088 */     mitigationDto.setMitigatedAmount(BigDecimal.ZERO);
/* 1089 */     mitigationDto.setCoveredEa(BigDecimal.ZERO);
/* 1090 */     mitigationDto.setMitigatedEffect(BigDecimal.ZERO);
/* 1091 */     mitigationDto.setHc(mitigationDto.getShc());
/* 1092 */     return mitigationDto;
/*      */   }
/*      */   
/*      */   public static RelevanceDto convert2Relevance(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1096 */     Map<String, String> mappings = new LinkedHashMap<>();
/* 1097 */     DataUtils.setMapping(mappings, "group_id");
/* 1098 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1099 */     DataUtils.setMapping(mappings, "mitigation_id");
/* 1100 */     DataUtils.setMapping(mappings, "mitigation_type");
/* 1101 */     DataUtils.setMapping(mappings, "is_positive_correlation");
/* 1102 */     return (RelevanceDto)DataUtils.dataMapping(RelevanceDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static AbsUnionDto convert2AbsExposure(List<Map<String, Object>> list, AbsUnionDto unionDto) throws InstantiationException, IllegalAccessException {
/* 1106 */     List<AbsExposureDto> exposureDtoList = new ArrayList<>(list.size());
/* 1107 */     Map<String, AbsExposureDto> exposureDtoMap = new HashMap<>(list.size());
/* 1108 */     for (Map<String, Object> exposure : list) {
/* 1109 */       AbsExposureDto exposureDto = convert2AbsExposure(exposure);
/* 1110 */       exposureDtoList.add(exposureDto);
/* 1111 */       exposureDtoMap.put(exposureDto.getAbsExposureId(), exposureDto);
/*      */     } 
/* 1113 */     unionDto.setSize(list.size());
/* 1114 */     unionDto.setExposureList(exposureDtoList);
/* 1115 */     unionDto.setExposureMap(exposureDtoMap);
/*      */     
/* 1117 */     unionDto.setDetailList(new ArrayList(list.size() * 2));
/* 1118 */     unionDto.setAmpResultList(new ArrayList(list.size()));
/* 1119 */     return unionDto;
/*      */   }
/*      */   
/*      */   public static AbsUnionDto convert2AbsProduct(Map<String, Object> product, AbsUnionDto unionDto) throws InstantiationException, IllegalAccessException {
/* 1123 */     unionDto.setProductList(new ArrayList());
/* 1124 */     if (product != null) {
/* 1125 */       unionDto.setProduct(convert2AbsProduct(product));
/* 1126 */       unionDto.getProductList().add(unionDto.getProduct());
/*      */     } 
/* 1128 */     return unionDto;
/*      */   }
/*      */ 
/*      */   
/*      */   public static AbsUnionDto convert2AbsMitigation(Map<String, Object> item, AbsUnionDto unionDto) throws InstantiationException, IllegalAccessException {
/* 1133 */     List<Map<String, Object>> relevanceList = (List<Map<String, Object>>)item.get(InterfaceDataType.ABS_RELEVANCE.getCode());
/* 1134 */     if (CollUtil.isEmpty(relevanceList)) {
/* 1135 */       return unionDto;
/*      */     }
/*      */     
/* 1138 */     List<Map<String, Object>> guaranteeList = (List<Map<String, Object>>)item.get(InterfaceDataType.ABS_GUARANTEE.getCode());
/* 1139 */     List<MitigationDto> mitigationDtoList = new ArrayList<>();
/* 1140 */     Map<String, MitigationDto> mitigationMap = new HashMap<>();
/* 1141 */     if (!CollUtil.isEmpty(guaranteeList)) {
/* 1142 */       for (Map<String, Object> guarantee : guaranteeList) {
/* 1143 */         MitigationDto mitigationDto = convert2Guarantee(guarantee);
/* 1144 */         mitigationDto.setAbsProductId(unionDto.getId());
/* 1145 */         mitigationDtoList.add(mitigationDto);
/* 1146 */         mitigationMap.put(mitigationDto.getMitigationId(), mitigationDto);
/*      */       } 
/*      */     }
/* 1149 */     unionDto.setMitigationSize(mitigationDtoList.size());
/* 1150 */     unionDto.setMitigationList(mitigationDtoList);
/* 1151 */     unionDto.setMitigationMap(mitigationMap);
/* 1152 */     return convert2AbsRelevance(item, unionDto);
/*      */   }
/*      */   
/*      */   public static AbsUnionDto convert2AbsRelevance(Map<String, Object> item, AbsUnionDto unionDto) throws InstantiationException, IllegalAccessException {
/* 1156 */     List<Map<String, Object>> relevanceList = (List<Map<String, Object>>)item.get(InterfaceDataType.ABS_RELEVANCE.getCode());
/*      */     
/* 1158 */     Map<String, Map<String, RelevanceDto>> exposureRelevanceMap = new HashMap<>(unionDto.getSize());
/* 1159 */     for (Map<String, Object> relevance : relevanceList) {
/* 1160 */       RelevanceDto relevanceDto = convert2Relevance(relevance);
/*      */       
/* 1162 */       Map<String, RelevanceDto> tempMitigationMap = exposureRelevanceMap.get(relevanceDto.getExposureId());
/* 1163 */       if (tempMitigationMap == null) {
/* 1164 */         tempMitigationMap = new HashMap<>(unionDto.getMitigationSize());
/* 1165 */         exposureRelevanceMap.put(relevanceDto.getExposureId(), tempMitigationMap);
/*      */       } 
/* 1167 */       tempMitigationMap.put(relevanceDto.getMitigationId(), relevanceDto);
/*      */     } 
/* 1169 */     unionDto.setExposureRelevanceMap(exposureRelevanceMap);
/* 1170 */     unionDto.setRelevanceSize(relevanceList.size());
/* 1171 */     unionDto.setDetailList(new ArrayList(relevanceList.size()));
/* 1172 */     return unionDto;
/*      */   }
/*      */   
/*      */   public static DiNettingDto convert2DiNetting(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1176 */     Map<String, String> mappings = new HashMap<>();
/* 1177 */     DataUtils.setMapping(mappings, "netting_id");
/* 1178 */     DataUtils.setMapping(mappings, "approach");
/* 1179 */     DataUtils.setMapping(mappings, "client_id");
/* 1180 */     DataUtils.setMapping(mappings, "client_name");
/* 1181 */     DataUtils.setMapping(mappings, "client_type");
/* 1182 */     DataUtils.setMapping(mappings, "annual_sale");
/* 1183 */     DataUtils.setMapping(mappings, "qual_ccp_flag");
/* 1184 */     DataUtils.setMapping(mappings, "org_id");
/* 1185 */     DataUtils.setMapping(mappings, "asset_type");
/* 1186 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/* 1187 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/* 1188 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/* 1189 */     DataUtils.setMapping(mappings, "exposure_belong");
/* 1190 */     DataUtils.setMapping(mappings, "book_type");
/* 1191 */     DataUtils.setMapping(mappings, "currency");
/* 1192 */     DataUtils.setMapping(mappings, "book_value");
/* 1193 */     DataUtils.setMapping(mappings, "transactions_num");
/* 1194 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1195 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1196 */     DataUtils.setMapping(mappings, "margin_trading_flag");
/* 1197 */     DataUtils.setMapping(mappings, "margin_agreement_id");
/* 1198 */     DataUtils.setMapping(mappings, "margin_mtm_interval");
/* 1199 */     DataUtils.setMapping(mappings, "mta");
/* 1200 */     DataUtils.setMapping(mappings, "th");
/* 1201 */     DataUtils.setMapping(mappings, "nica");
/* 1202 */     DataUtils.setMapping(mappings, "is_controversial");
/* 1203 */     DataUtils.setMapping(mappings, "mtm");
/* 1204 */     DataUtils.setMapping(mappings, "tm");
/* 1205 */     DataUtils.setMapping(mappings, "default_flag");
/* 1206 */     DataUtils.setMapping(mappings, "beel");
/* 1207 */     DataUtils.setMapping(mappings, "default_lgd");
/* 1208 */     DataUtils.setMapping(mappings, "model_id");
/* 1209 */     DataUtils.setMapping(mappings, "rating");
/* 1210 */     DataUtils.setMapping(mappings, "pd");
/* 1211 */     DataUtils.setMapping(mappings, "lgd");
/* 1212 */     DataUtils.setMapping(mappings, "maturity");
/* 1213 */     DataUtils.setMapping(mappings, "rel");
/* 1214 */     DataUtils.setMapping(mappings, "bma");
/* 1215 */     DataUtils.setMapping(mappings, "kcr");
/* 1216 */     DataUtils.setMapping(mappings, "rw");
/* 1217 */     DataUtils.setMapping(mappings, "formula_no");
/* 1218 */     return (DiNettingDto)DataUtils.dataMapping(DiNettingDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static DiExposureDto convert2DiExposure(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1222 */     Map<String, String> mappings = new HashMap<>();
/* 1223 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1224 */     DataUtils.setMapping(mappings, "approach");
/* 1225 */     DataUtils.setMapping(mappings, "netting_id");
/* 1226 */     DataUtils.setMapping(mappings, "netting_flag");
/* 1227 */     DataUtils.setMapping(mappings, "client_id");
/* 1228 */     DataUtils.setMapping(mappings, "client_type");
/* 1229 */     DataUtils.setMapping(mappings, "annual_sale");
/* 1230 */     DataUtils.setMapping(mappings, "qual_ccp_flag");
/* 1231 */     DataUtils.setMapping(mappings, "org_id");
/* 1232 */     DataUtils.setMapping(mappings, "asset_type");
/* 1233 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/* 1234 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/* 1235 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/* 1236 */     DataUtils.setMapping(mappings, "exposure_belong");
/* 1237 */     DataUtils.setMapping(mappings, "book_type");
/* 1238 */     DataUtils.setMapping(mappings, "central_clear_flag");
/* 1239 */     DataUtils.setMapping(mappings, "ccp_id");
/* 1240 */     DataUtils.setMapping(mappings, "trading_role");
/* 1241 */     DataUtils.setMapping(mappings, "margin_trading_flag");
/* 1242 */     DataUtils.setMapping(mappings, "margin_agreement_id");
/* 1243 */     DataUtils.setMapping(mappings, "margin_mtm_interval");
/* 1244 */     DataUtils.setMapping(mappings, "margin_risk_period");
/* 1245 */     DataUtils.setMapping(mappings, "mta");
/* 1246 */     DataUtils.setMapping(mappings, "th");
/* 1247 */     DataUtils.setMapping(mappings, "nica");
/* 1248 */     DataUtils.setMapping(mappings, "currency");
/* 1249 */     DataUtils.setMapping(mappings, "book_value");
/* 1250 */     DataUtils.setMapping(mappings, "notional_principal1");
/* 1251 */     DataUtils.setMapping(mappings, "currency1");
/* 1252 */     DataUtils.setMapping(mappings, "notional_principal2");
/* 1253 */     DataUtils.setMapping(mappings, "currency2");
/* 1254 */     DataUtils.setMapping(mappings, "start_date");
/* 1255 */     DataUtils.setMapping(mappings, "due_date");
/* 1256 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1257 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1258 */     DataUtils.setMapping(mappings, "derivative_asset_type");
/* 1259 */     DataUtils.setMapping(mappings, "pm_trading_flag");
/* 1260 */     DataUtils.setMapping(mappings, "credit_derivative_type");
/* 1261 */     DataUtils.setMapping(mappings, "offset_comb");
/* 1262 */     DataUtils.setMapping(mappings, "commodity_subtype");
/* 1263 */     DataUtils.setMapping(mappings, "trading_direction");
/* 1264 */     DataUtils.setMapping(mappings, "unpaid_fee");
/* 1265 */     DataUtils.setMapping(mappings, "option_cdo_flag");
/* 1266 */     DataUtils.setMapping(mappings, "cdo_level_attachment_point");
/* 1267 */     DataUtils.setMapping(mappings, "cdo_level_decoupling_point");
/* 1268 */     DataUtils.setMapping(mappings, "option_type");
/* 1269 */     DataUtils.setMapping(mappings, "underlying_price");
/* 1270 */     DataUtils.setMapping(mappings, "executive_price");
/* 1271 */     DataUtils.setMapping(mappings, "exercise_date");
/* 1272 */     DataUtils.setMapping(mappings, "option_premium_status");
/* 1273 */     DataUtils.setMapping(mappings, "is_controversial");
/* 1274 */     DataUtils.setMapping(mappings, "ir_time_type");
/* 1275 */     DataUtils.setMapping(mappings, "mtm");
/* 1276 */     DataUtils.setMapping(mappings, "tm");
/* 1277 */     DataUtils.setMapping(mappings, "addon_factor");
/* 1278 */     DataUtils.setMapping(mappings, "supervision_factor");
/* 1279 */     DataUtils.setMapping(mappings, "supervision_coefficient");
/* 1280 */     DataUtils.setMapping(mappings, "supervision_volatility");
/* 1281 */     DataUtils.setMapping(mappings, "ead_haircut");
/* 1282 */     DataUtils.setMapping(mappings, "default_flag");
/* 1283 */     DataUtils.setMapping(mappings, "beel");
/* 1284 */     DataUtils.setMapping(mappings, "default_lgd");
/* 1285 */     DataUtils.setMapping(mappings, "model_id");
/* 1286 */     DataUtils.setMapping(mappings, "rating");
/* 1287 */     DataUtils.setMapping(mappings, "pd");
/* 1288 */     DataUtils.setMapping(mappings, "lgd");
/* 1289 */     DataUtils.setMapping(mappings, "maturity");
/* 1290 */     DataUtils.setMapping(mappings, "rel");
/* 1291 */     DataUtils.setMapping(mappings, "bma");
/* 1292 */     DataUtils.setMapping(mappings, "kcr");
/* 1293 */     DataUtils.setMapping(mappings, "rw");
/* 1294 */     DataUtils.setMapping(mappings, "formula_no");
/* 1295 */     return (DiExposureDto)DataUtils.dataMapping(DiExposureDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static DiCollateralDto convert2DiCollateral(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1299 */     Map<String, String> mappings = new HashMap<>();
/* 1300 */     DataUtils.setMapping(mappings, "collateral_id");
/* 1301 */     DataUtils.setMapping(mappings, "approach");
/* 1302 */     DataUtils.setMapping(mappings, "netting_flag");
/* 1303 */     DataUtils.setMapping(mappings, "netting_id");
/* 1304 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1305 */     DataUtils.setMapping(mappings, "client_id");
/* 1306 */     DataUtils.setMapping(mappings, "client_type");
/* 1307 */     DataUtils.setMapping(mappings, "qual_ccp_flag");
/* 1308 */     DataUtils.setMapping(mappings, "issuer_id");
/* 1309 */     DataUtils.setMapping(mappings, "issuer_type");
/* 1310 */     DataUtils.setMapping(mappings, "is_apply_wa");
/* 1311 */     DataUtils.setMapping(mappings, "is_apply_firb");
/* 1312 */     DataUtils.setMapping(mappings, "qual_flag_wa");
/* 1313 */     DataUtils.setMapping(mappings, "qual_flag_firb");
/* 1314 */     DataUtils.setMapping(mappings, "mitigation_main_type");
/* 1315 */     DataUtils.setMapping(mappings, "mitigation_small_type");
/* 1316 */     DataUtils.setMapping(mappings, "collateral_amount");
/* 1317 */     DataUtils.setMapping(mappings, "currency");
/* 1318 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1319 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1320 */     DataUtils.setMapping(mappings, "reva_frequency");
/* 1321 */     DataUtils.setMapping(mappings, "is_our_bank_submit");
/* 1322 */     DataUtils.setMapping(mappings, "bankruptcy_separation_flag");
/* 1323 */     DataUtils.setMapping(mappings, "sh");
/* 1324 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa", "supervisionClass");
/*      */ 
/*      */     
/* 1327 */     return (DiCollateralDto)DataUtils.dataMapping(DiCollateralDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static SftNettingDto convert2SftNetting(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1331 */     Map<String, String> mappings = new HashMap<>();
/* 1332 */     DataUtils.setMapping(mappings, "netting_id");
/* 1333 */     DataUtils.setMapping(mappings, "approach");
/* 1334 */     DataUtils.setMapping(mappings, "client_id");
/* 1335 */     DataUtils.setMapping(mappings, "client_name");
/* 1336 */     DataUtils.setMapping(mappings, "client_type");
/* 1337 */     DataUtils.setMapping(mappings, "annual_sale");
/* 1338 */     DataUtils.setMapping(mappings, "qual_ccp_flag");
/* 1339 */     DataUtils.setMapping(mappings, "org_id");
/* 1340 */     DataUtils.setMapping(mappings, "asset_type");
/* 1341 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/* 1342 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/* 1343 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/* 1344 */     DataUtils.setMapping(mappings, "exposure_belong");
/* 1345 */     DataUtils.setMapping(mappings, "book_type");
/* 1346 */     DataUtils.setMapping(mappings, "currency");
/* 1347 */     DataUtils.setMapping(mappings, "asset_balance");
/* 1348 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1349 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1350 */     DataUtils.setMapping(mappings, "tm");
/* 1351 */     DataUtils.setMapping(mappings, "default_flag");
/* 1352 */     DataUtils.setMapping(mappings, "beel");
/* 1353 */     DataUtils.setMapping(mappings, "default_lgd");
/* 1354 */     DataUtils.setMapping(mappings, "model_id");
/* 1355 */     DataUtils.setMapping(mappings, "rating");
/* 1356 */     DataUtils.setMapping(mappings, "pd");
/* 1357 */     DataUtils.setMapping(mappings, "lgd");
/* 1358 */     DataUtils.setMapping(mappings, "maturity");
/* 1359 */     DataUtils.setMapping(mappings, "rel");
/* 1360 */     DataUtils.setMapping(mappings, "bma");
/* 1361 */     DataUtils.setMapping(mappings, "kcr");
/* 1362 */     DataUtils.setMapping(mappings, "rw");
/* 1363 */     DataUtils.setMapping(mappings, "formula_no");
/* 1364 */     return (SftNettingDto)DataUtils.dataMapping(SftNettingDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static SftExposureDto convert2SftExposure(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1368 */     Map<String, String> mappings = new HashMap<>();
/* 1369 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1370 */     DataUtils.setMapping(mappings, "approach");
/* 1371 */     DataUtils.setMapping(mappings, "netting_id");
/* 1372 */     DataUtils.setMapping(mappings, "netting_flag");
/* 1373 */     DataUtils.setMapping(mappings, "client_id");
/* 1374 */     DataUtils.setMapping(mappings, "client_type");
/* 1375 */     DataUtils.setMapping(mappings, "annual_sale");
/* 1376 */     DataUtils.setMapping(mappings, "qual_ccp_flag");
/* 1377 */     DataUtils.setMapping(mappings, "org_id");
/* 1378 */     DataUtils.setMapping(mappings, "instruments_type");
/* 1379 */     DataUtils.setMapping(mappings, "asset_type");
/* 1380 */     DataUtils.setMapping(mappings, "exposure_type_wa");
/* 1381 */     DataUtils.setMapping(mappings, "exposure_type_irb");
/* 1382 */     DataUtils.setMapping(mappings, "exposure_rpt_item_wa");
/* 1383 */     DataUtils.setMapping(mappings, "exposure_belong");
/* 1384 */     DataUtils.setMapping(mappings, "book_type");
/* 1385 */     DataUtils.setMapping(mappings, "central_clear_flag");
/* 1386 */     DataUtils.setMapping(mappings, "ccp_id");
/* 1387 */     DataUtils.setMapping(mappings, "trading_role");
/* 1388 */     DataUtils.setMapping(mappings, "currency");
/* 1389 */     DataUtils.setMapping(mappings, "notional_principal");
/* 1390 */     DataUtils.setMapping(mappings, "asset_balance");
/* 1391 */     DataUtils.setMapping(mappings, "repo_amt");
/* 1392 */     DataUtils.setMapping(mappings, "lend_amt");
/* 1393 */     DataUtils.setMapping(mappings, "start_date");
/* 1394 */     DataUtils.setMapping(mappings, "due_date");
/* 1395 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1396 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1397 */     DataUtils.setMapping(mappings, "sft_type");
/* 1398 */     DataUtils.setMapping(mappings, "repo_direction");
/* 1399 */     DataUtils.setMapping(mappings, "reva_frequency");
/* 1400 */     DataUtils.setMapping(mappings, "issuer_id");
/* 1401 */     DataUtils.setMapping(mappings, "issuer_type");
/* 1402 */     DataUtils.setMapping(mappings, "factor_line");
/* 1403 */     DataUtils.setMapping(mappings, "core_market_party_flag");
/* 1404 */     DataUtils.setMapping(mappings, "sh", "she");
/* 1405 */     DataUtils.setMapping(mappings, "tm");
/* 1406 */     DataUtils.setMapping(mappings, "default_flag");
/* 1407 */     DataUtils.setMapping(mappings, "beel");
/* 1408 */     DataUtils.setMapping(mappings, "default_lgd");
/* 1409 */     DataUtils.setMapping(mappings, "model_id");
/* 1410 */     DataUtils.setMapping(mappings, "rating");
/* 1411 */     DataUtils.setMapping(mappings, "pd");
/* 1412 */     DataUtils.setMapping(mappings, "lgd");
/* 1413 */     DataUtils.setMapping(mappings, "maturity");
/* 1414 */     DataUtils.setMapping(mappings, "rel");
/* 1415 */     DataUtils.setMapping(mappings, "bma");
/* 1416 */     DataUtils.setMapping(mappings, "kcr");
/* 1417 */     DataUtils.setMapping(mappings, "rw");
/* 1418 */     DataUtils.setMapping(mappings, "rw_fi");
/* 1419 */     DataUtils.setMapping(mappings, "mitigation_rpt_item_wa");
/* 1420 */     DataUtils.setMapping(mappings, "formula_no");
/* 1421 */     return (SftExposureDto)DataUtils.dataMapping(SftExposureDto.class, data, mappings);
/*      */   }
/*      */   
/*      */   public static SftCollateralDto convert2SftCollateral(Map<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 1425 */     Map<String, String> mappings = new HashMap<>();
/* 1426 */     DataUtils.setMapping(mappings, "collateral_id");
/* 1427 */     DataUtils.setMapping(mappings, "approach");
/* 1428 */     DataUtils.setMapping(mappings, "netting_flag");
/* 1429 */     DataUtils.setMapping(mappings, "netting_id");
/* 1430 */     DataUtils.setMapping(mappings, "exposure_id");
/* 1431 */     DataUtils.setMapping(mappings, "issuer_id");
/* 1432 */     DataUtils.setMapping(mappings, "issuer_type");
/* 1433 */     DataUtils.setMapping(mappings, "is_apply_wa");
/* 1434 */     DataUtils.setMapping(mappings, "is_apply_firb");
/* 1435 */     DataUtils.setMapping(mappings, "qual_flag_wa");
/* 1436 */     DataUtils.setMapping(mappings, "qual_flag_firb");
/* 1437 */     DataUtils.setMapping(mappings, "mitigation_main_type");
/* 1438 */     DataUtils.setMapping(mappings, "mitigation_small_type");
/* 1439 */     DataUtils.setMapping(mappings, "collateral_amount");
/* 1440 */     DataUtils.setMapping(mappings, "currency");
/* 1441 */     DataUtils.setMapping(mappings, "original_maturity");
/* 1442 */     DataUtils.setMapping(mappings, "residual_maturity");
/* 1443 */     DataUtils.setMapping(mappings, "reva_frequency");
/* 1444 */     DataUtils.setMapping(mappings, "factor_line");
/* 1445 */     DataUtils.setMapping(mappings, "sh");
/* 1446 */     DataUtils.setMapping(mappings, "mitigation_rpt_item_wa", "supervisionClass");
/* 1447 */     DataUtils.setMapping(mappings, "rw");
/* 1448 */     return (SftCollateralDto)DataUtils.dataMapping(SftCollateralDto.class, data, mappings);
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<EcFactorDo> mappingEcFactor(Map<String, Object> data) {
/* 1453 */     List<EcFactorDo> mappingList = new ArrayList<>();
/*      */     
/* 1455 */     List<EcFactorDo> multiList = new ArrayList<>();
/*      */     
/* 1457 */     Map<String, List<EcFactorDo>> generalMap = new HashMap<>();
/*      */     
/* 1459 */     Map<String, List<EcFactorDo>> complexMap = new HashMap<>();
/*      */     
/* 1461 */     for (EcFactorDo ecFactorDo : RwaConfig.getEcFactorList()) {
/*      */       
/* 1463 */       if (isMappingEcFactor(data, ecFactorDo)) {
/*      */         
/* 1465 */         ColumnRuleDto ruleDto = (ColumnRuleDto)RwaConfig.getEcColumnRuleMap().get(ecFactorDo.getEcAdjDim());
/* 1466 */         if (ruleDto == null) {
/*      */           
/* 1468 */           multiList.add(ecFactorDo);
/*      */           continue;
/*      */         } 
/* 1471 */         if (StrUtil.equals(ruleDto.getMappingMode(), MappingMode.COMPLEX.getCode())) {
/* 1472 */           putObject(complexMap, ecFactorDo.getEcAdjDim(), ecFactorDo); continue;
/*      */         } 
/* 1474 */         putObject(generalMap, ecFactorDo.getEcAdjDim(), ecFactorDo);
/*      */       } 
/*      */     } 
/*      */ 
/*      */ 
/*      */     
/* 1480 */     if (!CollUtil.isEmpty(mappingList)) {
/* 1481 */       mappingList.addAll(confirmEcFactorList(multiList));
/*      */     }
/*      */     
/* 1484 */     if (!CollUtil.isEmpty(generalMap)) {
/* 1485 */       for (String key : generalMap.keySet()) {
/* 1486 */         mappingList.add(confirmEcFactorByGeneral(generalMap.get(key)));
/*      */       }
/*      */     }
/*      */ 
/*      */     
/* 1491 */     if (!CollUtil.isEmpty(complexMap)) {
/* 1492 */       for (String key : complexMap.keySet()) {
/* 1493 */         mappingList.add(confirmEcFactorByComplex(key, complexMap.get(key)));
/*      */       }
/*      */     }
/* 1496 */     return mappingList;
/*      */   }
/*      */   
/*      */   public static String getEcScopeKey(EcFactorDo ecFactorDo, boolean isMulti) {
/* 1500 */     if (CollUtil.isEmpty(ecFactorDo.getAdjScopeMap())) {
/* 1501 */       throw new ParamConfigException("经济资本参数配置异常， 非法调整范围！ ecFactor: " + JsonUtils.object2Json(ecFactorDo));
/*      */     }
/*      */     
/* 1504 */     if (!isMulti) {
/*      */       
/* 1506 */       ColumnRuleDto ruleDto = (ColumnRuleDto)RwaConfig.getEcColumnRuleMap().get(ecFactorDo.getEcAdjDim());
/* 1507 */       if (ruleDto != null)
/*      */       {
/* 1509 */         return ((ColumnScopeDto)ecFactorDo.getAdjScopeMap().get(ecFactorDo.getEcAdjDim())).getColId().get(0);
/*      */       }
/*      */     } 
/*      */     
/* 1513 */     String name = null;
/* 1514 */     for (String key : ecFactorDo.getAdjScopeMap().keySet()) {
/* 1515 */       if (name == null) {
/* 1516 */         name = DataUtils.generateKey(new String[] { key, ((ColumnScopeDto)ecFactorDo.getAdjScopeMap().get(key)).getColId().toString() }); continue;
/*      */       } 
/* 1518 */       name = name + "|" + DataUtils.generateKey(new String[] { key, ((ColumnScopeDto)ecFactorDo.getAdjScopeMap().get(key)).getColId().toString() });
/*      */     } 
/*      */     
/* 1521 */     return name;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMappingEcFactor(Map<String, Object> data, EcFactorDo ecFactorDo) {
/* 1526 */     if (isMappingEcFactorOfDate(DataUtils.getDate(data, (ICodeEnum)RwaParam.START_DATE), ecFactorDo.getEffectiveDate())) {
/*      */       
/* 1528 */       for (ColumnScopeDto scopeDto : ecFactorDo.getAdjScopeMap().values()) {
/* 1529 */         if (!isMappingEcFactorOfColumn(data.get(scopeDto.getColCode()), scopeDto.getColCode(), scopeDto.getColId())) {
/* 1530 */           return false;
/*      */         }
/*      */       } 
/* 1533 */       return true;
/*      */     } 
/* 1535 */     return false;
/*      */   }
/*      */ 
/*      */   
/*      */   public static boolean isMappingEcFactorOfDate(Date startDate, Date effectiveDate) {
/* 1540 */     if (effectiveDate == null) {
/* 1541 */       return true;
/*      */     }
/*      */     
/* 1544 */     if (startDate == null) {
/* 1545 */       return false;
/*      */     }
/*      */     
/* 1548 */     if (DateUtil.compare(effectiveDate, startDate) <= 0) {
/* 1549 */       return true;
/*      */     }
/* 1551 */     return false;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean isMappingEcFactorOfColumn(Object value, String column, List<String> scopeList) {
/*      */     int i;
/* 1563 */     ColumnRuleDto ruleDto = (ColumnRuleDto)RwaConfig.getEcColumnRuleMap().get(column);
/* 1564 */     MappingMode mappingMode = (MappingMode)EnumUtils.getEnumByCode(ruleDto.getMappingMode(), MappingMode.class);
/*      */     
/* 1566 */     String v = Convert.toStr(value);
/*      */     
/* 1568 */     if (StrUtil.isEmpty(v)) {
/* 1569 */       return false;
/*      */     }
/* 1571 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$MappingMode[mappingMode.ordinal()]) {
/*      */       
/*      */       case 1:
/* 1574 */         for (String scope : scopeList) {
/* 1575 */           if (StrUtil.equals(v, scope)) {
/* 1576 */             return true;
/*      */           }
/*      */         } 
/* 1579 */         return false;
/*      */       
/*      */       case 2:
/* 1582 */         for (String scope : scopeList) {
/* 1583 */           if (v.length() < scope.length()) {
/*      */             continue;
/*      */           }
/* 1586 */           if (StrUtil.equals(v.substring(0, scope.length()), scope)) {
/* 1587 */             return true;
/*      */           }
/*      */         } 
/* 1590 */         return false;
/*      */ 
/*      */       
/*      */       case 3:
/* 1594 */         for (i = 0; i < 10; i++) {
/* 1595 */           if (StrUtil.isEmpty(v)) {
/* 1596 */             return false;
/*      */           }
/* 1598 */           if (StrUtil.equals(v, scopeList.get(0))) {
/* 1599 */             return true;
/*      */           }
/* 1601 */           v = (String)ruleDto.getCodeMap().get(v);
/*      */         } 
/* 1603 */         return false;
/*      */     } 
/* 1605 */     throw new RuntimeException();
/*      */   }
/*      */ 
/*      */   
/*      */   public static List<EcFactorDo> confirmEcFactorList(List<EcFactorDo> list) {
/* 1610 */     List<EcFactorDo> adjList = new ArrayList<>();
/*      */     
/* 1612 */     Map<String, List<EcFactorDo>> map = (Map<String, List<EcFactorDo>>)list.stream().collect(Collectors.groupingBy(ec -> getEcScopeKey(ec, true)));
/*      */     
/* 1614 */     for (List<EcFactorDo> group : map.values()) {
/* 1615 */       adjList.add(confirmEcFactorByGeneral(group));
/*      */     }
/* 1617 */     return adjList;
/*      */   }
/*      */   
/*      */   public static EcFactorDo confirmEcFactorByGeneral(List<EcFactorDo> list) {
/* 1621 */     if (list.size() == 1) {
/* 1622 */       return list.get(0);
/*      */     }
/*      */     
/* 1625 */     Collections.sort(list, (Comparator<? super EcFactorDo>)new Object());
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
/* 1637 */     return list.get(0);
/*      */   }
/*      */   
/*      */   public static EcFactorDo confirmEcFactorByComplex(String key, List<EcFactorDo> list) {
/* 1641 */     if (list.size() == 1) {
/* 1642 */       return list.get(0);
/*      */     }
/*      */ 
/*      */     
/* 1646 */     Map<String, String> codeMap = ((ColumnRuleDto)RwaConfig.getEcColumnRuleMap().get(key)).getCodeMap();
/*      */     
/* 1648 */     Collections.sort(list, (Comparator<? super EcFactorDo>)new Object(codeMap));
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
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 1685 */     return list.get(0);
/*      */   }
/*      */   
/*      */   public static <T> void putObject(Map<String, List<T>> map, String key, T value) {
/* 1689 */     List<T> list = map.get(key);
/* 1690 */     if (list == null) {
/* 1691 */       list = new ArrayList<>();
/* 1692 */       map.put(key, list);
/*      */     } 
/* 1694 */     list.add(value);
/*      */   }
/*      */ 
/*      */   
/*      */   public static String getEcParamInfo(List<EcFactorDo> list) {
/* 1699 */     List<Map<String, String>> rl = new ArrayList<>();
/* 1700 */     for (EcFactorDo ecFactorDo : list) {
/* 1701 */       Map<String, String> map = new LinkedHashMap<>();
/* 1702 */       map.put("paramId", ecFactorDo.getParamId());
/* 1703 */       map.put("adjDim", ecFactorDo.getEcAdjDim());
/* 1704 */       map.put("scopeValue", ecFactorDo.getScopeValue());
/* 1705 */       map.put("effectiveDate", DateUtil.formatDate(ecFactorDo.getEffectiveDate()));
/* 1706 */       map.put("adjFactor", DataUtils.formatBigDecimal(ecFactorDo.getEcAdjFactor(), "0.##%"));
/* 1707 */       rl.add(map);
/*      */     } 
/* 1709 */     return JsonUtils.object2Json(rl);
/*      */   }
/*      */   
/*      */   public static boolean isRealEstateExposure(String exposureTypeWa) {
/* 1713 */     return DataUtils.isInList(exposureTypeWa, new String[] { SpecExposureTypeWa.CRE.getCode(), SpecExposureTypeWa.RRE.getCode() });
/*      */   }
/*      */ 
/*      */   
/*      */   public static Map<String, Map<String, Object>> getRealEstateExposureMap(List<Map<String, Object>> exposureList) {
/* 1718 */     Map<String, Map<String, Object>> map = new HashMap<>(exposureList.size());
/* 1719 */     for (Map<String, Object> exposure : exposureList) {
/* 1720 */       String type = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/* 1721 */       if (StrUtil.isEmpty(type)) {
/*      */         continue;
/*      */       }
/* 1724 */       if (isRealEstateExposure(type)) {
/* 1725 */         map.put(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID), exposure);
/*      */       }
/*      */     } 
/* 1728 */     return map;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static Map<String, Map<String, Object>> getRealEstateExposureMap(List<Map<String, Object>> exposureList, Map<String, Map<String, RelevanceDto>> exposureRelevanceMap, Map<String, List<RelevanceDto>> mitigationRelevanceMap) {
/* 1735 */     Map<String, Map<String, Object>> reeMap = getRealEstateExposureMap(exposureList);
/* 1736 */     if (CollUtil.isEmpty(exposureRelevanceMap) || CollUtil.isEmpty(mitigationRelevanceMap)) {
/* 1737 */       return reeMap;
/*      */     }
/* 1739 */     Map<String, Map<String, Object>> exposureMap = (Map<String, Map<String, Object>>)exposureList.stream().collect(Collectors.toMap(exposure -> DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID), Function.identity()));
/* 1740 */     Map<String, Map<String, Object>> map = new HashMap<>(reeMap);
/* 1741 */     for (Map<String, Object> exposure : reeMap.values()) {
/* 1742 */       String exposureId = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID);
/* 1743 */       Map<String, RelevanceDto> exposureRelevance = exposureRelevanceMap.get(exposureId);
/* 1744 */       if (CollUtil.isEmpty(exposureRelevance)) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/* 1749 */       for (RelevanceDto relevanceDto : exposureRelevance.values()) {
/* 1750 */         if (StrUtil.equals(relevanceDto.getMitigationType(), MitigationType.COLLATERAL.getCode())) {
/*      */           
/* 1752 */           List<RelevanceDto> relevanceDtoList = mitigationRelevanceMap.get(relevanceDto.getMitigationId());
/* 1753 */           for (RelevanceDto collRel : relevanceDtoList) {
/* 1754 */             if (!StrUtil.equals(collRel.getExposureId(), exposureId)) {
/* 1755 */               map.putIfAbsent(collRel.getExposureId(), exposureMap.get(collRel.getExposureId()));
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/*      */     } 
/* 1761 */     return map;
/*      */   }
/*      */ 
/*      */   
/*      */   public static void calculateLtv(Map<String, Map<String, Object>> reeMap, Map<String, List<RelevanceDto>> mitigationRelevanceMap, List<Map<String, Object>> collateralList) {
/* 1766 */     if (CollUtil.isEmpty(reeMap)) {
/*      */       return;
/*      */     }
/*      */     
/* 1770 */     Map<String, Map<String, BigDecimal>> exposureRecMap = new HashMap<>(reeMap.size());
/*      */     
/* 1772 */     Map<String, Set<String>> exposureRecTypeMap = new HashMap<>(reeMap.size());
/*      */     
/* 1774 */     if (CollUtil.isEmpty(collateralList) || CollUtil.isEmpty(mitigationRelevanceMap)) {
/* 1775 */       calculateLtv(reeMap, exposureRecMap, exposureRecTypeMap);
/*      */       
/*      */       return;
/*      */     } 
/* 1779 */     for (Map<String, Object> collateral : collateralList) {
/*      */       
/* 1781 */       String realEstateType = DataUtils.getString(collateral, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE);
/* 1782 */       if (isNotRealEstateCollateral(realEstateType)) {
/*      */         continue;
/*      */       }
/*      */       
/* 1786 */       BigDecimal initialValue = DataUtils.getBigDecimal(collateral, (ICodeEnum)RwaParam.INITIAL_VALUE);
/* 1787 */       if (RwaMath.isNullOrNegative(initialValue)) {
/*      */         continue;
/*      */       }
/*      */ 
/*      */       
/* 1792 */       String isPrudential = DataUtils.getString(collateral, (ICodeEnum)RwaParam.IS_PRUDENTIAL);
/*      */       
/* 1794 */       BigDecimal total = BigDecimal.ZERO;
/* 1795 */       List<RelevanceDto> relevanceDtoList = mitigationRelevanceMap.get(DataUtils.getString(collateral, (ICodeEnum)RwaParam.COLLATERAL_ID));
/* 1796 */       List<ReeRelevanceDto> rrList = new ArrayList<>(relevanceDtoList.size());
/* 1797 */       for (RelevanceDto relevanceDto : relevanceDtoList) {
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 1802 */         Map<String, Object> exposure = reeMap.get(relevanceDto.getExposureId());
/*      */         
/* 1804 */         if (exposure == null) {
/*      */           continue;
/*      */         }
/*      */         
/* 1808 */         DataUtils.add2SetMap(exposureRecTypeMap, realEstateType, relevanceDto.getExposureId());
/*      */         
/* 1810 */         if (StrUtil.isEmpty(isPrudential) || !Identity.YES.getCode().equals(isPrudential)) {
/*      */           continue;
/*      */         }
/*      */         
/* 1814 */         BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/* 1815 */         if (RwaMath.isNullOrNegative(balance)) {
/* 1816 */           balance = BigDecimal.ZERO;
/*      */         }
/* 1818 */         total = RwaMath.add(total, balance);
/* 1819 */         rrList.add(new ReeRelevanceDto(relevanceDto.getExposureId(), balance));
/*      */       } 
/*      */       
/* 1822 */       for (ReeRelevanceDto rr : rrList) {
/* 1823 */         BigDecimal value = BigDecimal.ZERO;
/* 1824 */         if (!RwaMath.isZero(total)) {
/* 1825 */           value = RwaMath.mul(RwaMath.div(rr.getBalance(), total), initialValue);
/*      */         }
/*      */         
/* 1828 */         Map<String, BigDecimal> recValueMap = exposureRecMap.get(rr.getExposureId());
/* 1829 */         if (CollUtil.isEmpty(recValueMap)) {
/* 1830 */           recValueMap = new HashMap<>();
/* 1831 */           exposureRecMap.put(rr.getExposureId(), recValueMap);
/*      */         } 
/* 1833 */         recValueMap.put(realEstateType, RwaMath.add(recValueMap.get(realEstateType), value));
/*      */       } 
/*      */     } 
/*      */     
/* 1837 */     calculateLtv(reeMap, exposureRecMap, exposureRecTypeMap);
/*      */   }
/*      */ 
/*      */   
/*      */   public static void calculateLtv(Map<String, Map<String, Object>> reeMap, Map<String, Map<String, BigDecimal>> exposureRecMap, Map<String, Set<String>> exposureRecTypeMap) {
/* 1842 */     for (Map<String, Object> exposure : reeMap.values()) {
/* 1843 */       String type = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/*      */       
/* 1845 */       if (StrUtil.isEmpty(type) || !isRealEstateExposure(type)) {
/*      */         continue;
/*      */       }
/* 1848 */       String exposureId = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID);
/*      */       
/* 1850 */       DataUtils.remove(exposure, (ICodeEnum)RwaParam.IS_PRUDENTIAL);
/* 1851 */       DataUtils.remove(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE);
/* 1852 */       DataUtils.remove(exposure, (ICodeEnum)RwaParam.LTV);
/* 1853 */       Map<String, BigDecimal> recValueMap = exposureRecMap.get(exposureId);
/*      */       
/* 1855 */       if (CollUtil.isEmpty(recValueMap)) {
/* 1856 */         setReeNotPrudential(exposure, exposureRecTypeMap.get(exposureId));
/*      */         continue;
/*      */       } 
/* 1859 */       BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*      */       
/* 1861 */       if (RwaMath.isNullOrNegative(balance) || RwaMath.isZero(balance)) {
/* 1862 */         setReeNotPrudential(exposure, exposureRecTypeMap.get(exposureId));
/*      */         
/*      */         continue;
/*      */       } 
/* 1866 */       BigDecimal rv = recValueMap.get(RealEstateType.RESIDENTIAL.getCode());
/* 1867 */       BigDecimal cv = recValueMap.get(RealEstateType.COMMERCIAL.getCode());
/* 1868 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RESIDENTIAL_VALUE, rv);
/* 1869 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.COMMERCIAL_VALUE, cv);
/* 1870 */       BigDecimal rltv = null;
/* 1871 */       BigDecimal cltv = null;
/*      */       
/* 1873 */       if (!RwaMath.isZeroOrNull(rv)) {
/* 1874 */         rltv = RwaMath.div(balance, rv, 8);
/*      */         
/* 1876 */         if (NumberUtil.isGreaterOrEqual(rltv, BigDecimal.valueOf(999L))) {
/* 1877 */           rltv = null;
/*      */         }
/* 1879 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.RLTV, rltv);
/*      */       } 
/*      */       
/* 1882 */       if (!RwaMath.isZeroOrNull(cv)) {
/*      */         
/* 1884 */         cltv = RwaMath.div(balance, RwaMath.add(rv, cv), 8);
/*      */         
/* 1886 */         if (NumberUtil.isGreaterOrEqual(cltv, BigDecimal.valueOf(999L))) {
/* 1887 */           cltv = null;
/*      */         }
/* 1889 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CLTV, cltv);
/*      */       } 
/*      */       
/* 1892 */       if (rltv == null && cltv == null) {
/* 1893 */         setReeNotPrudential(exposure, exposureRecTypeMap.get(exposureId)); continue;
/*      */       } 
/* 1895 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_PRUDENTIAL, Identity.YES.getCode());
/* 1896 */       if (rltv == null) {
/* 1897 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.COMMERCIAL.getCode());
/* 1898 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.LTV, cltv); continue;
/* 1899 */       }  if (cltv == null) {
/* 1900 */         DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.RESIDENTIAL.getCode());
/* 1901 */         DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.LTV, rltv);
/*      */       } 
/*      */     } 
/*      */   }
/*      */ 
/*      */   
/*      */   public static void setReeNotPrudential(Map<String, Object> exposure, Set<String> recTypes) {
/* 1908 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.IS_PRUDENTIAL, Identity.NO.getCode());
/* 1909 */     if (!CollUtil.isEmpty(recTypes) && recTypes.contains(RealEstateType.RESIDENTIAL.getCode())) {
/* 1910 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.RESIDENTIAL.getCode());
/*      */     } else {
/*      */       
/* 1913 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.REAL_ESTATE_TYPE, RealEstateType.COMMERCIAL.getCode());
/*      */     } 
/*      */   }
/*      */   
/*      */   public static boolean isNotRealEstateCollateral(String realEstateType) {
/* 1918 */     if (StrUtil.isEmpty(realEstateType)) {
/* 1919 */       return true;
/*      */     }
/* 1921 */     if (RealEstateType.RESIDENTIAL.getCode().equals(realEstateType) || RealEstateType.COMMERCIAL.getCode().equals(realEstateType)) {
/* 1922 */       return false;
/*      */     }
/* 1924 */     return true;
/*      */   }
/*      */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\RwaUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */