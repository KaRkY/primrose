package primrose.service.customer;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

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
@JsonDeserialize(builder = CustomerCreate.CustomerCreateBuilder.class)
public class CustomerCreate {

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

  @JsonPOJOBuilder(withPrefix = "")
  public static class CustomerCreateBuilder {

  }

}
