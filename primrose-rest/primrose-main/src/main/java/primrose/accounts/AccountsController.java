package primrose.accounts;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.util.Encrypt;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {

  private final AccountsService accountsService;

  public AccountsController(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @GetMapping("/{code}")
  public ResponseEntity<Account> loadByCode(@PathVariable("code") final String code) {
    return ResponseEntity.ok(accountsService.loadById(Encrypt.pseudo(Long.valueOf(code, 36))));
  }

  @PostMapping
  public ResponseEntity<Account> save(@RequestBody final Account account) {
    return ResponseEntity.ok(accountsService.save(account));
  }
}
