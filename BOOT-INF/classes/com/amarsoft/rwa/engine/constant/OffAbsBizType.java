/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum OffAbsBizType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   LF("1", "流动性便利"),
/* 11 */   CAF("2", "现金透支便利"),
/* 12 */   OTHER("9", "其他表外资产证券化");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   OffAbsBizType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\OffAbsBizType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */