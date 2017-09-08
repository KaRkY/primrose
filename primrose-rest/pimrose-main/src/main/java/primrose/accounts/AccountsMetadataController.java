package primrose.accounts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/accounts")
public class AccountsMetadataController {

  private final AccountsRepository accountsRepository;

  public AccountsMetadataController(final AccountsRepository accountsRepository) {
    this.accountsRepository = accountsRepository;
  }

  @GetMapping("/types")
  public ResponseEntity<List<AccountType>> getTypes() {
    return ResponseEntity.ok(accountsRepository.getTypes());
  }
}
