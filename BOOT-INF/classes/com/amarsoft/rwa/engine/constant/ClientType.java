/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ClientType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   SOV_SOV("1111", "我国主权政府"),
/* 11 */   SOV_CB("1112", "我国中央银行"),
/* 12 */   SOV_POLICY("1113", "我国开发性金融机构和政策性银行"),
/* 13 */   SOV_MOF("1114", "我国财政部"),
/* 14 */   FSOV_SOV("1211", "境外主权政府"),
/* 15 */   FSOV_CB("1212", "境外中央银行"),
/* 16 */   FSOV_IFI("1213", "国际金融组织类机构"),
/* 17 */   PSE_SOV_PL("2111", "省级及计划单列市人民政府"),
/* 18 */   PSE_SOV_RFCT("2112", "收入主要源于中央财政的公共部门实体"),
/* 19 */   PSE_SOV_OTHER("2113", "我国其他视同我国主权的公共部门实体"),
/* 20 */   PSE_FSOV("2211", "境外视同主权的公共部门实体"),
/* 21 */   BANK("3121", "我国商业银行"),
/* 22 */   NBF_AMC("3122", "我国中央政府投资的金融资产管理公司"),
/* 23 */   NBF_CCP("3123", "我国中央交易对手"),
/* 24 */   NBF_OTHER("3129", "我国其他非银行金融机构"),
/* 25 */   BANK_OS("3221", "境外商业银行"),
/* 26 */   NBF_OS("3229", "境外非银行金融机构");
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String code;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   private String name;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   ClientType(String code, String name) {
/* 57 */     this.code = code;
/* 58 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 63 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 67 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ClientType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */