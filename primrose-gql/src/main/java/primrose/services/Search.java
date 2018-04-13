package primrose.services;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutableSearch.class)
public interface Search {
  int page();

  int size();

  Optional<Sort> sort();

  Optional<String> query();
}
