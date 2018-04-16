package primrose.data.impl;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import pimrose.jooq.Tables;
import primrose.data.CustomerRepository;
import primrose.rpcservices.CustomerCreate;

@Repository
public class CustomerRepositoryImpl implements CustomerRepository {

  private DSLContext create;

  public CustomerRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public long create(CustomerCreate customer) {
    return create
        .insertInto(Tables.CUSTOMERS)
        .columns(
            Tables.CUSTOMERS.CUSTOMER_TYPE,
            Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE,
            Tables.CUSTOMERS.FULL_NAME,
            Tables.CUSTOMERS.DISPLAY_NAME,
            Tables.CUSTOMERS.DESCRIPTION)
        .values(
            create
                .select(Tables.CUSTOMER_TYPES.ID)
                .from(Tables.CUSTOMER_TYPES)
                .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer.type()))
                .asField(),
            create
                .select(Tables.CUSTOMER_RELATION_TYPES.ID)
                .from(Tables.CUSTOMER_RELATION_TYPES)
                .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customer.relationType()))
                .asField(),
            DSL.value(customer.fullName()),
            DSL.value(customer.displayName().orElse(null)),
            DSL.value(customer.description().orElse(null)))
        .returning(Tables.CUSTOMERS.ID)
        .fetchOne()
        .getId();
  }

}
