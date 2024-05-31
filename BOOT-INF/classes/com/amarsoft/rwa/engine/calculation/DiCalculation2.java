/*     */ package BOOT-INF.classes.com.amarsoft.rwa.engine.calculation;
/*     */ 
/*     */ import cn.hutool.core.util.NumberUtil;
/*     */ import cn.hutool.core.util.StrUtil;
/*     */ import com.amarsoft.rwa.engine.config.RwaConfig;
/*     */ import com.amarsoft.rwa.engine.constant.CreditDerivativeType;
/*     */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*     */ import com.amarsoft.rwa.engine.constant.Identity;
/*     */ import com.amarsoft.rwa.engine.constant.QualCcp;
/*     */ import com.amarsoft.rwa.engine.constant.TradingDirection;
/*     */ import com.amarsoft.rwa.engine.constant.TradingRole;
/*     */ import com.amarsoft.rwa.engine.entity.DiCollateralDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiExposureDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiNettingDto;
/*     */ import com.amarsoft.rwa.engine.entity.DiUnionDto;
/*     */ import com.amarsoft.rwa.engine.util.RwaMath;
/*     */ import java.math.BigDecimal;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.function.Function;
/*     */ import java.util.stream.Collectors;
/*     */ import org.slf4j.Logger;
/*     */ import org.slf4j.LoggerFactory;
/*     */ 
/*     */ public class DiCalculation2
/*     */ {
/*  27 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.calculation.DiCalculation2.class);
/*     */   private DiUnionDto unionDto;
/*     */   
/*  30 */   public DiUnionDto getUnionDto() { return this.unionDto; } public void setUnionDto(DiUnionDto unionDto) {
/*  31 */     this.unionDto = unionDto;
/*     */   }
/*     */   
/*     */   private DiCalculation2(DiUnionDto unionDto) {
/*  35 */     this.unionDto = unionDto;
/*     */   }
/*     */   
/*     */   public static com.amarsoft.rwa.engine.calculation.DiCalculation2 createCalculation(DiUnionDto unionDto) {
/*  39 */     return new com.amarsoft.rwa.engine.calculation.DiCalculation2(unionDto);
/*     */   }
/*     */ 
/*     */   
/*     */   public void execute() {
/*  44 */     DiNettingDto netting = this.unionDto.getNettingDto();
/*     */     
/*  46 */     Map<String, DiExposureDto> exposureMap = (Map<String, DiExposureDto>)this.unionDto.getExposureList().stream().collect(Collectors.toMap(DiExposureDto::getExposureId, Function.identity()));
/*     */ 
/*     */     
/*  49 */     for (DiCollateralDto collateral : this.unionDto.getCollateralList()) {
/*  50 */       DiExposureDto exposureDto = exposureMap.get(collateral.getExposureId());
/*  51 */       calculateCollateralAmount(collateral, this.unionDto.getNettingFlag(), netting, exposureDto);
/*     */       
/*  53 */       if (this.unionDto.getNettingFlag() == Identity.NO) {
/*  54 */         exposureDto.setNca(RwaMath.add(exposureDto.getNca(), collateral.getAhcAmount()));
/*     */         
/*     */         continue;
/*     */       } 
/*  58 */       netting.setNca(RwaMath.add(netting.getNca(), collateral.getAhcAmount()));
/*     */     } 
/*     */ 
/*     */ 
/*     */ 
/*     */     
/*  64 */     for (DiExposureDto exposure : this.unionDto.getExposureList()) {
/*     */       
/*  66 */       calculateExposure(exposure, this.unionDto.getNettingFlag(), this.unionDto.getApproach(), this.unionDto.getDataDate());
/*     */       
/*  68 */       if (this.unionDto.getNettingFlag() == Identity.YES) {
/*     */         
/*  70 */         netting.setNotionalPrincipal(RwaMath.add(netting.getNotionalPrincipal(), exposure.getNotionalPrincipal()));
/*     */         
/*  72 */         netting.setNmtAddOn(RwaMath.add(netting.getNmtAddOn(), exposure.getNmtAddOn()));
/*     */         
/*  74 */         netting.setWeightingMaturity(RwaMath.add(netting.getWeightingMaturity(), RwaMath.mul(exposure.getTeMaturity(), exposure.getNotionalPrincipal())));
/*     */         
/*  76 */         netting.setGrc(RwaMath.add(netting.getGrc(), NumberUtil.max(new BigDecimal[] { BigDecimal.ZERO, exposure.getMtm() })));
/*     */       } 
/*     */     } 
/*     */     
/*  80 */     if (this.unionDto.getNettingFlag() == Identity.NO) {
/*     */       return;
/*     */     }
/*     */     
/*  84 */     calculateNetting(netting, this.unionDto.getApproach());
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateCollateralAmount(DiCollateralDto collateral, Identity nettingFlag, DiNettingDto netting, DiExposureDto exposure) {
/*  89 */     if (StrUtil.equals(Identity.YES.getCode(), collateral.getIsOurBankSubmit())) {
/*  90 */       collateral.setAhcAmount(BigDecimal.ZERO);
/*     */ 
/*     */ 
/*     */ 
/*     */       
/*     */       return;
/*     */     } 
/*     */ 
/*     */ 
/*     */     
/* 100 */     if (!StrUtil.equals(Identity.YES.getCode(), collateral.getQualFlagFirb())) {
/* 101 */       collateral.setAhcAmount(BigDecimal.ZERO);
/*     */       return;
/*     */     } 
/* 104 */     Integer tm = null;
/* 105 */     String currency = null;
/* 106 */     Integer frequency = Integer.valueOf(RwaMath.nvl(collateral.getRevaFrequency(), this.unionDto.getSchemeConfig().getWaParamVersion().getCreditRule().getDefaultFrequency().intValue()));
/* 107 */     if (nettingFlag == Identity.NO) {
/* 108 */       tm = exposure.getTm();
/* 109 */       currency = exposure.getCurrency();
/*     */     } else {
/* 111 */       tm = netting.getTm();
/* 112 */       currency = netting.getCurrency();
/*     */     } 
/*     */ 
/*     */     
/* 116 */     collateral.setHaircut(RwaMath.adjustHaircut(collateral.getSh(), frequency, tm));
/*     */     
/* 118 */     collateral.setHfx(RwaMath.getHfx(currency, collateral.getCurrency(), frequency, tm, this.unionDto.getSchemeConfig().getWaParamVersion().getCreditRule().getShfx()));
/*     */     
/* 120 */     collateral.setAhcAmount(RwaMath.adjustMitigation(collateral.getCollateralAmount(), collateral.getHaircut(), collateral.getHfx()));
/* 121 */     if (collateral.getAhcAmount().compareTo(BigDecimal.ZERO) < 0)
/*     */     {
/* 123 */       collateral.setAhcAmount(BigDecimal.ZERO);
/*     */     }
/*     */   }
/*     */ 
/*     */ 
/*     */   
/*     */   public void calculateExposure(DiExposureDto exposure, Identity nettingFlag, ExposureApproach approach, Date now) {
/* 130 */     exposure.setTsMaturity(BigDecimal.valueOf(RwaConfig.getWorkDays(now, exposure.getStartDate())));
/*     */     
/* 132 */     exposure.setTeMaturity(BigDecimal.valueOf(RwaConfig.getWorkDays(now, exposure.getDueDate())));
/*     */     
/* 134 */     exposure.setNotionalPrincipal(exposure.getNotionalPrincipal1());
/*     */     
/* 136 */     exposure.setNmtAddOn(RwaMath.mul(exposure.getNotionalPrincipal(), exposure.getAddonFactor()));
/*     */ 
/*     */     
/* 139 */     if (StrUtil.equals(exposure.getCreditDerivativeType(), CreditDerivativeType.CDS.getCode()) && 
/* 140 */       StrUtil.equals(exposure.getTradingDirection(), TradingDirection.SELL.getCode())) {
/* 141 */       exposure.setNmtAddOn(BigDecimal.ZERO);
/*     */     }
/*     */     
/* 144 */     if (nettingFlag == Identity.NO) {
/*     */       
/* 146 */       exposure.setEad(RwaMath.getEadOfDiCem(exposure.getMtm(), exposure.getNmtAddOn()));
/*     */       
/* 148 */       if (StrUtil.equals(exposure.getCentralClearFlag(), Identity.YES.getCode()) && 
/* 149 */         StrUtil.equals(exposure.getTradingRole(), TradingRole.MEMBER.getCode()) && 
/* 150 */         !StrUtil.equals(exposure.getQualCcpFlag(), QualCcp.QUAL_CCP.getCode())) {
/*     */         
/* 152 */         if (exposure.getEadHaircut() == null) {
/* 153 */           exposure.setEadHaircut(BigDecimal.ONE);
/*     */         }
/* 155 */         exposure.setEad(RwaMath.mul(exposure.getEad(), exposure.getEadHaircut()));
/*     */       } 
/*     */       
/* 158 */       exposure.setMitigatedEad(RwaMath.getMitigatedEad(exposure.getEad(), exposure.getNca()));
/*     */       
/* 160 */       exposure.setTransactionMaturity(RwaMath.getEffectiveMaturity(exposure.getTeMaturity(), exposure.getTm()));
/*     */       
/* 162 */       exposure.setDiscountFactor(RwaMath.getDiscountFactor(exposure.getTransactionMaturity(), exposure.getTm()));
/*     */       
/* 164 */       exposure.setRwa(RwaMath.getRwa(approach, exposure.getEad(), exposure.getRw(), exposure.getKcr()));
/*     */     } 
/*     */   }
/*     */ 
/*     */   
/*     */   public void calculateNetting(DiNettingDto netting, ExposureApproach approach) {
/* 170 */     netting.setNgr(RwaMath.getNgr(netting.getMtm(), netting.getGrc()));
/*     */     
/* 172 */     netting.setAnet(RwaMath.getAnet(netting.getNmtAddOn(), netting.getNgr()));
/*     */     
/* 174 */     netting.setEad(RwaMath.getNettingEad(netting.getMtm(), netting.getAnet()));
/* 175 */     netting.setMitigatedEad(RwaMath.getMitigatedEad(netting.getEad(), netting.getNca()));
/*     */     
/* 177 */     netting.setRwa(RwaMath.getRwa(approach, netting.getEad(), netting.getRw(), netting.getKcr()));
/*     */     
/* 179 */     netting.setNettingMaturity(RwaMath.getEffectiveMaturity(RwaMath.div(netting.getWeightingMaturity(), netting.getNotionalPrincipal()), netting.getTm()));
/*     */     
/* 181 */     netting.setDiscountFactor(RwaMath.getDiscountFactor(netting.getNettingMaturity(), netting.getTm()));
/*     */     
/* 183 */     if (netting.getNca() == null)
/* 184 */       netting.setNca(BigDecimal.ZERO); 
/*     */   }
/*     */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\calculation\DiCalculation2.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */