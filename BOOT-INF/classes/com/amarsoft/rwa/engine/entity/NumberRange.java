/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.entity;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.constant.LeftNotation;
/*    */ import com.amarsoft.rwa.engine.constant.RightNotation;
/*    */ import java.math.BigDecimal;
/*    */ 
/*    */ 
/*    */ public class NumberRange
/*    */ {
/*    */   private LeftNotation left;
/*    */   private BigDecimal min;
/*    */   private BigDecimal max;
/*    */   private RightNotation right;
/*    */   
/*    */   public void setLeft(LeftNotation left) {
/* 16 */     this.left = left; } public void setMin(BigDecimal min) { this.min = min; } public void setMax(BigDecimal max) { this.max = max; } public void setRight(RightNotation right) { this.right = right; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof com.amarsoft.rwa.engine.entity.NumberRange)) return false;  com.amarsoft.rwa.engine.entity.NumberRange other = (com.amarsoft.rwa.engine.entity.NumberRange)o; if (!other.canEqual(this)) return false;  Object this$left = getLeft(), other$left = other.getLeft(); if ((this$left == null) ? (other$left != null) : !this$left.equals(other$left)) return false;  Object this$min = getMin(), other$min = other.getMin(); if ((this$min == null) ? (other$min != null) : !this$min.equals(other$min)) return false;  Object this$max = getMax(), other$max = other.getMax(); if ((this$max == null) ? (other$max != null) : !this$max.equals(other$max)) return false;  Object this$right = getRight(), other$right = other.getRight(); return !((this$right == null) ? (other$right != null) : !this$right.equals(other$right)); } protected boolean canEqual(Object other) { return other instanceof com.amarsoft.rwa.engine.entity.NumberRange; } public int hashCode() { int PRIME = 59; result = 1; Object $left = getLeft(); result = result * 59 + (($left == null) ? 43 : $left.hashCode()); Object $min = getMin(); result = result * 59 + (($min == null) ? 43 : $min.hashCode()); Object $max = getMax(); result = result * 59 + (($max == null) ? 43 : $max.hashCode()); Object $right = getRight(); return result * 59 + (($right == null) ? 43 : $right.hashCode()); } public NumberRange(LeftNotation left, BigDecimal min, BigDecimal max, RightNotation right) {
/* 17 */     this.left = left; this.min = min; this.max = max; this.right = right;
/*    */   }
/*    */   
/*    */   public NumberRange() {}
/* 21 */   public LeftNotation getLeft() { return this.left; }
/* 22 */   public BigDecimal getMin() { return this.min; }
/* 23 */   public BigDecimal getMax() { return this.max; } public RightNotation getRight() {
/* 24 */     return this.right;
/*    */   }
/*    */   
/*    */   public String toString() {
/* 28 */     String l = "";
/* 29 */     if (this.min == null) {
/* 30 */       l = "-inf";
/*    */     } else {
/* 32 */       l = this.min.stripTrailingZeros().toPlainString();
/*    */     } 
/* 34 */     String r = "";
/* 35 */     if (this.max == null) {
/* 36 */       r = "+inf";
/*    */     } else {
/* 38 */       r = this.max.stripTrailingZeros().toPlainString();
/*    */     } 
/* 40 */     return this.left.getCode() + l + "," + r + this.right.getCode();
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\entity\NumberRange.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */