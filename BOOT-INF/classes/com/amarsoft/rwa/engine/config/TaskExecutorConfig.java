/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ 
/*    */ import java.util.concurrent.ThreadPoolExecutor;
/*    */ import org.springframework.beans.factory.annotation.Value;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.scheduling.annotation.EnableAsync;
/*    */ import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ @EnableAsync
/*    */ public class TaskExecutorConfig
/*    */ {
/*    */   @Value("${spring.task.execution.pool.core-size}")
/*    */   private int corePoolSize;
/*    */   @Value("${spring.task.execution.pool.max-size}")
/*    */   private int maxPoolSize;
/*    */   @Value("${spring.task.execution.pool.queue-capacity}")
/*    */   private int queueCapacity;
/*    */   @Value("${spring.task.execution.pool.keep-alive}")
/*    */   private int keepAliveSeconds;
/*    */   
/*    */   @Bean({"myTaskExecutor"})
/*    */   public ThreadPoolTaskExecutor myTaskExecutor() {
/* 31 */     ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
/* 32 */     taskExecutor.initialize();
/* 33 */     taskExecutor.setCorePoolSize(this.corePoolSize);
/* 34 */     taskExecutor.setMaxPoolSize(this.maxPoolSize);
/* 35 */     taskExecutor.setQueueCapacity(this.queueCapacity);
/* 36 */     taskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
/* 37 */     taskExecutor.setThreadNamePrefix("async-task-");
/* 38 */     taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
/* 39 */     return taskExecutor;
/*    */   }
/*    */   
/*    */   @Bean({"jobTaskExecutor"})
/*    */   public ThreadPoolTaskExecutor jobTaskExecutor() {
/* 44 */     ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
/* 45 */     taskExecutor.initialize();
/* 46 */     taskExecutor.setCorePoolSize(this.corePoolSize);
/* 47 */     taskExecutor.setMaxPoolSize(this.maxPoolSize);
/* 48 */     taskExecutor.setQueueCapacity(this.queueCapacity);
/* 49 */     taskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
/* 50 */     taskExecutor.setThreadNamePrefix("async-job-");
/* 51 */     taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
/* 52 */     return taskExecutor;
/*    */   }
/*    */   
/*    */   @Bean({"itlTaskExecutor"})
/*    */   public ThreadPoolTaskExecutor itlTaskExecutor() {
/* 57 */     ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
/* 58 */     taskExecutor.initialize();
/* 59 */     taskExecutor.setCorePoolSize(this.corePoolSize);
/* 60 */     taskExecutor.setMaxPoolSize(this.maxPoolSize);
/* 61 */     taskExecutor.setQueueCapacity(this.queueCapacity);
/* 62 */     taskExecutor.setKeepAliveSeconds(this.keepAliveSeconds);
/* 63 */     taskExecutor.setThreadNamePrefix("async-itl-");
/* 64 */     taskExecutor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
/* 65 */     return taskExecutor;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\TaskExecutorConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */