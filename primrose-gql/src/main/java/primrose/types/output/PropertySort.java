package primrose.types.output;

import java.util.Optional;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import primrose.pagination.SortDirection;
import primrose.types.ImmutablePropertySort;

@Value.Immutable
@JsonDeserialize(as = ImmutablePropertySort.class)
public interface PropertySort {

  String propertyName();

  Optional<SortDirection> direction();
}
