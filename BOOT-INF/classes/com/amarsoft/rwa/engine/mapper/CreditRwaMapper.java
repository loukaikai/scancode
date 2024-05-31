package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.amarsoft.rwa.engine.entity.CreditRwaDo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CreditRwaMapper extends BaseMapper<CreditRwaDo> {
  @Delete({"delete from RWA_ER_CVA where result_no = #{resultNo} and cva_type = #{cvaType}"})
  int deleteCvaResult(@Param("resultNo") String paramString1, @Param("cvaType") String paramString2);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\CreditRwaMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */