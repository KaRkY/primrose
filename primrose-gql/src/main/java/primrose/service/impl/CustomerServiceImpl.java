package primrose.service.impl;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.github.slugify.Slugify;

import primrose.data.CustomerRepository;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;
import primrose.service.customer.CustomerService;

@Component
public class CustomerServiceImpl implements CustomerService {

  private CustomerRepository customerRepository;
  private Slugify            slugify;

  public CustomerServiceImpl(
    CustomerRepository customerRepository,
    Slugify slugify) {
    this.customerRepository = customerRepository;
    this.slugify = slugify;
  }

  @Override
  public SearchResult<CustomerSearch> search(Search search) {
    return new SearchResult<>(new ArrayList<>(), 0);
  }

  @Override
  public long create(CustomerCreate customer) {
    long customerId = customerRepository.create(customer);
    return customerId;
  }

}
