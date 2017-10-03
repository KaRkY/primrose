package primrose.contacts;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.util.IdUtil;

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
    return contactsRepository
      .loadById(contactId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find contact %s",
          Long.toString(IdUtil.pseudo(contactId), 36))));
  }

  @Transactional(readOnly = true)
  @Secured({ "contacts:read" })
  public Contact loadById(final long contactId) {
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
  @Secured({ "contacts:read", "account_contacts:read" })
  public Map<String, List<Contact>> loadByAccountId(final Long accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }
}
