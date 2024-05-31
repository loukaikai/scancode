/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.CacheId;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.MitigateSortType;
/*     */ import com.amarsoft.rwa.engine.constant.ParamTemplate;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.ColumnRuleDto;
/*     */ import com.amarsoft.rwa.engine.entity.ColumnScopeDto;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureParamLimitDto;
/*     */ import com.amarsoft.rwa.engine.entity.FormulaDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateAssetDo;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSchemeDo;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*     */ import com.amarsoft.rwa.engine.entity.ParamColumnDo;
/*     */ import com.amarsoft.rwa.engine.entity.ParamDetailDo;
/*     */ import com.amarsoft.rwa.engine.entity.ParamTemplateDo;
/*     */ import com.amarsoft.rwa.engine.entity.ParamVersionDo;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeAssetDo;
/*     */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*     */ import com.baomidou.mybatisplus.core.conditions.Wrapper;
/*     */ import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/*     */ import java.lang.invoke.SerializedLambda;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;
/*     */ import java.util.stream.Collectors;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ 
/*     */ @Service
/*     */ public class ParamService {
/*  41 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.ParamService.class);
/*     */   
/*     */   @Autowired
/*     */   private MitigateSchemeMapper mitigateSchemeMapper;
/*     */   
/*     */   @Autowired
/*     */   private MitigateAssetMapper mitigateAssetMapper;
/*     */   
/*     */   @Autowired
/*     */   private MitigateSortMapper mitigateSortMapper;
/*     */   
/*     */   @Autowired
/*     */   private ParamVersionMapper paramVersionMapper;
/*     */   
/*     */   @Autowired
/*     */   private ParamTemplateMapper paramTemplateMapper;
/*     */   
/*     */   @Autowired
/*     */   private ParamDetailMapper paramDetailMapper;
/*     */   
/*     */   @Autowired
/*     */   private ParamColumnMapper paramColumnMapper;
/*     */   
/*     */   @Autowired
/*     */   private CreditRuleMapper creditRuleMapper;
/*     */   @Autowired
/*     */   private SchemeConfigMapper schemeConfigMapper;
/*     */   @Autowired
/*     */   private SchemeAssetMapper schemeAssetMapper;
/*     */   @Autowired
/*     */   private EcFactorMapper ecFactorMapper;
/*     */   @Autowired
/*     */   private CommonService commonService;
/*     */   @Autowired
/*     */   private CacheService cacheService;
/*     */   
/*     */   public String getSchemeId(RiskDataPeriodDo dataPeriodDo, String approach) {
/*  78 */     String schemeId = StrUtil.equals(approach, Approach.WA.getCode()) ? dataPeriodDo.getWaSchemeId() : dataPeriodDo.getIrbSchemeId();
/*     */     
/*  80 */     if (StrUtil.isEmpty(schemeId)) {
/*  81 */       throw new JobParameterException("非法数据批次-数据确认异常，无计算方案[" + approach + "]");
/*     */     }
/*  83 */     return schemeId;
/*     */   }
/*     */   
/*     */   public void clearAllConfig() {
/*  87 */     this.cacheService.clearCache(CacheId.SCHEME.getCode());
/*  88 */     this.cacheService.clearCache(CacheId.HOLIDAY.getCode());
/*  89 */     this.cacheService.clearCache(CacheId.EC_FACTOR.getCode());
/*  90 */     this.cacheService.clearCache(CacheId.EC_COLUMN.getCode());
/*  91 */     log.info("清理全部参数缓存完成");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized SchemeConfigDo initSchemeConfig(@NotNull String schemeId) {
/* 100 */     SchemeConfigDo schemeConfigDo = RwaConfig.getSchemeConfig(schemeId);
/* 101 */     if (schemeConfigDo == null) {
/*     */       
/* 103 */       schemeConfigDo = ((com.amarsoft.rwa.engine.service.ParamService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.ParamService.class)).getSchemeConfig(schemeId);
/* 104 */       RwaConfig.putSchemeConfig(schemeId, schemeConfigDo);
/*     */     } 
/* 106 */     return schemeConfigDo;
/*     */   }
/*     */   
/*     */   public void clearSchemeConfig(@NotNull String schemeId) {
/* 110 */     this.cacheService.deleteCache(CacheId.SCHEME.getCode(), schemeId);
/* 111 */     log.info("清理方案配置缓存[{}]完成", schemeId);
/*     */   }
/*     */   
/*     */   public void clearSchemeConfig() {
/* 115 */     this.cacheService.clearCache(CacheId.SCHEME.getCode());
/* 116 */     log.info("清理全部方案配置缓存完成");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized SchemeConfigDo getSchemeConfig(@NotNull String schemeId) {
/* 125 */     SchemeConfigDo configDo = (SchemeConfigDo)this.cacheService.getCache(CacheId.SCHEME.getCode(), schemeId);
/* 126 */     if (configDo != null) {
/* 127 */       return configDo;
/*     */     }
/* 129 */     SchemeConfigDo schemeConfigDo = (SchemeConfigDo)this.schemeConfigMapper.selectById(schemeId);
/* 130 */     if (schemeConfigDo == null) {
/* 131 */       throw new JobParameterException("异常计算方案ID[schemeId=" + schemeId + "]");
/*     */     }
/*     */     
/* 134 */     schemeConfigDo.setSchemeAssetList(getSchemeAssetDoList(schemeId));
/* 135 */     schemeConfigDo.setSchemeAssetMap(convert2SchemeAssetMap(schemeConfigDo.getSchemeAssetList()));
/*     */     
/* 137 */     if (StrUtil.isEmpty(schemeConfigDo.getWaParamVersionNo())) {
/* 138 */       throw new JobParameterException("异常计算方案[schemeId=" + schemeId + "]未设定权重法参数版本");
/*     */     }
/* 140 */     schemeConfigDo.setWaParamVersion(((com.amarsoft.rwa.engine.service.ParamService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.ParamService.class)).getParamVersion(schemeConfigDo.getWaParamVersionNo()));
/*     */     
/* 142 */     if (StrUtil.isNotEmpty(schemeConfigDo.getIrbParamVersionNo())) {
/* 143 */       schemeConfigDo.setIrbParamVersion(((com.amarsoft.rwa.engine.service.ParamService)SpringUtil.getBean(com.amarsoft.rwa.engine.service.ParamService.class)).getParamVersion(schemeConfigDo.getIrbParamVersionNo()));
/*     */     }
/*     */     
/* 146 */     schemeConfigDo.setWaMitigateScheme(getMitigateScheme(schemeConfigDo.getWaMitigateSchemeNo()));
/*     */     
/* 148 */     schemeConfigDo.setIrbMitigateScheme(getMitigateScheme(schemeConfigDo.getIrbMitigateSchemeNo()));
/* 149 */     this.cacheService.putCache(CacheId.SCHEME.getCode(), schemeId, schemeConfigDo);
/* 150 */     log.info("初始化计算方案[{}]完成", schemeId);
/* 151 */     return schemeConfigDo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<SchemeAssetDo> getSchemeAssetDoList(String schemeId) {
/* 160 */     return this.schemeAssetMapper.selectList((Wrapper)(new LambdaQueryWrapper()).eq(SchemeAssetDo::getSchemeId, schemeId));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, SchemeAssetDo> convert2SchemeAssetMap(List<SchemeAssetDo> schemeAssetDoList) {
/* 169 */     if (CollUtil.isEmpty(schemeAssetDoList)) {
/* 170 */       return null;
/*     */     }
/* 172 */     Map<String, SchemeAssetDo> map = new HashMap<>();
/* 173 */     for (SchemeAssetDo schemeAssetDo : schemeAssetDoList) {
/* 174 */       if (StrUtil.isEmpty(schemeAssetDo.getAssetType())) {
/* 175 */         throw new ParamConfigException("方案资产方法配置异常，产品为空！schemeAssetDo=" + JsonUtils.object2Json(schemeAssetDo));
/*     */       }
/* 177 */       map.put(DataUtils.generateKey(new String[] { schemeAssetDo.getAssetType(), schemeAssetDo.getExposureType() }, ), schemeAssetDo);
/*     */     } 
/* 179 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigateSchemeDo getMitigateScheme(String mitigateSchemeNo) {
/* 188 */     if (StrUtil.isEmpty(mitigateSchemeNo)) {
/* 189 */       return null;
/*     */     }
/* 191 */     MitigateSchemeDo mitigateSchemeDo = (MitigateSchemeDo)this.mitigateSchemeMapper.selectById(mitigateSchemeNo);
/* 192 */     if (mitigateSchemeDo == null) {
/* 193 */       return null;
/*     */     }
/* 195 */     mitigateSchemeDo.setMitigateAssetDoMap(new HashMap<>());
/* 196 */     List<MitigateAssetDo> mitigateAssetDoList = getMitigateAssetDoList(mitigateSchemeNo);
/* 197 */     for (MitigateAssetDo mitigateAssetDo : mitigateAssetDoList) {
/* 198 */       initMitigateAssetDo(mitigateAssetDo);
/* 199 */       if (StrUtil.equals(mitigateAssetDo.getIsDefault(), Identity.YES.getCode())) {
/* 200 */         mitigateSchemeDo.setDefaultMitigateAsset(mitigateAssetDo); continue;
/*     */       } 
/* 202 */       if (StrUtil.isEmpty(mitigateAssetDo.getAssetType())) {
/* 203 */         throw new ParamConfigException("资产缓释方法配置异常！mitigateAssetDo=" + mitigateAssetDo);
/*     */       }
/*     */       
/* 206 */       String[] assetTypes = mitigateAssetDo.getAssetType().split(",");
/* 207 */       for (String asset : assetTypes) {
/* 208 */         mitigateSchemeDo.getMitigateAssetDoMap().put(asset, mitigateAssetDo);
/*     */       }
/*     */     } 
/*     */     
/* 212 */     log.info("初始化缓释方案[{}]完成", mitigateSchemeNo);
/* 213 */     return mitigateSchemeDo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MitigateAssetDo> getMitigateAssetDoList(String mitigateSchemeNo) {
/* 222 */     return this.mitigateAssetMapper.selectList((Wrapper)(new LambdaQueryWrapper()).eq(MitigateAssetDo::getMitigateSchemeNo, mitigateSchemeNo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public MitigateAssetDo initMitigateAssetDo(MitigateAssetDo mitigateAssetDo) {
/* 231 */     mitigateAssetDo.setMitigateSortListMap(new HashMap<>());
/* 232 */     mitigateAssetDo.getMitigateSortListMap().put(MitigateSortType.EXPOSURE, getMitigateSortDoList(mitigateAssetDo.getMitigateMethodNo(), MitigateSortType.EXPOSURE));
/* 233 */     mitigateAssetDo.getMitigateSortListMap().put(MitigateSortType.MITIGATION, getMitigateSortDoList(mitigateAssetDo.getMitigateMethodNo(), MitigateSortType.MITIGATION));
/* 234 */     mitigateAssetDo.getMitigateSortListMap().put(MitigateSortType.MITIGATION_TYPE, getMitigateSortDoList(mitigateAssetDo.getMitigateMethodNo(), MitigateSortType.MITIGATION_TYPE));
/* 235 */     mitigateAssetDo.setMitigationTypeSortMap(convert2MitigationTypeSortMap((List<MitigateSortDo>)mitigateAssetDo.getMitigateSortListMap().get(MitigateSortType.MITIGATION_TYPE)));
/* 236 */     return mitigateAssetDo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<MitigateSortDo> getMitigateSortDoList(String mitigateMethodNo, MitigateSortType mitigateSortType) {
/* 246 */     return this.mitigateSortMapper.selectList((Wrapper)((LambdaQueryWrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/* 247 */         .eq(MitigateSortDo::getMitigateMethodNo, mitigateMethodNo))
/* 248 */         .eq(MitigateSortDo::getMitigateSortType, mitigateSortType.getCode()))
/* 249 */         .orderByAsc(MitigateSortDo::getSortNo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Integer> convert2MitigationTypeSortMap(List<MitigateSortDo> mitigateSortDoList) {
/* 259 */     if (CollUtil.isEmpty(mitigateSortDoList)) {
/* 260 */       return null;
/*     */     }
/* 262 */     Map<String, Integer> map = new HashMap<>();
/* 263 */     for (MitigateSortDo sortDo : mitigateSortDoList) {
/* 264 */       map.put(sortDo.getSortParam(), sortDo.getSortNo());
/*     */     }
/* 266 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ParamVersionDo getParamVersion(String paramVersionNo) {
/* 275 */     if (StrUtil.isEmpty(paramVersionNo)) {
/* 276 */       return null;
/*     */     }
/* 278 */     ParamVersionDo paramVersionDo = (ParamVersionDo)this.paramVersionMapper.selectById(paramVersionNo);
/* 279 */     if (paramVersionDo == null) {
/* 280 */       log.error("没有关联的参数版本[{}]", paramVersionNo);
/* 281 */       return null;
/*     */     } 
/*     */     
/* 284 */     paramVersionDo.setCreditRule((CreditRuleDo)this.creditRuleMapper.selectById((Serializable)paramVersionDo));
/* 285 */     if (paramVersionDo.getCreditRule() == null) {
/* 286 */       throw new ParamConfigException("参数版本未配置信用风险计量规则！paramVersionDo=" + paramVersionDo);
/*     */     }
/*     */     
/* 289 */     paramVersionDo.setParamTemplateDoMap(convert2ParamTemplateMap(this.paramTemplateMapper.selectList((Wrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/*     */             
/* 291 */             .eq(ParamTemplateDo::getParamVersionNo, paramVersionNo))
/* 292 */             .orderByAsc(ListUtil.toList((Object[])new SFunction[] { ParamTemplateDo::getParamTemplate, ParamTemplateDo::getSortNo })))));
/*     */ 
/*     */     
/* 295 */     paramVersionDo.setParamDetailListMap(convert2ParamDetailDoListMap(this.paramDetailMapper.selectList((Wrapper)((LambdaQueryWrapper)(new LambdaQueryWrapper())
/*     */             
/* 297 */             .eq(ParamDetailDo::getParamVersionNo, paramVersionNo)).orderByAsc(ParamDetailDo::getSortNo))));
/*     */ 
/*     */     
/* 300 */     paramVersionDo.setParamColumnListMap(convert2ParamColumnDoListMap(this.paramColumnMapper.selectList((Wrapper)(new LambdaQueryWrapper())
/*     */             
/* 302 */             .eq(ParamColumnDo::getParamVersionNo, paramVersionNo))));
/*     */ 
/*     */     
/* 305 */     paramVersionDo.getFormulaListMap().put(ExposureApproach.FIRB.getCode(), convert2FormulaList((List<ParamDetailDo>)paramVersionDo
/* 306 */           .getParamDetailListMap().get(ParamTemplate.FORMULA_FIRB.getCode()), paramVersionDo
/* 307 */           .getParamColumnListMap()));
/*     */     
/* 309 */     paramVersionDo.getFormulaListMap().put(ExposureApproach.AIRB.getCode(), convert2FormulaList((List<ParamDetailDo>)paramVersionDo
/* 310 */           .getParamDetailListMap().get(ParamTemplate.FORMULA_AIRB.getCode()), paramVersionDo
/* 311 */           .getParamColumnListMap()));
/*     */     
/* 313 */     paramVersionDo.getFormulaListMap().put(ExposureApproach.RIRB.getCode(), convert2FormulaList((List<ParamDetailDo>)paramVersionDo
/* 314 */           .getParamDetailListMap().get(ParamTemplate.FORMULA_RIRB.getCode()), paramVersionDo
/* 315 */           .getParamColumnListMap()));
/*     */     
/* 317 */     paramVersionDo.setFormulaIdMap(convert2FormulaIdMap(paramVersionDo.getFormulaListMap()));
/* 318 */     paramVersionDo.setFormulaExposureMap(convert2FormulaExposureMap(paramVersionDo.getFormulaListMap()));
/*     */ 
/*     */     
/* 321 */     List<ExposureParamLimitDto> nrLgdLimitList = getExposureParamLimitList((List<ParamDetailDo>)paramVersionDo
/* 322 */         .getParamDetailListMap().get(ParamTemplate.LGD_LIMIT_NR.getCode()), paramVersionDo
/* 323 */         .getParamColumnListMap());
/*     */ 
/*     */     
/* 326 */     paramVersionDo.setNrLgdLimitMap(convert2ExposureParamLimitMap(nrLgdLimitList));
/* 327 */     List<ExposureParamLimitDto> reLgdLimitList = getExposureParamLimitList((List<ParamDetailDo>)paramVersionDo
/* 328 */         .getParamDetailListMap().get(ParamTemplate.LGD_LIMIT_RE.getCode()), paramVersionDo
/* 329 */         .getParamColumnListMap());
/*     */     
/* 331 */     paramVersionDo.setReLgdLimitMap(convert2ExposureParamLimitMap(reLgdLimitList));
/* 332 */     log.info("初始化参数版本[{}]完成", paramVersionNo);
/* 333 */     return paramVersionDo;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ParamTemplateDo> convert2ParamTemplateMap(@NotNull List<ParamTemplateDo> paramTemplateDoList) {
/* 342 */     return (Map<String, ParamTemplateDo>)paramTemplateDoList.stream().collect(Collectors.toMap(ParamTemplateDo::getParamTemplateColNo, Function.identity()));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<ParamDetailDo>> convert2ParamDetailDoListMap(@NotNull List<ParamDetailDo> paramDetailDoList) {
/* 351 */     return (Map<String, List<ParamDetailDo>>)paramDetailDoList.stream().collect(Collectors.groupingBy(ParamDetailDo::getParamTemplate));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, List<ParamColumnDo>> convert2ParamColumnDoListMap(@NotNull List<ParamColumnDo> paramColumnDoList) {
/* 360 */     return (Map<String, List<ParamColumnDo>>)paramColumnDoList.stream().collect(Collectors.groupingBy(ParamColumnDo::getParamDetailNo));
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<ExposureParamLimitDto> getExposureParamLimitList(List<ParamDetailDo> detailDoList, Map<String, List<ParamColumnDo>> paramColumnListMap) {
/* 370 */     if (CollUtil.isEmpty(detailDoList)) {
/* 371 */       return null;
/*     */     }
/* 373 */     List<ExposureParamLimitDto> list = new ArrayList<>();
/* 374 */     for (ParamDetailDo detailDo : detailDoList) {
/* 375 */       ExposureParamLimitDto limitDto = new ExposureParamLimitDto();
/* 376 */       limitDto.setLimitNo(detailDo.getParamDetailNo());
/* 377 */       for (ParamColumnDo paramColumnDo : paramColumnListMap.get(detailDo.getParamDetailNo())) {
/*     */         
/*     */         try {
/* 380 */           if (StrUtil.equals(paramColumnDo.getRuleColCode().toLowerCase(), RwaParam.EXPOSURE_MAIN_TYPE_IRB.getCode()) || 
/* 381 */             StrUtil.equals(paramColumnDo.getRuleColCode().toLowerCase(), RwaParam.EXPOSURE_TYPE_IRB.getCode())) {
/* 382 */             limitDto.setExposureType(paramColumnDo.getRuleConfigContent());
/*     */             continue;
/*     */           } 
/* 385 */           if (StrUtil.equals(paramColumnDo.getRuleColCode().toLowerCase(), RwaParam.PROVIDE_MITIGATION_TYPE.getCode())) {
/* 386 */             limitDto.setProvideMitigationType(paramColumnDo.getRuleConfigContent());
/*     */             continue;
/*     */           } 
/* 389 */           if (paramColumnDo.getRuleColCode().toLowerCase().contains("limit")) {
/* 390 */             limitDto.setLimit(NumberUtil.toBigDecimal(paramColumnDo.getRuleConfigContent()));
/*     */           }
/* 392 */         } catch (Exception e) {
/* 393 */           throw new CodeMappingException("参数版本配置异常：ParamDetailDo=" + detailDo, e);
/*     */         } 
/*     */       } 
/* 396 */       list.add(limitDto);
/*     */     } 
/* 398 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, BigDecimal> convert2ExposureParamLimitMap(List<ExposureParamLimitDto> list) {
/* 407 */     if (CollUtil.isEmpty(list)) {
/* 408 */       return null;
/*     */     }
/* 410 */     Map<String, BigDecimal> map = new HashMap<>();
/* 411 */     for (ExposureParamLimitDto limitDto : list) {
/* 412 */       if (StrUtil.isEmpty(limitDto.getExposureType())) {
/* 413 */         throw new ParamConfigException("参数配置异常，无暴露类型，ExposureParamLimitDto=" + JsonUtils.object2Json(limitDto));
/*     */       }
/* 415 */       map.put(DataUtils.generateKey(new String[] { limitDto.getExposureType(), limitDto.getProvideMitigationType() }, ), limitDto.getLimit());
/*     */     } 
/* 417 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<FormulaDto> convert2FormulaList(List<ParamDetailDo> detailDoList, Map<String, List<ParamColumnDo>> paramColumnListMap) {
/* 427 */     if (CollUtil.isEmpty(detailDoList)) {
/* 428 */       return null;
/*     */     }
/* 430 */     List<FormulaDto> list = new ArrayList<>();
/* 431 */     for (ParamDetailDo detailDo : detailDoList) {
/* 432 */       FormulaDto formulaDto = new FormulaDto();
/* 433 */       formulaDto.setFormulaNo(detailDo.getParamDetailNo());
/* 434 */       for (ParamColumnDo paramColumnDo : paramColumnListMap.get(detailDo.getParamDetailNo())) {
/*     */         
/*     */         try {
/* 437 */           DataUtils.setValue(formulaDto, paramColumnDo.getRuleColCode(), paramColumnDo.getRuleConfigContent());
/* 438 */         } catch (Exception e) {
/* 439 */           throw new CodeMappingException("参数版本配置异常：ParamDetailDo=" + detailDo, e);
/*     */         } 
/*     */       } 
/* 442 */       list.add(formulaDto);
/*     */     } 
/* 444 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, FormulaDto> convert2FormulaIdMap(Map<String, List<FormulaDto>> formulaListMap) {
/* 453 */     if (CollUtil.isEmpty(formulaListMap)) {
/* 454 */       return null;
/*     */     }
/* 456 */     Map<String, FormulaDto> map = new HashMap<>();
/* 457 */     for (List<FormulaDto> list : formulaListMap.values()) {
/* 458 */       if (CollUtil.isEmpty(list)) {
/*     */         continue;
/*     */       }
/* 461 */       for (FormulaDto formulaDto : list) {
/* 462 */         if (StrUtil.isEmpty(formulaDto.getFormulaNo())) {
/* 463 */           throw new ParamConfigException("计算公式配置异常，ID为空！formulaDto=" + JsonUtils.object2Json(formulaDto));
/*     */         }
/* 465 */         map.put(formulaDto.getFormulaNo(), formulaDto);
/*     */       } 
/*     */     } 
/* 468 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, Map<String, FormulaDto>> convert2FormulaExposureMap(Map<String, List<FormulaDto>> formulaListMap) {
/* 477 */     if (CollUtil.isEmpty(formulaListMap)) {
/* 478 */       return null;
/*     */     }
/* 480 */     Map<String, Map<String, FormulaDto>> map = new HashMap<>();
/* 481 */     for (String approach : formulaListMap.keySet()) {
/* 482 */       List<FormulaDto> list = formulaListMap.get(approach);
/* 483 */       if (CollUtil.isEmpty(list)) {
/*     */         continue;
/*     */       }
/* 486 */       Map<String, FormulaDto> formulaMap = new HashMap<>();
/* 487 */       for (FormulaDto formulaDto : list) {
/* 488 */         if (StrUtil.isEmpty(formulaDto.getExposureTypeIrb())) {
/* 489 */           throw new ParamConfigException("计算公式配置异常，暴露类型为空！formulaDto=" + JsonUtils.object2Json(formulaDto));
/*     */         }
/*     */         
/* 492 */         formulaMap.put(DataUtils.generateKey(new String[] { formulaDto.getExposureTypeIrb(), formulaDto.getSibFlag(), formulaDto.getIsVolatility() }, ), formulaDto);
/*     */       } 
/* 494 */       map.put(approach, formulaMap);
/*     */     } 
/* 496 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearHolidayConfig() {
/* 503 */     this.cacheService.clearCache(CacheId.HOLIDAY.getCode());
/* 504 */     log.info("清理假日配置缓存完成");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public synchronized Map<String, String> getHolidayConfigMap() {
/* 512 */     Map<String, String> cache = (Map<String, String>)this.cacheService.getCache(CacheId.HOLIDAY.getCode());
/* 513 */     if (CollUtil.isNotEmpty(cache)) {
/* 514 */       return cache;
/*     */     }
/* 516 */     String sql = "select DATA_DATE, IS_HOLIDAY from RWA_EP_Holiday";
/* 517 */     List<LinkedHashMap<String, Object>> list = this.commonService.select(sql);
/* 518 */     Map<String, String> map = new HashMap<>();
/* 519 */     if (CollUtil.isEmpty(list)) {
/* 520 */       return map;
/*     */     }
/* 522 */     DateTimeFormatter formatter = RwaConfig.DATE_FORMATTER;
/* 523 */     for (LinkedHashMap<String, Object> data : list) {
/* 524 */       LocalDate date = Convert.toLocalDateTime(data.get("DATA_DATE")).toLocalDate();
/* 525 */       String flag = Convert.toStr(data.get("IS_HOLIDAY"));
/* 526 */       if (data == null || StrUtil.isEmpty(flag)) {
/*     */         continue;
/*     */       }
/* 529 */       if (StrUtil.equals(flag, Identity.YES.getCode()) || StrUtil.equals(flag, Identity.NO.getCode())) {
/* 530 */         map.put(date.format(formatter), flag);
/*     */       }
/*     */     } 
/* 533 */     this.cacheService.putCache(CacheId.HOLIDAY.getCode(), map);
/* 534 */     log.info("初始化假日配置完成");
/* 535 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void clearEcConfig() {
/* 542 */     this.cacheService.clearCache(CacheId.EC_FACTOR.getCode());
/* 543 */     this.cacheService.clearCache(CacheId.EC_COLUMN.getCode());
/* 544 */     log.info("清理经济资本参数缓存完成");
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<EcFactorDo> getEcFactorList() {
/* 552 */     List<EcFactorDo> cache = (List<EcFactorDo>)this.cacheService.getCache(CacheId.EC_FACTOR.getCode());
/* 553 */     if (CollUtil.isNotEmpty(cache)) {
/* 554 */       return cache;
/*     */     }
/* 556 */     LambdaQueryWrapper<EcFactorDo> queryWrapper = new LambdaQueryWrapper();
/*     */     
/* 558 */     queryWrapper.in(EcFactorDo::getParamStatus, new Object[] { "12", "13", "31" });
/* 559 */     List<EcFactorDo> list = this.ecFactorMapper.selectList((Wrapper)queryWrapper);
/*     */     
/* 561 */     for (EcFactorDo ecFactorDo : list) {
/* 562 */       TreeMap<String, ColumnScopeDto> map = null;
/*     */       try {
/* 564 */         map = (TreeMap<String, ColumnScopeDto>)JsonUtils.objectMapper.readValue(ecFactorDo.getEcAdjScope(), (TypeReference)new Object(this));
/* 565 */       } catch (JsonProcessingException e) {
/* 566 */         throw new ParamConfigException("经济资本调整范围配置异常， ecFactorDo=" + JsonUtils.object2Json(ecFactorDo), e);
/*     */       } 
/* 568 */       ecFactorDo.setAdjScopeMap(map);
/* 569 */       ecFactorDo.setScopeValue(RwaUtils.getEcScopeKey(ecFactorDo, false));
/*     */     } 
/* 571 */     this.cacheService.putCache(CacheId.EC_FACTOR.getCode(), list);
/* 572 */     log.info("初始化经济资本系数配置完成");
/* 573 */     return list;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public Map<String, ColumnRuleDto> getEcColumnRuleConfig() {
/* 581 */     Map<String, ColumnRuleDto> cache = (Map<String, ColumnRuleDto>)this.cacheService.getCache(CacheId.EC_COLUMN.getCode());
/* 582 */     if (CollUtil.isNotEmpty(cache)) {
/* 583 */       return cache;
/*     */     }
/* 585 */     String sql = "select c.itemno as RULE_COL_CODE, c.itemname as RULE_COL_NAME, c.attribute1 as RULE_COL_ATTR, c.attribute2 as RULE_COL_TYPE, c.attribute3 as COL_FORMAT, c.attribute6 as MAPPING_MODE, c.sortno as SORT_NO from code_library c where c.codeno = 'ec_adj_dim'";
/*     */ 
/*     */     
/* 588 */     List<LinkedHashMap<String, Object>> list = this.commonService.select(sql);
/* 589 */     Map<String, ColumnRuleDto> map = new HashMap<>();
/* 590 */     for (LinkedHashMap<String, Object> data : list) {
/*     */       try {
/* 592 */         ColumnRuleDto ruleDto = rowMapperByColumnRule(data);
/*     */         
/* 594 */         if (StrUtil.equals(ruleDto.getMappingMode(), MappingMode.COMPLEX.getCode())) {
/* 595 */           if (StrUtil.isEmpty(ruleDto.getColFormat())) {
/* 596 */             throw new ParamConfigException("没有代码格式配置， ColumnRule=" + JsonUtils.object2Json(ruleDto));
/*     */           }
/*     */           
/* 599 */           List<LinkedHashMap<String, Object>> codeList = getComplexCode(ruleDto.getColFormat());
/* 600 */           if (CollUtil.isEmpty(codeList)) {
/* 601 */             throw new ParamConfigException("没有复杂层级代码配置， ColumnRule=" + JsonUtils.object2Json(ruleDto));
/*     */           }
/* 603 */           Map<String, String> codeMap = new HashMap<>(codeList.size());
/* 604 */           for (LinkedHashMap<String, Object> code : codeList) {
/* 605 */             codeMap.put(DataUtils.getString(code, "col_code"), DataUtils.getString(code, "relative_code"));
/*     */           }
/* 607 */           ruleDto.setCodeMap(codeMap);
/*     */         } 
/* 609 */         map.put(ruleDto.getRuleColCode(), ruleDto);
/* 610 */       } catch (Exception e) {
/* 611 */         throw new ParamConfigException("初始化经济资本参数规则异常", e);
/*     */       } 
/*     */     } 
/* 614 */     this.cacheService.putCache(CacheId.EC_COLUMN.getCode(), map);
/* 615 */     log.info("初始化经济资本基础字段配置完成");
/* 616 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public ColumnRuleDto rowMapperByColumnRule(LinkedHashMap<String, Object> data) throws InstantiationException, IllegalAccessException {
/* 627 */     Map<String, String> mappings = new HashMap<>();
/* 628 */     DataUtils.setMapping(mappings, "rule_col_code");
/* 629 */     DataUtils.setMapping(mappings, "rule_col_name");
/* 630 */     DataUtils.setMapping(mappings, "rule_col_name");
/* 631 */     DataUtils.setMapping(mappings, "rule_col_type");
/* 632 */     DataUtils.setMapping(mappings, "col_format");
/* 633 */     DataUtils.setMapping(mappings, "mapping_mode");
/* 634 */     DataUtils.setMapping(mappings, "sort_no");
/* 635 */     return (ColumnRuleDto)DataUtils.dataMapping(ColumnRuleDto.class, data, mappings);
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public List<LinkedHashMap<String, Object>> getComplexCode(String code) {
/* 644 */     String sql = "select c.itemno as COL_CODE, c.relativecode as RELATIVE_CODE from code_library c where c.codeno = #{code}";
/*     */     
/* 646 */     return this.commonService.select(SqlBuilder.create(sql).setString("code", code).build());
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\ParamService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */