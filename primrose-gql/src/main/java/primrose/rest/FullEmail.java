package primrose.rest;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = FullEmail.FullEmailBuilder.class)
public class FullEmail {
  @NotBlank
  private final String type;
  @NotBlank
  @javax.validation.constraints.Email
  private final String value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class FullEmailBuilder {

  }

}
