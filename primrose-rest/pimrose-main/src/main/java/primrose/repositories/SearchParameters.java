package primrose.repositories;

import primrose.util.SearchRequest;

public class SearchParameters {
  private final Integer page;
  private final Integer size;
  private final String query;

  private SearchParameters(final Integer page, final Integer size, final String query) {
    super();
    this.page = page;
    this.size = size;
    this.query = query;
  }

  public Integer getPage() {
    return page;
  }

  public Integer getSize() {
    return size;
  }

  public String getQuery() {
    return query;
  }

  public static SearchParameters of(final Integer page, final Integer size, final String query) {
    return new SearchParameters(page, size, query);
  }

  public static SearchParameters of(final SearchRequest searchRequest) {
    return new SearchParameters(searchRequest.getPage(), searchRequest.getSize(), searchRequest.getQuery());
  }
}
