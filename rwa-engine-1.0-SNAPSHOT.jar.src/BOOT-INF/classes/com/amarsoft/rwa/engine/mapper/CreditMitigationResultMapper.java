package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.amarsoft.rwa.engine.entity.CreditMitigationResultDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CreditMitigationResultMapper extends BaseMapper<CreditMitigationResultDo> {
  @Select({"select d.approach, '1' as qual_flag, d.mitigation_main_type, d.mitigation_small_type, d.mitigation_rpt_item_wa, d.mitigated_rw as rw, d.exposure_type_wa, d.exposure_type_irb, d.exposure_belong, 0 as mitigation_amount, sum(d.mitigation_use_amount) as mitigated_amount, sum(d.covered_ea) as covered_ea, sum(d.rwa_mb - d.rwa_ma) as mitigated_effect from rwa_er_nr_detail d where d.result_no = #{resultNo} and d.is_result = '1' and d.mitigated_flag = '1' and d.approach like '1%' group by d.approach, d.mitigation_main_type, d.mitigation_small_type, d.mitigation_rpt_item_wa, d.mitigated_rw, d.exposure_type_wa, d.exposure_type_irb, d.exposure_belong"})
  List<CreditMitigationResultDo> nrWaDetailResultList(@Param("resultNo") String paramString);
  
  @Select({"select d.approach, '1' as qual_flag, d.mitigation_main_type, d.mitigation_small_type, d.mitigation_rpt_item_wa, d.mitigated_rw as rw, d.exposure_type_wa, d.exposure_type_irb, d.exposure_belong, 0 as mitigation_amount, sum(d.mitigation_use_amount) as mitigated_amount, sum(d.covered_ea) as covered_ea, sum(d.rwa_mb - d.rwa_ma) as mitigated_effect from rwa_er_re_detail d where d.result_no = #{resultNo} and d.is_result = '1' and d.mitigated_flag = '1' and d.approach like '1%' group by d.approach, d.mitigation_main_type, d.mitigation_small_type, d.mitigation_rpt_item_wa, d.mitigated_rw, d.exposure_type_wa, d.exposure_type_irb, d.exposure_belong"})
  List<CreditMitigationResultDo> reWaDetailResultList(@Param("resultNo") String paramString);
  
  @Select({"select m.approach, m.qual_flag_wa as qual_flag, m.mitigation_main_type, m.mitigation_small_type, m.mitigation_rpt_item_wa, m.rw, sum(m.mitigation_amount) as mitigation_amount, 0 as mitigated_amount, 0 as covered_ea, 0 as mitigated_effect from rwa_er_nr_mitigation m where m.result_no = #{resultNo} and m.approach like '1%' group by m.approach, m.qual_flag_wa, m.mitigation_main_type, m.mitigation_small_type, m.mitigation_rpt_item_wa, m.rw"})
  List<CreditMitigationResultDo> nrWaTotalResultList(@Param("resultNo") String paramString);
  
  @Select({"select m.approach, m.qual_flag_wa as qual_flag, m.mitigation_main_type, m.mitigation_small_type, m.mitigation_rpt_item_wa, m.rw, sum(m.mitigation_amount) as mitigation_amount, 0 as mitigated_amount, 0 as covered_ea, 0 as mitigated_effect from rwa_er_re_mitigation m where m.result_no = #{resultNo} and m.approach like '1%' group by m.approach, m.qual_flag_wa, m.mitigation_main_type, m.mitigation_small_type, m.mitigation_rpt_item_wa, m.rw"})
  List<CreditMitigationResultDo> reWaTotalResultList(@Param("resultNo") String paramString);
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\CreditMitigationResultMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */