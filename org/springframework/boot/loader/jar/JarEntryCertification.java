/*    */ package org.springframework.boot.loader.jar;
/*    */ 
/*    */ import java.security.CodeSigner;
/*    */ import java.security.cert.Certificate;
/*    */ import java.util.jar.JarEntry;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ class JarEntryCertification
/*    */ {
/* 30 */   static final JarEntryCertification NONE = new JarEntryCertification(null, null);
/*    */   
/*    */   private final Certificate[] certificates;
/*    */   
/*    */   private final CodeSigner[] codeSigners;
/*    */   
/*    */   JarEntryCertification(Certificate[] certificates, CodeSigner[] codeSigners) {
/* 37 */     this.certificates = certificates;
/* 38 */     this.codeSigners = codeSigners;
/*    */   }
/*    */   
/*    */   Certificate[] getCertificates() {
/* 42 */     return (this.certificates != null) ? (Certificate[])this.certificates.clone() : null;
/*    */   }
/*    */   
/*    */   CodeSigner[] getCodeSigners() {
/* 46 */     return (this.codeSigners != null) ? (CodeSigner[])this.codeSigners.clone() : null;
/*    */   }
/*    */   
/*    */   static JarEntryCertification from(JarEntry certifiedEntry) {
/* 50 */     Certificate[] certificates = (certifiedEntry != null) ? certifiedEntry.getCertificates() : null;
/* 51 */     CodeSigner[] codeSigners = (certifiedEntry != null) ? certifiedEntry.getCodeSigners() : null;
/* 52 */     if (certificates == null && codeSigners == null) {
/* 53 */       return NONE;
/*    */     }
/* 55 */     return new JarEntryCertification(certificates, codeSigners);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\org\springframework\boot\loader\jar\JarEntryCertification.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */