package primrose.contacts;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/contacts")
public class ContactsController {

  private final ContactsService contactsService;

  public ContactsController(final ContactsService contactsService) {
    this.contactsService = contactsService;
  }

}
