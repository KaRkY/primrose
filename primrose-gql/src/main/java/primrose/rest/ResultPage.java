package primrose.rest;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = ResultPage.ResultPageBuilder.class)
public class ResultPage<T> {

  @NotNull
  @Singular("data")
  private final ImmutableList<T> data;
  @Min(0)
  private final long             count;
  @Min(0)
  private final long             page;
  @Min(0)
  private final long             pageSize;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ResultPageBuilder<T> {

  }

}
