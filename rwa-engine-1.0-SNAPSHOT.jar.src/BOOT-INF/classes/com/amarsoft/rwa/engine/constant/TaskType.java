/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum TaskType
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   GROUP("10", "数据分组"),
/* 11 */   RWA("31", "巴三RWA计算"),
/* 12 */   STRESS("32", "巴三压力测试"),
/* 13 */   SINGLE("33", "巴三单笔计算"),
/* 14 */   IMTASK("34", "巴三即时任务"),
/* 15 */   RWA2("21", "巴二RWA计算"),
/* 16 */   STRESS2("22", "巴二压力测试"),
/* 17 */   SINGLE2("23", "巴二单笔计算"),
/* 18 */   IMTASK2("24", "巴二即时任务");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   TaskType(String code, String name) {
/* 25 */     this.code = code;
/* 26 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 31 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 35 */     return this.name;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\TaskType.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */