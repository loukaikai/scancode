package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.amarsoft.rwa.engine.entity.RiskDataPeriodDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface RiskDataPeriodMapper extends BaseMapper<RiskDataPeriodDo> {
  @Update({"update RWA_EL_Risk set group_flag = #{groupFlag} where data_batch_no = #{dataBatchNo}"})
  int updateGroupFlag(@Param("dataBatchNo") String paramString1, @Param("groupFlag") String paramString2);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\RiskDataPeriodMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */