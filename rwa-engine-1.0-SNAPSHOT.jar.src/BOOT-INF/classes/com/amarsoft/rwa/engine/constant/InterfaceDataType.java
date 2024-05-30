/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum InterfaceDataType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   EXPOSURE("exposure", "暴露信息"),
/* 11 */   AMP_INFO("amp_info", "资管信息"),
/* 12 */   RELEVANCE("relevance", "暴露与缓释物关联关系"),
/* 13 */   COLLATERAL("collateral", "抵质押品"),
/* 14 */   GUARANTEE("guarantee", "保证"),
/*    */   
/* 16 */   ABS_EXPOSURE("abs_exposure", "资产证券化暴露"),
/* 17 */   ABS_PRODUCT("abs_product", "资产证券化产品"),
/* 18 */   ABS_RELEVANCE("abs_relevance", "暴露与缓释物关联关系"),
/* 19 */   ABS_GUARANTEE("abs_guarantee", "保证"),
/*    */   
/* 21 */   DI_NETTING("di_netting", "衍生工具净额结算"),
/* 22 */   DI_EXPOSURE("di_exposure", "衍生工具暴露"),
/* 23 */   DI_COLLATERAL("di_collateral", "衍生工具抵质押品"),
/*    */   
/* 25 */   SFT_NETTING("sft_netting", "回购交易净额结算"),
/* 26 */   SFT_EXPOSURE("sft_exposure", "证券融资交易暴露"),
/* 27 */   SFT_COLLATERAL("sft_collateral", "证券融资交易抵质押品"),
/*    */   
/* 29 */   CCP_DF("ccp_df", "中央交易对手违约基金"),
/*    */   
/* 31 */   CVA("cva", "CVA结果");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   InterfaceDataType(String code, String name) {
/* 38 */     this.code = code;
/* 39 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 44 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 48 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\InterfaceDataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */