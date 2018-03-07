package primrose.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import pimrose.jooq.Tables;
import primrose.types.output.Account;
import primrose.types.output.ImmutableAccount;

@Component
public class AccountQuery {
  private DSLContext create;

  public AccountQuery(DSLContext create) {
    this.create = create;
  }

  public List<List<Account>> list(List<Long> customerId) {
    Map<Long, List<Account>> accounts = create
      .select(
        Tables.ACCOUNTS.ID,
        Tables.ACCOUNTS.NAME,
        Tables.ACCOUNTS.CUSTOMER)
      .from(Tables.ACCOUNTS)
      .where(Tables.ACCOUNTS.CUSTOMER.in(customerId))
      .fetchGroups(Tables.ACCOUNTS.CUSTOMER, record -> ImmutableAccount.builder()
        .id(record.get(Tables.ACCOUNTS.ID))
        .name(record.get(Tables.ACCOUNTS.NAME))
        .build());

    return customerId
      .stream()
      .map(id -> accounts.getOrDefault(id, new ArrayList<>()))
      .collect(Collectors.toList());
  }
}
