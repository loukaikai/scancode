/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ 
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.convert.Convert;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.entity.EngineConfigDo;
/*     */ import com.amarsoft.rwa.engine.job.JobUtils;
/*     */ import com.amarsoft.rwa.engine.mapper.EngineConfigMapper;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.baomidou.mybatisplus.core.conditions.Wrapper;
/*     */ import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.stereotype.Service;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ 
/*     */ 
/*     */ @Service
/*     */ public class ConfigService
/*     */ {
/*  34 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.ConfigService.class);
/*     */ 
/*     */   
/*     */   @Autowired
/*     */   private EngineConfigMapper engineConfigMapper;
/*     */ 
/*     */   
/*     */   public void initEngineConfig() {
/*  42 */     QueryWrapper<EngineConfigDo> queryWrapper = (QueryWrapper<EngineConfigDo>)(new QueryWrapper()).eq("enable_status", "1");
/*  43 */     initEngineConfig(this.engineConfigMapper.selectList((Wrapper)queryWrapper));
/*  44 */     log.info("初始化引擎计算参数完成");
/*     */   }
/*     */   
/*     */   public void initEngineConfig(List<EngineConfigDo> engineConfigDoList) {
/*  48 */     Map<String, EngineConfigDo> configMap = null;
/*  49 */     if (CollUtil.isEmpty(engineConfigDoList)) {
/*  50 */       configMap = new HashMap<>();
/*     */     } else {
/*  52 */       configMap = (Map<String, EngineConfigDo>)engineConfigDoList.stream().collect(Collectors.toMap(EngineConfigDo::getParamId, Function.identity()));
/*     */     } 
/*  54 */     initEngineConfig(configMap);
/*     */   }
/*     */   
/*     */   public void initEngineConfig(Map<String, EngineConfigDo> configMap) {
/*  58 */     RwaConfig.headOrgId = getStringConfig(configMap, "headOrgId", "1");
/*     */     
/*  60 */     RwaConfig.rwLineAdjustMode = getStringConfig(configMap, "rwLineAdjustMode", "1");
/*     */     
/*  62 */     RwaConfig.ecCalculationMode = getStringConfig(configMap, "ecCalculationMode", "1");
/*     */     
/*  64 */     RwaConfig.waMitigateMode = getStringConfig(configMap, "waMitigateMode", "2");
/*     */     
/*  66 */     RwaConfig.diCvaAssetType = getStringConfig(configMap, "diCvaAssetType", "");
/*  67 */     RwaConfig.sftCvaAssetType = getStringConfig(configMap, "sftCvaAssetType", "");
/*     */     
/*  69 */     RwaConfig.cvaIndustryId = getStringConfig(configMap, "cvaIndustryId", "J66S662");
/*  70 */     RwaConfig.cvaBusinessLine = getStringConfig(configMap, "cvaBusinessLine", "");
/*     */     
/*  72 */     RwaConfig.rmbCurrencyCode = getStringConfig(configMap, "rmbCurrencyCode", "CNY,CNH,156,CHN");
/*     */ 
/*     */ 
/*     */     
/*  76 */     RwaConfig.enableMappingExceptionOut = getBooleanConfig(configMap, "enableMappingExceptionOut", Boolean.valueOf(false)).booleanValue();
/*  77 */     RwaConfig.enablePriorityMitigateByProportional = getBooleanConfig(configMap, "enablePriorityMitigateByProportional", Boolean.valueOf(false)).booleanValue();
/*  78 */     RwaConfig.enableConsolidatedSubWaCalculate = getBooleanConfig(configMap, "enableConsolidatedSubWaCalculate", Boolean.valueOf(true)).booleanValue();
/*     */     
/*  80 */     RwaConfig.enableRealEstateLtvCalculate = getBooleanConfig(configMap, "enableRealEstateLtvCalculate", Boolean.valueOf(true)).booleanValue();
/*     */     
/*  82 */     RwaConfig.enableRetailIrbProportional = getBooleanConfig(configMap, "enableRetailIrbProportional", Boolean.valueOf(false)).booleanValue();
/*     */ 
/*     */ 
/*     */     
/*  86 */     JobUtils.chunkSize = getIntegerConfig(configMap, "chunkSize", 1000);
/*  87 */     JobUtils.batchSize = getIntegerConfig(configMap, "batchSize", 2000);
/*  88 */     JobUtils.singleThreadCount = getIntegerConfig(configMap, "singleThreadCount", 100000);
/*  89 */     JobUtils.enableBigGroup = getBooleanConfig(configMap, "enableBigGroup", Boolean.valueOf(true)).booleanValue();
/*  90 */     JobUtils.bigGroupCount = getIntegerConfig(configMap, "bigGroupCount", 200000);
/*  91 */     JobUtils.writerAssertUpdates = getBooleanConfig(configMap, "writerAssertUpdates", Boolean.valueOf(true));
/*  92 */     JobUtils.taskPollingInterval = getIntegerConfig(configMap, "taskPollingInterval", 10000).intValue();
/*  93 */     JobUtils.syncIntervalTime = getIntegerConfig(configMap, "syncIntervalTime", 30).intValue();
/*     */     
/*  95 */     JobUtils.groupTimePercent = getPercentList(getBigDecimalListConfig(configMap, "groupTimePercent", null));
/*  96 */     JobUtils.rwaResultTimePercent = getBigDecimalConfig(configMap, "rwaResultTimePercent", "0.01");
/*     */     
/*  98 */     JobUtils.enableThreadTaskInit = getBooleanConfig(configMap, "enableThreadTaskInit", Boolean.valueOf(true)).booleanValue();
/*  99 */     JobUtils.enableLocalThreadCalculate = getBooleanConfig(configMap, "enableLocalThreadCalculate", Boolean.valueOf(false)).booleanValue();
/*     */   }
/*     */   
/*     */   public String getStringConfig(Map<String, EngineConfigDo> configMap, String paramId) {
/* 103 */     EngineConfigDo configDo = configMap.get(paramId);
/* 104 */     if (configDo != null) {
/* 105 */       return configDo.getParamConfig();
/*     */     }
/* 107 */     return null;
/*     */   }
/*     */   
/*     */   public String getStringConfig(Map<String, EngineConfigDo> configMap, String paramId, String defaultValue) {
/* 111 */     String config = getStringConfig(configMap, paramId);
/* 112 */     if (config == null) {
/* 113 */       return defaultValue;
/*     */     }
/* 115 */     return config;
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimalConfig(Map<String, EngineConfigDo> configMap, String paramId) {
/* 119 */     String config = getStringConfig(configMap, paramId);
/* 120 */     if (config == null) {
/* 121 */       return null;
/*     */     }
/* 123 */     return Convert.toBigDecimal(config);
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimalConfig(Map<String, EngineConfigDo> configMap, String paramId, BigDecimal defaultValue) {
/* 127 */     BigDecimal config = getBigDecimalConfig(configMap, paramId);
/* 128 */     if (config == null) {
/* 129 */       return defaultValue;
/*     */     }
/* 131 */     return config;
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimalConfig(Map<String, EngineConfigDo> configMap, String paramId, String defaultValue) {
/* 135 */     return getBigDecimalConfig(configMap, paramId, new BigDecimal(defaultValue));
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimalConfig(Map<String, EngineConfigDo> configMap, String paramId, Integer defaultValue) {
/* 139 */     return getBigDecimalConfig(configMap, paramId, BigDecimal.valueOf(defaultValue.intValue()));
/*     */   }
/*     */   
/*     */   public List<BigDecimal> getBigDecimalListConfig(Map<String, EngineConfigDo> configMap, String paramId, List<BigDecimal> defaultValue) {
/* 143 */     String config = getStringConfig(configMap, paramId);
/* 144 */     if (config == null || config.trim().length() == 0) {
/* 145 */       return defaultValue;
/*     */     }
/* 147 */     String[] configs = config.split(",");
/* 148 */     List<BigDecimal> bdList = new ArrayList<>(configs.length);
/* 149 */     for (String cf : configs) {
/* 150 */       bdList.add(Convert.toBigDecimal(cf));
/*     */     }
/* 152 */     return bdList;
/*     */   }
/*     */   
/*     */   public List<BigDecimal> getPercentList(List<BigDecimal> list) {
/* 156 */     if (CollUtil.isEmpty(list)) {
/* 157 */       return null;
/*     */     }
/* 159 */     BigDecimal total = BigDecimal.ZERO;
/* 160 */     for (BigDecimal bd : list) {
/* 161 */       bd = RwaMath.isNullOrNegative(bd) ? BigDecimal.ZERO : bd;
/* 162 */       total = total.add(bd);
/*     */     } 
/* 164 */     List<BigDecimal> resultList = new ArrayList<>();
/* 165 */     for (BigDecimal bd : list) {
/* 166 */       bd = RwaMath.isNullOrNegative(bd) ? BigDecimal.ZERO : bd;
/* 167 */       resultList.add(RwaMath.div(bd, total));
/*     */     } 
/* 169 */     return resultList;
/*     */   }
/*     */   
/*     */   public Boolean getBooleanConfig(Map<String, EngineConfigDo> configMap, String paramId) {
/* 173 */     String config = getStringConfig(configMap, paramId);
/* 174 */     if (config == null) {
/* 175 */       return null;
/*     */     }
/* 177 */     if (StrUtil.equals(config, Identity.YES.getCode())) {
/* 178 */       return Boolean.valueOf(true);
/*     */     }
/* 180 */     if (StrUtil.equals(config, Identity.NO.getCode())) {
/* 181 */       return Boolean.valueOf(false);
/*     */     }
/* 183 */     return null;
/*     */   }
/*     */   
/*     */   public Boolean getBooleanConfig(Map<String, EngineConfigDo> configMap, String paramId, Boolean defaultValue) {
/* 187 */     Boolean config = getBooleanConfig(configMap, paramId);
/* 188 */     if (config == null) {
/* 189 */       return defaultValue;
/*     */     }
/* 191 */     return config;
/*     */   }
/*     */   
/*     */   public Integer getIntegerConfig(Map<String, EngineConfigDo> configMap, String paramId) {
/* 195 */     String config = getStringConfig(configMap, paramId);
/* 196 */     if (config == null) {
/* 197 */       return null;
/*     */     }
/* 199 */     return Convert.toInt(config);
/*     */   }
/*     */   
/*     */   public Integer getIntegerConfig(Map<String, EngineConfigDo> configMap, String paramId, int defaultValue) {
/* 203 */     Integer config = getIntegerConfig(configMap, paramId);
/* 204 */     if (config == null) {
/* 205 */       return Integer.valueOf(defaultValue);
/*     */     }
/* 207 */     return config;
/*     */   }
/*     */   
/*     */   public Map<String, BigDecimal> getNumberMapConfig(Map<String, EngineConfigDo> configMap, String paramId, String defaultValue) {
/* 211 */     Assert.notNull(defaultValue, "默认值不能为空！defaultValue=" + defaultValue);
/* 212 */     String config = getStringConfig(configMap, paramId);
/* 213 */     if (config == null || config.trim().length() == 0) {
/* 214 */       config = defaultValue;
/*     */     }
/* 216 */     Map<String, Object> map = JsonUtils.convertJson2Map(config);
/* 217 */     return DataUtils.convert2NumberMap(map);
/*     */   }
/*     */   
/*     */   public Map<String, String> getStringMapConfig(Map<String, EngineConfigDo> configMap, String paramId, String defaultValue) {
/* 221 */     Assert.notNull(defaultValue, "默认值不能为空！defaultValue=" + defaultValue);
/* 222 */     String config = getStringConfig(configMap, paramId);
/* 223 */     if (config == null || config.trim().length() == 0) {
/* 224 */       config = defaultValue;
/*     */     }
/* 226 */     Map<String, Object> map = JsonUtils.convertJson2Map(config);
/* 227 */     return DataUtils.convert2StringMap(map);
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\ConfigService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */