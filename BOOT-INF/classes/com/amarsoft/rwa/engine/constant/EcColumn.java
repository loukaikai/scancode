/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum EcColumn
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NRE("nre", "一般非零售风险暴露"),
/* 11 */   REE("ree", "一般零售风险暴露"),
/* 12 */   ABSE("abse", "资产证券化风险暴露"),
/* 13 */   AMPE("ampe", "资产管理产品风险暴露"),
/* 14 */   DIN("din", "衍生工具净额结算"),
/* 15 */   DIE("die", "衍生工具风险暴露"),
/* 16 */   SFTN("sftn", "证券融资交易净额结算"),
/* 17 */   SFTE("sfte", "证券融资交易风险暴露"),
/* 18 */   DFE("dfe", "中央交易对手风险暴露");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   EcColumn(String code, String name) {
/* 25 */     this.code = code;
/* 26 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 31 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\EcColumn.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */