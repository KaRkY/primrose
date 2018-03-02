package primrose.types.output;

import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Address {

  Long id();

  String street();

  String streetNumber();

  String city();

  String postalCode();

  Optional<String> state();

  String country();
}
