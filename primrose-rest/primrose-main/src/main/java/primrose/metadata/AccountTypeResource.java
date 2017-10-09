package primrose.metadata;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.hal.HalResource;
import primrose.meta.ImmutableAccountTypeResource;

@Value.Immutable()
@Value.Style(
  validationMethod = ValidationMethod.NONE,
  depluralize = true,
  deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableAccountTypeResource.class)
@JsonDeserialize(as = ImmutableAccountTypeResource.class)
public interface AccountTypeResource extends HalResource {

  String name();
}
