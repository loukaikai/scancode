package org.springframework.boot.loader.jar;

import org.springframework.boot.loader.data.RandomAccessData;

interface CentralDirectoryVisitor {
  void visitStart(CentralDirectoryEndRecord paramCentralDirectoryEndRecord, RandomAccessData paramRandomAccessData);
  
  void visitFileHeader(CentralDirectoryFileHeader paramCentralDirectoryFileHeader, long paramLong);
  
  void visitEnd();
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\jar\CentralDirectoryVisitor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */