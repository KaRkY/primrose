package primrose.contacts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.sequences.Sequences;
import primrose.sequences.SequencesRepository;

@Service
public class ContactsService {

  private final ContactsRepository contactsRepository;
  private final SequencesRepository sequencesRepository;

  public ContactsService(final ContactsRepository contactsRepository, final SequencesRepository sequencesRepository) {
    this.contactsRepository = contactsRepository;
    this.sequencesRepository = sequencesRepository;
  }

  @Transactional
  public Contact save(final Contact contact) {
    final long contactId = sequencesRepository.nextvalue(Sequences.CONTACTS);
    contactsRepository.insert(contactId, contact);
    return contactsRepository.getById(contactId);
  }
}
