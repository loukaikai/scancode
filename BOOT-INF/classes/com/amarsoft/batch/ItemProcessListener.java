package BOOT-INF.classes.com.amarsoft.batch;

public interface ItemProcessListener<T, S> {
  void beforeProcess(T paramT);
  
  void afterProcess(T paramT, S paramS);
  
  void onProcessError(T paramT, Throwable paramThrowable);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */