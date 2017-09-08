package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.addresses.Address;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableContact.class)
@JsonDeserialize(as = ImmutableContact.class)
public interface Contact {
  String code();
  String personName();
  String email();
  String phone();
  Address address();
}
