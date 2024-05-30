/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum NullSortType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   FIX_TOP("1", "固定null小于任何对象，排在前面"),
/* 11 */   FIX_BOTTOM("2", "固定null大于任何对象，排在后面"),
/* 12 */   FOLLOW_TOP("3", "顺序下null小于任何对象，排在前面，倒序反之"),
/* 13 */   FOLLOW_BOTTOM("4", "顺序下null大于任何对象，排在后面，倒序反之");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   NullSortType(String code, String name) {
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


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\NullSortType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */