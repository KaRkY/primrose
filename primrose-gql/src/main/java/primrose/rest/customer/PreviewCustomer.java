package primrose.rest.customer;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import primrose.service.Email;
import primrose.service.PhoneNumber;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = PreviewCustomer.PreviewCustomerBuilder.class)
public class PreviewCustomer {

  private final CustomerCode   code;
  private final String         type;
  private final String         relationType;
  private final String         displayName;
  private final String         fullName;
  private final Email          primaryEmail;
  private final PhoneNumber    primaryPhoneNumber;
  private final OffsetDateTime version;

  @JsonPOJOBuilder(withPrefix = "")
  public static class PreviewCustomerBuilder {

  }

}
