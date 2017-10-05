package primrose.accounts;

import java.util.List;
import java.util.stream.Collectors;

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

  public AccountsController(final AccountsService accountsService) {
    this.accountsService = accountsService;
  }

  @GetMapping(
    path = "/{account}",
    produces = "application/vnd.primrose.account.load.response.v.1.0+json")
  public ResponseEntity<AccountLoadResponse> loadByCode(@PathVariable("account") final String account) {
    final Account loadedAccount = accountsService
      .loadById(account);
    return ResponseEntity
      .ok(ImmutableAccountLoadResponse.builder()
        .id(loadedAccount.id())
        .name(loadedAccount.name())
        .displayName(loadedAccount.displayName())
        .description(loadedAccount.description())
        .email(loadedAccount.email())
        .phone(loadedAccount.phone())
        .type(loadedAccount.type())
        .validFrom(loadedAccount.validFrom())
        .validTo(loadedAccount.validTo())
        .build());
  }

  @PostMapping(
    consumes = "application/vnd.primrose.account.save.request.v.1.0+json",
    produces = "application/vnd.primrose.account.save.response.v.1.0+json")
  public ResponseEntity<AccountSaveResponse> save(@RequestBody final AccountSaveRequest account) {
    final Account savedAccount = accountsService
      .save(ImmutableAccount.builder()
        .id(accountsService.getNextId())
        .name(account.name())
        .displayName(account.displayName())
        .description(account.description())
        .email(account.email())
        .phone(account.phone())
        .type(account.type())
        .build());
    return ResponseEntity
      .ok(ImmutableAccountSaveResponse.builder()
        .id(savedAccount.id())
        .name(savedAccount.name())
        .displayName(savedAccount.displayName())
        .description(savedAccount.description())
        .email(savedAccount.email())
        .phone(savedAccount.phone())
        .type(savedAccount.type())
        .build());
  }

  @GetMapping(
    produces = "application/vnd.primrose.account.search.response.v.1.0+json")
  public ResponseEntity<List<AccountSearchResponse>> loadBySearch(final AccountSearchRequest accountSearch) {
    return ResponseEntity
      .ok(accountsService
        .loadBySearch(
          accountSearch.account(),
          accountSearch.address(),
          accountSearch.contact(),
          accountSearch.page(),
          accountSearch.size(),
          accountSearch.sort())
        .stream()
        .map(account -> ImmutableAccountSearchResponse.builder()
          .id(account.id())
          .displayName(account.displayName())
          .type(account.type())
          .validFrom(account.validFrom())
          .validTo(account.validTo())
          .build())
        .collect(Collectors.toList()));
  }
}
