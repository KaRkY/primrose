package primrose.addresses;

import java.util.List;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.model.BaseInputAddress;
import primrose.model.BaseOutputAccountAddress;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;

  public AddressesService(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @Transactional(readOnly = true)
  @Secured({ "addresses:read" })
  public int count() {
    return addressesRepository.count();
  }

  @Transactional
  @Secured({ "addresses:create" })
  public String save(final BaseInputAddress address) {

    final String addressId = getNextId();

    addressesRepository.insert(
      addressId,
      address,
      SecurityContextHolder.getContext().getAuthentication().getName());
    return addressId;
  }

  @Transactional
  @Secured({ "addresses:update" })
  public String edit(final String addressId, final BaseInputAddress address) {
    addressesRepository.update(
      addressId,
      address,
      SecurityContextHolder.getContext().getAuthentication().getName());

    return addressId;
  }

  @Transactional
  public String getNextId() {
    return addressesRepository.nextValAddresses();
  }

  @Transactional(readOnly = true)
  @Secured({ "account_addresses:read", "addresses:read" })
  public List<List<BaseOutputAccountAddress>> loadByAccountId(final List<String> accountId) {
    return addressesRepository.loadByAccountId(accountId);
  }
}
