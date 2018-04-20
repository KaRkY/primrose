package primrose.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
      CodeId code = customerRepository.codeId(customerCode);
      return customerRepository.get(code, emailRepository.customerEmails(code), phoneRepository.customerPhones(code));
   }

   @Override
   @Transactional
   // TODO implement meta validation
   public String create(CustomerCreate customer) {
      CodeId code = customerRepository.generateCode();
      customerRepository.create(code, customer);
      customer
         .getEmails()
         .forEach(email -> {
            Long emailId = emailRepository.get(email.getValue());
            if (emailId == null) {
               emailId = emailRepository.save(email.getValue());
            }
            emailRepository.assignToCustomer(code, emailId, email.getType(), email.getPrimary());
         });
      customer
         .getPhones()
         .forEach(phone -> {
            Long phoneId = phoneRepository.get(phone.getValue());
            if (phoneId == null) {
               phoneId = phoneRepository.save(phone.getValue());
            }
            phoneRepository.assignToCustomer(code, phoneId, phone.getType(), phone.getPrimary());
         });
      return code.getCode();
   }

   @Override
   public String edit(String customerCode, CustomerCreate customer) {
      CodeId code = customerRepository.codeId(customerCode);
      CustomerFullDisplay currentCustomer = customerRepository.getForUpdate(
         code,
         emailRepository.customerEmailsForUpdate(code),
         phoneRepository.customerPhonesForUpdate(code));

      if (currentCustomer == null) { throw new RuntimeException(); }

      if (!(Objects.equals(customer.getFullName(), currentCustomer.getFullName()) &&
         Objects.equals(customer.getDescription(), currentCustomer.getDescription()) &&
         Objects.equals(customer.getDisplayName(), currentCustomer.getDisplayName()) &&
         Objects.equals(customer.getType(), currentCustomer.getType()) &&
         Objects.equals(customer.getRelationType(), currentCustomer.getRelationType()))) {
         customerRepository.deactivate(code);
         customerRepository.create(customerRepository.codeId(customerCode), customer);
      }

      Set<Long> emails = new HashSet<>();
      customer
         .getEmails()
         .forEach(email -> {
            Long emailId = emailRepository.get(email.getValue());
            if (emailId == null) {
               emailId = emailRepository.save(email.getValue());
               emailRepository.assignToCustomer(code, emailId, email.getType(), email.getPrimary());
            } else {
               if (!emailRepository.isAssignedToCustomer(code, emailId)) {
                  emailRepository.assignToCustomer(code, emailId, email.getType(), email.getPrimary());
               }
            }
            emails.add(emailId);
         });
      System.out.println(emails);
      emailRepository.removeExceptFromCustomer(code, emails);

      Set<Long> phones = new HashSet<>();
      customer
         .getPhones()
         .forEach(phone -> {
            Long phoneId = phoneRepository.get(phone.getValue());
            if (phoneId == null) {
               phoneId = phoneRepository.save(phone.getValue());
               phoneRepository.assignToCustomer(code, phoneId, phone.getType(), phone.getPrimary());
            } else {
               if (!phoneRepository.isAssignedToCustomer(code, phoneId)) {
                  phoneRepository.assignToCustomer(code, phoneId, phone.getType(), phone.getPrimary());
               }
            }
            phones.add(phoneId);
         });
      phoneRepository.removeExceptFromCustomer(code, phones);

      return code.getCode();
   }

}
