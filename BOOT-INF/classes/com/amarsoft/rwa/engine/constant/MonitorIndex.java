/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum MonitorIndex
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   RWA("101", "风险加权资产"),
/* 11 */   EAD("102", "风险暴露"),
/* 12 */   AB("103", "资产余额"),
/* 13 */   ARW("104", "平均风险权重"),
/* 14 */   RWA_RATIO("111", "风险加权资产占比"),
/* 15 */   EAD_RATIO("112", "风险暴露占比"),
/* 16 */   AB_RATIO("113", "资产余额占比"),
/* 17 */   PROV_RATIO("114", "减值准备占比");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   MonitorIndex(String code, String name) {
/* 24 */     this.code = code;
/* 25 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 30 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 34 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\MonitorIndex.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */