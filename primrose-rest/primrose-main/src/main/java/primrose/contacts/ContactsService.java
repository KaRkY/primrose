package primrose.contacts;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.pagging.sort.Sort;

@Service
public class ContactsService {

  private final ContactsRepository contactsRepository;

  public ContactsService(final ContactsRepository contactsRepository) {
    this.contactsRepository = contactsRepository;
  }

  @Transactional
  public String getNextId() {
    return contactsRepository.nextValContact();
  }

  @Transactional
  @Secured({"contacts:create"})
  public Contact save(final Contact contact) {
    contactsRepository.insert(
      contact,
      SecurityContextHolder.getContext().getAuthentication().getName());
    return contactsRepository
      .loadById(contact.id())
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find contact %s", contact.id())));
  }

  @Transactional
  @Secured({ "contacts:update" })
  public Contact edit(final String contactId, final Contact contact) {
    contactsRepository.update(
      contactId,
      contact,
      SecurityContextHolder.getContext().getAuthentication().getName());

    return contactsRepository
      .loadById(contactId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format("Could not find address %s", contactId)));
  }

  @Transactional(readOnly = true)
  @Secured({"contacts:read"})
  public Contact loadById(final String contactId) {
    return contactsRepository
      .loadById(contactId)
      .orElseThrow(NoEntityFoundException::new);
  }

  @Transactional(readOnly = true)
  @Secured({"contacts:read"})
  public Contact loadByName(final String contactName) {
    return contactsRepository
      .loadByName(contactName)
      .orElseThrow(() -> new NoEntityFoundException(String
      .format(
        "Could not find contact %s",
        contactName)));
  }

  @Transactional(readOnly = true)
  @Secured({"contacts:read"})
  public Contact loadById(final String accountId, final String type, final String contactId) {
    return contactsRepository
      .loadById(accountId, type, contactId)
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find contact %s with type %s for account %s", contactId, type, accountId)));
  }

  @Transactional(readOnly = true)
  @Secured({"contacts:read", "account_contacts:read"})
  public Map<String, List<Contact>> loadByAccountId(final String accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public int count() {
    return contactsRepository.count();
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public List<Contact> load(final Integer page, final Integer size, final Sort sort) {
    return contactsRepository.load(page, size, sort);
  }
}
