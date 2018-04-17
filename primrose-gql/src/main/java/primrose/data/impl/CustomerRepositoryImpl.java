package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.CustomerRepository;
import primrose.jooq.Tables;
import primrose.service.Email;
import primrose.service.Phone;
import primrose.service.Search;
import primrose.service.customer.Customer;
import primrose.service.customer.CustomerCreate;
import primrose.service.customer.CustomerSearch;

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
          .where(Tables.CUSTOMER_TYPES.SLUG.eq(customer.getType()))
          .asField(),
        create
          .select(Tables.CUSTOMER_RELATION_TYPES.ID)
          .from(Tables.CUSTOMER_RELATION_TYPES)
          .where(Tables.CUSTOMER_RELATION_TYPES.SLUG.eq(customer.getRelationType()))
          .asField(),
        DSL.value(customer.getFullName()),
        DSL.value(customer.getDisplayName()),
        DSL.value(customer.getDescription()))
      .returning(Tables.CUSTOMERS.ID)
      .fetchOne()
      .getId();
  }

  @Override
  public List<CustomerSearch> search(Search search) {
    int limit = search.getSize();
    int offset = search.getPage() * search.getSize();
    return create
      .select(
        Tables.CUSTOMERS.ID,
        Tables.CUSTOMER_TYPES.SLUG,
        Tables.CUSTOMER_RELATION_TYPES.SLUG,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.FULL_NAME,
        create
          .select(Tables.EMAILS.EMAIL)
          .from(Tables.CUSTOMER_EMAILS)
          .leftJoin(Tables.EMAILS).on(Tables.EMAILS.ID.eq(Tables.CUSTOMER_EMAILS.EMAIL))
          .where(Tables.CUSTOMER_EMAILS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
          .orderBy(Tables.CUSTOMER_EMAILS.PRIM.desc(), Tables.CUSTOMER_EMAILS.VALID_FROM.asc())
          .limit(1)
          .<String>asField(),
        create
          .select(Tables.PHONE_NUMBERS.PHONE)
          .from(Tables.CUSTOMER_PHONE_NUMBERS)
          .leftJoin(Tables.PHONE_NUMBERS).on(Tables.PHONE_NUMBERS.ID.eq(Tables.CUSTOMER_PHONE_NUMBERS.PHONE))
          .where(Tables.CUSTOMER_PHONE_NUMBERS.CUSTOMER.eq(Tables.CUSTOMERS.ID))
          .orderBy(Tables.CUSTOMER_PHONE_NUMBERS.PRIM.desc(), Tables.CUSTOMER_PHONE_NUMBERS.VALID_FROM.asc())
          .limit(1)
          .<String>asField())
      .from(Tables.CUSTOMERS)
      .leftJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
      .leftJoin(Tables.CUSTOMER_RELATION_TYPES).on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .limit(limit)
      .offset(offset)
      .fetch()
      .map(record -> new CustomerSearch(
        record.value1(),
        record.value2(),
        record.value3(),
        record.value4(),
        record.value5(),
        record.value6(),
        record.value7()));
  }

  @Override
  public long count(Search search) {
    return create
      .selectCount()
      .from(Tables.CUSTOMERS)
      .fetchOne()
      .value1();
  }

  @Override
  public Customer get(long customerId, List<Email> emails, List<Phone> phones) {
    return create
      .select(
        Tables.CUSTOMER_TYPES.SLUG,
        Tables.CUSTOMER_RELATION_TYPES.SLUG,
        Tables.CUSTOMERS.DISPLAY_NAME,
        Tables.CUSTOMERS.FULL_NAME,
        Tables.CUSTOMERS.DESCRIPTION)
      .from(Tables.CUSTOMERS)
      .leftJoin(Tables.CUSTOMER_TYPES).on(Tables.CUSTOMER_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_TYPE))
      .leftJoin(Tables.CUSTOMER_RELATION_TYPES).on(Tables.CUSTOMER_RELATION_TYPES.ID.eq(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE))
      .where(Tables.CUSTOMERS.ID.eq(customerId))
      .fetchOne(record -> new Customer(
        record.value1(),
        record.value2(),
        record.value3(),
        record.value4(),
        record.value5(),
        emails,
        phones));
  }

}
