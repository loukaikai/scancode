package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemProcessListener;

public class NullItemProcessListener<T, S> implements ItemProcessListener<T, S> {
  public void beforeProcess(T item) {}
  
  public void afterProcess(T item, S result) {}
  
  public void onProcessError(T item, Throwable e) {}
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */