package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
public interface Address {

  String id();

  String street();

  String streetNumber();

  String city();

  String postalCode();

  String state();

  String country();
}
