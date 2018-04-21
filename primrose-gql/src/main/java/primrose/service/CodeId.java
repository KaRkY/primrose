package primrose.service;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = CodeId.CodeIdBuilder.class)
public class CodeId {
  private final long   id;
  private final String code;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CodeIdBuilder {

  }

}
