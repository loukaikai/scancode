/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity.compare;
/*     */ 
/*     */ import cn.hutool.core.bean.BeanUtil;
/*     */ import cn.hutool.core.collection.CollUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.NullSortType;
/*     */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*     */ import com.amarsoft.rwa.engine.constant.SortType;
/*     */ import com.amarsoft.rwa.engine.entity.ExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*     */ import com.amarsoft.rwa.engine.util.DataUtils;
/*     */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Comparator;
/*     */ import java.util.List;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class ExposureComparator
/*     */   implements Comparator<ExposureDto>
/*     */ {
/*     */   private ExposureApproach approach;
/*     */   private List<MitigateSortDo> exposureSortDoList;
/*     */   
/*     */   public void setApproach(ExposureApproach approach) {
/*  28 */     this.approach = approach; } public void setExposureSortDoList(List<MitigateSortDo> exposureSortDoList) { this.exposureSortDoList = exposureSortDoList; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.compare.ExposureComparator)) return false;  com.amarsoft.rwa.engine.entity.compare.ExposureComparator other = (com.amarsoft.rwa.engine.entity.compare.ExposureComparator)o; if (!other.canEqual(this)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object<MitigateSortDo> this$exposureSortDoList = (Object<MitigateSortDo>)getExposureSortDoList(), other$exposureSortDoList = (Object<MitigateSortDo>)other.getExposureSortDoList(); return !((this$exposureSortDoList == null) ? (other$exposureSortDoList != null) : !this$exposureSortDoList.equals(other$exposureSortDoList)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.compare.ExposureComparator; } public int hashCode() { int PRIME = 59; result = 1; Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object<MitigateSortDo> $exposureSortDoList = (Object<MitigateSortDo>)getExposureSortDoList(); return result * 59 + (($exposureSortDoList == null) ? 43 : $exposureSortDoList.hashCode()); } public String toString() { return "ExposureComparator(approach=" + getApproach() + ", exposureSortDoList=" + getExposureSortDoList() + ")"; } public ExposureComparator(ExposureApproach approach, List<MitigateSortDo> exposureSortDoList) {
/*  29 */     this.approach = approach; this.exposureSortDoList = exposureSortDoList;
/*     */   }
/*     */   
/*     */   public ExposureComparator() {}
/*  33 */   public ExposureApproach getApproach() { return this.approach; } public List<MitigateSortDo> getExposureSortDoList() {
/*  34 */     return this.exposureSortDoList;
/*     */   }
/*     */ 
/*     */   
/*     */   public int compare(ExposureDto o1, ExposureDto o2) {
/*  39 */     if (CollUtil.isEmpty(this.exposureSortDoList)) {
/*  40 */       if (this.approach == ExposureApproach.WA) {
/*     */         
/*  42 */         this.exposureSortDoList = defaultExposureSortListWa();
/*  43 */       } else if (this.approach == ExposureApproach.FIRB) {
/*     */         
/*  45 */         this.exposureSortDoList = defaultExposureSortListFirb();
/*     */       } else {
/*     */         
/*  48 */         this.exposureSortDoList = defaultExposureSortListIrb();
/*     */       } 
/*     */     }
/*  51 */     return compare(o1, o2, this.exposureSortDoList);
/*     */   }
/*     */   
/*     */   public List<MitigateSortDo> defaultExposureSortListWa() {
/*  55 */     List<MitigateSortDo> list = new ArrayList<>();
/*  56 */     list.add(new MitigateSortDo(RwaParam.RW, SortType.DESC, Integer.valueOf(1)));
/*  57 */     list.add(new MitigateSortDo(RwaParam.RESIDUAL_MATURITY, SortType.DESC, Integer.valueOf(2)));
/*  58 */     list.add(new MitigateSortDo(RwaParam.ASSET_BALANCE, SortType.DESC, Integer.valueOf(3)));
/*  59 */     list.add(new MitigateSortDo(RwaParam.EXPOSURE_ID, SortType.ASC, Integer.valueOf(4)));
/*  60 */     return list;
/*     */   }
/*     */   
/*     */   public List<MitigateSortDo> defaultExposureSortListFirb() {
/*  64 */     List<MitigateSortDo> list = new ArrayList<>();
/*  65 */     list.add(new MitigateSortDo(RwaParam.K, SortType.DESC, Integer.valueOf(1)));
/*  66 */     list.add(new MitigateSortDo(RwaParam.PD, SortType.DESC, Integer.valueOf(2)));
/*  67 */     list.add(new MitigateSortDo(RwaParam.RESIDUAL_MATURITY, SortType.DESC, Integer.valueOf(3)));
/*  68 */     list.add(new MitigateSortDo(RwaParam.ASSET_BALANCE, SortType.DESC, Integer.valueOf(4)));
/*  69 */     list.add(new MitigateSortDo(RwaParam.EXPOSURE_ID, SortType.ASC, Integer.valueOf(5)));
/*  70 */     return list;
/*     */   }
/*     */   
/*     */   public List<MitigateSortDo> defaultExposureSortListIrb() {
/*  74 */     List<MitigateSortDo> list = new ArrayList<>();
/*  75 */     list.add(new MitigateSortDo(RwaParam.K, SortType.DESC, Integer.valueOf(1)));
/*  76 */     list.add(new MitigateSortDo(RwaParam.PD, SortType.DESC, Integer.valueOf(2)));
/*  77 */     list.add(new MitigateSortDo(RwaParam.ASSET_BALANCE, SortType.DESC, Integer.valueOf(4)));
/*  78 */     list.add(new MitigateSortDo(RwaParam.EXPOSURE_ID, SortType.ASC, Integer.valueOf(5)));
/*  79 */     return list;
/*     */   }
/*     */   
/*     */   public int compare(ExposureDto o1, ExposureDto o2, List<MitigateSortDo> sortList) {
/*  83 */     int r = 0;
/*  84 */     for (MitigateSortDo sortDo : sortList) {
/*  85 */       r = compare(o1, o2, sortDo.getSortParam(), (SortType)EnumUtils.getEnumByCode(sortDo.getSortType(), SortType.class));
/*  86 */       if (r != 0) {
/*  87 */         return r;
/*     */       }
/*     */     } 
/*  90 */     return r;
/*     */   }
/*     */ 
/*     */ 
/*     */ 
/*     */   
/*     */   public int compare(ExposureDto o1, ExposureDto o2, String field, SortType sortType) {
/*  97 */     if (!StrUtil.isEmpty(field)) {
/*     */       
/*  99 */       if (StrUtil.equals(field, RwaParam.RW.getField()))
/*     */       {
/* 101 */         return DataUtils.compare(RwaMath.getCcfConversion(o1.getCcf(), o1.getRw()), RwaMath.getCcfConversion(o2.getCcf(), o2.getRw()), sortType, NullSortType.FIX_BOTTOM); } 
/* 102 */       if (StrUtil.equals(field, RwaParam.K.getField()))
/*     */       {
/* 104 */         return DataUtils.compare(RwaMath.getCcfConversion(o1.getCcf(), o1.getKcr()), RwaMath.getCcfConversion(o2.getCcf(), o2.getKcr()), sortType, NullSortType.FIX_BOTTOM);
/*     */       }
/* 106 */       return DataUtils.compare(BeanUtil.getProperty(o1, field), BeanUtil.getProperty(o2, field), sortType, NullSortType.FIX_BOTTOM);
/*     */     } 
/*     */     
/* 109 */     return 0;
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\compare\ExposureComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */