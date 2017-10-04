package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.pagging.Pagging;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAddressesSearchRequest.class)
@JsonDeserialize(as = ImmutableAddressesSearchRequest.class)
public interface AddressesSearchRequest extends Pagging {

  AddressSearchParameters address();
}
