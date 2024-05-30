/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ public class SftCollateralDto { private String collateralId; private String approach; private String nettingFlag; private String nettingId; private String exposureId; private String issuerId; private String issuerType; private String isApplyWa; private String isApplyFirb; private String qualFlagWa;
/*    */   private String qualFlagFirb;
/*    */   private String mitigationMainType;
/*    */   private String mitigationSmallType;
/*    */   
/*  7 */   public void setCollateralId(String collateralId) { this.collateralId = collateralId; } private BigDecimal collateralAmount; private String currency; private BigDecimal originalMaturity; private BigDecimal residualMaturity; private Integer revaFrequency; private String isZeroHaircut; private BigDecimal factorLine; private BigDecimal sh; private BigDecimal haircut; private BigDecimal hfx; private String supervisionClass; private BigDecimal exposure; private BigDecimal rw; private BigDecimal rwa; public void setApproach(String approach) { this.approach = approach; } public void setNettingFlag(String nettingFlag) { this.nettingFlag = nettingFlag; } public void setNettingId(String nettingId) { this.nettingId = nettingId; } public void setExposureId(String exposureId) { this.exposureId = exposureId; } public void setIssuerId(String issuerId) { this.issuerId = issuerId; } public void setIssuerType(String issuerType) { this.issuerType = issuerType; } public void setIsApplyWa(String isApplyWa) { this.isApplyWa = isApplyWa; } public void setIsApplyFirb(String isApplyFirb) { this.isApplyFirb = isApplyFirb; } public void setQualFlagWa(String qualFlagWa) { this.qualFlagWa = qualFlagWa; } public void setQualFlagFirb(String qualFlagFirb) { this.qualFlagFirb = qualFlagFirb; } public void setMitigationMainType(String mitigationMainType) { this.mitigationMainType = mitigationMainType; } public void setMitigationSmallType(String mitigationSmallType) { this.mitigationSmallType = mitigationSmallType; } public void setCollateralAmount(BigDecimal collateralAmount) { this.collateralAmount = collateralAmount; } public void setCurrency(String currency) { this.currency = currency; } public void setOriginalMaturity(BigDecimal originalMaturity) { this.originalMaturity = originalMaturity; } public void setResidualMaturity(BigDecimal residualMaturity) { this.residualMaturity = residualMaturity; } public void setRevaFrequency(Integer revaFrequency) { this.revaFrequency = revaFrequency; } public void setIsZeroHaircut(String isZeroHaircut) { this.isZeroHaircut = isZeroHaircut; } public void setFactorLine(BigDecimal factorLine) { this.factorLine = factorLine; } public void setSh(BigDecimal sh) { this.sh = sh; } public void setHaircut(BigDecimal haircut) { this.haircut = haircut; } public void setHfx(BigDecimal hfx) { this.hfx = hfx; } public void setSupervisionClass(String supervisionClass) { this.supervisionClass = supervisionClass; } public void setExposure(BigDecimal exposure) { this.exposure = exposure; } public void setRw(BigDecimal rw) { this.rw = rw; } public void setRwa(BigDecimal rwa) { this.rwa = rwa; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.SftCollateralDto)) return false;  com.amarsoft.rwa.engine.entity.SftCollateralDto other = (com.amarsoft.rwa.engine.entity.SftCollateralDto)o; if (!other.canEqual(this)) return false;  Object this$revaFrequency = getRevaFrequency(), other$revaFrequency = other.getRevaFrequency(); if ((this$revaFrequency == null) ? (other$revaFrequency != null) : !this$revaFrequency.equals(other$revaFrequency)) return false;  Object this$collateralId = getCollateralId(), other$collateralId = other.getCollateralId(); if ((this$collateralId == null) ? (other$collateralId != null) : !this$collateralId.equals(other$collateralId)) return false;  Object this$approach = getApproach(), other$approach = other.getApproach(); if ((this$approach == null) ? (other$approach != null) : !this$approach.equals(other$approach)) return false;  Object this$nettingFlag = getNettingFlag(), other$nettingFlag = other.getNettingFlag(); if ((this$nettingFlag == null) ? (other$nettingFlag != null) : !this$nettingFlag.equals(other$nettingFlag)) return false;  Object this$nettingId = getNettingId(), other$nettingId = other.getNettingId(); if ((this$nettingId == null) ? (other$nettingId != null) : !this$nettingId.equals(other$nettingId)) return false;  Object this$exposureId = getExposureId(), other$exposureId = other.getExposureId(); if ((this$exposureId == null) ? (other$exposureId != null) : !this$exposureId.equals(other$exposureId)) return false;  Object this$issuerId = getIssuerId(), other$issuerId = other.getIssuerId(); if ((this$issuerId == null) ? (other$issuerId != null) : !this$issuerId.equals(other$issuerId)) return false;  Object this$issuerType = getIssuerType(), other$issuerType = other.getIssuerType(); if ((this$issuerType == null) ? (other$issuerType != null) : !this$issuerType.equals(other$issuerType)) return false;  Object this$isApplyWa = getIsApplyWa(), other$isApplyWa = other.getIsApplyWa(); if ((this$isApplyWa == null) ? (other$isApplyWa != null) : !this$isApplyWa.equals(other$isApplyWa)) return false;  Object this$isApplyFirb = getIsApplyFirb(), other$isApplyFirb = other.getIsApplyFirb(); if ((this$isApplyFirb == null) ? (other$isApplyFirb != null) : !this$isApplyFirb.equals(other$isApplyFirb)) return false;  Object this$qualFlagWa = getQualFlagWa(), other$qualFlagWa = other.getQualFlagWa(); if ((this$qualFlagWa == null) ? (other$qualFlagWa != null) : !this$qualFlagWa.equals(other$qualFlagWa)) return false;  Object this$qualFlagFirb = getQualFlagFirb(), other$qualFlagFirb = other.getQualFlagFirb(); if ((this$qualFlagFirb == null) ? (other$qualFlagFirb != null) : !this$qualFlagFirb.equals(other$qualFlagFirb)) return false;  Object this$mitigationMainType = getMitigationMainType(), other$mitigationMainType = other.getMitigationMainType(); if ((this$mitigationMainType == null) ? (other$mitigationMainType != null) : !this$mitigationMainType.equals(other$mitigationMainType)) return false;  Object this$mitigationSmallType = getMitigationSmallType(), other$mitigationSmallType = other.getMitigationSmallType(); if ((this$mitigationSmallType == null) ? (other$mitigationSmallType != null) : !this$mitigationSmallType.equals(other$mitigationSmallType)) return false;  Object this$collateralAmount = getCollateralAmount(), other$collateralAmount = other.getCollateralAmount(); if ((this$collateralAmount == null) ? (other$collateralAmount != null) : !this$collateralAmount.equals(other$collateralAmount)) return false;  Object this$currency = getCurrency(), other$currency = other.getCurrency(); if ((this$currency == null) ? (other$currency != null) : !this$currency.equals(other$currency)) return false;  Object this$originalMaturity = getOriginalMaturity(), other$originalMaturity = other.getOriginalMaturity(); if ((this$originalMaturity == null) ? (other$originalMaturity != null) : !this$originalMaturity.equals(other$originalMaturity)) return false;  Object this$residualMaturity = getResidualMaturity(), other$residualMaturity = other.getResidualMaturity(); if ((this$residualMaturity == null) ? (other$residualMaturity != null) : !this$residualMaturity.equals(other$residualMaturity)) return false;  Object this$isZeroHaircut = getIsZeroHaircut(), other$isZeroHaircut = other.getIsZeroHaircut(); if ((this$isZeroHaircut == null) ? (other$isZeroHaircut != null) : !this$isZeroHaircut.equals(other$isZeroHaircut)) return false;  Object this$factorLine = getFactorLine(), other$factorLine = other.getFactorLine(); if ((this$factorLine == null) ? (other$factorLine != null) : !this$factorLine.equals(other$factorLine)) return false;  Object this$sh = getSh(), other$sh = other.getSh(); if ((this$sh == null) ? (other$sh != null) : !this$sh.equals(other$sh)) return false;  Object this$haircut = getHaircut(), other$haircut = other.getHaircut(); if ((this$haircut == null) ? (other$haircut != null) : !this$haircut.equals(other$haircut)) return false;  Object this$hfx = getHfx(), other$hfx = other.getHfx(); if ((this$hfx == null) ? (other$hfx != null) : !this$hfx.equals(other$hfx)) return false;  Object this$supervisionClass = getSupervisionClass(), other$supervisionClass = other.getSupervisionClass(); if ((this$supervisionClass == null) ? (other$supervisionClass != null) : !this$supervisionClass.equals(other$supervisionClass)) return false;  Object this$exposure = getExposure(), other$exposure = other.getExposure(); if ((this$exposure == null) ? (other$exposure != null) : !this$exposure.equals(other$exposure)) return false;  Object this$rw = getRw(), other$rw = other.getRw(); if ((this$rw == null) ? (other$rw != null) : !this$rw.equals(other$rw)) return false;  Object this$rwa = getRwa(), other$rwa = other.getRwa(); return !((this$rwa == null) ? (other$rwa != null) : !this$rwa.equals(other$rwa)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.SftCollateralDto; } public int hashCode() { int PRIME = 59; result = 1; Object $revaFrequency = getRevaFrequency(); result = result * 59 + (($revaFrequency == null) ? 43 : $revaFrequency.hashCode()); Object $collateralId = getCollateralId(); result = result * 59 + (($collateralId == null) ? 43 : $collateralId.hashCode()); Object $approach = getApproach(); result = result * 59 + (($approach == null) ? 43 : $approach.hashCode()); Object $nettingFlag = getNettingFlag(); result = result * 59 + (($nettingFlag == null) ? 43 : $nettingFlag.hashCode()); Object $nettingId = getNettingId(); result = result * 59 + (($nettingId == null) ? 43 : $nettingId.hashCode()); Object $exposureId = getExposureId(); result = result * 59 + (($exposureId == null) ? 43 : $exposureId.hashCode()); Object $issuerId = getIssuerId(); result = result * 59 + (($issuerId == null) ? 43 : $issuerId.hashCode()); Object $issuerType = getIssuerType(); result = result * 59 + (($issuerType == null) ? 43 : $issuerType.hashCode()); Object $isApplyWa = getIsApplyWa(); result = result * 59 + (($isApplyWa == null) ? 43 : $isApplyWa.hashCode()); Object $isApplyFirb = getIsApplyFirb(); result = result * 59 + (($isApplyFirb == null) ? 43 : $isApplyFirb.hashCode()); Object $qualFlagWa = getQualFlagWa(); result = result * 59 + (($qualFlagWa == null) ? 43 : $qualFlagWa.hashCode()); Object $qualFlagFirb = getQualFlagFirb(); result = result * 59 + (($qualFlagFirb == null) ? 43 : $qualFlagFirb.hashCode()); Object $mitigationMainType = getMitigationMainType(); result = result * 59 + (($mitigationMainType == null) ? 43 : $mitigationMainType.hashCode()); Object $mitigationSmallType = getMitigationSmallType(); result = result * 59 + (($mitigationSmallType == null) ? 43 : $mitigationSmallType.hashCode()); Object $collateralAmount = getCollateralAmount(); result = result * 59 + (($collateralAmount == null) ? 43 : $collateralAmount.hashCode()); Object $currency = getCurrency(); result = result * 59 + (($currency == null) ? 43 : $currency.hashCode()); Object $originalMaturity = getOriginalMaturity(); result = result * 59 + (($originalMaturity == null) ? 43 : $originalMaturity.hashCode()); Object $residualMaturity = getResidualMaturity(); result = result * 59 + (($residualMaturity == null) ? 43 : $residualMaturity.hashCode()); Object $isZeroHaircut = getIsZeroHaircut(); result = result * 59 + (($isZeroHaircut == null) ? 43 : $isZeroHaircut.hashCode()); Object $factorLine = getFactorLine(); result = result * 59 + (($factorLine == null) ? 43 : $factorLine.hashCode()); Object $sh = getSh(); result = result * 59 + (($sh == null) ? 43 : $sh.hashCode()); Object $haircut = getHaircut(); result = result * 59 + (($haircut == null) ? 43 : $haircut.hashCode()); Object $hfx = getHfx(); result = result * 59 + (($hfx == null) ? 43 : $hfx.hashCode()); Object $supervisionClass = getSupervisionClass(); result = result * 59 + (($supervisionClass == null) ? 43 : $supervisionClass.hashCode()); Object $exposure = getExposure(); result = result * 59 + (($exposure == null) ? 43 : $exposure.hashCode()); Object $rw = getRw(); result = result * 59 + (($rw == null) ? 43 : $rw.hashCode()); Object $rwa = getRwa(); return result * 59 + (($rwa == null) ? 43 : $rwa.hashCode()); } public String toString() { return "SftCollateralDto(collateralId=" + getCollateralId() + ", approach=" + getApproach() + ", nettingFlag=" + getNettingFlag() + ", nettingId=" + getNettingId() + ", exposureId=" + getExposureId() + ", issuerId=" + getIssuerId() + ", issuerType=" + getIssuerType() + ", isApplyWa=" + getIsApplyWa() + ", isApplyFirb=" + getIsApplyFirb() + ", qualFlagWa=" + getQualFlagWa() + ", qualFlagFirb=" + getQualFlagFirb() + ", mitigationMainType=" + getMitigationMainType() + ", mitigationSmallType=" + getMitigationSmallType() + ", collateralAmount=" + getCollateralAmount() + ", currency=" + getCurrency() + ", originalMaturity=" + getOriginalMaturity() + ", residualMaturity=" + getResidualMaturity() + ", revaFrequency=" + getRevaFrequency() + ", isZeroHaircut=" + getIsZeroHaircut() + ", factorLine=" + getFactorLine() + ", sh=" + getSh() + ", haircut=" + getHaircut() + ", hfx=" + getHfx() + ", supervisionClass=" + getSupervisionClass() + ", exposure=" + getExposure() + ", rw=" + getRw() + ", rwa=" + getRwa() + ")"; }
/*    */ 
/*    */   
/* 10 */   public String getCollateralId() { return this.collateralId; }
/* 11 */   public String getApproach() { return this.approach; }
/* 12 */   public String getNettingFlag() { return this.nettingFlag; }
/* 13 */   public String getNettingId() { return this.nettingId; }
/* 14 */   public String getExposureId() { return this.exposureId; }
/* 15 */   public String getIssuerId() { return this.issuerId; }
/* 16 */   public String getIssuerType() { return this.issuerType; }
/* 17 */   public String getIsApplyWa() { return this.isApplyWa; }
/* 18 */   public String getIsApplyFirb() { return this.isApplyFirb; }
/* 19 */   public String getQualFlagWa() { return this.qualFlagWa; }
/* 20 */   public String getQualFlagFirb() { return this.qualFlagFirb; }
/* 21 */   public String getMitigationMainType() { return this.mitigationMainType; }
/* 22 */   public String getMitigationSmallType() { return this.mitigationSmallType; }
/* 23 */   public BigDecimal getCollateralAmount() { return this.collateralAmount; }
/* 24 */   public String getCurrency() { return this.currency; }
/* 25 */   public BigDecimal getOriginalMaturity() { return this.originalMaturity; }
/* 26 */   public BigDecimal getResidualMaturity() { return this.residualMaturity; }
/* 27 */   public Integer getRevaFrequency() { return this.revaFrequency; }
/* 28 */   public String getIsZeroHaircut() { return this.isZeroHaircut; }
/* 29 */   public BigDecimal getFactorLine() { return this.factorLine; }
/* 30 */   public BigDecimal getSh() { return this.sh; }
/* 31 */   public BigDecimal getHaircut() { return this.haircut; }
/* 32 */   public BigDecimal getHfx() { return this.hfx; }
/* 33 */   public String getSupervisionClass() { return this.supervisionClass; }
/* 34 */   public BigDecimal getExposure() { return this.exposure; }
/* 35 */   public BigDecimal getRw() { return this.rw; } public BigDecimal getRwa() {
/* 36 */     return this.rwa;
/*    */   } }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\SftCollateralDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */