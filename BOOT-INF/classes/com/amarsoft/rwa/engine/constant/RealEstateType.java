/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum RealEstateType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   RESIDENTIAL("1", "居住用"),
/* 11 */   COMMERCIAL("2", "商用");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   RealEstateType(String code, String name) {
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


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\RealEstateType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */