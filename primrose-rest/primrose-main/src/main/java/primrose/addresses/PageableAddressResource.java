package primrose.addresses;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.hal.HalResource;

@Value.Immutable()
@Value.Style(
  validationMethod = ValidationMethod.NONE,
  depluralize = true,
  deepImmutablesDetection = true)
@JsonSerialize(as = ImmutablePageableAddressResource.class)
@JsonDeserialize(as = ImmutablePageableAddressResource.class)
public interface PageableAddressResource extends HalResource {

  int count();
}
