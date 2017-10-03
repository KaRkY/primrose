package primrose.accounts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.util.IdUtil;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {
  private final AccountsService accountsService;

  public AccountsController(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @GetMapping(
    path = "/{code}",
    produces = { "application/primrose.account.v.1.0+json" })
  public ResponseEntity<Account> loadByCode(@PathVariable("code") final String code) {
    return ResponseEntity.ok(accountsService.loadById(IdUtil.pseudo(Long.valueOf(code, 36))));
  }

  @PostMapping(
    consumes = { "application/primrose.account.v.1.0+json" },
    produces = { "application/primrose.account.v.1.0+json" })
  public ResponseEntity<Account> save(@RequestBody final Account account) {
    return ResponseEntity.ok(accountsService.save(account));
  }

  @GetMapping(
    produces = { "application/primrose.account.v.1.0+json" })
  public ResponseEntity<List<Account>> loadBySearch(final AccountsSearch accountSearch) {
    return ResponseEntity.ok(accountsService.loadBySearch(accountSearch));
  }
}
