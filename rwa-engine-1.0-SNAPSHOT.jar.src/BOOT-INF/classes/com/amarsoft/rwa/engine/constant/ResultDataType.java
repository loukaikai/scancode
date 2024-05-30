/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ResultDataType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   DETAIL("detailResultList", "缓释明细结果"),
/* 11 */   EXPOSURE("exposureResultList", "暴露结果"),
/* 12 */   MITIGATION("mitigationResultList", "缓释物结果"),
/* 13 */   AMP("ampResultList", "资管风险暴露结果"),
/*    */   
/* 15 */   ABS_EXPOSURE("exposureList", "ABS暴露结果"),
/* 16 */   ABS_PRODUCT("productList", "ABS产品结果"),
/* 17 */   ABS_MITIGATION("mitigationList", "ABS缓释物结果"),
/* 18 */   ABS_DETAIL("detailList", "ABS缓释明细结果"),
/*    */   
/* 20 */   DI_EXPOSURE("exposureList", "衍生工具暴露结果"),
/* 21 */   DI_NETTING("nettingList", "衍生工具净额结算结果"),
/* 22 */   DI_INTERMEDIATE("intermediateList", "衍生工具中间结果"),
/* 23 */   DI_COLLATERAL("collateralList", "衍生工具抵质押品结果"),
/*    */   
/* 25 */   SFT_NETTING("nettingList", "回购交易净额结算结果"),
/* 26 */   SFT_EXPOSURE("exposureList", "证券融资交易暴露结果"),
/* 27 */   SFT_COLLATERAL("collateralList", "证券融资交易抵质押品结果");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ResultDataType(String code, String name) {
/* 34 */     this.code = code;
/* 35 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 40 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 44 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ResultDataType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */