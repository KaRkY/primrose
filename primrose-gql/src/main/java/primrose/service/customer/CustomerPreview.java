package primrose.service.customer;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder
@Wither
@JsonDeserialize(builder = CustomerPreview.CustomerPreviewBuilder.class)
public class CustomerPreview {

  private final String code;
  @NotBlank
  private final String type;
  @NotBlank
  private final String relationType;
  private final String displayName;
  @NotBlank
  private final String fullName;
  private final String primaryEmail;
  private final String primaryPhone;

  @JsonPOJOBuilder(withPrefix = "")
  public static class CustomerPreviewBuilder {

  }

}
