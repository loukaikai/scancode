/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*     */ 
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.date.DateUtil;
/*     */ import cn.hutool.core.date.LocalDateTimeUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.exception.SqlBuildException;
/*     */ import java.math.BigDecimal;
/*     */ import java.sql.Date;
/*     */ import java.sql.Timestamp;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.ZoneId;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.TreeSet;
/*     */ import java.util.regex.Matcher;
/*     */ import java.util.regex.Pattern;
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
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class SqlBuilder
/*     */ {
/*     */   public static DbType ctDbType;
/*  88 */   private final StringBuilder sql = new StringBuilder();
/*     */   private DbType dbType;
/*     */   public static final String SQL_AND = " and ";
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder create() {
/*  93 */     return new com.amarsoft.rwa.engine.util.SqlBuilder();
/*     */   }
/*     */ 
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder create(CharSequence sql) {
/*  98 */     return create().append(sql.toString()).setDbType(null);
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder create(CharSequence sql, DbType dbProduct) {
/* 102 */     return create().append(sql.toString()).setDbType(dbProduct);
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder create(CharSequence sql, String dbProduct) {
/* 106 */     return create().append(sql.toString()).setDbType(getDbType(dbProduct));
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder createBySub(CharSequence sql) {
/* 110 */     return create("select * from (" + sql + ") t ");
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.util.SqlBuilder createBySub(CharSequence sql, DbType dbType) {
/* 114 */     return createBySub("select * from (" + sql + ") t ", dbType);
/*     */   }
/*     */   
/*     */   public static DbType getDbType(String dbType) {
/* 118 */     if (StrUtil.isNotEmpty(dbType)) {
/* 119 */       dbType = dbType.toLowerCase();
/* 120 */       if (dbType.equals("mysql"))
/* 121 */         return DbType.MYSQL; 
/* 122 */       if (dbType.equals("oracle"))
/* 123 */         return DbType.ORACLE; 
/* 124 */       if (dbType.equals("postgresql"))
/* 125 */         return DbType.POSTGRESQL; 
/* 126 */       if (dbType.equals("opengauss"))
/* 127 */         return DbType.OPENGAUSS; 
/* 128 */       if (dbType.equals("gauss")) {
/* 129 */         return DbType.GAUSS;
/*     */       }
/* 131 */       throw new RuntimeException("暂不支持非MySQL、Oracle、PostgreSql、OpenGauss、Gauss的数据库");
/*     */     } 
/*     */     
/* 134 */     return null;
/*     */   }
/*     */   
/*     */   public static void setCtDbType(String dbType) {
/* 138 */     ctDbType = getDbType(dbType);
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder append(String sql) {
/* 142 */     this.sql.append(sql);
/* 143 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setDbType(DbType dbType) {
/* 147 */     if (dbType == null) {
/* 148 */       if (ctDbType == null) {
/* 149 */         this.dbType = DbType.MYSQL;
/*     */       } else {
/* 151 */         this.dbType = ctDbType;
/*     */       } 
/*     */     } else {
/* 154 */       this.dbType = dbType;
/*     */     } 
/* 156 */     return this;
/*     */   }
/*     */   
/*     */   public String build() {
/* 160 */     if (this.sql.indexOf("#") > 0) {
/* 161 */       throw new SqlBuildException("SQL配置未完成[sql=" + this.sql + "]");
/*     */     }
/* 163 */     return this.sql.toString();
/*     */   }
/*     */   
/*     */   public String limitBuild(int limit) {
/* 167 */     if (this.sql.indexOf("#") > 0) {
/* 168 */       throw new SqlBuildException("SQL配置未完成[sql=" + this.sql + "]");
/*     */     }
/* 170 */     StringBuilder s = new StringBuilder();
/* 171 */     s.append("select * from (").append(this.sql).append(") t ");
/* 172 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[this.dbType.ordinal()]) {
/*     */       case 1:
/* 174 */         s.append("where rownum <= ").append(limit);
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
/* 186 */         return s.toString();case 2: case 3: case 4: case 5: s.append("limit ").append(limit); return s.toString();
/*     */     } 
/*     */     throw new SqlBuildException("非法数据库类型!");
/*     */   }
/*     */   public String toString() {
/* 191 */     return build();
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder orderByAsc(String column) {
/* 195 */     if (StrUtil.isEmpty(column)) {
/* 196 */       return this;
/*     */     }
/* 198 */     this.sql.append(" order by ").append(checkName(column));
/* 199 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder orderByAsc(String tableAlias, String column) {
/* 203 */     if (StrUtil.isEmpty(column)) {
/* 204 */       return this;
/*     */     }
/* 206 */     this.sql.append(" order by ").append(getFullColumn(tableAlias, column));
/* 207 */     return this;
/*     */   }
/*     */   
/*     */   public static StringBuilder replaceAll(StringBuilder sb, String oldStr, CharSequence newStr) {
/* 211 */     if (StrUtil.isEmpty(sb) || StrUtil.isEmpty(oldStr)) {
/* 212 */       return sb;
/*     */     }
/* 214 */     String n = null;
/* 215 */     if (StrUtil.isEmpty(newStr)) {
/* 216 */       n = "";
/*     */     } else {
/* 218 */       n = newStr.toString();
/*     */     } 
/* 220 */     int index = sb.indexOf(oldStr);
/* 221 */     if (index > -1) {
/* 222 */       int lastIndex = 0;
/* 223 */       while (index > -1) {
/* 224 */         sb.replace(index, index + oldStr.length(), n);
/* 225 */         lastIndex = index + n.length();
/* 226 */         index = sb.indexOf(oldStr, lastIndex);
/*     */       } 
/*     */     } 
/* 229 */     return sb;
/*     */   }
/*     */   
/*     */   private String checkName(String name) {
/* 233 */     if (StrUtil.isEmpty(name)) {
/* 234 */       throw new SqlBuildException("字段设置名称不能为空！");
/*     */     }
/* 236 */     name = name.trim();
/*     */     
/* 238 */     Pattern pattern = Pattern.compile("[A-Za-z]+[A-Za-z0-9_]*");
/* 239 */     Matcher matcher = pattern.matcher(name);
/* 240 */     if (matcher.find()) {
/* 241 */       return name;
/*     */     }
/* 243 */     throw new SqlBuildException("字段名称[" + name + "]配置异常");
/*     */   }
/*     */ 
/*     */   
/*     */   private String getKey(String name) {
/* 248 */     return "#{" + checkName(name) + "}";
/*     */   }
/*     */   
/*     */   private String getValue(LocalDate date) {
/* 252 */     return getValueOfDate(LocalDateTimeUtil.format(date, "yyyy-MM-dd"));
/*     */   }
/*     */   
/*     */   private String getValueOfDate(String date) {
/* 256 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[this.dbType.ordinal()]) {
/*     */       case 2:
/* 258 */         return "'" + date + "'";
/*     */       case 1:
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 263 */         return "to_date('" + date + "','yyyy-MM-dd')";
/*     */     } 
/* 265 */     throw new RuntimeException("非法数据库类型[" + this.dbType + "]");
/*     */   }
/*     */ 
/*     */   
/*     */   private String getValue(Timestamp time) {
/* 270 */     return getValueOfTime(DateUtil.format(time, "yyyy-MM-dd HH:mm:ss.SSS"));
/*     */   }
/*     */   
/*     */   private String getValue(LocalDateTime dateTime) {
/* 274 */     return getValueOfTime(LocalDateTimeUtil.format(dateTime, "yyyy-MM-dd HH:mm:ss.SSS"));
/*     */   }
/*     */   
/*     */   private String getValueOfTime(String time) {
/* 278 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$util$SqlBuilder$DbType[this.dbType.ordinal()]) {
/*     */       case 1:
/* 280 */         return "to_timestamp('" + time + "', 'yyyy-mm-dd hh24:mi:ss.ff3')";
/*     */       case 2:
/* 282 */         return "'" + time + "'";
/*     */       case 3:
/*     */       case 4:
/*     */       case 5:
/* 286 */         return "to_timestamp('" + time + "', 'yyyy-mm-dd hh24:mi:ss.ms')";
/*     */     } 
/* 288 */     throw new RuntimeException("非法数据库类型[" + this.dbType + "]");
/*     */   }
/*     */ 
/*     */   
/*     */   private String getValue(Date date) {
/* 293 */     return getValue(date.toLocalDate());
/*     */   }
/*     */   
/*     */   private String getValue(Date date) {
/* 297 */     return getValue(LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate());
/*     */   }
/*     */   
/*     */   public String getValue(String value) {
/* 301 */     return "'" + value + "'";
/*     */   }
/*     */   
/*     */   public String getValue(Number number) {
/* 305 */     return getValue(NumberUtil.toBigDecimal(number));
/*     */   }
/*     */   
/*     */   public String getValue(Integer value) {
/* 309 */     return value.toString();
/*     */   }
/*     */   
/*     */   public String getValue(BigDecimal value) {
/* 313 */     return value.stripTrailingZeros().toPlainString();
/*     */   }
/*     */   
/*     */   private String getValue(Object obj) {
/* 317 */     if (obj instanceof String)
/* 318 */       return getValue((String)obj); 
/* 319 */     if (obj instanceof Number)
/* 320 */       return getValue((Number)obj); 
/* 321 */     if (obj instanceof Timestamp)
/* 322 */       return getValue((Timestamp)obj); 
/* 323 */     if (obj instanceof Date)
/* 324 */       return getValue((Date)obj); 
/* 325 */     if (obj instanceof Date)
/* 326 */       return getValue((Date)obj); 
/* 327 */     if (obj instanceof LocalDateTime)
/* 328 */       return getValue((LocalDateTime)obj); 
/* 329 */     if (obj instanceof LocalDate) {
/* 330 */       return getValue((LocalDate)obj);
/*     */     }
/* 332 */     throw new SqlBuildException("参数值异常[value=" + obj + "]");
/*     */   }
/*     */   
/*     */   public String getValues(Collection values) {
/* 336 */     StringBuilder sb = new StringBuilder();
/* 337 */     for (Object value : values) {
/* 338 */       if (value != null) {
/* 339 */         sb.append(getValue(value)).append(",");
/*     */       }
/*     */     } 
/* 342 */     if (sb.length() > 0) {
/* 343 */       sb.deleteCharAt(sb.lastIndexOf(","));
/* 344 */       return "(" + sb + ")";
/*     */     } 
/* 346 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setTable(String name, String value) {
/* 351 */     if (name != null && value != null) {
/* 352 */       replaceAll(this.sql, getKey(name), value);
/*     */     }
/* 354 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setKeyword(String name, String value) {
/* 358 */     if (name != null && value != null) {
/* 359 */       replaceAll(this.sql, getKey(name), value);
/*     */     }
/* 361 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setDate(String name, Date value) {
/* 365 */     if (name != null && value != null) {
/* 366 */       if (value instanceof Date) {
/* 367 */         return setDate(name, ((Date)value).toLocalDate());
/*     */       }
/* 369 */       return setDate(name, value.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
/*     */     } 
/*     */     
/* 372 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setDate(String name, Date value) {
/* 376 */     if (name != null && value != null) {
/* 377 */       return setDate(name, value.toLocalDate());
/*     */     }
/* 379 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setTimestamp(String name, Timestamp value) {
/* 383 */     if (name != null && value != null) {
/* 384 */       replaceAll(this.sql, getKey(name), getValue(value));
/*     */     }
/* 386 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setDate(String name, LocalDate value) {
/* 390 */     if (name != null && value != null) {
/* 391 */       replaceAll(this.sql, getKey(name), getValue(value));
/*     */     }
/* 393 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setInt(String name, int value) {
/* 397 */     if (name != null) {
/* 398 */       replaceAll(this.sql, getKey(name), getValue(Integer.valueOf(value)));
/*     */     }
/* 400 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setBigDecimal(String name, BigDecimal value) {
/* 404 */     if (name != null) {
/* 405 */       replaceAll(this.sql, getKey(name), getValue(value));
/*     */     }
/* 407 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setString(String name, String value) {
/* 411 */     if (name != null && value != null) {
/* 412 */       replaceAll(this.sql, getKey(name), getValue(value));
/*     */     }
/* 414 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setString(String name, ICodeEnum value) {
/* 418 */     if (name != null && value != null) {
/* 419 */       replaceAll(this.sql, getKey(name), getValue(value.getCode()));
/*     */     }
/* 421 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setString(String name, Collection values) {
/* 425 */     if (name != null && values != null && values.size() > 0) {
/* 426 */       replaceAll(this.sql, getKey(name), getValues(values));
/*     */     }
/* 428 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setParam(String name, Object value) {
/* 432 */     if (name == null) {
/* 433 */       throw new SqlBuildException("参数名不能为空");
/*     */     }
/* 435 */     if (value != null) {
/* 436 */       replaceAll(this.sql, getKey(name), getValue(value));
/*     */     } else {
/* 438 */       replaceAll(this.sql, getKey(name), "null");
/*     */     } 
/* 440 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder setParam(Map<String, Object> paramMap) {
/* 444 */     TreeSet<String> set = scanParams(this.sql.toString());
/* 445 */     if (set == null || set.size() == 0) {
/* 446 */       return this;
/*     */     }
/* 448 */     for (String param : set) {
/* 449 */       setParam(param, paramMap.get(param));
/*     */     }
/* 451 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder isNull(String tableAlias, String columnName) {
/* 455 */     this.sql.append(" and ").append(getFullColumn(tableAlias, columnName)).append(" is null");
/* 456 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder isNotNull(String tableAlias, String columnName) {
/* 460 */     this.sql.append(" and ").append(getFullColumn(tableAlias, columnName)).append(" is not null");
/* 461 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder isNull(String tableAlias, String columnName, boolean judgment) {
/* 465 */     if (judgment) {
/* 466 */       return isNull(tableAlias, columnName);
/*     */     }
/* 468 */     return isNotNull(tableAlias, columnName);
/*     */   }
/*     */ 
/*     */   
/*     */   public static QueryCondition createQueryCondition(String at, String alias, String column, ConditionSymbol symbol, Object value, boolean judgment) {
/* 473 */     if (judgment) {
/* 474 */       return createQueryCondition(at, alias, column, symbol, value);
/*     */     }
/* 476 */     return null;
/*     */   }
/*     */   
/*     */   public static QueryCondition createQueryCondition(String at, String alias, String column, ConditionSymbol symbol, Object value) {
/* 480 */     return new QueryCondition(at, alias, column, symbol, value);
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder condition(String at, String condition) {
/* 484 */     if (StrUtil.isEmpty(condition) && StrUtil.isEmpty(at))
/* 485 */       return this; 
/* 486 */     if (StrUtil.isEmpty(condition)) {
/* 487 */       clear(at);
/* 488 */     } else if (StrUtil.isEmpty(at)) {
/* 489 */       this.sql.append(condition);
/*     */     } else {
/* 491 */       replaceAll(this.sql, getKey(at), condition);
/*     */     } 
/* 493 */     return this;
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder condition(String at, List<QueryCondition> conditions) {
/* 497 */     return condition(at, getSqlCondition(conditions));
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder condition(QueryCondition queryCondition) {
/* 501 */     if (queryCondition == null) {
/* 502 */       return this;
/*     */     }
/* 504 */     String condition = getSqlCondition(queryCondition);
/* 505 */     if (StrUtil.isEmpty(condition)) {
/* 506 */       if (StrUtil.isEmpty(queryCondition.getConditionAt())) {
/* 507 */         return this;
/*     */       }
/* 509 */       return clear(queryCondition.getConditionAt());
/*     */     } 
/*     */     
/* 512 */     if (StrUtil.isEmpty(queryCondition.getConditionAt())) {
/* 513 */       this.sql.append(condition);
/*     */     } else {
/* 515 */       replaceAll(this.sql, getKey(queryCondition.getConditionAt()), condition);
/*     */     } 
/* 517 */     return this;
/*     */   }
/*     */   
/*     */   public String getSqlCondition(List<QueryCondition> conditions) {
/* 521 */     if (CollUtil.isEmpty(conditions)) {
/* 522 */       return "";
/*     */     }
/* 524 */     StringBuilder sqlCond = new StringBuilder();
/* 525 */     for (QueryCondition queryCondition : conditions) {
/* 526 */       sqlCond.append(getSqlCondition(queryCondition));
/*     */     }
/* 528 */     return sqlCond.toString();
/*     */   }
/*     */   
/*     */   public String getSqlCondition(QueryCondition queryCondition) {
/* 532 */     return getSqlCondition(queryCondition.getTableAlias(), queryCondition.getColumnName(), queryCondition.getConditionSymbol(), queryCondition.getValue());
/*     */   }
/*     */   
/*     */   public String getSqlCondition(String tableAlias, String column, ConditionSymbol symbol, Object value) {
/* 536 */     if (column == null) {
/* 537 */       return "";
/*     */     }
/* 539 */     StringBuilder sb = new StringBuilder();
/* 540 */     if (symbol == ConditionSymbol.IS_NULL || symbol == ConditionSymbol.IS_NOT_NULL)
/* 541 */       return sb.append(" and ").append(getFullColumn(tableAlias, column)).append(symbol.getCode()).toString(); 
/* 542 */     if (value != null) {
/*     */       
/* 544 */       if (symbol == null) {
/* 545 */         symbol = ConditionSymbol.EQUAL;
/*     */       }
/*     */       
/* 548 */       sb.append(" and ").append(getFullColumn(tableAlias, column));
/* 549 */       if (value instanceof Collection) {
/*     */         
/* 551 */         if (symbol == ConditionSymbol.EQUAL) {
/* 552 */           sb.append(" in ");
/* 553 */         } else if (symbol == ConditionSymbol.UNEQUAL) {
/* 554 */           sb.append(" not in ");
/*     */         } else {
/* 556 */           throw new SqlBuildException("条件符号异常[symbol=" + symbol + "]");
/*     */         } 
/* 558 */         sb.append(getValues((Collection)value));
/*     */       } else {
/* 560 */         sb.append(symbol.getCode()).append(getValue(value));
/*     */       } 
/*     */     } 
/* 563 */     return sb.toString();
/*     */   }
/*     */   
/*     */   private String getFullColumn(String tableAlias, String column) {
/* 567 */     if (StrUtil.isEmpty(tableAlias)) {
/* 568 */       return checkName(column);
/*     */     }
/* 570 */     return checkName(tableAlias) + "." + checkName(column);
/*     */   }
/*     */   
/*     */   public com.amarsoft.rwa.engine.util.SqlBuilder clear(String key) {
/* 574 */     replaceAll(this.sql, getKey(key), "");
/* 575 */     return this;
/*     */   }
/*     */   
/*     */   public static IdParamType confirmIdParamType(String beginId, String endId) {
/* 579 */     if (StrUtil.isEmpty(beginId) && StrUtil.isEmpty(endId))
/* 580 */       return IdParamType.NULL; 
/* 581 */     if (StrUtil.isEmpty(beginId))
/* 582 */       return IdParamType.END; 
/* 583 */     if (StrUtil.isEmpty(endId))
/* 584 */       return IdParamType.BEGIN; 
/* 585 */     if (StrUtil.equals(beginId, endId)) {
/* 586 */       return IdParamType.SINGLE;
/*     */     }
/* 588 */     return IdParamType.FULL;
/*     */   }
/*     */ 
/*     */   
/*     */   public static TreeSet<String> scanParams(String sql) {
/* 593 */     if (StrUtil.isEmpty(sql)) {
/* 594 */       return null;
/*     */     }
/* 596 */     TreeSet<String> set = new TreeSet<>();
/* 597 */     String exp = "#\\{\\w+[\\.\\w+]*\\}";
/* 598 */     Pattern pattern = Pattern.compile(exp);
/* 599 */     Matcher matcher = pattern.matcher(sql);
/* 600 */     while (matcher.find()) {
/* 601 */       String group = matcher.group();
/* 602 */       set.add(group.substring(2, group.length() - 1));
/*     */     } 
/* 604 */     return set;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\SqlBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */