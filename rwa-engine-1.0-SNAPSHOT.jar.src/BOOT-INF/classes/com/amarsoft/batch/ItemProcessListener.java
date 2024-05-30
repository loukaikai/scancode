package BOOT-INF.classes.com.amarsoft.batch;

public interface ItemProcessListener<T, S> {
  void beforeProcess(T paramT);
  
  void afterProcess(T paramT, S paramS);
  
  void onProcessError(T paramT, Throwable paramThrowable);
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemProcessListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */