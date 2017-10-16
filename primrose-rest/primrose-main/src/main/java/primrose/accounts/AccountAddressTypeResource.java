package primrose.accounts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.hal.HalResource;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE, depluralize = true, deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableAccountAddressTypeResource.class)
@JsonDeserialize(as = ImmutableAccountAddressTypeResource.class)
public interface AccountAddressTypeResource extends HalResource {

  String name();
}
