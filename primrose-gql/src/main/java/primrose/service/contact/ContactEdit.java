package primrose.service.contact;

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
@JsonDeserialize(builder = ContactEdit.ContactEditBuilder.class)
public class ContactEdit {

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
  public static class ContactEditBuilder {

  }

}
