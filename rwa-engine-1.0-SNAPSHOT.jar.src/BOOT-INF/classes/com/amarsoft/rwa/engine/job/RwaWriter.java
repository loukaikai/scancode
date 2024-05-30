/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*    */ 
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import cn.hutool.core.util.StrUtil;
/*    */ import com.amarsoft.batch.support.ItemPst;
/*    */ import com.amarsoft.batch.support.MultiTableBatchWriter;
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.constant.ProcessStatus;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.job.JobUtils;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import javax.sql.DataSource;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RwaWriter<T extends Map<String, Object>>
/*    */   extends MultiTableBatchWriter<T>
/*    */ {
/* 23 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.RwaWriter.class);
/*    */   private boolean isCountNumber;
/*    */   
/* 26 */   public boolean isCountNumber() { return this.isCountNumber; } public void setCountNumber(boolean isCountNumber) {
/* 27 */     this.isCountNumber = isCountNumber;
/*    */   }
/*    */   
/*    */   public RwaWriter(String jobId, DataSource dataSource, Map<String, ItemPst> pstMap, boolean isCountNumber) {
/* 31 */     super(jobId, dataSource, pstMap);
/* 32 */     this.isCountNumber = isCountNumber;
/*    */   }
/*    */ 
/*    */   
/*    */   public String getWriteName(List<? extends T> items) {
/* 37 */     if (CollUtil.isEmpty(items)) {
/* 38 */       return "null";
/*    */     }
/* 40 */     if (items.size() == 1) {
/* 41 */       return DataUtils.getString((Map)items.get(0), (ICodeEnum)RwaParam.ID);
/*    */     }
/* 43 */     return DataUtils.getString((Map)items.get(0), (ICodeEnum)RwaParam.ID) + " - " + DataUtils.getString((Map)items.get(items.size() - 1), (ICodeEnum)RwaParam.ID);
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public boolean isNotWrite(T item) {
/* 49 */     return !StrUtil.equals(DataUtils.getString((Map)item, (ICodeEnum)RwaParam.PROCESS_STATUS), ProcessStatus.COMPLETE.getCode());
/*    */   }
/*    */ 
/*    */   
/*    */   public boolean isStop(String jobId) {
/* 54 */     return JobUtils.isStopRwaJob(jobId);
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */