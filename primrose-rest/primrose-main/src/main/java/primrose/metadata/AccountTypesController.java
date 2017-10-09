package primrose.metadata;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import primrose.hal.ImmutableLink;
import primrose.meta.ImmutableAccountTypeResource;

@Controller
@RequestMapping(path = "/metadata/accounts/types")
public class AccountTypesController {
  private final AccountsMetadataService accountsMetadataService;

  public AccountTypesController(final AccountsMetadataService accountsMetadataService) {
    this.accountsMetadataService = accountsMetadataService;
  }

  @GetMapping()
  public ResponseEntity<List<AccountTypeResource>> listTypes() {
    return ResponseEntity.ok(accountsMetadataService
      .loadTypes()
      .stream()
      .map(type -> ImmutableAccountTypeResource.builder()
        .name(type.name())
        .putLink("self", ImmutableLink.builder()
          .href(
            fromController(AccountsMetadataController.class)
              .path("/types/{type}")
              .build()
              .expand(type.name())
              .toUriString())
          .title(type.name())
          .build())
        .build())
      .collect(Collectors.toList()));
  }

  @GetMapping("/{type}")
  public ResponseEntity<AccountTypeResource> loadType(
    @PathVariable("type") final String type) {
    final AccountType loadedType = accountsMetadataService.loadType(type);
    return ResponseEntity.ok(ImmutableAccountTypeResource.builder()
      .name(loadedType.name())
      .build());
  }
}
