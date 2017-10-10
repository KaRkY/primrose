package primrose.hal;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable()
@Value.Style(
  validationMethod = ValidationMethod.NONE,
  depluralize = true,
  deepImmutablesDetection = true)
@JsonSerialize(as = ImmutableEntity.class)
@JsonDeserialize(as = ImmutableEntity.class)
public interface Entity {

  String name();

  String mediaType();
}
