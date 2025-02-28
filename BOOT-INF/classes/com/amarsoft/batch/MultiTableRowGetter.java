package BOOT-INF.classes.com.amarsoft.batch;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import org.springframework.lang.Nullable;

@FunctionalInterface
public interface MultiTableRowGetter<T> {
  @Nullable
  T nextRow(LinkedHashMap<String, ResultSet> paramLinkedHashMap, String paramString1, String paramString2, int paramInt) throws SQLException;
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\MultiTableRowGetter.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */