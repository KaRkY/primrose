package primrose.service.contact;

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
@JsonDeserialize(builder = Contact.ContactBuilder.class)
public class Contact {

  @NotBlank(groups = UpdateValidationGroup.class)
  @Null(groups = CreateValidationGroup.class)
  private final ContactCode                code;
  @NotBlank
  private final String                     fullName;
  private final String                     description;
  @Valid
  @Singular
  private final ImmutableList<Email>       emails;
  @Valid
  @Singular
  private final ImmutableList<PhoneNumber> phoneNumbers;
  @NotNull(groups = UpdateValidationGroup.class)
  @Null(groups = CreateValidationGroup.class)
  private final OffsetDateTime             version;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ContactBuilder {

  }

}
