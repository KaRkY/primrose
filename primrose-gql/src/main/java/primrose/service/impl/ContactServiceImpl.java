package primrose.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.ContactRepository;
import primrose.data.EmailRepository;
import primrose.data.PhoneRepository;
import primrose.service.CodeId;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;
import primrose.service.contact.ContactService;

@Component
public class ContactServiceImpl implements ContactService {

  private ContactRepository contactRepository;
  private EmailRepository   emailRepository;
  private PhoneRepository   phoneRepository;

  public ContactServiceImpl(
      ContactRepository contactRepository,
      EmailRepository emailRepository,
      PhoneRepository phoneRepository) {
    this.contactRepository = contactRepository;
    this.emailRepository = emailRepository;
    this.phoneRepository = phoneRepository;
  }

  @Override
  public SearchResult<ContactReducedDisplay> search(Search search) {
    List<ContactReducedDisplay> date = contactRepository.search(search);
    long count = contactRepository.count(search);
    return new SearchResult<>(date, count);
  }

  @Override
  public ContactFullDisplay get(String contactCode) {
    CodeId code = contactRepository.codeId(contactCode);
    return contactRepository.get(code, emailRepository.contactEmails(code), phoneRepository.contactPhones(code));
  }

  @Override
  @Transactional
  public String create(ContactCreate contact) {
    CodeId code = contactRepository.generateCode();
    contactRepository.create(code, contact);
    contact
        .getEmails()
        .forEach(email -> {
          Long emailId = emailRepository.get(email.getValue());
          if (emailId == null) {
            emailId = emailRepository.save(email.getValue());
          }
          emailRepository.assignToContact(code, emailId, email.getType(), email.getPrimary());
        });
    contact
        .getPhones()
        .forEach(phone -> {
          Long phoneId = phoneRepository.get(phone.getValue());
          if (phoneId == null) {
            phoneId = phoneRepository.save(phone.getValue());
          }
          phoneRepository.assignToContact(code, phoneId, phone.getType(), phone.getPrimary());
        });
    return code.getCode();
  }

  @Override
  public String edit(String contactCode, ContactCreate contact) {
    CodeId code = contactRepository.codeId(contactCode);
    ContactFullDisplay currentContact = contactRepository.getForUpdate(
        code,
        emailRepository.contactEmailsForUpdate(code),
        phoneRepository.contactPhonesForUpdate(code));

    if (currentContact == null) {
      throw new RuntimeException();
    }

    if (!(Objects.equals(contact.getFullName(), currentContact.getFullName()) &&
        Objects.equals(contact.getDescription(), currentContact.getDescription()))) {
      contactRepository.deactivate(code);
      contactRepository.create(code, contact);
    }

    Set<Long> emails = new HashSet<>();
    contact
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
    emailRepository.removeExceptFromContact(code, emails);

    Set<Long> phones = new HashSet<>();
    contact
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
    phoneRepository.removeExceptFromContact(code, phones);

    return code.getCode();
  }

}
