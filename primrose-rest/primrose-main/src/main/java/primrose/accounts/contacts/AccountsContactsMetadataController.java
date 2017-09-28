package primrose.accounts.contacts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/metadata/accounts/contacts")
public class AccountsContactsMetadataController {

  private final AccountsContactsRepository accountsContactsRepository;

  public AccountsContactsMetadataController(final AccountsContactsRepository accountsContactsRepository) {
    this.accountsContactsRepository = accountsContactsRepository;
  }

  @GetMapping("/types")
  public ResponseEntity<List<ContactType>> listTypes() {
    return ResponseEntity.ok(accountsContactsRepository.listTypes());
  }
}
