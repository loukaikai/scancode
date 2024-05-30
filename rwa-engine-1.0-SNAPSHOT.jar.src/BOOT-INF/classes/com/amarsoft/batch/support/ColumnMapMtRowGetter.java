/*     */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*     */ 
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.batch.MultiTableRowGetter;
/*     */ import java.sql.ResultSet;
/*     */ import java.sql.SQLException;
/*     */ import java.time.Duration;
/*     */ import java.time.LocalDateTime;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ import org.springframework.jdbc.core.ColumnMapRowMapper;
/*     */ import org.springframework.util.StringUtils;
/*     */ 
/*     */ public class ColumnMapMtRowGetter implements MultiTableRowGetter<Map<String, Object>> {
/*  20 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.support.ColumnMapMtRowGetter.class);
/*     */ 
/*     */   
/*  23 */   private Map<String, Map<String, Object>> currentDataMap = new HashMap<>();
/*  24 */   private Map<String, String> currentIdMap = new HashMap<>();
/*  25 */   private Map<String, Boolean> endFlagMap = new HashMap<>();
/*     */   private Map<String, Object> currentData;
/*     */   private String currentId;
/*     */   private boolean endFlag = false;
/*  29 */   private ColumnMapRowMapper rowMapper = new ColumnMapRowMapper();
/*     */   
/*     */   private static final String KEY_ID = "ID";
/*     */   
/*     */   private static final String KEY_SIZE = "SIZE";
/*     */ 
/*     */   
/*     */   public Map<String, Object> nextRow(LinkedHashMap<String, ResultSet> resultSetMap, String mainKey, String associatedField, int rowNum) throws SQLException {
/*  37 */     if (this.endFlag) {
/*  38 */       return null;
/*     */     }
/*  40 */     LocalDateTime start = LocalDateTime.now();
/*  41 */     Map<String, Object> row = new LinkedHashMap<>();
/*     */     
/*  43 */     List<Map<String, Object>> mainDataList = new ArrayList<>();
/*  44 */     row.put(mainKey, mainDataList);
/*     */     
/*  46 */     if (this.currentData != null) {
/*  47 */       mainDataList.add(this.currentData);
/*  48 */       row.put("ID", this.currentId);
/*     */     } 
/*  50 */     ResultSet mainRs = resultSetMap.get(mainKey);
/*  51 */     while (mainRs.next()) {
/*  52 */       Map<String, Object> mainData = this.rowMapper.mapRow(mainRs, 0);
/*     */       
/*  54 */       if (mainData.get(associatedField) == null || 
/*  55 */         !StringUtils.hasLength(mainData.get(associatedField).toString())) {
/*  56 */         throw new SQLException("[" + mainKey + "]存在关联字段为空的数据， 请检查sql是否准确");
/*     */       }
/*  58 */       if (StringUtils.hasLength(this.currentId)) {
/*  59 */         if (StrUtil.equals(this.currentId, (String)mainData.get(associatedField))) {
/*     */           
/*  61 */           mainDataList.add(mainData);
/*     */           
/*     */           continue;
/*     */         } 
/*  65 */         row.put("SIZE", Integer.valueOf(mainDataList.size()));
/*  66 */         debugTime(this.currentId, mainKey, mainDataList.size(), start);
/*     */         
/*  68 */         initOtherData(resultSetMap, mainKey, associatedField, row);
/*     */         
/*  70 */         this.currentData = mainData;
/*  71 */         this.currentId = (String)mainData.get(associatedField);
/*  72 */         return row;
/*     */       } 
/*     */ 
/*     */       
/*  76 */       this.currentId = (String)mainData.get(associatedField);
/*  77 */       row.put("ID", this.currentId);
/*  78 */       mainDataList.add(mainData);
/*     */     } 
/*     */ 
/*     */     
/*  82 */     this.endFlag = true;
/*  83 */     if (mainDataList.size() > 0) {
/*     */       
/*  85 */       row.put("SIZE", Integer.valueOf(mainDataList.size()));
/*  86 */       initOtherData(resultSetMap, mainKey, associatedField, row);
/*  87 */       return row;
/*     */     } 
/*  89 */     return null;
/*     */   }
/*     */   
/*     */   public void debugTime(String id, String key, int size, LocalDateTime start) {
/*  93 */     if (log.isDebugEnabled() && size > 3000) {
/*  94 */       log.debug("---> 获取数据[{}-{}][{}]costs[{}]", new Object[] { id, key, Integer.valueOf(size), Duration.between(start, LocalDateTime.now()) });
/*     */     }
/*     */   }
/*     */   
/*     */   private void initOtherData(LinkedHashMap<String, ResultSet> resultSetMap, String mainKey, String associatedField, Map<String, Object> row) throws SQLException {
/*  99 */     for (String key : resultSetMap.keySet()) {
/* 100 */       if (StrUtil.equals(mainKey, key)) {
/*     */         continue;
/*     */       }
/*     */       try {
/* 104 */         LocalDateTime start = LocalDateTime.now();
/* 105 */         List<Map<String, Object>> list = getOtherData(resultSetMap.get(key), key, associatedField);
/* 106 */         if (list != null) {
/* 107 */           row.put(key, list);
/* 108 */           debugTime(this.currentId, key, list.size(), start);
/*     */         } 
/* 110 */       } catch (SQLException e) {
/* 111 */         throw new SQLException("查询[" + key + "]异常", e);
/*     */       } 
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   private List<Map<String, Object>> getOtherData(ResultSet rs, String key, String associatedField) throws SQLException {
/* 118 */     if (this.endFlagMap.get(key) != null && ((Boolean)this.endFlagMap.get(key)).booleanValue()) {
/* 119 */       return null;
/*     */     }
/*     */     
/* 122 */     List<Map<String, Object>> list = new ArrayList<>();
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/* 131 */     if (!StringUtils.hasLength(this.currentIdMap.get(key)) || ((String)this.currentIdMap
/* 132 */       .get(key)).compareTo(this.currentId) <= 0) {
/*     */ 
/*     */       
/* 135 */       if (StrUtil.equals(this.currentId, this.currentIdMap.get(key))) {
/* 136 */         list.add(this.currentDataMap.get(key));
/*     */       }
/* 138 */       while (rs.next()) {
/* 139 */         String id = rs.getString(associatedField);
/*     */         
/* 141 */         if (!StringUtils.hasLength(id)) {
/* 142 */           throw new SQLException("[" + key + "]存在关联字段为空的数据， 请检查sql是否准确");
/*     */         }
/*     */         
/* 145 */         if (id.compareTo(this.currentId) < 0) {
/*     */           continue;
/*     */         }
/*     */         
/* 149 */         Map<String, Object> data = this.rowMapper.mapRow(rs, 0);
/* 150 */         if (StrUtil.equals(id, this.currentId)) {
/*     */           
/* 152 */           list.add(data);
/* 153 */           if (this.currentIdMap.get(key) == null) {
/* 154 */             this.currentIdMap.put(key, id);
/*     */           }
/*     */           continue;
/*     */         } 
/* 158 */         this.currentDataMap.put(key, data);
/* 159 */         this.currentIdMap.put(key, id);
/* 160 */         return list;
/*     */       } 
/*     */ 
/*     */       
/* 164 */       this.endFlagMap.put(key, Boolean.valueOf(true));
/*     */     } 
/*     */     
/* 167 */     return list;
/*     */   }
/*     */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\ColumnMapMtRowGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */