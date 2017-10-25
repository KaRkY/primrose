package primrose.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.data.AddressesRepository;
import primrose.model.input.BaseInputAddress;
import primrose.model.output.BaseOutputAccountAddress;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;

  public AddressesService(final AddressesRepository addressesRepository) {
    this.addressesRepository = addressesRepository;
  }

  @Transactional(readOnly = true)
  public int count() {
    return addressesRepository.count();
  }

  @Transactional
  public String edit(final String addressId, final BaseInputAddress address) {
    addressesRepository.update(
      addressId,
      address);

    return addressId;
  }

  @Transactional
  public String getNextId() {
    return addressesRepository.nextValAddresses();
  }

  @Transactional(readOnly = true)
  public List<List<BaseOutputAccountAddress>> loadByAccountId(final List<String> accountId) {
    return addressesRepository.loadByAccountId(accountId);
  }

  @Transactional
  public String save(final BaseInputAddress address) {

    final String addressId = getNextId();

    addressesRepository.insert(
      addressId,
      address);
    return addressId;
  }
}
