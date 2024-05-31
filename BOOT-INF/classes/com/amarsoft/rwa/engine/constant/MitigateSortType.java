/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum MitigateSortType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   EXPOSURE("10", "暴露"),
/* 11 */   MITIGATION("21", "缓释工具"),
/* 12 */   MITIGATION_TYPE("29", "缓释工具类型");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   MitigateSortType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\MitigateSortType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */