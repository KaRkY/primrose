package primrose.service.customer;

import java.time.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;

@Value
@Builder
@Wither
@JsonDeserialize(builder = CustomerFullDisplay.CustomerFullDisplayBuilder.class)
public class CustomerFullDisplay {

  private final String                          code;
  @NotBlank
  private final String                          type;
  @NotBlank
  private final String                          relationType;
  private final String                          displayName;
  @NotBlank
  private final String                          fullName;
  private final String                          description;
  @Valid
  @Singular
  private final ImmutableList<EmailFullDisplay> emails;
  @Valid
  @Singular
  private final ImmutableList<PhoneFullDisplay> phones;
  @NotNull
  private final OffsetDateTime                  validFrom;
  private final OffsetDateTime                  validTo;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CustomerFullDisplayBuilder {

  }

}
