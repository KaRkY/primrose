package primrose.contacts;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;

@Service
public class ContactsService {
  private final ContactsRepository contactsRepository;

  public ContactsService(final ContactsRepository contactsRepository) {
    this.contactsRepository = contactsRepository;
  }

  @Transactional(readOnly = true)
  public String getNextId() {
    return contactsRepository.nextValContact();
  }

  @Transactional
  @Secured({ "contacts:create" })
  public Contact save(final Contact contact) {
    contactsRepository.insert(
      contact,
      SecurityContextHolder.getContext().getAuthentication().getName());
    return contactsRepository
      .loadById(contact.id())
      .orElseThrow(() -> new NoEntityFoundException(String
        .format("Could not find contact %s", contact.id())));
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadById(final String contactId) {
    return contactsRepository
      .loadById(contactId)
      .orElseThrow(NoEntityFoundException::new);
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadByName(final String contactName) {
    return contactsRepository
      .loadByName(contactName)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find contact %s",
          contactName)));
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadById(final String accountId, final String type, final String contactId) {
    return contactsRepository
      .loadById(accountId, type, contactId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format("Could not find contact %s with type %s for account %s", contactId, type, accountId)));
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read", "account_contacts:read" })
  public Map<String, List<Contact>> loadByAccountId(final String accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }
}
