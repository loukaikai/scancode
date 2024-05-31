/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity.compare;
/*    */ 
/*    */ import cn.hutool.core.bean.BeanUtil;
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import cn.hutool.core.util.StrUtil;
/*    */ import com.amarsoft.rwa.engine.constant.NullSortType;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.constant.SortType;
/*    */ import com.amarsoft.rwa.engine.entity.AbsExposureDto;
/*    */ import com.amarsoft.rwa.engine.entity.MitigateSortDo;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import com.amarsoft.rwa.engine.util.EnumUtils;
/*    */ import java.util.ArrayList;
/*    */ import java.util.Comparator;
/*    */ import java.util.List;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class AbsExposureComparator
/*    */   implements Comparator<AbsExposureDto>
/*    */ {
/*    */   private List<MitigateSortDo> exposureSortDoList;
/*    */   
/*    */   public void setExposureSortDoList(List<MitigateSortDo> exposureSortDoList) {
/* 26 */     this.exposureSortDoList = exposureSortDoList; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.compare.AbsExposureComparator)) return false;  com.amarsoft.rwa.engine.entity.compare.AbsExposureComparator other = (com.amarsoft.rwa.engine.entity.compare.AbsExposureComparator)o; if (!other.canEqual(this)) return false;  Object<MitigateSortDo> this$exposureSortDoList = (Object<MitigateSortDo>)getExposureSortDoList(), other$exposureSortDoList = (Object<MitigateSortDo>)other.getExposureSortDoList(); return !((this$exposureSortDoList == null) ? (other$exposureSortDoList != null) : !this$exposureSortDoList.equals(other$exposureSortDoList)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.compare.AbsExposureComparator; } public int hashCode() { int PRIME = 59; result = 1; Object<MitigateSortDo> $exposureSortDoList = (Object<MitigateSortDo>)getExposureSortDoList(); return result * 59 + (($exposureSortDoList == null) ? 43 : $exposureSortDoList.hashCode()); } public String toString() { return "AbsExposureComparator(exposureSortDoList=" + getExposureSortDoList() + ")"; } public AbsExposureComparator(List<MitigateSortDo> exposureSortDoList) {
/* 27 */     this.exposureSortDoList = exposureSortDoList;
/*    */   }
/*    */   public AbsExposureComparator() {}
/*    */   public List<MitigateSortDo> getExposureSortDoList() {
/* 31 */     return this.exposureSortDoList;
/*    */   }
/*    */ 
/*    */   
/*    */   public int compare(AbsExposureDto o1, AbsExposureDto o2) {
/* 36 */     if (CollUtil.isEmpty(this.exposureSortDoList)) {
/* 37 */       this.exposureSortDoList = defaultExposureSortList();
/*    */     }
/* 39 */     return compare(o1, o2, this.exposureSortDoList);
/*    */   }
/*    */   
/*    */   public List<MitigateSortDo> defaultExposureSortList() {
/* 43 */     List<MitigateSortDo> list = new ArrayList<>();
/* 44 */     list.add(new MitigateSortDo(RwaParam.TRANCHE_SN, SortType.ASC, Integer.valueOf(1)));
/* 45 */     list.add(new MitigateSortDo(RwaParam.TRANCHE_STARTING_POINT, SortType.ASC, Integer.valueOf(2)));
/* 46 */     list.add(new MitigateSortDo(RwaParam.ABS_EXPOSURE_ID, SortType.ASC, Integer.valueOf(3)));
/* 47 */     return list;
/*    */   }
/*    */   
/*    */   public int compare(AbsExposureDto o1, AbsExposureDto o2, List<MitigateSortDo> sortList) {
/* 51 */     int r = 0;
/* 52 */     for (MitigateSortDo sortDo : sortList) {
/* 53 */       r = compare(o1, o2, sortDo.getSortParam(), (SortType)EnumUtils.getEnumByCode(sortDo.getSortType(), SortType.class));
/* 54 */       if (r != 0) {
/* 55 */         return r;
/*    */       }
/*    */     } 
/* 58 */     return r;
/*    */   }
/*    */   
/*    */   public int compare(AbsExposureDto o1, AbsExposureDto o2, String field, SortType sortType) {
/* 62 */     if (!StrUtil.isEmpty(field)) {
/* 63 */       return DataUtils.compare(BeanUtil.getProperty(o1, field), BeanUtil.getProperty(o2, field), sortType, NullSortType.FIX_BOTTOM);
/*    */     }
/* 65 */     return 0;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\compare\AbsExposureComparator.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */