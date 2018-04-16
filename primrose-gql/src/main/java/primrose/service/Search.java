package primrose.service;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class Search {

  @NotNull
  @Min(0)
  private final Integer page;

  @NotNull
  @Min(1)
  private final Integer size;
  @Valid
  private final Sort    sort;
  private final String  query;

  @SuppressWarnings("unused")
  private Search() {
    this(null, null, null, null);
  }

  public Search(Integer page, Integer size, Sort sort, String query) {
    super();
    this.page = page;
    this.size = size;
    this.sort = sort;
    this.query = query;
  }

  public Integer getPage() {
    return page;
  }

  public Integer getSize() {
    return size;
  }

  public Sort getSort() {
    return sort;
  }

  public String getQuery() {
    return query;
  }

}
