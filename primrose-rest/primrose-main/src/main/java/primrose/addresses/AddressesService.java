package primrose.addresses;

import java.util.List;
import java.util.Map;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;
import primrose.pagging.sort.Sort;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;

  public AddressesService(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @Transactional
  public String getNextId() {
    return addressesRepository.nextValAddresses();
  }

  @Transactional
  @Secured({"addresses:create"})
  public Address save(final Address address) {
    addressesRepository.insert(
      address,
      SecurityContextHolder.getContext().getAuthentication().getName());
    return addressesRepository
      .loadById(address.id())
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find address %s", address.id())));
  }

  @Transactional
  @Secured({"addresses:update"})
  public Address edit(final String addressId, final Address address) {
    addressesRepository.update(
      addressId,
      address,
      SecurityContextHolder.getContext().getAuthentication().getName());

    return addressesRepository
      .loadById(addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find address %s", addressId)));
  }

  @Transactional(readOnly = true)
  @Secured({"addresses:read"})
  public Address loadById(final String addressId) {
    return addressesRepository
      .loadById(addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find address %s", addressId)));
  }

  @Transactional(readOnly = true)
  @Secured({"addresses:read"})
  public Address loadById(final String accountId, final String type, final String addressId) {
    return addressesRepository
      .loadById(accountId, type, addressId)
      .orElseThrow(() -> new NoEntityFoundException(String
      .format("Could not find address %s with type %s for account %s", addressId, type, accountId)));
  }

  @Transactional(readOnly = true)
  @Secured({"account_addresses:read", "addresses:read"})
  public Map<String, List<Address>> loadByAccountId(final String accountId) {
    return addressesRepository.loadByAccountId(accountId);
  }

  @Transactional(readOnly = true)
  @Secured({"addresses:read"})
  public int count() {
    return addressesRepository.count();
  }

  @Transactional(readOnly = true)
  @Secured({"addresses:read"})
  public List<Address> load(final Integer page, final Integer size, final Sort sort) {
    return addressesRepository.load(page, size, sort);
  }

}
