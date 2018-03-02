package primrose.types.output;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Customer {

  Long id();

  String type();

  String relationType();

  String fullName();

  Optional<String> displayName();

  String email();

  Optional<String> phone();

  Optional<String> description();
}
