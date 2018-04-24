package primrose.service.contact;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = ContactPreview.ContactReducedDisplayBuilder.class)
public class ContactPreview {

  @NotBlank
  private final String code;
  @NotBlank
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  @JsonPOJOBuilder(withPrefix = "")
  public static class ContactReducedDisplayBuilder {

  }

}
