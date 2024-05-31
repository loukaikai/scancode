package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.amarsoft.rwa.engine.entity.CreditOrgRwaDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CreditOrgRwaMapper extends BaseMapper<CreditOrgRwaDo> {
  @Select({"select r.org_id, sum(r.rwa_adj) as rwa_amp, sum(case when amp_approach = '10' then r.rwa end) as rwa_amp_pene, sum(case when amp_approach = '20' then r.rwa end) as rwa_amp_aba, sum(case when amp_approach = '30' then r.rwa end) as rwa_amp_f1250, sum(r.rwa_cp) as rwa_amp_cp, sum(r.cva) as cva_amp, sum(r.rwa_aa) as rwa_amp_aa from RWA_ER_Amp_Exposure r where r.result_no = #{resultNo} group by r.org_id"})
  List<CreditOrgRwaDo> ampResultList(@Param("resultNo") String paramString);
  
  @Select({"select r.org_id, sum(r.rwa_adj) as rwa_amp, sum(case when amp_approach = '10' then r.rwa end) as rwa_amp_pene, sum(case when amp_approach = '20' then r.rwa end) as rwa_amp_aba, sum(case when amp_approach = '30' then r.rwa end) as rwa_amp_f1250, sum(r.rwa_cp) as rwa_amp_cp, sum(r.cva) as cva_amp, sum(r.rwa_aa) as rwa_amp_aa from RWA_ESR_Amp_Exposure r where r.result_no = #{resultNo} group by r.org_id"})
  List<CreditOrgRwaDo> ampSingleResultList(@Param("resultNo") String paramString);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\CreditOrgRwaMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */