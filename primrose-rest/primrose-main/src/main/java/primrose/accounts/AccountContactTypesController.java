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
@RequestMapping(path = "/account-contact-types")
public class AccountContactTypesController {
  private final AccountsService                     accountsService;
  private final AccountContactTypeResourceAssembler accountContactTypeResourceAssembler;

  public AccountContactTypesController(
    final AccountsService accountsService,
    final AccountContactTypeResourceAssembler accountContactTypeResourceAssembler) {
    this.accountsService = accountsService;
    this.accountContactTypeResourceAssembler = accountContactTypeResourceAssembler;
  }

  @GetMapping(produces = "application/vnd.primrose.collection.v.1.0+json")
  public ResponseEntity<CollectionResource> listTypes() {
    final List<AccountContactTypeResource> resource = accountContactTypeResourceAssembler
      .toResource(accountsService.loadContactTypes());
    return ResponseEntity.ok(ImmutableCollectionResource.builder()
      .size(resource.size())
      .addEntity(ImmutableEntity.builder()
        .name("account-contact-types")
        .mediaType("application/vnd.primrose.account-contact-type.list.v.1.0+json")
        .build())
      .putEmbedded(
        "account-contact-types",
        resource)
      .build());
  }

  @GetMapping(path = "/{type}", produces = "application/vnd.primrose.account-contact-type.list.v.1.0+json")
  public ResponseEntity<AccountContactTypeResource> loadType(
    @PathVariable("type") final String type) {
    return ResponseEntity.ok(accountContactTypeResourceAssembler.toResource(accountsService.loadContactType(type)));
  }
}
