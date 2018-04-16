package primrose.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchResult<T> {
  private final List<T> data;
  private final long    count;

  public SearchResult(List<T> data, long count) {
    super();
    this.data = new ArrayList<>(data);
    this.count = count;
  }

  public List<T> getData() {
    return Collections.unmodifiableList(data);
  }

  public long getCount() {
    return count;
  }
}
