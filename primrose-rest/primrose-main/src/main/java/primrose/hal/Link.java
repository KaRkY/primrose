package primrose.hal;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableLink.class)
@JsonDeserialize(as = ImmutableLink.class)
public interface Link {

  String href();

  String hreflang();

  String media();

  String title();

  String type();

  String deprecation();
}
