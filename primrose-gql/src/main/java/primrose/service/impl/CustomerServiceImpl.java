package primrose.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

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
    CustomerCode customerCode = customerRepository.generate();
    customerRepository.create(customer
      .toBuilder()
      .code(customerCode)
      .emails(customer
        .getEmails()
        .stream()
        .map(email -> email.toBuilder().value(email.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .phoneNumbers(customer
        .getPhoneNumbers()
        .stream()
        .map(phoneNumber -> phoneNumber.toBuilder().value(phoneNumber.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .build());
    return customerCode;
  }

  @Override
  @Transactional
  public CustomerCode update(Customer customer) {
    customerRepository.update(customer
      .toBuilder()
      .emails(customer
        .getEmails()
        .stream()
        .map(email -> email.toBuilder().value(email.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .phoneNumbers(customer
        .getPhoneNumbers()
        .stream()
        .map(phoneNumber -> phoneNumber.toBuilder().value(phoneNumber.getValue().toLowerCase()).build())
        .collect(Collectors.toList()))
      .build());
    return customer.getCode();
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
