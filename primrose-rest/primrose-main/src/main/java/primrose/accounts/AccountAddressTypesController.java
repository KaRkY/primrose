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
@RequestMapping(path = "/account-address-types")
public class AccountAddressTypesController {
  private final AccountsService                     accountsService;
  private final AccountAddressTypeResourceAssembler accountAddressTypeResourceAssembler;

  public AccountAddressTypesController(
    final AccountsService accountsService,
    final AccountAddressTypeResourceAssembler accountAddressTypeResourceAssembler) {
    this.accountsService = accountsService;
    this.accountAddressTypeResourceAssembler = accountAddressTypeResourceAssembler;
  }

  @GetMapping(produces = "application/vnd.primrose.collection.v.1.0+json")
  public ResponseEntity<CollectionResource> listTypes() {
    final List<AccountAddressTypeResource> resource = accountAddressTypeResourceAssembler
      .toResource(accountsService.loadAddressTypes());
    return ResponseEntity.ok(ImmutableCollectionResource.builder()
      .size(resource.size())
      .addEntity(ImmutableEntity.builder()
        .name("account-address-types")
        .mediaType("application/vnd.primrose.account-address-type.list.v.1.0+json")
        .build())
      .putEmbedded(
        "account-address-types",
        resource)
      .build());
  }

  @GetMapping(path = "/{type}", produces = "application/vnd.primrose.account-address-type.list.v.1.0+json")
  public ResponseEntity<AccountAddressTypeResource> loadType(
    @PathVariable("type") final String type) {
    return ResponseEntity.ok(accountAddressTypeResourceAssembler.toResource(accountsService.loadAddressType(type)));
  }
}
