/*     */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*     */ import com.amarsoft.batch.ItemReader;
/*     */ import com.amarsoft.batch.MultiTableRowGetter;
/*     */ import com.amarsoft.batch.exception.ItemStreamException;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import java.sql.Connection;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.SQLWarning;
/*     */ import java.sql.Statement;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.Map;
/*     */ import java.util.concurrent.ConcurrentHashMap;
/*     */ import javax.sql.DataSource;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.jdbc.SQLWarningException;
/*     */ import org.springframework.jdbc.support.JdbcUtils;
/*     */ import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
/*     */ import org.springframework.jdbc.support.SQLExceptionTranslator;
/*     */ import org.springframework.jdbc.support.SQLStateSQLExceptionTranslator;
/*     */ import org.springframework.util.Assert;
/*     */ 
/*     */ public class MultiTableCursorItemReader<T extends Map<String, Object>> implements ItemReader<T> {
/*     */   private DataSource dataSource;
/*     */   private Connection conn;
/*  30 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.support.MultiTableCursorItemReader.class); private String jobId; private String name;
/*     */   
/*     */   public void setDataSource(DataSource dataSource) {
/*  33 */     this.dataSource = dataSource; } public DataSource getDataSource() {
/*  34 */     return this.dataSource;
/*     */   }
/*     */   
/*     */   public void setJobId(String jobId)
/*     */   {
/*  39 */     this.jobId = jobId; } public String getJobId() {
/*  40 */     return this.jobId;
/*     */   }
/*     */   
/*  43 */   public void setName(String name) { this.name = name; } public String getName() {
/*  44 */     return this.name;
/*     */   }
/*     */   private boolean ignoreWarnings = true;
/*  47 */   public void setIgnoreWarnings(boolean ignoreWarnings) { this.ignoreWarnings = ignoreWarnings; } public boolean isIgnoreWarnings() {
/*  48 */     return this.ignoreWarnings;
/*     */   }
/*     */ 
/*     */   
/*  52 */   private int queryTimeout = -1;
/*     */   private boolean initialized = false;
/*     */   private LinkedHashMap<String, String> sqlMap;
/*     */   private String associatedField;
/*     */   private String beginId;
/*     */   private String endId;
/*     */   private SqlBuilder.IdParamType idParamType;
/*     */   private String mainKey;
/*     */   
/*     */   public void setSqlMap(LinkedHashMap<String, String> sqlMap) {
/*  62 */     this.sqlMap = sqlMap; } public LinkedHashMap<String, String> getSqlMap() {
/*  63 */     return this.sqlMap;
/*     */   }
/*     */   
/*     */   public void setAssociatedField(String associatedField)
/*     */   {
/*  68 */     this.associatedField = associatedField; } public String getAssociatedField() {
/*  69 */     return this.associatedField;
/*     */   }
/*  71 */   public void setBeginId(String beginId) { this.beginId = beginId; } public String getBeginId() {
/*  72 */     return this.beginId;
/*     */   }
/*  74 */   public void setEndId(String endId) { this.endId = endId; } public String getEndId() {
/*  75 */     return this.endId;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  85 */   private LinkedHashMap<String, PreparedStatement> preparedStatementMap = new LinkedHashMap<>();
/*     */   
/*  87 */   private LinkedHashMap<String, ResultSet> resultSetMap = new LinkedHashMap<>(); private MultiTableRowGetter<T> rowGetter; private SQLExceptionTranslator exceptionTranslator;
/*     */   
/*  89 */   public MultiTableRowGetter<T> getRowGetter() { return this.rowGetter; } public void setRowGetter(MultiTableRowGetter<T> rowGetter) {
/*  90 */     this.rowGetter = rowGetter;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  96 */   private int currentItemCount = 0; public void setCurrentItemCount(int currentItemCount) { this.currentItemCount = currentItemCount; } public int getCurrentItemCount() {
/*  97 */     return this.currentItemCount;
/*     */   }
/*     */   
/*     */   protected void doOpen() throws Exception {
/* 101 */     Assert.state(!this.initialized, "Stream is already initialized.  Close before re-opening.");
/* 102 */     Assert.notNull(this.sqlMap, "SqlMap must be provided.");
/* 103 */     Assert.notNull(this.rowGetter, "RowGetter must be provided.");
/* 104 */     Assert.notNull(this.associatedField, "AssociatedField must be provided.");
/* 105 */     initConnection();
/* 106 */     openCursor(this.conn);
/* 107 */     this.initialized = true;
/*     */   }
/*     */   
/*     */   private void initConnection() {
/* 111 */     Assert.state((getDataSource() != null), "DataSource must not be null.");
/*     */     try {
/* 113 */       this.conn = this.dataSource.getConnection();
/* 114 */     } catch (SQLException e) {
/* 115 */       close();
/* 116 */       throw new RuntimeException("get connection exception.", e);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void open(ExecutionContext executionContext) throws ItemStreamException {
/*     */     try {
/* 124 */       doOpen();
/* 125 */     } catch (Exception var5) {
/* 126 */       throw new ItemStreamException("Failed to initialize the reader", var5);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public T read() throws Exception {
/* 133 */     this.currentItemCount++;
/* 134 */     return doRead();
/*     */   }
/*     */ 
/*     */   
/*     */   public void close() throws ItemStreamException {
/* 139 */     this.currentItemCount = 0;
/*     */     try {
/* 141 */       doClose();
/* 142 */     } catch (Exception var2) {
/* 143 */       throw new ItemStreamException("Error while closing item reader", var2);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private void openCursor(Connection conn) {
/* 149 */     this.idParamType = SqlBuilder.confirmIdParamType(this.beginId, this.endId);
/* 150 */     int i = 0;
/* 151 */     String ctKey = null;
/* 152 */     Map<String, String> ctSqlMap = new ConcurrentHashMap<>();
/*     */     try {
/* 154 */       for (String key : this.sqlMap.keySet()) {
/* 155 */         ctKey = key;
/* 156 */         if (i == 0) {
/* 157 */           this.mainKey = key;
/*     */         }
/* 159 */         LocalDateTime start = LocalDateTime.now();
/* 160 */         PreparedStatement pst = initPreparedStatement(key, this.sqlMap.get(key), ctSqlMap);
/*     */ 
/*     */         
/* 163 */         ResultSet rs = pst.executeQuery();
/* 164 */         log.debug("<== MtReader[{}] costs : {}", key, Duration.between(start, LocalDateTime.now()));
/* 165 */         handleWarnings(pst);
/* 166 */         this.preparedStatementMap.put(key, pst);
/* 167 */         this.resultSetMap.put(key, rs);
/* 168 */         i++;
/*     */       } 
/* 170 */     } catch (SQLException e) {
/* 171 */       log.error("<==  ", e);
/* 172 */       log.error("<==  Executing[{}]: {}", ctKey, ctSqlMap.get(ctKey));
/* 173 */       close();
/* 174 */       throw getExceptionTranslator().translate("Executing [" + ctKey + "] query", (String)ctSqlMap.get(ctKey), e);
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private PreparedStatement initPreparedStatement(String key, String sql, Map<String, String> sqlMap) throws SQLException {
/* 180 */     PreparedStatement pst = null;
/* 181 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$IdParamType[this.idParamType.ordinal()]) {
/*     */ 
/*     */ 
/*     */       
/*     */       case 1:
/* 186 */         sql = SqlBuilder.create("select * from (" + sql + ") t where 1 = 1 ").isNotNull("t", this.associatedField).orderByAsc("t", this.associatedField).build();
/* 187 */         log.debug("==> MtReader[{}] Preparing Sql: {}", key, sql);
/* 188 */         pst = this.conn.prepareStatement(sql, 1003, 1007);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 193 */         sql = SqlBuilder.create("select * from (" + sql + ") t where t." + this.associatedField + " >= ? and t." + this.associatedField + " < ? ").orderByAsc("t", this.associatedField).build();
/* 194 */         log.debug("==> MtReader[{}] Preparing Sql : {}", key, sql);
/* 195 */         log.debug("==> MtReader[{}] Preparing Params: [1:{}][2:{}]", new Object[] { key, this.beginId, this.endId });
/* 196 */         pst = this.conn.prepareStatement(sql, 1003, 1007);
/* 197 */         pst.setString(1, this.beginId);
/* 198 */         pst.setString(2, this.endId);
/*     */         break;
/*     */       case 3:
/* 201 */         sql = "select * from (" + sql + ") t where t." + this.associatedField + " = ?";
/* 202 */         log.debug("==> MtReader[{}] Preparing Sql : {}", key, sql);
/* 203 */         log.debug("==> MtReader[{}] Preparing Params: [1:{}][2:{}]", new Object[] { key, this.beginId, this.endId });
/* 204 */         pst = this.conn.prepareStatement(sql, 1003, 1007);
/* 205 */         pst.setString(1, this.beginId);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 4:
/* 210 */         sql = SqlBuilder.create("select * from (" + sql + ") t where t." + this.associatedField + " >= ? and length(t." + this.associatedField + ") > 0").orderByAsc("t", this.associatedField).build();
/* 211 */         log.debug("==> MtReader[{}] Preparing Sql : {}", key, sql);
/* 212 */         log.debug("==> MtReader[{}] Preparing Params: [1:{}][2:{}]", new Object[] { key, this.beginId, this.endId });
/* 213 */         pst = this.conn.prepareStatement(sql, 1003, 1007);
/* 214 */         pst.setString(1, this.beginId);
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 219 */         sql = SqlBuilder.create("select * from (" + sql + ") t where t." + this.associatedField + " < ? and length(t." + this.associatedField + ") > 0").orderByAsc("t", this.associatedField).build();
/* 220 */         log.debug("==> MtReader[{}] Preparing Sql : {}", key, sql);
/* 221 */         log.debug("==> MtReader[{}] Preparing Params: [1:{}][2:{}]", new Object[] { key, this.beginId, this.endId });
/* 222 */         pst = this.conn.prepareStatement(sql, 1003, 1007);
/* 223 */         pst.setString(1, this.endId);
/*     */         break;
/*     */     } 
/* 226 */     sqlMap.put(key, sql);
/* 227 */     return pst;
/*     */   }
/*     */   
/*     */   protected synchronized T doRead() throws Exception {
/* 231 */     ResultSet mainRs = this.resultSetMap.get(this.mainKey);
/* 232 */     if (mainRs == null) {
/* 233 */       throw new ItemStreamException("Reader must be open before it can be read.");
/*     */     }
/* 235 */     int currentRow = getCurrentItemCount();
/* 236 */     Map map = (Map)this.rowGetter.nextRow(this.resultSetMap, this.mainKey, this.associatedField, currentRow);
/* 237 */     if (map == null) {
/* 238 */       return null;
/*     */     }
/* 240 */     return (T)map;
/*     */   }
/*     */   
/*     */   protected void doClose() throws Exception {
/* 244 */     this.initialized = false;
/* 245 */     for (String key : this.sqlMap.keySet()) {
/* 246 */       ResultSet rs = this.resultSetMap.get(key);
/* 247 */       JdbcUtils.closeResultSet(rs);
/* 248 */       rs = null;
/* 249 */       JdbcUtils.closeStatement(this.preparedStatementMap.get(key));
/*     */     } 
/* 251 */     this.resultSetMap = new LinkedHashMap<>();
/* 252 */     this.preparedStatementMap = new LinkedHashMap<>();
/* 253 */     JdbcUtils.closeConnection(this.conn);
/*     */   }
/*     */   
/*     */   private void handleWarnings(Statement statement) throws SQLWarningException, SQLException {
/* 257 */     if (this.ignoreWarnings) {
/* 258 */       if (log.isDebugEnabled()) {
/* 259 */         SQLWarning warningToLog = statement.getWarnings();
/* 260 */         while (warningToLog != null) {
/* 261 */           log.debug("SQLWarning ignored: SQL state '" + warningToLog.getSQLState() + "', error code '" + warningToLog
/* 262 */               .getErrorCode() + "', message [" + warningToLog.getMessage() + "]");
/* 263 */           warningToLog = warningToLog.getNextWarning();
/*     */         } 
/*     */       } 
/*     */     } else {
/*     */       
/* 268 */       SQLWarning warnings = statement.getWarnings();
/* 269 */       if (warnings != null) {
/* 270 */         throw new SQLWarningException("Warning not ignored", warnings);
/*     */       }
/*     */     } 
/*     */   }
/*     */   
/*     */   private SQLExceptionTranslator getExceptionTranslator() {
/* 276 */     synchronized (this) {
/* 277 */       if (this.exceptionTranslator == null) {
/* 278 */         if (this.dataSource != null) {
/* 279 */           this.exceptionTranslator = (SQLExceptionTranslator)new SQLErrorCodeSQLExceptionTranslator(this.dataSource);
/*     */         } else {
/*     */           
/* 282 */           this.exceptionTranslator = (SQLExceptionTranslator)new SQLStateSQLExceptionTranslator();
/*     */         } 
/*     */       }
/*     */     } 
/* 286 */     return this.exceptionTranslator;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\MultiTableCursorItemReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */