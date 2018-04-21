package primrose.data;

import com.google.common.collect.ImmutableList;

import primrose.service.CodeId;
import primrose.service.Pagination;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerFullDisplay;
import primrose.service.customer.CustomerReducedDisplay;

public interface CustomerRepository {

  CodeId generateCode();

  CodeId codeId(String code);

  void create(CodeId code, CustomerCreate customer);

  ImmutableList<CustomerReducedDisplay> list(Pagination search);

  long count(Pagination search);

  CustomerFullDisplay get(CodeId code);

  CustomerFullDisplay getForUpdate(CodeId code);

  void deactivate(CodeId code);
}
