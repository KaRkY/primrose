package primrose.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.CustomerRepository;
import primrose.data.EmailRepository;
import primrose.data.PhoneRepository;
import primrose.service.CodeId;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;
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
  public SearchResult<CustomerReducedDisplay> search(Search search) {
    List<CustomerReducedDisplay> date = customerRepository.search(search);
    long count = customerRepository.count(search);
    return new SearchResult<>(date, count);
  }

  @Override
  public CustomerFullDisplay get(String customerCode) {
    Long customerCodeId = customerRepository.codeId(customerCode);
    return customerRepository.get(customerCode, emailRepository.customerEmails(customerCodeId), phoneRepository.customerPhones(customerCodeId));
  }

  @Override
  @Transactional
  // TODO implement meta validation
  public String create(CustomerCreate customer) {
    CodeId code = customerRepository.generateCode();
    customerRepository.create(code.getId(), customer);
    customer
      .getEmails()
      .forEach(email -> {
        Long emailId = emailRepository.get(email.getValue());
        if (emailId == null) {
          emailId = emailRepository.save(email.getValue());
        }
        emailRepository.assignToCustomer(code.getId(), emailId, email.getType(), email.getPrimary());
      });
    customer
      .getPhones()
      .forEach(phone -> {
        Long phoneId = phoneRepository.get(phone.getValue());
        if (phoneId == null) {
          phoneId = phoneRepository.save(phone.getValue());
        }
        phoneRepository.assignToCustomer(code.getId(), phoneId, phone.getType(), phone.getPrimary());
      });
    return code.getCode();
  }

  @Override
  public String edit(String customerCode, CustomerCreate customer) {
    Long customerCodeId = customerRepository.codeId(customerCode);
    CustomerFullDisplay currentCustomer = customerRepository.getForUpdate(
      customerCode,
      emailRepository.customerEmailsForUpdate(customerCodeId),
      phoneRepository.customerPhonesForUpdate(customerCodeId));

    if (currentCustomer == null) { throw new RuntimeException(); }

    if (!(Objects.equals(customer.getFullName(), currentCustomer.getFullName()) &&
      Objects.equals(customer.getDescription(), currentCustomer.getDescription()) &&
      Objects.equals(customer.getDisplayName(), currentCustomer.getDisplayName()) &&
      Objects.equals(customer.getType(), currentCustomer.getType()) &&
      Objects.equals(customer.getRelationType(), currentCustomer.getRelationType()))) {
      customerRepository.deactivate(currentCustomer.getId());
      customerRepository.create(customerRepository.codeId(customerCode), customer);
    }

    customer
      .getEmails()
      .forEach(email -> {
        Long emailId = emailRepository.get(email.getValue());
        if (emailId == null) {
          emailRepository.assignToCustomer(customerCodeId, emailRepository.save(email.getValue()), email.getType(), email.getPrimary());
        } else {
          if (!emailRepository.isAssignedToCustomer(customerCodeId, emailId)) {
            emailRepository.assignToCustomer(customerCodeId, emailId, email.getType(), email.getPrimary());
          }
        }
      });

    customer
      .getPhones()
      .forEach(phone -> {
        Long phoneId = phoneRepository.get(phone.getValue());
        if (phoneId == null) {
          phoneRepository.assignToCustomer(customerCodeId, phoneRepository.save(phone.getValue()), phone.getType(), phone.getPrimary());
        } else {
          if (!phoneRepository.isAssignedToCustomer(customerCodeId, phoneId)) {
            phoneRepository.assignToCustomer(customerCodeId, phoneId, phone.getType(), phone.getPrimary());
          }
        }
      });

    return customerCode;
  }

}
