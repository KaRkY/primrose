package primrose.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.CustomerRepository;
import primrose.data.EmailRepository;
import primrose.data.PhoneRepository;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;
import primrose.service.customer.CustomerService;

@Component
public class CustomerServiceImpl implements CustomerService {

  private CustomerRepository customerRepository;
  private EmailRepository    emailRepository;
  private PhoneRepository    phoneRepository;

  public CustomerServiceImpl(
    CustomerRepository customerRepository,
    EmailRepository emailRepository,
    PhoneRepository phoneRepository) {
    this.customerRepository = customerRepository;
    this.emailRepository = emailRepository;
    this.phoneRepository = phoneRepository;
  }

  @Override
  public SearchResult<CustomerSearch> search(Search search) {
    List<CustomerSearch> date = customerRepository.search(search);
    long count = customerRepository.count(search);
    return new SearchResult<>(date, count);
  }

  @Override
  public Customer get(long customerId) {
    return customerRepository.get(customerId, emailRepository.customerEmails(customerId), phoneRepository.customerPhones(customerId));
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public long create(CustomerCreate customer) {
    long customerId = customerRepository.create(customer);
    customer
      .getEmails()
      .forEach(email -> {
        long emailId = emailRepository.save(email.getValue());
        emailRepository.assignToCustomer(customerId, emailId, email.getType(), email.getPrimary());
      });
    customer
      .getPhones()
      .forEach(phone -> {
        long phoneId = phoneRepository.save(phone.getValue());
        phoneRepository.assignToCustomer(customerId, phoneId, phone.getType(), phone.getPrimary());
      });
    return customerId;
  }

}
