package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemProcessListener;

public class NullItemProcessListener<T, S> implements ItemProcessListener<T, S> {
  public void beforeProcess(T item) {}
  
  public void afterProcess(T item, S result) {}
  
  public void onProcessError(T item, Throwable e) {}
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */