package BOOT-INF.classes.com.amarsoft.batch.listener;

import com.amarsoft.batch.ItemWriteListener;
import java.util.List;

public class NullItemWriteListener<S> implements ItemWriteListener<S> {
  public void beforeWrite(List<? extends S> resultList) {}
  
  public void afterWrite(List<? extends S> resultList) {}
  
  public void onWriteError(Throwable e, List<? extends S> resultList) {}
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\listener\NullItemWriteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */