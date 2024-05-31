/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.service;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*     */ import com.baomidou.mybatisplus.core.metadata.IPage;
/*     */ import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.springframework.beans.factory.annotation.Autowired;
/*     */ import org.springframework.beans.factory.annotation.Value;
/*     */ import org.springframework.dao.DataAccessException;
/*     */ import org.springframework.jdbc.core.JdbcTemplate;
/*     */ import org.springframework.jdbc.core.RowMapper;
/*     */ 
/*     */ @Service
/*     */ public class CommonService {
/*     */   @Autowired
/*     */   private DataMapper dataMapper;
/*     */   @Autowired
/*     */   private JdbcTemplate jdbcTemplate;
/*     */   @Value("${rwa.db-ssv}")
/*     */   private String ssv;
/*     */   @Value("${rwa.db-schema}")
/*     */   private String dbSchema;
/*     */   @Value("${rwa.db-partition}")
/*     */   private boolean dbPartition;
/*     */   @Value("${rwa.db-tablespace}")
/*     */   private String dbTablespace;
/*  34 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.service.CommonService.class);
/*     */   @Autowired
/*     */   private RestTemplate restTemplate; @Value("${rwa.nodes}")
/*     */   private String nodes;
/*     */   
/*  39 */   public JdbcTemplate getJdbcTemplate() { return this.jdbcTemplate; } @Value("${spring.application.name}")
/*     */   private String appName; @Value("${server.port}")
/*     */   private String serverPort; @Value("${server.servlet.context-path}")
/*  42 */   private String serverName; public String getSsv() { return this.ssv; } public void setSsv(String ssv) {
/*  43 */     this.ssv = ssv;
/*     */   }
/*     */   
/*     */   public boolean isDbPartition()
/*     */   {
/*  48 */     return this.dbPartition; } public void setDbPartition(boolean dbPartition) {
/*  49 */     this.dbPartition = dbPartition;
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
/*     */ 
/*     */ 
/*     */   
/*     */   public void execute(String sql) {
/*  66 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   public List<LinkedHashMap<String, Object>> select(Map<String, Object> param) {
/*  70 */     return this.dataMapper.select(param);
/*     */   }
/*     */   
/*     */   public int insert(Map<String, Object> param) {
/*  74 */     return this.dataMapper.insert(param);
/*     */   }
/*     */   
/*     */   public int update(Map<String, Object> param) {
/*  78 */     return this.dataMapper.update(param);
/*     */   }
/*     */   
/*     */   public int delete(Map<String, Object> param) {
/*  82 */     return this.dataMapper.delete(param);
/*     */   }
/*     */   
/*     */   public List<LinkedHashMap<String, Object>> select(String sql) {
/*  86 */     return select(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int insert(String sql) {
/*  90 */     return insert(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int update(String sql) {
/*  94 */     return update(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int delete(String sql) {
/*  98 */     return delete(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public LinkedHashMap<String, Object> selectData(String sql) {
/* 102 */     List<LinkedHashMap<String, Object>> list = select(sql);
/* 103 */     if (CollUtil.isEmpty(list)) {
/* 104 */       return null;
/*     */     }
/* 106 */     return list.get(0);
/*     */   }
/*     */   
/*     */   public int getInt(String sql) {
/* 110 */     return getInt(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int getInt(Map<String, Object> param) {
/* 114 */     BigDecimal v = getBigDecimal(param);
/* 115 */     if (v == null) {
/* 116 */       return 0;
/*     */     }
/* 118 */     return v.intValue();
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimal(String sql) {
/* 122 */     return getBigDecimal(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public BigDecimal getBigDecimal(Map<String, Object> param) {
/* 126 */     List<LinkedHashMap<String, Object>> list = select(param);
/* 127 */     if (CollUtil.isEmpty(list)) {
/* 128 */       return null;
/*     */     }
/* 130 */     LinkedHashMap<String, Object> result = list.get(0);
/* 131 */     if (result == null) {
/* 132 */       return null;
/*     */     }
/* 134 */     Iterator iterator = result.values().iterator(); if (iterator.hasNext()) { Object obj = iterator.next();
/* 135 */       return Convert.toBigDecimal(obj); }
/*     */     
/* 137 */     throw new RuntimeException("查询数据异常: param=" + param);
/*     */   }
/*     */   
/*     */   public String getString(String sql) {
/* 141 */     return getString(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public String getString(Map<String, Object> param) {
/* 145 */     List<LinkedHashMap<String, Object>> list = select(param);
/* 146 */     if (CollUtil.isEmpty(list)) {
/* 147 */       return null;
/*     */     }
/* 149 */     LinkedHashMap<String, Object> result = list.get(0);
/* 150 */     if (result == null) {
/* 151 */       return null;
/*     */     }
/* 153 */     Iterator iterator = result.values().iterator(); if (iterator.hasNext()) { Object obj = iterator.next();
/* 154 */       return obj.toString(); }
/*     */     
/* 156 */     throw new RuntimeException("查询数据异常: param=" + param);
/*     */   }
/*     */   
/*     */   public int deleteByDataBatchNo(String table, String dataBatchNo) {
/* 160 */     String sql = "delete from #{table} where data_batch_no = #{dataBatchNo}";
/* 161 */     sql = SqlBuilder.create(sql).setTable("table", table).setString("dataBatchNo", dataBatchNo).build();
/* 162 */     return delete(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int deleteByResultNo(String table, String resultNo) {
/* 166 */     String sql = "delete from #{table} where result_no = #{resultNo}";
/* 167 */     sql = SqlBuilder.create(sql).setTable("table", table).setString("resultNo", resultNo).build();
/* 168 */     return delete(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int getCountByDataBatchNo(String table, String dataBatchNo) {
/* 172 */     String sql = "select count(1) AS CNT from #{table} where data_batch_no = #{dataBatchNo}";
/* 173 */     sql = SqlBuilder.create(sql).setTable("table", table).setString("dataBatchNo", dataBatchNo).build();
/* 174 */     return getInt(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int getCountByResultNo(String table, String resultNo) {
/* 178 */     String sql = "select count(1) AS CNT from #{table} where result_no = #{resultNo}";
/* 179 */     sql = SqlBuilder.create(sql).setTable("table", table).setString("resultNo", resultNo).build();
/* 180 */     return getInt(DataUtils.createSqlMap(sql));
/*     */   }
/*     */   
/*     */   public int getCount(String sql) {
/* 184 */     return getInt("select count(*) AS CNT from (" + sql + ") t");
/*     */   }
/*     */   
/*     */   public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException {
/* 188 */     return this.jdbcTemplate.query(sql, rowMapper);
/*     */   }
/*     */   
/*     */   public List<Map<String, Object>> queryForList(String sql) throws DataAccessException {
/* 192 */     return this.jdbcTemplate.queryForList(sql);
/*     */   }
/*     */   
/*     */   public String getPartitionName(String tableName, String columnValue) {
/* 196 */     String partitionName = null;
/* 197 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/*     */       case 2:
/*     */       case 3:
/*     */       case 4:
/* 202 */         partitionName = "P" + columnValue.toUpperCase();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */         
/* 210 */         return partitionName;case 5: partitionName = tableName.toLowerCase() + "_p" + columnValue.toLowerCase(); return partitionName;
/*     */     } 
/*     */     throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */   } public boolean isExistsPartition(String tableName, String columnValue) {
/* 214 */     String partitionName, sql = null;
/* 215 */     SqlBuilder sqlBuilder = null;
/* 216 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/* 218 */         sql = "select p.* from user_tab_partitions p where upper(table_name) = upper(#{tableName}) ";
/* 219 */         sqlBuilder = SqlBuilder.create(sql);
/* 220 */         if (StrUtil.isEmpty(columnValue)) {
/* 221 */           sqlBuilder.isNotNull("p", "partition_name"); break;
/*     */         } 
/* 223 */         partitionName = getPartitionName(tableName, columnValue);
/* 224 */         sqlBuilder.condition(SqlBuilder.createQueryCondition(null, "p", "partition_name", null, partitionName));
/*     */         break;
/*     */ 
/*     */       
/*     */       case 2:
/* 229 */         sql = "select p.* from information_schema.PARTITIONS p where upper(table_name) = upper(#{tableName}) and upper(table_schema) = upper(#{dbSchema})";
/* 230 */         sqlBuilder = SqlBuilder.create(sql);
/* 231 */         sqlBuilder.setString("dbSchema", this.dbSchema);
/* 232 */         if (StrUtil.isEmpty(columnValue)) {
/* 233 */           sqlBuilder.isNotNull("p", "partition_name"); break;
/*     */         } 
/* 235 */         partitionName = getPartitionName(tableName, columnValue);
/* 236 */         sqlBuilder.condition(SqlBuilder.createQueryCondition(null, "p", "partition_name", null, partitionName));
/*     */         break;
/*     */ 
/*     */       
/*     */       case 5:
/* 241 */         sql = "select * from pg_class p where p.relname like '#{tbName}_p%' and p.relkind = 'r'";
/* 242 */         if (StrUtil.isEmpty(columnValue)) {
/* 243 */           sqlBuilder = SqlBuilder.create(sql);
/* 244 */           sqlBuilder.setKeyword("tbName", tableName.toLowerCase()); break;
/*     */         } 
/* 246 */         sql = sql + " and upper(p.relname) = upper(#{partitionName})";
/* 247 */         sqlBuilder = SqlBuilder.create(sql);
/* 248 */         sqlBuilder.setKeyword("tbName", tableName.toLowerCase())
/* 249 */           .setString("partitionName", getPartitionName(tableName, columnValue));
/*     */         break;
/*     */ 
/*     */       
/*     */       case 3:
/*     */       case 4:
/* 255 */         sql = "select p.* from pg_catalog.pg_partition p join pg_catalog.pg_class c on p.parentid = c.oid where p.parttype = 'p' and upper(c.relname) = upper(#{tableName})";
/*     */         
/* 257 */         if (StrUtil.isEmpty(columnValue)) {
/* 258 */           sqlBuilder = SqlBuilder.create(sql);
/* 259 */           sqlBuilder.isNotNull("p", "relname"); break;
/*     */         } 
/* 261 */         sql = sql + " and upper(p.relname) = upper(#{partitionName})";
/* 262 */         sqlBuilder = SqlBuilder.create(sql);
/* 263 */         sqlBuilder.setString("partitionName", getPartitionName(tableName, columnValue));
/*     */         break;
/*     */ 
/*     */       
/*     */       default:
/* 268 */         throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */     } 
/* 270 */     sqlBuilder.setString("tableName", tableName.toUpperCase());
/* 271 */     return (getCount(sqlBuilder.build()) > 0);
/*     */   }
/*     */   
/*     */   public void addPartition(String tableName, String columnValue) {
/* 275 */     String partitionName = getPartitionName(tableName, columnValue);
/* 276 */     String sql = null;
/* 277 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/* 279 */         sql = "ALTER TABLE #{table} ADD PARTITION #{partitionName} VALUES(#{columnValue})";
/* 280 */         if (StrUtil.isNotEmpty(this.dbTablespace)) {
/* 281 */           sql = sql + " TABLESPACE " + this.dbTablespace;
/*     */         }
/*     */         break;
/*     */       case 3:
/*     */       case 4:
/* 286 */         sql = "ALTER TABLE #{table} ADD PARTITION #{partitionName} VALUES(#{columnValue})";
/*     */         break;
/*     */       case 2:
/* 289 */         sql = "ALTER TABLE #{table} ADD PARTITION (PARTITION `#{partitionName}` VALUES IN (#{columnValue}))";
/*     */         break;
/*     */       case 5:
/* 292 */         sql = "CREATE TABLE #{partitionName} PARTITION OF #{table} FOR VALUES IN (#{columnValue})";
/*     */         break;
/*     */       default:
/* 295 */         throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */     } 
/*     */     
/* 298 */     sql = SqlBuilder.create(sql).setTable("table", tableName).setKeyword("partitionName", partitionName).setString("columnValue", columnValue).build();
/* 299 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   public void truncatePartition(String tableName, String columnValue) {
/* 303 */     String partitionName = getPartitionName(tableName, columnValue);
/* 304 */     String sql = null;
/* 305 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/*     */       case 3:
/*     */       case 4:
/* 309 */         sql = "ALTER TABLE #{table} TRUNCATE PARTITION #{partitionName}";
/*     */         break;
/*     */       case 2:
/* 312 */         sql = "ALTER TABLE #{table} TRUNCATE PARTITION `#{partitionName}`";
/*     */         break;
/*     */       case 5:
/* 315 */         sql = "TRUNCATE TABLE #{partitionName}";
/*     */         break;
/*     */       default:
/* 318 */         throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */     } 
/* 320 */     sql = SqlBuilder.create(sql).setTable("table", tableName).setKeyword("partitionName", partitionName).build();
/* 321 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   public void dropPartition(String tableName, String columnValue) {
/* 325 */     String partitionName = getPartitionName(tableName, columnValue);
/* 326 */     String sql = null;
/* 327 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/*     */       case 3:
/*     */       case 4:
/* 331 */         sql = "ALTER TABLE #{table} DROP PARTITION #{partitionName}";
/*     */         break;
/*     */       case 2:
/* 334 */         sql = "ALTER TABLE #{table} DROP PARTITION `#{partitionName}`";
/*     */         break;
/*     */       case 5:
/* 337 */         sql = "DROP TABLE #{partitionName}";
/*     */         break;
/*     */       default:
/* 340 */         throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */     } 
/* 342 */     sql = SqlBuilder.create(sql).setTable("table", tableName).setKeyword("partitionName", partitionName).build();
/* 343 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   public int initTable(String tableName, String id) {
/* 347 */     if (!isDbPartition()) {
/* 348 */       return 0;
/*     */     }
/*     */     
/* 351 */     if (isExistsPartition(tableName, id)) {
/*     */       
/* 353 */       truncatePartition(tableName, id);
/* 354 */       return 1;
/* 355 */     }  if (isExistsPartition(tableName, null)) {
/*     */ 
/*     */       
/* 358 */       addPartition(tableName, id);
/* 359 */       return 1;
/*     */     } 
/* 361 */     return 0;
/*     */   }
/*     */   
/*     */   public int clearTable(String tableName, String id) {
/* 365 */     if (!isDbPartition()) {
/* 366 */       return 0;
/*     */     }
/*     */     
/* 369 */     if (isExistsPartition(tableName, id)) {
/*     */       
/* 371 */       dropPartition(tableName, id);
/* 372 */       return 1;
/*     */     } 
/* 374 */     return 0;
/*     */   }
/*     */   
/*     */   public List<LinkedHashMap<String, Object>> selectPage(String sql, int current, int size) {
/* 378 */     IPage<LinkedHashMap<String, Object>> page = this.dataMapper.selectPage((IPage)Page.of(current, size), sql);
/* 379 */     log.debug("current:{} size:{}", Long.valueOf(page.getCurrent()), Long.valueOf(page.getSize()));
/* 380 */     return page.getRecords();
/*     */   }
/*     */   
/*     */   public void analyzeTable(String tableName, String columnValue) {
/* 384 */     String partitionName = "P" + columnValue;
/* 385 */     String sql = null;
/* 386 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[SqlBuilder.ctDbType.ordinal()]) {
/*     */       case 1:
/* 388 */         sql = "begin dbms_stats.gather_table_stats(USER, tabname=> #{tableName}, partname => #{partitionName}); end;";
/* 389 */         sql = SqlBuilder.create(sql).setString("tableName", tableName.toUpperCase()).setString("partitionName", partitionName).build();
/*     */         break;
/*     */       case 2:
/* 392 */         sql = "ALTER TABLE #{tableName} ANALYZE PARTITION #{partitionName}";
/* 393 */         sql = SqlBuilder.create(sql).setKeyword("tableName", tableName.toUpperCase()).setKeyword("partitionName", partitionName).build();
/*     */         break;
/*     */       default:
/* 396 */         throw new RuntimeException("非法数据库类型[" + SqlBuilder.ctDbType + "]");
/*     */     } 
/* 398 */     this.jdbcTemplate.execute(sql);
/*     */   }
/*     */   
/*     */   public String getSelfAddress() {
/* 402 */     Map<String, String> serverInfo = DataUtils.getServerInfo();
/* 403 */     return getUrl((String)serverInfo.get("ip") + ":" + this.serverPort);
/*     */   }
/*     */   
/*     */   public String getUrl(String node) {
/* 407 */     StringBuilder builder = new StringBuilder();
/* 408 */     if (!node.startsWith("http://")) {
/* 409 */       builder.append("http://");
/*     */     }
/* 411 */     builder.append(node);
/*     */     
/* 413 */     if (node.endsWith("/") && this.serverName.startsWith("/")) {
/* 414 */       builder.deleteCharAt(builder.lastIndexOf("/"));
/*     */     }
/*     */     
/* 417 */     if (!node.endsWith("/") && !this.serverName.startsWith("/")) {
/* 418 */       builder.append("/");
/*     */     }
/* 420 */     builder.append(this.serverName);
/* 421 */     return builder.toString();
/*     */   }
/*     */   
/*     */   public List<String> getActiveServices(boolean isIncludeSelf) {
/* 425 */     List<String> list = new ArrayList<>();
/* 426 */     String localhost = getSelfAddress();
/*     */     
/* 428 */     if (isIncludeSelf) {
/* 429 */       list.add(localhost);
/*     */     }
/* 431 */     if (StrUtil.isEmpty(this.nodes)) {
/* 432 */       return list;
/*     */     }
/* 434 */     String[] nodeArray = this.nodes.split(",");
/*     */     
/* 436 */     if (nodeArray.length == 1) {
/* 437 */       String node = nodeArray[0];
/*     */       
/* 439 */       if (node.contains("127.0.0.1") || node.contains("localhost") || localhost.contains(node)) {
/* 440 */         return list;
/*     */       }
/* 442 */       String url = getUrl(node);
/* 443 */       if (checkServiceHealth(url)) {
/* 444 */         list.add(url);
/*     */       }
/* 446 */       return list;
/*     */     } 
/*     */     
/* 449 */     for (String node : nodeArray) {
/*     */       
/* 451 */       if (!node.contains("127.0.0.1") && !node.contains("localhost") && !localhost.contains(node)) {
/*     */ 
/*     */         
/* 454 */         String url = getUrl(node);
/* 455 */         if (checkServiceHealth(url))
/* 456 */           list.add(url); 
/*     */       } 
/*     */     } 
/* 459 */     return list;
/*     */   }
/*     */   
/*     */   public boolean checkServiceHealth(String url) {
/*     */     try {
/* 464 */       if (!url.endsWith("/")) {
/* 465 */         url = url + "/";
/*     */       }
/* 467 */       String ok = (String)this.restTemplate.getForObject(url + "info/health", String.class, new Object[0]);
/* 468 */       return true;
/* 469 */     } catch (Exception e) {
/* 470 */       log.error("服务健康检查失败， 请检查当前节点是否异常！url=" + url);
/* 471 */       return false;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\service\CommonService.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */