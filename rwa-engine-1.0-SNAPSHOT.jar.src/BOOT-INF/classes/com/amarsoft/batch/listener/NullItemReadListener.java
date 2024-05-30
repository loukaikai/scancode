package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemReadListener;

public class NullItemReadListener<T> implements ItemReadListener<T> {
  public void beforeRead() {}
  
  public void afterRead(T item) {}
  
  public void onReadError(Throwable e) {}
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemReadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */