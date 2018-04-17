package primrose.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.ContactRepository;
import primrose.data.EmailRepository;
import primrose.data.PhoneRepository;
import primrose.service.Search;
import primrose.service.SearchResult;
import primrose.service.contact.Contact;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactSearch;
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
  public SearchResult<ContactSearch> search(Search search) {
    List<ContactSearch> date = contactRepository.search(search);
    long count = contactRepository.count(search);
    return new SearchResult<>(date, count);
  }

  @Override
  public Contact get(long contactId) {
    return contactRepository.get(contactId, emailRepository.contactEmails(contactId), phoneRepository.contactPhones(contactId));
  }

  @Override
  @Transactional
  public long create(ContactCreate contact) {
    long customerId = contactRepository.create(contact);
    contact
      .getEmails()
      .forEach(email -> {
        long emailId = emailRepository.save(email.getValue());
        emailRepository.assignToContact(customerId, emailId, email.getType(), email.getPrimary());
      });
    contact
      .getPhones()
      .forEach(phone -> {
        long phoneId = phoneRepository.save(phone.getValue());
        phoneRepository.assignToContact(customerId, phoneId, phone.getType(), phone.getPrimary());
      });
    return customerId;
  }

}
