package primrose.service.impl;

import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.ImmutableList;

import primrose.data.CustomerRepository;
import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCode;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCode;
import primrose.service.customer.CustomerPreview;
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
  // TODO normalize emails and phoneNumbers
  public CustomerCode create(Customer customer) {
    return customerRepository.create(customer);
  }

  @Override
  @Transactional
  public CustomerCode update(Customer customer) {
    return customerRepository.update(customer);
  }

  @Override
  public ListResult<CustomerPreview> list(Pagination pagination) {
    ImmutableList<CustomerPreview> data = customerRepository.list(pagination);
    long count = customerRepository.count(pagination);
    return ListResult.<CustomerPreview>builder()
      .count(count)
      .data(data)
      .build();
  }

  @Override
  public Customer get(CustomerCode code) {
    return customerRepository.get(code);
  }

  @Override
  public void add(ContactCode contactCode) {
  }

  @Override
  public void add(Set<ContactCode> contactCodes) {
  }
}
