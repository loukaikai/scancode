/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExposureSubtype
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   BANKS("2010", "银行类金融机构"),
/*    */   
/* 12 */   HM("4010", "个人住房抵押贷款"),
/* 13 */   TQCR("4021", "交易者合格循环零售"),
/* 14 */   GQCR("4022", "一般合格循环零售"),
/* 15 */   SME("4031", "零售小微企业"),
/* 16 */   QPRAR("4032", "合格购入零售应收账款"),
/* 17 */   OTHER_RE("4033", "其他零售"),
/*    */   
/* 19 */   REL("3044", "产生收入的房地产贷款");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ExposureSubtype(String code, String name) {
/* 26 */     this.code = code;
/* 27 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 32 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ExposureSubtype.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */