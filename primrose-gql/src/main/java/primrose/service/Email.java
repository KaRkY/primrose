package primrose.service;

import java.time.OffsetDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = Email.EmailBuilder.class)
public class Email {
  @NotBlank
  private final String         type;
  @NotBlank
  @javax.validation.constraints.Email
  private final String         value;
  @NotNull(groups = UpdateValidationGroup.class)
  @Null(groups = CreateValidationGroup.class)
  private final OffsetDateTime             versionm;

  @JsonPOJOBuilder(withPrefix = "")
  public static class EmailBuilder {

  }

}
