package primrose.meta;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountsMetadataService {

  private final AccountsMetadataRepository accountsMetadataRepository;

  public AccountsMetadataService(final AccountsMetadataRepository accountsMetadataRepository) {
    this.accountsMetadataRepository = accountsMetadataRepository;
  }

  @Transactional(readOnly = true)
  @Cacheable("accountTypes")
  public List<String> loadTypes() {
    return accountsMetadataRepository.loadTypes();
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
