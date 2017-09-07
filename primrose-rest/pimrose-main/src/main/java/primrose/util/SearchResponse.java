package primrose.util;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;

import primrose.repositories.SearchResult;

public class SearchResponse<T> {
  private final int page;
  private final int requestedSize;
  private final int actualSize;
  private final int numberOfRecords;
  private final int numberOfPages;
  private final String query;
  private final List<T> records;

  public SearchResponse(
    final int page,
    final int requestedSize,
    final int actualSize,
    final int numberOfRecords,
    final int numberOfPages,
    final String query,
    final List<T> records) {
    super();
    this.page = page;
    this.requestedSize = requestedSize;
    this.actualSize = actualSize;
    this.numberOfRecords = numberOfRecords;
    this.numberOfPages = numberOfPages;
    this.query = query;
    this.records = records;
  }

  public int getPage() {
    return page;
  }

  public int getRequestedSize() {
    return requestedSize;
  }

  public int getActualSize() {
    return actualSize;
  }

  public int getNumberOfRecords() {
    return numberOfRecords;
  }

  public int getNumberOfPages() {
    return numberOfPages;
  }

  public String getQuery() {
    return query;
  }

  public List<T> getRecords() {
    return records;
  }

  public ResponseEntity<List<T>> buildResponse() {
    return ResponseEntity.ok()
      .header("search-page-number", String.valueOf(page))
      .header("search-requested-page-size", String.valueOf(requestedSize))
      .header("search-actual-page-size", String.valueOf(actualSize))
      .header("search-number-of-records", String.valueOf(numberOfRecords))
      .header("search-number-of-pages", String.valueOf(numberOfPages))
      .header("search-query", query)
      .body(records);
  }

  public static <T> SearchResponse<T> of(
    final int page,
    final int requestedSize,
    final int numberOfRecords,
    final String query,
    final List<T> records) {
    final int numberOfPages = (int) (requestedSize != 0 ? Math.ceil(numberOfRecords / (double) requestedSize) : 1);
    return new SearchResponse<>(
      page,
      requestedSize,
      records == null ? 0 : records.size(),
      numberOfRecords,
      numberOfPages,
      query,
      records == null ? Collections.emptyList() : records);
  }

  public static <T> SearchResponse<T> of(
    final Integer page,
    final Integer requestedSize,
    final String query,
    final SearchResult<T> result) {
    return SearchResponse.of(
      page == null ? 1 : page,
      requestedSize == null ? Integer.MAX_VALUE : requestedSize,
      result.getCount(),
      query,
      result.getResults());
  }

  public static <T> SearchResponse<T> of(
    final SearchRequest request,
    final SearchResult<T> result) {
    return SearchResponse.of(request.getPage(), request.getSize(), request.getQuery(), result);
  }
}
