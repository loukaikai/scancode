package BOOT-INF.classes.com.amarsoft.batch;

import java.util.List;

public interface ItemWriter<T> {
  void init();
  
  void write(List<? extends T> paramList) throws Exception;
  
  void close();
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemWriter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */