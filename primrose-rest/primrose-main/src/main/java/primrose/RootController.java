package primrose;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.fromController;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import primrose.accounts.AccountAddressTypesController;
import primrose.accounts.AccountContactTypesController;
import primrose.accounts.AccountTypesController;
import primrose.accounts.AccountsController;
import primrose.addresses.AddressesControler;
import primrose.contacts.ContactsController;
import primrose.hal.ImmutableLink;

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
      .putLink("account-types", ImmutableLink.builder()
        .href(fromController(AccountTypesController.class).toUriString())
        .title("Account types")
        .build())
      .putLink("account-address-types", ImmutableLink.builder()
        .href(fromController(AccountAddressTypesController.class).toUriString())
        .title("Account address types")
        .build())
      .putLink("account-contact-types", ImmutableLink.builder()
        .href(fromController(AccountContactTypesController.class).toUriString())
        .title("Account contact types")
        .build())
      .putLink("addresses", ImmutableLink.builder()
        .href(fromController(AddressesControler.class).toUriString())
        .title("Addresses")
        .build())
      .putLink("contacts", ImmutableLink.builder()
        .href(fromController(ContactsController.class).toUriString())
        .title("Contacts")
        .build())
      .build());
  }
}
