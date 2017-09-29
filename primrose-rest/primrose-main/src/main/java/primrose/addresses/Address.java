package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAddress.class)
@JsonDeserialize(as = ImmutableAddress.class)
public interface Address {

  long id();

  String street();

  String streetNumber();

  String city();

  String postalCode();

  String state();

  String country();
}
