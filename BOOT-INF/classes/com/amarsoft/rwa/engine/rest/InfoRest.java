/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.rest;
/*    */ 
/*    */ import cn.hutool.core.collection.CollUtil;
/*    */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*    */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*    */ import com.amarsoft.rwa.engine.entity.JobQueryRequest;
/*    */ import com.amarsoft.rwa.engine.job.JobUtils;
/*    */ import java.util.HashMap;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.cache.annotation.CacheConfig;
/*    */ import org.springframework.validation.annotation.Validated;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.ResponseBody;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ @RestController
/*    */ @CacheConfig
/*    */ @RequestMapping({"/info/"})
/*    */ public class InfoRest
/*    */ {
/* 27 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.rest.InfoRest.class);
/*    */ 
/*    */   
/*    */   @RequestMapping({"/health"})
/*    */   public String health() throws Exception {
/* 32 */     return "ok";
/*    */   }
/*    */   
/*    */   @RequestMapping({"/rwaJob"})
/*    */   @ResponseBody
/*    */   public ServiceResult rwaJob(@Validated @RequestBody JobQueryRequest jobQueryRequest) throws Exception {
/* 38 */     Map<String, Map<String, JobInfoDto>> jobMap = JobUtils.getRwaJobMap();
/* 39 */     Map<String, Integer> stat = new HashMap<>();
/* 40 */     Integer total = Integer.valueOf(0);
/* 41 */     if (CollUtil.isNotEmpty(jobMap)) {
/* 42 */       for (String key : jobMap.keySet()) {
/* 43 */         Map<String, JobInfoDto> map = jobMap.get(key);
/* 44 */         total = Integer.valueOf(total.intValue() + map.size());
/* 45 */         stat.put(key, Integer.valueOf(map.size()));
/*    */       } 
/*    */     }
/* 48 */     stat.put("total", total);
/* 49 */     return ServiceResult.success(stat);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\rest\InfoRest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */