package primrose.service.customer;

import java.util.Set;

import primrose.service.ListResult;
import primrose.service.Pagination;
import primrose.service.contact.ContactCode;

public interface CustomerService {

  CustomerCode create(Customer customer);

  CustomerCode update(Customer customer);

  ListResult<CustomerPreview> list(Pagination pagination);

  Customer get(CustomerCode customerCode);

  void add(ContactCode contactCode);

  void add(Set<ContactCode> contactCodes);
}
