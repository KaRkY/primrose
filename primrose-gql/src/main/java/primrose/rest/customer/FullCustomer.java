package primrose.rest.customer;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import com.google.common.collect.ImmutableList;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;
import lombok.experimental.Wither;
import primrose.rest.FullEmail;
import primrose.rest.FullPhoneNumber;

@Value
@Builder(toBuilder = true)
@Wither
@JsonDeserialize(builder = FullCustomer.FullCustomerBuilder.class)
public class FullCustomer {

  private final CustomerCode                   code;
  private final String                         type;
  private final String                         relationType;
  private final String                         displayName;
  private final String                         fullName;
  private final String                         description;
  @Singular
  private final ImmutableList<FullEmail>       emails;
  @Singular
  private final ImmutableList<FullPhoneNumber> phoneNumbers;
  private final OffsetDateTime                 version;

  @JsonPOJOBuilder(withPrefix = "")
  public static class FullCustomerBuilder {

  }

}
