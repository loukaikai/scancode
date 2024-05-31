/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExcDataCode
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CCF_WA("101", "权重法CCF"),
/* 11 */   CCF_FIRB("102", "初级法CCF"),
/* 12 */   RW_EXPOSURE("103", "暴露RW"),
/* 13 */   LGD_EXPOSURE("104", "暴露LGD"),
/* 14 */   TM_FIRB("105", "暴露最低持有期"),
/* 15 */   M_FIRB("106", "暴露有效期限"),
/* 16 */   EXPOSURE_TYPE("107", "暴露类型"),
/* 17 */   SIB_EXPOSURE("108", "暴露参与主体是否系统重要性银行标识"),
/* 18 */   RW_AMP("109", "资管RW"),
/* 19 */   RW_RETAIL("110", "零售RW"),
/* 20 */   HE("111", "风险暴露折扣系数"),
/* 21 */   RW_SL("112", "专业贷款RW"),
/*    */   
/* 23 */   RW_GUARANTEE("201", "保证权重"),
/* 24 */   PD_GUARANTEE("202", "保证PD"),
/* 25 */   GUARANTOR_EXPO_TYPE("203", "保证人暴露类型"),
/* 26 */   LGD_GUARANTEE("204", "保证LGD"),
/* 27 */   SIB_GUARANTOR("205", "保证人是否系统重要性银行标识"),
/* 28 */   AMT_GUARANTEE("211", "保证总额"),
/*    */   
/* 30 */   RW_COLLATERAL("301", "押品RW"),
/* 31 */   LGD_COLLATERAL("302", "押品LGD"),
/* 32 */   HC("303", "押品折扣系数"),
/* 33 */   AMT_COLLATERAL("311", "押品总额"),
/*    */   
/* 35 */   RW_ABS("401", "资产证券化RW"),
/* 36 */   CCF_ABS("402", "资产证券化CCF"),
/* 37 */   ABS_SECU_CODE("403", "资产证券化证券代码为空"),
/*    */   
/* 39 */   RW_CVA("501", "CVA交易对手RW"),
/* 40 */   F_SFT("502", "证券融资交易折扣系数底线");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ExcDataCode(String code, String name) {
/* 47 */     this.code = code;
/* 48 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 53 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 57 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ExcDataCode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */