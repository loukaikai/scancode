package BOOT-INF.classes.com.amarsoft.batch;

import java.util.List;

public interface ItemWriteListener<S> {
  void beforeWrite(List<? extends S> paramList);
  
  void afterWrite(List<? extends S> paramList);
  
  void onWriteError(Throwable paramThrowable, List<? extends S> paramList);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemWriteListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */