package primrose.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResult<T> {
   @NotNull
   private final List<T> data;
   @Min(0)
   private final long    count;

   @JsonCreator
   public SearchResult(
      @JsonProperty("data") List<T> data,
      @JsonProperty("count") long count) {
      super();
      this.data = new ArrayList<>(data);
      this.count = count;
   }

   public List<T> getData() {
      return Collections.unmodifiableList(data);
   }

   public long getCount() {
      return count;
   }
}
