package primrose.data;

import java.util.List;

import primrose.service.CodeId;
import primrose.service.EmailFullDisplay;
import primrose.service.PhoneFullDisplay;
import primrose.service.Search;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

//TODO: Change code to codeId
public interface CustomerRepository {

  CodeId generateCode();

  CodeId codeId(String code);

  void create(CodeId code, CustomerCreate customer);

  List<CustomerReducedDisplay> search(Search search);

  long count(Search search);

  CustomerFullDisplay get(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

  CustomerFullDisplay getForUpdate(CodeId code, List<EmailFullDisplay> emails, List<PhoneFullDisplay> phones);

  void deactivate(CodeId code);
}
