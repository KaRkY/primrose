package primrose.accounts;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import primrose.hal.ImmutableEntity;
import primrose.hal.ImmutableLink;
import primrose.hal.ImmutablePageableResource;
import primrose.hal.PageableResource;
import primrose.pagging.sort.SortUtil;

@RestController
@RequestMapping(path = "/accounts")
public class AccountsController {

  private final AccountsService accountsService;
  private final AccountResourceAssembler accountResourceAssembler;

  public AccountsController(
    final AccountsService accountsService,
    final AccountResourceAssembler accountResourceAssembler) {
    this.accountsService = accountsService;
    this.accountResourceAssembler = accountResourceAssembler;
  }

  @GetMapping(
    path = "/{account}",
    produces = "application/vnd.primrose.account.v.1.0+json")
  public ResponseEntity<AccountResource> loadById(@PathVariable("account") final String account) {
    return ResponseEntity.ok(accountResourceAssembler.toResource(accountsService.loadById(account)));
  }

  @PostMapping(
    consumes = "application/vnd.primrose.account.v.1.0+json",
    produces = "application/vnd.primrose.account.v.1.0+json")
  public ResponseEntity<AccountResource> create(@RequestBody final AccountResource account) {
    final Account savedAccount = accountsService
      .create(ImmutableAccount.copyOf(accountResourceAssembler
        .fromResource(account))
        .withId(accountsService.getNextId()));
    return ResponseEntity
      .created(fromController(getClass()).buildAndExpand(savedAccount.id()).toUri())
      .body(accountResourceAssembler.toResource(savedAccount));
  }

  @PutMapping(
    path = "/{account}",
    consumes = "application/vnd.primrose.account.v.1.0+json",
    produces = "application/vnd.primrose.account.v.1.0+json")
  public ResponseEntity<AccountResource> edit(
    @PathVariable("account") final String accountId,
    @RequestBody final AccountResource account) {
    return ResponseEntity
      .ok(accountResourceAssembler
        .toResource(accountsService
          .update(accountId, accountResourceAssembler.fromResource(account))));
  }

  @GetMapping(
    produces = "application/vnd.primrose.pageable.v.1.0+json")
  public ResponseEntity<PageableResource> list(
    @RequestParam(required = false) final Integer page,
    @RequestParam(required = false) final Integer size,
    @RequestParam(required = false) final String sort) {
    final List<AccountResource> resource = accountResourceAssembler.toResource(accountsService.load(page, size, SortUtil.parseSort(sort)));
    final ImmutablePageableResource.Builder builder = ImmutablePageableResource.builder();
    final int count = accountsService.count();

    final UriComponentsBuilder self = fromController(AccountsController.class)
      .replaceQueryParam("page", page != null && size != null ? new Object[] { page } : null)
      .replaceQueryParam("size", page != null && size != null ? new Object[] { size } : null)
      .replaceQueryParam("sort", sort != null ? new Object[] { sort } : null);

    if (page != null && size != null && size * page < count) {
      builder.putLink("next", ImmutableLink.builder()
        .href(self.cloneBuilder().replaceQueryParam("page", page + 1).toUriString())
        .title("Next")
        .build());
    }

    if (page != null && size != null && page > 1) {
      builder.putLink("previous", ImmutableLink.builder()
        .href(self.cloneBuilder().replaceQueryParam("page", page - 1).toUriString())
        .title("Previous")
        .build());
    }

    return ResponseEntity
      .ok(builder
        .count(count)
        .size(resource.size())
        .addEntity(ImmutableEntity.builder()
          .name("accounts")
          .mediaType("application/vnd.primrose.account.v.1.0+json")
          .build())
        .putEmbedded("accounts", resource)
        .putLink("self", ImmutableLink.builder().href(self.toUriString()).build())
        .build());
  }
}
