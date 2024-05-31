/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity.compare;
/*     */ 
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.MitigationMainType;
/*     */ import com.amarsoft.rwa.engine.constant.NullSortType;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.constant.SortType;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*     */ import com.amarsoft.rwa.engine.entity.MitigationDto;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MitigationComparator implements Comparator<MitigationDto> {
/*     */   private ExposureApproach approach;
/*     */   
/*  23 */   public void setApproach(ExposureApproach approach) { this.approach = approach; } private List<MitigateSortDo> mitigationSortDoList; private Map<String, Integer> mitigationTypeSortMap; public void setMitigationSortDoList(List<MitigateSortDo> mitigationSortDoList) { this.mitigationSortDoList = mitigationSortDoList; } public void setMitigationTypeSortMap(Map<String, Integer> mitigationTypeSortMap) { this.mitigationTypeSortMap = mitigationTypeSortMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.compare.MitigationComparator)) return false;  com.amarsoft.rwa.engine.entity.compare.MitigationComparator other = (com.amarsoft.rwa.engine.entity.compare.MitigationComparator)o; if (!other.canEqual(this)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object<MitigateSortDo> this$mitigationSortDoList = (Object<MitigateSortDo>)getMitigationSortDoList(), other$mitigationSortDoList = (Object<MitigateSortDo>)other.getMitigationSortDoList(); if ((this$mitigationSortDoList == null) ? (other$mitigationSortDoList != null) : !this$mitigationSortDoList.equals(other$mitigationSortDoList)) return false;  Object<String, Integer> this$mitigationTypeSortMap = (Object<String, Integer>)getMitigationTypeSortMap(), other$mitigationTypeSortMap = (Object<String, Integer>)other.getMitigationTypeSortMap(); return !((this$mitigationTypeSortMap == null) ? (other$mitigationTypeSortMap != null) : !this$mitigationTypeSortMap.equals(other$mitigationTypeSortMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.compare.MitigationComparator; } public int hashCode() { int PRIME = 59; result = 1; Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object<MitigateSortDo> $mitigationSortDoList = (Object<MitigateSortDo>)getMitigationSortDoList(); result = result * 59 + (($mitigationSortDoList == null) ? 43 : $mitigationSortDoList.hashCode()); Object<String, Integer> $mitigationTypeSortMap = (Object<String, Integer>)getMitigationTypeSortMap(); return result * 59 + (($mitigationTypeSortMap == null) ? 43 : $mitigationTypeSortMap.hashCode()); } public String toString() { return "MitigationComparator(approach=" + getApproach() + ", mitigationSortDoList=" + getMitigationSortDoList() + ", mitigationTypeSortMap=" + getMitigationTypeSortMap() + ")"; } public MitigationComparator(ExposureApproach approach, List<MitigateSortDo> mitigationSortDoList, Map<String, Integer> mitigationTypeSortMap) {
/*  24 */     this.approach = approach; this.mitigationSortDoList = mitigationSortDoList; this.mitigationTypeSortMap = mitigationTypeSortMap;
/*     */   }
/*     */   public MitigationComparator() {}
/*     */   
/*  28 */   public ExposureApproach getApproach() { return this.approach; }
/*  29 */   public List<MitigateSortDo> getMitigationSortDoList() { return this.mitigationSortDoList; } public Map<String, Integer> getMitigationTypeSortMap() {
/*  30 */     return this.mitigationTypeSortMap;
/*     */   }
/*     */   
/*     */   public int compare(MitigationDto o1, MitigationDto o2) {
/*  34 */     if (this.approach == null) {
/*     */       
/*  36 */       if (CollUtil.isEmpty(this.mitigationSortDoList)) {
/*  37 */         this.mitigationSortDoList = defaultMitigationSortDoListFirb();
/*     */       }
/*  39 */       return compareBySpecified(o1, o2, this.mitigationSortDoList);
/*  40 */     }  if (this.approach == ExposureApproach.WA) {
/*     */       
/*  42 */       int i = compare(o1, o2, RwaParam.QUAL_FLAG_WA.getCode(), SortType.DESC);
/*  43 */       if (i != 0) {
/*  44 */         return i;
/*     */       }
/*  46 */       if (CollUtil.isEmpty(this.mitigationSortDoList))
/*     */       {
/*  48 */         this.mitigationSortDoList = defaultMitigationSortDoListWa();
/*     */       }
/*  50 */       return compare(o1, o2, this.mitigationSortDoList);
/*     */     } 
/*     */     
/*  53 */     int r = compare(o1, o2, RwaParam.QUAL_FLAG_FIRB.getCode(), SortType.DESC);
/*  54 */     if (r != 0) {
/*  55 */       return r;
/*     */     }
/*     */     
/*  58 */     if (CollUtil.isEmpty(this.mitigationTypeSortMap)) {
/*  59 */       this.mitigationTypeSortMap = defaultMitigationTypeSortMap();
/*     */     }
/*  61 */     r = compareByType(o1, o2, this.mitigationTypeSortMap);
/*  62 */     if (r != 0) {
/*  63 */       return r;
/*     */     }
/*     */     
/*  66 */     r = compareByGuarantee(o1, o2);
/*  67 */     if (r != 0) {
/*  68 */       return r;
/*     */     }
/*     */     
/*  71 */     if (CollUtil.isEmpty(this.mitigationSortDoList)) {
/*  72 */       this.mitigationSortDoList = defaultMitigationSortDoListFirb();
/*     */     }
/*  74 */     return compare(o1, o2, this.mitigationSortDoList);
/*     */   }
/*     */ 
/*     */   
/*     */   public List<MitigateSortDo> defaultMitigationSortDoListWa() {
/*  79 */     List<MitigateSortDo> list = new ArrayList<>();
/*  80 */     list.add(new MitigateSortDo(RwaParam.RW, SortType.ASC, Integer.valueOf(1)));
/*  81 */     list.add(new MitigateSortDo(RwaParam.IS_ALONE, SortType.DESC, Integer.valueOf(2)));
/*  82 */     list.add(new MitigateSortDo(RwaParam.RESIDUAL_MATURITY, SortType.DESC, Integer.valueOf(3)));
/*  83 */     list.add(new MitigateSortDo(RwaParam.MITIGATION_AMOUNT, SortType.DESC, Integer.valueOf(4)));
/*  84 */     list.add(new MitigateSortDo(RwaParam.MITIGATION_ID, SortType.ASC, Integer.valueOf(5)));
/*  85 */     return list;
/*     */   }
/*     */   
/*     */   public List<MitigateSortDo> defaultMitigationSortDoListFirb() {
/*  89 */     List<MitigateSortDo> list = new ArrayList<>();
/*  90 */     list.add(new MitigateSortDo(RwaParam.IS_ALONE, SortType.DESC, Integer.valueOf(1)));
/*  91 */     list.add(new MitigateSortDo(RwaParam.RESIDUAL_MATURITY, SortType.DESC, Integer.valueOf(2)));
/*  92 */     list.add(new MitigateSortDo(RwaParam.MITIGATION_AMOUNT, SortType.DESC, Integer.valueOf(3)));
/*  93 */     list.add(new MitigateSortDo(RwaParam.MITIGATION_ID, SortType.ASC, Integer.valueOf(4)));
/*  94 */     return list;
/*     */   }
/*     */   
/*     */   public Map<String, Integer> defaultMitigationTypeSortMap() {
/*  98 */     Map<String, Integer> map = new HashMap<>();
/*     */ 
/*     */     
/* 101 */     map.put(MitigationMainType.FINANCIAL_COLLATERAL.getCode(), Integer.valueOf(1));
/* 102 */     map.put(MitigationMainType.ACCOUNTS_RECEIVABLE.getCode(), Integer.valueOf(2));
/* 103 */     map.put(MitigationMainType.REAL_ESTATE.getCode(), Integer.valueOf(3));
/* 104 */     map.put(MitigationMainType.OTHER_COLLATERAL.getCode(), Integer.valueOf(4));
/*     */     
/* 106 */     map.put(MitigationMainType.GUARANTEE.getCode(), Integer.valueOf(5));
/* 107 */     map.put(MitigationMainType.CREDIT_DERIVATIVE.getCode(), Integer.valueOf(6));
/*     */     
/* 109 */     return map;
/*     */   }
/*     */   
/*     */   public int compareByType(MitigationDto o1, MitigationDto o2, Map<String, Integer> typeMap) {
/* 113 */     if (o1.getMitigationMainType() == null && o2.getMitigationMainType() == null) {
/* 114 */       return 0;
/*     */     }
/* 116 */     if (StrUtil.equals(o1.getMitigationMainType(), o2.getMitigationMainType())) {
/* 117 */       return 0;
/*     */     }
/* 119 */     Integer s1 = typeMap.get(o1.getMitigationMainType());
/* 120 */     Integer s2 = typeMap.get(o2.getMitigationMainType());
/*     */     
/* 122 */     return DataUtils.compare(s1, s2, SortType.ASC, NullSortType.FIX_BOTTOM);
/*     */   }
/*     */   
/*     */   public int compareByGuarantee(MitigationDto o1, MitigationDto o2) {
/* 126 */     if (!StrUtil.equals(o1.getMitigationMainType(), MitigationMainType.GUARANTEE.getCode())) {
/* 127 */       return 0;
/*     */     }
/* 129 */     int r = DataUtils.compare(o1.getKcr(), o2.getKcr(), SortType.ASC, NullSortType.FIX_BOTTOM);
/* 130 */     if (r != 0) {
/* 131 */       return r;
/*     */     }
/* 133 */     return DataUtils.compare(o1.getPd(), o2.getPd(), SortType.ASC, NullSortType.FIX_BOTTOM);
/*     */   }
/*     */   
/*     */   public int compareBySpecified(MitigationDto o1, MitigationDto o2, List<MitigateSortDo> sortList) {
/* 137 */     int r = 0;
/* 138 */     for (MitigateSortDo sortDo : sortList) {
/* 139 */       String field = sortDo.getSortParam();
/* 140 */       if (StrUtil.equals(RwaParam.K.getField(), field)) {
/*     */         
/* 142 */         r = DataUtils.compare(NumberUtil.div(o1.getKcr(), o1.getLgd()), NumberUtil.div(o2.getKcr(), o2.getLgd()), SortType.ASC, NullSortType.FIX_BOTTOM);
/*     */       } else {
/* 144 */         r = compare(o1, o2, field, (SortType)EnumUtils.getEnumByCode(sortDo.getSortType(), SortType.class));
/*     */       } 
/* 146 */       if (r != 0) {
/* 147 */         return r;
/*     */       }
/*     */     } 
/* 150 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(MitigationDto o1, MitigationDto o2, List<MitigateSortDo> sortList) {
/* 157 */     int r = 0;
/* 158 */     for (MitigateSortDo sortDo : sortList) {
/* 159 */       r = compare(o1, o2, sortDo.getSortParam(), (SortType)EnumUtils.getEnumByCode(sortDo.getSortType(), SortType.class));
/* 160 */       if (r != 0) {
/* 161 */         return r;
/*     */       }
/*     */     } 
/* 164 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(MitigationDto o1, MitigationDto o2, String field, SortType sortType) {
/* 171 */     if (!StrUtil.isBlank(field))
/*     */     {
/* 173 */       return DataUtils.compare(BeanUtil.getProperty(o1, field), BeanUtil.getProperty(o2, field), sortType, NullSortType.FIX_BOTTOM);
/*     */     }
/* 175 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\compare\MitigationComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */