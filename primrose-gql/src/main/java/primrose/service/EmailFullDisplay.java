package primrose.service;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = EmailFullDisplay.EmailFullDisplayBuilder.class)
public class EmailFullDisplay {

  private final long   id;
  @NotBlank
  private final String type;

  @NotBlank
  @Email
  private final String  value;
  private final Boolean primary;

  @JsonPOJOBuilder(withPrefix = "")
  public static class EmailFullDisplayBuilder {

  }

}
