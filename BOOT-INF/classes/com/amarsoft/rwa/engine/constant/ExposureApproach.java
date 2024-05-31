/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.constant;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ 
/*    */ 
/*    */ 
/*    */ public enum ExposureApproach
/*    */   implements ICodeEnum
/*    */ {
/* 10 */   WA("101", "权重法"),
/* 11 */   ABSERA("102", "资产证券化外部评级法"),
/* 12 */   ABSSA("103", "资产证券化标准法"),
/* 13 */   ABS1250("104", "资产证券化1250%"),
/* 14 */   FIRB("201", "初级内评法"),
/* 15 */   AIRB("202", "高级内评法"),
/* 16 */   RIRB("203", "零售内评法"),
/* 17 */   SMA("204", "监管映射法"),
/* 18 */   ABSRBA("205", "资产证券化评级基础法"),
/* 19 */   ABSIRB("206", "资产证券化内部评级法");
/*    */   
/*    */   private String code;
/*    */   
/*    */   private String name;
/*    */   
/*    */   ExposureApproach(String code, String name) {
/* 26 */     this.code = code;
/* 27 */     this.name = name;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getCode() {
/* 32 */     return this.code;
/*    */   }
/*    */   
/*    */   public String getName() {
/* 36 */     return this.name;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\constant\ExposureApproach.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */