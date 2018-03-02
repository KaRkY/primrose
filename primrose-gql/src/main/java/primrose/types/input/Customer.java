package primrose.types.input;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableCustomer.class)
public interface Customer {

  String type();

  String relationType();

  String fullName();

  Optional<String> displayName();

  String email();

  Optional<String> phone();

  Optional<String> description();
}
