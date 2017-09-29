package primrose.addresses;

import java.util.List;
import java.util.Map;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;

  public AddressesService(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @Transactional
  @Secured({ "addresses:create" })
  public Address save(final Address address) {
    final long addressId = addressesRepository.nextValAddresses();
    addressesRepository.insert(addressId, address, SecurityContextHolder.getContext().getAuthentication().getName());
    return addressesRepository.loadById(addressId);
  }

  @Transactional(readOnly = true)
  @Secured({ "addresses:read" })
  public Address loadById(final long addressId) {
    return addressesRepository.loadById(addressId);
  }

  @Transactional(readOnly = true)
  @Secured({ "account_addresses:read", "addresses:read" })
  public Map<String, List<Address>> loadByAccountId(final long accountId) {
    return addressesRepository.loadByAccountId(accountId);
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public List<String> loadAccountTypes() {
    return addressesRepository.loadAccountTypes();
  }
}
