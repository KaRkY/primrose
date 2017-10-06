package primrose.accounts;

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
@JsonSerialize(as = ImmutablePageableAccountResource.class)
@JsonDeserialize(as = ImmutablePageableAccountResource.class)
public interface PageableAccountResource extends HalResource {

  int count();
}