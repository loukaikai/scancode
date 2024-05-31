/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum AmpApproach
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   PENETRATION("10", "穿透法"),
/* 11 */   BASIS("20", "授权基础法"),
/* 12 */   OTHER("30", "1250%");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   AmpApproach(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\AmpApproach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */