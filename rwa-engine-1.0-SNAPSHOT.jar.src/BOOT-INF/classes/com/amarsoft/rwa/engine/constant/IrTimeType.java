/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum IrTimeType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   L1("1", "少于1年"),
/* 11 */   B15("2", "1年至5年"),
/* 12 */   G5("3", "大于5年");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   IrTimeType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\IrTimeType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */