/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.AmpApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureBelong;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*     */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.entity.EcFactorDo;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*     */ import com.amarsoft.rwa.engine.entity.ParamVersionDo;
/*     */ import com.amarsoft.rwa.engine.entity.TaskConfigDo;
/*     */ import com.amarsoft.rwa.engine.entity.UnionDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AmpProcessor extends RwaProcessor {
/*     */   private List<Map<String, Object>> exposureList;
/*     */   
/*     */   public AmpProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess) {
/*  31 */     super(jobInfo, irbUncoveredProcess);
/*     */   }
/*     */   private UnionDto unionDto; private Map<String, List<Map<String, Object>>> ampAbaInfoMap;
/*     */   
/*     */   void init(Map<String, Object> item) throws Exception {
/*  36 */     this.exposureList = (List<Map<String, Object>>)item.get(InterfaceDataType.EXPOSURE.getCode());
/*  37 */     this.unionDto = new UnionDto();
/*  38 */     this.unionDto.setId(DataUtils.getString(item, (ICodeEnum)RwaParam.ID));
/*  39 */     this.unionDto.setSize(DataUtils.getCollSize(new Collection[] { this.exposureList }));
/*  40 */     this.unionDto.setSchemeConfig(getSchemeConfig());
/*  41 */     this.unionDto.setUnionType(UnionType.EXPOSURE);
/*  42 */     this.unionDto.setTaskType(getTaskType());
/*     */     
/*  44 */     this.ampAbaInfoMap = getAmpAbaInfoMap(item);
/*     */   }
/*     */   
/*     */   public Map<String, List<Map<String, Object>>> getAmpAbaInfoMap(Map<String, Object> item) {
/*  48 */     List<Map<String, Object>> list = (List<Map<String, Object>>)item.get(InterfaceDataType.AMP_INFO.getCode());
/*  49 */     Map<String, List<Map<String, Object>>> map = new HashMap<>();
/*  50 */     if (CollUtil.isEmpty(list)) {
/*  51 */       return map;
/*     */     }
/*  53 */     for (Map<String, Object> info : list) {
/*  54 */       String exposureId = DataUtils.getString(info, (ICodeEnum)RwaParam.EXPOSURE_ID);
/*  55 */       List<Map<String, Object>> tempList = map.get(exposureId);
/*  56 */       if (CollUtil.isEmpty(tempList)) {
/*  57 */         tempList = new ArrayList<>();
/*  58 */         tempList.add(info);
/*  59 */         map.put(exposureId, tempList); continue;
/*     */       } 
/*  61 */       tempList.add(info);
/*     */     } 
/*     */     
/*  64 */     return map;
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/*  70 */     return ExposureApproach.WA;
/*     */   }
/*     */ 
/*     */   
/*     */   void paramMapping(Map<String, Object> item) throws Exception {
/*  75 */     this.unionDto.setApproach(getConfirmApproach());
/*  76 */     for (Map<String, Object> exposure : this.exposureList) {
/*  77 */       paramMappingExposure(InterfaceDataType.EXPOSURE, exposure);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/*  84 */     RwaUtils.convert2Exposure(item, this.unionDto, false);
/*  85 */     RwaUtils.convert2Mitigation(item, this.unionDto);
/*  86 */     WaCalculation.createCalculation(this.unionDto).execute();
/*     */     
/*  88 */     for (ExposureDto exposureDto : this.unionDto.getExposureResultList()) {
/*     */       
/*  90 */       if (getSchemeConfig().getWaParamVersion().getCreditRule().isEnAmpCalc() && 
/*  91 */         StrUtil.equals(exposureDto.getAmpFlag(), Identity.YES.getCode())) {
/*  92 */         this.unionDto.getAmpResultList().add(exposureDto);
/*  93 */         if (StrUtil.equals(exposureDto.getIsTpCalc(), Identity.YES.getCode())) {
/*     */ 
/*     */ 
/*     */           
/*  97 */           exposureDto.setRw(NumberUtil.mul(exposureDto.getRw(), Double.valueOf(1.2D)));
/*  98 */           exposureDto.setWarw(NumberUtil.mul(exposureDto.getWarw(), Double.valueOf(1.2D)));
/*  99 */           exposureDto.setRwaMb(NumberUtil.mul(exposureDto.getRwaMb(), Double.valueOf(1.2D)));
/* 100 */           exposureDto.setRwaMa(NumberUtil.mul(exposureDto.getRwaMa(), Double.valueOf(1.2D)));
/* 101 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/* 102 */         } else if (!StrUtil.equals(exposureDto.getAmpApproach(), AmpApproach.OTHER.getCode()) && 
/* 103 */           NumberUtil.isGreater(exposureDto.getRwaMa(), RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw))) {
/*     */           
/* 105 */           exposureDto.setRwaAa(RwaMath.sub(RwaMath.mul(exposureDto.getNetAsset(), RwaMath.maxRw), exposureDto.getRwaMa()));
/*     */         } else {
/*     */           
/* 108 */           exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */         } 
/*     */       } else {
/* 111 */         exposureDto.setRwaAa(BigDecimal.ZERO);
/*     */       } 
/* 113 */       exposureDto.setRwaAdj(RwaMath.add(exposureDto.getRwaMa(), exposureDto.getRwaAa()));
/*     */     } 
/*     */     
/* 116 */     TaskConfigDo taskConfigDo = getJobInfo().getTaskConfigDo();
/* 117 */     if (taskConfigDo.isEnEcCalc()) {
/* 118 */       for (Map<String, Object> exposure : this.exposureList) {
/* 119 */         ExposureDto expo = (ExposureDto)this.unionDto.getExposureMap().get(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID));
/* 120 */         if (expo == null) {
/*     */           continue;
/*     */         }
/* 123 */         List<EcFactorDo> mappingEcFactor = RwaUtils.mappingEcFactor(exposure);
/* 124 */         BigDecimal df = RwaMath.getEcDf(mappingEcFactor);
/* 125 */         expo.setEcParamInfo(RwaUtils.getEcParamInfo(mappingEcFactor));
/* 126 */         expo.setEcDf(df);
/* 127 */         expo.setEc(RwaMath.getEc(expo.getRwaAdj(), df, taskConfigDo.getEcOf()));
/*     */       } 
/*     */     }
/* 130 */     return RwaUtils.beanToMap(this.unionDto);
/*     */   }
/*     */   
/*     */   public void paramMappingExposure(InterfaceDataType dataType, Map<String, Object> exposure) {
/* 134 */     String id = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_ID);
/* 135 */     DataUtils.setString(exposure, (ICodeEnum)RwaParam.APPROACH, getConfirmApproach().getCode());
/* 136 */     ParamVersionDo paramVersionDo = getSchemeConfig().getWaParamVersion();
/*     */     
/* 138 */     String exposureTypeWa = DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_TYPE_WA);
/* 139 */     String ampApproach = DataUtils.getString(exposure, (ICodeEnum)RwaParam.AMP_APPROACH);
/*     */     
/* 141 */     ExposureBelong exposureBelong = (ExposureBelong)EnumUtils.getEnumByCode(DataUtils.getString(exposure, (ICodeEnum)RwaParam.EXPOSURE_BELONG), ExposureBelong.class);
/* 142 */     BigDecimal ccf = null;
/* 143 */     if (exposureBelong == ExposureBelong.OFF) {
/* 144 */       ccf = paramVersionDo.getCreditRule().getAmpOffCcf();
/* 145 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.CCF, ccf);
/* 146 */       DataUtils.setString(exposure, (ICodeEnum)RwaParam.OFF_RPT_ITEM_WA, paramVersionDo.getCreditRule().getAmpOffRptItem());
/*     */     } 
/*     */ 
/*     */     
/* 150 */     BigDecimal balance = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE);
/*     */     
/* 152 */     if (balance == null) {
/* 153 */       balance = BigDecimal.ZERO;
/* 154 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/*     */ 
/*     */     
/* 158 */     BigDecimal lr = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR);
/* 159 */     if (RwaMath.isNullOrNegative(lr) || RwaMath.isZero(lr) || 
/* 160 */       StrUtil.equals(exposureTypeWa, SpecExposureTypeWa.AMP_ADV.getCode())) {
/*     */       
/* 162 */       lr = BigDecimal.ONE;
/* 163 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.AMP_LR, lr);
/*     */     } 
/* 165 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.NET_ASSET, balance);
/* 166 */     if (!StrUtil.equals(AmpApproach.OTHER.getCode(), ampApproach)) {
/* 167 */       balance = RwaMath.mul(balance, lr);
/* 168 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.ASSET_BALANCE, balance);
/*     */     } 
/*     */     
/* 171 */     BigDecimal provision = DataUtils.getBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION);
/*     */     
/* 173 */     if (RwaMath.isNullOrNegative(provision)) {
/* 174 */       provision = BigDecimal.ZERO;
/* 175 */       DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION, BigDecimal.ZERO);
/*     */     } 
/* 177 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_PROP, RwaMath.getProvisionProp(balance, ccf, provision));
/* 178 */     DataUtils.setBigDecimal(exposure, (ICodeEnum)RwaParam.PROVISION_DED, RwaMath.getProvisionDed(balance, ccf, provision));
/*     */     
/* 180 */     BigDecimal rw = null;
/*     */     
/* 182 */     if (StrUtil.equals(AmpApproach.BASIS.getCode(), ampApproach)) {
/*     */       
/* 184 */       rw = RwaMapping.mappingExposureAmpAbaRw(getSchemeConfig(), getJobId(), dataType, exposure, this.ampAbaInfoMap.get(id), id);
/*     */     } else {
/*     */       
/* 187 */       rw = RwaMapping.mappingExposureAmpOther1250Rw(getSchemeConfig(), getJobId(), dataType, exposure, id);
/*     */     } 
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\AmpProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */