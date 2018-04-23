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
import primrose.service.CreateEmail;
import primrose.service.CreatePhone;

@Value
@Builder
@Wither
@JsonDeserialize(builder = CustomerEdit.CustomerEditBuilder.class)
public class CustomerEdit {

  @NotBlank
  private final String                     type;
  @NotBlank
  private final String                     relationType;
  private final String                     displayName;
  @NotBlank
  private final String                     fullName;
  private final String                     description;
  @Valid
  @Singular
  private final ImmutableList<CreateEmail> emails;
  @Valid
  @Singular
  private final ImmutableList<CreatePhone> phones;
  @NotNull
  private final OffsetDateTime             version;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CustomerEditBuilder {

  }

}
