package primrose.service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = Pagination.PaginationBuilder.class)
public class Pagination {

  @NotNull
  @Min(0)
  private final Integer page;

  @NotNull
  @Min(1)
  private final Integer size;
  @Valid
  private final Sort    sort;
  private final String  search;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PaginationBuilder {

  }

}
