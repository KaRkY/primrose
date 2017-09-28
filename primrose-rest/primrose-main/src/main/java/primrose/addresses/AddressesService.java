package primrose.addresses;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.sequences.Sequences;
import primrose.sequences.SequencesRepository;

@Service
public class AddressesService {

  private final AddressesRepository addressesRepository;
  private final SequencesRepository sequencesRepository;

  public AddressesService(final AddressesRepository addressesRepository, final SequencesRepository sequencesRepository) {
    this.addressesRepository = addressesRepository;
    this.sequencesRepository = sequencesRepository;
  }

  @Transactional
  public Address save(final Address address) {
    final long addressId = sequencesRepository.nextvalue(Sequences.ADDRESSES);
    addressesRepository.insert(addressId, address);
    return addressesRepository.getById(addressId);
  }
}
