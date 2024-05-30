/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum IrbUncoveredProcess
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   CALCULATE("1", "重新计算"),
/* 11 */   SKIP("0", "跳过");
/*    */   
/*    */   private String code;
/*    */   private String name;
/*    */   
/*    */   IrbUncoveredProcess(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\IrbUncoveredProcess.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */