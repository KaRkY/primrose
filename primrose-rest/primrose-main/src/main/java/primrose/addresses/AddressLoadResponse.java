package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAddressLoadResponse.class)
@JsonDeserialize(as = ImmutableAddressLoadResponse.class)
public interface AddressLoadResponse {

  String id();

  String street();

  String streetNumber();

  String city();

  String postalCode();

  String state();

  String country();
}
