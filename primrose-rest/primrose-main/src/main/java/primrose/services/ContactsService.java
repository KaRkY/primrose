package primrose.services;

import java.util.List;

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
  public String edit(final String contactId, final BaseInputContact contact) {
    contactsRepository.update(
      contactId,
      contact);

    return contactId;
  }

  @Transactional
  public String getNextId() {
    return contactsRepository.nextValContact();
  }

  @Transactional(readOnly = true)
  public List<List<BaseOutputAccountContact>> loadByAccountId(final List<String> accountId) {
    return contactsRepository.loadByAccountId(accountId);
  }

  @Transactional
  public String save(final BaseInputContact contact) {
    final String contactId = getNextId();

    contactsRepository.insert(
      contactId,
      contact);
    return contactId;
  }
}
