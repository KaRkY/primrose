package primrose.accounts.contacts;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import primrose.accounts.AccountsService;
import primrose.contacts.Contact;
import primrose.contacts.ContactsService;
import primrose.contacts.ImmutableContact;

@RestController
@RequestMapping(path = "/accounts/{account}/contacts")
public class AccountsContactsController {
  private final AccountsService accountsService;
  private final ContactsService contactsService;

  public AccountsContactsController(final AccountsService accountsService, final ContactsService contactsService) {
    this.accountsService = accountsService;
    this.contactsService = contactsService;
  }

  @GetMapping(
    produces = "application/primrose.account.contact.list.response.v.1.0+json")
  public ResponseEntity<Map<String, List<AccountContactListResponse>>> listByAccountCode(
    @PathVariable("account") final String account) {
    return ResponseEntity.ok(contactsService
      .loadByAccountId(account)
      .entrySet()
      .stream()
      .collect(Collectors
        .toMap(
          entry -> entry.getKey(),
          entry -> entry.getValue()
            .stream()
            .map(contact -> ImmutableAccountContactListResponse.builder()
              .id(contact.id())
              .name(contact.name())
              .phone(contact.phone())
              .email(contact.email())
              .build())
            .collect(Collectors.toList()))));
  }

  @GetMapping(
    path = "/{type}/{contact}",
    produces = "application/primrose.account.contact.load.response.v.1.0+json")
  public ResponseEntity<AccountContactLoadResponse> load(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @PathVariable("contact") final String contact) {
    final Contact loadedContact = contactsService.loadById(account, type, contact);
    return ResponseEntity
      .ok(ImmutableAccountContactLoadResponse.builder()
        .id(loadedContact.id())
        .name(loadedContact.name())
        .phone(loadedContact.phone())
        .email(loadedContact.email())
        .build());
  }

  @PostMapping(
    path = "/{type}",
    consumes = "application/primrose.account.contact.save.request.v.1.0+json",
    produces = "application/primrose.account.contact.save.response.v.1.0+json")
  public ResponseEntity<AccountContactSaveResponse> save(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @RequestBody final AccountContactSaveRequest contact) {
    final Contact savedContact = accountsService.addContact(
      account,
      ImmutableContact.builder()
        .id(contactsService.getNextId())
        .name(contact.name())
        .phone(contact.phone())
        .email(contact.email())
        .build(),
      type);
    return ResponseEntity.ok(ImmutableAccountContactSaveResponse.builder()
      .id(savedContact.id())
      .name(savedContact.name())
      .phone(savedContact.phone())
      .email(savedContact.email())
      .build());
  }

  @PostMapping(
    path = "/{type}",
    consumes = "application/primrose.account.contactId.save.request.v.1.0+json",
    produces = "application/primrose.account.contact.save.response.v.1.0+json")
  public ResponseEntity<AccountContactSaveResponse> save(
    @PathVariable("account") final String account,
    @PathVariable("type") final String type,
    @RequestBody final AccountContactIdSaveRequest contact) {
    final Contact savedContact = accountsService.addContact(account, contact.id(), type);
    return ResponseEntity.ok(ImmutableAccountContactSaveResponse.builder()
      .id(savedContact.id())
      .name(savedContact.name())
      .phone(savedContact.phone())
      .email(savedContact.email())
      .build());
  }
}
