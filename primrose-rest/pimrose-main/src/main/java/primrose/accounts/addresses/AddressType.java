package primrose.accounts.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAddressType.class)
@JsonDeserialize(as = ImmutableAddressType.class)
public interface AddressType {
  String code();
  String def();
}
