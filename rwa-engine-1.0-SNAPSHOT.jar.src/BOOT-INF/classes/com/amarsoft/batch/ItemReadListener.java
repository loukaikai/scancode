package BOOT-INF.classes.com.amarsoft.batch;

public interface ItemReadListener<T> {
  void beforeRead();
  
  void afterRead(T paramT);
  
  void onReadError(Throwable paramThrowable);
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemReadListener.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */