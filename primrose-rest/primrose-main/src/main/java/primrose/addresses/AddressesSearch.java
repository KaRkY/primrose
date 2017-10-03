package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.pagging.Pagging;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableAddressesSearch.class)
@JsonDeserialize(as = ImmutableAddressesSearch.class)
public interface AddressesSearch extends Pagging {

  SearchAddress address();
}
