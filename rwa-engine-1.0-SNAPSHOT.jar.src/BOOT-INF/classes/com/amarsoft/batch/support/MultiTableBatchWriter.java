/*     */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*     */ 
/*     */ import com.amarsoft.batch.ItemPstSetter;
/*     */ import com.amarsoft.batch.ItemWriter;
/*     */ import com.amarsoft.batch.exception.JobRunningException;
/*     */ import com.amarsoft.batch.exception.JobStopException;
/*     */ import com.amarsoft.batch.support.ItemPst;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.jdbc.support.JdbcUtils;
/*     */ 
/*     */ public abstract class MultiTableBatchWriter<T extends Map<String, Object>>
/*     */   implements ItemWriter<T>
/*     */ {
/*     */   private String jobId;
/*     */   private DataSource dataSource;
/*     */   private Connection conn;
/*  25 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.support.MultiTableBatchWriter.class); private String mainKey; private Map<String, ItemPst> pstMap;
/*     */   
/*     */   public String getJobId() {
/*  28 */     return this.jobId; } public void setJobId(String jobId) {
/*  29 */     this.jobId = jobId;
/*     */   }
/*  31 */   public DataSource getDataSource() { return this.dataSource; } public void setDataSource(DataSource dataSource) {
/*  32 */     this.dataSource = dataSource;
/*     */   }
/*     */   
/*     */   public Map<String, ItemPst> getPstMap() {
/*  36 */     return this.pstMap; } public void setPstMap(Map<String, ItemPst> pstMap) {
/*  37 */     this.pstMap = pstMap;
/*     */   }
/*  39 */   private Map<String, Integer> pstSizeMap = new LinkedHashMap<>();
/*     */   
/*     */   private String writeGroupName;
/*     */   
/*     */   public MultiTableBatchWriter(String jobId, DataSource dataSource, Map<String, ItemPst> pstMap) {
/*  44 */     this.jobId = jobId;
/*  45 */     this.dataSource = dataSource;
/*  46 */     this.pstMap = pstMap;
/*     */   }
/*     */ 
/*     */   
/*     */   public void write(List<? extends T> items) throws Exception {
/*  51 */     if (isStop(this.jobId)) {
/*  52 */       throw new JobStopException("Job-" + this.jobId + " is stop!");
/*     */     }
/*  54 */     if (items.isEmpty()) {
/*     */       return;
/*     */     }
/*     */     
/*  58 */     this.writeGroupName = getWriteName(items);
/*  59 */     if (log.isDebugEnabled()) {
/*  60 */       log.debug("start batch write with [{}] items : {}", Integer.valueOf(items.size()), this.writeGroupName);
/*     */     }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  67 */     for (int i = 0; i < items.size(); i++) {
/*  68 */       Map map = (Map)items.get(i);
/*     */       
/*  70 */       if (!isNotWrite((T)map))
/*     */       {
/*     */ 
/*     */         
/*  74 */         for (String key : this.pstMap.keySet()) {
/*  75 */           addBatch(key, (T)map, ((ItemPst)this.pstMap.get(key)).getPst(), ((ItemPst)this.pstMap.get(key)).getSetter());
/*     */         }
/*     */       }
/*     */     } 
/*  79 */     executeBatch();
/*  80 */     if (log.isDebugEnabled()) {
/*  81 */       log.debug("end batch write with [{}] items : {}", Integer.valueOf(items.size()), this.writeGroupName);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public void init() throws JobRunningException {
/*     */     try {
/*  97 */       this.conn = this.dataSource.getConnection();
/*  98 */       this.conn.setAutoCommit(false);
/*  99 */     } catch (SQLException e) {
/* 100 */       throw new JobRunningException("get connection exception.", e);
/*     */     } 
/*     */     
/* 103 */     for (String key : this.pstMap.keySet()) {
/* 104 */       if (this.mainKey == null) {
/* 105 */         this.mainKey = key;
/*     */       }
/* 107 */       ItemPst itemPst = this.pstMap.get(key);
/* 108 */       itemPst.initPreparedStatement(this.conn);
/*     */     } 
/*     */   }
/*     */   
/*     */   public void close() {
/* 113 */     for (String key : this.pstMap.keySet()) {
/* 114 */       JdbcUtils.closeStatement(((ItemPst)this.pstMap.get(key)).getPst());
/*     */     }
/* 116 */     JdbcUtils.closeConnection(this.conn);
/*     */   }
/*     */ 
/*     */   
/*     */   private void addBatch(String key, T item, PreparedStatement pst, ItemPstSetter<Object> setter) throws SQLException {
/* 121 */     Object value = item.get(key);
/* 122 */     if (value == null) {
/*     */       return;
/*     */     }
/* 125 */     List<Object> list = (List<Object>)value;
/* 126 */     if (list.size() == 0) {
/*     */       return;
/*     */     }
/*     */     
/* 130 */     for (int i = 0; i < list.size(); i++) {
/* 131 */       Object data = list.get(i);
/*     */       try {
/* 133 */         setter.setValues(data, pst);
/* 134 */         pst.addBatch();
/* 135 */       } catch (SQLException e) {
/* 136 */         throw new SQLException("[" + key + " : " + this.writeGroupName + "] inserting data exception, current data is : " + data, e);
/*     */       } 
/* 138 */       addPstSize(key, 1);
/*     */     } 
/*     */   }
/*     */   
/*     */   private void executeBatch() throws SQLException {
/* 143 */     for (String key : this.pstMap.keySet()) {
/*     */       try {
/* 145 */         executeBatch(key);
/* 146 */       } catch (SQLException e) {
/* 147 */         throw new SQLException("[" + key + " : " + this.writeGroupName + "] executing batch exception.", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */   
/*     */   private int[] executeBatch(String key) throws SQLException {
/* 153 */     int count = getPstSize(key);
/* 154 */     PreparedStatement pst = ((ItemPst)this.pstMap.get(key)).getPst();
/* 155 */     if (count > 0) {
/* 156 */       int[] updateCounts = pst.executeBatch();
/*     */       
/* 158 */       this.conn.commit();
/* 159 */       if (log.isDebugEnabled()) {
/* 160 */         log.debug("[{}] commit batch with {} items.", key, Integer.valueOf(count));
/*     */       }
/* 162 */       return updateCounts;
/*     */     } 
/* 164 */     return null;
/*     */   }
/*     */   
/*     */   private void addPstSize(String key, int size) {
/* 168 */     if (this.pstSizeMap.get(key) == null) {
/* 169 */       this.pstSizeMap.put(key, Integer.valueOf(size));
/*     */     } else {
/* 171 */       this.pstSizeMap.put(key, Integer.valueOf(((Integer)this.pstSizeMap.get(key)).intValue() + size));
/*     */     } 
/*     */   }
/*     */   
/*     */   private int getPstSize(String key) {
/* 176 */     Integer size = this.pstSizeMap.get(key);
/* 177 */     if (size == null) {
/* 178 */       return 0;
/*     */     }
/* 180 */     return size.intValue();
/*     */   }
/*     */   
/*     */   public abstract String getWriteName(List<? extends T> paramList);
/*     */   
/*     */   public abstract boolean isNotWrite(T paramT);
/*     */   
/*     */   public abstract boolean isStop(String paramString);
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\MultiTableBatchWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */