package primrose.service.account;

import java.time.OffsetDateTime;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;
import primrose.service.CreateValidationGroup;
import primrose.service.Email;
import primrose.service.PhoneNumber;
import primrose.service.UpdateValidationGroup;

@Value
@Builder(toBuilder=true)
@Wither
@JsonDeserialize(builder = Account.AccountBuilder.class)
public class Account {

  @NotBlank(groups = UpdateValidationGroup.class)
  @Null(groups = CreateValidationGroup.class)
  private final AccountCode                code;
  @NotBlank
  private final String                     name;
  private final String                     description;
  @Valid
  @Singular
  private final ImmutableList<Email>       emails;
  @Valid
  @Singular
  private final ImmutableList<PhoneNumber> phones;
  @NotNull(groups = UpdateValidationGroup.class)
  @Null(groups = CreateValidationGroup.class)
  private final OffsetDateTime             versionm;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AccountBuilder {

  }

}
