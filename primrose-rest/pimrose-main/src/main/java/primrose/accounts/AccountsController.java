package primrose.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {

  private final AccountsService accountsService;
  private final AccountsRepository accountsRepository;

  public AccountsController(final AccountsService accountsService, final AccountsRepository accountsRepository) {
    this.accountsService = accountsService;
    this.accountsRepository = accountsRepository;
  }

  @GetMapping("/{code}")
  public ResponseEntity<Account> getByCode(@PathVariable("code") final String code) {
    return ResponseEntity.ok(accountsRepository.getByCode(code));
  }

  @PostMapping
  public ResponseEntity<Account> save(@RequestBody final Account account) {
    return ResponseEntity.ok(accountsService.save(account));
  }
}
