package primrose.accounts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

  @GetMapping
  @Secured({"TEST"})
  public ResponseEntity<List<Account>> getAllPaginated(
    @RequestParam(value="page_size", required=false) final Long pageSize,
    @RequestParam(value="page_number", required=false) final Long pageNumber) {
    final List<Account> paginated = accountsRepository.getAllPaginated(pageNumber == null ? 1 : pageNumber, pageSize == null ? Long.MAX_VALUE : pageSize);
    return ResponseEntity.ok()
      .header("page-size", String.valueOf(pageSize == null ? Long.MAX_VALUE : pageSize))
      .header("result-size", String.valueOf(paginated.size()))
      .header("result-number", String.valueOf(pageNumber == null ? 1 : pageNumber))
      .header("result-count", String.valueOf(accountsRepository.countAll()))
      .body(paginated);
  }

  @PostMapping
  public ResponseEntity<Account> save(@RequestBody final Account account) {
    return ResponseEntity.ok(accountsService.save(account));
  }
}
