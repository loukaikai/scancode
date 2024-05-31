/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.rest;
/*    */ 
/*    */ import com.amarsoft.rwa.engine.config.ServiceResult;
/*    */ import com.amarsoft.rwa.engine.entity.TestRequest;
/*    */ import com.amarsoft.rwa.engine.service.CommonService;
/*    */ import com.amarsoft.rwa.engine.service.JobService;
/*    */ import com.amarsoft.rwa.engine.service.ParamService;
/*    */ import com.amarsoft.rwa.engine.service.ResultService;
/*    */ import com.amarsoft.rwa.engine.service.TestService;
/*    */ import com.amarsoft.rwa.engine.util.SqlBuilder;
/*    */ import java.sql.SQLException;
/*    */ import java.util.LinkedHashMap;
/*    */ import java.util.List;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ import org.springframework.beans.factory.annotation.Autowired;
/*    */ import org.springframework.cache.annotation.CacheConfig;
/*    */ import org.springframework.validation.annotation.Validated;
/*    */ import org.springframework.web.bind.annotation.RequestBody;
/*    */ import org.springframework.web.bind.annotation.RequestMapping;
/*    */ import org.springframework.web.bind.annotation.ResponseBody;
/*    */ import org.springframework.web.bind.annotation.RestController;
/*    */ 
/*    */ @RestController
/*    */ @CacheConfig
/*    */ @RequestMapping({"/test/"})
/*    */ public class TestRest {
/* 29 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.rest.TestRest.class);
/*    */   
/*    */   @Autowired
/*    */   private TestService testService;
/*    */   
/*    */   @Autowired
/*    */   private ResultService resultService;
/*    */   @Autowired
/*    */   private JobService jobService;
/*    */   @Autowired
/*    */   private CommonService commonService;
/*    */   @Autowired
/*    */   private ParamService paramService;
/*    */   
/*    */   @RequestMapping({"/initTable"})
/*    */   @ResponseBody
/*    */   public ServiceResult initTable(@Validated @RequestBody TestRequest testRequest) throws SQLException {
/* 46 */     this.resultService.initTable(testRequest.getDataNo(), new String[] { testRequest.getTable() });
/* 47 */     return ServiceResult.success();
/*    */   }
/*    */   
/*    */   @RequestMapping({"/insertTable"})
/*    */   @ResponseBody
/*    */   public ServiceResult insertTable(@Validated @RequestBody TestRequest testRequest) throws Exception {
/* 53 */     if (testRequest.getBatchSize() != null) {
/* 54 */       this.testService.setBatchSize(testRequest.getBatchSize().intValue());
/*    */     }
/* 56 */     if (testRequest.getLogSize() != null) {
/* 57 */       this.testService.setLogSize(testRequest.getLogSize().intValue());
/*    */     }
/* 59 */     this.testService.commonInsert(testRequest.getSql(), testRequest.getTable(), testRequest.getDataNo(), testRequest.getSize().intValue(), testRequest.getParams());
/* 60 */     return ServiceResult.success();
/*    */   }
/*    */   
/*    */   @RequestMapping({"/update"})
/*    */   @ResponseBody
/*    */   public ServiceResult updateByLike(@Validated @RequestBody TestRequest testRequest) throws Exception {
/* 66 */     String sql = testRequest.getSql();
/* 67 */     List<LinkedHashMap<String, Object>> list = this.commonService.select(sql);
/* 68 */     String updateSql = testRequest.getUpdate();
/* 69 */     int dc = 0;
/* 70 */     int uc = 0;
/* 71 */     for (Map<String, Object> data : list) {
/* 72 */       uc += this.commonService.update(SqlBuilder.create(updateSql).setParam(data).build());
/* 73 */       dc++;
/*    */     } 
/* 75 */     return ServiceResult.success("list = " + dc + "   update = " + uc);
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\rest\TestRest.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */