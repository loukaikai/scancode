package BOOT-INF.classes.com.amarsoft.batch;

import com.amarsoft.batch.exception.ItemStreamException;
import com.amarsoft.batch.support.ExecutionContext;

public interface ItemReader<T> {
  void open(ExecutionContext paramExecutionContext) throws ItemStreamException;
  
  T read() throws Exception;
  
  void close() throws ItemStreamException;
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */