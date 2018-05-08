package primrose.service;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = Sort.SortBuilder.class)
public class Sort {

  @NotNull
  private final String property;
  @Pattern(regexp = "asc|desc|ASC|DESC")
  private final String direction;

  @JsonPOJOBuilder(withPrefix = "")
  public static class SortBuilder {

  }

}
