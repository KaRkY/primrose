package primrose.query;

import java.util.List;
import java.util.Optional;

import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.ResultQuery;
import org.jooq.SelectSeekStepN;
import org.springframework.stereotype.Component;

import pimrose.jooq.Tables;
import pimrose.jooq.tables.records.CustomerRelationTypesRecord;
import pimrose.jooq.tables.records.CustomerTypesRecord;
import pimrose.jooq.tables.records.CustomersRecord;
import primrose.jooq.JooqUtil;
import primrose.pagination.Pageable;
import primrose.types.output.Customer;
import primrose.types.output.ImmutableCustomer;

@Component
public class CustomerQuery {

  private DSLContext create;

  public CustomerQuery(DSLContext create) {
    this.create = create;
  }

  public List<Customer> list(Pageable pageable) {
    SelectSeekStepN<Record> select = create
      .select()
      .from(Tables.CUSTOMERS)
      .join(Tables.CUSTOMER_TYPES).onKey(Tables.CUSTOMERS.CUSTOMER_TYPE)
      .join(Tables.CUSTOMER_RELATION_TYPES).onKey(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE)
      .orderBy(JooqUtil.map(
        pageable.sortProperties(),
        property -> {
          switch (property) {
          case "type":
            return Tables.CUSTOMER_TYPES.NAME;

          case "fullName":
            return Tables.CUSTOMERS.FULL_NAME;

          case "displayName":
            return Tables.CUSTOMERS.DISPLAY_NAME;

          case "id":
            return Tables.CUSTOMERS.ID;

          default:
            return null;
          }
        }));

    ResultQuery<Record> resQuery = null;
    if (pageable.paged()) {
      resQuery = select
        .offset((int) pageable.offset())
        .limit(pageable.pageSize());
    } else {
      resQuery = select;
    }

    return resQuery
      .fetch()
      .map(record -> {
        CustomersRecord customersRecord = record.into(CustomersRecord.class);
        CustomerTypesRecord customerTypesRecord = record.into(CustomerTypesRecord.class);
        CustomerRelationTypesRecord customerRelationTypesRecord = record.into(CustomerRelationTypesRecord.class);

        return ImmutableCustomer.builder()
          .id(customersRecord.getId())
          .type(customerTypesRecord.getName())
          .relationType(customerRelationTypesRecord.getName())
          .fullName(customersRecord.getFullName())
          .displayName(Optional.ofNullable(customersRecord.getDisplayName()))
          .description(Optional.ofNullable(customersRecord.getDescription()))
          .build();
      });
  }

  public Integer count() {
    return create.selectCount().from(Tables.CUSTOMERS).fetchOne().value1();
  }

  public Customer load(long id) {
    return create
      .select()
      .from(Tables.CUSTOMERS)
      .join(Tables.CUSTOMER_TYPES).onKey(Tables.CUSTOMERS.CUSTOMER_TYPE)
      .join(Tables.CUSTOMER_RELATION_TYPES).onKey(Tables.CUSTOMERS.CUSTOMER_RELATION_TYPE)
      .where(Tables.CUSTOMERS.ID.eq(id))
      .fetchOne(record -> {
        CustomersRecord customersRecord = record.into(CustomersRecord.class);
        CustomerTypesRecord customerTypesRecord = record.into(CustomerTypesRecord.class);
        CustomerRelationTypesRecord customerRelationTypesRecord = record.into(CustomerRelationTypesRecord.class);

        return ImmutableCustomer.builder()
          .id(customersRecord.getId())
          .type(customerTypesRecord.getName())
          .relationType(customerRelationTypesRecord.getName())
          .fullName(customersRecord.getFullName())
          .displayName(Optional.ofNullable(customersRecord.getDisplayName()))
          .description(Optional.ofNullable(customersRecord.getDescription()))
          .build();
      });
  }

  public List<String> listTypes() {
    return create
      .select(Tables.CUSTOMER_TYPES.NAME)
      .from(Tables.CUSTOMER_TYPES)
      .fetch(record -> record.get(Tables.CUSTOMER_TYPES.NAME));
  }

  public List<String> listRelationTypes() {
    return create
      .select(Tables.CUSTOMER_RELATION_TYPES.NAME)
      .from(Tables.CUSTOMER_RELATION_TYPES)
      .fetch(record -> record.get(Tables.CUSTOMER_RELATION_TYPES.NAME));
  }
}
