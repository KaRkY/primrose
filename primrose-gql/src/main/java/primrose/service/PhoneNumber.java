package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder=true)
@Wither
@JsonDeserialize(builder = PhoneNumber.PhoneNumberBuilder.class)
public class PhoneNumber {

  @NotBlank
  private final String         type;
  @NotBlank
  private final String         value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PhoneNumberBuilder {

  }

}
