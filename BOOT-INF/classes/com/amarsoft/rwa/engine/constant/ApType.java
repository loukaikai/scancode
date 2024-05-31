/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ApType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   AIRB("22", "高级法资产池"),
/* 11 */   FIRB("21", "初级法资产池"),
/* 12 */   WA("11", "权重法资产池"),
/* 13 */   NON("90", "不合格资产池");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ApType(String code, String name) {
/* 20 */     this.code = code;
/* 21 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 26 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 30 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ApType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */