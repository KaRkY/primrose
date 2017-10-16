package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.accounts.ImmutableAccountResource;
import primrose.hal.HalResource;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE, depluralize = true, deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableAccountResource.class)
@JsonDeserialize(as = ImmutableAccountResource.class)
public interface AddressResource extends HalResource {

  String street();

  String streetNumber();

  String city();

  String postalCode();

  String state();

  String country();
}
