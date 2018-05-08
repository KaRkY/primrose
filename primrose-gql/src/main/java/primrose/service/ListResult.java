package primrose.service;

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
@JsonDeserialize(builder = ListResult.ListResultBuilder.class)
public class ListResult<T> {

  @NotNull
  @Singular("data")
  private final ImmutableList<T> data;
  @Min(0)
  private final long             count;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ListResultBuilder<T> {

  }

}
