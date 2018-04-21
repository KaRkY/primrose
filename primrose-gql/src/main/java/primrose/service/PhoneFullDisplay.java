package primrose.service;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = PhoneFullDisplay.PhoneFullDisplayBuilder.class)
public class PhoneFullDisplay {

  private final long   id;
  @NotBlank
  private final String type;

  @NotBlank
  @javax.validation.constraints.Email
  private final String  value;
  private final Boolean primary;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PhoneFullDisplayBuilder {

  }

}
