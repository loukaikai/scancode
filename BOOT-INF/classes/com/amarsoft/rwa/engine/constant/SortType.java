/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum SortType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   ASC("1", "顺序"),
/* 11 */   DESC("2", "倒序");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   SortType(String code, String name) {
/* 17 */     this.code = code;
/* 18 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 23 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 27 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\SortType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */