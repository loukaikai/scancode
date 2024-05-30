package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemWriteListener;
import java.util.List;

public class NullItemWriteListener<S> implements ItemWriteListener<S> {
  public void beforeWrite(List<? extends S> resultList) {}
  
  public void afterWrite(List<? extends S> resultList) {}
  
  public void onWriteError(Throwable e, List<? extends S> resultList) {}
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemWriteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */