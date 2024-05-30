/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ParamTemplate
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CCF("1101", "1", "信用风险转换系数"),
/* 11 */   RW_NR("1102", "1", "非零售暴露风险权重"),
/* 12 */   RW_RETAIL("1103", "1", "零售暴露风险权重"),
/* 13 */   RW_DEFAULT("1104", "1", "已违约暴露风险权重"),
/* 14 */   RW_ABA("1105", "1", "授权基础法权重映射参数表"),
/* 15 */   RW_COLLATERAL("1106", "1", "抵质押品风险权重"),
/* 16 */   RW_GUARANTEE("1107", "1", "保证风险权重"),
/* 17 */   RW_CP("1108", "1", "交易对手风险权重"),
/*    */   
/* 19 */   RW_REE("1109", "1", "房地产暴露风险权重"),
/*    */   
/* 21 */   RW_DFT("1110", "1", "暴露默认风险权重"),
/*    */   
/* 23 */   CCF_ABS("1201", "1", "资产证券化CCF"),
/* 24 */   RW_ABS("1202", "1", "资产证券化RW"),
/* 25 */   B2_CCF_ABS_SA("1203", "1", "资产证券化标准法CCF"),
/* 26 */   B2_RW_ABS_SA("1204", "1", "资产证券化标准法RW"),
/*    */   
/* 28 */   ADDON_DI("1301", "1", "衍生工具附加系数"),
/* 29 */   SP_DI("1302", "1", "衍生工具监管参数"),
/* 30 */   HE_DI("1303", "1", "衍生工具客户风险暴露折扣系数"),
/* 31 */   RW_CVA("1304", "1", "信用估值调整风险权重"),
/* 32 */   FL_SFT("2301", "2", "证券融资交易折扣系数底线"),
/*    */   
/* 34 */   CCF_FIRB("2101", "2", "初级法信用风险转换系数"),
/* 35 */   LGD_EXPOSURE("2102", "2", "风险暴露LGD"),
/* 36 */   TM_EXPOSURE("2103", "2", "风险暴露最低持有期"),
/* 37 */   M_EXPOSURE("2104", "2", "风险暴露M"),
/* 38 */   LGD_COLLATERAL("2105", "2", "抵质押品LGD"),
/* 39 */   SH("2106", "2", "金融工具标准折扣系数"),
/* 40 */   RW_SL("2107", "2", "专业贷款监管映射法风险权重"),
/*    */   
/* 42 */   FORMULA_FIRB("2111", "2", "初级法计算公式"),
/* 43 */   FORMULA_AIRB("2112", "2", "高级法计算公式"),
/* 44 */   FORMULA_RIRB("2113", "2", "零售内评法计算公式"),
/* 45 */   PD_LIMIT_NR("2114", "2", "非零售违约概率下限"),
/* 46 */   PD_LIMIT_RE("2115", "2", "零售违约概率下限"),
/* 47 */   LGD_LIMIT_NR("2116", "2", "非零售违约损失率下限"),
/* 48 */   LGD_LIMIT_RE("2117", "2", "零售违约损失率下限"),
/*    */   
/* 50 */   SF_ABS("2201", "2", "资产证券化内部评级法监管因子"),
/* 51 */   B2_RW_ABS_RBA("2202", "2", "资产证券化评级基础法RW");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String approach;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ParamTemplate(String code, String approach, String name) {
/* 60 */     this.code = code;
/* 61 */     this.approach = approach;
/* 62 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 67 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getApproach() {
/* 71 */     return this.approach;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 75 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ParamTemplate.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */