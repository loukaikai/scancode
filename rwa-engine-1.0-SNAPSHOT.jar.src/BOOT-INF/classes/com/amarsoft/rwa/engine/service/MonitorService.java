/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ 
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.convert.Convert;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.CalculateStatus;
/*     */ import com.amarsoft.rwa.engine.constant.MonitorIndex;
/*     */ import com.amarsoft.rwa.engine.constant.RuleConfigWay;
/*     */ import com.amarsoft.rwa.engine.entity.CreditRwaDo;
/*     */ import com.amarsoft.rwa.engine.entity.MonitorLogDo;
/*     */ import com.amarsoft.rwa.engine.entity.MonitorRecordDo;
/*     */ import com.amarsoft.rwa.engine.entity.MonitorRuleDo;
/*     */ import com.amarsoft.rwa.engine.entity.MonitorWaringDo;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.mapper.MonitorLogMapper;
/*     */ import com.amarsoft.rwa.engine.mapper.MonitorRuleMapper;
/*     */ import com.amarsoft.rwa.engine.mapper.MonitorWaringMapper;
/*     */ import com.amarsoft.rwa.engine.service.CommonService;
/*     */ import com.amarsoft.rwa.engine.service.ResultService;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.IdWorker;
/*     */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import com.baomidou.mybatisplus.core.conditions.Wrapper;
/*     */ import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
/*     */ import java.lang.invoke.SerializedLambda;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Timestamp;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.scheduling.annotation.Async;
/*     */ import org.springframework.stereotype.Service;
/*     */ 
/*     */ @Service
/*     */ public class MonitorService {
/*     */   @Autowired
/*     */   private MonitorRuleMapper monitorRuleMapper;
/*     */   @Autowired
/*     */   private MonitorWaringMapper monitorWaringMapper;
/*     */   @Autowired
/*     */   private MonitorLogMapper monitorLogMapper;
/*     */   
/*  51 */   public List<MonitorRuleDo> selectMonitorRuleList(String monitorTaskId) { LambdaQueryWrapper<MonitorRuleDo> queryWrapper = new LambdaQueryWrapper();
/*  52 */     queryWrapper.eq(MonitorRuleDo::getMonitorTaskId, monitorTaskId);
/*  53 */     return this.monitorRuleMapper.selectList((Wrapper)queryWrapper); } @Autowired
/*     */   private MonitorRecordMapper monitorRecordMapper; @Autowired
/*     */   private ResultService resultService; @Autowired
/*     */   private CommonService commonService; public List<MonitorWaringDo> selectMonitorWaringList(String monitorRuleId) {
/*  57 */     LambdaQueryWrapper<MonitorWaringDo> queryWrapper = new LambdaQueryWrapper();
/*  58 */     ((LambdaQueryWrapper)queryWrapper.eq(MonitorWaringDo::getMonitorRuleId, monitorRuleId))
/*  59 */       .orderByAsc(MonitorWaringDo::getSerialNo);
/*  60 */     return this.monitorWaringMapper.selectList((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public List<MonitorRuleDo> initMonitorRuleList(String monitorTaskId) {
/*  64 */     List<MonitorRuleDo> list = selectMonitorRuleList(monitorTaskId);
/*  65 */     if (CollUtil.isEmpty(list)) {
/*  66 */       return list;
/*     */     }
/*  68 */     for (MonitorRuleDo ruleDo : list) {
/*  69 */       ruleDo.setMonitorWaringList(selectMonitorWaringList(ruleDo.getMonitorRuleId()));
/*     */     }
/*  71 */     return list;
/*     */   }
/*     */   
/*     */   public MonitorLogDo initMonitorLog(String taskId, String resultNo) {
/*  75 */     MonitorLogDo logDo = new MonitorLogDo();
/*  76 */     logDo.setLogNo(IdWorker.getId());
/*  77 */     logDo.setMonitorTaskId(taskId);
/*  78 */     logDo.setResultNo(resultNo);
/*     */     
/*  80 */     logDo.setDataBatchNo(resultNo.substring(0, 9));
/*  81 */     logDo.setDataDate((Date)DateUtil.parse(resultNo.substring(0, 8), "yyyyMMdd"));
/*  82 */     logDo.setStartTime(DataUtils.nowTimestamp());
/*  83 */     logDo.setTaskStatus(CalculateStatus.CALCULATE.getCode());
/*  84 */     this.monitorLogMapper.insert(logDo);
/*  85 */     return logDo;
/*     */   }
/*     */   
/*     */   public MonitorLogDo endMonitorLog(MonitorLogDo logDo, Timestamp endTime, CalculateStatus calculateStatus, String exceptionInfo) {
/*  89 */     if (endTime == null) {
/*  90 */       endTime = DataUtils.nowTimestamp();
/*     */     }
/*  92 */     if (calculateStatus == null) {
/*  93 */       calculateStatus = CalculateStatus.COMPLETE;
/*     */     }
/*     */     
/*  96 */     logDo.setEndTime(endTime);
/*  97 */     logDo.setCalcTime(DataUtils.timeBetween(logDo.getStartTime(), logDo.getEndTime()));
/*  98 */     logDo.setTaskStatus(calculateStatus.getCode());
/*  99 */     logDo.setExceptionInfo(exceptionInfo);
/* 100 */     this.monitorLogMapper.updateById(logDo);
/* 101 */     return logDo;
/*     */   }
/*     */   
/*     */   public int deleteMonitorLog(String taskId, String resultNo) {
/* 105 */     LambdaQueryWrapper<MonitorLogDo> queryWrapper = new LambdaQueryWrapper();
/* 106 */     ((LambdaQueryWrapper)queryWrapper.eq(MonitorLogDo::getResultNo, resultNo)).eq(MonitorLogDo::getMonitorTaskId, taskId);
/* 107 */     return this.monitorLogMapper.delete((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   public int deleteMonitorRecord(String taskId, String resultNo) {
/* 111 */     LambdaQueryWrapper<MonitorRecordDo> queryWrapper = new LambdaQueryWrapper();
/* 112 */     ((LambdaQueryWrapper)queryWrapper.eq(MonitorRecordDo::getResultNo, resultNo)).eq(MonitorRecordDo::getMonitorTaskId, taskId);
/* 113 */     return this.monitorRecordMapper.delete((Wrapper)queryWrapper);
/*     */   }
/*     */   
/*     */   @Async("myTaskExecutor")
/*     */   public void monitor(String taskId, String resultNo) {
/* 118 */     List<MonitorRuleDo> ruleList = initMonitorRuleList(taskId);
/*     */     
/* 120 */     if (CollUtil.isEmpty(ruleList)) {
/*     */       return;
/*     */     }
/*     */     
/* 124 */     deleteMonitorLog(taskId, resultNo);
/* 125 */     deleteMonitorRecord(taskId, resultNo);
/*     */     
/* 127 */     MonitorLogDo monitorLogDo = initMonitorLog(taskId, resultNo);
/*     */     
/* 129 */     for (MonitorRuleDo ruleDo : ruleList)
/*     */     {
/* 131 */       this.monitorRecordMapper.insert(monitor(ruleDo, monitorLogDo));
/*     */     }
/*     */     
/* 134 */     endMonitorLog(monitorLogDo, null, null, null);
/*     */   }
/*     */   
/*     */   public MonitorRecordDo monitor(MonitorRuleDo ruleDo, MonitorLogDo monitorLogDo) {
/* 138 */     MonitorRecordDo recordDo = initMonitorRecord(ruleDo, monitorLogDo);
/*     */     
/* 140 */     if (!StrUtil.equals(ruleDo.getMonitorTaskType(), "11")) {
/* 141 */       return recordDo;
/*     */     }
/*     */ 
/*     */     
/* 145 */     CreditRwaDo rwaDo = this.resultService.getRwaResult(monitorLogDo.getResultNo());
/* 146 */     Map<MonitorIndex, BigDecimal> rwaMap = convert2CreditRwaResult(rwaDo);
/* 147 */     BigDecimal index = null;
/* 148 */     MonitorIndex monitorIndex = (MonitorIndex)EnumUtils.getEnumByCode(ruleDo.getMonitorIndexCode(), MonitorIndex.class);
/* 149 */     if (StrUtil.isEmpty(ruleDo.getMonitorScopeCode())) {
/* 150 */       index = rwaMap.get(monitorIndex);
/*     */     } else {
/*     */       
/* 153 */       if (StrUtil.isEmpty(ruleDo.getMonitorDim())) {
/* 154 */         throw new ParamConfigException("监控维度为空！ruleDo: " + JsonUtils.object2Json(ruleDo));
/*     */       }
/* 156 */       Map<MonitorIndex, BigDecimal> map = getResultByScope(monitorLogDo.getResultNo(), ruleDo.getMonitorDim(), ruleDo.getMonitorDim());
/* 157 */       switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$MonitorIndex[monitorIndex.ordinal()]) {
/*     */         case 1:
/*     */         case 2:
/*     */         case 3:
/*     */         case 4:
/* 162 */           index = map.get(monitorIndex);
/*     */           break;
/*     */         case 5:
/* 165 */           index = RwaMath.div(map.get(MonitorIndex.RWA), rwaMap.get(MonitorIndex.RWA));
/*     */           break;
/*     */         case 6:
/* 168 */           index = RwaMath.div(map.get(MonitorIndex.EAD), rwaMap.get(MonitorIndex.EAD));
/*     */           break;
/*     */         case 7:
/* 171 */           index = RwaMath.div(map.get(MonitorIndex.AB), rwaMap.get(MonitorIndex.AB));
/*     */           break;
/*     */         case 8:
/* 174 */           throw new ParamConfigException("暂不支持减值准备比例计算");
/*     */       } 
/*     */     } 
/* 177 */     recordDo.setIndexValue(index);
/*     */     
/* 179 */     if (CollUtil.isEmpty(ruleDo.getMonitorWaringList())) {
/* 180 */       throw new ParamConfigException("预警阈值没有配置！");
/*     */     }
/* 182 */     for (MonitorWaringDo waringDo : ruleDo.getMonitorWaringList()) {
/*     */       
/* 184 */       if (isWarning(index, waringDo)) {
/* 185 */         recordDo.setWarningDegree(waringDo.getWarningDegree());
/* 186 */         recordDo.setThresholdTrgWay(waringDo.getThresholdTrgWay());
/* 187 */         recordDo.setThresholdConfig(waringDo.getThresholdConfig());
/* 188 */         recordDo.setRecordTime(DataUtils.nowTimestamp());
/* 189 */         return recordDo;
/*     */       } 
/*     */     } 
/* 192 */     recordDo.setWarningDegree("0");
/* 193 */     recordDo.setRecordTime(DataUtils.nowTimestamp());
/* 194 */     return recordDo;
/*     */   }
/*     */   
/*     */   public MonitorRecordDo initMonitorRecord(MonitorRuleDo ruleDo, MonitorLogDo monitorLogDo) {
/* 198 */     MonitorRecordDo recordDo = new MonitorRecordDo();
/* 199 */     recordDo.setId(IdWorker.getId());
/* 200 */     recordDo.setResultNo(monitorLogDo.getResultNo());
/* 201 */     recordDo.setDataDate(monitorLogDo.getDataDate());
/* 202 */     recordDo.setDataBatchNo(monitorLogDo.getDataBatchNo());
/* 203 */     recordDo.setMonitorTaskId(ruleDo.getMonitorTaskId());
/* 204 */     recordDo.setMonitorTaskType(ruleDo.getMonitorTaskType());
/* 205 */     recordDo.setMonitorRuleId(ruleDo.getMonitorRuleId());
/* 206 */     recordDo.setMonitorRuleName(ruleDo.getMonitorRuleName());
/* 207 */     recordDo.setMonitorRuleType(ruleDo.getMonitorRuleType());
/* 208 */     recordDo.setMonitorIndexCode(ruleDo.getMonitorIndexCode());
/* 209 */     recordDo.setMonitorIndexName(ruleDo.getMonitorIndexName());
/* 210 */     recordDo.setCustomIndex(ruleDo.getCustomIndex());
/* 211 */     recordDo.setMonitorDim(ruleDo.getMonitorDim());
/* 212 */     recordDo.setMonitorScopeCode(ruleDo.getMonitorScopeCode());
/* 213 */     recordDo.setMonitorScopeName(ruleDo.getMonitorScopeName());
/*     */     
/* 215 */     return recordDo;
/*     */   }
/*     */   
/*     */   public Map<MonitorIndex, BigDecimal> convert2CreditRwaResult(CreditRwaDo rwaDo) {
/* 219 */     Map<MonitorIndex, BigDecimal> map = new HashMap<>();
/* 220 */     map.put(MonitorIndex.RWA, rwaDo.getRwa());
/* 221 */     map.put(MonitorIndex.EAD, rwaDo.getExposure());
/* 222 */     map.put(MonitorIndex.AB, rwaDo.getAssetBalance());
/* 223 */     map.put(MonitorIndex.ARW, RwaMath.div(rwaDo.getRwa(), rwaDo.getExposure()));
/* 224 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public Map<MonitorIndex, BigDecimal> getResultByScope(String resultNo, String dim, String scope) {
/* 229 */     String sql = "select sum(m.rwa_ma) as RWA, sum(m.exposure) as EAD, sum(m.asset_balance) as AB from RWA_GR_Credit_Dim m where m.result_no = #{resultNo} ";
/*     */     
/* 231 */     SqlBuilder sqlBuilder = SqlBuilder.create(sql).setString("resultNo", resultNo);
/* 232 */     switch (dim) {
/*     */       
/*     */       case "11":
/* 235 */         sqlBuilder.condition(SqlBuilder.createQueryCondition(null, null, "business_line", null, scope));
/*     */         break;
/*     */       
/*     */       case "12":
/* 239 */         sqlBuilder.condition(SqlBuilder.createQueryCondition(null, null, "asset_type", null, scope));
/*     */         break;
/*     */       
/*     */       case "13":
/* 243 */         sqlBuilder.condition(SqlBuilder.createQueryCondition(null, null, "org_id", null, scope));
/*     */         break;
/*     */     } 
/* 246 */     List<LinkedHashMap<String, Object>> list = this.commonService.select(sqlBuilder.build());
/* 247 */     if (CollUtil.isEmpty(list)) {
/* 248 */       return null;
/*     */     }
/* 250 */     Map<MonitorIndex, BigDecimal> map = new HashMap<>();
/* 251 */     for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)((LinkedHashMap)list.get(0)).entrySet()) {
/* 252 */       map.put(EnumUtils.getEnumByCode(entry.getKey(), MonitorIndex.class), Convert.toBigDecimal(entry.getValue()));
/*     */     }
/* 254 */     map.put(MonitorIndex.ARW, RwaMath.div(map.get(MonitorIndex.RWA), map.get(MonitorIndex.EAD)));
/* 255 */     return map;
/*     */   }
/*     */ 
/*     */   
/*     */   public boolean isWarning(BigDecimal index, MonitorWaringDo waringDo) {
/* 260 */     if (StrUtil.equals(waringDo.getWarningDegree(), "0")) {
/* 261 */       return false;
/*     */     }
/* 263 */     RuleConfigWay ruleConfigWay = (RuleConfigWay)EnumUtils.getEnumByCode(waringDo.getThresholdTrgWay(), RuleConfigWay.class);
/*     */     
/* 265 */     if (ruleConfigWay == RuleConfigWay.RANGE) {
/* 266 */       return RwaMapping.isInRange(index, waringDo.getThresholdConfig(), null);
/*     */     }
/* 268 */     return RwaMapping.numberCompare(ruleConfigWay, index, Convert.toBigDecimal(waringDo.getThresholdConfig()));
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\MonitorService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */