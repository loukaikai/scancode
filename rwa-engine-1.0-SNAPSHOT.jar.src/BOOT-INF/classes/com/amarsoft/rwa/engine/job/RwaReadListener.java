/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job;
/*    */ 
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import com.amarsoft.batch.ItemReadListener;
/*    */ import com.amarsoft.batch.support.ChunkPolicy;
/*    */ import com.amarsoft.rwa.engine.constant.CalculateSizeType;
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.constant.InterfaceDataType;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.job.JobUtils;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import java.time.LocalDateTime;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import java.util.concurrent.ConcurrentHashMap;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ public class RwaReadListener<T extends Map<String, Object>>
/*    */   implements ItemReadListener<T>
/*    */ {
/* 22 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.RwaReadListener.class);
/*    */   
/*    */   private String jobId;
/*    */   
/*    */   private ChunkPolicy policy;
/*    */   private int chunkSize;
/*    */   private LocalDateTime start;
/* 29 */   private Map<String, Integer> sizeMap = new ConcurrentHashMap<>();
/*    */   
/*    */   public RwaReadListener(String jobId, ChunkPolicy policy, int chunkSize) {
/* 32 */     this.jobId = jobId;
/* 33 */     this.policy = policy;
/* 34 */     this.chunkSize = chunkSize;
/*    */   }
/*    */ 
/*    */   
/*    */   public void beforeRead() {
/* 39 */     this.start = LocalDateTime.now();
/*    */   }
/*    */ 
/*    */ 
/*    */ 
/*    */   
/*    */   public void afterRead(T item) {
/* 46 */     int unionSize = addAndGetCalculateSize(1, CalculateSizeType.UNION);
/*    */     
/* 48 */     int size = getCoreSize(item);
/* 49 */     int calcSize = addAndGetCalculateSize(size, CalculateSizeType.EXPOSURE);
/*    */     
/* 51 */     int relevanceSize = getRelevanceSize(item, size);
/* 52 */     int relCalcSize = addAndGetCalculateSize(relevanceSize, CalculateSizeType.RELEVANCE);
/*    */     
/* 54 */     if (relCalcSize >= this.chunkSize || calcSize >= this.chunkSize) {
/* 55 */       this.policy.setChunkSize(unionSize);
/*    */       
/* 57 */       this.sizeMap.clear();
/*    */     } else {
/* 59 */       this.policy.setChunkSize(this.chunkSize);
/*    */     } 
/*    */     
/* 62 */     JobUtils.addReaderTime(this.jobId, this.start, LocalDateTime.now());
/*    */   }
/*    */ 
/*    */   
/*    */   public void onReadError(Throwable e) {
/* 67 */     JobUtils.addReaderTime(this.jobId, this.start, LocalDateTime.now());
/*    */   }
/*    */   
/*    */   public int getCoreSize(T item) {
/* 71 */     String id = DataUtils.getString((Map)item, (ICodeEnum)RwaParam.ID);
/* 72 */     Integer size = DataUtils.getInt((Map)item, (ICodeEnum)RwaParam.SIZE);
/* 73 */     if (size == null) {
/* 74 */       size = Integer.valueOf(1);
/* 75 */       log.warn("---> 异常，当前数据[{}]没有SIZE", id);
/*    */     } 
/* 77 */     return size.intValue();
/*    */   }
/*    */   
/*    */   public int getRelevanceSize(T item, int size) {
/* 81 */     List<Map<String, Object>> relevanceList = (List<Map<String, Object>>)item.get(InterfaceDataType.RELEVANCE.getCode());
/* 82 */     if (CollUtil.isEmpty(relevanceList)) {
/* 83 */       return size;
/*    */     }
/* 85 */     return relevanceList.size();
/*    */   }
/*    */   
/*    */   public int addAndGetCalculateSize(int size, CalculateSizeType calculateSizeType) {
/* 89 */     Integer current = this.sizeMap.get(calculateSizeType.getCode());
/* 90 */     if (current == null) {
/* 91 */       current = Integer.valueOf(0);
/*    */     }
/* 93 */     current = Integer.valueOf(current.intValue() + size);
/* 94 */     this.sizeMap.put(calculateSizeType.getCode(), current);
/* 95 */     return current.intValue();
/*    */   }
/*    */   
/*    */   public Integer getCalculateSize(CalculateSizeType calculateSizeType) {
/* 99 */     return this.sizeMap.get(calculateSizeType.getCode());
/*    */   }
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\RwaReadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */