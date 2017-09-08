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
  String code();

  String street();

  String city();

  String postalCode();

  String state();

  String country();
}
