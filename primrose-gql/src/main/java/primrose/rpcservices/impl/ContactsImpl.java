package primrose.rpcservices.impl;

import java.util.Set;

import org.springframework.stereotype.Component;

import com.googlecode.jsonrpc4j.spring.AutoJsonRpcServiceImpl;

import primrose.rpcservices.ContactCreate;
import primrose.rpcservices.Contacts;
import primrose.rpcservices.ContactsSearchResult;
import primrose.rpcservices.CustomerCreate;
import primrose.rpcservices.Customers;
import primrose.rpcservices.CustomersSearchResult;
import primrose.rpcservices.Search;
import primrose.services.ImmutableContactsSearchResult;
import primrose.services.ImmutableCustomersSearchResult;

@AutoJsonRpcServiceImpl
@Component
public class ContactsImpl implements Contacts {

  @Override
  public ContactsSearchResult search(Search search) {
    System.out.println(search);
    return ImmutableContactsSearchResult.builder()
        .count(0)
        .build();
  }

  @Override
  public long create(ContactCreate contact) {
    System.out.println(contact);
    return 0;
  }

  @Override
  public long delete(long id) {
    System.out.println(id);
    return 0;
  }

  @Override
  public Set<Long> delete(Set<Long> ids) {
    System.out.println(ids);
    return null;
  }

}
