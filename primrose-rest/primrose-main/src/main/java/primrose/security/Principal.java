package primrose.security;

import java.util.List;

import org.immutables.value.Value;
import org.immutables.value.Value.Style.ValidationMethod;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@Value.Style(validationMethod = ValidationMethod.NONE)
@JsonSerialize(as = ImmutablePrincipal.class)
@JsonDeserialize(as = ImmutablePrincipal.class)
public interface Principal {

  String name();

  String credidentials();

  boolean enabled();

  boolean locked();

  List<String> permissions();
}
