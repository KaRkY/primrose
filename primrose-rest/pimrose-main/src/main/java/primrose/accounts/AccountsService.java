package primrose.accounts;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import primrose.sequences.Sequences;
import primrose.sequences.SequencesRepository;

@Service
public class AccountsService {

  private final AccountsRepository accountsRepository;
  private final SequencesRepository sequencesRepository;

  public AccountsService(final AccountsRepository accountsRepository, final SequencesRepository sequencesRepository) {
    this.accountsRepository = accountsRepository;
    this.sequencesRepository = sequencesRepository;
  }

  @Transactional
  public Account save(final Account account) {
    final long accountId = sequencesRepository.nextvalue(Sequences.ACCOUNTS);
    accountsRepository.insert(accountId, account);
    return accountsRepository.getById(accountId);
  }

}
