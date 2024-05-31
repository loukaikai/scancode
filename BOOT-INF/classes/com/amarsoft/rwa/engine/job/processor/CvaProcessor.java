/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*    */ 
/*    */ import cn.hutool.core.util.NumberUtil;
/*    */ import cn.hutool.extra.spring.SpringUtil;
/*    */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*    */ import com.amarsoft.rwa.engine.constant.CvaType;
/*    */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*    */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*    */ import com.amarsoft.rwa.engine.job.processor.RwaProcessor;
/*    */ import com.amarsoft.rwa.engine.service.JobService;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import com.amarsoft.rwa.engine.util.RwaMapping;
/*    */ import com.amarsoft.rwa.engine.util.RwaMath;
/*    */ import com.amarsoft.rwa.engine.util.RwaUtils;
/*    */ import java.math.BigDecimal;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.validation.constraints.NotNull;
/*    */ 
/*    */ public class CvaProcessor extends RwaProcessor {
/*    */   private CvaType cvaType;
/*    */   
/*    */   public CvaType getCvaType() {
/* 28 */     return this.cvaType; } public void setCvaType(CvaType cvaType) {
/* 29 */     this.cvaType = cvaType;
/*    */   }
/*    */   
/*    */   public CvaProcessor(JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess, @NotNull CvaType cvaType) {
/* 33 */     super(jobInfo, irbUncoveredProcess);
/* 34 */     this.cvaType = cvaType;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void init(Map<String, Object> item) throws Exception {}
/*    */ 
/*    */   
/*    */   ExposureApproach confirmExposureApproach(Map<String, Object> item) throws Exception {
/* 43 */     return ExposureApproach.WA;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   void paramMapping(Map<String, Object> item) throws Exception {}
/*    */ 
/*    */ 
/*    */   
/*    */   Map<String, Object> calculateResult(Map<String, Object> item) throws Exception {
/* 53 */     List<Map<String, Object>> cpList = (List<Map<String, Object>>)item.get(InterfaceDataType.CVA.getCode());
/* 54 */     BigDecimal scva1 = BigDecimal.ZERO;
/* 55 */     BigDecimal scva2 = BigDecimal.ZERO;
/* 56 */     for (Map<String, Object> cp : cpList) {
/* 57 */       String id = DataUtils.getString(cp, (ICodeEnum)RwaParam.CLIENT_ID);
/*    */       
/* 59 */       DataUtils.setString(cp, (ICodeEnum)RwaParam.ORG_ID, RwaConfig.headOrgId);
/*    */       
/* 61 */       RwaMapping.mappingCvaRw(getSchemeConfig(), getJobId(), InterfaceDataType.CVA, cp, id);
/* 62 */       BigDecimal scva = null;
/* 63 */       if (RwaUtils.isB3(getTaskType())) {
/*    */         
/* 65 */         scva = RwaMath.getScva(Double.valueOf(1.4D), DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.CP_RW), DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.AS_EAD));
/*    */       }
/*    */       else {
/*    */         
/* 69 */         BigDecimal m = RwaMath.div(DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.MWP), DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.NOTIONAL_PRINCIPAL));
/*    */         
/* 71 */         BigDecimal df = RwaMath.getDiscountFactor(m, Integer.valueOf(10));
/*    */         
/* 73 */         m = RwaMath.div(m, Integer.valueOf(250));
/*    */         
/* 75 */         BigDecimal asEad = RwaMath.mul(new Number[] { DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.EAD), df, m });
/*    */         
/* 77 */         scva = RwaMath.getScva(Integer.valueOf(1), DataUtils.getBigDecimal(cp, (ICodeEnum)RwaParam.CP_RW), asEad);
/* 78 */         DataUtils.setBigDecimal(cp, (ICodeEnum)RwaParam.CP_MATURITY, m);
/* 79 */         DataUtils.setBigDecimal(cp, (ICodeEnum)RwaParam.DISCOUNT_FACTOR, df);
/* 80 */         DataUtils.setBigDecimal(cp, (ICodeEnum)RwaParam.AS_EAD, asEad);
/*    */       } 
/* 82 */       DataUtils.setBigDecimal(cp, (ICodeEnum)RwaParam.SCVA, scva);
/* 83 */       scva1 = RwaMath.add(scva1, scva);
/* 84 */       scva2 = RwaMath.add(scva2, NumberUtil.pow(scva, 2));
/*    */     } 
/*    */     
/* 87 */     BigDecimal cva = null;
/* 88 */     if (RwaUtils.isB3(getTaskType())) {
/* 89 */       cva = RwaMath.getCva(Double.valueOf(0.65D), BigDecimal.valueOf(0.5D), scva1, scva2);
/*    */     } else {
/* 91 */       cva = RwaMath.getCva(Double.valueOf(2.33D), BigDecimal.valueOf(0.5D), scva1, scva2);
/*    */     } 
/*    */     
/* 94 */     ((JobService)SpringUtil.getBean(JobService.class)).putCva(getJobInfo().getResultNo(), getCvaType(), cva);
/* 95 */     return item;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\CvaProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */