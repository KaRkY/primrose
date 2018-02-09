package primrose.pagination;

import java.util.List;

import org.immutables.value.Value;

@Value.Immutable
public interface Page<T> {

  List<T> elements();

  Pageable pageable();

  int total();

  default int getNumber() {
    return pageable().paged() ? pageable().pageNumber() : 0;
  }

  default int getSize() {
    return pageable().paged() ? pageable().pageSize() : 0;
  }

  default int getTotalPages() {
    return getSize() == 0 ? 1 : (int) Math.ceil((double) total() / (double) getSize());
  }
}
