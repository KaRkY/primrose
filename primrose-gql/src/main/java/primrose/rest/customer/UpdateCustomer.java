package primrose.rest.customer;

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
@Wither
@Builder(toBuilder = true)
@JsonDeserialize(builder = UpdateCustomer.UpdateCustomerBuilder.class)
public class UpdateCustomer {

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

  @JsonPOJOBuilder(withPrefix = "")
  public static class UpdateCustomerBuilder {

  }

}
