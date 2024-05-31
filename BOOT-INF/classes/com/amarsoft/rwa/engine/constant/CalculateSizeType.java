/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CalculateSizeType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   UNION("UNION", "联通体"),
/* 11 */   EXPOSURE("EXPOSURE", "暴露"),
/* 12 */   RELEVANCE("RELEVANCE", "关联关系");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   CalculateSizeType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\CalculateSizeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */