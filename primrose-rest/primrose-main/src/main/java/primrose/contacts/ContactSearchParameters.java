package primrose.contacts;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutableContactSearchParameters.class)
@JsonDeserialize(as = ImmutableContactSearchParameters.class)
public interface ContactSearchParameters {

  String name();

  String email();

  String phone();

}
