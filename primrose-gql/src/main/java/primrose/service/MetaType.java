package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = MetaType.MetaTypeBuilder.class)
public class MetaType {

  @NotBlank
  private final String slug;
  @NotBlank
  private final String name;

  @JsonPOJOBuilder(withPrefix = "")
  public static class MetaTypeBuilder {

  }

}
