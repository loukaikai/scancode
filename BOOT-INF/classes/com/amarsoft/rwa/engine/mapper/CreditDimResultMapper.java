package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.amarsoft.rwa.engine.entity.CreditDimResultDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CreditDimResultMapper extends BaseMapper<CreditDimResultDo> {
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa_mb) as rwa_mb, sum(r.rwa_ma) as rwa_ma, 0 as ela, sum(r.ec) as ec, sum(r.provision_ded) as provision, sum(case when r.exposure_belong = '1' then r.uncovered_ea else r.uncovered_ea * r.ccf end) as ead_um from rwa_er_nr_exposure r join rwa_ei_nr_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> nrWaDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, sum(r.provision_ded) as provision, sum(r.ead) as ead_um from RWA_ER_Amp_Exposure r join RWA_EI_Amp_Exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} and r.credit_risk_data_type = '24' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ampWaNpDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, #{lraItemNo} as exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, 0 as asset_balance, 0 as exposure, sum(r.rwa_aa) as rwa_mb, sum(r.rwa_aa) as rwa_ma, 0 as ela, 0 as ec, 0 as provision, 0 as ead_um from RWA_ER_Amp_Exposure r join RWA_EI_Amp_Exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ampLraDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2, @Param("lraItemNo") String paramString3);
  
  @Select({"select r.approach, r.org_id, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, sum(r.provision_ded) as provision, sum(r.ead) as ead_um from RWA_ESR_Amp_Exposure r where r.result_no = #{resultNo} and r.credit_risk_data_type = '24' group by r.approach, r.org_id, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ampWaNpSingleDimResultList(@Param("resultNo") String paramString);
  
  @Select({"select r.approach, r.org_id, r.asset_type, r.exposure_type_wa, #{lraItemNo} as exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, 0 as asset_balance, 0 as exposure, sum(r.rwa_aa) as rwa_mb, sum(r.rwa_aa) as rwa_ma, 0 as ela, 0 as ec, 0 as provision, 0 as ead_um from RWA_ESR_Amp_Exposure r where r.result_no = #{resultNo} group by r.approach, r.org_id, r.asset_type, r.exposure_type_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ampLraSingleDimResultList(@Param("resultNo") String paramString1, @Param("lraItemNo") String paramString2);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa_mb) as rwa_mb, sum(r.rwa_ma) as rwa_ma, 0 as ela, sum(r.ec) as ec, sum(r.provision_ded) as provision, sum(case when r.exposure_belong = '1' then r.uncovered_ea else r.uncovered_ea * r.ccf end) as ead_um from rwa_er_re_exposure r join rwa_ei_re_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> reWaDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select r.approach, r.org_id, null as industry_id, e.business_line, r.asset_type, r.exposure_type_wa, #{itemNo} as exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa_mb) as rwa_mb, sum(r.rwa_adj) as rwa_ma, 0 as ela, sum(r.ec) as ec, sum(r.provision_ded) as provision, sum(case when r.exposure_belong = '1' then r.uncovered_ea else r.uncovered_ea * r.ccf end) as ead_um from rwa_er_abs_exposure r join rwa_ei_abs_exposure e on e.data_batch_no = #{dataBatchNo} and e.abs_exposure_id = r.abs_exposure_id where r.result_no = #{resultNo} group by r.approach, r.org_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> absDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2, @Param("itemNo") String paramString3);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.notional_principal) as asset_balance, sum(r.ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, 0 as provision, sum(r.ead) as ead_um from rwa_er_di_netting r join rwa_ei_di_netting e on e.data_batch_no = #{dataBatchNo} and e.netting_id = r.netting_id where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong union all select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.notional_principal) as asset_balance, sum(r.ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, 0 as provision, sum(r.ead) as ead_um from rwa_er_di_exposure r join rwa_ei_di_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.netting_flag = '0' where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> diWaDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.mitigated_ea) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, 0 as provision, sum(r.mitigated_ea) as ead_um from rwa_er_sft_netting r join rwa_ei_sft_netting e on e.data_batch_no = #{dataBatchNo} and e.netting_id = r.netting_id where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong union all select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.mitigated_ea) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, 0 as provision, sum(r.mitigated_ea) as ead_um from rwa_er_sft_exposure r join rwa_ei_sft_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id and e.netting_flag = '0' where r.result_no = #{resultNo} and r.approach like '1%' group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.rw, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> sftWaDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select '101' as approach, e.org_id, e.industry_id, e.business_line, e.asset_type, cl.supervision_class as exposure_rpt_item_wa, cl.rw, e.exposure_belong, #{collExpoTypeWa} as exposure_type_wa, #{collExpoTypeIrb} as exposure_type_irb, 0 as asset_balance, sum(cl.exposure) as exposure, sum(cl.rwa) as rwa_mb, sum(cl.rwa) as rwa_ma, 0 as ela, 0 as provision, sum(cl.exposure) as ead_um from rwa_er_sft_collateral cl left join rwa_ei_sft_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = cl.exposure_id where cl.result_no = #{resultNo} and cl.exposure > 0 group by e.org_id, e.industry_id, e.business_line, e.asset_type, cl.supervision_class, cl.rw, e.exposure_belong "})
  List<CreditDimResultDo> sftBankCollDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2, @Param("collExpoTypeWa") String paramString3, @Param("collExpoTypeIrb") String paramString4);
  
  @Select({"select r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.df) as asset_balance, sum(r.df) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec, 0 as provision, sum(r.df) as ead_um from rwa_er_ccp_df r join rwa_ei_ccp_df e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} group by r.approach, r.org_id, e.industry_id, e.business_line, r.asset_type, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ccpDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2);
  
  @Select({"select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa_mb) as rwa_mb, sum(r.rwa_ma) as rwa_ma, sum(r.ela) as ela, sum(r.ec) as ec from rwa_esr_ge_exposure r join rwa_esi_ge_exposure e on e.data_batch_no = #{dataBatchNo} and e.exposure_id = r.exposure_id where r.result_no = #{resultNo} group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> geSingleDimResultList(@Param("resultNo") String paramString1, @Param("dataBatchNo") String paramString2, @Param("orgId") String paramString3);
  
  @Select({"select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_type_irb, r.exposure_belong, #{itemNo} as exposure_rpt_item_wa, sum(r.asset_balance) as asset_balance, sum(r.ead) as exposure, sum(r.rwa_mb) as rwa_mb, sum(r.rwa_adj) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_abs_exposure r where r.result_no = #{resultNo} group by r.approach, r.exposure_type_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> absSingleDimResultList(@Param("resultNo") String paramString1, @Param("orgId") String paramString2, @Param("itemNo") String paramString3);
  
  @Select({"select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.notional_principal+r.coll_ead) as asset_balance, sum(r.ead+r.coll_ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_di_exposure r where r.result_no = #{resultNo} and r.netting_flag = '0' group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong union all select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.notional_principal+r.coll_ead) as asset_balance, sum(r.ead+r.coll_ead) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_di_netting r where r.result_no = #{resultNo} group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> diSingleDimResultList(@Param("resultNo") String paramString1, @Param("orgId") String paramString2);
  
  @Select({"select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.mitigated_ea+r.coll_ead) as exposure, sum(r.rwa+r.coll_rwa) as rwa_mb, sum(r.rwa+r.coll_rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_sft_exposure r where r.result_no = #{resultNo} and r.netting_flag = '0' group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong union all select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.asset_balance) as asset_balance, sum(r.mitigated_ea) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_sft_netting r where r.result_no = #{resultNo} group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> sftSingleDimResultList(@Param("resultNo") String paramString1, @Param("orgId") String paramString2);
  
  @Select({"select #{orgId} as org_id, r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong, sum(r.df) as asset_balance, sum(r.df) as exposure, sum(r.rwa) as rwa_mb, sum(r.rwa) as rwa_ma, 0 as ela, sum(r.ec) as ec from rwa_esr_ccp_df r where r.result_no = #{resultNo} group by r.approach, r.exposure_type_wa, r.exposure_rpt_item_wa, r.exposure_type_irb, r.exposure_belong"})
  List<CreditDimResultDo> ccpSingleDimResultList(@Param("resultNo") String paramString1, @Param("orgId") String paramString2);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\CreditDimResultMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */