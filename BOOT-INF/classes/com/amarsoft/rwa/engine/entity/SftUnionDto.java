/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.entity.SftCollateralDto;
/*    */ import com.amarsoft.rwa.engine.entity.SftExposureDto;
/*    */ import com.amarsoft.rwa.engine.entity.SftNettingDto;
/*    */ import java.util.List;
/*    */ 
/*    */ public class SftUnionDto {
/*    */   private TaskType taskType;
/*    */   private String id;
/*    */   private Date dataDate;
/*    */   private SchemeConfigDo schemeConfig;
/*    */   private ExposureApproach approach;
/*    */   private int size;
/*    */   private Identity nettingFlag;
/*    */   
/* 17 */   public void setTaskType(TaskType taskType) { this.taskType = taskType; } private boolean isSpecialApproach; private SftNettingDto nettingDto; private List<SftNettingDto> nettingList; private SftExposureDto exposureDto; private List<SftExposureDto> exposureList; private List<SftCollateralDto> collateralList; private Map<String, List<SftCollateralDto>> collateralListMap; public void setId(String id) { this.id = id; } public void setDataDate(Date dataDate) { this.dataDate = dataDate; } public void setSchemeConfig(SchemeConfigDo schemeConfig) { this.schemeConfig = schemeConfig; } public void setApproach(ExposureApproach approach) { this.approach = approach; } public void setSize(int size) { this.size = size; } public void setNettingFlag(Identity nettingFlag) { this.nettingFlag = nettingFlag; } public void setSpecialApproach(boolean isSpecialApproach) { this.isSpecialApproach = isSpecialApproach; } public void setNettingDto(SftNettingDto nettingDto) { this.nettingDto = nettingDto; } public void setNettingList(List<SftNettingDto> nettingList) { this.nettingList = nettingList; } public void setExposureDto(SftExposureDto exposureDto) { this.exposureDto = exposureDto; } public void setExposureList(List<SftExposureDto> exposureList) { this.exposureList = exposureList; } public void setCollateralList(List<SftCollateralDto> collateralList) { this.collateralList = collateralList; } public void setCollateralListMap(Map<String, List<SftCollateralDto>> collateralListMap) { this.collateralListMap = collateralListMap; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.SftUnionDto)) return false;  com.amarsoft.rwa.engine.entity.SftUnionDto other = (com.amarsoft.rwa.engine.entity.SftUnionDto)o; if (!other.canEqual(this)) return false;  if (getSize() != other.getSize()) return false;  if (isSpecialApproach() != other.isSpecialApproach()) return false;  Object this$taskType = getTaskType(), other$taskType = other.getTaskType(); if ((this$taskType == null) ? (other$taskType != null) : !this$taskType.equals(other$taskType)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$dataDate = getDataDate(), other$dataDate = other.getDataDate(); if ((this$dataDate == null) ? (other$dataDate != null) : !this$dataDate.equals(other$dataDate)) return false;  Object this$schemeConfig = getSchemeConfig(), other$schemeConfig = other.getSchemeConfig(); if ((this$schemeConfig == null) ? (other$schemeConfig != null) : !this$schemeConfig.equals(other$schemeConfig)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$nettingFlag = getNettingFlag(), other$nettingFlag = other.getNettingFlag(); if ((this$nettingFlag == null) ? (other$nettingFlag != null) : !this$nettingFlag.equals(other$nettingFlag)) return false;  Object this$nettingDto = getNettingDto(), other$nettingDto = other.getNettingDto(); if ((this$nettingDto == null) ? (other$nettingDto != null) : !this$nettingDto.equals(other$nettingDto)) return false;  Object<SftNettingDto> this$nettingList = (Object<SftNettingDto>)getNettingList(), other$nettingList = (Object<SftNettingDto>)other.getNettingList(); if ((this$nettingList == null) ? (other$nettingList != null) : !this$nettingList.equals(other$nettingList)) return false;  Object this$exposureDto = getExposureDto(), other$exposureDto = other.getExposureDto(); if ((this$exposureDto == null) ? (other$exposureDto != null) : !this$exposureDto.equals(other$exposureDto)) return false;  Object<SftExposureDto> this$exposureList = (Object<SftExposureDto>)getExposureList(), other$exposureList = (Object<SftExposureDto>)other.getExposureList(); if ((this$exposureList == null) ? (other$exposureList != null) : !this$exposureList.equals(other$exposureList)) return false;  Object<SftCollateralDto> this$collateralList = (Object<SftCollateralDto>)getCollateralList(), other$collateralList = (Object<SftCollateralDto>)other.getCollateralList(); if ((this$collateralList == null) ? (other$collateralList != null) : !this$collateralList.equals(other$collateralList)) return false;  Object<String, List<SftCollateralDto>> this$collateralListMap = (Object<String, List<SftCollateralDto>>)getCollateralListMap(), other$collateralListMap = (Object<String, List<SftCollateralDto>>)other.getCollateralListMap(); return !((this$collateralListMap == null) ? (other$collateralListMap != null) : !this$collateralListMap.equals(other$collateralListMap)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.SftUnionDto; } public int hashCode() { int PRIME = 59; result = 1; result = result * 59 + getSize(); result = result * 59 + (isSpecialApproach() ? 79 : 97); Object $taskType = getTaskType(); result = result * 59 + (($taskType == null) ? 43 : $taskType.hashCode()); Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $dataDate = getDataDate(); result = result * 59 + (($dataDate == null) ? 43 : $dataDate.hashCode()); Object $schemeConfig = getSchemeConfig(); result = result * 59 + (($schemeConfig == null) ? 43 : $schemeConfig.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $nettingFlag = getNettingFlag(); result = result * 59 + (($nettingFlag == null) ? 43 : $nettingFlag.hashCode()); Object $nettingDto = getNettingDto(); result = result * 59 + (($nettingDto == null) ? 43 : $nettingDto.hashCode()); Object<SftNettingDto> $nettingList = (Object<SftNettingDto>)getNettingList(); result = result * 59 + (($nettingList == null) ? 43 : $nettingList.hashCode()); Object $exposureDto = getExposureDto(); result = result * 59 + (($exposureDto == null) ? 43 : $exposureDto.hashCode()); Object<SftExposureDto> $exposureList = (Object<SftExposureDto>)getExposureList(); result = result * 59 + (($exposureList == null) ? 43 : $exposureList.hashCode()); Object<SftCollateralDto> $collateralList = (Object<SftCollateralDto>)getCollateralList(); result = result * 59 + (($collateralList == null) ? 43 : $collateralList.hashCode()); Object<String, List<SftCollateralDto>> $collateralListMap = (Object<String, List<SftCollateralDto>>)getCollateralListMap(); return result * 59 + (($collateralListMap == null) ? 43 : $collateralListMap.hashCode()); } public String toString() { return "SftUnionDto(taskType=" + getTaskType() + ", id=" + getId() + ", dataDate=" + getDataDate() + ", schemeConfig=" + getSchemeConfig() + ", approach=" + getApproach() + ", size=" + getSize() + ", nettingFlag=" + getNettingFlag() + ", isSpecialApproach=" + isSpecialApproach() + ", nettingDto=" + getNettingDto() + ", nettingList=" + getNettingList() + ", exposureDto=" + getExposureDto() + ", exposureList=" + getExposureList() + ", collateralList=" + getCollateralList() + ", collateralListMap=" + getCollateralListMap() + ")"; }
/*    */ 
/*    */   
/* 20 */   public TaskType getTaskType() { return this.taskType; }
/* 21 */   public String getId() { return this.id; }
/* 22 */   public Date getDataDate() { return this.dataDate; }
/* 23 */   public SchemeConfigDo getSchemeConfig() { return this.schemeConfig; }
/* 24 */   public ExposureApproach getApproach() { return this.approach; }
/* 25 */   public int getSize() { return this.size; }
/* 26 */   public Identity getNettingFlag() { return this.nettingFlag; }
/* 27 */   public boolean isSpecialApproach() { return this.isSpecialApproach; }
/* 28 */   public SftNettingDto getNettingDto() { return this.nettingDto; }
/* 29 */   public List<SftNettingDto> getNettingList() { return this.nettingList; }
/* 30 */   public SftExposureDto getExposureDto() { return this.exposureDto; }
/* 31 */   public List<SftExposureDto> getExposureList() { return this.exposureList; }
/* 32 */   public List<SftCollateralDto> getCollateralList() { return this.collateralList; } public Map<String, List<SftCollateralDto>> getCollateralListMap() {
/* 33 */     return this.collateralListMap;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\SftUnionDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */