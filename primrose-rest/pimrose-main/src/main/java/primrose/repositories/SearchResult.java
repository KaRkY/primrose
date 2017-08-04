package primrose.repositories;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SearchResult<T> {
  private final int     count;
  private final List<T> results;

  private SearchResult(final int count, final List<T> results) {
    super();
    this.count = count;
    this.results = results;
  }

  public int getCount() {
    return count;
  }

  public List<T> getResults() {
    return results;
  }

  public static <T> SearchResult<T> of(final int count, final List<T> result) {
    return new SearchResult<>(count, result);
  }

  public static <T> SearchResult<T> of(final Map<Integer, List<T>> result) {
    final Iterator<Entry<Integer, List<T>>> entries = result.entrySet().iterator();

    int numberOfRecords = 0;
    List<T> records = Collections.emptyList();

    if (entries.hasNext()) {
      final Entry<Integer, List<T>> entry = entries.next();

      numberOfRecords = entry.getKey();
      records = entry.getValue();

      if (entries.hasNext()) {
        throw new RuntimeException("Result contains more than 1 count.");
      }
    }
    return new SearchResult<>(numberOfRecords, records);
  }
}
