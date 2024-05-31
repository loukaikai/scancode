/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum DiEadApproach
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CEM("1", "现期暴露法"),
/* 11 */   SA("2", "标准法");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   DiEadApproach(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\DiEadApproach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */