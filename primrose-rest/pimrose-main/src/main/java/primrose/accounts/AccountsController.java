package primrose.accounts;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.repositories.SearchParameters;
import primrose.util.SearchRequest;
import primrose.util.SearchRequestValidator;
import primrose.util.SearchResponse;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {

  private final AccountsService    accountsService;
  private final AccountsRepository accountsRepository;

  public AccountsController(final AccountsService accountsService, final AccountsRepository accountsRepository) {
    this.accountsService = accountsService;
    this.accountsRepository = accountsRepository;
  }

  @InitBinder("searchRequest")
  private void initBinder(final WebDataBinder binder) {
    binder.setValidator(new SearchRequestValidator());
  }

  @GetMapping()
  public ResponseEntity<List<Account>> search(@Valid final SearchRequest searchRequest) {
    return SearchResponse.of(searchRequest, accountsRepository.search(SearchParameters.of(searchRequest)))
      .buildResponse();
  }

  @GetMapping("/{url_code}")
  public ResponseEntity<Account> search(@PathVariable("url_code") final String urlCode) {
    return ResponseEntity.ok(accountsRepository.getByUrl(urlCode));
  }

  @PostMapping
  public ResponseEntity<Account> save(@RequestBody final Account account) {
    return ResponseEntity.ok(accountsService.save(account));
  }
}
