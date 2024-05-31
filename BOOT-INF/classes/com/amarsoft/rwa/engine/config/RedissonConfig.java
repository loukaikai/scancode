/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.config;
/*    */ 
/*    */ import cn.hutool.core.util.StrUtil;
/*    */ import org.redisson.Redisson;
/*    */ import org.redisson.api.RedissonClient;
/*    */ import org.redisson.config.ClusterServersConfig;
/*    */ import org.redisson.config.Config;
/*    */ import org.redisson.config.SentinelServersConfig;
/*    */ import org.redisson.config.SingleServerConfig;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.context.annotation.Bean;
/*    */ import org.springframework.context.annotation.Configuration;
/*    */ import org.springframework.core.env.Environment;
/*    */ 
/*    */ 
/*    */ 
/*    */ @Configuration
/*    */ public class RedissonConfig
/*    */ {
/*    */   @Autowired
/*    */   private Environment environment;
/*    */   
/*    */   @Bean(destroyMethod = "shutdown")
/*    */   public RedissonClient redisson() {
/* 25 */     String host = this.environment.getProperty("spring.redis.host");
/* 26 */     String port = this.environment.getProperty("spring.redis.port");
/* 27 */     String password = this.environment.getProperty("spring.redis.password");
/* 28 */     int threads = ((Integer)this.environment.getProperty("spring.redis.threads", Integer.class)).intValue();
/* 29 */     int database = ((Integer)this.environment.getProperty("spring.redis.database", Integer.class)).intValue();
/* 30 */     String masterName = this.environment.getProperty("spring.redis.sentinel.master");
/* 31 */     String sentinelNodes = this.environment.getProperty("spring.redis.sentinel.nodes");
/* 32 */     String clusterNodes = this.environment.getProperty("spring.redis.cluster.nodes");
/* 33 */     Config config = new Config();
/* 34 */     if (StrUtil.isNotEmpty(masterName)) {
/*    */       
/* 36 */       String[] nodes = sentinelNodes.split(",");
/* 37 */       for (int i = 0; i < nodes.length; i++) {
/* 38 */         String node = "redis://" + nodes[i];
/* 39 */         nodes[i] = node;
/*    */       } 
/* 41 */       ((SentinelServersConfig)config.useSentinelServers()
/* 42 */         .setMasterName(masterName)
/* 43 */         .setPassword(password))
/* 44 */         .setScanInterval(2000)
/* 45 */         .setDatabase(database)
/* 46 */         .addSentinelAddress(nodes);
/* 47 */       config.setThreads(threads);
/* 48 */     } else if (StrUtil.isNotEmpty(clusterNodes)) {
/*    */       
/* 50 */       String[] nodes = clusterNodes.split(",");
/* 51 */       for (int i = 0; i < nodes.length; i++) {
/* 52 */         String node = "redis://" + nodes[i];
/* 53 */         nodes[i] = node;
/*    */       } 
/* 55 */       ((ClusterServersConfig)config.useClusterServers()
/* 56 */         .setScanInterval(2000)
/* 57 */         .setPassword(password))
/* 58 */         .addNodeAddress(nodes);
/* 59 */       config.setThreads(threads);
/*    */     } else {
/*    */       
/* 62 */       ((SingleServerConfig)config.useSingleServer()
/* 63 */         .setAddress("redis://" + host + ":" + port)
/* 64 */         .setPassword(password))
/* 65 */         .setDatabase(database)
/*    */         
/* 67 */         .setPingConnectionInterval(600000);
/* 68 */       config.setThreads(threads);
/*    */     } 
/* 70 */     return Redisson.create(config);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\config\RedissonConfig.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */