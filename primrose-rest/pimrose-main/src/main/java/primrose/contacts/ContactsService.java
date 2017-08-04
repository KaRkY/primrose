package primrose.contacts;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.addresses.AddressesRepository;

@Service
public class ContactsService {

  private final ContactsRepository  contactsRepository;
  private final AddressesRepository addressesRepository;

  public ContactsService(final ContactsRepository contactsRepository, final AddressesRepository addressesRepository) {
    this.contactsRepository = contactsRepository;
    this.addressesRepository = addressesRepository;
  }

  @Transactional
  public Contact save(final String accountCode, final String contactTitle, final Contact contact) {
    final long contactId = contactsRepository.getNewId();
    final long addressId = addressesRepository.getNewId();

    contact.setId(contactId);
    contact.getAddress().setId(addressId);
    addressesRepository.insert(contact.getAddress());
    contactsRepository.insert(contact, addressId);

    Optional<Long> contactTitleId = contactsRepository.getContactTitle(contactTitle);
    if (!contactTitleId.isPresent()) {
      contactTitleId = Optional.of(contactsRepository.getNewContactTitleId());

      contactsRepository.insert(contactTitleId.get(), contactTitle);
    }

    contactsRepository.insert(contactTitle, contactId, accountCode);

    return contact;
  }
}
