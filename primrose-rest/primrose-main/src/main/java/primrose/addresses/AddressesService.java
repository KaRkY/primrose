package primrose.addresses;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.util.IdUtil;

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
    return addressesRepository
      .loadById(addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find address %s",
          Long.toString(IdUtil.pseudo(addressId), 36))));
  }

  @Transactional(readOnly = true)
  @Secured({ "addresses:read" })
  public Address loadById(final long addressId) {
    return addressesRepository
      .loadById(addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find address %s",
          Long.toString(IdUtil.pseudo(addressId), 36))));
  }

  @Transactional(readOnly = true)
  @Secured({ "addresses:read" })
  public Address loadById(final long accountId, final long addressId) {
    return addressesRepository
      .loadById(accountId, addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find address %s for account %s",
          Long.toString(IdUtil.pseudo(addressId), 36),
          Long.toString(IdUtil.pseudo(accountId), 36))));
  }

  @Transactional(readOnly = true)
  @Secured({ "account_addresses:read", "addresses:read" })
  public Map<String, List<Address>> loadByAccountId(final long accountId) {
    return addressesRepository.loadByAccountId(accountId);
  }
}
