package primrose.rest;

import javax.validation.Valid;
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
@JsonDeserialize(builder = PageRequest.PageRequestBuilder.class)
public class PageRequest {

  @NotNull
  @Min(0)
  private final Long                page;
  @NotNull
  @Min(1)
  private final Long                size;
  @Valid
  @Singular
  private final ImmutableList<Sort> sortProperties;
  private final String              search;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PageRequestBuilder {

  }

}
