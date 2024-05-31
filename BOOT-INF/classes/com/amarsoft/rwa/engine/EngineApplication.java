/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine;
/*    */ 
/*    */ import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
/*    */ import org.mybatis.spring.annotation.MapperScan;
/*    */ import org.springframework.boot.SpringApplication;
/*    */ import org.springframework.boot.autoconfigure.SpringBootApplication;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.retry.annotation.EnableRetry;
/*    */ import org.springframework.transaction.annotation.EnableTransactionManagement;
/*    */ import org.springframework.web.client.RestTemplate;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @SpringBootApplication
/*    */ @EnableTransactionManagement
/*    */ @MapperScan({"com.amarsoft.rwa.engine.mapper"})
/*    */ @EnableEncryptableProperties
/*    */ @EnableRetry
/*    */ public class EngineApplication
/*    */ {
/*    */   @Bean
/*    */   public RestTemplate restTemplate() {
/* 25 */     return new RestTemplate();
/*    */   }
/*    */   
/*    */   public static void main(String[] args) {
/* 29 */     SpringApplication.run(com.amarsoft.rwa.engine.EngineApplication.class, args);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\EngineApplication.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */