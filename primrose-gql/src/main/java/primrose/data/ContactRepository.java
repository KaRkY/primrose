package primrose.data;

import java.util.List;

import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
import primrose.service.contact.ContactCreate;
import primrose.service.contact.ContactFullDisplay;
import primrose.service.contact.ContactReducedDisplay;

public interface ContactRepository {

   CodeId generateCode();

   CodeId codeId(String code);

   void create(CodeId code, ContactCreate contact);

   List<ContactReducedDisplay> search(Search search);

   long count(Search search);

   ContactFullDisplay get(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

   ContactFullDisplay getForUpdate(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

   void deactivate(CodeId code);
}
