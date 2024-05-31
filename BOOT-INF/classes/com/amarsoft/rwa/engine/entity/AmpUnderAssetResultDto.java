/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ public class AmpUnderAssetResultDto
/*    */ {
/*    */   private String id;
/*    */   private String type;
/*    */   private String flag;
/*    */   private BigDecimal ratio;
/*    */   private BigDecimal ab;
/*    */   
/*    */   public void setId(String id) {
/* 14 */     this.id = id; } private BigDecimal prl; private BigDecimal ead; private BigDecimal rw; private BigDecimal rwa; private BigDecimal cp; private BigDecimal cva; public void setType(String type) { this.type = type; } public void setFlag(String flag) { this.flag = flag; } public void setRatio(BigDecimal ratio) { this.ratio = ratio; } public void setAb(BigDecimal ab) { this.ab = ab; } public void setPrl(BigDecimal prl) { this.prl = prl; } public void setEad(BigDecimal ead) { this.ead = ead; } public void setRw(BigDecimal rw) { this.rw = rw; } public void setRwa(BigDecimal rwa) { this.rwa = rwa; } public void setCp(BigDecimal cp) { this.cp = cp; } public void setCva(BigDecimal cva) { this.cva = cva; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.AmpUnderAssetResultDto)) return false;  com.amarsoft.rwa.engine.entity.AmpUnderAssetResultDto other = (com.amarsoft.rwa.engine.entity.AmpUnderAssetResultDto)o; if (!other.canEqual(this)) return false;  Object this$id = getId(), other$id = other.getId(); if ((this$id == null) ? (other$id != null) : !this$id.equals(other$id)) return false;  Object this$type = getType(), other$type = other.getType(); if ((this$type == null) ? (other$type != null) : !this$type.equals(other$type)) return false;  Object this$flag = getFlag(), other$flag = other.getFlag(); if ((this$flag == null) ? (other$flag != null) : !this$flag.equals(other$flag)) return false;  Object this$ratio = getRatio(), other$ratio = other.getRatio(); if ((this$ratio == null) ? (other$ratio != null) : !this$ratio.equals(other$ratio)) return false;  Object this$ab = getAb(), other$ab = other.getAb(); if ((this$ab == null) ? (other$ab != null) : !this$ab.equals(other$ab)) return false;  Object this$prl = getPrl(), other$prl = other.getPrl(); if ((this$prl == null) ? (other$prl != null) : !this$prl.equals(other$prl)) return false;  Object this$ead = getEad(), other$ead = other.getEad(); if ((this$ead == null) ? (other$ead != null) : !this$ead.equals(other$ead)) return false;  Object this$rw = getRw(), other$rw = other.getRw(); if ((this$rw == null) ? (other$rw != null) : !this$rw.equals(other$rw)) return false;  Object this$rwa = getRwa(), other$rwa = other.getRwa(); if ((this$rwa == null) ? (other$rwa != null) : !this$rwa.equals(other$rwa)) return false;  Object this$cp = getCp(), other$cp = other.getCp(); if ((this$cp == null) ? (other$cp != null) : !this$cp.equals(other$cp)) return false;  Object this$cva = getCva(), other$cva = other.getCva(); return !((this$cva == null) ? (other$cva != null) : !this$cva.equals(other$cva)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.AmpUnderAssetResultDto; } public int hashCode() { int PRIME = 59; result = 1; Object $id = getId(); result = result * 59 + (($id == null) ? 43 : $id.hashCode()); Object $type = getType(); result = result * 59 + (($type == null) ? 43 : $type.hashCode()); Object $flag = getFlag(); result = result * 59 + (($flag == null) ? 43 : $flag.hashCode()); Object $ratio = getRatio(); result = result * 59 + (($ratio == null) ? 43 : $ratio.hashCode()); Object $ab = getAb(); result = result * 59 + (($ab == null) ? 43 : $ab.hashCode()); Object $prl = getPrl(); result = result * 59 + (($prl == null) ? 43 : $prl.hashCode()); Object $ead = getEad(); result = result * 59 + (($ead == null) ? 43 : $ead.hashCode()); Object $rw = getRw(); result = result * 59 + (($rw == null) ? 43 : $rw.hashCode()); Object $rwa = getRwa(); result = result * 59 + (($rwa == null) ? 43 : $rwa.hashCode()); Object $cp = getCp(); result = result * 59 + (($cp == null) ? 43 : $cp.hashCode()); Object $cva = getCva(); return result * 59 + (($cva == null) ? 43 : $cva.hashCode()); } public String toString() { return "AmpUnderAssetResultDto(id=" + getId() + ", type=" + getType() + ", flag=" + getFlag() + ", ratio=" + getRatio() + ", ab=" + getAb() + ", prl=" + getPrl() + ", ead=" + getEad() + ", rw=" + getRw() + ", rwa=" + getRwa() + ", cp=" + getCp() + ", cva=" + getCva() + ")"; }
/*    */    public AmpUnderAssetResultDto() {} public AmpUnderAssetResultDto(String id, String type, String flag, BigDecimal ratio, BigDecimal ab, BigDecimal prl, BigDecimal ead, BigDecimal rw, BigDecimal rwa, BigDecimal cp, BigDecimal cva) {
/* 16 */     this.id = id; this.type = type; this.flag = flag; this.ratio = ratio; this.ab = ab; this.prl = prl; this.ead = ead; this.rw = rw; this.rwa = rwa; this.cp = cp; this.cva = cva;
/*    */   }
/*    */   
/* 19 */   public String getId() { return this.id; }
/* 20 */   public String getType() { return this.type; }
/* 21 */   public String getFlag() { return this.flag; }
/* 22 */   public BigDecimal getRatio() { return this.ratio; }
/* 23 */   public BigDecimal getAb() { return this.ab; }
/* 24 */   public BigDecimal getPrl() { return this.prl; }
/* 25 */   public BigDecimal getEad() { return this.ead; }
/* 26 */   public BigDecimal getRw() { return this.rw; }
/* 27 */   public BigDecimal getRwa() { return this.rwa; }
/* 28 */   public BigDecimal getCp() { return this.cp; } public BigDecimal getCva() {
/* 29 */     return this.cva;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\AmpUnderAssetResultDto.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */