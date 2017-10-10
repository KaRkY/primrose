package primrose.accounts;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import primrose.hal.CollectionResource;
import primrose.hal.ImmutableCollectionResource;
import primrose.hal.ImmutableEntity;

@Controller
@RequestMapping(path = "/account-types")
public class AccountTypesController {
  private final AccountsService accountsService;
  private final AccountTypeResourceAssembler accountTypeResourceAssembler;

  public AccountTypesController(
    final AccountsService accountsService,
    final AccountTypeResourceAssembler accountTypeResourceAssembler) {
    this.accountsService = accountsService;
    this.accountTypeResourceAssembler = accountTypeResourceAssembler;
  }

  @GetMapping(
    produces="application/vnd.primrose.collection.v.1.0+json")
  public ResponseEntity<CollectionResource> listTypes() {
    final List<AccountTypeResource> resource = accountTypeResourceAssembler.toResource(accountsService.loadTypes());
    return ResponseEntity.ok(ImmutableCollectionResource.builder()
      .size(resource.size())
      .addEntity(ImmutableEntity.builder()
        .name("account-types")
        .mediaType("application/vnd.primrose.account-type.v.1.0+json")
        .build())
      .putEmbedded(
        "account-types",
        resource)
      .build());
  }

  @GetMapping(
    path="/{type}",
    produces="application/vnd.primrose.account-type.v.1.0+json")
  public ResponseEntity<AccountTypeResource> loadType(
    @PathVariable("type") final String type) {
    return ResponseEntity.ok(accountTypeResourceAssembler.toResource(accountsService.loadType(type)));
  }
}
