/*    */ package BOOT-INF.classes.com.amarsoft.rwa.engine.job.processor;
/*    */ import com.amarsoft.rwa.engine.constant.Approach;
/*    */ import com.amarsoft.rwa.engine.constant.ExposureApproach;
/*    */ import com.amarsoft.rwa.engine.constant.ICodeEnum;
/*    */ import com.amarsoft.rwa.engine.constant.IrbUncoveredProcess;
/*    */ import com.amarsoft.rwa.engine.constant.JobType;
/*    */ import com.amarsoft.rwa.engine.constant.ProcessStatus;
/*    */ import com.amarsoft.rwa.engine.constant.RwaParam;
/*    */ import com.amarsoft.rwa.engine.constant.TaskType;
/*    */ import com.amarsoft.rwa.engine.entity.JobInfoDto;
/*    */ import com.amarsoft.rwa.engine.entity.SchemeConfigDo;
/*    */ import com.amarsoft.rwa.engine.util.DataUtils;
/*    */ import com.amarsoft.rwa.engine.util.JsonUtils;
/*    */ import java.util.Map;
/*    */ import org.slf4j.Logger;
/*    */ 
/*    */ public abstract class RwaProcessor implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
/*    */   private JobInfoDto jobInfo;
/*    */   private String jobId;
/*    */   private TaskType taskType;
/*    */   private JobType jobType;
/* 22 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.rwa.engine.job.processor.RwaProcessor.class); private SchemeConfigDo schemeConfig; private Approach approach; private IrbUncoveredProcess irbUncoveredProcess; private ExposureApproach confirmApproach;
/*    */   
/*    */   public JobInfoDto getJobInfo() {
/* 25 */     return this.jobInfo; } public void setJobInfo(JobInfoDto jobInfo) {
/* 26 */     this.jobInfo = jobInfo;
/*    */   }
/* 28 */   public String getJobId() { return this.jobId; } public void setJobId(String jobId) {
/* 29 */     this.jobId = jobId;
/*    */   }
/* 31 */   public TaskType getTaskType() { return this.taskType; } public void setTaskType(TaskType taskType) {
/* 32 */     this.taskType = taskType;
/*    */   }
/* 34 */   public JobType getJobType() { return this.jobType; } public void setJobType(JobType jobType) {
/* 35 */     this.jobType = jobType;
/*    */   }
/* 37 */   public SchemeConfigDo getSchemeConfig() { return this.schemeConfig; } public void setSchemeConfig(SchemeConfigDo schemeConfig) {
/* 38 */     this.schemeConfig = schemeConfig;
/*    */   }
/* 40 */   public Approach getApproach() { return this.approach; } public void setApproach(Approach approach) {
/* 41 */     this.approach = approach;
/*    */   }
/* 43 */   public IrbUncoveredProcess getIrbUncoveredProcess() { return this.irbUncoveredProcess; } public void setIrbUncoveredProcess(IrbUncoveredProcess irbUncoveredProcess) {
/* 44 */     this.irbUncoveredProcess = irbUncoveredProcess;
/*    */   }
/* 46 */   public ExposureApproach getConfirmApproach() { return this.confirmApproach; } public void setConfirmApproach(ExposureApproach confirmApproach) {
/* 47 */     this.confirmApproach = confirmApproach;
/*    */   }
/*    */   
/*    */   public RwaProcessor(@NotNull JobInfoDto jobInfo, IrbUncoveredProcess irbUncoveredProcess) {
/* 51 */     this.jobInfo = jobInfo;
/* 52 */     this.jobId = jobInfo.getJobId();
/* 53 */     this.taskType = jobInfo.getTaskType();
/* 54 */     this.jobType = jobInfo.getJobType();
/* 55 */     this.schemeConfig = RwaConfig.getSchemeConfig(jobInfo.getSchemeId());
/* 56 */     this.approach = jobInfo.getApproach();
/* 57 */     this.irbUncoveredProcess = (irbUncoveredProcess == null) ? IrbUncoveredProcess.CALCULATE : irbUncoveredProcess;
/*    */   }
/*    */ 
/*    */ 
/*    */   
/*    */   public Map<String, Object> process(Map<String, Object> item) throws Exception {
/*    */     try {
/* 64 */       init(item);
/*    */       
/* 66 */       this.confirmApproach = confirmExposureApproach(item);
/* 67 */       if (this.approach == Approach.IRB && this.irbUncoveredProcess == IrbUncoveredProcess.SKIP && (this.confirmApproach == ExposureApproach.WA || this.confirmApproach == ExposureApproach.ABSERA || this.confirmApproach == ExposureApproach.ABSSA || this.confirmApproach == ExposureApproach.ABS1250)) {
/*    */ 
/*    */ 
/*    */ 
/*    */         
/* 72 */         DataUtils.setString(item, (ICodeEnum)RwaParam.PROCESS_STATUS, ProcessStatus.SKIP.getCode());
/* 73 */         return item;
/*    */       } 
/*    */       
/* 76 */       paramMapping(item);
/*    */       
/* 78 */       Map<String, Object> result = calculateResult(item);
/* 79 */       DataUtils.setString(result, (ICodeEnum)RwaParam.PROCESS_STATUS, ProcessStatus.COMPLETE.getCode());
/* 80 */       return result;
/* 81 */     } catch (Exception e) {
/* 82 */       log.error(getJobType().getName() + "计算异常： data=" + JsonUtils.object2Json(item), e);
/* 83 */       DataUtils.setString(item, (ICodeEnum)RwaParam.PROCESS_STATUS, ProcessStatus.EXCEPTION.getCode());
/* 84 */       return item;
/*    */     } 
/*    */   }
/*    */   
/*    */   abstract void init(Map<String, Object> paramMap) throws Exception;
/*    */   
/*    */   abstract ExposureApproach confirmExposureApproach(Map<String, Object> paramMap) throws Exception;
/*    */   
/*    */   abstract void paramMapping(Map<String, Object> paramMap) throws Exception;
/*    */   
/*    */   abstract Map<String, Object> calculateResult(Map<String, Object> paramMap) throws Exception;
/*    */ }


/* Location:              C:\Users\16509\Desktop\工作文档\稠州银行\代码文件\rwa-engine-1.0-SNAPSHOT\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\rwa\engine\job\processor\RwaProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */