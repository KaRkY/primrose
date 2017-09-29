package primrose.contacts;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ContactsService {

  private final ContactsRepository contactsRepository;

  public ContactsService(final ContactsRepository contactsRepository) {
    this.contactsRepository = contactsRepository;
  }

  @Transactional
  @Secured({ "contacts:create" })
  public Contact save(final Contact contact) {
    final long contactId = contactsRepository.nextValAddresses();
    contactsRepository.insert(contactId, contact, SecurityContextHolder.getContext().getAuthentication().getName());
    return contactsRepository.loadById(contactId);
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadById(final long contactId) {
    return contactsRepository.loadById(contactId);
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadByName(final String contactName) {
    return contactsRepository.loadByName(contactName);
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read", "account_contacts:read" })
  public Map<String, List<Contact>> loadByAccountId(final Long accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }

  @Transactional(readOnly = true)
  @Cacheable("contactTypes")
  public List<String> loadContactTypes() {
    return contactsRepository.loadContactTypes();
  }
}
