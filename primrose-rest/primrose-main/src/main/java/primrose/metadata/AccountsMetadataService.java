package primrose.metadata;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.NoEntityFoundException;

@Service
public class AccountsMetadataService {

  private final AccountsMetadataRepository accountsMetadataRepository;

  public AccountsMetadataService(final AccountsMetadataRepository accountsMetadataRepository) {
    this.accountsMetadataRepository = accountsMetadataRepository;
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public List<AccountType> loadTypes() {
    return accountsMetadataRepository.loadTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public AccountType loadType(final String type) {
    return accountsMetadataRepository
      .loadType(type)
      .orElseThrow(() -> new NoEntityFoundException(String
        .format(
          "Could not find account type %s", type)));
  }

  @Transactional(readOnly = true)
  @Cacheable("accountAdressTypes")
  public List<String> loadAddressTypes() {
    return accountsMetadataRepository.loadAddressTypes();
  }

  @Transactional(readOnly = true)
  @Cacheable("accountContactTypes")
  public List<String> loadContactTypes() {
    return accountsMetadataRepository.loadContactTypes();
  }
}
