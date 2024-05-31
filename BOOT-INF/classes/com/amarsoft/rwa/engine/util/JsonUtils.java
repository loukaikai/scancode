/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.util;
/*     */ import com.fasterxml.jackson.core.JsonParser;
/*     */ import com.fasterxml.jackson.core.JsonProcessingException;
/*     */ import com.fasterxml.jackson.core.type.TypeReference;
/*     */ import com.fasterxml.jackson.databind.DeserializationFeature;
/*     */ import com.fasterxml.jackson.databind.JavaType;
/*     */ import com.fasterxml.jackson.databind.JsonDeserializer;
/*     */ import com.fasterxml.jackson.databind.JsonSerializer;
/*     */ import com.fasterxml.jackson.databind.Module;
/*     */ import com.fasterxml.jackson.databind.ObjectMapper;
/*     */ import com.fasterxml.jackson.databind.SerializationFeature;
/*     */ import com.fasterxml.jackson.databind.type.CollectionType;
/*     */ import com.fasterxml.jackson.databind.type.MapLikeType;
/*     */ import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
/*     */ import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
/*     */ import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
/*     */ import java.io.IOException;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.time.LocalDate;
/*     */ import java.time.LocalDateTime;
/*     */ import java.time.LocalTime;
/*     */ import java.time.format.DateTimeFormatter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class JsonUtils {
/*  33 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.util.JsonUtils.class);
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*  39 */   public static final ObjectMapper objectMapper = new ObjectMapper();
/*     */   static {
/*  41 */     objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
/*     */     
/*  43 */     JavaTimeModule javaTimeModule = new JavaTimeModule();
/*     */     
/*  45 */     DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
/*  46 */     javaTimeModule.addSerializer(LocalDateTime.class, (JsonSerializer)new LocalDateTimeSerializer(dateTimeFormatter));
/*  47 */     javaTimeModule.addDeserializer(LocalDateTime.class, (JsonDeserializer)new LocalDateTimeDeserializer(dateTimeFormatter));
/*     */     
/*  49 */     DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
/*  50 */     javaTimeModule.addSerializer(LocalDate.class, (JsonSerializer)new LocalDateSerializer(dateFormatter));
/*  51 */     javaTimeModule.addDeserializer(LocalDate.class, (JsonDeserializer)new LocalDateDeserializer(dateFormatter));
/*     */     
/*  53 */     DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss.SSS");
/*  54 */     javaTimeModule.addSerializer(LocalTime.class, (JsonSerializer)new LocalTimeSerializer(timeFormatter));
/*  55 */     javaTimeModule.addDeserializer(LocalTime.class, (JsonDeserializer)new LocalTimeDeserializer(timeFormatter));
/*     */ 
/*     */     
/*  58 */     javaTimeModule.addSerializer(Date.class, (JsonSerializer)new DateSerializer(Boolean.valueOf(false), new SimpleDateFormat("yyyy-MM-dd")));
/*  59 */     javaTimeModule.addDeserializer(Date.class, (JsonDeserializer)new DateDeserializers.DateDeserializer());
/*     */ 
/*     */     
/*  62 */     objectMapper.registerModule((Module)javaTimeModule);
/*     */ 
/*     */     
/*  65 */     objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
/*     */     
/*  67 */     objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
/*     */     
/*  69 */     objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
/*     */     
/*  71 */     objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, true);
/*  72 */     objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
/*     */   }
/*     */   
/*     */   public static ObjectNode createObjectNode() {
/*  76 */     return objectMapper.createObjectNode();
/*     */   }
/*     */   
/*     */   public static Map<String, Object> convertJson2Map(String jsonStr) {
/*  80 */     if (!StringUtils.hasLength(jsonStr)) {
/*  81 */       return null;
/*     */     }
/*  83 */     MapLikeType mapLikeType = objectMapper.getTypeFactory().constructMapLikeType(HashMap.class, String.class, Object.class);
/*     */     try {
/*  85 */       return (Map<String, Object>)objectMapper.readValue(jsonStr, (JavaType)mapLikeType);
/*  86 */     } catch (IOException e) {
/*  87 */       throw new RuntimeException("json转换Map异常！", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static List convertJson2List(String jsonStr) {
/*  92 */     if (!StringUtils.hasLength(jsonStr)) {
/*  93 */       return null;
/*     */     }
/*  95 */     CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, Object.class);
/*     */     try {
/*  97 */       return (List)objectMapper.readValue(jsonStr, (JavaType)collectionType);
/*  98 */     } catch (IOException e) {
/*  99 */       throw new RuntimeException("json转换List异常！", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> List<T> convertJson2List(String jsonStr, Class<T> clazz) {
/* 104 */     if (!StringUtils.hasLength(jsonStr)) {
/* 105 */       return null;
/*     */     }
/* 107 */     CollectionType collectionType = objectMapper.getTypeFactory().constructCollectionType(ArrayList.class, clazz);
/*     */     try {
/* 109 */       return (List<T>)objectMapper.readValue(jsonStr, (JavaType)collectionType);
/* 110 */     } catch (IOException e) {
/* 111 */       throw new RuntimeException("json转换List异常！", e);
/*     */     } 
/*     */   }
/*     */   
/*     */   public static <T> T json2Object(String json, Class<T> clazz) {
/*     */     try {
/* 117 */       return (T)objectMapper.readValue(json, clazz);
/* 118 */     } catch (JsonProcessingException e) {
/* 119 */       log.warn(e.getMessage(), (Throwable)e);
/*     */       
/* 121 */       return null;
/*     */     } 
/*     */   }
/*     */   public static <T> T json2Object(String json, TypeReference<T> clazz) {
/*     */     try {
/* 126 */       return (T)objectMapper.readValue(json, clazz);
/* 127 */     } catch (JsonProcessingException e) {
/* 128 */       log.warn(e.getMessage(), (Throwable)e);
/*     */       
/* 130 */       return null;
/*     */     } 
/*     */   }
/*     */   public static <T> String object2Json(T entity) {
/*     */     try {
/* 135 */       return objectMapper.writeValueAsString(entity);
/* 136 */     } catch (JsonProcessingException e) {
/* 137 */       log.warn(e.getMessage(), (Throwable)e);
/*     */       
/* 139 */       return null;
/*     */     } 
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engin\\util\JsonUtils.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */