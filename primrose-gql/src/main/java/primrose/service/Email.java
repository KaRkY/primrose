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
@JsonDeserialize(builder = Email.EmailBuilder.class)
public class Email {
  @NotBlank
  private final String         type;
  @NotBlank
  @javax.validation.constraints.Email
  private final String         value;

  @JsonPOJOBuilder(withPrefix = "")
  public static class EmailBuilder {

  }

}
