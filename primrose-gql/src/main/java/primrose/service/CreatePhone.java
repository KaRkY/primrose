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
@JsonDeserialize(builder = CreatePhone.CreatePhoneBuilder.class)
public class CreatePhone {

  @NotBlank
  private final String type;

  @NotBlank
  private final String  value;
  private final Boolean primary;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CreatePhoneBuilder {

  }

}
