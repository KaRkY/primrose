package primrose.service.customer;

import primrose.service.Search;
import primrose.service.SearchResult;

public interface CustomerService {

  SearchResult<CustomerSearch> search(Search search);

  long create(CustomerCreate customer);

}
