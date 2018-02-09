package primrose.pagination;

import java.util.Map;
import java.util.Optional;

import org.immutables.value.Value;

@Value.Immutable
public interface Pageable {

  int pageNumber();

  int pageSize();

  Optional<Map<String, SortDirection>> sortProperties();

  default long offset() {
    return pageNumber() * pageSize();
  }

  default boolean paged() {
    return pageSize() != 0;
  }
}
