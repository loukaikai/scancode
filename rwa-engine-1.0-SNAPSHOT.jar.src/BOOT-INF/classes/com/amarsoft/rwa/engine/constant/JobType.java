/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum JobType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NR_GROUP("11", "非零售分组作业"),
/* 11 */   RE_GROUP("12", "零售分组作业"),
/*    */   
/* 13 */   NR("21", "一般非零售计量作业"),
/* 14 */   RE("22", "一般零售计量作业"),
/* 15 */   ABS("23", "资产证券化计量作业"),
/* 16 */   AMP("24", "资产管理产品计量作业"),
/* 17 */   DI("25", "衍生工具计量作业"),
/* 18 */   SFT("26", "证券融资交易计量作业"),
/* 19 */   CCP("27", "中央交易对手计量作业"),
/* 20 */   RESULT("29", "结果统计");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   JobType(String code, String name) {
/* 27 */     this.code = code;
/* 28 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 33 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 37 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\JobType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */