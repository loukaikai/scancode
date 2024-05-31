/*    */ package BOOT-INF.classes.com.amarsoft.batch.support;
/*    */ 
/*    */ import com.amarsoft.batch.ItemPstSetter;
/*    */ import com.amarsoft.batch.exception.JobRunningException;
/*    */ import java.sql.Connection;
/*    */ import java.sql.PreparedStatement;
/*    */ import java.sql.SQLException;
/*    */ import org.slf4j.Logger;
/*    */ import org.slf4j.LoggerFactory;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class ItemPst
/*    */ {
/*    */   private String sql;
/*    */   private PreparedStatement pst;
/* 18 */   private static final Logger log = LoggerFactory.getLogger(com.amarsoft.batch.support.ItemPst.class); private ItemPstSetter<Object> setter;
/*    */   
/*    */   public String getSql() {
/* 21 */     return this.sql; } public void setSql(String sql) {
/* 22 */     this.sql = sql;
/*    */   }
/* 24 */   public PreparedStatement getPst() { return this.pst; } public void setPst(PreparedStatement pst) {
/* 25 */     this.pst = pst;
/*    */   }
/* 27 */   public ItemPstSetter<Object> getSetter() { return this.setter; } public void setSetter(ItemPstSetter<Object> setter) {
/* 28 */     this.setter = setter;
/*    */   }
/*    */   
/*    */   public void initPreparedStatement(Connection conn) {
/*    */     try {
/* 33 */       this.pst = conn.prepareStatement(this.sql);
/* 34 */     } catch (SQLException e) {
/* 35 */       throw new JobRunningException(e);
/*    */     } 
/*    */   }
/*    */   
/*    */   public static com.amarsoft.batch.support.ItemPst createSetter(String sql, ItemPstSetter<Object> pstSetter) {
/* 40 */     com.amarsoft.batch.support.ItemPst itemPstSetter = new com.amarsoft.batch.support.ItemPst();
/* 41 */     itemPstSetter.setSql(sql);
/* 42 */     itemPstSetter.setSetter(pstSetter);
/* 43 */     return itemPstSetter;
/*    */   }
/*    */ }


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\support\ItemPst.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */