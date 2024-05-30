package BOOT-INF.classes.com.amarsoft.batch;

import com.amarsoft.batch.exception.ItemStreamException;
import com.amarsoft.batch.support.ExecutionContext;

public interface ItemReader<T> {
  void open(ExecutionContext paramExecutionContext) throws ItemStreamException;
  
  T read() throws Exception;
  
  void close() throws ItemStreamException;
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemReader.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */