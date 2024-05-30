package BOOT-INF.classes.com.amarsoft.rwa.engine.mapper;

import com.baomidou.mybatisplus.core.metadata.IPage;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface DataMapper {
  @Select({"${sql}"})
  List<LinkedHashMap<String, Object>> select(Map<String, Object> paramMap);
  
  @Insert({"${sql}"})
  int insert(Map<String, Object> paramMap);
  
  @Update({"${sql}"})
  int update(Map<String, Object> paramMap);
  
  @Delete({"${sql}"})
  int delete(Map<String, Object> paramMap);
  
  @Select({"${sql}"})
  IPage<LinkedHashMap<String, Object>> selectPage(IPage<LinkedHashMap<String, Object>> paramIPage, @Param("sql") String paramString);
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\mapper\DataMapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */