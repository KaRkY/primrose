package primrose.util;

public class SearchRequest {
  private Integer page;
  private Integer size;
  private String  query;

  public Integer getPage() {
    return page;
  }

  public void setPage(final Integer page) {
    this.page = page;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(final Integer size) {
    this.size = size;
  }

  public String getQuery() {
    return query;
  }

  public void setQuery(final String query) {
    this.query = query;
  }

}
