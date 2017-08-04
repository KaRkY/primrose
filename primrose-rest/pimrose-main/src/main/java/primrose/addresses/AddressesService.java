package primrose.addresses;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;

  public AddressesService(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @Transactional
  public Address save(final String accountCode, final String addressType, final Address address) {
    final long addressId = addressesRepository.getNewId();

    address.setId(addressId);

    addressesRepository.insert(address);
    addressesRepository.insert(addressType, addressId, accountCode);

    return addressesRepository.get(addressId);
  }
}