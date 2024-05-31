/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RuleConfigWay
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   EQUAL("1", "等于"),
/* 11 */   UNEQUAL("2", "不等于"),
/* 12 */   GREAT("3", "大于"),
/* 13 */   GREAT_OR_EQUAL("4", "大于等于"),
/* 14 */   LESS("5", "小于"),
/* 15 */   LESS_OR_EQUAL("6", "小于等于"),
/* 16 */   RANGE("7", "范围"),
/* 17 */   CUSTOM("9", "自定义");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   RuleConfigWay(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\RuleConfigWay.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */