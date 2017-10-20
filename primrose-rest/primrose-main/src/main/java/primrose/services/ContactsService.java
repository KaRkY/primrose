package primrose.services;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.ContactsRepository;
import primrose.model.input.BaseInputContact;
import primrose.model.output.BaseOutputAccountContact;


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
  @Secured({ "contacts:create" })
  public String save(final BaseInputContact contact) {
    final String contactId = getNextId();

    contactsRepository.insert(
      contactId,
      contact,
      SecurityContextHolder.getContext().getAuthentication().getName());
    return contactId;
  }

  @Transactional
  @Secured({ "contacts:update" })
  public String edit(final String contactId, final BaseInputContact contact) {
    contactsRepository.update(
      contactId,
      contact,
      SecurityContextHolder.getContext().getAuthentication().getName());

    return contactId;
  }

  @Transactional(readOnly = true)
  @Secured({ "account_contacts:read", "contacts:read" })
  public List<List<BaseOutputAccountContact>> loadByAccountId(final List<String> accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }
}
