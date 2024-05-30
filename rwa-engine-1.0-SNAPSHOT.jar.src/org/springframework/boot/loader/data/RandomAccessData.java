package org.springframework.boot.loader.data;

import java.io.IOException;
import java.io.InputStream;

public interface RandomAccessData {
  InputStream getInputStream() throws IOException;
  
  RandomAccessData getSubsection(long paramLong1, long paramLong2);
  
  byte[] read() throws IOException;
  
  byte[] read(long paramLong1, long paramLong2) throws IOException;
  
  long getSize();
}


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\data\RandomAccessData.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */