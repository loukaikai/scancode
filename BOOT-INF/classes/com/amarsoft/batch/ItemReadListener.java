package BOOT-INF.classes.com.amarsoft.batch;

public interface ItemReadListener<T> {
  void beforeRead();
  
  void afterRead(T paramT);
  
  void onReadError(Throwable paramThrowable);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemReadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */