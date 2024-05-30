/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.convert.Convert;
/*     */ import cn.hutool.core.date.LocalDateTimeUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*     */ import com.amarsoft.rwa.engine.constant.RightNotation;
/*     */ import com.amarsoft.rwa.engine.constant.SortType;
/*     */ import com.amarsoft.rwa.engine.entity.NumberRange;
/*     */ import com.amarsoft.rwa.engine.exception.ParamConfigException;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import java.io.StringWriter;
/*     */ import java.lang.reflect.Field;
/*     */ import java.math.BigDecimal;
/*     */ import java.math.RoundingMode;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.sql.Date;
/*     */ import java.sql.PreparedStatement;
/*     */ import java.sql.SQLException;
/*     */ import java.sql.Timestamp;
/*     */ import java.text.DecimalFormat;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import java.util.regex.Pattern;
/*     */ import javax.validation.constraints.NotNull;
/*     */ import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
/*     */ import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;
/*     */ 
/*     */ public class DataUtils {
/*  42 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.DataUtils.class);
/*     */   
/*     */   public static Map<String, String> dataFormatMap;
/*     */   
/*     */   public static String defaultItemFormat;
/*     */   
/*     */   public static final String ISO_DATE_PATTERN = "yyyy-MM-dd";
/*     */   
/*     */   public static final String ISO_TIME_PATTERN = "HH:mm:ss";
/*     */   
/*     */   public static final String ISO_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
/*     */   
/*     */   public static final String ISO_FULL_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss.SSS";
/*  55 */   public static final Pattern NUMBER_PATTERN = Pattern.compile("^[-+]?\\d{1,24}(,\\d{3}){0,8}(\\.\\d{1,16})?%?$");
/*     */   
/*     */   public static String generateKey(String... strs) {
/*  58 */     if (strs.length == 0) {
/*  59 */       throw new RuntimeException("参数不能为空");
/*     */     }
/*  61 */     return generateKey(CollUtil.toList((Object[])strs));
/*     */   }
/*     */   
/*     */   public static String generateKey(List<String> keys) {
/*  65 */     if (CollUtil.isEmpty(keys)) {
/*  66 */       throw new RuntimeException("参数不能为空");
/*     */     }
/*  68 */     StringBuilder sb = new StringBuilder();
/*  69 */     for (String s : keys) {
/*  70 */       if (StrUtil.isEmpty(s)) {
/*     */         continue;
/*     */       }
/*  73 */       sb.append(s).append(":");
/*     */     } 
/*  75 */     if (sb.length() > 1) {
/*  76 */       sb.delete(sb.length() - 1, sb.length());
/*     */     }
/*  78 */     return sb.toString();
/*     */   }
/*     */   
/*     */   public static Map<String, String> createSimpleMap(String... params) {
/*  82 */     if (params == null || params.length % 2 != 0) {
/*  83 */       throw new RuntimeException("参数异常，生成简单Map必须为1个key对1个value；params=" + ListUtil.toList(params));
/*     */     }
/*  85 */     int size = params.length / 2;
/*  86 */     Map<String, String> map = new HashMap<>();
/*  87 */     for (int i = 0; i < size; i++) {
/*  88 */       map.put(params[i * 2], params[i * 2 + 1]);
/*     */     }
/*  90 */     return map;
/*     */   }
/*     */   
/*     */   public static BigDecimal toBigDecimal(Object value) {
/*  94 */     if (value == null) return null; 
/*  95 */     if (value instanceof Number) {
/*  96 */       return NumberUtil.toBigDecimal((Number)value);
/*     */     }
/*  98 */     if (value instanceof CharSequence) {
/*  99 */       String v = value.toString();
/* 100 */       if (StrUtil.isEmpty(v)) {
/* 101 */         return null;
/*     */       }
/* 103 */       if (NUMBER_PATTERN.matcher(v).find()) {
/* 104 */         boolean p = v.endsWith("%");
/* 105 */         v = v.replaceAll(",", "").replaceAll("%", "");
/* 106 */         BigDecimal b = null;
/*     */         try {
/* 108 */           b = new BigDecimal(v);
/* 109 */         } catch (Exception e) {
/* 110 */           return null;
/*     */         } 
/*     */ 
/*     */         
/* 114 */         if (p) {
/* 115 */           b = b.divide(new BigDecimal(100)).stripTrailingZeros();
/*     */         }
/*     */         
/* 118 */         return b;
/*     */       } 
/*     */     } 
/* 121 */     return null;
/*     */   }
/*     */   
/*     */   public static int getLength(BigDecimal value) {
/* 125 */     if (value == null) {
/* 126 */       return 0;
/*     */     }
/* 128 */     return getLength(Long.valueOf(value.longValue()));
/*     */   }
/*     */   
/*     */   public static int getLength(Long value) {
/* 132 */     if (value == null) {
/* 133 */       return 0;
/*     */     }
/* 135 */     return value.toString().length();
/*     */   }
/*     */   
/*     */   public static boolean validateNumberLength(BigDecimal value, int maxLength) {
/* 139 */     return (getLength(value) > maxLength);
/*     */   }
/*     */   
/*     */   public static Object getValue(Map<String, Object> data, ICodeEnum code) {
/* 143 */     return getValue(data, code.getCode());
/*     */   }
/*     */   
/*     */   public static Object getValue(Map<String, Object> data, String code) {
/* 147 */     if (CollUtil.isEmpty(data) || StrUtil.isEmpty(code)) {
/* 148 */       return null;
/*     */     }
/*     */     
/* 151 */     Object obj = data.get(code);
/* 152 */     if (obj != null) {
/* 153 */       return obj;
/*     */     }
/*     */     
/* 156 */     obj = data.get(code.toUpperCase());
/* 157 */     if (obj != null) {
/* 158 */       return obj;
/*     */     }
/*     */     
/* 161 */     return data.get(code.toLowerCase());
/*     */   }
/*     */   
/*     */   public static String getString(Map<String, Object> data, ICodeEnum code) {
/* 165 */     return getString(data, code.getCode());
/*     */   }
/*     */   
/*     */   public static String getString(Map<String, Object> data, String code) {
/* 169 */     return Convert.toStr(getValue(data, code));
/*     */   }
/*     */   
/*     */   public static String getString(Map<String, Object> data, ICodeEnum code, String defaultValue) {
/* 173 */     String value = getString(data, code);
/* 174 */     if (StrUtil.isEmpty(value)) {
/* 175 */       return defaultValue;
/*     */     }
/* 177 */     return value;
/*     */   }
/*     */   
/*     */   public static Date getDate(Map<String, Object> data, ICodeEnum code) {
/* 181 */     return getDate(data, code.getCode());
/*     */   }
/*     */   
/*     */   public static Date getDate(Map<String, Object> data, String code) {
/* 185 */     return Convert.toDate(getValue(data, code));
/*     */   }
/*     */   
/*     */   public static Date getSqlDate(Map<String, Object> data, String code) {
/* 189 */     return convert2SqlDate(Convert.toDate(getValue(data, code)));
/*     */   }
/*     */   
/*     */   public static Timestamp getTimestamp(Map<String, Object> data, String code) {
/* 193 */     Object value = getValue(data, code);
/* 194 */     if (value instanceof Timestamp) {
/* 195 */       return (Timestamp)value;
/*     */     }
/* 197 */     if (value instanceof Date) {
/* 198 */       Date date = (Date)value;
/* 199 */       return new Timestamp(date.getTime());
/*     */     } 
/* 201 */     if (value instanceof LocalDateTime) {
/* 202 */       return Timestamp.valueOf((LocalDateTime)value);
/*     */     }
/* 204 */     return null;
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getStringProperty(Object bean, String expression) {
/* 209 */     String value = (String)BeanUtil.getProperty(bean, expression);
/* 210 */     return value;
/*     */   }
/*     */   
/*     */   public static BigDecimal getBigDecimalProperty(Object bean, String expression) {
/* 214 */     BigDecimal value = (BigDecimal)BeanUtil.getProperty(bean, expression);
/* 215 */     return value;
/*     */   }
/*     */   
/*     */   public static void setString(Map<String, Object> data, ICodeEnum code, String value) {
/* 219 */     setString(data, code.getCode(), value);
/*     */   }
/*     */   
/*     */   public static void setString(Map<String, Object> data, ICodeEnum code, ICodeEnum value) {
/* 223 */     setString(data, code.getCode(), value.getCode());
/*     */   }
/*     */   
/*     */   public static void setString(Map<String, Object> data, String code, String value) {
/* 227 */     data.put(code.toUpperCase(), value);
/*     */   }
/*     */   
/*     */   public static void setString(Map<String, Object> toData, Map<String, Object> fromData, ICodeEnum code) {
/* 231 */     setString(toData, code, getString(fromData, code));
/*     */   }
/*     */   
/*     */   public static BigDecimal getBigDecimal(Map<String, Object> data, ICodeEnum code) {
/* 235 */     return getBigDecimal(data, code.getCode());
/*     */   }
/*     */   
/*     */   public static BigDecimal getBigDecimal(Map<String, Object> data, String code) {
/* 239 */     return toBigDecimal(getValue(data, code));
/*     */   }
/*     */   
/*     */   public static BigDecimal getBigDecimal(Map<String, Object> data, ICodeEnum code, BigDecimal defaultValue) {
/* 243 */     BigDecimal value = getBigDecimal(data, code);
/* 244 */     if (value == null) {
/* 245 */       return defaultValue;
/*     */     }
/* 247 */     return value;
/*     */   }
/*     */   
/*     */   public static void setBigDecimal(Map<String, Object> data, ICodeEnum code, BigDecimal value) {
/* 251 */     setBigDecimal(data, code.getCode(), value);
/*     */   }
/*     */   
/*     */   public static void setBigDecimal(Map<String, Object> data, String code, BigDecimal value) {
/* 255 */     data.put(code.toUpperCase(), value);
/*     */   }
/*     */   
/*     */   public static void setInt(Map<String, Object> data, ICodeEnum code, Integer value) {
/* 259 */     setInt(data, code.getCode(), value);
/*     */   }
/*     */   
/*     */   public static void setInt(Map<String, Object> data, String code, Integer value) {
/* 263 */     data.put(code.toUpperCase(), value);
/*     */   }
/*     */   
/*     */   public static void setBigDecimal(Map<String, Object> toData, Map<String, Object> fromData, ICodeEnum code) {
/* 267 */     setBigDecimal(toData, code, getBigDecimal(fromData, code));
/*     */   }
/*     */   
/*     */   public static Integer getInt(Map<String, Object> data, ICodeEnum code) {
/* 271 */     BigDecimal b = getBigDecimal(data, code);
/* 272 */     if (b == null) {
/* 273 */       return null;
/*     */     }
/* 275 */     return Integer.valueOf(b.intValue());
/*     */   }
/*     */   
/*     */   public static Integer getInt(Map<String, Object> data, String code) {
/* 279 */     BigDecimal b = getBigDecimal(data, code);
/* 280 */     if (b == null) {
/* 281 */       return null;
/*     */     }
/* 283 */     return Integer.valueOf(b.intValue());
/*     */   }
/*     */   
/*     */   public static void remove(Map<String, Object> data, ICodeEnum code) {
/* 287 */     data.remove(code.getCode().toUpperCase());
/*     */   }
/*     */   
/*     */   public static boolean codeEquals(String code, ICodeEnum codeEnum) {
/* 291 */     return codeEnum.getCode().toUpperCase().equals(code);
/*     */   }
/*     */   
/*     */   public static void mappingValue(Map<String, Object> data, ICodeEnum source, ICodeEnum target) {
/* 295 */     mappingValue(data, source.getCode(), target.getCode());
/*     */   }
/*     */   
/*     */   public static void mappingValue(Map<String, Object> data, String source, String target) {
/* 299 */     Object value = data.get(source.toUpperCase());
/* 300 */     if (isNumber(value)) {
/* 301 */       setBigDecimal(data, target, toBigDecimal(value));
/*     */     } else {
/* 303 */       setString(data, target, Convert.toStr(value));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static Map<String, String> setMapping(Map<String, String> mappings, String source) {
/* 308 */     if (StrUtil.isEmpty(source)) {
/* 309 */       return mappings;
/*     */     }
/* 311 */     return setMapping(mappings, source.toUpperCase(), StrUtil.toCamelCase(source.toLowerCase()));
/*     */   }
/*     */   
/*     */   public static Map<String, String> setMapping(Map<String, String> mappings, String source, String target) {
/* 315 */     if (mappings == null) {
/* 316 */       mappings = new LinkedHashMap<>();
/*     */     }
/*     */     
/* 319 */     if (StrUtil.isEmpty(source) || StrUtil.isEmpty(target)) {
/* 320 */       return mappings;
/*     */     }
/* 322 */     mappings.put(source, target);
/* 323 */     return mappings;
/*     */   }
/*     */   
/*     */   public static <T> void setValue(T object, String source, String value) {
/* 327 */     setValue(object, source, null, value);
/*     */   }
/*     */   
/*     */   public static <T> void setValue(T object, String source, String target, String value) {
/* 331 */     if (StrUtil.isEmpty(target))
/*     */     {
/*     */       
/* 334 */       target = StrUtil.toCamelCase(source.toLowerCase());
/*     */     }
/* 336 */     Field field = ReflectUtil.getField(object.getClass(), target);
/* 337 */     if (field == null) {
/* 338 */       throw new RuntimeException("参数版本配置异常， 没有[" + source + "]对应的字段[" + target + "]");
/*     */     }
/*     */     
/* 341 */     if (field.getType() == BigDecimal.class) {
/* 342 */       BeanUtil.setProperty(object, target, toBigDecimal(value));
/*     */     } else {
/* 344 */       BeanUtil.setProperty(object, target, value);
/*     */     } 
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public static <T> T dataMapping(Class<T> c, Map<String, Object> data, Map<String, String> mappings) throws InstantiationException, IllegalAccessException {
/* 351 */     T t = c.newInstance();
/* 352 */     for (Map.Entry<String, String> mapping : mappings.entrySet()) {
/* 353 */       String target = mapping.getValue();
/* 354 */       if (StrUtil.isEmpty(target))
/*     */       {
/* 356 */         target = StrUtil.toCamelCase(mapping.getKey());
/*     */       }
/* 358 */       BeanUtil.setProperty(t, target, BeanUtil.getProperty(data, mapping.getKey()));
/*     */     } 
/* 360 */     return t;
/*     */   }
/*     */   
/*     */   public static <T> void setProperty(T t, Map<String, Object> data, String name) {
/* 364 */     String target = StrUtil.toCamelCase(name.toLowerCase());
/* 365 */     setProperty(t, data, target, name);
/*     */   }
/*     */   
/*     */   public static <T> void setProperty(T t, Map<String, Object> data, String target, String from) {
/* 369 */     BeanUtil.setProperty(t, target, BeanUtil.getProperty(data, from));
/*     */   }
/*     */   
/*     */   public static boolean isNumber(Object obj) {
/* 373 */     if (obj instanceof Character || obj instanceof String || obj instanceof Date || obj instanceof LocalDate || obj instanceof java.time.LocalTime || obj instanceof LocalDateTime)
/*     */     {
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */       
/* 380 */       return false;
/*     */     }
/*     */     
/* 383 */     return obj instanceof Number;
/*     */   }
/*     */   
/*     */   public static int compare(Object o1, Object o2, SortType sortType, NullSortType nullSortType) {
/* 387 */     if (o1 == o2) {
/* 388 */       return 0;
/*     */     }
/* 390 */     int r = 0;
/* 391 */     switch (null.$SwitchMap$com$amarsoft$rwa$engine$constant$NullSortType[nullSortType.ordinal()]) {
/*     */       
/*     */       case 1:
/* 394 */         r = -compareOfNull(o1, o2);
/* 395 */         if (r != 0) {
/* 396 */           return r;
/*     */         }
/*     */         break;
/*     */       case 2:
/* 400 */         r = compareOfNull(o1, o2);
/* 401 */         if (r != 0) {
/* 402 */           return r;
/*     */         }
/*     */         break;
/*     */       case 3:
/* 406 */         r = -compareOfNull(o1, o2);
/*     */         break;
/*     */       case 4:
/* 409 */         r = compareOfNull(o1, o2);
/*     */         break;
/*     */     } 
/*     */     
/* 413 */     if (r == 0) {
/* 414 */       if (isNumber(o1) || isNumber(o2)) {
/* 415 */         r = CompareUtil.compare(toBigDecimal(o1), toBigDecimal(o2));
/*     */       } else {
/* 417 */         r = CompareUtil.compare(Convert.toStr(o1), Convert.toStr(o2));
/*     */       } 
/*     */     }
/*     */     
/* 421 */     if (sortType == SortType.DESC) {
/* 422 */       r = -r;
/*     */     }
/* 424 */     return r;
/*     */   }
/*     */   
/*     */   public static int compareOfNull(Object o1, Object o2) {
/* 428 */     if (o1 == o2) {
/* 429 */       return 0;
/*     */     }
/*     */     
/* 432 */     if (o1 == null) {
/* 433 */       return 1;
/*     */     }
/* 435 */     if (o2 == null) {
/* 436 */       return -1;
/*     */     }
/*     */     
/* 439 */     return 0;
/*     */   }
/*     */   
/*     */   public static void setPstBigDecimal(Object item, String expression, PreparedStatement ps, int index) throws SQLException {
/* 443 */     if (item == null) {
/* 444 */       setPstBigDecimal(ps, index, null);
/*     */     } else {
/* 446 */       setPstBigDecimal(ps, index, (BigDecimal)BeanUtil.getProperty(item, expression));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setPstBigDecimal(PreparedStatement ps, int index, BigDecimal value) throws SQLException {
/* 451 */     if (value == null) {
/* 452 */       ps.setNull(index, 3);
/*     */     } else {
/*     */       
/* 455 */       ps.setBigDecimal(index, value.setScale(16, RoundingMode.HALF_UP).stripTrailingZeros());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setPstInteger(PreparedStatement ps, int index, Integer value) throws SQLException {
/* 460 */     if (value == null) {
/* 461 */       ps.setNull(index, 3);
/*     */     } else {
/* 463 */       ps.setInt(index, value.intValue());
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setPstBigDecimalByColumn(Object item, String expression, PreparedStatement ps, int index) throws SQLException {
/* 468 */     setPstBigDecimal(item, StrUtil.toCamelCase(expression.toLowerCase()), ps, index);
/*     */   }
/*     */   
/*     */   public static void setPstString(Object item, String expression, PreparedStatement ps, int index) throws SQLException {
/* 472 */     if (item == null) {
/* 473 */       setPstString(ps, index, null);
/*     */     } else {
/* 475 */       setPstString(ps, index, (String)BeanUtil.getProperty(item, expression));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setPstString(PreparedStatement ps, int index, String value) throws SQLException {
/* 480 */     if (StrUtil.isEmpty(value)) {
/* 481 */       ps.setNull(index, 12);
/*     */     } else {
/* 483 */       ps.setString(index, value);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static void setPstStringByColumn(Object item, String expression, PreparedStatement ps, int index) throws SQLException {
/* 488 */     setPstString(item, StrUtil.toCamelCase(expression.toLowerCase()), ps, index);
/*     */   }
/*     */   
/*     */   public static Map<String, String> getServerInfo() {
/*     */     try {
/* 493 */       InetAddress address = InetAddress.getLocalHost();
/* 494 */       return createSimpleMap(new String[] { "ip", address.getHostAddress(), "name", address.getHostName() });
/* 495 */     } catch (UnknownHostException e) {
/* 496 */       log.error("获取服务器信息异常", e);
/*     */       
/* 498 */       return null;
/*     */     } 
/*     */   }
/*     */   public static LocalDate toLocalDate(Date date) {
/* 502 */     return date.toLocalDate();
/*     */   }
/*     */   
/*     */   public static LocalDate toLocalDate(Date date) {
/* 506 */     if (date instanceof Date) {
/* 507 */       return ((Date)date).toLocalDate();
/*     */     }
/* 509 */     return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
/*     */   }
/*     */   
/*     */   public static Date convert2SqlDate(Date date) {
/* 513 */     return new Date(date.getTime());
/*     */   }
/*     */   
/*     */   public static Date convert2SqlDate(LocalDate date) {
/* 517 */     return Date.valueOf(date);
/*     */   }
/*     */   
/*     */   public static BigDecimal timeBetween(Date startTime, Date endTime) {
/* 521 */     if (startTime == null || endTime == null) {
/* 522 */       return null;
/*     */     }
/* 524 */     return timeBetween(LocalDateTimeUtil.of(startTime), LocalDateTimeUtil.of(endTime));
/*     */   }
/*     */   
/*     */   public static BigDecimal timeBetween(LocalDateTime startTime, LocalDateTime endTime) {
/* 528 */     Duration duration = Duration.between(startTime, endTime);
/* 529 */     return BigDecimal.valueOf(duration.getSeconds()).add(BigDecimal.valueOf(duration.getNano() / 1.0E9D));
/*     */   }
/*     */   
/*     */   public static long timeBt(LocalDateTime start, LocalDateTime end) {
/* 533 */     return end.toInstant(ZoneOffset.of("+8")).toEpochMilli() - start.toInstant(ZoneOffset.of("+8")).toEpochMilli();
/*     */   }
/*     */   
/*     */   public static <T extends Date> T maxDate(T d1, T d2) {
/* 537 */     if (d1 == null) {
/* 538 */       return d2;
/*     */     }
/* 540 */     if (d2 == null) {
/* 541 */       return d1;
/*     */     }
/* 543 */     if (d1.after((Date)d2)) {
/* 544 */       return d1;
/*     */     }
/* 546 */     return d2;
/*     */   }
/*     */ 
/*     */   
/*     */   public static <T extends Date> T minDate(T d1, T d2) {
/* 551 */     if (d1 == null) {
/* 552 */       return d2;
/*     */     }
/* 554 */     if (d2 == null) {
/* 555 */       return d1;
/*     */     }
/* 557 */     if (d1.after((Date)d2)) {
/* 558 */       return d2;
/*     */     }
/* 560 */     return d1;
/*     */   }
/*     */ 
/*     */   
/*     */   public static BigDecimal add(Object o1, Object o2, String column) {
/* 565 */     BigDecimal b1 = BigDecimal.ZERO;
/* 566 */     if (o1 != null) {
/* 567 */       b1 = toBigDecimal(BeanUtil.getProperty(o1, column));
/*     */     }
/* 569 */     BigDecimal b2 = BigDecimal.ZERO;
/* 570 */     if (o2 != null) {
/* 571 */       b2 = toBigDecimal(BeanUtil.getProperty(o2, column));
/*     */     }
/* 573 */     return NumberUtil.add(b1, b2);
/*     */   }
/*     */   
/*     */   public static Duration getDuration(long nano) {
/* 577 */     long seconds = nano / 1000L;
/* 578 */     long nanoAdjustment = nano % 1000L * 1000000L;
/* 579 */     return Duration.ofSeconds(seconds, nanoAdjustment);
/*     */   }
/*     */   
/*     */   public static Duration getDuration(Date start, Date end) {
/* 583 */     return Duration.between(LocalDateTimeUtil.of(start), LocalDateTimeUtil.of(end));
/*     */   }
/*     */   
/*     */   public static String stackTraceToString(Throwable t) {
/* 587 */     if (t == null) {
/* 588 */       return null;
/*     */     }
/* 590 */     StringWriter stringWriter = new StringWriter();
/* 591 */     t.printStackTrace(new PrintWriter(stringWriter, true));
/* 592 */     return stringWriter.getBuffer().toString();
/*     */   }
/*     */ 
/*     */   
/*     */   public static String encrypt(String text, String password) {
/* 597 */     StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
/*     */     
/* 599 */     EnvironmentStringPBEConfig config = new EnvironmentStringPBEConfig();
/* 600 */     config.setAlgorithm("PBEWithMD5AndDES");
/* 601 */     config.setPassword(password);
/* 602 */     encryptor.setConfig((PBEConfig)config);
/*     */     
/* 604 */     return encryptor.encrypt(text);
/*     */   }
/*     */   
/*     */   public static Map<String, BigDecimal> convert2NumberMap(Map<String, Object> map) {
/* 608 */     if (CollUtil.isEmpty(map)) {
/* 609 */       return null;
/*     */     }
/* 611 */     Map<String, BigDecimal> target = new HashMap<>();
/* 612 */     for (String key : map.keySet()) {
/*     */       try {
/* 614 */         BigDecimal bd = toBigDecimal(map.get(key));
/* 615 */         target.put(key, bd);
/* 616 */       } catch (Exception e) {
/* 617 */         log.error("数值转换异常[key=" + key + "][map=" + map + "]", e);
/*     */       } 
/*     */     } 
/* 620 */     if (CollUtil.isEmpty(target)) {
/* 621 */       return null;
/*     */     }
/* 623 */     return target;
/*     */   }
/*     */   
/*     */   public static Map<String, String> convert2StringMap(Map<String, Object> map) {
/* 627 */     if (CollUtil.isEmpty(map)) {
/* 628 */       return null;
/*     */     }
/* 630 */     Map<String, String> target = new HashMap<>();
/* 631 */     for (String key : map.keySet()) {
/*     */       try {
/* 633 */         target.put(key, Convert.toStr(map.get(key)));
/* 634 */       } catch (Exception e) {
/* 635 */         log.error("数值转换异常[key=" + key + "][map=" + map + "]", e);
/*     */       } 
/*     */     } 
/* 638 */     if (CollUtil.isEmpty(target)) {
/* 639 */       return null;
/*     */     }
/* 641 */     return target;
/*     */   }
/*     */   
/*     */   public static Map<String, Object> getFirstItem(Object list) {
/* 645 */     if (list == null) {
/* 646 */       return null;
/*     */     }
/* 648 */     List<Map<String, Object>> itemList = (List<Map<String, Object>>)list;
/* 649 */     if (CollUtil.isEmpty(itemList)) {
/* 650 */       return null;
/*     */     }
/* 652 */     return itemList.get(0);
/*     */   }
/*     */   
/*     */   public static BigDecimal nvl(BigDecimal value, @NotNull BigDecimal defaultValue) {
/* 656 */     if (value == null) {
/* 657 */       return defaultValue;
/*     */     }
/* 659 */     return value;
/*     */   }
/*     */ 
/*     */   
/*     */   public static int nvl(Integer value, @NotNull int defaultValue) {
/* 664 */     if (value == null) {
/* 665 */       return defaultValue;
/*     */     }
/* 667 */     return value.intValue();
/*     */   }
/*     */ 
/*     */   
/*     */   public static Map<String, Object> createSqlMap(String sql) {
/* 672 */     Map<String, Object> map = new HashMap<>();
/* 673 */     map.put("sql", sql);
/* 674 */     return map;
/*     */   }
/*     */   
/*     */   public static <T> List<T> getList(Collection<T> collection) {
/* 678 */     List<T> list = new ArrayList<>();
/* 679 */     list.addAll(collection);
/* 680 */     return list;
/*     */   }
/*     */   
/*     */   public static int getCollSize(Collection... collections) {
/* 684 */     int size = 0;
/* 685 */     for (Collection collection : collections) {
/* 686 */       if (!CollUtil.isEmpty(collection))
/*     */       {
/*     */         
/* 689 */         size += collection.size(); } 
/*     */     } 
/* 691 */     return size;
/*     */   }
/*     */   
/*     */   public static Timestamp nowTimestamp() {
/* 695 */     return new Timestamp((new Date()).getTime());
/*     */   }
/*     */ 
/*     */   
/*     */   public static String getRoundName(BigDecimal bd) {
/* 700 */     return getRoundName(bd, 6);
/*     */   }
/*     */   
/*     */   public static String getRoundName(BigDecimal bd, int scale) {
/* 704 */     if (bd == null) {
/* 705 */       return null;
/*     */     }
/* 707 */     return round(bd, scale).stripTrailingZeros().toPlainString();
/*     */   }
/*     */   
/*     */   public static BigDecimal round(BigDecimal bd, int scale) {
/* 711 */     if (bd == null) {
/* 712 */       return null;
/*     */     }
/* 714 */     return bd.setScale(scale, 4);
/*     */   }
/*     */   
/*     */   public static boolean isInList(String value, String... array) {
/* 718 */     if (StrUtil.isEmpty(value) || array == null) {
/* 719 */       throw new RuntimeException("判断字符串不能为空！");
/*     */     }
/* 721 */     for (String str : array) {
/* 722 */       if (value.equals(str)) {
/* 723 */         return true;
/*     */       }
/*     */     } 
/* 726 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isInList(String value, ICodeEnum... codes) {
/* 730 */     if (StrUtil.isEmpty(value) || codes == null) {
/* 731 */       throw new RuntimeException("判断字符串不能为空！");
/*     */     }
/* 733 */     for (ICodeEnum code : codes) {
/* 734 */       if (value.equals(code.getCode())) {
/* 735 */         return true;
/*     */       }
/*     */     } 
/* 738 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isInList(String value, String content) {
/* 742 */     return isInList(value, content.split(","));
/*     */   }
/*     */   
/*     */   public static boolean isInList(String value, @NotNull List<String> list) {
/* 746 */     return list.contains(value);
/*     */   }
/*     */   
/*     */   public static <T extends ICodeEnum> boolean isInList(T value, ICodeEnum... codes) {
/* 750 */     for (ICodeEnum code : codes) {
/* 751 */       if (Objects.equals(code, value)) {
/* 752 */         return true;
/*     */       }
/*     */     } 
/* 755 */     return false;
/*     */   }
/*     */   
/*     */   public static String formatBigDecimal(BigDecimal b, String pattern) {
/* 759 */     DecimalFormat decimalFormat = new DecimalFormat(pattern);
/* 760 */     decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
/* 761 */     return decimalFormat.format(b);
/*     */   }
/*     */   
/*     */   public static void putCountMap(Map<String, Integer> map, String key) {
/* 765 */     if (map.get(key) == null) {
/* 766 */       map.put(key, Integer.valueOf(1));
/*     */     } else {
/* 768 */       map.put(key, Integer.valueOf(((Integer)map.get(key)).intValue() + 1));
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <K, V> void add2ListMap(Map<K, List<V>> map, V value, K key) {
/* 773 */     List<V> list = map.get(key);
/* 774 */     if (CollUtil.isEmpty(list)) {
/* 775 */       list = new ArrayList<>();
/* 776 */       map.put(key, list);
/*     */     } 
/* 778 */     list.add(value);
/*     */   }
/*     */   
/*     */   public static <K, V> List<V> getList(Map<K, List<V>> map, K key) {
/* 782 */     if (CollUtil.isEmpty(map)) {
/* 783 */       return null;
/*     */     }
/* 785 */     return map.get(key);
/*     */   }
/*     */   
/*     */   public static <K, V> void add2SetMap(Map<K, Set<V>> map, V value, K key) {
/* 789 */     Set<V> list = map.get(key);
/* 790 */     if (CollUtil.isEmpty(list)) {
/* 791 */       list = new TreeSet<>();
/* 792 */       map.put(key, list);
/*     */     } 
/* 794 */     list.add(value);
/*     */   }
/*     */   
/*     */   public static <K, V> Set<V> getSet(Map<K, Set<V>> map, K key) {
/* 798 */     if (CollUtil.isEmpty(map)) {
/* 799 */       return null;
/*     */     }
/* 801 */     return map.get(key);
/*     */   }
/*     */   
/*     */   public static boolean isDate(Object value) {
/* 805 */     if (value instanceof Date || value instanceof Date || value instanceof LocalDate) {
/* 806 */       return true;
/*     */     }
/* 808 */     return false;
/*     */   }
/*     */   
/*     */   public static boolean isTime(Object value) {
/* 812 */     if (value instanceof Timestamp || value instanceof LocalDateTime) {
/* 813 */       return true;
/*     */     }
/* 815 */     return false;
/*     */   }
/*     */   
/*     */   public static LocalDate toLocalDate(Object value) {
/* 819 */     if (value instanceof Date)
/* 820 */       return toLocalDate((Date)value); 
/* 821 */     if (value instanceof Date)
/* 822 */       return toLocalDate((Date)value); 
/* 823 */     if (value instanceof LocalDate) {
/* 824 */       return (LocalDate)value;
/*     */     }
/* 826 */     return null;
/*     */   }
/*     */   
/*     */   public static LocalDateTime convert2LocalDateTime(Timestamp timestamp) {
/* 830 */     return timestamp.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
/*     */   }
/*     */   
/*     */   public static LocalDateTime toLocalDateTime(Object value) {
/* 834 */     if (value instanceof LocalDateTime) {
/* 835 */       return (LocalDateTime)value;
/*     */     }
/* 837 */     if (value instanceof Timestamp) {
/* 838 */       return convert2LocalDateTime((Timestamp)value);
/*     */     }
/* 840 */     if (value instanceof CharSequence) {
/* 841 */       return toLocalDateTime(value.toString());
/*     */     }
/* 843 */     return null;
/*     */   }
/*     */   
/*     */   public static LocalDateTime toLocalDateTime(String value) {
/* 847 */     value = value.trim();
/*     */     
/*     */     try {
/* 850 */       if (value.length() <= 10) {
/* 851 */         value = value.replace("/", "");
/* 852 */         value = value.replace("-", "");
/* 853 */         value = value.replace("\\", "");
/* 854 */         return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyyMMdd"));
/* 855 */       }  if (value.length() == 19) {
/*     */         
/* 857 */         if (value.contains("-"))
/* 858 */           return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")); 
/* 859 */         if (value.contains("/")) {
/* 860 */           return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
/*     */         }
/* 862 */       } else if (value.length() == 23) {
/* 863 */         if (value.contains("-"))
/* 864 */           return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")); 
/* 865 */         if (value.contains("/")) {
/* 866 */           return LocalDateTime.parse(value, DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSS"));
/*     */         }
/*     */       } 
/* 869 */       return null;
/* 870 */     } catch (Exception e) {
/* 871 */       return null;
/*     */     } 
/*     */   }
/*     */   
/*     */   public static String toBigDecimalString(BigDecimal b) {
/* 876 */     return b.stripTrailingZeros().toPlainString();
/*     */   }
/*     */   
/*     */   public static NumberRange parseNumberRange(String content) {
/* 880 */     content = content.trim();
/* 881 */     String[] config = content.substring(1, content.length() - 1).split(",");
/* 882 */     if (config.length != 2) {
/* 883 */       throw new ParamConfigException("值域范围非法配置[1], content=" + content);
/*     */     }
/* 885 */     NumberRange range = new NumberRange();
/*     */     
/* 887 */     range.setLeft((LeftNotation)EnumUtils.getEnumByCode(content.substring(0, 1), LeftNotation.class));
/* 888 */     range.setRight((RightNotation)EnumUtils.getEnumByCode(content.substring(content.length() - 1), RightNotation.class));
/*     */     
/* 890 */     String ln = config[0].trim();
/* 891 */     if (!StrUtil.equals("-inf", ln.toLowerCase())) {
/*     */       
/* 893 */       range.setMin(toBigDecimal(ln));
/* 894 */       if (range.getMin() == null) {
/* 895 */         throw new ParamConfigException("左区间数值配置异常，content=" + content);
/*     */       }
/*     */     } 
/*     */     
/* 899 */     String rn = config[1].trim();
/* 900 */     if (!StrUtil.equals("+inf", rn.toLowerCase())) {
/*     */       
/* 902 */       range.setMax(toBigDecimal(rn));
/* 903 */       if (range.getMax() == null) {
/* 904 */         throw new ParamConfigException("右区间数值配置异常，content=" + content);
/*     */       }
/*     */     } 
/* 907 */     if (range.getMin() == null && range.getMax() == null) {
/* 908 */       throw new ParamConfigException("区间值不能都为无穷，content=" + content);
/*     */     }
/* 910 */     return range;
/*     */   }
/*     */   
/*     */   public static <V> void put2Map(Map<String, Map<String, V>> map, V value, String key1, String key2) {
/* 914 */     Map<String, V> nextMap = map.get(key1);
/* 915 */     if (nextMap == null) {
/* 916 */       nextMap = new HashMap<>();
/* 917 */       map.put(key1, nextMap);
/*     */     } 
/* 919 */     nextMap.put(key2, value);
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\DataUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */