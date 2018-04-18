package primrose.data.impl;

import java.util.List;

import org.jooq.DSLContext;
import org.jooq.Field;
import org.jooq.Name;
import org.jooq.Record2;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import primrose.data.MetaRepository;
import primrose.jooq.Tables;
import primrose.service.MetaType;

@Repository
public class MetaRepositoryImpl implements MetaRepository {

  private DSLContext create;

  public MetaRepositoryImpl(DSLContext create) {
    this.create = create;
  }

  @Override
  public List<MetaType> list(MetaTypes type) {
    Name tableName = mapTable(type);
    Field<String> slug = DSL.field("slug", String.class);
    Field<String> name = DSL.field("name", String.class);
    Field<String> sort = DSL.field("sort", String.class);
    return create
      .select(slug, name)
      .from(tableName)
      .orderBy(sort.asc())
      .fetch()
      .map(this::map);
  }

  @Override
  public boolean contains(MetaTypes type, String value) {
    Name tableName = mapTable(type);
    Field<String> slug = DSL.field("slug", String.class);

    return create
      .selectOne()
      .from(tableName)
      .where(slug.eq(value))
      .fetchOne(0) != null;
  }

  public enum MetaTypes {
    CUSTOMER, CUSTOMER_RELATION, ADDRESS, PHONE_NUMBER, EMAIL, CONTACT, MEETING
  }

  private Name mapTable(MetaTypes types) {
    switch (types) {
    case ADDRESS:
      return Tables.ADDRESS_TYPES.getQualifiedName();
    case CONTACT:
      return Tables.CONTACT_TYPES.getQualifiedName();
    case CUSTOMER:
      return Tables.CUSTOMER_TYPES.getQualifiedName();
    case CUSTOMER_RELATION:
      return Tables.CUSTOMER_RELATION_TYPES.getQualifiedName();
    case EMAIL:
      return Tables.EMAIL_TYPES.getQualifiedName();
    case MEETING:
      return Tables.MEETING_TYPES.getQualifiedName();
    case PHONE_NUMBER:
      return Tables.PHONE_NUMBER_TYPES.getQualifiedName();
    default:
      return null;
    }
  }

  private MetaType map(Record2<String, String> record) {
    return new MetaType(record.value1(), record.value2());
  }
}
