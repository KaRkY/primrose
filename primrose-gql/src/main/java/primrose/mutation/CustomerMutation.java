package primrose.mutation;

import java.util.List;

import org.jooq.DSLContext;
import org.springframework.stereotype.Component;

import pimrose.jooq.Tables;

@Component
public class CustomerMutation {

  private DSLContext create;

  public CustomerMutation(DSLContext create) {
    this.create = create;
  }

  public void deleteCustomers(List<Long> ids) {
    create.deleteFrom(Tables.CUSTOMERS).where(Tables.CUSTOMERS.ID.in(ids)).execute();
  }
}
