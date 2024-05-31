/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum QualCcp
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   NON_CCP("1", "非中央交易对手"),
/* 11 */   QUAL_CCP("2", "合格中央交易对手"),
/* 12 */   NQ_CCP("3", "非合格中央交易对手");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   QualCcp(String code, String name) {
/* 19 */     this.code = code;
/* 20 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 25 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 29 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\QualCcp.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */