package primrose.datafetchers;

import java.util.List;
import java.util.stream.Collectors;

import graphql.execution.batched.Batched;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.model.output.BaseOutputAccount;
import primrose.model.output.BaseOutputAccountContact;
import primrose.services.ContactsService;

public class AccountsContactsDataFetcher implements DataFetcher<List<List<BaseOutputAccountContact>>> {

  private final ContactsService contactsService;

  public AccountsContactsDataFetcher(final ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  @Override
  @Batched
  public List<List<BaseOutputAccountContact>> get(final DataFetchingEnvironment environment) {
    final List<BaseOutputAccount> sources = environment.getSource();
    return contactsService.loadByAccountId(sources.stream().map(BaseOutputAccount::id).collect(Collectors.toList()));
  }

}
