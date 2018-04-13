package primrose.services;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableSort.class)
public interface Sort {

  String property();

  Optional<String> direction();
}
