package primrose;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import primrose.accounts.AccountsController;
import primrose.addresses.AddressesControler;
import primrose.contacts.ContactsController;
import primrose.hal.ImmutableLink;
import primrose.metadata.MetadataController;

@Controller
@RequestMapping(path = "/")
public class RootController {

  @GetMapping(
    produces = "application/vnd.primrose.v.1.0+json")
  public ResponseEntity<RootResource> load() {
    return ResponseEntity.ok(ImmutableRootResource.builder()
      .putLink("accounts", ImmutableLink.builder()
        .href(fromController(AccountsController.class).toUriString())
        .title("Accounts")
        .build())
      .putLink("addresses", ImmutableLink.builder()
        .href(fromController(AddressesControler.class).toUriString())
        .title("Addresses")
        .build())
      .putLink("contacts", ImmutableLink.builder()
        .href(fromController(ContactsController.class).toUriString())
        .title("Contacts")
        .build())
      .putLink("metadata", ImmutableLink.builder()
        .href(fromController(MetadataController.class).toUriString())
        .title("Metadata")
        .build())
      .build());
  }
}
