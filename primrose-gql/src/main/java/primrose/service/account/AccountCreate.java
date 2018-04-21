package primrose.service.account;

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
@JsonDeserialize(builder = AccountCreate.AccountCreateBuilder.class)
public class AccountCreate {

  @NotBlank
  private final String                     name;
  private final String                     description;
  @Valid
  @Singular
  private final ImmutableList<CreateEmail> emails;
  @Valid
  @Singular
  private final ImmutableList<CreatePhone> phones;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AccountCreateBuilder {

  }

}
