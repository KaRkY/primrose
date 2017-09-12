package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableContactType.class)
@JsonDeserialize(as = ImmutableContactType.class)
public interface ContactType {
  String code();
  String def();
}
