/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.exception.CodeMappingException;
/*    */ import java.util.ArrayList;
/*    */ import java.util.HashSet;
/*    */ import java.util.List;
/*    */ import java.util.Set;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class EnumUtils
/*    */ {
/*    */   public static <T extends ICodeEnum> T getEnumByCode(String code, Class<T> enumClass) {
/* 19 */     if (code == null || code.length() == 0) {
/* 20 */       throw new CodeMappingException("代码[" + enumClass + "]转换异常：code为空， 请检查数据是否准确");
/*    */     }
/* 22 */     for (ICodeEnum iCodeEnum : (ICodeEnum[])enumClass.getEnumConstants()) {
/* 23 */       if (code.equals(iCodeEnum.getCode())) {
/* 24 */         return (T)iCodeEnum;
/*    */       }
/*    */     } 
/* 27 */     throw new CodeMappingException("代码[" + enumClass + "]转换异常：无法获取[code=" + code + "]对应枚举，请检查数据或配置是否准确，或枚举类是否定义完整");
/*    */   }
/*    */   
/*    */   public static <T extends ICodeEnum> List<T> getEnumList(String[] codes, Class<T> enumClass) {
/* 31 */     if (codes == null || codes.length == 0) {
/* 32 */       throw new CodeMappingException("codes为空， 请检查数据是否准确");
/*    */     }
/* 34 */     List<T> list = new ArrayList<>();
/* 35 */     for (String code : codes) {
/* 36 */       list.add(getEnumByCode(code, enumClass));
/*    */     }
/* 38 */     return list;
/*    */   }
/*    */   
/*    */   public static <T extends ICodeEnum> Set<String> getCodes(T... enums) {
/* 42 */     Set<String> set = new HashSet<>();
/* 43 */     for (T e : enums) {
/* 44 */       set.add(e.getCode());
/*    */     }
/* 46 */     return set;
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\EnumUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */