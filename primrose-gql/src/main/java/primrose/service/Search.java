package primrose.service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Search {

   @NotNull
   @Min(0)
   private final Integer page;

   @NotNull
   @Min(1)
   private final Integer size;
   @Valid
   private final Sort    sort;
   private final String  query;

   @JsonCreator
   public Search(
      @JsonProperty("page") Integer page,
      @JsonProperty("size") Integer size,
      @JsonProperty("sort") Sort sort,
      @JsonProperty("query") String query) {
      super();
      this.page = page;
      this.size = size;
      this.sort = sort;
      this.query = query;
   }

   public Integer getPage() {
      return page;
   }

   public Integer getSize() {
      return size;
   }

   public Sort getSort() {
      return sort;
   }

   public String getQuery() {
      return query;
   }

}
