package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemReadListener;

public class NullItemReadListener<T> implements ItemReadListener<T> {
  public void beforeRead() {}
  
  public void afterRead(T item) {}
  
  public void onReadError(Throwable e) {}
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemReadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */