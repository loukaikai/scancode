package BOOT-INF.classes.com.amarsoft.batch;

import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public interface ItemProcessor<I, O> {
  @Nullable
  O process(@NonNull I paramI) throws Exception;
}


/* Location:              D:\JDI\rwa-engine-1.0-SNAPSHOT(1)\rwa-engine-1.0-SNAPSHOT.jar!\BOOT-INF\classes\com\amarsoft\batch\ItemProcessor.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */