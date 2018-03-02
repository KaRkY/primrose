package primrose.types.output;

import org.immutables.value.Value;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Value.Immutable
@JsonDeserialize(as = ImmutablePageable.class)
public interface Pageable {

  Integer pageNumber();

  Integer pageSize();
}
