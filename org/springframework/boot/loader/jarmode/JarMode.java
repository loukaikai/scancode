package org.springframework.boot.loader.jarmode;

public interface JarMode {
  boolean accepts(String paramString);
  
  void run(String paramString, String[] paramArrayOfString);
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\jarmode\JarMode.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */