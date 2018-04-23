package primrose.service.impl;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import primrose.data.CustomerRepository;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerEdit;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;
import primrose.service.customer.CustomerService;

@Component
public class CustomerServiceImpl implements CustomerService {

  private CustomerRepository customerRepository;

  public CustomerServiceImpl(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  @Override
  @Transactional
  // TODO implement meta validation
  // TODO implement duplicates email and phone numbers
  public String create(CustomerCreate customer) {
    return customerRepository.create(customer);
  }

  @Override
  @Transactional
  public void update(String code, CustomerEdit customer) {
    customerRepository.update(code, customer);
  }

  @Override
  public ListResult<CustomerReducedDisplay> list(Pagination pagination) {
    ImmutableList<CustomerReducedDisplay> data = customerRepository.list(pagination);
    long count = customerRepository.count(pagination);
    return ListResult.<CustomerReducedDisplay>builder()
      .count(count)
      .data(data)
      .build();
  }

  @Override
  public CustomerFullDisplay get(String code) {
    return customerRepository.get(code);
  }

  @Override
  public void delete(String code) {
    customerRepository.delete(code);
  }

  @Override
  public void delete(Set<String> codes) {
    customerRepository.delete(codes);
  }
}
