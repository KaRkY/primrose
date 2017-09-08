package primrose.contacts;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.addresses.AddressesRepository;

@Service
public class ContactsService {

  private final ContactsRepository contactsRepository;
  private final AddressesRepository addressesRepository;

  public ContactsService(final ContactsRepository contactsRepository, final AddressesRepository addressesRepository) {
    this.contactsRepository = contactsRepository;
    this.addressesRepository = addressesRepository;
  }

  @Transactional
  public Contact save(final String accountCode, final String contactTitle, final Contact contact) {
    final long contactId = contactsRepository.getNewId();
    final long addressId = addressesRepository.getNewId();

    addressesRepository.insert(addressId, contact.address());
    contactsRepository.insert(contactId, contact, addressId);

    Optional<Long> contactTitleId = contactsRepository.getContactType(contactTitle);
    if (!contactTitleId.isPresent()) {
      contactTitleId = Optional.of(contactsRepository.getNewContactTypeId());

      contactsRepository.insert(contactTitleId.get(), contactTitle);
    }

    contactsRepository.insert(contactTitle, contactId, accountCode);

    return contact;
  }

  @Transactional
  public Contact save(final long accountId, final String contactType, final Contact contact) {
    final long contactId = contactsRepository.getNewId();
    final long addressId = addressesRepository.getNewId();

    addressesRepository.insert(addressId, contact.address());
    contactsRepository.insert(contactId, contact, addressId);

    Optional<Long> contactTitleId = contactsRepository.getContactType(contactType);
    if (!contactTitleId.isPresent()) {
      contactTitleId = Optional.of(contactsRepository.getNewContactTypeId());

      contactsRepository.insert(contactTitleId.get(), contactType);
    }

    contactsRepository.insert(contactType, contactId, accountId);

    return contact;
  }
}
