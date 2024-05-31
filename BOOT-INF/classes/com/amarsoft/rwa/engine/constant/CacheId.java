/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum CacheId
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   WORKER_ID("workerId", "ID工具"),
/* 11 */   GROUP_TASK("groupTask", "分组任务"),
/* 12 */   STOP_GROUP("stopGroup", "停止分组任务"),
/* 13 */   STOP_RWA("stopRwa", "停止RWA任务"),
/* 14 */   GROUP_JOB("groupJob", "分组作业"),
/* 15 */   RWA_JOB("rwaJob", "RWA计算作业"),
/* 16 */   RESULT_JOB("resultJob", "结果统计作业"),
/* 17 */   CVA("cva", "cva结果缓存"),
/*    */   
/* 19 */   RUNNING_MSG("runningMsg", "消费中的消息"),
/*    */   
/* 21 */   DIST_JOB("distJob", "当前分发数"),
/* 22 */   TASK_LIST("taskList", "任务列表"),
/* 23 */   TASK_STATUS("taskStatus", "任务状态"),
/*    */   
/* 25 */   SCHEME("scheme", "计算方案"),
/* 26 */   HOLIDAY("holiday", "假日表"),
/* 27 */   EC_FACTOR("ecFactor", "经济资本系数"),
/* 28 */   EC_COLUMN("ecColumn", "经济资本字段配置");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   CacheId(String code, String name) {
/* 35 */     this.code = code;
/* 36 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 41 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 45 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\CacheId.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */