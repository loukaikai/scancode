/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RepoDirection
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   BUY("1", "正回购"),
/* 11 */   SELL("2", "逆回购");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   RepoDirection(String code, String name) {
/* 18 */     this.code = code;
/* 19 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 24 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 28 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\RepoDirection.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */