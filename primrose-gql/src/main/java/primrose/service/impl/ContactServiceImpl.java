package primrose.service.impl;

import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.ContactRepository;
import primrose.data.EmailRepository;
import primrose.data.PhoneRepository;
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
  public ContactFullDisplay get(long contactId) {
    return contactRepository.get(contactId, emailRepository.contactEmails(contactId), phoneRepository.contactPhones(contactId));
  }

  @Override
  @Transactional
  public long create(ContactCreate contact) {
    long customerId = contactRepository.create(contact);
    contact
      .getEmails()
      .forEach(email -> {
        Long emailId = emailRepository.get(email.getValue());
        if (emailId == null) {
          emailId = emailRepository.save(email.getValue());
        }
        emailRepository.assignToContact(customerId, emailId, email.getType(), email.getPrimary());
      });
    contact
      .getPhones()
      .forEach(phone -> {
        Long phoneId = phoneRepository.get(phone.getValue());
        if (phoneId == null) {
          phoneId = phoneRepository.save(phone.getValue());
        }
        phoneRepository.assignToContact(customerId, phoneId, phone.getType(), phone.getPrimary());
      });
    return customerId;
  }

  @Override
  public long edit(long contactId, ContactCreate contact) {
    long newContactId = contactId;
    ContactFullDisplay currentContact = contactRepository.getForUpdate(
      contactId,
      emailRepository.contactEmailsForUpdate(contactId),
      phoneRepository.contactPhonesForUpdate(contactId));

    if (currentContact == null) { throw new RuntimeException(); }

    if (!(Objects.equals(contact.getFullName(), currentContact.getFullName()) &&
      Objects.equals(contact.getDescription(), currentContact.getDescription()))) {
      contactRepository.deactivate(contactId);
      newContactId = contactRepository.create(contact);
    }

    long actualContactId = newContactId;
    contact
      .getEmails()
      .forEach(email -> {
        Long emailId = emailRepository.get(email.getValue());
        if (emailId == null) {
          emailRepository.assignToCustomer(actualContactId, emailRepository.save(email.getValue()), email.getType(), email.getPrimary());
        } else {
          if (!emailRepository.isAssignedToCustomer(actualContactId, emailId)) {
            emailRepository.assignToCustomer(actualContactId, emailId, email.getType(), email.getPrimary());
          }
        }
      });

    contact
      .getPhones()
      .forEach(phone -> {
        Long phoneId = phoneRepository.get(phone.getValue());
        if (phoneId == null) {
          phoneRepository.assignToCustomer(actualContactId, phoneRepository.save(phone.getValue()), phone.getType(), phone.getPrimary());
        } else {
          if (!phoneRepository.isAssignedToCustomer(actualContactId, phoneId)) {
            phoneRepository.assignToCustomer(actualContactId, phoneId, phone.getType(), phone.getPrimary());
          }
        }
      });

    return newContactId;
  }

}
