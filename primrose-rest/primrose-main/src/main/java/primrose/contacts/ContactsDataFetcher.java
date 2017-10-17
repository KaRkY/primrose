package primrose.contacts;

import java.util.List;
import java.util.stream.Collectors;

import graphql.execution.batched.Batched;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import primrose.graphql.GQLDataFetcher;
import primrose.model.BaseOutputAccount;
import primrose.model.BaseOutputAccountContact;

@GQLDataFetcher(type = "Account", property = "contacts")
public class ContactsDataFetcher implements DataFetcher<List<List<BaseOutputAccountContact>>> {

  private final ContactsService contactsService;

  public ContactsDataFetcher(final ContactsService contactsService) {
    this.contactsService = contactsService;
  }

  @Override
  @Batched
  public List<List<BaseOutputAccountContact>> get(final DataFetchingEnvironment environment) {
    final List<BaseOutputAccount> sources = environment.getSource();
    return contactsService.loadByAccountId(sources.stream().map(BaseOutputAccount::id).collect(Collectors.toList()));
  }

}
