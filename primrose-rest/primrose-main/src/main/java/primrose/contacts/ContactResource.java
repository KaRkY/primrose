package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import primrose.hal.HalResource;

@Value.Immutable()
@Value.Style(validationMethod = ValidationMethod.NONE, depluralize = true, deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableContactResource.class)
@JsonDeserialize(as = ImmutableContactResource.class)
public interface ContactResource extends HalResource {

  String name();

  String email();

  String phone();
}
