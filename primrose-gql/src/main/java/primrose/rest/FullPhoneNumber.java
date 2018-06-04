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
@JsonDeserialize(builder = FullPhoneNumber.FullPhoneNumberBuilder.class)
public class FullPhoneNumber {

  @NotBlank
  private final String type;
  @NotBlank
  private final String value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class FullPhoneNumberBuilder {

  }

}
