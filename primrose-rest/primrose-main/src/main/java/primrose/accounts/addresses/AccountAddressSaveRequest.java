package primrose.accounts.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAccountAddressSaveResponse.class)
@JsonDeserialize(as = ImmutableAccountAddressSaveResponse.class)
public interface AccountAddressSaveRequest {

  String street();

  String streetNumber();

  String city();

  String postalCode();

  String state();

  String country();
}
