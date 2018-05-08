package primrose.service.account;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = AccountPreview.AccountPreviewBuilder.class)
public class AccountPreview {

  @NotBlank
  private final String code;
  @NotBlank
  private final String name;
  private final String primaryEmail;
  private final String primaryPhone;

  @JsonPOJOBuilder(withPrefix = "")
  public static class AccountPreviewBuilder {

  }

}
