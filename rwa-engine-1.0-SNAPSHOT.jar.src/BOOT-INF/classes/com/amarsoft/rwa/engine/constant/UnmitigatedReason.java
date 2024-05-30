/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum UnmitigatedReason
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   WA_OSN_OFF("101", "权重法表内净额结算不能缓释表外业务"),
/* 11 */   WA_RW("102", "缓释物风险权重>=暴露风险权重"),
/* 12 */   WA_MM_LESS("103", "缓释物剩余期限<暴露剩余期限"),
/* 13 */   WA_MM_ZERO("104", "缓释物剩余期限为0"),
/* 14 */   WA_MM_NULL("105", "缓释物剩余期限为空"),
/* 15 */   WA_GM_NULL("106", "缓释物担保剩余期限为空"),
/* 16 */   WA_GM_ZERO("107", "缓释物担保剩余期限为0"),
/* 17 */   WA_CDG("111", "信用衍生保证缓释后RWA超过缓释前RWA"),
/*    */   
/* 19 */   IRB_OSN_OFF("201", "内评法表内净额结算不能缓释表外业务"),
/* 20 */   IRB_K("202", "保证当前K>=暴露K"),
/* 21 */   IRB_SUBPRIME("203", "次级债权不认可抵质押品的缓释作用"),
/* 22 */   IRB_MM_OM("204", "存在期限错配且缓释物原始期限不足1年"),
/* 23 */   IRB_MM_RM("205", "存在期限错配且缓释物剩余期限不足3个月"),
/* 24 */   IRB_MM_ZERO("206", "缓释物剩余期限为0"),
/* 25 */   IRB_OVER_MITIGATE("207", "缓释金额已超过暴露金额"),
/* 26 */   IRB_PD("208", "保证PD>=暴露PD"),
/* 27 */   IRB_MM_NULL("209", "缓释物剩余期限为空"),
/* 28 */   IRB_HAIRCUT_ZERO("210", "折扣系数调整后缓释金额为0"),
/* 29 */   IRB_CDG("211", "信用衍生保证缓释后RWA超过缓释前RWA"),
/* 30 */   IRB_MIN_CL("221", "不满足最低抵质押水平");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   UnmitigatedReason(String code, String name) {
/* 37 */     this.code = code;
/* 38 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 43 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 47 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\UnmitigatedReason.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */