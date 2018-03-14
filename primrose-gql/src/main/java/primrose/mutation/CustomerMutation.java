package primrose.mutation;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Component;

import pimrose.jooq.Tables;
import primrose.types.input.Customer;

@Component
public class CustomerMutation {

  private DSLContext create;

  public CustomerMutation(DSLContext create) {
    this.create = create;
  }

  public void deleteCustomers(List<Long> ids) {
    create.deleteFrom(Tables.CUSTOMERS).where(Tables.CUSTOMERS.ID.in(ids)).execute();
  }

  public long createCustomer(Customer customer) {
    return create
      .insertInto(Tables.CUSTOMERS)
      .columns(
        Tables.CUSTOMERS.CUSTOMER_TYPE,
        Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE,
        Tables.CUSTOMERS.FULL_NAME,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.EMAIL,
        Tables.CUSTOMERS.PHONE,
        Tables.CUSTOMERS.DESCRIPTION)
      .values(
        create
          .select(Tables.CUSTOMER_TYPES.ID)
          .from(Tables.CUSTOMER_TYPES)
          .where(Tables.CUSTOMER_TYPES.NAME.eq(customer.type()))
          .asField(),
        create
          .select(Tables.CUSTOMER_RELATION_TYPES.ID)
          .from(Tables.CUSTOMER_RELATION_TYPES)
          .where(Tables.CUSTOMER_RELATION_TYPES.NAME.eq(customer.relationType()))
          .asField(),
        DSL.value(customer.fullName()),
        DSL.value(customer.displayName().orElse(null)),
        DSL.value(customer.email()),
        DSL.value(customer.phone().orElse(null)),
        DSL.value(customer.description().orElse(null)))
      .returning(Tables.CUSTOMERS.ID)
      .fetchOne()
      .getId();
  }
}
